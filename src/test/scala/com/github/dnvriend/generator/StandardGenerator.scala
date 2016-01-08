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

import org.scalacheck.Gen
import org.scalacheck.Gen.Choose

trait StandardGenerator {

  implicit class UniqueGenerator[T](gen: Gen[T]) {
    var generatorHistory = Set[T]()

    private def uniqueFilter =
      (t: T) ⇒ {
        if (generatorHistory.contains(t)) false
        else {
          generatorHistory += t
          true
        }
      }

    val unique: Gen[T] = for {
      result ← gen.withFilter(uniqueFilter)
    } yield result
  }

  /* Stateful helper class to eliminate duplicate items from a generator
    (the resulting generator will never produce None, but may StackOverflow if it has to try many times to find a free value)
  */
  class DepletedDomain(msg: String, t: Throwable) extends RuntimeException(msg, t) {
    def this(msg: String) {
      this(msg, null)
    }
  }

  object UniqueInts {
    def choose(min: Int, max: Int) = (new UniqueInts).choose(min, max)
  }

  class UniqueInts extends Choose[Int] {
    var history: Set[Int] = Set.empty

    def isUniqueAttempt(i: Int): Boolean = {
      val preChosenHistorySize = history.size
      history += i
      history.size != preChosenHistorySize
    }

    def isDomainDepleted(min: Int, max: Int): Boolean = {
      if (max - min + 1 == 0) history.isEmpty
      val t = history.size / (max - min + 1)
      if (t > 0.9) t >= 0.99 else false
    }

    override def choose(min: Int, max: Int): Gen[Int] = for {
      choice ← Choose.chooseInt.choose(min, max)
      finalChoice ← isUniqueAttempt(choice) match {
        case true ⇒ Gen.const(choice)
        case false ⇒ isDomainDepleted(min, max) match {
          case true ⇒ throw new DepletedDomain(
            s"Input domain ($min to $max) does not have sufficient unique values remaining, given ${history.size} values already picked from it. Choose a larger domain, or a different way of picking values than trial-and-error")
          case false ⇒ choose(min, max)
        }
      }
    } yield finalChoice
  }

  class UniqueRandomLong extends scala.util.Random {
    var longHistory: Set[Long] = Set.empty
    var doubleHistory: Set[Double] = Set.empty

    /* stateful implementation */
    override def nextLong(): Long = {
      val preGenSize = longHistory.size
      val generatedLong = super.nextLong()
      longHistory += generatedLong

      if (preGenSize == longHistory.size)
        nextLong()
      else
        generatedLong
    }

    /* stateful implementation */
    override def nextDouble(): Double = {
      val preGenSize = doubleHistory.size
      val generateDouble = super.nextDouble()
      doubleHistory += generateDouble

      if (preGenSize == doubleHistory.size)
        nextDouble()
      else
        generateDouble
    }
  }

  val genNonNegativeInt = Gen.choose(0, Int.MaxValue)
  val genNonNegativeLong = Gen.choose(0l, Int.MaxValue.toLong)

  def genNonNegativeUniqueInt = UniqueInts.choose(0, Int.MaxValue)

  def genNonNegativeUniqueLong = UniqueInts.choose(0, Int.MaxValue) map (_.toLong)

  def enumGenerator[T](values: Set[T]): Gen[T] = Gen.oneOf(values.toSeq)
}

object StandardGenerator extends StandardGenerator
