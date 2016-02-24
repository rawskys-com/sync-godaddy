import com.typesafe.scalalogging.LazyLogging

import scala.io.Source

object App extends LazyLogging {

  def main(args: Array[String]) {
    val current = Source.fromURL("http://checkip.amazonaws.com").mkString
    val stored = Source.fromFile("ip.txt").mkString
    if (stored != current) logger.info(s"$stored out-of-date. Current is $current")
  }
}
