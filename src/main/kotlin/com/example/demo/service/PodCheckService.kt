package com.example.demo.service

import com.example.demo.model.PodState
import com.example.demo.repository.PodStateRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class PodCheckService(private val podStateRepository: PodStateRepository) {

    fun getAllPodStates(): List<PodState> {
        return podStateRepository.findAll()
    }

    fun getPodState(id: Long): PodState {
        return podStateRepository.findByIdOrNull(id) ?: throw PodStateNotFoundException()
    }

    fun saveOrUpdate(podState: PodState): PodState {
        return podStateRepository.save(podState)
    }

    fun updatePodHealth(name: String, namespace: String, status: String) {
        val podState = podStateRepository.findByPodNameAndNamespace(name, namespace) ?: PodState(name, namespace)
        //if (status != podState.status) {  //Uncomment this if just the time of the last status change needs to be tracked, and no live view on the status is required
            podState.status = status
            podState.lastChecked = LocalDateTime.now()
            podStateRepository.save(podState)
        //}
    }
}

class PodStateNotFoundException : RuntimeException()
