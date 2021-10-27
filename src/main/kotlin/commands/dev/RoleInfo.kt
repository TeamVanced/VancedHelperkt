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
    commandDescription = "Send the role info"
) {

    override suspend fun execute(
        ctx: CommandContext
    ) {
        ctx.respondPublic {
            embed {
                title = "Roles"
                description = """
                    Hello! This Channel is for info on roles available such as: 
                    - The Nitro Booster role
                    - Weeber role (grants you access to weeb-sht  and weeb emojis) 
                    - MCGamer role (grants you access to our minecraft server (play.vancedapp.com) chat in true-gamer-chat ) 
                    - Cursed role (grants you access to horrific chat, viewer discretion advised)
                    Also select for which kinds of devices you wish to receive pings for and access channels for, using the buttons below.
                """.trimIndent()
                color = Color(64, 78, 237)
                actionRow {
                    interactionButton(
                        style = ButtonStyle.Primary,
                        customId = "$commandName-797141785880035358"
                    ) {
                        label = "Android"
                        emoji = DiscordPartialEmoji(id = Snowflake(677222645292597266))
                    }
                    interactionButton(
                        style = ButtonStyle.Primary,
                        customId = "$commandName-797141920118997053"
                    ) {
                        label = "iOS"
                        emoji = DiscordPartialEmoji(id = Snowflake(797891150337933353))
                    }
                    interactionButton(
                        style = ButtonStyle.Primary,
                        customId = "$commandName-650741600846217217"
                    ) {
                        label = "Weeber"
                        emoji = DiscordPartialEmoji(id = Snowflake(651054603793858561))
                    }
                    interactionButton(
                        style = ButtonStyle.Primary,
                        customId = "$commandName-682204644268703746"
                    ) {
                        label = "MCGamer"
                        emoji = DiscordPartialEmoji(id = Snowflake(361948780926337036))
                    }
                    interactionButton(
                        style = ButtonStyle.Primary,
                        customId = "$commandName-798910044791767110"
                    ) {
                        label = "Cursed"
                        emoji = DiscordPartialEmoji(id = Snowflake(788530533549211668))
                    }
                }
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