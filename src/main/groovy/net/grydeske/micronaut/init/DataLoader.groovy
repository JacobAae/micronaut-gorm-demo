package net.grydeske.micronaut.init

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import io.micronaut.context.annotation.Requires
import io.micronaut.context.env.Environment
import io.micronaut.context.event.ApplicationEventListener
import io.micronaut.runtime.server.event.ServerStartupEvent
import net.grydeske.micronaut.services.UserService

import javax.inject.Singleton

@Slf4j
@CompileStatic
@Singleton
@Requires(notEnv = Environment.TEST)
class DataLoader implements ApplicationEventListener<ServerStartupEvent> {

    final UserService userService

    DataLoader(UserService userService) {
        this.userService = userService
    }

    @Override
    void onApplicationEvent(ServerStartupEvent event) {
        if (!userService.count()) {
            log.debug "Loading sample users"
            userService.save("William", "Adama", "admiral@12colonies.com")
            userService.save("Laura", "Roslin", "president@12colonies.com")
            userService.save("Saul", "Tigh", "xo@galactica.space")
        }
    }
}
