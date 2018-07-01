package goods.cap.app.goodsgoods.Model.Therapy;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import goods.cap.app.goodsgoods.Model.Diet.Header;

@Root(strict = false)
public class TherapyResponseModel {

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

        @Element(name = "items")
        private Items items;

        public Items getItems() {
            return items;
        }
        public void setItems(Items items) {
            this.items = items;
        }

        @Override
        public String toString() {
            return "Body{" +
                    "items=" + items +
                    '}';
        }
    }


}
