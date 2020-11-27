package webcrawl.webcrawl;

import webcrawl.webcrawl.type.ProcessStatusType;
import webcrawl.webcrawl.dto.ProcessControlDTO;
import webcrawl.util.Util;
import webcrawl.webcrawl.dto.WebCrawlProcessStatusDTO;
import webcrawl.webcrawl.dto.WebCrawlRequestDTO;
import java.net.URI;
import java.util.AbstractMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebCrawlManager {

    static Logger logger = LoggerFactory.getLogger(WebCrawlManager.class);
    
    static private final ConcurrentHashMap<ProcessControlDTO, String> MAP_PROCESS_CONTROL_ID = new ConcurrentHashMap();
    static private final ConcurrentHashMap<String, WebCrawlProcessStatusDTO> MAP_ID_STATUS = new ConcurrentHashMap();
    static private final ConcurrentLinkedQueue<AbstractMap.SimpleEntry<ProcessControlDTO, String>> PROCESSING_QUEUE = new ConcurrentLinkedQueue<>();
    
    public WebCrawlManager() {
        this.startProcessing();
    }
    
    static public boolean verfifyIdExists(String id){        
        return MAP_ID_STATUS.containsKey(id);
    }
    
    synchronized static public boolean addURLProcessingStatus(ProcessControlDTO processControl, String id, String url){
        
        if(Util.isIDValid(id) && Util.isStringValid(url) && vefifyCanAddURL(id, processControl.getMaxResults())){
            
            if(MAP_ID_STATUS.containsKey(id)){

                MAP_ID_STATUS.get(id).getUrls().add(url);
                
                return true;
            }
        }
        
        return false;
    }

    static public boolean vefifyCanAddURL(String id, int maxResults){
        
        if(maxResults == -1) return true;
        else if(getURLsSize(id) < maxResults) return true;
        
        return false;
    }
    
    static public int getURLsSize(String id){
        
        WebCrawlProcessStatusDTO dto = MAP_ID_STATUS.get(id);

        if(dto != null)
            return dto.getUrls().size();

        return 0;
    }
    
    static public boolean finalizeProcessing(String id){
        
        if(Util.isIDValid(id)){
            
            if(MAP_ID_STATUS.containsKey(id)){

                WebCrawlProcessStatusDTO dto = MAP_ID_STATUS.get(id);
                
                if(dto != null){
                    
                    dto.setStatus(ProcessStatusType.done);
                    
                    MAP_ID_STATUS.put(id, dto);
                    
                    return true;
                }
            }
        }
        
        return false;
    }
    
    public String createNewCrawlProcess(WebCrawlRequestDTO requestDTO, URI uriBase, int maxResults){
        
        if((requestDTO != null) && (uriBase != null)){
            
            ProcessControlDTO processControl = new ProcessControlDTO(requestDTO.getKeyword().toLowerCase(), uriBase.toString(), maxResults);
            
            String id = this.getProcessControlID(processControl);
        
            if(!Util.isIDValid(id)) return this.createNewProcess(processControl);
            else{
                    
                WebCrawlProcessStatusDTO statusDTO = MAP_ID_STATUS.get(id);

                if(statusDTO != null)
                    return statusDTO.getId();
            } 
        }
        
        return null;
    }
    
    public WebCrawlProcessStatusDTO getProcessStatus(String id){        
        return MAP_ID_STATUS.get(id);
    }
    
    private void startProcessing(){
        
        new Thread(() -> {
            
            while(true){
                
                AbstractMap.SimpleEntry<ProcessControlDTO, String> entry = PROCESSING_QUEUE.poll();
                
                if(entry != null)
                    new WebCrawlProcess(entry.getKey(), entry.getValue()).startProcess();
                
                Util.sleep(1000);
            }
            
        }).start();
    }
    
    private String getProcessControlID(ProcessControlDTO processControl){
        
        if(processControl != null){
        
            ProcessControlDTO pc = MAP_PROCESS_CONTROL_ID.keySet().stream()
                                                   .filter(k -> k.equals(processControl))
                                                   .findAny().orElse(null);
            
            if(pc != null)
                return MAP_PROCESS_CONTROL_ID.get(pc);

        }
        
        return null;
    }
    
    private String createNewProcess(ProcessControlDTO processControl){
        
        if((processControl != null)){
            
            String id = Util.generateID();
            
            if(Util.isIDValid(id)){

                MAP_PROCESS_CONTROL_ID.put(processControl, id);                        
                MAP_ID_STATUS.put(id, new WebCrawlProcessStatusDTO(id, ProcessStatusType.active));
                PROCESSING_QUEUE.add(new AbstractMap.SimpleEntry<>(processControl, id));
            
                return id;
            }
        }
        
        return null;
    }
}
