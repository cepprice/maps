package ru.cepprice.maps

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.cepprice.maps.data.Region
import ru.cepprice.maps.utils.RegionProvider
import ru.cepprice.maps.utils.mapstate.NotDownloaded
import ru.cepprice.maps.utils.mapstate.NotProvided

@RunWith(AndroidJUnit4::class)
class ParserTest {

    private val countries = ArrayList<Region>(49)
    private lateinit var context: Context

    @Before
    fun before() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        countries.addAll(RegionProvider.getRegions(context))
    }

    @Test
    fun testGeneral() {
        assertEquals(49, countries.size.toLong())
    }

    @Test
    fun testDenmark() {
        val list = countries.filter { it.name == "Denmark" }
        assertTrue(list.size == 1)

        val denmark = list[0]
        with(denmark) {
            assertEquals("Denmark", name)
            assertEquals("Denmark_europe_2.obf.zip", downloadName)
            assertEquals("denmark", innerDownloadPrefix)
            assertEquals(5, childRegions.size)
            assertTrue(mapState is NotDownloaded)
        }

        val northDenmarkRegion = denmark.childRegions[2]
        with(northDenmarkRegion){
            assertEquals("North Denmark Region", name)
            assertEquals("Denmark_north-region_europe_2.obf.zip", downloadName)
            assertEquals(0, childRegions.size)
            assertEquals("", innerDownloadPrefix)
            assertTrue(mapState is NotDownloaded)
        }

    }

    @Test
    fun testGermany() {
        val list = countries.filter { it.name == "Germany" }
        assertTrue(list.size == 1)

        val germany = list[0]
        with(germany) {
            assertEquals("Germany", name)
            assertEquals("", downloadName)
            assertEquals("germany", innerDownloadPrefix)
            assertTrue(mapState is NotProvided)
            assertTrue(childRegions.size == 15)
        }

        // Baden-Württemberg
        val list1 = germany.childRegions.filter { it.name == "Baden-Württemberg" }
        assertTrue(list1.size == 1)

        val badenWerttemberg = list1.first()
        with(badenWerttemberg) {
            assertEquals("Baden-Württemberg", name)
            assertEquals("Germany_baden-wuerttemberg_europe_2.obf.zip", downloadName)
            assertEquals("germany_baden-wuerttemberg", innerDownloadPrefix)
            assertTrue(childRegions.size == 4)
            assertTrue(mapState is NotDownloaded)
        }

        // Regierungsbezirk Karlsruhe
        val list2 = badenWerttemberg.childRegions.filter { it.name == "Regierungsbezirk Karlsruhe" }
        assertTrue(list2.size == 1)

        val karlsruhe = list2.first()
        with(karlsruhe) {
            assertEquals("Regierungsbezirk Karlsruhe", name)
            assertEquals("Germany_baden-wuerttemberg_karlsruhe_europe_2.obf.zip", downloadName)
            assertEquals("", innerDownloadPrefix)
            assertTrue(mapState is NotDownloaded)
            assertTrue(childRegions.isEmpty())
        }

    }

    @Test
    fun testMonaco() {
        val list = countries.filter { it.name == "Monaco" }
        assertTrue(list.size == 1)

        val monaco = list.first()
        with(monaco) {
            assertEquals("Monaco", name)
            assertEquals("Monaco_europe_2.obf.zip", downloadName)
            assertEquals("", innerDownloadPrefix)
            assertTrue(mapState is NotDownloaded)
            assertTrue(childRegions.isEmpty())
        }
    }
}