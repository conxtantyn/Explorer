package com.explorer.core.common.usecase

interface SuspendUsecase<R> : Usecase {
    suspend operator fun invoke(): R
}
