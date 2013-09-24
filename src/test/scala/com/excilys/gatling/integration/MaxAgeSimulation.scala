import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.Headers.Names._

class MaxAgeSimulation extends Simulation {

  val httpProtocol = http
    .baseURL("http://5.104.98.58:8080")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .acceptEncodingHeader("gzip, deflate, sdch")
    .acceptLanguageHeader("fr-FR,fr;q=0.8,en-US;q=0.6,en;q=0.4")
    .connection("keep-alive")
    .userAgentHeader("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/28.0.1500.71 Chrome/28.0.1500.71 Safari/537.36")

  val scn = scenario("Spray Test")
    .exec(http("reset_cache_counter")
      .get("/cache/reset")
      .check(bodyString.is("Ok")))
    .exec(http("cache_first")
      .get("/cache/maxAge")
      .check(header(CACHE_CONTROL).is("max-age=300"))
      .check(bodyString.saveAs("cacheMaxAge")))
    .exec(http("cache_second")
      .get("/cache/maxAge")
      .check(header(CACHE_CONTROL).is("max-age=300"))
      .check(bodyString.is("${cacheMaxAge}")))

  setUp(scn.inject(atOnce(1 user))).protocols(httpProtocol)

}