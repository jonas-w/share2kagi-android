package com.kagi.summarizer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kagi.summarizer.ui.theme.KagiSummarizerTheme
import me.zhanghai.compose.preference.ProvidePreferenceLocals
import me.zhanghai.compose.preference.listPreference
import me.zhanghai.compose.preference.preferenceCategory
import me.zhanghai.compose.preference.switchPreference


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            Settings()

        }
    }
}

@Composable
fun ShareButtons() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(16.dp) // Add rounded corners
            ), horizontalAlignment = Alignment.CenterHorizontally, // Center horizontally
        verticalArrangement = Arrangement.Center // Center vertically
    ) {
        Text(
            text = "To try out the functionality of this app, you can use the buttons below to share either an article URL or some sample text with the Kagi Summarizer.",
            style = MaterialTheme.typography.bodyMedium,
            color= MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.padding(30.dp) // Add padding for better readability
        )
        Button(onClick = {
            shareContent(
                context, "https://blog.kagi.com/kagi-translate", "Check out this article!"
            )
        }) {
            Text("Share an Article")
        }

        Button(onClick = {
            shareContent(
                context, "The cake is a lie.", "Sharing some thoughts."
            )
        }) {
            Text("Share Text")
        }
    }
}

fun shareContent(context: Context, content: String, subject: String) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, content)
        putExtra(Intent.EXTRA_SUBJECT, subject)
        type = "text/plain"
        `package` = context.packageName
    }

    val shareIntent = Intent.createChooser(sendIntent, null)
    context.startActivity(shareIntent)
}

@Preview
@Composable
fun Settings() {
    KagiSummarizerTheme {
        ProvidePreferenceLocals {
            Column(modifier = Modifier.fillMaxSize()) {
                LazyColumn(modifier = Modifier.weight(1f)) {

                    preferenceCategory(
                        key = "summary",
                        title = { Text(text = "Summary") },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    listPreference(
                        key = "kagi_language",
                        defaultValue = "Default",
                        values = SUMMARY_LANGUAGES.keys.toList(),
                        title = { Text(text = "Language") },
                        summary = { Text(text = it) },
                    )
                    preferenceCategory(
                        key = "translate",
                        title = { Text(text = "Translation") },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    listPreference(
                        key = "kagi_translate_source_language",
                        defaultValue = "Automatic",
                        values = TRANSLATE_LANGUAGES,
                        title = { Text(text = "Source Language") },
                        summary = { Text(text = it) },
                    )
                    listPreference(key = "kagi_translate_target_language",
                        defaultValue = "English",
                        values = TRANSLATE_LANGUAGES.slice(1..TRANSLATE_LANGUAGES.lastIndex),
                        title = { Text(text = "Target Language") },
                        summary = { Text(text = it) })
                    preferenceCategory(
                        key = "browser",
                        title = { Text(text = "Browser") },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    switchPreference(key = "custom_tab",
                        defaultValue = true,
                        title = { Text(text = "Open Kagi in Custom Tab") })

                }

                ShareButtons()

            }
        }
    }
}