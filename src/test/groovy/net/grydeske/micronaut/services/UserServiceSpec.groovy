package net.grydeske.micronaut.services

import grails.gorm.transactions.Rollback
import io.micronaut.context.ApplicationContext
import io.micronaut.runtime.server.EmbeddedServer
import net.grydeske.micronaut.domains.User
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

@Rollback
class UserServiceSpec extends Specification {

    @Shared @AutoCleanup EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer)
    @Shared UserService service = embeddedServer.applicationContext.getBean(UserService)



    void "service"() {
        setup:
        println "SERVICE: $service"

        expect:
        true
    }

    void "initially no users"() {
        expect:
        service.count() == 0
    }

    void "saving a user without errors increments count"() {
        when:
        User user = service.save('Laura', 'Roslin', 'poc@colonialone.space')

        then:
        !user.hasErrors()

        and:
        service.count() == old(service.count()) + 1

    }


}
