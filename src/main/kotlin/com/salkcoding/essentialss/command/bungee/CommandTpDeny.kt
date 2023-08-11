package com.salkcoding.essentialss.command.bungee

import com.salkcoding.essentialss.bukkitLinkedAPI
import com.salkcoding.essentialss.essentials
import com.salkcoding.essentialss.util.errorFormat
import com.salkcoding.essentialss.util.infoFormat
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CommandTpDeny : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val player = sender as? Player
        if (player == null) {
            essentials.logger.warning("Player only command")
            return true
        }

        if (!tpaInviteMap.containsKey(sender.uniqueId)) {
            sender.sendMessage("받은 tpa 요청이 없습니다.".errorFormat())
            return true
        }

        sender.sendMessage("요청을 거절하였습니다.".infoFormat())
        val from = tpaInviteMap.remove(sender.uniqueId)!!
        bukkitLinkedAPI.sendMessageAcrossServer(from,"상대방이 tpa를 ${ChatColor.RED}거절${ChatColor.WHITE}하였습니다.".infoFormat())
        return true
    }
}