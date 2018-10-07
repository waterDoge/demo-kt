package com.example.demokt

import com.example.demokt.config.ApplicationProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.core.MethodParameter
import org.springframework.core.ReactiveAdapterRegistry
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer
import org.springframework.web.reactive.result.method.annotation.RequestParamMethodArgumentResolver
import org.springframework.web.server.ServerWebExchange

@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties::class)
class DemoKtApplication

fun main(args: Array<String>) {
    runApplication<DemoKtApplication>(*args)
}

//与传统方式不同, webflux默认form表单数据无法绑定到参数
@Configuration
class WebConfig : WebFluxConfigurer {

    @Autowired
    private lateinit var applicationContext: ConfigurableApplicationContext

    override fun configureArgumentResolvers(configurer: ArgumentResolverConfigurer) {

        class RequestFormDataMethodArgumentResolver(
                beanFactory: ConfigurableBeanFactory,
                registry: ReactiveAdapterRegistry,
                useDefaultResolution: Boolean
        ): RequestParamMethodArgumentResolver(beanFactory, registry, useDefaultResolution) {

            override fun resolveNamedValue(name: String, parameter: MethodParameter, exchange: ServerWebExchange): Any? {
                var result: Any? = null
                val formData = exchange.formData.block()
                val values = formData[name]
                if( null != values ){
                    result = values.takeUnless { it.size == 1 } ?: values[0]
                }
                return result
            }
        }
        configurer.addCustomResolver(RequestFormDataMethodArgumentResolver(applicationContext.beanFactory, ReactiveAdapterRegistry.getSharedInstance(), true))
    }
}

