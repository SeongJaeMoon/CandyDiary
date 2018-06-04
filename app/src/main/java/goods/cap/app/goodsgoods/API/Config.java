package goods.cap.app.goodsgoods.API;

/* API 관련 변수 설정, created by supermoon. */

public class Config {
    public static final String API_KEY = "d43e3638a6db0b8a56a6fce44d37a02949b592cfbcf66f0eb5ef58aba9fb980f";
    public static final String API_KEY2 = "20180528IDTJDIIPM4WXBJAIJK4QPA";
    public static final String API_KEY3 = "RhBtw0D38rfS94cQItO%2FF9mSUzUT32Q7ZH2GJFhCm0Sr4EwhKovhnDRMiVNbe9SeMeMQ%2BehHdi92MabxV1ozoQ%3D%3D";

    // 식단 구분 코드
    /* 수능점수 Up 특별식단: 254001,
    美와건강 다이어트 식단: 254002,
    행복한 가정을위한 식단: 254003,
    특별한 날 이벤트: 254004
    기분이 좋아지는 식단: 254005*/
    public static final String[] tabList = new String[]{"수험생을 위한 식단", "美를 위한 다이어트 식단", "가정을 위한 식단", "특별한 날 이벤트 식단", "기분이 좋아지는 식단"};
    public static final int[] dietCode = new int[]{254001, 254002, 254003, 254004, 254005};
    public static final int[] limitCount = new int[]{103, 91, 42, 23, 10};

    public static String getAbUrl(String oldPath, String newPath){
        int len = oldPath.length();
        for(int i = len - 1; i > 0; --i){
            char temp = oldPath.charAt(i);
            if(temp == '/'){
                oldPath = oldPath.substring(0, i + 1);
                break;
            }
        }
        return oldPath + newPath;
    }
}
