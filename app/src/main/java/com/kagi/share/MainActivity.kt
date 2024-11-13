package com.kagi.share

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.kagi.share.ui.theme.Share2KagiTheme
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
            text = "To try out the functionality of this app, you can use the buttons below to share either an article URL or some sample text with Kagi.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.padding(30.dp) // Add padding for better readability
        )
        Row(
            horizontalArrangement = Arrangement.Center, modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Button(onClick = {
                    shareContent(
                        context, "https://blog.kagi.com/kagi-translate", "Check out this article!"
                    )
                }, modifier = Modifier.padding(5.dp)) {
                    Text("Share an Article")
                }
                Button(onClick = {
                    shareContent(
                        context,
                        "https://kagifeedback.org/assets/files/2024-01-23/1706048438-993881-img-0492.jpg",
                        "Find similar images!"
                    )
                }, modifier = Modifier.padding(5.dp)) {
                    Text("Share an Image URL")
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Button(onClick = {
                    shareContent(
                        context, "The cake is a lie.", "Sharing some thoughts."
                    )
                }, modifier = Modifier.padding(5.dp)) {
                    Text("Share Text")
                }
                Button(onClick = {
                    shareContent(
                        context,
                        "At Kagi, we envision a friendly version of the internet where users can explore, learn, and interact with confidence and ease. Our mission is to humanize the web, making it more accessible, ethical, and centered around the needs of individuals. We are driven by our core purpose: to inform and educate, empowering users with knowledge and understanding in their digital journey.\n" + "\n" + "Kagi (pronounced kah-gee) was founded in 2018 by Vladimir Prelovac in Palo Alto, CA (USA). Kagi's advisory board consists of Raghu Murthi, Dr. Norman Winarsky, and Stephen Wolfram. Our team is fully remote, operating in over 10 countries. We are actively hiring.\n" + "\n" + "Kagi was bootstrapped from 2018 to 2023 with ~\$3M initial funding from the founder. In 2023, Kagi raised \$670K from Kagi users in its first external fundraise, followed by \$1.88M raised in 2024, again from our users, bringing the number of users-investors to 93.\n" + "\n" + "Kagi launched in June 2022 and we maintain a public page tracking real-time Kagi growth and usage statistics at kagi.com/stats.\n" + "\n" + "In early 2024, Kagi became a Public Benefit Corporation (PBC).\n" + "\n" + "We want to bring a friendly version of the internet, one that has the user's best interest in mind, into homes and businesses worldwide with our portfolio of products.\n" + "\n" + "Kagi currently has two core products: Kagi Search, a fast, private search engine, and Orion Browser, a fast, zero-telemetry, WebKit-based browser.",
                        "Sharing some thoughts."
                    )
                }, modifier = Modifier.padding(5.dp)) {
                    Text("Share long Text")
                }
            }
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
    Share2KagiTheme {
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