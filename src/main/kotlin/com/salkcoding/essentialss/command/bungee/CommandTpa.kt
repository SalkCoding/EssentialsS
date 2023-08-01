package com.salkcoding.essentialss.command.bungee

import com.salkcoding.essentialss.bukkitLinkedAPI
import com.salkcoding.essentialss.command.admin.tpaTicketMap
import com.salkcoding.essentialss.essentials
import com.salkcoding.essentialss.util.errorFormat
import com.salkcoding.essentialss.util.infoFormat
import com.salkcoding.essentialss.util.warnFormat
import fish.evatuna.metamorphosis.syncedmap.MetaSyncedMap
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

//Key: invited user, value: sender
val tpaInviteMap = MetaSyncedMap<UUID, UUID>("com.salkcoding.essentialss.tpa", UUID::class.java, UUID::class.java)

class CommandTpa : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val player = sender as? Player
        if (player == null) {
            essentials.logger.warning("Player only command")
            return true
        }

        if (!sender.isOp && !tpaTicketMap.containsKey(player.uniqueId)) {
            sender.sendMessage("권한이 없습니다.".errorFormat())
            return true
        }

        when (args.size) {
            //send tpa
            1 -> {
                if (tpaTicketMap.containsKey(player.uniqueId)) {
                    val between = tpaTicketMap[sender.uniqueId]!! - System.currentTimeMillis()
                    if (between <= 0) {
                        tpaTicketMap.remove(sender.uniqueId)
                        return true
                    }
                }

                val targetUUID = UUID.fromString(args[0])
                if (tpaInviteMap.containsKey(targetUUID)) {
                    sender.sendMessage("해당 플레이어는 현재 다른 플레이어의 tpa를 기다리고 있습니다. 나중에 다시 시도해 주세요.".warnFormat())
                    return true
                }

                tpaInviteMap[targetUUID] = sender.uniqueId
                Bukkit.getScheduler().runTaskLater(
                    essentials,
                    Runnable {
                        if (tpaInviteMap.containsKey(targetUUID)) {
                            tpaInviteMap.remove(targetUUID)
                            bukkitLinkedAPI.sendMessageAcrossServer(targetUUID, "tpa 요청이 만료되었습니다.".warnFormat())
                            sender.sendMessage("해당 플레이어가 tpa에 응답하지 않습니다.".warnFormat())
                        }
                    }, 200L
                )
                sender.sendMessage("해당 플레이어에게 tpa를 요청하였습니다.".infoFormat())
            }
        }
        return false
    }
}