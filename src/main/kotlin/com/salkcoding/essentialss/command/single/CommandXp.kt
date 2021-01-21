package com.salkcoding.essentialss.command.single

import com.salkcoding.essentialss.util.ExpUtil
import com.salkcoding.essentialss.util.errorFormat
import com.salkcoding.essentialss.util.infoFormat
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import java.util.*

class CommandXp : CommandExecutor, TabCompleter {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        when (args.size) {
            2 -> {
                if (args[0].equals("show", ignoreCase = true)) {
                    val targetPlayer = Bukkit.getPlayer(args[1])
                    if (targetPlayer != null) {
                        sender.sendMessage("${targetPlayer.name}의 경험치: ${ExpUtil.getExp(targetPlayer)}")
                    } else sender.sendMessage("존재하지 않는 플레이어입니다.".errorFormat())
                }
                return true
            }
            3 -> {
                try {
                    val targetPlayer = Bukkit.getPlayer(args[1])
                    val exp = args[2].toInt()
                    if (targetPlayer != null) {
                        when {
                            args[0].equals("set", ignoreCase = true) -> {
                                ExpUtil.setExp(targetPlayer, exp)
                                targetPlayer.sendMessage("경험치가 ${exp}로 설정되었습니다.".infoFormat())
                                sender.sendMessage("${targetPlayer.name}의 경험치를 ${exp}로 설정하였습니다.".infoFormat())
                            }
                            args[0].equals("give", ignoreCase = true) -> {
                                ExpUtil.changeExp(targetPlayer, exp)
                                targetPlayer.sendMessage("경험치가 ${exp}만큼을 받았습니다.".infoFormat())
                                sender.sendMessage("${targetPlayer.name}에게 ${exp}만큼의 경험치를 지급하였습니다.".infoFormat())
                            }
                        }
                        targetPlayer.playSound(targetPlayer.location, Sound.ENTITY_PLAYER_LEVELUP, 0.5f, 1f)
                    } else sender.sendMessage("존재하지 않는 플레이어입니다.".errorFormat())
                } catch (e: NumberFormatException) {
                    return false
                }
                return true
            }
            else -> return false
        }
    }

    companion object {
        val query = listOf("show", "set", "give")
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): List<String>? {
        if (alias.equals("xp", ignoreCase = true)) {
            when (args.size) {
                1 -> {
                    return query.filter {
                        args[0].startsWith(it, ignoreCase = true)
                    }
                }
                2 -> {
                    val playerList: MutableList<String> = ArrayList()
                    for (player in Bukkit.getOnlinePlayers()) playerList.add(player.name)
                    return playerList.filter {
                        args[1].startsWith(it, ignoreCase = true)
                    }
                }
                3, 4 -> if (!args[0].equals("show", ignoreCase = true)) return listOf("0")
            }
        }
        return null
    }
}