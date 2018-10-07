package com.example.demokt.service

import com.example.demokt.config.ApplicationProperties
import com.example.demokt.entity.User
import com.example.demokt.repository.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.util.*
import javax.transaction.Transactional

@Service
class UserService(
        private val applicationProperties: ApplicationProperties,
        private val userRepository: UserRepository
) {

    @Transactional
    fun save(userName: String, nickname: String, rawPassword: ByteArray, oldPassword: ByteArray?): User {

        val origin = userRepository.findOneByUsername(userName)

        when {
            origin != null && oldPassword == null -> throw IllegalArgumentException("old password required")
            origin != null && oldPassword != null -> run {
                val digest = origin.algorithm.digest(oldPassword, origin.salt ?: "".toByteArray())
                if (digest != origin.password) throw IllegalArgumentException("old password did not correct")
            }
        }

        val salt = System.currentTimeMillis().toString().toByteArray()
        val password = applicationProperties.passwordStrategies.digest(rawPassword, salt)

        val user = User(userName, nickname, applicationProperties.passwordStrategies, password, salt)

        if (origin != null) run {
            user.id = origin.id
        }

        return userRepository.save(user)
    }

    fun findUsers(current: Int, size: Int): Page<User> {
        return userRepository.findAll(PageRequest.of(current, size))
    }

    fun findById(id: String): Optional<User>? {
        return userRepository.findById(id)
    }

    fun del(id: String, password: ByteArray): Boolean {
        val optional = userRepository.findById(id)
        if (!optional.isPresent)
            return false

        val origin = optional.get()

        val digest = origin.algorithm.digest(password, origin.salt ?: "".toByteArray())
        if (digest != origin.password) throw IllegalArgumentException("old password did not correct")
        userRepository.delete(origin)
        return true
    }
}