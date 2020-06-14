package com.rain1208.kakecraftoni

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerDropItemEvent

class EventListener :Listener {
    @EventHandler
    fun onDropItem(event:PlayerDropItemEvent)
    {
        event.isCancelled = KakeCraftOni.instance?.game!!
    }

    @EventHandler
    fun attackPlayer(event:EntityDamageByEntityEvent)
    {
        val instance = KakeCraftOni.instance!!
        if (!instance.game) return
        val attacker = event.damager
        val hitter = event.entity
        if (attacker is Player && hitter is Player) {
            if (attacker.scoreboardTags.contains("KakeOni.Demon")) {
                when (attacker.inventory.itemInMainHand.itemMeta?.displayName) {
                    "デーモンの鎌" -> event.damage = 100.0
                    else -> instance.addDamage(hitter)
                }
            }
        }
        event.isCancelled = false
    }
}