package com.recursive.quester.framework.reward


interface Reward {
    fun rewardType(): RewardType
    fun getDisplayText(): String
}