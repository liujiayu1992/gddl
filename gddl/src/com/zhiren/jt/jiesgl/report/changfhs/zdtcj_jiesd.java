package com.zhiren.jt.jiesgl.report.changfhs;

import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;

public class zdtcj_jiesd extends BasePage {
	 //	页面判定方法
    public void pageValidate(PageEvent arg0) {
    	String PageName = arg0.getPage().getPageName();
    	String ValPageName = Login.ValidateLogin(arg0.getPage());
    	if (!PageName.equals(ValPageName)) {
    		ValPageName = Login.ValidateAdmin(arg0.getPage());
    		if(!PageName.equals(ValPageName)) {
    			IPage ipage = arg0.getRequestCycle().getPage(ValPageName);
        		throw new PageRedirectException(ipage);
    		}
		}
	}
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

//	日期1
	public Date getRiq1() {
		if (((Visit) this.getPage().getVisit()).getDate1() == null) {
			
			((Visit) this.getPage().getVisit()).setDate1(new Date());
		}
		return ((Visit) this.getPage().getVisit()).getDate1();
	}

	public void setRiq1(Date riq1) {

		if (((Visit) this.getPage().getVisit()).getDate1() != null 
				&& ((Visit) this.getPage().getVisit()).getDate1()!=riq1) {
			
			((Visit) this.getPage().getVisit()).setDate1(riq1);
			((Visit) this.getPage().getVisit()).setboolean1(true);
		}
	}
	
//	日期2
	public Date getRiq2() {
		if (((Visit) this.getPage().getVisit()).getDate2() == null) {
			
			((Visit) this.getPage().getVisit()).setDate2(new Date());
		}
		return ((Visit) this.getPage().getVisit()).getDate2();
	}

	public void setRiq2(Date riq2) {

		if (((Visit) this.getPage().getVisit()).getDate2() != null) {
			
			((Visit) this.getPage().getVisit()).setDate2(riq2);
			((Visit) this.getPage().getVisit()).setboolean1(true);
		}
	}
	
	public boolean getRaw() {
		return true;
	}
	
	private int _CurrentPage = -1;
	
	public int getCurrentPage() {
		return _CurrentPage;
	}
	
	public void setCurrentPage(int _value) {
		_CurrentPage= _value;
	}
	
	private int _AllPages = -1;
	
	public int getAllPages() {
		return _AllPages;
	}
	
	public void setAllPages(int _value) {
		_AllPages= _value;
	}
	
	private String mstrReportName="";
	
