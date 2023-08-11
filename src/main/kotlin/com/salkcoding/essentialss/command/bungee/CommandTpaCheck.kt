package com.salkcoding.essentialss.command.bungee

import com.salkcoding.essentialss.command.admin.tpaTicketMap
import com.salkcoding.essentialss.util.infoFormat
import com.salkcoding.essentialss.util.warnFormat
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CommandTpaCheck : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("Player only command")
            return true
        }

        if (sender.uniqueId !in tpaTicketMap) {
            sender.sendMessage("현재 tpa를 사용할 수 없습니다.".warnFormat())
            return true
        }

        val expiredMilliseconds = tpaTicketMap[sender.uniqueId]!!
        val delta = expiredMilliseconds.milliseconds - System.currentTimeMillis()
        if (delta <= 0) {
            sender.sendMessage("Tpa 사용 가능 시간이 만료되었습니다.".warnFormat())
            return true
        }

        val hours = (delta / 3600000) % 24
        val minutes = (delta / 60000) % 60
        val seconds = (delta / 1000) % 60

        sender.sendMessage(
            "남은 Tpa 사용 가능 시간: ${
                when {
                    hours > 0 -> "${hours}시간 ${minutes}분 ${seconds}초"
                    minutes > 0 -> "${minutes}분 ${seconds}초"
                    seconds > 0 -> "${seconds}초"
                    else -> "만료됨"
                }
            }".infoFormat()
        )

        return true
    }
}