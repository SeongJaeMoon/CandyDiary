package goods.cap.app.goodsgoods.Model.Health;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(strict = false)
public class HealthResponseModel {

    @Element(name = "body")
    private Body body;

    @Element(name = "header", required = false)
    private Header header;

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public static class Body{

        @Element(name="totalCount")
        private String totalCount;

        @Element(name="pageNo")
        private String pageNo;

        @Element(name="numOfRows")
        private String numOfRows;

        @ElementList(inline = true, required = false)
        private List<Health> healthList;

        public List<Health> getHealthList() {
            return healthList;
        }

        public void setHealthList(List<Health> healthList) {
            this.healthList = healthList;
        }

//        @Element(name = "items")
//        private Items items;
//
//        public Items getItems() {
//            return items;
//        }
//        public void setItems(Items items) {
//            this.items = items;
//        }

        public String getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(String totalCount) {
            this.totalCount = totalCount;
        }

        public String getPageNo() {
            return pageNo;
        }

        public void setPageNo(String pageNo) {
            this.pageNo = pageNo;
        }

        public String getNumOfRows() {
            return numOfRows;
        }

        public void setNumOfRows(String numOfRows) {
            this.numOfRows = numOfRows;
        }

        @Override
        public String toString() {
            return "Body{" +
                    "items=" + getHealthList() +
                    '}';
        }
    }

}
