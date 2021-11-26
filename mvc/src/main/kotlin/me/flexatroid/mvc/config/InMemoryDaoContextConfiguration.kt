package me.flexatroid.mvc.config

import me.flexatroid.mvc.dao.TaskDao
import me.flexatroid.mvc.dao.TaskInMemoryDao
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile("memory")
class InMemoryDaoContextConfiguration {
    @Bean
    fun taskDao(): TaskDao {
        return TaskInMemoryDao()
    }
}