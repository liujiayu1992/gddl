package com.zhiren.jt.het.diancgmht;

import java.io.Serializable;

/**
 * @author yinjm
 * 类名：杂费信息Bean
 */

public class Zafxxbean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int xuh;
	
	private long id;
	
	private String hetb_id;
	
	private String item_id;
	
	private String diancjszf;

	public String getDiancjszf() {
		return diancjszf;
	}

	public void setDiancjszf(String diancjszf) {
		this.diancjszf = diancjszf;
	}

	public String getHetb_id() {
		return hetb_id;
	}

	public void setHetb_id(String hetb_id) {
		this.hetb_id = hetb_id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getItem_id() {
		return item_id;
	}

	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}

	public int getXuh() {
		return xuh;
	}

	public void setXuh(int xuh) {
		this.xuh = xuh;
	}
	
	public Zafxxbean(int xuh) {
		super();
		this.xuh = xuh;
	}

	public Zafxxbean(int xuh, long id, String item_id, String diancjszf) {
		super();
		this.xuh = xuh;
		this.id = id;
		this.item_id = item_id;
		this.diancjszf = diancjszf;
	}

}
