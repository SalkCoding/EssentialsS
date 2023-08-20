package com.salkcoding.essentialss.command.bungee

import com.salkcoding.essentialss.bukkitLinkedAPI
import com.salkcoding.essentialss.essentials
import com.salkcoding.essentialss.util.errorFormat
import com.salkcoding.essentialss.util.infoFormat
import fish.evatuna.metamorphosis.syncedmap.MetaSyncedMap
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

val tpAcceptMap = MetaSyncedMap<UUID, UUID>("com.salkcoding.essentialss.tpaccept", UUID::class.java, UUID::class.java)

class CommandTpAccept : CommandExecutor {

    override fun onCommand(acceptor: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val player = acceptor as? Player
        if (player == null) {
            essentials.logger.warning("Player only command")
            return true
        }

        if (!tpaInviteMap.containsKey(acceptor.uniqueId)) {
            acceptor.sendMessage("받은 tpa 요청이 없습니다.".errorFormat())
            return true
        }

        acceptor.sendMessage("요청을 승낙하여, tpa를 요청한 사람이 텔레포트 됩니다.".infoFormat())
        val sender = tpaInviteMap[acceptor.uniqueId]!!
        bukkitLinkedAPI.sendMessageAcrossServer(
            sender,
            "상대방이 tpa를 ${ChatColor.GREEN}승낙${ChatColor.WHITE}하였습니다.".infoFormat()
        )
        tpAcceptMap[sender] = acceptor.uniqueId
        return true
    }
}