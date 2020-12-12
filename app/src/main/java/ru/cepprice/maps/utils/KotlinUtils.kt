package ru.cepprice.maps.utils

import ru.cepprice.maps.data.Region

object KotlinUtils {

    fun sortRegions(regions: List<Region>): ArrayList<Region> =
            if (regions.size < 2) ArrayList(regions)
            else {
                val pivot = regions.first()
                val (smaller, greater) = regions.drop(1).partition { it.name <= pivot.name }
                ArrayList(sortRegions(smaller) + pivot + sortRegions(greater))
            }

}