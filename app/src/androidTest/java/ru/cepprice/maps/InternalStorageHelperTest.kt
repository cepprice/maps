package ru.cepprice.maps

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.cepprice.maps.data.local.InternalStorageHelper

@RunWith(AndroidJUnit4::class)
class InternalStorageHelperTest {

    private lateinit var context: Context

    @Before
    fun before() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun testFileHandling() {
        clearFile()

        InternalStorageHelper.addDownloadedMap(context, "1")
        InternalStorageHelper.addDownloadedMap(context, "2")
        InternalStorageHelper.addDownloadedMap(context, "3")
        var downloadedMaps = InternalStorageHelper.getDownloadedMaps(context)
        assertEquals(3, downloadedMaps.size)

        clearFile()
        downloadedMaps = InternalStorageHelper.getDownloadedMaps(context)
        assertEquals(0, downloadedMaps.size)
    }

    private fun clearFile() {
        try {
            val out = context.openFileOutput("downloaded_maps.txt", Context.MODE_PRIVATE)
            out.write(("").toByteArray())
            out.flush()
            out.close()
        } catch (ignored: Exception) {
        }
    }

}