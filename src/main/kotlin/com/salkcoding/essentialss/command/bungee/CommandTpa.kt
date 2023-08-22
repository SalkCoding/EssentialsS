package com.salkcoding.essentialss.command.bungee

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.salkcoding.essentialss.*
import com.salkcoding.essentialss.command.admin.tpaPermissionKey
import com.salkcoding.essentialss.util.errorFormat
import com.salkcoding.essentialss.util.warnFormat
import fish.evatuna.metamorphosis.redis.MetamorphosisReceiveEvent
import me.baiks.bukkitlinked.models.PlayerInfo
import net.luckperms.api.LuckPermsProvider
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import java.util.*

//Key: target, Value: Invitor information
val tpaInviteMap: HashMap<UUID, Invitor> = hashMapOf()

class CommandTpa : CommandExecutor, Listener {

    private val metaTpaInviteKey = "com.salkcoding.essentialss.tpa"

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val invitor = sender as? Player
        if (invitor == null) {
            essentials.logger.warning("Player only command")
            return true
        }

        val lp = LuckPermsProvider.get()
        val user =
            lp.userManager.getUser(invitor.uniqueId) ?: throw IllegalStateException("User not found in luckperms")
        val hasPermission = user.nodes.any { it.key == tpaPermissionKey && it.hasExpiry() && !it.hasExpired() }

        if (!invitor.isOp && !hasPermission) {
            invitor.sendMessage("마일리지를 사용해서 TPA권을 구매해주세요.".errorFormat())
            return true
        }

        when (args.size) {
            //send tpa
            1 -> {
                val info: PlayerInfo? = bukkitLinkedAPI.getPlayerInfo(args[0])
                if (info == null) {
                    sender.sendMessage("${args[0]}이라는 이름을 가진 유저가 없습니다.".errorFormat())
                    return true
                }

                if (info.world in tpaLimitWorldName) {
                    sender.sendMessage("해당 유저가 TPA 요청을 받을 수 없는 월드에 있습니다.".errorFormat())
                    return true
                }

                val json = JsonObject().apply {
                    addProperty("invitor", invitor.uniqueId.toString())
                    addProperty("invitorServer", currentServerName)
                    addProperty("target", info.playerUUID.toString())
                    addProperty("targetServer", info.serverName)
                }
                metamorphosis.send(metaTpaInviteKey, json.toString())
                return true
            }
        }
        return false
    }

    @EventHandler
    fun onTpaReceive(event: MetamorphosisReceiveEvent) {
        if (event.key != metaTpaInviteKey) return
        val json = JsonParser.parseString(event.value).asJsonObject

        val targetServer = json["targetServer"].asString
        //초대 대상의 서버가 아닌 곳에서 메세지 받을 시 무시
        if (currentServerName != targetServer) return

        val targetUUID = UUID.fromString(json["target"].asString)
        if (targetUUID in tpaInviteMap) {
            val invitorInfo = tpaInviteMap[targetUUID]!!
            val delta = invitorInfo.expired - System.currentTimeMillis()
            if (delta > 0) {
                bukkitLinkedAPI.sendMessageAcrossServer(
                    invitorInfo.invitor,
                    "해당 플레이어는 현재 다른 플레이어의 tpa를 기다리고 있습니다. 나중에 다시 시도해 주세요.".warnFormat()
                )
                return
            }
        }

        val target = Bukkit.getPlayer(targetUUID) ?: return
        target.sendMessage(
            "승낙하려면 ${ChatColor.GREEN}/tpaccept${ChatColor.WHITE}," +
                    " 거절하려면 ${ChatColor.RED}/tpdeny${ChatColor.WHITE}을 입력하세요."
        )

        tpaInviteMap[targetUUID] = Invitor(
            UUID.fromString(json["invitor"].asString),
            json["invitorServer"].asString,
            System.currentTimeMillis() + 10000 // 10초
        )
    }
}