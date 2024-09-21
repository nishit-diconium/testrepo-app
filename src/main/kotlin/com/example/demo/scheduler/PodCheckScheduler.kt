package com.example.demo.scheduler

import com.example.demo.service.KubernetesService
import com.example.demo.service.PodCheckService
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class PodCheckScheduler(
    val podCheckService: PodCheckService,
    val kubernetesService: KubernetesService,
    @Value("\${app.scheduler.podCheck.simulate}") val simulatePodCheck: Boolean
) {

    private val logger = KotlinLogging.logger {}

    @Scheduled(fixedRateString = "\${app.scheduler.podCheck.interval}")
    fun schedulePodCheck() {
        when (simulatePodCheck) {
            true -> simulatePodCheck()
            false -> performRealPodCheck()
        }
    }

    private fun simulatePodCheck() {
        val pods = podCheckService.getAllPodStates()
        logger.info { "PodCheck: Performing check for ${pods.size} pods" }
        pods.forEach { pod ->
            logger.info { "PodCheck: Perform check for pod:\"$pod\"" }
            podCheckService.updatePodHealth(pod.podName, pod.namespace, "Healthy")
        }
    }

    private fun performRealPodCheck() {
        kubernetesService.runPodHealthCheck()
    }
}