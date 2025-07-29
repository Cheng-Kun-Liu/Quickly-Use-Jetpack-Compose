package com.laomuji1999.compose.core.logic.model.response

import kotlinx.serialization.Serializable

@Serializable
class UserInfoResponse {
    var page:Int? = null
    var per_page:Int? = null
    var total:Int? = null
    var total_pages:Int? = null
    var data:List<UserInfo>? = null

    @Serializable
    class UserInfo{
        var id:Int? = null
        var email:String? = null
        var first_name:String? = null
        var last_name:String? = null
        var avatar:String? = null
    }
}