package org.hacklord.sharedcanvas.domain.event

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hacklord.sharedcanvas.components.Line

@Serializable
@Polymorphic
sealed interface WhiteboardRequest {
    @Serializable
    @SerialName("200")
    data class DrawLine(
        val line: Line
    ): WhiteboardRequest

    @Serializable
    @SerialName("201")
    data class EraseLine(
        val lineId: String
    ) : WhiteboardRequest

    @Serializable
    @SerialName("202")
    data object ExitBoard : WhiteboardRequest

    @Serializable
    @SerialName("220")
    data class AddUser(
        val username: String
    ) : WhiteboardRequest

    @Serializable
    @SerialName("221")
    data class RemoveUser(
        val username: String
    ) : WhiteboardRequest
}