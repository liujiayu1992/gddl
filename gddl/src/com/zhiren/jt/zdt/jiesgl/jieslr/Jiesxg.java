package com.zhiren.jt.zdt.jiesgl.jieslr;

import org.apache.tapestry.html.BasePage;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;

import com.zhiren.main.Visit;
import org.apache.tapestry.form.IPropertySelectionModel;
import com.zhiren.common.Locale;
import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.Money;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;

 
public class Jiesxg extends BasePage {

	private static int _editTableRow = -1;//编辑框中选中的行

	public int getEditTableRow() {
		return _editTableRow;
	}

	public void setEditTableRow(int _value) {
		_editTableRow = _value;
	}
	private String _msg;

	
	public void setMsg(String _value) {
		_msg = _value;
	}

	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}
	public String getDXMoney(double _Money ){
		Money money=new Money();
		return money.NumToRMBStr(_Money);
	}
	private String mjiesbh = "";
	private static Date _JiesrqValue = new Date();
	private boolean _Jiesrqchange = false;
	public Date getJiesrq() {
		return _JiesrqValue;
	}

	public void setJiesrq(Date _value) {
		if (FormatDate(_JiesrqValue).equals(FormatDate(_value))) {
			_Jiesrqchange = false;
		} else {
			_JiesrqValue = _value;
			_Jiesrqchange = true;
		}
	}
	private static Date _ChengyrqValue = new Date();
	private boolean _Chengyrqchange = false;
	public Date getChengyrq() {
		return _ChengyrqValue;
	}

	public void setChengyrq(Date _value) {
		if (FormatDate(_ChengyrqValue).equals(FormatDate(_value))) {
			_Chengyrqchange = false;
		} else {
			_ChengyrqValue = _value;
			_Chengyrqchange = true;
		}
	}
	private static Date _DaocrqValue = new Date();
	private boolean _Daocrqchange = false;
	public Date getDaocrq() {
		return _DaocrqValue;
	}

	public void setDaocrq(Date _value) {
		if (FormatDate(_DaocrqValue).equals(FormatDate(_value))) {
			_Daocrqchange = false;
		} else {
			_DaocrqValue = _value;
			_Daocrqchange = true;
		}
	}
	private boolean checkbh() {
		// TODO 自动生成方法存根
		JDBCcon con = new JDBCcon();
		String sql = "";
		try {
			sql = " select jiesbh from ((select id,bianm as jiesbh from jiesb) union (select id,bianm as jiesbh from jiesyfb)) where jiesbh='" + ((Jieslrbean)getEditValues().get(0)).getJiesbh()
			+ "' and (id <> "+((Jieslrbean)getEditValues().get(0)).getId()+" and id <> "+((Jieslrbean)getEditValues().get(0)).getYid()+") ";
			ResultSet rs = con.getResultSet(sql);
			if (rs.next()) {
				return false;
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return true;
	}
	private void Save() {

	    JDBCcon con = new JDBCcon();
	    Visit visit=(Visit) getPage().getVisit();
	    String sql = "";
//		double dityf=0;
//		if(((Jieslrbean) getEditValues().get(0)).getHansyf_k()==0){
//			dityf=((Jieslrbean) getEditValues().get(0)).getHansyf_q();
//		}else{
//			dityf=((Jieslrbean) getEditValues().get(0)).getHansyf_k();
//		}
		long mid = Long.parseLong(visit.getString1().substring(0));
		
		String hetbhm=getHetbhValue().getValue();
	    try{
	        if(getEditValues()!= null && !getEditValues().isEmpty()&& !((Jieslrbean)getEditValues().get(0)).getJiesbh().equals("")){
			        		sql="update jiesb\n" +
			        			"   set gongysb_id =(select max(id) from gongysb where mingc='" +((Jieslrbean) getEditValues().get(0)).getGonghdw()+"'), gongysmc = '" + ((Jieslrbean) getEditValues().get(0)).getGonghdw()+"',faz = '" + ((Jieslrbean) getEditValues().get(0)).getFaz()+"'," +
			        			"		fahksrq = to_date('"+this.FormatDate(((Jieslrbean) getEditValues().get(0)).getChengyrq())+"','yyyy-MM-dd'),\n" + 
			        			"       fahjzrq =to_date('"+this.FormatDate(((Jieslrbean) getEditValues().get(0)).getChengyrq())+"','yyyy-MM-dd'),\n" + 
			        			"       meiz ='"+((Jieslrbean) getEditValues().get(0)).getMeitpz()+"',\n" + 
			        			"       yansksrq = to_date('"+ this.FormatDate(((Jieslrbean) getEditValues().get(0)).getDaocrq())+ "','yyyy-MM-dd'),\n" + 
			        			"       shoukdw = '"+((Jieslrbean) getEditValues().get(0)).getJiesdw()+"',\n" + 
			        			"       ches = "+((Jieslrbean) getEditValues().get(0)).getChes()+",\n" + 
			        			"       jiessl = "+((Jieslrbean) getEditValues().get(0)).getShijjsl()+",\n" + 
			        			"       guohl = "+((Jieslrbean) getEditValues().get(0)).getJingz()+",\n" + 
//			        			"       hansdj = "+((Jieslrbean) getEditValues().get(0)).getHansdj()+",\n" +
			        			"		hansdj = "+((Jieslrbean) getEditValues().get(0)).getMeij()+",\n"+
			        			"       bukmk = "+((Jieslrbean) getEditValues().get(0)).getJiakmk()+",\n" + 
//			        			"       hansmk = "+((Jieslrbean) getEditValues().get(0)).getMeij()*((Jieslrbean) getEditValues().get(0)).getShijjsl()+",\n" +
			        			"		hansmk = "+((Jieslrbean) getEditValues().get(0)).getJiashj()+",\n"+
			        			"       buhsmk = "+((Jieslrbean) getEditValues().get(0)).getBuhsmk()+",\n" + 
//			        			"       meikje = "+((Jieslrbean) getEditValues().get(0)).getJiashj()+",\n" + 
			        			"       shuik = "+((Jieslrbean) getEditValues().get(0)).getShuij()+",\n" + 
			        			"       shuil = "+((Jieslrbean) getEditValues().get(0)).getShuil()+",\n" + 
			        			"       buhsdj = "+((Jieslrbean) getEditValues().get(0)).getBuhsdj()+",\n" + 
			        			"       jiesrq = to_date('"+ this.FormatDate(((Jieslrbean) getEditValues().get(0)).getJiesrq())+ "','yyyy-MM-dd'),\n" + 
			        			"       ruzrq = to_date('"+ this.FormatDate(((Jieslrbean) getEditValues().get(0)).getJiesrq())+ "','yyyy-MM-dd'),\n"+ 
//			        			if(!((Jieslrbean) getEditValues().get(0)).getHetbh().equals("")){
//			        					sql=sql+"hetb_id = (select id from hetb where hetbh='"+ ((Jieslrbean) getEditValues().get(0)).getHetbh()+ "'),\n";  
//			        			}
			        			"		hetb_id = (select id from hetb where hetbh='"+ hetbhm+ "'),\n"+
//		        						sql=sql+
		        				"		ranlbmjbr = '"+((Jieslrbean) getEditValues().get(0)).getJingbr()+"',\n" + 
			        			"       ranlbmjbrq = to_date('"+ this.FormatDate(((Jieslrbean) getEditValues().get(0)).getJiesrq())+ "','yyyy-MM-dd'),\n" + 
			        			"       beiz = '"+((Jieslrbean) getEditValues().get(0)).getBeiz()+"',\n" + 
			        			"       yunsfsb_id = (select id from yunsfsb where mingc='"+((Jieslrbean) getEditValues().get(0)).getYunsfs()+"'),\n" + 
			        			"       yunj = "+((Jieslrbean) getEditValues().get(0)).getYunsjl()+",\n" + 
			        			"       yingd = "+ ((Jieslrbean) getEditValues().get(0)).getYingd()+",\n" + 
			        			"       kuid = "+((Jieslrbean) getEditValues().get(0)).getKuid()+",\n" + 
			        			"       yuns = "+((Jieslrbean) getEditValues().get(0)).getYuns()+",\n" + 
			        			"       koud = "+((Jieslrbean) getEditValues().get(0)).getKoud()+",\n" + 
			        			"       jiesslcy = "+((Jieslrbean) getEditValues().get(0)).getChayl()+",\n" + 
			        			"		hetj = "+((Jieslrbean) getEditValues().get(0)).getJiashj()+",\n"+
			        			"		jihkjb_id =(select id from jihkjb where mingc='"+((Jieslrbean) getEditValues().get(0)).getJihkj()+"')\n"+
			        			" where id ="+mid;
			        		con.getUpdate(sql);
			        		sql=
			        			"update jiesyfb\n" +
			        			"   set bianm = '"+ hetbhm+"',\n" + 
			        			"       gongysb_id = (select max(id) from gongysb where mingc='"+((Jieslrbean) getEditValues().get(0)).getGonghdw()+"'),\n" + 
			        			"       gongysmc = '" + ((Jieslrbean) getEditValues().get(0)).getGonghdw()+"',faz = '" + ((Jieslrbean) getEditValues().get(0)).getFaz()+"'," +
			        			"		fahksrq = to_date('"+this.FormatDate(((Jieslrbean) getEditValues().get(0)).getChengyrq())+"','yyyy-MM-dd'),\n" + 
			        			"       fahjzrq =to_date('"+this.FormatDate(((Jieslrbean) getEditValues().get(0)).getChengyrq())+"','yyyy-MM-dd'),\n" + 
			        			"       meiz ='"+((Jieslrbean) getEditValues().get(0)).getMeitpz()+"',\n" + 
			        			"       yansksrq = to_date('"+ this.FormatDate(((Jieslrbean) getEditValues().get(0)).getDaocrq())+ "','yyyy-MM-dd'),\n" + 
			        			"       shoukdw = '"+((Jieslrbean) getEditValues().get(0)).getJiesdw()+"',\n" + 
			        			"       ches = "+((Jieslrbean) getEditValues().get(0)).getChes()+",\n" + 
			        			"       jiessl = "+((Jieslrbean) getEditValues().get(0)).getShijjsl()+",\n" + 
			        			"       guohl = "+((Jieslrbean) getEditValues().get(0)).getJingz()+",\n" + 
			        			"       guotyf = "+((Jieslrbean) getEditValues().get(0)).getYingfyf()+",\n" + 
			        			"       dityf = "+((Jieslrbean) getEditValues().get(0)).getHansyf_k()+",\n" +
			        			"       jiskc = "+(((Jieslrbean) getEditValues().get(0)).getHej_b()+((Jieslrbean) getEditValues().get(0)).getQityzf())+",\n" + 
//			        			"       hansdj = "+((Jieslrbean) getEditValues().get(0)).getHansdj()+",\n" + 
			        			"       bukyf = "+((Jieslrbean) getEditValues().get(0)).getQityzf()+",\n" + 
			        			"       hansyf = "+((Jieslrbean) getEditValues().get(0)).getYunfhj()+",\n" + 
			        			"       buhsyf = "+((Jieslrbean) getEditValues().get(0)).getYunf()+",\n" + 
			        			"       shuik = "+((Jieslrbean) getEditValues().get(0)).getYunfsj()+",\n" + 
			        			"       shuil = "+((Jieslrbean) getEditValues().get(0)).getYunfsl()+",\n" + 
//			        			"       buhsdj = "+((Jieslrbean) getEditValues().get(0)).getBuhsdj()+",\n" + 
			        			"       jiesrq = to_date('"+ this.FormatDate(((Jieslrbean) getEditValues().get(0)).getJiesrq())+ "','yyyy-MM-dd'),\n" + 
			        			"       ruzrq = to_date('"+ this.FormatDate(((Jieslrbean) getEditValues().get(0)).getJiesrq())+ "','yyyy-MM-dd'),\n" +
//			        			if(!((Jieslrbean) getEditValues().get(0)).getHetbh().equals("")){
//			        					sql=sql+"hetb_id = (select id from hetb where hetbh='"+ ((Jieslrbean) getEditValues().get(0)).getHetbh()+ "'),\n" ; 
//			        			}
//			        					sql=sql+"diancjsmkb_id = "+mid+",\n" + 
				        		"		hetb_id = (select id from hetb where hetbh='"+ hetbhm+ "'),\n" +
				        		"		diancjsmkb_id = "+mid+",\n" + 
			        			"       ranlbmjbr = '"+((Jieslrbean) getEditValues().get(0)).getJingbr()+"',\n" + 
			        			"       ranlbmjbrq = to_date('"+ this.FormatDate(((Jieslrbean) getEditValues().get(0)).getJiesrq())+"','yyyy-MM-dd'),\n" + 
			        			"       beiz = '"+((Jieslrbean) getEditValues().get(0)).getBeiz()+"',\n" + 
			        			"       yunsfsb_id = (select id from yunsfsb where mingc='"+((Jieslrbean) getEditValues().get(0)).getYunsfs()+"'),\n" + 
			        			"       yunj = "+((Jieslrbean) getEditValues().get(0)).getYunsjl()+",\n" + 
			        			"       yingd = "+ ((Jieslrbean) getEditValues().get(0)).getYingd()+",\n" + 
			        			"       kuid = "+((Jieslrbean) getEditValues().get(0)).getKuid()+",\n" + 
			        			"       yuns = "+((Jieslrbean) getEditValues().get(0)).getYuns()+",\n" + 
			        			"       koud = "+((Jieslrbean) getEditValues().get(0)).getKoud()+",\n" + 
			        			"       jiesslcy = "+((Jieslrbean) getEditValues().get(0)).getChayl()+",\n" + 
			        			"       guotzf = "+((Jieslrbean) getEditValues().get(0)).getYingfyf_b()+",\n" +
			        			"       guotyfjf = "+((Jieslrbean) getEditValues().get(0)).getYingkyf()+",\n" + 
			        			"       guotzfjf = "+((Jieslrbean) getEditValues().get(0)).getYingkyf_b()+",\n" + 
			        			"       qiyf = "+((Jieslrbean) getEditValues().get(0)).getHansyf_q()+" \n" +
			        			" where id = (select id from jiesyfb where diancjsmkb_id="+mid+")";
			        		con.getUpdate(sql);
			        		sql="update jieszbsjb\n" +
			        			"   set gongf = "+((Jieslrbean) getEditValues().get(0)).getPiaoz()+",\n" + 
			        			"		changf="+((Jieslrbean) getEditValues().get(0)).getJingz()+"+"+((Jieslrbean) getEditValues().get(0)).getYuns()+",\n"+
			        			"		jies="+((Jieslrbean) getEditValues().get(0)).getShijjsl()+"\n"+
			        			" where id = (select j.id from jieszbsjb j,zhibb z where j.zhibb_id = z.id and z.bianm='数量'and j.jiesdid="+mid+")";
			        		con.getUpdate(sql);
			        		sql="update jieszbsjb\n" +
			        			"   set  hetbz = '"+((Jieslrbean) getEditValues().get(0)).getRez_ht()+"',\n" +
			        			"        gongf = "+((Jieslrbean) getEditValues().get(0)).getRez_gf()+",\n" + 
			        			"        changf = "+((Jieslrbean) getEditValues().get(0)).getRez_ys()+",\n" + 
			        			"        jies = "+((Jieslrbean) getEditValues()	.get(0)).getRez_js()+
			        			" where id = (select j.id from jieszbsjb j,zhibb z where j.zhibb_id = z.id and z.bianm='收到基低位热值Qnetar(MJ/Kg)'and j.jiesdid="+mid+")";
			        		con.getUpdate(sql);
			        		sql="update jieszbsjb\n" +
				        		"   set  hetbz = '"+((Jieslrbean) getEditValues().get(0)).getHuif_ht()+"',\n" +
			        			"        gongf = "+((Jieslrbean) getEditValues().get(0)).getHuif_gf()+",\n" + 
			        			"        changf = "+((Jieslrbean) getEditValues().get(0)).getHuif_ys()+",\n" + 
			        			"        jies = "+((Jieslrbean) getEditValues()	.get(0)).getHuif_js()+
			        			" where id = (select j.id from jieszbsjb j,zhibb z where j.zhibb_id = z.id and z.bianm='干燥基灰分Ad(%)'and j.jiesdid="+mid+")";
			        		con.getUpdate(sql);
			        		sql="update jieszbsjb\n" +
				        		"   set  hetbz = '"+((Jieslrbean) getEditValues().get(0)).getHuiff_ht()+"',\n" +
			        			"        gongf = "+((Jieslrbean) getEditValues().get(0)).getHuiff_gf()+",\n" + 
			        			"        changf = "+((Jieslrbean) getEditValues().get(0)).getHuiff_ys()+",\n" + 
			        			"        jies = "+((Jieslrbean) getEditValues()	.get(0)).getHuiff_js()+
			        			" where id = (select j.id from jieszbsjb j,zhibb z where j.zhibb_id = z.id and z.bianm='干燥无灰基挥发分Vdaf(%)'and j.jiesdid="+mid+")";
			        		con.getUpdate(sql);
			        		sql="update jieszbsjb\n" +
				        		"   set  hetbz = '"+((Jieslrbean) getEditValues().get(0)).getShuif_ht()+"',\n" +
			        			"        gongf = "+((Jieslrbean) getEditValues().get(0)).getShuif_gf()+",\n" + 
			        			"        changf = "+((Jieslrbean) getEditValues().get(0)).getShuif_ys()+",\n" + 
			        			"        jies = "+((Jieslrbean) getEditValues()	.get(0)).getShuif_js()+
			        			" where id = (select j.id from jieszbsjb j,zhibb z where j.zhibb_id = z.id and z.bianm='全水分Mt(%)'and j.jiesdid="+mid+")";
			        		con.getUpdate(sql);
			        		sql="update jieszbsjb\n" +
				        		"   set  hetbz = '"+((Jieslrbean) getEditValues().get(0)).getLiuf_ht()+"',\n" +
			        			"        gongf = "+((Jieslrbean) getEditValues().get(0)).getLiuf_gf()+",\n" + 
			        			"        changf = "+((Jieslrbean) getEditValues().get(0)).getLiuf_ys()+",\n" + 
			        			"        jies = "+((Jieslrbean) getEditValues()	.get(0)).getLiuf_js()+
			        			" where id = (select j.id from jieszbsjb j,zhibb z where j.zhibb_id = z.id and z.bianm='干燥基全硫Std(%)'and j.jiesdid="+mid+")";
			        		
			        		con.getUpdate(sql);
			        		
			        		setMsg("结算单更新成功！");

	        }
	        
	    }catch(Exception e){
	        e.printStackTrace();
	    }finally{
	    	con.Close();
	    }
//	    getSelectData();
	}	
	
	private String hetbh="";
	public List getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		List _editvalues = new ArrayList();
		if(getEditValues()!=null){
			
			getEditValues().clear();
		}
		JDBCcon con = new JDBCcon();
		try {
			ResultSet rs;
//			String strleix = visit.getString1().substring(0,2);
//			long mid = Long.parseLong(visit.getString1().substring(2));
			long mid = Long.parseLong(visit.getString1().substring(0));
			String sql="";
			long myid=0;
			long mhetid=0;
			String mjiesbh="";
			String mhetbh="";
			String mjiesdw="";
			String mfaz = "";
			String mmeitpz="";
			String mgongysdw="";
			String myunsjl="";
			String myunsfs="";
			String mjihkj = "";
			Date mjiesrq = new Date();
			Date mchengyrq = new Date();
			Date mdaocrq = new Date();
			double mmeij = 0;
			double myingfyf = 0;
			long mches = 0;
			double mkuid = 0;
			double mjiakmk = 0;
			double myingkyf = 0;
			double mpiaoz = 0;
			double myuns = 0;
			double mhansdj = 0;
			
			double mhej = 0;
			double mjingz = 0;
			double mkoud = 0;
			double mbuhsdj = 0;
			double myingd = 0;
			double mchayl = 0;
			double mshuil = 0;
			double myingfyf_b = 0;
			double myingkyf_b = 0;
			double mshijjsl = 0;
			double mbuhsmk = 0;
			double mhej_b = 0;
			double mshuij = 0;
//			double mkuangyf = 0;
			String mrez_ht = "";
			double mrez_ys = 0;
			double mrez_gf = 0;
			double mrez_js = 0;
			double mjiashj = 0;
//			double mqiyf = 0;
			String mhuif_ht = "";
			double mhuif_ys = 0;
			double mhuif_gf = 0;
			double mhuif_js = 0;
			double myunfsl = 0;
			double mqityzf = 0;
			String mhuiff_ht = "";
			double mhuiff_ys = 0;
			double mhuiff_gf = 0;
			double mhuiff_js = 0;
			double myunf = 0;
//			double mguot = 0;
			String mshuif_ht = "";
			double mshuif_ys = 0;
			double mshuif_gf = 0;
			double mshuif_js = 0;
			double myunfsj = 0;
//			double mkuangy = 0;
			String mliuf_ht = "";
			double mliuf_ys = 0;
			double mliuf_gf = 0;
			double mliuf_js = 0;
			double myunfhj = 0;
//			double mqiy = 0;
			String mbeiz = "";
			double mshifzje=0;
			String mshifzjexx="";
			String mshifzjedx="";
			double mhansyf_k=0;
			double mshuij_k=0;
			double mhansyf_q=0;
			double mshuij_q=0;
//			mhansdj= (mmeij*mshijjsl+mjiakmk)/mshijjsl;
			String mjingbr= visit.getDiancmc();

				 sql = "select js.bianm as jiesbh,js.shoukdw,js.faz,js.meiz,gy.mingc as gongysmc,ys.mingc as yunsfs,kj.mingc as jihkjmc,ht.hetbh,js.YUNJ as yunsjl,js.yingd,js.kuid,js.guohl as jingz,js.koud,js.yuns,  \n"
					+ "       js.jiesrq as jiesrq,js.fahksrq as chengyrq,js.yansksrq as daocrq,js.JIESSLCY as chal,js.ranlbmjbr,\n"
					+ "       js.hansdj as hansdj,js.hansmk as jiashj,js.ches,js.hansdj,js.buhsdj,js.shuil,js.jiessl as shijjsl,js.buhsmk,\n"
					+ "		  js.shuik as shuij,js.bukmk as jiakmk,js.beiz as shuom,round(((js.hansdj*js.jiessl+js.bukmk)/js.jiessl),2) as hansdj_js  \n"
					+ "  from jiesb js,diancxxb dc,gongysb gy,hetb ht,jihkjb kj,yunsfsb ys \n"
					+ " where js.diancxxb_id=dc.id and js.gongysb_id=gy.id and js.hetb_id=ht.id(+) and js.jihkjb_id=kj.id(+) and js.yunsfsb_id=ys.id(+) \n"
					+ "   and js.id="+mid+"";
				 
				 rs = con.getResultSet(sql);
				 if(rs.next()){
					 mjiesbh = rs.getString("jiesbh");
					 mhetbh = rs.getString("hetbh");
					 hetbh=mhetbh;
					 mjiesdw = rs.getString("shoukdw");
					 mfaz = rs.getString("faz");
					 mmeitpz = rs.getString("meiz");
					 mgongysdw = rs.getString("gongysmc");
					 myunsjl = rs.getString("yunsjl");//运输距离
					 myunsfs = rs.getString("yunsfs");
					 mjihkj = rs.getString("jihkjmc");
					 mjiesrq = rs.getDate("jiesrq");
					 mchengyrq = rs.getDate("chengyrq");
					 mdaocrq = rs.getDate("daocrq");
					 mmeij = rs.getDouble("hansdj");//取得含税单价
					 mches = rs.getLong("ches");
					 mhansdj = rs.getDouble("hansdj_js");
					 mbuhsdj = rs.getDouble("buhsdj");
					 mshuil = rs.getDouble("shuil");
					 mshijjsl = rs.getLong("shijjsl");
					 mbuhsmk = rs.getDouble("buhsmk");
					 mshuij = rs.getDouble("shuij");//取的税款
					 mjiakmk = rs.getDouble("jiakmk");//加扣煤款
					 mjiashj = rs.getDouble("jiashj");//价税合计
					 mbeiz = rs.getString("shuom");
					 mjingbr=rs.getString("ranlbmjbr");//經辦人
					 
					 myingd = rs.getLong("yingd");
					 mkuid = rs.getLong("kuid"); 
					 mjingz = rs.getLong("jingz");
					 myuns = rs.getLong("yuns");
					 mkoud = rs.getLong("koud");
					 mchayl = rs.getLong("chal");//差异量
				}
				
				sql = "select nvl(zl.gongf,0) as piaoz from jiesb js,jieszbsjb zl,zhibb zb "
					+ " where zl.jiesdid=js.id and zl.zhibb_id=zb.id and js.id="+mid+" and zb.bianm='数量' and zb.leib=1 ";
				rs = con.getResultSet(sql);
				if(rs.next()){
					mpiaoz = rs.getLong("piaoz");
				}
//				质量数据
				//热量
				sql = "select nvl(zl.hetbz,0) as hetrz,nvl(zl.gongf,0) as gongfrz,nvl(zl.changf,0) as yansrz,nvl(zl.jies,0) as jiesrz from jiesb js,jieszbsjb zl,zhibb zb "
					+ " where zl.jiesdid=js.id and zl.zhibb_id=zb.id and js.id="+mid+" and zb.bianm='收到基低位热值Qnetar(MJ/Kg)' and zb.leib=1 ";
				rs = con.getResultSet(sql);
				if(rs.next()){
					 mrez_ht = rs.getString("hetrz");
					 mrez_ys = rs.getDouble("yansrz");
					 mrez_gf = rs.getDouble("gongfrz");
					 mrez_js = rs.getDouble("jiesrz");
				}
//				硫分
				sql = "select nvl(zl.hetbz,0) as hetlf,nvl(zl.gongf,0) as gongflf,nvl(zl.changf,0) as yanslf,nvl(zl.jies,0) as jieslf from jiesb js,jieszbsjb zl,zhibb zb "
					+ " where zl.jiesdid=js.id and zl.zhibb_id=zb.id and js.id="+mid
//					+"   and zb.mingc='干燥基全硫'and zb.leib=1 ";
					+ "   and (zb.bianm='干燥基全硫Std(%)' or zb.bianm='一般分析煤样全硫Stad(%)') and zb.leib=1 ";
				rs = con.getResultSet(sql);
				if(rs.next()){
					 mliuf_ht = rs.getString("hetlf");
					 mliuf_ys = rs.getDouble("yanslf");
					 mliuf_gf = rs.getDouble("gongflf");
					 mliuf_js = rs.getDouble("jieslf");
				}
//				灰分
				sql = "select nvl(zl.hetbz,0) as hethf,nvl(zl.gongf,0) as gongfhf,nvl(zl.changf,0) as yanshf,nvl(zl.jies,0) as jieshf from jiesb js,jieszbsjb zl,zhibb zb "
					+ " where zl.jiesdid=js.id and zl.zhibb_id=zb.id and js.id="+mid
//					+ "   and  zb.mingc='干燥基灰分'  and zb.leib=1 ";
					+ "   and (zb.bianm='干燥基灰分Ad(%)' or zb.bianm='收到基灰分Aar(%)' or zb.bianm='一般分析煤样灰分Aad(%)') and zb.leib=1 ";
				rs = con.getResultSet(sql);
				if(rs.next()){
					 mhuif_ht = rs.getString("hethf");
					 mhuif_ys = rs.getDouble("yanshf");
					 mhuif_gf = rs.getDouble("gongfhf");
					 mhuif_js = rs.getDouble("jieshf");
				}
//				挥发分
				sql = "select nvl(zl.hetbz,0) as hethff,nvl(zl.gongf,0) as gongfhff,nvl(zl.changf,0) as yanshff,nvl(zl.jies,0) as jieshff from jiesb js,jieszbsjb zl,zhibb zb "
					+ " where zl.jiesdid=js.id and zl.zhibb_id=zb.id and js.id="+mid
//					+ "   and zb.mingc='干燥无灰基挥发分' and zb.leib=1 ";
					+ "   and (zb.bianm='干燥无灰基挥发分Vdaf(%)' or zb.bianm='一般分析煤样挥发分Vad(%)') and zb.leib=1 ";
				rs = con.getResultSet(sql);
				if(rs.next()){
					 mhuiff_ht = rs.getString("hethff");
					 mhuiff_ys = rs.getDouble("yanshff");
					 mhuiff_gf = rs.getDouble("gongfhff");
					 mhuiff_js = rs.getDouble("jieshff");
				}
//				水分
				sql = "select nvl(zl.hetbz,0) as hetsf,nvl(zl.gongf,0) as gongfsf,nvl(zl.changf,0) as yanssf,nvl(zl.jies,0) as jiessf from jiesb js,jieszbsjb zl,zhibb zb "
					+ " where zl.jiesdid=js.id and zl.zhibb_id=zb.id and js.id="+mid
//					+ "   and  zb.mingc='全水分' and zb.leib=1 ";
					+ "   and (zb.bianm='全水分Mt(%)' or zb.bianm='一般分析煤样水分Mad(%)') and zb.leib=1 ";
				rs = con.getResultSet(sql);
				if(rs.next()){
					 mshuif_ht = rs.getString("hetsf");
					 mshuif_ys = rs.getDouble("yanssf");
					 mshuif_gf = rs.getDouble("gongfsf");
					 mshuif_js = rs.getDouble("jiessf");
				}
				 
//				运费数据
				sql = "select yf.shuil as yunfsl,yf.shuik as yunfsj,yf.buhsyf as yunf,yf.guotyf,yf.dityf,yf.jiskc,yf.bukyf,yf.guotyfjf,yf.guotzfjf,yf.qiyf \n"
					+"		,yf.guotzf "
					+ " from jiesb js,jiesyfb yf where yf.diancjsmkb_id=js.id and js.id="+mid;
				rs = con.getResultSet(sql);
				if(rs.next()){
					 myunf = rs.getDouble("yunf");//运费
//					 myunfsj = rs.getDouble("yunfsj");//运费税金
//					 myunfsl = rs.getDouble("yunfsl");//运费税率
					 myingfyf=rs.getDouble("guotyf");
					 myingkyf=rs.getDouble("guotyfjf");
					 mhej=myingfyf-myingkyf;
					 myingfyf_b=rs.getDouble("guotzf");
					 myingkyf_b=rs.getDouble("guotzfjf");
					 mhej_b=myingfyf_b-myingkyf_b;
					 mhansyf_k=rs.getDouble("dityf");
					 mhansyf_q=rs.getDouble("qiyf");
					 mqityzf=rs.getDouble("bukyf");
					 myunfsl=rs.getDouble("yunfsl");//运费税率
					 
					 myunfsj=rs.getDouble("yunfsj");
//					 String stryunfsj=new DecimalFormat("0.00").format(mhej*myunfsl+mshuij_k+mshuij_q);
//					 myunfsj=Double.parseDouble(stryunfsj);
					 String stryunfhj=new DecimalFormat("0.00").format(mhej+mhej_b+mhansyf_k+mshuij_k+mhansyf_q+mqityzf);
					 myunfhj=Double.parseDouble(stryunfhj);
					 String stryunf = new DecimalFormat("0.00").format(myunfhj-myunfsj);
					 myunf =Double.parseDouble(stryunf);
					 mshifzje=myunfhj+mjiashj;
					 
					 mshifzjedx=getDXMoney(mshifzje);
					 
					 
	//				 strkuangyf = rs.getString("kuangyf");//矿运费
	//				 strkedsyfyf = rs.getString("kedsyfyf");//可抵税应付运费				 
	//				 strkedsykyf = rs.getString("kedsykyf");//可抵税应扣运费
	//				 strkedshj = rs.getString("kedshj");//可抵税合计
	//				 strbukdsyfyf = rs.getString("bukdsyfyf");//不可抵税应付运费
	//				 strbukdsykyf = rs.getString("bukdsykyf");//不可抵税应扣运费
	//				 strbukdshj = rs.getString("bukdshj");//不可抵税合计
	//				 strqiyf = rs.getString("qiyf");//汽运费
	//				 
	//				 strqityzf = rs.getString("qityzf");//其它运杂费
	//				 strguotyj = rs.getString("guotyj");//国铁运价
	//				 strkuangyyj = rs.getString("kuangyyj");//矿运运价
	//				 stryunfhj = rs.getString("yunfhj");//运费合计
		//				 strqiyyj = rs.getString("qiyyj");//汽运运价
									 
//					 	}
					 
				 
//					 sql = "select js.bianm as jiesbh,js.shoukdw,js.faz,js.meiz,js.gongysmc,ys.mingc as yunsfs, \n"
//						+ "       kj.mingc as hetxz,ht.hetbh,js.YUNJ as yunsjl,js.yingd,js.kuid,js.guohl as jingz,js.koud,js.yuns, \n"
//						+ "       to_char(js.jiesrq,'yyyy-mm-dd') as jiesrq,getRiqDuan(js.fahksrq,js.fahjzrq) as chengyrq,getRiqDuan(js.yansksrq,js.yansjzrq) as daocrq,js.JIESSLCY as chal, \n"
//						+ "       js.hansdj as meij,js.ches,js.hansdj,js.buhsdj,js.shuil as yunfsl,js.jiessl as shijjsl,js.shuik as yunfsj,js.guotyf as yunf,js.beiz as shuom \n"
//						+ "  from jiesyfb js,diancxxb dc,hetb ht,jihkjb kj,YUNSFSB ys \n"
//						+ " where js.diancxxb_id=dc.id  and js.hetb_id=ht.id and ht.jihkjb_id=kj.id and js.YUNSFSB_ID=ys.id(+) \n"
//						+ "   and js.id="+mid+"";
//					 rs = con.getResultSet(sql);
//					 if(rs.next()){
//					 }
				}
			_editvalues.add(new Jieslrbean(mid,myid,mhetid, mjiesbh,mhetbh,mjiesdw,
					mfaz,mmeitpz,mgongysdw,myunsjl,myunsfs,mjihkj,
					mjiesrq,mchengyrq,mdaocrq,mmeij,myingfyf,mches,
					mkuid, mjiakmk, myingkyf, mpiaoz, myuns, mhansdj,
					mhej, mjingz, mkoud, mbuhsdj, myingfyf_b, myingd,
					mchayl, mshuil, myingkyf_b, mshijjsl, mbuhsmk, mhej_b,
					mshuij,// mkuangyf,
					mrez_ht, mrez_ys, mrez_gf, mrez_js,
					mjiashj, //mqiyf,
					mhuif_ht, mhuif_ys, mhuif_gf, mhuif_js,
					myunfsl, mqityzf, mhuiff_ht, mhuiff_ys, mhuiff_gf,
					mhuiff_js, myunf,// mguot,
					mshuif_ht, mshuif_ys,
					mshuif_gf, mshuif_js, myunfsj,// mkuangy,
					mliuf_ht, mliuf_ys, mliuf_gf, mliuf_js, myunfhj,// mqiy,
					mbeiz,mjingbr,mshifzje,mshifzjedx,mhansyf_k,mshuij_k,mhansyf_q,mshuij_q));

			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
			
		}
		if (_editvalues == null) {
			_editvalues.add(new Jieslrbean());
		}
		_editTableRow = -1;
		setEditValues(_editvalues);
		return getEditValues();
	}
//		private void Delete(){
//	    JDBCcon con=new JDBCcon();
//	    Visit visit = (Visit) getPage().getVisit();
//	    try{
//	    	String sql="";
//	    	long mid = Long.parseLong(visit.getString1().substring(2));
//	    	boolean flag=false;
//	    	if(getEditValues()!= null && !getEditValues().isEmpty()){
//	    		//不单独删除运费
//	    			sql="select id from jiesb d where d.id="+mid+"";
//	    			
//	    			ResultSet rs=con.getResultSet(sql);
//	    			if(rs.next()){
//	    				
//	    					sql="delete from jiesb where id="+rs.getLong("id");
//	    					con.getDelete(sql);
//	    					
//	    					sql="delete from jiesyfb  where DIANCJSMKB_ID="+rs.getLong("id");
//	    					con.getDelete(sql);
//	    					
//	    					sql="delete from jieszbsjb where jiesdid="+rs.getLong("id");
//	    					con.getDelete(sql);
//	    					
//	    					flag=true;
//	    			
//	    			}
////	    			单独删除运费
//				setMsg("结算单已删除！");
//	    	}
//	     }catch(Exception e){
//	    	
//	    	e.printStackTrace();
//	    }finally{
//	    	
//	    	con.Close();
//	    }
//		getEditValues().clear();
//	}

	private void Retruns() {

		getSelectData();
	}
	private boolean _RetrunsChick = false;

	public void RetrunsButton(IRequestCycle cycle) {
		_RetrunsChick = true;
	}
	
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _ReturnChick = false;

	public void ReturnButton(IRequestCycle cycle) {
		_ReturnChick = true;
	}
	
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_RetrunsChick) {
			_RetrunsChick = false;
			Retruns();
		}
		if(_ReturnChick){
			_ReturnChick=false;
			
			((Visit) getPage().getVisit()).getPagePreferences().equals("Jiesdtb");
			cycle.activate("Jiesdtb");
		}
		
	}
	public List getEditValues() {
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setEditValues(List editList) {
		((Visit) getPage().getVisit()).setList1(editList);
	}

	
	private Jieslrbean _EditValue;
	public Jieslrbean getEditValue() {
		return _EditValue;
	}

	public void setEditValue(Jieslrbean EditValue) {
		_EditValue = EditValue;
	}
	
	
	
	public String getTitle(){
		
		return Locale.jiesd_title;
	}


	
	private void getToolbars(){

		Toolbar tb1 = new Toolbar("tbdiv");
		ToolbarButton fanhbt=new ToolbarButton(null,"返回","function (){ document.getElementById('ReturnButton').click();}");
		fanhbt.setId("fanhbt");
		tb1.addItem(fanhbt);
		tb1.addText(new ToolbarText("-"));
//		刷新
		ToolbarButton shuaxbt=new ToolbarButton(null,"刷新","function(){ document.Form0.RetrunsButton.click();}");
		shuaxbt.setId("Shuaxbt");
		tb1.addItem(shuaxbt);
		tb1.addText(new ToolbarText("-"));
//		保存
		ToolbarButton savebt=new ToolbarButton(null,"保存","function(){ document.Form0.SaveButton.click(); }");
		savebt.setId("savebt");
		tb1.addItem(savebt);
		tb1.addText(new ToolbarText("-"));
		setToolbar(tb1);
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			//在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setInt1(0);
			_JiesrqValue = new Date();
			_ChengyrqValue = new Date();
			_DaocrqValue = new Date();

			visit.setString1(null);
			setHetbhValue(null);
			getIHetbhModels();
			setJiesdwValue(null);
			getIJiesdwModels();
			setJihkjValue(null);
			getIJihkjModels();
			setFazValue(null);
			getIFazModels();
			setGonghdwValue(null);
			getIGonghdwModels();
			setMeitpzValue(null);
			getIMeitpzModels();
			setYunsfsValue(null);
			getIYunsfsModels();
			
//			getSelectData();
		}

		if (cycle.getRequestContext().getParameter("bianm") != null&&!cycle.getRequestContext().getParameter("bianm").equals("-1")) {
			visit.setString1(cycle.getRequestContext().getParameter("bianm"));
		}
		getToolbars();
		getSelectData();
	}

	
	public void setJieszb(String value){
		
		((Visit) getPage().getVisit()).setString2(value);
	}
	
	public String getJieszb(){
		
		return ((Visit) getPage().getVisit()).getString2();
	}
	
	//工具条_begin
	
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
	
	//工具条_end
	
	public boolean isGongsdp() {
		return ((Visit) getPage().getVisit()).getboolean2();
	}

	public void setGongsdp(boolean gongsdp) {
		((Visit) getPage().getVisit()).setboolean2(gongsdp);
	}
	
	public boolean isDiancdp() {
		return ((Visit) getPage().getVisit()).getboolean3();
	}

	public void setDiancdp(boolean diancdp) {
		((Visit) getPage().getVisit()).setboolean3(diancdp);
	}
