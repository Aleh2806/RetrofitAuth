package aleh.ahiyevich.retrofit.api.cases

import retrofit2.Call
import retrofit2.http.*

// Интерфейс для отправки данных в БД, чтобы получить ServerResponse данные в ответ
interface CasesApi {
//    /get-cases-by-season?season_id=2
    @GET("cases/get-cases-by-season?")
    fun getCases(@Header("Authorization") token: String,@Query("season_id") seasonId: Int): Call<Cases>

    @GET("cases/get-one?id=9")
    fun getCase(@Header("Authorization") token: String): Call<Cases>


}