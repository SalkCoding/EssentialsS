package com.salkcoding.essentialss.command.bungee

import com.salkcoding.essentialss.bukkitLinkedAPI
import com.salkcoding.essentialss.essentials
import com.salkcoding.essentialss.util.errorFormat
import com.salkcoding.essentialss.util.infoFormat
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CommandTp : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (!sender.isOp) {
            sender.sendMessage("권한이 없습니다.".errorFormat())
            return true
        }

        when (args.size) {
            1 -> {
                val player = sender as? Player
                if (player == null) {
                    essentials.logger.warning("Player only command")
                    return true
                }
                val targetName = args[0]
                val targetPlayer = Bukkit.getPlayer(targetName)
                if (targetPlayer != null) {
                    player.teleportAsync(targetPlayer.location)
                } else {
                    player.sendMessage("이동되었습니다.".infoFormat())
                    bukkitLinkedAPI.sendMessageAcrossServer(targetName, "이동되었습니다.".infoFormat())
                    bukkitLinkedAPI.teleport(player.name, targetName)
                    return true
                }
            }
            2 -> {
                val fromName = args[0]
                val toName = args[1]
                val fromPlayer = Bukkit.getPlayer(fromName)
                val toPlayer = Bukkit.getPlayer(toName)
                when {
                    //Just teleport
                    fromPlayer != null && toPlayer != null -> {
                        fromPlayer.sendMessage("이동되었습니다.".infoFormat())
                        toPlayer.sendMessage("이동되었습니다.".infoFormat())
                        fromPlayer.teleportAsync(toPlayer.location)
                        return true
                    }
                    //Have to use bungee
                    else -> {
                        bukkitLinkedAPI.sendMessageAcrossServer(fromName, "이동되었습니다.".infoFormat())
                        bukkitLinkedAPI.sendMessageAcrossServer(toName, "이동되었습니다.".infoFormat())
                        bukkitLinkedAPI.teleport(fromName, toName)
                    }
                }
            }
        }
        return false
    }
}