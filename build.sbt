import Dependencies._
import _root_.play.twirl.sbt.SbtTwirl

name := "ute-hhhistory-svc"

organization := "com.rogers.ute"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  Tst.junit,
  Tst.junitInterface,
  Tst.mockito,
  Tst.cassandraUnit,
  Tst.comSunXmlWs
)

libraryDependencies ++= Seq(
  ThirdParty.scalaJava8Compat,
  ThirdParty.cassandraDriver,
  ThirdParty.cassandraObjectMapper,
  ThirdParty.hibernateEntityManager,
  ThirdParty.jacksonCoreAnnotations
)

libraryDependencies ++= Seq(
  Ute.uteLogging,
  Ute.uteCommons,
  Ute.uteRespEsbClient,
  Ute.uteAsyncSoapClient,
  "com.rogers.ute" %% "ute-billing-account-service" % "0.1.10-SNAPSHOT"
)

resolvers ++= Seq(
  "Rogers Nexus Releases" at "http://10.16.91.11:8080/nexus/content/repositories/releases/",
  "Rogers Nexus Snapshots" at "http://10.16.91.11:8080/nexus/content/repositories/snapshots/"
)

lazy val root = (project in file("."))
  .enablePlugins(SbtTwirl)
