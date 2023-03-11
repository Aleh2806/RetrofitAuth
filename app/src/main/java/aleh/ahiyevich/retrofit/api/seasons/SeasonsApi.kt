package aleh.ahiyevich.retrofit.api.seasons

import aleh.ahiyevich.retrofit.api.auth.Season
import retrofit2.http.GET
import retrofit2.http.Header

// Интерфейс для отправки данных в БД, чтобы получить ServerResponse данные в ответ
interface SeasonsApi {

    @GET("seasons/get-all")
    suspend fun getSeasons(@Header ("Authorization") token: String): List<Season>
}