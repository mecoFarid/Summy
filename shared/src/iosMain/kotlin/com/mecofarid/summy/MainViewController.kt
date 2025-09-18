package com.mecofarid.summy

import androidx.compose.ui.window.ComposeUIViewController
import com.mecofarid.summy.app.App
import com.mecofarid.yarus.core.platform.PlatformConfig
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

fun MainViewController() = ComposeUIViewController {
    App(
        platformConfig = PlatformConfig(),
        onHandleUrl = {
            val nsUrl = NSURL.URLWithString(it)
            if (nsUrl != null && UIApplication.sharedApplication.canOpenURL(nsUrl)) {
                UIApplication.sharedApplication.openURL(
                    url = nsUrl,
                    options = emptyMap<Any?, Any>(),
                    completionHandler = null,
                )
            }
        },
    )
}
