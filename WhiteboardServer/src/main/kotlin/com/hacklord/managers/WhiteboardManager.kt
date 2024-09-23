package com.hacklord.managers

import com.hacklord.components.Line
import com.hacklord.components.OnlineUser
import com.hacklord.components.User
import com.hacklord.components.Whiteboard
import com.hacklord.settings.ValidValues

class WhiteboardManager(
    private val info: Whiteboard,
) {
    private val connectedUsers: MutableList<OnlineUser> = mutableListOf()
    private val lines = info.lines.toMutableList()

    private var currId = info.currLineId

    fun connectUser(user: User): OnlineUser? {
        if (!info.users.contains(user)) {
            return null
        }

        val onlineUser = OnlineUser(
            user,
            info.id
        )

        connectedUsers.add(onlineUser)

        return onlineUser
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
            // TODO: add new user list (add/remove users' access)
        )
    }
}