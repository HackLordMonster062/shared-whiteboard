package com.hacklord.managers

import com.hacklord.components.Line
import com.hacklord.components.OnlineUser
import com.hacklord.components.User
import com.hacklord.components.Whiteboard
import com.hacklord.settings.ValidValues

class WhiteboardManager(
    val info: Whiteboard,
) {
    private val connectedUsers: MutableList<OnlineUser> = mutableListOf()

    private val whitelist = info.userWhitelist.toMutableSet()
    private val lines = info.lines.toMutableList()

    private var currId = info.currLineId

    private fun isUserConnected(userId: Long): Boolean {
        return connectedUsers.any {user ->
            user.user.id == userId
        }
    }

    fun connectUser(user: User): OnlineUser? {
        if (!whitelist.contains(user.id) || isUserConnected(user.id)) {
            return null
        }

        val onlineUser = OnlineUser(
            user,
            info.id
        )

        connectedUsers.add(onlineUser)

        return onlineUser
    }

    fun disconnectUser(userId: Long): Boolean {
        return connectedUsers.removeIf {onlineUser ->
            onlineUser.user.id == userId
        }
    }

    fun addUser(userId: Long) {
        whitelist.add(userId)
    }

    fun removeUser(userId: Long) {
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