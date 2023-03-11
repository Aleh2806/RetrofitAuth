package aleh.ahiyevich.retrofit.api.auth

// класс для запроса в БД
data class AuthRequest(
    val email: String,
    val password: String
)

data class RefreshTokenRequest(
    val refresh_token: String
)