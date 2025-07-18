plugins {
    alias(libs.plugins.laomuji1999.compose.library)
}

android {
    namespace = "com.laomuji1999.compose.core.ui"
}

dependencies {
    //为所有ui引入资源文件
    api(project(":res"))

    //引入common
    implementation(project(":core-logic:common"))

    //为Android提供kotlin扩展功能,view,context,collection,和其它扩展
    api(libs.androidx.core.ktx)
    //管理Android生命周期
    api(libs.androidx.lifecycle.runtime.ktx)

    //基础库
    api(libs.androidx.material3)
    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.compose.ui.tooling)
    api(libs.androidx.compose.ui.tooling.preview)
    api(libs.androidx.compose.ui)
    api(libs.androidx.compose.ui.graphics)

    //compose的navigation扩展
    implementation(libs.androidx.navigation.compose)

    //hilt的navigation扩展
    api(libs.androidx.hilt.navigation.compose)

    //coil 图像加载
    api(libs.coil.compose)
}
