package net.grydeske.micronaut.controllers

import grails.gorm.transactions.Transactional
import io.micronaut.context.ApplicationContext
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.RxHttpClient
import io.micronaut.runtime.server.EmbeddedServer
import net.grydeske.micronaut.domains.User
import net.grydeske.micronaut.services.UserService
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

class UserControllerSpec extends Specification {

    @Shared @AutoCleanup EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer)
    @Shared UserService userService = embeddedServer.applicationContext.getBean(UserService)
    @Shared Long id

    @Shared
    @AutoCleanup
    RxHttpClient client = embeddedServer.applicationContext.createBean(RxHttpClient, embeddedServer.getURL())


    @Transactional
    void setupSpec() {
        id = userService.save("William", "Adama", "wa@galactica.space").id
        userService.save("Kara", "Trace", "starbuck@galactica.space")
    }

    @Transactional
    void cleanupSpec() {
        User.list()*.delete()
    }

    void "test list"() {
        when:
        HttpRequest request = HttpRequest.GET("/")
        List<User> users = client.toBlocking().retrieve(request, Argument.of(List, User))

        then:
        users
        users.size() == 2
    }

    void "test single user"() {
        when:
        User user = client.toBlocking().exchange("/${id}", User).body()

        then:
        user
    }

    void "test static user"() {
        when:
        User user = client.toBlocking().exchange("/user/static", User).body()

        then:
        user
        user.firstname
    }

}