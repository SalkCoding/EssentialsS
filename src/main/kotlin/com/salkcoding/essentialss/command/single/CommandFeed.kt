package com.salkcoding.essentialss.command.single

import com.salkcoding.essentialss.essentials
import com.salkcoding.essentialss.exception.QuietAbortException
import com.salkcoding.essentialss.util.errorFormat
import com.salkcoding.essentialss.util.infoFormat
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.entity.FoodLevelChangeEvent

class CommandFeed : CommandExecutor {

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

        when (args.size) {
            0 -> {
                val event = FoodLevelChangeEvent(player, 20)
                essentials.server.pluginManager.callEvent(event)
                if (event.isCancelled) throw QuietAbortException("Food level changing cancelled!")

                player.foodLevel = 20
                player.sendMessage("허기가 채워졌습니다.".infoFormat())
            }
            1 -> {
                val targetPlayer = Bukkit.getPlayer(args[0])
                if (targetPlayer == null) {
                    player.sendMessage("존재하지 않는 플레이어입니다.".errorFormat())
                    return true
                }
                val event = FoodLevelChangeEvent(targetPlayer, 20)
                essentials.server.pluginManager.callEvent(event)
                if (event.isCancelled) throw QuietAbortException("Food level changing cancelled!")

                targetPlayer.foodLevel = 20
                targetPlayer.sendMessage("허기가 채워졌습니다.".infoFormat())
                player.sendMessage("해당 플레이어의 허기가 채워졌습니다.".infoFormat())
            }
        }
        return false
    }
}