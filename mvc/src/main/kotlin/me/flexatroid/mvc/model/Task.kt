package me.flexatroid.mvc.model

data class Task(var taskId: Int = 0, var listId: Int = 0, var description: String = "", var completed: Boolean = false) {
    override fun toString(): String {
        return "Task: '$description', completed: $completed"
    }
}