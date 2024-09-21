package com.example.demo.controller

import com.example.demo.model.PodState
import com.example.demo.service.KubernetesService
import com.example.demo.service.PodCheckService
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/pods")
class PodStateController(val podCheckService: PodCheckService, val kubernetesService: KubernetesService) {

    @GetMapping
    fun getAllPods(): List<PodStateDTO> = podCheckService.getAllPodStates().map { mapToDTO(it) }.toList()

    @GetMapping("/{id}")
    fun getPodByName(@PathVariable id: Long): PodStateDTO = mapToDTO(podCheckService.getPodState(id))

    @PostMapping
    fun saveSimulatedPod(@RequestBody podState: SimulatedPodState): PodStateDTO {
        val newPodState = PodState(
            podName = podState.podName,
            namespace = podState.namespace
            )
        return mapToDTO(podCheckService.saveOrUpdate(newPodState))
    }

    @PutMapping("/{id}/status")
    @Transactional
    fun updateSimulatedPodStatus(@PathVariable id: Long, @RequestParam status: String): PodStateDTO {
        val pod = podCheckService.getPodState(id)
        pod.status = status
        pod.lastChecked = LocalDateTime.now()
        return mapToDTO(podCheckService.saveOrUpdate(pod))
    }

    @PostMapping("/deploy")
    fun deployPod(@RequestBody deployPodDTO: DeployPodDTO) {
        kubernetesService.deployKubernetesPod(deployPodDTO.namespace, deployPodDTO.podBody)
    }

    private fun mapToDTO(podState: PodState) : PodStateDTO =
        PodStateDTO(
            podName = podState.podName,
            namespace = podState.namespace,
            status = podState.status,
            lastChecked = podState.lastChecked,
            id = podState.id
        )

}

data class SimulatedPodState(
    @JsonProperty("podName") val podName: String,
    @JsonProperty("namespace") val namespace: String
)

data class PodStateDTO(
    val podName: String,
    var namespace: String,
    var status: String?,
    var lastChecked: LocalDateTime?,
    val id: Long?
)

data class DeployPodDTO(
    @JsonProperty("namespace") val namespace: String,
    @JsonProperty("podBody") val podBody: String
)