package ru.cepprice.maps.utils;

// Map of region either provided or not
// If it is provided, it can be not downloaded, downloaded,
// or in process of being downloaded
public enum MapState {
    NOT_PROVIDED, NOT_DOWNLOADED, IN_PROCESS, DOWNLOADED
}
