package goods.cap.app.goodsgoods.API;

import android.content.Context;
import android.support.annotation.NonNull;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class ApiDataFood {
    // 건강 기능 식품 DB 요청 URL
    private static final String BASE_URL = "http://apis.data.go.kr/1470000/HtfsTrgetInfoService/";

    private static ApiDataFood apiDataFood;
    private HttpAPI httpAPI;

    public static synchronized ApiDataFood getInstance(){
        if (apiDataFood == null){
            apiDataFood = new ApiDataFood();
        }
        return apiDataFood;
    }
    private void ApiDataFood(@NonNull final Context currContext) {
        try {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            // 로그 수준 설정
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            Interceptor headerInterceptor = new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {
                    Request original = chain.request();
                    Request.Builder builder = original.newBuilder();
                    builder.method(original.method(), original.body());

                    Request request = builder.build();

                    return chain.proceed(request);
                }
            };

            OkHttpClient httpClient = new OkHttpClient.Builder()
                    .addInterceptor(headerInterceptor)
                    .addInterceptor(logging)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(SimpleXmlConverterFactory.createNonStrict(new Persister(new AnnotationStrategy())))
                    .client(httpClient)
                    .build();

            httpAPI = retrofit.create(HttpAPI.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public HttpAPI getApi(Context currContext) {
        if (apiDataFood == null) {
            getInstance();
        }
        apiDataFood.ApiDataFood(currContext);

        return httpAPI;
    }
}
