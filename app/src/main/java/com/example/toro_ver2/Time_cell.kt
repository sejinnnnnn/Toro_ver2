package com.example.toro_ver2

import java.util.ArrayList

class Time_cell : Cloneable {
    private val workable_staves_num = ArrayList<Int>()

    fun set(staff_num: Int) {
        this.workable_staves_num.add(staff_num)
    }

    fun size(): Int {
        return this.workable_staves_num.size
    }

    fun get_staff_num(idx: Int): Int {
        return this.workable_staves_num.get(idx)
    }

    @Throws(CloneNotSupportedException::class)
    override fun clone(): Any {
        return super.clone()
    }
}

