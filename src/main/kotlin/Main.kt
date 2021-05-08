import commandhandler.CommandListener
import di.commandManagerModule
import di.mapperModule
import di.repositoryModule
import di.retrofitModule
import eventhandler.ActionListener
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.MemberCachePolicy
import org.koin.core.context.startKoin

class Main {

    fun start() {
        jda = JDABuilder.createDefault(config.token)
            .addEventListeners(ActionListener(), CommandListener())
            .setMemberCachePolicy(MemberCachePolicy.ALL)
            .enableIntents(GatewayIntent.GUILD_MEMBERS)
            .setActivity(Activity.listening("${defaultPrefix}help"))
            .build()
    }

    companion object {

        @JvmStatic
        fun main(vararg args: String) {

            startKoin {
                modules(
                    commandManagerModule,
                    retrofitModule,
                    mapperModule,
                    repositoryModule
                )
            }

            Main().start()
        }

    }

}