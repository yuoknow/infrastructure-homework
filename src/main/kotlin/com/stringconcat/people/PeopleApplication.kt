package com.stringconcat.people

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@ComponentScan(value = ["com.stringconcat.people"])
@SpringBootApplication
class PeopleApplication

fun main(args: Array<String>) {
    runApplication<PeopleApplication>(args = args)
}
