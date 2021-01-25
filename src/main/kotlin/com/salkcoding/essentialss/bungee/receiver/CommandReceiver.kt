package com.salkcoding.essentialss.bungee.receiver

import com.google.common.io.ByteStreams
import com.salkcoding.essentialss.bukkitLinkedAPI
import com.salkcoding.essentialss.bungee.chnnelapi.BungeeChannelApi
import com.salkcoding.essentialss.util.teleport
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
                if (fromPlayer != null) bukkitLinkedAPI.teleport(fromPlayer, toName)
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