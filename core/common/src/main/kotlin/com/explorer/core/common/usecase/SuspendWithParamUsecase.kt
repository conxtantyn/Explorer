package com.explorer.core.common.usecase

interface SuspendWithParamUsecase<P, R> : Usecase {
    suspend operator fun invoke(param: P): R
}
