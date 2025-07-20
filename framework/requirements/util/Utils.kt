package com.recursive.quester.framework.requirements.util

import com.recursive.quester.framework.util.getLogger

object Utils {
    private val log = getLogger("Utils")

    fun unpackWidget(componentId: Int): Pair<Int, Int> =
        Pair(componentId shr 16, componentId and 0xFFFF)
}