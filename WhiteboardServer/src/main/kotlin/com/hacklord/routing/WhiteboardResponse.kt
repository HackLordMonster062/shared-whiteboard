package com.hacklord.routing

import com.hacklord.components.User
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Polymorphic
sealed interface WhiteboardResponse {
    @Serializable
    @SerialName("200")
    data object DrawLine : WhiteboardResponse

    @Serializable
    @SerialName("201")
    data object EraseLine : WhiteboardResponse

    @Serializable
    @SerialName("202")
    data object ExitBoard : WhiteboardResponse

    @Serializable
    @SerialName("203")
    data class GetAllUsers(
        val users: List<User>
    ) : WhiteboardResponse

    @Serializable
    @SerialName("-1")
    data class Error(
        val message: String
    ) : WhiteboardResponse
}