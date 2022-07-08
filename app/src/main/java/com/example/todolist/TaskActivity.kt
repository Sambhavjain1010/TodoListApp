package com.example.todolist

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Adapter
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_task.*
import kotlinx.android.synthetic.main.item_todo.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.Time
import java.text.SimpleDateFormat

const val DB_NAME = "todo.db"
class TaskActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var myCalendar : Calendar

    lateinit var dateSetListener : DatePickerDialog.OnDateSetListener
    lateinit var timeSetListener : TimePickerDialog.OnTimeSetListener

    var finalDate = 0L
    var finalTime = 0L

    private val labels = arrayListOf("Personal","Business","Education","Insurance","Shopping","Banking")

    val db by lazy {
        AppDataBase.getDatabase(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        etDate.setOnClickListener(this)
        etTime.setOnClickListener(this)
        savebtn.setOnClickListener(this)

        setUpSpinner()
    }

    private fun setUpSpinner() {
        val adapter = ArrayAdapter<String>(this,
            com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
            labels)

        labels.sort()

        spinner.adapter = adapter
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.etDate -> {
                setDateListener()
            }
            R.id.etTime ->{
                setTimeListener()
            }
            R.id.savebtn ->{
                saveTodo()
            }
        }
    }

    private fun saveTodo() {
        val category = spinner.selectedItem.toString()
        val title = tvTitle.text.toString()
        val description = tvTask.text.toString()

        GlobalScope.launch(Dispatchers.IO) {
            val id = withContext(Dispatchers.IO){
                return@withContext db.todoDao().insertTask(
                    TodoModel(
                        title,
                        description,
                        category,
                        finalDate,
                        finalTime
                    )
                )
            }
            finish()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setTimeListener() {
        myCalendar = Calendar.getInstance()

        timeSetListener = TimePickerDialog.OnTimeSetListener{timePicker, hourOfDay : Int, min : Int ->
            myCalendar.set(Calendar.HOUR_OF_DAY,hourOfDay)
            myCalendar.set(Calendar.MINUTE,min)
            updateTime()
        }

        val timePickerDialog = TimePickerDialog(this,
            timeSetListener,
            myCalendar.get(Calendar.HOUR_OF_DAY),
            myCalendar.get(Calendar.MINUTE),
            true)

        timePickerDialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun updateTime() {
        val myFormat = "h:mm a"
        val stf = SimpleDateFormat(myFormat)
        finalTime = myCalendar.time.time
        etTime.setText(stf.format(myCalendar.time))
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setDateListener() {
        myCalendar = Calendar.getInstance()

        dateSetListener = DatePickerDialog.OnDateSetListener{ DatePicker, year : Int, month : Int, dayOfMonth : Int ->

            myCalendar.set(Calendar.YEAR,year)
            myCalendar.set(Calendar.MONTH,month)
            myCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth)
            updateDate()
        }

        val datePickerDialog = DatePickerDialog(this ,
            dateSetListener,
            myCalendar.get(Calendar.YEAR),
            myCalendar.get(Calendar.MONTH),
            myCalendar.get(Calendar.DAY_OF_MONTH))

        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun updateDate() {
        //Mon , 5 Jan 2022
        val myFormat = "EEE , d MMM yyyy"
        val sdf = SimpleDateFormat(myFormat)
        finalDate = myCalendar.time.time
        etDate.setText(sdf.format(myCalendar.time))

        etTime.visibility = View.VISIBLE
    }
}