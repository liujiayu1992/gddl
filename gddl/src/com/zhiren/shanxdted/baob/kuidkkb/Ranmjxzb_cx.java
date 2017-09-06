package com.zhiren.shanxdted.baob.kuidkkb;

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
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Ranmjxzb_cx extends BasePage implements PageValidateListener {
	
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

	// ***************������Ϣ��******************//
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
	
	public String getPrintTable(){
	/*	if(this.getBaoblxValue().getValue().equals("����ͳ��")){
			return getChangbTj();
		}else
		if(this.getBaoblxValue().getValue().equals("����ͳ��")){
			
		}*/ 
		return getKuangbTj();
	}
	


	
	
	private String getKuangbTj() {
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
		
		String riqi=this.getRiqi();
		String riqi2=this.getRiqi2();
		String diancTiaoj="";
		if(this.getTreeid_dc().equals("300")){
			diancTiaoj="";
		}else{
			diancTiaoj=" and dc.id in ("+this.getTreeid_dc()+")";
		}
		
	
	

		StringBuffer buffer = new StringBuffer();
		
		if(this.getBaoblxValue().getValue().equals("�ֳ���ͳ��")){
			
			buffer.append(
					"select decode(dc.mingc,null,'�ܼ�',dc.mingc) as dcmc,\n" +
					"      decode(grouping(dc.mingc)+grouping(g.mingc),2,'',1,'�ϼ�',g.mingc) as meikdq,\n" + 
					"      decode(grouping(g.mingc)+grouping(mk.mingc),2,'',1,'С��',mk.mingc) as meikdw,\n" + 
					"      to_char(min(f.daohrq),'yyyy-mm-dd')||'��'||to_char(max(f.daohrq),'yyyy-mm-dd') as riq,\n" + 
					"       sum(f.biaoz) as biaoz,sum(f.jingz) as jingz,\n" + 
					"      decode(sum(f.jingz),0,0,round_new(sum(kk.hetrz*f.jingz)/sum(f.jingz),0)) as hetrz,\n" + 
					"      decode(sum(f.jingz),0,0,round_new(sum(kk.kuangfrz*f.jingz)/sum(f.jingz),0)) as kuangfrz,\n" + 
					"      decode(sum(f.jingz),0,0,round_new(sum(kk.chukrz*f.jingz)/sum(f.jingz),0)) as chukrz,\n" + 
					"      decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0)) as rucrz,\n" + 
					"      decode(sum(f.jingz),0,0,round_new(sum(kk.jiesrz*f.jingz)/sum(f.jingz),0)) as jiesrz,\n" + 
					"       decode(sum(f.jingz),0,0,round_new(sum(kk.hetrz*f.jingz)/sum(f.jingz),0))-\n" + 
					"         decode(sum(f.jingz),0,0,round_new(sum(kk.kuangfrz*f.jingz)/sum(f.jingz),0)) as hetkfrzc,\n" + 
					"      decode(sum(f.jingz),0,0,round_new(sum(kk.kuangfrz*f.jingz)/sum(f.jingz),0))-\n" + 
					"          decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0))   as kuangfrcrzc,\n" + 
					"      decode(sum(f.jingz),0,0,round_new(sum(kk.chukrz*f.jingz)/sum(f.jingz),0))-\n" + 
					"          decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0))   as chukrcrzc,\n" + 
					"      decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0)) -\n" + 
					"            decode(sum(f.jingz),0,0,round_new(sum(kk.jiesrz*f.jingz)/sum(f.jingz),0)) as rucjsrzc,\n" + 
					"       decode(sum(f.jingz),0,0,round_new(sum(kk.stad*f.jingz)/sum(f.jingz),2)) as stad,\n" + 
					"       round_new((sum(f.biaoz)-sum(f.jingz))/sum(f.biaoz)*100,2) as kuidl,\n" + 
					"      decode(sum(f.jingz),0,0,round_new(sum(kk.meij*f.jingz)/sum(f.jingz),2)) as meij,\n" + 
					"      decode(sum(f.jingz),0,0,round_new(sum(yf.yunf*f.jingz)/sum(f.jingz),2)) as yunf,\n" + 
					"      --��Ϊsum(f.jingz)�ڷ�����������0,���ԾͲ��жϱ�����sum(f.jingz),�ĳ��ж�sum(hetrz)\n" + 
					"      decode(sum(hetrz),0,0,round_new(\n" + 
					"       (decode(sum(f.jingz),0,0,round_new(sum(kk.hetmj*f.jingz)/sum(f.jingz),2))/1.17+\n" + 
					"        decode(sum(f.jingz),0,0,round_new(sum(yf.yunf*f.jingz)/sum(f.jingz),2))/1.11)*7000/\n" + 
					"        decode(sum(f.jingz),0,0,round_new(sum(kk.hetrz*f.jingz)/sum(f.jingz),0)),2))  as hetbmdj,\n" + 
					"     --��Ϊsum(f.jingz)�ڷ�����������0,���ԾͲ��жϱ�����sum(f.jingz),�ĳ��ж�sum(rucrz)\n" + 
					"      decode(sum(rucrz),0,0,round_new(\n" + 
					"       (decode(sum(f.jingz),0,0,round_new(sum(kk.meij*f.jingz)/sum(f.jingz),2))/1.17+\n" + 
					"        decode(sum(f.jingz),0,0,round_new(sum(yf.yunf*f.jingz)/sum(f.jingz),2))/1.11)*7000/\n" + 
					"        decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0)),2))  as rucbmdj,\n" + 
					"       --��Ϊsum(f.jingz)�ڷ�����������0,���ԾͲ��жϱ�����sum(f.jingz),�ĳ��ж�sum(jiesrz)\n" + 
					"      decode(sum(jiesrz),0,0,round_new(\n" + 
					"       (decode(sum(f.jingz),0,0,round_new(sum(kk.meij*f.jingz)/sum(f.jingz),2))/1.17+\n" + 
					"        decode(sum(f.jingz),0,0,round_new(sum(yf.yunf*f.jingz)/sum(f.jingz),2))/1.11)*7000/\n" + 
					"        decode(sum(f.jingz),0,0,round_new(sum(kk.jiesrz*f.jingz)/sum(f.jingz),0)),2))  as jiesbmdj,\n" + 
					"      --����Ӱ��ɱ�\n" + 
					"      sum(kk.kuidje) as kuidje,\n" + 
					"      --�ֿ���Ӱ��ɱ�  ��ʽ=�ֿ���Ӱ��*�볧����/10000\n" + 
					"      round_new(round_new((decode(sum(rucrz),0,0,round_new(\n" + 
					"       (decode(sum(f.jingz),0,0,round_new(sum(kk.meij*f.jingz)/sum(f.jingz),2))/1.17+\n" + 
					"        decode(sum(f.jingz),0,0,round_new(sum(yf.yunf*f.jingz)/sum(f.jingz),2))/1.11)*7000/\n" + 
					"        decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0)),2)) -\n" + 
					"      decode(sum(jiesrz),0,0,round_new(\n" + 
					"       (decode(sum(f.jingz),0,0,round_new(sum(kk.meij*f.jingz)/sum(f.jingz),2))/1.17+\n" + 
					"        decode(sum(f.jingz),0,0,round_new(sum(yf.yunf*f.jingz)/sum(f.jingz),2))/1.11)*7000/\n" + 
					"        decode(sum(f.jingz),0,0,round_new(sum(kk.jiesrz*f.jingz)/sum(f.jingz),0)),2)))*\n" + 
					"       decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0))/7000,2)*\n" + 
					"       sum(jingz)/10000,2) as dunkkyxcb,\n" + 
					"       --���ֿ������ϼ�\n" + 
					"       sum(kk.kuidje)+\n" + 
					"        round_new(round_new((decode(sum(rucrz),0,0,round_new(\n" + 
					"       (decode(sum(f.jingz),0,0,round_new(sum(kk.meij*f.jingz)/sum(f.jingz),2))/1.17+\n" + 
					"        decode(sum(f.jingz),0,0,round_new(sum(yf.yunf*f.jingz)/sum(f.jingz),2))/1.11)*7000/\n" + 
					"        decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0)),2)) -\n" + 
					"      decode(sum(jiesrz),0,0,round_new(\n" + 
					"       (decode(sum(f.jingz),0,0,round_new(sum(kk.meij*f.jingz)/sum(f.jingz),2))/1.17+\n" + 
					"        decode(sum(f.jingz),0,0,round_new(sum(yf.yunf*f.jingz)/sum(f.jingz),2))/1.11)*7000/\n" + 
					"        decode(sum(f.jingz),0,0,round_new(sum(kk.jiesrz*f.jingz)/sum(f.jingz),0)),2)))*\n" + 
					"       decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0))/7000,2)*\n" + 
					"       sum(f.jingz)/10000,2) as kuidkkhj\n" + 
					"   from kuidkkb_wh kk,fahb f ,meikxxb mk,kuidkkb_wh_yunf yf,gongysb g,diancxxb dc\n" + 
					"   where kk.daohrq>=date'"+riqi+"'\n" + 
					"   and kk.daohrq<=date'"+riqi2+"'\n" + 
					"   and kk.fahb_id=f.id\n" + 
					"   and kk.meikxxb_id=mk.id\n" + 
					"   and kk.fahb_id=yf.fahb_id(+)\n" + 
					"   and mk.meikdq_id=g.id\n" + 
					"   "+diancTiaoj+"\n"+
					"   and f.diancxxb_id=dc.id\n" + 
					"   group by rollup(dc.mingc,g.mingc,mk.mingc)\n" + 
					"   order by grouping(dc.mingc),min(dc.id),grouping(g.mingc),g.mingc,grouping(mk.mingc),mk.mingc");
			
		}else{
			
			//���ֳ���ͳ��
			buffer.append(
					"select decode(g.mingc,null,'�ܼ�',g.mingc) as meikdw,\n" +
				    "      decode(grouping(g.mingc)+grouping(mk.mingc),2,'',1,'С��',mk.mingc) as meikdw,\n" + 
					"      to_char(min(f.daohrq),'yyyy-mm-dd')||'��'||to_char(max(f.daohrq),'yyyy-mm-dd') as riq,\n" + 
					"       sum(f.biaoz) as biaoz,sum(jingz) as jingz,\n" + 
					"      decode(sum(f.jingz),0,0,round_new(sum(kk.hetrz*f.jingz)/sum(f.jingz),0)) as hetrz,\n" + 
					"      decode(sum(f.jingz),0,0,round_new(sum(kk.kuangfrz*f.jingz)/sum(f.jingz),0)) as kuangfrz,\n" +
					"      decode(sum(f.jingz),0,0,round_new(sum(kk.chukrz*f.jingz)/sum(f.jingz),0)) as chukrz,\n" +
					"      decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0)) as rucrz,\n" + 
					"      decode(sum(f.jingz),0,0,round_new(sum(kk.jiesrz*f.jingz)/sum(f.jingz),0)) as jiesrz,\n" + 
					"       decode(sum(f.jingz),0,0,round_new(sum(kk.hetrz*f.jingz)/sum(f.jingz),0))-\n" + 
					"         decode(sum(f.jingz),0,0,round_new(sum(kk.kuangfrz*f.jingz)/sum(f.jingz),0)) as hetkfrzc,\n" + 
					"      decode(sum(jingz),0,0,round_new(sum(kk.kuangfrz*f.jingz)/sum(f.jingz),0))-\n" + 
					"          decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0))   as kuangfrcrzc,\n" + 
					"      decode(sum(jingz),0,0,round_new(sum(kk.chukrz*f.jingz)/sum(f.jingz),0))-\n" + 
					"          decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0))   as chukrcrzc,\n" + 
					"      decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0)) -\n" + 
					"            decode(sum(jingz),0,0,round_new(sum(kk.jiesrz*f.jingz)/sum(f.jingz),0)) as rucjsrzc,\n" + 
					"       decode(sum(f.jingz),0,0,round_new(sum(kk.stad*f.jingz)/sum(f.jingz),2)) as stad,\n" + 
					"       round_new((sum(f.biaoz)-sum(f.jingz))/sum(f.biaoz)*100,2) as kuidl,\n" + 
					"      decode(sum(f.jingz),0,0,round_new(sum(kk.meij*f.jingz)/sum(f.jingz),2)) as meij,\n" + 
					"      decode(sum(f.jingz),0,0,round_new(sum(yf.yunf*f.jingz)/sum(f.jingz),2)) as yunf,\n" + 
					"      --��Ϊsum(f.jingz)�ڷ�����������0,���ԾͲ��жϱ�����sum(f.jingz),�ĳ��ж�sum(hetrz)\n" + 
					"      decode(sum(hetrz),0,0,round_new(\n" + 
					"       (decode(sum(f.jingz),0,0,round_new(sum(kk.hetmj*f.jingz)/sum(f.jingz),2))/1.17+\n" + 
					"        decode(sum(f.jingz),0,0,round_new(sum(yf.yunf*f.jingz)/sum(f.jingz),2))/1.11)*7000/\n" + 
					"        decode(sum(f.jingz),0,0,round_new(sum(kk.hetrz*f.jingz)/sum(f.jingz),0)),2))  as hetbmdj,\n" + 
					"     --��Ϊsum(f.jingz)�ڷ�����������0,���ԾͲ��жϱ�����sum(f.jingz),�ĳ��ж�sum(rucrz)\n" + 
					"      decode(sum(rucrz),0,0,round_new(\n" + 
					"       (decode(sum(f.jingz),0,0,round_new(sum(kk.meij*f.jingz)/sum(f.jingz),2))/1.17+\n" + 
					"        decode(sum(f.jingz),0,0,round_new(sum(yf.yunf*f.jingz)/sum(f.jingz),2))/1.11)*7000/\n" + 
					"        decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0)),2))  as rucbmdj,\n" + 
					"       --��Ϊsum(f.jingz)�ڷ�����������0,���ԾͲ��жϱ�����sum(f.jingz),�ĳ��ж�sum(jiesrz)\n" + 
					"      decode(sum(jiesrz),0,0,round_new(\n" + 
					"       (decode(sum(f.jingz),0,0,round_new(sum(kk.meij*f.jingz)/sum(f.jingz),2))/1.17+\n" + 
					"        decode(sum(f.jingz),0,0,round_new(sum(yf.yunf*f.jingz)/sum(f.jingz),2))/1.11)*7000/\n" + 
					"        decode(sum(f.jingz),0,0,round_new(sum(kk.jiesrz*f.jingz)/sum(f.jingz),0)),2))  as jiesbmdj,\n" + 
					"      --����Ӱ��ɱ�\n" + 
					"      sum(kk.kuidje) as kuidje,\n" + 
					"      --�ֿ���Ӱ��ɱ�  ��ʽ=�ֿ���Ӱ��*�볧����/10000\n" + 
					"      round_new(round_new((decode(sum(rucrz),0,0,round_new(\n" + 
					"       (decode(sum(f.jingz),0,0,round_new(sum(kk.meij*f.jingz)/sum(f.jingz),2))/1.17+\n" + 
					"        decode(sum(f.jingz),0,0,round_new(sum(yf.yunf*f.jingz)/sum(f.jingz),2))/1.11)*7000/\n" + 
					"        decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0)),2)) -\n" + 
					"      decode(sum(jiesrz),0,0,round_new(\n" + 
					"       (decode(sum(f.jingz),0,0,round_new(sum(kk.meij*f.jingz)/sum(f.jingz),2))/1.17+\n" + 
					"        decode(sum(f.jingz),0,0,round_new(sum(yf.yunf*f.jingz)/sum(f.jingz),2))/1.11)*7000/\n" + 
					"        decode(sum(f.jingz),0,0,round_new(sum(kk.jiesrz*f.jingz)/sum(f.jingz),0)),2)))*\n" + 
					"       decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0))/7000,2)*\n" + 
					"       sum(jingz)/10000,2) as dunkkyxcb,\n" + 
					"       --���ֿ������ϼ�\n" + 
					"       sum(kk.kuidje) +\n" + 
					"        round_new(round_new((decode(sum(rucrz),0,0,round_new(\n" + 
					"       (decode(sum(f.jingz),0,0,round_new(sum(kk.meij*f.jingz)/sum(f.jingz),2))/1.17+\n" + 
					"        decode(sum(f.jingz),0,0,round_new(sum(yf.yunf*f.jingz)/sum(f.jingz),2))/1.11)*7000/\n" + 
					"        decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0)),2)) -\n" + 
					"      decode(sum(jiesrz),0,0,round_new(\n" + 
					"       (decode(sum(f.jingz),0,0,round_new(sum(kk.meij*f.jingz)/sum(f.jingz),2))/1.17+\n" + 
					"        decode(sum(f.jingz),0,0,round_new(sum(yf.yunf*f.jingz)/sum(f.jingz),2))/1.11)*7000/\n" + 
					"        decode(sum(f.jingz),0,0,round_new(sum(kk.jiesrz*f.jingz)/sum(f.jingz),0)),2)))*\n" + 
					"       decode(sum(f.jingz),0,0,round_new(sum(kk.rucrz*f.jingz)/sum(f.jingz),0))/7000,2)*\n" + 
					"       sum(jingz)/10000,2) as kuidkkhj\n" + 
					"   from kuidkkb_wh kk,fahb f ,meikxxb mk,kuidkkb_wh_yunf yf,gongysb g,diancxxb dc\n" + 
					"   where kk.daohrq>=date'"+riqi+"'\n" + 
					"   and kk.daohrq<=date'"+riqi2+"'\n" + 
					"   and kk.fahb_id=f.id\n" + 
					"   and kk.meikxxb_id=mk.id\n" + 
					"   and kk.fahb_id=yf.fahb_id(+)\n" + 
					"   and mk.meikdq_id=g.id\n" + 
					"   "+diancTiaoj+"\n"+
					"   and f.diancxxb_id=dc.id\n" + 
					"   group by rollup(g.mingc,mk.mingc)\n" + 
					"   order by grouping(g.mingc),g.mingc,grouping(mk.mingc),mk.mingc");
			
		}
		
		
		ResultSet rs = con.getResultSet(buffer,
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		
		
		String ArrHeader[][];
		int ArrWidth[] ;
		if(this.getBaoblxValue().getValue().equals("�ֳ���ͳ��")){
			 ArrHeader = new String[1][25];
			ArrHeader[0] = new String[] {"��λ", "����ṹ","��ú��λ","��������", "����", "�볧��", "��ͬ��ֵ", "����ֵ","������ֵ","�볧��ֵ", "������ֵ","��ͬ��<br>��ֵ��",
					"���볧<br>��ֵ��","�����볧<br>��ֵ��","�볧����<br>��ֵ��","�볧���", "������(%)",
					"ú��","�˼�","��ͬ��ú<br>����","�볧��ú<br>����","�����ú<br>����","����Ӱ��<br>�ɱ�(��Ԫ)","����Ӱ��<br>�ɱ�(��Ԫ)","���ֿ���<br>�ϼ�(��Ԫ)"};
			 ArrWidth = new int[] {100, 80, 140,150,70, 70, 60, 60, 60,60,60, 60, 60,60 ,60, 60,60 , 60, 60, 60, 60,60 , 60, 60,60};
		
		}else{
		   ArrHeader = new String[1][24];
		   ArrHeader[0] = new String[] { "����ṹ","��ú��λ","��������", "����", "�볧��", "��ͬ��ֵ", "����ֵ","������ֵ","�볧��ֵ", "������ֵ","��ͬ��<br>��ֵ��",
					"���볧<br>��ֵ��","�����볧<br>��ֵ��","�볧����<br>��ֵ��", "�볧���", "������(%)",
					"ú��","�˼�","��ͬ��ú<br>����","�볧��ú<br>����","�����ú<br>����","����Ӱ��<br>�ɱ�(��Ԫ)","����Ӱ��<br>�ɱ�(��Ԫ)","���ֿ���<br>�ϼ�(��Ԫ)"};
			ArrWidth = new int[] { 80, 140,150,70, 70, 60, 60,60,60, 60, 60, 60,60 ,60, 60,60 , 60, 60, 60, 60,60 , 60, 60,60};
		
		}
			
		if(riqi.equals(riqi2)){
			
			rt.setTitle(riqi+"  ȼ ú �� Ч ָ �� ", ArrWidth);
		}else{
			
			rt.setTitle(riqi+"��"+riqi2+"  ȼ ú �� Ч ָ �� ", ArrWidth);
		}
		String baot="";
		if(this.getTreeid_dc().equals("300")){
			baot="�����ͬ����(1-10)";
		}else if(this.getTreeid_dc().equals("301")){
			baot="�����ͬһ��(1-6)";
		}else if(this.getTreeid_dc().equals("302")){
			baot="�����ͬ����(7-8)";
		}else if(this.getTreeid_dc().equals("303")){
			baot="�����ͬ����(9-10)";
		}else if(this.getTreeid_dc().equals("302,303")){
			baot="�����ͬ���繫˾(7-10)";
		}else if(this.getTreeid_dc().equals("301,302,303")){
			baot="�����ͬ����(1-10)";
		}
		rt.setDefaultTitle(1, 3, "��λ��" + baot, Table.ALIGN_LEFT);
		
		rt.setBody(new Table(rs, 1, 0, 3));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(1000);
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();
		rt.createDefautlFooter(ArrWidth);
		
		//----------------
		
		
		rt.body.ShowZero=true;
		//ҳ��Ϊ�յ�ʱ����ʾ0,�����˷���һ�г���
		for(int ab=1;ab<=rt.body.getRows();ab++){
			if(rt.body.getCellValue(ab, 6).equals("0")){
				rt.body.setCellValue(ab, 6, "");
			}
		}
		
		
		//----------------
		
		for(int i=0; i<=rt.body.getCols(); i++){
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}
		rt.setDefautlFooter(1, 3, "��ӡ����:" + DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
		rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);

		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		return rt.getAllPagesHtml();
	}


	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			this.setRiqi(null);
			this.setRiqi2(null);
			
			setTreeid_dc(null);
			getDiancmcModels();
			
			this.setBaoblxValue(null);
			this.getIBaoblxModels();
			
			this.setTongjfsValue(null);
			this.getTongjfsModels();
			getSelectData();
		}
		getSelectData();

	}
	
	// ������
	private String riqi;
	public String getRiqi() {
		if (riqi == null || riqi.equals("")) {
			riqi = DateUtil.FormatDate(DateUtil.AddDate(new Date(), -2, DateUtil.AddType_intDay));
		}
		return riqi;
	}

	public void setRiqi(String riqi) {
		if (this.riqi != null && !this.riqi.equals(riqi)) {
			this.riqi = riqi;
		}

	}

	
