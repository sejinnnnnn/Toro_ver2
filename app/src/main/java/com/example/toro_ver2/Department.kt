package com.example.toro_ver2

import java.util.*
import android.R.attr.name
import android.R.attr.name
import android.util.Log
import com.example.toro_ver2.Time_table
import java.util.Collections.sort


class Department {
    var _name: String? = null
    private val work_days = BooleanArray(7)
    public val needs_num: Array<IntArray>
    private val time_table: ArrayList<Time_table>
    public val fixed_table: Array<Array<Time_cell?>>
    public val ratio_color: Array<IntArray>

    init {
        Arrays.fill(this.work_days, false)
        this.needs_num = Array(7) { IntArray(24) }
        var var4: Array<IntArray>
        var4 = this.needs_num
        var var3 = var4.size

        var rows: IntArray
        var j: Int
        j = 0
        while (j < var3) {
            rows = var4[j]
            Arrays.fill(rows, 0)
            ++j
        }

        this.time_table = ArrayList()
        this.fixed_table = Array(7) { arrayOfNulls<Time_cell>(24) }

        for (i in 0..6) {
            j = 0
            while (j < 24) {
                this.fixed_table[i][j] = Time_cell()
                ++j
            }
        }

        this.ratio_color = Array(7) { IntArray(24) }
        var4 = this.ratio_color
        var3 = var4.size

        j = 0
        while (j < var3) {
            rows = var4[j]
            Arrays.fill(rows, -1)
            ++j
        }

    }

    fun set_Dname(name: String) {
        this._name = name
    }

    fun get_Dname(): String {
        return this._name!!
    }
    fun printFix() {
        for (i in 0..6) {
            for (j in 0..23) {
                val sSize = this.fixed_table[i][j]!!.size()
                for (k in 0 until sSize) {
                    print(this.fixed_table[i][j]!!.get_staff_num(k).toString() + " ")
                }
                print("|")
            }
            println()
        }

    }

    fun printTimeTable() {
        val temp = this.time_table[0]

        for (i in 0..6) {
            for (j in 0..23) {
                val sSize = temp[i, j]!!.size()

                for (k in 0 until sSize) {
                    print(temp[i, j]!!.get_staff_num(k).toString() + " ")
                }

                println()
            }
        }

    }

    fun get_fixed_staves_name(day: Int, start_time: Int, staves: ArrayList<Staff>): ArrayList<String> {
        val ret = ArrayList<String>()
        val size = this.fixed_table[day][start_time]!!.size()

        Log.e("Err", _name)
        for (i in 0 until size) {
            ret.add(staves[this.fixed_table[day][start_time]!!.get_staff_num(i)]._name!!)
            Log.e("Err", staves[this.fixed_table[day][start_time]!!.get_staff_num(i)]._name!!)
        }
        return ret
    }

    fun set_work_days(work_day: Int) {
        this.work_days[work_day] = true
    }

    fun set_needs_num(day: Int, time: Int, num: Int) {
        this.needs_num[day][time] = num
    }

    fun set_time_table(priority: Int, day: Int, start_time: Int, end_time: Int, staff_num: Int) {
        var time: Int
        if (priority >= this.time_table.size) {
            time = this.time_table.size
            while (time <= priority) {
                this.time_table.add(Time_table())
                ++time
            }
        }

        val temp: Int
        if (start_time > end_time) {
            temp = 24

            time = 0
            while (time < end_time) {
                this.time_table[priority][day, time]!!.set(staff_num)
                ++time
            }
        } else {
            temp = end_time
        }

        time = start_time
        while (time < temp) {
            this.time_table[priority][day, time]!!.set(staff_num)
            ++time
        }

    }

    fun set_color() {
        for (day in 0..6) {
            if (this.work_days[day]) {
                for (time in 0..23) {
                    val needs = this.needs_num[day][time]
                    val fixed_num = this.fixed_table[day][time]!!.size()
                    if (needs != 0) {
                        if (fixed_num == needs) {
                            this.ratio_color[day][time] = 0
                        } else if (fixed_num < needs) {
                            if (fixed_num.toDouble() >= needs.toDouble() / 2.toDouble()) {
                                this.ratio_color[day][time] = 1
                            } else {
                                this.ratio_color[day][time] = 2
                            }
                        }
                    }
                }
            }
        }

    }

    fun get_color() {
        for (i in 0..6) {
            for (j in 0..23) {
                print(ratio_color[i][j].toString() + " ")
            }
            println()
        }
    }

    fun scheduling(priority: Int, staves: ArrayList<Staff>) {
        for (day in 0..6) {
            if (this.work_days[day]) {

                for (time in 0..23) {
                    val needs = this.needs_num[day][time]

                    //우선순위에 해당하는 테이블이 없을 경우 스케쥴링하지 않는다.
                    if (priority >= this.time_table.size) return

                    val staves_num = this.time_table[priority][day, time]!!.size()
                    if (needs != 0 && this.ratio_color[day][time] != 0) {
                        val map = HashMap<Int, Int>()
                        print(".")
                        for (i in 0 until staves_num) {
                            val staff_num = time_table[priority][day, time]!!.get_staff_num(i)

                            if (staves[staff_num].workable()) { //근무 가능시간 초과했는지 확인
                                if( (staves[staff_num]).already.contains(day*100+time))
                                    continue;
                                val minus = staves[staff_num]._minus
                                map[staff_num] = minus
                            }
                        }
                        //                        int i;
                        //                        int temp_num;
                        //                        for(i = 0; i < staves_num; ++i) {
                        //                            i = ((Time_table)this.time_table.get(priority)).get(day, time).get_staff_num(i);
                        //                            if (((Staff)staves.get(i)).workable()) {
                        //                                temp_num = ((Staff)staves.get(i)).get_minus();
                        //                                map.put(i, temp_num);
                        //                            }
                        //                        }

                        val list = map.toList().sortedBy { (_, value) -> value}.toMap()

                        val it = list.iterator()
                        var i = this.fixed_table[day][time]!!.size()
                        while (it.hasNext()) {
                            if (i == needs) {
                                this.ratio_color[day][time] = 0
                                break
                            }

                            if (i > needs) {
                                println("working staves more than needs")
                                break
                            }
                            val temp_num = it.next()
                            val t_num = temp_num.key

                            this.fixed_table[day][time]!!.set(t_num)

//                            if(staves[t_num].enable_time[day][time]){
//                                staves[t_num].enable_time[day][time] = false
//                            } else{
//                                continue
//                            }

                            //                            System.out.println(temp_num);
                            staves[t_num]._work_time = 1
                            (staves[t_num] as Staff).already.add(day * 100 + time)
                            i++
                        }
                    }
                }
            }
        }

        this.set_color()
    }

/*
    companion object {

        fun sortByValue(map: Map<Int, Int>): ArrayList<Int> {
            val list = ArrayList<Int>()
            list.addAll(map.keys)
            sort<Int>(list, object : Comparator<Int> {
                override fun compare(o1: Int?, o2: Int?): Int {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    val v1 = map[o1]
                    val v2 = map[o2]
                    return (v2 as Comparable<Int>).compareTo(v1!!)
                }

            })
            Collections.reverse(list)
            return list
        }
    }
*/
}
