import aspect.ProfilingExecutionTimeAspect
import dao.PlaneInMemoryDao
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.EnableAspectJAutoProxy
import passenger.model.implementations.PassengerBusiness
import passenger.model.implementations.PassengerEconomy
import plane.PlaneManager
import plane.PlaneManagerImpl

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
open class ContextConfiguration {
    @Bean
    open fun planeManager(): PlaneManager {
        return PlaneManagerImpl(PlaneInMemoryDao())
    }

    @Bean
    open fun passengerEconomy(): PassengerEconomy {
        return PassengerEconomy()
    }

    @Bean
    open fun passengerBusiness(): PassengerBusiness {
        return PassengerBusiness()
    }

    @Bean
    open fun aspect(): ProfilingExecutionTimeAspect {
        return ProfilingExecutionTimeAspect()
    }
}