package com.laomuji1999.compose.core.ui.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.laomuji1999.compose.core.ui.theme.QuicklyTheme
import com.laomuji1999.compose.core.ui.we.widget.toast.WeToast
import com.laomuji1999.compose.core.ui.we.widget.toast.WeToastType
import com.laomuji1999.compose.res.R

/**
 * 加载弹窗
 * @author laomuji666
 * @since 2025/7/18
 */
@Composable
fun LoadingDialog(loading: Boolean) {
    if (!loading) {
        return
    }
    WeToast(
        weToastType = WeToastType.Loading,
        message = stringResource(R.string.string_toast_loading)
    )

}

@Preview
@Composable
fun PreviewLoadingDialog() {
    QuicklyTheme {
        LoadingDialog(loading = true)
    }
}