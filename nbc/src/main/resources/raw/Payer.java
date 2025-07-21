package raw;

import jakarta.json.bind.annotation.JsonbProperty;

public class Payer{

	@JsonbProperty("phone")
	private String phone;

	@JsonbProperty("fullName")
	private String fullName;

	@JsonbProperty("language")
	private String language;

	@JsonbProperty("branch")
	private String branch;

	@JsonbProperty("email")
	private String email;

	@JsonbProperty("account")
	private String account;

	public void setPhone(String phone){
		this.phone = phone;
	}

	public String getPhone(){
		return phone;
	}

	public void setFullName(String fullName){
		this.fullName = fullName;
	}

	public String getFullName(){
		return fullName;
	}

	public void setLanguage(String language){
		this.language = language;
	}

	public String getLanguage(){
		return language;
	}

	public void setBranch(String branch){
		this.branch = branch;
	}

	public String getBranch(){
		return branch;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setAccount(String account){
		this.account = account;
	}

	public String getAccount(){
		return account;
	}

	@Override
 	public String toString(){
		return 
			"Payer{" + 
			"phone = '" + phone + '\'' + 
			",fullName = '" + fullName + '\'' + 
			",language = '" + language + '\'' + 
			",branch = '" + branch + '\'' + 
			",email = '" + email + '\'' + 
			",account = '" + account + '\'' + 
			"}";
		}
}