	//得到登陆人员所属电厂或分公司的名称
	public String getDiancmc(){
		String diancmc="";
		JDBCcon cn = new JDBCcon();
		long diancid=((Visit) getPage().getVisit()).getDiancxxb_id();
		String sql_diancmc="select d.quanc from diancxxb d where d.id="+diancid;
		ResultSet rs=cn.getResultSet(sql_diancmc);
		try {
			while(rs.next()){
				 diancmc=rs.getString("quanc");
			}
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		cn.Close();
		return diancmc;
		
	}
	private boolean isBegin=false;
	
	
	public String getPrintTable(){
		
		if(! isBegin){
			return "";
		}
		
		return getJiestjquery();
	}

	public String getJiestjquery(){
		
		JDBCcon cn = new JDBCcon();
		String strsql="";
		String strsql2="";
		int butgys=0;
		String mtianzdw="";
		String shoukdw="";
		String fahrq="";
		String fahdw="";
		String meikdqbm="";
		String jiesrq="";
		String pinz="";
		String xianshr = "";
		String zhangh = "";
		String gongfsl = "";
		String ches = "";
		String yansbh = "";
		String fapbh = "";
		String duifdd ="";
		String fukfs = "";
		String shulzjbz = "";
		String gongfrl = "";
		String yansrl = "";
		String yingkrl = "";
		String relzjbz = "";
		String relzjje = "";
		String gongfl = "";
		String yansl ="";
		String yingkl = "";
		String liuzjbz = "";
		String liuzjje ="";
		String yanssl = "";
		String yingk = "";
		String shulzjje = "";
		String jiessl = "";
		String buhsdj = "";
		String jiakje = "";
		String bukyqjk = "";
		String jiakhj = "";
		String jiaksl = "";
		String jiaksk = "";
		String jiasje = "";
		String tielyf = "";
		String zaf = "";
		String bukyqyzf = "";
		String jiskc = "";
		String buhsyf = "";
		String yunfsl = "";
		String yunfsk = "";
		String yunzfhj = "";
		String jiesbh = "";
		String beiz = "";
		String hej = "";
		String hejdx = "";
		String guohl = "";
		String jiesrcrl = "";
		String jiesrclf = "";
		String faz="";
		String daibcc="";
		String yuanshr="";
		String kaihyh="";
		String jiesysrq="";
		
		Report rt=new Report();
		try{
			
			ResultSet rs=null;
			strsql="select butgys,diancxxb.quanc from jiesb,diancxxb where jiesb.diancxxb_id=diancxxb.id and jiesbh='"+this.getJiesbhValue().getValue()+"'";
			rs=cn.getResultSet(strsql);
			while(rs.next()){
				
				butgys=rs.getInt("butgys");
				mtianzdw=rs.getString("tianzdw");
			}
			
			if(butgys==1){
				
				strsql = " select jiesb.id,m.meikdwqc as fah,md.meikdqbm,decode(c.jianc,null,' ',c.jianc)  as faz,daibcc,shoukdw,decode(fahksrq,fahjzrq,to_char(fahksrq,'yyyy-mm-dd'),to_char(fahksrq,'yyyy-mm-dd')||'至'||to_char(fahjzrq,'yyyy-mm-dd')) as fahrq,to_char(jiesb.jiesrq,'yyyy')||'年'||to_char(jiesb.jiesrq,'mm')||'月'||to_char(jiesb.jiesrq,'dd')||'日' as jiesrq,"
	          		+ " yuanshr,kaihyh,decode(kaisysrq,jiesysrq,to_char(kaisysrq,'yyyy-mm-dd'),to_char(kaisysrq,'yyyy-mm-dd')||'至'||to_char(jiesysrq,'yyyy-mm-dd')) as jiesysrq,pinz,xianshr,zhangh,gongfsl,ches,yansbh,fapbh,duifdd,fukfs,shulzjbz,gongfrl,yansrl,yingkrl,relzjbz,relzjje,gongfl,yansl,decode(jiesb.gongfl,0,0,yansl-gongfl) as yingkl,liuzjbz,liuzjje,gongfsl,yanssl,yingk,shulzjje,"
	          		+ " jiessl,buhsdj,jiakje,bukyqjk,jiakhj,jiaksl,jiaksk,jiasje,tielyf,zaf,bukyqyzf,jiskc,  buhsyf, yunfsl, yunfsk, yunzfhj,jiesbh,"
	 				+ " decode(kuidjf,0,jiesb.beiz,jiesb.beiz) as beiz,hej,hejdx,guohl,jiesrcrl,jiesrclf from jiesb,meikxxb m,meikdqb md,chezxxb c"
	 				+ " where jiesb.meikxxb_id=m.id and m.meikdqb_id=md.id and jiesb.chezxxb_id=c.id(+) ";
	 	
				strsql=strsql+ " and jiesrq>=to_date('"+this.getRiq1()+"','yyyy-mm-dd') and jiesrq<=to_date('"+this.getRiq2()+"','yyyy-mm-dd') and jiesbh='"+this.getJiesbhValue().getValue()+"'";
				
				strsql2="select jiesb.id,m.meikdwqc as fah,md.meikdqbm,decode(c.jianc, null, ' ', c.jianc) as faz,daibcc,decode(fahksrq,"
	 				+ " fahjzrq,to_char(fahksrq, 'yyyy-mm-dd'),to_char(fahksrq, 'yyyy-mm-dd') || '至' ||to_char(fahjzrq, 'yyyy-mm-dd')) as fahrq,"
	 				+ " to_char(jiesb.jiesrq,'yyyy')||'年'||to_char(jiesb.jiesrq,'mm')||'月'||to_char(jiesb.jiesrq,'dd')||'日' as jiesrq,"
	 				+ " jiesb.qiyfsk as yunfsk,jiesb.qiyfjk as buhsyf,DAOZQZF,0 as bukyqyzf,jiesb.qiyfjskc as jiskc,jiesb.qiyfhj-jiesb.daozqzf as TIELYF,jiesb.qiyfhj as yunzfhj,jiesb.gongfsl as guohl2,"
	 				+ " jiesb.jiessl as jiessl,jiesb.yunfsl,jiesb.hejdx2 as hejdx,0 as QIYFDJ,guohl,"
	 				+ " shoukdw2,JIESBH2,jiesb.yuanshr,jiesb.xianshr,jiesb.kaihyh2,decode(kaisysrq,jiesysrq,to_char(kaisysrq,'yyyy-mm-dd'),"
	 				+ " to_char(kaisysrq,'yyyy-mm-dd')||'至'||to_char(jiesysrq,'yyyy-mm-dd')) as jiesysrq,jiesb.zhangh2,jiesb.ches,jiesb.yansbh2,"
	 				+ " jiesb.fapbh2,jiesb.duifdd2,jiesb.fukfs2,decode(kuidjf,0,jiesb.beiz2,jiesb.beiz||'   亏吨拒运费：'||kuidjf) as beiz,jiesrcrl,jiesrclf "
	 				+ " from jiesb, meikxxb m, meikdqb md, chezxxb c "
	 				+ " where jiesb.meikxxb_id = m.id and m.meikdqb_id = md.id "
	 				+ " and jiesb.chezxxb_id = c.id(+) ";
	 				
				strsql2=strsql2 + " and jiesb.jiesbh2='"+this.getJiesbhValue().getValue()+"-1'";
	 			
			}else{
				
				strsql=" select jiesb.id,m.meikdwqc as fah,md.meikdqbm,decode(c.jianc,null,' ',c.jianc)  as faz,decode(fahksrq,fahjzrq,to_char(fahksrq,'yyyy-mm-dd'),to_char(fahksrq,'yyyy-mm-dd')||'至'||to_char(fahjzrq,'yyyy-mm-dd')) as fahrq,to_char(jiesb.jiesrq,'yyyy')||'年'||to_char(jiesb.jiesrq,'mm')||'月'||to_char(jiesb.jiesrq,'dd')||'日' as jiesrq,"
					+ " decode(kuidjf,0,jiesb.beiz,jiesb.beiz||'   亏吨拒运费：'||kuidjf) as beiz,decode(jiesb.gongfl,0,0,yansl-gongfl) as yingkl,jiesb.* from jiesb ,meikxxb m,meikdqb md,chezxxb c"
					+ " where jiesb.meikxxb_id=m.id and m.meikdqb_id=md.id and jiesb.chezxxb_id=c.id(+) ";
			
				strsql=strsql+ " and jiesrq>=to_date('"+this.getRiq1()+"','yyyy-mm-dd') and jiesrq<=to_date('"+this.getRiq2()+"','yyyy-mm-dd') ";
			
				strsql=strsql+" and jiesbh='"+strsql+"'";
			            
				
				strsql2="select jiesb.id,m.meikdwqc as fah,md.meikdqbm,decode(c.jianc, null, ' ', c.jianc) as faz,daibcc,decode(fahksrq,"
					+ " fahjzrq,to_char(fahksrq, 'yyyy-mm-dd'),to_char(fahksrq, 'yyyy-mm-dd') || '至' ||to_char(fahjzrq, 'yyyy-mm-dd')) as fahrq,"
					+ " to_char(jiesb.jiesrq,'yyyy')||'年'||to_char(jiesb.jiesrq,'mm')||'月'||to_char(jiesb.jiesrq,'dd')||'日' as jiesrq,"
					+ " jiesb.yunfsk,jiesb.buhsyf,jiesb.tielyf,jiesb.yunzfhj,0 as daozqzf,bukyqyzf,jiskc,"
					+ " jiesb.guohl,jiesb.guohl as guohl2,jiesb.jiessl as jiessl,jiesb.yunfsl,jiesb.hejdx2 as hejdx,QIYFDJ,"
					+ " shoukdw2,JIESBH2,jiesb.yuanshr,jiesb.xianshr,jiesb.kaihyh2,jiesb.jiesysrq,jiesb.zhangh2,jiesb.ches,jiesb.yansbh2,jiesb.fapbh2,jiesb.duifdd2,jiesb.fukfs2"
					+ " from jiesb, meikxxb m, meikdqb md, chezxxb c "
					+ " where jiesb.meikxxb_id = m.id and m.meikdqb_id = md.id "
					+ " and jiesb.chezxxb_id = c.id(+) ";
				
				strsql2=strsql2 + " and jiesb.jiesbh2='"+this.getJiesbhValue().getValue()+"-1'";
			}
			
			
			rs=cn.getResultSet(strsql);
			while(rs.next()){
				
				fahdw=rs.getString("fah");
				meikdqbm=rs.getString("meikdqbm");
				fahrq=rs.getString("fahrq");
				jiesrq=rs.getString("jiesrq");
				jiesysrq=rs.getString("jiesysrq");
				pinz=rs.getString("pinz");
				faz=rs.getString("faz");
				shoukdw=rs.getString("shoukdw");
				yuanshr=rs.getString("yuanshr");
				kaihyh=rs.getString("kaihyh");
				daibcc=rs.getString("daibcc");
				xianshr = rs.getString("xianshr");
				zhangh = rs.getString("zhangh");
				gongfsl = rs.getString("gongfsl");
				ches = rs.getString("ches");
				yansbh = rs.getString("yansbh");
				fapbh = rs.getString("fapbh");
				duifdd = rs.getString("duifdd");
				fukfs = rs.getString("fukfs");
				shulzjbz = rs.getString("shulzjbz");
				gongfrl = rs.getString("gongfrl");
				yansrl = rs.getString("yansrl");
				yingkrl = rs.getString("yingkrl");
				relzjje = rs.getString("relzjje");
				gongfl = rs.getString("gongfl");
				yansl = rs.getString("yansl");
				yingkl = rs.getString("yingkl");
				liuzjbz = rs.getString("liuzjbz");
				liuzjje = rs.getString("liuzjje");
				yanssl = rs.getString("yanssl");
				yingk = rs.getString("yingk");
				shulzjje = rs.getString("shulzjje");
				jiessl = rs.getString("jiessl");
				buhsdj = rs.getString("buhsdj");
				jiakje = rs.getString("jiakje");
				bukyqjk = rs.getString("bukyqjk");
				jiakhj = rs.getString("jiakhj");
				jiaksl = rs.getString("jiaksl");
				jiaksk = rs.getString("jiaksk");
				tielyf = rs.getString("tielyf");
				zaf = rs.getString("zaf");
				bukyqyzf = rs.getString("bukyqyzf");
				jiskc = rs.getString("jiskc");
				buhsyf = rs.getString("buhsyf");
				yunfsl = rs.getString("yunfsl");
				yunfsk = rs.getString("yunfsk");
				yunzfhj = rs.getString("yunzfhj");
				jiesbh = rs.getString("jiesbh");
				beiz = rs.getString("beiz");
				hej = rs.getString("hej");
				hejdx = rs.getString("hejdx");
				guohl = rs.getString("guohl");
				jiesrcrl = rs.getString("jiesrcrl");
				jiesrclf = rs.getString("jiesrclf");
			}
			
			
			 String ArrHeader[][]=new String[19][13];
			 ArrHeader[0]=new String[] {"填制单位："+mtianzdw+"","填制单位：江西大唐国际新余发电有限责任公司","填制单位：江西大唐国际新余发电有限责任公司","填制单位：江西大唐国际新余发电有限责任公司","",""+jiesrq+"","2008年04月02日","","","","编号：2008040810","编号：2008040810",""};
			 ArrHeader[1]=new String[] {"填制单位：江西大唐国际新余发电有限责任公司","填制单位：江西大唐国际新余发电有限责任公司","填制单位：江西大唐国际新余发电有限责任公司","填制单位：江西大唐国际新余发电有限责任公司","","","","","","","编号：2008040810","编号：2008040810",""};
			 ArrHeader[2]=new String[] {"供货单位："+fahdw+"","供货单位：新余市卓成矿业有限公司","供货单位：新余市卓成矿业有限公司","发站："+faz+"","发站：","代表车号："+daibcc+"","代表车号：","代表车号：","收款单位："+shoukdw+"","收款单位：","收款单位：","收款单位：",""};
			 ArrHeader[3]=new String[] {"发货日期："+fahrq+"","发货日期：2008-03-31","发货日期：2008-03-31","地区代码："+meikdqbm+"","地区代码：213608","原收货人："+yuanshr+"","原收货人：","原收货人：","开户银行："+kaihyh+"","开户银行：","开户银行：","开户银行：",""};
			 ArrHeader[4]=new String[] {"验收日期："+jiesysrq+"","验收日期：2008-3-31","验收日期：2008-3-31","货物名称："+pinz+"","货物名称：混煤","现收货人："+xianshr+"","现收货人：","现收货人：","银行帐号："+zhangh+"","银行帐号：","银行帐号：","银行帐号：",""};
			 ArrHeader[5]=new String[] {"发运数量："+gongfsl+"      车数："+ches+"","发运数量：2452.52      车数：","发运数量：2452.52      车数：","验收编号："+yansbh+"","验收编号：","发票编号："+fapbh+"","发票编号：","发票编号：","兑付地点："+duifdd+"","兑付地点：","付款方式："+fukfs+"","付款方式：","第一联   燃管部门留存"};
			 ArrHeader[6]=new String[] {"质量验收","质量验收","质量验收","质量验收","质量验收","质量验收","质量验收","质量验收","数量验收","数量验收","数量验收","数量验收","第一联   燃管部门留存"};
			 ArrHeader[7]=new String[] {"实际付款："+shulzjbz+"元/吨","供方标准","厂方化验","结算热值","相差数量","折价标准","折合金额","折合金额","供方数量","验收数量","盈亏数量","折合金额","第一联   燃管部门留存"};
			 ArrHeader[8]=new String[] {"热量(大卡/千克)",""+gongfrl+"",""+jiesrcrl+"",""+yansrl+"",""+yingkrl+"",""+relzjbz+"",""+relzjje+""," ","(吨)","(吨)","(吨)","(元)","第一联   燃管部门留存"};
			 ArrHeader[9]=new String[] {"硫分(%)",""+gongfl+"",""+jiesrclf+"",""+yansl+"",""+yingkl+"",""+liuzjbz+"",""+liuzjje+""," ",""+gongfsl+"",""+yanssl+"",""+yingk+"",""+shulzjje+"","第一联   燃管部门留存"};
			 ArrHeader[10]=new String[] {"结算数量","单价","金额","金额","补(扣)以前价款","补(扣)以前价款","价款合计","价款合计","税率","税款","价税合计","价税合计","第一联   燃管部门留存"};
			 ArrHeader[11]=new String[] {""+jiessl+"",""+buhsdj+"",""+jiakje+"","670818.48",""+bukyqjk+""," ",""+jiakhj+"","670818.48",""+jiaksl+"",""+jiaksk+"",""+jiasje+"","758024.88","第一联   燃管部门留存"};
			 ArrHeader[12]=new String[] {"铁路运费","杂费","补(扣)以前运杂费","补(扣)以前运杂费","计税扣除","计税扣除","不含税运费","不含税运费","税率","税款","运杂费合计","运杂费合计","第一联   燃管部门留存"};
			 ArrHeader[13]=new String[] {""+tielyf+"",""+zaf+"",""+bukyqyzf+""," ",""+jiskc+""," ",""+buhsyf+"","57021.09",""+yunfsl+"",""+yunfsk+"",""+yunzfhj+""," ","第一联   燃管部门留存"};
			 ArrHeader[14]=new String[] {"合计(大写)：","合计(大写)：","合计(大写)：","合计(大写)：","合计(大写)：","合计(大写)：","合计(大写)：","合计(大写)：","合计(大写)：","合计(小写)：819337.88","合计(小写)：819337.88","合计(小写)：819337.88","第一联   燃管部门留存"};
			 ArrHeader[15]=new String[] {"备注：   亏吨拒运费：","备注：   亏吨拒运费：","备注：   亏吨拒运费：","备注：   亏吨拒运费：","备注：   亏吨拒运费：","备注：   亏吨拒运费：","备注：   亏吨拒运费：","备注：   亏吨拒运费：","备注：   亏吨拒运费：","过衡重量："," "," ","第一联   燃管部门留存"};
			 ArrHeader[16]=new String[] {"","","主管厂级领导：","主管厂级领导：","主管厂级领导：","电厂财务部门(盖章):","电厂财务部门(盖章):","电厂财务部门(盖章):","电厂财务部门(盖章):","电厂燃料部门(盖章):","电厂燃料部门(盖章):","电厂燃料部门(盖章):",""};
			 ArrHeader[17]=new String[] {"","","","","","电厂财务部门(盖章):","电厂财务部门(盖章):","电厂财务部门(盖章):","电厂财务部门(盖章):","电厂燃料部门(盖章):","电厂燃料部门(盖章):","电厂燃料部门(盖章):",""};
			 ArrHeader[18]=new String[] {"年   月   日","","年   月   日","","","年   月   日","","","","年   月   日","","",""};
	
			 int ArrWidth[]=new int[] {48,48,48,48,48,48,48,48,48,48,48,48};
	
	//		 定义页Title
			 rt.setTitle("燃  料  结  算  单",ArrWidth);
			 rt.setDefaultTitleLeft("填制单位：",1);
			 rt.setDefaultTitleLeft("日期",1);
			 rt.setDefaultTitleRight("编号：",1);
			 rt.setBody(new Table(ArrHeader,0,0,0));
	//		 合并单元格
			 rt.body.mergeCell(1,1,3,4);
			 rt.body.mergeCell(1,6,1,7);
			 rt.body.mergeCell(1,9,3,12);
			 rt.body.mergeCell(1,11,3,12);
			 rt.body.mergeCell(4,1,4,3);
			 rt.body.mergeCell(4,4,4,5);
			 rt.body.mergeCell(4,6,4,8);
			 rt.body.mergeCell(4,9,4,12);
			 rt.body.mergeCell(5,1,5,3);
			 rt.body.mergeCell(5,4,5,5);
			 rt.body.mergeCell(5,6,5,8);
			 rt.body.mergeCell(5,9,5,12);
			 rt.body.mergeCell(6,1,6,3);
			 rt.body.mergeCell(6,4,6,5);
			 rt.body.mergeCell(6,6,6,8);
			 rt.body.mergeCell(6,9,6,10);
			 rt.body.mergeCell(6,11,6,12);
			 rt.body.mergeCell(7,1,7,8);
			 rt.body.mergeCell(7,9,7,12);
			 rt.body.mergeCell(8,1,8,1);
			 rt.body.mergeCell(8,2,8,2);
			 rt.body.mergeCell(8,3,8,3);
			 rt.body.mergeCell(8,6,8,6);
			 rt.body.mergeCell(8,7,8,8);
			 rt.body.mergeCell(8,11,8,11);
			 rt.body.mergeCell(9,1,9,1);
			 rt.body.mergeCell(9,2,9,2);
			 rt.body.mergeCell(9,3,9,3);
			 rt.body.mergeCell(9,6,9,6);
			 rt.body.mergeCell(9,7,9,8);
			 rt.body.mergeCell(9,11,9,11);
			 rt.body.mergeCell(10,1,10,1);
			 rt.body.mergeCell(10,2,10,2);
			 rt.body.mergeCell(10,3,10,3);
			 rt.body.mergeCell(10,6,10,6);
			 rt.body.mergeCell(10,7,10,8);
			 rt.body.mergeCell(10,11,10,11);
			 rt.body.mergeCell(11,1,11,1);
			 rt.body.mergeCell(11,2,11,2);
			 rt.body.mergeCell(11,3,11,4);
			 rt.body.mergeCell(11,5,11,6);
			 rt.body.mergeCell(11,7,11,8);
			 rt.body.mergeCell(11,11,11,12);
			 rt.body.mergeCell(12,1,12,1);
			 rt.body.mergeCell(12,2,12,2);
			 rt.body.mergeCell(12,3,12,4);
			 rt.body.mergeCell(12,5,12,6);
			 rt.body.mergeCell(12,7,12,8);
			 rt.body.mergeCell(12,11,12,12);
			 rt.body.mergeCell(13,1,13,1);
			 rt.body.mergeCell(13,2,13,2);
			 rt.body.mergeCell(13,3,13,4);
			 rt.body.mergeCell(13,5,13,6);
			 rt.body.mergeCell(13,7,13,8);
			 rt.body.mergeCell(13,11,13,12);
			 rt.body.mergeCell(14,1,14,1);
			 rt.body.mergeCell(14,2,14,2);
			 rt.body.mergeCell(14,3,14,4);
			 rt.body.mergeCell(14,5,14,6);
			 rt.body.mergeCell(14,7,14,8);
			 rt.body.mergeCell(14,11,14,12);
			 rt.body.mergeCell(15,1,15,9);
			 rt.body.mergeCell(15,10,15,12);
			 rt.body.mergeCell(16,1,16,9);
			 rt.body.mergeCell(16,11,16,12);
			 rt.body.mergeCell(17,1,18,2);
			 rt.body.mergeCell(17,3,18,5);
			 rt.body.mergeCell(17,6,18,9);
			 rt.body.mergeCell(17,10,18,12);
				
	//		页脚 
			 rt.createDefautlFooter(ArrWidth);
			 rt.setDefautlFooter(1,4,"打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
	//		 rt.setDefautlFooter(15,3,"单位:吨  元/吨",Table.ALIGN_RIGHT);
//			 rt.body.geth
			_CurrentPage = 1;
			_AllPages = rt.body.getPages();
			if (_AllPages == 0) {
				_CurrentPage = 0;
			}
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			cn.Close();
		}
		return rt.getAllPagesHtml();
	}

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}


	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
		}
	}

	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			
			visit.setActivePageName(getPageName().toString());
			this.setRiq1(null);				//日期Date1
			this.setRiq2(null);				//日期Date2
			setMeikdwmcValue(null);			//矿别1
			setIMeikdwmcModel(null);		//矿别1
			getIMeikdwmcModels();
			visit.setDropDownBean2(null);	//结算编号
			visit.setProSelectionModel2(null);	//结算编号
		}
		isBegin=true;
	}
