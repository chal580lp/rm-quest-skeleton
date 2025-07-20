package com.recursive.quester.framework.requirements

interface Requirement {
    fun check(): Boolean
    fun getDisplayText(): String = ""
    fun tooltip(): String = ""
}

