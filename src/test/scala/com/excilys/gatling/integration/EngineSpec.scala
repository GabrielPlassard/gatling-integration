package com.excilys.gatling.integration

import org.junit.runner.RunWith
import org.scalatest.{BeforeAndAfter, Matchers, FlatSpec}
import org.scalatest.junit.JUnitRunner
import com.excilys.spraytestserver.Server

@RunWith(classOf[JUnitRunner])
class EngineSpec extends FlatSpec with Matchers with BeforeAndAfter{

  before {
    println("before start")
    Server.start
    println("after start")
  }

  "With a server we" should  "launch the scenario" in {
      println("before test")
      Engine.go()
      1 + 1 should be (2)
      println("after test")
  }

  after {
    println("before stop")
    Server.stop
    println("after stop")
  }

}