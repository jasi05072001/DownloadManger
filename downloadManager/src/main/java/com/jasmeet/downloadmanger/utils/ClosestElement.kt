package com.jasmeet.downloadmanger.utils

import kotlin.math.abs

fun returnClosestElement(list: List<Int>, target: Int): Int {
    var closest = list[0]
    var diff = abs(target - closest)
    for (i in 1 until list.size) {
        val newDiff = abs(target - list[i])
        if (newDiff < diff) {
            diff = newDiff
            closest = list[i]
        }
    }
    return closest
}
