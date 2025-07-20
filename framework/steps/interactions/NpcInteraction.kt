package com.recursive.quester.framework.steps.interactions

import com.recursive.quester.framework.requirements.ItemRequirement
import com.recursive.quester.framework.steps.NpcStep
import com.recursive.quester.framework.util.getLogger
import com.runemate.game.api.hybrid.RuneScape
import com.runemate.game.api.hybrid.entities.Npc
import com.runemate.game.api.hybrid.local.hud.interfaces.ChatDialog
import com.runemate.game.api.hybrid.region.Players

sealed class NpcInteraction : CommonInteraction<Npc, NpcStep>() {
    companion object {
        private val log = getLogger("NpcInteraction")
        fun simple(action: String = "Talk-to") = SimpleInteraction(action)
        fun talkTo(action: String = "Talk-to") = TalkToInteraction(action)
        fun custom() = Custom()

        fun determineStrategy(step: NpcStep): NpcInteraction {

            if (step.dialogChoiceSteps.choices.isNotEmpty()) {
                log.debug("Has dialog choices")
            }
            return talkTo()
        }
    }

    override fun addBaseSteps(step: NpcStep) {
        // Use the full visibility check which includes movement and scanning
        addMovementStep(
            getPosition = { step.config.position },
            getEntity = { step.getClosestEntity() },
            visibilityCheck = { entityVisible(it) }
        )
        addFullVisibilityCheck(
            getEntity = { step.getClosestEntity() },
            getPosition = { step.config.position },
            entityName = "NPC"
        )

        // Add focus check if needed
        addFocusCheckIfNeeded(step)
    }

    // Helper method for NPC focus check
    private fun addFocusCheckIfNeeded(step: NpcStep) {
        if (step.npcConfig.mustBeFocusedOnPlayer) {
            addStep(
                "Waiting for NPC to focus on player",
                validate = {
                    val npc = step.getClosestEntity()
                    npc != null && npc.target != Players.getLocal()
                },
                "NPC is required to be focused on player",
                execute = { true }
            )
        }
    }

    // Helper method for dialog interaction
    protected fun addDialogInteraction(step: NpcStep) {
        addStep(
            "Talking to NPC",
            validate = { ChatDialog.isOpen() },
            execute = {
                step.npcDialog.talkToNPCSmart(step.getClosestEntity(), step.dialogChoiceSteps)
                true
            }
        )
    }

    data class SimpleInteraction(
        private val action: String = "Talk-to"
    ) : NpcInteraction() {
        override fun execute(step: NpcStep): Boolean {
            prepareSteps(step)
            return execute()
        }

        override fun addSpecificSteps(step: NpcStep) {
            addSimpleInteraction(
                getEntity = { step.getClosestEntity() },
                action = action
            )
        }
    }

    data class TalkToInteraction(
        private val action: String = "Talk-to"
    ) : NpcInteraction() {
        override fun execute(step: NpcStep): Boolean {
            prepareSteps(step)
            return execute()
        }

        override fun addSpecificSteps(step: NpcStep) {
            // Add pre-dialog interaction step
            addStep(
                "Attempting to $action NPC",
                validate = { !ChatDialog.isOpen() && !RuneScape.isCutscenePlaying() },
                "Chat dialog is not open",
                execute = {
                    step.getClosestEntity()?.interact(action) ?: false
                }
            )

            // Add dialog handling
            addDialogInteraction(step)
        }
    }

    data class UseItemOnNpc(
        private val itemRequirement: ItemRequirement
    ) : NpcInteraction() {
        override fun execute(step: NpcStep): Boolean {
            prepareSteps(step)
            return execute()
        }

        override fun addSpecificSteps(step: NpcStep) {
            addItemInteraction(
                getEntity = { step.getClosestEntity() },
                item = itemRequirement
            )
            addContinueDialogStep()
        }
    }

    class Custom : NpcInteraction() {
        override fun execute(step: NpcStep): Boolean {
            prepareSteps(step)
            return execute()
        }

        override fun addSpecificSteps(step: NpcStep) {
            // Custom implementation
        }
    }
}