package com.salkcoding.essentialss.command.bungee

import com.salkcoding.essentialss.essentials
import com.salkcoding.essentialss.util.errorFormat
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

                val targetPlayer = Bukkit.getPlayer(args[0])
                if (targetPlayer != null) {
                    player.teleportAsync(targetPlayer.location)
                } else {
                    //TODO Bungee
                    player.sendMessage("존재하지 않는 플레이어입니다.".errorFormat())
                    return true
                }
            }
            2 -> {
                //TODO bungee(Have to consider...)
                val fromPlayer = Bukkit.getPlayer(args[0])
                if (fromPlayer == null) {
                    sender.sendMessage("존재하지 않는 플레이어입니다.".errorFormat())
                    return true
                }

                val toPlayer = Bukkit.getPlayer(args[1])
                if (toPlayer == null) {
                    sender.sendMessage("존재하지 않는 플레이어입니다.".errorFormat())
                    return true
                }

                fromPlayer.teleportAsync(toPlayer.location)
            }
        }
        return false
    }
}

/*
        Bukkit.getScheduler().runTaskAsynchronously(essentials, Runnable {
            val messageBytes = ByteArrayOutputStream()
            val messageOut = DataOutputStream(messageBytes)
            try {
                messageOut.writeUTF(player.uniqueId.toString())
                messageOut.writeUTF(player.name)
                messageOut.writeUTF(tunaLands.serverName)
            } catch (exception: IOException) {
                exception.printStackTrace()
            }

            bungeeApi.forward("ALL", "tunalands-spawn", messageBytes.toByteArray())
        })
*/