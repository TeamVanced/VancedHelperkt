package commands.vanced

import commands.BasePagerCommand
import commands.CommandTypes.Vanced
import net.dv8tion.jda.api.entities.MessageEmbed

class BugReport : BasePagerCommand(
    commandName = "bugreport",
    commandDescription = "Report Vanced bugs. For broken toasters and such, consult your local Indian tech guru ;P",
    commandType = Vanced,
    commandAliases = listOf("bug")
) {

    override val totalEmbedPages: Int
        get() = 2

    override val embedPages: List<MessageEmbed>
        get() = listOf(
            embedBuilder.apply {
                setTitle("Report a regular bug")
                setDescription(
                    "Modifying an app is not an easy job, sometimes applying a modification can cause an unexpected bug and we can't catch all of them.\n" +
                    "That's where you come in! If you experience a bug and want to help us improve Vanced, you would help a ton by providing a logcat.\n" +
                    "A logcat is a log of all processes on your system. This helps a lot with identifying the problem. This is however a bit inconvenient and requires a pc.\n" +
                    "If you do have one, please take the time and take one to help us fix this!"
                )
                addField(
                    "On your Phone",
                    ":one: Open your settings app and go to `About Phone/Phone Info` and click your build number 7 times.\n" +
                    ":two: Now go back to the settings main menu.\n" +
                    ":three: You will now either have a new tab `Developer Options` in this menu or in the system menu.\n" +
                    ":four: Open Developer Options and enable `USB-Debugging`.",
                    false
                )
                addField(
                    "On your PC/Laptop",
                    ":one: [Visit this website](https://developer.android.com/studio/releases/platform-tools) and download the tools for your operating system.\n" +
                    ":two: Extract them.\n" +
                    ":three: Open the extracted folder to find many `.exe` files.\n" +
                    ":four: `Shift + Rightclick` the background of the extracted folder and select `Run command prompt / powershell here`.\n" +
                    ":five: Plug your phone into your PC, unlock it and grant USB-Debugging Permission.\n" +
                    ":six: Type `adb devices`. If your phone is shown, proceed. If not, google for `[yourBrandHere] usb drivers` and install them.\n" +
                    ":seven: Type `adb logcat -c`, then `adb logcat *:W > logcat.txt`.\n" +
                    ":eight: Open your Vanced, reproduce your bug and then close the command prompt.\n" +
                    ":nine: Now you will find a new file `logcat.txt` in the folder. Open it, scan it quickly to remove confidential info.\n" +
                    "Press `CTRL + A` => `CTRL + C`, open https://hastebin.com/, paste it and save. Use the output link for your bug report.\n",
                    false
                )
            }.build(1),
            embedBuilder.apply {
                setTitle("Report Theme bugs")
                setDescription("This guide will show you how to report Theme based bugs.\nIn order to report bugs, you will need [Developer Assistant](https://play.google.com/store/apps/details?id=com.appsisle.developerassistant)")
                addField(
                    "Before you continue",
                    "Please read info in <#663348498389008384> to completely understand how to report a bug, otherwise your report won't be viewed. Don't forget to check <#663158441879142410> to see if your bug has already been reported",
                    false
                )
                addField(
                    "Let's get started",
                    "After you have downloaded `Developer Assistant`, open Vanced and find the bug, then open layoutviewer by holding down your home button until the app launches.\n" +
                    "Now follow these steps:\n" +
                    ":one: Make sure the bug you're experiencing is on the screen\n" +
                    ":two: Tap on the element that you think is affected by a bug\n" +
                    ":three: Go to the `Element` and `Hierarchy` tabs and screenshot the output\n" +
                    ":four: Now head over to <#663170881572438036> and report the bug",
                    false
                )
                addField(
                    "When will my bug be reviewed?",
                    "Your feedback will be reviewed as soon as possible. This should ususally not take lonegr than 6 hours. Thanks for understanding!",
                    false
                )
            }.build(2)
        )

}