package commands.vanced

import core.command.CommandContext
import core.command.base.BaseCommand
class SupportUs : BaseCommand(
    commandName = "supportus",
    commandDescription = "Learn how to support us",
) {

    override suspend fun execute(
        ctx: CommandContext
    ) {
        ctx.respondPublic {
            embed {
                title = "How to support us"
                description = "Vanced does not take donations! This means, that any donation links you might find are fake!\\n\\nAs mentioned above, Vanced does not take donations, but there's other way to support us, that you can find below."
                field("Nitro Boost") {
                    """
                        If you own a [Discord Nitro Subscription](https://discordapp.com/nitro), you can boost our Server, which will give us Perks like a custom invite link [(discord.gg/vanced)](https://discord.gg/vanced), a custom server banner, more emotes etc.
                        You yourself will also get some Perks:
                        - Youtube Vanced Beta
                        - Custom role (and colour)
                    """.trimIndent()
                }
                field {
                    name = "Brave Referral Link"
                    value = "If you are planning to download Brave Browser, [do it via our referral link](https://vanced.app/brave)."
                }
                field("Do not ask unnecessary questions") {
                    """
                        Before asking for support or reporting a bug, please see if your problem has a solution on our bot and use the discord search tool.
                        This way, 95% of the problems can be solved without us having to waste time on explaining the same thing over and over.
                    """.trimIndent()
                }
                field {
                    name = "Be a nice member of our community"
                    value = "If you like what we are doing, consider hanging out on our Discord or Telegram, chatting with other users, helping users with Vanced issues, etc."
                }
                thumbnail {
                    url = "https://i.imgur.com/mFkZnUB.png"
                }
            }
        }
    }
}