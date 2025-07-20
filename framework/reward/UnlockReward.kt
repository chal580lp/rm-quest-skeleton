package com.recursive.quester.framework.reward

class UnlockReward(private val unlock: String) : Reward {

    override fun rewardType(): RewardType {
        return RewardType.UNLOCK
    }

    override fun getDisplayText(): String {
        return unlock
    }
}