//	 ������2
	private String riqi2;
	public String getRiqi2() {
		if (riqi2 == null || riqi2.equals("")) {
			riqi2 = DateUtil.FormatDate(DateUtil.AddDate(new Date(), -2, DateUtil.AddType_intDay));
		}
		return riqi2;
	}

	public void setRiqi2(String riqi2) {
		if (this.riqi2 != null && !this.riqi2.equals(riqi2)) {
			this.riqi2 = riqi2;
		}

	}


	private boolean _RefurbishChick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			//getPrintTable();
			_RefurbishChick = false;
		}

	}

    
	public void getSelectData() {
		Toolbar tb1 = new Toolbar("tbdiv");
		
		tb1.addText(new ToolbarText("��������:"));
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiqi());
		df.Binding("riqi", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("riqi");
		tb1.addField(df);
		tb1.addText(new ToolbarText("��"));
		DateField df2 = new DateField();
		df2.setReadOnly(true);
		df2.setValue(this.getRiqi2());
		df2.Binding("riqi2", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df2.setId("riqi2");
		tb1.addField(df2);
		tb1.addText(new ToolbarText("-"));

		
		tb1.addText(new ToolbarText("-"));
		long diancxxb_id = ((Visit) this.getPage().getVisit()).getDiancxxb_id();
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowCheck_Dianc, diancxxb_id, getTreeid_dc(),null,true);

				setDCTree(etu);
				TextField tf = new TextField();
				tf.setId("diancTree_text");
				tf.setWidth(100);
				String[] str=getTreeid_dc().split(",");
				tf.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long.parseLong(str[0])));
				ToolbarButton toolb2 = new ToolbarButton(null, null,
						"function(){diancTree_window.show();}");
				toolb2.setIcon("ext/resources/images/list-items.gif");
				toolb2.setCls("x-btn-icon");
				toolb2.setMinWidth(20);
				
				tb1.addText(new ToolbarText("��λ:"));
				tb1.addField(tf);
				tb1.addItem(toolb2);
		tb1.addText(new ToolbarText("-"));
		
		
		tb1.addText(new ToolbarText("ͳ������:"));
		ComboBox cb = new ComboBox();
		cb.setTransform("BaoblxDropDown");
		cb.setListeners("select:function(){document.Form0.submit();}");
		cb.setId("Baoblx");
		cb.setWidth(100);
		tb1.addField(cb);
		tb1.addText(new ToolbarText("-"));
		
		
		/*tb1.addText(new ToolbarText("��ʾ:"));
		ComboBox tj = new ComboBox();
		tj.setTransform("TongjfsDropDown");
		tj.setListeners("select:function(){document.Form0.submit();}");
		tj.setId("Tongjfs");
		tj.setWidth(100);
		tb1.addField(tj);
		tb1.addText(new ToolbarText("-"));
		
		*/
		
		

		ToolbarButton rbtn = new ToolbarButton(null,"��ѯ","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.addItem(rbtn);

		setToolbar(tb1);

	}
	
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(_value);
	}

	public void getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql));
	}

	public String getTreeid_dc() {
		String treeid = ((Visit) getPage().getVisit()).getString3();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString3(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString3();
	}

	public void setTreeid_dc(String treeid) {
		if(((Visit) getPage().getVisit()).getString3()!=null && !((Visit) getPage().getVisit()).getString3().equals(treeid)){
		}
		((Visit) getPage().getVisit()).setString3(treeid);
	}
	public ExtTreeUtil getDCTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setDCTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}

	public String getTreeScript() {
		return getDCTree().getWindowTreeScript();
	}
	public String getTreeHtml() {
		return getDCTree().getWindowTreeHtml(this);
	}

	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}

	private String _pageLink;

	public String getpageLink() {
		if (!_pageLink.equals("")) {
			return _pageLink;
		} else {
			return "";
		}
	}

	// Page����
	protected void initialize() {
		_pageLink = "";
	}

