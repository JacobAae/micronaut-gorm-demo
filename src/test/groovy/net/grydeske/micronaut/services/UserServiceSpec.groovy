package net.grydeske.micronaut.services

import grails.gorm.transactions.Rollback
import grails.gorm.transactions.Transactional
import io.micronaut.context.ApplicationContext
import io.micronaut.runtime.server.EmbeddedServer
import net.grydeske.micronaut.domains.User
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.spock.Testcontainers
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

@Rollback
@Testcontainers
class UserServiceSpec extends Specification {

    @Shared
    PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer()

    @Shared @AutoCleanup EmbeddedServer embeddedServer
    @Shared UserService service

    void setupSpec() {
        embeddedServer = ApplicationContext
                .build()
                .properties(
                    'dataSource.url':"${postgreSQLContainer.getJdbcUrl()}",
                    'dataSource.username':"${postgreSQLContainer.username}",
                    'dataSource.password':"${postgreSQLContainer.password}",
                )
                .packages(
                'net.grydeske.micronaut.controllers',
                'net.grydeske.micronaut.domains',
                'net.grydeske.micronaut.init',
                'net.grydeske.micronaut.services',
        )
                .environments('test')
                .run(EmbeddedServer)
        service = embeddedServer.applicationContext.getBean(UserService)
    }

    void "initially no users"() {
        expect:
        service.count() == 0
    }

    @Transactional
    void "saving a user without errors increments count"() {
        when:
        User user = service.save('Laura', 'Roslin', 'poc@colonialone.space')

        then:
        !user.hasErrors()

        and:
        service.count() == old(service.count()) + 1
    }
}
