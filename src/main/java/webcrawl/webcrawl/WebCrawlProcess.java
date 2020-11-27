package webcrawl.webcrawl;

import webcrawl.webcrawl.dto.ProcessControlDTO;
import webcrawl.util.URLUtil;
import webcrawl.util.Util;
import java.util.HashSet;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebCrawlProcess implements Runnable{

    Logger logger = LoggerFactory.getLogger(WebCrawlProcess.class);
    
    private final HashSet<String> URLS_VISITED = new HashSet();    
    private final ProcessControlDTO processControl;
    private final String id;
    
    public WebCrawlProcess(ProcessControlDTO processControl, String id) {        
        this.processControl = processControl;
        this.id = id;
    }
    
    public void startProcess(){
        
        if((processControl != null) && Util.isIDValid(id))
            new Thread(this).start();
    }

    @Override
    public void run() {
        
        logger.info("Initializing WebCrawl Thread Process. [ID: " + id + " - KEYWORD: " + processControl.getKeyword() +
                    " - URL_BASE: " + processControl.getUriBase() + " - MAX_RESULTS: " + processControl.getMaxResults() + "]");

        findKeywordInURL(processControl.getUriBase());
        
        WebCrawlManager.finalizeProcessing(id);
        
        logger.info("WebCrawl Process successfully finalized. [ID: " + id + " - KEYWORD: " + processControl.getKeyword() + 
                    " - URLS VISITED: " + URLS_VISITED.size() + " - URLS KEYWORD FOUND: " + WebCrawlManager.getURLsSize(id) + "]");
    }
    
    private void findKeywordInURL(String url){
        
        if(Util.isStringValid(url)){
            
            url = URLUtil.normalizeURL(url, processControl.getUriBase());
  
            if(this.verifyAddVisitedURL(url)){

                String text = URLUtil.getTextFromURL(url);

                if(Util.isStringValid(text) && text.toLowerCase().contains(processControl.getKeyword().toLowerCase()))
                    WebCrawlManager.addURLProcessingStatus(processControl, id, url);

                List<String> urls = URLUtil.getValidURLListFromText(text, processControl.getUriBase());

                if((urls != null) && WebCrawlManager.vefifyCanAddURL(id, processControl.getMaxResults()))
                    urls.parallelStream().forEach(u -> findKeywordInURL(u));
            }
        }
    }
    
    synchronized private boolean verifyAddVisitedURL(String url){    
        
        if(Util.isStringValid(url) && !URLS_VISITED.contains(url) && WebCrawlManager.vefifyCanAddURL(id, processControl.getMaxResults())){
            
            URLS_VISITED.add(url);

            return true;
        }
        
        return false;
    }
}
