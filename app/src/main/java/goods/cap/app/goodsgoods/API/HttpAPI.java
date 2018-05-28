package goods.cap.app.goodsgoods.API;

import goods.cap.app.goodsgoods.Model.FoodResponseModel;
import goods.cap.app.goodsgoods.Model.GroceryResponseModel;
import goods.cap.app.goodsgoods.Model.RecipeResponseModel;
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
     * @param: 키, 시작 인덱스, 종료 인덱스, 타입(xml, json), 음식 타입, 재료명, 레시피 코드
     *
     */
    @GET("openapi")
    Call<GroceryResponseModel> getGrocery(@Query("API_KEY") String apiKey,
                                          @Query("START_INDEX") int startIndex,
                                          @Query("END_INDEX") int endIndex,
                                          @Query("TYPE")String type);

    @GET("openapi")
    Call<GroceryResponseModel> getGroceryWithQuery(@Query("API_KEY") String apiKey,
                                          @Query("START_INDEX") int startIndex,
                                          @Query("END_INDEX") int endIndex,
                                          @Query("TYPE")String type,
                                          @Query("IRDNT_NM") String irdnt_nm,
                                          @Query("RECIPE_ID")String recipe_id);
}
