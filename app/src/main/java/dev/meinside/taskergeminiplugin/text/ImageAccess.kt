package dev.meinside.taskergeminiplugin.text

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import dev.meinside.taskergeminiplugin.R
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.net.URI

/**
 * Reads the image Tasker passes in, as a file path, a `file://` URI or a `content://` URI.
 *
 * The plugin runs in its own process, so Tasker's storage permission does not cover it:
 * images owned by other apps can only be read after the user grants [permissionsFor]
 * on the configuration screen (the runner itself has no UI to ask from).
 */
object ImageAccess {
    sealed interface Read {
        class Image(val bytes: ByteArray, val mimeType: String) : Read
        class Failed(@StringRes val messageRes: Int) : Read
    }

    /**
     * Runtime permissions to request on [sdkInt]; the first one grants full access.
     *
     * Every one of them must also be declared in AndroidManifest.xml, or the request is
     * denied without asking.
     */
    fun permissionsFor(sdkInt: Int): List<String> = when {
        // lets the user pick "Allow limited access" instead of being forced to all-or-nothing
        sdkInt >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> listOf(
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED,
        )
        sdkInt >= Build.VERSION_CODES.TIRAMISU -> listOf(Manifest.permission.READ_MEDIA_IMAGES)
        else -> listOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    /** Whether every image can be read, not just the ones picked under limited access. */
    fun hasFullAccess(context: Context): Boolean = ContextCompat.checkSelfPermission(
        context, permissionsFor(Build.VERSION.SDK_INT).first()
    ) == PackageManager.PERMISSION_GRANTED

    /** Whether only the photos the user picked can be read (Android 14+). */
    fun hasLimitedAccess(context: Context): Boolean =
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE &&
                !hasFullAccess(context) &&
                ContextCompat.checkSelfPermission(
                    context, Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
                ) == PackageManager.PERMISSION_GRANTED

    fun isContentUri(source: String): Boolean = source.startsWith("content://", ignoreCase = true)

    /** MediaStore URIs are covered by [permissionsFor]; other providers must grant access themselves. */
    fun isMediaStoreUri(source: String): Boolean = source.startsWith("content://media/", ignoreCase = true)

    /**
     * Whether reading [source] may need [permissionsFor]: any path, as a Tasker variable such
     * as `%photo_path` can expand to anything, but not URIs of other content providers.
     */
    fun mayNeedPermission(source: String): Boolean {
        val trimmed = source.trim()
        return trimmed.isNotEmpty() && (!isContentUri(trimmed) || isMediaStoreUri(trimmed))
    }

    /** File path of [source], which may be a plain path or a `file://` URI. */
    fun filePathOf(source: String): String =
        if (source.startsWith("file://", ignoreCase = true)) {
            URI(source).path ?: source
        } else {
            source
        }

    /** Error for an image file that does not exist, or that cannot be seen without permission. */
    @StringRes
    fun missingFileError(hasPermission: Boolean): Int =
        if (hasPermission) R.string.error_message_image_not_found
        else R.string.error_message_image_no_permission

    /** Error for a `content://` URI whose provider refused to open it. */
    @StringRes
    fun deniedUriError(source: String, hasPermission: Boolean): Int =
        if (!hasPermission && isMediaStoreUri(source)) R.string.error_message_image_no_permission
        else R.string.error_message_image_uri_denied

    fun read(context: Context, source: String): Read {
        val hasPermission = hasFullAccess(context)

        if (isContentUri(source)) {
            val uri = source.toUri()
            val resolver = context.contentResolver
            val bytes = try {
                resolver.openInputStream(uri)?.use { it.readBytes() }
            } catch (_: SecurityException) {
                return Read.Failed(deniedUriError(source, hasPermission))
            } catch (_: FileNotFoundException) {
                null
            } ?: return Read.Failed(missingFileError(hasPermission))
            val mimeType = resolver.getType(uri)?.takeIf { it.startsWith("image/") }
                ?: Runner.inferMimeType(source)
            return Read.Image(bytes, mimeType)
        }

        val path = filePathOf(source)
        val file = File(path)
        if (!file.exists()) {
            return Read.Failed(missingFileError(hasPermission))
        }
        val bytes = try {
            file.readBytes()
        } catch (e: IOException) {
            // scoped storage refuses with EACCES; without permission that is the likely cause
            if (!hasPermission) return Read.Failed(R.string.error_message_image_no_permission)
            throw e
        }
        return Read.Image(bytes, Runner.inferMimeType(path))
    }
}
