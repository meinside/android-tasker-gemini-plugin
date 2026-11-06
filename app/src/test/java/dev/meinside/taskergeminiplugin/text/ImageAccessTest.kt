package dev.meinside.taskergeminiplugin.text

import android.Manifest
import dev.meinside.taskergeminiplugin.R
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.w3c.dom.Element
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

class ImageAccessPermissionTest {

    @Test
    fun beforeAndroid13_requestsExternalStorage() {
        assertEquals(listOf(Manifest.permission.READ_EXTERNAL_STORAGE), ImageAccess.permissionsFor(24))
        assertEquals(listOf(Manifest.permission.READ_EXTERNAL_STORAGE), ImageAccess.permissionsFor(32))
    }

    @Test
    fun android13_requestsMediaImages() {
        assertEquals(listOf(Manifest.permission.READ_MEDIA_IMAGES), ImageAccess.permissionsFor(33))
    }

    @Test
    fun android14AndLater_alsoRequestsUserSelected() {
        val expected = listOf(
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED,
        )
        assertEquals(expected, ImageAccess.permissionsFor(34))
        assertEquals(expected, ImageAccess.permissionsFor(37))
    }

    /**
     * Issue #4: images could not be read because the manifest declared no storage permission,
     * and a runtime request for an undeclared permission is denied without asking.
     */
    @Test
    fun manifest_declaresEveryRequestedPermission() {
        val manifest = File("src/main/AndroidManifest.xml")
        val document = DocumentBuilderFactory.newInstance()
            .apply { isNamespaceAware = true }
            .newDocumentBuilder()
            .parse(manifest)
        val android = "http://schemas.android.com/apk/res/android"
        val nodes = document.getElementsByTagName("uses-permission")
        // permission name -> highest SDK it is declared for
        val declared = (0 until nodes.length).associate {
            val element = nodes.item(it) as Element
            element.getAttributeNS(android, "name") to
                    (element.getAttributeNS(android, "maxSdkVersion").toIntOrNull() ?: Int.MAX_VALUE)
        }

        for (sdk in 24..37) {
            for (permission in ImageAccess.permissionsFor(sdk)) {
                val maxSdk = declared[permission]
                assertNotNull("$permission (requested on SDK $sdk) is not declared", maxSdk)
                assertTrue("$permission is declared only up to SDK $maxSdk, but requested on $sdk", sdk <= maxSdk!!)
            }
        }
    }
}

class ImageAccessSourceTest {

    @Test
    fun contentUri_isDetected() {
        assertTrue(ImageAccess.isContentUri("content://media/external/images/media/42"))
        assertTrue(ImageAccess.isContentUri("CONTENT://com.example.provider/image"))
        assertFalse(ImageAccess.isContentUri("/sdcard/DCIM/photo.jpg"))
        assertFalse(ImageAccess.isContentUri("file:///sdcard/DCIM/photo.jpg"))
    }

    @Test
    fun mediaStoreUri_isDetected() {
        assertTrue(ImageAccess.isMediaStoreUri("content://media/external/images/media/42"))
        assertFalse(ImageAccess.isMediaStoreUri("content://com.example.provider/image"))
        assertFalse(ImageAccess.isMediaStoreUri("/sdcard/DCIM/photo.jpg"))
    }

    @Test
    fun mayNeedPermission_forPathsAndMediaStoreUris() {
        assertTrue(ImageAccess.mayNeedPermission("/sdcard/DCIM/photo.jpg"))
        assertTrue(ImageAccess.mayNeedPermission("%photo_path"))
        assertTrue(ImageAccess.mayNeedPermission("file:///sdcard/DCIM/photo.jpg"))
        assertTrue(ImageAccess.mayNeedPermission("content://media/external/images/media/42"))
    }

    @Test
    fun mayNeedPermission_notForBlankOrOtherProviders() {
        assertFalse(ImageAccess.mayNeedPermission(""))
        assertFalse(ImageAccess.mayNeedPermission("   "))
        assertFalse(ImageAccess.mayNeedPermission("content://com.example.provider/image"))
    }

    @Test
    fun filePathOf_plainPath_isUnchanged() {
        assertEquals("/sdcard/DCIM/photo.jpg", ImageAccess.filePathOf("/sdcard/DCIM/photo.jpg"))
    }

    @Test
    fun filePathOf_fileUri_isDecoded() {
        assertEquals("/sdcard/DCIM/photo.jpg", ImageAccess.filePathOf("file:///sdcard/DCIM/photo.jpg"))
        assertEquals("/sdcard/DCIM/my photo.jpg", ImageAccess.filePathOf("file:///sdcard/DCIM/my%20photo.jpg"))
    }
}

class ImageAccessErrorTest {

    @Test
    fun missingFile_withPermission_isNotFound() {
        assertEquals(R.string.error_message_image_not_found, ImageAccess.missingFileError(hasPermission = true))
    }

    @Test
    fun missingFile_withoutPermission_blamesPermission() {
        assertEquals(R.string.error_message_image_no_permission, ImageAccess.missingFileError(hasPermission = false))
    }

    @Test
    fun deniedMediaStoreUri_withoutPermission_blamesPermission() {
        assertEquals(
            R.string.error_message_image_no_permission,
            ImageAccess.deniedUriError("content://media/external/images/media/42", hasPermission = false)
        )
    }

    @Test
    fun deniedMediaStoreUri_withPermission_isUriDenied() {
        assertEquals(
            R.string.error_message_image_uri_denied,
            ImageAccess.deniedUriError("content://media/external/images/media/42", hasPermission = true)
        )
    }

    @Test
    fun deniedOtherProviderUri_isUriDenied() {
        assertEquals(
            R.string.error_message_image_uri_denied,
            ImageAccess.deniedUriError("content://com.example.provider/image", hasPermission = false)
        )
    }
}
