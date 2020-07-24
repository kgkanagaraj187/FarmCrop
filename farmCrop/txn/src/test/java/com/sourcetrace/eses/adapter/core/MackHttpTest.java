package com.sourcetrace.eses.adapter.core;

import org.apache.struts2.StrutsStatics;
import org.springframework.mock.web.MockHttpServletRequest;

import junit.framework.TestCase;


public class MackHttpTest extends TestCase implements StrutsStatics {

    public void testMockHttpTest(){
        
        MockHttpServletRequest mock = new MockHttpServletRequest("POST", "/tserv/rs");
        //mock.
        
        
    }
    
}
