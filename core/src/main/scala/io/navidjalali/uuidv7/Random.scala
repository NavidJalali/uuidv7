package io.navidjalali.uuidv7

import cats.Id

trait Random[F[_]]:
  def nextBytes(length: Int): F[Array[Byte]]

object Random:
  def secureRandom: Random[Id] = new Random[Id]:
    private val random = new java.security.SecureRandom()

    def nextBytes(length: Int): Id[Array[Byte]] =
      val bytes = new Array[Byte](length)
      random.nextBytes(bytes)
      bytes
