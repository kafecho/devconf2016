package org.kafecho.devconf

import org.scalacheck.{Gen, Arbitrary}

/**
  * Created by guillaume on 05/03/2016.
  */
object Monads {
  val numbers = Arbitrary.arbitrary[Int]
  val positiveNumbers = numbers.map( n => Math.abs(n))
  val evenNumbers = numbers.filter( n => n % 2 == 0)
  val evenPairs = for{
    a <- evenNumbers;
    b <- evenNumbers
  } yield (a,b)

  val fruits = Gen.oneOf("Apple","Blueberry","Banana")
  val bakings = Gen.oneOf("tart", "crumble", "muffin")
  val desserts = for {
    quantity <- positiveNumbers;
    fruit <- fruits;
    baking <- bakings
  } yield (quantity, fruit + " " + baking)
}


