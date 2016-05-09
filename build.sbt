name := """APIChallenge2"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  javaJpa,
  "org.hibernate" % "hibernate-entitymanager" % "5.1.0.Final",
  "mysql" % "mysql-connector-java" % "5.1.36",
  "org.apache.httpcomponents" % "httpclient" % "4.5.2",
  "org.apache.httpcomponents" % "httpcore" % "4.4.4"
)
