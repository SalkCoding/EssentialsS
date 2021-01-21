package com.salkcoding.essentialss.command.single

import com.salkcoding.essentialss.essentials
import com.salkcoding.essentialss.util.errorFormat
import com.salkcoding.essentialss.util.infoFormat
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.HoverEvent
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.EntityType
import org.bukkit.entity.Item
import org.bukkit.entity.Player
import net.md_5.bungee.api.chat.TextComponent

class CommandNear : CommandExecutor {

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
            1 -> {
                val distance = args[0].toDoubleOrNull() ?: return false
                player.sendMessage("===================================".infoFormat())
                player.getNearbyEntities(distance, distance, distance).forEach {
                    when (it.type) {
                        EntityType.PLAYER -> {
                            val otherPlayer = it as Player
                            val location = it.location
                            val component =
                                TextComponent("${it.type}, x: ${location.blockX}, y: ${location.blockY}, z: ${location.blockZ}")
                            component.hoverEvent = HoverEvent(
                                HoverEvent.Action.SHOW_TEXT,
                                ComponentBuilder("name: ${otherPlayer.name}, health: ${otherPlayer.health}/${otherPlayer.healthScale}").create()
                            )
                            player.sendMessage(component)
                        }
                        EntityType.DROPPED_ITEM -> {
                            val item = it as Item
                            val itemStack = item.itemStack
                            val location = it.location
                            val builder =
                                StringBuilder("type: ${itemStack.type}, displayName: ${itemStack.displayName}\n")
                            builder.append("lore:\n")
                            itemStack.lore?.forEach { line ->
                                builder.append("     $line\n")
                            }
                            builder.append("enchantments:\n")
                            itemStack.enchantments.forEach { enchantment ->
                                builder.append("     ${enchantment.key}, ${enchantment.value + 1}\n")
                            }
                            builder.append("amount: ${itemStack.amount}/${itemStack.maxStackSize}")

                            val component =
                                TextComponent("${it.type}, x: ${location.blockX}, y: ${location.blockY}, z: ${location.blockZ}")
                            component.hoverEvent =
                                HoverEvent(HoverEvent.Action.SHOW_TEXT, ComponentBuilder(builder.toString()).create())
                            player.sendMessage(component)
                        }
                        else -> {
                            val location = it.location
                            player.sendMessage("${it.type}, x: ${location.blockX}, y: ${location.blockY}, z: ${location.blockZ}")
                        }
                    }
                }
                player.sendMessage("===================================".infoFormat())
            }
        }

        return false
    }
}