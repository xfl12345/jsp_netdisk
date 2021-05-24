package com.github.xfl12345.jsp_netdisk.model.utility.check;

import org.apache.ibatis.io.Resources;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;

public class JsonSchemaCheck{
    private final Schema schema;

    public JsonSchemaCheck(String jsonSchemaFilePath) throws IOException {
        //JSON格式模板
        InputStream inputStream = Resources.getResourceAsStream(jsonSchemaFilePath);
        JSONObject schemaObject = new JSONObject(new JSONTokener(inputStream));
        inputStream.close();
        schema = SchemaLoader.load(schemaObject);
    }

    public boolean getCheckResultAsBoolean(String jsonString) {
        boolean isOK = false;
        JSONObject data = new JSONObject(new JSONTokener(jsonString) );
        try {
            schema.validate(data);
            isOK = true;
        } catch (ValidationException ignored) {
        }
        return isOK;
    }

    public void check(String jsonString) throws Exception {
        JSONObject data = new JSONObject(new JSONTokener(jsonString) );
        schema.validate(data);
    }
}
