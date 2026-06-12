package zip.jespersen.xhappyspeed.commands

import dev.jorel.commandapi.kotlindsl.commandTree
import dev.jorel.commandapi.kotlindsl.doubleArgument
import dev.jorel.commandapi.kotlindsl.literalArgument
import dev.jorel.commandapi.kotlindsl.playerExecutor
import zip.jespersen.xhappyspeed.XHappySpeedPlugin
import net.crystopia.crystalshard.paper.custom.smart.CustomGUI.Companion.openInventory
import net.crystopia.crystalshard.paper.custom.smart.smartGUI
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.EntityType
import org.bukkit.entity.HappyGhast
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import zip.jespersen.xhappyspeed.Config
import zip.jespersen.xhappyspeed.Configs
import java.util.*

object Happy {

    val mm = MiniMessage.miniMessage()

    val command = commandTree(Configs.config.data.commandName, "xhappyspeed") {
        withPermission("xhappyspeed.command.xhappyspeed")
        Configs.config.data.commandAliases.forEach { withAliases(it) }
        withHelp(
            Configs.messages.data.commandHelpMessage,
            Configs.messages.data.commandHelpDescription.joinToString("\n"),
        )


        if (Configs.config.data.enableSilent)
            literalArgument("silent") {
                withPermission("xhappyspeed.command.silent")
                playerExecutor { sender, args ->
                    val targets = sender.location.getNearbyEntities(10.0, 10.0, 10.0)
                    targets.forEach { target ->
                        if (target.type == EntityType.HAPPY_GHAST && target is HappyGhast) {
                            if (target.isSilent) {
                                target.isSilent = false
                                sender.sendActionBar(mm.deserialize(Configs.messages.data.isNotSilent))
                            } else {
                                target.isSilent = true
                                sender.sendActionBar(mm.deserialize(Configs.messages.data.isSilent))
                            }
                        }

                    }
                }
            }
        if (Configs.config.data.enableSpeed)
            literalArgument("speed") {
                withPermission("xhappyspeed.command.speed")
                doubleArgument("speed", Configs.config.data.speedMin, Configs.config.data.speedMax) {
                    playerExecutor { sender, args ->

                        val targets = sender.location.getNearbyEntities(10.0, 10.0, 10.0)
                        targets.forEach { target ->
                            if (target.type == EntityType.HAPPY_GHAST && target is HappyGhast) {
                                target.getAttribute(Attribute.FLYING_SPEED)?.baseValue = args[0] as Double
                                XHappySpeedPlugin.instance.setSpeed(args[0] as Double, target)
                                sender.sendActionBar(
                                    mm.deserialize(
                                        Configs.messages.data.speed.replace(
                                            "{speed}",
                                            args[0].toString()
                                        )
                                    )
                                )
                            }
                        }
                    }
                }
            }
        if (Configs.config.data.enablePassengers)
            literalArgument("passengers") {
                withPermission("xhappyspeed.command.passengers")
                playerExecutor { sender, args ->
                    val entityId = XHappySpeedPlugin.instance.happyPassengers[sender.entityId.toString()]

                    if (entityId == null) {
                        sender.sendActionBar(mm.deserialize(Configs.messages.data.passengerNotOwner))
                    } else {
                        val entity = Bukkit.getServer().getEntity(UUID.fromString(entityId))
                        if (entity == null) {
                            sender.sendActionBar(mm.deserialize(Configs.messages.data.noHappyGhastFound))
                        } else {
                            if (entity.type != EntityType.HAPPY_GHAST) {
                                sender.sendActionBar(mm.deserialize(Configs.messages.data.noHappyGhastFound))
                            } else {
                                val gui = smartGUI(mm.deserialize(Configs.messages.data.happyPassengerGuiTitle)) {
                                    val happyGhast = entity as HappyGhast

                                    var slot = 0

                                    happyGhast.passengers.forEach { player ->
                                        player as Player

                                        if (player.uniqueId != sender.uniqueId) {
                                            set(slot, ItemStack(Material.PLAYER_HEAD).apply {
                                                slot += 1
                                                val meta = itemMeta as SkullMeta
                                                meta.displayName(mm.deserialize("<gray>${player.name}</gray>"))
                                                meta.owningPlayer = player

                                                meta.playerProfile = player.playerProfile
                                                itemMeta = meta
                                            }) {
                                                isCancelled = true
                                                happyGhast.removePassenger(player)
                                                player.teleport(
                                                    Location(
                                                        happyGhast.location.world,
                                                        happyGhast.location.x,
                                                        happyGhast.location.y - 5,
                                                        happyGhast.location.z
                                                    )
                                                )
                                                viewer.sendActionBar(
                                                    mm.deserialize(
                                                        Configs.messages.data.successRemovedFromGhast.replace(
                                                            "{player}",
                                                            player.name
                                                        )
                                                    )
                                                )
                                            }
                                        }

                                    }

                                }
                                sender.openInventory(gui)
                            }
                        }
                    }
                }
            }
    }
}