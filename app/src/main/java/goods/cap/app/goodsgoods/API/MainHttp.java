package goods.cap.app.goodsgoods.API;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import goods.cap.app.goodsgoods.Helper.DietDtlHelper;
import goods.cap.app.goodsgoods.Helper.DietHelper;
import goods.cap.app.goodsgoods.Helper.TherapyDtlHelper;
import goods.cap.app.goodsgoods.Helper.TherapyHelper;
import goods.cap.app.goodsgoods.Model.Diet.DietDtlResponseModel;
import goods.cap.app.goodsgoods.Model.Diet.DietResponseModel;
import goods.cap.app.goodsgoods.Model.Therapy.TherapyDtlResponseModel;
import goods.cap.app.goodsgoods.Model.Therapy.TherapyResponseModel;
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
    public void getTherapy(final TherapyHelper therapyCallback){
        final ApiData objApi = ApiData.getInstance();
        try {
            Call objCall = null;
            objCall = objApi.getApi(context).getTherapy(this.API_KEY, this.pageNo, 10);
            if (objCall != null) {
                final ProgressDialog dialog = new ProgressDialog(context);
                dialog.setTitle(dialogTitle);
                dialog.setMessage(dialogMessage);
                dialog.show();
                objCall.enqueue(new HttpCallback<TherapyResponseModel>(context) {

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        dialog.dismiss();
                        therapyCallback.failure("Failed");
                        Log.w(logger, t.getCause() + "," + t.getMessage());
                        super.onFailure(call, t);
                    }

                    @Override
                    protected void onRecipeResponse(Call call, retrofit2.Response response) {
                        dialog.dismiss();
                        Log.w(logger, "onTherapyResponse : " + response.toString());
                        if (!response.isSuccessful()) therapyCallback.failure("Failed");
                    }

                    @Override
                    protected void onRecipeObject(Call call, TherapyResponseModel response) {
                        dialog.dismiss();
                        Log.w(logger, "onTherapyObject : " + response.toString());
                        therapyCallback.success(response);
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
    public void getTherapyDtl(final TherapyDtlHelper therapyDtlCallback){
        final ApiData objApi = ApiData.getInstance();
        try {
            Call objCall = null;
            objCall = objApi.getApi(context).getTherapyDtl(this.API_KEY, this.cntntsNo);
            if (objCall != null) {
                final ProgressDialog dialog = new ProgressDialog(context);
                dialog.setTitle(dialogTitle);
                dialog.setMessage(dialogMessage);
                dialog.show();

                objCall.enqueue(new HttpCallback<TherapyDtlResponseModel>(context) {

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        dialog.dismiss();
                        therapyDtlCallback.failure("Failed");
                        Log.w(logger, t.getCause() + "," + t.getMessage());
                        super.onFailure(call, t);
                    }

                    @Override
                    protected void onRecipeResponse(Call call, retrofit2.Response response) {
                        dialog.dismiss();
                        Log.w(logger, "onTherapyResponse : " + response.toString());
                        if (!response.isSuccessful()) therapyDtlCallback.failure("Failed");
                    }

                    @Override
                    protected void onRecipeObject(Call call, TherapyDtlResponseModel response) {
                        dialog.dismiss();
                        Log.w(logger, "onTherapyObject : " + response.toString());
                        therapyDtlCallback.success(response);
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
