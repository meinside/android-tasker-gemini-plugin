package dev.meinside.taskergeminiplugin.text

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.meinside.taskergeminiplugin.R
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assume.assumeFalse
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

/**
 * Reads through the real file system and content resolver, which the JVM tests cannot.
 */
@RunWith(AndroidJUnit4::class)
class ImageAccessReadTest {

    private val context: Context = ApplicationProvider.getApplicationContext()

    private fun ownImage(): File = File(context.cacheDir, "image-access-test.png").apply {
        writeBytes(byteArrayOf(1, 2, 3))
    }

    @Test
    fun ownFile_isReadByPath() {
        val file = ownImage()
        val read = ImageAccess.read(context, file.path)
        assertTrue(read is ImageAccess.Read.Image)
        assertArrayEquals(byteArrayOf(1, 2, 3), (read as ImageAccess.Read.Image).bytes)
        assertEquals("image/png", read.mimeType)
    }

    @Test
    fun ownFile_isReadByFileUri() {
        val file = ownImage()
        val read = ImageAccess.read(context, file.toURI().toString())
        assertTrue(read is ImageAccess.Read.Image)
        assertArrayEquals(byteArrayOf(1, 2, 3), (read as ImageAccess.Read.Image).bytes)
    }

    @Test
    fun missingFile_isNotFound() {
        val read = ImageAccess.read(context, File(context.cacheDir, "missing.png").path)
        assertEquals(
            ImageAccess.missingFileError(ImageAccess.hasFullAccess(context)),
            (read as ImageAccess.Read.Failed).messageRes
        )
    }

    @Test
    fun mediaStoreUri_withoutPermission_blamesPermission() {
        assumeFalse(ImageAccess.hasFullAccess(context))
        val read = ImageAccess.read(context, "content://media/external/images/media/1")
        assertEquals(R.string.error_message_image_no_permission, (read as ImageAccess.Read.Failed).messageRes)
    }

    @Test
    fun unknownProviderUri_isNotFound() {
        val read = ImageAccess.read(context, "content://dev.meinside.no.such.provider/image")
        assertTrue(read is ImageAccess.Read.Failed)
    }
}
