package org.lalelu.wiggins.requests.json;

import org.lalelu.wiggins.net.DefaultFileRetriever;
import org.lalelu.wiggins.net.FileRetriever;

public class JsonDataSource {
    private String url = "";
    private String mappingFileUrl = "";
    
    @SuppressWarnings("unused") // TODO
	private FileRetriever fileRetriever = null;

    public JsonDataSource() {
        fileRetriever = new DefaultFileRetriever();
    }

    public void setFileRetriever(FileRetriever fileRetriever) {
        this.fileRetriever = fileRetriever;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMappingFileUrl() {
        return mappingFileUrl;
    }

    public void setMappingFileUrl(String mappingFileUrl) {
        this.mappingFileUrl = mappingFileUrl;
    }

    public void load() {

    }
}
