package com.stringconcat.people.useCasePeople

import com.stringconcat.people.businessPeople.Person
import java.util.UUID

interface GetPerson {
    fun get(id: UUID): Person?
}
