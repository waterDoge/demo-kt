package com.example.demokt.repository

import com.example.demokt.entity.User
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: PagingAndSortingRepository<User, String> {
    fun findOneByUsername(username: String): User?
}