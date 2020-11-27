package webcrawl.util;

import webcrawl.util.JSONUtil;
import webcrawl.webcrawl.dto.ErrorResponseDTO;
import webcrawl.webcrawl.dto.ProcessResponseDTO;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JSONUtilTest {
    
    String id = "12345678";
    
    @Test
    public void testJsonToObject() {
        
        String json = "{\n" +
                     "  \"status\": 200,\n" +
                     "  \"message\": \"message ok\"\n" +
                     "}";
        
        ErrorResponseDTO obj = (ErrorResponseDTO) JSONUtil.jsonToObject(json, ErrorResponseDTO.class);
        assertNotNull(obj);
        assertThat(obj.getStatus(), is(200));
        assertThat(obj.getMessage(), is("message ok"));
    }

    @Test
    public void testObjToJSON() {
                
        String json = JSONUtil.objToJSON(new ProcessResponseDTO(id));
        assertNotNull(json);
        assertThat(json.length() > 0, is(true));
        assertThat(json.contains(id), is(true));
    }
}
