package aleh.ahiyevich.retrofit.api.auth

// Классы для принятия данных при ответе сервера
data class ResponseToken(
    val success: Boolean,
    val data: DataTokens
)

data class DataTokens(
    val access_token: String,
    val refresh_token: String
)



data class AuthUser(
    val success: Boolean,
    val data: UserData
)

data class UserData(
    val user: DetailsUser
)

data class DetailsUser(
    val id: Int,
    val login: String,
    val email: String,
    val name: String,
)

data class Season(
    val success: Boolean,
    val data: List<SeasonData>
)

data class SeasonData(
    val id: Int,
    val name: String,
    val image: String,
    val created_at: String,
    val updated_at: String
)