package io.github.helpingstar.protest_alert.core.network

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(val paDispatcher: PaDispatchers)

enum class PaDispatchers {
    Default,
    IO,
}
