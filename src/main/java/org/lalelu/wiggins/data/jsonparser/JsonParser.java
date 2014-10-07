package org.lalelu.wiggins.data.jsonparser;

import java.util.Map;

public interface JsonParser {

    public Map<String, Object> parseJson(String json);
}
