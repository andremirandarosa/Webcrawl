package webcrawl.webcrawl.dto;

public class ProcessControlDTO {
    
    private String keyword; 
    
    private String uriBase;
    
    private int maxResults;
    
    public ProcessControlDTO(String keyword, String uriBase, int maxResults) {
        this.keyword = keyword;
        this.uriBase = uriBase;
        this.maxResults = maxResults;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getUriBase() {
        return uriBase;
    }

    public void setUriBase(String uriBase) {
        this.uriBase = uriBase;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }
    
    @Override
    public boolean equals(Object obj) {
        
        if(obj instanceof ProcessControlDTO){
           
            ProcessControlDTO pc = (ProcessControlDTO) obj;
                        
            return (this.keyword.equals(pc.keyword) &&
                    this.uriBase.equals(pc.uriBase) &&
                    (this.maxResults == pc.maxResults));
        }
        
        return false;
    }
}
