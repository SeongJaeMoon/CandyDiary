package goods.cap.app.goodsgoods.API;

import goods.cap.app.goodsgoods.Model.Diet.DietDtlResponseModel;
import goods.cap.app.goodsgoods.Model.Diet.DietResponseModel;
import goods.cap.app.goodsgoods.Model.Health.HealthResponseModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Body;

public interface HttpAPI {

    /**
     * @param: 키, 식단 구분코드, 조회할 페이지 번호, 한 페이지에 제공할 건수
     *
     */
    @GET("recomendDietList")
    Call<DietResponseModel>getDiet(@Query("apiKey")String apiKey,
                                   @Query("dietSeCode")Integer dietSeCode,
                                   @Query("pageNo")Integer pageNo,
                                   @Query("numOfRows")Integer numOfRows);

    @GET("recomedDietList")
    Call<DietDtlResponseModel>getDietWithQuery(@Query("apiKey")String apiKey,
                                               @Query("dietSeCode")Integer dietSeCode,
                                               @Query("pageNo")Integer pageNo,
                                               @Query("numOfRows")Integer numOfRows);
    /**
     * @param: 키, 컨텐츠 번호
     *
     */
    @GET("recomendDietDtl")
    Call<DietDtlResponseModel>getDietDtl(@Query("apiKey") String apiKey,
                                         @Query("cntntsNo") String cntntsNo);

    /**
     * @param: 키, 조회할 페이지 번호, 한 페이지에 제공할 건수, 제품명(prdlst_nm), 업체명(bssh_nm)
     *
     */
    //URLEncoder.encode(, "utf-8");
    @GET("getHtfsInfoList")
    Call<HealthResponseModel>getHealth(@Query(value = "ServiceKey", encoded = true)String serviceKey,
                                     @Query("pageNo")Integer pageNo,
                                     @Query("numOfRows")Integer numOfRows);

    @GET("getHtfsInfoList")
    Call<HealthResponseModel>getHealthWithQuery(@Query(value = "ServiceKey", encoded = true)String serviceKey,
                                   @Query("pageNo")Integer pageNo,
                                   @Query("numOfRows")Integer numOfRows,
                                   @Query("prdlst_nm")String prdlst_nm);
}
