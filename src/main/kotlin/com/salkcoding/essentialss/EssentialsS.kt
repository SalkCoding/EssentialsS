package com.salkcoding.essentialss

import com.salkcoding.essentialss.bungee.channelapi.BungeeChannelApi
import com.salkcoding.essentialss.command.admin.CommandTpaTicket
import com.salkcoding.essentialss.command.bungee.*
import com.salkcoding.essentialss.command.single.*
import fish.evatuna.metamorphosis.Metamorphosis
import me.baiks.bukkitlinked.BukkitLinked
import me.baiks.bukkitlinked.api.BukkitLinkedAPI
import org.bukkit.plugin.java.JavaPlugin

lateinit var essentials: EssentialsS
lateinit var currentServerName: String
lateinit var tpaLimitWorldName: Set<String>

lateinit var bukkitLinkedAPI: BukkitLinkedAPI
lateinit var metamorphosis: Metamorphosis
lateinit var bungeeAPI: BungeeChannelApi

class EssentialsS : JavaPlugin() {

    override fun onEnable() {
        essentials = this

        val bukkitLinked = server.pluginManager.getPlugin("BukkitLinked") as? BukkitLinked
        if (bukkitLinked == null) {
            server.pluginManager.disablePlugin(this)
            return
        }
        bukkitLinkedAPI = bukkitLinked.api

        val meta = server.pluginManager.getPlugin("Metamorphosis") as? Metamorphosis
        if (meta == null) {
            server.pluginManager.disablePlugin(this)
            return
        }
        metamorphosis = meta

        bungeeAPI = BungeeChannelApi.of(this)

        saveDefaultConfig()
        currentServerName = config.getString("serverName")!!
        tpaLimitWorldName= config.getList("tpaLimitWorld")!!.toSet() as Set<String>

        getCommand("tpaticket")!!.setExecutor(CommandTpaTicket())
        getCommand("tpa")!!.setExecutor(CommandTpa())
        getCommand("tpacheck")!!.setExecutor(CommandTpaCheck())
        getCommand("tpaccept")!!.setExecutor(CommandTpAccept())
        getCommand("tpdeny")!!.setExecutor(CommandTpDeny())

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
        val killAll = CommandKillAll()
        getCommand("killAll")!!.setExecutor(killAll)
        getCommand("killAll")!!.tabCompleter = killAll
        getCommand("more")!!.setExecutor(CommandMore())
        getCommand("near")!!.setExecutor(CommandNear())
        getCommand("repair")!!.setExecutor(CommandRepair())
        getCommand("speed")!!.setExecutor(CommandSpeed())
        getCommand("top")!!.setExecutor(CommandTop())
        val tppos = CommandTpPos()
        getCommand("tppos")!!.setExecutor(tppos)
        getCommand("tppos")!!.tabCompleter = tppos
        val tpwpos = CommandTpwPos()
        getCommand("tpwpos")!!.setExecutor(tpwpos)
        getCommand("tpwpos")!!.tabCompleter = tpwpos
        val xp = CommandXp()
        getCommand("xp")!!.setExecutor(xp)
        getCommand("xp")!!.tabCompleter = xp

        server.pluginManager.registerEvents(CommandList(), this)

        logger.info("Plugin enabled")
    }

    override fun onDisable() {
        logger.warning("Plugin disabled")
    }
}