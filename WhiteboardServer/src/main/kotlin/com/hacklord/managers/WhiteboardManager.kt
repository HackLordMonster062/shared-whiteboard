package com.hacklord.managers

import com.hacklord.components.Line
import com.hacklord.components.OnlineUser
import com.hacklord.components.Whiteboard
import com.hacklord.routing.WhiteboardBroadcast
import com.hacklord.settings.ValidValues
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*

class WhiteboardManager(
    val info: Whiteboard,
) {
    val connectedUsers: MutableList<OnlineUser> = mutableListOf()

    val whitelist = info.userWhitelist.toMutableSet()
    private val lines = info.lines.toMutableList()

    fun connectUser(user: OnlineUser) {
        connectedUsers.add(user)
    }

    fun disconnectUser(userId: String): Boolean {
        return connectedUsers.removeIf {onlineUser ->
            onlineUser.user.id == userId
        }
    }

    suspend fun broadcast(sender: String, response: WhiteboardBroadcast) {
        if (connectedUsers.any { it.user.id == sender }) {
            connectedUsers.forEach { user ->
                if (user.user.id != sender) {
                    user.session.send(
                        Frame.Text(
                        Json.encodeToString(response)
                    ))
                }
            }
        }
    }

    fun addUser(userId: String) {
        whitelist.add(userId)
    }

    fun removeUser(userId: String) {
        whitelist.remove(userId)
    }

    fun drawLine(line: Line) {
        if (!ValidValues.lineWidth.contains(line.width)) {
            return
        }

        if (!ValidValues.colors.contains(line.color)) {
            return
        }

        lines.add(line)
    }

    fun eraseLine(lineId: UUID): Boolean {
        val line = lines.find { it.id == lineId.toString() }

        if (line == null) {
            return false
        }

        lines.remove(line)

        return true
    }

    fun closeBoard(): Whiteboard {
        return info.copy(
            lines = lines,
            userWhitelist = whitelist
        )
    }
}