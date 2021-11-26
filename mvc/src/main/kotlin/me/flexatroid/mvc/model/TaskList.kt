package me.flexatroid.mvc.model

data class TaskList(var listId: Int = 0, var name: String = "", var tasks: MutableList<Task> = mutableListOf()) {
    override fun toString(): String {
        return "Task list '$name': $tasks"
    }
}