package com.awkwardlydevelopedapps.unicharsheet.common.utils

import android.util.Log

class LogWrapper {
    companion object {
        private const val isRelease = false

        fun v(tag: String, message: String) {
            if (!isRelease)
                Log.v(tag, message)
        }
    }

}