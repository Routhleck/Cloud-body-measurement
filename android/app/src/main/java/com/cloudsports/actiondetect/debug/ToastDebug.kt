package com.cloudsports.actiondetect.debug

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast

class ToastDebug(private var context: Context?) {
    private var toast: Toast? = null

    fun show(text: String) {
        // 实时更新Toast
        Handler(Looper.getMainLooper()).post {
            toast?.cancel()
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT)
            toast?.show()
        }
    }
}