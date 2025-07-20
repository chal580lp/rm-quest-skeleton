package com.recursive.quester.framework.extension


import com.runemate.game.api.hybrid.entities.GameObject
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem
import com.runemate.game.api.hybrid.region.Players
import com.runemate.game.api.script.Execution

fun SpriteItem.useOn(target: GameObject?): Boolean = run {
    if (target == null) {
        return false
    }
    if (Inventory.getSelectedItem() != null && Inventory.getSelectedItem() != this) {
        Inventory.getSelectedItem()?.click()
        return Execution.delayUntil({ Inventory.getSelectedItem() == null }, 1200)
    }
    if (!interact("Use")) return@run false
    Execution.delayUntil({ Inventory.getSelectedItem() == this }, 1200)
    if (!target.interact("Use")) return@run false
    Execution.delayUntil({ Inventory.getSelectedItem() != this }, { Players.getLocal()?.isMoving }, 1800)
    true
}

fun SpriteItem.useOn(target: SpriteItem): Boolean {
    this.interact("Use")
    Execution.delayUntil({ Inventory.isItemSelected() }, 1200)
    target.interact("Use")
    Execution.delayUntil({ !isValid }, 1200)
    return true

}