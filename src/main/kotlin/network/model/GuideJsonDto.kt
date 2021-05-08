package network.model

import com.google.gson.annotations.SerializedName

data class GuideJsonDto(
    @SerializedName(
        value = "data",
        alternate = [
            "bugreport",
            "troubleshooting",
            "faq",
            "features"
        ]
    ) val data: List<GuideSingleJsonDto>
)
