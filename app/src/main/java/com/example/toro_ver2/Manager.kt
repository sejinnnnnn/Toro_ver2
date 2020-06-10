package com.example.toro_ver2

import android.content.Context
import android.util.Log
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import java.io.IOException


class Manager {
    public val staves = Staves()
    private val departments = ArrayList<Department>()
    private val staff_num_TO_depart_num = HashMap< Int, ArrayList<Int> >()
    private val depart_name_TO_depart_num = HashMap<String, Int>()
    private val staff_num_TO_staves_index = HashMap<Int, Int>()



    init {

    }

    //
    //    public void set_staves_name(int staff_num, String name) {
    //        ((Staff)this.staves.get(staff_num)).set_name(name);
    //    }
    //
    //    public void set_staves_min_time(int staff_num, int min_time, int max_time) {
    //        ((Staff)this.staves.get(staff_num)).set_min_max_time(min_time, max_time);
    //    }
    //
    //    public void staves_add() {
    //        this.staves.add(new Staff());
    //    }
    //
    //    public int get_staves_size() {
    //        return this.staves.size();
    //    }

    public fun readFile_staff_info(context: Context) {
        try {
            // val path = "android.resource://com.example.toro_ver2/" + R.raw.info
            // var uri = Uri.parse(path)
     //       val fi = File("android.resource://com.example.toro_ver2/raw/info.xlsx")
//            val fis = Resources.getSystem().assets.open("info.xls")

            /////////////////////// File IO PATH ERR ///////////////////////
//            val fis = FileInputStream("src/main/res/raw/info.xls")

            val fis = context.assets.open("info.xls")
            Log.v("File I/O", fis.toString())
            val workbook = HSSFWorkbook(fis)
            val sheet = workbook.getSheetAt(0)
            val rows = sheet.physicalNumberOfRows

            for (rowIndex in 1 until rows) {
                val row = sheet.getRow(rowIndex)
                if (row != null) {
                    val cell_1 = row.getCell(0)
                    val cell_2 = row.getCell(1)
                    val cell_3 = row.getCell(2)
                    val sNum = cell_1.numericCellValue.toInt()
                    val sName = cell_2.stringCellValue
                    val sDept = cell_3.stringCellValue
                    val sDept_arr = sDept.split(", ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    print("$sNum $sName ")

                    // Log.v("File I/O", sName.toString())

                    for (i in sDept_arr.indices) {
                        print(sDept_arr[i] + " ")
                    }

                    println()
                    this.arrange_info(sNum, sName, sDept_arr)
                }

                workbook.close()
            }
        } catch (var15: IOException) {
            var15.printStackTrace()
        }

    }

    public fun readFile_schedule(context: Context) {
        try {
//            val path ="src/main/res/raw/schedule.xls"
//            Log.v("File I/O", path)
//            val f = File(path).exists()
//            Log.v("File I/O", f.toString())
//            val fis = FileInputStream(path)

            val fis = context.assets.open("schedule.xls")

            val workbook = HSSFWorkbook(fis)
            val sheet = workbook.getSheetAt(0)
            val rows = sheet.physicalNumberOfRows

            for (rowIndex in 1 until rows) {
                val row = sheet.getRow(rowIndex)
                if (row != null) {
                    val cell_1 = row.getCell(0)
                    val cell_2 = row.getCell(1)
                    val cell_3 = row.getCell(2)
                    val cell_4 = row.getCell(3)
                    val cell_5 = row.getCell(4)
                    val cell_6 = row.getCell(5)
                    val cell_7 = row.getCell(6)
                    val cell_8 = row.getCell(7)
                    val cell_9 = row.getCell(8)
                    val cell_10 = row.getCell(9)
                    val sNum = cell_1.numericCellValue.toInt()
                    val days = arrayOf(
                        cell_2.stringCellValue,
                        cell_3.stringCellValue,
                        cell_4.stringCellValue,
                        cell_5.stringCellValue,
                        cell_6.stringCellValue,
                        cell_7.stringCellValue,
                        cell_8.stringCellValue
                    )
                    val min = cell_9.numericCellValue.toInt()
                    val max = cell_10.numericCellValue.toInt()
                    print("$sNum ")

                    // Test
                    // Log.v("File I/O", sNum.toString())

                    for (i in 0..6) {
                        print(days[i] + " ")
                    }

                    println("$min $max")
                    this.arrange_schedule(sNum, days, min, max)
                }

                workbook.close()
            }
        } catch (var22: IOException) {
            var22.printStackTrace()
        }

    }

    public fun readFile_days_needNum() {
        val days = 7
        val num = 1
        for (dept in departments.indices) {
            for (day in 0 until days) {
                //set_work_days
                departments.get(dept).set_work_days(day)
                //set_needs_num
                for (time in 9..23) {
                    departments.get(dept).set_needs_num(day, time, num)
                }
            }
        }

    }

    private fun arrange_info(staff_num: Int, staff_name: String, dept: Array<String>) {
        this.staves.staves.add(Staff())
        val staff_index = this.staves.staves.size - 1
        val depart_num = ArrayList<Int>()

        var i: Int
        i = 0
        while (i < dept.size) {
            val temp_dept: Int
            if (this.depart_name_TO_depart_num.containsKey(dept[i])) {
                temp_dept = this.depart_name_TO_depart_num.get(dept[i])!!
            } else {
                this.departments.add(Department())
                temp_dept = this.departments.size - 1
                this.departments.get(temp_dept).set_Dname(dept[i])
                this.depart_name_TO_depart_num.put(dept[i], temp_dept)
            }

            depart_num.add(temp_dept)
            ++i
        }

        (this.staves.staves.get(staff_index) as Staff)._name = staff_name
        this.staff_num_TO_depart_num.put(staff_num, depart_num)

        this.staff_num_TO_staves_index.put(staff_num, staff_index)
    }

    private fun arrange_schedule(staff_num: Int, days: Array<String>, min: Int, max: Int) {
        val startTime = IntArray(7)
        val endTime = IntArray(7)

        var staff_index: Int
        staff_index = 0
        while (staff_index < 7) {
            startTime[staff_index] =
                Integer.parseInt(days[staff_index].split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0])
            endTime[staff_index] =
                Integer.parseInt(days[staff_index].split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1])
            (this.staves.staves.get(staff_index) as Staff).init_enable(staff_index, startTime[staff_index], endTime[staff_index] - 1)

            ++staff_index
        }

