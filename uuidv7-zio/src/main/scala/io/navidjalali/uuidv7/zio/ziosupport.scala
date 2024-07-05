package io.navidjalali.uuidv7.zio

import zio.ZIO
import io.navidjalali.uuidv7.Clock
import java.util.concurrent.TimeUnit
import io.navidjalali.uuidv7.Random

given clockForZIO[R, E]: Clock[ZIO[R, E, _]] =
  import zio.interop.catz.*

  new Clock[ZIO[R, E, _]]:
    def currentEpochMilli: ZIO[R, E, Long] =
      ZIO.clock
        .flatMap(_.currentTime(TimeUnit.MILLISECONDS))

given randomForZIO[R, E]: Random[ZIO[R, E, _]] =
  new Random[ZIO[R, E, _]]:
    def nextBytes(n: Int): ZIO[R, E, Array[Byte]] =
      ZIO.random
        .flatMap(_.nextBytes(n))
        .map(_.toArray)
