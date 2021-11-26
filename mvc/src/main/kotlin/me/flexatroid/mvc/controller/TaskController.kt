package me.flexatroid.mvc.controller

import me.flexatroid.mvc.dao.TaskDao
import me.flexatroid.mvc.model.Task
import me.flexatroid.mvc.model.TaskList
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam


@Controller
class TaskController(private val taskDao: TaskDao) {
    @GetMapping("/get-lists")
    fun getAllLists(modelMap: ModelMap): String {
        prepareModelMap(modelMap, taskDao.getAllLists())
        return "index"
    }

    @PostMapping("/add-list")
    fun addList(@ModelAttribute("taskList") taskList: TaskList): String {
        taskDao.addList(taskList)
        return "redirect:/get-lists"
    }

    @PostMapping("/add-task")
    fun addTask(@ModelAttribute("task") task: Task): String {
        taskDao.addTask(task)
        return "redirect:/get-lists"
    }

    @PostMapping("/delete-list")
    fun deleteList(@RequestParam("listId") listId: Int): String {
        taskDao.deleteList(listId)
        return "redirect:/get-lists"
    }

    @PostMapping("/delete-task")
    fun deleteTask(@RequestParam("listId") listId: Int, @RequestParam("taskId") taskId: Int): String {
        taskDao.deleteTask(listId, taskId)
        return "redirect:/get-lists"
    }

    @PostMapping("/complete")
    fun markAsCompleted(@RequestParam("listId") listId: Int, @RequestParam("taskId") taskId: Int): String {
        taskDao.complete(listId, taskId)
        return "redirect:/get-lists"
    }

    @GetMapping("/")
    fun getIndex(map: ModelMap): String {
        prepareModelMap(map, taskDao.getAllLists())
        return "index"
    }

    private fun prepareModelMap(map: ModelMap, lists: List<TaskList>) {
        map.addAttribute("task", Task())
        map.addAttribute("taskList", TaskList())
        map.addAttribute("lists", lists)
    }
}