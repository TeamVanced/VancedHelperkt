package core.wrapper.applicationcommand

import dev.kord.rest.builder.interaction.ApplicationCommandPermissionsModifyBuilder

data class CustomApplicationCommandPermissionBuilder(
    val permissions: ApplicationCommandPermissionsModifyBuilder.() -> Unit = {}
)
