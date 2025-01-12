package org.example.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class University(
    @SerialName("state-province")
    val stateProvince: String?,
    @SerialName("alpha_two_code")
    val alphaTwoCode: String?,
    @SerialName("name")
    val name: String?,
    @SerialName("domains")
    val domains: List<String?>,
    @SerialName("web_pages")
    val webPages: List<String?>,
    @SerialName("country")
    val country: String?,
)