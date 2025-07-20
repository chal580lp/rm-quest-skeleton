package com.recursive.quester.framework.interact

import com.recursive.quester.framework.steps.choice.DialogChoiceSteps
import com.recursive.quester.framework.util.Delay
import com.recursive.quester.framework.util.QuestUtil.findQuestHelperOption
import com.recursive.quester.framework.util.QuestUtil.isQuestHelperPluginOn
import com.recursive.quester.framework.util.getLogger
import com.runemate.game.api.hybrid.RuneScape
import com.runemate.game.api.hybrid.entities.Npc
import com.runemate.game.api.hybrid.local.Camera
import com.runemate.game.api.hybrid.local.hud.interfaces.ChatDialog
import com.runemate.game.api.hybrid.region.Npcs

class NpcDialog {
    private val selectedOptions = mutableListOf<String>()

    fun talkToNPCSmart(npc: Npc?, dialogChoiceSteps: DialogChoiceSteps) {
        if (npc == null) return
        if (npc.isVisible) {
            talkToNPCWithTracking(npc, dialogChoiceSteps.getChoicesAsStringList())
        } else {
            Camera.turnTo(npc)
        }
    }


    fun talkToNPCWithTracking(npc: Npc?, dialogOptions: List<String> = emptyList()) {
        log.info("Talking to tracked NPC: ${npc?.name}")
        if (startInteractionWithNPC(npc)) {
            processDialogWithTracking(dialogOptions)
        }
    }

    private fun processDialogWithTracking(dialogOptions: List<String>) {
        val availableOptions = ChatDialog.getOptions()
        if (availableOptions.isNotEmpty()) {
            val hasRecognizedOption = availableOptions.any { option ->
                dialogOptions.any { it.equals(option.text, ignoreCase = true) && !selectedOptions.contains(it) }
            }
            if (hasRecognizedOption) {
                selectDialogOptionWithTracking(dialogOptions)
            } else {
                checkForQuestStart(availableOptions)
                logUnrecognizedOptions(availableOptions)
                selectedOptions.clear()
            }
        } else {
            if (isQuestHelperPluginOn()) {
                val option = findQuestHelperOption()
                option?.let {
                    log.info("Quest Helper option found: ${it.text}")
                    it.select()
                    selectedOptions.clear()
                    return
                }
            }
            ChatDialog.getContinue()?.select()
        }
    }

    private fun selectDialogOptionWithTracking(dialogOptions: List<String>) {
        val availableOptions = ChatDialog.getOptions()
        dialogOptions.forEach { dialogOption ->
            availableOptions.find {
                it.text.equals(dialogOption, ignoreCase = true) && !selectedOptions.contains(
                    dialogOption
                )
            }?.let { option ->
                option.select()
                selectedOptions.add(option.text)
                log.info("Selected tracked dialog option: ${option.text}")
                return
            }
        }
        // Reset if we get to the end of dialog options
        if (selectedOptions.size == dialogOptions.size) {
            selectedOptions.clear()
        }
    }

    companion object {
        private val log = getLogger("NpcInteractionHandler")
        fun talkToNPC(name: String, vararg dialogOptions: String = emptyArray()) {
            val npc = Npcs.newQuery().names(name).results().nearest() ?: return
            talkToNPC(npc, dialogOptions.toList())
        }

        fun talkToNPC(npc: Npc?, dialogOptions: List<String> = emptyList()) {
            log.info("Talking to NPC: ${npc?.name}")
            if (startInteractionWithNPC(npc)) {
                processDialog(dialogOptions)
            }
        }

        private fun startInteractionWithNPC(npc: Npc?): Boolean {
            if (!ChatDialog.isOpen() && !RuneScape.isCutscenePlaying()) {
                npc?.let {
                    if (!it.isVisible) Camera.turnTo(it)
                    it.interact("Talk-to")
                    Delay.until({ ChatDialog.isOpen() }, 1200, Delay.MOVING)
                }
            }
            return ChatDialog.isOpen()
        }

        private fun processDialog(dialogOptions: List<String>) {
            val availableOptions = ChatDialog.getOptions()
            if (availableOptions.isNotEmpty()) {
                val hasRecognizedOption = availableOptions.any { option ->
                    dialogOptions.any { it.equals(option.text, ignoreCase = true) }
                }
                if (hasRecognizedOption) {
                    selectDialogOption(dialogOptions)
                } else {
                    logUnrecognizedOptions(availableOptions)
                }
            } else {
                if (isQuestHelperPluginOn()) {
                    val option = findQuestHelperOption()
                    option?.let {
                        log.info("Quest Helper option found: ${it.text}")
                        it.select()
                        return
                    }
                }
                dialogOptions.forEach { option ->
                    ChatDialog.getOption(option)?.select()
                }
                ChatDialog.getContinue()?.select()
            }
        }

        private fun selectDialogOption(dialogOptions: List<String>) {
            val availableOptions = ChatDialog.getOptions()
            dialogOptions.forEach { dialogOption ->
                availableOptions.find { it.text.equals(dialogOption, ignoreCase = true) }?.let { option ->
                    option.select()
                    log.info("Selected dialog option: ${option.text}")
                    return
                }
            }
        }

        private fun logUnrecognizedOptions(availableOptions: List<ChatDialog.Option>) {
            log.warn("Unrecognized dialog options:")
            availableOptions.forEach { option ->
                log.warn("- ${option.text}")
            }
        }

        private fun checkForQuestStart(availableOptions: List<ChatDialog.Option>): Boolean {
            ChatDialog.getTitle()?.contains("quest?", ignoreCase = true) ?: return false
            if (availableOptions.firstOrNull { it.text.contains("Yes.", ignoreCase = true) }?.select() == true) {
                log.info("Quest started")
                return true
            }
            return false
        }
    }
}