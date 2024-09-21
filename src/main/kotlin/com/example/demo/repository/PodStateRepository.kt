package com.example.demo.repository

import com.example.demo.model.PodState
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PodStateRepository : JpaRepository<PodState, Long> {
    fun findByPodNameAndNamespace(podName: String, namespace: String): PodState?
}

