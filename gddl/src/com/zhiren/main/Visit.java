/**
 * 
 */
package com.zhiren.main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.tapestry.form.IPropertySelectionModel;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.common.ext.tree.Tree;
import com.zhiren.zidy.Aotcr;

/**
 * @author 王磊
 * 
 */
/*
 * 2009-05-06
 * 王磊
 * 增加系统查询默认的开始时间和默认结束时间变量
 * 现版本：1.1.1.40
 */
public class Visit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3710701246732610367L;

	// 记录用户当前使用的页
	private String ActivePageName = "Home";

	public String getActivePageName() {
		return ActivePageName;
	}

	public void setActivePageName(String activePageName) {
		ActivePageName = activePageName;
	}
	
	// 记录session信息
	private HttpSession Session;

	public HttpSession getSession() {
		return Session;
	}

	public void setSession(HttpSession session) {
		Session = session;
	}

	// 出错信息
	private int ErrCode = 0;

	public int getErrcode() {
		return ErrCode;
	}

	public void setErrcode(int errcode) {
		ErrCode = errcode;
	}

	// 登陆人员信息
	private long RenyId;

	public long getRenyID() {
		return RenyId;
	}

	public void setRenyID(long renyid) {
		RenyId = renyid;
	}

	private int RenyJb;

	public int getRenyjb() {
		return RenyJb;
	}

	public void setRenyjb(int renyjb) {
		RenyJb = renyjb;
	}

	private String Renymc;

	public String getRenymc() {
		return Renymc;
	}

	public void setRenymc(String renymc) {
		Renymc = renymc;
	}

	private long Diancxxb_id;

	public long getDiancxxb_id() {
		return Diancxxb_id;
	}

	public void setDiancxxb_id(long diancxxbid) {
		Diancxxb_id = diancxxbid;
	}
	
	private String Diancmc;
	public String getDiancmc() {
		return Diancmc;
	}
	public void setDiancmc(String Diancmc) {
		this.Diancmc = Diancmc;
	}
	
	private String Diancqc;
	public String getDiancqc() {
		return Diancqc;
	}
	public void setDiancqc(String Diancqc) {
		this.Diancqc = Diancqc;
	}
	
	private String daoz;
	public String getDaoz() {
		return daoz;
	}
	public void setDaoz(String daoz) {
		this.daoz = daoz;
	}
	
	private boolean ReportShowZero;
	public boolean isReportShowZero() {
		return ReportShowZero;
	}
	public void setReportShowZero(boolean ReportShowZero) {
		this.ReportShowZero = ReportShowZero;
	}
	private boolean Shifsh;
	public boolean isShifsh() {
		return Shifsh;
	}
	public void setShifsh(boolean Shifsh) {
		this.Shifsh = Shifsh;
	}
	
	private String Ruljiaql;
	public String getRuljiaql() {
		return Ruljiaql;
	}

	public void setRuljiaql(String ruljiaql) {
		this.Ruljiaql = ruljiaql;
	}
	
