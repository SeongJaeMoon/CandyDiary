package goods.cap.app.goodsgoods.API;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.ProgressBar;

import goods.cap.app.goodsgoods.Helper.GroceryHelper;
import goods.cap.app.goodsgoods.Model.GroceryResponseModel;
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

    public void getGrocery(final GroceryHelper groceryCallback) {
        final ApiClient objApi = ApiClient.getInstance();
        try {
            Call objCall = null;

            objCall = objApi.getApi(context).getGrocery(API_KEY, 1, 5, "json");

            if (objCall != null) {
                objCall.enqueue(new HttpCallback<GroceryResponseModel>(context) {

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        groceryCallback.failure("Failed");
                        super.onFailure(call, t);
                    }

                    @Override
                    protected void onRecipeResponse(Call call, retrofit2.Response response) {
                        if (!response.isSuccessful()) groceryCallback.failure("Failed");
                    }

                    @Override
                    protected void onRecipeObject(Call call, GroceryResponseModel response) {
                        groceryCallback.success(response);
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
}
