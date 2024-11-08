package com.kagi.summarizer

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.preference.PreferenceManager
import android.util.Patterns
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.browser.customtabs.CustomTabsIntent
import java.util.regex.Matcher

class HandleShare {
    companion object {
        fun launchURL(activity: ComponentActivity, preferences: SharedPreferences, intentUrl: Uri) {
            if (preferences.getBoolean("custom_tab", false)) {
                val intent: CustomTabsIntent = CustomTabsIntent.Builder().build()
                intent.launchUrl(activity, intentUrl)
            } else {
                activity.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        intentUrl,
                    )
                )
            }
        }

        fun summary(
            activity: ComponentActivity,
            kagiType: KagiType.SUMMARY,
            sharedText: String?,
            urlMatcher: Matcher,
            preferences: SharedPreferences
        ) {
            val summaryLanguage = preferences.getString("kagi_language", "Default") ?: "Default"

            val builder = Uri.Builder().scheme("https").authority("kagi.com")

            if (kagiType != KagiType.SUMMARY.DISCUSS) {
                if (summaryLanguage != "Default") {
                    builder.appendQueryParameter(
                        "target_language", SUMMARY_LANGUAGES.getOrDefault(summaryLanguage, "")
                    )
                }
            }
            when (kagiType) {
                KagiType.SUMMARY.DISCUSS -> builder.path("discussdoc")
                KagiType.SUMMARY.DEFAULT -> builder.path("summarizer")
                    .appendQueryParameter("summary", "summary")

                KagiType.SUMMARY.TAKEAWAY -> builder.path("summarizer")
                    .appendQueryParameter("summary", "takeaway")
            }

            if (urlMatcher.matches()) {
                builder.appendQueryParameter("url", Uri.encode(urlMatcher.group().toString()))
            } else {
                if (activity is DiscussActivity) {
                    Toast.makeText(
                        activity,
                        "Discuss is not supported for text, only URLs can be discussed currently.",
                        Toast.LENGTH_SHORT
                    ).show()
                    activity.finish()
                    return
                }
                builder.fragment(Uri.encode(sharedText.toString()))
            }
            launchURL(activity, preferences, builder.build())
        }

        fun openKagi(activity: ComponentActivity, kagiType: KagiType) {
            val sharedText = activity.intent.getStringExtra(Intent.EXTRA_TEXT)
            val urlMatcher = Patterns.WEB_URL.matcher(sharedText.toString())

            val preferences =
                PreferenceManager.getDefaultSharedPreferences(activity.applicationContext)
            if (kagiType is KagiType.SUMMARY) {
                summary(
                    activity, kagiType, sharedText, urlMatcher, preferences
                )
            } else if (kagiType is KagiType.TRANSLATE) {
                translate(
                    activity, sharedText, urlMatcher, preferences
                )
            }

            activity.finish()
        }

        private fun translate(
            activity: ComponentActivity,
            sharedText: String?,
            urlMatcher: Matcher,
            preferences: SharedPreferences
        ) {
            val sourceLanguage =
                preferences.getString("kagi_translate_source_language", "Automatic") ?: "Automatic"
            val targetLanguage =
                preferences.getString("kagi_translate_target_language", "English") ?: "English"
            val builder = Uri.Builder().scheme("https").authority("translate.kagi.com")

            if (urlMatcher.matches()) {
                builder.path(
                    sourceLanguage + "/" + targetLanguage + "/" + urlMatcher.group().toString()
                )
            } else {
                // there is also a model parameter, only known value is "fast" though
                builder.appendQueryParameter("source", sourceLanguage)
                    .appendQueryParameter("target", targetLanguage)
                    .appendQueryParameter("text", sharedText.toString())
            }

            launchURL(activity, preferences, builder.build())
        }
    }
}