package zip.jespersen.xhappyspeed.events

import zip.jespersen.xhappyspeed.XHappySpeedPlugin
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.attribute.Attribute
import org.bukkit.entity.EntityType
import org.bukkit.entity.HappyGhast
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDismountEvent
import zip.jespersen.xhappyspeed.Configs

object HappyGhastDismountEvent : Listener {
    val mm = MiniMessage.miniMessage()

    @EventHandler
    fun onEntityDismountEvent(event: EntityDismountEvent) {
        if (!Configs.config.data.enableResetGhastSpeed) return
        val realSize = event.dismounted.passengers.size - 1
        if (event.dismounted.type == EntityType.HAPPY_GHAST && realSize == 0) {

            XHappySpeedPlugin.instance.happyPassengers.remove(event.entity.entityId.toString())

            (event.dismounted as HappyGhast).getAttribute(Attribute.FLYING_SPEED)!!.baseValue = 0.05
            event.entity.sendActionBar(mm.deserialize(Configs.messages.data.resetGhastToBaseSpeed))
        }
    }

}