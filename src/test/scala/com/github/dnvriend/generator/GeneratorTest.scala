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

import com.github.dnvriend.TestSpec
import org.scalacheck.Gen

class GeneratorTest extends TestSpec {

  /**
   * Generators are responsible for generating test data in ScalaCheck,
   * and are represented by the `org.scalacheck.Gen` class. In the Gen object,
   * there are several methods for creating new and modifying existing generators.
   *
   * A generator can be seen simply as a function that takes some generation parameters,
   * and (maybe) returns a generated value. That is, the type Gen[T] may be thought of as a
   * function of type Gen.Params => Option[T].
   *
   * However, the Gen class contains additional methods to make it possible to map generators,
   * use them in for-comprehensions and so on. Conceptually, though, you should think of generators
   * simply as functions, and the combinators in the Gen object can be used to create or modify
   * the behaviour of such generator functions.
   *
   * The best way to create new generators is to use the generator combinators that exist in the
   * `org.scalacheck.Gen` module. For example, the `choose` method creates a generator that generates
   * a random value in the given (inclusive) range. If the range is invalid, the generator will not
   * generate any value.
   */

  "intGenerator" should "generate integers between given minimum and maximum" in {
    val intGenerator: Gen[Int] = Gen.choose(0, Int.MaxValue)
    forAll(intGenerator) { int ⇒
      int should equal(0 +- Int.MaxValue)
    }
  }

  "longGenerator" should "generate longs between given minimum and maximum" in {
    val longGenerator: Gen[Long] = Gen.choose(0, Long.MaxValue)
    forAll(longGenerator) { long ⇒
      long should equal(0L +- Long.MaxValue)
    }
  }

  /**
   * Generators can be combined using a for-comprehension. Suppose you need a generator which
   * generates a tuple that contains two random integer values, one of them being at least
   * twice as big as the other. The following definition does this:
   */

  "Generators" should "be combined using a for-comprehension" in {
    val tupleGenerator: Gen[(Int, Int)] = for {
      n ← Gen.choose(10, 20)
      m ← Gen.choose(2 * n, 500)
    } yield (n, m)

    forAll(tupleGenerator) { tuple ⇒
      val (n, m) = tuple
      n should (be > 0)
      m should (be > 0)
      m should (be >= 2 * n)
    }
  }
}
