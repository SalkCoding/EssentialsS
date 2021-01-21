package com.salkcoding.essentialss.command.single

import br.com.devsrsouza.kotlinbukkitapi.extensions.player.clearAll
import com.salkcoding.essentialss.essentials
import com.salkcoding.essentialss.util.errorFormat
import com.salkcoding.essentialss.util.infoFormat
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CommandClearInventory : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        when (args.size) {
            0 -> {
                val player = sender as? Player
                if (player == null) {
                    essentials.logger.warning("Player only command")
                    return true
                }
                player.inventory.clearAll()
                player.sendMessage("인벤토리가 초기화되었습니다.".infoFormat())
            }
            1 -> {
                if (!sender.isOp) {
                    sender.sendMessage("권한이 없습니다.".errorFormat())
                    return true
                }

                val player = Bukkit.getPlayer(args[0])
                if (player == null) {
                    sender.sendMessage("존재하지 않는 플레이어입니다.")
                    return true
                }
                player.inventory.clearAll()
                player.sendMessage("인벤토리가 초기화되었습니다.".infoFormat())
                sender.sendMessage("해당 플레이어의 인벤토리가 초기화되었습니다.".infoFormat())
            }
        }
        return true
    }
}