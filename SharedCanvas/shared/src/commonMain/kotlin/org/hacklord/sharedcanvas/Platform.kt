package org.hacklord.sharedcanvas

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform