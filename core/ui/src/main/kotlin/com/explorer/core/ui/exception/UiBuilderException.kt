package com.explorer.core.ui.exception

import java.lang.RuntimeException

class UiBuilderException(name: String) : RuntimeException("Builder $name not found! Is this fragment a child fragment of the page?")
