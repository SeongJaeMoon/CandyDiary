package goods.cap.app.goodsgoods.API;

import goods.cap.app.goodsgoods.Model.Diet.DietDtlResponseModel;
import goods.cap.app.goodsgoods.Model.Diet.DietResponseModel;
import goods.cap.app.goodsgoods.Model.Therapy.TherapyDtlResponseModel;
import goods.cap.app.goodsgoods.Model.Therapy.TherapyResponseModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface HttpAPI {

    /**
     * @param: 키, 식단 구분코드, 조회할 페이지 번호, 한 페이지에 제공할 건수
     *
     */
    @GET("recomendDiet/recomendDietList")
    Call<DietResponseModel>getDiet(@Query("apiKey")String apiKey,
                                   @Query("dietSeCode")Integer dietSeCode,
                                   @Query("pageNo")Integer pageNo,
                                   @Query("numOfRows")Integer numOfRows);
    /**
     * @param: 키, 컨텐츠 번호
     *
     */
    @GET("recomendDiet/recomendDietDtl")
    Call<DietDtlResponseModel>getDietDtl(@Query("apiKey") String apiKey,
                                         @Query("cntntsNo") String cntntsNo);

    /**
     * @Param: 키, 조회할 페이지 번호, 한페이지 제공할 건수, (검색어), (검색 구분)
     * 약초 정보 요청 URL
     */
    @GET("prvateTherpy/prvateTherpyList")
    Call<TherapyResponseModel>getTherapy(@Query("apiKey")String apiKey,
                                         @Query("pageNo")Integer pageNo,
                                         @Query("numOfRows")Integer numOfRows);
    @GET("prvateTherpy/prvateTherpyDtl")
    Call<TherapyDtlResponseModel>getTherapyDtl(@Query("apiKey")String apiKey,
                                               @Query("cntntsNo")String cntntsNo);
}
