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

import java.io.File

import akka.NotUsed
import akka.actor._
import akka.event.{ Logging, LoggingAdapter }
import akka.stream.scaladsl.{ Flow, Sink, FileIO, Source }
import akka.stream.{ SinkShape, ActorMaterializer, Materializer }
import akka.util.ByteString
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.prop.PropertyChecks
import org.scalatest.{ BeforeAndAfterAll, FlatSpec, Matchers, OptionValues }

import scala.concurrent.duration._
import scala.concurrent.{ ExecutionContext, Future }
import scala.util.Try

class TestSpec extends FlatSpec with Matchers with ScalaFutures with BeforeAndAfterAll with PropertyChecks with OptionValues {
  implicit val system: ActorSystem = ActorSystem()
  implicit val ec: ExecutionContext = system.dispatcher
  implicit val mat: Materializer = ActorMaterializer()
  implicit val log: LoggingAdapter = Logging(system, this.getClass)
  implicit val pc: PatienceConfig = PatienceConfig(timeout = 50.seconds)

  implicit class FutureToTry[T](f: Future[T]) {
    def toTry: Try[T] = Try(f.futureValue)
  }

  def writeToFile(str: String, file: File) =
    Source.single(str)
      .map(ByteString(_))
      .runWith(FileIO.toFile(file))

  def writeToFile(xs: List[String], file: File) =
    Source(xs)
      .map(ByteString(_))
      .runWith(FileIO.toFile(file))

  /**
   * Returns a Source[Int, Unit]
   */
  def withIterator[T](start: Int = 0)(f: Source[Int, NotUsed] ⇒ T): T =
    f(Source.fromIterator(() ⇒ Iterator from start))

  override protected def afterAll(): Unit = {
    system.terminate()
    system.whenTerminated.toTry should be a 'success
  }
}
