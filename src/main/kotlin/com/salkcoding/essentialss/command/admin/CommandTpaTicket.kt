package com.salkcoding.essentialss.command.admin

import com.salkcoding.essentialss.essentials
import com.salkcoding.essentialss.util.errorFormat
import fish.evatuna.metamorphosis.syncedmap.MetaSyncedMap
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

val tpaTicketMap = MetaSyncedMap<UUID, Long>(
    "com.salkcoding.essentialss.tpa_ticket",
    UUID::class.java,
    Long::class.java
)

class CommandTpaTicket : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (!sender.isOp) {
            sender.sendMessage("권한이 없습니다.".errorFormat())
            return true
        }

        when (args.size) {
            0 -> {
                val player = sender as? Player
                if (player == null) {
                    essentials.logger.warning("Player only command")
                    return true
                }
                val expired = Calendar.getInstance().apply {
                    add(Calendar.HOUR, 3)
                }.timeInMillis
                tpaTicketMap[sender.uniqueId] = expired
            }
        }
        return false
    }
}