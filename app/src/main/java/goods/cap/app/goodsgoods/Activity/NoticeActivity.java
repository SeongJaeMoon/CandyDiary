package goods.cap.app.goodsgoods.Activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import goods.cap.app.goodsgoods.R;

public class NoticeActivity extends AppCompatActivity {
    @BindView(R.id.webView)WebView webView;
    @BindView(R.id.web_container)SwipeRefreshLayout refreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        ButterKnife.bind(this);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://foodreet-e783e.firebaseapp.com/");
        webView.reload();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.reload();
                if(refreshLayout.isRefreshing())
                    refreshLayout.setRefreshing(false);
            }
        });
        refreshLayout.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if(refreshLayout.getScrollY() == 0){
                    refreshLayout.setEnabled(true);
                }else{
                    refreshLayout.setEnabled(false);
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }
}
