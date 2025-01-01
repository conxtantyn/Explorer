package com.explorer.discovery.data.exception

class ConsumerException(code: Int) : Throwable("Device consumer exited with code $code")
