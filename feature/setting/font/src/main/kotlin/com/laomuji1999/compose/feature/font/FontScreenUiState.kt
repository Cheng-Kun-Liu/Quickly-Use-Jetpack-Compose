package com.laomuji1999.compose.feature.font

import com.laomuji1999.compose.core.ui.we.typography.WeTypography

data class FontScreenUiState(
    val currentWeTypography: WeTypography? = null,
    val currentIndex: Int = -1,
    val totalSize: Int = 0,
)