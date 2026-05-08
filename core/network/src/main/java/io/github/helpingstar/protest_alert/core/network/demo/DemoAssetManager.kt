package io.github.helpingstar.protest_alert.core.network.demo

import java.io.InputStream

fun interface DemoAssetManager {
    fun open(fileName: String): InputStream
}