import sbt._
import Keys._

object HerokuPlugin extends Plugin {
  override def settings = Seq(
    externalResolvers <<= resolvers map { appResolvers =>
      Seq(Resolver.defaultLocal) ++ appResolvers ++
      Seq(Resolver.url("typesafe-ivy-releases") artifacts "http://typesafe.artifactoryonline.com/typesafe/ivy-releases/[organization]/[module]/[revision]/[type]s/[artifact](-[classifier]).[ext]",
          "typesafe" at "http://repo.typesafe.com/typesafe/repo/")
    },
    sources in doc in Compile := List()
  )
}
