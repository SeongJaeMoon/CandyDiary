package goods.cap.app.goodsgoods.API;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import goods.cap.app.goodsgoods.Helper.DietDtlHelper;
import goods.cap.app.goodsgoods.Helper.DietHelper;
import goods.cap.app.goodsgoods.Helper.HealthHelper;
import goods.cap.app.goodsgoods.Model.Diet.DietDtlResponseModel;
import goods.cap.app.goodsgoods.Model.Diet.DietResponseModel;
import goods.cap.app.goodsgoods.Model.Health.HealthResponseModel;
import retrofit2.Call;

/* API 컨트롤, created by supermoon. */

public class MainHttp {

    private static final String logger = MainHttp.class.getSimpleName();

    private Context context;
    private String API_KEY;
    private String dialogTitle;
    private String dialogMessage;
    private int pageNo;
    private int flag;
    private String cntntsNo;

    public MainHttp(Context context, String dialogTitle, String dialogMessage, String API_KEY) {
        this.context = context;
        this.API_KEY = API_KEY;
        this.dialogTitle = dialogTitle;
        this.dialogMessage = dialogMessage;
    }

    public void setPageNo(int pageNo){this.pageNo = pageNo;}
    public void setFlag(int flag){this.flag = flag;}
    public void setCntntsNo(String cntntsNo){this.cntntsNo = cntntsNo;}

    public void getDiet(final DietHelper dietCallback){
        final ApiData objApi = ApiData.getInstance();
        try {
            Call objCall = null;
            final ProgressDialog dialog;
            objCall = objApi.getApi(context).getDiet(this.API_KEY, this.flag, this.pageNo, 10);

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
            objCall = objApi.getApi(context).getDietDtl(this.API_KEY, this.cntntsNo);

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

    public void getHealth(final HealthHelper healthCallback){
        final ApiDataFood objApi = ApiDataFood.getInstance();
        try {
            Call objCall = null;
            final ProgressDialog dialog;
            objCall = objApi.getApi(context).getHealth(this.API_KEY, this.pageNo, 10);

            if (objCall != null) {
                dialog = new ProgressDialog(context);
                dialog.setTitle(dialogTitle);
                dialog.setMessage(dialogMessage);
                dialog.show();

                objCall.enqueue(new HttpCallback<HealthResponseModel>(context) {

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        dialog.dismiss();
                        healthCallback.failure("Failed");
                        Log.w(logger, t.getCause() + "," + t.getMessage());
                        super.onFailure(call, t);
                    }

                    @Override
                    protected void onRecipeResponse(Call call, retrofit2.Response response) {
                        dialog.dismiss();
                        Log.w(logger, "onDietResponse : " + response.toString());
                        if (!response.isSuccessful()) healthCallback.failure("Failed");
                    }

                    @Override
                    protected void onRecipeObject(Call call, HealthResponseModel response) {
                        dialog.dismiss();
                        Log.w(logger, "onDietObject : " + response.toString());
                        healthCallback.success(response);
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
