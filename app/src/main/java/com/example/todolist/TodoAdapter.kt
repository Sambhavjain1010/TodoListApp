package com.example.todolist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_task.*
import kotlinx.android.synthetic.main.item_todo.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

class TodoAdapter(val list : List<TodoModel>) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        return TodoViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_todo,parent,false))
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemId(position: Int): Long {
        return list[position].id
    }

    override fun getItemCount() : Int = list.size

    class TodoViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(todoModel: TodoModel) {
            with(itemView){
                val colors = resources.getIntArray(R.array.random_color)
                val randomColor = colors[java.util.Random().nextInt(colors.size)]
                viewColorTag.setBackgroundColor(randomColor)
                tvTitle.text = todoModel.title
                tvCategory.text = todoModel.category
                tvSubtitle.text = todoModel.description
                updateTime(todoModel.time)
                updateDate(todoModel.date)
            }
        }
        private fun updateTime(time : Long) {
            val myFormat = "h:mm a"
            val stf = SimpleDateFormat(myFormat)
            itemView.tvTime.text = stf.format(Date(time))
        }
        private fun updateDate(date : Long) {
            val myFormat = "h:mm a"
            val stf = SimpleDateFormat(myFormat)
            itemView.tvDate.text = stf.format(Date(date))
        }
    }
}