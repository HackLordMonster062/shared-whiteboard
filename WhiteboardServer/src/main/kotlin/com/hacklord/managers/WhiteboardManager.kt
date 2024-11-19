package com.hacklord.managers

import com.hacklord.components.Line
import com.hacklord.components.OnlineUser
import com.hacklord.components.Whiteboard
import com.hacklord.routing.WhiteboardResponse
import com.hacklord.settings.ValidValues
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class WhiteboardManager(
    val info: Whiteboard,
) {
    private val connectedUsers: MutableList<OnlineUser> = mutableListOf()

    val whitelist = info.userWhitelist.toMutableSet()
    private val lines = info.lines.toMutableList()

    private var currId = info.currLineId

    private fun isUserConnected(userId: String): Boolean {
        return connectedUsers.any {user ->
            user.user.id == userId
        }
    }

    fun connectUser(user: OnlineUser) {
        connectedUsers.add(user)
    }

    fun disconnectUser(userId: String): Boolean {
        return connectedUsers.removeIf {onlineUser ->
            onlineUser.user.id == userId
        }
    }

    suspend fun broadcast(sender: String, response: WhiteboardResponse) {
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

    fun drawLine(line: Line): Long? {
        if (!ValidValues.lineWidth.contains(line.width)) {
            return null
        }

        if (!ValidValues.colors.contains(line.color)) {
            return null
        }

        val newLine = line.copy(id = currId++)

        lines.add(newLine)

        return newLine.id!!
    }

    fun eraseLine(lineId: Long): Boolean {
        val line = lines.find { it.id == lineId }

        if (line == null) {
            return false
        }

        lines.remove(line)

        return true
    }

    fun closeBoard(): Whiteboard {
        return info.copy(
            lines = lines,
            currLineId = currId,
            userWhitelist = whitelist
        )
    }
}