package com.mdrlzy.budgetwise.core.domain

import arrow.core.Either

/**
 * A typealias for `Either<Throwable, T>` using the Arrow library.
 *
 * Represents a computation that may either result in a success ([Right] with value [T])
 * or a failure ([Left] with a [Throwable]).
 */
typealias EitherT<T> = Either<Throwable, T>