package goods.cap.app.goodsgoods.API;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import goods.cap.app.goodsgoods.Helper.DietDtlHelper;
import goods.cap.app.goodsgoods.Helper.DietHelper;
import goods.cap.app.goodsgoods.Helper.FoodHelper;
import goods.cap.app.goodsgoods.Helper.GroceryHelper;
import goods.cap.app.goodsgoods.Model.Diet;
import goods.cap.app.goodsgoods.Model.DietDtlResponseModel;
import goods.cap.app.goodsgoods.Model.DietResponseModel;
import goods.cap.app.goodsgoods.Model.FoodResponseModel;
import goods.cap.app.goodsgoods.Model.GroceryResponseModel;
import goods.cap.app.goodsgoods.Model.Recipe;
import goods.cap.app.goodsgoods.Model.RecipeResponseModel;
import goods.cap.app.goodsgoods.Helper.RecipeHelper;
import goods.cap.app.goodsgoods.R;
import retrofit2.Call;

/* API 컨트롤, created by supermoon. */

public class MainHttp {

    private static final String logger = MainHttp.class.getSimpleName();

    private Context context;
    private String API_KEY;
    private String dialogTitle;
    private String dialogMessage;
    private String query;
    private int startIndex;
    private int endIndex;
    private int recipeId;
    private int pageNo;
    private int numOfRows;
    private String cntntsNo;

    public MainHttp(Context context, String dialogTitle, String dialogMessage, String API_KEY) {
        this.context = context;
        this.API_KEY = API_KEY;
        this.dialogTitle = dialogTitle;
        this.dialogMessage = dialogMessage;
    }

    public void setStartIndex(int startIndex){ this.startIndex = startIndex; }
    public void setEndIndex(int endIndex){ this.endIndex = endIndex; }
    public void setQuery(String query) {
        this.query = query;
    }
    public void setRecipeId(int recipeId){this.recipeId = recipeId;}
    public void setPageNo(int pageNo){this.pageNo = pageNo;}
    public void setNumOfRows(int numOfRows){this.numOfRows = numOfRows;}
    public void setCntntsNo(String cntntsNo){this.cntntsNo = cntntsNo;}

