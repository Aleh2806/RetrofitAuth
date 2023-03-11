package aleh.ahiyevich.retrofit.api.cases

import retrofit2.http.GET
import retrofit2.http.Header

// Интерфейс для отправки данных в БД, чтобы получить ServerResponse данные в ответ
interface CasesApi {

    @GET("cases/get-cases-by-season?season_id={id}")
    suspend fun getCases(@Header ("Authorization") token: String, id: Int): List<Case>

    @GET("cases/get-one?id={id}")
    suspend fun getCase(@Header ("Authorization") token: String, id: Int): Case


}