package aleh.ahiyevich.retrofit.getContent

import retrofit2.Call
import retrofit2.http.GET

interface JsonPlaceholder {

    @GET("posts")
    fun getPost(): Call<List<Post>>

}