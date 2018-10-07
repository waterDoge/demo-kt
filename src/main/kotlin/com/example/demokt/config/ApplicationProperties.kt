package com.example.demokt.config

import com.example.demokt.strategy.PasswordStrategies
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "config")
class ApplicationProperties(
        var passwordStrategies: PasswordStrategies = PasswordStrategies.SHA_256
)