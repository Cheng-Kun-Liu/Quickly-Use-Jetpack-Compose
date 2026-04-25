package com.laomuji1999.compose.buildlogic.config

import com.android.build.api.dsl.DefaultConfig

object BuildConfigDefaults {
    const val WEB_CLIENT_ID = "954472126977-chps0hidiamvrln1ls96hqp4lgq14co6.apps.googleusercontent.com"
    const val GEMINI_API_KEY = "AIzaSyCuM1ecXRu37ZFy_DIQlIQWC9fkzkljKzg"
}

fun DefaultConfig.addDemoBuildConfigFields() {
    buildConfigField("String", "WEB_CLIENT_ID", "\"${BuildConfigDefaults.WEB_CLIENT_ID}\"")
    buildConfigField("String", "GEMINI_API_KEY", "\"${BuildConfigDefaults.GEMINI_API_KEY}\"")
}
