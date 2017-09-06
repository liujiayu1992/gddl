package com.zhiren.pub.quanx;

import java.io.Serializable;

public class GroupBean implements Serializable {

	private static final long serialVersionUID = -7474668705325981306L;
	
	private boolean Selected;
	private long ID;
	private String GroupName;
	private String Remarks;
	public String getGroupName() {
		return GroupName;
	}
	public void setGroupName(String groupName) {
		GroupName = groupName;
	}
	public long getID() {
		return ID;
	}
	public void setID(long id) {
		ID = id;
	}
	public String getRemarks() {
		return Remarks;
	}
	public void setRemarks(String remarks) {
		Remarks = remarks;
	}
	public boolean isSelected() {
		return Selected;
	}
	public void setSelected(boolean selected) {
		Selected = selected;
	}
	
	public GroupBean(boolean selected, long id, String groupName, String remarks) {
		super();
		Selected = selected;
		ID = id;
		GroupName = groupName;
		Remarks = remarks;
	}
	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final GroupBean other = (GroupBean) obj;
		if (GroupName == null) {
			if (other.GroupName != null)
				return false;
		} else if (!GroupName.equals(other.GroupName))
			return false;
		if (ID != other.ID)
			return false;
		if (Remarks == null) {
			if (other.Remarks != null)
				return false;
		} else if (!Remarks.equals(other.Remarks))
			return false;
		if (Selected != other.Selected)
			return false;
		return true;
	}
	
	
}
