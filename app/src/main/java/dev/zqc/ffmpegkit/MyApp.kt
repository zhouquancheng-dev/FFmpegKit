package dev.zqc.ffmpegkit

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.gif.AnimatedImageDecoder
import coil3.gif.GifDecoder
import coil3.svg.SvgDecoder
import coil3.util.DebugLogger
import coil3.video.VideoFrameDecoder

class MyApp : Application(),  SingletonImageLoader.Factory {

    companion object {
        private var instance: MyApp? = null

        @JvmStatic
        fun getApplication(): MyApp {
            return instance ?: throw IllegalStateException("Application is not created yet!")
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        initAppLifecycle()
    }

    private fun initAppLifecycle() {
        ProcessLifecycleOwner.get().lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onStart(owner: LifecycleOwner) {
                // 应用进入前台
                Log.i("AppLifecycle", "onForeground")
            }

            override fun onStop(owner: LifecycleOwner) {
                // 应用进入后台
                Log.i("AppLifecycle", "onBackground")
            }
        })
    }

    override fun newImageLoader(context: PlatformContext): ImageLoader {
        return ImageLoader.Builder(context)
            .components {
                add(
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        AnimatedImageDecoder.Factory()
                    } else {
                        GifDecoder.Factory()
                    }
                )
                add(SvgDecoder.Factory())
                add(VideoFrameDecoder.Factory())
            }
            .logger(
                if (BuildConfig.DEBUG) {
                    DebugLogger()
                } else {
                    null
                }
            )
            .build()
    }

}