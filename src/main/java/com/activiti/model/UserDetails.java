package com.activiti.model;

import com.activiti.domain.idm.UserStatus;

public class UserDetails {
	private String email; 
	private String firstName; 
	private String lastName; 
	private String password; 
	private String company; 
	private UserStatus initialStatus; 
	private String accountType; 
	private String tenantId;
	private String userGroup;
	
	public String getUserGroup() {
		return userGroup;
	}
	public void setUserGroup(String userGroup) {
		this.userGroup = userGroup;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public UserStatus getInitialStatus() {
		return initialStatus;
	}
	public void setInitialStatus(UserStatus initialStatus) {
		this.initialStatus = initialStatus;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	
}
