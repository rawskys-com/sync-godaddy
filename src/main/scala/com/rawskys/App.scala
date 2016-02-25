package com.rawskys

import scala.concurrent.ExecutionContext.Implicits.global

import com.typesafe.scalalogging.LazyLogging
import dispatch.{Http, host}

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

  def login(userName: String, password: String) = {
    val enterPage = godaddy.GET / "default.aspx"
    Http(enterPage) onFailure {
      case r => logger.info("message: " + r.getMessage)
    }

//    val req = godaddy.POST / "default.aspx" << Map("app" -> "idp", "realm" -> "idp", "name" -> userName, "password" -> password)
//    val a = req.url
//    val response = Http(req)(global)
//    logger.info(s"$a")
  }
}