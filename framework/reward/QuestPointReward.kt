package com.recursive.quester.framework.reward

class QuestPointReward(val points: Int) : Reward {

    override fun rewardType(): RewardType {
        return RewardType.QUEST_POINT
    }

    override fun getDisplayText(): String {
        return "$points Quest Point${if (points != 1) "s" else ""}"
    }
}
