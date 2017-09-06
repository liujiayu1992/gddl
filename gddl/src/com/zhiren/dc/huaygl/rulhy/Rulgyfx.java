package com.zhiren.dc.huaygl.rulhy;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.main.Visit;

public class Rulgyfx extends BasePage {

	private boolean _Rulrqchange = false;

	private static Date Rulrq = new Date();

	public Date getRulrq() {
		return Rulrq;
	}

	public void setRulrq(Date _value) {
		if (Rulrq.equals(_value)) {
			_Rulrqchange = false;
		} else {
			Rulrq = _value;
			_Rulrqchange = true;
		}

	}

	private boolean _Huayrqchange = false;

	private static Date Huayrq = new Date();

	public Date getHuayrq() {
		return Huayrq;
	}

	public void setHuayrq(Date _value) {
		if (Huayrq.equals(_value)) {
			_Rulrqchange = false;
		} else {
			Rulrq = _value;
			_Rulrqchange = true;
		}
	}

	//	
	// private boolean _Rulbzchange = false;
	// private static String Rulbz ;//入炉班值
	// public String getRulbz()
	// {
	// return Rulbz;
	// }
	// public void setRulbz(String _value)
	// {
	// Rulbz=_value;
	// _Rulbzchange=true;
	// }

	// private boolean _Qumwchange = false;
	// private static String Qumw ;//取煤位
	// public String getQumw()
	// {
	// return Qumw;
	// }
	// public void setQumw(String value)
	// {
	// Qumw = value;
	// _Qumwchange = true;
	// }

	private String Qing;// 氢

	// private String Liu;//硫

	public String getQing() {
		return Qing;
	}

	public void setQing(String qing) {
		Qing = qing;
	}

	// public String getLiu()
	// {
	// return Liu;
	// }
	// public void setLiu(String liu)
	// {
	// Liu = liu;
	// }

	// ///////////////代替bean的代码////////////////////
	private long Mt_id1 = -1;// 全水分id1

	private long Mt_id2 = -1;// 全水分id2

	private long Mt_id3 = -1;// 全水分id3

	private long Mt_id4 = -1;// 全水分id4

	private double Mt_clph1=0.0D;// a 称量瓶号

	private double Mt_clpzl1;// 称量瓶质量a

	private double Mt_clpzlsyzl1;// 称量瓶质量+式样质量

	private double Mt_syzl1;// 式样质量

	private double Mt_hhzzl1;// 烘后总质量

	private double Mt_jcxsyhzzl1;// 检查性实验后总质量

	private double Mt_qsf1;// 全水分

	private double Mt_pjz = 0.0D;//

	private String Mt_sh1 = "";// 审核

	private String Mt_sh2 = "";// 审核

	private String Mt_sh3 = "";// 审核

	private String Mt_sh4 = "";// 审核

	private long Mad_id1 = -1;// 水分id1

	private long Mad_id2 = -1;// 水分id2

	private long Mad_id3 = -1;// 水分id3

	private long Mad_id4 = -1;// 水分id4

	private double Mad_clph1;// 称量瓶号

	private double Mad_clpzl1;// 称量瓶质量

	private double Mad_clpzlsyzl1;// 称量瓶质量+式样质量

	private double Mad_syzl1;// 式样质量

	private double Mad_hhzzl1;// 烘后总质量

	private double Mad_jcxsyhzzl1;// 检查性实验后总质量

	private double Mad_qsf1;// 全水分

	private double Mad_pjz = 0.0D;// 平均值

	private String Mad_sh1 = "";// 审核

	private String Mad_sh2 = "";// 审核

	private String Mad_sh3 = "";// 审核

	private String Mad_sh4 = "";// 审核

	private long Vad_id1 = -1;// 挥发分id1

	private long Vad_id2 = -1;// 挥发分id2

	private long Vad_id3 = -1;// 挥发分id3

	private long Vad_id4 = -1;// 挥发分id4

	private double Vad_ggh1;// 坩埚号

	private double Vad_ggzl1;// 坩埚质量

	private double Vad_ggzlsyzl1;// 坩埚质量+试样质量

	private double Vad_syzl1;// 试样质量

	private double Vad_jrhzzl1;// 加热后总质量

	private double Vad_myjrhjszl1;// 煤样加热后减少的质量

	private double Vad_hffqsf1;// 挥发分Vad=m/m*00-Mad(%)

	private double Vad_hff1;// 挥发分

	private double Vad_pjz = 0.0D; // 平均值

	private String Vad_sh1 = "";// 审核

	private String Vad_sh2 = "";// 审核

	private String Vad_sh3 = "";// 审核

	private String Vad_sh4 = "";// 审核

	private long Aad_id1 = -1;// 灰分id1

	private long Aad_id2 = -1;// 灰分id2

	private long Aad_id3 = -1;// 灰分id3

	private long Aad_id4 = -1;// 灰分id4

	private double Aad_hmh1;// 灰皿号

	private double Aad_hmzl1;// 灰皿质量

	private double Aad_hmzlsyzl1;// 灰皿质量+试样质量

	private double Aad_syzl1;// 试样质量

	private double Aad_jrhzzl1;// 加热后总质量

	private double Aad_jcxsyhzzl1;// 检查性试验后总质量

	private double Aad_clwdzl1;// 残留物的质量

	private double Aad_hf1;// 灰分Aad

	private double Aad_pjz = 0.0D;// 平均值

	private String Aad_sh1 = "";// 审核

	private String Aad_sh2 = "";// 审核

	private String Aad_sh3 = "";// 审核

	private String Aad_sh4 = "";// 审核

	private long Ql_id1 = -1;// 全硫id1

	private long Ql_id2 = -1;// 全硫id2

	private long Ql_id3 = -1;// 全硫id3

	private long Ql_id4 = -1;// 全硫id4

	private double Ql_qmh1;// 器皿号

	private double Ql_qmzl1;// 器皿质量

	private double Ql_qmzlsyzl1;// 器皿质量+式样质量

	private double Ql_syzl1;// 式样质量

	private double Ql_stad1;// 全硫St,ad

	private double Ql_std1;// 全硫St,d

	private double Ql_pjz = 0.0D;// 平均值

	private String Ql_sh1 = "";// 审核

	private String Ql_sh2 = "";// 审核

	private String Ql_sh3 = "";// 审核

	private String Ql_sh4 = "";// 审核

	private long Frl_id1 = -1;// 发热量id1

	private long Frl_id2 = -1;// 发热量id2

	private long Frl_id3 = -1;// 发热量id3

	private long Frl_id4 = -1;// 发热量id4

	private double Frl_ggzl1;// 坩埚质量

	private double Frl_ggzlsyzl1;// 坩埚质量+式样质量

	private double Frl_syzl1;// 式样质量

	private double Frl_qbad1;// Qb,ad

	private double Frl_qgrad1;// Qgr,ad

	private double Frl_qnetar1;// Qnet,ar

	private double Frl_pjz = 0.0D;// 平均值

	private String Frl_sh1 = "";// 审核

	private String Frl_sh2 = "";// 审核

	private String Frl_sh3 = "";// 审核

	private String Frl_sh4 = "";// 审核

	// /////////////////////////////////////////////1111111

	private double Mt_clph2;// 称量瓶号

	private double Mt_clpzl2;// 称量瓶质量

	private double Mt_clpzlsyzl2;// 称量瓶质量+式样质量

	private double Mt_syzl2;// 式样质量

	private double Mt_hhzzl2;// 烘后总质量

	private double Mt_jcxsyhzzl2;// 检查性实验后总质量

	private double Mt_qsf2;// 全水分

	private double Mad_clph2;// 称量瓶号

	private double Mad_clpzl2;// 称量瓶质量

	private double Mad_clpzlsyzl2;// 称量瓶质量+式样质量

	private double Mad_syzl2;// 式样质量

	private double Mad_hhzzl2;// 烘后总质量

	private double Mad_jcxsyhzzl2;// 检查性实验后总质量

	private double Mad_qsf2;// 全水分

	private double Vad_ggh2;// 坩埚号

	private double Vad_ggzl2;// 坩埚质量

	private double Vad_ggzlsyzl2;// 坩埚质量+试样质量

	private double Vad_syzl2;// 试样质量

	private double Vad_jrhzzl2;// 加热后总质量

	private double Vad_myjrhjszl2;// 煤样加热后减少的质量

	private double Vad_hffqsf2;// 挥发分Vad=m/m*00-Mad(%)

	private double Vad_hff2;// 挥发分

	private double Aad_hmh2;// 灰皿号

	private double Aad_hmzl2;// 灰皿质量

	private double Aad_hmzlsyzl2;// 灰皿质量+试样质量

	private double Aad_syzl2;// 试样质量

	private double Aad_jrhzzl2;// 加热后总质量

	private double Aad_jcxsyhzzl2;// 检查性试验后总质量

	private double Aad_clwdzl2;// 残留物的质量

	private double Aad_hf2;// 灰分Aad

	private double Ql_qmh2;// 器皿号

	private double Ql_qmzl2;// 器皿质量

	private double Ql_qmzlsyzl2;// 器皿质量+式样质量

	private double Ql_syzl2;// 式样质量

	private double Ql_stad2;// 全硫St,ad

	private double Ql_std2;// 全硫St,d

	private double Frl_ggzl2;// 坩埚质量

	private double Frl_ggzlsyzl2;// 坩埚质量+式样质量

	private double Frl_syzl2;// 式样质量

	private double Frl_qbad2;// Qb,ad

	private double Frl_qgrad2;// Qgr,ad

	private double Frl_qnetar2;// Qnet,ar

	// //////////////////////////////////222222222

	private double Mt_clph3;// 称量瓶号

	private double Mt_clpzl3;// 称量瓶质量

	private double Mt_clpzlsyzl3;// 称量瓶质量+式样质量

	private double Mt_syzl3;// 式样质量

	private double Mt_hhzzl3;// 烘后总质量

	private double Mt_jcxsyhzzl3;// 检查性实验后总质量

	private double Mt_qsf3;// 全水分

	private double Mad_clph3;// 称量瓶号

	private double Mad_clpzl3;// 称量瓶质量

	private double Mad_clpzlsyzl3;// 称量瓶质量+式样质量

	private double Mad_syzl3;// 式样质量

	private double Mad_hhzzl3;// 烘后总质量

	private double Mad_jcxsyhzzl3;// 检查性实验后总质量

	private double Mad_qsf3;// 全水分

	private double Vad_ggh3;// 坩埚号

	private double Vad_ggzl3;// 坩埚质量

	private double Vad_ggzlsyzl3;// 坩埚质量+试样质量

	private double Vad_syzl3;// 试样质量

	private double Vad_jrhzzl3;// 加热后总质量

	private double Vad_myjrhjszl3;// 煤样加热后减少的质量

	private double Vad_hffqsf3;// 挥发分Vad=m/m*00-Mad(%)

	private double Vad_hff3;// 挥发分

	private double Aad_hmh3;// 灰皿号

	private double Aad_hmzl3;// 灰皿质量

	private double Aad_hmzlsyzl3;// 灰皿质量+试样质量

	private double Aad_syzl3;// 试样质量

	private double Aad_jrhzzl3;// 加热后总质量

	private double Aad_jcxsyhzzl3;// 检查性试验后总质量

	private double Aad_clwdzl3;// 残留物的质量

	private double Aad_hf3;// 灰分Aad

	private double Ql_qmh3;// 器皿号

	private double Ql_qmzl3;// 器皿质量

	private double Ql_qmzlsyzl3;// 器皿质量+式样质量

	private double Ql_syzl3;// 式样质量

	private double Ql_stad3;// 全硫St,ad

	private double Ql_std3;// 全硫St,d

	private double Frl_ggzl3;// 坩埚质量

	private double Frl_ggzlsyzl3;// 坩埚质量+式样质量

	private double Frl_syzl3;// 式样质量

	private double Frl_qbad3;// Qb,ad

	private double Frl_qgrad3;// Qgr,ad

	private double Frl_qnetar3;// Qnet,ar

	// /////////////////////////////////333333333333

	private double Mt_clph4;// 称量瓶号

	private double Mt_clpzl4;// 称量瓶质量

	private double Mt_clpzlsyzl4;// 称量瓶质量+式样质量

	private double Mt_syzl4;// 式样质量

	private double Mt_hhzzl4;// 烘后总质量

	private double Mt_jcxsyhzzl4;// 检查性实验后总质量

	private double Mt_qsf4;// 全水分

	private double Mad_clph4;// 称量瓶号

	private double Mad_clpzl4;// 称量瓶质量

	private double Mad_clpzlsyzl4;// 称量瓶质量+式样质量

	private double Mad_syzl4;// 式样质量

	private double Mad_hhzzl4;// 烘后总质量

	private double Mad_jcxsyhzzl4;// 检查性实验后总质量

	private double Mad_qsf4;// 全水分

	private double Vad_ggh4;// 坩埚号

	private double Vad_ggzl4;// 坩埚质量

	private double Vad_ggzlsyzl4;// 坩埚质量+试样质量

	private double Vad_syzl4;// 试样质量

	private double Vad_jrhzzl4;// 加热后总质量

	private double Vad_myjrhjszl4;// 煤样加热后减少的质量

	private double Vad_hffqsf4;// 挥发分Vad=m/m*00-Mad(%)

	private double Vad_hff4;// 挥发分

	private double Aad_hmh4;// 灰皿号

	private double Aad_hmzl4;// 灰皿质量

	private double Aad_hmzlsyzl4;// 灰皿质量+试样质量

	private double Aad_syzl4;// 试样质量

	private double Aad_jrhzzl4;// 加热后总质量

	private double Aad_jcxsyhzzl4;// 检查性试验后总质量

	private double Aad_clwdzl4;// 残留物的质量

	private double Aad_hf4;// 灰分Aad

	private double Ql_qmh4;// 器皿号

	private double Ql_qmzl4;// 器皿质量

	private double Ql_qmzlsyzl4;// 器皿质量+式样质量

	private double Ql_syzl4;// 式样质量

	private double Ql_stad4;// 全硫St,ad

	private double Ql_std4;// 全硫St,d

	private double Frl_ggzl4;// 坩埚质量

	private double Frl_ggzlsyzl4;// 坩埚质量+式样质量

	private double Frl_syzl4;// 式样质量

	private double Frl_qbad4;// Qb,ad

	private double Frl_qgrad4;// Qgr,ad

	private double Frl_qnetar4;// Qnet,ar

	public double getAad_clwdzl1() {
		return Aad_clwdzl1;
	}

	public void setAad_clwdzl1(double aad_clwdzl1) {
		Aad_clwdzl1 = aad_clwdzl1;
	}

	public double getAad_clwdzl2() {
		return Aad_clwdzl2;
	}

	public void setAad_clwdzl2(double aad_clwdzl2) {
		Aad_clwdzl2 = aad_clwdzl2;
	}

	public double getAad_clwdzl3() {
		return Aad_clwdzl3;
	}

	public void setAad_clwdzl3(double aad_clwdzl3) {
		Aad_clwdzl3 = aad_clwdzl3;
	}

	public double getAad_clwdzl4() {
		return Aad_clwdzl4;
	}

	public void setAad_clwdzl4(double aad_clwdzl4) {
		Aad_clwdzl4 = aad_clwdzl4;
	}

	public double getAad_hf1() {
		return Aad_hf1;
	}

	public void setAad_hf1(double aad_hf1) {
		Aad_hf1 = aad_hf1;
	}

	public double getAad_hf2() {
		return Aad_hf2;
	}

	public void setAad_hf2(double aad_hf2) {
		Aad_hf2 = aad_hf2;
	}

	public double getAad_hf3() {
		return Aad_hf3;
	}

	public void setAad_hf3(double aad_hf3) {
		Aad_hf3 = aad_hf3;
	}

	public double getAad_hf4() {
		return Aad_hf4;
	}

	public void setAad_hf4(double aad_hf4) {
		Aad_hf4 = aad_hf4;
	}

	public double getAad_hmh1() {
		return Aad_hmh1;
	}

	public void setAad_hmh1(double aad_hmh1) {
		Aad_hmh1 = aad_hmh1;
	}

	public double getAad_hmh2() {
		return Aad_hmh2;
	}

	public void setAad_hmh2(double aad_hmh2) {
		Aad_hmh2 = aad_hmh2;
	}

	public double getAad_hmh3() {
		return Aad_hmh3;
	}

	public void setAad_hmh3(double aad_hmh3) {
		Aad_hmh3 = aad_hmh3;
	}

	public double getAad_hmh4() {
		return Aad_hmh4;
	}

	public void setAad_hmh4(double aad_hmh4) {
		Aad_hmh4 = aad_hmh4;
	}

	public double getAad_hmzl1() {
		return Aad_hmzl1;
	}

	public void setAad_hmzl1(double aad_hmzl1) {
		Aad_hmzl1 = aad_hmzl1;
	}

	public double getAad_hmzl2() {
		return Aad_hmzl2;
	}

	public void setAad_hmzl2(double aad_hmzl2) {
		Aad_hmzl2 = aad_hmzl2;
	}

	public double getAad_hmzl3() {
		return Aad_hmzl3;
	}

	public void setAad_hmzl3(double aad_hmzl3) {
		Aad_hmzl3 = aad_hmzl3;
	}

	public double getAad_hmzl4() {
		return Aad_hmzl4;
	}

	public void setAad_hmzl4(double aad_hmzl4) {
		Aad_hmzl4 = aad_hmzl4;
	}

	public double getAad_hmzlsyzl1() {
		return Aad_hmzlsyzl1;
	}

	public void setAad_hmzlsyzl1(double aad_hmzlsyzl1) {
		Aad_hmzlsyzl1 = aad_hmzlsyzl1;
	}

	public double getAad_hmzlsyzl2() {
		return Aad_hmzlsyzl2;
	}

	public void setAad_hmzlsyzl2(double aad_hmzlsyzl2) {
		Aad_hmzlsyzl2 = aad_hmzlsyzl2;
	}

	public double getAad_hmzlsyzl3() {
		return Aad_hmzlsyzl3;
	}

	public void setAad_hmzlsyzl3(double aad_hmzlsyzl3) {
		Aad_hmzlsyzl3 = aad_hmzlsyzl3;
	}

	public double getAad_hmzlsyzl4() {
		return Aad_hmzlsyzl4;
	}

	public void setAad_hmzlsyzl4(double aad_hmzlsyzl4) {
		Aad_hmzlsyzl4 = aad_hmzlsyzl4;
	}

	public double getAad_jcxsyhzzl1() {
		return Aad_jcxsyhzzl1;
	}

	public void setAad_jcxsyhzzl1(double aad_jcxsyhzzl1) {
		Aad_jcxsyhzzl1 = aad_jcxsyhzzl1;
	}

	public double getAad_jcxsyhzzl2() {
		return Aad_jcxsyhzzl2;
	}

	public void setAad_jcxsyhzzl2(double aad_jcxsyhzzl2) {
		Aad_jcxsyhzzl2 = aad_jcxsyhzzl2;
	}

	public double getAad_jcxsyhzzl3() {
		return Aad_jcxsyhzzl3;
	}

