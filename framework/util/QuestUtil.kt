package com.recursive.quester.framework.util

import com.runemate.game.api.hybrid.Environment
import com.runemate.game.api.hybrid.local.hud.interfaces.ChatDialog
import java.awt.Color

object QuestUtil {
    private val log = getLogger("QuestUtil")
    private val QUESTHELPER_TEXT_COLOR = Color(0, 0, 255)

    fun isQuestHelperPluginOn(): Boolean {
        val plugins = Environment.getActiveRuneLitePlugins()
        return plugins.contains("Quest Helper")
    }

    fun findQuestHelperOption(): ChatDialog.Option? {
        getChatOptionByColor(QUESTHELPER_TEXT_COLOR)?.let {
            return it
        }
        return null
    }

    private fun getChatOptionByColor(targetColor: Color): ChatDialog.Option? {
        val options = ChatDialog.getOptions()

        if (options.isNullOrEmpty()) {
            return null
        }

        return options.find { option ->
            option?.component?.textColor?.let { color ->
                color == targetColor
            } ?: false
        }
    }

}