//	********************合同编码********************************************************
	 public IDropDownBean getHetbhValue() {
	    	if(((Visit)getPage().getVisit()).getDropDownBean14()==null){
	    		
	    		((Visit)getPage().getVisit()).setDropDownBean14((IDropDownBean)getIHetbhModel().getOption(0));
	    		
	    		for (int i =0; i< ((Visit)getPage().getVisit()).getProSelectionModel14().getOptionCount();i++) {
	    			Object obj= ((Visit)getPage().getVisit()).getProSelectionModel14().getOption(i);
	    			if(hetbh.equals(((IDropDownBean) obj).getValue())){
	    				
	    				((Visit)getPage().getVisit()).setDropDownBean14((IDropDownBean) obj);
						break;
	    			}
	    		}
	    	}
	       return ((Visit)getPage().getVisit()).getDropDownBean14();
	       
	    }
	    
	    public void setHetbhValue(IDropDownBean value)
	    {
	    	if(((Visit)getPage().getVisit()).getDropDownBean14()!=value){
	    		
	    		((Visit)getPage().getVisit()).setDropDownBean14(value);
	        }
	    }
	    
	    public void setIHetbhModel(IPropertySelectionModel value){
	    	

	    	((Visit)getPage().getVisit()).setProSelectionModel14(value);
		}
	    
	    public IPropertySelectionModel getIHetbhModel(){
	            
	    	if(((Visit)getPage().getVisit()).getProSelectionModel14()==null){
	        
	    		getIHetbhModels();
	        }
	        return ((Visit)getPage().getVisit()).getProSelectionModel14();
	    }
	    
	    public IPropertySelectionModel getIHetbhModels() {

			JDBCcon con = new JDBCcon();
			List List = new ArrayList();
			try {

				int i = -1;
				List.add(new IDropDownBean(i++, "请选择"));
				String sql = "select id, hetbh from hetb order by id";
				ResultSet rs = con.getResultSet(sql);
				while (rs.next()) {

					List.add(new IDropDownBean(i++, rs.getString("hetbh")));
				}
				rs.close();
			} catch (Exception e) {

				e.printStackTrace();
			} finally {

				con.Close();
			}
			((Visit) getPage().getVisit())
					.setProSelectionModel14(new IDropDownModel(List));
			return ((Visit) getPage().getVisit()).getProSelectionModel14();
		}
	/*public IDropDownBean getHetbhValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean14() == null) {
			((Visit) getPage().getVisit()).setDropDownBean14((IDropDownBean) getIHetbhModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean14();
	}

	public void setHetbhValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean14(Value);
	}

	public void setIHetbhModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel14(value);
	}

	public IPropertySelectionModel getIHetbhModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel14() == null) {
			getIHetbhModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel14();
	}

	public  IPropertySelectionModel getIHetbhModels() {
		String sql = "select id,hetbh\n" + "from hetb \n";
		((Visit) getPage().getVisit()).setProSelectionModel14(new IDropDownModel(sql, "全部"));
		return ((Visit) getPage().getVisit()).getProSelectionModel14();
	}	*/
	//****************************************************************************
	//********************结算单位（收款单位）********************************************************
	 public IDropDownBean getJiesdwValue() {
	    	if(((Visit)getPage().getVisit()).getDropDownBean10()==null){
	    		
	    		((Visit)getPage().getVisit()).setDropDownBean10((IDropDownBean)getIJiesdwModel().getOption(0));
	    	}
	       return ((Visit)getPage().getVisit()).getDropDownBean10();
	    }
	    
	    public void setJiesdwValue(IDropDownBean value)
	    {
	    	if(((Visit)getPage().getVisit()).getDropDownBean10()!=value){
	    		
	    		((Visit)getPage().getVisit()).setDropDownBean10(value);
	        }
	    }
	    
	    public void setIJiesdwModel(IPropertySelectionModel value){

	    	((Visit)getPage().getVisit()).setProSelectionModel10(value);
		}
	    
	    public IPropertySelectionModel getIJiesdwModel(){
	            
	    	if(((Visit)getPage().getVisit()).getProSelectionModel10()==null){
	        
	    		getIJiesdwModels();
	        }
	        return ((Visit)getPage().getVisit()).getProSelectionModel10();
	    }
	    
	    public IPropertySelectionModel getIJiesdwModels() {

			JDBCcon con = new JDBCcon();
			List List = new ArrayList();
			try {

				int i = -1;
				List.add(new IDropDownBean(i++, "请选择"));
				String sql = "select distinct quanc from shoukdw ";
				ResultSet rs = con.getResultSet(sql);
				while (rs.next()) {

					List.add(new IDropDownBean(i++, rs.getString("quanc")));
				}
				rs.close();
			} catch (Exception e) {

				e.printStackTrace();
			} finally {

				con.Close();
			}
			((Visit) getPage().getVisit())
					.setProSelectionModel10(new IDropDownModel(List));
			return ((Visit) getPage().getVisit()).getProSelectionModel10();
		}
	//****************************************************************************
	//********************发站********************************************************
	 public IDropDownBean getFazValue() {
	    	if(((Visit)getPage().getVisit()).getDropDownBean11()==null){
	    		
	    		((Visit)getPage().getVisit()).setDropDownBean11((IDropDownBean)getIFazModel().getOption(0));
	    	}
	       return ((Visit)getPage().getVisit()).getDropDownBean11();
	    }
	    
	    public void setFazValue(IDropDownBean value)
	    {
	    	if(((Visit)getPage().getVisit()).getDropDownBean11()!=value){
	    		
	    		((Visit)getPage().getVisit()).setDropDownBean11(value);
	        }
	    }
	    
	    public void setIFazModel(IPropertySelectionModel value){

	    	((Visit)getPage().getVisit()).setProSelectionModel11(value);
		}
	    
	    public IPropertySelectionModel getIFazModel(){
	            
	    	if(((Visit)getPage().getVisit()).getProSelectionModel11()==null){
	        
	    		getIFazModels();
	        }
	        return ((Visit)getPage().getVisit()).getProSelectionModel11();
	    }
	    
	    public IPropertySelectionModel getIFazModels() {

			JDBCcon con = new JDBCcon();
			List List = new ArrayList();
			try {

				int i = -1;
				List.add(new IDropDownBean(i++, "请选择"));
				String sql = "select distinct mingc from chezxxb";
				ResultSet rs = con.getResultSet(sql);
				while (rs.next()) {

					List.add(new IDropDownBean(i++, rs.getString("mingc")));
				}
				rs.close();
			} catch (Exception e) {

				e.printStackTrace();
			} finally {

				con.Close();
			}
			((Visit) getPage().getVisit())
					.setProSelectionModel11(new IDropDownModel(List));
			return ((Visit) getPage().getVisit()).getProSelectionModel11();
		}
	//****************************************************************************
	//********************煤炭品种********************************************************
	 public IDropDownBean getMeitpzValue() {
	    	if(((Visit)getPage().getVisit()).getDropDownBean12()==null){
	    		
	    		((Visit)getPage().getVisit()).setDropDownBean12((IDropDownBean)getIMeitpzModel().getOption(0));
	    	}
	       return ((Visit)getPage().getVisit()).getDropDownBean12();
	    }
	    
	    public void setMeitpzValue(IDropDownBean value)
	    {
	    	if(((Visit)getPage().getVisit()).getDropDownBean12()!=value){
	    		
	    		((Visit)getPage().getVisit()).setDropDownBean12(value);
	        }
	    }
	    
	    public void setIMeitpzModel(IPropertySelectionModel value){

	    	((Visit)getPage().getVisit()).setProSelectionModel12(value);
		}
	    
	    public IPropertySelectionModel getIMeitpzModel(){
	            
	    	if(((Visit)getPage().getVisit()).getProSelectionModel12()==null){
	        
	    		getIMeitpzModels();
	        }
	        return ((Visit)getPage().getVisit()).getProSelectionModel12();
	    }
	    
	    public IPropertySelectionModel getIMeitpzModels() {

			JDBCcon con = new JDBCcon();
			List List = new ArrayList();
			try {

				int i = -1;
				List.add(new IDropDownBean(i++, "请选择"));
				String sql = "select distinct mingc from pinzb";
				ResultSet rs = con.getResultSet(sql);
				while (rs.next()) {

					List.add(new IDropDownBean(i++, rs.getString("mingc")));
				}
				rs.close();
			} catch (Exception e) {

				e.printStackTrace();
			} finally {

				con.Close();
			}
			((Visit) getPage().getVisit())
					.setProSelectionModel12(new IDropDownModel(List));
			return ((Visit) getPage().getVisit()).getProSelectionModel12();
		}
	//****************************************************************************
	//********************供货单位********************************************************
	 public IDropDownBean getGonghdwValue() {
	    	if(((Visit)getPage().getVisit()).getDropDownBean2()==null){
	    		
	    		((Visit)getPage().getVisit()).setDropDownBean2((IDropDownBean)getIGonghdwModel().getOption(0));
	    	}
	       return ((Visit)getPage().getVisit()).getDropDownBean2();
	    }
	    
	    public void setGonghdwValue(IDropDownBean value)
	    {
	    	if(((Visit)getPage().getVisit()).getDropDownBean2()!=value){
	    		
	    		((Visit)getPage().getVisit()).setDropDownBean2(value);
	        }
	    }
	    
	    public void setIGonghdwModel(IPropertySelectionModel value){

	    	((Visit)getPage().getVisit()).setProSelectionModel2(value);
		}
	    
	    public IPropertySelectionModel getIGonghdwModel(){
	            
	    	if(((Visit)getPage().getVisit()).getProSelectionModel2()==null){
	        
	    		getIGonghdwModels();
	        }
	        return ((Visit)getPage().getVisit()).getProSelectionModel2();
	    }
	    
	    public IPropertySelectionModel getIGonghdwModels() {

			JDBCcon con = new JDBCcon();
			List List = new ArrayList();
			try {

				int i = -1;
				List.add(new IDropDownBean(i++, "请选择"));
				String sql = "select  mingc from gongysb order by id";
				ResultSet rs = con.getResultSet(sql);
				while (rs.next()) {

					List.add(new IDropDownBean(i++, rs.getString("mingc")));
				}
				rs.close();
			} catch (Exception e) {

				e.printStackTrace();
			} finally {

				con.Close();
			}
			((Visit) getPage().getVisit())
					.setProSelectionModel2(new IDropDownModel(List));
			return ((Visit) getPage().getVisit()).getProSelectionModel2();
		}
	//****************************************************************************
