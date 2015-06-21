package tk.monnef.randomrenamer

import java.io.{IOException, File}
import java.nio.file.Files

import scalaz._
import Scalaz._
import tk.monnef.randomrenamer.Utils._

import scala.util.Random

object Main extends App {
  def generateRandomName(f: File): String = {
    val extPart =
      if (config.retainExtensions) getFileExtension(f).map("." + _).getOrElse("")
      else ""
    Random.nextInt(config.maxIntValue).toString + extPart
  }

  def generateRandomUniqueName(f: File, otherNames: Seq[String]): String = {
    def gen = generateRandomName(f)
    def nameConflicts(n: String) = fileExists(path, n) || otherNames.contains(n)
    Iterator.continually {gen}.dropWhile(nameConflicts).take(1).toList.head
  }

  def generateNames(files: Seq[File]): Seq[(File, String)] = {
    val newNames = files.foldLeft(Seq[String]()) { case (acc, f) => generateRandomUniqueName(f, acc) +: acc }.reverse
    files.zip(newNames)
  }

  def rename(x: (File, String)): Option[String] =
    x match {
      case (f: File, name: String) =>
        val newFile = new File(f.getParentFile.getAbsolutePath, name)
        if (newFile.exists()) {
          Some( s"""File \"$name\" already exists, cannot rename.""")
        } else {
          try {
            Files.move(f.toPath, newFile.toPath)
            None
          } catch {
            case io: IOException => Some( s"""Error occurred while renaming \"${f.getName}\" to \"$name\": ${io.getClass.getSimpleName} - ${io.getMessage}""")
          }
        }
    }

  def printInfoMessage() {
    val i = BuildInfoWrapper
    println( s"""${i.title} ${i.version}""")
    println(s"${i.createdBy}")
  }

  val config = Config.configParser.parse(args, Config()).orCrash("Error occurred, halting.")
  val myFile = getMyJarFile()
  val myFileName = myFile.getCanonicalPath
  val path = new File(".")

  printInfoMessage()

  println( s"""My file name is \"$myFileName\".""")
  println( s"""Running in directory: \"${path.getCanonicalPath}\".""")

  private val fileList = getListOfFiles(".")
  if (config.maxIntValue < fileList.length) {
    println("Got more files than possible names, please consider setting larger name space.")
    System.exit(1)
  } else {
    val fileAndNewName = fileList
      .filter(_.getCanonicalPath != myFileName) |> generateNames

    println( s"""Started renaming ${fileAndNewName.length} file(s).""")
    if (config.verbose) println(fileAndNewName.map { case (f, nn) => f.getName + " -> " + nn }.mkString("\n"))

    val errors = fileAndNewName |> {
      case in: Seq[(File, String)] =>
        if (config.parallel) in.par.flatMap(rename).seq
        else in.flatMap(rename)
    }: Seq[String]
    errors.foreach(println(_))

    println("Done.")
  }
}
