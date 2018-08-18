package net.grydeske.micronaut.init

import groovy.util.logging.Slf4j
import io.micronaut.context.annotation.Requires
import io.micronaut.context.env.Environment
import io.micronaut.context.event.ApplicationEventListener
import io.micronaut.runtime.server.event.ServerStartupEvent
import net.grydeske.micronaut.domains.User
import org.grails.orm.hibernate.HibernateDatastore

import javax.inject.Singleton

@Slf4j
@Singleton
@Requires(env = Environment.TEST)
class TestDataLoader implements ApplicationEventListener<ServerStartupEvent> {

    void onApplicationEvent(ServerStartupEvent event) {
        Map configuration = [
                'hibernate.hbm2ddl.auto':'create-drop',
                'dataSource.url':'jdbc:h2:mem:myDB'
        ]
        new HibernateDatastore( configuration, User)
    }
}
