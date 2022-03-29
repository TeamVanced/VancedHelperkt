package core.command

import dev.kord.core.entity.*
import dev.kord.core.entity.channel.ResolvedChannel

class CommandArguments(
    val strings: Map<String, String>,
    val integers: Map<String, Long>,
    val numbers: Map<String, Double>,
    val booleans: Map<String, Boolean>,
    val users: Map<String, User>,
    val members: Map<String, Member>,
    val channels: Map<String, ResolvedChannel>,
    val roles: Map<String, Role>,
    val mentionables: Map<String, Entity>,
    val attachments: Map<String, Attachment>,
)