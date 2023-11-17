package com.jasmeet.downloadmanger

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.media3.exoplayer.offline.DownloadHelper
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jasmeet.downloadmanger.room.DownloadVideoDatabase

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.jasmeet.downloadmanger.test", appContext.packageName)
    }
}

@RunWith(AndroidJUnit4::class)
class DownloadHelperTest{

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

}