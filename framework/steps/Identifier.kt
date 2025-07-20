package com.recursive.quester.framework.steps

data class Identifier(
    val name: String? = null,
    val id: Int? = null,
    val alternateIds: List<Int> = emptyList()
)