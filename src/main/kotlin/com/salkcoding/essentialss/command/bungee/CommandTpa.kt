package com.salkcoding.essentialss.command.bungee

import com.salkcoding.essentialss.bukkitLinkedAPI
import com.salkcoding.essentialss.command.admin.tpaTicketMap
import com.salkcoding.essentialss.essentials
import com.salkcoding.essentialss.tpaLimitWorldName
import com.salkcoding.essentialss.util.TeleportCooltime
import com.salkcoding.essentialss.util.errorFormat
import com.salkcoding.essentialss.util.infoFormat
import com.salkcoding.essentialss.util.warnFormat
import fish.evatuna.metamorphosis.syncedmap.MetaSyncedMap
import me.baiks.bukkitlinked.models.PlayerInfo
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask
import java.util.*

//Key: invited user, value: sender
val tpaInviteMap = MetaSyncedMap<UUID, UUID>("com.salkcoding.essentialss.tpa", UUID::class.java, UUID::class.java)

val taskAcceptMap: HashMap<UUID, BukkitTask> = hashMapOf()

class CommandTpa : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val player = sender as? Player
        if (player == null) {
            essentials.logger.warning("Player only command")
            return true
        }

        if (!sender.isOp && player.uniqueId !in tpaTicketMap) {
            sender.sendMessage("권한이 없습니다.".errorFormat())
            return true
        }

        when (args.size) {
            //send tpa
            1 -> {
                val between = tpaTicketMap[sender.uniqueId]!!.milliseconds - System.currentTimeMillis()
                if (between <= 0) {
                    player.sendMessage("Tpa 시간이 만료되었습니다.".warnFormat())
                    tpaTicketMap.remove(sender.uniqueId)
                    return true
                }

                val info: PlayerInfo? = bukkitLinkedAPI.getPlayerInfo(args[0])
                if (info == null) {
                    sender.sendMessage("${args[0]}이라는 이름을 가진 유저가 없습니다.".errorFormat())
                    return true
                }

                if (info.world in tpaLimitWorldName) {
                    sender.sendMessage("해당 유저가 tpa를 받을 수 없는 월드에 있습니다.".errorFormat())
                    return true
                }

                val targetUUID = info.playerUUID
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
                            sender.sendMessage("해당 플레이어가 tpa에 응답하지 않습니다.".warnFormat())
                            bukkitLinkedAPI.sendMessageAcrossServer(targetUUID, "tpa 요청이 만료되었습니다.".warnFormat())
                        }
                    }, 200L
                )

                sender.sendMessage("해당 플레이어에게 tpa를 요청하였습니다.".infoFormat())
                bukkitLinkedAPI.sendMessageAcrossServer(targetUUID, "${sender.name}님이 tpa를 요청하였습니다.".infoFormat())
                bukkitLinkedAPI.sendMessageAcrossServer(
                    targetUUID,
                    "승낙하려면 ${ChatColor.GREEN}/tpaccept${ChatColor.WHITE}, 거절하려면 ${ChatColor.RED}/tpdeny${ChatColor.WHITE}을 입력하세요."
                )
                //Teleport accept cooldown
                taskAcceptMap[sender.uniqueId] = Bukkit.getScheduler().runTaskTimer(
                    essentials,
                    Runnable {
                        if (tpAcceptMap.containsKey(sender.uniqueId)) {
                            TeleportCooltime.addPlayer(sender, null, 100, Runnable {
                                bukkitLinkedAPI.teleport(sender.uniqueId, targetUUID)
                            }, false)
                            tpAcceptMap.remove(sender.uniqueId)
                            taskAcceptMap.remove(sender.uniqueId)?.cancel()
                        }
                    }, 5, 5
                )
            }
        }
        return false
    }
}