package com.salkcoding.essentialss.bungee.receiver

import com.google.common.io.ByteStreams
import com.salkcoding.essentialss.bungee.channelapi.BungeeChannelApi
import com.salkcoding.essentialss.util.bungeeTeleport
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class CommandReceiver : BungeeChannelApi.ForwardConsumer {

    override fun accept(channel: String, player: Player, data: ByteArray) {
        val inMessage = ByteStreams.newDataInput(data)
        when (channel) {
            "essentials-tp" -> {
                val fromName = inMessage.readUTF()
                val toName = inMessage.readUTF()

                val fromPlayer = Bukkit.getPlayer(fromName)
                if (fromPlayer != null) bungeeTeleport(fromPlayer, toName)
            }
            /*"essentials-tphere" -> {

            }
            "essentials-tppos" -> {

            }
            "essentials-tpwpos" -> {

            }*/
        }
    }
}