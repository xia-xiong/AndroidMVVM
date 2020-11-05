package com.live.common.utils.event

import java.util.*

/**
 * @author : fansan
 * @date   : 2020/6/8
 */
class Event<T>(content: T?) {
    private var content: T?
    private var hasHandled = false
    private var isDelaying = false
    fun getContent(): T? {
        return if (!hasHandled) {
            hasHandled = true
            isDelaying = true
            val timer = Timer()
            val task: TimerTask = object : TimerTask() {
                override fun run() {
                    content = null
                    isDelaying = false
                }
            }
            timer.schedule(task, 1000)
            content
        } else if (isDelaying) {
            content
        } else {
            null
        }
    }

    init {
        this.content = content
    }
}