package com.kagi.summarizer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kagi.summarizer.ui.theme.KagiSummarizerTheme
import me.zhanghai.compose.preference.ProvidePreferenceLocals
import me.zhanghai.compose.preference.listPreference
import me.zhanghai.compose.preference.switchPreference


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            Settings()

        }
    }
}

@Preview
@Composable
fun Settings() {
    KagiSummarizerTheme {
        ProvidePreferenceLocals {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                listPreference(key = "kagi_language",
                    defaultValue = "Default",
                    values = SUMMARY_LANGUAGES.keys.toList(),
                    title = { Text(text = "Summary Language") },
                    summary = { Text(text = it) })
                listPreference(key = "kagi_translate_source_language",
                    defaultValue = "Automatic",
                    values = TRANSLATE_LANGUAGES,
                    title = { Text(text = "Translation Source Language") },
                    summary = { Text(text = it) })
                listPreference(key = "kagi_translate_target_language",
                    defaultValue = "English",
                    values = TRANSLATE_LANGUAGES,
                    title = { Text(text = "Translation Target Language") },
                    summary = { Text(text = it) })
                switchPreference(key = "custom_tab",
                    defaultValue = true,
                    title = { Text(text = "Open Summary in Custom Tab") })
            }
        }
    }
}