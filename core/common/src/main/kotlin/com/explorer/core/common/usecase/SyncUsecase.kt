package com.explorer.core.common.usecase

interface SyncUsecase<R> : Usecase {
    operator fun invoke(): R
}
