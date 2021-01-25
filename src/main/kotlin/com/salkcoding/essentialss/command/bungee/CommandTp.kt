package com.salkcoding.essentialss.command.bungee

import com.salkcoding.essentialss.bukkitLinkedAPI
import com.salkcoding.essentialss.bungeeApi
import com.salkcoding.essentialss.essentials
import com.salkcoding.essentialss.util.errorFormat
import com.salkcoding.essentialss.util.infoFormat
import com.salkcoding.essentialss.util.teleport
import me.baiks.bukkitlinked.api.TeleportResult
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException

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
                    bukkitLinkedAPI.teleport(player, targetName)
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
                        fromPlayer.teleportAsync(toPlayer.location)
                        fromPlayer.sendMessage("이동되었습니다.".infoFormat())
                        toPlayer.sendMessage("이동되었습니다.".infoFormat())
                        return true
                    }
                    //Have to use bungee
                    fromPlayer != null && toPlayer == null -> {
                        bukkitLinkedAPI.teleport(fromPlayer, toName)
                    }
                    else -> {
                        val onlinePlayerList = bukkitLinkedAPI.onlinePlayersInfo
                        val fromPlayerInfo = onlinePlayerList.find { it.playerName == fromName }
                        val toPlayerInfo = onlinePlayerList.find { it.playerName == toName }
                        if (fromPlayerInfo == null || toPlayerInfo == null) {
                            sender.sendMessage("존재하지 않은 플레이어입니다.".errorFormat())
                            return true
                        }

                        val messageBytes = ByteArrayOutputStream()
                        val messageOut = DataOutputStream(messageBytes)
                        try {
                            messageOut.writeUTF(fromName)
                            messageOut.writeUTF(toName)
                        } catch (exception: IOException) {
                            exception.printStackTrace()
                        }
                        bungeeApi.forward(fromPlayerInfo.serverName, "essentials-tp", messageBytes.toByteArray())
                    }
                }
            }
        }
        return false
    }
}