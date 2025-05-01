package model;

public class Staff {
	private Long staffID;
	private String staffName;

	public Staff(Long staffID, String staffName) {
		super();
		this.staffID = staffID;
		this.staffName = staffName;
	}
	public Long getStaffID() {
		return staffID;
	}
	public void setStaffID(Long staffID) {
		this.staffID = staffID;
	}
	public String getStaffName() {
		return staffName;
	}
	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}


}
