package com.salkcoding.essentialss.command.single

import com.salkcoding.essentialss.essentials
import com.salkcoding.essentialss.util.errorFormat
import com.salkcoding.essentialss.util.infoFormat
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player

class CommandKillAll : CommandExecutor, TabCompleter {

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
                var deleted = 0
                player.world.entities.forEach {
                    if (it.type != EntityType.PLAYER) {
                        it.remove()
                        deleted++
                    }
                }
                player.sendMessage("${deleted}마리의 엔티티가 제거되었습니다.".infoFormat())
                return true
            }
            1 -> {
                try {
                    val type = EntityType.valueOf(args[0].toUpperCase())
                    player.sendMessage("${deleteEntities(player.location, type)}마리의 ${type}이/가 제거되엇습니다.")
                } catch (e: IllegalArgumentException) {
                    return false
                }
                return true
            }
            2 -> {
                try {
                    val type = EntityType.valueOf(args[0].toUpperCase())
                    val range = args[1].toDouble()
                    player.sendMessage("${deleteEntities(player.location, type, range)}마리의 ${type}이/가 제거되엇습니다.")
                } catch (e: IllegalArgumentException) {
                    return false
                }
                return true
            }
            4 -> {
                try {
                    val type = EntityType.valueOf(args[0].toUpperCase())
                    val rangeX = args[1].toDouble()
                    val rangeY = args[2].toDouble()
                    val rangeZ = args[3].toDouble()
                    player.sendMessage(
                        "${
                            deleteEntities(
                                player.location,
                                type,
                                rangeX,
                                rangeY,
                                rangeZ
                            )
                        }마리의 ${type}이/가 제거되엇습니다."
                    )
                } catch (e: IllegalArgumentException) {
                    return false
                }
                return true
            }
            else -> return false
        }
    }

    companion object {
        val typeList = mutableListOf<String>().apply {
            EntityType.values().forEach {
                this.add(it.toString())
            }
        }
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): List<String>? {
        if (alias.equals("killall", ignoreCase = true)) {
            when (args.size) {
                1 -> {
                    return typeList.filter {
                        it.startsWith(args[0], ignoreCase = true)
                    }
                }
                2, 3, 4 -> return listOf("0")
            }
        }
        return null
    }

    private fun deleteEntities(location: Location, type: EntityType): Int {
        var deleted = 0
        location.world.entities.forEach {
            if (it.type == type) {
                it.remove()
                deleted++
            }
        }
        return deleted
    }

    private fun deleteEntities(location: Location, type: EntityType, range: Double): Int {
        return deleteEntities(location, type, range, range, range)
    }

    private fun deleteEntities(
        location: Location,
        type: EntityType,
        rangeX: Double,
        rangeY: Double,
        rangeZ: Double
    ): Int {
        var deleted = 0
        location.world.getNearbyLivingEntities(location, rangeX, rangeY, rangeZ).forEach {
            if (it.type == type) {
                it.remove()
                deleted++
            }
        }
        return deleted
    }
}