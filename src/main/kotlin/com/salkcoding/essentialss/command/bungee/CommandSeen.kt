package com.salkcoding.essentialss.command.bungee

import com.salkcoding.essentialss.bukkitLinkedAPI
import com.salkcoding.essentialss.util.errorFormat
import com.salkcoding.essentialss.util.infoFormat
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import java.util.*

class CommandSeen : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (!sender.isOp) {
            sender.sendMessage("권한이 없습니다.".errorFormat())
            return true
        }

        when (args.size) {
            1 -> {
                val targetPlayer = Bukkit.getOfflinePlayerIfCached(args[0])
                if (targetPlayer == null) {
                    val playerInfo = bukkitLinkedAPI.getPlayerInfo(args[0])
                    if (playerInfo != null) {
                        val lastOnline = Calendar.getInstance()
                        lastOnline.timeInMillis = playerInfo.lastOnline
                        val updatedAt = Calendar.getInstance()
                        updatedAt.timeInMillis = playerInfo.updatedAt
                        sender.sendMessage("name: ${playerInfo.playerName}, UUID: ${playerInfo.playerUUID}".infoFormat())
                        sender.sendMessage("     isOp: ${playerInfo.isOp}, isOnline: ${playerInfo.isOnline}")
                        sender.sendMessage(
                            "     lastOnline: ${
                                lastOnline.get(Calendar.YEAR)
                            }/${
                                lastOnline.get(Calendar.MONTH) + 1
                            }/${
                                lastOnline.get(Calendar.DATE)
                            } ${
                                lastOnline.get(Calendar.HOUR_OF_DAY)
                            }:${
                                lastOnline.get(Calendar.MINUTE)
                            }"
                        )
                        sender.sendMessage(
                            "     updatedAt: ${
                                updatedAt.get(Calendar.YEAR)
                            }/${
                                updatedAt.get(Calendar.MONTH) + 1
                            }/${
                                updatedAt.get(Calendar.DATE)
                            } ${
                                updatedAt.get(Calendar.HOUR_OF_DAY)
                            }:${
                                updatedAt.get(Calendar.MINUTE)
                            }"
                        )
                        return true
                    }

                    sender.sendMessage("존재하지 않는 플레이어입니다.".errorFormat())
                    return true
                }

                val lastLogin = Calendar.getInstance()
                lastLogin.timeInMillis = targetPlayer.lastLogin
                val lastSeen = Calendar.getInstance()
                lastSeen.timeInMillis = targetPlayer.lastSeen
                sender.sendMessage("name: ${targetPlayer.name}, UUID: ${targetPlayer.uniqueId}".infoFormat())
                sender.sendMessage("     isOp: ${targetPlayer.isOp}, isBanned: ${targetPlayer.isBanned}")
                sender.sendMessage(
                    "     lastLogin: ${
                        lastLogin.get(Calendar.YEAR)
                    }/${
                        lastLogin.get(Calendar.MONTH) + 1
                    }/${
                        lastLogin.get(Calendar.DATE)
                    } ${
                        lastLogin.get(Calendar.HOUR_OF_DAY)
                    }:${
                        lastLogin.get(Calendar.MINUTE)
                    }"
                )
                sender.sendMessage(
                    "     lastSeen: ${
                        lastSeen.get(Calendar.YEAR)
                    }/${
                        lastSeen.get(Calendar.MONTH) + 1
                    }/${
                        lastSeen.get(Calendar.DATE)
                    } ${
                        lastSeen.get(Calendar.HOUR_OF_DAY)
                    }:${
                        lastSeen.get(Calendar.MINUTE)
                    }"
                )
                if (targetPlayer.isOnline) {
                    val online = targetPlayer.player!!
                    sender.sendMessage("     IP: ${online.address.address.hostAddress}")
                }
            }
        }

        return false
    }
}