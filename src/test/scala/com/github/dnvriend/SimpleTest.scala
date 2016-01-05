package com.github.dnvriend

class SimpleTest extends TestSpec {

  "String" should "append each other with the concat method" in {
    forAll { (a: String, b: String) =>
      a.concat(b) should be (a + b)
    }
  }
}
