package com.salkcoding.essentialss.util

import com.salkcoding.essentialss.bukkitLinkedAPI
import com.salkcoding.essentialss.essentials
import me.baiks.bukkitlinked.api.TeleportResult
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

fun String.getUUIDFromName(): UUID {
    val player = Bukkit.getPlayer(this)
    if (player != null) return player.uniqueId

    val playerInfo = bukkitLinkedAPI.getPlayerInfo(this)
    if (playerInfo != null) return playerInfo.playerUUID

    val cachedPlayer = Bukkit.getOfflinePlayerIfCached(this)
    if (cachedPlayer != null) return cachedPlayer.uniqueId

    return Bukkit.getOfflinePlayer(this).uniqueId
}

fun UUID.getUUIDFromName(): String? {
    val p = Bukkit.getPlayer(this)
    if (p != null) return p.name

    val playerInfo = bukkitLinkedAPI.getPlayerInfo(this)
    if (playerInfo != null) return playerInfo.playerName

    return Bukkit.getOfflinePlayer(this).name
}

fun bungeeTeleport(player: Player, targetName: String) {
    val toPlayerInfo = bukkitLinkedAPI.onlinePlayersInfo.find { it.playerName == targetName }
    if (toPlayerInfo != null) {
        val result = bukkitLinkedAPI.teleport(player.uniqueId, toPlayerInfo.playerUUID)
        if (result == TeleportResult.TELEPORT_STARTED) {
            essentials.logger.info("Teleport succeeded, ${player.name}(${player.uniqueId}) -> ${toPlayerInfo.playerName}(${toPlayerInfo.playerUUID}), Result: $result")
        } else {
            player.sendMessage("Teleport failed, Result: $result".errorFormat())
            essentials.logger.warning("Teleport failed, ${player.name}(${player.uniqueId}) -> ${toPlayerInfo.playerName}(${toPlayerInfo.playerUUID}), Result: $result")
        }
    }
}