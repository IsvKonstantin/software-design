package me.flexatroid.mvc.dao

import me.flexatroid.mvc.model.Task
import me.flexatroid.mvc.model.TaskList
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

class TaskInMemoryDao : TaskDao {
    private val lastListId = AtomicInteger(0)
    private val lastTaskId = AtomicInteger(0)
    private val taskListMap: MutableMap<Int, TaskList> = ConcurrentHashMap()

    override fun getAllLists(): List<TaskList> {
        return taskListMap.values.toList()
    }

    override fun addList(taskList: TaskList) {
        val id = lastListId.incrementAndGet()
        taskList.listId = id
        taskListMap[id] = taskList
    }

    override fun addTask(task: Task) {
        val taskList = taskListMap[task.listId]
        val taskId = lastTaskId.incrementAndGet()
        task.taskId = taskId
        taskList!!.tasks.add(task)
    }

    override fun deleteList(listId: Int) {
        taskListMap.remove(listId)
    }

    override fun deleteTask(listId: Int, taskId: Int) {
        taskListMap[listId]!!.tasks.removeIf { it.taskId == taskId }
    }

    override fun complete(listId: Int, taskId: Int) {
        val taskList = taskListMap[listId]
        val task = taskList!!.tasks.firstOrNull { it.taskId == taskId }
        task!!.completed = true
    }
}