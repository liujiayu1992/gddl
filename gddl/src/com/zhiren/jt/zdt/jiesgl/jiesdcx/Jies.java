//chh 2008-03-17 �޸Ĳ�ѯ��������ʽ
package com.zhiren.jt.zdt.jiesgl.jiesdcx;

/* 
* ʱ�䣺2009-07-31
* ���ߣ� ll
* �޸����ݣ�1���޸Ľ��㵥��ʽ
* 		   
*/ 

/* 
* ʱ�䣺2009-08-28
* ���ߣ� chh
* �޸����ݣ�1���޸Ľ��㵥��ʽ,���ز�ͬ����ı߿�
* 		   
*/ 

import org.apache.tapestry.html.BasePage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Date;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.Money;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Jies extends BasePage {
	public boolean getRaw() {
		return true;
	}
	private int _CurrentPage = -1;

	public int getCurrentPage() {
		return _CurrentPage;
	}
	public void setCurrentPage(int _value) {
		_CurrentPage = _value;
	}

	private int _AllPages = -1;

	public int getAllPages() {
		return _AllPages; 
	}

	public void setAllPages(int _value) {
		_AllPages = _value;
	}
	
	public String getDXMoney(double _Money ){
		Money money=new Money();
		return money.NumToRMBStr1(_Money);
	}
	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
			return getJiesd();
//		} else {
//			return "�޴˱���";
//		}
	}
	
	
	//��ú�����ֳ�ͳ�Ʊ���
	private String getJiesd() {
		JDBCcon con = new JDBCcon();
		ResultSet rs;
		Visit visit = (Visit) getPage().getVisit();
		
		String strleix = visit.getString1().substring(0,2);
		long lngjiesbID = Long.parseLong(visit.getString1().substring(2));
		String sql = "";
		
		 String strjiesbh = "";
		 String strhetbh = "";
		 String strshoukdw = "";
		 String strfaz = "";
		 String strmeiz = "";
		 String strgongysmc = "";
		 String stryunsjl = "";
		 String stryunsfs = "";
		 String strjihkj = "";
		 String strjiesrq = "";
		 String strchengyrq = "";
		 String strdaocrq = "";
		 String strmeij = "";
		 String strkedsyfyf = "";
		 String strches = "";
		 String strkund = "";
		 String strjiakmk = "";
		 String strkedsykyf = "";
		 String strpiaoz = "";
		 String stryuns = "";
		 String strhansdj = "";
		 String strkedshj = "";
		 String strjingz = "";
		 String strkoud = "";
		 String strbuhsdj = "";
		 String strbukdsyfyf = "";
		 String stryingd = "";
		 String strchal = "";
		 String strshuil = "";
		 String strbukdsykyf = "";
		 String strshijjsl = "";
		 String strbuhsmk = "";
		 String strbukdshj = "";
		 String strshuij = "";
		 String strkuangyf = "";
		 String strhetrz = "";
		 String stryansrz = "";
		 String strgongfrz = "";
		 String strjiesrz = "";
		 String strjiashj = "";
		 String strqiyf = "";
		 String strhethf = "";
		 String stryanshf = "";
		 String strgongfhf = "";
		 String strjieshf = "";
		 String stryunfsl = "";
		 String strqityzf = "";
		 String strhethff = "";
		 String stryanshff = "";
		 String strgongfhff = "";
		 String strjieshff = "";
		 String stryunf = "";
		 String strguotyj = "";
		 String strhetsf = "";
		 String stryanssf = "";
		 String strgongfsf = "";
		 String strjiessf = "";
		 String stryunfsj = "";
		 String strkuangyyj = "";
		 String strhetlf = "";
		 String stryanslf = "";
		 String strgongflf = "";
		 String strjieslf = "";
		 String stryunfhj = "";
		 String strqiyyj = "";
		 String strshifzjedx = "";
		 String strshifzjexx = "";
		 String strshuom = "";
		 String strkuangy="";
		 String strshisl="";//ʵ����
		 String strjufd="";//�ܸ���
		 String strdaoz="";//��վ
		 String strgonghdq="";//��������
		 String strhetrz_kcal= "" ;//��ͬ��ֵkcal
		 String stryansrz_kcal = "";//������ֵkcal
		 String strgongfrz_kcal = "" ;//������ֵkcal
		 String strjiesrz_kcal = "" ;//������ֵkcal
		 String strzhej_rz ="";//��ֵ�ۼ�
		 double mzhej_rz=0;
		 String strzhej_lf = "";//���ۼ�
		 double mzhej_lf=0;//
		 String strzhej_sf = "";//ȫˮ�ۼ�
		 double mzhej_sf=0;//
		 String strzhej_hf = "";//���ۼ�
		 double mzhej_hf=0;//
		 String strzhej_hff = "";//�ӷ����ۼ�
		 double mzhej_hff=0;//
		 String strzhej_qit="";//�����ۼ�
		 double mzhej_qit=0;//
		 double mhansdj=0;//��˰����
		 String stryuanj="";//ԭ��
		 double myuanj=0;
		 long mjiesl= 0;//����ú��
		 String strguotyfdj="";//�����˷ѵ���
		 String strshuiyfdj="";//ˮ�˷ѵ���
		 String strqiyfdj="";//���˷ѵ���
		 String strdityfdj="";//���˷ѵ���
		 String strhejdj ="";//�ϼƵ���
		 String stryunfhj_new="";//�˷Ѻϼ�
		 long mpiaoz=0;
		 long myingd=0;
		 long mkuid=0;
		
		 
		String strshuiyf="";//ˮ�˺�˰�˷��˷�
		String strshuiyfsj="";//ˮ���˷�˰��
        String strguotsfyf_kds="";//����ʵ���˷�_�ɵ�˰
		String strguotsj_kds="";//����˰��_�ɵ�˰
	    String strguotsfyf_bkds="";//����ʵ���˷�_���ɵ�˰
	    String strqiysj="";//����˰��
	    String strdityfsj="";//���˷�˰��
	    String strqitzf_yf="";//�����ӷѣ��˷ѣ�
	    String strjufje="";//�ܸ����
		 
		 double mshijjsl = 0;
		 double mhej = 0;
		 double mkuangyf = 0;
		 double mqiyf = 0;
		 double myunf = 0;
		 double mguot = 0;
		 double mqiy=0;
		 double myunfsj = 0;
		 double myunfhj = 0;
		 double mkuangy = 0;
		 double mjiashj = 0;
		 double myingfyf = 0;
		 double myingkyf = 0;
		 double myingfyf_b = 0;
		 double myingkyf_b = 0;
		 double mhej_b = 0;
	     double mshifzje=0;
	     long mchayl=0;
	     double mqityzf=0;
	     double mhansyf_k=0;
	     double mshuij_k=0;
	     double mhansyf_q=0;
	     double mshuij_q=0;
	     double mbuhsdj=0;
	     double mjiakmk=0;
	     String strranlbmjbr="";
	     String strhansyf_k="";
	     String strshuij_k="";
	     String strhansyf_q="";
	     String strshuij_q="";
	     long strhtbh=0;
	     String strdiancmc="";
		 try {
//			 ����������Ϣ
				 sql = "select distinct ht.id as het_id,dc.quanc as diancmc,js.bianm as jiesbh,js.shoukdw,js.faz,dz.mingc as daoz,js.meiz,gy.mingc as gongysmc,gy.dqmc as gydq,ys.mingc as yunsfs,kj.mingc as jihkjmc,ht.hetbh,js.YUNJ as yunsjl,js.yingd,js.kuid,js.guohl as jingz,js.koud,js.yuns,  \n"
					+ "       to_char(js.jiesrq,'yyyy-mm-dd') as jiesrq,getRiqDuan(js.fahksrq,js.fahjzrq) as chengyrq,getRiqDuan(js.yansksrq,js.yansjzrq) as daocrq,js.JIESSLCY as chal,js.ranlbmjbr as ranlbmjbr,\n"
					+ "       js.hansdj as hansdj,js.ches,js.buhsdj,js.shuil,js.jiessl as shijjsl,js.buhsmk,js.shuik as shuij,js.hansmk as jiashj,js.bukmk as jiakmk,js.beiz as shuom,round(((js.hansdj*js.jiessl+js.bukmk)/js.jiessl),2) as hansdj_js  \n"
					+ "  from jiesb js,diancxxb dc,vwgongys gy,hetb ht,jihkjb kj,YUNSFSB ys, fahb fh,chezxxb dz\n"
					+ " where js.diancxxb_id=dc.id and js.gongysb_id=gy.id and js.hetb_id=ht.id(+) and js.jihkjb_id=kj.id(+) and js.YUNSFSB_ID=ys.id(+) and fh.jiesb_id=js.id  and fh.daoz_id=dz.id \n"
					+ "   and js.id="+lngjiesbID+"";
				 
				 rs = con.getResultSet(sql);
				 if(rs.next()){
					 strhtbh=rs.getLong("het_id");
					 strdiancmc=rs.getString("diancmc");
					 strjiesbh = rs.getString("jiesbh");
					 strhetbh = rs.getString("hetbh");
					 strshoukdw = rs.getString("shoukdw");
					 strfaz = rs.getString("faz");
					 strdaoz = rs.getString("daoz");
					 strmeiz = rs.getString("meiz");
					 strgongysmc = rs.getString("gongysmc");
					 strgonghdq = rs.getString("gydq");
					 stryunsjl = rs.getString("yunsjl");//�������
					 stryunsfs = rs.getString("yunsfs");
					 strjihkj = rs.getString("jihkjmc");
					 strjiesrq = rs.getString("jiesrq");
					 strchengyrq = rs.getString("chengyrq");
					 strdaocrq = rs.getString("daocrq");
					 strranlbmjbr=rs.getString("ranlbmjbr");
//					 strmeij = new DecimalFormat( "0.00" ).format( rs.getDouble("meij") );//ȡ�ú�˰����
					 strmeij = new DecimalFormat( "0.00" ).format( rs.getDouble("hansdj") );//ȡ�ú�˰����
					 strches = rs.getString("ches");
//					 strhansdj = new DecimalFormat( "0.00" ).format( rs.getDouble("hansdj") );//rs.getString("hansdj");
					 strhansdj = new DecimalFormat( "0.00" ).format( rs.getDouble("hansdj_js") );//rs.getString("hansdj");
					 mhansdj= rs.getDouble("hansdj_js");
					 mbuhsdj=rs.getDouble("buhsdj");
					 strbuhsdj = new DecimalFormat( "0.00" ).format( rs.getDouble("buhsdj") );//mbuhsdj+"";
					 strshuil = new DecimalFormat( "0.00" ).format( rs.getDouble("shuil") );//rs.getString("shuil");
					 strshijjsl=new DecimalFormat( "0" ).format( rs.getDouble("shijjsl") );//rs.getString("shijjsl");
					 strbuhsmk = new DecimalFormat( "0.00" ).format( rs.getDouble("buhsmk") );//rs.getString("buhsmk");
					 strshuij = new DecimalFormat( "0.00" ).format( rs.getDouble("shuij") );//rs.getString("shuij");//ȡ��˰��
					 mjiakmk= rs.getDouble("jiakmk");
					 strjiakmk = new DecimalFormat( "0.00" ).format( rs.getDouble("jiakmk") );//rs.getString("jiakmk");//�ӿ�ú��
			
					 strjiashj = new DecimalFormat( "0.00" ).format( rs.getDouble("jiashj") );//rs.getString("jiashj");//��˰�ϼ�
					 strshuom = rs.getString("shuom");
					 
					 stryingd = rs.getString("yingd");
					 strkund = rs.getString("kuid");
					 strjingz = rs.getString("jingz");
					 stryuns = rs.getString("yuns");
					 strkoud = rs.getString("koud");
					 mchayl=rs.getLong("chal");
					 strchal = mchayl+"";//������
					 myingd=rs.getLong("yingd");
					 mkuid=rs.getLong("kuid");
				}
				
				sql = "select nvl(zl.gongf,0) as piaoz from jiesb js,jieszbsjb zl,zhibb zb "
					+ " where zl.jiesdid=js.id and zl.zhibb_id=zb.id and js.id="+lngjiesbID+" and zb.bianm='����' and zb.leib=1 ";
//					+ " where zl.jiesdid=js.id and zl.zhibb_id=zb.id and js.id="+lngjiesbID+" and zb.bianm='"+Locale.jiessl_zhibb+"' and zb.leib=1 ";
				rs = con.getResultSet(sql);
				if(rs.next()){
					mpiaoz=rs.getLong("piaoz");
					strpiaoz = rs.getString("piaoz");
					
				}
				strshisl=mpiaoz+myingd-mkuid+"";
//				��������
				//����
				sql = "select nvl(round(zl.hetbz*4.1816/1000,2),0) as hetrz,nvl(round(zl.gongf*4.1816/1000,2),0) as gongfrz,nvl(round(zl.changf*4.1816/1000,2),0) as yansrz,nvl(round(zl.jies*4.1816/1000,2),0) as jiesrz " +
						",nvl(round(zl.hetbz,0),0) as hetrz_kcal,nvl(round(zl.gongf,0),0) as gongfrz_kcal,nvl(round(zl.changf,0),0) as yansrz_kcal,nvl(round(zl.jies,0),0) as jiesrz_kcal,nvl(round(zl.zhejbz,2),0) as zhej_rz from jiesb js,jieszbsjb zl,zhibb zb "
					+ " where zl.jiesdid=js.id and zl.zhibb_id=zb.id and js.id="+lngjiesbID+" and zb.bianm='�յ�����λ��ֵQnetar(MJ/Kg)' and zb.leib=1 ";
				rs = con.getResultSet(sql);
				if(rs.next()){
					 strhetrz = new DecimalFormat( "0.00" ).format( rs.getDouble("hetrz") );//rs.getString("hetrz");
					 strhetrz_kcal= new DecimalFormat( "0" ).format(rs.getLong("hetrz_kcal")) ;
					 stryansrz = new DecimalFormat( "0.00" ).format( rs.getDouble("yansrz") );
					 stryansrz_kcal = new DecimalFormat( "0" ).format(rs.getLong("yansrz_kcal")) ;
					 strgongfrz = new DecimalFormat( "0.00" ).format( rs.getDouble("gongfrz") );
					 strgongfrz_kcal = new DecimalFormat( "0" ).format(rs.getLong("gongfrz_kcal")) ;
					 strjiesrz = new DecimalFormat( "0.00" ).format( rs.getDouble("jiesrz") );
					 strjiesrz_kcal = new DecimalFormat( "0" ).format(rs.getLong("jiesrz_kcal")) ;
					 strzhej_rz = new DecimalFormat( "0.00" ).format( rs.getDouble("zhej_rz") );
					 mzhej_rz=rs.getDouble("zhej_rz") ;
				}
//				���
				sql = "select nvl(zl.hetbz,0) as hetlf,nvl(zl.gongf,0) as gongflf,nvl(zl.changf,0) as yanslf,nvl(zl.jies,0) as jieslf,nvl(round(zl.zhejbz,2),0) as zhej_lf from jiesb js,jieszbsjb zl,zhibb zb "
					+ " where zl.jiesdid=js.id and zl.zhibb_id=zb.id and js.id="+lngjiesbID+" "
					+ "   and (zb.bianm='�����ȫ��Std(%)' or zb.bianm='һ�����ú��ȫ��Stad(%)' or zb.bianm='�յ���ȫ��') and zb.leib=1 ";
//					+ "   and (zb.bianm='"+Locale.Std_zhibb+"' or zb.bianm='"+Locale.Stad_zhibb+"') and zb.leib=1 ";
				rs = con.getResultSet(sql);
				if(rs.next()){
					 strhetlf = new DecimalFormat( "0.00" ).format( rs.getDouble("hetlf") );
						 //rs.getString("hetlf");
					 stryanslf = new DecimalFormat( "0.00" ).format( rs.getDouble("yanslf") );
						 //rs.getString("yanslf");
					 strgongflf = new DecimalFormat( "0.00" ).format( rs.getDouble("gongflf") );
						 //rs.getString("gongflf");
					 strjieslf = new DecimalFormat( "0.00" ).format( rs.getDouble("jieslf") );
					 	 //rs.getString("jieslf");
					 strzhej_lf = new DecimalFormat( "0.00" ).format( rs.getDouble("zhej_lf") );
					 mzhej_lf=rs.getDouble("zhej_lf") ;
				}
//				�ҷ�
				sql = "select nvl(zl.hetbz,0) as hethf,nvl(zl.gongf,0) as gongfhf,nvl(zl.changf,0) as yanshf,nvl(zl.jies,0) as jieshf,nvl(round(zl.zhejbz,2),0) as zhej_hf from jiesb js,jieszbsjb zl,zhibb zb "
					+ " where zl.jiesdid=js.id and zl.zhibb_id=zb.id and js.id="+lngjiesbID+" "
					+ "   and (zb.bianm='������ҷ�Ad(%)' or zb.bianm='�յ����ҷ�Aar(%)' or zb.bianm='һ�����ú���ҷ�Aad(%)') and zb.leib=1 ";
//					+ "   and (zb.bianm='"+Locale.Ad_zhibb+"' or zb.bianm='"+Locale.Aar_zhibb+"' or zb.bianm='"+Locale.Aad_zhibb+"') and zb.leib=1 ";
				rs = con.getResultSet(sql);
				if(rs.next()){
					 strhethf = new DecimalFormat( "0.00" ).format( rs.getDouble("hethf") );//rs.getString("hethf");
					 stryanshf = new DecimalFormat( "0.00" ).format( rs.getDouble("yanshf") );
					 strgongfhf = new DecimalFormat( "0.00" ).format( rs.getDouble("gongfhf") );
					 strjieshf = new DecimalFormat( "0.00" ).format( rs.getDouble("jieshf") );
					 strzhej_hf = new DecimalFormat( "0.00" ).format( rs.getDouble("zhej_hf") );
					 mzhej_hf=rs.getDouble("zhej_hf") ;
				}
//				�ӷ���
				sql = "select nvl(zl.hetbz,0) as hethff,nvl(zl.gongf,0) as gongfhff,nvl(zl.changf,0) as yanshff,nvl(zl.jies,0) as jieshff,nvl(round(zl.zhejbz,2),0) as zhej_hff from jiesb js,jieszbsjb zl,zhibb zb "
					+ " where zl.jiesdid=js.id and zl.zhibb_id=zb.id and js.id="+lngjiesbID+" "
					+ "   and (zb.bianm='�����޻һ��ӷ���Vdaf(%)' or zb.bianm='һ�����ú���ӷ���Vad(%)') and zb.leib=1 ";
//					+ "   and (zb.bianm='"+Locale.Vdaf_zhibb+"' or zb.bianm='"+Locale.Vad_zhibb+"') and zb.leib=1 ";
				rs = con.getResultSet(sql);
				if(rs.next()){
					 strhethff = new DecimalFormat( "0.00" ).format( rs.getDouble("hethff") );//rs.getString("hethff");
					 stryanshff = new DecimalFormat( "0.00" ).format( rs.getDouble("yanshff") ); 
					 strgongfhff = new DecimalFormat( "0.00" ).format( rs.getDouble("gongfhff") );
					 strjieshff = new DecimalFormat( "0.00" ).format( rs.getDouble("jieshff") );
					 strzhej_hff = new DecimalFormat( "0.00" ).format( rs.getDouble("zhej_hff") );
					 mzhej_hff= rs.getDouble("zhej_hff") ;
				}
//				ˮ��
				sql = "select nvl(zl.hetbz,0) as hetsf,nvl(zl.gongf,0) as gongfsf,nvl(zl.changf,0) as yanssf,nvl(zl.jies,0) as jiessf,nvl(round(zl.zhejbz,2),0) as zhej_sf from jiesb js,jieszbsjb zl,zhibb zb "
					+ " where zl.jiesdid=js.id and zl.zhibb_id=zb.id and js.id="+lngjiesbID+" "
					+ "   and (zb.bianm='ȫˮ��Mt(%)' or zb.bianm='һ�����ú��ˮ��Mad(%)') and zb.leib=1 ";
//					+ "   and (zb.bianm='"+Locale.Mt_zhibb+"' or zb.bianm='"+Locale.Mad_zhibb+"') and zb.leib=1 ";
				rs = con.getResultSet(sql);
				if(rs.next()){
					 strhetsf =new DecimalFormat( "0.00" ).format( rs.getDouble("hetsf") );
					 	 // rs.getString("hetsf");
					 stryanssf = new DecimalFormat( "0.00" ).format( rs.getDouble("yanssf") );
						 // rs.getString("yanssf");
					 strgongfsf = new DecimalFormat( "0.00" ).format( rs.getDouble("gongfsf") );
						 // rs.getString("gongfsf");
					 strjiessf = new DecimalFormat( "0.00" ).format( rs.getDouble("jiessf") );
						 //rs.getString("jiessf");
					 strzhej_sf = new DecimalFormat( "0.00" ).format( rs.getDouble("zhej_sf") );
					 mzhej_sf=rs.getDouble("zhej_sf");
				}
				myuanj=mhansdj-mzhej_sf-mzhej_hff-mzhej_hf-mzhej_lf-mzhej_rz-mzhej_qit;
				stryuanj= new DecimalFormat( "0.00" ).format(myuanj );
//				�˷�����
				sql = "select js.jiessl as jiesl,yf.shuil as yunfsl,yf.shuik as yunfsj,yf.buhsyf as yunf,yf.guotyf,yf.dityf,yf.jiskc,yf.bukyf,yf.guotyfjf,yf.guotzfjf \n"
					+"		,yf.guotzf,yf.qiyf,round(decode(nvl(js.jiessl,0),0,0,nvl(yf.guotyf,0)/nvl(js.jiessl,0)),2) as guotyfdj \n"+
					",round(decode(nvl(js.jiessl,0),0,0,nvl(yf.dityf,0)/nvl(js.jiessl,0)),2) as dityfdj\n" +
					",round(decode(nvl(js.jiessl,0),0,0,nvl(yf.qiyf,0)/nvl(js.jiessl,0)),2) as qiyfdj \n"+
					",round(decode(nvl(js.jiessl,0),0,0,nvl(yf.bukyf,0)/nvl(js.jiessl,0)),2) as qitzfdj \n"+
					",round(decode(nvl(js.jiessl,0),0,0,nvl(yf.qiyf+yf.guotyf+yf.dityf+yf.bukyf,0)/nvl(js.jiessl,0)),2) as hejdj\n"+
					",round(nvl(yf.bukyf,0),2) as qitzf_yf\n" +
					" ,round((nvl(yf.guotyf,0)-nvl(yf.guotyfjf,0)),2) as shifyf_kds\n" + 
					" ,round((nvl(yf.guotyf,0)-nvl(yf.guotyfjf,0)),2)*0.07 as shuij_kds\n" + 
					" ,round((nvl(yf.guotzf,0)-nvl(yf.guotzfjf,0)),2) as shifyf_bkds\n" + 
					" ,round(nvl(yf.dityf,0),2)*0.07 as shuij_dityf\n" + 
					" ,round(nvl(yf.qiyf,0),2)*0.07 as shuij_qiyf\n" + 
					" ,round((nvl(yf.guotyfjf,0)+nvl(yf.guotzfjf,0)),2) as jufje \n"+
                    " ,round(((nvl(yf.guotyf,0)-nvl(yf.guotyfjf,0))+(nvl(yf.guotzf,0)-nvl(yf.guotzfjf,0))+nvl(yf.dityf,0)+nvl(yf.qiyf,0)+nvl(yf.bukyf,0)),2) as yunfhj \n"+
				    " from jiesb js,jiesyfb yf where yf.diancjsmkb_id=js.id and js.id="+lngjiesbID;
				rs = con.getResultSet(sql);
				if(rs.next()){
				
					 myunf = rs.getDouble("yunf");//�˷�
					 myingfyf=rs.getDouble("guotyf");//�����˷�		
					 
					 if(stryunsfs.equals("ˮ·����")){
						 strshuiyfdj=new DecimalFormat( "0.00" ).format(rs.getDouble("guotyfdj"));//ˮ�˷ѵ���
						 strshuiyf=new DecimalFormat( "0.00" ).format(rs.getDouble("shifyf_kds"));//ˮ�˺�˰�˷��˷�
						 strshuiyfsj=new DecimalFormat( "0.00" ).format(rs.getDouble("shuij_kds"));//ˮ���˷�˰��
					 }else{
						 strguotyfdj=new DecimalFormat( "0.00" ).format(rs.getDouble("guotyfdj"));//�����˷ѵ���
						 strkedsyfyf=new DecimalFormat( "0.00" ).format( myingfyf );
						 strkedsykyf=new DecimalFormat( "0.00" ).format( myingkyf );
						 strguotsfyf_kds=new DecimalFormat( "0.00" ).format(rs.getDouble("shifyf_kds"));//����ʵ���˷�_�ɵ�˰
						 strguotsj_kds=new DecimalFormat( "0.00" ).format(rs.getDouble("shuij_kds"));//����˰��_�ɵ�˰
						 
						 
					 }
					 strguotsfyf_bkds=new DecimalFormat( "0.00" ).format( rs.getDouble("shifyf_bkds") );//����ʵ���˷�_���ɵ�˰
					 myingkyf=rs.getDouble("guotyfjf");
					 mhej=myingfyf-myingkyf;
					 myingfyf_b=rs.getDouble("guotzf");
					 myingkyf_b=rs.getDouble("guotzfjf");
					 mhej_b=myingfyf_b-myingkyf_b;
					
					 mhansyf_q=rs.getDouble("qiyf");
					 strhansyf_q=new DecimalFormat( "0.00" ).format( mhansyf_q );
					 
					 strqiyfdj=new DecimalFormat( "0.00" ).format( rs.getDouble("qiyfdj") );//���˷ѵ���
					 strqiysj=new DecimalFormat( "0.00" ).format( rs.getDouble("shuij_qiyf") );//����˰��
					 mhansyf_k=rs.getDouble("dityf");
				 	 strhansyf_k=new DecimalFormat( "0.00" ).format( mhansyf_k );//���˷� 
				 	
					 strdityfdj=new DecimalFormat( "0.00" ).format( rs.getDouble("dityfdj") );//���˷ѵ���
					 strdityfsj=new DecimalFormat( "0.00" ).format( rs.getDouble("shuij_dityf") );//���˷�˰��
					 
					 strhejdj=new DecimalFormat( "0.00" ).format( rs.getDouble("hejdj") );//�ϼƵ���
					 strqitzf_yf=new DecimalFormat( "0.00" ).format( rs.getDouble("qitzf_yf") );//�����ӷѣ��˷ѣ�
					 strjufje=new DecimalFormat( "0.00" ).format( rs.getDouble("jufje") );//�ܸ����
					 stryunfhj_new =new DecimalFormat( "0.00" ).format( rs.getDouble("yunfhj") );//�˷Ѻϼ�
//					 mhansyf_q=rs.getDouble("guotyf");
					 mjiashj =Double.parseDouble(strjiashj);
					 mshijjsl=Double.parseDouble(strshijjsl);
					 
					 myunfsj=rs.getDouble("yunfsj");
					 myunfhj=myunf+myunfsj;
					 
					 mqityzf=rs.getDouble("bukyf");
					 strqityzf=new DecimalFormat( "0.00" ).format( mqityzf );
					 
					 strdityfdj=new DecimalFormat( "0.00" ).format( rs.getDouble("qitzfdj") );//�����˷ѵ���
			
					 myunfhj=mhej+mhej_b+mhansyf_k+mshuij_k+mhansyf_q+mqityzf;
					 
//					 myunfsj=myunfhj-myunf;
					 mshifzje=myunfhj+mjiashj+mjiakmk;//�˷Ѻϼ�+��ˮ�ϼ�-�������ӷ�+�ӿ�ú��
					 stryunf = new DecimalFormat( "0.00" ).format( myunf );//�˷�
					 
					 strkedshj=new DecimalFormat( "0.00" ).format( mhej );
					 strbukdsyfyf=new DecimalFormat( "0.00" ).format( myingfyf_b );
				  	 strbukdsykyf=new DecimalFormat( "0.00" ).format( myingkyf_b );
					 strbukdshj=new DecimalFormat( "0.00" ).format( mhej_b );

					
					 stryunfsl=rs.getString("yunfsl");//�˷�˰��
					 stryunfhj=new DecimalFormat( "0.00" ).format( myunfhj );		

					 stryunfsj = new DecimalFormat( "0.00" ).format( myunfsj );
					 strshifzjexx="��"+new DecimalFormat( "0.00" ).format( mshifzje );
					 strshifzjedx=getDXMoney(mshifzje);

				}

		 
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}finally{
			con.Close();
		}


 //******************************************************�ɽ��㵥*************************************************************************
//	 	String ArrHeader[][]=new String[17][13];
//		ArrHeader[0]=new String[] {"���㵥λ��","",strshoukdw,"","","","","","","��վ��",strfaz,"ú̿Ʒ��",strmeiz};
//		ArrHeader[1]=new String[] {"������λ��","",strgongysmc,"","","","","","","�������",stryunsjl,"���䷽ʽ",stryunsfs};
//		ArrHeader[2]=new String[] {"�ƻ��ھ�","",strjihkj,"","��������",strjiesrq,"","��������",strchengyrq,"��������",strdaocrq,"ȼ�ϲ�������",strranlbmjbr};
//		ArrHeader[3]=new String[] {"��  ��(��)","��Ŀ","","����","��Ŀ","","����","ú ��(Ԫ/��)","",strmeij,"    ������<br>��˰(Ԫ)","Ӧ���˷�",strkedsyfyf};
//		ArrHeader[4]=new String[] {"","��     ��","",strches,"��     ��","",strkund,"�ӿ�ú��(Ԫ)","",strjiakmk,"","Ӧ���˷�",strkedsykyf};
//		ArrHeader[5]=new String[] {"","Ʊ     ��","",strpiaoz,"��  ��","",stryuns,"��˰����(Ԫ/��)","",strhansdj,"","��        ��",strkedshj};
//		ArrHeader[6]=new String[] {"","��   ��","",strjingz,"��    ��","",strkoud,"˰��(%)","",strshuil,"��������<br>��˰(Ԫ)","Ӧ���˷�",strbukdsyfyf};
//		ArrHeader[7]=new String[] {"","ӯ     ��","",stryingd,"������","",strchal,"����˰��(Ԫ/��)","",strbuhsdj,"","Ӧ���˷�",strbukdsykyf};
//		ArrHeader[8]=new String[] {"","ʵ�ʽ�����","",strshijjsl,"","","","����˰ú��(Ԫ)","",strbuhsmk,"","��        ��",strbukdshj};
//		ArrHeader[9]=new String[] {"��  ��","��Ŀ","","��ͬ","����","����","����","˰��(Ԫ)","",strshuij,"���˷�<br>��Ԫ��","��˰�˷�",strhansyf_k};
//		ArrHeader[10]=new String[] {"","�� ֵ","mj/kg",strhetrz,stryansrz,strgongfrz,strjiesrz,"��˰�ϼ�(Ԫ)","",strjiashj,"","˰   ��","0"};
//		ArrHeader[11]=new String[] {"","��    ��","Ad",strhethf,stryanshf,strgongfhf,strjieshf,"",""," ","���˷�<br>��Ԫ��","��˰�˷�",strhansyf_q};
//		ArrHeader[12]=new String[] {"","�ӷ���","Vdaf",strhethff,stryanshff,strgongfhff,strjieshff,"�˷� (Ԫ)","",stryunf,"","˰   ��","0"};
//		ArrHeader[13]=new String[] {"","ˮ    ��","Mt",strhetsf,stryanssf,strgongfsf,strjiessf,"�˷�˰��(Ԫ)","",stryunfsj,"�������ӷѣ�Ԫ��","",strqityzf};
//		ArrHeader[14]=new String[] {"","��    ��","St,d",strhetlf,stryanslf,strgongflf,strjieslf,"�˷Ѻϼ�(Ԫ)","",stryunfhj,"�˷�˰��(%)","",stryunfsl};
//		ArrHeader[15]=new String[] {"ʵ���ܽ��(��д)","","",strshifzjedx,"","","","","","ʵ���ܽ��(Сд)","",strshifzjexx,""};
//		ArrHeader[16]=new String[] {"˵��","","",strshuom,"","","","","","","","",""};
//		int ArrWidth[]=new int[] {30,45,40,80,70,70,70,110,130,80,80,120,90};
//		*******************************************************�½��㵥**********************************************************

//			 ����ҳTitle
	 
		 
		 String ArrHeader[][]=new String[21][16];
		 ArrHeader[0]=new String[] {"���㵥λ��","",strshoukdw,"","","","","������λ��","",strgongysmc,"����������","",strgonghdq,"","�� վ��",strfaz};
		 ArrHeader[1]=new String[] {"","","","","","","","","","","","","","","�� վ��",strdaoz};
		 ArrHeader[2]=new String[] {"��  ��","��     Ŀ","","��  ��","��    Ŀ","","��  �� ","ú ��","ԭ��",stryuanj,"�˷�","��Ŀ","","���","���䷽ʽ",stryunsfs};
		 ArrHeader[3]=new String[] {"","","","","","","","","�����ۼ�",strzhej_rz,"","�����ɵ�˰","Ӧ���˷�",strkedsyfyf,"��������",""};
		 ArrHeader[4]=new String[] {"","��     ��","",strches,"����","",stryuns,"","�ҷ��ۼ�",strzhej_hf,"","","˰    ��",strguotsj_kds,"",""};
		 ArrHeader[5]=new String[] {"","Ʊ     ��","",strpiaoz,"ʵ����","",strshisl,"","�ӷ����ۼ�",strzhej_hff,"","","Ӧ���˷�",strkedsykyf,"ú̿Ʒ��",strmeiz};
		 ArrHeader[6]=new String[] {"","��     ��","",strjingz,"�� �� ��","",strjufd,"","ˮ���ۼ�",strzhej_sf,"","","ʵ���˷�",strguotsfyf_kds,"�ƻ�����",strjihkj};
		 ArrHeader[7]=new String[] {"","ӯ     ��","",stryingd,"ʵ�ʽ�����","",strshijjsl,"","���ۼ�",strzhej_lf,"","�������ɵ�˰","Ӧ���˷�",strbukdsyfyf,"��������",strjiesrq};
		 ArrHeader[8]=new String[] {"","��     ��","",strkund,"������","",strchal,"","�����ۼ�",strzhej_qit,"","","Ӧ���˷�",strbukdsykyf,"��������",strchengyrq};
		 ArrHeader[9]=new String[] {"��  ��","��     Ŀ","","��ͬ","��  ��","��","��  ��","","ú�ۺϼ�",strhansdj,"","","ʵ���˷�",strguotsfyf_bkds,"",""};
		 ArrHeader[10]=new String[] {"","","","","","","","","����˰��",strbuhsdj,"","���˷�","��˰�˷�",strhansyf_k,"��������",strdaocrq};
		 ArrHeader[11]=new String[] {"","��ֵ","Kcal/kg",strhetrz_kcal,stryansrz_kcal,strgongfrz_kcal,strjiesrz_kcal,"��  ��","�����˷ѵ���",strguotyfdj,"","","˰��",strdityfsj,"",""};
		 ArrHeader[12]=new String[] {"","","","","","","","","���˷ѵ���",strdityfdj,"","���˷�","��˰�˷�",strhansyf_q,"ú��",strbuhsmk};
		 ArrHeader[13]=new String[] {"","","MJ/kg",strhetrz,stryansrz,strgongfrz,strjiesrz,"","���˷ѵ���",strqiyfdj,"","","˰��",strqiysj,"˰��",strshuij};
		 ArrHeader[14]=new String[] {"","","","","","","","","ˮ�˷ѵ���",strguotyfdj,"","ˮ�˷�","��˰�˷�",strshuiyf,"��������",strqityzf};
		 ArrHeader[15]=new String[] {"","��   ��  Ad","",strhethf,stryanshf,strgongfhf,strjieshf,"","�����ӷѵ���",strdityfdj,"","","˰��",strshuiyfsj,"�˷Ѻϼ�",stryunfhj_new};
		 ArrHeader[16]=new String[] {"","�ӷ���  Vdaf","",strhethff,stryanshff,strgongfhff,strjieshff,"","��        ��",strhejdj,"","�����ӷ�","",strqityzf,"�ܸ����",strjufje};
		 ArrHeader[17]=new String[] {"","ˮ   ��  Mt","",strhetsf,stryanssf,strgongfsf,strjiessf,"�˾�",stryunsjl,"","","","","","ʵ�����",strshifzjexx};
		 ArrHeader[18]=new String[] {"","  ��   ��  St��d","",strhetlf,stryanslf,strgongflf,strjieslf,"","","","","","","","",""};
		 ArrHeader[19]=new String[] {"ʵ���ܽ�� (��д)��","","",strshifzjedx,"","","","","","","","","","","",""};
		 ArrHeader[20]=new String[] {" ˵  ����","","",strshuom,"","","","","","","","","","","",""};

		 int ArrWidth[]=new int[] {40,30,45,60,60,60,60,40,80,130,40,75,75,80,70,90};


			Report rt=new Report();
			rt.setTitle(strdiancmc+"ȼ�Ͻ��㵥",ArrWidth);
			rt.setDefaultTitleLeft("������:"+strjiesbh,5);
			rt.setDefaultTitle(8,4,"��������:"+strjiesrq,Table.ALIGN_CENTER);
			rt.setDefaultTitle(14,3,"��ͬ���:"+"<a target=_blank href="+MainGlobal.getHomeContext(this)+"/app?service=page/Shenhrz&hetb_id="+strhtbh+">"+strhetbh+"</a>",Table.ALIGN_RIGHT);
			
			rt.setBody(new Table(ArrHeader,0,0,0));
			rt.body.setWidth(ArrWidth);
//				 �ϲ���Ԫ��

			rt.body.mergeCell(1,1,2,2);
			rt.body.mergeCell(1,3,2,7);
			rt.body.mergeCell(1,8,2,9);
			rt.body.mergeCell(1,10,2,10);
			rt.body.mergeCell(1,11,2,12);
			rt.body.mergeCell(1,13,2,14);
			rt.body.mergeCell(3,1,9,1);
			rt.body.mergeCell(3,2,4,3);
			rt.body.mergeCell(3,4,4,4);
			rt.body.mergeCell(3,5,4,6);
			rt.body.mergeCell(3,7,4,7);
			rt.body.mergeCell(3,8,11,8);
			rt.body.mergeCell(3,11,17,11);
			rt.body.mergeCell(3,12,3,13);
			rt.body.mergeCell(4,12,7,12);
			rt.body.mergeCell(4,15,5,15);
			rt.body.mergeCell(5,2,5,3);
			rt.body.mergeCell(5,5,5,6);
			rt.body.mergeCell(6,2,6,3);
			rt.body.mergeCell(6,5,6,6);
			rt.body.mergeCell(7,2,7,3);
			rt.body.mergeCell(7,5,7,6);
			rt.body.mergeCell(8,2,8,3);
			rt.body.mergeCell(8,5,8,6);
			rt.body.mergeCell(8,12,10,12);
			rt.body.mergeCell(9,2,9,3);
			rt.body.mergeCell(9,5,9,6);
			rt.body.mergeCell(9,15,10,15);
			rt.body.mergeCell(9,16,10,16);
			rt.body.mergeCell(10,1,19,1);
			rt.body.mergeCell(10,2,11,3);
			rt.body.mergeCell(10,4,11,4);
			rt.body.mergeCell(10,5,11,5);
			rt.body.mergeCell(10,6,11,6);
			rt.body.mergeCell(10,7,11,7);
			rt.body.mergeCell(11,12,12,12);
			rt.body.mergeCell(11,15,12,15);
			rt.body.mergeCell(11,16,12,16);
			rt.body.mergeCell(12,2,15,2);
			rt.body.mergeCell(12,3,13,3);
			rt.body.mergeCell(12,4,13,4);
			rt.body.mergeCell(12,5,13,5);
			rt.body.mergeCell(12,6,13,6);
			rt.body.mergeCell(12,7,13,7);
			rt.body.mergeCell(12,8,17,8);
			rt.body.mergeCell(13,12,14,12);
			rt.body.mergeCell(14,3,15,3);
			rt.body.mergeCell(14,4,15,4);
			rt.body.mergeCell(14,5,15,5);
			rt.body.mergeCell(14,6,15,6);
			rt.body.mergeCell(14,7,15,7);
			rt.body.mergeCell(15,12,16,12);
			rt.body.mergeCell(16,2,16,3);
			rt.body.mergeCell(17,2,17,3);
			rt.body.mergeCell(17,12,17,13);
			rt.body.mergeCell(18,2,18,3);
			rt.body.mergeCell(18,8,19,8);
			rt.body.mergeCell(18,9,19,14);
			rt.body.mergeCell(18,15,19,15);
			rt.body.mergeCell(18,16,19,16);
			rt.body.mergeCell(19,2,19,3);
			rt.body.mergeCell(20,1,20,3);
			rt.body.mergeCell(20,4,20,16);
			rt.body.mergeCell(21,1,21,3);
			rt.body.mergeCell(21,4,21,16);
			
			 for(int i=1;i<=rt.body.getRows();i++){
				 for(int j=1;j<=16;j++){
					 rt.body.setCellAlign(i, j, Table.ALIGN_CENTER);
				 }
			 }
			 
			 rt.body.setCellAlign(20, 4, Table.ALIGN_LEFT);
			 rt.body.setCellAlign(21, 4, Table.ALIGN_LEFT);
			//chh 2009-08-28 ��Ҫ��Ӵֲ�ͬ����ı߿�
			 rt.body.setRowCells(3, Table.PER_BORDER_TOP , 1);
			 rt.body.setColCells(8, Table.PER_BORDER_LEFT, 1);
			 rt.body.setColCells(11, Table.PER_BORDER_LEFT, 1);
			 rt.body.setColCells(15, Table.PER_BORDER_LEFT, 1);
			 rt.body.setCells(10, 1, 10, 8, Table.PER_BORDER_TOP, 1);
			 rt.body.setCells(12, 8, 10, 10, Table.PER_BORDER_TOP, 1);
			 rt.body.setCells(18, 8, 18, 14, Table.PER_BORDER_TOP, 1);
			 rt.body.setCells(20, 1, 20, 16, Table.PER_BORDER_TOP, 1);
			 rt.body.setCells(21, 1, 21, 16, Table.PER_BORDER_TOP, 1);

//************************************************************************************************************************		
		

		/*Report rt=new Report();
		rt.setTitle(strdiancmc+"ȼ�Ͻ��㵥",ArrWidth);
		rt.setDefaultTitleLeft("������:"+strjiesbh,5);
		rt.setDefaultTitle(6,4,"��������:"+strjiesrq,Table.ALIGN_CENTER);
		rt.setDefaultTitle(10,3,"��ͬ���:"+"<a target=_blank href="+MainGlobal.getHomeContext(this)+"/app?service=page/Shenhrz&hetb_id="+strhtbh+">"+strhetbh+"</a>",Table.ALIGN_RIGHT);
		
		rt.setBody(new Table(ArrHeader,0,0,0));
		rt.body.setWidth(ArrWidth);
//	�ϲ���Ԫ��
		rt.body.mergeCell(1,1,1,2);
		rt.body.mergeCell(1,3,1,9);
		rt.body.mergeCell(2,1,2,2);
		rt.body.mergeCell(2,3,2,9);
		rt.body.mergeCell(3,1,3,2);
		rt.body.mergeCell(3,3,3,4);
		rt.body.mergeCell(3,6,3,7);
		rt.body.mergeCell(4,1,9,1);
		rt.body.mergeCell(4,2,4,3);
		rt.body.mergeCell(4,5,4,6);
		rt.body.mergeCell(4,8,4,9);
		rt.body.mergeCell(4,11,6,11);
		rt.body.mergeCell(5,2,5,3);
		rt.body.mergeCell(5,5,5,6);
		rt.body.mergeCell(5,8,5,9);
		rt.body.mergeCell(6,2,6,3);
		rt.body.mergeCell(6,5,6,6);
		rt.body.mergeCell(6,8,6,9);
		rt.body.mergeCell(7,2,7,3);
		rt.body.mergeCell(7,5,7,6);
		rt.body.mergeCell(7,8,7,9);
		rt.body.mergeCell(7,11,9,11);
		rt.body.mergeCell(8,2,8,3);
		rt.body.mergeCell(8,5,8,6);
		rt.body.mergeCell(8,8,8,9);
		rt.body.mergeCell(9,2,9,3);
		rt.body.mergeCell(9,4,9,7);
		rt.body.mergeCell(9,8,9,9);
		rt.body.mergeCell(10,1,15,1);
		rt.body.mergeCell(10,2,10,3);
		rt.body.mergeCell(10,8,10,9);
		rt.body.mergeCell(10,11,11,11);
		rt.body.mergeCell(11,8,11,9);
		rt.body.mergeCell(12,11,13,11);
//		----------------------------------
		rt.body.mergeCell(12,8,12,9);
		rt.body.mergeCell(13,8,13,9);
		rt.body.mergeCell(14,8,14,9);
		rt.body.mergeCell(14,11,14,12);
		rt.body.mergeCell(15,8,15,9);
		rt.body.mergeCell(15,11,15,12);
		rt.body.mergeCell(16,1,16,3);
		rt.body.mergeCell(16,4,16,9);
		rt.body.mergeCell(16,10,16,11);
		rt.body.mergeCell(16,12,16,13);
		rt.body.mergeCell(17,1,17,3);
		rt.body.mergeCell(17,4,17,13);		
		
		 for(int i=1;i<=rt.body.getRows();i++){
			 for(int j=1;j<=13;j++){
				 rt.body.setCellAlign(i, j, Table.ALIGN_CENTER);
			 }
		 }
		 rt.body.setCellAlign(4, 10, Table.ALIGN_RIGHT);
		 rt.body.setCellAlign(4, 13, Table.ALIGN_RIGHT);
		 rt.body.setCellAlign(5, 4, Table.ALIGN_RIGHT);
		 rt.body.setCellAlign(5, 7, Table.ALIGN_RIGHT);
		 rt.body.setCellAlign(5, 10, Table.ALIGN_RIGHT);
		 rt.body.setCellAlign(5, 13, Table.ALIGN_RIGHT);
		 rt.body.setCellAlign(6, 4, Table.ALIGN_RIGHT);
		 rt.body.setCellAlign(6, 7, Table.ALIGN_RIGHT);
		 rt.body.setCellAlign(6, 10, Table.ALIGN_RIGHT);
		 rt.body.setCellAlign(6, 13, Table.ALIGN_RIGHT);
		 rt.body.setCellAlign(7, 4, Table.ALIGN_RIGHT);
		 rt.body.setCellAlign(7, 7, Table.ALIGN_RIGHT);
		 rt.body.setCellAlign(7, 10, Table.ALIGN_RIGHT);
		 rt.body.setCellAlign(7, 13, Table.ALIGN_RIGHT);
		 rt.body.setCellAlign(8, 4, Table.ALIGN_RIGHT);
		 rt.body.setCellAlign(8, 7, Table.ALIGN_RIGHT);
		 rt.body.setCellAlign(8, 10, Table.ALIGN_RIGHT);
		 rt.body.setCellAlign(8, 13, Table.ALIGN_RIGHT);
		 rt.body.setCellAlign(9, 4, Table.ALIGN_RIGHT);
		 rt.body.setCellAlign(9, 10, Table.ALIGN_RIGHT);
		 rt.body.setCellAlign(9, 13, Table.ALIGN_RIGHT);
		 rt.body.setCellAlign(10, 10, Table.ALIGN_RIGHT);
		 rt.body.setCellAlign(10, 13, Table.ALIGN_RIGHT);
		 rt.body.setCellAlign(11, 4, Table.ALIGN_RIGHT);
		 rt.body.setCellAlign(11, 5, Table.ALIGN_RIGHT);
		 rt.body.setCellAlign(11, 6, Table.ALIGN_RIGHT);
		 rt.body.setCellAlign(11, 7, Table.ALIGN_RIGHT);
		 rt.body.setCellAlign(11, 10, Table.ALIGN_RIGHT);
		 rt.body.setCellAlign(11, 13, Table.ALIGN_RIGHT);
		 rt.body.setCellAlign(12, 4, Table.ALIGN_RIGHT);
		 rt.body.setCellAlign(12, 5, Table.ALIGN_RIGHT);
		 rt.body.setCellAlign(12, 6, Table.ALIGN_RIGHT);
		 rt.body.setCellAlign(12, 7, Table.ALIGN_RIGHT);
		 rt.body.setCellAlign(12, 10, Table.ALIGN_RIGHT);
		 rt.body.setCellAlign(12, 13, Table.ALIGN_RIGHT);
		 rt.body.setCellAlign(13, 4, Table.ALIGN_RIGHT);
		 rt.body.setCellAlign(13, 5, Table.ALIGN_RIGHT);
		 rt.body.setCellAlign(13, 6, Table.ALIGN_RIGHT);
		 rt.body.setCellAlign(13, 7, Table.ALIGN_RIGHT);
		 rt.body.setCellAlign(13, 10, Table.ALIGN_RIGHT);
		 rt.body.setCellAlign(13, 13, Table.ALIGN_RIGHT);
		 rt.body.setCellAlign(14, 4, Table.ALIGN_RIGHT);
		 rt.body.setCellAlign(14, 5, Table.ALIGN_RIGHT);
		 rt.body.setCellAlign(14, 6, Table.ALIGN_RIGHT);
		 rt.body.setCellAlign(14, 7, Table.ALIGN_RIGHT);
		 rt.body.setCellAlign(14, 10, Table.ALIGN_RIGHT);
		 rt.body.setCellAlign(14, 13, Table.ALIGN_RIGHT);
		 rt.body.setCellAlign(15, 4, Table.ALIGN_RIGHT);
		 rt.body.setCellAlign(15, 5, Table.ALIGN_RIGHT);
		 rt.body.setCellAlign(15, 6, Table.ALIGN_RIGHT);
		 rt.body.setCellAlign(15, 7, Table.ALIGN_RIGHT);
		 rt.body.setCellAlign(15, 10, Table.ALIGN_RIGHT);
		 rt.body.setCellAlign(15, 13, Table.ALIGN_RIGHT);
		 rt.body.setCellAlign(16, 12,  Table.ALIGN_RIGHT); 
		 rt.body.setCellAlign(16, 4, Table.ALIGN_LEFT);
		 rt.body.setCellAlign(17, 4, Table.ALIGN_LEFT);*/
		 rt.createDefautlFooter(ArrWidth);
		 rt.setDefautlFooter(1, 4, "ȼ������:",Table.ALIGN_LEFT);
		 rt.setDefautlFooter(8, 2, "���:",Table.ALIGN_LEFT);
		 rt.setDefautlFooter(11, 2, "���񸴺�:",Table.ALIGN_CENTER);
		 rt.setDefautlFooter(15, 2, "�Ʊ�:",Table.ALIGN_LEFT);
		 
		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		return rt.getAllPagesHtml();
	}
//	________________________________________________________

	private boolean _QueryClick = false;

	public void QueryButton(IRequestCycle cycle) {
		_QueryClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_QueryClick) {
			_QueryClick = false;
		}
	}

	public String getPageHome() {
		if (((Visit) getPage().getVisit()).getboolean1()) {
			return "window.location = '" + MainGlobal.getHomeContext(this)
					+ "';";
		} else {
			return "";
		}
	}
	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy��MM��dd��", _date);
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (visit.getRenyID() == -1) {
			 visit.setboolean1(true);
			 return;
		}
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
//			visit.setList1(null);
			visit.setString1("");
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean2(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean3(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
//			visit.setDefaultTree(null);
		}
		if (cycle.getRequestContext().getParameter("bianm") != null&&!cycle.getRequestContext().getParameter("bianm").equals("-1")) {
			visit.setString1(cycle.getRequestContext().getParameter("bianm"));
		}else{
			blnIsBegin = false;
			return;
		}
//		getToolbars();
		blnIsBegin = true;
	}
   
}
