package tk.monnef.randomrenamer

import java.io.{ByteArrayOutputStream, PrintWriter, File}

import scalaz._
import Scalaz._
import scala.sys.process.ProcessLogger
import scala.sys.process._

object Utils {
  def getListOfFiles(dir: String): List[File] = {
    val d = new File(dir)
    if (d.exists && d.isDirectory) {
      d.listFiles.filter(_.isFile).toList
    } else {
      List[File]()
    }
  }

  def runCommand(cmd: Seq[String]): (Int, String, String) = {
    val stdout = new ByteArrayOutputStream
    val stderr = new ByteArrayOutputStream
    val stdoutWriter = new PrintWriter(stdout)
    val stderrWriter = new PrintWriter(stderr)
    val p = Process(cmd, None, "Path" -> System.getenv("Path"))
    val exitValue = p ! ProcessLogger(stdoutWriter.println, stderrWriter.println)
    stdoutWriter.close()
    stderrWriter.close()
    (exitValue, stdout.toString, stderr.toString)
  }

  def getMyJarFile(): File = new File(classOf[Utils].getProtectionDomain.getCodeSource.getLocation.toURI.getPath)

  def fileExists(path: File, name: String): Boolean = new File(path, name).exists()

  def fileExists(path: String, name: String): Boolean = fileExists(new File(path), name)

  implicit class OptionPimps[T](o: Option[T]) {
    def orCrash(msg: String): T = o match {
      case Some(x) => x
      case None => throw new RuntimeException(msg)
    }
  }

  private[this] val fileExt = """(.*)[.](.*)""".r

  def getFileExtension(f: File): Option[String] = {
    f.getName match {
      case fileExt(name, ext) => ext.some
      case _ => none
    }
  }

}

trait TuplePimps {

  implicit class PipedObject[A](value: A) {
    def |>[B](f: A => B): B = f(value)
  }

  implicit class PipedObjectTuple1[A](value: Tuple1[A]) {
    def |>[B](f: (A) => B): B = f(value._1)
  }

  implicit class PipedObjectTuple2[A, B](value: (A, B)) {
    def |>[O](f: (A, B) => O): O = f(value._1, value._2)
  }

  implicit class PipedObjectTuple3[A, B, C](value: (A, B, C)) {
    def |>[O](f: (A, B, C) => O): O = f(value._1, value._2, value._3)
  }

  implicit class PipedObjectTuple4[A, B, C, D](value: (A, B, C, D)) {
    def |>[O](f: (A, B, C, D) => O): O = f(value._1, value._2, value._3, value._4)
  }

  implicit class TraversablePimps[A](value: Traversable[A]) {
    def asTuple1(): Tuple1[A] = Tuple1(value.head)

    def asTuple2(): (A, A) = {
      val seq = value.toSeq
      (seq(0), seq(1))
    }

    def asTuple3(): (A, A, A) = {
      val seq = value.toSeq
      (seq(0), seq(1), seq(2))
    }

    def asTuple4(): (A, A, A, A) = {
      val seq = value.toSeq
      (seq(0), seq(1), seq(2), seq(3))
    }
  }

}

class Utils