//	系统文件夹位置
	private String xitwjjwz;
	public String getXitwjjwz() {
		return xitwjjwz;
	}
	public void setXitwjjwz(String weiz) {
		xitwjjwz = weiz;
	}
	
	// 基础判断	
	public boolean isJTUser() {
		return getRenyjb() == SysConstant.JIB_JT;
	}
	public boolean isGSUser() {
		return getRenyjb() == SysConstant.JIB_GS;
	}
	public boolean isDCUser() {
		return getRenyjb() == SysConstant.JIB_DC;
	}
	private boolean fenc;
	public boolean isFencb() {
		return fenc;
	}
	public void setFencb(boolean fenc) {
		this.fenc = fenc;
	}
	// 基础参数
	private int int1;

	public int getInt1() {
		return int1;
	}

	public void setInt1(int in1) {
		int1 = in1;
	}

	private int int2;

	public int getInt2() {
		return int2;
	}

	public void setInt2(int in2) {
		int2 = in2;
	}

	private int int3;

	public int getInt3() {
		return int3;
	}

	public void setInt3(int in3) {
		int3 = in3;
	}
	private int int4;

	public int getInt4() {
		return int4;
	}

	public void setInt4(int in4) {
		int4 = in4;
	}
	private int int5;
	public int getInt5() {
		return int5;
	}

	public void setInt5(int in5) {
		int5 = in5;
	}
	private long long1;

	public long getLong1() {
		return long1;
	}

	public void setLong1(long l1) {
		long1 = l1;
	}
	
	private long long2;

	public long getLong2() {
		return long2;
	}

	public void setLong2(long l2) {
		long2 = l2;
	}

	private long long3;

	public long getLong3() {
		return long3;
	}

	public void setLong3(long l3) {
		long3 = l3;
	}
	
	private long long4;

	public long getLong4() {
		return long4;
	}

	public void setLong4(long l4) {
		long4 = l4;
	}
	
	private long long5;

	public long getLong5() {
		return long5;
	}

	public void setLong5(long l5) {
		long5 = l5;
	}
	
	private long long6;

	public long getLong6() {
		return long6;
	}

	public void setLong6(long l6) {
		long6 = l6;
	}
	
	private long long7;

	public long getLong7() {
		return long7;
	}

	public void setLong7(long l7) {
		long7 = l7;
	}
	
	private long long8;

	public long getLong8() {
		return long8;
	}

	public void setLong8(long l8) {
		long8 = l8;
	}
	
	private long long9;

	public long getLong9() {
		return long9;
	}

	public void setLong9(long l9) {
		long9 = l9;
	}
	
	private long long10;

	public long getLong10() {
		return long10;
	}

	public void setLong10(long l10) {
		long10 = l10;
	}
	
	private boolean boolean1;

	public boolean getboolean1() {
		return boolean1;
	}

	public void setboolean1(boolean value) {
		boolean1 = value;
	}

	private boolean boolean2;

	public boolean getboolean2() {
		return boolean2;
	}

	public void setboolean2(boolean value) {
		boolean2 = value;
	}

	private boolean boolean3;

	public boolean getboolean3() {
		return boolean3;
	}

	public void setboolean3(boolean value) {
		boolean3 = value;
	}

	private boolean boolean4;

	public boolean getboolean4() {
		return boolean4;
	}

	public void setboolean4(boolean value) {
		boolean4 = value;
	}

	public void setboolean5(boolean value) {
		boolean5 = value;
	}

	private boolean boolean5;

	public boolean getboolean5() {
		return boolean5;
	}

	public void setboolean6(boolean value) {
		boolean6 = value;
	}

	private boolean boolean6;

	public boolean getboolean6() {
		return boolean6;
	}

	public void setboolean7(boolean value) {
		boolean7 = value;
	}

	private boolean boolean7;

	public boolean getboolean7() {
		return boolean7;
	}
	
	public void setboolean8(boolean value) {
		boolean8 = value;
	}

	private boolean boolean8;

	public boolean getboolean8() {
		return boolean8;
	}
	
	public void setboolean9(boolean value) {
		boolean9 = value;
	}

	private boolean boolean9;

	public boolean getboolean9() {
		return boolean9;
	}
	
	public void setboolean10(boolean value) {
		boolean10 = value;
	}
	private boolean boolean10;

	public boolean getboolean10() {
		return boolean10;
	}
	
	Date date1;
	public Date getDate1(){
		return date1;
	}
	public void setDate1(Date value){
		date1=value;
	}
	Date date2;
	public Date getDate2(){
		return date2;
	}
	public void setDate2(Date value){
		date2=value;
	}
	Date morkssj;
	Date morjssj;
	public Date getMorkssj(){
		return morkssj;
	}
	public void setMorkssj(Date date){
		morkssj = date;
	}
	public Date getMorjssj(){
		return morjssj;
	}
	public void setMorjssj(Date date){
		morjssj = date;
	}
	private Object object1;

	public Object getObject1() {
		return object1;
	}

	public Object setObject1(Object value) {
		return object1 = value;
	}

	private String string1;

	public String getString1() {
		return string1;
	}

	public void setString1(String string) {
		string1 = string;
	}
	
	private String string2;

	public String getString2() {
		return string2;
	}

	public void setString2(String string) {
		string2 = string;
	}
	private String string3;

	public String getString3() {
		return string3;
	}

	public void setString3(String string) {
		string3 = string;
	}
	private String string4;

	public String getString4() {
		return string4;
	}

	public void setString4(String string) {
		string4 = string;
	}
	private String string5;

	public String getString5() {
		return string5;
	}

	public void setString5(String string) {
		string5 = string;
	}
	
	private String string6;

	public String getString6() {
		return string6;
	}

	public void setString6(String string) {
		string6 = string;
	}
	
	private String string7;

	public String getString7() {
		return string7;
	}

	public void setString7(String string) {
		string7 = string;
	}
	
	private String string8;

	public String getString8() {
		return string8;
	}

	public void setString8(String string) {
		string8 = string;
	}
	
	private String string9;
	
	public String getString9() {
		return string9;
	}

	public void setString9(String string) {
		string9 = string;
	}
	
	private String string10;
	
	public String getString10() {
		return string10;
	}

	public void setString10(String string) {
		string10 = string;
	}
    private String string11;
	
	public String getString11() {
		return string11;
	}

	public void setString11(String string) {
		string11 = string;
	}
	
    private String string12;
	
	public String getString12() {
		return string12;
	}

	public void setString12(String string) {
		string12 = string;
	}
	
    private String string13;
	
	public String getString13() {
		return string13;
	}

	public void setString13(String string) {
		string13 = string;
	}
	
    private String string14;
	
	public String getString14() {
		return string14;
	}

	public void setString14(String string) {
		string14 = string;
	}
	
    private String string15;
	
	public String getString15() {
		return string15;
	}

	public void setString15(String string) {
		string15 = string;
	}
	
    private String string16;
	
	public String getString16() {
		return string16;
	}

	public void setString16(String string) {
		string16 = string;
	}
	
    private String string17;
	
	public String getString17() {
		return string17;
	}

	public void setString17(String string) {
		string17 = string;
	}
	
	private String string18;
	
	public String getString18() {
		return string18;
	}

	public void setString18(String string18) {
		this.string18 = string18;
	}
	
	private String string19;
	
	public String getString19() {
		return string19;
	}

	public void setString19(String string19) {
		this.string19 = string19;
	}
	
	private String string20;
	
	public String getString20() {
		return string20;
	}

	public void setString20(String string20) {
		this.string20 = string20;
	}
	
	
	private String string21;
	
	public String getString21() {
		return string21;
	}

	public void setString21(String string21) {
		this.string21 = string21;
	}
	
	private double double1;;
	public double getDouble1() {
		return double1;
	}

	public void setDouble1(double double1) {
		this.double1 = double1;
	}
	
	private double double2;
	public double getDouble2() {
		return double2;
	}

	public void setDouble2(double double2) {
		this.double2 = double2;
	}
	
	private double double3;
	public double getDouble3() {
		return double3;
	}

	public void setDouble3(double double3) {
		this.double3 = double3;
	}
	
	private double double4;
	public double getDouble4() {
		return double4;
	}

	public void setDouble4(double double4) {
		this.double4 = double4;
	}
	
	private double double5;
	public double getDouble5() {
		return double5;
	}

	public void setDouble5(double double5) {
		this.double5 = double5;
	}
	
	private double double6;
	public double getDouble6() {
		return double6;
	}

	public void setDouble6(double double6) {
		this.double6 = double6;
	}
	
	private double double7;
	public double getDouble7() {
		return double7;
	}

	public void setDouble7(double double7) {
		this.double7 = double7;
	}
	
	private double double8;
	public double getDouble8() {
		return double8;
	}

	public void setDouble8(double double8) {
		this.double8 = double8;
	}
	
	private double double9;
	public double getDouble9() {
		return double9;
	}

	public void setDouble9(double double9) {
		this.double9 = double9;
	}
	
	private double double10;
	public double getDouble10() {
		return double10;
	}

	public void setDouble10(double double10) {
		this.double10 = double10;
	}
	
	private double double11;
	public double getDouble11() {
		return double11;
	}

	public void setDouble11(double double11) {
		this.double11 = double11;
	}
	
	private double double12;
	public double getDouble12() {
		return double12;
	}

	public void setDouble12(double double12) {
		this.double12 = double12;
	}
	
	private double double13;
	public double getDouble13() {
		return double13;
	}

	public void setDouble13(double double13) {
		this.double13 = double13;
	}
	
	private double double14;
	public double getDouble14() {
		return double14;
	}

	public void setDouble14(double double14) {
		this.double14 = double14;
	}
	
	private double double15;
	public double getDouble15() {
		return double15;
	}

	public void setDouble15(double double15) {
		this.double15 = double15;
	}
	
	private double double16;
	public double getDouble16() {
		return double16;
	}

	public void setDouble16(double double16) {
		this.double16 = double16;
	}
	
	private double double17;
	public double getDouble17() {
		return double17;
	}

	public void setDouble17(double double17) {
		this.double17 = double17;
	}
	
	private double double18;
	public double getDouble18() {
		return double18;
	}

	public void setDouble18(double double18) {
		this.double18 = double18;
	}
	
	private double double19;
	public double getDouble19() {
		return double19;
	}

	public void setDouble19(double double19) {
		this.double19 = double19;
	}
	
	private double double20;
	public double getDouble20() {
		return double20;
	}

	public void setDouble20(double double20) {
		this.double20 = double20;
	}
	
	private double double21;
	public double getDouble21() {
		return double21;
	}

	public void setDouble21(double double21) {
		this.double21 = double21;
	}
	
	private List list1;

	public List getList1() {
		return list1;
	}

	public void setList1(List list) {
		list1 = list;
	}

	private List list2;

	public List getList2() {
		return list2;
	}

	public void setList2(List list) {
		list2 = list;
	}

	private List list3;

	public List getList3() {
		return list3;
	}

	public void setList3(List list) {
		list3 = list;
	}

	private List list4;

	public List getList4() {
		return list4;
	}

	public void setList4(List list) {
		list4 = list;
	}

	private List list5;

	public List getList5() {
		return list5;
	}

	public void setList5(List list) {
		list5 = list;
	}
	private List list6;

	public List getList6() {
		return list6;
	}

	public void setList6(List list) {
		list6 = list;
	}
	private List list7;

	public List getList7() {
		return list7;
	}

	public void setList7(List list) {
		list7 = list;
	}
	private List list8;

	public List getList8() {
		return list8;
	}

	public void setList8(List list) {
		list8 = list;
	}
	private List list9;

	public List getList9() {
		return list9;
	}

	public void setList9(List list) {
		list9 = list;
	}
	private List list10;

	public List getList10() {
		return list10;
	}

	public void setList10(List list) {
		list10 = list;
	}
	private IDropDownBean _DropDownBean1;

	public IDropDownBean getDropDownBean1() {
		return _DropDownBean1;
	}

	public void setDropDownBean1(IDropDownBean value) {
		_DropDownBean1 = value;
	}

	private IDropDownBean _DropDownBean2;

	public IDropDownBean getDropDownBean2() {
		return _DropDownBean2;
	}

	public void setDropDownBean2(IDropDownBean value) {
		_DropDownBean2 = value;
	}

	private IDropDownBean _DropDownBean3;

	public IDropDownBean getDropDownBean3() {
		return _DropDownBean3;
	}

	public void setDropDownBean3(IDropDownBean value) {
		_DropDownBean3 = value;
	}

	private IDropDownBean _DropDownBean4;

	public IDropDownBean getDropDownBean4() {
		return _DropDownBean4;
	}

	public void setDropDownBean4(IDropDownBean value) {
		_DropDownBean4 = value;
	}

	private IDropDownBean _DropDownBean5;

	public IDropDownBean getDropDownBean5() {
		return _DropDownBean5;
	}

	public void setDropDownBean5(IDropDownBean value) {
		_DropDownBean5 = value;
	}

	private IDropDownBean _DropDownBean6;

	public IDropDownBean getDropDownBean6() {
		return _DropDownBean6;
	}

	public void setDropDownBean6(IDropDownBean value) {
		_DropDownBean6 = value;
	}

	private IDropDownBean _DropDownBean7;

	public IDropDownBean getDropDownBean7() {
		return _DropDownBean7;
	}

	public void setDropDownBean7(IDropDownBean value) {
		_DropDownBean7 = value;
	}

	private IDropDownBean _DropDownBean8;

	public IDropDownBean getDropDownBean8() {
		return _DropDownBean8;
	}

	public void setDropDownBean8(IDropDownBean value) {
		_DropDownBean8 = value;
	}

	private IDropDownBean _DropDownBean9;

	public IDropDownBean getDropDownBean9() {
		return _DropDownBean9;
	}

	public void setDropDownBean9(IDropDownBean value) {
		_DropDownBean9 = value;
	}

	private IDropDownBean _DropDownBean10;

	public IDropDownBean getDropDownBean10() {
		return _DropDownBean10;
	}

	public void setDropDownBean10(IDropDownBean value) {
		_DropDownBean10 = value;
	}

	private IPropertySelectionModel _ProSelectionModel1;

	public IPropertySelectionModel getProSelectionModel1() {
		return _ProSelectionModel1;
	}

	public void setProSelectionModel1(IPropertySelectionModel value) {
		_ProSelectionModel1 = value;
	}

	private IPropertySelectionModel _ProSelectionModel2;

	public IPropertySelectionModel getProSelectionModel2() {
		return _ProSelectionModel2;
	}

	public void setProSelectionModel2(IPropertySelectionModel value) {
		_ProSelectionModel2 = value;
	}

	private IPropertySelectionModel _ProSelectionModel3;

	public IPropertySelectionModel getProSelectionModel3() {
		return _ProSelectionModel3;
	}

	public void setProSelectionModel3(IPropertySelectionModel value) {
		_ProSelectionModel3 = value;
	}

	private IPropertySelectionModel _ProSelectionModel4;

	public IPropertySelectionModel getProSelectionModel4() {
		return _ProSelectionModel4;
	}

	public void setProSelectionModel4(IPropertySelectionModel value) {
		_ProSelectionModel4 = value;
	}

	private IPropertySelectionModel _ProSelectionModel5;

	public IPropertySelectionModel getProSelectionModel5() {
		return _ProSelectionModel5;
	}

	public void setProSelectionModel5(IPropertySelectionModel value) {
		_ProSelectionModel5 = value;
	}

	private IPropertySelectionModel _ProSelectionModel6;

	public IPropertySelectionModel getProSelectionModel6() {
		return _ProSelectionModel6;
	}

	public void setProSelectionModel6(IPropertySelectionModel value) {
		_ProSelectionModel6 = value;
	}

	private IPropertySelectionModel _ProSelectionModel7;

	public IPropertySelectionModel getProSelectionModel7() {
		return _ProSelectionModel7;
	}

	public void setProSelectionModel7(IPropertySelectionModel value) {
		_ProSelectionModel7 = value;
	}

	private IPropertySelectionModel _ProSelectionModel8;

	public IPropertySelectionModel getProSelectionModel8() {
		return _ProSelectionModel8;
	}

	public void setProSelectionModel8(IPropertySelectionModel value) {
		_ProSelectionModel8 = value;
	}

	private IPropertySelectionModel _ProSelectionModel9;

	public IPropertySelectionModel getProSelectionModel9() {
		return _ProSelectionModel9;
	}

	public void setProSelectionModel9(IPropertySelectionModel value) {
		_ProSelectionModel9 = value;
	}

	private IPropertySelectionModel _ProSelectionModel10;

	public IPropertySelectionModel getProSelectionModel10() {
		return _ProSelectionModel10;
	}

	public void setProSelectionModel10(IPropertySelectionModel value) {
		_ProSelectionModel10 = value;
	}

	// /////////////
	private IDropDownBean _DropDownBean11;

	public IDropDownBean getDropDownBean11() {
		return _DropDownBean11;
	}

	public void setDropDownBean11(IDropDownBean value) {
		_DropDownBean11 = value;
	}

	private IPropertySelectionModel _ProSelectionModel11;

	public IPropertySelectionModel getProSelectionModel11() {
		return _ProSelectionModel11;
	}

	public void setProSelectionModel11(IPropertySelectionModel value) {
		_ProSelectionModel11 = value;
	}

	//
	private IDropDownBean _DropDownBean12;

	public IDropDownBean getDropDownBean12() {
		return _DropDownBean12;
	}

	public void setDropDownBean12(IDropDownBean value) {
		_DropDownBean12 = value;
	}

	private IPropertySelectionModel _ProSelectionModel12;

	public IPropertySelectionModel getProSelectionModel12() {
		return _ProSelectionModel12;
	}

	public void setProSelectionModel12(IPropertySelectionModel value) {
		_ProSelectionModel12 = value;
	}

	//
	private IDropDownBean _DropDownBean13;

	public IDropDownBean getDropDownBean13() {
		return _DropDownBean13;
	}

	public void setDropDownBean13(IDropDownBean value) {
		_DropDownBean13 = value;
	}

	private IPropertySelectionModel _ProSelectionModel13;

	public IPropertySelectionModel getProSelectionModel13() {
		return _ProSelectionModel13;
	}

	public void setProSelectionModel13(IPropertySelectionModel value) {
		_ProSelectionModel13 = value;
	}

	//
	private IDropDownBean _DropDownBean14;

	public IDropDownBean getDropDownBean14() {
		return _DropDownBean14;
	}

	public void setDropDownBean14(IDropDownBean value) {
		_DropDownBean14 = value;
	}

	private IPropertySelectionModel _ProSelectionModel14;

	public IPropertySelectionModel getProSelectionModel14() {
		return _ProSelectionModel14;
	}

	public void setProSelectionModel14(IPropertySelectionModel value) {
		_ProSelectionModel14 = value;
	}

	//
	private IDropDownBean _DropDownBean15;

	public IDropDownBean getDropDownBean15() {
		return _DropDownBean15;
	}

	public void setDropDownBean15(IDropDownBean value) {
		_DropDownBean15 = value;
	}

	private IPropertySelectionModel _ProSelectionModel15;

	public IPropertySelectionModel getProSelectionModel15() {
		return _ProSelectionModel15;
	}

	public void setProSelectionModel15(IPropertySelectionModel value) {
		_ProSelectionModel15 = value;
	}

	//
	private IDropDownBean _DropDownBean16;

	public IDropDownBean getDropDownBean16() {
		return _DropDownBean16;
	}

	public void setDropDownBean16(IDropDownBean value) {
		_DropDownBean16 = value;
	}

	private IPropertySelectionModel _ProSelectionModel16;

	public IPropertySelectionModel getProSelectionModel16() {
		return _ProSelectionModel16;
	}

	public void setProSelectionModel16(IPropertySelectionModel value) {
		_ProSelectionModel16 = value;
	}

	//
	private IDropDownBean _DropDownBean17;

	public IDropDownBean getDropDownBean17() {
		return _DropDownBean17;
	}

	public void setDropDownBean17(IDropDownBean value) {
		_DropDownBean17 = value;
	}

	private IPropertySelectionModel _ProSelectionModel17;

	public IPropertySelectionModel getProSelectionModel17() {
		return _ProSelectionModel17;
	}

	public void setProSelectionModel17(IPropertySelectionModel value) {
		_ProSelectionModel17 = value;
	}

	//
	private IDropDownBean _DropDownBean18;

	public IDropDownBean getDropDownBean18() {
		return _DropDownBean18;
	}

	public void setDropDownBean18(IDropDownBean value) {
		_DropDownBean18 = value;
	}

	private IPropertySelectionModel _ProSelectionModel18;

	public IPropertySelectionModel getProSelectionModel18() {
		return _ProSelectionModel18;
	}

	public void setProSelectionModel18(IPropertySelectionModel value) {
		_ProSelectionModel18 = value;
	}

	//
	private IDropDownBean _DropDownBean19;

	public IDropDownBean getDropDownBean19() {
		return _DropDownBean19;
	}

	public void setDropDownBean19(IDropDownBean value) {
		_DropDownBean19 = value;
	}

	private IPropertySelectionModel _ProSelectionModel19;

	public IPropertySelectionModel getProSelectionModel19() {
		return _ProSelectionModel19;
	}

	public void setProSelectionModel19(IPropertySelectionModel value) {
		_ProSelectionModel19 = value;
	}

	//
	private IDropDownBean _DropDownBean20;

	public IDropDownBean getDropDownBean20() {
		return _DropDownBean20;
	}

	public void setDropDownBean20(IDropDownBean value) {
		_DropDownBean20 = value;
	}

	private IPropertySelectionModel _ProSelectionModel20;

	public IPropertySelectionModel getProSelectionModel20() {
		return _ProSelectionModel20;
	}

	public void setProSelectionModel20(IPropertySelectionModel value) {
		_ProSelectionModel20 = value;
	}

	//
	private IDropDownBean _DropDownBean21;

	public IDropDownBean getDropDownBean21() {
		return _DropDownBean21;
	}

	public void setDropDownBean21(IDropDownBean value) {
		_DropDownBean21 = value;
	}

	private IPropertySelectionModel _ProSelectionModel21;

	public IPropertySelectionModel getProSelectionModel21() {
		return _ProSelectionModel21;
	}

	public void setProSelectionModel21(IPropertySelectionModel value) {
		_ProSelectionModel21 = value;
	}

	//
	private IDropDownBean _DropDownBean22;

	public IDropDownBean getDropDownBean22() {
		return _DropDownBean22;
	}

	public void setDropDownBean22(IDropDownBean value) {
		_DropDownBean22 = value;
	}

	private IPropertySelectionModel _ProSelectionModel22;

	public IPropertySelectionModel getProSelectionModel22() {
		return _ProSelectionModel22;
	}

	public void setProSelectionModel22(IPropertySelectionModel value) {
		_ProSelectionModel22 = value;
	}

	//
	private IDropDownBean _DropDownBean23;

	public IDropDownBean getDropDownBean23() {
		return _DropDownBean23;
	}

	public void setDropDownBean23(IDropDownBean value) {
		_DropDownBean23 = value;
	}

	private IPropertySelectionModel _ProSelectionModel23;

	public IPropertySelectionModel getProSelectionModel23() {
		return _ProSelectionModel23;
	}

	public void setProSelectionModel23(IPropertySelectionModel value) {
		_ProSelectionModel23 = value;
	}
	//
	private IDropDownBean _DropDownBean24;

	public IDropDownBean getDropDownBean24() {
		return _DropDownBean24;
	}

	public void setDropDownBean24(IDropDownBean value) {
		_DropDownBean24 = value;
	}

	private IPropertySelectionModel _ProSelectionModel24;

	public IPropertySelectionModel getProSelectionModel24() {
		return _ProSelectionModel24;
	}

	public void setProSelectionModel24(IPropertySelectionModel value) {
		_ProSelectionModel24 = value;
	}
	//
	private IDropDownBean _DropDownBean25;

	public IDropDownBean getDropDownBean25() {
		return _DropDownBean25;
	}

	public void setDropDownBean25(IDropDownBean value) {
		_DropDownBean25 = value;
	}

	private IPropertySelectionModel _ProSelectionModel25;

	public IPropertySelectionModel getProSelectionModel25() {
		return _ProSelectionModel25;
	}

	public void setProSelectionModel25(IPropertySelectionModel value) {
		_ProSelectionModel25 = value;
	}
	private IDropDownBean _DropDownBean26;

	public IDropDownBean getDropDownBean26() {
		return _DropDownBean26;
	}

	public void setDropDownBean26(IDropDownBean value) {
		_DropDownBean26 = value;
	}
	private IPropertySelectionModel _ProSelectionModel26;

	public IPropertySelectionModel getProSelectionModel26() {
		return _ProSelectionModel26;
	}

	public void setProSelectionModel26(IPropertySelectionModel value) {
		_ProSelectionModel26 = value;
	}
	//
	private IDropDownBean _DropDownBean27;

	public IDropDownBean getDropDownBean27() {
		return _DropDownBean27;
	}

	public void setDropDownBean27(IDropDownBean value) {
		_DropDownBean27 = value;
	}
	private IPropertySelectionModel _ProSelectionModel27;

	public IPropertySelectionModel getProSelectionModel27() {
		return _ProSelectionModel27;
	}

	public void setProSelectionModel27(IPropertySelectionModel value) {
		_ProSelectionModel27 = value;
	}
	
	private IDropDownBean _DropDownBean28;

	public IDropDownBean getDropDownBean28() {
		return _DropDownBean28;
	}

	public void setDropDownBean28(IDropDownBean value) {
		_DropDownBean28 = value;
	}
	private IPropertySelectionModel _ProSelectionModel28;

	public IPropertySelectionModel getProSelectionModel28() {
		return _ProSelectionModel28;
	}

	public void setProSelectionModel28(IPropertySelectionModel value) {
		_ProSelectionModel28 = value;
	}
	
	private IDropDownBean _DropDownBean29;

	public IDropDownBean getDropDownBean29() {
		return _DropDownBean29;
	}

	public void setDropDownBean29(IDropDownBean value) {
		_DropDownBean29 = value;
	}
	private IPropertySelectionModel _ProSelectionModel29;

	public IPropertySelectionModel getProSelectionModel29() {
		return _ProSelectionModel29;
	}

	public void setProSelectionModel29(IPropertySelectionModel value) {
		_ProSelectionModel29 = value;
	}
	
	private ExtGridUtil ExtGrid1;
	public ExtGridUtil getExtGrid1() {
		return ExtGrid1;
	}
	public void setExtGrid1(ExtGridUtil ExtGrid1) {
		this.ExtGrid1 = ExtGrid1;
	}
	
	private ExtGridUtil ExtGrid2;
	public ExtGridUtil getExtGrid2() {
		return ExtGrid2;
	}
	public void setExtGrid2(ExtGridUtil ExtGrid2) {
		this.ExtGrid2 = ExtGrid2;
	}
	
	private ExtGridUtil ExtGrid3;
	public ExtGridUtil getExtGrid3() {
		return ExtGrid3;
	}
	public void setExtGrid3(ExtGridUtil ExtGrid3) {
		this.ExtGrid3 = ExtGrid3;
	}
	private ExtGridUtil ExtGrid4;
	public ExtGridUtil getExtGrid4() {
		return ExtGrid4;
	}
	public void setExtGrid4(ExtGridUtil ExtGrid4) {
		this.ExtGrid4 = ExtGrid4;
	}
	private ExtGridUtil ExtGrid5;
	public ExtGridUtil getExtGrid5() {
		return ExtGrid5;
	}
	public void setExtGrid5(ExtGridUtil ExtGrid5) {
		this.ExtGrid5 = ExtGrid5;
	}
	private ExtGridUtil ExtGrid6;
	public ExtGridUtil getExtGrid6() {
		return ExtGrid6;
	}
	public void setExtGrid6(ExtGridUtil ExtGrid6) {
		this.ExtGrid6 = ExtGrid6;
	}
	private ExtTreeUtil ExtTree1;
	public ExtTreeUtil getExtTree1() {
		return ExtTree1;
	}
	public void setExtTree1(ExtTreeUtil ExtTree1) {
		this.ExtTree1 = ExtTree1;
	}
	private ExtTreeUtil ExtTree2;
	public ExtTreeUtil getExtTree2() {
		return ExtTree2;
	}
	public void setExtTree2(ExtTreeUtil ExtTree2) {
		this.ExtTree2 = ExtTree2;
	}
	private ExtTreeUtil ExtTree3;
	public ExtTreeUtil getExtTree3() {
		return ExtTree3;
	}
	public void setExtTree3(ExtTreeUtil ExtTree3) {
		this.ExtTree3 = ExtTree3;
	}
	private Toolbar toolbar1;
	public Toolbar getToolbar() {
		return toolbar1;
	}
	public void setToolbar(Toolbar tb1) {
		toolbar1 = tb1;
	}
	private DefaultTree dftree1;
	public DefaultTree getDefaultTree() {
		return dftree1;
	}
	public void setDefaultTree(DefaultTree dftree1) {
		this.dftree1 = dftree1;
	}
	private Tree tree1;
	public Tree getTree1() {
		return tree1;
	}
	public void setTree1(Tree tree) {
		tree1 = tree;
	}
	
	private int Farldec;
	public int getFarldec(){
		return Farldec;
	}

	public void setFarldec(int farldec) {
		Farldec = farldec;
	}
	
	private int Mtdec;
	public int getMtdec(){
		return Mtdec;
	}

	public void setMtdec(int mtdec) {
		Mtdec = mtdec;
	}
	
	/*
	 * 增加决定报表中数量修约的小数位数的变量
	 * 修改时间：2008-12-03
	 * 修改人：王磊
	 */
	private int Shuldec;
	public int getShuldec(){
		return Shuldec;
	}
	public void setShuldec(int shuldec){
		Shuldec = shuldec;
	}
