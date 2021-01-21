package com.salkcoding.essentialss.bungee.receiver

import com.salkcoding.essentialss.bungee.chnnelapi.BungeeChannelApi
import org.bukkit.entity.Player

class CommandReceiver : BungeeChannelApi.ForwardConsumer {

    override fun accept(channel: String, player: Player, data: ByteArray) {
        when (channel) {
            "essentials-kickall" -> {

            }
            "essentials-tp" -> {

            }
            "essentials-tpall" -> {

            }
            "essentials-tphere" -> {

            }
            "essentials-tppos" -> {

            }
            "essentials-tpwpos" -> {

            }
        }
    }
}