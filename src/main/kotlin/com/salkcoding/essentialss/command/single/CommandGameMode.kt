package com.salkcoding.essentialss.command.single

import com.salkcoding.essentialss.essentials
import com.salkcoding.essentialss.exception.QuietAbortException
import com.salkcoding.essentialss.util.errorFormat
import com.salkcoding.essentialss.util.infoFormat
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerGameModeChangeEvent

class CommandGameMode : CommandExecutor {

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
                val gameMode = args[0].toGameMode() ?: when (player.gameMode) {
                    GameMode.SURVIVAL -> GameMode.CREATIVE
                    GameMode.CREATIVE -> GameMode.ADVENTURE
                    else -> GameMode.SURVIVAL
                }

                val event = PlayerGameModeChangeEvent(player, gameMode)
                essentials.server.pluginManager.callEvent(event)
                if (event.isCancelled) throw QuietAbortException("GameMode changing cancelled!")

                player.gameMode = gameMode
                player.sendMessage(
                    "게임모드가 ${
                        when (gameMode) {
                            GameMode.CREATIVE -> "크리에이티브"
                            GameMode.SURVIVAL -> "서바이벌"
                            GameMode.ADVENTURE -> "어드벤처"
                            GameMode.SPECTATOR -> "스펙터"
                        }
                    } 모드로 변경되었습니다.".infoFormat()
                )
            }
            2 -> {
                val targetPlayer = Bukkit.getPlayer(args[0])
                if (targetPlayer == null) {
                    player.sendMessage("존재하지 않는 플레이어입니다.".errorFormat())
                    return true
                }

                val gameMode = args[0].toGameMode() ?: when (targetPlayer.gameMode) {
                    GameMode.SURVIVAL -> GameMode.CREATIVE
                    GameMode.CREATIVE -> GameMode.ADVENTURE
                    else -> GameMode.SURVIVAL
                }
                val event = PlayerGameModeChangeEvent(targetPlayer, gameMode)
                essentials.server.pluginManager.callEvent(event)
                if (event.isCancelled) throw QuietAbortException("GameMode changing cancelled!")

                targetPlayer.gameMode = gameMode
                targetPlayer.sendMessage(
                    "게임모드가 ${
                        when (gameMode) {
                            GameMode.CREATIVE -> "크리에이티브"
                            GameMode.SURVIVAL -> "서바이벌"
                            GameMode.ADVENTURE -> "어드벤처"
                            GameMode.SPECTATOR -> "스펙터"
                        }
                    } 모드로 변경되었습니다.".infoFormat()
                )
            }
        }
        return false
    }

    private fun String.toGameMode(): GameMode? {
        return when (this.toLowerCase()) {
            "0", "s", "survival" -> GameMode.SURVIVAL
            "1", "c", "creative" -> GameMode.CREATIVE
            "2", "a", "adventure" -> GameMode.ADVENTURE
            "3", "sp", "spectator" -> GameMode.SPECTATOR
            else -> null
        }
    }
}