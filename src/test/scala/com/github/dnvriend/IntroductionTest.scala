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

import org.scalacheck._

class IntroductionTest extends TestSpec {

  /**
   * A `property` is the testable unit in ScalaCheck. There are several ways to create properties in ScalaCheck
   * one of them is to use the `forAll` method. That method creates properties directly,
   * but it is also possible to create new properties by combining other properties.
   *
   * The method `forAll` creates properties. forAll takes a function as parameter, and creates a property
   * that can be tested. The function should return `Boolean` or another property, and can take parameters
   * of any types, as long as there exist implicit `Arbitrary` instances for the types.
   *
   * By default, ScalaCheck has instances for common types like `Int`, `String`, `List`, etc, but it is also
   * possible to define your own `Arbitrary` instances.
   */

  /**
   * For all the tests below, ScalaCheck generates random instances of the function parameters
   * and evaluates the results, reporting any failing cases.
   */

  "String" should "append each other with the concat method" in {
    forAll { (a: String, b: String) ⇒
      a.concat(b) shouldBe (a + b)
    }
  }

  it should "reverse a list" in {
    forAll { xs: List[String] ⇒
      xs.reverse.reverse shouldBe xs
    }
  }

  it should "end with s2 when appended" in {
    forAll { (s1: String, s2: String) ⇒
      (s1 + s2).endsWith(s2) shouldBe true
    }
  }

  "List" should "append each other with the append method" in {
    forAll { (xs: List[Int], xy: List[Int]) ⇒
      xs.size + xy.size shouldBe (xs ::: xy).size
    }
  }

  /**
   * You can also give forAll a specific data generator. `smallInteger` defines a generator that generates
   * integers between 0 and 100, inclusively. This way of using the forAll method is good to use when you want to
   * control the data generation by specifying exactly which generator that should be used,
   * and not rely on a default generator for the given type (which is in implicit scope)
   */

  "forAll" should "accept a specific data generator" in {
    val smallInteger = Gen.choose(0, 100)
    forAll(smallInteger) { n ⇒
      n should equal(0 +- 100)
    }
  }
}
