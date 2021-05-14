import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.github.xfl12345.jsp_netdisk.appconst.api.request.JsonApiRequestField;
import com.github.xfl12345.jsp_netdisk.model.pojo.database.TbAccount;
import com.github.xfl12345.jsp_netdisk.model.pojo.api.response.JsonCommonApiResponseObject;

public class AlibabaJsonTest {
    public static void main(String[] args) {
        TbAccount tbAccount = new TbAccount();
        tbAccount = JSON.parseObject("{\"accountId\":'123', \"email\":\"\"}", TbAccount.class, Feature.SupportNonPublicField );
        String str = JSON.toJSONString(tbAccount);
        System.out.println(str);

        JsonCommonApiResponseObject jsonCommonApiResponseObject = new JsonCommonApiResponseObject("1");
        System.out.println(JSON.toJSON(jsonCommonApiResponseObject));
        System.out.println(JSON.toJSON(new JsonApiRequestField()));
    }
}
