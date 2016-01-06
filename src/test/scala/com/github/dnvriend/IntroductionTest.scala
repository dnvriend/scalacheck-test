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
import org.scalatest.exceptions.TestFailedException

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
   * For all the tests below, ScalaCheck `generates` random instances of the function
   * parameters and evaluates the results, reporting any failing cases.
   *
   * ScalaCheck is really good in finding counter examples to your assertions, ie. it
   * does it best to make your assertion fail! After it has done so, it will shrink the
   * failing input parameters and report those. Based on the failing input parameters
   * you can change the assertion.
   */

  "String" should "append each other with the concat method" in {
    forAll { (a: String, b: String) ⇒
      a.concat(b) shouldBe (a + b)
    }
  }

  it should "end with s2 when appended" in {
    forAll { (s1: String, s2: String) ⇒
      (s1 + s2).endsWith(s2) shouldBe true
    }
  }

  it should "the following assertion should fail when s1.length == 0 and s2.length == 0" in {
    an[TestFailedException] should be thrownBy forAll { (s1: String, s2: String) ⇒
      s1.length should (be < (s1 + s2).length)
    }
  }

  it should "concatenate two strings, the first string should be smaller or equal than the concatenation" in {
    forAll { (s1: String, s2: String) ⇒
      s1.length should (be <= (s1 + s2).length)
    }
  }

  "List" should "append each other with the append method" in {
    forAll { (xs: List[Int], xy: List[Int]) ⇒
      xs.size + xy.size shouldBe (xs ::: xy).size
    }
  }

  it should "should be reversable" in {
    forAll { xs: List[String] ⇒
      xs.reverse.reverse shouldBe xs
    }
  }

  it should "failing reversing two lists and zipping them is the same as first zipping them and then reversing the resulting list of pairs" in {
    a[TestFailedException] should be thrownBy forAll { (a: List[Int], b: List[Int]) ⇒
      // note that a and b are lists and do not have to have the same length
      // and in that case the test will fail.
      (a.reverse zip b.reverse) shouldBe (a zip b).reverse
    }
  }

  it should "reversing two lists and zipping them is the same as first zipping them and then reversing the resulting list of pairs" in {
    forAll { (a: List[Int], b: List[Int]) ⇒
      // note that a and b are lists and do not have to have the same length
      // and in that case the test will fail.

      // this can be fixed by creating two lists that have the same length
      // based on the generated lists
      val a1 = a.take(b.length)
      val b1 = b.take(a.length)
      (a1.reverse zip b1.reverse) shouldBe (a1 zip b1).reverse
    }
  }

  "Map" should "fail increase size after a new element has been added" in {
    a[TestFailedException] should be thrownBy forAll { (m: Map[Int, Int], key: Int, value: Int) ⇒
      (m + (key -> value)).size should equal(m.size + 1)
    }
  }

  it should "increase size after a new element has been added" in {
    forAll { (m: Map[Int, Int], key: Int, value: Int) ⇒
      val m1 = m - key
      (m1 + (key -> value)).size should equal(m1.size + 1)
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
