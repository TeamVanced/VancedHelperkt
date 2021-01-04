import commandhandler.CommandListener
import errorhandler.ErrorListener
import eventhandler.ActionListener
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.MemberCachePolicy

class Main {

    fun start() {
        jda = JDABuilder.createDefault(config.token)
            .addEventListeners(ActionListener(), CommandListener(), ErrorListener())
            .setMemberCachePolicy(MemberCachePolicy.ALL)
            .enableIntents(GatewayIntent.GUILD_MEMBERS)
            .setActivity(Activity.listening("${defaultPrefix}help"))
            .build()
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            Main().start()
        }

    }

}