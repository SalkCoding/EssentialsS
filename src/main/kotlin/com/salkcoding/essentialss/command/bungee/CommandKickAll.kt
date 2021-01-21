package com.salkcoding.essentialss.command.bungee

import com.salkcoding.essentialss.bungeeApi
import com.salkcoding.essentialss.essentials
import com.salkcoding.essentialss.util.errorFormat
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import java.util.concurrent.TimeUnit

class CommandKickAll : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (!sender.isOp) {
            sender.sendMessage("권한이 없습니다.".errorFormat())
            return true
        }

        when (args.size) {
            0 -> {
                Bukkit.getOnlinePlayers().filter {
                    !it.isOp
                }.forEach {
                    it.kickPlayer("관리자에의해 킥당하셨습니다.")
                }

                Bukkit.getScheduler().runTaskAsynchronously(essentials, Runnable {
                    val playerListFuture = bungeeApi.getPlayerList("ALL")
                    playerListFuture.get(10, TimeUnit.SECONDS).forEach {
                        val offlinePlayer = Bukkit.getOfflinePlayerIfCached(it)
                        if (offlinePlayer == null || !offlinePlayer.isOp) {
                            bungeeApi.kickPlayer(it, "관리자에의해 킥당하셨습니다.")
                        }
                    }
                })
            }
            else -> {
                val message = args.joinToString(" ")
                Bukkit.getOnlinePlayers().filter {
                    !it.isOp
                }.forEach {
                    it.kickPlayer(message)
                }

                Bukkit.getScheduler().runTaskAsynchronously(essentials, Runnable {
                    val playerListFuture = bungeeApi.getPlayerList("ALL")
                    playerListFuture.get(10, TimeUnit.SECONDS).forEach {
                        val offlinePlayer = Bukkit.getOfflinePlayerIfCached(it)
                        if (offlinePlayer == null || !offlinePlayer.isOp) {
                            bungeeApi.kickPlayer(it, message)
                        }
                    }
                })
            }
        }

        return false
    }
}