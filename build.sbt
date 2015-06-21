name := "Random Renamer"

version := "0.2"

scalaVersion := "2.11.2"

resolvers += Resolver.sonatypeRepo("public")

libraryDependencies += "com.github.scopt" %% "scopt" % "3.2.0"

libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test"

libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.1.0"

buildInfoSettings

sourceGenerators in Compile <+= buildInfo

buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion)

buildInfoPackage := "tk.monnef.randomrenamer"

// disable using the Scala version in output paths and artifacts
crossPaths := false

artifactName := { (sv: ScalaVersion, module: ModuleID, artifact: Artifact) =>
  "randomrenamer" + "_" + module.revision + "_slim" + "." + artifact.extension
}

assemblyJarName in assembly := s"randomrenamer_${version.value}.jar"
