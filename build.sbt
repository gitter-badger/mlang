import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}




lazy val core = crossProject(JSPlatform, JVMPlatform).in(file("core")).settings(
  sharedSettings,
//    libraryDependencies ++= Deps.shared.value,
).jsConfigure(_.enablePlugins(ScalaJSPlugin)).jvmConfigure(_.settings(
  libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-compiler" % scalaVersion.value
  )
))

lazy val `client-web` = project.in(file("client-web")).settings(
  sharedSettings,
  scalaJSUseMainModuleInitializer := true,
  libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "0.9.6",
    "com.lihaoyi" %%% "scalatags" % "0.6.7"
  )
).enablePlugins(ScalaJSPlugin).dependsOn(core.js)

val sharedSettings = Seq(
  scalaVersion := "2.12.6",
  resolvers ++= Seq(
    "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
    Resolver.jcenterRepo,
    Resolver.sonatypeRepo("releases"),
  ),
  sources in (Compile, doc) := Seq.empty,
  publishArtifact in (Compile, packageDoc) := false,
  testFrameworks += new TestFramework("utest.runner.Framework"),
  scalacOptions ++= Seq(
    "-language:implicitConversions",
    "-deprecation", // Emit warning and location for usages of deprecated APIs.
    "-feature", // Emit warning and location for usages of features that should be imported explicitly.
    "-unchecked", // Enable additional warnings where generated code depends on assumptions.
    //"-Xfatal-warnings", // Fail the compilation if there are any warnings.
    //"-Xlint", // Enable recommended additional warnings.
    //"-Ywarn-adapted-args", // Warn if an argument list is modified to match the receiver.
    "-Ywarn-dead-code", // Warn when dead code is identified.
    "-Ywarn-inaccessible", // Warn about inaccessible types in method signatures.
    "-Ywarn-nullary-override", // Warn when non-nullary overrides nullary, e.g. def foo() over def foo.
    "-Ywarn-numeric-widen", // Warn when numerics are widened.
    "-Xlint:-unused,_",
    "-P:acyclic:force",
  ),
  autoCompilerPlugins := true,
  addCompilerPlugin("com.lihaoyi" %% "acyclic" %  "0.1.8"),
  libraryDependencies += "com.lihaoyi" %% "acyclic" % "0.1.8" % "provided",

  resolvers += Resolver.bintrayRepo("stg-tud", "maven"),
  addCompilerPlugin("de.tuda.stg" % "dslparadise" % "0.2.0" cross CrossVersion.patch),
  libraryDependencies += "de.tuda.stg" %% "dslparadise-types" % "0.2.0"
)

