package com.laomuji1999.compose.feature.explore.http

sealed interface HttpScreenAction {
    data object GetListUsers : HttpScreenAction
    data object CreateUser : HttpScreenAction
    data object LoadMore : HttpScreenAction
}
