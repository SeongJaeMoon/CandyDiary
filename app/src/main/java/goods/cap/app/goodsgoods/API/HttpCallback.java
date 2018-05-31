package goods.cap.app.goodsgoods.API;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class HttpCallback <S> implements Callback {

    private static final String logger = HttpCallback.class.getSimpleName();

    Activity activity;
    Context context;

    public HttpCallback(Activity activity){
        this.activity = activity;
    }

    public HttpCallback(Context context){
        this.context = context;
    }

    @Override
    public void onResponse(Call call, Response response) {
        common();
        onRecipeResponse(call, response);
        Log.w(logger, "Response : " + response.toString());

        Object obj = response.body();
        Log.w(logger, "ResponseBody : " + obj.toString());

        if (obj != null) {
            S objectResponse = (S) obj;
            onRecipeObject(call, objectResponse);
        }
    }

    @Override
    public void onFailure(Call call, Throwable t) {
        common();
        //onFailureRecipe(call, t);
    }
    /**
     * 수신 된 HTTP 응답에 대해 호출.
     *
     * Note: 404, 500번 에러 처리 필요.
     * Call {@link Response # isSuccess()} 응답이 성공을 나타내는 지 여부를 결정.
     */
    protected abstract void onRecipeResponse(Call call, Response response);

    /**
     * 수신 된 HTTP 응답에 대해 호출.
     *
     * Note: 404, 500번 에러 처리 필요.
     * Call {@link Response # isSuccess()} 응답이 성공을 나타내는 지 여부를 결정.
     */
    protected abstract void onRecipeObject(Call call, S response);

    /**
     * 네트워크 예외가 서버와 통신 중이거나 예상치 못한 상황이 발생하면 호출.
     * 요청 중 혹은, 응답을 처리하는 중 예외가 발생.
     */
    //protected abstract void onFailureRecipe(Call call, Throwable t);

    /**
     * 계속해서 실행.
     */
    protected abstract void common();
}
