package com.recursive.quester.framework.steps.choice


import java.util.regex.Pattern

// Assuming WidgetChoiceStep is a class that your code extends
open class DialogChoiceStep : WidgetChoiceStep {

    var expectedPreviousLine: String? = null

    // Constructor for a String choice with default groupId and childId
    constructor(choice: String) : super(choice, 219, 1) {
        shouldNumber = true
    }

    // Constructor for a Pattern choice with default groupId and childId
    constructor(pattern: Pattern) : super(pattern, 219, 1) {
        shouldNumber = true
    }

    // Constructor for an Int choiceId and String choice
    constructor(choiceId: Int, choice: String) : super(choiceId, choice, 219, 1) {
        shouldNumber = true
    }

    // Constructor for an Int choiceId and a Pattern
    constructor(choiceId: Int, pattern: Pattern) : super(choiceId, pattern, 219, 1) {
        shouldNumber = true
    }

    // Constructor for just an Int choiceId with default groupId and childId
    constructor(choiceId: Int) : super(choiceId, 219, 1) {
        shouldNumber = true
    }
}