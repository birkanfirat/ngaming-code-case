package com.ngaming.ngamingcase.posts.ui.testing

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher

/**
 * Akışı testin arka planında topluyor. WhileSubscribed ile paylaşılan bir StateFlow abonesi
 * olmadan üst akışı çalıştırmıyor, dolayısıyla value okunmadan önce bu gerekiyor.
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun TestScope.collectInBackground(flow: Flow<Any?>) {
    backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { flow.collect { } }
}
