package com.recursive.quester.framework.reward

class ItemReward(
    private val name: String,
    val itemID: Int,
    val quantity: Int = 1
) : Reward {

    override fun rewardType(): RewardType {
        return RewardType.ITEM
    }

    override fun getDisplayText(): String {
        return "${if (quantity > 1) "$quantity x " else ""}$name"
    }

    fun getName(): String {
        return name
    }
}
