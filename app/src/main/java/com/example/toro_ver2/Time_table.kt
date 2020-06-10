package com.example.toro_ver2

import com.example.toro_ver2.Time_cell

class Time_table {
    private var time_table = Array<Array<Time_cell?>>(7) { arrayOfNulls(24) }

    init {
        for (i in 0..6) {
            for (j in 0..23) {
                this.time_table[i][j] = Time_cell()
            }
        }

    }

    operator fun set(day: Int, start_time: Int, end_time: Int, staff_num: Int) {
        val temp: Int
        var time: Int
        if (start_time > end_time) {
            temp = 24

            time = 0
            while (time < end_time) {
                this.time_table[day][time]!!.set(staff_num)
                ++time
            }
        } else {
            temp = end_time
        }

        time = start_time
        while (time < temp) {
            this.time_table[day][time]!!.set(staff_num)
            ++time
        }

    }

    fun size(day: Int, time: Int): Int {
        return this.time_table[day][time]!!.size()
    }

    operator fun get(day: Int, time: Int): Time_cell? {
        return this.time_table[day][time]
    }
}