//	  ********************运输方式********************************************************
	 public IDropDownBean getYunsfsValue() {
	    	if(((Visit)getPage().getVisit()).getDropDownBean3()==null){
	    		
	    		((Visit)getPage().getVisit()).setDropDownBean3((IDropDownBean)getIYunsfsModel().getOption(0));
	    	}
	       return ((Visit)getPage().getVisit()).getDropDownBean3();
	    }
	    
	    public void setYunsfsValue(IDropDownBean value)
	    {
	    	if(((Visit)getPage().getVisit()).getDropDownBean3()!=value){
	    		
	    		((Visit)getPage().getVisit()).setDropDownBean3(value);
	        }
	    }
	    
	    public void setIYunsfsModel(IPropertySelectionModel value){

	    	((Visit)getPage().getVisit()).setProSelectionModel3(value);
		}
	    
	    public IPropertySelectionModel getIYunsfsModel(){
	            
	    	if(((Visit)getPage().getVisit()).getProSelectionModel3()==null){
	        
	    		getIYunsfsModels();
	        }
	        return ((Visit)getPage().getVisit()).getProSelectionModel3();
	    }
	    
	    public IPropertySelectionModel getIYunsfsModels() {

			JDBCcon con = new JDBCcon();
			List List = new ArrayList();
			try {

				int i = -1;
				List.add(new IDropDownBean(i++, "请选择"));
				String sql = "select mingc from yunsfsb order by id";
				ResultSet rs = con.getResultSet(sql);
				while (rs.next()) {

					List.add(new IDropDownBean(i++, rs.getString("mingc")));
				}
				rs.close();
			} catch (Exception e) {

				e.printStackTrace();
			} finally {

				con.Close();
			}
			((Visit) getPage().getVisit())
					.setProSelectionModel3(new IDropDownModel(List));
			return ((Visit) getPage().getVisit()).getProSelectionModel3();
		}
	//****************************************************************************
	//		  ********************计划口径********************************************************
		 public IDropDownBean getJihkjValue() {
		    	if(((Visit)getPage().getVisit()).getDropDownBean4()==null){
		    		
		    		((Visit)getPage().getVisit()).setDropDownBean4((IDropDownBean)getIJihkjModel().getOption(0));
		    	}
		       return ((Visit)getPage().getVisit()).getDropDownBean4();
		    }
		    
		    public void setJihkjValue(IDropDownBean value)
		    {
		    	if(((Visit)getPage().getVisit()).getDropDownBean4()!=value){
		    		
		    		((Visit)getPage().getVisit()).setDropDownBean4(value);
		        }
		    }
		    
		    public void setIJihkjModel(IPropertySelectionModel value){
	
		    	((Visit)getPage().getVisit()).setProSelectionModel4(value);
			}
		    
		    public IPropertySelectionModel getIJihkjModel(){
		            
		    	if(((Visit)getPage().getVisit()).getProSelectionModel4()==null){
		        
		    		getIJihkjModels();
		        }
		        return ((Visit)getPage().getVisit()).getProSelectionModel4();
		    }
		    
		    public IPropertySelectionModel getIJihkjModels() {
	
				JDBCcon con = new JDBCcon();
				List List = new ArrayList();
				try {
	
					int i = -1;
					List.add(new IDropDownBean(i++, "请选择"));
					String sql = "select distinct mingc from jihkjb";
					ResultSet rs = con.getResultSet(sql);
					while (rs.next()) {
	
						List.add(new IDropDownBean(i++, rs.getString("mingc")));
					}
					rs.close();
				} catch (Exception e) {
	
					e.printStackTrace();
				} finally {
	
					con.Close();
				}
				((Visit) getPage().getVisit())
						.setProSelectionModel4(new IDropDownModel(List));
				return ((Visit) getPage().getVisit()).getProSelectionModel4();
			}
		//****************************************************************************
    
