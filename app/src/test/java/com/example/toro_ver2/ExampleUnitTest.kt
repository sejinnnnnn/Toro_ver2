package com.example.toro_ver2

import android.net.Uri
import android.util.Log
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.junit.Test

import org.junit.Assert.*
import java.io.FileInputStream
import java.io.IOException

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun test(){
        try {
            val fis = FileInputStream("src\\main\\res\\raw\\schedule.xls")

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
          //          Log.v("File I/O", sNum.toString())

                    for (i in 0..6) {
                        print(days[i] + " ")
                    }

                    println("$min $max")
                }

                workbook.close()
            }
        } catch (var22: IOException) {
            var22.printStackTrace()
        }
    }
}