    public void getRecipe(final RecipeHelper recipeCallback) {
        final ApiClient objApi = ApiClient.getInstance();
        try {
            Call objCall = null;
            final ProgressDialog dialog;
            objCall = objApi.getApi(context).getRecipe(API_KEY, startIndex, endIndex);

            if (objCall != null) {
                dialog = new ProgressDialog(context);
                dialog.setTitle(dialogTitle);
                dialog.setMessage(dialogMessage);
                dialog.show();
                objCall.enqueue(new HttpCallback<RecipeResponseModel>(context) {
                    @Override
                    public void onFailure(Call call, Throwable t) {
                        dialog.dismiss();
                        recipeCallback.failure("Failed");
                        super.onFailure(call, t);
                    }

                    @Override
                    protected void onRecipeResponse(Call call, retrofit2.Response response) {
                        dialog.dismiss();
                        Log.w(logger, "onRecipeResponse : " + response.toString());
                        if (!response.isSuccessful()) recipeCallback.failure("Failed");
                    }

                    @Override
                    protected void onRecipeObject(Call call, RecipeResponseModel response) {
                        dialog.dismiss();
                        Log.w(logger, "onRecipeObject : " + response.toString());
                        recipeCallback.success(response);
                    }

                    @Override
                    protected void common(){

                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void getRecipeWithQuery(final RecipeHelper recipeCallback){
        final ApiClient objApi = ApiClient.getInstance();
        try {
            Call objCall = null;
            final ProgressDialog dialog;
            objCall = objApi.getApi(context).getRecipeWithQuery(API_KEY, 1, 10, query);

            if (objCall != null) {
                dialog = new ProgressDialog(context);
                dialog.setTitle(dialogTitle);
                dialog.setMessage(dialogMessage);
                dialog.show();
                objCall.enqueue(new HttpCallback<RecipeResponseModel>(context) {

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        dialog.dismiss();
                        recipeCallback.failure("Failed");
                        super.onFailure(call, t);
                    }

                    @Override
                    protected void onRecipeResponse(Call call, retrofit2.Response response) {
                        dialog.dismiss();
                        Log.w(logger, "onRecipeResponse : " + response.toString());
                        if (!response.isSuccessful()) recipeCallback.failure("Failed");
                    }

                    @Override
                    protected void onRecipeObject(Call call, RecipeResponseModel response) {
                        dialog.dismiss();
                        Log.w(logger, "onRecipeObject : " + response.toString());
                        recipeCallback.success(response);
                    }

                    @Override
                    protected void common(){

                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void getFood(final FoodHelper foodCallback){
        final ApiClient objApi = ApiClient.getInstance();
        try {
            Call objCall = null;
            final ProgressDialog dialog;
            objCall = objApi.getApi(context).getFood(API_KEY, startIndex, endIndex, recipeId);

            if (objCall != null) {
                dialog = new ProgressDialog(context);
                dialog.setTitle(dialogTitle);
                dialog.setMessage(dialogMessage);
                dialog.show();

                objCall.enqueue(new HttpCallback<FoodResponseModel>(context) {

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        dialog.dismiss();
                        foodCallback.failure("Failed");
                        super.onFailure(call, t);
                    }

                    @Override
                    protected void onRecipeResponse(Call call, retrofit2.Response response) {
                        dialog.dismiss();
                        Log.w(logger, "onFoodResponse : " + response.toString());
                        if (!response.isSuccessful()) foodCallback.failure("Failed");
                    }

                    @Override
                    protected void onRecipeObject(Call call, FoodResponseModel response) {
                        dialog.dismiss();
                        Log.w(logger, "onFoodObject : " + response.toString());
                        foodCallback.success(response);
                    }

                    @Override
                    protected void common(){

                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getDiet(final DietHelper dietCallback){
        final ApiData objApi = ApiData.getInstance();
        try {
            Call objCall = null;
            final ProgressDialog dialog;
            objCall = objApi.getApi(context).getDiet(API_KEY, Config.dietCode1, this.pageNo, 10);

            if (objCall != null) {
                dialog = new ProgressDialog(context);
                dialog.setTitle(dialogTitle);
                dialog.setMessage(dialogMessage);
                dialog.show();

                objCall.enqueue(new HttpCallback<DietResponseModel>(context) {

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        dialog.dismiss();
                        dietCallback.failure("Failed");
                        Log.w(logger, t.getCause() + "," + t.getMessage());
                        super.onFailure(call, t);
                    }

                    @Override
                    protected void onRecipeResponse(Call call, retrofit2.Response response) {
                        dialog.dismiss();
                        Log.w(logger, "onDietResponse : " + response.toString());
                        if (!response.isSuccessful()) dietCallback.failure("Failed");
                    }

                    @Override
                    protected void onRecipeObject(Call call, DietResponseModel response) {
                        dialog.dismiss();
                        Log.w(logger, "onDietObject : " + response.toString());
                        dietCallback.success(response);
                    }

                    @Override
                    protected void common(){

                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void getDietDtl(final DietDtlHelper dietCallback){
        final ApiData objApi = ApiData.getInstance();
        try {
            Call objCall = null;
            final ProgressDialog dialog;
            objCall = objApi.getApi(context).getDietDtl(API_KEY, cntntsNo);

            if (objCall != null) {
                dialog = new ProgressDialog(context);
                dialog.setTitle(dialogTitle);
                dialog.setMessage(dialogMessage);
                dialog.show();

                objCall.enqueue(new HttpCallback<DietDtlResponseModel>(context) {

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        dialog.dismiss();
                        dietCallback.failure("Failed");
                        Log.w(logger, t.getCause() + "," + t.getMessage());
                        super.onFailure(call, t);
                    }

                    @Override
                    protected void onRecipeResponse(Call call, retrofit2.Response response) {
                        dialog.dismiss();
                        Log.w(logger, "onDietResponse : " + response.toString());
                        if (!response.isSuccessful()) dietCallback.failure("Failed");
                    }

                    @Override
                    protected void onRecipeObject(Call call, DietDtlResponseModel response) {
                        dialog.dismiss();
                        Log.w(logger, "onDietObject : " + response.toString());
                        dietCallback.success(response);
                    }

                    @Override
                    protected void common(){

                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
