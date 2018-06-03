package goods.cap.app.goodsgoods.API;

/* API 관련 변수 설정, created by supermoon. */

public class Config {
    public static final String API_KEY = "d43e3638a6db0b8a56a6fce44d37a02949b592cfbcf66f0eb5ef58aba9fb980f";
    public static final String API_KEY2 = "20180528IDTJDIIPM4WXBJAIJK4QPA";
    public static final String API_KEY3 = "j1GvGzyIqKc2sp69A1Za2l4jqMxvo00nTaZQeO%2BbF%2BxoIAd8Azd7p0rHGdvMtL35zuIwVrbaQkMtV7f7%2FJgBkA%3D%3D";
    // 식단 구분 코드
    /* 수능점수 Up 특별식단: 254001,
    美와건강 다이어트 식단: 254002,
    행복한 가정을위한 식단: 254003,
    특별한 날 이벤트: 254004, -> 태그 바꿔야 함.
    기분이 좋아지는 식단: 254005 -> 태그 바꿔야 함.*/
    public static final int[] dietCode = new int[]{254001, 254002, 254002, 254003, 254004, 254005};
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
