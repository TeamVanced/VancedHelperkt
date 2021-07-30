import configuration.Config

val config = Config(
    token = System.getenv("BOT_TOKEN"),
    guildId = System.getenv("GUILD_ID"),
    coinlibToken = System.getenv("COINLIB_TOKEN"),
    mongoString = System.getenv("MONGO_STRING"),
    genderToken = System.getenv("GENDER_TOKEN"),
)