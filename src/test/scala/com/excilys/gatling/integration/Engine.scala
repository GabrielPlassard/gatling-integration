package com.excilys.gatling.integration

import io.gatling.app.Gatling
import io.gatling.core.config.GatlingPropertiesBuilder

object Engine extends App {

  def go() = {
    val props = new GatlingPropertiesBuilder
    props.dataDirectory(IDEPathHelper.dataDirectory.toString)
    props.resultsDirectory(IDEPathHelper.resultsDirectory.toString)
    props.requestBodiesDirectory(IDEPathHelper.requestBodiesDirectory.toString)
    props.binariesDirectory(IDEPathHelper.mavenBinariesDirectory.toString)
    props.simulationClass("SprayTestSimulation")
    props.runDescription("run description")

    Gatling.fromMap(props.build)
  }
}
