
import org.apache.ibatis.io.Resources;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

public class JsonSchemaValidatorTest {

    public static void main(String[] args) throws IOException {
        String jsonString = "{" +
                "  \"version\": '1'," +
                "    \"operation\": 'login'," +
                "    \"data\": {} }";

        //JSON格式模板
        InputStream inputStream = Resources.getResourceAsStream("json/schema/base_request_object.json");
        JSONObject Schema = new JSONObject(new JSONTokener(inputStream));
        //这个是你打算验证的JSON，这里我也用一份文件来存放，你也可以使用string或者jsonObject
        JSONObject data = new JSONObject(new JSONTokener(jsonString) );

        org.everit.json.schema.Schema schema = SchemaLoader.load(Schema);
        try {
            schema.validate(data);
            System.out.println("true");
        } catch (ValidationException e) {
            System.out.println(e.getErrorMessage());
        }
        inputStream.close();

    }
}