//   结算编号 DropDownBean6
//   结算编号 ProSelectionModel6
    
    public void setIJiesbhModel(IPropertySelectionModel value){
        
        ((Visit)getPage().getVisit()).setProSelectionModel6(value);
    }
    
    public IPropertySelectionModel getIJiesbhModel(){
    	if(((Visit)getPage().getVisit()).getProSelectionModel6()==null){
    		getIJiesbhModels();
        }
        return ((Visit)getPage().getVisit()).getProSelectionModel6();
    }
    
    public IPropertySelectionModel getIJiesbhModels(){
    	
    	String sql = "";
    	JDBCcon con=new JDBCcon();
    	List List=new ArrayList();
    	try{
    		int i=-1;
    		List.add(new IDropDownBean(i++, "请选择"));
    		if(((Visit)getPage().getVisit()).getRenyjb()==3){
    			
    			sql=" select bianm from "
    				+ " (select distinct bianm from jiesb where diancxxb_id="+((Visit)getPage().getVisit()).getDiancxxb_id()+" and liucztb_id=0)"
    				+ " union"
    				+ " (select distinct bianm from jiesyfb where diancxxb_id="+((Visit)getPage().getVisit()).getDiancxxb_id()+" and liucztb_id=0) order by bianm";
    		}
    		
    		ResultSet rs=con.getResultSet(sql);
    		while(rs.next()){
    			
    			List.add(new IDropDownBean(i++, rs.getString("bianm")));
    		}
    		rs.close();
    	}catch(Exception e){
    		
    		e.printStackTrace();
    	}finally{
    		
    		con.Close();
    	}
    	
    	((Visit)getPage().getVisit()).setProSelectionModel6(new IDropDownModel(List));
		return ((Visit)getPage().getVisit()).getProSelectionModel6();
    }
    
    public IDropDownBean getJiesbhValue() {
    	
    	if(((Visit)getPage().getVisit()).getDropDownBean6()==null){
    		
    		((Visit)getPage().getVisit()).setDropDownBean6((IDropDownBean)getIJiesbhModel().getOption(0));
    	}
    	
		return ((Visit)getPage().getVisit()).getDropDownBean6();
	}
	
    public void setJiesbhValue(IDropDownBean value) {
		
		if(((Visit) getPage().getVisit()).getDropDownBean6()!=value){
			
			((Visit) getPage().getVisit()).setDropDownBean6(value);
		}
	}
