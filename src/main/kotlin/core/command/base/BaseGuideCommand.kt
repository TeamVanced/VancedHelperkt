package core.command.base

import core.command.CommandContext
import core.util.randomColor
import dev.kord.common.annotation.KordPreview
import dev.kord.core.behavior.interaction.edit
import dev.kord.core.entity.interaction.SelectMenuInteraction
import dev.kord.rest.builder.message.EmbedBuilder
import dev.kord.rest.builder.message.modify.embed
import domain.model.guide.GuideSingleJsonModel
import ext.takeMax
import org.koin.core.component.inject
import repository.guide.GuideRepository

@OptIn(KordPreview::class)
abstract class BaseGuideCommand(
    commandName: String,
    commandDescription: String,
    private val jsonName: String,
) : BaseCommand(
    commandName = commandName,
    commandDescription = commandDescription,
) {

    private val repository by inject<GuideRepository>()

    private lateinit var jsonData: List<GuideSingleJsonModel>

    override suspend fun preInit() {
        jsonData = repository.fetch(
            jsonName = jsonName,
            language = "en" //TODO support multiple languages
        ).data
    }

    override suspend fun execute(ctx: CommandContext) {
        ctx.respondPublic {
            embed {
                buildGuideEmbed(0)
            }

            if (jsonData.size > 1) {
                actionRow {
                    selectMenu(
                        customId = "${commandName}-selectmenu",
                        builder = {
                            placeholder = "Select page"
                            jsonData.forEachIndexed { index, jsonData ->
                                option(
                                    label = "${index + 1}. ${jsonData.title}".takeMax(25),
                                    value = index.toString(),
                                )
                            }
                        }
                    )
                }
            }
        }
    }

    override suspend fun onSelectMenuInteraction(
        interaction: SelectMenuInteraction
    ) {
        val index = interaction.values.first().toInt()
        interaction.acknowledgePublicDeferredMessageUpdate().edit {
            embed {
                buildGuideEmbed(index)
                color = randomColor
            }
        }
    }

    private fun EmbedBuilder.buildGuideEmbed(index: Int) {
        val currentData = jsonData[index]
        title = currentData.title
        description = currentData.description
        currentData.fields?.forEach { field ->
            field {
                name = field.title
                value = field.content
            }
        }
        if (jsonData.size > 1) {
            footer {
                text = "Page ${index + 1}/${jsonData.size}"
            }
        }
    }

}