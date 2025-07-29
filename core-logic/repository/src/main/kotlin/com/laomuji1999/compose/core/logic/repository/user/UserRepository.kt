package com.laomuji1999.compose.core.logic.repository.user

import com.laomuji1999.compose.core.logic.network.http.HttpService
import com.laomuji1999.compose.core.logic.model.asHttpResult
import com.laomuji1999.compose.core.logic.model.request.CreateUserRequest
import com.laomuji1999.compose.core.logic.model.response.CreateUserResponse
import com.laomuji1999.compose.core.logic.model.response.UserInfoResponse
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    httpService: HttpService,
) {
    val client = httpService.client

    val isConnectedFlow = httpService.connectivityObserver.isConnectedFlow

    fun delayRequest() = flow {
        val response = client.get("https://reqres.in/api/users?delay=3") {
            header("x-api-key", "reqres-free-v1")
        }
        val body: UserInfoResponse = response.body()
        emit(Json.Default.encodeToString(body))
    }.asHttpResult()

    fun createUser(request: CreateUserRequest) = flow {
        val response = client.post("https://reqres.in/api/users") {
            header("x-api-key", "reqres-free-v1")
            setBody(request)
            contentType(ContentType.Application.Json)
        }
        val body: CreateUserResponse = response.body()
        emit(Json.Default.encodeToString(body))
    }.asHttpResult()
}