//    end
    
	
//	流程名称 DropDownBean8
//  流程名称 ProSelectionModel8
	public IDropDownBean getLiucmcValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean8() == null) {
			((Visit) getPage().getVisit()).setDropDownBean8((IDropDownBean) getILiucmcModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean8();
	}

	public void setLiucmcValue(IDropDownBean value) {
		
		if(((Visit) getPage().getVisit()).getDropDownBean8()!=value){
			
			((Visit) getPage().getVisit()).setDropDownBean8(value);
		}
	}

	public void setILiucmcModel(IPropertySelectionModel value) {
		
		((Visit) getPage().getVisit()).setProSelectionModel8(value);
	}

	public IPropertySelectionModel getILiucmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel8() == null) {
			
			getILiucmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel8();
	}

	public IPropertySelectionModel getILiucmcModels() {
		
		String sql="";
		sql="select liucb.id,liucb.mingc from liucb,liuclbb where liucb.liuclbb_id=liuclbb.id and liuclbb.mingc='结算' order by mingc";

		((Visit) getPage().getVisit()).setProSelectionModel8(new IDropDownModel(sql,"请选择"));
		return ((Visit) getPage().getVisit()).getProSelectionModel8();
	}
//   流程名称 end
	
//***************************************************************************//
	public boolean getRaw() {
		return true;
	}

