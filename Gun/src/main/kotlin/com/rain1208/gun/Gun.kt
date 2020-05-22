package com.rain1208.gun

import org.bukkit.*
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Entity
import org.bukkit.entity.Item
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

class Gun : JavaPlugin(), Listener {
    override fun onEnable() {
        logger.info("有効化します")
        server.pluginManager.registerEvents(this,this)
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (label == "gun") {
            if (sender is Player) {
                val item = ItemStack(Material.STICK)
                val meta = item.itemMeta
                meta!!.setDisplayName("gun")
                item.itemMeta = meta
                sender.inventory.addItem(item)
                sender.sendMessage("gunを配布しました")
            }
        }
        return true
    }

    @EventHandler
    fun useGun(ev: PlayerInteractEvent) {
        if (ev.action == Action.RIGHT_CLICK_AIR || ev.action == Action.RIGHT_CLICK_BLOCK) {
            if (ev.item != null && ev.item!!.type == Material.STICK && ev.item!!.itemMeta?.displayName == "gun") {
                val p: Player = ev.player
                var pos:Location = p.location
                pos.world!!.playSound(pos,Sound.ENTITY_FIREWORK_ROCKET_BLAST,0.1f,1.0f) //発射地点
                for (i in 0..300) { //射程
                    pos = pos.add(pos.direction.x,pos.direction.y - 0.005,pos.direction.z)
                    pos.world!!.playSound(pos,Sound.ENTITY_FIREWORK_ROCKET_BLAST,0.1f,1.0f) //パーティクル上
                    ev.player.world.spawnParticle(Particle.VILLAGER_ANGRY,pos, 1, 0.02, 0.02, 0.05, 4.0)

                    for (entity in getEntitiesByPos(pos,0.55f)) {
                        if (entity is LivingEntity) {
                            entity.damage(4.0)
                        }
                    }
                    if (pos.block.type.isSolid) {
                        break
                    }
                }
            }
        }
    }

    private fun getEntitiesByPos(pos:Location, r:Float):ArrayList<Entity> {
        val entity: ArrayList<Entity> = ArrayList()
        for (e in pos.world!!.entities) {
            if (e.location.distanceSquared(pos) <= r) {
                entity.add(e)
            }
        }
        return entity
    }

    override fun onDisable() {
        logger.info("無効化しました")
    }
}