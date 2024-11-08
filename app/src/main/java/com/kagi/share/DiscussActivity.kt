package com.kagi.share

import android.os.Bundle
import androidx.activity.ComponentActivity

class DiscussActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HandleShare.openKagi(this, KagiType.SUMMARY.DISCUSS)
    }
}
