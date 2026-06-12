package zip.jespersen.xhappyspeed

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIPaperConfig
import zip.jespersen.xhappyspeed.commands.Happy
import zip.jespersen.xhappyspeed.events.HappyGhastDismountEvent
import zip.jespersen.xhappyspeed.events.HappyGhastMountEvent
import zip.jespersen.xhappyspeed.events.PlayerInteractEvent
import zip.jespersen.xhappyspeed.events.PlayerInteractEvent.key
import net.crystopia.crystalshard.paper.core.crystalshard
import org.bukkit.NamespacedKey
import org.bukkit.entity.HappyGhast
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin

class XHappySpeedPlugin : JavaPlugin() {

    companion object {
        lateinit var instance: XHappySpeedPlugin
    }

    init {
        instance = this
    }

    val happyPassengers = mutableMapOf<String, String>()

    override fun onLoad() {
        CommandAPI.onLoad(CommandAPIPaperConfig(this).silentLogs(true))

        logger.info("Loading Plugin...")

    }

    override fun onEnable() {
        CommandAPI.onEnable()

        crystalshard(this)
        Configs
        Happy
        server.pluginManager.registerEvents(HappyGhastDismountEvent, this)
        server.pluginManager.registerEvents(PlayerInteractEvent, this)
        server.pluginManager.registerEvents(HappyGhastMountEvent, this)

        logger.info("Plugin enabled!")

    }

    override fun onDisable() {
        CommandAPI.onDisable()

        logger.info("Plugin disabled!")
    }

    val speedKey = NamespacedKey(this, "speed")
    fun setSpeed(speed: Double, target: HappyGhast) {
        target.persistentDataContainer.set(speedKey, PersistentDataType.DOUBLE, speed)
    }

    fun setParked(isParked: Boolean, target: HappyGhast) {
        target.persistentDataContainer.set(key, PersistentDataType.BOOLEAN, isParked)
    }

}