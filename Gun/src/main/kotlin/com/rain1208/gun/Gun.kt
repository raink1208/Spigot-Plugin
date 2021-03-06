package com.rain1208.gun

import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

class Gun : JavaPlugin(), Listener {
    companion object {
        lateinit var instance:Gun
        lateinit var configs:FileConfiguration
        private set
    }
    override fun onEnable() {
        logger.info("有効化します")
        saveDefaultConfig()
        configs = config
        instance = this
        server.pluginManager.registerEvents(UseGun(),this)
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (label == "gun") {
            if (sender is Player) {
                if (args[0].isNotEmpty()) {
                    val item = ItemStack(Material.STICK)
                    val meta = item.itemMeta
                    meta!!.setDisplayName(args[0])
                    item.itemMeta = meta
                    sender.inventory.addItem(item)
                    sender.sendMessage("gunを配布しました")
                } else {
                    sender.sendMessage("/gun <gunName>")
                }
            }
        }
        return true
    }

    override fun onDisable() {
        logger.info("無効化しました")
    }
}