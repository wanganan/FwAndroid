package com.oplus.fwandroid.common.net

import com.oplus.fwandroid.common.bean.GoodsList
import io.reactivex.rxjava3.core.Flowable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*


/**
 * @author Sinaan
 * @date 2020/7/3
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：Url统一存放
 * version: 1.0
 */
interface API {

    /*****************************************     The Official Sample     *****************************************/

    /**
     * A request URL can be updated dynamically using replacement blocks and parameters on the method. A replacement block is
     * an alphanumeric string surrounded by { and }. A corresponding parameter must be annotated with @Path using the same string.
     */
//    @GET("group/{id}/users")
//    fun groupList(@Path("id") groupId: Int): Call<List<User?>?>?

    /**
     * Query parameters can also be added.
     */
//    @GET("group/{id}/users")
//    open fun groupList(
//        @Path("id") groupId: Int,
//        @Query("sort") sort: String?
//    ): Call<MutableList<User?>?>?

    /**
     * For complex query parameter combinations a Map can be used.
     */
//    @GET("group/{id}/users")
//    open fun groupList(
//        @Path("id") groupId: Int,
//        @QueryMap options: Map<String?, String?>?
//    ): Call<MutableList<User?>?>?

    /**
     * An object can be specified for use as an HTTP request body with the @Body annotation.
     * var map: HashMap<String?, Any?>? = HashMap()
     * map.put("str", "content")
     * map.put("list", lists)
     * var strEntity: String? = Gson().toJson(map)
     * var requestBody =
     * RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), strEntity)
     */
//    @POST("users/new")
//    open fun createUser(@Body user: User?): Call<User?>?

    /**
     * Methods can also be declared to send form-encoded and multipart data.
     * Form-encoded data is sent when @FormUrlEncoded is present on the method.
     * Each key-value pair is annotated with @Field containing the name and the object providing the value.
     */
//    @FormUrlEncoded
//    @POST("user/edit")
//    open fun updateUser(
//        @Field("first_name") first: String?,
//        @Field("last_name") last: String?
//    ): Call<User?>?

    /**
     * For complex field parameter combinations a Map can be used.
     */
//    @POST("register/users")
//    @FormUrlEncoded
//    open fun createUser(@FieldMap map: Map<String?, String?>?): Call<User?>?

    /**
     * Multipart requests are used when @Multipart is present on the method. Parts are declared using the @Part annotation.
     */
//    @Multipart
//    @PUT("user/photo")
//    open fun updateUser(
//        @Part("photo") photo: RequestBody?,
//        @Part("description") description: RequestBody?
//    ): Call<User?>?

    /**
     * You can set static headers for a method using the @Headers annotation.
     */
//    @Headers("Cache-Control: max-age=640000")
//    @GET("widget/list")
//    open fun widgetList(): Call<MutableList<Widget?>?>?

//    @Headers(
//        "Accept: application/vnd.github.v3.full+json",
//        "User-Agent: Retrofit-Sample-App"
//    )
//    @GET("users/{username}")
//    open fun getUser(@Path("username") username: String?): Call<User?>?

    /**
     * A request Header can be updated dynamically using the @Header annotation.
     * A corresponding parameter must be provided to the @Header.
     * If the value is null, the header will be omitted. Otherwise, toString will be called on the value, and the result used.
     */
//    @GET("user")
//    Call<User> getUser(@Header("Authorization") String authorization)

    /**
     * Similar to query parameters, for complex header combinations, a Map can be used.
     * Headers that need to be added to every request can be specified using an OkHttp interceptor.
     * https://square.github.io/okhttp/interceptors/
     */
//    @GET("user")
//    Call<User> getUser(@HeaderMap Map<String, String> headers)

    /**
     * method 表示请求的方法，区分大小写
     * path表示路径
     * hasBody表示是否有请求体
     */
//    @HTTP(method = "GET", path = "blog/{id}", hasBody = false)
//    open fun getBlog(@Path("id") id: Int): Call<ResponseBody?>?

    /**
     * You can pass in the Url directly using the @Url annotation.
     */
//    @GET
//    open fun getUser(@Url url: String?): Call<User?>?


