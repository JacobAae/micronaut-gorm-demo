package net.grydeske.micronaut.controllers

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import net.grydeske.micronaut.domains.User
import net.grydeske.micronaut.services.UserService

@Controller("/")
class UserController {

    final UserService userService

    UserController(UserService userService) {
        this.userService = userService
    }

    @Get("/")
    List<User> list() {
        userService.list()
    }

    @Get("/user/static")
    User showStatic() {
        new User(
                firstname: 'Gaius',
                lastname: 'Baltar',
                email: 'gaius@baltar.space'
        )
    }

    @Get("/{id}")
    User show(Long id) {
        userService.find(id)
    }


}