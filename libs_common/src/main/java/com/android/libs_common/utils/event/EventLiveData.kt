package com.live.common.utils.event

/**
 * @author : fansan
 * @date   : 2020/6/8
 */
class EventLiveData<T> : EventBaseLiveData<T> {
    /**
     * Creates a MutableLiveData initialized with the given `value`.
     *
     * @param value initial value
     */
    constructor(value: Event<T>?) : super(value!!) {}

    /**
     * Creates a MutableLiveData with no value assigned to it.
     */
    constructor() : super() {}

    fun postValue(value: T) {
        super.postEvent(Event(value))
    }

    fun setValue(value: T) {
        super.setEvent(Event(value))
    }

    fun call(){
        super.postEvent(Event(null))
    }
}