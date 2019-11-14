package eu.radusw.api

import java.nio.file.{Files, Path, Paths}

import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

final class FrontendResource() {

  val route: Route = customFrontend(Paths.get(s"frontend/"))

  private def customFrontend(resDir: Path): Route = {
    val extPattern = """(.*)[.](.*)""".r
    pathEnd {
      val page = resDir.resolve("index.html")
      val byteArray = Files.readAllBytes(page)
      complete(HttpResponse(StatusCodes.OK, entity = HttpEntity(ContentTypes.`text/html(UTF-8)`, byteArray)))
    } ~
      path(Segment) { resource =>
        val res = resDir.resolve(resource)
        if (res.getParent == resDir && Files.exists(res) && !Files.isDirectory(res)) {
          val ext = res.getFileName.toString match {
            case extPattern(_, extGroup) => extGroup
            case _ => ""
          }
          val byteArray = Files.readAllBytes(res)
          complete(
            HttpResponse(
              StatusCodes.OK,
              entity = HttpEntity(ContentType(MediaTypes.forExtension(ext), () => HttpCharsets.`UTF-8`), byteArray)
            )
          )
        } else {
          complete(HttpResponse(StatusCodes.NotFound, entity = "w00t"))
        }
      }
  }
}
