package net.grydeske.micronaut.services

import grails.gorm.transactions.Rollback
import io.micronaut.context.ApplicationContext
import io.micronaut.runtime.server.EmbeddedServer
import net.grydeske.micronaut.domains.User
import org.grails.orm.hibernate.HibernateDatastore
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

@Rollback
class UserServiceSpec extends Specification {

    @Shared @AutoCleanup EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer)
    @Shared UserService service = embeddedServer.applicationContext.getBean(UserService)

/*
    void setupSpec() {
        Map configuration = [
                'hibernate.hbm2ddl.auto':'create-drop',
                'dataSource.url':'jdbc:h2:mem:myDB'
        ]
        HibernateDatastore datastore = new HibernateDatastore( configuration, User)
    }
*/

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
