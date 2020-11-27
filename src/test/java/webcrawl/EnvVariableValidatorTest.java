package webcrawl;

import webcrawl.EnvVariableValidator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat; 
import static org.hamcrest.Matchers.*;

public class EnvVariableValidatorTest {

    @Test
    public void testValidate() {
        
        EnvVariableValidator envValidator = new EnvVariableValidator();
        
        assertTrue(envValidator.validate("http://hiring.axreng.com", null));
        assertThat(envValidator.getMaxResults(), is(-1));
        
        assertTrue(envValidator.validate("HTTP://hiring.axreng.com", null));        
        assertThat(envValidator.getMaxResults(), is(-1));
        
        assertTrue(envValidator.validate("https://hiring.axreng.com", null));
        assertThat(envValidator.getMaxResults(), is(-1));
        
        assertTrue(envValidator.validate("hTtPs://hiring.axreng.com", null));
        assertThat(envValidator.getMaxResults(), is(-1));
        
        assertTrue(envValidator.validate("http://hiring.axreng.com/", null));
        assertThat(envValidator.getMaxResults(), is(-1));
        
        assertTrue(envValidator.validate("http://hiring.axreng.com/", "10"));        
        assertThat(envValidator.getMaxResults(), is(10));
        
        assertTrue(envValidator.validate("http://hiring.axreng.com/", "300"));        
        assertThat(envValidator.getMaxResults(), is(300));
        
        assertTrue(envValidator.validate("http://hiring.axreng.com/", "-1"));
        assertThat(envValidator.getMaxResults(), is(-1));
        
        assertTrue(envValidator.validate("http://hiring.axreng.com/", "-30"));
        assertThat(envValidator.getMaxResults(), is(-1));
        
        assertTrue(envValidator.validate("http://hiring.axreng.com/", "0"));
        assertThat(envValidator.getMaxResults(), is(-1));
        
        assertTrue(envValidator.validate("http://hiring.axreng.com/", "a"));
        assertThat(envValidator.getMaxResults(), is(-1));
        
        assertTrue(envValidator.validate("http://hiring.axreng.com/", "1a"));
        assertThat(envValidator.getMaxResults(), is(-1));
        
        assertFalse(envValidator.validate(null, null));        
        assertFalse(envValidator.validate(null, "10"));        
        assertFalse(envValidator.validate("htt://hiring.axreng.com/", "10"));
        assertFalse(envValidator.validate("httpsx://hiring.axreng.com/", "10"));
        assertFalse(envValidator.validate("https:x//hiring.axreng.com/", "10"));
        assertFalse(envValidator.validate("ahttps//hiring.axreng.com/", "10"));
        assertFalse(envValidator.validate("//hiring.axreng.com/", ""));
        assertFalse(envValidator.validate("/hiring.axreng.com/", ""));
        assertFalse(envValidator.validate("hiring.axreng.com/", ""));
        assertFalse(envValidator.validate("http://", "10"));
        assertFalse(envValidator.validate("http://", null));
        assertFalse(envValidator.validate("http://", ""));
        assertFalse(envValidator.validate("http://teste", ""));
    }
}
