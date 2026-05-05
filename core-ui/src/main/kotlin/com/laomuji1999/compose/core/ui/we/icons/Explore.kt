package com.laomuji1999.compose.core.ui.we.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val WeIcons.Explore: ImageVector
    get() {
        if (_Explore != null) {
            return _Explore!!
        }
        _Explore = ImageVector.Builder(
            name = "WeIcons.Explore",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(fill = SolidColor(Color.White)) {
                moveTo(300f, 660f)
                lineTo(580f, 580f)
                lineTo(660f, 300f)
                lineTo(380f, 380f)
                lineTo(300f, 660f)
                close()
                moveTo(480f, 540f)
                quadTo(455f, 540f, 437.5f, 522.5f)
                quadTo(420f, 505f, 420f, 480f)
                quadTo(420f, 455f, 437.5f, 437.5f)
                quadTo(455f, 420f, 480f, 420f)
                quadTo(505f, 420f, 522.5f, 437.5f)
                quadTo(540f, 455f, 540f, 480f)
                quadTo(540f, 505f, 522.5f, 522.5f)
                quadTo(505f, 540f, 480f, 540f)
                close()
                moveTo(480f, 880f)
                quadTo(397f, 880f, 324f, 848.5f)
                quadTo(251f, 817f, 197f, 763f)
                quadTo(143f, 709f, 111.5f, 636f)
                quadTo(80f, 563f, 80f, 480f)
                quadTo(80f, 397f, 111.5f, 324f)
                quadTo(143f, 251f, 197f, 197f)
                quadTo(251f, 143f, 324f, 111.5f)
                quadTo(397f, 80f, 480f, 80f)
                quadTo(563f, 80f, 636f, 111.5f)
                quadTo(709f, 143f, 763f, 197f)
                quadTo(817f, 251f, 848.5f, 324f)
                quadTo(880f, 397f, 880f, 480f)
                quadTo(880f, 563f, 848.5f, 636f)
                quadTo(817f, 709f, 763f, 763f)
                quadTo(709f, 817f, 636f, 848.5f)
                quadTo(563f, 880f, 480f, 880f)
                close()
                moveTo(480f, 800f)
                quadTo(613f, 800f, 706.5f, 706.5f)
                quadTo(800f, 613f, 800f, 480f)
                quadTo(800f, 347f, 706.5f, 253.5f)
                quadTo(613f, 160f, 480f, 160f)
                quadTo(347f, 160f, 253.5f, 253.5f)
                quadTo(160f, 347f, 160f, 480f)
                quadTo(160f, 613f, 253.5f, 706.5f)
                quadTo(347f, 800f, 480f, 800f)
                close()
                moveTo(480f, 480f)
                quadTo(480f, 480f, 480f, 480f)
                quadTo(480f, 480f, 480f, 480f)
                quadTo(480f, 480f, 480f, 480f)
                quadTo(480f, 480f, 480f, 480f)
                quadTo(480f, 480f, 480f, 480f)
                quadTo(480f, 480f, 480f, 480f)
                quadTo(480f, 480f, 480f, 480f)
                quadTo(480f, 480f, 480f, 480f)
                close()
            }
        }.build()

        return _Explore!!
    }

@Suppress("ObjectPropertyName")
private var _Explore: ImageVector? = null
