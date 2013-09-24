import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.Headers.Names._
import org.apache.commons.codec.digest.DigestUtils

class SprayTestSimulation extends Simulation {

	val httpProtocol = http
		.baseURL("http://localhost:8080")
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate, sdch")
		.acceptLanguageHeader("fr-FR,fr;q=0.8,en-US;q=0.6,en;q=0.4")
		.connection("keep-alive")
		.userAgentHeader("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/28.0.1500.71 Chrome/28.0.1500.71 Safari/537.36")

	val scn = scenario("Spray Test")
        .group("Verb")            (Execs.verb)
        .group("Query parameters")(Execs.queryParameters)
        .group("HTML")            (Execs.html)
        .group("XML")             (Execs.xml)
        .group("JSON")            (Execs.json)
        .group("Cookies")         (Execs.cookies)
        .group("Cache")           (Execs.cache)



	setUp(scn.inject(atOnce(1 user))).protocols(httpProtocol)

  object Execs {
    def verb = bootstrap
          .exec(http("DELETE")
                .delete("/verb")
                .check(bodyString.is("DELETE")))
          .exec(http("GET")
                .get("/verb")
                .check(bodyString.is("GET")))
          .exec(http("HEAD")
                .head("/verb")
                .check(bodyString.is("")))
          .exec(http("PATCH")
                .patch("/verb")
                .check(bodyString.is("PATCH")))
          .exec(http("POST")
                .post("/verb")
                .check(bodyString.is("POST")))
          .exec(http("PUT")
                .put("/verb")
                .check(bodyString.is("PUT")))

    def queryParameters = bootstrap
          .exec(http("Hash empty")
                .get("/hashQueryParameters")
                .check(bodyString.is(_ => DigestUtils.sha1Hex(""))))
          .exec(http("Hash world!")
                .get("/hashQueryParameters?hello=world&str=!")
                .check(bodyString.is(_ => DigestUtils.sha1Hex("world!"))))

    def html = bootstrap
          .exec(http("Ipsum")
                .get("/htmlIpsum")
                .check(headerRegex(CONTENT_TYPE, "text/html"))
                .check(header(CONTENT_LENGTH).is("1274")))

    def xml = bootstrap
          .exec(http("SOAP")
                .get("/soapXml")
                .check(headerRegex(CONTENT_TYPE, "text/xml"))
                .check(header(CONTENT_LENGTH).is("1777")))

    def json = bootstrap
          .exec(http("JSON Object")
                .get("/jsonObject")
                .check(headerRegex(CONTENT_TYPE, "application/json"))
                .check(header(CONTENT_LENGTH).is("207")))
          .exec(http("JSON Array")
                .get("/jsonArray")
                .check(headerRegex(CONTENT_TYPE, "application/json"))
                .check(header(CONTENT_LENGTH).is("307")))

    def cookies = bootstrap
          .exec(http("No cookies")
                .get("/cookies/countCookies")
                .check(regex("have 0 cookie").exists))
          .exec(http("Take cookie")
                .get("/cookies/take")
                .check(header(SET_COOKIE).exists))
          .exec(http("1 cookie")
                .get("/cookies/countCookies")
                .check(regex("have 1 cookie").exists)
                .check(header(SET_COOKIE).notExists))
          .exec(http("No cookies for path")
                .get("/countCookies")
                .check(regex("have 0 cookie").exists))
          .exec(http("Delete cookie")
                .get("/cookies/take?maxAge=-200")
                .check(header(SET_COOKIE).exists))
          .exec(http("No cookies again")
                .get("/cookies/countCookies")
                .check(regex("have 0 cookie").exists))

    def cache = bootstrap
          .exec(http("Expire 1")
                .get("/cache/expires")
                .check(header(EXPIRES).exists)
                .check(bodyString.saveAs("cacheExpires")))
          .exec(http("Expire 2")
                .get("/cache/expires")
                .check(header(EXPIRES).exists)
                .check(bodyString.is("${cacheExpires}")))
          .exec(http("MaxAge 1")
                .get("/cache/maxAge")
                .check(header(CACHE_CONTROL).is("max-age=300"))
                .check(bodyString.saveAs("cacheMaxAge")))
          .exec(http("MaxAge 2")
                .get("/cache/maxAge")
                .check(header(CACHE_CONTROL).is("max-age=300"))
                .check(bodyString.is("${cacheMaxAge}")))

    def basicAuth = bootstrap
          .exec(http("No auth")
                .get("/admin")
                .check(header(WWW_AUTHENTICATE).is("""Basic realm="Secured Resource""""))
                .check(status.is(401)))
          .exec(http("Wrong auth")
                .get("/admin")
                .check(header(WWW_AUTHENTICATE).is("""Basic realm="Secured Resource""""))
                .basicAuth("toto", "titi")
                .check(status.is(401)))
          .exec(http("Correct auth")
                .get("/admin")
                .check(header(WWW_AUTHENTICATE).not("""Basic realm="Secured Resource""""))
                .basicAuth("admin", "password")
                .check(status.is(200)))
  }
}
