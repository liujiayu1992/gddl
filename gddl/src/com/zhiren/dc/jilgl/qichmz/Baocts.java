/*
 * ʱ�䣺2008-06-09
 * ���ߣ�Qiuzw
 * ��������������������ʱ�������󡰿۶֡����ƣ�ͨ��ϵͳ����ʵ��
 */
package com.zhiren.project.cpicrmis.factory.jilxxcl.gonglxx.qichmz;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.SysInfo;

public class Baocts extends BasePage {

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		SysInfo si = new SysInfo();
		String maxKoud = si.getValue("����������۶���", "5");
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
