package com.kinar.firstopencvapp.extensions

import android.app.Activity
import android.content.Intent

/**
 * Project FirstOpenCVApp.
 *
 * Created by Rhony Abdullah Siagian on 19/10/18.
 */

fun Activity.start(target: Class<*>, requestCode: Int? = null, func: (Intent.() -> Unit)? = null) {
    val intent = Intent(this, target)
    func?.let { intent.it() }
    if (requestCode != null) {
        startActivityForResult(intent, requestCode)
    } else {
        startActivity(intent)
    }
}