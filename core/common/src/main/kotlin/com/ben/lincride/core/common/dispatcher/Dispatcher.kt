package com.ben.lincride.core.common.dispatcher

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val dispatcher: LincRideDispatchers)

enum class LincRideDispatchers {
    Default,
    IO,
    Main,
    Unconfined,
}