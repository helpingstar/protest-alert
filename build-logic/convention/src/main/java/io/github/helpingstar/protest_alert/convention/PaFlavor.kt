package io.github.helpingstar.protest_alert.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.ApplicationProductFlavor
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.ProductFlavor
import org.gradle.kotlin.dsl.invoke

@Suppress("EnumEntryName")
enum class FlavorDimension {
    contentType,
}

@Suppress("EnumEntryName")
enum class PaFlavor(
    val dimension: FlavorDimension,
    val applicationIdSuffix: String? = null,
) {
    demo(FlavorDimension.contentType, applicationIdSuffix = ".demo"),
    prod(FlavorDimension.contentType)
}

fun configureFlavors(
    commonExtension: CommonExtension,
    flavorConfigurationBlock: ProductFlavor.(flavor: PaFlavor) -> Unit = {},
) {
    commonExtension.apply {
        FlavorDimension.entries.forEach { flavorDimension ->
            flavorDimensions += flavorDimension.name
        }

        productFlavors {
            PaFlavor.entries.forEach { paFlavor ->
                register(paFlavor.name) {
                    dimension = paFlavor.dimension.name
                    flavorConfigurationBlock(this, paFlavor)
                    if (commonExtension is ApplicationExtension && this is ApplicationProductFlavor) {
                        if (paFlavor.applicationIdSuffix != null) {
                            applicationIdSuffix = paFlavor.applicationIdSuffix
                        }
                    }
                }
            }
        }
    }
}