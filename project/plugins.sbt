resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"

resolvers += "Rogers release repo" at "http://10.16.91.11:8080/nexus/content/repositories/releases"
resolvers += "Rogers snapshot repo" at "http://10.16.91.11:8080/nexus/content/repositories/snapshots"

addSbtPlugin("com.typesafe.sbt" % "sbt-twirl" % "1.0.0")

addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.3")

addSbtPlugin("com.rogers.ute" % "ute-dependency-management" % "0.0.2")