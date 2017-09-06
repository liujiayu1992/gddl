package com.zhiren.jt.jihgl.ranlzhzb;

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
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.report.ChessboardTable;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Ranmhyysb extends BasePage {
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

	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		return getRanmhyys();
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
	
    //��¼��ѯ������ID
	public long getChaxtjb_id() {
		return getChaxtjValue().getId();
	}
	
	public String getNianf() {
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		return intyear + "";
	}
	
	private String getRanmhyys() {
		JDBCcon con = new JDBCcon();
		ChessboardTable cd = new ChessboardTable();
		Report rt = new Report();
		int[] ArrWidth;
		
		String sql = "select nianf, beginyuef, endyuef from chaxtjb where id = " + getChaxtjb_id();
		ResultSetList rsl = con.getResultSetList(sql);
		String nianf = "";
		String beginyuef = "";
		String endyuef = "";
		if(rsl.next()) {
			nianf = rsl.getString("nianf");
			beginyuef = rsl.getString("beginyuef");
			endyuef = rsl.getString("endyuef");
		}
		String Nianf = (Integer.parseInt(nianf) + 1) + "";
		
        //�б���_ָ������
		StringBuffer col_title = new StringBuffer();
		col_title.append(
				"select biaot as ����, mingc as ָ��, danw as ��λ\n" +
				"  from (select '�ڳ����' as biaot, mingc, '���' as danw, 1 as xuh\n" + 
				"          from item\n" + 
				"         where bianm = 'QICKCML'\n" + 
				"        union\n" + 
				"        select '�ڳ����' as biaot, mingc, 'Ԫ/��' as danw, 2 as xuh\n" + 
				"          from item\n" + 
				"         where bianm = 'QICKCMDJ'\n" + 
				"        union\n" + 
				"  		 select '���ڵ���' as biaot, '����ú��' as mingc, '���' as danw, 3 as xuh\n" +
				"		   from dual\n" +
				"        union\n" +
				"  		 select '���ڵ���' as biaot, '����' as mingc, '���' as danw, 4 as xuh\n" +
				"		   from dual\n" +
				"        union\n" +
				"  		 select '���ڵ���' as biaot, '������ֵ' as mingc, 'KJ/kg' as danw, 5 as xuh\n" +
				"		   from dual\n" +
				"        union\n" +
				"  		 select '���ڵ���' as biaot, '�����ۺϼ�(����˰)' as mingc, 'Ԫ/��' as danw, 6 as xuh\n" +
				"		   from dual\n" +
				"        union\n" +
				"  		 select '���ڵ���' as biaot, '������ú���ۣ�����˰��' as mingc, 'Ԫ/��' as danw, 7 as xuh\n" +
				"		   from dual\n" +
				"        union\n" +
				"        select '�������' as biaot, mingc, '���' as danw, 8 as xuh\n" + 
				"          from item\n" + 
				"         where bianm = 'CHUCSHL'\n" + 
				"        union\n" + 
				"        select '���繩�Ⱥ���' as biaot, mingc, '���' as danw, 9 as xuh\n" + 
				"          from item\n" + 
				"         where bianm = 'RULHYYML'\n" + 
				"        union\n" + 
				"        select '���繩�Ⱥ���' as biaot, mingc, 'Ԫ/��' as danw, 10 as xuh\n" + 
				"          from item\n" + 
				"         where bianm = 'LUQFY_BHS'\n" + 
				"        union\n" + 
				"        select '���繩�Ⱥ���' as biaot, mingc, 'KJ/kg' as danw, 11 as xuh\n" + 
				"          from item\n" + 
				"         where bianm = 'RULMPJRZ'\n" + 
				"        union\n" + 
				"        select '���繩�Ⱥ���' as biaot, mingc, 'KJ/kg' as danw, 12 as xuh\n" + 
				"          from item\n" + 
				"         where bianm = 'REZC'\n" + 
				"        union\n" + 
				"        select '���繩�Ⱥ���' as biaot, mingc, 'Ԫ/��' as danw, 13 as xuh\n" + 
				"          from item\n" + 
				"         where bianm = 'RULYMDJ_BHS'\n" + 
				"        union\n" + 
				"        select '���繩�Ⱥ���' as biaot, mingc, 'Ԫ/��' as danw, 14 as xuh\n" + 
				"          from item\n" + 
				"         where bianm = 'RULMMZBMDJ'\n" + 
				"        union\n" + 
				"        select '�������ã�����������' as biaot, mingc, '���' as danw, 15 as xuh\n" + 
				"          from item\n" + 
				"         where bianm = 'QITHY_HAOYL'\n" + 
				"        union\n" + 
				"        select '�������ã�����������' as biaot, mingc, 'KJ/kg' as danw, 16 as xuh\n" + 
				"          from item\n" + 
				"         where bianm = 'QITHY_REZ'\n" + 
				"        union\n" + 
				"        select '�������ã�����������' as biaot, mingc, 'Ԫ/��' as danw, 17 as xuh\n" + 
				"          from item\n" + 
				"         where bianm = 'QITHY_TIANRMDJ_BHS'\n" + 
				"        union\n" + 
				"        select '�������ã�����������' as biaot, mingc, 'Ԫ/��' as danw, 18 as xuh\n" + 
				"          from item\n" + 
				"         where bianm = 'QITHY_BIAOMDJ_BHS'\n" + 
				"        union\n" + 
				"        select '��ĩ���' as biaot, mingc, '���' as danw, 19 as xuh\n" + 
				"          from item\n" + 
				"         where bianm = 'QIMKCML'\n" + 
				"        union\n" + 
				"        select '��ĩ���' as biaot, mingc, 'Ԫ/��' as danw, 20 as xuh\n" + 
				"          from item\n" + 
				"         where bianm = 'QIMKCMDJ')\n" + 
				" order by xuh"
		);
		
		//�б���_�糧����
		StringBuffer row_title = new StringBuffer();
		row_title.append(
				"select vwdianc.mingc as �糧, 'ȼúС��' as diq, t.mingc as ʱ��\n" +
				"  from vwdianc,\n" + 
				"       (select *\n" + 
				"          from (select 3 as id, '"+nianf+"��"+beginyuef+"-"+endyuef+"��' as mingc\n" + 
				"                  from dual\n" + 
				"                union\n" + 
				"                select 2 as id, '"+nianf+"��Ԥ��' as mingc\n" + 
				"                  from dual\n" + 
				"                union\n" + 
				"                select 1 as id, '"+Nianf+"��Ԥ��' as mingc from dual)\n" + 
				"         order by id desc) t\n" + 
				" order by vwdianc.fuid desc, vwdianc.mingc, t.id desc"
		);
		
		//����
		StringBuffer data = new StringBuffer();
		data.append(

				"select decode(d.mingc,null,d.fgsmc,d.mingc) as �糧,\n" +
				"       n.tablename as ʱ��,\n" + 
				"       'ȼúС��' as diq,\n" +
				"       d.fgsmc as �ֹ�˾����,\n" +
				"       n.bianm,\n" + 
				"       decode(n.bianm,\n" + 
				"              'QICKCML',\n" + 
				"              '�ڳ����',\n" + 
				"              'QICKCMDJ',\n" + 
				"              '�ڳ����',\n" + 
				"              'DAOCML',\n" + 
				"              '���ڵ���',\n" + 
				"              'YUNS',\n" + 
				"              '���ڵ���',\n" + 
				"              'DAOCRZ',\n" + 
				"              '���ڵ���',\n" + 
				"              'DAOCZHJBHS',\n" + 
				"              '���ڵ���',\n" + 
				"              'DAOCBMDJBHS',\n" + 
				"              '���ڵ���',\n" + 
				"              'CHUCSHL',\n" + 
				"              '�������',\n" + 
				"              'RULHYYML',\n" + 
				"              '���繩�Ⱥ���',\n" + 
				"              'LUQFY_BHS',\n" + 
				"              '���繩�Ⱥ���',\n" + 
				"              'RULMPJRZ',\n" + 
				"              '���繩�Ⱥ���',\n" + 
				"              'REZC',\n" + 
				"              '���繩�Ⱥ���',\n" + 
				"              'RULYMDJ_BHS',\n" + 
				"              '���繩�Ⱥ���',\n" + 
				"              'RULMMZBMDJ',\n" + 
				"              '���繩�Ⱥ���',\n" + 
				"              'QITHY_HAOYL',\n" + 
				"              '�������ã�����������',\n" + 
				"              'QITHY_REZ',\n" + 
				"              '�������ã�����������',\n" + 
				"              'QITHY_TIANRMDJ_BHS',\n" + 
				"              '�������ã�����������',\n" + 
				"              'QITHY_BIAOMDJ_BHS',\n" + 
				"              '�������ã�����������',\n" + 
				"              'QIMKCML',\n" + 
				"              '��ĩ���',\n" + 
				"              'QIMKCMDJ',\n" + 
				"              '��ĩ���',\n" + 
				"              '') as ����,\n" + 
				"       n.mingc as ָ��,\n" + 
				"       decode(n.bianm,\n" + 
				"              'QICKCML',\n" + 
				"              '���',\n" + 
				"              'QICKCMDJ',\n" + 
				"              'Ԫ/��',\n" + 
				"              'DAOCML',\n" + 
				"              '���',\n" + 
				"              'YUNS',\n" + 
				"              '���',\n" + 
				"              'DAOCRZ',\n" + 
				"              'KJ/kg',\n" + 
				"              'DAOCZHJBHS',\n" + 
				"              'Ԫ/��',\n" + 
				"              'DAOCBMDJBHS',\n" + 
				"              'Ԫ/��',\n" + 
				"              'CHUCSHL',\n" + 
				"              '���',\n" + 
				"              'RULHYYML',\n" + 
				"              '���',\n" + 
				"              'LUQFY_BHS',\n" + 
				"              'Ԫ/��',\n" + 
				"              'RULMPJRZ',\n" + 
				"              'KJ/kg',\n" + 
				"              'REZC',\n" + 
				"              'KJ/kg',\n" + 
				"              'RULYMDJ_BHS',\n" + 
				"              'Ԫ/��',\n" + 
				"              'RULMMZBMDJ',\n" + 
				"              'Ԫ/��',\n" + 
				"              'QITHY_HAOYL',\n" + 
				"              '���',\n" + 
				"              'QITHY_REZ',\n" + 
				"              'KJ/kg',\n" + 
				"              'QITHY_TIANRMDJ_BHS',\n" + 
				"              'Ԫ/��',\n" + 
				"              'QITHY_BIAOMDJ_BHS',\n" + 
				"              'Ԫ/��',\n" + 
				"              'QIMKCML',\n" + 
				"              '���',\n" + 
				"              'QIMKCMDJ',\n" + 
				"              'Ԫ/��',\n" + 
				"              '') as ��λ,\n" + 
				"       sum(n.shul) as shul\n" + 
				"  from ((select diancxxb_id,\n" + 
				"                '"+nianf+"��"+beginyuef+"-"+endyuef+"��' as tablename,\n" + 
				"                i.mingc,\n" + 
				"                i.bianm,\n" + 
				"                shijwc as shul\n" + 
				"           from shijwcb wc, item i\n" + 
				"          where wc.item_id = i.id\n" + 
				"            and (bianm = 'QICKCML' or bianm = 'QICKCMDJ' or\n" + 
				"                bianm = 'CHUCSHL' or bianm = 'LUQFY_BHS' or\n" + 
				"                bianm = 'RULMPJRZ' or bianm = 'REZC' or\n" + 
				"                bianm = 'RULYMDJ_BHS' or bianm = 'RULMMZBMDJ' or\n" + 
				"                bianm = 'QITHY_HAOYL' or bianm = 'QITHY_REZ' or\n" + 
				"                bianm = 'QITHY_TIANRMDJ_BHS' or bianm = 'QITHY_BIAOMDJ_BHS' or\n" + 
				"                bianm = 'QIMKCML' or bianm = 'QIMKCMDJ')\n" + 
				"            and chaxtjb_id = "+getChaxtjb_id()+"\n" + 
				"         union\n" + 
				"         select diancxxb_id,\n" + 
				"                '"+nianf+"��"+beginyuef+"-"+endyuef+"��' as tablename,\n" + 
				"                i.mingc,\n" + 
				"                i.bianm,\n" + 
				"                shijwc / 10000 as shul\n" + 
				"           from shijwcb wc, item i\n" + 
				"          where wc.item_id = i.id\n" + 
				"            and bianm = 'RULHYYML'\n" + 
				"         union\n" + 
				"         select diancxxb_id,\n" + 
				"                '"+nianf+"��"+beginyuef+"-"+endyuef+"��' as tablename,\n" + 
				"                '����ú��' as mingc,\n" + 
				"                'DAOCML' as bianm,\n" + 
				"                caigl as shul\n" + 
				"           from ranmzbsjb\n" + 
				"          where chaxtjb_id = "+getChaxtjb_id()+"\n" + 
				"         union\n" + 
				"         select diancxxb_id,\n" + 
				"                '"+nianf+"��"+beginyuef+"-"+endyuef+"��' as tablename,\n" + 
				"                '����' as mingc,\n" + 
				"                'YUNS' as bianm,\n" + 
				"                yuns as shul\n" + 
				"           from ranmzbsjb\n" + 
				"          where chaxtjb_id = "+getChaxtjb_id()+"\n" + 
				"         union\n" + 
				"         select diancxxb_id,\n" + 
				"                '"+nianf+"��"+beginyuef+"-"+endyuef+"��' as tablename,\n" + 
				"                '������ֵ' as mingc,\n" + 
				"                'DAOCRZ' as bianm,\n" + 
				"                daocrz as shul\n" + 
				"           from ranmzbsjb\n" + 
				"          where chaxtjb_id = "+getChaxtjb_id()+"\n" + 
				"         union\n" + 
				"         select diancxxb_id,\n" + 
				"                '"+nianf+"��"+beginyuef+"-"+endyuef+"��' as tablename,\n" + 
				"                '�����ۺϼ�(����˰)' as mingc,\n" + 
				"                'DAOCZHJBHS' as bianm,\n" + 
				"                daoczhjbhs as shul\n" + 
				"           from ranmzbsjb\n" + 
				"          where chaxtjb_id = "+getChaxtjb_id()+"\n" + 
				"         union\n" + 
				"         select diancxxb_id,\n" + 
				"                '"+nianf+"��"+beginyuef+"-"+endyuef+"��' as tablename,\n" + 
				"                '������ú���ۣ�����˰��' as mingc,\n" + 
				"                'DAOCBMDJBHS' as bianm,\n" + 
				"                daocbmdjbhs as shul\n" + 
				"           from ranmzbsjb\n" + 
				"          where chaxtjb_id = "+getChaxtjb_id()+") union\n" + 
				"        (select diancxxb_id,\n" + 
				"                '"+nianf+"��Ԥ��' as tablename,\n" + 
				"                i.mingc,\n" + 
				"                i.bianm,\n" + 
				"                quannyj as shul\n" + 
				"           from quannyjb q, item i\n" + 
				"          where q.item_id = i.id\n" + 
				"            and to_char(riq, 'yyyy') = '"+nianf+"'\n" + 
				"            and (bianm = 'QICKCML' or bianm = 'QICKCMDJ' or bianm = 'CHUCSHL' or\n" + 
				"                bianm = 'LUQFY_BHS' or bianm = 'RULMPJRZ' or bianm = 'REZC' or\n" + 
				"                bianm = 'RULYMDJ_BHS' or bianm = 'RULMMZBMDJ' or\n" + 
				"                bianm = 'QITHY_HAOYL' or bianm = 'QITHY_REZ' or\n" + 
				"                bianm = 'QITHY_TIANRMDJ_BHS' or bianm = 'QITHY_BIAOMDJ_BHS' or\n" + 
				"                bianm = 'QIMKCML' or bianm = 'QIMKCMDJ')\n" + 
				"         union\n" + 
				"         select diancxxb_id,\n" + 
				"                '"+nianf+"��Ԥ��' as tablename,\n" + 
				"                i.mingc,\n" + 
				"                i.bianm,\n" + 
				"                quannyj / 10000 as shul\n" + 
				"           from quannyjb q, item i\n" + 
				"          where q.item_id = i.id\n" + 
				"            and to_char(riq, 'yyyy') = '"+nianf+"'\n" + 
				"            and bianm = 'RULHYYML'\n" + 
				"         union\n" + 
				"         select diancxxb_id,\n" + 
				"                '"+nianf+"��Ԥ��' as tablename,\n" + 
				"                '����ú��' as mingc,\n" + 
				"                'DAOCML' as bianm,\n" + 
				"                sum(caigl) as shul\n" + 
				"           from ranmcgysb\n" + 
				"          where to_char(nianf, 'yyyy') = '"+nianf+"'\n" + 
				"          group by diancxxb_id, gongysb_id\n" + 
				"         union\n" + 
				"         select diancxxb_id,\n" + 
				"                '"+nianf+"��Ԥ��' as tablename,\n" + 
				"                '����' as mingc,\n" + 
				"                'YUNS' as bianm,\n" + 
				"                sum(yuns) as shul\n" + 
				"           from ranmcgysb\n" + 
				"          where to_char(nianf, 'yyyy') = '"+nianf+"'\n" + 
				"          group by diancxxb_id, gongysb_id\n" + 
				"         union\n" + 
				"         select diancxxb_id,\n" + 
				"                '"+nianf+"��Ԥ��' as tablename,\n" + 
				"                '������ֵ' as mingc,\n" + 
				"                'DAOCRZ' as bianm,\n" + 
				"                sum(daocrz) as shul\n" + 
				"           from ranmcgysb\n" + 
				"          where to_char(nianf, 'yyyy') = '"+nianf+"'\n" + 
				"          group by diancxxb_id, gongysb_id\n" + 
				"         union\n" + 
				"         select diancxxb_id,\n" + 
				"                '"+nianf+"��Ԥ��' as tablename,\n" + 
				"                '�����ۺϼ�(����˰)' as mingc,\n" + 
				"                'DAOCZHJBHS' as bianm,\n" + 
				"                sum(daoczhjbhs) as shul\n" + 
				"           from ranmcgysb\n" + 
				"          where to_char(nianf, 'yyyy') = '"+nianf+"'\n" + 
				"          group by diancxxb_id, gongysb_id\n" + 
				"         union\n" + 
				"         select diancxxb_id,\n" + 
				"                '"+nianf+"��Ԥ��' as tablename,\n" + 
				"                '������ú���ۣ�����˰��' as mingc,\n" + 
				"                'DAOCBMDJBHS' as bianm,\n" + 
				"                sum(daocbmdjbhs) as shul\n" + 
				"           from ranmcgysb\n" + 
				"          where to_char(nianf, 'yyyy') = '"+nianf+"'\n" + 
				"          group by diancxxb_id, gongysb_id) union\n" + 
				"        (select diancxxb_id,\n" + 
				"                '"+Nianf+"��Ԥ��' as tablename,\n" + 
				"                i.mingc,\n" + 
				"                i.bianm,\n" + 
				"                quannyj as shul\n" + 
				"           from quannyjb q, item i\n" + 
				"          where q.item_id = i.id\n" + 
				"            and to_char(riq, 'yyyy') = '"+Nianf+"'\n" + 
				"            and (bianm = 'QICKCML' or bianm = 'QICKCMDJ' or bianm = 'CHUCSHL' or\n" + 
				"                bianm = 'LUQFY_BHS' or bianm = 'RULMPJRZ' or bianm = 'REZC' or\n" + 
				"                bianm = 'RULYMDJ_BHS' or bianm = 'RULMMZBMDJ' or\n" + 
				"                bianm = 'QITHY_HAOYL' or bianm = 'QITHY_REZ' or\n" + 
				"                bianm = 'QITHY_TIANRMDJ_BHS' or bianm = 'QITHY_BIAOMDJ_BHS' or\n" + 
				"                bianm = 'QIMKCML' or bianm = 'QIMKCMDJ')\n" + 
				"         union\n" + 
				"         select diancxxb_id,\n" + 
				"                '"+Nianf+"��Ԥ��' as tablename,\n" + 
				"                i.mingc,\n" + 
				"                i.bianm,\n" + 
				"                quannyj / 10000 as shul\n" + 
				"           from quannyjb q, item i\n" + 
				"          where q.item_id = i.id\n" + 
				"            and to_char(riq, 'yyyy') = '"+Nianf+"'\n" + 
				"            and bianm = 'RULHYYML'\n" + 
				"         union\n" + 
				"         select diancxxb_id,\n" + 
				"                '"+Nianf+"��Ԥ��' as tablename,\n" + 
				"                '����ú��' as mingc,\n" + 
				"                'DAOCML' as bianm,\n" + 
				"                sum(caigl) as shul\n" + 
				"           from ranmcgysb\n" + 
				"          where to_char(nianf, 'yyyy') = '"+Nianf+"'\n" + 
				"          group by diancxxb_id, gongysb_id\n" + 
				"         union\n" + 
				"         select diancxxb_id,\n" + 
				"                '"+Nianf+"��Ԥ��' as tablename,\n" + 
				"                '����' as mingc,\n" + 
				"                'YUNS' as bianm,\n" + 
				"                sum(yuns) as shul\n" + 
				"           from ranmcgysb\n" + 
				"          where to_char(nianf, 'yyyy') = '"+Nianf+"'\n" + 
				"          group by diancxxb_id, gongysb_id\n" + 
				"         union\n" + 
				"         select diancxxb_id,\n" + 
				"                '"+Nianf+"��Ԥ��' as tablename,\n" + 
				"                '������ֵ' as mingc,\n" + 
				"                'DAOCRZ' as bianm,\n" + 
				"                sum(daocrz) as shul\n" + 
				"           from ranmcgysb\n" + 
				"          where to_char(nianf, 'yyyy') = '"+Nianf+"'\n" + 
				"          group by diancxxb_id, gongysb_id\n" + 
				"         union\n" + 
				"         select diancxxb_id,\n" + 
				"                '"+Nianf+"��Ԥ��' as tablename,\n" + 
				"                '�����ۺϼ�(����˰)' as mingc,\n" + 
				"                'DAOCZHJBHS' as bianm,\n" + 
				"                sum(daoczhjbhs) as shul\n" + 
				"           from ranmcgysb\n" + 
				"          where to_char(nianf, 'yyyy') = '"+Nianf+"'\n" + 
				"          group by diancxxb_id, gongysb_id\n" + 
				"         union\n" + 
				"         select diancxxb_id,\n" + 
				"                '"+Nianf+"��Ԥ��' as tablename,\n" + 
				"                '������ú���ۣ�����˰��' as mingc,\n" + 
				"                'DAOCBMDJBHS' as bianm,\n" + 
				"                sum(daocbmdjbhs) as shul\n" + 
				"           from ranmcgysb\n" + 
				"          where to_char(nianf, 'yyyy') = '"+Nianf+"'\n" + 
				"          group by diancxxb_id, gongysb_id)) n,\n" + 
				"       vwdianc d\n" + 
				" where n.diancxxb_id = d.id\n" + 
				" group by rollup(n.tablename, n.mingc, d.fgsmc,n.bianm,d.mingc)\n" + 
				"having not grouping(n.bianm)=1"
		);
		
		try {
			rsl = con.getResultSetList(data.toString());
			if(!rsl.next()) {
				return "�����ݣ�";
			}
			rsl.close();
		} catch (RuntimeException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		} 
		
		cd.setRowNames("�糧,diq,ʱ��");
	    cd.setColNames("����,ָ��,��λ");
	    cd.setDataNames("shul");
	    cd.setDataOnRow(false);
	    cd.setRowToCol(false);
	    //�������̱�����
	    cd.setData(row_title.toString(), col_title.toString(), data.toString());
	    
	    ArrWidth = new int[cd.DataTable.getCols()];
		for (int i = 1; i < ArrWidth.length; i++) {
			ArrWidth[i] = 65;
		}
		ArrWidth[0] = 80;
		rt.setBody(cd.DataTable);
		rt.body.setWidth(ArrWidth);
		
		rt.setTitle(nianf + "ȼú����Ԥ���", ArrWidth);
				
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.setDefaultTitle(1, 5, "���λ:" + ((Visit) getPage().getVisit()).getDiancqc(),
						Table.ALIGN_LEFT);
		
		rt.body.setPageRows(21);
		
		sql = "select id from diancxxb";
		rsl = con.getResultSetList(sql);
		for(int i = 1; i <= rsl.getRows(); i++) {
			rt.body.merge(3*i+1, 2, 3*i+3, 2);
		}
		rt.body.mergeCol(1);
		rt.body.merge(1, 1, 3, 1);
		rt.body.merge(1, 2, 3, 3);
		rt.body.merge(1, 4, 1, 5);
		
		rt.body.merge(1, 6, 1, 10);
		rt.body.merge(1, 12, 1, 17);
		rt.body.merge(1, 18, 1, 21);
		rt.body.merge(1, 22, 1, rt.body.getCols());
		rt.createDefautlFooter(ArrWidth);
		rt.body.setCells(1, 1, 3, 1, Table.PER_VALUE, "��λ����");
		rt.body.setCells(1, 2, 3, 3, Table.PER_VALUE, "��𼰵���");
		rt.body.setCells(2, 4, 2, 4, Table.PER_VALUE, "ú��");
		rt.body.setCells(2, 5, 2, 5, Table.PER_VALUE, "��Ȼú��");
		rt.body.setCells(2, 11, 2, 11, Table.PER_VALUE, "�������");
		rt.body.merge(1, 11, 2, 11);
		rt.body.setCells(2, 12, 2, 12, Table.PER_VALUE, "���繩�Ⱥ�����");
		rt.body.setCells(2, 13, 2, 13, Table.PER_VALUE, "¯ǰ���ã�����˰��");
		rt.body.setCells(2, 14, 2, 14, Table.PER_VALUE, "��¯��ֵ");
		rt.body.setCells(2, 15, 2, 15, Table.PER_VALUE, "��ֵ��");
		rt.body.setCells(2, 16, 2, 16, Table.PER_VALUE, "��¯ԭú���ۣ�����˰��");
		rt.body.setCells(2, 17, 2, 17, Table.PER_VALUE, "��¯ú��ú����(����˰)");
		rt.body.setCells(2, 18, 2, 18, Table.PER_VALUE, "������");
		rt.body.setCells(2, 19, 2, 19, Table.PER_VALUE, "��ֵ");
		rt.body.setCells(2, 20, 2, 20, Table.PER_VALUE, "��Ȼú����(����˰)");
		rt.body.setCells(2, 21, 2, 21, Table.PER_VALUE, "��ú����(����˰)");
		rt.body.setCells(2, 22, 2, 22, Table.PER_VALUE, "ú��");
		rt.body.setCells(2, 23, 2, 23, Table.PER_VALUE, "��Ȼú��");

		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		return rt.getAllPagesHtml();

	}

	public String getPageHome() {
		if (((Visit) getPage().getVisit()).getboolean1()) {
			return "window.location = '" + MainGlobal.getHomeContext(this)
					+ "';";
		} else {
			return "";
		}
	}

	private void getToolbars() {
		Toolbar tb1 = new Toolbar("tbdiv");

		 // ���ComBox
		tb1.addText(new ToolbarText("���:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NIANF");
		nianf.setId("NIANF");// ���Զ�ˢ�°�
		nianf.setLazyRender(true);
		nianf.setEditable(false);
		nianf.setWidth(60);
		nianf.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(nianf);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("��ѯ����:"));
		ComboBox chaxtj = new ComboBox();
		chaxtj.setTransform("CHAXTJB_ID");
		chaxtj.setWidth(150);
		chaxtj.setListeners("select:function(){document.Form0.submit();}");
		chaxtj.setLazyRender(true);
		tb1.addField(chaxtj);
		tb1.addText(new ToolbarText("-"));

		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(),
				"-1".equals(getTreeid()) ? null : getTreeid());
		setTree(etu);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(120);
		tf.setValue(((IDropDownModel) getIDiancmcModel()).getBeanValue(Long
				.parseLong(getTreeid())));

		ToolbarButton tb2 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);

		tb1.addText(new ToolbarText("��λ:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		tb1.addText(new ToolbarText("-"));

		ToolbarButton tb = new ToolbarButton(null, "ˢ��",
				"function(){document.Form0.submit();}");

		tb1.addItem(tb);
		setToolbar(tb1);
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		visit.setboolean1(false);
		if (visit.getRenyID() == -1) {
			visit.setboolean1(true);
			return;
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			
			setChaxtjModel(null);
			setChaxtjValue(null);
			visit.setDropDownBean2(null);
			visit.setProSelectionModel2(null);

			visit.setDefaultTree(null);
			setTreeid(null);
		}
		
		if(((Visit) getPage().getVisit()).getboolean2()){
			setChaxtjModel(null);
			setChaxtjValue(null);
		}
		
		getToolbars();
		blnIsBegin = true;
		((Visit) getPage().getVisit()).setboolean2(false);
	}

	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString2();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString2(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}

	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString2(treeid);
	}

	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}

	public String getTreeHtml() {
		return getTree().getWindowTreeHtml(this);
	}

	public String getTreeScript() {
		return getTree().getWindowTreeScript();
	}

    //��ѯ����������
	public IDropDownBean getChaxtjValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
			if (getChaxtjModel().getOptionCount()>0){
				((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean)getChaxtjModel().getOption(0));
			}
		}
		return ((Visit)getPage().getVisit()).getDropDownBean1();
	}

	public void setChaxtjValue(IDropDownBean Value) {
		((Visit)getPage().getVisit()).setDropDownBean1(Value);
	}

	public void setChaxtjModel(IPropertySelectionModel value) {
		((Visit)getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getChaxtjModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			getIChaxtjModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}

	public void getIChaxtjModels() {
		
		String sql = "select id, mingc from chaxtjb where nianf = " + getNianf();
		((Visit)getPage().getVisit()).setProSelectionModel1(new IDropDownModel(sql));
	}
	
	// �糧����
	public boolean _diancmcchange = false;

	private IDropDownBean _DiancmcValue;

	public IDropDownBean getDiancmcValue() {
		if (_DiancmcValue == null) {
			_DiancmcValue = (IDropDownBean) getIDiancmcModel().getOption(0);
		}
		return _DiancmcValue;
	}

	public void setDiancmcValue(IDropDownBean Value) {
		long id = -2;
		if (_DiancmcValue != null) {
			id = _DiancmcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_diancmcchange = true;
			} else {
				_diancmcchange = false;
			}
		}
		_DiancmcValue = Value;
	}

	private IPropertySelectionModel _IDiancmcModel;

	public void setIDiancmcModel(IPropertySelectionModel value) {
		_IDiancmcModel = value;
	}

	public IPropertySelectionModel getIDiancmcModel() {
		if (_IDiancmcModel == null) {
			getIDiancmcModels();
		}
		return _IDiancmcModel;
	}

	public void getIDiancmcModels() {
		String sql = "";
		sql = "select d.id,d.mingc from diancxxb d order by d.mingc desc";

		_IDiancmcModel = new IDropDownModel(sql);
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
	
	// ���
	public IPropertySelectionModel getNianfModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getNianfModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public IDropDownBean getNianfValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
				Object obj = getNianfModel().getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					((Visit) getPage().getVisit())
							.setDropDownBean2((IDropDownBean) obj);
					break;
				}
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();

	}

	public void setNianfValue(IDropDownBean Value) {
		if (((Visit) getPage().getVisit()).getDropDownBean2() != Value) {
			((Visit) getPage().getVisit()).setboolean2(true);
		}
		((Visit) getPage().getVisit()).setDropDownBean2(Value);
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(listNianf));
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(_value);
	}
}
