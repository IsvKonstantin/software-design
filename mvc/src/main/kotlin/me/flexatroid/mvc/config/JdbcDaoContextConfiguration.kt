package me.flexatroid.mvc.config

import me.flexatroid.mvc.dao.TaskDao
import me.flexatroid.mvc.dao.TaskJdbcDao
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import javax.sql.DataSource

@Configuration
@Profile("database")
class JdbcDaoContextConfiguration {
    @Bean
    fun taskDao(dataSource: DataSource): TaskDao {
        return TaskJdbcDao(dataSource)
    }
}