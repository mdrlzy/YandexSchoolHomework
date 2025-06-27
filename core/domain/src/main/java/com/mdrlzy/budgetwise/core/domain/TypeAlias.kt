package com.mdrlzy.budgetwise.core.domain

import arrow.core.Either

typealias EitherT<T> = Either<Throwable, T>