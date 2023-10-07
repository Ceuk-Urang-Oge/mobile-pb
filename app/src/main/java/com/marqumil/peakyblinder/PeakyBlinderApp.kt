package com.marqumil.peakyblinder

import android.app.Application
import com.orhanobut.hawk.Hawk

class PeakyBlinderApp: Application() {
    override fun onCreate() {
        super.onCreate()
        Hawk.init(this)
            .build()
        context = this
    }

    companion object {
        lateinit var context: PeakyBlinderApp
            private set
    }
}