//	用于跳转页传参数
	public String pagepreferences="";
	public String getPagePreferences(){
		
		return pagepreferences;
	}
	
	public void setPagePreferences(String pagepreferences){
		
		this.pagepreferences=pagepreferences;
	}
	
//	自定义报表
	public Aotcr aotcr;
	public Aotcr getAotcr() {
		return aotcr;
	}
	public void setAotcr(Aotcr aotcr) {
		this.aotcr = aotcr;
	}
//	记录页面Class
	private List Classes;
	public List getClasses() {
		if(Classes == null) {
			Classes = new ArrayList();
		}
		return Classes;
	}
	
	public void setClasses(List c) {
		Classes = c;
	}

	/*
	增加一个StringBuffer变量
	目的:流程处理中使用,用来存储表名、表id、历史意见用
	*/
	private StringBuffer Liucclsb=new StringBuffer();;
	
	public StringBuffer getLiucclsb(){
		
		return Liucclsb;
	}
	
	public void setLiucclsb(StringBuffer value){
		
		Liucclsb=value;
	}
	
	private StringBuffer StringBuffer1=null;
	
	public StringBuffer getStringBuffer1(){
		
		if(StringBuffer1==null){
			
			StringBuffer1 = new StringBuffer();
		}
		
		return StringBuffer1;
	}
	
	public void SetStringBuffer1(StringBuffer value){
		
		if(StringBuffer1==null){
			
			StringBuffer1 = new StringBuffer();
		}
		
		StringBuffer1 = value;
	}
	
	private StringBuffer StringBuffer2=null;
	
	public StringBuffer getStringBuffer2(){
		
		if(StringBuffer2==null){
			
			StringBuffer2 = new StringBuffer();
		}
		
		return StringBuffer2;
	}
	
	public void SetStringBuffer2(StringBuffer value){
		
		if(StringBuffer2==null){
			
			StringBuffer2 = new StringBuffer();
		}
		
		StringBuffer2 = value;
	}
	
	private StringBuffer StringBuffer3=null;
	
	public StringBuffer getStringBuffer3(){
		
		if(StringBuffer3==null){
			
			StringBuffer3 = new StringBuffer();
		}
		
		return StringBuffer3;
	}
	
	public void SetStringBuffer3(StringBuffer value){
		
		if(StringBuffer3==null){
			
			StringBuffer3 = new StringBuffer();
		}
		
		StringBuffer3 = value;
	}
	
	private StringBuffer StringBuffer4=null;
	
	public StringBuffer getStringBuffer4(){
		
		if(StringBuffer4==null){
			
			StringBuffer4 = new StringBuffer();
		}
		
		return StringBuffer4;
	}
	
	public void SetStringBuffer4(StringBuffer value){
		
		if(StringBuffer4==null){
			
			StringBuffer4 = new StringBuffer();
		}
		
		StringBuffer4 = value;
	}
	
	private StringBuffer StringBuffer5=null;
	
	public StringBuffer getStringBuffer5(){
		
		if(StringBuffer5==null){
			
			StringBuffer5 = new StringBuffer();
		}
		
		return StringBuffer5;
	}
	
	public void SetStringBuffer5(StringBuffer value){
		
		if(StringBuffer5==null){
			
			StringBuffer5 = new StringBuffer();
		}
		
		StringBuffer5 = value;
	}
	
	private StringBuffer StringBuffer6=null;
	
	public StringBuffer getStringBuffer6(){
		
		if(StringBuffer6==null){
			
			StringBuffer6 = new StringBuffer();
		}
		
		return StringBuffer6;
	}
	
	public void SetStringBuffer6(StringBuffer value){
		
		if(StringBuffer6==null){
			
			StringBuffer6 = new StringBuffer();
		}
		
		StringBuffer6 = value;
	}
	
	private StringBuffer StringBuffer7=null;
	
	public StringBuffer getStringBuffer7(){
		
		if(StringBuffer7==null){
			
			StringBuffer7 = new StringBuffer();
		}
		
		return StringBuffer7;
	}
	
	public void SetStringBuffer7(StringBuffer value){
		
		if(StringBuffer7==null){
			
			StringBuffer7 = new StringBuffer();
		}
		
		StringBuffer7 = value;
	}
	
	private StringBuffer StringBuffer8=null;
	
	public StringBuffer getStringBuffer8(){
		
		if(StringBuffer8==null){
			
			StringBuffer8 = new StringBuffer();
		}
		
		return StringBuffer8;
	}
	
	public void SetStringBuffer8(StringBuffer value){
		
		if(StringBuffer8==null){
			
			StringBuffer8 = new StringBuffer();
		}
		
		StringBuffer8 = value;
	}
	
	private StringBuffer StringBuffer9=null;
	
	public StringBuffer getStringBuffer9(){
		
		if(StringBuffer9==null){
			
			StringBuffer9 = new StringBuffer();
		}
		
		return StringBuffer9;
	}
	
	public void SetStringBuffer9(StringBuffer value){
		
		if(StringBuffer9==null){
			
			StringBuffer9 = new StringBuffer();
		}
		
		StringBuffer9 = value;
	}
	
	private StringBuffer StringBuffer10=null;
	
	public StringBuffer getStringBuffer10(){
		
		if(StringBuffer10==null){
			
			StringBuffer10 = new StringBuffer();
		}
		
		return StringBuffer10;
	}
	
	public void SetStringBuffer10(StringBuffer value){
		
		if(StringBuffer10==null){
			
			StringBuffer10 = new StringBuffer();
		}
		
		StringBuffer10 = value;
	}
	
	private StringBuffer StringBuffer11=null;
	
	public StringBuffer getStringBuffer11(){
		
		if(StringBuffer11==null){
			
			StringBuffer11 = new StringBuffer();
		}
		
		return StringBuffer11;
	}
	private StringBuffer StringBuffer12=null;
	
	public StringBuffer getStringBuffer12(){
		
		if(StringBuffer12==null){
			
			StringBuffer12 = new StringBuffer();
		}
		
		return StringBuffer12;
	}
	
	public void SetStringBuffer12(StringBuffer value){
	
		if(StringBuffer12==null){
			
			StringBuffer12 = new StringBuffer();
		}
	
		StringBuffer12= value;
	}
	
	private StringBuffer StringBuffer13=null;
	
	public StringBuffer getStringBuffer13(){
		
		if(StringBuffer13==null){
			
			StringBuffer13 = new StringBuffer();
		}
		
		return StringBuffer13;
	}
	public void SetStringBuffer13(StringBuffer value){
	
		if(StringBuffer13==null){
			
			StringBuffer13 = new StringBuffer();
		}
	
		StringBuffer13= value;
	}
	
	private StringBuffer StringBuffer14=null;
	
	public StringBuffer getStringBuffer14(){
		
		if(StringBuffer14==null){
			
			StringBuffer14 = new StringBuffer();
		}
		
		return StringBuffer14;
	}
	public void SetStringBuffer14(StringBuffer value){
	
		if(StringBuffer14==null){
			
			StringBuffer14 = new StringBuffer();
		}
		
		StringBuffer14= value;
	}
	private StringBuffer StringBuffer15=null;
	
	public StringBuffer getStringBuffer15(){
		
		if(StringBuffer15==null){
			
			StringBuffer15 = new StringBuffer();
		}
		
		return StringBuffer15;
	}
	public void SetStringBuffer15(StringBuffer value){
	
		if(StringBuffer15==null){
			
			StringBuffer15 = new StringBuffer();
		}
		
		StringBuffer15= value;
	}
	
	private StringBuffer StringBuffer16=null;
	
	public StringBuffer getStringBuffer16(){
		
		if(StringBuffer16==null){
			
			StringBuffer16 = new StringBuffer();
		}
		
		return StringBuffer16;
	}
	public void SetStringBuffer16(StringBuffer value){
	
		if(StringBuffer16==null){
			
			StringBuffer16 = new StringBuffer();
		}
		
		StringBuffer16= value;
	}
	
	private StringBuffer StringBuffer17=null;
	
	public StringBuffer getStringBuffer17(){
		
		if(StringBuffer17==null){
			
			StringBuffer17 = new StringBuffer();
		}
		
		return StringBuffer17;
	}
	public void SetStringBuffer17(StringBuffer value){
	
		if(StringBuffer17==null){
			
			StringBuffer17 = new StringBuffer();
		}
		
		StringBuffer17= value;
	}
	
	private StringBuffer StringBuffer18=null;
	
	public StringBuffer getStringBuffer18(){
		
		if(StringBuffer18==null){
			
			StringBuffer18 = new StringBuffer();
		}
		
		return StringBuffer18;
	}
	public void SetStringBuffer18(StringBuffer value){
	
		if(StringBuffer18==null){
			
			StringBuffer18 = new StringBuffer();
		}
		
		StringBuffer18= value;
	}
	
	private StringBuffer StringBuffer19=null;
	
	public StringBuffer getStringBuffer19(){
		
		if(StringBuffer19==null){
			
			StringBuffer19 = new StringBuffer();
		}
		
		return StringBuffer19;
	}
	public void SetStringBuffer19(StringBuffer value){
	
		if(StringBuffer19==null){
			
			StringBuffer19 = new StringBuffer();
		}
		
		StringBuffer19= value;
	}
	
	private StringBuffer StringBuffer20=null;
	
	public StringBuffer getStringBuffer20(){
		
		if(StringBuffer20==null){
			
			StringBuffer20 = new StringBuffer();
		}
		
		return StringBuffer20;
	}
	public void SetStringBuffer20(StringBuffer value){
	
		if(StringBuffer20==null){
			
			StringBuffer20 = new StringBuffer();
		}
		
		StringBuffer20= value;
	}
	
	private StringBuffer StringBuffer21=null;
	
	public StringBuffer getStringBuffer21(){
		
		if(StringBuffer21==null){
			
			StringBuffer21 = new StringBuffer();
		}
		
		return StringBuffer21;
	}
	public void SetStringBuffer21(StringBuffer value){
	
		if(StringBuffer21==null){
			
			StringBuffer21 = new StringBuffer();
		}
		
		StringBuffer21= value;
	}
	
	private StringBuffer StringBuffer22=null;
	
	public StringBuffer getStringBuffer22(){
		
		if(StringBuffer22==null){
			
			StringBuffer22 = new StringBuffer();
		}
		
		return StringBuffer22;
	}
	public void SetStringBuffer22(StringBuffer value){
	
		if(StringBuffer22==null){
			
			StringBuffer22 = new StringBuffer();
		}
		
		StringBuffer22= value;
	}
	
	private StringBuffer StringBuffer23=null;
	
	public StringBuffer getStringBuffer23(){
		
		if(StringBuffer23==null){
			
			StringBuffer23 = new StringBuffer();
		}
		
		return StringBuffer23;
	}
	public void SetStringBuffer23(StringBuffer value){
	
		if(StringBuffer23==null){
			
			StringBuffer23 = new StringBuffer();
		}
		
		StringBuffer23= value;
	}
	
	private StringBuffer StringBuffer24=null;
	
	public StringBuffer getStringBuffer24(){
		
		if(StringBuffer24==null){
			
			StringBuffer24 = new StringBuffer();
		}
		
		return StringBuffer24;
	}
	public void SetStringBuffer24(StringBuffer value){
	
		if(StringBuffer24==null){
			
			StringBuffer24 = new StringBuffer();
		}
		
		StringBuffer24= value;
	}
	
	private StringBuffer StringBuffer25=null;
	
	public StringBuffer getStringBuffer25(){
		
		if(StringBuffer25==null){
			
			StringBuffer25 = new StringBuffer();
		}
		
		return StringBuffer25;
	}
	public void SetStringBuffer25(StringBuffer value){
	
		if(StringBuffer25==null){
			
			StringBuffer25 = new StringBuffer();
		}
		
		StringBuffer25= value;
	}
	
