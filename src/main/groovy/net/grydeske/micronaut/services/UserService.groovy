package net.grydeske.micronaut.services

import grails.gorm.transactions.Transactional
import net.grydeske.micronaut.domains.User

import javax.inject.Singleton

@Transactional
@Singleton
class UserService {

    Integer count() {
        User.count()
    }

    User save(String firstname, String lastname, String email) {
        User user = new User(
                firstname: firstname,
                lastname: lastname,
                email: email
        )
        user.save()

        user
    }

    User find(Long id) {
        User.get(id)
    }

    List<User> list() {
        User.list()
    }
}
