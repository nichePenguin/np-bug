package com.np.bug

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ConfigurationPropertiesScan
@ComponentScan("com.np.bug", "com.np.auth.common")
class BugApplication

fun main(args: Array<String>) {
	runApplication<BugApplication>(*args)
}
