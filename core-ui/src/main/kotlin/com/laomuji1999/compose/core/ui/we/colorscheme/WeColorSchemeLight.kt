package com.laomuji1999.compose.core.ui.we.colorscheme

/**
 * 白天风格颜色
 * @author laomuji666
 * @since 2025/5/23
 */
data object WeColorSchemeLight : WeColorScheme(
    isDarkFont = true,

    background = WeColors.Color_EDEDED,
    cursorColor = WeColors.Color_07C160,


    fontColorHeavy = WeColors.Color_000000_90,
    fontColorLight = WeColors.Color_000000_50,
    fontColorError = WeColors.Color_FA5151,
    fontColorPrimary = WeColors.Color_07C160,

    primaryButton = WeColors.Color_07C160,
    onPrimaryButton = WeColors.Color_FFFFFFFF,
    secondaryButton = WeColors.Color_F7F7F7,
    onSecondaryButton = WeColors.Color_07C160,
    disableButton = WeColors.Color_F7F7F7,
    onDisableButton = WeColors.Color_000000_10,
    wrongButton = WeColors.Color_F7F7F7,
    onWrongButton = WeColors.Color_FA5151,


    outline = WeColors.Color_000000_10,
    pressed = WeColors.Color_000000_05,

    bottomBarBackground = WeColors.Color_EDEDED,
    bottomBarSelect = WeColors.Color_07C160,
    bottomBarUnSelect = WeColors.Color_000000_90,
    toastBackgroundColor = WeColors.Color_4C4C4C_90,
    onToastBackgroundColor = WeColors.Color_FFFFFF_90,

    switchThumbColor = WeColors.Color_FFFFFFFF,
    switchSelectBackground = WeColors.Color_07C160,
    switchUnSelectBackground = WeColors.Color_000000_10,


    rowBackground = WeColors.Color_FFFFFFFF,


    chatInputBackground = WeColors.Color_EDEDED,
    chatMessageBackgroundSend = WeColors.Color_95EC6A,
    chatMessageBackgroundReceive = WeColors.Color_FFFFFFFF,
    chatMessageTextSend = WeColors.Color_000000_90,
    chatMessageTextReceive = WeColors.Color_000000_90,

    categoryTextColor = WeColors.Color_FFFFFF_90,
    categoryBackground = WeColors.Color_07C160,
)