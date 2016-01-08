/*
 * Copyright 2016 Dennis Vriend
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.dnvriend

import com.github.dnvriend.generator.PersonGenerator
import com.github.dnvriend.generator.PersonGenerator.Person

class PersonTest extends TestSpec {
  it should "generate random persons" in {
    forAll(PersonGenerator.genPerson) { person ⇒
      person shouldBe Person(person.name, person.age, person.gender, person.salary, person.key, person.id)
    }
  }

  it should "generate a random list of persons" in {
    val xs: List[Person] = PersonGenerator.randomPersons
    xs.foreach { person ⇒
      person shouldBe Person(person.name, person.age, person.gender, person.salary, person.key, person.id)
    }
  }

  it should "generate random list of persons" in {
    forAll(PersonGenerator.genListPersons) { xs ⇒
      xs.foreach { person ⇒
        person shouldBe Person(person.name, person.age, person.gender, person.salary, person.key, person.id)
      }
    }
  }
}
