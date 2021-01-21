package com.salkcoding.essentialss.command.bungee

import com.salkcoding.essentialss.bungeeApi
import com.salkcoding.essentialss.essentials
import com.salkcoding.essentialss.util.errorFormat
import com.salkcoding.essentialss.util.infoFormat
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

class CommandList : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (!sender.isOp) {
            sender.sendMessage("권한이 없습니다.".errorFormat())
            return true
        }

        when (args.size) {
            0 -> {
                Bukkit.getScheduler().runTaskAsynchronously(essentials, Runnable {
                    val serverFuture = bungeeApi.servers
                    serverFuture.get(10, TimeUnit.SECONDS).forEach { server ->
                        val playerListFuture = bungeeApi.getPlayerList(server)
                        val playerList = playerListFuture.get(10, TimeUnit.SECONDS).joinToString(", ")
                        sender.sendMessage("${server}: $playerList".infoFormat())
                    }
                })
                return true
            }
            1 -> {
                val playerListFuture = bungeeApi.getPlayerList(args[0])
                val playerList = playerListFuture.get(10, TimeUnit.SECONDS).joinToString(", ")
                sender.sendMessage("${args[0]}: $playerList".infoFormat())
                return true
            }
        }

        return false
    }
}