//	ҳ���½��֤
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
	
	

	public boolean _Baoblxchange = false;
	private IDropDownBean _BaoblxValue;

	public IDropDownBean getBaoblxValue() {
		if(_BaoblxValue==null){
			_BaoblxValue=(IDropDownBean)getIBaoblxModels().getOption(0);
		}
		return _BaoblxValue;
	}

	public void setBaoblxValue(IDropDownBean Value) {
		long id = -2;
		if (_BaoblxValue != null) {
			id = _BaoblxValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_Baoblxchange = true;
			} else {
				_Baoblxchange = false;
			}
		}
		_BaoblxValue = Value;
	}

	private IPropertySelectionModel _IBaoblxModel;

	public void setIBaoblxModel(IPropertySelectionModel value) {
		_IBaoblxModel = value;
	}

	public IPropertySelectionModel getIBaoblxModel() {
		if (_IBaoblxModel == null) {
			getIBaoblxModels();
		}
		return _IBaoblxModel;
	}

	public IPropertySelectionModel getIBaoblxModels() {
		JDBCcon con = new JDBCcon();
		try{
			List fahdwList = new ArrayList();
			//fahdwList.add(new IDropDownBean(0,"����ͳ��"));
			fahdwList.add(new IDropDownBean(1,"�ֳ���ͳ��"));
			fahdwList.add(new IDropDownBean(2,"���ֳ���ͳ��"));
			_IBaoblxModel = new IDropDownModel(fahdwList);

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return _IBaoblxModel;
	}
	
	
	
	
	

///ͳ�Ʒ�ʽ
	public boolean _Tongjfschange = false;
	private IDropDownBean _TongjfsValue;

	public IDropDownBean getTongjfsValue() {
		if(_TongjfsValue==null){
			_TongjfsValue=(IDropDownBean)getTongjfsModels().getOption(0);
		}
		return _TongjfsValue;
	}

	public void setTongjfsValue(IDropDownBean Value) {
		long id = -2;
		if (_TongjfsValue != null) {
			id = _TongjfsValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_Tongjfschange = true;
			} else {
				_Tongjfschange = false;
			}
		}
		_TongjfsValue = Value;
	}

	private IPropertySelectionModel _TongjfsModel;

	public void setTongjfsModel(IPropertySelectionModel value) {
		_TongjfsModel = value;
	}

	public IPropertySelectionModel getTongjfsModel() {
		if (_TongjfsModel == null) {
			getTongjfsModels();
		}
		return _TongjfsModel;
	}

	public IPropertySelectionModel getTongjfsModels() {
		
		try{
			List fangs = new ArrayList();
			//fahdwList.add(new IDropDownBean(0,"����ͳ��"));
			fangs.add(new IDropDownBean(1,"��ʾ�ۼ�"));
			fangs.add(new IDropDownBean(2,"��ʾ����"));
			_TongjfsModel = new IDropDownModel(fangs);

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//con.Close();
		}
		return _TongjfsModel;
	}
}