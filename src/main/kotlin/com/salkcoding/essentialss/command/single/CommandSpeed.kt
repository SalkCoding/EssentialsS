package com.salkcoding.essentialss.command.single

import com.salkcoding.essentialss.essentials
import com.salkcoding.essentialss.util.errorFormat
import com.salkcoding.essentialss.util.infoFormat
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CommandSpeed : CommandExecutor {

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
                try {
                    //speed -1 < n < 1, normal speed : 0.2
                    // -1 < n < 1
                    val speed = args[0].toFloat()
                    if (player.isFlying) {
                        player.flySpeed = getRealMoveSpeed(speed, true)
                        player.sendMessage("플라이 속도를 ${speed}로 설정하였습니다.".infoFormat())
                    } else {
                        player.walkSpeed = getRealMoveSpeed(speed, false)
                        player.sendMessage("걷는 속도를 ${speed}로 설정하였습니다.".infoFormat())
                    }
                } catch (e: NumberFormatException) {
                    return false
                } catch (e: IllegalArgumentException) {
                    player.sendMessage("값이 올바르지 않습니다.".errorFormat())
                }
                return true
            }
        }
        return false
    }

    private fun getRealMoveSpeed(userSpeed: Float, isFly: Boolean): Float {
        val defaultSpeed = if (isFly) 0.1f else 0.2f
        val maxSpeed = 1f
        return if (userSpeed < 1f) {
            defaultSpeed * userSpeed
        } else {
            val ratio = (userSpeed - 1) / 9 * (maxSpeed - defaultSpeed)
            ratio + defaultSpeed
        }
    }
}