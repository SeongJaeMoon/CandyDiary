package goods.cap.app.goodsgoods.Fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import goods.cap.app.goodsgoods.R;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public class NoticeFragment extends Fragment{

    private static final String logger = NoticeFragment.class.getSimpleName();
    public static final String ARG_PAGE = "ARG_PAGE";
    private ListView notiListView;
    private String cVersion, lVersion;
    public static NoticeFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        NoticeFragment fragment = new NoticeFragment();
        fragment.setArguments(args);
        Log.i(logger, "page : "+ args);
        return fragment;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        Log.w(logger, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_noti, container, false);
        notiListView = view.findViewById(R.id.noti_list);
        checkVersion();
        List<String>list = new ArrayList<String>();
        list.add(String.format("현재 버전: %s, 최신 버전: %s", cVersion, lVersion));
        list.add("문의하기");
        list.add("공지사항");
        list.add("운영정책");
        list.add("개인정보 처리방침");
        list.add("이용약관");
        list.add("오픈소스 라이선스");
        list.add("별점 주기");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list);
        notiListView.setAdapter(adapter);
        notiListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.w(logger, "position => " + position);
                switch (position){
                    case 0: if (!TextUtils.equals(cVersion, lVersion)) { showUpdateDialog();}break;//버전 정보
                    case 1: sendEmail(getActivity()); break; //문의하기
                    case 2: break; //공지사항
                    case 3: break; //운영 정책
                    case 4: break; //개인정보 처리방침
                    case 5: break; //이용 약관
                    case 6: break; //오픈소스 라이센스
                    case 7: break; //별점 주기
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            for (String key : savedInstanceState.keySet()) {
                Log.i(logger, "key : " + key);
            }
        }
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void onDetach(){
        super.onDetach();
    }

    private void sendEmail(Context context){
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        try {
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"seongjae.m@gmail.com"});
            emailIntent.setType("text/html");
            emailIntent.setPackage("com.google.android.gm");
            if(emailIntent.resolveActivity(context.getPackageManager())!=null) startActivity(emailIntent);
//            startActivity(emailIntent);
        } catch (Exception e) {
            e.printStackTrace();
            emailIntent.setType("text/html");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"seongjae.m@gmail.com"});
            startActivity(Intent.createChooser(emailIntent, "Send Email"));
        }
    }
    private void checkPlayStore(){
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(getActivity());
        if (status != ConnectionResult.SUCCESS) {
            Dialog dialog = googleApiAvailability.getErrorDialog(getActivity(), status, -1);
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    dialog.dismiss();
                }
            });
            dialog.show();
            googleApiAvailability.showErrorNotification(getActivity(), status);
        }
    }
    private void checkVersion() {
        try {
            FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
            String latestVersion = remoteConfig.getString("latest_version");
            String currentVersion = getAppVersion(getActivity());
            lVersion = latestVersion;
            cVersion = currentVersion;
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private String getAppVersion(Context context){
        String result = "";
        try {
            result = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0)
                    .versionName;
            result = result.replace("[a-zA-Z]|-", "");
        } catch (Exception e) {
            Log.e("getAppVersion", e.getMessage());
        }

        return result;
    }
    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getResources().getString(R.string.pref_version_up));
        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent marketLaunch = new Intent(Intent.ACTION_VIEW);
                marketLaunch.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + getActivity().getPackageName()));
                startActivity(marketLaunch);
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
