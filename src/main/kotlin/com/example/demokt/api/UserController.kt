package com.example.demokt.api

import com.example.demokt.entity.User
import com.example.demokt.service.UserService
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("users")
class UserController(private val userService: UserService) {
    @PostMapping
    fun create(username: String, password: ByteArray, nickname: String?, oldPassword: ByteArray?): Mono<User>? {

        return Mono.just(userService.save(username, nickname ?: username, password, oldPassword))
    }

    @GetMapping
    fun users(current: Int?, size: Int?): Flux<User> {
        return Flux.fromIterable(userService.findUsers(current?:0, size?:10))
    }

    @GetMapping("{id}")
    fun user(@PathVariable id: String): Mono<User> {
        return Mono.justOrEmpty(userService.findById(id))
    }

    @DeleteMapping("{id}")
    fun delUser(@PathVariable id: String, password: ByteArray): Mono<Boolean> {
        return Mono.justOrEmpty(userService.del(id, password))
    }
}