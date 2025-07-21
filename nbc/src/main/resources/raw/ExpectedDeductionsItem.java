package raw;

import jakarta.json.bind.annotation.JsonbProperty;

public class ExpectedDeductionsItem{

	@JsonbProperty("DebtType")
	private String debtType;

	@JsonbProperty("DebtName")
	private String debtName;

	@JsonbProperty("DebtRate")
	private String debtRate;

	@JsonbProperty("DebtBalance")
	private String debtBalance;

	public void setDebtType(String debtType){
		this.debtType = debtType;
	}

	public String getDebtType(){
		return debtType;
	}

	public void setDebtName(String debtName){
		this.debtName = debtName;
	}

	public String getDebtName(){
		return debtName;
	}

	public void setDebtRate(String debtRate){
		this.debtRate = debtRate;
	}

	public String getDebtRate(){
		return debtRate;
	}

	public void setDebtBalance(String debtBalance){
		this.debtBalance = debtBalance;
	}

	public String getDebtBalance(){
		return debtBalance;
	}

	@Override
 	public String toString(){
		return 
			"ExpectedDeductionsItem{" + 
			"debtType = '" + debtType + '\'' + 
			",debtName = '" + debtName + '\'' + 
			",debtRate = '" + debtRate + '\'' + 
			",debtBalance = '" + debtBalance + '\'' + 
			"}";
		}
}