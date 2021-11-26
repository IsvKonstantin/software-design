package me.flexatroid.mvc.dao

import me.flexatroid.mvc.model.Task
import me.flexatroid.mvc.model.TaskList

interface TaskDao {
    fun getAllLists(): List<TaskList>

    fun addList(taskList: TaskList)

    fun addTask(task: Task)

    fun deleteList(listId: Int)

    fun deleteTask(listId: Int, taskId: Int)

    fun complete(listId: Int, taskId: Int)
}