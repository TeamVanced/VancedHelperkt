package commands.vanced

import commands.BasePagerCommand
import commands.CommandTypes.Vanced
import net.dv8tion.jda.api.entities.MessageEmbed

class Troubleshoot : BasePagerCommand(
    commandName = "troubleshoot",
    commandDescription = "Troubleshoot Vanced issues. For your own issues, consult your therapist ;)",
    commandType = Vanced,
    commandAliases = listOf("ts")
) {

    override val totalEmbedPages: Int
        get() = 8

    override val embedPages: List<MessageEmbed>
        get() = listOf(
            embedBuilder.apply {
                setTitle("MIUI")
                setDescription(
                    "You might run into issues while using Vanced, as MiUi is known to cause trouble. To solve this, disable MiUi optimisation:\n" +
                    ":one: Enable Developer options (`Settings > About Phone (or something similar) > Tap Build Number 7 times`) and enter the developer settings menu\n" +
                    ":two: Scroll down until you see `Turn on MIUI optimization`\n" +
                    ":three: Disable it"
                )
            }.build(1),
            embedBuilder.apply {
                setTitle("Dark Splashscreen")
                setDescription("To get a dark loading screen, enable dark theme in your system settings. If you don't have this setting, find an app that does it for you on the Google Play Store")
            }.build(2),
            embedBuilder.apply {
                setTitle("Disable 60fps playback")
                setDescription(
                    "You can disable 60fps playback in 3 simple steps\n" +
                    ":one: Head over to the vanced settings and tap `about` 7 times to enable the hidden menu\n" +
                    ":two: Go to codec settings\n" +
                    ":three: Set `Override Model` to `sm - t520` and `Override Manufacturer` to `Samsung`"
                )
            }.build(3),
            embedBuilder.apply {
                setTitle("Notifications")
                setDescription("Sometimes you may not recieve notifications from Vanced, watch this guide for a solution to that issue. https://youtu.be/Q-5YFmFWVok")
            }.build(4),
            embedBuilder.apply {
                setTitle("No connection/Vanced broken after password change")
                setDescription("Watch this guide for the solution to the error https://youtu.be/S5sTXRTrD5Y")
            }.build(5),
            embedBuilder.apply {
                setTitle("Smartphones are turning back into dumbphones")
                setDescription(
                    "To squeeze a little extra battery out of your phone, Vendors implement aggresive Battery savers that kill tasks.\n" +
                    "MicroG was killed by your battery saver. That's why Vanced is stuck. To solve this issue, follow the guide below."
                )
                addField(
                    "Stock Android (or close to it)",
                    "Open your settings app and navigate to\n" +
                    "Apps & Notifications > See all apps > Vanced Microg > Battery > Battery Optimisation > Dropdown menu > All apps" +
                    "Locate Vanced MicroG and set it to `Don't optimise`, then reboot.\n" +
                    "If the issue still persists, do the same for Youtube Vanced.",
                    false
                )
                addField(
                    "If the above doesn't work for you",
                    "Due to the many different Android Roms, this varies. [Visit this site](https://dontkillmyapp.com/), navigate to your vendor and follow the guide.",
                    false
                )
            }.build(6),
            embedBuilder.apply {
                setTitle("YouTube Vanced is draining more battery than stock YouTube")
                setDescription(
                    "This might be caused by the current method used to hide home ads, to fix go to `Vanced Settings > Ad settings > And enable home ads`\n" +
                    "A better method to hide home ads might be available in a future update"
                )
            }.build(7),
            embedBuilder.apply {
                setTitle("Picture-in-Picture not working")
                setDescription("The way PiP works is controlled by your operating system. If it's broken, there's nothing we can do, so please don't report PiP related issues.")
            }.build(8)
        )

}