	public void setAad_jcxsyhzzl3(double aad_jcxsyhzzl3) {
		Aad_jcxsyhzzl3 = aad_jcxsyhzzl3;
	}

	public double getAad_jcxsyhzzl4() {
		return Aad_jcxsyhzzl4;
	}

	public void setAad_jcxsyhzzl4(double aad_jcxsyhzzl4) {
		Aad_jcxsyhzzl4 = aad_jcxsyhzzl4;
	}

	public double getAad_jrhzzl1() {
		return Aad_jrhzzl1;
	}

	public void setAad_jrhzzl1(double aad_jrhzzl1) {
		Aad_jrhzzl1 = aad_jrhzzl1;
	}

	public double getAad_jrhzzl2() {
		return Aad_jrhzzl2;
	}

	public void setAad_jrhzzl2(double aad_jrhzzl2) {
		Aad_jrhzzl2 = aad_jrhzzl2;
	}

	public double getAad_jrhzzl3() {
		return Aad_jrhzzl3;
	}

	public void setAad_jrhzzl3(double aad_jrhzzl3) {
		Aad_jrhzzl3 = aad_jrhzzl3;
	}

	public double getAad_jrhzzl4() {
		return Aad_jrhzzl4;
	}

	public void setAad_jrhzzl4(double aad_jrhzzl4) {
		Aad_jrhzzl4 = aad_jrhzzl4;
	}

	public double getAad_syzl1() {
		return Aad_syzl1;
	}

	public void setAad_syzl1(double aad_syzl1) {
		Aad_syzl1 = aad_syzl1;
	}

	public double getAad_syzl2() {
		return Aad_syzl2;
	}

	public void setAad_syzl2(double aad_syzl2) {
		Aad_syzl2 = aad_syzl2;
	}

	public double getAad_syzl3() {
		return Aad_syzl3;
	}

	public void setAad_syzl3(double aad_syzl3) {
		Aad_syzl3 = aad_syzl3;
	}

	public double getAad_syzl4() {
		return Aad_syzl4;
	}

	public void setAad_syzl4(double aad_syzl4) {
		Aad_syzl4 = aad_syzl4;
	}

	public double getFrl_ggzl1() {
		return Frl_ggzl1;
	}

	public void setFrl_ggzl1(double frl_ggzl1) {
		Frl_ggzl1 = frl_ggzl1;
	}

	public double getFrl_ggzl2() {
		return Frl_ggzl2;
	}

	public void setFrl_ggzl2(double frl_ggzl2) {
		Frl_ggzl2 = frl_ggzl2;
	}

	public double getFrl_ggzl3() {
		return Frl_ggzl3;
	}

	public void setFrl_ggzl3(double frl_ggzl3) {
		Frl_ggzl3 = frl_ggzl3;
	}

	public double getFrl_ggzl4() {
		return Frl_ggzl4;
	}

	public void setFrl_ggzl4(double frl_ggzl4) {
		Frl_ggzl4 = frl_ggzl4;
	}

	public double getFrl_ggzlsyzl1() {
		return Frl_ggzlsyzl1;
	}

	public void setFrl_ggzlsyzl1(double frl_ggzlsyzl1) {
		Frl_ggzlsyzl1 = frl_ggzlsyzl1;
	}

	public double getFrl_ggzlsyzl2() {
		return Frl_ggzlsyzl2;
	}

	public void setFrl_ggzlsyzl2(double frl_ggzlsyzl2) {
		Frl_ggzlsyzl2 = frl_ggzlsyzl2;
	}

	public double getFrl_ggzlsyzl3() {
		return Frl_ggzlsyzl3;
	}

	public void setFrl_ggzlsyzl3(double frl_ggzlsyzl3) {
		Frl_ggzlsyzl3 = frl_ggzlsyzl3;
	}

	public double getFrl_ggzlsyzl4() {
		return Frl_ggzlsyzl4;
	}

	public void setFrl_ggzlsyzl4(double frl_ggzlsyzl4) {
		Frl_ggzlsyzl4 = frl_ggzlsyzl4;
	}

	public double getFrl_qbad1() {
		return Frl_qbad1;
	}

	public void setFrl_qbad1(double frl_qb1) {
		Frl_qbad1 = frl_qb1;
	}

	public double getFrl_qbad2() {
		return Frl_qbad2;
	}

	public void setFrl_qbad2(double frl_qb2) {
		Frl_qbad2 = frl_qb2;
	}

	public double getFrl_qbad3() {
		return Frl_qbad3;
	}

	public void setFrl_qbad3(double frl_qb3) {
		Frl_qbad3 = frl_qb3;
	}

	public double getFrl_qbad4() {
		return Frl_qbad4;
	}

	public void setFrl_qbad4(double frl_qb4) {
		Frl_qbad4 = frl_qb4;
	}

	public double getFrl_qgrad1() {
		return Frl_qgrad1;
	}

	public void setFrl_qgrad1(double frl_qgr1) {
		Frl_qgrad1 = frl_qgr1;
	}

	public double getFrl_qgrad2() {
		return Frl_qgrad2;
	}

	public void setFrl_qgrad2(double frl_qgr2) {
		Frl_qgrad2 = frl_qgr2;
	}

	public double getFrl_qgrad3() {
		return Frl_qgrad3;
	}

	public void setFrl_qgrad3(double frl_qgr3) {
		Frl_qgrad3 = frl_qgr3;
	}

	public double getFrl_qgrad4() {
		return Frl_qgrad4;
	}

	public void setFrl_qgrad4(double frl_qgr4) {
		Frl_qgrad4 = frl_qgr4;
	}

	public double getFrl_qnetar1() {
		return Frl_qnetar1;
	}

	public void setFrl_qnetar1(double frl_qnet1) {
		Frl_qnetar1 = frl_qnet1;
	}

	public double getFrl_qnetar2() {
		return Frl_qnetar2;
	}

	public void setFrl_qnetar2(double frl_qnet2) {
		Frl_qnetar2 = frl_qnet2;
	}

	public double getFrl_qnetar3() {
		return Frl_qnetar3;
	}

	public void setFrl_qnetar3(double frl_qnet3) {
		Frl_qnetar3 = frl_qnet3;
	}

	public double getFrl_qnetar4() {
		return Frl_qnetar4;
	}

	public void setFrl_qnetar4(double frl_qnet4) {
		Frl_qnetar4 = frl_qnet4;
	}

	public double getFrl_syzl1() {
		return Frl_syzl1;
	}

	public void setFrl_syzl1(double frl_syzl1) {
		Frl_syzl1 = frl_syzl1;
	}

	public double getFrl_syzl2() {
		return Frl_syzl2;
	}

	public void setFrl_syzl2(double frl_syzl2) {
		Frl_syzl2 = frl_syzl2;
	}

	public double getFrl_syzl3() {
		return Frl_syzl3;
	}

	public void setFrl_syzl3(double frl_syzl3) {
		Frl_syzl3 = frl_syzl3;
	}

	public double getFrl_syzl4() {
		return Frl_syzl4;
	}

	public void setFrl_syzl4(double frl_syzl4) {
		Frl_syzl4 = frl_syzl4;
	}

	public double getMad_hhzzl1() {
		return Mad_hhzzl1;
	}

	public void setMad_hhzzl1(double mad_hhzzl1) {
		Mad_hhzzl1 = mad_hhzzl1;
	}

	public double getMad_hhzzl2() {
		return Mad_hhzzl2;
	}

	public void setMad_hhzzl2(double mad_hhzzl2) {
		Mad_hhzzl2 = mad_hhzzl2;
	}

	public double getMad_hhzzl3() {
		return Mad_hhzzl3;
	}

	public void setMad_hhzzl3(double mad_hhzzl3) {
		Mad_hhzzl3 = mad_hhzzl3;
	}

	public double getMad_hhzzl4() {
		return Mad_hhzzl4;
	}

	public void setMad_hhzzl4(double mad_hhzzl4) {
		Mad_hhzzl4 = mad_hhzzl4;
	}

	public double getMad_jcxsyhzzl1() {
		return Mad_jcxsyhzzl1;
	}

	public void setMad_jcxsyhzzl1(double mad_jcxsyhzzl1) {
		Mad_jcxsyhzzl1 = mad_jcxsyhzzl1;
	}

	public double getMad_jcxsyhzzl2() {
		return Mad_jcxsyhzzl2;
	}

	public void setMad_jcxsyhzzl2(double mad_jcxsyhzzl2) {
		Mad_jcxsyhzzl2 = mad_jcxsyhzzl2;
	}

	public double getMad_jcxsyhzzl3() {
		return Mad_jcxsyhzzl3;
	}

	public void setMad_jcxsyhzzl3(double mad_jcxsyhzzl3) {
		Mad_jcxsyhzzl3 = mad_jcxsyhzzl3;
	}

	public double getMad_jcxsyhzzl4() {
		return Mad_jcxsyhzzl4;
	}

	public void setMad_jcxsyhzzl4(double mad_jcxsyhzzl4) {
		Mad_jcxsyhzzl4 = mad_jcxsyhzzl4;
	}

	public double getMad_clph1() {
		return Mad_clph1;
	}

	public void setMad_clph1(double mad_clph1) {
		Mad_clph1 = mad_clph1;
	}

	public double getMad_clph2() {
		return Mad_clph2;
	}

	public void setMad_clph2(double mad_clph2) {
		Mad_clph2 = mad_clph2;
	}

	public double getMad_clph3() {
		return Mad_clph3;
	}

	public void setMad_clph3(double mad_clph3) {
		Mad_clph3 = mad_clph3;
	}

	public double getMad_clph4() {
		return Mad_clph4;
	}

	public void setMad_clph4(double mad_clph4) {
		Mad_clph4 = mad_clph4;
	}

	public double getMad_clpzl1() {
		return Mad_clpzl1;
	}

	public void setMad_clpzl1(double mad_clpzl1) {
		Mad_clpzl1 = mad_clpzl1;
	}

	public double getMad_clpzl2() {
		return Mad_clpzl2;
	}

	public void setMad_clpzl2(double mad_clpzl2) {
		Mad_clpzl2 = mad_clpzl2;
	}

	public double getMad_clpzl3() {
		return Mad_clpzl3;
	}

	public void setMad_clpzl3(double mad_clpzl3) {
		Mad_clpzl3 = mad_clpzl3;
	}

	public double getMad_clpzl4() {
		return Mad_clpzl4;
	}

	public void setMad_clpzl4(double mad_clpzl4) {
		Mad_clpzl4 = mad_clpzl4;
	}

	public double getMad_clpzlsyzl1() {
		return Mad_clpzlsyzl1;
	}

	public void setMad_clpzlsyzl1(double mad_clpzlsyzl1) {
		Mad_clpzlsyzl1 = mad_clpzlsyzl1;
	}

	public double getMad_clpzlsyzl2() {
		return Mad_clpzlsyzl2;
	}

	public void setMad_clpzlsyzl2(double mad_clpzlsyzl2) {
		Mad_clpzlsyzl2 = mad_clpzlsyzl2;
	}

	public double getMad_clpzlsyzl3() {
		return Mad_clpzlsyzl3;
	}

	public void setMad_clpzlsyzl3(double mad_clpzlsyzl3) {
		Mad_clpzlsyzl3 = mad_clpzlsyzl3;
	}

	public double getMad_clpzlsyzl4() {
		return Mad_clpzlsyzl4;
	}

	public void setMad_clpzlsyzl4(double mad_clpzlsyzl4) {
		Mad_clpzlsyzl4 = mad_clpzlsyzl4;
	}

	public double getMad_qsf1() {
		return Mad_qsf1;
	}

	public void setMad_qsf1(double mad_qsf1) {
		Mad_qsf1 = mad_qsf1;
	}

	public double getMad_qsf2() {
		return Mad_qsf2;
	}

	public void setMad_qsf2(double mad_qsf2) {
		Mad_qsf2 = mad_qsf2;
	}

	public double getMad_qsf3() {
		return Mad_qsf3;
	}

	public void setMad_qsf3(double mad_qsf3) {
		Mad_qsf3 = mad_qsf3;
	}

	public double getMad_qsf4() {
		return Mad_qsf4;
	}

	public void setMad_qsf4(double mad_qsf4) {
		Mad_qsf4 = mad_qsf4;
	}

	public double getMad_syzl1() {
		return Mad_syzl1;
	}

	public void setMad_syzl1(double mad_syzl1) {
		Mad_syzl1 = mad_syzl1;
	}

	public double getMad_syzl2() {
		return Mad_syzl2;
	}

	public void setMad_syzl2(double mad_syzl2) {
		Mad_syzl2 = mad_syzl2;
	}

	public double getMad_syzl3() {
		return Mad_syzl3;
	}

	public void setMad_syzl3(double mad_syzl3) {
		Mad_syzl3 = mad_syzl3;
	}

	public double getMad_syzl4() {
		return Mad_syzl4;
	}

	public void setMad_syzl4(double mad_syzl4) {
		Mad_syzl4 = mad_syzl4;
	}

	public double getMt_hhzzl1() {
		return Mt_hhzzl1;
	}

	public void setMt_hhzzl1(double mt_hhzzl1) {
		Mt_hhzzl1 = mt_hhzzl1;
	}

	public double getMt_hhzzl2() {
		return Mt_hhzzl2;
	}

	public void setMt_hhzzl2(double mt_hhzzl2) {
		Mt_hhzzl2 = mt_hhzzl2;
	}

	public double getMt_hhzzl3() {
		return Mt_hhzzl3;
	}

	public void setMt_hhzzl3(double mt_hhzzl3) {
		Mt_hhzzl3 = mt_hhzzl3;
	}

	public double getMt_hhzzl4() {
		return Mt_hhzzl4;
	}

	public void setMt_hhzzl4(double mt_hhzzl4) {
		Mt_hhzzl4 = mt_hhzzl4;
	}

	public double getMt_jcxsyhzzl1() {
		return Mt_jcxsyhzzl1;
	}

	public void setMt_jcxsyhzzl1(double mt_jcxsyhzzl1) {
		Mt_jcxsyhzzl1 = mt_jcxsyhzzl1;
	}

	public double getMt_jcxsyhzzl2() {
		return Mt_jcxsyhzzl2;
	}

	public void setMt_jcxsyhzzl2(double mt_jcxsyhzzl2) {
		Mt_jcxsyhzzl2 = mt_jcxsyhzzl2;
	}

	public double getMt_jcxsyhzzl3() {
		return Mt_jcxsyhzzl3;
	}

	public void setMt_jcxsyhzzl3(double mt_jcxsyhzzl3) {
		Mt_jcxsyhzzl3 = mt_jcxsyhzzl3;
	}

	public double getMt_jcxsyhzzl4() {
		return Mt_jcxsyhzzl4;
	}

	public void setMt_jcxsyhzzl4(double mt_jcxsyhzzl4) {
		Mt_jcxsyhzzl4 = mt_jcxsyhzzl4;
	}

	public double getMt_clph1() {
		return Mt_clph1;
	}

	public void setMt_clph1(double mt_clph1) {
		Mt_clph1 = mt_clph1;
	}

	public double getMt_clph2() {
		return Mt_clph2;
	}

	public void setMt_clph2(double mt_clph2) {
		Mt_clph2 = mt_clph2;
	}

	public double getMt_clph3() {
		return Mt_clph3;
	}

	public void setMt_clph3(double mt_clph3) {
		Mt_clph3 = mt_clph3;
	}

	public double getMt_clph4() {
		return Mt_clph4;
	}

	public void setMt_clph4(double mt_clph4) {
		Mt_clph4 = mt_clph4;
	}

	public double getMt_clpzl1() {
		return Mt_clpzl1;
	}

	public void setMt_clpzl1(double mt_clpzl1) {
		Mt_clpzl1 = mt_clpzl1;
	}

	public double getMt_clpzl2() {
		return Mt_clpzl2;
	}

	public void setMt_clpzl2(double mt_clpzl2) {
		Mt_clpzl2 = mt_clpzl2;
	}

	public double getMt_clpzl3() {
		return Mt_clpzl3;
	}

	public void setMt_clpzl3(double mt_clpzl3) {
		Mt_clpzl3 = mt_clpzl3;
	}

	public double getMt_clpzl4() {
		return Mt_clpzl4;
	}

	public void setMt_clpzl4(double mt_clpzl4) {
		Mt_clpzl4 = mt_clpzl4;
	}

	public double getMt_clpzlsyzl1() {
		return Mt_clpzlsyzl1;
	}

	public void setMt_clpzlsyzl1(double mt_clpzlsyzl1) {
		Mt_clpzlsyzl1 = mt_clpzlsyzl1;
	}

	public double getMt_clpzlsyzl2() {
		return Mt_clpzlsyzl2;
	}

	public void setMt_clpzlsyzl2(double mt_clpzlsyzl2) {
		Mt_clpzlsyzl2 = mt_clpzlsyzl2;
	}

	public double getMt_clpzlsyzl3() {
		return Mt_clpzlsyzl3;
	}

	public void setMt_clpzlsyzl3(double mt_clpzlsyzl3) {
		Mt_clpzlsyzl3 = mt_clpzlsyzl3;
	}

	public double getMt_clpzlsyzl4() {
		return Mt_clpzlsyzl4;
	}

	public void setMt_clpzlsyzl4(double mt_clpzlsyzl4) {
		Mt_clpzlsyzl4 = mt_clpzlsyzl4;
	}

	public double getMt_qsf1() {
		return Mt_qsf1;
	}

	public void setMt_qsf1(double mt_qsf1) {
		Mt_qsf1 = mt_qsf1;
	}

	public double getMt_qsf2() {
		return Mt_qsf2;
	}

	public void setMt_qsf2(double mt_qsf2) {
		Mt_qsf2 = mt_qsf2;
	}

	public double getMt_qsf3() {
		return Mt_qsf3;
	}

	public void setMt_qsf3(double mt_qsf3) {
		Mt_qsf3 = mt_qsf3;
	}

	public double getMt_qsf4() {
		return Mt_qsf4;
	}

	public void setMt_qsf4(double mt_qsf4) {
		Mt_qsf4 = mt_qsf4;
	}

	public double getMt_syzl1() {
		return Mt_syzl1;
	}

	public void setMt_syzl1(double mt_syzl1) {
		Mt_syzl1 = mt_syzl1;
	}

	public double getMt_syzl2() {
		return Mt_syzl2;
	}

	public void setMt_syzl2(double mt_syzl2) {
		Mt_syzl2 = mt_syzl2;
	}

	public double getMt_syzl3() {
		return Mt_syzl3;
	}

	public void setMt_syzl3(double mt_syzl3) {
		Mt_syzl3 = mt_syzl3;
	}

	public double getMt_syzl4() {
		return Mt_syzl4;
	}

	public void setMt_syzl4(double mt_syzl4) {
		Mt_syzl4 = mt_syzl4;
	}

	public double getQl_stad1() {
		return Ql_stad1;
	}

	public void setQl_stad1(double ql_ad1) {
		Ql_stad1 = ql_ad1;
	}

	public double getQl_stad2() {
		return Ql_stad2;
	}

