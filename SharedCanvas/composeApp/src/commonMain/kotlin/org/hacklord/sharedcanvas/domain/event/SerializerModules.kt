package org.hacklord.sharedcanvas.domain.event

import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

val lobbyRequestModule = SerializersModule {
    polymorphic(LobbyRequest::class) {
        subclass(LobbyRequest.GetWhiteboards::class, LobbyRequest.GetWhiteboards.serializer())
        subclass(LobbyRequest.EnterWhiteboard::class, LobbyRequest.EnterWhiteboard.serializer())
        subclass(LobbyRequest.CreateWhiteboard::class, LobbyRequest.CreateWhiteboard.serializer())
        subclass(LobbyRequest.DeleteWhiteboard::class, LobbyRequest.DeleteWhiteboard.serializer())
    }
}

val whiteboardRequestModule = SerializersModule {
    polymorphic(WhiteboardRequest::class) {
        subclass(WhiteboardRequest.DrawLine::class, WhiteboardRequest.DrawLine.serializer())
        subclass(WhiteboardRequest.EraseLine::class, WhiteboardRequest.EraseLine.serializer())
        subclass(WhiteboardRequest.AddUser::class, WhiteboardRequest.AddUser.serializer())
        subclass(WhiteboardRequest.RemoveUser::class, WhiteboardRequest.RemoveUser.serializer())
        subclass(WhiteboardRequest.ExitBoard::class, WhiteboardRequest.ExitBoard.serializer())
    }
}