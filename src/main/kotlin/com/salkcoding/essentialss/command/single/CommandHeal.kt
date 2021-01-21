package com.salkcoding.essentialss.command.single

import com.salkcoding.essentialss.essentials
import com.salkcoding.essentialss.exception.QuietAbortException
import com.salkcoding.essentialss.util.errorFormat
import com.salkcoding.essentialss.util.infoFormat
import org.bukkit.Bukkit
import org.bukkit.attribute.Attribute
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityRegainHealthEvent

class CommandHeal : CommandExecutor {

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

        when (args.size) {
            0 -> {
                val maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value
                val deltaHealth = maxHealth - player.health

                val event = EntityRegainHealthEvent(player, deltaHealth, EntityRegainHealthEvent.RegainReason.CUSTOM)
                essentials.server.pluginManager.callEvent(event)
                if (event.isCancelled) throw QuietAbortException("Heal cancelled!")

                player.health = maxHealth
                player.activePotionEffects.forEach {
                    player.removePotionEffect(it.type)
                }
                player.fireTicks = 0
                player.foodLevel = 20
                player.sendMessage("회복되었습니다.".infoFormat())
                return true
            }
            1 -> {
                val targetPlayer = Bukkit.getPlayer(args[0])
                if (targetPlayer == null) {
                    player.sendMessage("존재하지 않는 플레이어입니다.".errorFormat())
                    return true
                }
                val maxHealth = targetPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value
                val deltaHealth = maxHealth - targetPlayer.health

                val event =
                    EntityRegainHealthEvent(targetPlayer, deltaHealth, EntityRegainHealthEvent.RegainReason.CUSTOM)
                essentials.server.pluginManager.callEvent(event)
                if (event.isCancelled) throw QuietAbortException("Heal cancelled!")

                targetPlayer.health = maxHealth
                targetPlayer.activePotionEffects.forEach {
                    targetPlayer.removePotionEffect(it.type)
                }
                targetPlayer.fireTicks = 0
                targetPlayer.foodLevel = 20
                targetPlayer.sendMessage("회복되었습니다.".infoFormat())
                player.sendMessage("해당 플레이어가 회복되었습니다.".infoFormat())
                return true
            }
        }
        return false
    }
}