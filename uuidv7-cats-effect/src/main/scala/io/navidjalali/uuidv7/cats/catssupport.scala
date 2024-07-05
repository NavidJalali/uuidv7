package io.navidjalali.uuidv7.cats

import cats.Functor
import cats.effect.{IO, Resource, Clock as CatsClock}
import cats.effect.std.Random as CatsRandom
import io.navidjalali.uuidv7.{Clock, Random}
import cats.syntax.functor.toFunctorOps

given clockForEffect[F[_]: cats.effect.Clock: Functor]: Clock[F] =
  new Clock[F]:
    def currentEpochMilli: F[Long] =
      summon[CatsClock[F]].realTime.map(_.toMillis)

given clockForResource[F[_]: CatsClock: Functor]: Clock[Resource[F, _]] =
  new Clock[Resource[F, *]]:
    def currentEpochMilli: Resource[F, Long] =
      Resource.eval(summon[cats.effect.Clock[F]].realTime.map(_.toMillis))

given randomForEffect[F[_]: CatsRandom: Functor]: Random[F] =
  new Random[F]:
    def nextBytes(n: Int): F[Array[Byte]] =
      summon[CatsRandom[F]].nextBytes(n)

given randomForResource[F[_]: CatsRandom: Functor]: Random[Resource[F, _]] =
  new Random[Resource[F, *]]:
    def nextBytes(n: Int): Resource[F, Array[Byte]] =
      Resource.eval(summon[CatsRandom[F]].nextBytes(n))
