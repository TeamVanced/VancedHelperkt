package commands.vanced

import commands.BasePagerCommand
import commands.CommandTypes.Vanced
import net.dv8tion.jda.api.entities.MessageEmbed

class Info : BasePagerCommand(
    commandName = "info",
    commandDescription = "Vanced FAQ",
    commandType = Vanced,
    commandAliases = listOf("faq")
) {

    override val totalEmbedPages: Int
        get() = 3

    override val embedPages: List<MessageEmbed>
        get() = listOf(
            embedBuilder.apply {
                setThumbnail("https://i.imgur.com/NYmMUq5.png")
                setTitle("Downloading Videos")
                setDescription(
                    "Downloading videos has never been and will never be a feature of Vanced. It led to the shutdown of many Youtube Mods in the past.[In some regions](https://support.google.com/youtube/answer/6141269?co=GENIE.Platform%3DAndroid&hl=en) however, downloading is free.\n\n" +
                    "For downloading, use a third party tool like [Ymusic](https://ymusic.io)."
                )
            }.build(1),
            embedBuilder.apply {
                setImage("https://i.imgur.com/IIJcQ4Z.png")
                setThumbnail("https://i.imgur.com/6xeelhB.png")
                setTitle("No, you won't get banned for using Vanced.")
                setDescription(
                    "No, Youtube's new ToS do not state that your account might be removed due to not being commercially available. In other words, Youtube will not ban you for using Adblock or Vanced.\n\n" +
                    "However, you  use Vanced at your own discretion. Seeing as it is an unofficial client and as we can't predict what Youtube will do in the future, there's always a very slight risk, and we can't guarantee your account's safety."
                )
            }.build(2),
            embedBuilder.apply {
                setThumbnail("https://i.imgur.com/gvNSmzo.png")
                setTitle("Casting to TV")
                setDescription(
                    "Casting to TV doesn't actually cast, it just makes your TV open its Youtube app and play the video there.\n" +
                    "This means that casting to TV will use the standard Youtube player which has ads and lacks the Vanced modifications.\n" +
                    "There's nothing Vanced can do about this and this is **not a bug**."
                )
            }.build(3)
        )

}