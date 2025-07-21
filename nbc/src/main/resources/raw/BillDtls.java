package raw;

import java.util.List;
import jakarta.json.bind.annotation.JsonbProperty;

public class BillDtls{

	@JsonbProperty("Owner")
	private String owner;

	@JsonbProperty("ReservedField3")
	private String reservedField3;

	@JsonbProperty("ReservedField2")
	private String reservedField2;

	@JsonbProperty("Meter")
	private String meter;

	@JsonbProperty("ReservedField1")
	private String reservedField1;

	@JsonbProperty("ExpectedDeductions")
	private List<ExpectedDeductionsItem> expectedDeductions;

	public void setOwner(String owner){
		this.owner = owner;
	}

	public String getOwner(){
		return owner;
	}

	public void setReservedField3(String reservedField3){
		this.reservedField3 = reservedField3;
	}

	public String getReservedField3(){
		return reservedField3;
	}

	public void setReservedField2(String reservedField2){
		this.reservedField2 = reservedField2;
	}

	public String getReservedField2(){
		return reservedField2;
	}

	public void setMeter(String meter){
		this.meter = meter;
	}

	public String getMeter(){
		return meter;
	}

	public void setReservedField1(String reservedField1){
		this.reservedField1 = reservedField1;
	}

	public String getReservedField1(){
		return reservedField1;
	}

	public void setExpectedDeductions(List<ExpectedDeductionsItem> expectedDeductions){
		this.expectedDeductions = expectedDeductions;
	}

	public List<ExpectedDeductionsItem> getExpectedDeductions(){
		return expectedDeductions;
	}

	@Override
 	public String toString(){
		return 
			"{" +
			"owner = '" + owner + '\'' + 
			",reservedField3 = '" + reservedField3 + '\'' + 
			",reservedField2 = '" + reservedField2 + '\'' + 
			",meter = '" + meter + '\'' + 
			",reservedField1 = '" + reservedField1 + '\'' + 
			",expectedDeductions = '" + expectedDeductions + '\'' + 
			"}";
		}
}