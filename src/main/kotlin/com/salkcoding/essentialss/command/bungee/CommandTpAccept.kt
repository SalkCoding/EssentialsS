package com.salkcoding.essentialss.command.bungee

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.salkcoding.essentialss.bukkitLinkedAPI
import com.salkcoding.essentialss.currentServerName
import com.salkcoding.essentialss.essentials
import com.salkcoding.essentialss.metamorphosis
import com.salkcoding.essentialss.util.TeleportCooltime
import com.salkcoding.essentialss.util.errorFormat
import com.salkcoding.essentialss.util.infoFormat
import fish.evatuna.metamorphosis.redis.MetamorphosisReceiveEvent
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import java.util.*

class CommandTpAccept : CommandExecutor, Listener {

    private val metaTpAcceptKey = "com.salkcoding.essentialss.tpaccept"

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val acceptor = sender as? Player
        if (acceptor == null) {
            essentials.logger.warning("Player only command")
            return true
        }

        val acceptorUUID = acceptor.uniqueId
        if (acceptorUUID !in tpaInviteMap) {
            acceptor.sendMessage("받은 TPA 요청이 없습니다.".errorFormat())
            return true
        }

        val invitorInfo = tpaInviteMap[acceptorUUID]!!
        val info = bukkitLinkedAPI.getPlayerInfo(invitorInfo.invitor)
        if (!info.isOnline) {
            acceptor.sendMessage("TPA 요청자가 접속을 종료했습니다.".errorFormat())
            tpaInviteMap.remove(acceptorUUID)
            return true
        }

        val delta = invitorInfo.expired - System.currentTimeMillis()
        if (delta <= 0) {
            acceptor.sendMessage("TPA 초대가 만료되었습니다.".errorFormat())
            tpaInviteMap.remove(acceptorUUID)
            return true
        }

        val json = JsonObject().apply {
            addProperty("invitor", invitorInfo.invitor.toString())
            addProperty("invitorServer", invitorInfo.invitorServer)
            addProperty("acceptor", acceptorUUID.toString())
        }
        metamorphosis.send(metaTpAcceptKey, json.toString())
        acceptor.sendMessage("요청을 승낙하여, TPA를 요청한 사람이 잠시 후, 텔레포트 됩니다.".infoFormat())
        return true
    }

    @EventHandler
    fun onTpAcceptReceive(event: MetamorphosisReceiveEvent) {
        if (metaTpAcceptKey != event.key) return

        val json = JsonParser.parseString(event.value).asJsonObject
        val invitorServer = json["invitorServer"].asString
        //초대한 사람의 서버가 아닌 곳에서 메세지 받을 시 무시
        if (currentServerName != invitorServer) return

        val invitorUUID = UUID.fromString(json["invitor"].asString)
        val invitor = Bukkit.getPlayer(invitorUUID) ?: return
        val acceptorUUID = UUID.fromString(json["acceptor"].asString)

        TeleportCooltime.addPlayer(invitor, null, 100, {
            bukkitLinkedAPI.teleport(invitorUUID, acceptorUUID)
        }, false)
    }
}

data class Invitor(val invitor: UUID, val invitorServer: String, val expired: Long)