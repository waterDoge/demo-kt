package com.example.demokt.entity

import com.example.demokt.strategy.PasswordStrategies
import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.validation.constraints.Pattern

@Entity
class User(
        @Pattern(regexp = "[a-zA-Z][\\w]{3,64}") val username: String = "",
        var nickname: String? = "",
        @JsonIgnore var algorithm: PasswordStrategies = PasswordStrategies.SHA_256,
        @JsonIgnore var password: String? = null,
        @JsonIgnore var salt: ByteArray? = null
) {

    //在 JVM 虚拟机中，如果主构造函数的所有参数都有默认值，编译器会生成一个附加的无参的构造函数，这个构造函数会直接使用默认值。这使得 Kotlin 可以更简单的使用像 Jackson 或者 JPA 这样使用无参构造函数来创建类实例的库。
    //constructor() : this("", "")

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    var id: String = ""

    @CreationTimestamp
    var createdTime: LocalDateTime = LocalDateTime.now()

    @UpdateTimestamp
    var updatedTime: LocalDateTime = LocalDateTime.now()

}