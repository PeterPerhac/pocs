name := """pea-o-seas"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

routesGenerator := InjectedRoutesGenerator

resolvers += Resolver.bintrayRepo("hmrc", "releases")

libraryDependencies ++= Seq(
  ws,
  "org.reactivemongo"      % "play2-reactivemongo_2.11"       % "0.12.0",
  "uk.gov.hmrc"            %% "play-conditional-form-mapping" % "0.2.0",
  "org.scalacheck"         %% "scalacheck"                    % "1.14.0",
  "org.scalatestplus.play" %% "scalatestplus-play"            % "1.5.1" % Test
)

libraryDependencies <+= scalaVersion("org.scala-lang" % "scala-compiler" % _)

JsEngineKeys.engineType := JsEngineKeys.EngineType.Node

scalacOptions ++= Seq("-feature")
