package com.salkcoding.essentialss.command.admin

import com.salkcoding.essentialss.EssentialsS
import com.salkcoding.essentialss.essentials
import com.salkcoding.essentialss.util.errorFormat
import com.salkcoding.essentialss.util.infoFormat
import fish.evatuna.metamorphosis.syncedmap.MetaSyncedMap
import net.luckperms.api.LuckPermsProvider
import net.luckperms.api.model.data.DataMutateResult
import net.luckperms.api.node.Node
import net.luckperms.api.node.types.PermissionNode
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalUnit
import java.util.*
import java.util.concurrent.TimeUnit


//val tpaTicketMap = MetaSyncedMap<UUID, ExpiredMilliseconds>(
//    "com.salkcoding.essentialss.tpa_ticket",
//    UUID::class.java,
//    ExpiredMilliseconds::class.java
//)

val tpaPermissionKey = "com.salk.coding.essentialss.timed_tpa"
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
                try {
                    val lp = LuckPermsProvider.get()
                    val user = lp.userManager.getUser(player.uniqueId) ?: throw IllegalStateException("User not found in luckperms")
                    val existingNode = user.nodes.firstOrNull { it.key == tpaPermissionKey && it.hasExpiry() && !it.hasExpired()}

                    val newPermNode: Node = if (existingNode == null) {
                        PermissionNode.builder(tpaPermissionKey).expiry(3, TimeUnit.HOURS).build()
                    } else {
                        val updatedExpiry = existingNode.expiry?.plus(3, ChronoUnit.HOURS)
                        if (updatedExpiry == null) {
                            PermissionNode.builder(tpaPermissionKey).expiry(3, TimeUnit.HOURS).build()
                        } else {
                            existingNode.toBuilder().expiry(updatedExpiry.epochSecond).build()
                        }
                    }
                    if (existingNode != null) {
                        user.data().remove(existingNode)
                    }
                    user.data().add(newPermNode)
                    lp.userManager.saveUser(user)

                    sender.sendMessage("지금부터 3시간 동안 Tpa 명령어 이용이 가능합니다.".infoFormat())
                } catch (e: IllegalStateException) {
                    sender.sendMessage("TPA권 사용 실패했습니다. 마일리지로 환불됩니다.".infoFormat())
                    essentials.server.dispatchCommand(essentials.server.consoleSender, "giveitemone ${player.name} misc MILEAGE")
                }
            }
        }
        return false
    }
}

data class ExpiredMilliseconds(val milliseconds: Long)