package com.zhiren.common.ext.form;

public class LovComboBox extends Field {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3254630685999718603L;
	
	private boolean editable;	
	private boolean forceSelection;
	private boolean lazyRender;
	private int listWidth;
	private String transform;
	private boolean typeAhead;
	private String triggerAction ;
	
	public LovComboBox() {
		super();
		typeAhead = true;
		triggerAction = "all";
		forceSelection = true;
		lazyRender = true;
	}
	
	public boolean isEditable() {
		return editable;
	}

	public boolean isForceSelection() {
		return forceSelection;
	}

	public boolean isLazyRender() {
		return lazyRender;
	}

	public int getListWidth() {
		return listWidth;
	}

	public String getTransform() {
		return transform==null?"":transform;
	}

	public String getTriggerAction() {
		return triggerAction==null?"":triggerAction;
	}

	public boolean isTypeAhead() {
		return typeAhead;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	
	public void setForceSelection(boolean forceSelection) {
		this.forceSelection = forceSelection;
	}
	
	public void setLazyRender(boolean lazyRender) {
		this.lazyRender = lazyRender;
	}
	
	public void setListWidth(int listWidth) {
		this.listWidth = listWidth;
	}
	
	public void setTransform(String transform) {
		this.transform = transform;
	}
	
	
	/**
	 * @param triggerAction 'all' 'query' default all
	 */
	public void setTriggerAction(String triggerAction) {
		this.triggerAction = triggerAction;
	}
	
	public void setTypeAhead(boolean typeAhead) {
		this.typeAhead = typeAhead;
	}
	
	public String getScript() {
		// TODO 自动生成方法存根
		StringBuffer sb = new StringBuffer();
		if(!"".equals(getId())) {
			sb.append(getId()).append("=");
		}
//		sb.append(id==null?"":id + " = ");
		sb.append("new Ext.ux.form.LovCombo({\n");
		
		if(isAllowBlank()) {
			sb.append("\tallowBlank:").append(isAllowBlank()).append(",\n");
		}
		if(!"".equals(getFieldLabel())) {
			sb.append("\tfieldLabel: '").append(getFieldLabel()).append("',\n");
		}
		if(!"".equals(getInputType())) {
			sb.append("\tinputType: '").append(getInputType()).append("',\n");
		}
		if(getWidth()!=null) {
			sb.append("\twidth:").append(getWidth()).append(",\n");
		}
		if(getListWidth()>0) {
			sb.append("\tlistWidth:").append(getListWidth()).append(",\n");
		}
		if(isSelectOnFocus()) {
			sb.append("\tselectOnFocus:").append(isSelectOnFocus()).append(",\n");
		}
		if(!"".equals(getTransform())) {
			sb.append("\ttransform:'").append(getTransform()).append("',\n");
		}
		if(isLazyRender()) {
			sb.append("\tlazyRender:").append(isLazyRender()).append(",\n");
		}
		if(!"".equals(getTriggerAction())) {
			sb.append("\ttriggerAction:'").append(getTriggerAction()).append("',\n");
		}
		if(isTypeAhead()) {
			sb.append("\ttypeAhead:").append(isTypeAhead()).append(",\n");
		}
		if(isForceSelection()) {
			sb.append("\tforceSelection:").append(isForceSelection()).append(",\n");
		}
		if(!"".equals(getListeners())) {
			sb.append("\tlisteners:{").append(getListeners()).append("},\n");
		}
		if(!isEditable()) {
			sb.append("\teditable:").append(isEditable()).append(",\n");
		}
		if(!"".equals(getValue())) {
			sb.append("\tvalue: '").append(getValue()).append("',\n");
		}
		sb.deleteCharAt(sb.length()-2);
		sb.append("})");
//		sb.append(fieldLabel==null?"":"\tfieldLabel: '"+fieldLabel+"',\n");
//		sb.append(inputType==null?"":"\tinputType: '"+inputType+"',\n");
//		sb.append(width == 0?"":"\twidth:"+width+",\n");
//		sb.append(listWidth==0?"":"\tlistWidth:"+listWidth+",\n");
//		sb.append("\tselectOnFocus:").append(selectOnFocus).append(",\n");
//		sb.append(transform==null?"":"\ttransform:'"+transform+"',\n");
//		sb.append("\tlazyRender:").append(lazyRender).append(",\n");
//		sb.append("\ttriggerAction:'").append(triggerAction).append("',\n");
//		sb.append("\ttypeAhead:").append(typeAhead).append(",\n");
//		sb.append("\tforceSelection:").append(forceSelection).append(",\n");
//		sb.append(this.listeners==null?"":"\tlisteners:{"+listeners+"},\n");
//		sb.append("\teditable:").append(editable).append(",\n");
		//sb.append("\tallowBlank:").append(allowBlank).append("})\n");
		return sb.toString();
	} 
}
