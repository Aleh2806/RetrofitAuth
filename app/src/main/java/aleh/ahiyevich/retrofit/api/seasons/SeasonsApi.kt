package aleh.ahiyevich.retrofit.api.seasons

import aleh.ahiyevich.retrofit.api.auth.Season
import aleh.ahiyevich.retrofit.api.auth.SeasonData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

// Интерфейс для отправки данных в БД, чтобы получить ServerResponse данные в ответ
interface SeasonsApi {

    @GET("seasons/get-all")
    fun getSeasons(@Header ("Authorization") token: String): Call<Season>
}