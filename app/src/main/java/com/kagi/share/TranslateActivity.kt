package com.kagi.share

import android.os.Bundle
import androidx.activity.ComponentActivity

class TranslateActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HandleShare.openKagi(this, KagiType.TRANSLATE)
    }
}