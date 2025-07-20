package com.recursive.quester.framework.steps.choice

import com.recursive.quester.framework.util.getLogger
import java.util.regex.Pattern

class DialogChoiceSteps {
    private val log = getLogger("DialogChoiceSteps")
    val choices: MutableList<DialogChoiceStep> = ArrayList()

    fun getChoicesAsStringList(): List<String> {
        val choicesList = mutableListOf<String>()
        for (choice in choices) {
            choice.choice?.let { choicesList.add(it) }
        }
        return choicesList
    }

    // Adds a choice without exclusions
    fun addChoice(choice: DialogChoiceStep) {
        choices.add(choice)
    }

    // Adds a dialog choice with a single exclusion string
    fun addDialogChoiceWithExclusion(choice: String, exclusionString: String) {
        val dialogChoiceStep = DialogChoiceStep(choice) // Use correct constructor for choice string
        dialogChoiceStep.addExclusion(219, 1, exclusionString)
        addChoice(dialogChoiceStep)
    }

    // Adds a dialog choice with multiple exclusion strings
    fun addDialogChoiceWithExclusions(choice: String, vararg exclusionStrings: String) {
        val dialogChoiceStep = DialogChoiceStep(choice) // Use correct constructor for choice string
        dialogChoiceStep.addExclusions(219, 1, *exclusionStrings)
        addChoice(dialogChoiceStep)
    }

    // Adds a dialog choice based on a pattern, with a single exclusion string
    fun addDialogChoiceWithExclusion(pattern: Pattern, exclusionString: String) {
        val dialogChoiceStep = DialogChoiceStep(pattern) // Use correct constructor for pattern
        dialogChoiceStep.addExclusion(219, 1, exclusionString)
        addChoice(dialogChoiceStep)
    }

    // Adds a dialog choice based on a pattern, with multiple exclusion strings
    fun addDialogChoiceWithExclusions(pattern: Pattern, vararg exclusionStrings: String) {
        val dialogChoiceStep = DialogChoiceStep(pattern) // Use correct constructor for pattern
        dialogChoiceStep.addExclusions(219, 1, *exclusionStrings)
        addChoice(dialogChoiceStep)
    }

    // Checks the choices based on the last dialog string
    fun checkChoices(lastDialog: String) {
        if (choices.isEmpty()) {
            return
        }

        for (currentChoice in choices) {
            if (currentChoice.expectedPreviousLine != null && !lastDialog.contains(currentChoice.expectedPreviousLine!!)) {
                continue
            }
            log.info("Checking choice: $currentChoice")
            // Implement your choice-checking logic here
        }
    }


    // Resets the choices
    fun resetChoices() {
        choices.clear()
    }
}