package com.salkcoding.essentialss.command.single

import com.salkcoding.essentialss.essentials
import com.salkcoding.essentialss.util.errorFormat
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.meta.Damageable
import org.bukkit.inventory.meta.ItemMeta

class CommandRepair : CommandExecutor {

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

        val itemOnMain = player.inventory.itemInMainHand
        if (itemOnMain.type != Material.AIR) {
            val meta = itemOnMain.itemMeta as? Damageable
            if (meta == null) {
                player.sendMessage("고칠 수 없는 아이템입니다.".errorFormat())
                return true
            }
            meta.damage = 0
            itemOnMain.itemMeta = meta as ItemMeta
            player.updateInventory()
            return true
        } else {
            val itemOnOff = player.inventory.itemInOffHand
            if (itemOnOff.type != Material.AIR) {
                val meta = itemOnOff.itemMeta as? Damageable
                if (meta == null) {
                    player.sendMessage("고칠 수 없는 아이템입니다.".errorFormat())
                    return true
                }
                meta.damage = 0
                itemOnOff.itemMeta = meta as ItemMeta
                player.updateInventory()
                return true
            } else
                player.sendMessage("손에 아이템을 들고 있어야합니다.".errorFormat())
        }
        return true
    }
}