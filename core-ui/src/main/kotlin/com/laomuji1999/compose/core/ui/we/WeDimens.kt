package com.laomuji1999.compose.core.ui.we

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class WeDimens(
    //事件(有点击效果的) Icon 尺寸
    val actionIconSize: Dp,

    //顶部导航 尺寸
    val topBarHeight: Dp,
    val topBarPaddingHorizontal: Dp,
    val topBarActionSpace: Dp,

    //底部导航 尺寸
    val bottomBarIconSize: Dp,
    val bottomBarHeight: Dp,

    //按钮 尺寸
    val buttonRondCornerDp: Dp,
    val bigButtonWidth: Dp,
    val bigButtonHeight: Dp,
    val smallButtonWidth: Dp,
    val smallButtonHeight: Dp,
    val warpButtonHorizontalPaddingDp: Dp,
    val warpButtonVerticalPaddingDp: Dp,

    //Row组件 尺寸
    val rowSingleHeight: Dp,
    val rowDoubleHeight: Dp,
    val rowPaddingHorizontal: Dp,
    val rowInnerPaddingHorizontal: Dp,
    val rowIconSize: Dp,
    val rowIconRoundedCornerDp: Dp,

    //Input 尺寸
    val inputLabelWidth: Dp,

    //Action Sheet 尺寸
    val actionSheetRoundedCornerDp: Dp,

    //开关按钮宽度
    val switchIconWidth: Dp,
    //开关按钮高度,不能超过宽度
    val switchIconHeight: Dp,

    //Toast 尺寸
    val toastSize: Dp,
    val toastIconSize: Dp,
    val toastDividerHeight: Dp,

    //Outline 尺寸
    val outlineHeight: Dp,
    val outlineSplitHeight: Dp,

    //聊天页面,特定尺寸
    val chatInputHeight: Dp,
    val chatPadding: Dp,
    val chatAvatarSize: Dp,
    val chatAvatarRoundedCornerDp: Dp,

    //联系人页面,特定尺寸
    val categorySize: Dp,
)

val WeDimensDefault = WeDimens(
    actionIconSize = 24.dp,

    topBarHeight = 44.dp,
    topBarPaddingHorizontal = 10.dp,
    topBarActionSpace = 16.dp,

    bigButtonWidth = 320.dp,
    bigButtonHeight = 40.dp,
    smallButtonWidth = 120.dp,
    smallButtonHeight = 40.dp,
    buttonRondCornerDp = 4.dp,
    warpButtonHorizontalPaddingDp = 10.dp,
    warpButtonVerticalPaddingDp = 6.dp,

    rowSingleHeight = 54.dp,
    rowDoubleHeight = 80.dp,
    rowPaddingHorizontal = 16.dp,
    rowInnerPaddingHorizontal = 8.dp,

    actionSheetRoundedCornerDp = 12.dp,

    bottomBarIconSize = 26.dp,
    bottomBarHeight = 52.dp,


    switchIconWidth = 50.dp,
    switchIconHeight = 30.dp,

    toastSize = 136.dp,
    toastIconSize = 40.dp,
    toastDividerHeight = 16.dp,

    outlineHeight = (0.5).dp,
    outlineSplitHeight = 8.dp,

    rowIconSize = 40.dp,
    rowIconRoundedCornerDp = 4.dp,

    inputLabelWidth = 60.dp,

    chatInputHeight = 56.dp,
    chatPadding = 10.dp,
    chatAvatarSize = 40.dp,
    chatAvatarRoundedCornerDp = 4.dp,

    categorySize = 20.dp,
)

internal val LocalWeDimens = staticCompositionLocalOf { WeDimensDefault }