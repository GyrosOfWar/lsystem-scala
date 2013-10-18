name := "LSystem"

version := "0.1"

scalaVersion := "2.10.2"

resolvers += "Sonatype OSS Snapshots" at
  "https://oss.sonatype.org/content/repositories/snapshots"

libraryDependencies += "com.github.axel22" %% "scalameter" % "0.4-SNAPSHOT"

testFrameworks += new TestFramework(
    "org.scalameter.ScalaMeterFramework")