package zip.jespersen.xhappyspeed

import kotlinx.serialization.Serializable
import zip.jespersen.korekt.config.ConfigType
import zip.jespersen.korekt.config.config
import java.io.File

object Configs {
    val messages = config<Messages>(File("plugins/XHappySpeed/messages.yml"), ConfigType.YAML) {
        load(Messages())
    }
    val config = config<Config>(File("plugins/XHappySpeed/config.yml"), ConfigType.YAML) {
        load(Config())
    }
}

@Serializable
data class Config(
    var commandName: String = "happy",
    var commandAliases: MutableList<String> = mutableListOf("xhappy"),
    var speedMin: Double = 0.1,
    var speedMax: Double = 0.5,
    var enableParking: Boolean = true,
    var enablePassengers: Boolean = true,
    var enableSpeed: Boolean = true,
    var enableResetGhastSpeed: Boolean = true,
    var enableSilent: Boolean = true,
)

@Serializable
data class Messages(
    var isNotSilent: String = "<color:#9cffc3>The yawn com Ghast can be heard again.</color>",
    var isSilent: String = "<color:#82c5ff>The ghast is now quiet.</color>",
    var speed: String = "<color:#57ff45>You have now set the speed to {speed}</color>",
    var passengerNotOwner: String = "<red>You are not a happy owner of a Ghast</red>",
    var noHappyGhastFound: String = "<red>No Happy Ghast found!</red>",
    var happyPassengerGuiTitle: String = "<gray>Happy Ghast Passengers</gray>",
    var successRemovedFromGhast: String = "<green>You removed {player} from the Happy Ghast</green>",
    var resetGhastToBaseSpeed: String = "<gray>No Passengers left! Set base speed for your Happy Ghast</gray>",
    var grantOwnerFromHappyGhast: String = "<gray>You are now the owner of the Happy Ghast!</gray>\n<gray>Use /happypassengers to manage players</gray>",
    var happyGhastUnPark: String = "<gray>Now your Happy Ghast can drive again!</gray>",
    var happyGhastParked: String = "<color:#57ff45>The Happy Ghast has now been parked.</color>",
    var commandHelpMessage: String = "Use this command to simply manage the Happy Ghast!",
    var commandHelpDescription: MutableList<String> = mutableListOf(
        "/happy silent - Mute or Unmute the Ghast Sounds",
        "/happy speed - Sets the current Fly Speed of the Ghast (0.0-0.5)",
        "/happy passengers - Opens a Passenger Manage Menu to kick passenger from the Ghast",
    )

)