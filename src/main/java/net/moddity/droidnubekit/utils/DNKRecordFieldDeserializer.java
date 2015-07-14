package net.moddity.droidnubekit.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.moddity.droidnubekit.responsemodels.DNKRecordField;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jaume on 13/7/15.
 */
public class DNKRecordFieldDeserializer implements JsonDeserializer<Map<String, DNKRecordField>> {
    @Override
    public Map<String, DNKRecordField> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Map<String, DNKRecordField> field = new HashMap<String, DNKRecordField>();

        JsonObject contents = json.getAsJsonObject();

        for(Map.Entry<String,JsonElement> entry  : contents.entrySet()) {
            DNKRecordField recordField = new DNKRecordField();

            String type = entry.getValue().getAsJsonObject().get("type").getAsString();
            recordField.setType(DNKFieldTypes.fromString(type));

            switch (recordField.getType()) {
                case STRING:
                    recordField.setValue(entry.getValue().getAsJsonObject().get("value").getAsString());
                    break;
                case INT64:
                    recordField.setValue(entry.getValue().getAsJsonObject().get("value").getAsBigInteger());
                    break;
                case TIMESTAMP:
                    Timestamp ts = new Timestamp(entry.getValue().getAsJsonObject().get("value").getAsBigInteger().longValue());
                    Date date = new Date(ts.getTime());
                    recordField.setValue(date);
                    break;
                default:
                    break;
            }

            field.put(entry.getKey(), recordField);
        }

        return field;
    }
}
