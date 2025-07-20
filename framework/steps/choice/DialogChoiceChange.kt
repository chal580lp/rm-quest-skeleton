package com.recursive.quester.framework.steps.choice

class DialogChoiceChange(
    choice: String,
    private val textChange: String
) : DialogChoiceStep(-1, choice) {
//	override fun highlightText(text: WidgetDefinition?, option: Int) {
//		if (shouldNumber) {
//			text?.text = "[$option] $textChange"
//		} else {
//			text?.text = textChange
//		}
//	}
}