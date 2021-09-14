package com.garanti.internal;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import io.cucumber.java.After;
import io.cucumber.java.Before;

public class TestLoanApprovalProcess extends JbpmTestUtil {
	
    private static final String PROCESS_DEFINITION_ID = "loanapproval.approval";
    
    private static final String USER_ID = "rhpamAdmin";
    

    public TestLoanApprovalProcess() {
        super();
    }

    @Before
    public void testBefore() throws Exception {
        setUp();
    }

    @After
    public void testAfter() throws Exception {
        tearDown();
    }

    @Test
    public void testStartAndComplete() {
    	List<String> bpmns = new ArrayList<String>();
    	bpmns.add("com/garanti/loanapproval/approval.bpmn");
        assertTrue(bpmns.size() > 0);
        initialize(bpmns);
        
        boolean hasDefinition = useProcessDefinition(PROCESS_DEFINITION_ID);
        assertTrue(hasDefinition);
        assertNotNull(getKieSession());
        
        startProcess();
        assertNotNull(getProcessInstance());
        
        assertTrue(isHumanTaskClaimableBy("Data Entry", USER_ID));
        
        Map<String, String> data = new HashMap<String, String>();
        data.put("firstName", "John" );
        data.put("lastName", "Finn" );
        data.put("DOB", "11/11/2011" );
        data.put("CreditScore", "0.4" );
        data.put("income", "500000" );
        
        assertTrue(completeHumanTask("Data Entry", USER_ID, data));
        
        // Check the node here
        assertNodeActive(getProcessInstance().getId(), getKieSession(), "Approval Documentation");
        
        assertTrue(isHumanTaskClaimableBy("Approval Documentation", USER_ID));
        
        data = new HashMap<String, String>();
        assertTrue(completeHumanTask("Approval Documentation", USER_ID, data));
        
        
        // Assert process completed
        assertProcessInstanceCompleted(getProcessInstance().getId());
    }
    
    @Test
    public void testLoanReject() {
    	List<String> bpmns = new ArrayList<String>();
    	bpmns.add("com/garanti/loanapproval/approval.bpmn");
        assertTrue(bpmns.size() > 0);
        initialize(bpmns);
        
        boolean hasDefinition = useProcessDefinition(PROCESS_DEFINITION_ID);
        assertTrue(hasDefinition);
        assertNotNull(getKieSession());
        
        startProcess();
        assertNotNull(getProcessInstance());
        
        assertTrue(isHumanTaskClaimableBy("Data Entry", USER_ID));
        
        Map<String, String> data = new HashMap<String, String>();
        data.put("firstName", "John" );
        data.put("lastName", "Finn" );
        data.put("DOB", "11/11/2011" );
        data.put("CreditScore", "0.8" );
        data.put("income", "500000" );
        
        assertTrue(completeHumanTask("Data Entry", USER_ID, data));
        
        // Check the node here
        assertNodeActive(getProcessInstance().getId(), getKieSession(), "Reject Documentation");
        
        assertTrue(isHumanTaskClaimableBy("Reject Documentation", USER_ID));
        
        data = new HashMap<String, String>();
        assertTrue(completeHumanTask("Reject Documentation", USER_ID, data));
        
        
        // Assert process completed
        assertProcessInstanceCompleted(getProcessInstance().getId());
    }
}
