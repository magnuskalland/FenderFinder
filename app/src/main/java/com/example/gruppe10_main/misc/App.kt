package com.example.gruppe10_main.misc

import android.app.Application
import android.content.Context

/**
 * Static method to get context
 */
class App : Application() {
    init { instance = this }
    companion object {
        private var instance: App? = null
        fun getContext(): Context = instance!!.applicationContext
    }
}

