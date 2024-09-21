package com.example.demo.model

import jakarta.persistence.Entity
import jakarta.persistence.Version
import jakarta.persistence.Id
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import java.sql.Timestamp
import java.time.LocalDateTime

@Entity
data class PodState(
    val podName: String,
    var namespace: String,
    var status: String? = null,
    var lastChecked: LocalDateTime? = null,
    @Version
    val version: Timestamp? = null,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
)