	public void setQl_stad2(double ql_ad2) {
		Ql_stad2 = ql_ad2;
	}

	public double getQl_stad3() {
		return Ql_stad3;
	}

	public void setQl_stad3(double ql_ad3) {
		Ql_stad3 = ql_ad3;
	}

	public double getQl_stad4() {
		return Ql_stad4;
	}

	public void setQl_stad4(double ql_ad4) {
		Ql_stad4 = ql_ad4;
	}

	public double getQl_std1() {
		return Ql_std1;
	}

	public void setQl_std1(double ql_d1) {
		Ql_std1 = ql_d1;
	}

	public double getQl_std2() {
		return Ql_std2;
	}

	public void setQl_std2(double ql_d2) {
		Ql_std2 = ql_d2;
	}

	public double getQl_std3() {
		return Ql_std3;
	}

	public void setQl_std3(double ql_d3) {
		Ql_std3 = ql_d3;
	}

	public double getQl_std4() {
		return Ql_std4;
	}

	public void setQl_std4(double ql_d4) {
		Ql_std4 = ql_d4;
	}

	public double getQl_qmh1() {
		return Ql_qmh1;
	}

	public void setQl_qmh1(double ql_qmh1) {
		Ql_qmh1 = ql_qmh1;
	}

	public double getQl_qmh2() {
		return Ql_qmh2;
	}

	public void setQl_qmh2(double ql_qmh2) {
		Ql_qmh2 = ql_qmh2;
	}

	public double getQl_qmh3() {
		return Ql_qmh3;
	}

	public void setQl_qmh3(double ql_qmh3) {
		Ql_qmh3 = ql_qmh3;
	}

	public double getQl_qmh4() {
		return Ql_qmh4;
	}

	public void setQl_qmh4(double ql_qmh4) {
		Ql_qmh4 = ql_qmh4;
	}

	public double getQl_qmzl1() {
		return Ql_qmzl1;
	}

	public void setQl_qmzl1(double ql_qmzl1) {
		Ql_qmzl1 = ql_qmzl1;
	}

	public double getQl_qmzl2() {
		return Ql_qmzl2;
	}

	public void setQl_qmzl2(double ql_qmzl2) {
		Ql_qmzl2 = ql_qmzl2;
	}

	public double getQl_qmzl3() {
		return Ql_qmzl3;
	}

	public void setQl_qmzl3(double ql_qmzl3) {
		Ql_qmzl3 = ql_qmzl3;
	}

	public double getQl_qmzl4() {
		return Ql_qmzl4;
	}

	public void setQl_qmzl4(double ql_qmzl4) {
		Ql_qmzl4 = ql_qmzl4;
	}

	public double getQl_qmzlsyzl1() {
		return Ql_qmzlsyzl1;
	}

	public void setQl_qmzlsyzl1(double ql_qmzlsyzl1) {
		Ql_qmzlsyzl1 = ql_qmzlsyzl1;
	}

	public double getQl_qmzlsyzl2() {
		return Ql_qmzlsyzl2;
	}

	public void setQl_qmzlsyzl2(double ql_qmzlsyzl2) {
		Ql_qmzlsyzl2 = ql_qmzlsyzl2;
	}

	public double getQl_qmzlsyzl3() {
		return Ql_qmzlsyzl3;
	}

	public void setQl_qmzlsyzl3(double ql_qmzlsyzl3) {
		Ql_qmzlsyzl3 = ql_qmzlsyzl3;
	}

	public double getQl_qmzlsyzl4() {
		return Ql_qmzlsyzl4;
	}

	public void setQl_qmzlsyzl4(double ql_qmzlsyzl4) {
		Ql_qmzlsyzl4 = ql_qmzlsyzl4;
	}

	public double getQl_syzl1() {
		return Ql_syzl1;
	}

	public void setQl_syzl1(double ql_syzl1) {
		Ql_syzl1 = ql_syzl1;
	}

	public double getQl_syzl2() {
		return Ql_syzl2;
	}

	public void setQl_syzl2(double ql_syzl2) {
		Ql_syzl2 = ql_syzl2;
	}

	public double getQl_syzl3() {
		return Ql_syzl3;
	}

	public void setQl_syzl3(double ql_syzl3) {
		Ql_syzl3 = ql_syzl3;
	}

	public double getQl_syzl4() {
		return Ql_syzl4;
	}

	public void setQl_syzl4(double ql_syzl4) {
		Ql_syzl4 = ql_syzl4;
	}

	public double getVad_ggh1() {
		return Vad_ggh1;
	}

	public void setVad_ggh1(double vad_ggh1) {
		Vad_ggh1 = vad_ggh1;
	}

	public double getVad_ggh2() {
		return Vad_ggh2;
	}

	public void setVad_ggh2(double vad_ggh2) {
		Vad_ggh2 = vad_ggh2;
	}

	public double getVad_ggh3() {
		return Vad_ggh3;
	}

	public void setVad_ggh3(double vad_ggh3) {
		Vad_ggh3 = vad_ggh3;
	}

	public double getVad_ggh4() {
		return Vad_ggh4;
	}

	public void setVad_ggh4(double vad_ggh4) {
		Vad_ggh4 = vad_ggh4;
	}

	public double getVad_ggzl1() {
		return Vad_ggzl1;
	}

	public void setVad_ggzl1(double vad_ggzl1) {
		Vad_ggzl1 = vad_ggzl1;
	}

	public double getVad_ggzl2() {
		return Vad_ggzl2;
	}

	public void setVad_ggzl2(double vad_ggzl2) {
		Vad_ggzl2 = vad_ggzl2;
	}

	public double getVad_ggzl3() {
		return Vad_ggzl3;
	}

	public void setVad_ggzl3(double vad_ggzl3) {
		Vad_ggzl3 = vad_ggzl3;
	}

	public double getVad_ggzl4() {
		return Vad_ggzl4;
	}

	public void setVad_ggzl4(double vad_ggzl4) {
		Vad_ggzl4 = vad_ggzl4;
	}

	public double getVad_ggzlsyzl1() {
		return Vad_ggzlsyzl1;
	}

	public void setVad_ggzlsyzl1(double vad_ggzlsyzl1) {
		Vad_ggzlsyzl1 = vad_ggzlsyzl1;
	}

	public double getVad_ggzlsyzl2() {
		return Vad_ggzlsyzl2;
	}

	public void setVad_ggzlsyzl2(double vad_ggzlsyzl2) {
		Vad_ggzlsyzl2 = vad_ggzlsyzl2;
	}

	public double getVad_ggzlsyzl3() {
		return Vad_ggzlsyzl3;
	}

	public void setVad_ggzlsyzl3(double vad_ggzlsyzl3) {
		Vad_ggzlsyzl3 = vad_ggzlsyzl3;
	}

	public double getVad_ggzlsyzl4() {
		return Vad_ggzlsyzl4;
	}

	public void setVad_ggzlsyzl4(double vad_ggzlsyzl4) {
		Vad_ggzlsyzl4 = vad_ggzlsyzl4;
	}

	public double getVad_hff1() {
		return Vad_hff1;
	}

	public void setVad_hff1(double vad_hff1) {
		Vad_hff1 = vad_hff1;
	}

	public double getVad_hff2() {
		return Vad_hff2;
	}

	public void setVad_hff2(double vad_hff2) {
		Vad_hff2 = vad_hff2;
	}

	public double getVad_hff3() {
		return Vad_hff3;
	}

	public void setVad_hff3(double vad_hff3) {
		Vad_hff3 = vad_hff3;
	}

	public double getVad_hff4() {
		return Vad_hff4;
	}

	public void setVad_hff4(double vad_hff4) {
		Vad_hff4 = vad_hff4;
	}

	public double getVad_hffqsf1() {
		return Vad_hffqsf1;
	}

	public void setVad_hffqsf1(double vad_hffqsf1) {
		Vad_hffqsf1 = vad_hffqsf1;
	}

	public double getVad_hffqsf2() {
		return Vad_hffqsf2;
	}

	public void setVad_hffqsf2(double vad_hffqsf2) {
		Vad_hffqsf2 = vad_hffqsf2;
	}

	public double getVad_hffqsf3() {
		return Vad_hffqsf3;
	}

	public void setVad_hffqsf3(double vad_hffqsf3) {
		Vad_hffqsf3 = vad_hffqsf3;
	}

	public double getVad_hffqsf4() {
		return Vad_hffqsf4;
	}

	public void setVad_hffqsf4(double vad_hffqsf4) {
		Vad_hffqsf4 = vad_hffqsf4;
	}

	public double getVad_jrhzzl1() {
		return Vad_jrhzzl1;
	}

	public void setVad_jrhzzl1(double vad_jrhzzl1) {
		Vad_jrhzzl1 = vad_jrhzzl1;
	}

	public double getVad_jrhzzl2() {
		return Vad_jrhzzl2;
	}

	public void setVad_jrhzzl2(double vad_jrhzzl2) {
		Vad_jrhzzl2 = vad_jrhzzl2;
	}

	public double getVad_jrhzzl3() {
		return Vad_jrhzzl3;
	}

	public void setVad_jrhzzl3(double vad_jrhzzl3) {
		Vad_jrhzzl3 = vad_jrhzzl3;
	}

	public double getVad_jrhzzl4() {
		return Vad_jrhzzl4;
	}

	public void setVad_jrhzzl4(double vad_jrhzzl4) {
		Vad_jrhzzl4 = vad_jrhzzl4;
	}

	public double getVad_myjrhjszl1() {
		return Vad_myjrhjszl1;
	}

	public void setVad_myjrhjszl1(double vad_myjrhjszl1) {
		Vad_myjrhjszl1 = vad_myjrhjszl1;
	}

	public double getVad_myjrhjszl2() {
		return Vad_myjrhjszl2;
	}

	public void setVad_myjrhjszl2(double vad_myjrhjszl2) {
		Vad_myjrhjszl2 = vad_myjrhjszl2;
	}

	public double getVad_myjrhjszl3() {
		return Vad_myjrhjszl3;
	}

	public void setVad_myjrhjszl3(double vad_myjrhjszl3) {
		Vad_myjrhjszl3 = vad_myjrhjszl3;
	}

	public double getVad_myjrhjszl4() {
		return Vad_myjrhjszl4;
	}

	public void setVad_myjrhjszl4(double vad_myjrhjszl4) {
		Vad_myjrhjszl4 = vad_myjrhjszl4;
	}

	public double getVad_syzl1() {
		return Vad_syzl1;
	}

	public void setVad_syzl1(double vad_syzl1) {
		Vad_syzl1 = vad_syzl1;
	}

	public double getVad_syzl2() {
		return Vad_syzl2;
	}

	public void setVad_syzl2(double vad_syzl2) {
		Vad_syzl2 = vad_syzl2;
	}

	public double getVad_syzl3() {
		return Vad_syzl3;
	}

	public void setVad_syzl3(double vad_syzl3) {
		Vad_syzl3 = vad_syzl3;
	}

	public double getVad_syzl4() {
		return Vad_syzl4;
	}

	public void setVad_syzl4(double vad_syzl4) {
		Vad_syzl4 = vad_syzl4;
	}

	// ////////////////结束////////////////////
	// ////////////代替bean的代码结束////////////////

	private String _editMeiyzl = "";

	public String getEditMeiyzl() {
		return _editMeiyzl;
	}

	public void setEditMeiyzl(String edit) {
		_editMeiyzl = edit;
	}

	private String _editMeiyzl2 = "";

	public String getEditMeiyzl2() {
		return _editMeiyzl2;
	}

	public void setEditMeiyzl2(String edit) {
		_editMeiyzl2 = edit;
	}

	private String _editMeiyzl3 = "";

	public String getEditMeiyzl3() {
		return _editMeiyzl3;
	}

	public void setEditMeiyzl3(String edit) {
		_editMeiyzl3 = edit;
	}

	private boolean editdisable;

	private boolean editable;

	private boolean editdisable1;

	private boolean editable1;

	private String _msg;

	private boolean fenh;

	public boolean isFenh() {
		return fenh;
	}

	public void setFenh(boolean fenh) {
		this.fenh = fenh;
	}

	private boolean sczh;

	public boolean isSczh() {
		return sczh;
	}

	public void setSczh(boolean sczh) {
		this.sczh = sczh;
	}

	private boolean jingwz;

	public boolean isJingwz() {
		return jingwz;
	}

	public void setJingwz(boolean jingwz) {
		this.jingwz = jingwz;
	}

