package com.kagi.share

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.util.Patterns
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.preference.PreferenceManager
import java.util.regex.Matcher

class HandleShare {
    companion object {
        private fun getKagiURLBuilder(): Uri.Builder {
            return Uri.Builder().scheme("https").authority("kagi.com")
        }

        fun openKagi(activity: ComponentActivity, kagiType: KagiType) {
            val sharedText = activity.intent.getStringExtra(Intent.EXTRA_TEXT)
            val urlMatcher = Patterns.WEB_URL.matcher(sharedText.toString())

            val preferences =
                PreferenceManager.getDefaultSharedPreferences(activity.applicationContext)

            when (kagiType) {
                is KagiType.SUMMARY -> summary(
                    activity, kagiType, sharedText, urlMatcher, preferences
                )

                is KagiType.TRANSLATE -> translate(
                    activity, sharedText, urlMatcher, preferences
                )

                is KagiType.IMAGE -> image(
                    activity, sharedText, urlMatcher, preferences
                )
            }

            activity.finish()
        }

        private fun image(
            activity: ComponentActivity,
            sharedText: String?,
            urlMatcher: Matcher,
            preferences: SharedPreferences
        ) {
            if (urlMatcher.matches()) {
                val builder = getKagiURLBuilder()
                builder.path("images").appendQueryParameter("q", urlMatcher.group().toString())
                    .appendQueryParameter("reverse", "reference")
                launchURL(activity, preferences, builder.build())
            } else {
                Toast.makeText(
                    activity,
                    "Reverse Image search currently only accepts image URLs.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        private fun launchURL(
            activity: ComponentActivity, preferences: SharedPreferences, intentUrl: Uri
        ) {
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

        private fun summary(
            activity: ComponentActivity,
            kagiType: KagiType.SUMMARY,
            sharedText: String?,
            urlMatcher: Matcher,
            preferences: SharedPreferences
        ) {
            val summaryLanguage = preferences.getString("kagi_language", "Default")

            val builder = getKagiURLBuilder()

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
                builder.appendQueryParameter("url", urlMatcher.group().toString())
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
                builder.encodedFragment(sharedText.toString())
            }
            launchURL(activity, preferences, builder.build())
        }


        private fun translate(
            activity: ComponentActivity,
            sharedText: String?,
            urlMatcher: Matcher,
            preferences: SharedPreferences
        ) {
            val sourceLanguage =
                preferences.getString("kagi_translate_source_language", "Automatic")
            val targetLanguage = preferences.getString("kagi_translate_target_language", "English")
            val builder = getKagiURLBuilder()

            if (urlMatcher.matches()) {
                if (sourceLanguage != "Automatic") {
                    builder.appendPath(
                        sourceLanguage
                    )
                }
                builder.appendPath(targetLanguage).appendEncodedPath(
                    urlMatcher.group().toString()
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