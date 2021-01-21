package com.salkcoding.essentialss.command.bungee

import com.salkcoding.essentialss.essentials
import com.salkcoding.essentialss.util.errorFormat
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerTeleportEvent

class CommandTpHere : CommandExecutor {

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
            1 -> {
                val target = Bukkit.getPlayer(args[0])
                if (target != null) {
                    target.teleportAsync(player.location, PlayerTeleportEvent.TeleportCause.COMMAND)
                } else {
                    //TODO bungee
                }
                return true
            }
        }

        return false
    }
}