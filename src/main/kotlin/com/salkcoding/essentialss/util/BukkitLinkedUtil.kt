package com.salkcoding.essentialss.util

import com.salkcoding.essentialss.bukkitLinkedAPI
import org.bukkit.Bukkit
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