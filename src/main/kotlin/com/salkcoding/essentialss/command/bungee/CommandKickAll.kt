package com.salkcoding.essentialss.command.bungee

import com.salkcoding.essentialss.bukkitLinkedAPI
import com.salkcoding.essentialss.bungeeApi
import com.salkcoding.essentialss.util.errorFormat
import com.salkcoding.essentialss.util.infoFormat
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

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

                bukkitLinkedAPI.onlinePlayersInfo.forEach {
                    if (!it.isOp)
                        bungeeApi.kickPlayer(it.playerName, "관리자에의해 킥당하셨습니다.")
                }
                sender.sendMessage("모든 플레이어를 킥했습니다.".infoFormat())
            }
            else -> {
                val message = ChatColor.translateAlternateColorCodes('&', args.joinToString(" "))
                Bukkit.getOnlinePlayers().filter {
                    !it.isOp
                }.forEach {
                    it.kickPlayer(message)
                }

                bukkitLinkedAPI.onlinePlayersInfo.forEach {
                    if (!it.isOp)
                        bungeeApi.kickPlayer(it.playerName, message)
                }
                sender.sendMessage("모든 플레이어를 킥했습니다.".infoFormat())
            }
        }

        return false
    }
}