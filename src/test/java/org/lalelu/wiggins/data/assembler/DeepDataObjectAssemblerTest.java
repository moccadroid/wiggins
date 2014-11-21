package org.lalelu.wiggins.data.assembler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lalelu.wiggins.data.assembler.sql.SqlDeepDataObjectAssembler;
import org.lalelu.wiggins.requests.sql.SqlRequest;
import org.lalelu.wiggins.selectors.sql.SqlFieldSelector;
import org.lalelu.wiggins.selectors.sql.SqlSelector;

public class DeepDataObjectAssemblerTest {
	private SqlDeepDataObjectAssembler assembler;
	
	@Before
	public void setUp() {
		assembler = new SqlDeepDataObjectAssembler();
	}
	
	/**
	 * Test with a single request with no sub-request
	 */
	@Test
	public void testSimpleRequest() {
		SqlRequest<TestData> request = new SqlRequest<TestData>(TestData.class);
		request.addSelector(new SqlFieldSelector<Object>("", "", "setTestField", Object.class));
		
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
		SqlRequest<TestData> request = new SqlRequest<TestData>(TestData.class);
		request.addSelector(new SqlFieldSelector<Object>("", "", "setTestField", Object.class));
		
		SqlRequest<TestData> subRequest = new SqlRequest<TestData>(TestData.class);
		subRequest.addSelector(new SqlFieldSelector<Object>("", "", "setTestField", Object.class));
		request.addSubRequest("setSubData", subRequest);
		
		request.compileSelect();
		
		List<SqlSelector<?>> selectors = new LinkedList<SqlSelector<?>>();
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
		
		SqlRequest<TestData> request = new SqlRequest<TestData>(TestData.class);
		request.addSelector(new SqlFieldSelector<Object>("", "", "setTestField", Object.class));
		
		SqlRequest<TestData> prevRequest = request;
		for(int i = p; i < k; i++) {
			SqlRequest<TestData> subRequest = new SqlRequest<TestData>(TestData.class);
			subRequest.addSelector(new SqlFieldSelector<Object>("", "", "setTestField", Object.class));
			prevRequest.addSubRequest("setOtherSubData", subRequest);
			prevRequest = subRequest;
		}
		
		prevRequest = request;
		for(int i = 1; i < p; i++) {
			SqlRequest<TestData> subRequest = new SqlRequest<TestData>(TestData.class);
			subRequest.addSelector(new SqlFieldSelector<Object>("", "", "setTestField", Object.class));
			prevRequest.addSubRequest("setSubData", subRequest);
			prevRequest = subRequest;
		}
		
		request.compileSelect();
		
		List<SqlSelector<?>> selectors = new LinkedList<SqlSelector<?>>();
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
		
		List<SqlRequest<TestData>> list = new ArrayList<SqlRequest<TestData>>();
		
		SqlRequest<TestData> request = new SqlRequest<TestData>(TestData.class);
		request.addSelector(new SqlFieldSelector<Object>("", "", "setTestField", Object.class));
		list.add(request);
		
		SqlRequest<TestData> prevRequest = request;
		for(int i = 1; i < k; i++) {
			SqlRequest<TestData> subRequest = new SqlRequest<TestData>(TestData.class);
			subRequest.addSelector(new SqlFieldSelector<Object>("", "", "setTestField", Object.class));
			prevRequest.addSubRequest("setSubData", subRequest);
			prevRequest = subRequest;
		}
		
		request.compileSelect();
		
		List<SqlSelector<?>> selectors = new LinkedList<SqlSelector<?>>();
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
