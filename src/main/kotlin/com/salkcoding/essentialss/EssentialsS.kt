package com.salkcoding.essentialss

import com.salkcoding.essentialss.bungee.chnnelapi.BungeeChannelApi
import com.salkcoding.essentialss.bungee.receiver.CommandReceiver
import com.salkcoding.essentialss.command.bungee.*
import com.salkcoding.essentialss.command.single.*
import me.baiks.bukkitlinked.BukkitLinked
import me.baiks.bukkitlinked.api.BukkitLinkedAPI
import org.bukkit.plugin.java.JavaPlugin

lateinit var essentials: EssentialsS
lateinit var bungeeApi: BungeeChannelApi
lateinit var bukkitLinkedAPI: BukkitLinkedAPI

class EssentialsS : JavaPlugin() {

    override fun onEnable() {
        essentials = this

        val bukkitLinked = server.pluginManager.getPlugin("BukkitLinked") as? BukkitLinked
        if (bukkitLinked == null) {
            server.pluginManager.disablePlugin(this)
            return
        }
        bukkitLinkedAPI = bukkitLinked.api

        bungeeApi = BungeeChannelApi.of(this)
        bungeeApi.registerForwardListener("essentials-tp", CommandReceiver())

        getCommand("kickall")!!.setExecutor(CommandKickAll())
        getCommand("list")!!.setExecutor(CommandList())
        getCommand("seen")!!.setExecutor(CommandSeen())
        getCommand("tp")!!.setExecutor(CommandTp())
        getCommand("tpall")!!.setExecutor(CommandTpAll())
        getCommand("tphere")!!.setExecutor(CommandTpHere())

        getCommand("ci")!!.setExecutor(CommandClearInventory())
        getCommand("feed")!!.setExecutor(CommandFeed())
        getCommand("fly")!!.setExecutor(CommandFly())
        getCommand("gamemode")!!.setExecutor(CommandGameMode())
        getCommand("heal")!!.setExecutor(CommandHeal())
        getCommand("killall")!!.setExecutor(CommandKillAll())
        getCommand("more")!!.setExecutor(CommandMore())
        getCommand("near")!!.setExecutor(CommandNear())
        getCommand("repair")!!.setExecutor(CommandRepair())
        getCommand("speed")!!.setExecutor(CommandSpeed())
        getCommand("top")!!.setExecutor(CommandTop())
        getCommand("tppos")!!.setExecutor(CommandTpPos())
        getCommand("tpwpos")!!.setExecutor(CommandTpwPos())
        getCommand("xp")!!.setExecutor(CommandXp())

        logger.info("Plugin enabled")
    }

    override fun onDisable() {
        logger.warning("Plugin disabled")
    }
}