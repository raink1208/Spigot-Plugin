package com.rain1208.kakecraftoni

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.*

class KakeCraftOni : JavaPlugin() {
    companion object {
        var instance:KakeCraftOni? = null
        private set
    }

    var game = false
    private var damages = mutableMapOf<String,Int>()

    private var pos:List<Location> = listOf(
            Location(Bukkit.getWorld("world"),9898.5, 65.0, 10064.5),
            Location(Bukkit.getWorld("world"),9775.5, 70.0, 10055.5),
            Location(Bukkit.getWorld("world"),9856.5, 67.0, 10054.5),
            Location(Bukkit.getWorld("world"),9862.5, 65.0, 10122.5),
            Location(Bukkit.getWorld("world"),9902.5, 68.0, 10130.5),
            Location(Bukkit.getWorld("world"),9895.5, 89.0, 10173.5),
            Location(Bukkit.getWorld("world"),9786.5, 65.0, 10185.5),
            Location(Bukkit.getWorld("world"),9716.5, 65.0, 10085.5),
            Location(Bukkit.getWorld("world"),9885.5, 69.0, 10148.5),
            Location(Bukkit.getWorld("world"),9740.5, 65.0, 10116.5),
            Location(Bukkit.getWorld("world"),9826.5, 66.0, 10172.5)
    )

    override fun onEnable() {
        instance = this
        server.pluginManager.registerEvents(EventListener(),this)
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            when (label) {
                "bdstart" -> startGame()
                "bdstop" -> endGame()
                "force" -> {
                    if (game) {
                        forcedStop(sender)
                    } else {
                        sender.sendMessage("既にゲームは起動しています")
                    }
                }
                "ai" -> setAI(sender)
            }
        }
        return true
    }

    private fun setAI(player: Player) {
        if (player.hasAI()) {
            player.setAI(false)
        } else {
            player.setAI(true)
        }
    }

    fun startGame() {
        Bukkit.getOnlinePlayers().forEach{ p -> damages[p.name] to  0}
        game = true
    }

    fun addDamage(player:Player) {
        val point = damages.getValue(player.name) + 1
        damages[player.name] = point
        if (point == 2) {
            damages[player.name] = 0
            val num = Random().nextInt(pos.size)
            server.broadcastMessage(player.name+"さんがフックにつられました")
            player.addPotionEffect(PotionEffect(PotionEffectType.GLOWING,100000000,100000))
            player.addScoreboardTag("hook")
            player.teleport(pos[num])
        }
    }

    fun endGame () {
        damages  = mutableMapOf()
        game = false
    }

    fun forcedStop(player:Player) {
        server.broadcastMessage("ゲームを強制終了します...")
        player.world.getBlockAt(9675,98,10130).type = Material.REDSTONE_BLOCK
        endGame()
    }

    override fun onDisable() {
    }
}