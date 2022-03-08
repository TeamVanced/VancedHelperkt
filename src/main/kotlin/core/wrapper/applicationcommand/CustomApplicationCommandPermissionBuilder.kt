package core.wrapper.applicationcommand

import dev.kord.rest.builder.interaction.ApplicationCommandPermissionsModifyBuilder

class CustomApplicationCommandPermissionBuilder(
    val permissions: ApplicationCommandPermissionsModifyBuilder.() -> Unit = {}
)
