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

package com.github.dnvriend.generator

import java.util.UUID

import org.scalacheck.{ Arbitrary, Gen }

object PersonGenerator extends StandardGenerator {

  sealed trait Gender
  final object Male extends Gender
  final object Female extends Gender

  case class Person(name: String, age: Int, gender: Gender, salary: Double, key: UUID, id: Option[Long])

  val genSalary = Gen.posNum[Double]

  val genGender: Gen[Gender] = Gen.oneOf(Male, Female)

  val ageGen: Gen[Int] = Gen.choose(0, 110)

  val genPerson = for {
    name ← Gen.alphaStr
    age ← ageGen
    gender ← genGender
    salary ← genSalary
    key ← Gen.uuid
    id ← Gen.option(Gen.posNum[Long])
  } yield Person(name, age, gender, salary, key, id)

  /**
    * ScalaCheck can generate the result of a function, by generating arbitrary parameters. Since each case class
    * is a function, we can get generators for free.
    */
  def randomPerson: Option[Person] = {
    implicit val genderArbitraty: Arbitrary[Gender] = Arbitrary(genGender)
    implicit val uuidArbitraty: Arbitrary[UUID] = Arbitrary(Gen.uuid)
    implicit val posDoubleArbitrary: Arbitrary[Double] = Arbitrary(genSalary)
    implicit val ageArbitrary: Arbitrary[Int] = Arbitrary(ageGen)
    Gen.resultOf(Person).sample
  }

  val genListPersons: Gen[List[Person]] = Gen.listOf(genPerson)

  def randomPersons: List[Person] = genListPersons.sample.getOrElse(Nil)
}
