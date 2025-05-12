import sbt.Keys.*
import sbt.*

ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "3.3.6"
ThisBuild / organization := "yuwakisa"
ThisBuild / description := "MiniMCP - A minimal Model Context Protocol implementation"

// Common settings for all projects
ThisBuild / scalacOptions ++= Seq(
  "-deprecation",         // Emit warning and location for usages of deprecated APIs
  "-explain",            // Explain errors in more detail
  "-explain-types",      // Explain type errors in more detail
  "-feature",            // Emit warning and location for usages of features that should be imported explicitly
  "-unchecked",          // Enable additional warnings where generated code depends on assumptions
  "-Xfatal-warnings",    // Fail the compilation if there are any warnings
  "-Ykind-projector",    // Enable kind projector syntax
  "-source:future"       // Enable future language features
)

ThisBuild / scalacOptions ++= Seq(
    "-language:implicitConversions"
  )
  
// Common dependencies
val commonDependencies = Seq(
  "jakarta.servlet" % "jakarta.servlet-api" % "6.0.0" % Provided,
  "com.fasterxml.jackson.core" % "jackson-core" % "2.19.0",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.19.0",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.19.0",
  "org.eclipse.jetty.ee10" % "jetty-ee10-servlet" % "12.0.20",
  "org.eclipse.jetty" % "jetty-server" % "12.0.20",
  "commons-io" % "commons-io" % "2.19.0",
  "org.scalamock" %% "scalamock" % "7.3.2" % Test,
  "org.scalameta" %% "munit" % "1.1.1" % Test,
)

lazy val root = (project in file("."))
  .settings(
    name := "minimcp",
    // idePackagePrefix := Some("yuwakisa"),
    libraryDependencies ++= commonDependencies,
    
    // Add resolvers for common repositories
    resolvers ++= Seq(
      Resolver.sonatypeOssRepos("releases"),
      Resolver.sonatypeOssRepos("snapshots")
    ).flatten
  )
