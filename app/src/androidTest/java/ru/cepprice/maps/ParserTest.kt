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
import ru.cepprice.maps.utils.Utils

@RunWith(AndroidJUnit4::class)
class ParserTest {

    private val countries = ArrayList<Region>(49)
    private lateinit var context: Context

    @Before
    fun before() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        countries.addAll(Utils.parseXml(context.resources.getXml(R.xml.regions)))
    }

    @Test
    fun testGeneral() {
        assertEquals(49, countries.size.toLong())
    }

    @Test
    fun testDenmark() {
        val list = countries.filter { it.name == "Denmark" }
        assertTrue(list.size == 1)

        val (name, hasMap, downloadName, prefix, subRegions) = list.first()
        assertEquals("Denmark", name)
        assertEquals("Denmark_europe_2.obf.zip", downloadName)
        assertEquals("denmark", prefix)
        assertEquals(5, subRegions.size)
        assertTrue(hasMap)

        val (name1, hasMap1, downloadName1, prefix1, subRegions1) = subRegions[2]
        assertEquals("North Denmark Region", name1)
        assertEquals("Denmark_north-region_europe_2.obf.zip", downloadName1)
        assertEquals(0, subRegions1.size)
        assertEquals("", prefix1)
        assertTrue(hasMap1)
    }

    @Test
    fun testGermany() {
        val list = countries.filter { it.name == "Germany" }
        assertTrue(list.size == 1)

        val (name, hasMap, downloadName, prefix, subRegions) = list.first()
        assertEquals("Germany", name)
        assertTrue(!hasMap)
        assertEquals("", downloadName)
        assertEquals("germany", prefix)
        assertTrue(subRegions.size == 15)

        // Baden-Württemberg
        val list1 = subRegions.filter { it.name == "Baden-Württemberg" }
        assertTrue(list1.size == 1)

        val (name1, hasMap1, downloadName1, prefix1, subRegions1) = list1.first()
        assertEquals("Baden-Württemberg", name1)
        assertTrue(hasMap1)
        assertEquals("Germany_baden-wuerttemberg_europe_2.obf.zip", downloadName1)
        assertEquals("germany_baden-wuerttemberg", prefix1)
        assertTrue(subRegions1.size == 4)

        // Regierungsbezirk Karlsruhe
        val list2 = subRegions1.filter { it.name == "Regierungsbezirk Karlsruhe" }
        assertTrue(list2.size == 1)

        val (name2, hasMap2, downloadName2, prefix2, subRegions2) = list2.first()
        assertEquals("Regierungsbezirk Karlsruhe", name2)
        assertTrue(hasMap2)
        assertEquals("Germany_baden-wuerttemberg_karlsruhe_europe_2.obf.zip", downloadName2)
        assertEquals("", prefix2)
        assertTrue(subRegions2.isEmpty())
    }

    @Test
    fun testMonaco() {
        val list = countries.filter { it.name == "Monaco" }
        assertTrue(list.size == 1)

        val (name, hasMap, downloadName, prefix, subRegions) = list.first()
        assertEquals("Monaco", name)
        assertTrue(hasMap)
        assertEquals("Monaco_europe_2.obf.zip", downloadName)
        assertEquals("", prefix)
        assertTrue(subRegions.isEmpty())
    }
}