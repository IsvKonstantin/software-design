package me.flexatroid.mvc.dao

import me.flexatroid.mvc.model.Task
import me.flexatroid.mvc.model.TaskList
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import javax.sql.DataSource


@Suppress("SqlConstantCondition")
class TaskJdbcDao(dataSource: DataSource) : TaskDao {
    private val jdbcTemplate = JdbcTemplate(dataSource)

    override fun getAllLists(): List<TaskList> {
        val sql = "SELECT * FROM Tasks NATURAL JOIN Lists ORDER BY ListId"
        val taskLists = mutableMapOf<Int, TaskList>()
        val listMapper = RowMapper<Unit> { rs, _ ->
            val listId = rs.getInt("ListId")
            if (!taskLists.containsKey(listId)) {
                taskLists[listId] = TaskList(listId = listId, name = rs.getString("ListName"))
            }
            taskLists[listId]!!.tasks.add(
                Task(
                    rs.getInt("TaskId"),
                    listId,
                    rs.getString("TaskDescription"),
                    rs.getBoolean("TaskCompleted")
                )
            )
            return@RowMapper
        }

        jdbcTemplate.query(sql, listMapper)
        return taskLists.values.toList()
    }

    override fun addList(taskList: TaskList) {
        val sql = "INSERT INTO Lists (ListName) VALUES (?)"
        jdbcTemplate.update(sql, taskList.name)
    }

    override fun addTask(task: Task) {
        val sql = "INSERT INTO tasks (ListId, TaskDescription, TaskCompleted) VALUES (?, ?, ?)"
        jdbcTemplate.update(sql, task.listId, task.description, false)
    }

    override fun deleteList(listId: Int) {
        val sql = "DELETE FROM Lists WHERE ListId = $listId"
        jdbcTemplate.update(sql)
    }

    override fun deleteTask(listId: Int, taskId: Int) {
        val sql = "DELETE FROM Tasks WHERE TaskId = $taskId"
        jdbcTemplate.update(sql)
    }

    override fun complete(listId: Int, taskId: Int) {
        val sql = "UPDATE Tasks SET TaskCompleted = true WHERE TaskId = $taskId"
        jdbcTemplate.update(sql)
    }
}