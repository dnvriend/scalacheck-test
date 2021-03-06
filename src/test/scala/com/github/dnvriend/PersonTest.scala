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

import akka.NotUsed
import akka.stream.scaladsl.Source
import com.github.dnvriend.generator.PersonGenerator
import com.github.dnvriend.generator.PersonGenerator.{ Person, arbitraryListPerson, arbitraryPersonRandom, arbitraryPersonSource }

class PersonTest extends TestSpec {
  it should "generate random persons" in
    forAll { (person: Person) ⇒
      person shouldBe Person(person.name, person.age, person.gender, person.salary, person.key, person.id)
    }

  it should "generate a random list of persons" in
    forAll { (xs: List[Person]) ⇒
      xs.foreach { person ⇒
        person shouldBe Person(person.name, person.age, person.gender, person.salary, person.key, person.id)
      }
    }

  it should "generate random list of persons" in
    forAll(PersonGenerator.genListPersons) { xs ⇒
      xs.foreach { person ⇒
        person shouldBe Person(person.name, person.age, person.gender, person.salary, person.key, person.id)
      }
    }

  it should "implicitly convert a Generator to an Iterator for use in a Source" in {
    import com.github.dnvriend.generator.GeneratorImplicits._
    val xs: List[Person] = Source.fromIterator(() ⇒ PersonGenerator.genPerson)
      .take(25)
      .runFold(List.empty[Person])(_ :+ _).futureValue

    xs should not be empty
    xs.size shouldBe 25
    xs.foreach(_ shouldBe a[Person])
  }

  it should "generate random Person Sources" in
    forAll { (src: Source[Person, NotUsed]) ⇒
      val xs: List[Person] = src.take(25).runFold(List.empty[Person])(_ :+ _).futureValue
      xs.foreach { person ⇒
        person shouldBe Person(person.name, person.age, person.gender, person.salary, person.key, person.id)
      }
    }

  it should "generate a random Person using Gen.resultOf[Person]" in {
    PersonGenerator.genPersonRandom.sample.value should matchPattern {
      case Person(_, _, _, _, _, _) ⇒
    }
  }
}