private StringBuffer StringBuffer26=null;
	
	public StringBuffer getStringBuffer26(){
		
		if(StringBuffer26==null){
			
			StringBuffer26 = new StringBuffer();
		}
		
		return StringBuffer26;
	}
	public void SetStringBuffer26(StringBuffer value){
	
		if(StringBuffer26==null){
			
			StringBuffer26 = new StringBuffer();
		}
		
		StringBuffer26= value;
	}
	
	private StringBuffer StringBuffer27=null;
	
	public StringBuffer getStringBuffer27(){
		
		if(StringBuffer27==null){
			
			StringBuffer27 = new StringBuffer();
		}
		
		return StringBuffer27;
	}
	public void SetStringBuffer27(StringBuffer value){
	
		if(StringBuffer27==null){
			
			StringBuffer27 = new StringBuffer();
		}
		
		StringBuffer27= value;
	}
	

	private StringBuffer StringBuffer28=null;
	
	public StringBuffer getStringBuffer28(){
		
		if(StringBuffer28==null){
			
			StringBuffer28 = new StringBuffer();
		}
		
		return StringBuffer28;
	}
	public void SetStringBuffer28(StringBuffer value){
	
		if(StringBuffer28==null){
			
			StringBuffer28 = new StringBuffer();
		}
		
		StringBuffer28= value;
	}

}