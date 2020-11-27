package webcrawl.webcrawl.dto;

import webcrawl.webcrawl.type.ProcessStatusType;
import java.util.HashSet;

public class WebCrawlProcessStatusDTO {
    
    private String id;
    
    private ProcessStatusType status;
    
    HashSet<String> urls;

    public WebCrawlProcessStatusDTO(String id, ProcessStatusType status) {
        this.id = id;
        this.status = status;
        this.urls = new HashSet();
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ProcessStatusType getStatus() {
        return status;
    }

    public void setStatus(ProcessStatusType status) {
        this.status = status;
    }

    public HashSet<String> getUrls() {
        return urls;
    }

    public void setUrls(HashSet<String> urls) {
        this.urls = urls;
    }
}
