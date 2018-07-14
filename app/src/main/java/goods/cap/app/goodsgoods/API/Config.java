package goods.cap.app.goodsgoods.API;

/* API 관련 변수 설정, created by supermoon. */

public class Config {
    public static final String API_KEY2 = "20180528IDTJDIIPM4WXBJAIJK4QPA";
    // 식단 구분 코드
    /* 수능점수 Up 특별식단: 254001,
    美와건강 다이어트 식단: 254002,
    행복한 가정을위한 식단: 254003,
    특별한 날 이벤트: 254004
    기분이 좋아지는 식단: 254005*/
    public static final String[] tabList = new String[]{"수험생을 위한 식단", "가정을 위한 식단", "기분이 좋아지는 식단", "특별한 날 이벤트 식단", "美를 위한 다이어트 식단"};
    public static final int[] dietCode = new int[]{254001,  254003, 254005, 254004, 254002};
    public static final int[] limitCount = new int[]{103, 42, 10, 23, 91};

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
