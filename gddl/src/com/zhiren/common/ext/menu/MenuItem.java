package com.zhiren.common.ext.menu;

import java.io.Serializable;

import com.zhiren.common.ext.Component;

public abstract class MenuItem extends Component implements Serializable {
	public abstract String getScript();
}
