package aleh.ahiyevich.retrofit.api.cases

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

// Интерфейс для отправки данных в БД, чтобы получить ServerResponse данные в ответ
interface CasesApi {

    @GET("cases/get-cases-by-season?season_id=2")
    fun getCases(@Header("Authorization") token: String): Call<Cases>

    @GET("cases/get-one?id=9")
    fun getCase(@Header("Authorization") token: String): Call<Cases>


}