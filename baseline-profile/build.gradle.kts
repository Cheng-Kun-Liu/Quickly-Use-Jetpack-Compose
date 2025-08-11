plugins {
    alias(libs.plugins.laomuji1999.compose.baselineprofile)
}

android {
    namespace = "com.laomuji1999.compose.baselineprofile"

    //因为在 :app 里有 flavor 所以需要在这里创建对应的编译变量
    targetProjectPath = ":app"
    flavorDimensions += listOf("channel")
    productFlavors {
        create("gp") { dimension = "channel" }
        create("sam") { dimension = "channel" }
    }

    //创建该设备来作为生成 baseline-prof.txt 的设备.
    testOptions.managedDevices.allDevices  {
        @Suppress("UnstableApiUsage")
        create<com.android.build.api.dsl.ManagedVirtualDevice>("pixel6Api33") {
            device = "Pixel 6"
            apiLevel = 33
            systemImageSource = "aosp"
        }
    }
}

//使用生成的 pixel6Api33 设备,不使用真机的原因是为了生成的目录一致.
//运行右侧 Gradle->quickly->baseline-profile->Tasks->baseline profile 中的 gradle 命令.
baselineProfile {
    managedDevices.clear()
    managedDevices += "pixel6Api33"
    useConnectedDevices = false
}

androidComponents {
    @Suppress("UnstableApiUsage")
    onVariants { v ->
        val artifactsLoader = v.artifacts.getBuiltArtifactsLoader()
        v.instrumentationRunnerArguments.put(
            "targetAppId",
            v.testedApks.map { artifactsLoader.load(it)?.applicationId!! }
        )
    }
}