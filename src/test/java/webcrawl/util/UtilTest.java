package webcrawl.util;

import webcrawl.util.Util;
import java.util.LinkedList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class UtilTest {
    
    @Test
    public void testGenerateID() {
        
        String id = Util.generateID();
        assertNotNull(id);
        assertThat(id.length(), equalTo(8));
    }
    
    @Test
    public void testGenerateResponseError(){
        
        String json = Util.generateResponseError(null, 200, "message ok");
        assertNotNull(json);   
        assertThat(json, equalTo("{\"status\":200,\"message\":\"message ok\"}"));        
        
        json = Util.generateResponseError(null, 400, "message error");
        assertNotNull(json); 
        assertThat(json, equalTo("{\"status\":400,\"message\":\"message error\"}"));
    }
    
    @Test
    public void testIsStringValid(){
        
         assertThat(Util.isStringValid("12345678"), is(true));
         assertThat(Util.isStringValid("12345678 90124"), is(true));
         
         assertThat(Util.isStringValid(""), is(false));
         assertThat(Util.isStringValid(null), is(false));
    }
    
    @Test
    public void testIsIDValid(){
        
        assertThat(Util.isIDValid("12345678"), is(true));
        assertThat(Util.isIDValid("1234"), is(false));
        assertThat(Util.isIDValid("123456789"), is(false));
    }
    
    @Test
    public void testSleep(){
    
        Util.sleep(10);
    }   
    
    @Test
    public void testStringToTokenList(){

        LinkedList<String> list = Util.stringToTokenList("a b c", " ");
        assertNotNull(list);
        assertTrue(list.size() == 3);
        
        list = Util.stringToTokenList("a", "a");
        assertNotNull(list);
        assertTrue(list.size() == 1);
        
        list = Util.stringToTokenList("", "");
        assertNull(list);
        
        
        list = Util.stringToTokenList(null, "");
        assertNull(list);
        
        list = Util.stringToTokenList(null, "b");
        assertNull(list);
        
        list = Util.stringToTokenList("", null);
        assertNull(list);
        
        list = Util.stringToTokenList("a", null);
        assertNull(list);
    }
}
