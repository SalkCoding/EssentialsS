package com.salkcoding.essentialss.command.single

import com.salkcoding.essentialss.essentials
import com.salkcoding.essentialss.util.errorFormat
import com.salkcoding.essentialss.util.infoFormat
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CommandFly : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val player = sender as? Player
        if (player == null) {
            essentials.logger.warning("Player only command")
            return true
        }

        if (!player.isOp) {
            player.sendMessage("권한이 없습니다.".errorFormat())
            return true
        }

        player.isFlying = !player.isFlying
        when (player.isFlying) {
            true -> {
                player.sendMessage("플라이가 활성화되었습니다.".infoFormat())
            }
            false -> {
                player.sendMessage("플라이가 비활성화되었습니다.".errorFormat())
            }
        }
        return true
    }
}