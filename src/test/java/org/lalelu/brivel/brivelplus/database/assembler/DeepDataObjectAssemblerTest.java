package org.lalelu.brivel.brivelplus.database.assembler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lalelu.brivel.brivelplus.requests.Request;
import org.lalelu.brivel.brivelplus.selectors.FieldSelector;
import org.lalelu.brivel.brivelplus.selectors.Selector;

public class DeepDataObjectAssemblerTest {
	private DeepDataObjectAssembler assembler;
	
	@Before
	public void setUp() {
		assembler = new DeepDataObjectAssembler();
	}
	
	/**
	 * Test with a single request with no sub-request
	 */
	@Test
	public void testSimpleRequest() {
		Request<TestData> request = new Request<TestData>(TestData.class);
		request.addSelector(new FieldSelector<Object>("", "", "setTestField", Object.class));
		
		Integer value = Integer.valueOf(42);
		assembler.setSelectorList(request.getSelectors());
		assembler.setValues(new Object[]{value});
		try {
			TestData testObject = assembler.assembleObject(request);
			Assert.assertTrue("Wrong Integer in testField. Is " + testObject.testField + ", should be 42",testObject.testField.equals(Integer.valueOf(42)));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue("Exception caught.",false);
		}
	}
	
	/**
	 * Test with a single request with one sub-request
	 */
	@Test
	public void testRequestWithOneSubRequest() {
		Request<TestData> request = new Request<TestData>(TestData.class);
		request.addSelector(new FieldSelector<Object>("", "", "setTestField", Object.class));
		
		Request<TestData> subRequest = new Request<TestData>(TestData.class);
		subRequest.addSelector(new FieldSelector<Object>("", "", "setTestField", Object.class));
		request.addSubRequest("setSubData", subRequest);
		
		request.compileSelect();
		
		List<Selector<?>> selectors = new LinkedList<Selector<?>>();
		selectors.addAll(request.getSelectSelectors());
		
		assembler.setSelectorList(selectors);
		assembler.setValues(new Object[]{Integer.valueOf(1),Integer.valueOf(2)});
		
		try {
			TestData testObject = assembler.assembleObject(request);
			Assert.assertTrue("Wrong Integer in testField in the main request. Is " + testObject.testField + ", should be 1",testObject.testField.equals(Integer.valueOf(1)));
			Assert.assertNotNull("SubData should be initialized",testObject.subData);
			Assert.assertTrue("Wrong Integer in testField in the sub-request. Is " + testObject.subData.testField + ", should be 2",testObject.subData.testField.equals(Integer.valueOf(2)));
			
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue("Exception caught.",false);
		}
	}
	
	/**
	 * Test with a single request with two sub-requests.
	 * subRequest has a depth of 2 (subRequest has an additional subRequest). 
	 * otherSubRequest has a depth of 3.
	 */
	@Test
	public void testRequestDepthTwoAndThreeSubRequest() {
		int k = 6;
		int p = 3;
		Integer[] values = new Integer[k];
		for(int i = 0; i < k; i++) {
			values[i] = i;
		}
		
		Request<TestData> request = new Request<TestData>(TestData.class);
		request.addSelector(new FieldSelector<Object>("", "", "setTestField", Object.class));
		
		Request<TestData> prevRequest = request;
		for(int i = p; i < k; i++) {
			Request<TestData> subRequest = new Request<TestData>(TestData.class);
			subRequest.addSelector(new FieldSelector<Object>("", "", "setTestField", Object.class));
			prevRequest.addSubRequest("setOtherSubData", subRequest);
			prevRequest = subRequest;
		}
		
		prevRequest = request;
		for(int i = 1; i < p; i++) {
			Request<TestData> subRequest = new Request<TestData>(TestData.class);
			subRequest.addSelector(new FieldSelector<Object>("", "", "setTestField", Object.class));
			prevRequest.addSubRequest("setSubData", subRequest);
			prevRequest = subRequest;
		}
		
		request.compileSelect();
		
		List<Selector<?>> selectors = new LinkedList<Selector<?>>();
		selectors.addAll(request.getSelectSelectors());
		
		assembler.setSelectorList(selectors);
		assembler.setValues(values);
		
		try {
			TestData testObject = assembler.assembleObject(request);
			Assert.assertTrue("Wrong Integer in testField in the main request. Is " + testObject.testField + ", should be " + values[0] ,testObject.testField == values[0]);
			Assert.assertNotNull("SubData should be initialized",testObject.subData);

			TestData prevData = testObject;
			for(int i = 1; i <= p; i++) {
				Assert.assertTrue("Wrong Integer in testField in the other sub-request tree. Is " + prevData.otherSubData.testField + ", should be " + values[i], prevData.otherSubData.testField == values[i]);
				prevData = prevData.otherSubData;
			}
			
			prevData = testObject;
			for(int i = k-(p-1); i < k; i++) {
				Assert.assertTrue("Wrong Integer in testField in the sub-request tree. Is " + prevData.subData.testField + ", should be " + values[i], prevData.subData.testField == values[i]);
				prevData = prevData.subData;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue("Exception caught.",false);
		}
	}
	
	/**
	 * Test with a single request with and a subRequest with a depth of 4.
	 * (request.subRequest.subRequest.subRequest.subRequest != null)
	 */
	@Test
	public void testRequestDepthFourSubRequest() {
		int k = 5;
		Integer[] values = new Integer[k];
		for(int i = 0; i < k; i++) {
			values[i] = i;
		}
		
		List<Request<TestData>> list = new ArrayList<Request<TestData>>();
		
		Request<TestData> request = new Request<TestData>(TestData.class);
		request.addSelector(new FieldSelector<Object>("", "", "setTestField", Object.class));
		list.add(request);
		
		Request<TestData> prevRequest = request;
		for(int i = 1; i < k; i++) {
			Request<TestData> subRequest = new Request<TestData>(TestData.class);
			subRequest.addSelector(new FieldSelector<Object>("", "", "setTestField", Object.class));
			prevRequest.addSubRequest("setSubData", subRequest);
			prevRequest = subRequest;
		}
		
		request.compileSelect();
		
		List<Selector<?>> selectors = new LinkedList<Selector<?>>();
		selectors.addAll(request.getSelectSelectors());
		
		assembler.setSelectorList(selectors);
		assembler.setValues(values);
		
		try {
			TestData testObject = assembler.assembleObject(request);
			Assert.assertTrue("Wrong Integer in testField in the main request. Is " + testObject.testField + ", should be " + values[0] ,testObject.testField == values[0]);
			Assert.assertNotNull("SubData should be initialized",testObject.subData);
			TestData prevData = testObject;
			for(int i = 1; i < k; i++) {
				Assert.assertTrue("Wrong Integer in testField in the sub-request. Is " + prevData.subData.testField + ", should be " + values[i], prevData.subData.testField == values[i]);
				prevData = prevData.subData;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue("Exception caught.",false);
		}
	}
}
