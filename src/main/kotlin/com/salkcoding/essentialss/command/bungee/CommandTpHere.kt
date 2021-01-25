package com.salkcoding.essentialss.command.bungee

import com.salkcoding.essentialss.bukkitLinkedAPI
import com.salkcoding.essentialss.essentials
import com.salkcoding.essentialss.util.errorFormat
import me.baiks.bukkitlinked.api.TeleportResult
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
                val targetName = args[0]
                val target = Bukkit.getPlayer(targetName)
                if (target != null) {
                    target.teleportAsync(player.location, PlayerTeleportEvent.TeleportCause.COMMAND)
                } else {
                    val fromPlayerInfo = bukkitLinkedAPI.onlinePlayersInfo.find { it.playerName == targetName }
                    if (fromPlayerInfo != null) {
                        val result = bukkitLinkedAPI.teleport(fromPlayerInfo.playerUUID, player.uniqueId)
                        if (result != TeleportResult.TELEPORT_STARTED) {
                            essentials.logger.info("Teleport succeeded, ${fromPlayerInfo.playerName}(${fromPlayerInfo.playerUUID}) -> ${player.name}(${player.uniqueId}), Result: $result")
                        } else {
                            player.sendMessage("Teleport failed, Result: $result".errorFormat())
                            essentials.logger.warning("Teleport failed, ${fromPlayerInfo.playerName}(${fromPlayerInfo.playerUUID}) -> ${player.name}(${player.uniqueId}), Result: $result")
                        }
                    }
                }
                return true
            }
        }

        return false
    }
}