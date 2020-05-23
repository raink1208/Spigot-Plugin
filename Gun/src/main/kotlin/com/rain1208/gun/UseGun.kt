package com.rain1208.gun

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class UseGun:Listener {
    @EventHandler
    fun useGun(ev: PlayerInteractEvent) {
        if (ev.action == Action.RIGHT_CLICK_AIR || ev.action == Action.RIGHT_CLICK_BLOCK) {
            if (ev.item != null && ev.item!!.type == Material.STICK && ev.item!!.itemMeta?.displayName == "gun") {
                val p: Player = ev.player
                var pos: Location = p.location
                pos.y += 1
                pos.world!!.playSound(pos, Sound.ENTITY_FIREWORK_ROCKET_BLAST,0.1f,1.0f) //発射地点
                for (i in 0..300) { //射程 300*0.02
                    pos = pos.add(pos.direction.x,pos.direction.y-0.002f,pos.direction.z)
                    pos.world!!.playSound(pos, Sound.ENTITY_FIREWORK_ROCKET_BLAST,0.1f,1.0f) //パーティクル上
                    ev.player.world.spawnParticle(Particle.VILLAGER_ANGRY,pos, 1, 0.02, 0.02, 0.02, 4.0)

                    for (entity in getEntitiesByPos(pos,0.55f)) {
                        if (entity is LivingEntity) {
                            entity.damage(5.0) //ダメージ量
                        }
                    }
                    if (pos.block.type.isSolid) {
                        break //ブロックに当たったら終了
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
}