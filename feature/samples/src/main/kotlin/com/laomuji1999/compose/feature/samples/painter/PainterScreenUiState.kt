package com.laomuji1999.compose.feature.samples.painter

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

data class PainterScreenUiState(
    val colorList:List<Color> = emptyList(),
    val currentColorIndex:Int = 0,
    val widthList:List<Dp> = emptyList(),
    val currentWidthIndex:Int = 0,
    val pathList:List<PathData> = emptyList(),
    val currentPath:PathData?=null
)

data class PathData(
    val color: Color,
    val width: Dp,
    val path: List<Offset>
)