//	矿别
	public IDropDownBean getMeikdwmcValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
			((Visit)getPage().getVisit()).setDropDownBean1(
					(IDropDownBean)getIMeikdwmcModel().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean1();
	}

	public void setMeikdwmcValue(IDropDownBean Value) {
		
		if(((Visit)getPage().getVisit()).getDropDownBean1()!=Value){
			
			((Visit)getPage().getVisit()).setDropDownBean1(Value);
		}
	}

	public void setIMeikdwmcModel(IPropertySelectionModel value) {
		
		if(((Visit)getPage().getVisit()).getProSelectionModel1()!=value){
			
			((Visit)getPage().getVisit()).setProSelectionModel1(value);
		}
	}

	public IPropertySelectionModel getIMeikdwmcModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			getIMeikdwmcModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}

	public IPropertySelectionModel getIMeikdwmcModels() {

		String sql="select distinct m.id,m.meikdwmc as jianc from jiesb j,meikxxb m where j.meikxxb_id=m.id and jiesrq<=to_date('"+this.getRiq2()+"','yyyy-mm-dd') "
				+ " and jiesrq>=to_date('"+this.getRiq1()+"','yyyy-mm-dd') order by m.meikdwmc";
		
		((Visit)getPage().getVisit()).setProSelectionModel1(new IDropDownModel(sql,"全部"));
		
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}
//	矿别_end
	
