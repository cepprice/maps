package ru.cepprice.maps

import android.os.Environment
import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.cepprice.maps.data.local.StorageHelper
import ru.cepprice.maps.utils.Constants
import java.io.File

@RunWith(AndroidJUnit4::class)
class StorageHelperTest {

    private lateinit var documentsFolder: File
    private lateinit var srcFolder: File


    @Before
    fun before() {
        documentsFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        Log.d("M_StorageHelperTest", "Documents folder was created: ${documentsFolder.mkdirs()}")
        srcFolder = File(documentsFolder, Constants.EXTERNAL_STORAGE_FOLDER_NAME)
        Log.d("M_StorageHelperTest", "Source folder was created: ${srcFolder.mkdir()}")
    }

    @Test
    fun testFileHandling() {
        clearSrcFolder()
        assertEquals(0, filesOfSrcFolder())

        repeat(3) {
            createNewFile("$it")
        }
        assertEquals(3, filesOfSrcFolder())

        createNewFile("dhdh")
        assertEquals(4, filesOfSrcFolder())

        clearSrcFolder()
        assertEquals(0, filesOfSrcFolder())

        repeat(10) {
            createNewFile("$it")
        }
        assertEquals(10, filesOfSrcFolder())

        clearSrcFolder()
    }

    private fun clearSrcFolder() {
        srcFolder.deleteRecursively()
        srcFolder.mkdirs()
    }

    private fun filesOfSrcFolder() = StorageHelper.getDownloadedMaps().size

    private fun createNewFile(name: String) {
        val newFile = File(srcFolder, name)
        newFile.createNewFile()
    }

}