package com.mecofarid.summy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalUriHandler
import com.mecofarid.summy.app.App
import com.mecofarid.yarus.core.platform.PlatformConfig

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val urlHandler = LocalUriHandler.current
            App(
                platformConfig = PlatformConfig(activity = this),
                onHandleUrl = {
                    urlHandler.openUri(it)
                }
            )
        }
    }
}