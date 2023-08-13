package com.salkcoding.essentialss.command.bungee

import com.salkcoding.essentialss.command.admin.tpaPermissionKey
import com.salkcoding.essentialss.util.infoFormat
import com.salkcoding.essentialss.util.warnFormat
import net.luckperms.api.LuckPermsProvider
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

        val lp = LuckPermsProvider.get()
        val user = lp.userManager.getUser(sender.uniqueId) ?: throw IllegalStateException("User not found in luckperms")
        val tpaPermission = user.nodes.firstOrNull { it.key == tpaPermissionKey && it.hasExpiry() && !it.hasExpired()}

        if (tpaPermission == null) {
            sender.sendMessage("현재 사용 가능한 TPA권이 없습니다.".warnFormat())
            return true
        }

        val delta = tpaPermission.expiryDuration!!.toMillis()

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