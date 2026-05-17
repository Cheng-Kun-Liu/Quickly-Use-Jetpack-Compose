package com.laomuji1999.compose.core.logic

import android.content.Context
import android.content.res.Resources
import com.laomuji1999.compose.res.R
import java.util.Locale

sealed class AppLanguages(open val locale: Locale, val tag: String) {
    data object FlowSystem : AppLanguages(getSystemLanguage(), "system") {
        override val locale: Locale
            get() = getSystemLanguage()
    }

    data object ChineseSimplified : AppLanguages(Locale.forLanguageTag("zh-Hans"), "zh-Hans")
    data object ChineseTraditional : AppLanguages(Locale.forLanguageTag("zh-Hant"), "zh-Hant")
    data object English : AppLanguages(Locale.forLanguageTag("en"), "en")
    data object Spanish : AppLanguages(Locale.forLanguageTag("es"), "es")
    data object Arabic : AppLanguages(Locale.forLanguageTag("ar"), "ar")
    data object Bengali : AppLanguages(Locale.forLanguageTag("bn"), "bn")

    companion object {
        fun getSystemLanguage(): Locale {
            val locales = Resources.getSystem().configuration.locales
            return if (locales.size() > 0) {
                locales[0]
            } else {
                Locale.getDefault()
            }
        }

        fun getAppLanguageList(): List<AppLanguages> {
            return listOf(
                FlowSystem,
                ChineseSimplified,
                ChineseTraditional,
                English,
                Spanish,
                Arabic,
                Bengali
            )
        }

        fun fromTag(tag: String?): AppLanguages {
            for (appLanguages in getAppLanguageList()) {
                if (appLanguages.tag == tag) {
                    return appLanguages
                }
            }
            return FlowSystem
        }
    }

    fun getDisplayName(context: Context): String {
        if (this is FlowSystem) {
            return context.getString(R.string.string_language_system)
        }
        return locale.displayName
    }
}