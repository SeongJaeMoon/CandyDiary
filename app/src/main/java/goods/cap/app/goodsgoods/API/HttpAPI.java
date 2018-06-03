package goods.cap.app.goodsgoods.API;

import goods.cap.app.goodsgoods.Model.Diet.DietDtlResponseModel;
import goods.cap.app.goodsgoods.Model.Diet.DietResponseModel;
import goods.cap.app.goodsgoods.Model.Food.FoodResponseModel;
import goods.cap.app.goodsgoods.Model.Recipe.RecipeResponseModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface HttpAPI {

    /**
    * @param: 키, 시작 인덱스, 종료 인덱스, 타입(xml, json), 음식 타입
    *
    */
    @GET("{API_KEY}/json/Grid_20150827000000000226_1/{START_INDEX}/{END_INDEX}")
    Call<RecipeResponseModel> getRecipe(@Path("API_KEY") String apiKey,
                                        @Path("START_INDEX") Integer startIndex,
                                        @Path("END_INDEX") Integer endIndex);


    @GET("{API_KEY}/json/Grid_20150827000000000226_1/{START_INDEX}/{END_INDEX}")
    Call<RecipeResponseModel> getRecipeWithQuery(@Path("API_KEY") String apiKey,
                                                 @Path("START_INDEX") Integer startIndex,
                                                 @Path("END_INDEX") Integer endIndex,
                                                 @Query("RECIPE_NM_KO") String recipeNmKo);

    /**
     * @param: 키, 시작 인덱스, 종료 인덱스, 타입(xml, json), 레시피 코드
     *
     */
    @GET("{API_KEY}/json/Grid_20150827000000000228_1/{START_INDEX}/{END_INDEX}")
    Call<FoodResponseModel>getFood(@Path("API_KEY") String apiKey,
                                   @Path("START_INDEX") Integer startIndex,
                                   @Path("END_INDEX") Integer endIndex,
                                   @Query("RECIPE_ID") Integer recipeID);


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
    @GET("getHtfsInfoList")
    Call<FoodResponseModel>getFood(@Query("ServiceKey")String serviceKey,
                                   @Query("pageNo")Integer pageNo,
                                   @Query("numOfRows")Integer numOfRows);

    @GET("getHtfsInfoList")
    Call<FoodResponseModel>getFoodWithQuery(@Query("ServiceKey")String serviceKey,
                                   @Query("pageNo")Integer pageNo,
                                   @Query("numOfRows")Integer numOfRows,
                                   @Query("prdlst_nm")String prdlst_nm);
}
