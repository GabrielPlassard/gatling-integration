net.virtualvoid.sbt.graph.Plugin.graphSettings

organization  := "com.excilys"

name          := "gatling-integration"

version       := "1.0"

scalaVersion  := "2.10.2"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

resolvers ++= Seq(
  "spray repo" at "http://nightlies.spray.io",
  "Gatling snapshot repo" at "http://repository-gatling.forge.cloudbees.com/snapshot/",
  "Excilys Nexus" at "http://repository.excilys.com/content/groups/public"
)

libraryDependencies ++= Seq(
  "io.gatling.highcharts"       %   "gatling-charts-highcharts" % "2.0.0-SNAPSHOT",
  "io.gatling"            	    %   "gatling-app"     			% "2.0.0-SNAPSHOT",
  "io.gatling"            	    %   "gatling-recorder"     		% "2.0.0-SNAPSHOT",
  "org.apache.directory.studio" %   "org.apache.commons.codec" 	% "1.8"           ,
  "com.excilys"				    %%	"spray-test-server"			% "1.1" 			% "test",
  "junit"                       %   "junit"                     % "4.11"            % "test",
  "org.scalatest"               %%  "scalatest"                 % "2.0.M8"          % "test"
)


