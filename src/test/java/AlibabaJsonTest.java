import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import pers.xfl.jsp_netdisk.model.pojo.database.TbAccount;

public class AlibabaJsonTest {
    public static void main(String[] args) {
        TbAccount tbAccount = new TbAccount();
        tbAccount = JSON.parseObject("{\"accountId\":1}", TbAccount.class, Feature.SupportNonPublicField );
        String str = JSON.toJSONString(tbAccount);
        System.out.println(str);
    }
}
