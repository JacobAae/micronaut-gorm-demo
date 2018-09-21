package net.grydeske.micronaut.domains

import grails.gorm.annotation.Entity
import org.grails.datastore.gorm.GormEntity

@Entity
class User implements GormEntity<User> {

    String firstname
    String lastname
    String email

    static constraints = {
        firstname nullable: false, blank: false, unique: true
        lastname nullable: false, blank: false, password: true
        email nullable: false, blank: false, unique: true
    }

    @Override
    String toString() {
        "$firstname $lastname $email"
    }

    static mapping = {
        table 'myuser'
    }

}
