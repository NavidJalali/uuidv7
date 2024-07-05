package io.navidjalali.uuidv7

import cats.{Id, Monad}
import cats.syntax.all.*

final class UUIDv7(val bytes: Array[Byte]):
  def mostSignificantBits: Long =
    bytes.take(8).foldLeft(0L)(_ << 8 | _)

  def leastSignificantBits: Long =
    bytes.drop(8).foldLeft(0L)(_ << 8 | _)

  override def toString: String =
    val sb = new StringBuilder(36)
    bytes.zipWithIndex.foreach { case (byte, index) =>
      if index == 4 || index == 6 || index == 8 || index == 10 then
        sb.append('-')
      sb.append(f"$byte%02x")
    }
    sb.toString

  override def equals(other: Any): Boolean =
    other match
      case that: UUIDv7 =>
        bytes.sameElements(that.bytes)
      case _ => false

  override def hashCode: Int = bytes.foldLeft(0)(_ * 31 + _.toInt)

object UUIDv7:
  def randomM[F[_]: Clock: Random: Monad]: F[UUIDv7] =
    for
      bytes <- summon[Random[F]].nextBytes(16)
      ts <- summon[Clock[F]].currentEpochMilli
    yield
      // timestamp
      bytes(0) = ((ts >> 40) & 0xff).toByte
      bytes(1) = ((ts >> 32) & 0xff).toByte
      bytes(2) = ((ts >> 24) & 0xff).toByte
      bytes(3) = ((ts >> 16) & 0xff).toByte
      bytes(4) = ((ts >> 8) & 0xff).toByte
      bytes(5) = (ts & 0xff).toByte
      // version and variant
      bytes(6) = ((bytes(6) & 0x0f) | 0x70).toByte
      bytes(8) = ((bytes(8) & 0x3f) | 0x80).toByte
      new UUIDv7(bytes)

  def random: UUIDv7 =
    randomM[Id](using
      Clock.systemClock,
      Random.secureRandom,
      Monad[Id]
    )

  def fromString(raw: String): Either[String, UUIDv7] =
    val stripped = raw.filterNot(_ == '-')
    val length = stripped.length

    if length > 32 then Left("UUIDv7 string too long")
    else if length < 32 then Left("UUIDv7 string too short")
    else if stripped(12) != '7' then
      Left(s"Invalid UUIDv7 version. Expected '7' got '${stripped(12)}'")
    else if stripped(16) != 'a' then
      Left(s"Invalid UUIDv7 variant. Expected 'a' got '${stripped(16)}'")
    else
      val bytes = new Array[Byte](16)
      try
        for i <- 0 until 16 do
          val s = stripped.substring(i * 2, i * 2 + 2)
          bytes(i) = Integer.parseInt(s, 16).toByte
        Right(new UUIDv7(bytes))
      catch case _: NumberFormatException => Left("Invalid UUIDv7 string")

extension (inline sc: StringContext)
  inline def v7(): UUIDv7 =
    ${ Macros.uuidv7('{ sc }) }
