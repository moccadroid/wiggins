package org.lalelu.wiggins.data.jsonparser;

import java.util.HashMap;
import java.util.Map;

public class DefaultJsonParser implements JsonParser {
    @Override
    public Map<String, Object> parseJson(String json) {
        return new HashMap<String, Object>();
    }
}