    /*****************************************     The Referable Sample     *****************************************/

    /**
     * TODO Get请求
     */
    //第一种方式：GET不带参数
    @GET("retrofit.txt")
    fun getUser(): Flowable<BaseResponse<Any?>?>?

    @GET
    fun getUser(@Url url: String?): Flowable<Any?>?

    @GET
    fun getUser1(@Url url: String?): Flowable<Any?>? //简洁方式   直接获取所需数据

    //第二种方式：GET带参数
    @GET("api/data/{type}/{count}/{page}")
    fun getUser(
        @Path("type") type: String?,
        @Path("count") count: Int,
        @Path("page") page: Int
    ): Flowable<Any?>?

    //第三种方式：GET带请求参数：https://api.github.com/users/whatever?client_id=xxxx&client_secret=yyyy
    @GET("users/whatever")
    fun getUser(
        @Query("client_id") id: String?,
        @Query("client_secret") secret: String?
    ): Flowable<Any?>?

    @GET("users/whatever")
    fun getUser(@QueryMap info: Map<String?, String?>?): Flowable<Any?>?

    /**
     * TODO POST请求
     */
    //第一种方式：@Body
    @Headers("Accept:application/json")
    @POST("login")
    fun postUser(@Body body: RequestBody?): Flowable<Any?>?
    //第二种方式：@Field

    //第二种方式：@Field
    @Headers("Accept:application/json")
    @POST("auth/login")
    @FormUrlEncoded
    fun postUser(
        @Field("username") username: String?,
        @Field("password") password: String?
    ): Flowable<Any?>?

    //多个参数
    fun postUser(@FieldMap map: Map<String?, String?>?): Flowable<Any?>?

    /**
     * TODO DELETE
     */
    @DELETE("member_follow_member/{id}")
    fun delete(
        @Header("Authorization") auth: String?,
        @Path("id") id: Int
    ): Flowable<Any?>?

    /**
     * TODO PUT
     */
    @PUT("member")
    fun put(
        @HeaderMap headers: Map<String?, String?>?,
        @Query("nickname") nickname: String?
    ): Flowable<Any?>?

    /**
     * TODO 文件上传
     */
    @Multipart
    @POST("upload")
    fun upload(
        @Part("description") description: RequestBody?,
        @Part file: MultipartBody.Part?
    ): Flowable<ResponseBody?>?

    //亲测可用
    @Multipart
    @POST("member/avatar")
    fun uploadImage(
        @HeaderMap headers: Map<String?, String?>?,
        @Part file: MultipartBody.Part?
    ): Flowable<Any?>?

    /**
     * 多文件上传
     */
    @Multipart
    @POST("register")
    fun upload(
        @PartMap params: Map<String?, RequestBody?>?,
        @Part("description") description: RequestBody?
    ): Flowable<ResponseBody?>?
    //Flowable<ResponseBody> upload(@Part() List<MultipartBody.Part> parts);

    //Flowable<ResponseBody> upload(@Part() List<MultipartBody.Part> parts);
    @Multipart
    @POST("member/avatar")
    fun uploadImage1(
        @HeaderMap headers: Map<String?, String?>?,
        @Part file: List<MultipartBody.Part?>?
    ): Flowable<Any?>?

    /**
     * 来自https://blog.csdn.net/impure/article/details/79658098
     * @Streaming 这个注解必须添加，否则文件全部写入内存，文件过大会造成内存溢出
     */
    @Streaming
    @GET
    fun download(
        @Header("RANGE") start: String?,
        @Url url: String?
    ): Flowable<ResponseBody?>?

    /*****************************************     Project Related     *****************************************/

    /**
     * 商品列表
     * @Headers cache：值为缓存天数，0表示不需要缓存（及时更新的接口）。不传表示支持默认缓存天数（RetrofitHelper），且只缓存Get请求。
     */
    @Headers("cache:0")
    @GET("appapi/taobao_api/getTBKMaterialGoodsList")
    fun getGoodsList(@QueryMap map: Map<String?, String?>?): Flowable<BaseResponse<ArrayList<GoodsList>>>
}