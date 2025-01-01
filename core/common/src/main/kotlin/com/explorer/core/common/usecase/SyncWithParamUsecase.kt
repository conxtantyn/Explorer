package com.explorer.core.common.usecase

interface SyncWithParamUsecase<P, R> : Usecase {
    operator fun invoke(param: P): R
}
