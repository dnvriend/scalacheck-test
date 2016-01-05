# scalacheck-test
This is a study project on [ScalaCheck](https://www.scalacheck.org/).

# TL;DR
[ScalaCheck](https://www.scalacheck.org/) is a unit testing library that provides unit testing of code using property 
specifications with random automatic test data generation.

# Documentation
* [Rickard Nilsson - ScalaCheck User Guide](https://github.com/rickynils/scalacheck/wiki/User-Guide)
* [Oscar Renalias - ScalaCheck Cookbook](https://github.com/oscarrenalias/scalacheck-cookbook/blob/master/markdown/TOC.md)

# Video
* [Kelsey Gilmore-Innis - I Dream of Gen'ning: ScalaCheck Beyond the Basics](https://www.youtube.com/watch?v=lgyGFG6hBa0)
* [Jessica Kerr - Property-Based Testing for Better Code](https://www.youtube.com/watch?v=shngiiBfD80)
* [Debasish Ghosh - Property based Testing for Functional Domain Models](https://www.youtube.com/watch?v=W0oFzBps-fg)

# Ourline
[ScalaCheck](https://www.scalacheck.org/) is a tool for testing Scala and Java programs, based on property specifications 
and automatic test data generation. The basic idea is that you define a property that specifies the behaviour of a 
method or some unit of code, and ScalaCheck checks that the property holds. All test data are generated automatically 
in a random fashion, so you don't have to worry about any missed cases.

## ScalaCheck features:

* Test case minimization (i.e. what is the shortest test case the fails?)
* Support for custom data generators (but a lot of generators come out of the box)
* Collection of statistical information about the input data

## Benefits
Compared to traditional unit testing frameworks, ScalaCheck has the following benefits:

* Automatic generation of data allows developers to focus on defining the purpose of the actual test case, 
rather than spending time and energy looking for corner cases by themselves
* Property-based tests provide a lot more testing for a lot less code than assertion-based tests
* Can help in scenarios where whole classes of test cases have to be tested and it is not feasible to write tests for all distinct test scenarios

## Limitations
ScalaCheck runs a whole lot of test cases so it is better not to use it on functions that have side effects like 
generating files, database access, web service calls and so on.

## Use Cases   
The following are use cases and scenarios where ScalaCheck could prove itself useful for existing projects:

* Testing of code that expects any kind of input values within the boundaries of a specific data type or domain type, 
i.e. code that works on strings or code that works on domain classes, where ScalaCheck can create any kind of string 
or any kind of random domain object for validating purposes
* Application logic that maintains mutable state, where ScalaCheck generates random commands/input that affect the 
internal state that gets validated at the end of the property check
* State machines, where ScalaCheck can be used to generate random input and to verify the correctness of the end state
* Parsers
* Data processors
* Hadoop’s mappers and reducers
* Validators (i.e. validators of web form fields in Spring or equivalent)

# Using ScalaCheck with ScalaTest
[ScalaTest](http://www.scalatest.org/user_guide/writing_scalacheck_style_properties), a popular Scala unit testing and 
behavior-driven design framework, offers integration with [ScalaCheck](https://www.scalacheck.org/) for writing property 
style unit tests.

ScalaTest supports two styles of property-based testing: `ScalaTest-style` by mixing in [org.scalatest.prop.PropertyChecks](http://doc.scalatest.org/2.2.4/index.html#org.scalatest.prop.PropertyChecks) 
and `ScalaCheck-style` by mixing in [org.scalatest.prop.Checkers](http://doc.scalatest.org/2.2.4/#org.scalatest.prop.Checkers).

The advantage of using ScalaTest together with ScalaCheck with `PropertyChecks` is that ScalaTest’s matchers 
can be used to validate if a property holds, which provides additional readability to the code.

The following is an example using the ScalaTest style:

```scala
import org.scalatest.prop.PropertyChecks
import org.scalatest.{FlatSpec, Matchers}

class SimpleTest extends FlatSpec with Matchers with PropertyChecks {
  "String" should "append each other with the concat method" in {
    forAll { (a: String, b: String) =>
      a.concat(b) should be (a + b)
    }
  }
}
```