//	结算编号
	public IDropDownBean getJiesbhValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean2()==null){
			((Visit)getPage().getVisit()).setDropDownBean2(
					(IDropDownBean)getIJiesbhModels().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean2();
	}

	public void setJiesbhValue(IDropDownBean Value) {
		
		if(((Visit)getPage().getVisit()).getDropDownBean2()!=Value){
			
			((Visit)getPage().getVisit()).setDropDownBean2(Value);
		}
	}

	public void setIJiesbhModel(IPropertySelectionModel value) {
		
		if(((Visit)getPage().getVisit()).getProSelectionModel2()!=value){
			
			((Visit)getPage().getVisit()).setProSelectionModel2(value);
		}
	}

	public IPropertySelectionModel getIJiesbhModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel2() == null) {
			getIJiesbhModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel2();
	}

	public IPropertySelectionModel getIJiesbhModels() {
		
		String sql="";
		if(this.getMeikdwmcValue().getId()==-1){
//			全部
			sql="select distinct id,jiesbh from jiesb where jiesrq<=to_date('"+this.getRiq2()+"','yyyy-mm-dd') "
					+ " and jiesrq>=to_date('"+this.getRiq1()+"','yyyy-mm-dd') order by jiesbh";
		}else{
			
			sql="select distinct id,jiesbh from jiesb where jiesrq<=to_date('"+this.getRiq2()+"','yyyy-mm-dd') "
					+ " and jiesrq>=to_date('"+this.getRiq1()+"','yyyy-mm-dd') and meikxxb_id="
					+getMeikdwmcValue().getId()+" order by jiesbh";
		}
		
		((Visit)getPage().getVisit()).setProSelectionModel2(new IDropDownModel(sql,"全部"));
		
		return ((Visit)getPage().getVisit()).getProSelectionModel2();
	}
//	结算编号_end
	
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript();
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

