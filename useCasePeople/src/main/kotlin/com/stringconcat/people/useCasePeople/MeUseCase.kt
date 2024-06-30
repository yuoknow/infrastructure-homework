package com.stringconcat.people.useCasePeople

import com.stringconcat.people.businessPeople.Person
import java.time.LocalDate
import java.util.UUID
import javax.inject.Named

@Named
class MeUseCase(
    private val persistPerson: PersistPerson,
) {
    operator fun invoke(): Person {
        val id = UUID.fromString("29f4d7e3-fd7c-4664-ad07-763326215ec4")
        val me =
            Person(
                id = UUID.fromString("29f4d7e3-fd7c-4664-ad07-763326215ec4"),
                firstName = "Sergey",
                secondName = "Bukharov",
                birthDate = LocalDate.of(1987, 12, 1),
                sex = Person.Sex.MAN,
                avatartUrl = "https://avatars.dicebear.com/v2/male/my-somffething.svg",
                favoriteQuote = "make the easy things easy, and the hard things possible",
            )
        persistPerson.persist(me)
        return me
    }
}
