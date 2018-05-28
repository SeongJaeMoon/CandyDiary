package goods.cap.app.goodsgoods.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FoodQuery {

    @SerializedName("totalCnt")
    @Expose
    private Integer totalCnt;
    @SerializedName("startRow")
    @Expose
    private Integer startRow;
    @SerializedName("endRow")
    @Expose
    private Integer endRow;
    @SerializedName("result")
    @Expose
    private Result result;
    @SerializedName("row")
    private List<Food>row = null;

    public Integer getTotalCnt() {
        return totalCnt;
    }

    public void setTotalCnt(Integer totalCnt) {
        this.totalCnt = totalCnt;
    }

    public Integer getStartRow() {
        return startRow;
    }

    public void setStartRow(Integer startRow) {
        this.startRow = startRow;
    }

    public Integer getEndRow() {
        return endRow;
    }

    public void setEndRow(Integer endRow) {
        this.endRow = endRow;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public List<Food> getRow() {
        return row;
    }

    public void setRow(List<Food> row) {
        this.row = row;
    }

    @Override
    public String toString() {
        return "FoodQuery{" +
                "totalCnt=" + totalCnt +
                ", startRow=" + startRow +
                ", endRow=" + endRow +
                ", result=" + result +
                ", row=" + row +
                '}';
    }
}
