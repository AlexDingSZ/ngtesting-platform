package com.ngtesting.platform.vo;


public class UserVo extends BaseVo {
	private static final long serialVersionUID = 1988353599211843484L;
	private String name;
	private String email;
//	private String password;
    private String phone;
    private String avatar;
    
    private String token;
    private Long defaultOrgId;
    private Long defaultProjectId;
    
    private Boolean selected;
    private Boolean selecting;
    
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
//	public String getPassword() {
//		return password;
//	}
//	public void setPassword(String password) {
//		this.password = password;
//	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public Boolean getSelected() {
		return selected;
	}
	public void setSelected(Boolean selected) {
		this.selected = selected;
	}
	public Boolean getSelecting() {
		return selecting;
	}
	public void setSelecting(Boolean selecting) {
		this.selecting = selecting;
	}
	public Long getDefaultOrgId() {
		return defaultOrgId;
	}
	public void setDefaultOrgId(Long defaultOrgId) {
		this.defaultOrgId = defaultOrgId;
	}
	public Long getDefaultProjectId() {
		return defaultProjectId;
	}
	public void setDefaultProjectId(Long defaultProjectId) {
		this.defaultProjectId = defaultProjectId;
	}

}
