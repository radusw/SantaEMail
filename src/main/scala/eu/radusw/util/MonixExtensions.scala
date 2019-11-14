package eu.radusw.util

import akka.event.LoggingAdapter
import monix.eval.Task

import scala.concurrent.duration.FiniteDuration

object MonixExtensions {

  implicit class RichTask[A](val underlying: Task[A]) extends AnyVal {
    def retryBackoff(
      maxRetries: Int,
      firstDelay: FiniteDuration
    )(
      implicit
      log: LoggingAdapter
    ): Task[A] = underlying.onErrorHandleWith { case ex: Exception =>
      if (maxRetries > 0) {
        log.info(s"$ex: Retrying (${maxRetries - 1})... ")
        retryBackoff(maxRetries - 1, firstDelay * 2).delayExecution(firstDelay)
      } else {
        Task.raiseError(ex)
      }
    }
  }
}
