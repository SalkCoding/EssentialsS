package com.salkcoding.essentialss.util

import org.bukkit.ChatColor

fun String.announceFormat(): String {
    return "\ue4db ${ChatColor.RESET}$this"
}

fun String.infoFormat(): String {
    return "\ue4dc ${ChatColor.RESET}$this"
}

fun String.warnFormat(): String {
    return "\ue4dd ${ChatColor.RESET}$this"
}

fun String.errorFormat(): String {
    return "\ue4de ${ChatColor.RESET}$this"
}