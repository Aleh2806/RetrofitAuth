package aleh.ahiyevich.retrofit.api.auth

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

// Интерфейс для отправки данных в БД, чтобы получить ServerResponse данные в ответ
interface AuthApi {

    // Отправляю запрос для получения токена по email и password
    @POST("auth/login")
    fun login(@Body authRequest: AuthRequest): Call<ResponseToken>


    @GET("user/get-auth-user")
    fun getAuthUser(@Header("Authorization") token: String): Call<AuthUser>


    @POST("auth/get-auth-user-by-refresh-token")
    fun getAuthByRefreshToken(
        @Body authRefresh: RefreshTokenRequest
    ): Call<ResponseToken>


}