//	格式化
	public String format(double dblValue,String strFormat){
		 DecimalFormat df = new DecimalFormat(strFormat);
		 return formatq(df.format(dblValue));
		 
	}
	
	public String formatq(String strValue){//加千位分隔符
		String strtmp="",xiaostmp="",tmp="";
		int i=3;
		if(strValue.lastIndexOf(".")==-1){
			
			strtmp=strValue;
			if(strValue.equals("")){
				
				xiaostmp="";
			}else{
				
				xiaostmp=".00";
			}
			
		}else {
			
			strtmp=strValue.substring(0,strValue.lastIndexOf("."));
			
			if(strValue.substring(strValue.lastIndexOf(".")).length()==2){
				
				xiaostmp=strValue.substring(strValue.lastIndexOf("."))+"0";
			}else{
				
				xiaostmp=strValue.substring(strValue.lastIndexOf("."));
			}
			
		}
		tmp=strtmp;
		
		while(i<tmp.length()){
			strtmp=strtmp.substring(0,strtmp.length()-(i+(i-3)/3))+","+strtmp.substring(strtmp.length()-(i+(i-3)/3),strtmp.length());
			i=i+3;
		}
		
		return strtmp+xiaostmp;
	}
	
public IPropertySelectionModel getIFahdwModels(){
    	
		JDBCcon con=new JDBCcon();
		List List=new ArrayList();
    	String sql="";
    	try{
    		
    		int i=-1;
    		List.add(new IDropDownBean(i++, "请选择"));
    		if(((Visit)getPage().getVisit()).getRenyjb()==3){
        		
        		sql=" select gongysmc from "
    				+ " (select distinct gongysmc from jiesb where diancxxb_id="+((Visit)getPage().getVisit()).getDiancxxb_id()+" and liucztb_id=0)"
    				+ " union"
    				+ " (select distinct gongysmc from jiesyfb where diancxxb_id="+((Visit)getPage().getVisit()).getDiancxxb_id()+" and liucztb_id=0) order by gongysmc";
        	
        	}else if(((Visit)getPage().getVisit()).getRenyjb()==2){
        		
        		sql=" select gongysmc from "
    				+ " (select distinct gongysmc from diancjsmkb,diancxxb where diancjsmkb.diancxxb_id=diancxxb.id and diancxxb.fuid="+((Visit)getPage().getVisit()).getDiancxxb_id()+" and liucztb_id=0)"
    				+ " union"
    				+ " (select distinct gongysmc from diancjsyfb,diancxxb where diancjsyfb.diancxxb_id=diancxxb.id and diancxxb.fuid="+((Visit)getPage().getVisit()).getDiancxxb_id()+" and liucztb_id=0) order by gongysmc";
        		
        	}else if(((Visit)getPage().getVisit()).getRenyjb()==1){
        		
        		sql="select gongysmc from "
    				+ " (select distinct gongysmc from diancjsmkb where liucztb_id=0)"
    				+ " union"
    				+ " (select distinct gongysmc from diancjsyfb where liucztb_id=0) order by gongysmc";
        	}
    		
    		ResultSet rs=con.getResultSet(sql);
    		while(rs.next()){	
    			
    			List.add(new IDropDownBean(i++, rs.getString("gongysmc")));
    		}
    		rs.close();
    	}catch(Exception e){
    		
    		e.printStackTrace();
    	}finally{
    		
    		con.Close();
    	}
    	((Visit)getPage().getVisit()).setProSelectionModel2(new IDropDownModel(List));
        return ((Visit)getPage().getVisit()).getProSelectionModel2();
    }
	

    //********************************************************************************//
    
	private String FormatDate(Date _date) {
		if (_date == null) {
			return DateUtil.Formatdate("yyyy-MM-dd", new Date());
		}
		return DateUtil.Formatdate("yyyy-MM-dd", _date);
	}

	private String getProperValue(IPropertySelectionModel _selectModel,
			int value) {
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
	protected String getpageLinks() {
		String PageLink = "";
		IRequestCycle cycle = this.getRequestCycle();
		if (cycle.isRewinding())
			return "";
		String _servername = cycle.getRequestContext().getRequest()
				.getServerName();
		String _scheme = cycle.getRequestContext().getRequest().getScheme();
		int _ServerPort = cycle.getRequestContext().getRequest()
				.getServerPort();
		if (_ServerPort != 80) {
			PageLink = _scheme + "://" + _servername + ":" + _ServerPort
					+ this.getEngine().getContextPath();
		} else {
			PageLink = _scheme + "://" + _servername
					+ this.getEngine().getContextPath();
		}
		return PageLink;
	}
}
