package com.zhiren.dc.huaygl.ruchy;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Rcgyfx extends BasePage implements PageValidateListener {

	private String _msg;

	protected void initialize() {
		_msg = "";
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

	// -------------------------------------------

	private boolean _RiqSelectchange = false;

	private Date _RiqSelectvalue = new Date();

	public Date getRiqSelect() {
		return _RiqSelectvalue;
	}

	public void setRiqSelect(Date value) {
		if (_RiqSelectvalue.equals(value)) {
			_RiqSelectchange = false;
		} else {
			_RiqSelectvalue = value;
			_RiqSelectchange = true;
		}
	}

	public List getEditTableRow() {
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setEditTableRow(List _value) {
		((Visit) getPage().getVisit()).setList1(_value);
	}

	// ---------------------编号行----------
	public List getSelectValues() {
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setSelectValues(List selectList) {
		((Visit) getPage().getVisit()).setList1(selectList);
	}

	private void Refurbish() {
		// 为 "刷新" 按钮添加处理程序
		// List _list =((Visit) getPage().getVisit()).getEditValues();

		getSelectData();
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

	public void Submit() {
		JDBCcon con = new JDBCcon();
		Save();
		List _list = new ArrayList();
		
		try
		{
			String SQL = "select id,huaysj  from zhillsb where to_char(huaysj, 'yyyy-mm-dd') = '"+ _RiqSelectvalue +"'";
		
		ResultSet rst = con.getResultSet(SQL);
		if(rst.next()){
			setZhillsb_id(rst.getLong("id"));
			}
		rst.close();

			String sql = "update zhillsb set shenhzt = 1  where id = " + getZhillsb_id();
			ResultSet rs = con.getResultSet(sql);
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

	public void Delete()
	{
		
		
		List _list = new ArrayList();
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit)getPage().getVisit();
		
		try
		{
			



			String sql = "delete from zhillsb where id = " + getZhillsb_id();
			String sqlMt = "delete from quansfhyb where zhillsb_id = " + getZhillsb_id(); 
			String sqlSHH = "delete from gongyfsb where zhillsb_id = " + getZhillsb_id();
			String sqlS = "delete from liufhyb where zhillsb_id = " + getZhillsb_id();
			String sqlH = "delete from danthyb where zhillsb_id = " + getZhillsb_id();

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
	
	

	public void Save() {
		Visit visit = ((Visit) getPage().getVisit());
		JDBCcon con = new JDBCcon();
		String strSql = "";
		// -------------全水--------------------------------
		if (Id_mt == -1) {
			strSql = "insert into quansfhyb (id,xuh,zhillsb_id,fenxrq,fenxxmb_id,qimbh,"
					+ "qimmyzl,qimzl,meiyzl,honghzl1,zuizhhzl,shiqzl,shengyzl,zhi,shenhzt,shenhry,lury) values (getnewid("
					+ visit.getDiancxxb_id()
					+ "),1,"
					+ getZhillsb_id()
					+ ",to_date('"
					+ FormatDate(getRiqSelect())
					+ "','yyyy-mm-dd'),"
					+ getFenxxmb_id1()
					+ ",'"
					+ getChenglph1_mt()
					+ "',"
					+ getChenglpzl1_mt()
					+ ","
					+ getChenglpzlys1_mt()
					+ ","
					+ getShiyzl1_mt()
					+ ","
					+ getHonghzzl1_mt()
					+ ","
					+ getJiancxsyhzzl1_mt()
					+ ","
					+ getShiqzl()
					+ ","
					+ getShengyzl()
					+ ","
					+ getQuansf1_mt()
					+ ","
					+ getShenhzt()
					+ ",'"
					+ getShenh1_mt()
					+ "','"
					+ getLury() + "')";
			con.getInsert(strSql);
		} else {
			strSql = "update quansfhyb set fenxrq=to_date('"
					+ FormatDate(getRiqSelect()) + "','yyyy-mm-dd'),qimbh='"
					+ getChenglph1_mt() + "',qimzl=" + getChenglpzl1_mt()
					+ ",qimmyzl=" + getChenglpzlys1_mt() + ",meiyzl="
					+ getShiyzl1_mt() + ",honghzl1=" + getHonghzzl1_mt()
					+ ",zuizhhzl=" + getJiancxsyhzzl1_mt() + ",zhi="
					+ getQuansf1_mt() + ",shenhry='" + getShenh1_mt() + "' "
					+ "where id = " + Id_mt + " and zhillsb_id ="
					+ getZhillsb_id() + " and fenxrq=to_date('"
					+ FormatDate(getFenxrq())
					+ "','yyyy-mm-dd') and fenxxmb_id =" + getFenxxmb_id1()
					+ " and xuh =1 ";
			con.getUpdate(strSql);
		}
		if (Id2_mt == -1) {
			strSql = "insert into quansfhyb (id,xuh,zhillsb_id,fenxrq,fenxxmb_id,qimbh,"
					+ "qimmyzl,qimzl,meiyzl,honghzl1,zuizhhzl,shiqzl,shengyzl,zhi,shenhzt,shenhry,lury) values (getnewid("
					+ visit.getDiancxxb_id()
					+ "),2,"
					+ getZhillsb_id()
					+ ",to_date('"
					+ FormatDate(getRiqSelect())
					+ "','yyyy-mm-dd'),"
					+ getFenxxmb_id1()
					+ ",'"
					+ getChenglph2_mt()
					+ "',"
					+ getChenglpzl2_mt()
					+ ","
					+ getChenglpzlys2_mt()
					+ ","
					+ getShiyzl2_mt()
					+ ","
					+ getHonghzzl2_mt()
					+ ","
					+ getJiancxsyhzzl2_mt()
					+ ","
					+ getShiqzl()
					+ ","
					+ getShengyzl()
					+ ","
					+ getQuansf2_mt()
					+ ","
					+ getShenhzt()
					+ ",'"
					+ getShenh2_mt()
					+ "','"
					+ getLury() + "')";
			con.getInsert(strSql);
		} else {
			strSql = "update quansfhyb set fenxrq=to_date('"
					+ FormatDate(getRiqSelect()) + "','yyyy-mm-dd'),qimbh='"
					+ getChenglph2_mt() + "',qimzl=" + getChenglpzl2_mt()
					+ ",qimmyzl=" + getChenglpzlys2_mt() + ",meiyzl="
					+ getShiyzl2_mt() + ",honghzl1=" + getHonghzzl2_mt()
					+ ",zuizhhzl=" + getJiancxsyhzzl2_mt() + ",zhi="
					+ getQuansf2_mt() + ",shenhry='" + getShenh2_mt() + "' "
					+ "where id = " + Id2_mt + " and zhillsb_id ="
					+ getZhillsb_id() + " and fenxrq=to_date('"
					+ FormatDate(getFenxrq())
					+ "','yyyy-mm-dd') and fenxxmb_id =" + getFenxxmb_id1()
					+ " and xuh =2 ";
			con.getUpdate(strSql);
		}
		if (Id3_mt == -1) {
			strSql = "insert into quansfhyb (id,xuh,zhillsb_id,fenxrq,fenxxmb_id,qimbh,"
					+ "qimmyzl,qimzl,meiyzl,honghzl1,zuizhhzl,shiqzl,shengyzl,zhi,shenhzt,shenhry,lury) values (getnewid("
					+ visit.getDiancxxb_id()
					+ "),3,"
					+ getZhillsb_id()
					+ ",to_date('"
					+ FormatDate(getRiqSelect())
					+ "','yyyy-mm-dd'),"
					+ getFenxxmb_id1()
					+ ",'"
					+ getChenglph3_mt()
					+ "',"
					+ getChenglpzl3_mt()
					+ ","
					+ getChenglpzlys3_mt()
					+ ","
					+ getShiyzl3_mt()
					+ ","
					+ getHonghzzl3_mt()
					+ ","
					+ getJiancxsyhzzl3_mt()
					+ ","
					+ getShiqzl()
					+ ","
					+ getShengyzl()
					+ ","
					+ getQuansf3_mt()
					+ ","
					+ getShenhzt()
					+ ",'"
					+ getShenh3_mt()
					+ "','"
					+ getLury() + "')";
			con.getInsert(strSql);
		} else {
			strSql = "update quansfhyb set  fenxrq=to_date('"
					+ FormatDate(getRiqSelect()) + "','yyyy-mm-dd'),qimbh='"
					+ getChenglph3_mt() + "',qimzl=" + getChenglpzl3_mt()
					+ ",qimmyzl=" + getChenglpzlys3_mt() + ",meiyzl="
					+ getShiyzl3_mt() + ",honghzl1=" + getHonghzzl3_mt()
					+ ",zuizhhzl=" + getJiancxsyhzzl3_mt() + ",zhi="
					+ getQuansf3_mt() + ",shenhry='" + getShenh3_mt() + "' "
					+ "where id = " + Id3_mt + " and zhillsb_id ="
					+ getZhillsb_id() + " and fenxrq=to_date('"
					+ FormatDate(getFenxrq())
					+ "','yyyy-mm-dd') and fenxxmb_id =" + getFenxxmb_id1()
					+ " and xuh =3 ";
			con.getUpdate(strSql);
		}
		if (Id4_mt == -1) {
			strSql = "insert into quansfhyb (id,xuh,zhillsb_id,fenxrq,fenxxmb_id,qimbh,"
					+ "qimmyzl,qimzl,meiyzl,honghzl1,zuizhhzl,shiqzl,shengyzl,zhi,shenhzt,shenhry,lury) values (getnewid("
					+ visit.getDiancxxb_id()
					+ "),4,"
					+ getZhillsb_id()
					+ ",to_date('"
					+ FormatDate(getRiqSelect())
					+ "','yyyy-mm-dd'),"
					+ getFenxxmb_id1()
					+ ",'"
					+ getChenglph4_mt()
					+ "',"
					+ getChenglpzl4_mt()
					+ ","
					+ getChenglpzlys4_mt()
					+ ","
					+ getShiyzl4_mt()
					+ ","
					+ getHonghzzl4_mt()
					+ ","
					+ getJiancxsyhzzl4_mt()
					+ ","
					+ getShiqzl()
					+ ","
					+ getShengyzl()
					+ ","
					+ getQuansf4_mt()
					+ ","
					+ getShenhzt()
					+ ",'"
					+ getShenh4_mt()
					+ "','"
					+ getLury() + "')";
			con.getInsert(strSql);
		} else {
			strSql = "update quansfhyb set fenxrq=to_date('"
					+ FormatDate(getRiqSelect()) + "','yyyy-mm-dd'),qimbh='"
					+ getChenglph4_mt() + "',qimzl=" + getChenglpzl4_mt()
					+ ",qimmyzl=" + getChenglpzlys4_mt() + ",meiyzl="
					+ getShiyzl4_mt() + ",honghzl1=" + getHonghzzl4_mt()
					+ ",zuizhhzl=" + getJiancxsyhzzl4_mt() + ",zhi="
					+ getQuansf4_mt() + ",shenhry='" + getShenh4_mt() + "' "
					+ "where id = " + Id4_mt + " and zhillsb_id ="
					+ getZhillsb_id() + " and fenxrq=to_date('"
					+ FormatDate(getFenxrq())
					+ "','yyyy-mm-dd') and fenxxmb_id =" + getFenxxmb_id1()
					+ " and xuh =4 ";
			con.getUpdate(strSql);
		}
		// ---------------------------工业--------------------------------
		if (Id_sf == -1) {			
				// ---------水分--------------
				strSql = "insert into gongyfsb (id,xuh,zhillsb_id,fenxrq,fenxxmb_id,"
						+ "huayyqbh,qimbh,qimmyzl,qimzl,meiyzl,honghzl1,honghzl2,honghzl3,honghzl4,"
						+ "zuizhhzl,shiqzl,shengyzl,zhi,shenhzt,shenhry,lury)"
						+ " values (getnewid("
						+ visit.getDiancxxb_id()
						+ "),1,"
						+ getZhillsb_id()
						+ ",to_date('"
						+ FormatDate(getRiqSelect())
						+ "','yyyy-mm-dd'),"
						+ getFenxxmb_id2()
						+ ",'"
						+ getHuayyqbh()
						+ "','"
						+ getChenglph1_mad()
						+ "',"
						+ getChenglpzlys1_mad()
						+ ","
						+ getChenglpzl1_mad()
						+ ","
						+ getShiyzl1_mad()
						+ ","
						+ getHonghzzl1_mad()
						+ ","
						+ getHonghzl2()
						+ ","
						+ getHonghzl3()
						+ ","
						+ getHonghzl4()
						+ ","
						+ getJiancxsyhzzl1_mad()
						+ ","
						+ getShiqzl()
						+ ","
						+ getShengyzl()
						+ ","
						+ getQuansf1_mad()
						+ ","
						+ getShenhzt()
						+ ",'"
						+ getShenh1_mad()
						+ "','"
						+ getLury() + "')";
				con.getInsert(strSql);
			}else {			
					// ---------水分--------------
				strSql = "update gongyfsb set fenxrq=to_date('"
					+ FormatDate(getRiqSelect())
					+ "','yyyy-mm-dd'),qimbh='" + getChenglph1_mad()
					+ "',qimzl=" + getChenglpzl1_mad() + ",qimmyzl="
					+ getChenglpzlys1_mad() + ",meiyzl=" + getShiyzl1_mad()
					+ ",honghzl1=" + getHonghzzl1_mad() + ",zuizhhzl="
					+ getJiancxsyhzzl1_mad() + ",zhi=" + getQuansf1_mad()
					+ ",shenhry='" + getShenh1_mad() + "' " + "where id = "
					+ Id_sf + " and zhillsb_id =" + getZhillsb_id()
					+ " and fenxrq=to_date('" + FormatDate(getFenxrq())
					+ "','yyyy-mm-dd') and fenxxmb_id =" + getFenxxmb_id2()
					+ " and xuh =1 ";
			con.getUpdate(strSql);
				} 
		if (Id2_sf == -1) {			
			// ---------水分--------------
			strSql = "insert into gongyfsb (id,xuh,zhillsb_id,fenxrq,fenxxmb_id,"
				+ "huayyqbh,qimbh,qimmyzl,qimzl,meiyzl,honghzl1,honghzl2,honghzl3,honghzl4,"
				+ "zuizhhzl,shiqzl,shengyzl,zhi,shenhzt,shenhry,lury)"
				+ " values (getnewid("
				+ visit.getDiancxxb_id()
				+ "),2,"
				+ getZhillsb_id()
				+ ",to_date('"
				+ FormatDate(getRiqSelect())
				+ "','yyyy-mm-dd'),"
				+ getFenxxmb_id2()
				+ ",'"
				+ getHuayyqbh()
				+ "','"
				+ getChenglph2_mad()
				+ "',"
				+ getChenglpzlys2_mad()
				+ ","
				+ getChenglpzl2_mad()
				+ ","
				+ getShiyzl2_mad()
				+ ","
				+ getHonghzzl2_mad()
				+ ","
				+ getHonghzl2()
				+ ","
				+ getHonghzl3()
				+ ","
				+ getHonghzl4()
				+ ","
				+ getJiancxsyhzzl2_mad()
				+ ","
				+ getShiqzl()
				+ ","
				+ getShengyzl()
				+ ","
				+ getQuansf2_mad()
				+ ","
				+ getShenhzt()
				+ ",'"
				+ getShenh2_mad()
				+ "','"
				+ getLury() + "')";
		con.getInsert(strSql);
		}else {			
				// ---------水分--------------
			strSql = "update gongyfsb set fenxrq=to_date('"
				+ FormatDate(getRiqSelect())
				+ "','yyyy-mm-dd'),qimbh='" + getChenglph2_mad()
				+ "',qimzl=" + getChenglpzl2_mad() + ",qimmyzl="
				+ getChenglpzlys2_mad() + ",meiyzl=" + getShiyzl2_mad()
				+ ",honghzl1=" + getHonghzzl2_mad() + ",zuizhhzl="
				+ getJiancxsyhzzl2_mad() + ",zhi=" + getQuansf2_mad()
				+ ",shenhry='" + getShenh2_mad() + "' " + "where id = "
				+ Id2_sf + " and zhillsb_id =" + getZhillsb_id()
				+ " and fenxrq=to_date('" + FormatDate(getFenxrq())
				+ "','yyyy-mm-dd') and fenxxmb_id =" + getFenxxmb_id2()
				+ " and xuh =2 ";
		con.getUpdate(strSql);
			} 
		if (Id3_sf == -1) {			
			// ---------水分--------------
			strSql = "insert into gongyfsb (id,xuh,zhillsb_id,fenxrq,fenxxmb_id,"
				+ "huayyqbh,qimbh,qimmyzl,qimzl,meiyzl,honghzl1,honghzl2,honghzl3,honghzl4,"
				+ "zuizhhzl,shiqzl,shengyzl,zhi,shenhzt,shenhry,lury)"
				+ " values (getnewid("
				+ visit.getDiancxxb_id()
				+ "),3,"
				+ getZhillsb_id()
				+ ",to_date('"
				+ FormatDate(getRiqSelect())
				+ "','yyyy-mm-dd'),"
				+ getFenxxmb_id2()
				+ ",'"
				+ getHuayyqbh()
				+ "','"
				+ getChenglph3_mad()
				+ "',"
				+ getChenglpzlys3_mad()
				+ ","
				+ getChenglpzl3_mad()
				+ ","
				+ getShiyzl3_mad()
				+ ","
				+ getHonghzzl3_mad()
				+ ","
				+ getHonghzl2()
				+ ","
				+ getHonghzl3()
				+ ","
				+ getHonghzl4()
				+ ","
				+ getJiancxsyhzzl3_mad()
				+ ","
				+ getShiqzl()
				+ ","
				+ getShengyzl()
				+ ","
				+ getQuansf3_mad()
				+ ","
				+ getShenhzt()
				+ ",'"
				+ getShenh3_mad()
				+ "','"
				+ getLury() + "')";
		con.getInsert(strSql);
		}else {			
				// ---------水分--------------
			strSql = "update gongyfsb set fenxrq=to_date('"
				+ FormatDate(getRiqSelect())
				+ "','yyyy-mm-dd'),qimbh='" + getChenglph3_mad()
				+ "',qimzl=" + getChenglpzl3_mad() + ",qimmyzl="
				+ getChenglpzlys3_mad() + ",meiyzl=" + getShiyzl3_mad()
				+ ",honghzl1=" + getHonghzzl3_mad() + ",zuizhhzl="
				+ getJiancxsyhzzl3_mad() + ",zhi=" + getQuansf3_mad()
				+ ",shenhry='" + getShenh3_mad() + "' " + "where id = "
				+ Id3_sf + " and zhillsb_id =" + getZhillsb_id()
				+ " and fenxrq=to_date('" + FormatDate(getFenxrq())
				+ "','yyyy-mm-dd') and fenxxmb_id =" + getFenxxmb_id2()
				+ " and xuh =3 ";
		con.getUpdate(strSql);
			} 
		if (Id4_sf == -1) {			
			// ---------水分--------------
			strSql = "insert into gongyfsb (id,xuh,zhillsb_id,fenxrq,fenxxmb_id,"
				+ "huayyqbh,qimbh,qimmyzl,qimzl,meiyzl,honghzl1,honghzl2,honghzl3,honghzl4,"
				+ "zuizhhzl,shiqzl,shengyzl,zhi,shenhzt,shenhry,lury)"
				+ " values (getnewid("
				+ visit.getDiancxxb_id()
				+ "),4,"
				+ getZhillsb_id()
				+ ",to_date('"
				+ FormatDate(getRiqSelect())
				+ "','yyyy-mm-dd'),"
				+ getFenxxmb_id2()
				+ ",'"
				+ getHuayyqbh()
				+ "','"
				+ getChenglph4_mad()
				+ "',"
				+ getChenglpzlys4_mad()
				+ ","
				+ getChenglpzl4_mad()
				+ ","
				+ getShiyzl4_mad()
				+ ","
				+ getHonghzzl4_mad()
				+ ","
				+ getHonghzl2()
				+ ","
				+ getHonghzl3()
				+ ","
				+ getHonghzl4()
				+ ","
				+ getJiancxsyhzzl4_mad()
				+ ","
				+ getShiqzl()
				+ ","
				+ getShengyzl()
				+ ","
				+ getQuansf4_mad()
				+ ","
				+ getShenhzt()
				+ ",'"
				+ getShenh4_mad()
				+ "','"
				+ getLury() + "')";
		con.getInsert(strSql);
		}else {			
				// ---------水分--------------
			strSql = "update gongyfsb set fenxrq=to_date('"
				+ FormatDate(getRiqSelect())
				+ "','yyyy-mm-dd'),qimbh='" + getChenglph4_mad()
				+ "',qimzl=" + getChenglpzl4_mad() + ",qimmyzl="
				+ getChenglpzlys4_mad() + ",meiyzl=" + getShiyzl4_mad()
				+ ",honghzl1=" + getHonghzzl4_mad() + ",zuizhhzl="
				+ getJiancxsyhzzl4_mad() + ",zhi=" + getQuansf4_mad()
				+ ",shenhry='" + getShenh4_mad() + "' " + "where id = "
				+ Id4_sf + " and zhillsb_id =" + getZhillsb_id()
				+ " and fenxrq=to_date('" + FormatDate(getFenxrq())
				+ "','yyyy-mm-dd') and fenxxmb_id =" + getFenxxmb_id2()
				+ " and xuh =4 ";
		con.getUpdate(strSql);
			} 
		//----------------------------挥发分------------------------------
		if (Id_huif == -1) {
			strSql = "insert into gongyfsb (id,xuh,zhillsb_id,fenxrq,fenxxmb_id,"
				+ "huayyqbh,qimbh,qimmyzl,qimzl,meiyzl,honghzl1,honghzl2,honghzl3,honghzl4,"
				+ "zuizhhzl,shiqzl,shengyzl,zhi,shenhzt,shenhry,lury)"
				+ " values (getnewid("
				+ visit.getDiancxxb_id()
				+ "),1,"
				+ getZhillsb_id()
				+ ",to_date('"
				+ FormatDate(getRiqSelect())
				+ "','yyyy-mm-dd'),"
				+ getFenxxmb_id2()
				+ ",'"
				+ getHuayyqbh()
				+ "','"
				+ getGangh1_vad()
				+ "',"
				+ getGangzlsyzl1_vad()
				+ ","
				+ getGangzl1_vad()
				+ ","
				+ getShiyzl1_vad()
				+ ","
				+ getJiarhzzl1_vad()
				+ ","
				+ getHonghzl2()
				+ ","
				+ getHonghzl3()
				+ ","
				+ getHonghzl4()
				+ ","
				+ getZuizhhzl()
				+ ","
				+ getJianshzl1_vad()
				+ ","
				+ getShengyzl()
				+ ","
				+ getHuiffvad1_vad()
				+ ","
				+ getShenhzt()
				+ ",'"
				+ getShenh1_vad()
				+ "','"
				+ getLury() + "')";
		
		con.getInsert(strSql);
		}else{
			strSql = "update gongyfsb set fenxrq=to_date('"
				+ FormatDate(getRiqSelect())
				+ "','yyyy-mm-dd'),qimbh='" + getGangh1_vad()
				+ "',qimzl=" + getGangzl1_vad() + ",qimmyzl="
				+ getGangzlsyzl1_vad() + ",meiyzl=" + getShiyzl1_vad()
				+ ",honghzl1=" + getJiarhzzl1_vad() + ",shiqzl="
				+ getJianshzl1_vad() + ",zhi=" + getHuiffvad1_vad()
				+ ",shenhry='" + getShenh1_vad() + "' " + "where id = "
				+ Id_huif + " and zhillsb_id =" + getZhillsb_id()
				+ " and fenxrq=to_date('" + FormatDate(getFenxrq())
				+ "','yyyy-mm-dd') and fenxxmb_id =" + getFenxxmb_id2()
				+ " and xuh =1 ";
		
		con.getUpdate(strSql);
		}
		//-----------------------------灰分-------------------------------
		if (Id_hf == -1) {					
			strSql = "insert into gongyfsb (id,xuh,zhillsb_id,fenxrq,fenxxmb_id,"
					+ "huayyqbh,qimbh,qimmyzl,qimzl,meiyzl,honghzl1,honghzl2,honghzl3,honghzl4,"
					+ "zuizhhzl,shiqzl,shengyzl,zhi,shenhzt,shenhry,lury)"
					+ " values (getnewid("
					+ visit.getDiancxxb_id()
					+ "),1,"
					+ getZhillsb_id()
					+ ",to_date('"
					+ FormatDate(getRiqSelect())
					+ "','yyyy-mm-dd'),"
					+ getFenxxmb_id2()
					+ ",'"
					+ getHuayyqbh()
					+ "','"
					+ getHuimh1_aad()
					+ "',"
					+ getHuimzlsyzl1_aad ()
					+ ","
					+ getHuimzl1_aad()
					+ ","
					+ getShiyzl1_aad()
					+ ","
					+ getJiarhzzl1_aad()
					+ ","
					+ getHonghzl2()
					+ ","
					+ getHonghzl3()
					+ ","
					+ getHonghzl4()
					+ ","
					+ getShiyhzl1_aad()
					+ ","
					+ getShiqzl()
					+ ","
					+ getCanlwzl1_aad()
					+ ","
					+ getHuifaad1_aad()
					+ ","
					+ getShenhzt()
					+ ",'"
					+ getShenh1_aad()
					+ "','"
					+ getLury() + "')";
			con.getInsert(strSql);
		}else {			
			strSql = "update gongyfsb set fenxrq=to_date('"
				+ FormatDate(getRiqSelect())
				+ "','yyyy-mm-dd'),qimbh='" + getHuimh1_aad()
				+ "',qimzl=" + getHuimzl1_aad() + ",qimmyzl="
				+ getHuimzlsyzl1_aad() + ",meiyzl=" + getShiyzl1_aad()
				+ ",honghzl1=" + getJiarhzzl1_aad() + ",zuizhhzl="
				+ getShiyhzl1_aad() + ",shengyzl="+getCanlwzl1_aad()+",zhi=" + getHuifaad1_aad()
				+ ",shenhry='" + getShenh1_aad() + "' " + "where id = "
				+ Id_sf + " and zhillsb_id =" + getZhillsb_id()
				+ " and fenxrq=to_date('" + FormatDate(getFenxrq())
				+ "','yyyy-mm-dd') and fenxxmb_id =" + getFenxxmb_id2()
				+ " and xuh =1 ";
		con.getUpdate(strSql);
			} 
		// ----------------------------全硫-------------------------------
		if (Id_s == -1) {
			strSql = "insert into liufhyb (id,xuh,zhillsb_id,fenxrq,fenxxmb_id,"
					+ "huayyqbh,qimmyzl,qimzl,meiyzl,zhi,shenhzt,shenhry,lury)"
					+ " values (getnewid("
					+ visit.getDiancxxb_id()
					+ "),1,"
					+ getZhillsb_id()
					+ ",to_date('"
					+ FormatDate(getRiqSelect())
					+ "','yyyy-mm-dd'),"
					+ getFenxxmb_id5()
					+ ",'"
					+ getQimh1()
					+ "',"
					+ getQimzlsyzl1()
					+ ","
					+ getQimzl1()
					+ ","
					+ getShiyzl1_s()
					+ ","
					+ getQuanlst1_d()
					+ ","
					+ getShenhzt()
					+ ",'"
					+ getShenh1_s()
					+ "','"
					+ getLury()
					+ "')";
			con.getInsert(strSql);
		} else {
			strSql = "update liufhyb set fenxrq=to_date('"
					+ FormatDate(getRiqSelect()) + "','yyyy-mm-dd'),huayyqbh='"
					+ getQimh1() + "',qimzl=" + getQimzl1() + ",qimmyzl="
					+ getQimzlsyzl1() + ",meiyzl=" + getShiyzl1_s() + ",zhi="
					+ getQuanlst1_d() + ",shenhry='" + getShenh1_s() + "' "
					+ "where id = " + Id_s + " and zhillsb_id ="
					+ getZhillsb_id() + " and fenxrq=to_date('"
					+ FormatDate(getFenxrq())
					+ "','yyyy-mm-dd') and fenxxmb_id =" + getFenxxmb_id5()
					+ " and xuh =1 ";
			con.getUpdate(strSql);
		}
		if (Id2_s == -1) {
			strSql = "insert into liufhyb (id,xuh,zhillsb_id,fenxrq,fenxxmb_id,"
					+ "huayyqbh,qimmyzl,qimzl,meiyzl,zhi,shenhzt,shenhry,lury)"
					+ " values (getnewid("
					+ visit.getDiancxxb_id()
					+ "),2,"
					+ getZhillsb_id()
					+ ",to_date('"
					+ FormatDate(getRiqSelect())
					+ "','yyyy-mm-dd'),"
					+ getFenxxmb_id5()
					+ ",'"
					+ getQimh2()
					+ "',"
					+ getQimzlsyzl2()
					+ ","
					+ getQimzl2()
					+ ","
					+ getShiyzl2_s()
					+ ","
					+ getQuanlst2_d()
					+ ","
					+ getShenhzt()
					+ ",'"
					+ getShenh2_s()
					+ "','"
					+ getLury()
					+ "')";
			con.getInsert(strSql);
		} else {
			strSql = "update liufhyb set fenxrq=to_date('"
					+ FormatDate(getRiqSelect()) + "','yyyy-mm-dd'),huayyqbh='"
					+ getQimh2() + "',qimzl=" + getQimzl2() + ",qimmyzl="
					+ getQimzlsyzl2() + ",meiyzl=" + getShiyzl2_s() + ",zhi="
					+ getQuanlst2_d() + ",shenhry='" + getShenh2_s() + "' "
					+ "where id = " + Id2_s + " and zhillsb_id ="
					+ getZhillsb_id() + " and fenxrq=to_date('"
					+ FormatDate(getFenxrq())
					+ "','yyyy-mm-dd') and fenxxmb_id =" + getFenxxmb_id5()
					+ " and xuh =2 ";
			con.getUpdate(strSql);
		}
		if (Id3_s == -1) {
			strSql = "insert into liufhyb (id,xuh,zhillsb_id,fenxrq,fenxxmb_id,"
					+ "huayyqbh,qimmyzl,qimzl,meiyzl,zhi,shenhzt,shenhry,lury)"
					+ " values (getnewid("
					+ visit.getDiancxxb_id()
					+ "),3,"
					+ getZhillsb_id()
					+ ",to_date('"
					+ FormatDate(getRiqSelect())
					+ "','yyyy-mm-dd'),"
					+ getFenxxmb_id5()
					+ ",'"
					+ getQimh3()
					+ "',"
					+ getQimzlsyzl3()
					+ ","
					+ getQimzl3()
					+ ","
					+ getShiyzl3_s()
					+ ","
					+ getQuanlst3_d()
					+ ","
					+ getShenhzt()
					+ ",'"
					+ getShenh3_s()
					+ "','"
					+ getLury()
					+ "')";
			con.getInsert(strSql);
		} else {
			strSql = "update liufhyb set fenxrq=to_date('"
					+ FormatDate(getRiqSelect()) + "','yyyy-mm-dd'),huayyqbh='"
					+ getQimh3() + "',qimzl=" + getQimzl3() + ",qimmyzl="
					+ getQimzlsyzl3() + ",meiyzl=" + getShiyzl3_s() + ",zhi="
					+ getQuanlst3_d() + ",shenhry='" + getShenh3_s() + "' "
					+ "where id = " + Id3_s + " and zhillsb_id ="
					+ getZhillsb_id() + " and fenxrq=to_date('"
					+ FormatDate(getFenxrq())
					+ "','yyyy-mm-dd') and fenxxmb_id =" + getFenxxmb_id5()
					+ " and xuh =3 ";
			con.getUpdate(strSql);
		}
		if (Id4_s == -1) {
			strSql = "insert into liufhyb (id,xuh,zhillsb_id,fenxrq,fenxxmb_id,"
					+ "huayyqbh,qimmyzl,qimzl,meiyzl,zhi,shenhzt,shenhry,lury)"
					+ " values (getnewid("
					+ visit.getDiancxxb_id()
					+ "),4,"
					+ getZhillsb_id()
					+ ",to_date('"
					+ FormatDate(getRiqSelect())
					+ "','yyyy-mm-dd'),"
					+ getFenxxmb_id5()
					+ ",'"
					+ getQimh4()
					+ "',"
					+ getQimzlsyzl4()
					+ ","
					+ getQimzl4()
					+ ","
					+ getShiyzl4_s()
					+ ","
					+ getQuanlst4_d()
					+ ","
					+ getShenhzt()
					+ ",'"
					+ getShenh4_s()
					+ "','"
					+ getLury()
					+ "')";
			con.getInsert(strSql);
		} else {
			strSql = "update liufhyb set fenxrq=to_date('"
					+ FormatDate(getRiqSelect()) + "','yyyy-mm-dd'),huayyqbh='"
					+ getQimh4() + "',qimzl=" + getQimzl4() + ",qimmyzl="
					+ getQimzlsyzl4() + ",meiyzl=" + getShiyzl4_s() + ",zhi="
					+ getQuanlst4_d() + ",shenhry='" + getShenh4_s() + "' "
					+ "where id = " + Id4_s + " and zhillsb_id ="
					+ getZhillsb_id() + " and fenxrq=to_date('"
					+ FormatDate(getFenxrq())
					+ "','yyyy-mm-dd') and fenxxmb_id =" + getFenxxmb_id5()
					+ " and xuh =4 ";
			con.getUpdate(strSql);
		}
		// ----------------------------发热量-------------------------------
		if (Id_f == -1) {
			strSql = "insert into danthyb (id,xuh,zhillsb_id,fenxrq,fenxxmb_id,"
					+ "huayyqbh,qimzl,qimmyzl,meiyzl,shenhzt,shenhry,lury,tianjwrz,tianjwzl,zhi)"
					+ " values (getnewid("
					+ visit.getDiancxxb_id()
					+ "),1,"
					+ getZhillsb_id()
					+ ",to_date('"
					+ FormatDate(getRiqSelect())
					+ "','yyyy-mm-dd'),"
					+ getFenxxmb_id6()
					+ ",'"
					+ getHuayyqbh()
					+ "',"
					+ getGangzl1()
					+ ","
					+ getGangzlsyzl1()
					+ ","
					+ getShiyzl1_f()
					+ ","
					+ getShenhzt()
					+ ",'"
					+ getShenh1_f()
					+ "','"
					+ getLury()
					+ "',"
					+ getTianjwrz()
					+ "," + getTianjwzl() + "," + getQb1() + ")";
			con.getInsert(strSql);
		} else {
			strSql = "update danthyb set  fenxrq=to_date('"
					+ FormatDate(getRiqSelect()) + "','yyyy-mm-dd'),qimzl="
					+ getGangzl1() + ",qimmyzl=" + getGangzlsyzl1()
					+ ",meiyzl=" + getShiyzl1_f() + ",zhi=" + getQb1()
					+ ",shenhry='" + getShenh1_f() + "' " + "where id=" + Id_f
					+ " and zhillsb_id =" + getZhillsb_id()
					+ " and fenxrq=to_date('" + FormatDate(getFenxrq())
					+ "','yyyy-mm-dd') and fenxxmb_id =" + getFenxxmb_id6()
					+ " and xuh =1 ";
			con.getUpdate(strSql);
		}
		if (Id2_f == -1) {
			strSql = "insert into danthyb (id,xuh,zhillsb_id,fenxrq,fenxxmb_id,"
					+ "huayyqbh,qimzl,qimmyzl,meiyzl,shenhzt,shenhry,lury,tianjwrz,tianjwzl,zhi)"
					+ " values (getnewid("
					+ visit.getDiancxxb_id()
					+ "),2,"
					+ getZhillsb_id()
					+ ",to_date('"
					+ FormatDate(getRiqSelect())
					+ "','yyyy-mm-dd'),"
					+ getFenxxmb_id6()
					+ ",'"
					+ getHuayyqbh()
					+ "',"
					+ getGangzl2()
					+ ","
					+ getGangzlsyzl2()
					+ ","
					+ getShiyzl2_f()
					+ ","
					+ getShenhzt()
					+ ",'"
					+ getShenh2_f()
					+ "','"
					+ getLury()
					+ "',"
					+ getTianjwrz()
					+ "," + getTianjwzl() + "," + getQb2() + ")";
			con.getInsert(strSql);
		} else {
			strSql = "update danthyb set  fenxrq=to_date('"
					+ FormatDate(getRiqSelect()) + "','yyyy-mm-dd'),qimzl="
					+ getGangzl2() + ",qimmyzl=" + getGangzlsyzl2()
					+ ",meiyzl=" + getShiyzl2_f() + ",zhi=" + getQb2()
					+ ",shenhry='" + getShenh2_f() + "' " + "where id=" + Id2_f
					+ " and zhillsb_id =" + getZhillsb_id()
					+ " and fenxrq=to_date('" + FormatDate(getFenxrq())
					+ "','yyyy-mm-dd') and fenxxmb_id =" + getFenxxmb_id6()
					+ " and xuh =2 ";
			con.getUpdate(strSql);
		}
		if (Id3_f == -1) {
			strSql = "insert into danthyb (id,xuh,zhillsb_id,fenxrq,fenxxmb_id,"
					+ "huayyqbh,qimzl,qimmyzl,meiyzl,shenhzt,shenhry,lury,tianjwrz,tianjwzl,zhi)"
					+ " values (getnewid("
					+ visit.getDiancxxb_id()
					+ "),3,"
					+ getZhillsb_id()
					+ ",to_date('"
					+ FormatDate(getRiqSelect())
					+ "','yyyy-mm-dd'),"
					+ getFenxxmb_id6()
					+ ",'"
					+ getHuayyqbh()
					+ "',"
					+ getGangzl3()
					+ ","
					+ getGangzlsyzl3()
					+ ","
					+ getShiyzl3_f()
					+ ","
					+ getShenhzt()
					+ ",'"
					+ getShenh3_f()
					+ "','"
					+ getLury()
					+ "',"
					+ getTianjwrz()
					+ "," + getTianjwzl() + "," + getQb3() + ")";
			con.getInsert(strSql);
		} else {
			strSql = "update danthyb set  fenxrq=to_date('"
					+ FormatDate(getRiqSelect()) + "','yyyy-mm-dd'),qimzl="
					+ getGangzl3() + ",qimmyzl=" + getGangzlsyzl3()
					+ ",meiyzl=" + getShiyzl3_f() + ",zhi=" + getQb3()
					+ ",shenhry='" + getShenh3_f() + "' " + "where id=" + Id3_f
					+ " and zhillsb_id =" + getZhillsb_id()
					+ " and fenxrq=to_date('" + FormatDate(getFenxrq())
					+ "','yyyy-mm-dd') and fenxxmb_id =" + getFenxxmb_id6()
					+ " and xuh =3 ";
			con.getUpdate(strSql);
		}
		if (Id4_f == -1) {
			strSql = "insert into danthyb (id,xuh,zhillsb_id,fenxrq,fenxxmb_id,"
					+ "huayyqbh,qimzl,qimmyzl,meiyzl,shenhzt,shenhry,lury,tianjwrz,tianjwzl,zhi)"
					+ " values (getnewid("
					+ visit.getDiancxxb_id()
					+ "),4,"
					+ getZhillsb_id()
					+ ",to_date('"
					+ FormatDate(getRiqSelect())
					+ "','yyyy-mm-dd'),"
					+ getFenxxmb_id6()
					+ ",'"
					+ getHuayyqbh()
					+ "',"
					+ getGangzl4()
					+ ","
					+ getGangzlsyzl4()
					+ ","
					+ getShiyzl4_f()
					+ ","
					+ getShenhzt()
					+ ",'"
					+ getShenh4_f()
					+ "','"
					+ getLury()
					+ "',"
					+ getTianjwrz()
					+ "," + getTianjwzl() + "," + getQb4() + ")";
			con.getInsert(strSql);
		} else {
			strSql = "update danthyb set  fenxrq=to_date('"
					+ FormatDate(getRiqSelect()) + "','yyyy-mm-dd'),qimzl="
					+ getGangzl4() + ",qimmyzl=" + getGangzlsyzl4()
					+ ",meiyzl=" + getShiyzl4_f() + ",zhi=" + getQb4()
					+ ",shenhry='" + getShenh4_f() + "' " + "where id=" + Id4_f
					+ " and zhillsb_id =" + getZhillsb_id()
					+ " and fenxrq=to_date('" + FormatDate(getFenxrq())
					+ "','yyyy-mm-dd') and fenxxmb_id =" + getFenxxmb_id6()
					+ " and xuh =4 ";
			con.getUpdate(strSql);
		}
		con.Close();

		getSelectData();
	}
	
	
	
	
	

	public List getSelectData() {

//		JDBCcon JDBCcon = new JDBCcon();
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
	
		
		try {
			
			String SQL = "select id,huaysj  from zhillsb where to_char(huaysj, 'yyyy-mm-dd') = '"+ FormatDate(_RiqSelectvalue) +"'";
			
				ResultSet rst = con.getResultSet(SQL);
				if(rst.next()){
					setZhillsb_id(rst.getLong("id"));
					}
				rst.close();
			
			
			
			
			
			
			
			
			
			// ------------取氢值---------
			long qing = 0;
			String sql = "select ysf.zhi,ysm.zhuangt from yuansfxb ysf,yuansxmb ysm,(\n"
					+ "select fh.meikxxb_id as meikxxb_id,fh.pinzb_id as pinzb_id,fh.yunsfsb_id as yunsfsb_id\n"
					+ "  from yangpdhb yg, caiyb cy, zhilb zl,\n"
					+ " fahb fh\n"
					+ "where yg.caiyb_id = cy.id\n"
					+ "  and cy.zhilb_id = zl.id\n"
					+ "  and fh.zhilb_id=zl.id\n"
					+ "  and yg.bianh ='"
					+ getBianhValue().getValue()
					+ "')c\n"
					+ "  where ysf.yuansxmb_id=ysm.id\n"
					+ "  and ysf.meikxxb_id=c.meikxxb_id\n"
					+ "  and ysf.meizb_id=c.pinzb_id\n"
					+ "  and ysf.yunsfsb_id =c.yunsfsb_id\n"
					+ "  and ysm.zhuangt=1 and ysf.zhuangt=1";
			ResultSet rs = con.getResultSet(sql);
			while (rs.next()) {
				qing = rs.getLong("zhi");
				setQing(qing);
			}
			rs.close();
			sql = "select yg.zhilblsb_id,\n"
					+ "               fh.meikxxb_id as meikxxb_id,\n"
					+ "               fh.pinzb_id as pinzb_id,\n"
					+ "               fh.yunsfsb_id as yunsfsb_id\n"
					+ "          from yangpdhb yg, caiyb cy, zhilb zl, fahb fh\n"
					+ "         where yg.caiyb_id = cy.id\n"
					+ "           and cy.zhilb_id = zl.id\n"
					+ "           and fh.zhilb_id = zl.id\n"
					+ "           and yg.bianh = '"
					+ getBianhValue().getValue() + "'";
			rs = con.getResultSet(sql);
			while (rs.next()) {
				setZhillsb_id(rs.getLong("zhilblsb_id"));
			}
			rs.close();
			
			
			// ---------全水-------
			rs = con.getResultSet("select id,mingc from fenxxmb");
			while (rs.next()) {
				if (rs.getString("mingc").equals("全水分")) {
					fenxxmb_id1 = rs.getLong("id");
				} else if (rs.getString("mingc").equals("水分")) {
					fenxxmb_id2 = rs.getLong("id");
				} else if (rs.getString("mingc").equals("挥发分")) {
					fenxxmb_id4 = rs.getLong("id");
				} else if (rs.getString("mingc").equals("灰分")) {
					fenxxmb_id3 = rs.getLong("id");
				} else if (rs.getString("mingc").equals("硫分")) {
					fenxxmb_id5 = rs.getLong("id");
				} else if (rs.getString("mingc").equals("弹筒热值")) {
					fenxxmb_id6 = rs.getLong("id");
				}
			}
			rs.close();
			sql = "select qs.id,\n" + "       qs.xuh,\n" + "       qs.qimbh,\n"
					+ "       qs.qimzl,\n" + "       qs.qimmyzl,\n"
					+ "       qs.meiyzl,\n" + "       qs.honghzl1,\n"
					+ "       qs.zuizhhzl,\n" + "       qs.zhi,\n"
					+ "       qs.shenhry,\n" + "       qs.zhillsb_id,\n"
					+ "       qs.fenxxmb_id,\n" + "       qs.fenxrq,\n"
					+ "       qs.huayyqbh,\n" + "       qs.shiqzl,\n"
					+ "       qs.shengyzl,\n" + "       qs.shenhzt,\n"
					+ "       qs.lury\n"
					+ "  from quansfhyb qs, zhillsb zls, yangpdhb yg\n"
					+ " where qs.zhillsb_id = zls.id\n"
					+ "   and yg.zhilblsb_id = zls.id\n"
					+ "   and qs.fenxxmb_id = " + fenxxmb_id1
					+ " and yg.bianh='" + getBianhValue().getValue() + "'";
			rs = con.getResultSet(sql);
			while (rs.next()) {
				// --------------------------------------
				setFenxrq(rs.getDate("fenxrq"));
				setHuayyqbh(rs.getString("huayyqbh"));
				setShiqzl(rs.getLong("shiqzl"));
				setShengyzl(rs.getLong("shengyzl"));
				setShenhzt(rs.getLong("shenhzt"));
				setLury(rs.getString("lury"));
				// ---------------------------------------
				if (rs.getInt("xuh") == 1) {
					setId_mt(rs.getLong("id"));
					setChenglph1_mt(rs.getString("qimbh"));
					setChenglpzl1_mt(rs.getLong("qimzl"));
					setChenglpzlys1_mt(rs.getLong("qimmyzl"));
					setShiyzl1_mt(rs.getLong("meiyzl"));
					setHonghzzl1_mt(rs.getLong("honghzl1"));
					setJiancxsyhzzl1_mt(rs.getLong("zuizhhzl"));
					setQuansf1_mt(rs.getLong("zhi"));
					setShenh1_mt(rs.getString("shenhry"));
				} else if (rs.getInt("xuh") == 2) {
					setId2_mt(rs.getLong("id"));
					setChenglph2_mt(rs.getString("qimbh"));
					setChenglpzl2_mt(rs.getLong("qimzl"));
					setChenglpzlys2_mt(rs.getLong("qimmyzl"));
					setShiyzl2_mt(rs.getLong("meiyzl"));
					setHonghzzl2_mt(rs.getLong("honghzl1"));
					setJiancxsyhzzl2_mt(rs.getLong("zuizhhzl"));
					setQuansf2_mt(rs.getLong("zhi"));
					setShenh2_mt(rs.getString("shenhry"));
				} else if (rs.getInt("xuh") == 3) {
					setId3_mt(rs.getLong("id"));
					setChenglph3_mt(rs.getString("qimbh"));
					setChenglpzl3_mt(rs.getLong("qimzl"));
					setChenglpzlys3_mt(rs.getLong("qimmyzl"));
					setShiyzl3_mt(rs.getLong("meiyzl"));
					setHonghzzl3_mt(rs.getLong("honghzl1"));
					setJiancxsyhzzl3_mt(rs.getLong("zuizhhzl"));
					setQuansf3_mt(rs.getLong("zhi"));
					setShenh3_mt(rs.getString("shenhry"));
				} else if (rs.getInt("xuh") == 4) {
					setId4_mt(rs.getLong("id"));
					setChenglph4_mt(rs.getString("qimbh"));
					setChenglpzl4_mt(rs.getLong("qimzl"));
					setChenglpzlys4_mt(rs.getLong("qimmyzl"));
					setShiyzl4_mt(rs.getLong("meiyzl"));
					setHonghzzl4_mt(rs.getLong("honghzl1"));
					setJiancxsyhzzl4_mt(rs.getLong("zuizhhzl"));
					setQuansf4_mt(rs.getLong("zhi"));
					setShenh4_mt(rs.getString("shenhry"));
				}
			}
			rs.close();
			// ---------------水分-----------------
			String chenglph2_mad = "";
			long chenglpzl2_mad = 0;
			long chenglpzlys2_mad = 0;
			long shiyzl2_mad = 0;
			long honghzzl2_mad = 0;
			long jiancxsyhzzl2_mad = 0;
			long quansf2_mad = 0;
			String shenh2_mad = "";

			String chenglph3_mad = "";
			long chenglpzl3_mad = 0;
			long chenglpzlys3_mad = 0;
			long shiyzl3_mad = 0;
			long honghzzl3_mad = 0;
			long jiancxsyhzzl3_mad = 0;
			long quansf3_mad = 0;
			String shenh3_mad = "";

			String chenglph4_mad = "";
			long chenglpzl4_mad = 0;
			long chenglpzlys4_mad = 0;
			long shiyzl4_mad = 0;
			long honghzzl4_mad = 0;
			long jiancxsyhzzl4_mad = 0;
			long quansf4_mad = 0;
			String shenh4_mad = "";
			sql = "select gf.id,gf.xuh,gf.zhillsb_id,gf.fenxrq,gf.fenxxmb_id,\n"
					+ "gf.huayyqbh,gf.qimbh,gf.qimzl,gf.qimmyzl,gf.meiyzl\n"
					+ ",gf.honghzl1,gf.honghzl2,gf.honghzl3,gf.honghzl4,\n"
					+ "gf.zuizhhzl,gf.shiqzl,gf.shengyzl,gf.zhi,gf.shenhzt,gf.shenhry,gf.lury"
					+ " from gongyfsb gf,zhillsb zls,yangpdhb yg\n"
					+ "where yg.zhilblsb_id=zls.id and gf.fenxxmb_id="
					+ fenxxmb_id2 + " and gf.zhillsb_id=zls.id\n"
					+ "and yg.bianh='" + getBianhValue().getValue() + "'";

			rs = con.getResultSet(sql);
			while (rs.next()) {
				setFenxrq(rs.getDate("fenxrq"));
				setHuayyqbh(rs.getString("huayyqbh"));
				setHonghzl2(rs.getDouble("honghzl2"));
				setHonghzl3(rs.getDouble("honghzl3"));
				setHonghzl4(rs.getDouble("honghzl4"));
				setShiqzl(rs.getDouble("shiqzl"));
				setShengyzl(rs.getDouble("shengyzl"));
				setShenhzt(rs.getLong("shenhzt"));
				setLury(rs.getString("lury"));
				if (rs.getInt("xuh") == 1) {
					setId_sf(rs.getLong("id"));
					setChenglph1_mad(rs.getString("qimbh"));
					setChenglpzl1_mad(rs.getDouble("qimzl"));
					setChenglpzlys1_mad(rs.getDouble("qimmyzl"));
					setShiyzl1_mad(rs.getDouble("meiyzl"));
					setHonghzzl1_mad(rs.getDouble("honghzl1"));
					setJiancxsyhzzl1_mad(rs.getDouble("zuizhhzl"));
					setQuansf1_mad(rs.getDouble("zhi"));
					setShenh1_mad(rs.getString("shenhry"));
				} else if (rs.getInt("xuh") == 2) {
					setId2_sf(rs.getLong("id"));
					chenglph2_mad = rs.getString("qimbh");
					chenglpzl2_mad = rs.getLong("qimzl");
					chenglpzlys2_mad = rs.getLong("qimmyzl");
					shiyzl2_mad = rs.getLong("meiyzl");
					honghzzl2_mad = rs.getLong("honghzl1");
					jiancxsyhzzl2_mad = rs.getLong("zuizhhzl");
					quansf2_mad = rs.getLong("zhi");
					shenh2_mad = rs.getString("shenhry");
					setChenglph2_mad(chenglph2_mad);
					setChenglpzl2_mad(chenglpzl2_mad);
					setChenglpzlys2_mad(chenglpzlys2_mad);
					setShiyzl2_mad(shiyzl2_mad);
					setHonghzzl2_mad(honghzzl2_mad);
					setJiancxsyhzzl2_mad(jiancxsyhzzl2_mad);
					setQuansf2_mad(quansf2_mad);
					setShenh2_mad(shenh2_mad);
				} else if (rs.getInt("xuh") == 3) {
					setId3_sf(rs.getLong("id"));
					chenglph3_mad = rs.getString("qimbh");
					chenglpzl3_mad = rs.getLong("qimzl");
					chenglpzlys3_mad = rs.getLong("qimmyzl");
					shiyzl3_mad = rs.getLong("meiyzl");
					honghzzl3_mad = rs.getLong("honghzl1");
					jiancxsyhzzl3_mad = rs.getLong("zuizhhzl");
					quansf3_mad = rs.getLong("zhi");
					shenh3_mad = rs.getString("shenhry");
					setChenglph3_mad(chenglph3_mad);
					setChenglpzl3_mad(chenglpzl3_mad);
					setChenglpzlys3_mad(chenglpzlys3_mad);
					setShiyzl3_mad(shiyzl3_mad);
					setHonghzzl3_mad(honghzzl3_mad);
					setJiancxsyhzzl3_mad(jiancxsyhzzl3_mad);
					setQuansf3_mad(quansf3_mad);
					setShenh3_mad(shenh3_mad);
				} else if (rs.getInt("xuh") == 4) {
					setId4_sf(rs.getLong("id"));
					chenglph4_mad = rs.getString("qimbh");
					chenglpzl4_mad = rs.getLong("qimzl");
					chenglpzlys4_mad = rs.getLong("qimmyzl");
					shiyzl4_mad = rs.getLong("meiyzl");
					honghzzl4_mad = rs.getLong("honghzl1");
					jiancxsyhzzl4_mad = rs.getLong("zuizhhzl");
					quansf4_mad = rs.getLong("zhi");
					shenh4_mad = rs.getString("shenhry");
					setChenglph4_mad(chenglph4_mad);
					setChenglpzl4_mad(chenglpzl4_mad);
					setChenglpzlys4_mad(chenglpzlys4_mad);
					setShiyzl4_mad(shiyzl4_mad);
					setHonghzzl4_mad(honghzzl4_mad);
					setJiancxsyhzzl4_mad(jiancxsyhzzl4_mad);
					setQuansf4_mad(quansf4_mad);
					setShenh4_mad(shenh4_mad);
				}
			}
			rs.close();
			// ---------挥发份-----
			String gangh2_vad = "";
			long gangzl2_vad = 0;
			long gangzlsyzl2_vad = 0;
			long shiyzl2_vad = 0;
			long jiarhzzl2_vad = 0;
			long jianshzl2_vad = 0;
			long huiffvad2_vad = 0;
			long huiffvdaf2_vad = 0;
			String shenh2_vad = "";

			String gangh3_vad = "";
			long gangzl3_vad = 0;
			long gangzlsyzl3_vad = 0;
			long shiyzl3_vad = 0;
			long jiarhzzl3_vad = 0;
			long jianshzl3_vad = 0;
			long huiffvad3_vad = 0;
			long huiffvdaf3_vad = 0;
			String shenh3_vad = "";

			String gangh4_vad = "";
			long gangzl4_vad = 0;
			long gangzlsyzl4_vad = 0;
			long shiyzl4_vad = 0;
			long jiarhzzl4_vad = 0;
			long jianshzl4_vad = 0;
			long huiffvad4_vad = 0;
			long huiffvdaf4_vad = 0;
			String shenh4_vad = "";
			sql = "select gf.id,gf.xuh,gf.zhillsb_id,gf.fenxrq,gf.fenxxmb_id,\n"
					+ "gf.huayyqbh,gf.qimbh,gf.qimzl,gf.qimmyzl,gf.meiyzl\n"
					+ ",gf.honghzl1,gf.honghzl2,gf.honghzl3,gf.honghzl4,\n"
					+ "gf.zuizhhzl,gf.shiqzl,gf.shengyzl,gf.zhi,gf.shenhzt,gf.shenhry,gf.lury"
					+ " from gongyfsb gf,zhillsb zls,yangpdhb yg\n"
					+ "where yg.zhilblsb_id=zls.id and gf.zhillsb_id=zls.id and gf.fenxxmb_id="
					+ fenxxmb_id4 + " and yg.bianh='"
					+ getBianhValue().getValue() + "'";
			rs = con.getResultSet(sql);
			while (rs.next()) {
				setFenxrq(rs.getDate("fenxrq"));
				setHuayyqbh(rs.getString("huayyqbh"));
				setHonghzl2(rs.getDouble("honghzl2"));
				setHonghzl3(rs.getDouble("honghzl3"));
				setHonghzl4(rs.getDouble("honghzl4"));
				setZuizhhzl(rs.getDouble("zuizhhzl"));
				setShengyzl(rs.getDouble("shengyzl"));
				setShenhzt(rs.getLong("shenhzt"));
				setLury(rs.getString("lury"));
				if (rs.getLong("xuh") == 1) {
					setId_huif(rs.getLong("id"));
					setGangh1_vad(rs.getString("qimbh"));
					setGangzl1_vad(rs.getLong("qimzl"));
					setGangzlsyzl1_vad(rs.getLong("qimmyzl"));
					setShiyzl1_vad(rs.getLong("meiyzl"));
					setJiarhzzl1_vad(rs.getLong("honghzl1"));
					setJianshzl1_vad(rs.getLong("shiqzl"));
					setHuiffvad1_vad(rs.getLong("zhi"));
					setShenh1_vad(rs.getString("shenhry"));
				} else if (rs.getLong("xuh") == 2) {
					setId2_huif(rs.getLong("id"));
					gangh2_vad = rs.getString("qimbh");
					gangzl2_vad = rs.getLong("qimzl");
					gangzlsyzl2_vad = rs.getLong("qimmyzl");
					shiyzl2_vad = rs.getLong("meiyzl");
					jiarhzzl2_vad = rs.getLong("honghzl1");
					jianshzl2_vad = rs.getLong("shiqzl");
					huiffvad2_vad = rs.getLong("zhi");
					shenh2_vad = rs.getString("shenhry");
					setGangh2_vad(gangh2_vad);
					setGangzl2_vad(gangzl2_vad);
					setGangzlsyzl2_vad(gangzlsyzl2_vad);
					setShiyzl2_vad(shiyzl2_vad);
					setJiarhzzl2_vad(jiarhzzl2_vad);
					setJianshzl2_vad(jianshzl2_vad);
					setHuiffvad2_vad(huiffvad2_vad);
					setShenh2_vad(shenh2_vad);
				} else if (rs.getLong("xuh") == 3) {
					setId3_huif(rs.getLong("id"));
					gangh3_vad = rs.getString("qimbh");
					gangzl3_vad = rs.getLong("qimzl");
					gangzlsyzl3_vad = rs.getLong("qimmyzl");
					shiyzl3_vad = rs.getLong("meiyzl");
					jiarhzzl3_vad = rs.getLong("honghzl1");
					jianshzl3_vad = rs.getLong("shiqzl");
					huiffvad3_vad = rs.getLong("zhi");
					shenh3_vad = rs.getString("shenhry");
					setGangh3_vad(gangh3_vad);
					setGangzl3_vad(gangzl3_vad);
					setGangzlsyzl3_vad(gangzlsyzl3_vad);
					setShiyzl3_vad(shiyzl3_vad);
					setJiarhzzl3_vad(jiarhzzl3_vad);
					setJianshzl3_vad(jianshzl3_vad);
					setHuiffvad3_vad(huiffvad3_vad);
					setShenh3_vad(shenh3_vad);
				} else if (rs.getLong("xuh") == 4) {
					setId4_huif(rs.getLong("id"));
					gangh4_vad = rs.getString("qimbh");
					gangzl4_vad = rs.getLong("qimzl");
					gangzlsyzl4_vad = rs.getLong("qimmyzl");
					shiyzl4_vad = rs.getLong("meiyzl");
					jiarhzzl4_vad = rs.getLong("honghzl1");
					jianshzl4_vad = rs.getLong("shiqzl");
					huiffvad4_vad = rs.getLong("zhi");
					shenh4_vad = rs.getString("shenhry");
					setGangh4_vad(gangh4_vad);
					setGangzl4_vad(gangzl4_vad);
					setGangzlsyzl4_vad(gangzlsyzl4_vad);
					setShiyzl4_vad(shiyzl4_vad);
					setJiarhzzl4_vad(jiarhzzl4_vad);
					setJianshzl4_vad(jianshzl4_vad);
					setHuiffvad4_vad(huiffvad4_vad);
					setShenh4_vad(shenh4_vad);
				}

			}
			rs.close();
			// ------------灰分-------

			String huimh1_aad = "";// 灰皿号
			long huimzl1_aad = 0;// 灰皿质量(g)
			long huimzlsyzl1_aad = 0;// 灰皿质量+试样质量(g)
			long shiyzl1_aad = 0;// 试样质量m(g)
			long jiarhzzl1_aad = 0;// 加热后总质量(g)
			long shiyhzl1_aad = 0;// 检查性试验后总质量(g)
			long canlwzl1_aad = 0;// 残留物的质量m1(g)
			long huifaad1_aad = 0;// 灰分Aad(%)
			String shenh1_aad = "";// 审核_aad

			String huimh2_aad = "";// 灰皿号
			long huimzl2_aad = 0;// 灰皿质量(g)
			long huimzlsyzl2_aad = 0;// 灰皿质量+试样质量(g)
			long shiyzl2_aad = 0;// 试样质量m(g)
			long jiarhzzl2_aad = 0;// 加热后总质量(g)
			long shiyhzl2_aad = 0;// 检查性试验后总质量(g)
			long canlwzl2_aad = 0;// 残留物的质量m2(g)
			long huifaad2_aad = 0;// 灰分Aad(%)
			String shenh2_aad = "";

			String huimh3_aad = "";// 灰皿号
			long huimzl3_aad = 0;// 灰皿质量(g)
			long huimzlsyzl3_aad = 0;// 灰皿质量+试样质量(g)
			long shiyzl3_aad = 0;// 试样质量m(g)
			long jiarhzzl3_aad = 0;// 加热后总质量(g)
			long shiyhzl3_aad = 0;// 检查性试验后总质量(g)
			long canlwzl3_aad = 0;// 残留物的质量m3(g)
			long huifaad3_aad = 0;// 灰分Aad(%)
			String shenh3_aad = "";

			String huimh4_aad = "";// 灰皿号
			long huimzl4_aad = 0;// 灰皿质量(g)
			long huimzlsyzl4_aad = 0;// 灰皿质量+试样质量(g)
			long shiyzl4_aad = 0;// 试样质量m(g)
			long jiarhzzl4_aad = 0;// 加热后总质量(g)
			long shiyhzl4_aad = 0;// 检查性试验后总质量(g)
			long canlwzl4_aad = 0;// 残留物的质量m4(g)
			long huifaad4_aad = 0;// 灰分Aad(%)
			String shenh4_aad = "";

			sql = "select gf.id,gf.xuh,gf.zhillsb_id,gf.fenxrq,gf.fenxxmb_id,\n"
					+ "gf.huayyqbh,gf.qimbh,gf.qimzl,gf.qimmyzl,gf.meiyzl\n"
					+ ",gf.honghzl1,gf.honghzl2,gf.honghzl3,gf.honghzl4,\n"
					+ "gf.zuizhhzl,gf.shiqzl,gf.shengyzl,gf.zhi,gf.shenhzt,gf.shenhry,gf.lury"
					+ " from gongyfsb gf,zhillsb zls,yangpdhb yg\n"
					+ "where yg.zhilblsb_id=zls.id and gf.zhillsb_id=zls.id and  gf.fenxxmb_id="
					+ fenxxmb_id3 + " and yg.bianh='"
					+ getBianhValue().getValue() + "'";
			rs = con.getResultSet(sql);
			while (rs.next()) {
				setFenxrq(rs.getDate("fenxrq"));
				setHuayyqbh(rs.getString("huayyqbh"));
				setHonghzl2(rs.getDouble("honghzl2"));
				setHonghzl3(rs.getDouble("honghzl3"));
				setHonghzl4(rs.getDouble("honghzl4"));
				setShiqzl(rs.getDouble("shiqzl"));
				setShengyzl(rs.getDouble("shengyzl"));
				setShenhzt(rs.getLong("shenhzt"));
				setLury(rs.getString("lury"));
				if (rs.getLong("xuh") == 1) {
					setId_hf(rs.getLong("id"));
					huimh1_aad = rs.getString("qimbh");
					huimzl1_aad = rs.getLong("qimzl");
					huimzlsyzl1_aad = rs.getLong("qimmyzl");
					shiyzl1_aad = rs.getLong("meiyzl");
					jiarhzzl1_aad = rs.getLong("honghzl1");
					shiyhzl1_aad = rs.getLong("zuizhhzl");
					canlwzl1_aad = rs.getLong("shengyzl");
					huifaad1_aad = rs.getLong("zhi");
					shenh1_aad = rs.getString("shenhry");
					setHuimh1_aad(huimh1_aad);
					setHuimzl1_aad(huimzl1_aad);
					setHuimzlsyzl1_aad(huimzlsyzl1_aad);
					setShiyhzl1_aad(shiyhzl1_aad);
					setJiarhzzl1_aad(jiarhzzl1_aad);
					setShiyhzl1_aad(shiyhzl1_aad);
					setCanlwzl1_aad(canlwzl1_aad);
					setHuifaad1_aad(huifaad1_aad);
					setShenh1_aad(shenh1_aad);
				} else if (rs.getLong("xuh") == 2) {
					setId2_hf(rs.getLong("id"));
					huimh2_aad = rs.getString("qimbh");
					huimzl2_aad = rs.getLong("qimzl");
					huimzlsyzl2_aad = rs.getLong("qimmyzl");
					shiyzl2_aad = rs.getLong("meiyzl");
					jiarhzzl2_aad = rs.getLong("honghzl2");
					shiyhzl1_aad = rs.getLong("zuizhhzl");
					canlwzl2_aad = rs.getLong("shengyzl");
					huifaad2_aad = rs.getLong("zhi");
					shenh2_aad = rs.getString("shenhry");
					setHuimh2_aad(huimh2_aad);
					setHuimzl2_aad(huimzl2_aad);
					setHuimzlsyzl2_aad(huimzlsyzl2_aad);
					setShiyhzl2_aad(shiyhzl2_aad);
					setJiarhzzl2_aad(jiarhzzl2_aad);
					setShiyhzl2_aad(shiyhzl2_aad);
					setCanlwzl2_aad(canlwzl2_aad);
					setHuifaad2_aad(huifaad2_aad);
					setShenh2_aad(shenh2_aad);
				} else if (rs.getLong("xuh") == 3) {
					setId3_hf(rs.getLong("id"));
					huimh3_aad = rs.getString("qimbh");
					huimzl3_aad = rs.getLong("qimzl");
					huimzlsyzl3_aad = rs.getLong("qimmyzl");
					shiyzl3_aad = rs.getLong("meiyzl");
					jiarhzzl3_aad = rs.getLong("honghzl3");
					shiyhzl1_aad = rs.getLong("zuizhhzl");
					canlwzl3_aad = rs.getLong("shengyzl");
					huifaad3_aad = rs.getLong("zhi");
					shenh3_aad = rs.getString("shenhry");
					setHuimh3_aad(huimh3_aad);
					setHuimzl3_aad(huimzl3_aad);
					setHuimzlsyzl3_aad(huimzlsyzl3_aad);
					setShiyhzl3_aad(shiyhzl3_aad);
					setJiarhzzl3_aad(jiarhzzl3_aad);
					setShiyhzl3_aad(shiyhzl3_aad);
					setCanlwzl3_aad(canlwzl3_aad);
					setHuifaad3_aad(huifaad3_aad);
					setShenh3_aad(shenh3_aad);
				} else if (rs.getLong("xuh") == 4) {
					setId4_hf(rs.getLong("id"));
					huimh4_aad = rs.getString("qimbh");
					huimzl4_aad = rs.getLong("qimzl");
					huimzlsyzl4_aad = rs.getLong("qimmyzl");
					shiyzl4_aad = rs.getLong("meiyzl");
					jiarhzzl4_aad = rs.getLong("honghzl1");
					shiyhzl4_aad = rs.getLong("zuizhhzl");
					canlwzl4_aad = rs.getLong("shengyzl");
					huifaad4_aad = rs.getLong("zhi");
					shenh4_aad = rs.getString("shenhry");
					setHuimh4_aad(huimh4_aad);
					setHuimzl4_aad(huimzl4_aad);
					setHuimzlsyzl4_aad(huimzlsyzl4_aad);
					setShiyhzl4_aad(shiyhzl4_aad);
					setJiarhzzl4_aad(jiarhzzl4_aad);
					setShiyhzl4_aad(shiyhzl4_aad);
					setCanlwzl4_aad(canlwzl4_aad);
					setHuifaad4_aad(huifaad4_aad);
					setShenh4_aad(shenh4_aad);
				}
			}
			rs.close();
			// --------------全硫----
			sql = "select lf.id,lf.xuh,lf.huayyqbh,lf.qimzl,lf.qimmyzl,lf.meiyzl,\n"
					+ " lf.zhi,lf.shenhry,lf.zhillsb_id,lf.fenxxmb_id,lf.fenxrq,lf.shenhzt,lf.lury\n"
					+ " from liufhyb lf,zhillsb zls,yangpdhb yg\n"
					+ "where lf.zhillsb_id=zls.id and yg.zhilblsb_id=zls.id and lf.fenxxmb_id="
					+ fenxxmb_id5
					+ " and\n"
					+ " yg.bianh='"
					+ getBianhValue().getValue() + "'";
			rs = con.getResultSet(sql);
			while (rs.next()) {
				// ------------------------------------------
				setFenxrq(rs.getDate("fenxrq"));
				setShenhzt(rs.getLong("shenhzt"));
				setLury(rs.getString("lury"));
				// ----------------------------------------------
				if (rs.getLong("xuh") == 1) {
					setId_s(rs.getLong("id"));
					setQimh1(rs.getString("huayyqbh"));
					setQimzl1(rs.getLong("qimzl"));
					setQimzlsyzl1(rs.getLong("qimmyzl"));
					setShiyzl1_s(rs.getLong("meiyzl"));
					setQuanlst1_d(rs.getLong("zhi"));
					setShenh1_s(rs.getString("shenhry"));
				} else if (rs.getLong("xuh") == 2) {
					setId2_s(rs.getLong("id"));
					setQimh2(rs.getString("huayyqbh"));
					setQimzl2(rs.getLong("qimzl"));
					setQimzlsyzl2(rs.getLong("qimmyzl"));
					setShiyzl2_s(rs.getLong("meiyzl"));
					setQuanlst2_d(rs.getLong("zhi"));
					setShenh2_s(rs.getString("shenhry"));
				} else if (rs.getLong("xuh") == 3) {
					setId3_s(rs.getLong("id"));
					setQimh3(rs.getString("huayyqbh"));
					setQimzl3(rs.getLong("qimzl"));
					setQimzlsyzl3(rs.getLong("qimmyzl"));
					setShiyzl3_s(rs.getLong("meiyzl"));
					setQuanlst3_d(rs.getLong("zhi"));
					setShenh3_s(rs.getString("shenhry"));
				} else if (rs.getLong("xuh") == 4) {
					setId4_s(rs.getLong("id"));
					setQimh4(rs.getString("huayyqbh"));
					setQimzl4(rs.getLong("qimzl"));
					setQimzlsyzl4(rs.getLong("qimmyzl"));
					setShiyzl4_s(rs.getLong("meiyzl"));
					setQuanlst4_d(rs.getLong("zhi"));
					setShenh4_s(rs.getString("shenhry"));
				}
			}
			rs.close();
			// -----------发热量----------
			sql = "select dt.id,dt.xuh,dt.qimzl,dt.qimmyzl,dt.meiyzl,"
					+ "dt.zhi,dt.shenhry,dt.zhillsb_id,dt.fenxrq,dt.fenxxmb_id,"
					+ "dt.huayyqbh,dt.shenhzt,dt.lury,dt.tianjwrz,dt.tianjwzl\n"
					+ "from danthyb dt,zhillsb zls,yangpdhb yg\n"
					+ "where dt.zhillsb_id=zls.id and yg.zhilblsb_id=zls.id\n"
					+ "and dt.fenxxmb_id=" + fenxxmb_id6 + " and yg.bianh='"
					+ getBianhValue().getValue() + "'";
			rs = con.getResultSet(sql);
			while (rs.next()) {
				// --------------------------------------
				setFenxrq(rs.getDate("fenxrq"));
				setHuayyqbh(rs.getString("huayyqbh"));
				setShenhzt(rs.getLong("shenhzt"));
				setLury(rs.getString("lury"));
				setTianjwrz(rs.getLong("tianjwrz"));
				setTianjwzl(rs.getLong("tianjwzl"));
				// ---------------------------------------
				if (rs.getLong("xuh") == 1) {
					setId_f(rs.getLong("id"));
					setGangzl1(rs.getLong("qimzl"));
					setGangzlsyzl1(rs.getLong("qimmyzl"));
					setShiyzl1_f(rs.getLong("meiyzl"));
					setQb1(rs.getLong("zhi"));
					setShenh1_f(rs.getString("shenhry"));
				} else if (rs.getLong("xuh") == 2) {
					setId2_f(rs.getLong("id"));
					setGangzl2(rs.getLong("qimzl"));
					setGangzlsyzl2(rs.getLong("qimmyzl"));
					setShiyzl2_f(rs.getLong("meiyzl"));
					setQb2(rs.getLong("zhi"));
					setShenh2_f(rs.getString("shenhry"));
				} else if (rs.getLong("xuh") == 3) {
					setId3_f(rs.getLong("id"));
					setGangzl3(rs.getLong("qimzl"));
					setGangzlsyzl3(rs.getLong("qimmyzl"));
					setShiyzl3_f(rs.getLong("meiyzl"));
					setQb3(rs.getLong("zhi"));
					setShenh3_f(rs.getString("shenhry"));
				} else if (rs.getLong("xuh") == 4) {
					setId4_f(rs.getLong("id"));
					setGangzl4(rs.getLong("qimzl"));
					setGangzlsyzl4(rs.getLong("qimmyzl"));
					setShiyzl4_f(rs.getLong("meiyzl"));
					setQb4(rs.getLong("zhi"));
					setShenh4_f(rs.getString("shenhry"));
				}
			}
			rs.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void pageValidate(PageEvent arg0) {
		String PageName = arg0.getPage().getPageName();
		String ValPageName = Login.ValidateLogin(arg0.getPage());
		if (!PageName.equals(ValPageName)) {
			ValPageName = Login.ValidateAdmin(arg0.getPage());
			if (!PageName.equals(ValPageName)) {
				IPage ipage = arg0.getRequestCycle().getPage(ValPageName);
				throw new PageRedirectException(ipage);
			}
		}
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			getShujztModels();
			getBianhModel();
		}
		getSelectData();
	}

	// ----------------------------------------------------------------
	private boolean BianhChange = false;

	private IDropDownBean _BianhValue;

	private IPropertySelectionModel _BianhModel;

	public IDropDownBean getBianhValue() {
		if (_BianhValue == null) {
			_BianhValue = (IDropDownBean) getBianhModel().getOption(0);
		}
		return _BianhValue;
	}

	public void setBianhValue(IDropDownBean Value) {
		if (Value.getId() != getBianhValue().getId()) {
			BianhChange = true;
		}
		_BianhValue = Value;
	}

	public void setBianhModel(IDropDownModel value) {
		_BianhModel = value;
	}

	public IPropertySelectionModel getBianhModel() {
		if (_BianhModel == null) {
			String sql = "select id ,bianh  from yangpdhb order by id";
			_BianhModel = new IDropDownModel(sql);
		}
		return _BianhModel;
	}

	// ---------------------------------------------------------------
	private boolean ischange;

	private static IPropertySelectionModel _ShujztModel;

	public IPropertySelectionModel getShujztModel() {
		if (_ShujztModel == null) {
			getShujztModels();
		}
		return _ShujztModel;
	}

	private IDropDownBean _ShujztValue;

	public IDropDownBean getShujztValue() {
		if (_ShujztValue == null) {
			_ShujztValue = (IDropDownBean) getShujztModels().getOption(0);
		}
		return _ShujztValue;
	}

	public void setShujztValue(IDropDownBean Value) {
		if (Value != null && getShujztValue() != null) {
			if (Value.getId() != getShujztValue().getId()) {
				ischange = true;
			}
		}
		_ShujztValue = Value;
	}

	public IPropertySelectionModel getShujztModels() {
		List listShujzt = new ArrayList();
		listShujzt.add(new IDropDownBean(0, "未录入"));
		listShujzt.add(new IDropDownBean(1, "未提交"));
		_ShujztModel = new IDropDownModel(listShujzt);
		return _ShujztModel;
	}

	public void setShujztModel(IDropDownModel _value) {
		_ShujztModel = _value;
	}

	// ----------------------------------------------------------------
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

	// ---------------bean-----------------
	private long m_id;

	private String m_bianh;

	public long getId1() {
		return m_id;
	}

	public void setId1(long id) {
		this.m_id = id;
	}

	public String getBianh() {
		return m_bianh;
	}

	public void setBianh(String bianh) {
		this.m_bianh = bianh;
	}

	// ------------------------------------------
	private double Qing = 0.0D;// 氢

	public double getQing() {
		return Qing;
	}

	public void setQing(double qing) {
		this.Qing = qing;
	}

	// -----------------id----------------
	private long Id_mt = -1;// id_全水分

	private long Id_sf = -1;// id_水分
	private long Id_hf=-1;//id_灰分
	private long Id_huif=-1;//id_挥发分
	private long Id2_hf=-1;
	private long Id3_hf=-1;
	private long Id4_hf=-1;
	private long Id2_huif=-1;
	private long Id3_huif=-1;
	private long Id4_huif=-1;
	
	private long Id_s = -1;// id_s全硫

	private long Id_f = -1;// Id_f发热量

	private long Id2_mt = -1;// Id_全水分

	private long Id2_sf = -1;// Id_水分

	private long Id2_s = -1;// Id_s全硫

	private long Id2_f = -1;// Id_f发热量

	private long Id3_mt = -1;// Id_全水分

	private long Id3_sf = -1;// Id_水分

	private long Id3_s = -1;// Id_s全硫

	private long Id3_f = -1;// Id_f发热量

	private long Id4_mt = -1;// Id_全水分

	private long Id4_sf = -1;// Id_水分

	private long Id4_s = -1;// Id_s全硫

	private long Id4_f = -1;// Id_f发热量

	// ------------------------------------
	private String Chenglph1_mt = null;// 称量瓶(盘)号_全水分

	private String Chenglph1_mad = null;// 称量瓶(盘)号_水分

	private double Chenglpzl1_mt = 0.0D;// 称量瓶(盘)质量(g)_全水分

	private double Chenglpzl1_mad = 0.0D;// 称量瓶(盘)质量(g)_水分

	private double Chenglpzlys1_mt = 0.0D;// 称量瓶(盘)质量+试样质量(g)_全水分

	private double Chenglpzlys1_mad = 0.0D;// 称量瓶(盘)质量+试样质量(g)_水分

	private double Shiyzl1_mt = 0.0D;// 试样质量m(g)_全水分

	private double Shiyzl1_mad = 0.0D;// 试样质量m(g)_水分

	private double Honghzzl1_mt = 0.0D;// 烘后总质量(g)_全水分

	private double Honghzzl1_mad = 0.0D;// 烘后总质量(g)_水分

	private double Jiancxsyhzzl1_mt = 0.0D;// 检查性试验后总质量(g)_全水分

	private double Jiancxsyhzzl1_mad = 0.0D;// 检查性试验后总质量(g)_水分

	private double Quansf1_mt = 0.0D;// 全水分Mt=m1/m*100(%)_全水分

	private double Quansf1_mad = 0.0D;// 全水分Mt=m1/m*100(%)_水分

	private String Shenh1_mt = null;// 审核_全水分

	private String Shenh1_mad = null;// 审核_水分

	private String Gangh1_vad = null;// 坩埚号

	private String Huimh1_aad = null;// 灰皿号

	private double Gangzl1_vad = 0.0D;// 坩埚质量(g)

	private double Huimzl1_aad = 0.0D;// 灰皿质量(g)

	private double Gangzlsyzl1_vad = 0.0D;// 坩埚质量+试样质量(g)

	private double Huimzlsyzl1_aad = 0.0D;// 灰皿质量+试样质量(g)

	private double Shiyzl1_vad = 0.0D;// 试样质量m(g)

	private double Shiyzl1_aad = 0.0D;// 试样质量m(g)

	private double Jiarhzzl1_vad = 0.0D;// 加热后总质量(g)

	private double Jiarhzzl1_aad = 0.0D;// 加热后总质量(g)

	private double Jianshzl1_vad = 0.0D;// 煤样加热后减少的质量m1(g)

	private double Shiyhzl1_aad = 0.0D;// 检查性试验后总质量(g)

	private double Huiffvad1_vad = 0.0D;// 挥发分Vad=m1/m*100-Mad(%)

	private double Canlwzl1_aad = 0.0D;// 残留物的质量m1(g)

	private double Huiffvdaf1_vad = 0.0D;// 挥发分Vdaf(%)

	private double Huifaad1_aad = 0.0D;// 灰分Aad(%)

	private String Shenh1_vad = null;// 审核_vad

	private String Shenh1_aad = null;// 审核_aad

	private String Qimh1 = null;// 器皿号

	private double Gangzl1 = 0.0D;// 坩埚质量(g)

	private double Qimzl1 = 0.0D;// 器皿质量(g)

	private double Gangzlsyzl1 = 0.0D;// 坩埚质量+试样质量(g)

	private double Qimzlsyzl1 = 0.0D;// 器皿质量+试样质量(g)

	private double Shiyzl1_f = 0.0D;// 试样质量m(g)

	private double Shiyzl1_s = 0.0D;// 试样质量m(g)

	private double Qb1 = 0.0D;// Qb,ad(J/g)

	private double Quanlst1_ad = 0.0D;// 全硫St,ad(%)

	private double Qgr1 = 0.0D;// Qgr,ad(J/g)

	private double Quanlst1_d = 0.0D;// 全硫St,d(%)

	private double Qnent1 = 0.0D;// Qnet,ar(MJ/kg)

	private String Shenh1_s = null;// 审核_s

	private String Shenh1_f = null;// 审核_f

	// ------------------------------------------
	private String Chenglph2_mt = null;// 称量瓶(盘)号_全水分

	private String Chenglph2_mad = null;// 称量瓶(盘)号_水分

	private double Chenglpzl2_mt = 0.0D;// 称量瓶(盘)质量(g)_全水分

	private double Chenglpzl2_mad = 0.0D;// 称量瓶(盘)质量(g)_水分

	private double Chenglpzlys2_mt = 0.0D;// 称量瓶(盘)质量+试样质量(g)_全水分

	private double Chenglpzlys2_mad = 0.0D;// 称量瓶(盘)质量+试样质量(g)_水分

	private double Shiyzl2_mt = 0.0D;// 试样质量m(g)_全水分

	private double Shiyzl2_mad = 0.0D;// 试样质量m(g)_水分

	private double Honghzzl2_mt = 0.0D;// 烘后总质量(g)_全水分

	private double Honghzzl2_mad = 0.0D;// 烘后总质量(g)_水分

	private double Jiancxsyhzzl2_mt = 0.0D;// 检查性试验后总质量(g)_全水分

	private double Jiancxsyhzzl2_mad = 0.0D;// 检查性试验后总质量(g)_水分

	private double Quansf2_mt = 0.0D;// 全水分Mt=m1/m*100(%)_全水分

	private double Quansf2_mad = 0.0D;// 全水分Mt=m1/m*100(%)_水分

	private String Shenh2_mt = null;// 审核_全水分

	private String Shenh2_mad = null;// 审核_水分

	private String Gangh2_vad = null;// 坩埚号

	private String Huimh2_aad = null;// 灰皿号

	private double Gangzl2_vad = 0.0D;// 坩埚质量(g)

	private double Huimzl2_aad = 0.0D;// 灰皿质量(g)

	private double Gangzlsyzl2_vad = 0.0D;// 坩埚质量+试样质量(g)

	private double Huimzlsyzl2_aad = 0.0D;// 灰皿质量+试样质量(g)

	private double Shiyzl2_vad = 0.0D;// 试样质量m(g)

	private double Shiyzl2_aad = 0.0D;// 试样质量m(g)

	private double Jiarhzzl2_vad = 0.0D;// 加热后总质量(g)

	private double Jiarhzzl2_aad = 0.0D;// 加热后总质量(g)

	private double Jianshzl2_vad = 0.0D;// 煤样加热后减少的质量m1(g)

	private double Shiyhzl2_aad = 0.0D;// 检查性试验后总质量(g)

	private double Huiffvad2_vad = 0.0D;// 挥发分Vad=m1/m*100-Mad(%)

	private double Canlwzl2_aad = 0.0D;// 残留物的质量m1(g)

	private double Huiffvdaf2_vad = 0.0D;// 挥发分Vdaf(%)

	private double Huifaad2_aad = 0.0D;// 灰分Aad(%)

	private String Shenh2_vad = null;// 审核_vad

	private String Shenh2_aad = null;// 审核_aad

	private String Qimh2 = null;// 器皿号

	private double Gangzl2 = 0.0D;// 坩埚质量(g)

	private double Qimzl2 = 0.0D;// 器皿质量(g)

	private double Gangzlsyzl2 = 0.0D;// 坩埚质量+试样质量(g)

	private double Qimzlsyzl2 = 0.0D;// 器皿质量+试样质量(g)

	private double Shiyzl2_f = 0.0D;// 试样质量m(g)

	private double Shiyzl2_s = 0.0D;// 试样质量m(g)

	private double Qb2 = 0.0D;// Qb,ad(J/g)

	private double Quanlst2_ad = 0.0D;// 全硫St,ad(%)

	private double Qgr2 = 0.0D;// Qgr,ad(J/g)

	private double Quanlst2_d = 0.0D;// 全硫St,d(%)

	private double Qnent2 = 0.0D;// Qnet,ar(MJ/kg)

	private String Shenh2_s = null;// 审核_s

	private String Shenh2_f = null;// 审核_f

	// -----------------------------------------
	private String Chenglph3_mt = null;// 称量瓶(盘)号_全水分

	private String Chenglph3_mad = null;// 称量瓶(盘)号_水分

	private double Chenglpzl3_mt = 0.0D;// 称量瓶(盘)质量(g)_全水分

	private double Chenglpzl3_mad = 0.0D;// 称量瓶(盘)质量(g)_水分

	private double Chenglpzlys3_mt = 0.0D;// 称量瓶(盘)质量+试样质量(g)_全水分

	private double Chenglpzlys3_mad = 0.0D;// 称量瓶(盘)质量+试样质量(g)_水分

	private double Shiyzl3_mt = 0.0D;// 试样质量m(g)_全水分

	private double Shiyzl3_mad = 0.0D;// 试样质量m(g)_水分

	private double Honghzzl3_mt = 0.0D;// 烘后总质量(g)_全水分

	private double Honghzzl3_mad = 0.0D;// 烘后总质量(g)_水分

	private double Jiancxsyhzzl3_mt = 0.0D;// 检查性试验后总质量(g)_全水分

	private double Jiancxsyhzzl3_mad = 0.0D;// 检查性试验后总质量(g)_水分

	private double Quansf3_mt = 0.0D;// 全水分Mt=m1/m*100(%)_全水分

	private double Quansf3_mad = 0.0D;// 全水分Mt=m1/m*100(%)_水分

	private String Shenh3_mt = null;// 审核_全水分

	private String Shenh3_mad = null;// 审核_水分

	private String Gangh3_vad = null;// 坩埚号

	private String Huimh3_aad = null;// 灰皿号

	private double Gangzl3_vad = 0.0D;// 坩埚质量(g)

	private double Huimzl3_aad = 0.0D;// 灰皿质量(g)

	private double Gangzlsyzl3_vad = 0.0D;// 坩埚质量+试样质量(g)

	private double Huimzlsyzl3_aad = 0.0D;// 灰皿质量+试样质量(g)

	private double Shiyzl3_vad = 0.0D;// 试样质量m(g)

	private double Shiyzl3_aad = 0.0D;// 试样质量m(g)

	private double Jiarhzzl3_vad = 0.0D;// 加热后总质量(g)

	private double Jiarhzzl3_aad = 0.0D;// 加热后总质量(g)

	private double Jianshzl3_vad = 0.0D;// 煤样加热后减少的质量m1(g)

	private double Shiyhzl3_aad = 0.0D;// 检查性试验后总质量(g)

	private double Huiffvad3_vad = 0.0D;// 挥发分Vad=m1/m*100-Mad(%)

	private double Canlwzl3_aad = 0.0D;// 残留物的质量m1(g)

	private double Huiffvdaf3_vad = 0.0D;// 挥发分Vdaf(%)

	private double Huifaad3_aad = 0.0D;// 灰分Aad(%)

	private String Shenh3_vad = null;// 审核_vad

	private String Shenh3_aad = null;// 审核_aad

	private String Qimh3 = null;// 器皿号

	private double Gangzl3 = 0.0D;// 坩埚质量(g)

	private double Qimzl3 = 0.0D;// 器皿质量(g)

	private double Gangzlsyzl3;// 坩埚质量+试样质量(g)

	private double Qimzlsyzl3 = 0.0D;// 器皿质量+试样质量(g)

	private double Shiyzl3_f = 0.0D;// 试样质量m(g)

	private double Shiyzl3_s = 0.0D;// 试样质量m(g)

	private double Qb3 = 0.0D;// Qb,ad(J/g)

	private double Quanlst3_ad = 0.0D;// 全硫St,ad(%)

	private double Qgr3 = 0.0D;// Qgr,ad(J/g)

	private double Quanlst3_d = 0.0D;// 全硫St,d(%)

	private double Qnent3 = 0.0D;// Qnet,ar(MJ/kg)

	private String Shenh3_s = null;// 审核_s

	private String Shenh3_f = null;// 审核_f

	// ------------------------------------------
	private String Chenglph4_mt = null;// 称量瓶(盘)号_全水分

	private String Chenglph4_mad = null;// 称量瓶(盘)号_水分

	private double Chenglpzl4_mt = 0.0D;// 称量瓶(盘)质量(g)_全水分

	private double Chenglpzl4_mad = 0.0D;// 称量瓶(盘)质量(g)_水分

	private double Chenglpzlys4_mt = 0.0D;// 称量瓶(盘)质量+试样质量(g)_全水分

	private double Chenglpzlys4_mad = 0.0D;// 称量瓶(盘)质量+试样质量(g)_水分

	private double Shiyzl4_mt = 0.0D;// 试样质量m(g)_全水分

	private double Shiyzl4_mad = 0.0D;// 试样质量m(g)_水分

	private double Honghzzl4_mt = 0.0D;// 烘后总质量(g)_全水分

	private double Honghzzl4_mad = 0.0D;// 烘后总质量(g)_水分

	private double Jiancxsyhzzl4_mt = 0.0D;// 检查性试验后总质量(g)_全水分

	private double Jiancxsyhzzl4_mad = 0.0D;// 检查性试验后总质量(g)_水分

	private double Quansf4_mt = 0.0D;// 全水分Mt=m1/m*100(%)_全水分

	private double Quansf4_mad = 0.0D;// 全水分Mt=m1/m*100(%)_水分

	private String Shenh4_mt = null;// 审核_全水分

	private String Shenh4_mad = null;// 审核_水分

	private String Gangh4_vad = null;// 坩埚号

	private String Huimh4_aad = null;// 灰皿号

	private double Gangzl4_vad = 0.0D;// 坩埚质量(g)

	private double Huimzl4_aad = 0.0D;// 灰皿质量(g)

	private double Gangzlsyzl4_vad = 0.0D;// 坩埚质量+试样质量(g)

	private double Huimzlsyzl4_aad = 0.0D;// 灰皿质量+试样质量(g)

	private double Shiyzl4_vad = 0.0D;// 试样质量m(g)

	private double Shiyzl4_aad = 0.0D;// 试样质量m(g)

	private double Jiarhzzl4_vad = 0.0D;// 加热后总质量(g)

	private double Jiarhzzl4_aad = 0.0D;// 加热后总质量(g)

	private double Jianshzl4_vad = 0.0D;// 煤样加热后减少的质量m1(g)

	private double Shiyhzl4_aad = 0.0D;// 检查性试验后总质量(g)

	private double Huiffvad4_vad = 0.0D;// 挥发分Vad=m1/m*100-Mad(%)

	private double Canlwzl4_aad = 0.0D;// 残留物的质量m1(g)

	private double Huiffvdaf4_vad = 0.0D;// 挥发分Vdaf(%)

	private double Huifaad4_aad = 0.0D;// 灰分Aad(%)

	private String Shenh4_vad = null;// 审核_vad

	private String Shenh4_aad = null;// 审核_aad

	private String Qimh4 = null;// 器皿号

	private double Gangzl4 = 0.0D;// 坩埚质量(g)

	private double Qimzl4 = 0.0D;// 器皿质量(g)

	private double Gangzlsyzl4 = 0.0D;// 坩埚质量+试样质量(g)

	private double Qimzlsyzl4 = 0.0D;// 器皿质量+试样质量(g)

	private double Shiyzl4_f = 0.0D;// 试样质量m(g)

	private double Shiyzl4_s = 0.0D;// 试样质量m(g)

	private double Qb4 = 0.0D;// Qb,ad(J/g)

	private double Quanlst4_ad = 0.0D;// 全硫St,ad(%)

	private double Qgr4 = 0.0D;// Qgr,ad(J/g)

	private double Quanlst4_d = 0.0D;// 全硫St,d(%)

	private double Qnent4 = 0.0D;// Qnet,ar(MJ/kg)

	private String Shenh4_s = null;// 审核_s

	private String Shenh4_f = null;// 审核_f

	private double Pingjz_mt = 0.0D;// 平均值Mt(%)_全水分

	private double Pingjz_mad = 0.0D;// 平均值Mt(%)_水分

	private double Pingjzvdaf_vad = 0.0D;// 平均值Vdaf(%)

	private double Pingjz_aad = 0.0D;// 平均值(%)

	private double Pingjzst_s = 0.0D;// 平均值St,d(%)

	private double Pingjzqnent_f = 0.0D;// 平均值Qnet,ar(MJ/kg)

	// ---------------------------------------------------

	public long getId_f() {
		return Id_f;
	}

	public void setId_f(long id_f) {
		Id_f = id_f;
	}

	public long getId_sf() {
		return Id_sf;
	}

	public void setId_sf(long id_sf) {
		Id_sf = id_sf;
	}

	public long getId_mt() {
		return Id_mt;
	}

	public void setId_mt(long id_mt) {
		Id_mt = id_mt;
	}

	public long getId_s() {
		return Id_s;
	}

	public void setId_s(long id_s) {
		Id_s = id_s;
	}

	public long getId2_f() {
		return Id2_f;
	}

	public void setId2_f(long id2_f) {
		Id2_f = id2_f;
	}

	public long getId2_sf() {
		return Id2_sf;
	}

	public void setId2_sf(long id2_sf) {
		Id2_sf = id2_sf;
	}

	public long getId2_mt() {
		return Id2_mt;
	}

	public void setId2_mt(long id2_mt) {
		Id2_mt = id2_mt;
	}

	public long getId2_s() {
		return Id2_s;
	}

	public void setId2_s(long id2_s) {
		Id2_s = id2_s;
	}

	public long getId3_f() {
		return Id3_f;
	}

	public void setId3_f(long id3_f) {
		Id3_f = id3_f;
	}

	public long getId3_sf() {
		return Id3_sf;
	}

	public void setId3_sf(long id3_sf) {
		Id3_sf = id3_sf;
	}

	public long getId3_mt() {
		return Id3_mt;
	}

	public void setId3_mt(long id3_mt) {
		Id3_mt = id3_mt;
	}

	public long getId3_s() {
		return Id3_s;
	}

	public void setId3_s(long id3_s) {
		Id3_s = id3_s;
	}

	public long getId4_f() {
		return Id4_f;
	}

	public void setId4_f(long id4_f) {
		Id4_f = id4_f;
	}

	public long getId4_sf() {
		return Id4_sf;
	}

	public void setId4_sf(long id4_sf) {
		Id4_sf = id4_sf;
	}

	public long getId4_mt() {
		return Id4_mt;
	}

	public void setId4_mt(long id4_mt) {
		Id4_mt = id4_mt;
	}

	public long getId4_s() {
		return Id4_s;
	}

	public void setId4_s(long id4_s) {
		Id4_s = id4_s;
	}

	// ----------------------------------------------------
	private long zhillsb_id = -1;

	private long fenxxmb_id1;

	private long fenxxmb_id2;

	private long fenxxmb_id3;

	private long fenxxmb_id4;

	private long fenxxmb_id5;

	private long fenxxmb_id6;

	private Date fenxrq;

	private String huayyqbh = null;

	private double shiqzl = 0.0D;

	private double shengyzl = 0.0D;

	private long shenhzt = 0;

	private String lury = null;

	private double tianjwrz = 0.0D;

	private double tianjwzl = 0.0D;

	private double honghzl2 = 0.0D;

	private double honghzl3 = 0.0D;

	private double honghzl4 = 0.0D;

	private double zuizhhzl = 0.0D;

	public double getZuizhhzl() {
		return zuizhhzl;
	}

	public void setZuizhhzl(double zuizhhzl) {
		this.zuizhhzl = zuizhhzl;
	}

	public double getCanlwzl1_aad() {
		return Canlwzl1_aad;
	}

	public void setCanlwzl1_aad(double canlwzl1_aad) {
		Canlwzl1_aad = canlwzl1_aad;
	}

	public double getCanlwzl2_aad() {
		return Canlwzl2_aad;
	}

	public void setCanlwzl2_aad(double canlwzl2_aad) {
		Canlwzl2_aad = canlwzl2_aad;
	}

	public double getCanlwzl3_aad() {
		return Canlwzl3_aad;
	}

	public void setCanlwzl3_aad(double canlwzl3_aad) {
		Canlwzl3_aad = canlwzl3_aad;
	}

	public double getCanlwzl4_aad() {
		return Canlwzl4_aad;
	}

	public void setCanlwzl4_aad(double canlwzl4_aad) {
		Canlwzl4_aad = canlwzl4_aad;
	}

	public String getChenglph1_mad() {
		return Chenglph1_mad;
	}

	public void setChenglph1_mad(String chenglph1_mad) {
		Chenglph1_mad = chenglph1_mad;
	}

	public String getChenglph1_mt() {
		return Chenglph1_mt;
	}

	public void setChenglph1_mt(String chenglph1_mt) {
		Chenglph1_mt = chenglph1_mt;
	}

	public String getChenglph2_mad() {
		return Chenglph2_mad;
	}

	public void setChenglph2_mad(String chenglph2_mad) {
		Chenglph2_mad = chenglph2_mad;
	}

	public String getChenglph2_mt() {
		return Chenglph2_mt;
	}

	public void setChenglph2_mt(String chenglph2_mt) {
		Chenglph2_mt = chenglph2_mt;
	}

	public String getChenglph3_mad() {
		return Chenglph3_mad;
	}

	public void setChenglph3_mad(String chenglph3_mad) {
		Chenglph3_mad = chenglph3_mad;
	}

	public String getChenglph3_mt() {
		return Chenglph3_mt;
	}

	public void setChenglph3_mt(String chenglph3_mt) {
		Chenglph3_mt = chenglph3_mt;
	}

	public String getChenglph4_mad() {
		return Chenglph4_mad;
	}

	public void setChenglph4_mad(String chenglph4_mad) {
		Chenglph4_mad = chenglph4_mad;
	}

	public String getChenglph4_mt() {
		return Chenglph4_mt;
	}

	public void setChenglph4_mt(String chenglph4_mt) {
		Chenglph4_mt = chenglph4_mt;
	}

	public double getChenglpzl1_mad() {
		return Chenglpzl1_mad;
	}

	public void setChenglpzl1_mad(double chenglpzl1_mad) {
		Chenglpzl1_mad = chenglpzl1_mad;
	}

	public double getChenglpzl1_mt() {
		return Chenglpzl1_mt;
	}

	public void setChenglpzl1_mt(double chenglpzl1_mt) {
		Chenglpzl1_mt = chenglpzl1_mt;
	}

	public double getChenglpzl2_mad() {
		return Chenglpzl2_mad;
	}

	public void setChenglpzl2_mad(double chenglpzl2_mad) {
		Chenglpzl2_mad = chenglpzl2_mad;
	}

	public double getChenglpzl2_mt() {
		return Chenglpzl2_mt;
	}

	public void setChenglpzl2_mt(double chenglpzl2_mt) {
		Chenglpzl2_mt = chenglpzl2_mt;
	}

	public double getChenglpzl3_mad() {
		return Chenglpzl3_mad;
	}

	public void setChenglpzl3_mad(double chenglpzl3_mad) {
		Chenglpzl3_mad = chenglpzl3_mad;
	}

	public double getChenglpzl3_mt() {
		return Chenglpzl3_mt;
	}

	public void setChenglpzl3_mt(double chenglpzl3_mt) {
		Chenglpzl3_mt = chenglpzl3_mt;
	}

	public double getChenglpzl4_mad() {
		return Chenglpzl4_mad;
	}

	public void setChenglpzl4_mad(double chenglpzl4_mad) {
		Chenglpzl4_mad = chenglpzl4_mad;
	}

	public double getChenglpzl4_mt() {
		return Chenglpzl4_mt;
	}

	public void setChenglpzl4_mt(double chenglpzl4_mt) {
		Chenglpzl4_mt = chenglpzl4_mt;
	}

	public double getChenglpzlys1_mad() {
		return Chenglpzlys1_mad;
	}

	public void setChenglpzlys1_mad(double chenglpzlys1_mad) {
		Chenglpzlys1_mad = chenglpzlys1_mad;
	}

	public double getChenglpzlys1_mt() {
		return Chenglpzlys1_mt;
	}

	public void setChenglpzlys1_mt(double chenglpzlys1_mt) {
		Chenglpzlys1_mt = chenglpzlys1_mt;
	}

	public double getChenglpzlys2_mad() {
		return Chenglpzlys2_mad;
	}

	public void setChenglpzlys2_mad(double chenglpzlys2_mad) {
		Chenglpzlys2_mad = chenglpzlys2_mad;
	}

	public double getChenglpzlys2_mt() {
		return Chenglpzlys2_mt;
	}

	public void setChenglpzlys2_mt(double chenglpzlys2_mt) {
		Chenglpzlys2_mt = chenglpzlys2_mt;
	}

	public double getChenglpzlys3_mad() {
		return Chenglpzlys3_mad;
	}

	public void setChenglpzlys3_mad(double chenglpzlys3_mad) {
		Chenglpzlys3_mad = chenglpzlys3_mad;
	}

	public double getChenglpzlys3_mt() {
		return Chenglpzlys3_mt;
	}

	public void setChenglpzlys3_mt(double chenglpzlys3_mt) {
		Chenglpzlys3_mt = chenglpzlys3_mt;
	}

	public double getChenglpzlys4_mad() {
		return Chenglpzlys4_mad;
	}

	public void setChenglpzlys4_mad(double chenglpzlys4_mad) {
		Chenglpzlys4_mad = chenglpzlys4_mad;
	}

	public double getChenglpzlys4_mt() {
		return Chenglpzlys4_mt;
	}

	public void setChenglpzlys4_mt(double chenglpzlys4_mt) {
		Chenglpzlys4_mt = chenglpzlys4_mt;
	}

	public Date getFenxrq() {
		return fenxrq;
	}

	public void setFenxrq(Date fenxrq) {
		this.fenxrq = fenxrq;
	}

	public long getFenxxmb_id1() {
		return fenxxmb_id1;
	}

	public void setFenxxmb_id1(long fenxxmb_id1) {
		this.fenxxmb_id1 = fenxxmb_id1;
	}

	public long getFenxxmb_id2() {
		return fenxxmb_id2;
	}

	public void setFenxxmb_id2(long fenxxmb_id2) {
		this.fenxxmb_id2 = fenxxmb_id2;
	}

	public long getFenxxmb_id3() {
		return fenxxmb_id3;
	}

	public void setFenxxmb_id3(long fenxxmb_id3) {
		this.fenxxmb_id3 = fenxxmb_id3;
	}

	public long getFenxxmb_id4() {
		return fenxxmb_id4;
	}

	public void setFenxxmb_id4(long fenxxmb_id4) {
		this.fenxxmb_id4 = fenxxmb_id4;
	}

	public long getFenxxmb_id5() {
		return fenxxmb_id5;
	}

	public void setFenxxmb_id5(long fenxxmb_id5) {
		this.fenxxmb_id5 = fenxxmb_id5;
	}

	public long getFenxxmb_id6() {
		return fenxxmb_id6;
	}

	public void setFenxxmb_id6(long fenxxmb_id6) {
		this.fenxxmb_id6 = fenxxmb_id6;
	}

	public String getGangh1_vad() {
		return Gangh1_vad;
	}

	public void setGangh1_vad(String gangh1_vad) {
		Gangh1_vad = gangh1_vad;
	}

	public String getGangh2_vad() {
		return Gangh2_vad;
	}

	public void setGangh2_vad(String gangh2_vad) {
		Gangh2_vad = gangh2_vad;
	}

	public String getGangh3_vad() {
		return Gangh3_vad;
	}

	public void setGangh3_vad(String gangh3_vad) {
		Gangh3_vad = gangh3_vad;
	}

	public String getGangh4_vad() {
		return Gangh4_vad;
	}

	public void setGangh4_vad(String gangh4_vad) {
		Gangh4_vad = gangh4_vad;
	}

	public double getGangzl1() {
		return Gangzl1;
	}

	public void setGangzl1(double gangzl1) {
		Gangzl1 = gangzl1;
	}

	public double getGangzl1_vad() {
		return Gangzl1_vad;
	}

	public void setGangzl1_vad(double gangzl1_vad) {
		Gangzl1_vad = gangzl1_vad;
	}

	public double getGangzl2() {
		return Gangzl2;
	}

	public void setGangzl2(double gangzl2) {
		Gangzl2 = gangzl2;
	}

	public double getGangzl2_vad() {
		return Gangzl2_vad;
	}

	public void setGangzl2_vad(double gangzl2_vad) {
		Gangzl2_vad = gangzl2_vad;
	}

	public double getGangzl3() {
		return Gangzl3;
	}

	public void setGangzl3(double gangzl3) {
		Gangzl3 = gangzl3;
	}

	public double getGangzl3_vad() {
		return Gangzl3_vad;
	}

	public void setGangzl3_vad(double gangzl3_vad) {
		Gangzl3_vad = gangzl3_vad;
	}

	public double getGangzl4() {
		return Gangzl4;
	}

	public void setGangzl4(double gangzl4) {
		Gangzl4 = gangzl4;
	}

	public double getGangzl4_vad() {
		return Gangzl4_vad;
	}

	public void setGangzl4_vad(double gangzl4_vad) {
		Gangzl4_vad = gangzl4_vad;
	}

	public double getGangzlsyzl1() {
		return Gangzlsyzl1;
	}

	public void setGangzlsyzl1(double gangzlsyzl1) {
		Gangzlsyzl1 = gangzlsyzl1;
	}

	public double getGangzlsyzl1_vad() {
		return Gangzlsyzl1_vad;
	}

	public void setGangzlsyzl1_vad(double gangzlsyzl1_vad) {
		Gangzlsyzl1_vad = gangzlsyzl1_vad;
	}

	public double getGangzlsyzl2() {
		return Gangzlsyzl2;
	}

	public void setGangzlsyzl2(double gangzlsyzl2) {
		Gangzlsyzl2 = gangzlsyzl2;
	}

	public double getGangzlsyzl2_vad() {
		return Gangzlsyzl2_vad;
	}

	public void setGangzlsyzl2_vad(double gangzlsyzl2_vad) {
		Gangzlsyzl2_vad = gangzlsyzl2_vad;
	}

	public double getGangzlsyzl3() {
		return Gangzlsyzl3;
	}

	public void setGangzlsyzl3(double gangzlsyzl3) {
		Gangzlsyzl3 = gangzlsyzl3;
	}

	public double getGangzlsyzl3_vad() {
		return Gangzlsyzl3_vad;
	}

	public void setGangzlsyzl3_vad(double gangzlsyzl3_vad) {
		Gangzlsyzl3_vad = gangzlsyzl3_vad;
	}

	public double getGangzlsyzl4() {
		return Gangzlsyzl4;
	}

	public void setGangzlsyzl4(double gangzlsyzl4) {
		Gangzlsyzl4 = gangzlsyzl4;
	}

	public double getGangzlsyzl4_vad() {
		return Gangzlsyzl4_vad;
	}

	public void setGangzlsyzl4_vad(double gangzlsyzl4_vad) {
		Gangzlsyzl4_vad = gangzlsyzl4_vad;
	}

	public double getHonghzzl1_mad() {
		return Honghzzl1_mad;
	}

	public void setHonghzzl1_mad(double honghzzl1_mad) {
		Honghzzl1_mad = honghzzl1_mad;
	}

	public double getHonghzzl1_mt() {
		return Honghzzl1_mt;
	}

	public void setHonghzzl1_mt(double honghzzl1_mt) {
		Honghzzl1_mt = honghzzl1_mt;
	}

	public double getHonghzzl2_mad() {
		return Honghzzl2_mad;
	}

	public void setHonghzzl2_mad(double honghzzl2_mad) {
		Honghzzl2_mad = honghzzl2_mad;
	}

	public double getHonghzzl2_mt() {
		return Honghzzl2_mt;
	}

	public void setHonghzzl2_mt(double honghzzl2_mt) {
		Honghzzl2_mt = honghzzl2_mt;
	}

	public double getHonghzzl3_mad() {
		return Honghzzl3_mad;
	}

	public void setHonghzzl3_mad(double honghzzl3_mad) {
		Honghzzl3_mad = honghzzl3_mad;
	}

	public double getHonghzzl3_mt() {
		return Honghzzl3_mt;
	}

	public void setHonghzzl3_mt(double honghzzl3_mt) {
		Honghzzl3_mt = honghzzl3_mt;
	}

	public double getHonghzzl4_mad() {
		return Honghzzl4_mad;
	}

	public void setHonghzzl4_mad(double honghzzl4_mad) {
		Honghzzl4_mad = honghzzl4_mad;
	}

	public double getHonghzzl4_mt() {
		return Honghzzl4_mt;
	}

	public void setHonghzzl4_mt(double honghzzl4_mt) {
		Honghzzl4_mt = honghzzl4_mt;
	}

	public String getHuayyqbh() {
		return huayyqbh;
	}

	public void setHuayyqbh(String huayyqbh) {
		this.huayyqbh = huayyqbh;
	}

	public double getHuifaad1_aad() {
		return Huifaad1_aad;
	}

	public void setHuifaad1_aad(double huifaad1_aad) {
		Huifaad1_aad = huifaad1_aad;
	}

	public double getHuifaad2_aad() {
		return Huifaad2_aad;
	}

	public void setHuifaad2_aad(double huifaad2_aad) {
		Huifaad2_aad = huifaad2_aad;
	}

	public double getHuifaad3_aad() {
		return Huifaad3_aad;
	}

	public void setHuifaad3_aad(double huifaad3_aad) {
		Huifaad3_aad = huifaad3_aad;
	}

	public double getHuifaad4_aad() {
		return Huifaad4_aad;
	}

	public void setHuifaad4_aad(double huifaad4_aad) {
		Huifaad4_aad = huifaad4_aad;
	}

	public double getHuiffvad1_vad() {
		return Huiffvad1_vad;
	}

	public void setHuiffvad1_vad(double huiffvad1_vad) {
		Huiffvad1_vad = huiffvad1_vad;
	}

	public double getHuiffvad2_vad() {
		return Huiffvad2_vad;
	}

	public void setHuiffvad2_vad(double huiffvad2_vad) {
		Huiffvad2_vad = huiffvad2_vad;
	}

	public double getHuiffvad3_vad() {
		return Huiffvad3_vad;
	}

	public void setHuiffvad3_vad(double huiffvad3_vad) {
		Huiffvad3_vad = huiffvad3_vad;
	}

	public double getHuiffvad4_vad() {
		return Huiffvad4_vad;
	}

	public void setHuiffvad4_vad(double huiffvad4_vad) {
		Huiffvad4_vad = huiffvad4_vad;
	}

	public double getHuiffvdaf1_vad() {
		return Huiffvdaf1_vad;
	}

	public void setHuiffvdaf1_vad(double huiffvdaf1_vad) {
		Huiffvdaf1_vad = huiffvdaf1_vad;
	}

	public double getHuiffvdaf2_vad() {
		return Huiffvdaf2_vad;
	}

	public void setHuiffvdaf2_vad(double huiffvdaf2_vad) {
		Huiffvdaf2_vad = huiffvdaf2_vad;
	}

	public double getHuiffvdaf3_vad() {
		return Huiffvdaf3_vad;
	}

	public void setHuiffvdaf3_vad(double huiffvdaf3_vad) {
		Huiffvdaf3_vad = huiffvdaf3_vad;
	}

	public double getHuiffvdaf4_vad() {
		return Huiffvdaf4_vad;
	}

	public void setHuiffvdaf4_vad(double huiffvdaf4_vad) {
		Huiffvdaf4_vad = huiffvdaf4_vad;
	}

	public String getHuimh1_aad() {
		return Huimh1_aad;
	}

	public void setHuimh1_aad(String huimh1_aad) {
		Huimh1_aad = huimh1_aad;
	}

	public String getHuimh2_aad() {
		return Huimh2_aad;
	}

	public void setHuimh2_aad(String huimh2_aad) {
		Huimh2_aad = huimh2_aad;
	}

	public String getHuimh3_aad() {
		return Huimh3_aad;
	}

	public void setHuimh3_aad(String huimh3_aad) {
		Huimh3_aad = huimh3_aad;
	}

	public String getHuimh4_aad() {
		return Huimh4_aad;
	}

	public void setHuimh4_aad(String huimh4_aad) {
		Huimh4_aad = huimh4_aad;
	}

	public double getHuimzl1_aad() {
		return Huimzl1_aad;
	}

	public void setHuimzl1_aad(double huimzl1_aad) {
		Huimzl1_aad = huimzl1_aad;
	}

	public double getHuimzl2_aad() {
		return Huimzl2_aad;
	}

	public void setHuimzl2_aad(double huimzl2_aad) {
		Huimzl2_aad = huimzl2_aad;
	}

	public double getHuimzl3_aad() {
		return Huimzl3_aad;
	}

	public void setHuimzl3_aad(double huimzl3_aad) {
		Huimzl3_aad = huimzl3_aad;
	}

	public double getHuimzl4_aad() {
		return Huimzl4_aad;
	}

	public void setHuimzl4_aad(double huimzl4_aad) {
		Huimzl4_aad = huimzl4_aad;
	}

	public double getHuimzlsyzl1_aad() {
		return Huimzlsyzl1_aad;
	}

	public void setHuimzlsyzl1_aad(double huimzlsyzl1_aad) {
		Huimzlsyzl1_aad = huimzlsyzl1_aad;
	}

	public double getHuimzlsyzl2_aad() {
		return Huimzlsyzl2_aad;
	}

	public void setHuimzlsyzl2_aad(double huimzlsyzl2_aad) {
		Huimzlsyzl2_aad = huimzlsyzl2_aad;
	}

	public double getHuimzlsyzl3_aad() {
		return Huimzlsyzl3_aad;
	}

	public void setHuimzlsyzl3_aad(double huimzlsyzl3_aad) {
		Huimzlsyzl3_aad = huimzlsyzl3_aad;
	}

	public double getHuimzlsyzl4_aad() {
		return Huimzlsyzl4_aad;
	}

	public void setHuimzlsyzl4_aad(double huimzlsyzl4_aad) {
		Huimzlsyzl4_aad = huimzlsyzl4_aad;
	}

	public double getJiancxsyhzzl1_mad() {
		return Jiancxsyhzzl1_mad;
	}

	public void setJiancxsyhzzl1_mad(double jiancxsyhzzl1_mad) {
		Jiancxsyhzzl1_mad = jiancxsyhzzl1_mad;
	}

	public double getJiancxsyhzzl1_mt() {
		return Jiancxsyhzzl1_mt;
	}

	public void setJiancxsyhzzl1_mt(double jiancxsyhzzl1_mt) {
		Jiancxsyhzzl1_mt = jiancxsyhzzl1_mt;
	}

	public double getJiancxsyhzzl2_mad() {
		return Jiancxsyhzzl2_mad;
	}

	public void setJiancxsyhzzl2_mad(double jiancxsyhzzl2_mad) {
		Jiancxsyhzzl2_mad = jiancxsyhzzl2_mad;
	}

	public double getJiancxsyhzzl2_mt() {
		return Jiancxsyhzzl2_mt;
	}

	public void setJiancxsyhzzl2_mt(double jiancxsyhzzl2_mt) {
		Jiancxsyhzzl2_mt = jiancxsyhzzl2_mt;
	}

	public double getJiancxsyhzzl3_mad() {
		return Jiancxsyhzzl3_mad;
	}

	public void setJiancxsyhzzl3_mad(double jiancxsyhzzl3_mad) {
		Jiancxsyhzzl3_mad = jiancxsyhzzl3_mad;
	}

	public double getJiancxsyhzzl3_mt() {
		return Jiancxsyhzzl3_mt;
	}

	public void setJiancxsyhzzl3_mt(double jiancxsyhzzl3_mt) {
		Jiancxsyhzzl3_mt = jiancxsyhzzl3_mt;
	}

	public double getJiancxsyhzzl4_mad() {
		return Jiancxsyhzzl4_mad;
	}

	public void setJiancxsyhzzl4_mad(double jiancxsyhzzl4_mad) {
		Jiancxsyhzzl4_mad = jiancxsyhzzl4_mad;
	}

	public double getJiancxsyhzzl4_mt() {
		return Jiancxsyhzzl4_mt;
	}

	public void setJiancxsyhzzl4_mt(double jiancxsyhzzl4_mt) {
		Jiancxsyhzzl4_mt = jiancxsyhzzl4_mt;
	}

	public double getJianshzl1_vad() {
		return Jianshzl1_vad;
	}

	public void setJianshzl1_vad(double jianshzl1_vad) {
		Jianshzl1_vad = jianshzl1_vad;
	}

	public double getJianshzl2_vad() {
		return Jianshzl2_vad;
	}

	public void setJianshzl2_vad(double jianshzl2_vad) {
		Jianshzl2_vad = jianshzl2_vad;
	}

	public double getJianshzl3_vad() {
		return Jianshzl3_vad;
	}

	public void setJianshzl3_vad(double jianshzl3_vad) {
		Jianshzl3_vad = jianshzl3_vad;
	}

	public double getJianshzl4_vad() {
		return Jianshzl4_vad;
	}

	public void setJianshzl4_vad(double jianshzl4_vad) {
		Jianshzl4_vad = jianshzl4_vad;
	}

	public double getJiarhzzl1_aad() {
		return Jiarhzzl1_aad;
	}

	public void setJiarhzzl1_aad(double jiarhzzl1_aad) {
		Jiarhzzl1_aad = jiarhzzl1_aad;
	}

	public double getJiarhzzl1_vad() {
		return Jiarhzzl1_vad;
	}

	public void setJiarhzzl1_vad(double jiarhzzl1_vad) {
		Jiarhzzl1_vad = jiarhzzl1_vad;
	}

	public double getJiarhzzl2_aad() {
		return Jiarhzzl2_aad;
	}

	public void setJiarhzzl2_aad(double jiarhzzl2_aad) {
		Jiarhzzl2_aad = jiarhzzl2_aad;
	}

	public double getJiarhzzl2_vad() {
		return Jiarhzzl2_vad;
	}

	public void setJiarhzzl2_vad(double jiarhzzl2_vad) {
		Jiarhzzl2_vad = jiarhzzl2_vad;
	}

	public double getJiarhzzl3_aad() {
		return Jiarhzzl3_aad;
	}

	public void setJiarhzzl3_aad(double jiarhzzl3_aad) {
		Jiarhzzl3_aad = jiarhzzl3_aad;
	}

	public double getJiarhzzl3_vad() {
		return Jiarhzzl3_vad;
	}

	public void setJiarhzzl3_vad(double jiarhzzl3_vad) {
		Jiarhzzl3_vad = jiarhzzl3_vad;
	}

	public double getJiarhzzl4_aad() {
		return Jiarhzzl4_aad;
	}

	public void setJiarhzzl4_aad(double jiarhzzl4_aad) {
		Jiarhzzl4_aad = jiarhzzl4_aad;
	}

	public double getJiarhzzl4_vad() {
		return Jiarhzzl4_vad;
	}

	public void setJiarhzzl4_vad(double jiarhzzl4_vad) {
		Jiarhzzl4_vad = jiarhzzl4_vad;
	}

	public String getLury() {
		return lury;
	}

	public void setLury(String lury) {
		this.lury = lury;
	}

	public double getPingjz_aad() {
		return Pingjz_aad;
	}

	public void setPingjz_aad(double pingjz_aad) {
		Pingjz_aad = pingjz_aad;
	}

	public double getPingjz_mad() {
		return Pingjz_mad;
	}

	public void setPingjz_mad(double pingjz_mad) {
		Pingjz_mad = pingjz_mad;
	}

	public double getPingjz_mt() {
		return Pingjz_mt;
	}

	public void setPingjz_mt(double pingjz_mt) {
		Pingjz_mt = pingjz_mt;
	}

	public double getPingjzqnent_f() {
		return Pingjzqnent_f;
	}

	public void setPingjzqnent_f(double pingjzqnent_f) {
		Pingjzqnent_f = pingjzqnent_f;
	}

	public double getPingjzst_s() {
		return Pingjzst_s;
	}

	public void setPingjzst_s(double pingjzst_s) {
		Pingjzst_s = pingjzst_s;
	}

	public double getPingjzvdaf_vad() {
		return Pingjzvdaf_vad;
	}

	public void setPingjzvdaf_vad(double pingjzvdaf_vad) {
		Pingjzvdaf_vad = pingjzvdaf_vad;
	}

	public double getQb1() {
		return Qb1;
	}

	public void setQb1(double qb1) {
		Qb1 = qb1;
	}

	public double getQb2() {
		return Qb2;
	}

	public void setQb2(double qb2) {
		Qb2 = qb2;
	}

	public double getQb3() {
		return Qb3;
	}

	public void setQb3(double qb3) {
		Qb3 = qb3;
	}

	public double getQb4() {
		return Qb4;
	}

	public void setQb4(double qb4) {
		Qb4 = qb4;
	}

	public double getQgr1() {
		return Qgr1;
	}

	public void setQgr1(double qgr1) {
		Qgr1 = qgr1;
	}

	public double getQgr2() {
		return Qgr2;
	}

	public void setQgr2(double qgr2) {
		Qgr2 = qgr2;
	}

	public double getQgr3() {
		return Qgr3;
	}

	public void setQgr3(double qgr3) {
		Qgr3 = qgr3;
	}

	public double getQgr4() {
		return Qgr4;
	}

	public void setQgr4(double qgr4) {
		Qgr4 = qgr4;
	}

	public String getQimh1() {
		return Qimh1;
	}

	public void setQimh1(String qimh1) {
		Qimh1 = qimh1;
	}

	public String getQimh2() {
		return Qimh2;
	}

	public void setQimh2(String qimh2) {
		Qimh2 = qimh2;
	}

	public String getQimh3() {
		return Qimh3;
	}

	public void setQimh3(String qimh3) {
		Qimh3 = qimh3;
	}

	public String getQimh4() {
		return Qimh4;
	}

	public void setQimh4(String qimh4) {
		Qimh4 = qimh4;
	}

	public double getQimzl1() {
		return Qimzl1;
	}

	public void setQimzl1(double qimzl1) {
		Qimzl1 = qimzl1;
	}

	public double getQimzl2() {
		return Qimzl2;
	}

	public void setQimzl2(double qimzl2) {
		Qimzl2 = qimzl2;
	}

	public double getQimzl3() {
		return Qimzl3;
	}

	public void setQimzl3(double qimzl3) {
		Qimzl3 = qimzl3;
	}

	public double getQimzl4() {
		return Qimzl4;
	}

	public void setQimzl4(double qimzl4) {
		Qimzl4 = qimzl4;
	}

	public double getQimzlsyzl1() {
		return Qimzlsyzl1;
	}

	public void setQimzlsyzl1(double qimzlsyzl1) {
		Qimzlsyzl1 = qimzlsyzl1;
	}

	public double getQimzlsyzl2() {
		return Qimzlsyzl2;
	}

	public void setQimzlsyzl2(double qimzlsyzl2) {
		Qimzlsyzl2 = qimzlsyzl2;
	}

	public double getQimzlsyzl3() {
		return Qimzlsyzl3;
	}

	public void setQimzlsyzl3(double qimzlsyzl3) {
		Qimzlsyzl3 = qimzlsyzl3;
	}

	public double getQimzlsyzl4() {
		return Qimzlsyzl4;
	}

	public void setQimzlsyzl4(double qimzlsyzl4) {
		Qimzlsyzl4 = qimzlsyzl4;
	}

	public double getQnent1() {
		return Qnent1;
	}

	public void setQnent1(double qnent1) {
		Qnent1 = qnent1;
	}

	public double getQnent2() {
		return Qnent2;
	}

	public void setQnent2(double qnent2) {
		Qnent2 = qnent2;
	}

	public double getQnent3() {
		return Qnent3;
	}

	public void setQnent3(double qnent3) {
		Qnent3 = qnent3;
	}

	public double getQnent4() {
		return Qnent4;
	}

	public void setQnent4(double qnent4) {
		Qnent4 = qnent4;
	}

	public double getQuanlst1_ad() {
		return Quanlst1_ad;
	}

	public void setQuanlst1_ad(double quanlst1_ad) {
		Quanlst1_ad = quanlst1_ad;
	}

	public double getQuanlst1_d() {
		return Quanlst1_d;
	}

	public void setQuanlst1_d(double quanlst1_d) {
		Quanlst1_d = quanlst1_d;
	}

	public double getQuanlst2_ad() {
		return Quanlst2_ad;
	}

	public void setQuanlst2_ad(double quanlst2_ad) {
		Quanlst2_ad = quanlst2_ad;
	}

	public double getQuanlst2_d() {
		return Quanlst2_d;
	}

	public void setQuanlst2_d(double quanlst2_d) {
		Quanlst2_d = quanlst2_d;
	}

	public double getQuanlst3_ad() {
		return Quanlst3_ad;
	}

	public void setQuanlst3_ad(double quanlst3_ad) {
		Quanlst3_ad = quanlst3_ad;
	}

	public double getQuanlst3_d() {
		return Quanlst3_d;
	}

	public void setQuanlst3_d(double quanlst3_d) {
		Quanlst3_d = quanlst3_d;
	}

	public double getQuanlst4_ad() {
		return Quanlst4_ad;
	}

	public void setQuanlst4_ad(double quanlst4_ad) {
		Quanlst4_ad = quanlst4_ad;
	}

	public double getQuanlst4_d() {
		return Quanlst4_d;
	}

	public void setQuanlst4_d(double quanlst4_d) {
		Quanlst4_d = quanlst4_d;
	}

	public double getQuansf1_mad() {
		return Quansf1_mad;
	}

	public void setQuansf1_mad(double quansf1_mad) {
		Quansf1_mad = quansf1_mad;
	}

	public double getQuansf1_mt() {
		return Quansf1_mt;
	}

	public void setQuansf1_mt(double quansf1_mt) {
		Quansf1_mt = quansf1_mt;
	}

	public double getQuansf2_mad() {
		return Quansf2_mad;
	}

	public void setQuansf2_mad(double quansf2_mad) {
		Quansf2_mad = quansf2_mad;
	}

	public double getQuansf2_mt() {
		return Quansf2_mt;
	}

	public void setQuansf2_mt(double quansf2_mt) {
		Quansf2_mt = quansf2_mt;
	}

	public double getQuansf3_mad() {
		return Quansf3_mad;
	}

	public void setQuansf3_mad(double quansf3_mad) {
		Quansf3_mad = quansf3_mad;
	}

	public double getQuansf3_mt() {
		return Quansf3_mt;
	}

	public void setQuansf3_mt(double quansf3_mt) {
		Quansf3_mt = quansf3_mt;
	}

	public double getQuansf4_mad() {
		return Quansf4_mad;
	}

	public void setQuansf4_mad(double quansf4_mad) {
		Quansf4_mad = quansf4_mad;
	}

	public double getQuansf4_mt() {
		return Quansf4_mt;
	}

	public void setQuansf4_mt(double quansf4_mt) {
		Quansf4_mt = quansf4_mt;
	}

	public double getShengyzl() {
		return shengyzl;
	}

	public void setShengyzl(double shengyzl) {
		this.shengyzl = shengyzl;
	}

	public String getShenh1_aad() {
		return Shenh1_aad;
	}

	public void setShenh1_aad(String shenh1_aad) {
		Shenh1_aad = shenh1_aad;
	}

	public String getShenh1_f() {
		return Shenh1_f;
	}

	public void setShenh1_f(String shenh1_f) {
		Shenh1_f = shenh1_f;
	}

	public String getShenh1_mad() {
		return Shenh1_mad;
	}

	public void setShenh1_mad(String shenh1_mad) {
		Shenh1_mad = shenh1_mad;
	}

	public String getShenh1_mt() {
		return Shenh1_mt;
	}

	public void setShenh1_mt(String shenh1_mt) {
		Shenh1_mt = shenh1_mt;
	}

	public String getShenh1_s() {
		return Shenh1_s;
	}

	public void setShenh1_s(String shenh1_s) {
		Shenh1_s = shenh1_s;
	}

	public String getShenh1_vad() {
		return Shenh1_vad;
	}

	public void setShenh1_vad(String shenh1_vad) {
		Shenh1_vad = shenh1_vad;
	}

	public String getShenh2_aad() {
		return Shenh2_aad;
	}

	public void setShenh2_aad(String shenh2_aad) {
		Shenh2_aad = shenh2_aad;
	}

	public String getShenh2_f() {
		return Shenh2_f;
	}

	public void setShenh2_f(String shenh2_f) {
		Shenh2_f = shenh2_f;
	}

	public String getShenh2_mad() {
		return Shenh2_mad;
	}

	public void setShenh2_mad(String shenh2_mad) {
		Shenh2_mad = shenh2_mad;
	}

	public String getShenh2_mt() {
		return Shenh2_mt;
	}

	public void setShenh2_mt(String shenh2_mt) {
		Shenh2_mt = shenh2_mt;
	}

	public String getShenh2_s() {
		return Shenh2_s;
	}

	public void setShenh2_s(String shenh2_s) {
		Shenh2_s = shenh2_s;
	}

	public String getShenh2_vad() {
		return Shenh2_vad;
	}

	public void setShenh2_vad(String shenh2_vad) {
		Shenh2_vad = shenh2_vad;
	}

	public String getShenh3_aad() {
		return Shenh3_aad;
	}

	public void setShenh3_aad(String shenh3_aad) {
		Shenh3_aad = shenh3_aad;
	}

	public String getShenh3_f() {
		return Shenh3_f;
	}

	public void setShenh3_f(String shenh3_f) {
		Shenh3_f = shenh3_f;
	}

	public String getShenh3_mad() {
		return Shenh3_mad;
	}

	public void setShenh3_mad(String shenh3_mad) {
		Shenh3_mad = shenh3_mad;
	}

	public String getShenh3_mt() {
		return Shenh3_mt;
	}

	public void setShenh3_mt(String shenh3_mt) {
		Shenh3_mt = shenh3_mt;
	}

	public String getShenh3_s() {
		return Shenh3_s;
	}

	public void setShenh3_s(String shenh3_s) {
		Shenh3_s = shenh3_s;
	}

	public String getShenh3_vad() {
		return Shenh3_vad;
	}

	public void setShenh3_vad(String shenh3_vad) {
		Shenh3_vad = shenh3_vad;
	}

	public String getShenh4_aad() {
		return Shenh4_aad;
	}

	public void setShenh4_aad(String shenh4_aad) {
		Shenh4_aad = shenh4_aad;
	}

	public String getShenh4_f() {
		return Shenh4_f;
	}

	public void setShenh4_f(String shenh4_f) {
		Shenh4_f = shenh4_f;
	}

	public String getShenh4_mad() {
		return Shenh4_mad;
	}

	public void setShenh4_mad(String shenh4_mad) {
		Shenh4_mad = shenh4_mad;
	}

	public String getShenh4_mt() {
		return Shenh4_mt;
	}

	public void setShenh4_mt(String shenh4_mt) {
		Shenh4_mt = shenh4_mt;
	}

	public String getShenh4_s() {
		return Shenh4_s;
	}

	public void setShenh4_s(String shenh4_s) {
		Shenh4_s = shenh4_s;
	}

	public String getShenh4_vad() {
		return Shenh4_vad;
	}

	public void setShenh4_vad(String shenh4_vad) {
		Shenh4_vad = shenh4_vad;
	}

	public long getShenhzt() {
		return shenhzt;
	}

	public void setShenhzt(long shenhzt) {
		this.shenhzt = shenhzt;
	}

	public double getShiqzl() {
		return shiqzl;
	}

	public void setShiqzl(double shiqzl) {
		this.shiqzl = shiqzl;
	}

	public double getShiyhzl1_aad() {
		return Shiyhzl1_aad;
	}

	public void setShiyhzl1_aad(double shiyhzl1_aad) {
		Shiyhzl1_aad = shiyhzl1_aad;
	}

	public double getShiyhzl2_aad() {
		return Shiyhzl2_aad;
	}

	public void setShiyhzl2_aad(double shiyhzl2_aad) {
		Shiyhzl2_aad = shiyhzl2_aad;
	}

	public double getShiyhzl3_aad() {
		return Shiyhzl3_aad;
	}

	public void setShiyhzl3_aad(double shiyhzl3_aad) {
		Shiyhzl3_aad = shiyhzl3_aad;
	}

	public double getShiyhzl4_aad() {
		return Shiyhzl4_aad;
	}

	public void setShiyhzl4_aad(double shiyhzl4_aad) {
		Shiyhzl4_aad = shiyhzl4_aad;
	}

	public double getShiyzl1_aad() {
		return Shiyzl1_aad;
	}

	public void setShiyzl1_aad(double shiyzl1_aad) {
		Shiyzl1_aad = shiyzl1_aad;
	}

	public double getShiyzl1_f() {
		return Shiyzl1_f;
	}

	public void setShiyzl1_f(double shiyzl1_f) {
		Shiyzl1_f = shiyzl1_f;
	}

	public double getShiyzl1_mad() {
		return Shiyzl1_mad;
	}

	public void setShiyzl1_mad(double shiyzl1_mad) {
		Shiyzl1_mad = shiyzl1_mad;
	}

	public double getShiyzl1_mt() {
		return Shiyzl1_mt;
	}

	public void setShiyzl1_mt(double shiyzl1_mt) {
		Shiyzl1_mt = shiyzl1_mt;
	}

	public double getShiyzl1_s() {
		return Shiyzl1_s;
	}

	public void setShiyzl1_s(double shiyzl1_s) {
		Shiyzl1_s = shiyzl1_s;
	}

	public double getShiyzl1_vad() {
		return Shiyzl1_vad;
	}

	public void setShiyzl1_vad(double shiyzl1_vad) {
		Shiyzl1_vad = shiyzl1_vad;
	}

	public double getShiyzl2_aad() {
		return Shiyzl2_aad;
	}

	public void setShiyzl2_aad(double shiyzl2_aad) {
		Shiyzl2_aad = shiyzl2_aad;
	}

	public double getShiyzl2_f() {
		return Shiyzl2_f;
	}

	public void setShiyzl2_f(double shiyzl2_f) {
		Shiyzl2_f = shiyzl2_f;
	}

	public double getShiyzl2_mad() {
		return Shiyzl2_mad;
	}

	public void setShiyzl2_mad(double shiyzl2_mad) {
		Shiyzl2_mad = shiyzl2_mad;
	}

	public double getShiyzl2_mt() {
		return Shiyzl2_mt;
	}

	public void setShiyzl2_mt(double shiyzl2_mt) {
		Shiyzl2_mt = shiyzl2_mt;
	}

	public double getShiyzl2_s() {
		return Shiyzl2_s;
	}

	public void setShiyzl2_s(double shiyzl2_s) {
		Shiyzl2_s = shiyzl2_s;
	}

	public double getShiyzl2_vad() {
		return Shiyzl2_vad;
	}

	public void setShiyzl2_vad(double shiyzl2_vad) {
		Shiyzl2_vad = shiyzl2_vad;
	}

	public double getShiyzl3_aad() {
		return Shiyzl3_aad;
	}

	public void setShiyzl3_aad(double shiyzl3_aad) {
		Shiyzl3_aad = shiyzl3_aad;
	}

	public double getShiyzl3_f() {
		return Shiyzl3_f;
	}

	public void setShiyzl3_f(double shiyzl3_f) {
		Shiyzl3_f = shiyzl3_f;
	}

	public double getShiyzl3_mad() {
		return Shiyzl3_mad;
	}

	public void setShiyzl3_mad(double shiyzl3_mad) {
		Shiyzl3_mad = shiyzl3_mad;
	}

	public double getShiyzl3_mt() {
		return Shiyzl3_mt;
	}

	public void setShiyzl3_mt(double shiyzl3_mt) {
		Shiyzl3_mt = shiyzl3_mt;
	}

	public double getShiyzl3_s() {
		return Shiyzl3_s;
	}

	public void setShiyzl3_s(double shiyzl3_s) {
		Shiyzl3_s = shiyzl3_s;
	}

	public double getShiyzl3_vad() {
		return Shiyzl3_vad;
	}

	public void setShiyzl3_vad(double shiyzl3_vad) {
		Shiyzl3_vad = shiyzl3_vad;
	}

	public double getShiyzl4_aad() {
		return Shiyzl4_aad;
	}

	public void setShiyzl4_aad(double shiyzl4_aad) {
		Shiyzl4_aad = shiyzl4_aad;
	}

	public double getShiyzl4_f() {
		return Shiyzl4_f;
	}

	public void setShiyzl4_f(double shiyzl4_f) {
		Shiyzl4_f = shiyzl4_f;
	}

	public double getShiyzl4_mad() {
		return Shiyzl4_mad;
	}

	public void setShiyzl4_mad(double shiyzl4_mad) {
		Shiyzl4_mad = shiyzl4_mad;
	}

	public double getShiyzl4_mt() {
		return Shiyzl4_mt;
	}

	public void setShiyzl4_mt(double shiyzl4_mt) {
		Shiyzl4_mt = shiyzl4_mt;
	}

	public double getShiyzl4_s() {
		return Shiyzl4_s;
	}

	public void setShiyzl4_s(double shiyzl4_s) {
		Shiyzl4_s = shiyzl4_s;
	}

	public double getShiyzl4_vad() {
		return Shiyzl4_vad;
	}

	public void setShiyzl4_vad(double shiyzl4_vad) {
		Shiyzl4_vad = shiyzl4_vad;
	}

	public double getTianjwrz() {
		return tianjwrz;
	}

	public void setTianjwrz(double tianjwrz) {
		this.tianjwrz = tianjwrz;
	}

	public double getTianjwzl() {
		return tianjwzl;
	}

	public void setTianjwzl(double tianjwzl) {
		this.tianjwzl = tianjwzl;
	}

	public long getZhillsb_id() {
		return zhillsb_id;
	}

	public void setZhillsb_id(long zhillsb_id) {
		this.zhillsb_id = zhillsb_id;
	}

	public double getHonghzl2() {
		return honghzl2;
	}

	public void setHonghzl2(double honghzl2) {
		this.honghzl2 = honghzl2;
	}

	public double getHonghzl3() {
		return honghzl3;
	}

	public void setHonghzl3(double honghzl3) {
		this.honghzl3 = honghzl3;
	}

	public double getHonghzl4() {
		return honghzl4;
	}

	public void setHonghzl4(double honghzl4) {
		this.honghzl4 = honghzl4;
	}

	public long getId_hf() {
		return Id_hf;
	}

	public void setId_hf(long id_hf) {
		Id_hf = id_hf;
	}

	public long getId_huif() {
		return Id_huif;
	}

	public void setId_huif(long id_huif) {
		Id_huif = id_huif;
	}

	public long getId2_hf() {
		return Id2_hf;
	}

	public void setId2_hf(long id2_hf) {
		Id2_hf = id2_hf;
	}

	public long getId2_huif() {
		return Id2_huif;
	}

	public void setId2_huif(long id2_huif) {
		Id2_huif = id2_huif;
	}

	public long getId3_hf() {
		return Id3_hf;
	}

	public void setId3_hf(long id3_hf) {
		Id3_hf = id3_hf;
	}

	public long getId3_huif() {
		return Id3_huif;
	}

	public void setId3_huif(long id3_huif) {
		Id3_huif = id3_huif;
	}

	public long getId4_hf() {
		return Id4_hf;
	}

	public void setId4_hf(long id4_hf) {
		Id4_hf = id4_hf;
	}

	public long getId4_huif() {
		return Id4_huif;
	}

	public void setId4_huif(long id4_huif) {
		Id4_huif = id4_huif;
	}


}
