package tk.monnef.randomrenamer

import scopt.OptionParser

import scalaz._
import Scalaz._

case class Config(retainExtensions: Boolean = true, parallel: Boolean = true, maxIntValue: Int = 1000000, verbose: Boolean = false)

object Config {
  val MinIntValue = 10

  val configParser = new OptionParser[Config]("randomrenamer") {
    head(BuildInfoWrapper.title, BuildInfoWrapper.version, "\n", BuildInfoWrapper.createdBy)
    opt[Unit]('e', "drop-extensions") action { (_, c) =>
      c.copy(retainExtensions = false)
    } text "drops file extensions"
    opt[Unit]('p', "disable-parallel") action { (_, c) =>
      c.copy(parallel = false)
    } text "disables parallel renaming"
    opt[Int]('m', "max-int-value") action { (x, c) =>
      c.copy(maxIntValue = if (x < MinIntValue) MinIntValue else x)
    } text "maximum integer value"
    opt[Unit]('v', "verbose") action { (_, c) =>
      c.copy(verbose = true)
    } text "more verbose output"
    help("help") text "prints this usage text" abbr "h"
  }
}

