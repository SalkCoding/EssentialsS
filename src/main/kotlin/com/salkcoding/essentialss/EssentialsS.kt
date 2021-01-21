package com.salkcoding.essentialss

import com.salkcoding.essentialss.bungee.chnnelapi.BungeeChannelApi
import org.bukkit.plugin.java.JavaPlugin

lateinit var essentials: EssentialsS
lateinit var bungeeApi: BungeeChannelApi

class EssentialsS : JavaPlugin() {

    override fun onEnable() {
        essentials = this
        bungeeApi = BungeeChannelApi.of(this)
    }

    override fun onDisable() {

    }
}