	public void setMsg(String _value) {
		_msg = _value;
	}

	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}

	protected void initialize() {
		super.initialize();
		_msg = "";
	}

	private static int _editTableRow = -1;// 编辑框中选中的行

	public int getEditTableRow() {
		return _editTableRow;
	}

	public void setEditTableRow(int _value) {
		_editTableRow = _value;
	}

	long rulmzlb_id = -1;// 入炉煤质量表编号

	// ///////////////////////////////////////////////////////////////////
	private void Refurbish() {
		getSelectData();
	}

	private void Delete()
	{
		List _list = new ArrayList();
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit)getPage().getVisit();
		try
		{
			String rulmzl = 
				"select id,fenxrq\n" +
				"   from rulmzlb\n" + 
				"  where jizfzb_id = " + getProperId(_QumwModel, getQumwValue().getValue()) +
				"    and to_char(rulrq, 'yyyy-mm-dd') = '" + getRulrq() + "'\n" + 
				"    and to_char(fenxrq, 'yyyy-mm-dd') = '" + getHuayrq() + "'";
			
			ResultSet zlidrs = con.getResultSet(rulmzl);
			if(zlidrs.next()){
				setRulmzlb_id(zlidrs.getLong("id"));
				
			}
			zlidrs.close();
			String sql = "delete from rulmzlb where id = " + rulmzlb_id;
			String sqlMt = "delete from quansfhyb where zhillsb_id = " + rulmzlb_id; 
			String sqlSHH = "delete from gongyfsb where zhillsb_id = " + rulmzlb_id;
			String sqlS = "delete from liufhyb where zhillsb_id = " + rulmzlb_id;
			String sqlH = "delete from danthyb where zhillsb_id = " + rulmzlb_id;

			con.getDelete(sql);
			con.getDelete(sqlMt);
			con.getDelete(sqlSHH);
			con.getDelete(sqlS);
			con.getDelete(sqlH);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			con.Close();
		}
	}
	private long pubzlb_id = -1;
	private void Save() 
	{
		List _list = new ArrayList();
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();

		try {
			// ////////////////////////////////////////////
			if (getRulmzlb_id() == -1)// 当返回的Rulmzlb_id为-1时执行insert
			{
				rulmzlb_id = Long.parseLong(MainGlobal.getNewID(visit
						.getDiancxxb_id()));
				String sql = "insert into rulmzlb(id,rulrq,diancxxb_id"
						+ ",rulbzb_id,jizfzb_id,lursj,shenhzt) values"
						+ "("
						+ rulmzlb_id
						+ ",to_date('"
						+ FormatDate(new Date())
						+ "','yyyy-mm-dd')" 
						+ "," + visit.getDiancxxb_id()
						+ ","
						+ getProperId(_RulbzModel, getRulbzValue().getValue())
						+ ","
						+ getProperId(_QumwModel, getQumwValue().getValue())
						+ ",to_date('"
						+ FormatDate(new Date())
						+ "','yyyy-mm-dd'),0)";
				con.getInsert(sql);
				// ///////////////////////全水分Mt/////////////////////
				// insert全水分第1次化验
				if (getMt_id1() == -1) {
					String sqlMt1 = "insert into quansfhyb "
							+ "(id,xuh,zhillsb_id,fenxrq,fenxxmb_id,qimbh,"
							+ "qimmyzl,qimzl,meiyzl,honghzl1,"
							+ "zuizhhzl,zhi,shenhry,lury,shenhzt)" + " values ("
							+ MainGlobal.getNewID(visit.getDiancxxb_id())
							+ ",1" + "," + rulmzlb_id + ",to_date('"
							+ FormatDate(getHuayrq()) + "','yyyy_mm_dd'),1,"
							+ getMt_clph1() + "," + getMt_clpzlsyzl1() + ","
							+ getMt_clpzl1() + "," + getMt_syzl1() + ","
							+ getMt_hhzzl1() + "," + getMt_jcxsyhzzl1() + ","
							+ getMt_qsf1() + "," + getMt_sh1() + ",'"
							+ visit.getRenymc() + "'," 
							+ getProperId(_ShenhztModel,getShenhztValue().getValue()) +")";
					con.getInsert(sqlMt1);
				}
				// insert全水分第2次化验
				if (getMt_id2() == -1) {
					String sqlMt2 = "insert into quansfhyb "
							+ "(id,xuh,zhillsb_id,fenxrq,fenxxmb_id,qimbh,"
							+ "qimmyzl,qimzl,meiyzl,honghzl1,"
							+ "zuizhhzl,zhi,shenhry,lury,shenhzt)" + " values ("
							+ MainGlobal.getNewID(visit.getDiancxxb_id())
							+ ",2" + "," + rulmzlb_id + ",to_date('"
							+ FormatDate(getHuayrq()) + "','yyyy_mm_dd'),1,"
							+ getMt_clph2() + "," + getMt_clpzlsyzl2() + ","
							+ getMt_clpzl2() + "," + getMt_syzl2() + ","
							+ getMt_hhzzl2() + "," + getMt_jcxsyhzzl2() + ","
							+ getMt_qsf2() + "," + getMt_sh2() + ",'"
							+ visit.getRenymc() + "'," +
							getProperId(_ShenhztModel,getShenhztValue().getValue()) +")";
					con.getInsert(sqlMt2);
				}
				// insert全水分第3次化验
				if (getMt_id3() == -1) {
					String sqlMt3 = "insert into quansfhyb "
							+ "(id,xuh,zhillsb_id,fenxrq,fenxxmb_id,qimbh,"
							+ "qimmyzl,qimzl,meiyzl,honghzl1,"
							+ "zuizhhzl,zhi,shenhry,lury,shenhzt)" + " values ("
							+ MainGlobal.getNewID(visit.getDiancxxb_id())
							+ ",3" + "," + rulmzlb_id + ",to_date('"
							+ FormatDate(getHuayrq()) + "','yyyy_mm_dd'),1,"
							+ getMt_clph3() + "," + getMt_clpzlsyzl3() + ","
							+ getMt_clpzl3() + "," + getMt_syzl3() + ","
							+ getMt_hhzzl3() + "," + getMt_jcxsyhzzl3() + ","
							+ getMt_qsf3() + "," + getMt_sh3() + ",'"
							+ visit.getRenymc() + "'," +
							getProperId(_ShenhztModel,getShenhztValue().getValue()) +")";
					con.getInsert(sqlMt3);
				}
				// insert全水分第4次化验
				if (getMt_id4() == -1) {

					String sqlMt4 = "insert into quansfhyb "
							+ "(id,xuh,zhillsb_id,fenxrq,fenxxmb_id,qimbh,"
							+ "qimmyzl,qimzl,meiyzl,honghzl1,"
							+ "zuizhhzl,zhi,shenhry,lury,shenhzt)" + " values ("
							+ MainGlobal.getNewID(visit.getDiancxxb_id())
							+ ",4" + "," + rulmzlb_id + ",to_date('"
							+ FormatDate(getHuayrq()) + "','yyyy_mm_dd'),1,"
							+ getMt_clph4() + "," + getMt_clpzlsyzl4() + ","
							+ getMt_clpzl4() + "," + getMt_syzl4() + ","
							+ getMt_hhzzl4() + "," + getMt_jcxsyhzzl4() + ","
							+ getMt_qsf4() + "," + getMt_sh4() + ",'"
							+ visit.getRenymc() + "'," +
							getProperId(_ShenhztModel,getShenhztValue().getValue()) +")";
					con.getInsert(sqlMt4);
				}
				// //////////////////////水分////////////////////////
				// insert水分第1次化验
				if (getMad_id1() == -1) {

					String sqlMad1 = "insert into gongyfsb "
							+ "(id,xuh,zhillsb_id,fenxrq,fenxxmb_id,qimbh,"
							+ "qimmyzl,qimzl,meiyzl,honghzl1,"
							+ "zuizhhzl,zhi,shenhry,lury,shenhzt)" + " values ("
							+ MainGlobal.getNewID(visit.getDiancxxb_id())
							+ ",1" + "," + rulmzlb_id + ",to_date('"
							+ FormatDate(getHuayrq()) + "','yyyy-mm-dd'),2,"
							+ getMad_clph1() + "," + getMad_clpzlsyzl1() + ","
							+ getMad_clpzl1() + "," + getMad_syzl1() + ","
							+ getMad_hhzzl1() + "," + getMad_jcxsyhzzl1() + ","
							+ getMad_qsf1() + "," + getMad_sh1() + ",'"
							+ visit.getRenymc() + "'," +
							getProperId(_ShenhztModel,getShenhztValue().getValue()) +")";
					con.getInsert(sqlMad1);
				}
				// insert水分第2次化验
				if (getMad_id2() == -1) {

					String sqlMad2 = "insert into gongyfsb "
							+ "(id,xuh,zhillsb_id,fenxrq,fenxxmb_id,qimbh,"
							+ "qimmyzl,qimzl,meiyzl,honghzl1,"
							+ "zuizhhzl,zhi,shenhry,lury,shenhzt)" + " values ("
							+ MainGlobal.getNewID(visit.getDiancxxb_id())
							+ ",2" + "," + rulmzlb_id + ",to_date('"
							+ FormatDate(getHuayrq()) + "','yyyy-mm-dd'),2,"
							+ getMad_clph2() + "," + getMad_clpzlsyzl2() + ","
							+ getMad_clpzl2() + "," + getMad_syzl2() + ","
							+ getMad_hhzzl2() + "," + getMad_jcxsyhzzl2() + ","
							+ getMad_qsf2() + "," + getMad_sh2() + ",'"
							+ visit.getRenymc() + "'," +
							getProperId(_ShenhztModel,getShenhztValue().getValue()) +")";
					con.getInsert(sqlMad2);

				}
				// insert水分第3次化验
				if (getMad_id3() == -1) {

					String sqlMad3 = "insert into gongyfsb "
							+ "(id,xuh,zhillsb_id,fenxrq,fenxxmb_id,qimbh,"
							+ "qimmyzl,qimzl,meiyzl,honghzl1,"
							+ "zuizhhzl,zhi,shenhry,lury,shenhzt)" + " values ("
							+ MainGlobal.getNewID(visit.getDiancxxb_id())
							+ ",3" + "," + rulmzlb_id + ",to_date('"
							+ FormatDate(getHuayrq()) + "','yyyy-mm-dd'),2,"
							+ getMad_clph3() + "," + getMad_clpzlsyzl3() + ","
							+ getMad_clpzl3() + "," + getMad_syzl3() + ","
							+ getMad_hhzzl3() + "," + getMad_jcxsyhzzl3() + ","
							+ getMad_qsf3() + "," + getMad_sh3() + ",'"
							+ visit.getRenymc() + "'," +
							getProperId(_ShenhztModel,getShenhztValue().getValue()) +")";
					con.getInsert(sqlMad3);

				}
				// insert水分第4次化验
				if (getMad_id4() == -1) {

					String sqlMad4 = "insert into gongyfsb "
							+ "(id,xuh,zhillsb_id,fenxrq,fenxxmb_id,qimbh,"
							+ "qimmyzl,qimzl,meiyzl,honghzl4,"
							+ "zuizhhzl,zhi,shenhry,lury,shenhzt)" + " values ("
							+ MainGlobal.getNewID(visit.getDiancxxb_id())
							+ ",4" + "," + rulmzlb_id + ",to_date('"
							+ FormatDate(getHuayrq()) + "','yyyy-mm-dd'),2,"
							+ getMad_clph4() + "," + getMad_clpzlsyzl4() + ","
							+ getMad_clpzl4() + "," + getMad_syzl4() + ","
							+ getMad_hhzzl4() + "," + getMad_jcxsyhzzl4() + ","
							+ getMad_qsf4() + "," + getMad_sh4() + ",'"
							+ visit.getRenymc() + "'," +
							getProperId(_ShenhztModel,getShenhztValue().getValue()) +")";
					con.getInsert(sqlMad4);

				}

				// //////////////////挥发分////////////////
				// insert挥发分第1次化验
				if (getVad_id1() == -1) {
					String sqlVad1 = "insert into gongyfsb "
							+ "(id,xuh,zhillsb_id,fenxrq,fenxxmb_id,qimbh,"
							+ "qimmyzl,qimzl,meiyzl,honghzl1,"
							+ "zuizhhzl,zhi,shenhry,lury,shenhzt)" + " values ("
							+ MainGlobal.getNewID(visit.getDiancxxb_id())
							+ ",1" + "," + rulmzlb_id + ",to_date('"
							+ FormatDate(getHuayrq()) + "','yyyy-mm-dd'),3,"
							+ getVad_ggh1() + "," + getVad_ggzlsyzl1() + ","
							+ getVad_ggzl1() + "," + getVad_syzl1() + ","
							+ getVad_myjrhjszl1() + "," + getVad_jrhzzl1()
							+ "," + getVad_hff1() + "," + getVad_sh1() + ",'"
							+ visit.getRenymc() + "'," +
							getProperId(_ShenhztModel,getShenhztValue().getValue()) +")";
					con.getInsert(sqlVad1);
				}
				// insert挥发分第2次化验
				if (getVad_id2() == -1) {

					String sqlVad2 = "insert into gongyfsb "
							+ "(id,xuh,zhillsb_id,fenxrq,fenxxmb_id,qimbh,"
							+ "qimmyzl,qimzl,meiyzl,honghzl1,"
							+ "zuizhhzl,zhi,shenhry,lury,shenhzt)" + " values ("
							+ MainGlobal.getNewID(visit.getDiancxxb_id())
							+ ",2" + "," + rulmzlb_id + ",to_date('"
							+ FormatDate(getHuayrq()) + "','yyyy-mm-dd'),3,"
							+ getVad_ggh2() + "," + getVad_ggzlsyzl2() + ","
							+ getVad_ggzl2() + "," + getVad_syzl2() + ","
							+ getVad_myjrhjszl2() + "," + getVad_jrhzzl2()
							+ "," + getVad_hff2() + "," + getVad_sh2() + ",'"
							+ visit.getRenymc() + "'," +
							getProperId(_ShenhztModel,getShenhztValue().getValue()) +")";
					con.getInsert(sqlVad2);

				}
				// insert挥发分第3次化验
				if (getVad_id3() == -1) {

					String sqlVad3 = "insert into gongyfsb "
							+ "(id,xuh,zhillsb_id,fenxrq,fenxxmb_id,qimbh,"
							+ "qimmyzl,qimzl,meiyzl,honghzl1,"
							+ "zuizhhzl,zhi,shenhry,lury,shenhzt)" + " values ("
							+ MainGlobal.getNewID(visit.getDiancxxb_id())
							+ ",3" + "," + rulmzlb_id + ",to_date('"
							+ FormatDate(getHuayrq()) + "','yyyy-mm-dd'),3,"
							+ getVad_ggh3() + "," + getVad_ggzlsyzl3() + ","
							+ getVad_ggzl3() + "," + getVad_syzl3() + ","
							+ getVad_myjrhjszl3() + "," + getVad_jrhzzl3()
							+ "," + getVad_hff3() + "," + getVad_sh3() + ",'"
							+ visit.getRenymc() + "'," +
							getProperId(_ShenhztModel,getShenhztValue().getValue()) +")";
					con.getInsert(sqlVad3);

				}
				// insert挥发分第4次化验
				if (getVad_id4() == -1) {

					String sqlVad4 = "insert into gongyfsb "
							+ "(id,xuh,zhillsb_id,fenxrq,fenxxmb_id,qimbh,"
							+ "qimmyzl,qimzl,meiyzl,honghzl1,"
							+ "zuizhhzl,zhi,shenhry,lury,shenhzt)" + " values ("
							+ MainGlobal.getNewID(visit.getDiancxxb_id())
							+ ",4" + "," + rulmzlb_id + ",to_date('"
							+ FormatDate(getHuayrq()) + "','yyyy-mm-dd'),3,"
							+ getVad_ggh4() + "," + getVad_ggzlsyzl4() + ","
							+ getVad_ggzl4() + "," + getVad_syzl4() + ","
							+ getVad_myjrhjszl4() + "," + getVad_jrhzzl4()
							+ "," + getVad_hff4() + "," + getVad_sh4() + ",'"
							+ visit.getRenymc() + "'," +
							getProperId(_ShenhztModel,getShenhztValue().getValue()) +")";
					con.getInsert(sqlVad4);

				}

				// //////////////////////Aad灰分////////////////////////
				// insertAad灰分第1次化验
				if (getAad_id1() == -1) {
					String sqlAad1 = "insert into gongyfsb "
							+ "(id,xuh,zhillsb_id,fenxrq,fenxxmb_id,qimbh,"
							+ "qimmyzl,qimzl,meiyzl,honghzl1,"
							+ "zuizhhzl,zhi,shenhry,lury,shenhzt)" + " values ("
							+ MainGlobal.getNewID(visit.getDiancxxb_id())
							+ ",1" + "," + rulmzlb_id + ",to_date('"
							+ FormatDate(getHuayrq()) + "','yyyy-mm-dd'),4,"
							+ getAad_hmh1() + "," + getAad_hmzlsyzl1() + ","
							+ getAad_hmzl1() + "," + getAad_syzl1() + ","
							+ getAad_jrhzzl1() + "," + getAad_jcxsyhzzl1()
							+ "," + getAad_hf1() + "," + getAad_sh1() + ",'"
							+ visit.getRenymc() + "'," +
							getProperId(_ShenhztModel,getShenhztValue().getValue()) +")";
					con.getInsert(sqlAad1);
				}
				// insertAad灰分第2次化验
				if (getAad_id2() == -1) {
					String sqlAad2 = "insert into gongyfsb "
							+ "(id,xuh,zhillsb_id,fenxrq,fenxxmb_id,qimbh,"
							+ "qimmyzl,qimzl,meiyzl,honghzl1,"
							+ "zuizhhzl,zhi,shenhry,lury,shenhzt)" + " values ("
							+ MainGlobal.getNewID(visit.getDiancxxb_id())
							+ ",2" + "," + rulmzlb_id + ",to_date('"
							+ FormatDate(getHuayrq()) + "','yyyy-mm-dd'),4,"
							+ getAad_hmh2() + "," + getAad_hmzlsyzl2() + ","
							+ getAad_hmzl2() + "," + getAad_syzl2() + ","
							+ getAad_jrhzzl2() + "," + getAad_jcxsyhzzl2()
							+ "," + getAad_hf2() + "," + getAad_sh2() + ",'"
							+ visit.getRenymc() + "'," +
							getProperId(_ShenhztModel,getShenhztValue().getValue()) +")";
					con.getInsert(sqlAad2);
				}
				// insertAad灰分第3次化验
				if (getAad_id3() == -1) {
					String sqlAad3 = "insert into gongyfsb "
							+ "(id,xuh,zhillsb_id,fenxrq,fenxxmb_id,qimbh,"
							+ "qimmyzl,qimzl,meiyzl,honghzl3,"
							+ "zuizhhzl,zhi,shenhry,lury,shenhzt)" + " values ("
							+ MainGlobal.getNewID(visit.getDiancxxb_id())
							+ ",3" + "," + rulmzlb_id + ",to_date('"
							+ FormatDate(getHuayrq()) + "','yyyy-mm-dd'),4,"
							+ getAad_hmh3() + "," + getAad_hmzlsyzl3() + ","
							+ getAad_hmzl3() + "," + getAad_syzl3() + ","
							+ getAad_jrhzzl3() + "," + getAad_jcxsyhzzl3()
							+ "," + getAad_hf3() + "," + getAad_sh3() + ",'"
							+ visit.getRenymc() + "'," +
							getProperId(_ShenhztModel,getShenhztValue().getValue()) +")";
					con.getInsert(sqlAad3);
				}
				// insertAad灰分第4次化验
				if (getAad_id4() == -1) {
					String sqlAad4 = "insert into gongyfsb "
							+ "(id,xuh,zhillsb_id,fenxrq,fenxxmb_id,qimbh,"
							+ "qimmyzl,qimzl,meiyzl,honghzl4,"
							+ "zuizhhzl,zhi,shenhry,lury,shenhzt)" + " values ("
							+ MainGlobal.getNewID(visit.getDiancxxb_id())
							+ ",4" + "," + rulmzlb_id + ",to_date('"
							+ FormatDate(getHuayrq()) + "','yyyy-mm-dd'),4,"
							+ getAad_hmh4() + "," + getAad_hmzlsyzl4() + ","
							+ getAad_hmzl4() + "," + getAad_syzl4() + ","
							+ getAad_jrhzzl4() + "," + getAad_jcxsyhzzl4()
							+ "," + getAad_hf4() + "," + getAad_sh4() + ",'"
							+ visit.getRenymc() + "'," +
							getProperId(_ShenhztModel,getShenhztValue().getValue()) +")";
					con.getInsert(sqlAad4);
				}
				// ///////////////////全硫////////////////////
				// insert全硫第1次化验
				if (getQl_id1() == -1) {
					String sqlQl1 = "insert into liufhyb"
							+ "(id,xuh,zhillsb_id,fenxrq,fenxxmb_id,huayyqbh,"
							+ "meiyzl,zhi,shenhry,lury,qimmyzl,qimzl,shenhzt) "
							+ " values("
							+ MainGlobal.getNewID(visit.getDiancxxb_id())
							+ ",1" + "," + rulmzlb_id + ",to_date('"
							+ FormatDate(getHuayrq()) + "','yyyy-mm-dd'),6,"
							+ getQl_qmh1() + "," + getQl_syzl1() + ","
							+ getQl_stad1() + "," + getQl_sh1() + ",'"
							+ visit.getRenymc() + "'," + getQl_qmzlsyzl1()
							+ "," + getQl_qmzl1() + "," +
							getProperId(_ShenhztModel,getShenhztValue().getValue()) +")";
					con.getInsert(sqlQl1);
				}
				// insert全硫第2次化验
				if (getQl_id2() == -1) {

					String sqlQl2 = "insert into liufhyb"
							+ "(id,xuh,zhillsb_id,fenxrq,fenxxmb_id,huayyqbh,"
							+ "meiyzl,zhi,shenhry,lury,qimmyzl,qimzl,shenhzt) "
							+ " values("
							+ MainGlobal.getNewID(visit.getDiancxxb_id())
							+ ",2" + "," + rulmzlb_id + ",to_date('"
							+ FormatDate(getHuayrq()) + "','yyyy-mm-dd'),6,"
							+ getQl_qmh2() + "," + getQl_syzl2() + ","
							+ getQl_stad2() + "," + getQl_sh2() + ",'"
							+ visit.getRenymc() + "'," + getQl_qmzlsyzl2()
							+ "," + getQl_qmzl2() + "," +
							getProperId(_ShenhztModel,getShenhztValue().getValue()) +")";
					con.getInsert(sqlQl2);

				}
				// insert全硫第3次化验
				if (getQl_id3() == -1) {

					String sqlQl3 = "insert into liufhyb"
							+ "(id,xuh,zhillsb_id,fenxrq,fenxxmb_id,huayyqbh,"
							+ "meiyzl,zhi,shenhry,lury,qimmyzl,qimzl,shenhzt) "
							+ " values("
							+ MainGlobal.getNewID(visit.getDiancxxb_id())
							+ ",3" + "," + rulmzlb_id + ",to_date('"
							+ FormatDate(getHuayrq()) + "','yyyy-mm-dd'),6,"
							+ getQl_qmh3() + "," + getQl_syzl3() + ","
							+ getQl_stad3() + "," + getQl_sh3() + ",'"
							+ visit.getRenymc() + "'," + getQl_qmzlsyzl3()
							+ "," + getQl_qmzl3() + "," +
							getProperId(_ShenhztModel,getShenhztValue().getValue()) +")";
					con.getInsert(sqlQl3);

				}
				// insert全硫第4次化验
				if (getQl_id4() == -1) {

					String sqlQl4 = "insert into liufhyb"
							+ "(id,xuh,zhillsb_id,fenxrq,fenxxmb_id,huayyqbh,"
							+ "meiyzl,zhi,shenhry,lury,qimmyzl,qimzl,shenhzt) "
							+ " values("
							+ MainGlobal.getNewID(visit.getDiancxxb_id())
							+ ",4" + "," + rulmzlb_id + ",to_date('"
							+ FormatDate(getHuayrq()) + "','yyyy-mm-dd'),6,"
							+ getQl_qmh4() + "," + getQl_syzl4() + ","
							+ getQl_stad4() + "," + getQl_sh4() + ",'"
							+ visit.getRenymc() + "'," + getQl_qmzlsyzl4()
							+ "," + getQl_qmzl4() + "," +
							getProperId(_ShenhztModel,getShenhztValue().getValue()) +")";
					con.getInsert(sqlQl4);

				}
				// /////////////////发热量////////////////////
				// insert发热量第1次化验
				if (getFrl_id1() == -1) {
					String sqlFrl1 = "insert into danthyb"
							+ "(id,xuh,zhillsb_id,fenxrq,fenxxmb_id,qimzl,"
							+ "meiyzl,shenhry,zhi,qimmyzl,lury,shenhzt)" + " values("
							+ MainGlobal.getNewID(visit.getDiancxxb_id())
							+ ",1" + "," + rulmzlb_id + ",to_date('"
							+ FormatDate(getHuayrq()) + "','yyyy-mm-dd'),5,"
							+ getFrl_ggzl1() + "," + getFrl_syzl1() + ","
							+ getFrl_sh1() + "," + getFrl_qbad1() + ","
							+ getFrl_ggzlsyzl1() + ",'" + visit.getRenymc()
							+ "'," +
							getProperId(_ShenhztModel,getShenhztValue().getValue()) +")";
					con.getInsert(sqlFrl1);
				}
				// insert发热量第2次化验
				if (getFrl_id2() == -1) {

					String sqlFrl2 = "insert into danthyb"
							+ "(id,xuh,zhillsb_id,fenxrq,fenxxmb_id,qimzl,"
							+ "meiyzl,shenhry,zhi,qimmyzl,lury,shenhzt)" + " values("
							+ MainGlobal.getNewID(visit.getDiancxxb_id())
							+ ",2" + "," + rulmzlb_id + ",to_date('"
							+ FormatDate(getHuayrq()) + "','yyyy-mm-dd'),5,"
							+ getFrl_ggzl2() + "," + getFrl_syzl2() + ","
							+ getFrl_sh2() + "," + getFrl_qbad2() + ","
							+ getFrl_ggzlsyzl2() + ",'" + visit.getRenymc()
							+ "'," +
							getProperId(_ShenhztModel,getShenhztValue().getValue()) +")";
					con.getInsert(sqlFrl2);

				}
				// insert发热量第3次化验
				if (getFrl_id3() == -1) {

					String sqlFrl3 = "insert into danthyb"
							+ "(id,xuh,zhillsb_id,fenxrq,fenxxmb_id,qimzl,"
							+ "meiyzl,shenhry,zhi,qimmyzl,lury,shenhzt)" + " values("
							+ MainGlobal.getNewID(visit.getDiancxxb_id())
							+ ",3" + "," + rulmzlb_id + ",to_date('"
							+ FormatDate(getHuayrq()) + "','yyyy-mm-dd'),5,"
							+ getFrl_ggzl3() + "," + getFrl_syzl3() + ","
							+ getFrl_sh3() + "," + getFrl_qbad3() + ","
							+ getFrl_ggzlsyzl3() + ",'" + visit.getRenymc()
							+ "'," +
							getProperId(_ShenhztModel,getShenhztValue().getValue()) +")";
					con.getInsert(sqlFrl3);

				}
				// insert发热量第4次化验
				if (getFrl_id4() == -1) {

					String sqlFrl4 = "insert into danthyb"
							+ "(id,xuh,zhillsb_id,fenxrq,fenxxmb_id,qimzl,"
							+ "meiyzl,shenhry,zhi,qimmyzl,lury,shenhzt)" + " values("
							+ MainGlobal.getNewID(visit.getDiancxxb_id())
							+ ",4" + "," + rulmzlb_id + ",to_date('"
							+ FormatDate(getHuayrq()) + "','yyyy-mm-dd'),5,"
							+ getFrl_ggzl4() + "," + getFrl_syzl4() + ","
							+ getFrl_sh4() + "," + getFrl_qbad4() + ","
							+ getFrl_ggzlsyzl4() + ",'" + visit.getRenymc()
							+ "'," +
							getProperId(_ShenhztModel,getShenhztValue().getValue()) +")";
					con.getInsert(sqlFrl4);

				}

			}else {// //////////////////////////////////////////
				// 当返回的Rulmzlb_id为不为-1时执行update
				rulmzlb_id = getRulmzlb_id();
				// //////////////////全水分Mt//////////////
				// update全水分第1次化验
				if (getMt_id1() != -1) {
					String sqlMt1 = "update quansfhyb set " + "zhillsb_id="
							+ rulmzlb_id + ",qimbh=" + getMt_clph1()
							+ ",qimzl=" + getMt_clpzl1() + ",meiyzl="
							+ getMt_syzl1() + ",honghzl1=" + getMt_hhzzl1()
							+ ",zuizhhzl=" + getMt_jcxsyhzzl1() + ",zhi="
							+ getMt_qsf1() + ",shenhry=" + getMt_sh1()
							+ ",lury='" + visit.getRenymc() + "' where id ="
							+ getMt_id1();
					con.getUpdate(sqlMt1);
				}
				// update全水分第2次化验
				if (getMt_id2() != -1) {
					String sqlMt2 = "update quansfhyb set " + "zhillsb_id="
							+ rulmzlb_id + ",qimbh=" + getMt_clph2()
							+ ",qimzl=" + getMt_clpzl2() + ",meiyzl="
							+ getMt_syzl2() + ",honghzl1=" + getMt_hhzzl2()
							+ ",zuizhhzl=" + getMt_jcxsyhzzl2() + ",zhi="
							+ getMt_qsf2() + ",shenhry=" + getMt_sh2()
							+ ",lury='" + visit.getRenymc() + "' where id ="
							+ getMt_id2();
					con.getUpdate(sqlMt2);
				}
				// update全水分第3次化验
				if (getMt_id3() != -1) {
					String sqlMt3 = "update quansfhyb set " + "zhillsb_id="
							+ rulmzlb_id + ",qimbh=" + getMt_clph3()
							+ ",qimzl=" + getMt_clpzl3() + ",meiyzl="
							+ getMt_syzl3() + ",honghzl1=" + getMt_hhzzl3()
							+ ",zuizhhzl=" + getMt_jcxsyhzzl3() + ",zhi="
							+ getMt_qsf3() + ",shenhry=" + getMt_sh3()
							+ ",lury='" + visit.getRenymc() + "' where id ="
							+ getMt_id3();
					con.getUpdate(sqlMt3);
				}
				// update全水分第4次化验
				if (getMt_id4() != -1) {
					String sqlMt4 = "update quansfhyb set " + "zhillsb_id="
							+ rulmzlb_id + ",qimbh=" + getMt_clph4()
							+ ",qimzl=" + getMt_clpzl4() + ",meiyzl="
							+ getMt_syzl4() + ",honghzl1=" + getMt_hhzzl4()
							+ ",zuizhhzl=" + getMt_jcxsyhzzl4() + ",zhi="
							+ getMt_qsf4() + ",shenhry=" + getMt_sh4()
							+ ",lury='" + visit.getRenymc() + "' where id ="
							+ getMt_id4();
					con.getUpdate(sqlMt4);
				}
				// //////////////////////水分Mad//////////////////////
				// update水分第1次化验
				if (getMad_id1() != -1) {
					String sqlMad1 = "update gongyfsb set " + "zhillsb_id="
							+ rulmzlb_id + ",qimbh=" + getMad_clph1()
							+ ",qimzl=" + getMad_clpzl1() + ",meiyzl="
							+ getMad_syzl1() + ",honghzl1=" + getMad_hhzzl1()
							+ ",zuizhhzl=" + getMad_jcxsyhzzl1() + ",zhi="
							+ getMad_qsf1() + ",shenhry=" + getMad_sh1()
							+ ",lury='" + visit.getRenymc() + "' where id="
							+ getMad_id1();
					con.getUpdate(sqlMad1);

				}
				// update水分第2次化验
				if (getMad_id2() != -1) {
					String sqlMad2 = "update gongyfsb set " + "zhillsb_id="
							+ rulmzlb_id + ",qimbh=" + getMad_clph2()
							+ ",qimzl=" + getMad_clpzl2() + ",meiyzl="
							+ getMad_syzl2() + ",honghzl1=" + getMad_hhzzl2()
							+ ",zuizhhzl=" + getMad_jcxsyhzzl2() + ",zhi="
							+ getMad_qsf2() + ",shenhry=" + getMad_sh2()
							+ ",lury='" + visit.getRenymc() + "' where id="
							+ getMad_id2();
					con.getUpdate(sqlMad2);

				}
				// update水分第3次化验
				if (getMad_id3() != -1) {
					String sqlMad3 = "update gongyfsb set " + "zhillsb_id="
							+ rulmzlb_id + ",qimbh=" + getMad_clph3()
							+ ",qimzl=" + getMad_clpzl3() + ",meiyzl="
							+ getMad_syzl3() + ",honghzl1=" + getMad_hhzzl3()
							+ ",zuizhhzl=" + getMad_jcxsyhzzl3() + ",zhi="
							+ getMad_qsf3() + ",shenhry=" + getMad_sh3()
							+ ",lury='" + visit.getRenymc() + "' where id="
							+ getMad_id3();
					con.getUpdate(sqlMad3);

				}
				// update水分第4次化验
				if (getMad_id4() != -1) {
					String sqlMad4 = "update gongyfsb set " + "zhillsb_id="
							+ rulmzlb_id + ",qimbh=" + getMad_clph4()
							+ ",qimzl=" + getMad_clpzl4() + ",meiyzl="
							+ getMad_syzl4() + ",honghzl1=" + getMad_hhzzl4()
							+ ",zuizhhzl=" + getMad_jcxsyhzzl4() + ",zhi="
							+ getMad_qsf4() + ",shenhry=" + getMad_sh4()
							+ ",lury='" + visit.getRenymc() + "' where id="
							+ getMad_id4();
					con.getUpdate(sqlMad4);

				}

				// /////////////////挥发分Vad///////////////////
				// update挥发分第1次化验
				if (getVad_id1() != -1) {
					String sqlVad1 = "update gongyfsb set " + "zhillsb_id="
							+ rulmzlb_id + ",qimbh=" + getVad_ggh1()
							+ ",qimzl=" + getVad_ggzl1() + ",meiyzl="
							+ getVad_syzl1() + ",honghzl1=" + getVad_jrhzzl1()
							+ ",zuizhhzl=" + getVad_myjrhjszl1() + ",zhi="
							+ getVad_hff1() + ",shenhry=" + getVad_sh1()
							+ ",lury='" + visit.getRenymc() + "' where id="
							+ getVad_id1();
					con.getUpdate(sqlVad1);
				}

				// update挥发分第2次化验
				if (getVad_id2() != -1) {
					String sqlVad2 = "update gongyfsb set " + "zhillsb_id="
							+ rulmzlb_id + ",qimbh=" + getVad_ggh2()
							+ ",qimzl=" + getVad_ggzl2() + ",meiyzl="
							+ getVad_syzl2() + ",honghzl1=" + getVad_jrhzzl2()
							+ ",zuizhhzl=" + getVad_myjrhjszl2() + ",zhi="
							+ getVad_hff2() + ",shenhry=" + getVad_sh2()
							+ ",lury='" + visit.getRenymc() + "' where id="
							+ getVad_id2();
					con.getUpdate(sqlVad2);
				}

				// update挥发分第3次化验
				if (getVad_id3() != -1) {
					String sqlVad3 = "update gongyfsb set " + "zhillsb_id="
							+ rulmzlb_id + ",qimbh=" + getVad_ggh3()
							+ ",qimzl=" + getVad_ggzl3() + ",meiyzl="
							+ getVad_syzl3() + ",honghzl1=" + getVad_jrhzzl3()
							+ ",zuizhhzl=" + getVad_myjrhjszl3() + ",zhi="
							+ getVad_hff3() + ",shenhry=" + getVad_sh3()
							+ ",lury='" + visit.getRenymc() + "' where id="
							+ getVad_id3();
					con.getUpdate(sqlVad3);
				}

				// update挥发分第4次化验
				if (getVad_id4() != -1) {
					String sqlVad4 = "update gongyfsb set " + "zhillsb_id="
							+ rulmzlb_id + ",qimbh=" + getVad_ggh4()
							+ ",qimzl=" + getVad_ggzl4() + ",meiyzl="
							+ getVad_syzl4() + ",honghzl1=" + getVad_jrhzzl4()
							+ ",zuizhhzl=" + getVad_myjrhjszl4() + ",zhi="
							+ getVad_hff4() + ",shenhry=" + getVad_sh4()
							+ ",lury='" + visit.getRenymc() + "' where id="
							+ getVad_id4();
					con.getUpdate(sqlVad4);
				}

				// ////////////////////////灰分Aad/////////////////////
				// update灰分第1次化验
				if (getAad_id1() != -1) {
					String sqlAad1 = "update gongyfsb set " + "zhillsb_id="
							+ rulmzlb_id + ",qimbh=" + getAad_hmh1()
							+ ",qimzl=" + getAad_hmzl1() + ",meiyzl="
							+ getAad_syzl1() + ",honghzl1=" + getAad_jrhzzl1()
							+ ",zuizhhzl=" + getAad_jcxsyhzzl1() + ",zhi="
							+ getAad_hf1() + ",shengyzl=" + getAad_clwdzl1()
							+ ",shenhry=" + getAad_sh1() + ",lury='"
							+ visit.getRenymc() + "' where id=" + getAad_id1();
					con.getUpdate(sqlAad1);
				}
				// update灰分第2次化验
				if (getAad_id2() != -1) {
					String sqlAad2 = "update gongyfsb set " + "zhillsb_id="
							+ rulmzlb_id + ",qimbh=" + getAad_hmh2()
							+ ",qimzl=" + getAad_hmzl2() + ",meiyzl="
							+ getAad_syzl2() + ",honghzl1=" + getAad_jrhzzl2()
							+ ",zuizhhzl=" + getAad_jcxsyhzzl2() + ",zhi="
							+ getAad_hf2() + ",shengyzl=" + getAad_clwdzl2()
							+ ",shenhry=" + getAad_sh2() + ",lury='"
							+ visit.getRenymc() + "' where id=" + getAad_id2();
					con.getUpdate(sqlAad2);
				}
				// update灰分第3次化验
				if (getAad_id3() != -1) {
					String sqlAad3 = "update gongyfsb set " + "zhillsb_id="
							+ rulmzlb_id + ",qimbh=" + getAad_hmh3()
							+ ",qimzl=" + getAad_hmzl3() + ",meiyzl="
							+ getAad_syzl3() + ",honghzl1=" + getAad_jrhzzl3()
							+ ",zuizhhzl=" + getAad_jcxsyhzzl3() + ",zhi="
							+ getAad_hf3() + ",shengyzl=" + getAad_clwdzl3()
							+ ",shenhry=" + getAad_sh3() + ",lury='"
							+ visit.getRenymc() + "' where id=" + getAad_id3();
					con.getUpdate(sqlAad3);
				}
				// update灰分第4次化验
				if (getAad_id4() != -1) {
					String sqlAad4 = "update gongyfsb set " + "zhillsb_id="
							+ rulmzlb_id + ",qimbh=" + getAad_hmh4()
							+ ",qimzl=" + getAad_hmzl4() + ",meiyzl="
							+ getAad_syzl4() + ",honghzl1=" + getAad_jrhzzl4()
							+ ",zuizhhzl=" + getAad_jcxsyhzzl4() + ",zhi="
							+ getAad_hf4() + ",shengyzl=" + getAad_clwdzl4()
							+ ",shenhry=" + getAad_sh4() + ",lury='"
							+ visit.getRenymc() + "' where id=" + getAad_id4();
					con.getUpdate(sqlAad4);
				}

				// //////////////////////全硫//////////////////////
				// update全硫第1次化验
				if (getQl_id1() != -1) {
					String sqlQl1 = "update liufhyb set " + "zhillsb_id="
							+ rulmzlb_id + ",qimbh=" + getQl_qmh1() + ",qimzl="
							+ getQl_qmzl1() + ",meiyzl=" + getQl_syzl1()
							+ ",zhi=" + getQl_stad1() + ",shenhry="
							+ getQl_sh1() + ",lury='" + visit.getRenymc()
							+ "' where id=" + getQl_id1();
					con.getUpdate(sqlQl1);
				}
				// update全硫第2次化验
				if (getQl_id2() != -1) {
					String sqlQl2 = "update liufhyb set " + "zhillsb_id="
							+ rulmzlb_id + ",qimbh=" + getQl_qmh2() + ",qimzl="
							+ getQl_qmzl2() + ",meiyzl=" + getQl_syzl2()
							+ ",zhi=" + getQl_stad2() + ",shenhry="
							+ getQl_sh2() + ",lury='" + visit.getRenymc()
							+ "' where id=" + getQl_id1();
					con.getUpdate(sqlQl2);
				}
				// update全硫第3次化验
				if (getQl_id3() != -1) {
					String sqlQl3 = "update liufhyb set " + "zhillsb_id="
							+ rulmzlb_id + ",qimbh=" + getQl_qmh3() + ",qimzl="
							+ getQl_qmzl3() + ",meiyzl=" + getQl_syzl3()
							+ ",zhi=" + getQl_stad3() + ",shenhry="
							+ getQl_sh3() + ",lury='" + visit.getRenymc()
							+ "' where id=" + getQl_id3();
					con.getUpdate(sqlQl3);
				}
				// update全硫第4次化验
				if (getQl_id4() != -1) {
					String sqlQl4 = "update liufhyb set " + "zhillsb_id="
							+ rulmzlb_id + ",qimbh=" + getQl_qmh4() + ",qimzl="
							+ getQl_qmzl4() + ",meiyzl=" + getQl_syzl4()
							+ ",zhi=" + getQl_stad4() + ",shenhry="
							+ getQl_sh4() + ",lury='" + visit.getRenymc()
							+ "' where id=" + getQl_id1();
					con.getUpdate(sqlQl4);
				}
				// ////////////////发热量///////////////
				// update发热量第1次化验
				if (getFrl_id1() != -1) {
					String sqlFrl1 = "update danthyb set " + "zhillsb_id="
							+ rulmzlb_id + ",qimzl=" + getFrl_ggzl1()
							+ ",qimmyzl=" + getFrl_ggzlsyzl1() + ",meiyzl="
							+ getFrl_syzl1() + ",zhi=" + getFrl_qbad1()
							+ ",shenhry=" + getFrl_sh1() + ",lury='"
							+ visit.getRenymc() + "' where id=" + getFrl_id1();
					con.getUpdate(sqlFrl1);
				}
				// update发热量第2次化验
				if (getFrl_id2() != -1) {
					String sqlFrl2 = "update danthyb set " + "zhillsb_id="
							+ rulmzlb_id + ",qimzl=" + getFrl_ggzl2()
							+ ",qimmyzl=" + getFrl_ggzlsyzl2() + ",meiyzl="
							+ getFrl_syzl2() + ",zhi=" + getFrl_qbad2()
							+ ",shenhry=" + getFrl_sh2() + ",lury='"
							+ visit.getRenymc() + "' where id=" + getFrl_id2();
					con.getUpdate(sqlFrl2);
				}
				// update发热量第3次化验
				if (getFrl_id3() != -1) {
					String sqlFrl3 = "update danthyb set " + "zhillsb_id="
							+ rulmzlb_id + ",qimzl=" + getFrl_ggzl3()
							+ ",qimmyzl=" + getFrl_ggzlsyzl3() + ",meiyzl="
							+ getFrl_syzl3() + ",zhi=" + getFrl_qbad3()
							+ ",shenhry=" + getFrl_sh3() + ",lury='"
							+ visit.getRenymc() + "' where id=" + getFrl_id3();
					con.getUpdate(sqlFrl3);
				}
				// update发热量第4次化验
				if (getFrl_id4() != -1) {
					String sqlFrl4 = "update danthyb set " + "zhillsb_id="
							+ rulmzlb_id + ",qimzl=" + getFrl_ggzl4()
							+ ",qimmyzl=" + getFrl_ggzlsyzl4() + ",meiyzl="
							+ getFrl_syzl4() + ",zhi=" + getFrl_qbad4()
							+ ",shenhry=" + getFrl_sh4() + ",lury='"
							+ visit.getRenymc() + "' where id=" + getFrl_id4();
					con.getUpdate(sqlFrl4);
				}
			}
			pubzlb_id = rulmzlb_id;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
	}

	private void Submit()
	{
		JDBCcon con = new JDBCcon();
		Save();
		List _list = new ArrayList();
		String sql = "update rulmzlb set shenhzt = 1  where id = " + pubzlb_id;
		ResultSet rs = con.getResultSet(sql);
		
	}

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _DeleteChick = false;

	public void DeleteButton(IRequestCycle cycle) {
		_DeleteChick = true;
	}

	private boolean _SubmitChick = false;

	public void SubmitButton(IRequestCycle cycle) {
		_SubmitChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			Refurbish();
		}
		if (_DeleteChick) {
			_DeleteChick = false;
			Delete();
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}
		if (_SubmitChick) {
			_SubmitChick = false;
			Submit();
		}
	}

	public String getArrayScript() {
		return "aaa";
	}

	public void getSelectData() {
		List _editvalues = new ArrayList();
		JDBCcon JDBCcon = new JDBCcon();
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		visit.getDiancxxb_id();
		setShenhztModel(null);
		setQumwModel(null);
		getShenhztModel();
		getQumwModel();
		try {
			// /////////////////////////////氢值/////////////////////////////////
			String hmingc = "had";
			String qingsql = "select mingc from yuansxmb where zhuangt = 1";
			ResultSet rsh = con.getResultSet(qingsql);
			while (rsh.next()) {
				if (rsh.getString("mingc").equals("had")) {
					hmingc = "had";
				}
				if (rsh.getString("mingc").equals("hdaf")) {
					hmingc = "hdaf";
				}
			}

			String sql = "select mingc,zhi from xitxxb where leib = '质量' and danw = '入炉化验初始值' and zhuangt = 1 and mingc = '"
					+ hmingc + "'";
			ResultSet rs = con.getResultSet(sql);
			if (rs.next()) {
				Qing = "" + rs.getDouble("zhi");
			} else {
				String qingz = "select sum(f.jingz*z."
						+ hmingc
						+ ")/sum(f.jingz) as zhi from zhilb z,fahb f where f.zhilb_id = z.id  and f.diancxxb_id  = "
						+ visit.getDiancxxb_id();
				ResultSet qzrs = con.getResultSet(qingz);
				while (qzrs.next()) {
					Qing = "" + qzrs.getDouble("zhi");
				}
				qzrs.close();
			}
			rs.close();
			String SQL = 
				"select id,fenxrq\n" +
				"   from rulmzlb\n" + 
				"  where jizfzb_id = " + getProperId(_QumwModel, getQumwValue().getValue()) +
				"    and to_char(rulrq, 'yyyy-mm-dd') = '" + FormatDate(getRulrq()) + "'\n" + 
				"    and to_char(fenxrq, 'yyyy-mm-dd') = '" + FormatDate(getHuayrq()) + "'";
			
			ResultSet zlidrs = con.getResultSet(SQL);
			if(zlidrs.next()){
				setRulmzlb_id(zlidrs.getLong("id"));
				
			}
			zlidrs.close();
			// ///////////////////////////全水分Mt////////////////////////////

			String quansf_id = "(select id from fenxxmb where mingc = '全水分')";
			String sqlMt = "select qs.id as id,qs.xuh as xuh,qimbh,qimzl,meiyzl,qimzl+meiyzl as clpzlsyzl,honghzl1,zuizhhzl,zhi,r.id as rulmzlb_id,qs.shenhry "
					+ "from quansfhyb qs, rulmzlb r"
					+ " where qs.zhillsb_id = r.id   "
					+ " and qs.fenxxmb_id ="
					+ 1
					+ " and r.rulbzb_id =    "
					+ getProperId(_RulbzModel, getRulbzValue().getValue())
					+ " and r.jizfzb_id =    "
					+ getProperId(_QumwModel, getQumwValue().getValue())
					+ " and qs.zhillsb_id = " + rulmzlb_id
					+ " order by xuh";
			ResultSet rsMt = con.getResultSet(sqlMt);
			long zhiMt1 = 0;
			long zhiMt2 = 0;
			long zhiMt3 = 0;
			long zhiMt4 = 0;
			while (rsMt.next()) {

				if (rsMt.getLong("xuh") == 1) {
					setMt_id1(rsMt.getLong("id"));
					setMt_clph1(rsMt.getLong("qimbh"));
					setMt_clpzl1(rsMt.getLong("qimzl"));
					setMt_syzl1(rsMt.getLong("meiyzl"));
					setMt_clpzlsyzl1(rsMt.getLong("qimzl")
							+ rsMt.getLong("meiyzl"));
					setMt_hhzzl1(rsMt.getLong("honghzl1"));
					setMt_jcxsyhzzl1(rsMt.getLong("zuizhhzl"));
					zhiMt1 = rsMt.getLong("zhi");
					setRulmzlb_id(rsMt.getLong("rulmzlb_id"));
					setMt_qsf1(zhiMt1);
					setMt_sh1(rsMt.getString("shenhry"));

				}
				if (rsMt.getLong("xuh") == 2) {
					setMt_id2(rsMt.getLong("id"));
					setMt_clph2(rsMt.getLong("qimbh"));
					setMt_clpzl2(rsMt.getLong("qimzl"));
					setMt_syzl2(rsMt.getLong("meiyzl"));
					setMt_clpzlsyzl2(rsMt.getLong("qimzl")
							+ rsMt.getLong("meiyzl"));
					setMt_hhzzl2(rsMt.getLong("honghzl1"));
					setMt_jcxsyhzzl2(rsMt.getLong("zuizhhzl"));
					zhiMt2 = rsMt.getLong("zhi");
					setMt_qsf2(zhiMt2);
					setMt_sh2(rsMt.getString("shenhry"));
					// Mt_sh2 = getProperValue(_shenhryModel,_sh);
					
				}
				if (rsMt.getLong("xuh") == 3) {
					setMt_id3(rsMt.getLong("id"));
					setMt_clph3(rsMt.getLong("qimbh"));
					setMt_clpzl3(rsMt.getLong("qimzl"));
					setMt_syzl3(rsMt.getLong("meiyzl"));
					setMt_clpzlsyzl3(rsMt.getLong("qimzl")
							+ rsMt.getLong("meiyzl"));
					setMt_hhzzl3(rsMt.getLong("honghzl1"));
					setMt_jcxsyhzzl3(rsMt.getLong("zuizhhzl"));
					zhiMt3 = rsMt.getLong("zhi");
					setMt_qsf3(zhiMt3);
					setMt_sh3(rsMt.getString("shenhry"));
					// Mt_sh3 = getProperValue(_ShenhztModel,_sh);
				}
				if (rsMt.getLong("xuh") == 4) {
					setMt_id4(rsMt.getLong("id"));
					setMt_clph4(rsMt.getLong("qimbh"));
					setMt_clpzl4(rsMt.getLong("qimzl"));
					setMt_syzl4(rsMt.getLong("meiyzl"));
					setMt_clpzlsyzl4(rsMt.getLong("qimzl")
							+ rsMt.getLong("meiyzl"));
					setMt_hhzzl4(rsMt.getLong("honghzl1"));
					setMt_jcxsyhzzl4(rsMt.getLong("zuizhhzl"));
					zhiMt4 = rsMt.getLong("zhi");
					setMt_qsf4(zhiMt4);
					setMt_sh4(rsMt.getString("shenhry"));
					// Mt_sh4 = getProperValue(_ShenhztModel,_sh);
				}
				long pjzMt = zhiMt1 + zhiMt2 + zhiMt3 + zhiMt4;
				if (pjzMt == 0) {
					setMt_pjz(0.0D);
				} else {
					setMt_pjz(pjzMt * 0.25);
				}
			}
			// ///////////////////////////水分Mad////////////////////////////

			String shuif_id = "(select id from fenxxmb where mingc = '水分')";
			String sqlMad = "select qs.id as id,qs.xuh as xuh,qimbh,qimzl,meiyzl,qimzl+meiyzl as clpzlsyzl,honghzl1,zuizhhzl,zhi,r.id as rulmzlb_id,qs.shenhry "
					+ "  from gongyfsb qs, rulmzlb r, fenxxmb xm "
					+ "  where qs.zhillsb_id = r.id   "
					+ "  and qs.fenxxmb_id = xm.id    "
					+ "  and xm.id = "
					+ 2
					+ "  and r.rulbzb_id =   "
					+ getProperId(_RulbzModel, getRulbzValue().getValue())
					+ "  and r.jizfzb_id =  "
					+ getProperId(_QumwModel, getQumwValue().getValue())
					+ " and qs.zhillsb_id = " + rulmzlb_id
					+ " order by xuh";
			ResultSet rsMad = con.getResultSet(sqlMad);
			long zhiMad1 = 0;
			long zhiMad2 = 0;
			long zhiMad3 = 0;
			long zhiMad4 = 0;
			while (rsMad.next()) {
				if (rsMad.getLong("xuh") == 1) {
					setMad_id1(rsMad.getLong("id"));
					setMad_clph1(rsMad.getLong("qimbh"));
					setMad_clpzl1(rsMad.getLong("qimzl"));
					setMad_syzl1(rsMad.getLong("meiyzl"));
					setMad_clpzlsyzl1(rsMad.getLong("qimzl")
							+ rsMad.getLong("meiyzl"));
					setMad_hhzzl1(rsMad.getLong("honghzl1"));
					setMad_jcxsyhzzl1(rsMad.getLong("zuizhhzl"));
					zhiMad1 = rsMad.getLong("zhi");
					setMad_qsf1(zhiMad1);
					setMad_sh1(rsMad.getString("shenhry"));
					setRulmzlb_id(rsMad.getLong("rulmzlb_id"));
					// setMad_sh1 = getProperValue(_shenhryModel,_sh);
				}
				if (rsMad.getLong("xuh") == 2) {
					setMad_id2(rsMad.getLong("id"));
					setMad_clph2(rsMad.getLong("qimbh"));
					setMad_clpzl2(rsMad.getLong("qimzl"));
					setMad_syzl2(rsMad.getLong("meiyzl"));
					setMad_clpzlsyzl2(rsMad.getLong("qimzl")
							+ rsMad.getLong("meiyzl"));
					setMad_hhzzl2(rsMad.getLong("honghzl1"));
					setMad_jcxsyhzzl2(rsMad.getLong("zuizhhzl"));
					zhiMad2 = rsMad.getLong("zhi");
					setMad_qsf2(zhiMad2);
					setMad_sh2(rsMad.getString("shenhry"));
					// setMad_sh2 = getProperValue(_shenhryModel,_sh);
				}
				if (rsMad.getLong("xuh") == 3) {
					setMad_id3(rsMad.getLong("id"));
					setMad_clph3(rsMad.getLong("qimbh"));
					setMad_clpzl3(rsMad.getLong("qimzl"));
					setMad_syzl3(rsMad.getLong("meiyzl"));
					setMad_clpzlsyzl3(rsMad.getLong("qimzl")
							+ rsMad.getLong("meiyzl"));
					setMad_hhzzl3(rsMad.getLong("honghzl1"));
					setMad_jcxsyhzzl3(rsMad.getLong("zuizhhzl"));
					zhiMad3 = rsMad.getLong("zhi");
					setMad_qsf3(zhiMad3);
					setMad_sh3(rsMad.getString("shenhry"));
					// setMad_sh3 = getProperValue(_shenhryModel,_sh);
				}
				if (rsMad.getLong("xuh") == 4) {
					setMad_id4(rsMad.getLong("id"));
					setMad_clph4(rsMad.getLong("qimbh"));
					setMad_clpzl4(rsMad.getLong("qimzl"));
					setMad_syzl4(rsMad.getLong("meiyzl"));
					setMad_clpzlsyzl4(rsMad.getLong("qimzl")
							+ rsMad.getLong("meiyzl"));
					setMad_hhzzl4(rsMad.getLong("honghzl1"));
					setMad_jcxsyhzzl4(rsMad.getLong("zuizhhzl"));
					zhiMad4 = rsMad.getLong("zhi");
					setMad_qsf4(zhiMad4);
					setMad_sh4(rsMad.getString("shenhry"));
					// setMad_sh4 = getProperValue(_shenhryModel,_sh);
				}
				long Madpjz = zhiMad1 + zhiMad2 + zhiMad3 + zhiMad4;
				if (Madpjz == 0) {
					setMad_pjz(0.0D);
				} else {
					setMad_pjz(Madpjz * 0.25);
				}

			}
			// ///////////////////////////挥发分Vad////////////////////////////
			String huiff_id = "(select id from fenxxmb where mingc = '挥发分')";
			String sqlVad = "select qs.id as id,qs.xuh as xuh,qimbh,qimzl,qimzl+meiyzl as ggzzsyzz,meiyzl,honghzl1,shiqzl,"
					+ "qs.zhi*100/(100-Mad-Aad) as vdaf,r.id as rulmzlb_id,qs.zhi,qs.shenhry "
					+ "from gongyfsb qs, rulmzlb r, fenxxmb xm   "
					+ "where qs.zhillsb_id = r.id     and qs.fenxxmb_id = xm.id     "
					+ "and xm.id = "
					+ 3
					+ " and r.rulbzb_id =    "
					+ getProperId(_RulbzModel, getRulbzValue().getValue())
					+ " and r.jizfzb_id =    "
					+ getProperId(_QumwModel, getQumwValue().getValue())
					+ " and qs.zhillsb_id = " + rulmzlb_id
					+ " order by xuh";
			ResultSet rsVad = con.getResultSet(sqlVad);
			long zhiVad1 = 0;
			long zhiVad2 = 0;
			long zhiVad3 = 0;
			long zhiVad4 = 0;
			while (rsVad.next()) {
				if (rsVad.getLong("xuh") == 1) {
					setVad_id1(rsVad.getLong("id"));
					setVad_ggh1(rsVad.getLong("qimbh"));
					setVad_ggzl1(rsVad.getLong("qimzl"));
					setVad_ggzlsyzl1(rsVad.getLong("ggzzsyzz"));
					setVad_syzl1(rsVad.getLong("meiyzl"));
					setVad_jrhzzl1(rsVad.getLong("honghzl1"));
					setVad_myjrhjszl1(rsVad.getLong("shiqzl"));
					setVad_hffqsf1(rsVad.getLong("vdaf"));
					zhiVad1 = rsVad.getLong("zhi");
					setVad_hff1(zhiVad1);
					setVad_sh1(rsVad.getString("shenhry"));
					setRulmzlb_id(rsVad.getLong("rulmzlb_id"));
					// setVad_sh1 = getProperValue(_shenhryModel,_sh);
				}
				if (rsVad.getLong("xuh") == 2) {
					setVad_id2(rsVad.getLong("id"));
					setVad_ggh2(rsVad.getLong("qimbh"));
					setVad_ggzl2(rsVad.getLong("qimzl"));
					setVad_ggzlsyzl2(rsVad.getLong("ggzzsyzz"));
					setVad_syzl2(rsVad.getLong("meiyzl"));
					setVad_jrhzzl2(rsVad.getLong("honghzl1"));
					setVad_myjrhjszl2(rsVad.getLong("shiqzl"));
					setVad_hffqsf2(rsVad.getLong("vdaf"));
					zhiVad2 = rsVad.getLong("zhi");
					setVad_hff2(zhiVad2);
					setVad_sh2(rsVad.getString("shenhry"));
					// setVad_sh2 = getProperValue(_shenhryModel,_sh);
				}
				if (rsVad.getLong("xuh") == 3) {
					setVad_id3(rsVad.getLong("id"));
					setVad_ggh3(rsVad.getLong("qimbh"));
					setVad_ggzl3(rsVad.getLong("qimzl"));
					setVad_ggzlsyzl3(rsVad.getLong("ggzzsyzz"));
					setVad_syzl3(rsVad.getLong("meiyzl"));
					setVad_jrhzzl3(rsVad.getLong("honghzl1"));
					setVad_myjrhjszl3(rsVad.getLong("shiqzl"));
					setVad_hffqsf3(rsVad.getLong("vdaf"));
					zhiVad3 = rsVad.getLong("zhi");
					setVad_hff3(zhiVad3);
					setVad_sh3(rsVad.getString("shenhry"));
					// setVad_sh3 = getProperValue(_shenhryModel,_sh);
				}
				if (rsVad.getLong("xuh") == 4) {
					setVad_id4(rsVad.getLong("id"));
					setVad_ggh4(rsVad.getLong("qimbh"));
					setVad_ggzl4(rsVad.getLong("qimzl"));
					setVad_ggzlsyzl4(rsVad.getLong("ggzzsyzz"));
					setVad_syzl4(rsVad.getLong("meiyzl"));
					setVad_jrhzzl4(rsVad.getLong("honghzl1"));
					setVad_myjrhjszl4(rsVad.getLong("shiqzl"));
					setVad_hffqsf4(rsVad.getLong("vdaf"));
					zhiVad4 = rsVad.getLong("zhi");
					setVad_hff4(zhiVad4);
					setVad_sh4(rsVad.getString("shenhry"));
					// setVad_sh4 = getProperValue(_shenhryModel,_sh);
				}
				long Vadpjz = zhiVad1 + zhiVad2 + zhiVad3 + zhiVad4;
				if (Vadpjz == 0) {
					setVad_pjz(0.0D);
				} else {
					setVad_pjz(Vadpjz * 0.25);
				}

			}

			// ///////////////////////////灰分Aad////////////////////////////
			String huif_id = " (select id from fenxxmb where mingc = '灰分')";
			String sqlAad = "select qs.id as id,qs.xuh as xuh,qimbh,qimzl,qimzl+meiyzl as hmzlsyzl,meiyzl,honghzl1,zuizhhzl,shengyzl,zhi,r.id as rulmzlb_id,qs.shenhry "
					+ "from gongyfsb qs, rulmzlb r, fenxxmb xm   where qs.zhillsb_id = r.id     and qs.fenxxmb_id = xm.id    "
					+ " and xm.id = "
					+ 4
					+ "  and r.rulbzb_id =  "
					+ getProperId(_RulbzModel, getRulbzValue().getValue())
					+ "  and r.jizfzb_id =   "
					+ getProperId(_QumwModel, getQumwValue().getValue())
					+ " and qs.zhillsb_id = " + rulmzlb_id
					+ " order by xuh";
			ResultSet rsAad = con.getResultSet(sqlAad);
			long zhiAad1 = 0;
			long zhiAad2 = 0;
			long zhiAad3 = 0;
			long zhiAad4 = 0;
			while (rsAad.next()) {
				if (rsAad.getLong("xuh") == 1) {
					setAad_id1(rsAad.getLong("id"));
					setAad_hmh1(rsAad.getLong("qimbh"));
					setAad_hmzl1(rsAad.getLong("qimzl"));
					setAad_hmzlsyzl1(rsAad.getLong("meiyzl"));
					setAad_syzl1(rsAad.getLong("meiyzl"));
					setAad_jrhzzl1(rsAad.getLong("honghzl1"));
					setAad_jcxsyhzzl1(rsAad.getLong("zuizhhzl"));
					setAad_clwdzl1(rsAad.getLong("shengyzl"));
					zhiAad1 = rsAad.getLong("zhi");
					setAad_hf1(zhiAad1);
					setRulmzlb_id(rsAad.getLong("rulmzlb_id"));
					setAad_sh1(rsAad.getString("shenhry"));
				}
				if (rsAad.getLong("xuh") == 2) {
					setAad_id2(rsAad.getLong("id"));
					setAad_hmh2(rsAad.getLong("qimbh"));
					setAad_hmzl2(rsAad.getLong("qimzl"));
					setAad_hmzlsyzl2(rsAad.getLong("meiyzl"));
					setAad_syzl2(rsAad.getLong("meiyzl"));
					setAad_jrhzzl2(rsAad.getLong("honghzl1"));
					setAad_jcxsyhzzl2(rsAad.getLong("zuizhhzl"));
					setAad_clwdzl2(rsAad.getLong("shengyzl"));
					zhiAad2 = rsAad.getLong("zhi");
					setAad_hf2(zhiAad2);
					setAad_sh2(rsAad.getString("shenhry"));
				}
				if (rsAad.getLong("xuh") == 3) {
					setAad_id3(rsAad.getLong("id"));
					setAad_hmh3(rsAad.getLong("qimbh"));
					setAad_hmzl3(rsAad.getLong("qimzl"));
					setAad_hmzlsyzl3(rsAad.getLong("meiyzl"));
					setAad_syzl3(rsAad.getLong("meiyzl"));
					setAad_jrhzzl3(rsAad.getLong("honghzl1"));
					setAad_jcxsyhzzl3(rsAad.getLong("zuizhhzl"));
					setAad_clwdzl3(rsAad.getLong("shengyzl"));
					zhiAad3 = rsAad.getLong("zhi");
					setAad_hf3(zhiAad3);
					setAad_sh3(rsAad.getString("shenhry"));
				}
				if (rsAad.getLong("xuh") == 4) {
					setAad_id4(rsAad.getLong("id"));
					setAad_hmh4(rsAad.getLong("qimbh"));
					setAad_hmzl4(rsAad.getLong("qimzl"));
					setAad_hmzlsyzl4(rsAad.getLong("meiyzl"));
					setAad_syzl4(rsAad.getLong("meiyzl"));
					setAad_jrhzzl4(rsAad.getLong("honghzl1"));
					setAad_jcxsyhzzl4(rsAad.getLong("zuizhhzl"));
					setAad_clwdzl4(rsAad.getLong("shengyzl"));
					zhiAad4 = rsAad.getLong("zhi");
					setAad_hf4(zhiAad4);
					setAad_sh4(rsAad.getString("shenhry"));
				}
				long Aadpjz = zhiAad1 + zhiAad2 + zhiAad3 + zhiAad4;
				if (Aadpjz == 0) {
					setAad_pjz(0.0D);
				} else {
					setAad_pjz(Aadpjz * 0.25);
				}
			}

			// ///////////////////////////全硫////////////////////////////
			String quanl_id = "(select id from fenxxmb where mingc = '全硫')";
			String sqlQl = "select qs.id as id,qs.xuh as xuh,huayyqbh,qimzl,qimzl+meiyzl as qmzlsyzl,meiyzl,zhi,r.id as rulmzlb_id,zhi*100/(100-Mad) as std,qs.shenhry as sh"
					+ " from liufhyb qs, rulmzlb r, fenxxmb xm   "
					+ " where qs.zhillsb_id = r.id     and qs.fenxxmb_id = xm.id  "
					+ " and xm.id = "
					+ 6
					+ " and r.rulbzb_id =   "
					+ getProperId(_RulbzModel, getRulbzValue().getValue())
					+ " and r.jizfzb_id =   "
					+ getProperId(_QumwModel, getQumwValue().getValue())
					+ " and qs.zhillsb_id = " + rulmzlb_id
					+ " order by xuh";
			ResultSet rsQl = con.getResultSet(sqlQl);
			long zhiQl1 = 0;
			long zhiQl2 = 0;
			long zhiQl3 = 0;
			long zhiQl4 = 0;
			while (rsQl.next()) {
				if (rsQl.getLong("xuh") == 1) {
					setQl_id1(rsQl.getLong("id"));
					setQl_qmh1(rsQl.getLong("huayyqbh"));
					setQl_qmzl1(rsQl.getLong("qimzl"));
					setQl_qmzlsyzl1(rsQl.getLong("qmzlsyzl"));
					setQl_syzl1(rsQl.getLong("meiyzl"));
					zhiQl1 = rsQl.getLong("zhi");
					setQl_stad1(zhiQl1);
					setQl_std1(rsQl.getLong("std"));
					setQl_sh1(rsQl.getString("sh"));
					setRulmzlb_id(rsQl.getLong("rulmzlb_id"));
				}
				if (rsQl.getLong("xuh") == 2) {
					setQl_id2(rsQl.getLong("id"));
					setQl_qmh2(rsQl.getLong("huayyqbh"));
					setQl_qmzl2(rsQl.getLong("qimzl"));
					setQl_qmzlsyzl2(rsQl.getLong("qmzlsyzl"));
					setQl_syzl2(rsQl.getLong("meiyzl"));
					zhiQl2 = rsQl.getLong("zhi");
					setQl_stad2(zhiQl2);
					setQl_std2(rsQl.getLong("std"));
					setQl_sh2(rsQl.getString("sh"));
				}
				if (rsQl.getLong("xuh") == 3) {
					setQl_id3(rsQl.getLong("id"));
					setQl_qmh3(rsQl.getLong("huayyqbh"));
					setQl_qmzl3(rsQl.getLong("qimzl"));
					setQl_qmzlsyzl3(rsQl.getLong("qmzlsyzl"));
					setQl_syzl3(rsQl.getLong("meiyzl"));
					zhiQl3 = rsQl.getLong("zhi");
					setQl_stad3(zhiQl3);
					setQl_std3(rsQl.getLong("std"));
					setQl_sh3(rsQl.getString("sh"));
				}
				if (rsQl.getLong("xuh") == 4) {
					setQl_id4(rsQl.getLong("id"));
					setQl_qmh4(rsQl.getLong("huayyqbh"));
					setQl_qmzl4(rsQl.getLong("qimzl"));
					setQl_qmzlsyzl4(rsQl.getLong("qmzlsyzl"));
					setQl_syzl4(rsQl.getLong("meiyzl"));
					zhiQl4 = rsQl.getLong("zhi");
					setQl_stad4(zhiQl4);
					setQl_std4(rsQl.getLong("std"));
					setQl_sh4(rsQl.getString("sh"));
				}
				long Qlpjz = zhiQl1 + zhiQl2 + zhiQl3 + zhiQl4;
				if (Qlpjz == 0) {
					setQl_pjz(0.0D);
				} else {
					setQl_pjz(Qlpjz * 0.25);
				}
			}

			// ///////////////////////////发热量////////////////////////////
			String farl_id = "(select id from fenxxmb where mingc = '发热量')";
			String sqlFrl = "select d.id as id,d.xuh as xuh,d.qimzl,d.qimzl+d.meiyzl as ggzlsyzl,r.id as rulmzlb_id,"
					+ "d.meiyzl,d.zhi,d.zhi-(0.0941*l.zhi+1*d.zhi) as qgrad,"
					+ "(d.zhi-(0.0941*l.zhi+1*d.zhi)-0.206*had)*(100-q.zhi)/(100-g.zhi)-0.023*q.zhi as qnet,d.shenhry "
					+ "from danthyb d,quansfhyb q,liufhyb l, gongyfsb g,rulmzlb r, fenxxmb xm "
					+ " where d.zhillsb_id = r.id   and d.fenxxmb_id = xm.id  "
					+ " and  xm.id =   "
					+ 5
					+ "  and r.rulbzb_id =  "
					+ getProperId(_RulbzModel, getRulbzValue().getValue())
					+ "  and r.jizfzb_id = "
					+ getProperId(_QumwModel, getQumwValue().getValue())
					+ " and d.zhillsb_id = " + rulmzlb_id
					+ " order by xuh";
			ResultSet rsFrl = con.getResultSet(sqlFrl);
			long zhiFrl1 = 0;
			long zhiFrl2 = 0;
			long zhiFrl3 = 0;
			long zhiFrl4 = 0;
			while (rsFrl.next()) {
				if (rsFrl.getLong("xuh") == 1) {
					setFrl_id1(rsFrl.getLong("id"));
					setFrl_ggzl1(rsFrl.getLong("qimzl"));
					setFrl_ggzlsyzl1(rsFrl.getLong("ggzlsyzl"));
					setFrl_syzl1(rsFrl.getLong("meiyzl"));
					zhiFrl1 = rsFrl.getLong("zhi");
					setFrl_qbad1(zhiFrl1);
					setFrl_qgrad1(rsFrl.getLong("qgrad"));
					setFrl_qnetar1(rsFrl.getLong("qnet"));
					setRulmzlb_id(rsFrl.getLong("rulmzlb_id"));
				}
				if (rsFrl.getLong("xuh") == 2) {
					setFrl_id2(rsFrl.getLong("id"));
					setFrl_ggzl2(rsFrl.getLong("qimzl"));
					setFrl_ggzlsyzl2(rsFrl.getLong("ggzlsyzl"));
					setFrl_syzl2(rsFrl.getLong("meiyzl"));
					zhiFrl2 = rsFrl.getLong("zhi");
					setFrl_qbad2(zhiFrl2);
					setFrl_qgrad2(rsFrl.getLong("qgrad"));
					setFrl_qnetar2(rsFrl.getLong("qnet"));
				}
				if (rsFrl.getLong("xuh") == 3) {
					setFrl_id3(rsFrl.getLong("id"));
					setFrl_ggzl3(rsFrl.getLong("qimzl"));
					setFrl_ggzlsyzl3(rsFrl.getLong("ggzlsyzl"));
					setFrl_syzl3(rsFrl.getLong("meiyzl"));
					zhiFrl3 = rsFrl.getLong("zhi");
					setFrl_qbad3(zhiFrl3);
					setFrl_qgrad3(rsFrl.getLong("qgrad"));
					setFrl_qnetar3(rsFrl.getLong("qnet"));
				}
				if (rsFrl.getLong("xuh") == 4) {
					setFrl_id4(rsFrl.getLong("id"));
					setFrl_ggzl4(rsFrl.getLong("qimzl"));
					setFrl_ggzlsyzl4(rsFrl.getLong("ggzlsyzl"));
					setFrl_syzl4(rsFrl.getLong("meiyzl"));
					zhiFrl4 = rsFrl.getLong("zhi");
					setFrl_qbad4(zhiFrl4);
					setFrl_qgrad4(rsFrl.getLong("qgrad"));
					setFrl_qnetar4(rsFrl.getLong("qnet"));
				}

				long Frlpjz = zhiFrl1 + zhiFrl2 + zhiFrl3 + zhiFrl4;
				if (Frlpjz == 0) {
					setFrl_pjz(0.0D);
				} else {
					setFrl_pjz(Frlpjz * 0.25);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();

		}

	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		getShenhztModel();
		getQumwModel();
//		setMt_id1(-1);
//		setMt_clph1(0.0D);
//		setMt_clpzl1(0.0D);
//		setMt_clpzlsyzl1(0.0D);
//		setMt_syzl1(0.0D);
//		setMt_hhzzl1(0.0D);
//		setMt_jcxsyhzzl1(0.0D);
//		setMt_qsf1(0.0D);
//		setMt_pjz(0.0D);
//		setMt_sh1(null);
//
//		setMad_id1(-1);
//		setMad_clph1(0.0D);
//		setMad_clpzl1(0.0D);
//		setMad_clpzlsyzl1(0.0D);
//		setMad_syzl1(0.0D);
//		setMad_hhzzl1(0.0D);
//		setMad_jcxsyhzzl1(0.0D);
//		setMad_qsf1(0.0D);
//		setMad_pjz(0.0D);
//		setMad_sh1(null);
//
//		setVad_id1(-1);
//		setVad_ggh1(0.0D);
//		setVad_ggzl1(0.0D);
//		setVad_ggzlsyzl1(0.0D);
//		setVad_syzl1(0.0D);
//		setVad_jrhzzl1(0.0D);
//		setVad_myjrhjszl1(0.0D);
//		setVad_hffqsf1(0.0D);
//		setVad_hff1(0.0D);
//		setVad_pjz(0.0D);
//		setVad_sh1("");
//
//		setAad_id1(-1);
//		setAad_hmh1(0.0D);
//		setAad_hmzl1(0.0D);
//		setAad_hmzlsyzl1(0.0D);
//		setAad_syzl1(0.0D);
//		setAad_jrhzzl1(0.0D);
//		setAad_jcxsyhzzl1(0.0D);
//		setAad_clwdzl1(0.0D);
//		setAad_hf1(0.0D);
//		setAad_pjz(0.0D);
//		setAad_sh1(null);
//
//		setQl_id1(-1);
//		setQl_qmh1(0.0D);
//		setQl_qmzl1(0.0D);
//		setQl_qmzlsyzl1(0.0D);
//		setQl_syzl1(0.0D);
//		setQl_stad1(0.0D);
//		setQl_std1(0.0D);
//		setQl_pjz(0.0D);
//		setQl_sh1(null);
//
//		setFrl_id1(-1);
//		setFrl_ggzl1(0.0D);
//		setFrl_ggzlsyzl1(0.0D);
//		setFrl_syzl1(0.0D);
//		setFrl_qbad1(0.0D);
//		setFrl_qgrad1(0.0D);
//		setFrl_qnetar1(0.0D);
//		setFrl_pjz(0.0D);
//		setFrl_sh1(null);
//
//		// /////2/////////
//		setMt_id2(-1);
//		setMt_clph2(0.0D);
//		setMt_clpzl2(0.0D);
//		setMt_clpzlsyzl2(0.0D);
//		setMt_syzl2(0.0D);
//		setMt_hhzzl2(0.0D);
//		setMt_jcxsyhzzl2(0.0D);
//		setMt_qsf2(0.0D);
//		setMt_pjz(0.0D);
//		setMt_sh2(null);
//
//		setMad_id2(-1);
//		setMad_clph2(0.0D);
//		setMad_clpzl2(0.0D);
//		setMad_clpzlsyzl2(0.0D);
//		setMad_syzl2(0.0D);
//		setMad_hhzzl2(0.0D);
//		setMad_jcxsyhzzl2(0.0D);
//		setMad_qsf2(0.0D);
//		setMad_pjz(0.0D);
//		setMad_sh2(null);
//
//		setVad_id2(-1);
//		setVad_ggh2(0.0D);
//		setVad_ggzl2(0.0D);
//		setVad_ggzlsyzl2(0.0D);
//		setVad_syzl2(0.0D);
//		setVad_jrhzzl2(0.0D);
//		setVad_myjrhjszl2(0.0D);
//		setVad_hffqsf2(0.0D);
//		setVad_hff2(0.0D);
//		setVad_pjz(0.0D);
//		setVad_sh2("");
//
//		setAad_id2(-1);
//		setAad_hmh2(0.0D);
//		setAad_hmzl2(0.0D);
//		setAad_hmzlsyzl2(0.0D);
//		setAad_syzl2(0.0D);
//		setAad_jrhzzl2(0.0D);
//		setAad_jcxsyhzzl2(0.0D);
//		setAad_clwdzl2(0.0D);
//		setAad_hf2(0.0D);
//		setAad_pjz(0.0D);
//		setAad_sh2(null);
//
//		setQl_id2(-1);
//		setQl_qmh2(0.0D);
//		setQl_qmzl2(0.0D);
//		setQl_qmzlsyzl2(0.0D);
//		setQl_syzl2(0.0D);
//		setQl_stad2(0.0D);
//		setQl_std2(0.0D);
//		setQl_pjz(0.0D);
//		setQl_sh2(null);
//
//		setFrl_id2(-1);
//		setFrl_ggzl2(0.0D);
//		setFrl_ggzlsyzl2(0.0D);
//		setFrl_syzl2(0.0D);
//		setFrl_qbad2(0.0D);
//		setFrl_qgrad2(0.0D);
//		setFrl_qnetar2(0.0D);
//		setFrl_pjz(0.0D);
//		setFrl_sh2(null);
//		// ///////3///////
//		setMt_id3(-1);
//		setMt_clph3(0.0D);
//		setMt_clpzl3(0.0D);
//		setMt_clpzlsyzl3(0.0D);
//		setMt_syzl3(0.0D);
//		setMt_hhzzl3(0.0D);
//		setMt_jcxsyhzzl3(0.0D);
//		setMt_qsf3(0.0D);
//		setMt_pjz(0.0D);
//		setMt_sh3(null);
//
//		setMad_id3(-1);
//		setMad_clph3(0.0D);
//		setMad_clpzl3(0.0D);
//		setMad_clpzlsyzl3(0.0D);
//		setMad_syzl3(0.0D);
//		setMad_hhzzl3(0.0D);
//		setMad_jcxsyhzzl3(0.0D);
//		setMad_qsf3(0.0D);
//		setMad_pjz(0.0D);
//		setMad_sh3(null);
//
//		setVad_id3(-1);
//		setVad_ggh3(0.0D);
//		setVad_ggzl3(0.0D);
//		setVad_ggzlsyzl3(0.0D);
//		setVad_syzl3(0.0D);
//		setVad_jrhzzl3(0.0D);
//		setVad_myjrhjszl3(0.0D);
//		setVad_hffqsf3(0.0D);
//		setVad_hff3(0.0D);
//		setVad_pjz(0.0D);
//		setVad_sh3("");
//
//		setAad_id3(-1);
//		setAad_hmh3(0.0D);
//		setAad_hmzl3(0.0D);
//		setAad_hmzlsyzl3(0.0D);
//		setAad_syzl3(0.0D);
//		setAad_jrhzzl3(0.0D);
//		setAad_jcxsyhzzl3(0.0D);
//		setAad_clwdzl3(0.0D);
//		setAad_hf3(0.0D);
//		setAad_pjz(0.0D);
//		setAad_sh3(null);
//
//		setQl_id3(-1);
//		setQl_qmh3(0.0D);
//		setQl_qmzl3(0.0D);
//		setQl_qmzlsyzl3(0.0D);
//		setQl_syzl3(0.0D);
//		setQl_stad3(0.0D);
//		setQl_std3(0.0D);
//		setQl_pjz(0.0D);
//		setQl_sh3(null);
//
//		setFrl_id3(-1);
//		setFrl_ggzl3(0.0D);
//		setFrl_ggzlsyzl3(0.0D);
//		setFrl_syzl3(0.0D);
//		setFrl_qbad3(0.0D);
//		setFrl_qgrad3(0.0D);
//		setFrl_qnetar3(0.0D);
//		setFrl_pjz(0.0D);
//		setFrl_sh3(null);
//		// ////////4//////////
//		setMt_id4(-1);
//		setMt_clph4(0.0D);
//		setMt_clpzl4(0.0D);
//		setMt_clpzlsyzl4(0.0D);
//		setMt_syzl4(0.0D);
//		setMt_hhzzl4(0.0D);
//		setMt_jcxsyhzzl4(0.0D);
//		setMt_qsf4(0.0D);
//		setMt_pjz(0.0D);
//		setMt_sh4(null);
//
//		setMad_id4(-1);
//		setMad_clph4(0.0D);
//		setMad_clpzl4(0.0D);
//		setMad_clpzlsyzl4(0.0D);
//		setMad_syzl4(0.0D);
//		setMad_hhzzl4(0.0D);
//		setMad_jcxsyhzzl4(0.0D);
//		setMad_qsf4(0.0D);
//		setMad_pjz(0.0D);
//		setMad_sh4(null);
//
//		setVad_id4(-1);
//		setVad_ggh4(0.0D);
//		setVad_ggzl4(0.0D);
//		setVad_ggzlsyzl4(0.0D);
//		setVad_syzl4(0.0D);
//		setVad_jrhzzl4(0.0D);
//		setVad_myjrhjszl4(0.0D);
//		setVad_hffqsf4(0.0D);
//		setVad_hff4(0.0D);
//		setVad_pjz(0.0D);
//		setVad_sh4(null);
//
//		setAad_id4(-1);
//		setAad_hmh4(0.0D);
//		setAad_hmzl4(0.0D);
//		setAad_hmzlsyzl4(0.0D);
//		setAad_syzl4(0.0D);
//		setAad_jrhzzl4(0.0D);
//		setAad_jcxsyhzzl4(0.0D);
//		setAad_clwdzl4(0.0D);
//		setAad_hf4(0.0D);
//		setAad_pjz(0.0D);
//		setAad_sh4(null);
//
//		setQl_id4(-1);
//		setQl_qmh4(0.0D);
//		setQl_qmzl4(0.0D);
//		setQl_qmzlsyzl4(0.0D);
//		setQl_syzl4(0.0D);
//		setQl_stad4(0.0D);
//		setQl_std4(0.0D);
//		setQl_pjz(0.0D);
//		setQl_sh4(null);
//
//		setFrl_id4(-1);
//		setFrl_ggzl4(0.0D);
//		setFrl_ggzlsyzl4(0.0D);
//		setFrl_syzl4(0.0D);
//		setFrl_qbad4(0.0D);
//		setFrl_qgrad4(0.0D);
//		setFrl_qnetar4(0.0D);
//		setFrl_pjz(0.0D);
//		setFrl_sh4(null);
		
		if (shenhzt)
		{
			
		}
	}

	public boolean isEditdisable() {
		return editdisable;
	}

	public void setEditdisable(boolean editdisable) {
		this.editdisable = editdisable;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public boolean isEditdisable1() {
		return editdisable1;
	}

	public void setEditdisable1(boolean editdisable) {
		this.editdisable1 = editdisable;
	}

	public boolean isEditable1() {
		return editable1;
	}

	public void setEditable1(boolean editable) {
		this.editable1 = editable;
	}

	public static int get_editTableRow() {
		return _editTableRow;
	}

	public static void set_editTableRow(int tableRow) {
		_editTableRow = tableRow;
	}

	public boolean is_DeleteChick() {
		return _DeleteChick;
	}

	public void set_DeleteChick(boolean deleteChick) {
		_DeleteChick = deleteChick;
	}

	public String get_msg() {
		return _msg;
	}

	public void set_msg(String _msg) {
		this._msg = _msg;
	}

	public boolean is_RefurbishChick() {
		return _RefurbishChick;
	}

	public void set_RefurbishChick(boolean refurbishChick) {
		_RefurbishChick = refurbishChick;
	}

	public boolean is_SaveChick() {
		return _SaveChick;
	}

	public void set_SaveChick(boolean saveChick) {
		_SaveChick = saveChick;
	}

	public boolean is_SubmitChick() {
		return _SubmitChick;
	}

	public void set_SubmitChick(boolean submitChick) {
		_SubmitChick = submitChick;
	}

	public String getMt_sh1() {
		return Mt_sh1;
	}

	public void setMt_sh1(String mt_sh1) {
		Mt_sh1 = mt_sh1;
	}

	public String getMt_sh2() {
		return Mt_sh2;
	}

	public void setMt_sh2(String mt_sh2) {
		Mt_sh2 = mt_sh2;
	}

	public String getMt_sh3() {
		return Mt_sh3;
	}

	public void setMt_sh3(String mt_sh3) {
		Mt_sh3 = mt_sh3;
	}

	public String getMt_sh4() {
		return Mt_sh4;
	}

	public void setMt_sh4(String mt_sh4) {
		Mt_sh4 = mt_sh4;
	}

	public String getMad_sh1() {
		return Mad_sh1;
	}

	public void setMad_sh1(String mad_sh1) {
		Mad_sh1 = mad_sh1;
	}

	public String getMad_sh2() {
		return Mad_sh2;
	}

	public void setMad_sh2(String mad_sh2) {
		Mad_sh2 = mad_sh2;
	}

	public String getMad_sh3() {
		return Mad_sh3;
	}

	public void setMad_sh3(String mad_sh3) {
		Mad_sh3 = mad_sh3;
	}

	public String getMad_sh4() {
		return Mad_sh4;
	}

	public void setMad_sh4(String mad_sh4) {
		Mad_sh4 = mad_sh4;
	}

	public String getVad_sh1() {
		return Vad_sh1;
	}

	public void setVad_sh1(String vad_sh1) {
		Vad_sh1 = vad_sh1;
	}

	public String getVad_sh2() {
		return Vad_sh2;
	}

	public void setVad_sh2(String vad_sh2) {
		Vad_sh2 = vad_sh2;
	}

	public String getVad_sh3() {
		return Vad_sh3;
	}

	public void setVad_sh3(String vad_sh3) {
		Vad_sh3 = vad_sh3;
	}

	public String getVad_sh4() {
		return Vad_sh4;
	}

	public void setVad_sh4(String vad_sh4) {
		Vad_sh4 = vad_sh4;
	}

	public String getAad_sh1() {
		return Aad_sh1;
	}

	public void setAad_sh1(String aad_sh1) {
		Aad_sh1 = aad_sh1;
	}

	public String getAad_sh2() {
		return Aad_sh2;
	}

	public void setAad_sh2(String aad_sh2) {
		Aad_sh2 = aad_sh2;
	}

	public String getAad_sh3() {
		return Aad_sh3;
	}

	public void setAad_sh3(String aad_sh3) {
		Aad_sh3 = aad_sh3;
	}

	public String getAad_sh4() {
		return Aad_sh4;
	}

	public void setAad_sh4(String aad_sh4) {
		Aad_sh4 = aad_sh4;
	}

	public String getFrl_sh1() {
		return Frl_sh1;
	}

	public void setFrl_sh1(String frl_sh1) {
		Frl_sh1 = frl_sh1;
	}

	public String getFrl_sh2() {
		return Frl_sh2;
	}

	public void setFrl_sh2(String frl_sh2) {
		Frl_sh2 = frl_sh2;
	}

	public String getFrl_sh3() {
		return Frl_sh3;
	}

	public void setFrl_sh3(String frl_sh3) {
		Frl_sh3 = frl_sh3;
	}

	public String getFrl_sh4() {
		return Frl_sh4;
	}

	public void setFrl_sh4(String frl_sh4) {
		Frl_sh4 = frl_sh4;
	}

	public String getQl_sh1() {
		return Ql_sh1;
	}

	public void setQl_sh1(String ql_sh1) {
		Ql_sh1 = ql_sh1;
	}

	public String getQl_sh2() {
		return Ql_sh2;
	}

	public void setQl_sh2(String ql_sh2) {
		Ql_sh2 = ql_sh2;
	}

	public String getQl_sh3() {
		return Ql_sh3;
	}

	public void setQl_sh3(String ql_sh3) {
		Ql_sh3 = ql_sh3;
	}

	public String getQl_sh4() {
		return Ql_sh4;
	}

	public void setQl_sh4(String ql_sh4) {
		Ql_sh4 = ql_sh4;
	}

	public double getAad_pjz() {
		return Aad_pjz;
	}

	public void setAad_pjz(double aad_pjz) {
		Aad_pjz = aad_pjz;
	}

	public double getFrl_pjz() {
		return Frl_pjz;
	}

	public void setFrl_pjz(double frl_pjz) {
		Frl_pjz = frl_pjz;
	}

	public double getMad_pjz() {
		return Mad_pjz;
	}

	public void setMad_pjz(double mad_pjz) {
		Mad_pjz = mad_pjz;
	}

	public double getMt_pjz() {
		return Mt_pjz;
	}

	public void setMt_pjz(double mt_pjz) {
		Mt_pjz = mt_pjz;
	}

	public double getQl_pjz() {
		return Ql_pjz;
	}

	public void setQl_pjz(double ql_pjz) {
		Ql_pjz = ql_pjz;
	}

	public double getVad_pjz() {
		return Vad_pjz;
	}

	public void setVad_pjz(double vad_pjz) {
		Vad_pjz = vad_pjz;
	}

	private boolean shenhzt = false;

	private IDropDownBean _ShenhztValue;

	public IDropDownBean getShenhztValue() {
		if (_ShenhztValue == null) {
			_ShenhztValue = (IDropDownBean) _ShenhztModel.getOption(1);
		}
		return _ShenhztValue;
	}

	public void setShenhztValue(IDropDownBean Value) {
		if (Value != null && getShenhztValue() != null) {

			if (Value.getId() != getShenhztValue().getId()) {
				shenhzt = true;
			}

		}
		_ShenhztValue = Value;
	}

	private IPropertySelectionModel _ShenhztModel;

	public void setShenhztModel(IPropertySelectionModel value) {
		_ShenhztModel = value;
	}

	public IPropertySelectionModel getShenhztModel() {
		if (_ShenhztModel == null) {
			getShenhztModels();
		}
		return _ShenhztModel;
	}

	public IPropertySelectionModel getShenhztModels() {
		List listShenhzt = new ArrayList();
		listShenhzt.add(new IDropDownBean(0, "未录入"));
		listShenhzt.add(new IDropDownBean(1, "未提交"));
		_ShenhztModel = new IDropDownModel(listShenhzt);
		return _ShenhztModel;
	}

	private IDropDownBean _RulbzValue;

	public IDropDownBean getRulbzValue() {
		if (_RulbzValue == null) {
			_RulbzValue = (IDropDownBean) _RulbzModel.getOption(0);
		}
		return _RulbzValue;
	}

	public void setRulbzValue(IDropDownBean Value) {

		if (Value != null && getRulbzValue() != null) {

			if (Value.getId() != getRulbzValue().getId()) {
				shenhzt = true;
			}
		}
		_RulbzValue = Value;
	}

	private IPropertySelectionModel _RulbzModel;

	public void setRulbzModel(IPropertySelectionModel value) {
		_RulbzModel = value;
	}

	public IPropertySelectionModel getRulbzModel() {
		if (_RulbzModel == null) {
			getRulbzModels();
		}
		return _RulbzModel;
	}

	public IPropertySelectionModel getRulbzModels() {

		Visit visit = (Visit) getPage().getVisit();
		String sql = "select id,mingc from rulbzb where diancxxb_id = "
				+ visit.getDiancxxb_id() + " order by xuh";

		_RulbzModel = new IDropDownModel(sql);
		return _RulbzModel;
	}

	private IDropDownBean _QumwValue;

	public IDropDownBean getQumwValue() {
		if (_QumwValue == null) {
			_QumwValue = (IDropDownBean) _QumwModel.getOption(0);
		}
		return _QumwValue;
	}

	public void setQumwValue(IDropDownBean Value) {

		if (Value != null && getQumwValue() != null) {

			if (Value.getId() != getQumwValue().getId()) {
				shenhzt = true;
			}
		}

		_QumwValue = Value;
	}

	private IPropertySelectionModel _QumwModel;

	public void setQumwModel(IPropertySelectionModel value) {
		_QumwModel = value;
	}

	public IPropertySelectionModel getQumwModel() {
		if (_QumwModel == null) {
			getQumwModels();
		}
		return _QumwModel;
	}

	public IPropertySelectionModel getQumwModels() {

		Visit visit = (Visit) getPage().getVisit();
//		String sql = "select jz.id, jz.mingc  from jizfzb jz  where jz.id not in (select r.jizfzb_id  from rulmzlb r  where "
//				+ "r.rulrq = to_date('"
//				+ FormatDate(getRulrq())
//				+ "', 'yyyy-mm-dd') "
//				+ "and r.rulbzb_id =  "
//				+ getProperId(_RulbzModel, getRulbzValue().getValue())
//				+ "and r.diancxxb_id = "
//				+ visit.getDiancxxb_id()
//				+ ") order by xuh";
		String sql = "";
		if(getShenhztValue().getId()==0){
			
			sql = 
				"select jz.id, jz.mingc\n" +
				"  from jizfzb jz\n" + 
				" where jz.id not in (select r.jizfzb_id\n" + 
				"                       from rulmzlb r\n" + 
				"                      where r.rulrq = to_date('"+FormatDate(getRulrq())+"', 'yyyy-mm-dd')\n" + 
				"                        and r.rulbzb_id = 1\n" + 
				"                        and r.diancxxb_id = 100)\n" + 
				" order by xuh";
		}else{
			sql = 
				"select jz.id, jz.mingc\n" +
				"  from jizfzb jz\n" + 
				" where jz.id in (select r.jizfzb_id\n" + 
				"                   from rulmzlb r\n" + 
				"                  where r.rulrq = to_date('"+FormatDate(getRulrq())+"', 'yyyy-mm-dd')\n" + 
				"                    and r.rulbzb_id = 1\n" + 
				"                    and r.diancxxb_id = 100\n" + 
				"                    and r.shenhzt = 0)\n" + 
				" order by xuh";

		}
		List dropdownlist = new ArrayList();
		dropdownlist.add(new IDropDownBean(-1, "无"));
		JDBCcon con = new JDBCcon();
		try {
			ResultSet rs = con.getResultSet(sql);
			while (rs.next()) {
				int id = rs.getInt(1);
				String mc = rs.getString(2);
				dropdownlist.add(new IDropDownBean(id, mc));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}

		_QumwModel = new IDropDownModel(dropdownlist);
		return _QumwModel;
	}

	private String FormatDate(Date _date) {
		if (_date == null) {
			return DateUtil.Formatdate("yyyy-MM-dd", new Date());
		}
		return DateUtil.Formatdate("yyyy-MM-dd", _date);
	}

	private String getProperValue(IPropertySelectionModel _selectModel,
			long value) {
		int OprionCount;
		OprionCount = _selectModel.getOptionCount();
		for (int i = 0; i < OprionCount; i++) {
			if (((IDropDownBean) _selectModel.getOption(i)).getId() == value) {
				return ((IDropDownBean) _selectModel.getOption(i)).getValue();
			}
		}
		return null;
	}

	private long getProperId(IPropertySelectionModel _selectModel, String value) {
		int OprionCount;
		OprionCount = _selectModel.getOptionCount();

		for (int i = 0; i < OprionCount; i++) {
			if (((IDropDownBean) _selectModel.getOption(i)).getValue().equals(
					value)) {
				return ((IDropDownBean) _selectModel.getOption(i)).getId();
			}
		}
		return -1;
	}

	public long getAad_id1() {
		return Aad_id1;
	}

	public void setAad_id1(long aad_id1) {
		Aad_id1 = aad_id1;
	}

	public long getAad_id2() {
		return Aad_id2;
	}

	public void setAad_id2(long aad_id2) {
		Aad_id2 = aad_id2;
	}

	public long getAad_id3() {
		return Aad_id3;
	}

	public void setAad_id3(long aad_id3) {
		Aad_id3 = aad_id3;
	}

	public long getAad_id4() {
		return Aad_id4;
	}

	public void setAad_id4(long aad_id4) {
		Aad_id4 = aad_id4;
	}

	public long getFrl_id1() {
		return Frl_id1;
	}

	public void setFrl_id1(long frl_id1) {
		Frl_id1 = frl_id1;
	}

	public long getFrl_id2() {
		return Frl_id2;
	}

	public void setFrl_id2(long frl_id2) {
		Frl_id2 = frl_id2;
	}

	public long getFrl_id3() {
		return Frl_id3;
	}

	public void setFrl_id3(long frl_id3) {
		Frl_id3 = frl_id3;
	}

	public long getFrl_id4() {
		return Frl_id4;
	}

	public void setFrl_id4(long frl_id4) {
		Frl_id4 = frl_id4;
	}

	public long getMad_id1() {
		return Mad_id1;
	}

	public void setMad_id1(long mad_id1) {
		Mad_id1 = mad_id1;
	}

	public long getMad_id2() {
		return Mad_id2;
	}

	public void setMad_id2(long mad_id2) {
		Mad_id2 = mad_id2;
	}

	public long getMad_id3() {
		return Mad_id3;
	}

	public void setMad_id3(long mad_id3) {
		Mad_id3 = mad_id3;
	}

	public long getMad_id4() {
		return Mad_id4;
	}

	public void setMad_id4(long mad_id4) {
		Mad_id4 = mad_id4;
	}

	public long getMt_id1() {
		return Mt_id1;
	}

	public void setMt_id1(long mt_id1) {
		Mt_id1 = mt_id1;
	}

	public long getMt_id2() {
		return Mt_id2;
	}

	public void setMt_id2(long mt_id2) {
		Mt_id2 = mt_id2;
	}

	public long getMt_id3() {
		return Mt_id3;
	}

	public void setMt_id3(long mt_id3) {
		Mt_id3 = mt_id3;
	}

	public long getMt_id4() {
		return Mt_id4;
	}

	public void setMt_id4(long mt_id4) {
		Mt_id4 = mt_id4;
	}

	public long getQl_id1() {
		return Ql_id1;
	}

	public void setQl_id1(long ql_id1) {
		Ql_id1 = ql_id1;
	}

	public long getQl_id2() {
		return Ql_id2;
	}

	public void setQl_id2(long ql_id2) {
		Ql_id2 = ql_id2;
	}

	public long getQl_id3() {
		return Ql_id3;
	}

	public void setQl_id3(long ql_id3) {
		Ql_id3 = ql_id3;
	}

	public long getQl_id4() {
		return Ql_id4;
	}

	public void setQl_id4(long ql_id4) {
		Ql_id4 = ql_id4;
	}

	public long getVad_id1() {
		return Vad_id1;
	}

	public void setVad_id1(long vad_id1) {
		Vad_id1 = vad_id1;
	}

	public long getVad_id2() {
		return Vad_id2;
	}

	public void setVad_id2(long vad_id2) {
		Vad_id2 = vad_id2;
	}

	public long getVad_id3() {
		return Vad_id3;
	}

	public void setVad_id3(long vad_id3) {
		Vad_id3 = vad_id3;
	}

	public long getVad_id4() {
		return Vad_id4;
	}

	public void setVad_id4(long vad_id4) {
		Vad_id4 = vad_id4;
	}

	public long getRulmzlb_id() {
		return rulmzlb_id;
	}

	public void setRulmzlb_id(long rulmzlb_id) {
		this.rulmzlb_id = rulmzlb_id;
	}

}
