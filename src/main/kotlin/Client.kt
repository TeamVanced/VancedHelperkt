import config.Config
import net.dv8tion.jda.api.JDA

val config = Config(
    token = System.getenv("BOT_TOKEN"),
    coinlibToken = System.getenv("COINLIB_TOKEN"),
    mongoString = System.getenv("MONGO_STRING"),
    genderToken = System.getenv("GENDER_TOKEN")
)

var jda: JDA? = null

const val defaultPrefix = "-"