package com.salkcoding.essentialss.command.single

import com.salkcoding.essentialss.essentials
import com.salkcoding.essentialss.util.errorFormat
import com.salkcoding.essentialss.util.infoFormat
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerTeleportEvent

class CommandTop : CommandExecutor {

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

        if (args.isEmpty()) {
            if (player.world.environment == World.Environment.NETHER) {
                val location = player.location.toCenterLocation()
                for (y in 120 downTo 30) {
                    location.y = y.toDouble()
                    if (location.block.type != Material.AIR)
                        break
                }
                player.teleportAsync(
                    location,
                    PlayerTeleportEvent.TeleportCause.COMMAND
                )
                player.sendMessage("이동되었습니다.".infoFormat())
            } else player.sendMessage("일반 월드만 가능한 명령어입니다.".infoFormat())
            return true
        }
        return false
    }
}