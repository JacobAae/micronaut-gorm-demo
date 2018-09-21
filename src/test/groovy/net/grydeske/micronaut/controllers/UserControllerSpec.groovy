package net.grydeske.micronaut.controllers

import io.micronaut.context.ApplicationContext
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.RxHttpClient
import io.micronaut.runtime.server.EmbeddedServer
import net.grydeske.micronaut.domains.User
import net.grydeske.micronaut.services.UserService
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.spock.Testcontainers
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

@Testcontainers
class UserControllerSpec extends Specification {

    @Shared
    PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer()

    @Shared @AutoCleanup EmbeddedServer embeddedServer
    @Shared UserService service

    @Shared
    @AutoCleanup
    RxHttpClient client

    void setupSpec() {
        println "${postgreSQLContainer.getJdbcUrl()}"
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
        client = embeddedServer.applicationContext.createBean(RxHttpClient, embeddedServer.getURL())
    }

    void "test list"() {
        setup:
        service.save("Kara", "Trace", "starbuck@galactica.space")

        when:
        HttpRequest request = HttpRequest.GET("/")
        List<User> users = client.toBlocking().retrieve(request, Argument.of(List, User))

        then:
        users
        users.size() == 1
        users.first().firstname == "Kara"
    }

    void "test single user"() {
        setup:
        Long id = service.save("William", "Adama", "wa@galactica.space").id

        when:
        User user = client.toBlocking().exchange("/${id}", User).body()

        then:
        user
        user.firstname == "William"
    }
}