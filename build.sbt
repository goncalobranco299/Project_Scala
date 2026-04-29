name := "gremlin-zio-api"
version := "0.1"
scalaVersion := "2.13.12" //

libraryDependencies ++= Seq(
  // Core do ZIO e JSON
  "dev.zio" %% "zio"      % "2.0.19", //
  "dev.zio" %% "zio-json" % "0.5.0",  //

  // ZIO HTTP (essencial para a API)
  "dev.zio" %% "zio-http" % "0.0.5", //

  // Tinkerpop / Gremlin (Driver e Core)
  "org.apache.tinkerpop" % "gremlin-driver" % "3.7.1", // Sugiro 3.7.1 por ser a mais estável com Java 17+
  "org.apache.tinkerpop" % "gremlin-core"   % "3.7.1",

  // Logging (Opcional, mas ajuda a ver erros no terminal)
  "ch.qos.logback" % "logback-classic" % "1.4.11"
)