package org.lalelu.wiggins.data.assembler;


public class TestData {
	public Object testField;
	public TestData subData;
	public TestData otherSubData;
	
	public Object getTestField() {
		return testField;
	}
	public void setTestField(Object testField) {
		this.testField = testField;
	}
	public TestData getSubData() {
		return subData;
	}
	public void setSubData(TestData subData) {
		this.subData = subData;
	}
	public TestData getOtherSubData() {
		return otherSubData;
	}
	public void setOtherSubData(TestData otherSubData) {
		this.otherSubData = otherSubData;
	}
}
