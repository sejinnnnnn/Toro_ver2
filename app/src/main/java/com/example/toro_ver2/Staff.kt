package com.example.toro_ver2

import java.util.*

class Staff {
    var _name: String? = null
    var _min_time: Int = 0
        private set
    var _max_time: Int = 0
        private set
    var _work_time = 0
        set(time) {
            field += time
        }

    var already = HashSet<Int>()

    var enable_time : Array<BooleanArray>

    init {
        enable_time = Array(7) { BooleanArray(24)}
        for(i in 0..6) {
            val rows = enable_time[i]
            Arrays.fill(rows, false)
        }
    }

    val _minus: Int
        get() = this._min_time - this._work_time

    fun set_min_max_time(min_time: Int, max_time: Int) {
        this._min_time = min_time
        this._max_time = max_time
    }

    fun workable(): Boolean {
        return this._work_time < this._max_time
    }

    fun init_enable(day: Int, start_time: Int, end_time: Int) {
        for(i in start_time..end_time) {
            enable_time[day][i] = true
        }
    }

    fun set_enable(day: Int, time: Int, work: Boolean) {
        enable_time[day][time] = work
    }

    fun printEnable(){
        for(i in 0..6){
            for(j in 0..23){
                print(enable_time[i][j].toString() + " ")
            }
            println()
        }
    }

}