package zip.jespersen.xhappyspeed.events

import zip.jespersen.xhappyspeed.XHappySpeedPlugin
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.attribute.Attribute
import org.bukkit.entity.EntityType
import org.bukkit.entity.HappyGhast
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityMountEvent
import org.bukkit.persistence.PersistentDataType
import zip.jespersen.xhappyspeed.Configs

object HappyGhastMountEvent : Listener {
    val mm = MiniMessage.miniMessage()

    @EventHandler
    fun onEntityDismountEvent(event: EntityMountEvent) {
        if (!Configs.config.data.enablePassengers) return
        if (event.mount.type == EntityType.HAPPY_GHAST && event.mount.passengers.isEmpty()) {

            (event.mount as HappyGhast).getAttribute(Attribute.FLYING_SPEED)!!.baseValue =
                (event.mount as HappyGhast).persistentDataContainer.get(
                    XHappySpeedPlugin.instance.speedKey, PersistentDataType.DOUBLE
                ) ?: 0.05

            XHappySpeedPlugin.instance.setParked(false, event.mount as HappyGhast)
            XHappySpeedPlugin.instance.happyPassengers[event.entity.entityId.toString()] =
                event.mount.uniqueId.toString()
            event.entity.sendActionBar(mm.deserialize(Configs.messages.data.grantOwnerFromHappyGhast))
        }
    }

}