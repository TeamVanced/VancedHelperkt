package commands.dev

import config
import core.command.CommandContext
import core.command.base.BaseCommand
import core.util.botOwners
import core.wrapper.applicationcommand.CustomApplicationCommandPermissionBuilder
import dev.kord.common.Color
import dev.kord.common.entity.ButtonStyle
import dev.kord.common.entity.DiscordPartialEmoji
import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.interaction.respondEphemeral
import dev.kord.core.entity.interaction.ButtonInteraction

class RoleInfo : BaseCommand(
    commandName = "roleinfo",
    commandDescription = "Send the role info",
    defaultPermissions = false
) {
    
    private data class EmojiButton(
        val label: String,
        val roleId: Long,
        val emojiId: Snowflake? = null,
        val emojiName: String? = null,
    )

    private val emojiRows = listOf(
        listOf(
            EmojiButton(
                label = "Android",
                roleId = 797141785880035358,
                emojiId = Snowflake(797147539941359616)
            ),
            EmojiButton(
                label = "iOS",
                roleId = 797141920118997053,
                emojiId = Snowflake(797891150337933353)
            ),
        ),
        listOf(
            EmojiButton(
                label = "Weeber",
                roleId = 650741600846217217,
                emojiId = Snowflake(903697212553453628)
            ),
            EmojiButton(
                label = "MCGamer",
                roleId = 682204644268703746,
                emojiId = Snowflake(361948780926337036)
            ),
            EmojiButton(
                label = "Cursed",
                roleId = 798910044791767110,
                emojiId = Snowflake(788530533549211668)
            ),
            EmojiButton(
                label = "Monkey",
                emojiName = "\uD83D\uDC12",
                roleId = 836255710362730578
            ),
        )
    )

    override suspend fun execute(
        ctx: CommandContext
    ) {
        ctx.respondPublic {
            embed {
                title = "Roles"
                description = """
                    Hello! This Channel is for info on roles available such as: 
                    - The Nitro Booster role
                    - Weeber role (grants you access to <#644156533046771722> and weeb emojis) 
                    - MCGamer role (grants you access to our minecraft server (play.vancedapp.com) chat in <#666690829376553001> ) 
                    - Cursed role (grants you access to horrific chat, viewer discretion advised)
                    - Monkey role (grants you access to the Vanced Public Beta Testing channel)
                    
                    Also select for which kinds of devices you wish to receive pings for and access channels for, using the buttons below.
                """.trimIndent()
                color = Color(64, 78, 237)
            }
            embed {
                title = "Nitro Boosters"
                description = """
                    Booster Benefits:
                    1. access to <#655495772405497895> and top-tier voice channel
                    2. access to betas (found pinned in <#358967484243640321> )
                    3. access to commands such as /colourme for custom role names and colours
                """.trimIndent()
                color = Color(217, 112, 221)
            }
            embed {
                title = "Other"
                description = """
                    Applications for certain roles such as tester, janitor and media team can be sent to <#837029449181429800> after reading the requirements carefully. 
                """.trimIndent()
                color = Color(88, 101, 242)
            }
            emojiRows.forEach { row ->
                actionRow {
                    row.forEach { emojiButton ->
                        interactionButton(
                            style = ButtonStyle.Primary,
                            customId = "$commandName-${emojiButton.emojiId}"
                        ) {
                            label = emojiButton.label
                            emoji = DiscordPartialEmoji(
                                id = emojiButton.emojiId,
                                name = emojiButton.emojiName
                            )
                        }
                    }
                }
            }
        }
    }

    override suspend fun onButtonInteraction(
        interaction: ButtonInteraction
    ) {
        val roleId = interaction.componentId.substringAfter("$commandName-")

        val guild = interaction.kord.getGuild(config.guildSnowflake)

        if (guild == null) {
            interaction.respondEphemeral {
                content = "Failed to retrieve Guild"
            }
            return
        }

        val role = guild.getRoleOrNull(Snowflake(roleId))

        if (role == null) {
            interaction.respondEphemeral {
                content = "Failed to retrieve a role with ID $roleId"
            }
            return
        }

        val member = interaction.user.asMemberOrNull(config.guildSnowflake)

        if (member == null) {
            interaction.respondEphemeral {
                content = "Failed to retrieve the member"
            }
            return
        }

        if (member.roleIds.contains(role.id)) {
            member.removeRole(role.id, "RoleInfo")
            interaction.respondEphemeral {
                content = "Successfully removed ${role.mention} from ${member.mention}"
            }
        } else {
            member.addRole(role.id, "RoleInfo")
            interaction.respondEphemeral {
                content = "Successfully assigned ${role.mention} to ${member.mention}"
            }
        }
    }

    override fun commandPermissions() =
        CustomApplicationCommandPermissionBuilder(
            permissions = {
                for (owner in botOwners) {
                    user(
                        id = Snowflake(owner),
                        allow = true
                    )
                }
            }
        )

}