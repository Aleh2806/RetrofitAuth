package aleh.ahiyevich.retrofit.api.cases

data class Case(
    val success: Boolean,
    val data: DataCase
)

data class DataCase(
    val id: Int,
    val name: String
)