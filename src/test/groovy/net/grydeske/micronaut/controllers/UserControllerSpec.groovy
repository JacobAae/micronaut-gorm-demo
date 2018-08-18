package net.grydeske.micronaut.controllers

import grails.gorm.transactions.Transactional
import io.micronaut.context.ApplicationContext
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
        List<User> users = client.exchange('/', User)

        then:
        users
    }

    void "test single user"() {
        when:
        User user = client.exchange("/${id}", User)

        then:
        user
    }

}