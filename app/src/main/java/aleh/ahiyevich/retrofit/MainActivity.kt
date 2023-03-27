package aleh.ahiyevich.retrofit

import aleh.ahiyevich.retrofit.api.auth.*
import aleh.ahiyevich.retrofit.api.cases.Cases
import aleh.ahiyevich.retrofit.api.cases.CasesApi
import aleh.ahiyevich.retrofit.api.cases.DataCase
import aleh.ahiyevich.retrofit.api.seasons.SeasonsApi
import aleh.ahiyevich.retrofit.databinding.ActivityMainBinding
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Query

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


        val localAccessToken = sharedPref.getString(ACCESS_TOKEN, "")
        if (localAccessToken != null) {
            //запрос на проверку валидности токена
            getAuthUser(localAccessToken)
            Toast.makeText(this, "Authorization Success go to Seasons", Toast.LENGTH_SHORT).show()
        } else {
            // отправляем на страницу авторизации
            Toast.makeText(
                this,
                "Go to Authorization page and use (fun login())",
                Toast.LENGTH_SHORT
            ).show()
        }


    }

    private fun login() {
        // TODO: Сделать получение данных из Едит текст
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()

        val request = BaseRequest().retrofit.create(AuthApi::class.java)
        val call: Call<ResponseToken> = request.login(AuthRequest(email, password))
        call.enqueue(object : Callback<ResponseToken> {
            override fun onResponse(call: Call<ResponseToken>, response: Response<ResponseToken>) {
                if (response.isSuccessful) {
                    saveTokens(
                        response.body()!!.data.access_token,
                        response.body()!!.data.refresh_token
                    )
                    Toast.makeText(
                        this@MainActivity,
                        "Авторизация успешная\n\nAccess token = ${response.body()!!.data.access_token}\n\n" +
                                "Refresh token = ${response.body()!!.data.refresh_token}",
                        Toast.LENGTH_SHORT
                    )
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
        var authUser: AuthUser? = null

        val request = BaseRequest().retrofit.create(AuthApi::class.java)
        val call: Call<AuthUser> = request.getAuthUser("Bearer $token")
        call.enqueue(object : Callback<AuthUser> {
            override fun onResponse(call: Call<AuthUser>, response: Response<AuthUser>) {
                authUser = response.body()
                if (response.isSuccessful) {
                    // на страницу сезонов
//                    getSeasons(authUser, token)
                    getCases(token,authUser)

                } else if (response.code() == 401) {
                    Toast.makeText(this@MainActivity, "code 401", Toast.LENGTH_SHORT).show()
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


    fun getSeasons(authUser: AuthUser?, token: String) {
        //TODO: getAuthUser(token). Вернуть какой то ответ из метода get auth.
        // Например если true,  делаем запрос на сезоны,
        // если false - на авторизацию.


        if (authUser?.success == true) {
            val request = BaseRequest().retrofit.create(SeasonsApi::class.java)
            val call = request.getSeasons("Bearer $token")
            call.enqueue(object : Callback<Season> {
                override fun onResponse(call: Call<Season>, response: Response<Season>) {
                    val data = response.body()!!.data
                    binding.refreshToken.text = data[0].id.toString()
                    Toast.makeText(this@MainActivity, "Seasons gets", Toast.LENGTH_SHORT).show()

                }

                override fun onFailure(call: Call<Season>, t: Throwable) {
                }
            })
        } else {
            // На страницу авторизации
            Toast.makeText(this, "go to auth", Toast.LENGTH_SHORT).show()
        }
    }


    private fun getCases(token: String, authUser: AuthUser?) {
        //TODO: Сделать динамически подставляемые id Сезонов

        if (authUser?.success == true) {
            val request = BaseRequest().retrofit.create(CasesApi::class.java)
            val call = request.getCases("Bearer $token",2)
            call.enqueue(object : Callback<Cases> {
                override fun onResponse(call: Call<Cases>, response: Response<Cases>) {
                    val data = response.body()!!.data

                    val d = ""


                    Toast.makeText(this@MainActivity, "Cases gets", Toast.LENGTH_SHORT).show()

                }

                override fun onFailure(call: Call<Cases>, t: Throwable) {
                }
            })
        } else {
            // На страницу авторизации
            Toast.makeText(this, "go to auth", Toast.LENGTH_SHORT).show()
        }
    }


//    private fun getMaterialsCase(authUser: AuthUser?, token: String) {
//
//        if (authUser?.success == true) {
//            val request = BaseRequest().retrofit.create(CasesApi::class.java)
//            val call = request.getCase("Bearer $token")
//            call.enqueue(object : Callback<Cases> {
//                override fun onResponse(call: Call<Cases>, response: Response<Cases>) {
//                    val data = response.body()!!
//
//                    Toast.makeText(this@MainActivity, "Materials gets", Toast.LENGTH_SHORT).show()
//
//                }
//
//                override fun onFailure(call: Call<Cases>, t: Throwable) {
//                }
//            })
//        } else {
//            // На страницу авторизации
//            Toast.makeText(this, "go to auth", Toast.LENGTH_SHORT).show()
//        }
//
//    }


    private fun getAuthByRefreshToken() {
        val refreshToken = sharedPref.getString(REFRESH_TOKEN, "")
        if (refreshToken != null) {
            val request = BaseRequest().retrofit.create(AuthApi::class.java)
            val call: Call<ResponseToken> =
                request.getAuthByRefreshToken(RefreshTokenRequest(refreshToken))
            call.enqueue(object : Callback<ResponseToken> {
                override fun onResponse(
                    call: Call<ResponseToken>,
                    response: Response<ResponseToken>
                ) {
                    if (response.code() == 401) {
                        // На страницу авторизации + fun login()
                    } else {
                        saveTokens(
                            response.body()!!.data.access_token,
                            response.body()!!.data.refresh_token
                        )
                        Toast.makeText(
                            this@MainActivity,
                            "Tokens refresh, go to seasons",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Обновить токены!!! и access  и  refresh и сохранить в
                        // локальное хранилище(мб SP)
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


    private fun saveTokens(accessToken: String, refreshToken: String) {
        sharedPref = getPreferences(MODE_PRIVATE)
        val mEditor: SharedPreferences.Editor = sharedPref.edit()
        mEditor.putString(ACCESS_TOKEN, accessToken)
        mEditor.putString(REFRESH_TOKEN, refreshToken)
        mEditor.apply()
    }

}