        staff_index = this.staff_num_TO_staves_index.get(staff_num)!!
        (this.staves.staves.get(staff_index) as Staff).set_min_max_time(min, max)
        val dept_num = ArrayList<Int>()
        dept_num.addAll(this.staff_num_TO_depart_num!!.get(staff_num) as ArrayList)

        for (day in 0..6) {
            for (priority in dept_num.indices) {
                (this.departments.get(dept_num.get(priority) as Int) as Department).set_time_table(
                    priority,
                    day,
                    startTime[day],
                    endTime[day],
                    staff_index
                )
            }
        }
    }

    fun test() {
        println(this.departments.size)

        var i: Int
        i = 0
        while (i < this.departments.size) {
            for (j in this.departments.indices) {
                (this.departments.get(i) as Department).scheduling(i, this.staves.staves)
            }
            ++i
        }

        i = 0
        while (i < this.departments.size) {
            (this.departments.get(i) as Department).printFix()
            ++i
        }

    }

    public fun scheduling() {
        println("Scheduling Start")
        for (priority in this.departments.indices) {
            for (i in this.departments.indices) {
                println(this.departments.get(i).get_Dname())
                this.departments.get(i).scheduling(priority, this.staves.staves)
            }
        }
        println()
        for (i in this.departments.indices) {
            println(this.departments.get(i).get_Dname())
            (this.departments.get(i) as Department).printFix()
            this.departments.get(i).get_color()
        }
        println("Scheduling Finish")

    }

    fun getDept():ArrayList<Department>{
        return this.departments
    }

}
