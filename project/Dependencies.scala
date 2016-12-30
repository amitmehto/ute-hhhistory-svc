/*
 * Copyright (c) 2015. Rogers Communications Inc. All Rights reserved.
 */

import sbt._

object Dependencies {

  object Tst {
    val typesafeAkkaTestKitVersion = "2.3.4"
    val junitVersion = "4.12"
    val mockitoVersion = "1.10.19"
    val junitInterfaceVersion = "0.11"
    val playVersion = "2.3.8"
    val cassandraUnitVersion = "3.0.0.1"
    val comSunXmlWsVersion = "2.2.8"


    val play = "com.typesafe.play" % "play_2.11" % playVersion % Test
    val typesafeAkkaTestKit = "com.typesafe.akka" %% "akka-testkit" % typesafeAkkaTestKitVersion % Test
    val junit = "junit" % "junit" % junitVersion % Test
    val junitInterface = "com.novocode" % "junit-interface" % junitInterfaceVersion % Test
    val mockito = "org.mockito" % "mockito-all" % mockitoVersion % Test
    val cassandraUnit = "org.cassandraunit" % "cassandra-unit" % cassandraUnitVersion % Test
    val comSunXmlWs = "com.sun.xml.ws" % "jaxws-rt" % comSunXmlWsVersion % Test
  }

  object ThirdParty {
    val hibernateEntityManagerVersion = "4.3.6.Final"
    val scalaJava8CompatVersion = "0.7.0"
    val cassandraDriverVersion = "3.1.0"
    val jacksonCoreAnnotationsVersion = "2.8.1"

    val hibernateEntityManager = "org.hibernate" % "hibernate-entitymanager" % hibernateEntityManagerVersion
    val scalaJava8Compat = "org.scala-lang.modules" %% "scala-java8-compat" % scalaJava8CompatVersion

    val cassandraDriver = "com.datastax.cassandra" % "cassandra-driver-core" % cassandraDriverVersion
    val cassandraObjectMapper = "com.datastax.cassandra" % "cassandra-driver-mapping" % cassandraDriverVersion
    val jacksonCoreAnnotations = "com.fasterxml.jackson.core" % "jackson-annotations" % jacksonCoreAnnotationsVersion
  }

  object Ute {
    val uteLoggingVersion = "0.1.6"
    val uteCommonsVersion = "0.3.11"
    val uteAsyncSoapClientVersion = "0.3.5"
    val uteRespEsbClientVersion = "3.2.1"

    val uteLogging = "com.rogers.ute.universal-logging" %% "universal-logging-core" % uteLoggingVersion
    val uteCommons = "com.rogers.ute" %% "ute-commons" % uteCommonsVersion
    val uteAsyncSoapClient = "com.rogers.ute" %% "async-soap-client" % uteAsyncSoapClientVersion
    val uteRespEsbClient = "com.rogers.ute" %% "resp-esb-client-jar" % uteRespEsbClientVersion
  }
}
