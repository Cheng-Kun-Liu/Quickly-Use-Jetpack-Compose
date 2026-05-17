package com.laomuji1999.compose.buildlogic.config

import com.android.build.api.dsl.DefaultConfig

object BuildConfigDefaults {
    const val WEB_CLIENT_ID = "954472126977-chps0hidiamvrln1ls96hqp4lgq14co6.apps.googleusercontent.com"
}

fun DefaultConfig.addDemoBuildConfigFields() {
    buildConfigField("String", "WEB_CLIENT_ID", "\"${BuildConfigDefaults.WEB_CLIENT_ID}\"")
}
