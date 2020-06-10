package com.example.toro_ver2

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.toro_ver2.Manager
import com.example.toro_ver2.R
import kotlinx.android.synthetic.main.activity_main.*
import org.w3c.dom.Text

class MainActivity : AppCompatActivity(){

    val row = 16
    val column = 8
    val id = 21

    val tableIdCode = 10551

    lateinit var m:Manager

    val tableLayout by lazy { TableLayout(this) }
    lateinit  var tview : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        change()
        createTable(row, column)
        fill_table(m.getDept()[0])
        onClick(m.getDept()[0])

    }
    fun init() {

        m = Manager()
        m.readFile_staff_info(this)
        m.readFile_schedule(this)
        m.readFile_days_needNum()
        m.scheduling()
        for(i in m.staves.staves.iterator()){
            i.printEnable()
        }

        change.setOnClickListener {
            val build = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.dialog, null)
            val dialogText = dialogView.findViewById<EditText>(R.id.changename)
            build.setView(dialogView)
                .setPositiveButton("확인") { dialogInterface, i ->
                    name.text = dialogText.text.toString()

                    /* 확인일 때 main의 View의 값에 dialog View에 있는 값을 적용 */

                }
                .setNegativeButton("취소") { dialogInterface, i ->
                    /* 취소일 때 아무 액션이 없으므로 빈칸 */
                }
                .show()
        }
    }


    fun change(){
        var arr = arrayOf("주방","배달","서빙")
        var i=0
        k1.setOnClickListener {
//            createCustomToast()
            if(i==0){
                i = 2
            } else {
                i--
            }
            department.setText(arr[i])
            fill_table(m.getDept()[i])
            onClick(m.getDept()[i])
        }
        k2.setOnClickListener {
            if(i==2)
                i = 0
            else i++
            department.setText(arr[i])
            fill_table(m.getDept()[i])
            onClick(m.getDept()[i])
        }

    }

    fun createTable(rows: Int, cols: Int) {
//        val lp = TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
//        tableLayout.apply {
//            layoutParams = lp
//            isShrinkAllColumns = true

//        }

        for (i in 0 until rows) {
            var num : Int
            var day:Int
            var id:Int
            var time:Int
            var str = ""
            val row = TableRow(this)
            row.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)

            for (j in 0 until cols) {
                val textview = TextView(this)
                var str : String

                textview.apply {
                    layoutParams = TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT
                    )

                    textview.width = 120
                    textview.height = 70

                    if(i==0&&j!=0) {
                        textview.setBackgroundColor(Color.parseColor("#F2BB09"))
                        if(j==1)textview.setText("월")
                        if(j==2)textview.setText("화")
                        if(j==3)textview.setText("수")
                        if(j==4)textview.setText("목")
                        if(j==5)textview.setText("금")
                        if(j==6)textview.setText("토")
                        if(j==7)textview.setText("일")
                        textview.textAlignment = View.TEXT_ALIGNMENT_CENTER
                    }
                    else if(i!=0) {
                        num = i - 1

                        if(j == 0){
                            textview.text = (8 + i).toString() + ":00"
                            textview.textAlignment = View.TEXT_ALIGNMENT_CENTER
                        }

                        /*
                        if (i == 1) {
                            str = "0$j"
                            Log.v("hi", str)
                            textview.setId(str.toInt())
                        } else str = "$num$j"
                        textview.setId(str.toInt())

                        text = (textview.getId()).toString()
                        id = (textview.getId().toInt())
                        if (id < 10) {
                            day = (id - 1)
                            time = 9
                        } else if (id >= 10 && id < 100) {
                            day = text.get(1).toInt() - '0'.toInt()
                            time = text.get(0).toInt() - '0'.toInt()

                        }else{
                            day = text.get(2).toInt()- '0'.toInt()
                            time =  (text.get(0).toInt()-'0'.toInt())*10 + text.get(1).toInt()-'0'.toInt()
                        }
                        str = day.toString()+" / "+time.toString()
                        */

                        if(i != 0 && j != 0){
                            day = j - 1
                            time = 8 + i
                            str = "$tableIdCode$day$time"
                            textview.setId(str.toInt())

                            print(str + " ")
                        }
                    }
                }
                row.addView(textview)
            }
            tableLayout.addView(row)
            println()

        }
        table.addView(tableLayout)
    }

    // 시간표 클릭시 직원 확인
    fun onClick(department: Department){

//        val dept: ArrayList<Department> = m.getDept()

        for(i in 0..6){
            for(j in 9..23){
                val str = "$tableIdCode$i$j"
                val t = findViewById<TextView>(str.toInt())

                val need = department.needs_num[i][j]

                t.setOnClickListener {
                    var x = 0
                    var y = 0
                    var getView = t.getRootView()
                    x = x + t.left - t.width
                    y = y + t.height*(j - 11)
                    Log.v("click", "Day: " + i.toString() + ", " + "Time: " + j.toString())

                    //////////////// TEST //////////////////
//                    var test1 = ""
//                    for(i in m.staves.iterator()){
//                        test1 += i._name
//                        test1 += " "
//                    }
//                    Log.v("All Staff: ", test1)
                    /////////////////////////////////////////

                    department.printFix()

                    val staff_name = department.get_fixed_staves_name(i, j, m.staves.staves)
                    for(i in staff_name.iterator()){
                        Log.v("S_NAME", i)
                    }
                    var arg = ""
                    val filled = staff_name.size
                    for(i in staff_name.iterator()){
                        arg += i
                        arg += "\n"
                    }
                    var rate = filled.toString() + "/" + need.toString()
                    var arg2 = rate + "\n" + arg


                    Log.w("Staff:", arg)
                    createCustomToast(arg2,x,y)
                }
            }
        }

    }

    // 시간표 채우기
    fun fill_table(department: Department){

        // Fill Table
        val ratio_color = department.ratio_color

        for(i in 0..6){
            for(j in 9..23){
                val str = "$tableIdCode$i$j"
                val textview = findViewById<TextView>(str.toInt())

                // 비율 표시
                val need = department.needs_num[i][j]
                var filled = department.get_fixed_staves_name(i, j, m.staves.staves).size
                var rate = filled.toString() + "/" + need.toString()
                textview.text = rate
                textview.textAlignment = View.TEXT_ALIGNMENT_CENTER

                if(ratio_color[i][j] == 0){
                    textview.setBackgroundColor(Color.parseColor("#D4E8CA"))
                } else if(ratio_color[i][j] == 1){
                    textview.setBackgroundColor(Color.parseColor("#FCF5E0"))
                } else if(ratio_color[i][j] == 2){
                    textview.setBackgroundColor(Color.parseColor("#E6C0C0"))
                }
                else {
                    textview.setBackgroundColor(Color.WHITE)
                }

                // 시간표가 채워지는 부분
//                textview.setBackgroundResource(R.drawable.table_border)

                print(ratio_color[i][j].toString() + " ")

            }
            println()
        }
    }


    fun createCustomToast(str: String,x:Int,y:Int){
        val toast = Toast(this)
        val layout = layoutInflater.inflate(R.layout.maketoast,null)
        val t1 = layout.findViewById<TextView>(R.id. img_text)
        t1.text=str
        toast.view = layout
        toast.setGravity(Gravity.LEFT,x,y)
        toast.show()
    }
}
