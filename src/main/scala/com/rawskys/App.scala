package com.rawskys

import com.typesafe.scalalogging.LazyLogging
import dispatch.{Http, host, url}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.io.Source

object App extends LazyLogging {

  val godaddy = host("dns.godaddy.com").secure

  def main(args: Array[String]) {
    val current = Source.fromURL("http://checkip.amazonaws.com").mkString
    val stored = Source.fromFile("ip.txt").mkString
    if (stored != current) {
      logger.info(s"$stored out-of-date. Current is $current")
      val config = Source.fromFile("config.txt").getLines()
      val userName = config.next()
      val password = config.next()
      login(userName, password)
    }
  }

  def login(userName: String, password: String) = for {
    enterPage <- Http(godaddy.GET / "default.aspx")
    location = enterPage.getHeader("Location")
    secondPage <- Http(url(location))
    location = secondPage.getHeader("Location")
    loginResponse <- Http(url(location).POST << Map("name" -> userName, "password" -> password))
    loggedIn = !loginResponse.getCookies.isEmpty
  } yield logger.info(s"User logged: $loggedIn")
}
