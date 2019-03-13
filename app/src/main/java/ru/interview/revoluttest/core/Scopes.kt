package ru.interview.revoluttest.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

object Scopes {
    val Main = CoroutineScope(Dispatchers.Main)
    val IO = CoroutineScope(Dispatchers.IO)
}
