package zip.jespersen.xhappyspeed.events

import zip.jespersen.xhappyspeed.XHappySpeedPlugin
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.entity.EntityType
import org.bukkit.entity.HappyGhast
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.persistence.PersistentDataType
import zip.jespersen.xhappyspeed.Configs

object PlayerInteractEvent : Listener {

    val key = NamespacedKey(XHappySpeedPlugin.instance, "parked")

    @EventHandler
    fun onInteract(event: PlayerInteractAtEntityEvent) {
        if (!Configs.config.data.enableParking) return
        val player = event.player
        val mm = MiniMessage.miniMessage()
        val target = event.rightClicked

        if (player.isSneaking && target.type == EntityType.HAPPY_GHAST) {
            if (event.hand === EquipmentSlot.HAND) return

            val flySpeed = (target as HappyGhast).getAttribute(Attribute.FLYING_SPEED)

            if (target.persistentDataContainer.get(key, PersistentDataType.BOOLEAN) == true) {
                flySpeed!!.baseValue = 0.05
                XHappySpeedPlugin.instance.setParked(false, target)
                return event.player.sendActionBar(mm.deserialize(Configs.messages.data.happyGhastUnPark))
            } else {
                flySpeed!!.baseValue = 0.0
                XHappySpeedPlugin.instance.setParked(true, target)
                return event.player.sendActionBar(mm.deserialize(Configs.messages.data.happyGhastParked))
            }
        }
    }


}