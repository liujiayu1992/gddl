/*
 * 时间：2008-06-09
 * 作者：Qiuzw
 * 描述：增加了汽车衡检斤时输入的最大“扣吨”限制，通过系统参数实现
 */
package com.zhiren.project.cpicrmis.factory.jilxxcl.gonglxx.qichmz;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.SysInfo;

public class Baocts extends BasePage {

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		SysInfo si = new SysInfo();
		String maxKoud = si.getValue("汽车检斤最大扣吨量", "5");
		setMaxKoud(maxKoud);
	}

	private String _MaxKoud = "";

	public String getMaxKoud() {
		return _MaxKoud;
	}

	public void setMaxKoud(String MaxKoud) {
		_MaxKoud = MaxKoud;
	}

}
