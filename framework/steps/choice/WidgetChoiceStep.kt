package com.recursive.quester.framework.steps.choice

import java.util.regex.Pattern

open class WidgetChoiceStep {

    // Primary constructor - most flexible version
    constructor(choice: String?, choiceById: Int = -1, groupId: Int, childId: Int, pattern: Pattern? = null) {
        this.choice = choice
        this.choiceById = choiceById
        this.groupId = groupId
        this.groupIdForChecking = groupId
        this.childId = childId
        this.pattern = pattern
    }

    // Constructor for a String choice
    constructor(choice: String, groupId: Int, childId: Int) : this(choice, -1, groupId, childId, null)

    // Constructor for a Pattern choice
    constructor(pattern: Pattern, groupId: Int, childId: Int) : this(null, -1, groupId, childId, pattern)

    // Constructor for a choiceId only
    constructor(choiceById: Int, groupId: Int, childId: Int) : this(null, choiceById, groupId, childId, null)

    // Constructor for a choiceId and String choice
    constructor(choiceById: Int, choice: String, groupId: Int, childId: Int) : this(
        choice,
        choiceById,
        groupId,
        childId,
        null
    )

    // Constructor for a choiceId and Pattern
    constructor(choiceById: Int, pattern: Pattern, groupId: Int, childId: Int) : this(
        null,
        choiceById,
        groupId,
        childId,
        pattern
    )

    // Properties
    var choice: String? = null
    var choiceById: Int = -1
    var groupId: Int = -1
    var childId: Int = -1
    open var pattern: Pattern? = null
    var shouldNumber: Boolean = false
    var groupIdForChecking: Int = groupId

    var expectedTextInWidget: String? = null
    private var excludedStrings: List<String> = emptyList()
    private var excludedGroupId: Int = -1
    private var excludedChildId: Int = -1

    // Add a single exclusion
    fun addExclusion(excludedGroupId: Int, excludedChildId: Int, excludedString: String) {
        this.excludedStrings = listOf(excludedString)
        this.excludedGroupId = excludedGroupId
        this.excludedChildId = excludedChildId
    }

    // Add multiple exclusions
    fun addExclusions(excludedGroupId: Int, excludedChildId: Int, vararg excludedStrings: String) {
        this.excludedStrings = excludedStrings.toList()
        this.excludedGroupId = excludedGroupId
        this.excludedChildId = excludedChildId
    }
}