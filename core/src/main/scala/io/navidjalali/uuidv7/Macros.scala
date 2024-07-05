package io.navidjalali.uuidv7

import scala.quoted.*

object Macros:
  def uuidv7(sc: Expr[StringContext])(using Quotes): Expr[UUIDv7] =
    import quotes.reflect.*
    val parts = sc.valueOrAbort.parts.mkString
    UUIDv7.fromString(parts) match
      case Left(value) => report.errorAndAbort(value)
      case Right(v7) =>
        '{
          UUIDv7(${Expr(v7.bytes)})
        }
