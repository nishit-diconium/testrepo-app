package com.example.demo.service

import io.kubernetes.client.openapi.ApiClient
import io.kubernetes.client.openapi.ApiException
import io.kubernetes.client.openapi.Configuration
import io.kubernetes.client.openapi.apis.CoreV1Api
import io.kubernetes.client.openapi.models.V1Pod
import io.kubernetes.client.util.Config
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class KubernetesService(val podCheckService: PodCheckService) {
    private val logger = KotlinLogging.logger {}

    init {
        val client: ApiClient = Config.defaultClient()
        Configuration.setDefaultApiClient(client)

        // Here you could also set auth tokens via
        // (ApiKeyAuth) client.getAuthentication("BearerToken").setApiKey("TOKEN_GOES_HERE")
    }

    fun deployKubernetesPod(podNamespace: String, podDescription: String) {
        val (podName, namespace, status) = deployKubernetesPodInternal(podNamespace, podDescription)
        podCheckService.updatePodHealth(podName, namespace, status)
    }

    fun runPodHealthCheck() {
        val pods = fetchKubernetesPods()
        for(pod in pods) {
            logger.info { "Updating health check for ${pod.metadata.name} ${pod.metadata.namespace}, new status is ${pod.status}"}
            podCheckService.updatePodHealth(pod.metadata.name, pod.metadata.namespace, pod.status.phase)
        }
    }

    private fun deployKubernetesPodInternal(namespace: String, podDescription: String): Triple<String,String,String> {
        val pod = if (podDescription.isEmpty()) V1Pod() else V1Pod.fromJson(podDescription)

        return runK8S { api ->
            val result: V1Pod = api.createNamespacedPod(namespace, pod).pretty("true").execute()
            logger.info { result }
            Triple(result.metadata.name, result.metadata.namespace, result.status.phase)
        }
    }

    private fun fetchKubernetesPods(): MutableList<V1Pod> {
        return runK8S { api ->
            val list = api.listPodForAllNamespaces().pretty("true").execute()
            list.items
        }
    }

    private fun <T> runK8S (call: (CoreV1Api) -> T  ): T {
        try {
            return call.invoke(CoreV1Api())
        } catch (e: ApiException) {
            logger.error { "Exception when calling K8s API" }
            logger.error { "Status code: " + e.code }
            logger.error { "Reason: " + e.responseBody }
            logger.error { "Response headers: " + e.responseHeaders }
            e.printStackTrace()
            throw e
        }
    }
}
