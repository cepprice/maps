package ru.cepprice.maps.data

data class Region(
    val name: String,
    val hasMap: Boolean,
    val downloadName: String,
    val innerDownloadPrefix: String,
    val subRegions: List<Region>
)