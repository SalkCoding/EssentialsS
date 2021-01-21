package com.salkcoding.essentialss.command.bungee

import com.salkcoding.essentialss.essentials
import com.salkcoding.essentialss.util.errorFormat
import com.salkcoding.essentialss.util.infoFormat
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CommandTpAll : CommandExecutor {

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

        Bukkit.getOnlinePlayers().forEach {
            if (it.uniqueId != player.uniqueId) {
                it.teleportAsync(player.location)
                it.sendMessage("텔레포트 되었습니다.".infoFormat())
            }
        }

        //TODO Bungee

        player.sendMessage("모든 서버의 유저를 텔레포트 시켰습니다.".infoFormat())
        return true
    }
}