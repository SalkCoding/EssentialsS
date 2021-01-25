package com.salkcoding.essentialss.command.single

import com.salkcoding.essentialss.essentials
import com.salkcoding.essentialss.util.errorFormat
import com.salkcoding.essentialss.util.infoFormat
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerTeleportEvent

class CommandTpPos : CommandExecutor, TabCompleter {
    override fun onTabComplete(
        sender: CommandSender,
        cmd: Command,
        label: String,
        args: Array<out String>
    ): List<String?>? {
        if (label.equals("tppos", ignoreCase = true)) {
            when (args.size) {
                1 -> {
                    val parameterList = mutableListOf<String>()
                    Bukkit.getOnlinePlayers().filter {
                        args[0].startsWith(it.name)
                    }.forEach {
                        parameterList.add(it.name)
                    }
                    return parameterList
                }
                2, 3, 4, 5, 6 -> return listOf("0")
            }
        }
        return null
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        when (args.size) {
            3 -> {
                val player = sender as? Player
                if (player == null) {
                    essentials.logger.warning("Player only command")
                    return true
                }

                try {
                    val x = args[0].toDouble()
                    val y = args[1].toDouble()
                    val z = args[2].toDouble()
                    player.teleportAsync(Location(player.world, x, y, z), PlayerTeleportEvent.TeleportCause.COMMAND)
                    player.sendMessage("텔레포트 되었습니다.".infoFormat())
                } catch (e: NumberFormatException) {
                    return false
                }
                return true
            }
            4 -> {
                try {
                    val target = Bukkit.getPlayer(args[0])
                    if (target != null) {
                        val x = args[1].toDouble()
                        val y = args[2].toDouble()
                        val z = args[3].toDouble()
                        target.teleportAsync(Location(target.world, x, y, z), PlayerTeleportEvent.TeleportCause.COMMAND)
                        target.sendMessage("텔레포트 되었습니다.".infoFormat())
                    } else sender.sendMessage("존재하지 않는 플레이어입니다.".errorFormat())
                } catch (e: NumberFormatException) {
                    return false
                }
                return true
            }
            6 -> {
                try {
                    val target = Bukkit.getPlayer(args[0])
                    if (target != null) {
                        val x = args[1].toDouble()
                        val y = args[2].toDouble()
                        val z = args[3].toDouble()
                        val yaw = args[4].toFloat()
                        val pitch = args[5].toFloat()
                        target.teleportAsync(
                            Location(target.world, x, y, z, yaw, pitch),
                            PlayerTeleportEvent.TeleportCause.COMMAND
                        )
                        target.sendMessage("텔레포트 되었습니다.".infoFormat())
                    } else sender.sendMessage("존재하지 않는 플레이어입니다.".errorFormat())
                } catch (e: NumberFormatException) {
                    return false
                }
                return true
            }
            else -> return false
        }
    }
}