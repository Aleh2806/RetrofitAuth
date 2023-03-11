package aleh.ahiyevich.retrofit

import aleh.ahiyevich.retrofit.api.auth.*
import aleh.ahiyevich.retrofit.databinding.ActivityMainBinding
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val ACCESS_TOKEN = "access token"
const val REFRESH_TOKEN = "refresh token"

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var sharedPref: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = getPreferences(MODE_PRIVATE)


        //TODO: первыцм делом проверяем, есть ли в памяти токены.
        // Еслит есть, отправляем проверку на валидность токена,
        //         если проверка токена пройдена, отправляем на страницу\фраггмент сезонов,
        //         если не прошла, пробуем отправить запрос на получение авторизации по рефрешь токену.
        //              Если пришла новая пара токенов, отправляет на страницу\фрагмент сезонов,
        //              если нет на страницу\фрагмент авторизации
        // Если нету, сразу отправляем чела на страницу\фрагмент авторизауии.
        // Пример условных операторов


//        //localAccessToken = токен из памети
//        //localRefreshToken = токен из памети
//
//        if(/*Токеты в памяти*/) {
//
//            //запрос на проверку валидности токена
//            //response = getAuthUser()
//
//            if(/*пришел объект пользователя*/){
//                //отправляем на страницу сезонов
//            } else {
//                // отправляем запрос на восстановление токена по refresh
//                //response = getAuthByRefreshToken()
//
//                if(/*пришли  новые токеты*/) {
//                    //отправляем на страницу сезонов
//                }else {
//                    //отправляем на страницу авторизации
//                }
//            }
//        }else {
//            // отправляем на страницу авторизации
//        }
//        login()

        var localAccessToken = sharedPref.getString(ACCESS_TOKEN, "")

        if (localAccessToken != null) {
            getAuthUser(localAccessToken)
        } else {
            // отправляем на страницу авторизации

        }


    }

    private fun login() {
        // TODO: Сделать получение данных из Едит текст
        val email = "admin@admin.com"
        val password = "Admin2022"

        val request = BaseRequest().retrofit.create(AuthApi::class.java)
        val call: Call<ResponseToken> = request.login(AuthRequest(email, password))
        call.enqueue(object : Callback<ResponseToken> {
            override fun onResponse(call: Call<ResponseToken>, response: Response<ResponseToken>) {
                if (response.isSuccessful) {
                    saveTokens(
                        response.body()!!.data.access_token,
                        response.body()!!.data.refresh_token
                    )
                    Toast.makeText(this@MainActivity, "Авторизация успешная", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(this@MainActivity, "Ошибка авторизации", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<ResponseToken>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Ошибка... ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }

        })


    }


    private fun getAuthUser(token: String) {

        var request = BaseRequest().retrofit.create(AuthApi::class.java)
        val call: Call<AuthUser> = request.getAuthUser("Bearer ${token}")
        call.enqueue(object : Callback<AuthUser> {
            override fun onResponse(call: Call<AuthUser>, response: Response<AuthUser>) {
                if (response.isSuccessful) {
                    // на страницу сезонов
                } else if (response.code() == 401) {
                    getAuthByRefreshToken()
                } else {
                    // вывод Ошибки
                }

            }

            override fun onFailure(call: Call<AuthUser>, t: Throwable) {
                Toast.makeText(this@MainActivity, t.message.toString(), Toast.LENGTH_SHORT).show()
            }

        })

    }

    private fun getAuthByRefreshToken() {
        val refreshToken = sharedPref.getString(REFRESH_TOKEN, "")
        if (refreshToken != null) {
            var request = BaseRequest().retrofit.create(AuthApi::class.java)
            val call: Call<ResponseToken> =
                request.getAuthByRefreshToken(RefreshTokenRequest(refreshToken))
            call.enqueue(object : Callback<ResponseToken> {
                override fun onResponse(
                    call: Call<ResponseToken>,
                    response: Response<ResponseToken>
                ) {
                    if (response.code() == 401) {
                        // На страницу авторизации
                    } else {
                        // Обновить токены!!! и access  и  refresh и сохранить в локальное хранилище(мб SP)
                        // НА страницу сезонов
                    }
                }

                override fun onFailure(call: Call<ResponseToken>, t: Throwable) {
                    Toast.makeText(this@MainActivity, t.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                }

            })
        } else {
            // На страницу Авторизации
        }
    }


    // Добавляю Вьюхи только для того, чтобы отобразить на экране(если необходимо)
    private fun saveTokens(accessToken: String, refreshToken: String) {
        sharedPref = getPreferences(MODE_PRIVATE)
        val mEditor: SharedPreferences.Editor = sharedPref.edit()
        mEditor.putString(ACCESS_TOKEN, accessToken)
        mEditor.putString(REFRESH_TOKEN, refreshToken)
        mEditor.apply()
        Toast.makeText(this, "Данные сохранены", Toast.LENGTH_SHORT).show()
    }
}

