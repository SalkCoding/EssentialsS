package com.salkcoding.essentialss.command.bungee

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.salkcoding.essentialss.currentServerName
import com.salkcoding.essentialss.essentials
import com.salkcoding.essentialss.metamorphosis
import com.salkcoding.essentialss.util.errorFormat
import com.salkcoding.essentialss.util.infoFormat
import fish.evatuna.metamorphosis.kafka.KafkaReceiveEvent
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class CommandList : CommandExecutor, Listener {
    private val gson = Gson()

    private val ALL_REQUEST_MESSAGE = "com.salkcoding.essentialss.request_all_server_player_list"
    private val SPECIFIC_REQUEST_MESSAGE = "com.salkcoding.essentialss.request_specific_server_player_list"
    private val RETURN_MESSAGE = "com.salkcoding.essentialss.return_player_list"

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (!sender.isOp) {
            sender.sendMessage("권한이 없습니다.".errorFormat())
            return true
        }

        when (args.size) {
            0 -> {
                val playerList = mutableListOf<String>()
                essentials.server.onlinePlayers.forEach { playerList.add(it.name) }
                sender.sendMessage("$currentServerName: ${playerList.joinToString(", ")} (${essentials.server.onlinePlayers.size})".infoFormat())

                metamorphosis.send(
                    ALL_REQUEST_MESSAGE,
                    gson.toJson(
                        JsonObject().apply {
                            addProperty("commandSender", sender.name)
                            addProperty("commandSenderServer", currentServerName)
                        }
                    )
                )
                return true
            }

            1 -> {
                val receiveServer = args[0]
                if (receiveServer == currentServerName) {
                    val playerList = mutableListOf<String>()
                    essentials.server.onlinePlayers.forEach { playerList.add(it.name) }
                    sender.sendMessage("$currentServerName: ${playerList.joinToString(", ")} (${essentials.server.onlinePlayers.size})".infoFormat())
                    return true
                }

                metamorphosis.send(
                    SPECIFIC_REQUEST_MESSAGE,
                    gson.toJson(
                        JsonObject().apply {
                            addProperty("commandSender", sender.name)
                            addProperty("commandSenderServer", currentServerName)
                            addProperty("commandReceiveServer", receiveServer)
                        }
                    )
                )
                return true
            }
        }

        return false
    }

    @EventHandler
    fun onKafkaReceive(event: KafkaReceiveEvent) {
        try {
            when (event.key) {
                ALL_REQUEST_MESSAGE -> {
                    val json = JsonParser.parseString(event.value).asJsonObject
                    val commandSenderServer = json.get("commandSenderServer").asString
                    if (commandSenderServer == currentServerName) return
                    essentials.logger.info("Received Kafka message, key:${event.key}, value:${event.value}")

                    metamorphosis.send(
                        RETURN_MESSAGE,
                        gson.toJson(
                            JsonObject().apply {
                                addProperty("commandSender", json.get("commandSender").asString)
                                addProperty("commandSenderServer", commandSenderServer)
                                addProperty("commandReceiveServer", currentServerName)

                                val playerList = mutableListOf<String>()
                                essentials.server.onlinePlayers.forEach { playerList.add(it.name) }
                                addProperty("result", "${playerList.joinToString(", ")} (${essentials.server.onlinePlayers.size})")
                            }
                        )
                    )
                }

                SPECIFIC_REQUEST_MESSAGE -> {
                    val json = JsonParser.parseString(event.value).asJsonObject
                    val commandReceiveServer = json.get("commandReceiveServer").asString
                    if (commandReceiveServer != currentServerName) return
                    essentials.logger.info("Received Kafka message, key:${event.key}, value:${event.value}")

                    metamorphosis.send(
                        RETURN_MESSAGE,
                        gson.toJson(
                            JsonObject().apply {
                                addProperty("commandSender", json.get("commandSender").asString)
                                addProperty("commandSenderServer", json.get("commandSenderServer").asString)
                                addProperty("commandReceiveServer", currentServerName)
                                val playerList = mutableListOf<String>()
                                essentials.server.onlinePlayers.forEach { playerList.add(it.name) }
                                addProperty("result", "${playerList.joinToString(", ")} (${essentials.server.onlinePlayers.size})")
                            }
                        )
                    )
                }

                RETURN_MESSAGE -> {
                    val json = JsonParser.parseString(event.value).asJsonObject
                    val commandSenderServer = json.get("commandSenderServer").asString
                    if (commandSenderServer != currentServerName) return
                    essentials.logger.info("Received Kafka message, key:${event.key}, value:${event.value}")

                    //getPlayer method don't return an instance when the player is offline
                    val commandSender = essentials.server.getPlayer(json.get("commandSender").asString) ?: return
                    commandSender.sendMessage("${json.get("commandReceiveServer").asString}: ${json.get("result").asString}".infoFormat())
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}