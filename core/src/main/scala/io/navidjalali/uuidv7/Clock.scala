package io.navidjalali.uuidv7

import cats.Id
import java.time.Instant

trait Clock[F[_]]:
  def currentEpochMilli: F[Long]

object Clock:
  def systemClock: Clock[Id] = new Clock[Id]:
    def currentEpochMilli: Id[Long] = Instant.now.toEpochMilli
