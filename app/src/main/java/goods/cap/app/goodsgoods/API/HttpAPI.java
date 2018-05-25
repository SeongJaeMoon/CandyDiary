package goods.cap.app.goodsgoods.API;

import com.google.gson.JsonObject;

import goods.cap.app.goodsgoods.Model.GroceryResponseModel;
import goods.cap.app.goodsgoods.Model.RecipeResponseModel;
import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface HttpAPI {

    /**
    * @param: 키, 시작 인덱스, 종료 인덱스, 타입(xml, json), 음식 타입
    *
    */
    @GET("Grid_20150827000000000226_1/1/5")
    Call<RecipeResponseModel> getRecipe();

    @GET("recipe")
    Call<RecipeResponseModel> getRecipeWithQuery(@Query("API_KEY") String apiKey,
                                        @Query("START_INDEX") int startIndex,
                                        @Query("END_INDEX") int endIndex,
                                        @Query("TYPE") String type,
                                        @Query("RECIPE_NM_KO") String recipeNmKo);
    /**
     * @param: 키, 시작 인덱스, 종료 인덱스, 타입(xml, json), 음식 타입, 재료명, 레시피 코드
     *
     */
    @GET("grocery")
    Call<GroceryResponseModel> getGrocery(@Query("API_KEY") String apiKey,
                                          @Query("START_INDEX") int startIndex,
                                          @Query("END_INDEX") int endIndex,
                                          @Query("TYPE")String type);

    @GET("grocery")
    Call<GroceryResponseModel> getGroceryWithQuery(@Query("API_KEY") String apiKey,
                                          @Query("START_INDEX") int startIndex,
                                          @Query("END_INDEX") int endIndex,
                                          @Query("TYPE")String type,
                                          @Query("IRDNT_NM") String irdnt_nm,
                                          @Query("RECIPE_ID")String recipe_id);

}
