drop table JIEKFSPZB;
-- Create table-------
create table JIEKFSPZB
(
  id      NUMBER(15) not null,
  renwmc  VARCHAR2(40) not null,
  renwsql VARCHAR2(4000) not null,
  renwbs  VARCHAR2(50) not null,
  renwtj  VARCHAR2(50) not null,
  beiz    VARCHAR2(100),
  mingllx VARCHAR2(50),
  gengxy  VARCHAR2(50)
)
;
-- Add comments to the table 
comment on table JIEKFSPZB
  is '接口发送配置';
-- Add comments to the columns 
comment on column JIEKFSPZB.renwmc
  is '任务名：发货，车皮，质量，质量子表';
comment on column JIEKFSPZB.renwsql
  is '任务sql:';
comment on column JIEKFSPZB.renwbs
  is '任务表标识 如：f.fahb_id';
comment on column JIEKFSPZB.renwtj
  is '任务条件的值 是id的值或日期';
comment on column JIEKFSPZB.beiz
  is '备注';
-- Create/Recreate primary, unique and foreign key constraints 
alter table JIEKFSPZB
  add constraint MK_JIEKFSPZB primary key (ID)
  using index 
;
-------------------
insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (34, 'gongyssqpfb', 'select
    ID,
    FUID,
    XUH,
    MINGC,
    QUANC,
    PINY,
    BIANM,
    DANWDZ,
    FADDBR,
    WEITDLR,
    KAIHYH,
    ZHANGH,
    DIANH,
    SHUIH,
    YOUZBM,
    CHUANZ,
    MEITLY,
    MEIZ,
    CHUBNL,
    KAICNL,
    KAICNX,
    SHENGCNL,
    GONGYNL,
    LIUX,
    YUNSFS,
    SHICCGL,
    ZHONGDHT,
    YUNSNL,
    HEZNX,
    RONGQGX,
    XINY,
    GONGSXZ,
    KEGYWFMZ,
    KEGYWFMZZB,
    SHENGFB_ID,
    SHIFSS,
    SHANGSDZ,
    ZICBFB,
    SHOUMBFB,
    QITBFB,
    BEIZ,
    CHENGSB_ID,
    (select max(diancxxb_id) from fahb) diancxxb_id
  from gongysb %% ', ' where gongysb.id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (35, 'meikxxbsqpfb', 'SELECT meikxxb.ID, meikxxb.XUH, meikxxb.BIANM, meikxxb.MINGC, meikxxb.QUANC, meikxxb.PINY, meikxxb.SHENGFB_ID as SHENGFB_ID, meikxxb.LEIB, (select bianm from jihkjb where id = meikxxb.JIHKJB_ID) as JIHKJB_ID, meikxxb.LEIX, meikxxb.BEIZ, meikxxb.DANWDZ, meikxxb.CHENGSB_ID as CHENGSB_ID, (select decode(max(diancxxb_id),309,306,max(diancxxb_id)) from fahb) diancxxb_id, meikdqb.bianm as meikdqbm from meikxxb, gongysb, meikdqb WHERE gongysb.fuid=meikdqb.id AND meikxxb.meikdq_id = gongysb.id %%', ' and meikxxb.id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (36, 'shujshb', 'select ID,
to_char(RIQ,''yyyy-mm-dd'')riq,
SHUJID,
DIANCXXB_ID,
MOKMC,
ZHUANGT
from shujshb %% ', ' where shujshb.id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (37, 'shujshzb', 'select ID,
SHUJSHB_ID,
to_char(CAOZSJ,''yyyy-mm-dd hh24:mi:ss'') CAOZSJ,
to_char(SHENQSJ,''yyyy-mm-dd hh24:mi:ss'') SHENQSJ,
MIAOS,
CAOZY,
ZHUANGT,
BEIZ
from shujshzb %% ', ' where shujshzb.id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (38, 'diaor16bb', 'select diaor16bb.ID,
to_char(RIQ,''yyyy-mm-dd'')riq,
FENX,
DIANCXXB_ID,
gongysb.shangjgsbm GONGYSB_ID,
PINZ,
SHANGYJC,
KUANGFGYL,
YUNS,
KUID,
FARL,
HUIF,
SHUIF,
HUIFF,
QITSRL,
SHIJHYLHJ,
SHIJHYLFDY,
SHIJHYLGRY,
SHIJHYLQTY,
SHIJHYLZCSH,
DIAOCL,
PANYPK,
YUEMJC,
to_char(DIANCSCSJ,''yyyy-mm-dd hh24:mi:ss'')DIANCSCSJ,
to_char(JIESSJ,''yyyy-mm-dd hh24:mi:ss'')jiessj,
JIESR,
diaor16bb.BEIZ,
DIANCSCWJM
from diaor16bb,gongysb where diaor16bb.gongysb_id=gongysb.id %% ', ' and diaor16bb.id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (39, 'diaor01bb', '
select ID,
to_char(RIQ,''yyyy-mm-dd'')riq,
FENX,
DIANCXXB_ID,
FADSBRL,
MEITSG,
MEITHYHJ,
MEITHYFD,
MEITHYGR,
MEITHYQT,
MEITHYSH,
MEITKC,
SHIYSG,
SHIYHYHJ,
SHIYHYFD,
SHIYHYGR,
SHIYHYQT,
SHIYHYSH,
SHIYKC,
FADL,
GONGRL,
BIAOZMHFD,
BIAOZMHGR,
TIANRMHFD,
TIANRMHGR,
BIAOZMLFD,
BIAOZMLGR,
ZONGHRL,
ZONGHM,
to_char(DIANCSCSJ,''yyyy-mm-dd hh24:mi:ss'')DIANCSCSJ,
to_char(JIESSJ,''yyyy-mm-dd hh24:mi:ss'')jiessj,
JIESR,
BEIZ,
DIANCSCWJM
from diaor01bb %% ', ' where diaor01bb.id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (40, 'diaor03bb', '
select SUOPSL,
SUOPJE,
SHUOM,
to_char(DIANCSCSJ,''yyyy-mm-dd hh24:mi:ss'')DIANCSCSJ,
to_char(JIESSJ,''yyyy-mm-dd hh24:mi:ss'')jiessj,
JIESR,
diaor03bb.BEIZ,
DIANCSCWJM,
diaor03bb.ID,
to_char(RIQ,''yyyy-mm-dd'')riq,
FENX,
DIANCXXB_ID,
gongysb.shangjgsbm GONGYSB_ID,
JINCSL,
CHOUCSL,
ZHANJCM,
GUOH,
JIANC,
YINGDSL,
YINGDZJE,
KUID,
KUIDZJE,
YUNSFSB_ID
from diaor03bb,gongysb where diaor03bb.gongysb_id=gongysb.id %% ', ' and diaor03bb.id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (41, 'diaor04bb', '

select diaor04bb.ID,
to_char(RIQ,''yyyy-mm-dd'')riq,
FENX,
DIANCXXB_ID,
gongysb.shangjgsbm GONGYSB_ID,
PINZ,
JINCSL,
YANSSL,
JIANZL,
KUANGFFRL,
KUANGFDJ,
KUANGFSF,
KUANGFHF,
KUANGFHFF,
KUANGFLF,
CHANGFFRL,
CHANGFDJ,
CHANGFSF,
CHANGFHF,
CHANGFHFF,
CHANGFLF,
DENGJC,
REJC,
BUFL,
DANJC,
ZONGJE,
SUOPJE,
RELSP,
KUIKSPSL,
LIUSP,
LIUSPSL,
to_char(DIANCSCSJ,''yyyy-mm-dd hh24:mi:ss'')DIANCSCSJ,
to_char(JIESSJ,''yyyy-mm-dd hh24:mi:ss'')jiessj,
JIESR,
diaor04bb.BEIZ,
DIANCSCWJM,
YUNSFSB_ID
from diaor04bb,gongysb where diaor04bb.gongysb_id=gongysb.id %% ', ' and diaor04bb.id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (42, 'diaor08bb', '

select diaor08bb.ID,
to_char(RIQ,''yyyy-mm-dd'')riq,
FENX,
DIANCXXB_ID,
gongysb.shangjgsbm GONGYSB_ID,
MEIL,
DAOCZHJ,
KUANGJ,
ZENGZSE,
JIAOHQYZF,
TIELYF,
TIEYSE,
TIELZF,
SHUILLYF,
SHUIYF,
SHUIYSE,
SHUIYZF,
QIYF,
GANGZF,
DAOZZF,
QITFY,
SUNHL,
SUNHZHJE,
REZ,
BIAOMDJ,
BUHSBMDJ,
MEIK,
QIYSE,
to_char(DIANCSCSJ,''yyyy-mm-dd hh24:mi:ss'')DIANCSCSJ,
to_char(JIESSJ,''yyyy-mm-dd hh24:mi:ss'')jiessj,
JIESR,
diaor08bb.BEIZ,
DIANCSCWJM,
YUNSFSB_ID
from diaor08bb,gongysb where diaor08bb.gongysb_id=gongysb.id %% ', ' and diaor08bb.id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (43, 'diaor08bb_new', '
select DIAOR08BB_NEW.ID,
to_char(RIQ,''yyyy-mm-dd'')riq,
FENX,
DIANCXXB_ID,
gongysb.bianm GONGYSB_ID,
MEIL,
DAOCZHJ,
KUANGJ,
ZENGZSE,
JIAOHQYZF,
TIELYF,
TIEYSE,
TIELZF,
SHUILLYF,
SHUIYF,
SHUIYSE,
SHUIYZF,
QIYF,
GANGZF,
DAOZZF,
QITFY,
SUNHL,
SUNHZHJE,
REZ,
BIAOMDJ,
BUHSBMDJ,
MEIK,
DIANCSCSJ,
QIYSE,
DIAOR08BB_NEW.BEIZ,
DIANCSCWJM,
JIESR,
JIESSJ,
SHUJQRZT
from DIAOR08BB_NEW,gongysb
where DIAOR08BB_NEW.Gongysb_Id=gongysb.id %% ', ' and DIAOR08BB_NEW.id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (44, 'zhibwcqkyb', '
select
ID,
to_char(RIQ,''yyyy-mm-dd'')riq,
DIANCXXB_ID,
id XUH,
FENX,
ZHIBMC,
BENQ,
TONGQ,
BIANHQK,
to_char(DIANCSCSJ,''yyyy-mm-dd hh24:mi:ss'')DIANCSCSJ,
BEIZ
from zhibwcqkb %% ', ' where zhibwcqkb.id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (45, 'jieszbsjb', '
select ID,
JIESDID,
ZHIBB_ID,
HETBZ,
GONGF,
CHANGF,
JIES,
YINGK,
ZHEJBZ,
ZHEJJE,
ZHUANGT,
YANSBHB_ID
from jieszbsjb %% ', ' where jieszbsjb.JIESDID=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (46, 'hetys', '
select ID,
       MINGC,
       FUID,
       DIANCXXB_ID,
       HETBH,
      to_char(QIANDRQ,''yyyy-mm-dd'')qiandrq,
       QIANDDD,
       YUNSDWB_ID,
       GONGFDWMC,
       GONGFDWDZ,
       GONGFDH,
       GONGFFDDBR,
       GONGFWTDLR,
       GONGFDBGH,
       GONGFKHYH,
       GONGFZH,
       GONGFYZBM,
       GONGFSH,
       XUFDWMC,
       XUFDWDZ,
       XUFFDDBR,
       XUFWTDLR,
       XUFDH,
       XUFDBGH,
       XUFKHYH,
       XUFZH,
       XUFYZBM,
       XUFSH,
       to_char(QISRQ,''yyyy-mm-dd'')QISRQ,
       to_char(GUOQRQ,''yyyy-mm-dd'')GUOQRQ,
       HETYS_MB_ID,
       MEIKMCS,
       LIUCZTB_ID,
       LIUCGZID
  from hetys %% ', ' where hetys.id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (47, 'yuetjkjb', 'select yuetjkjb.ID,
             to_char(RIQ, ''yyyy-mm-dd'') riq,
             diancxxb.bianm DIANCXXB_ID,
             yuetjkjb.XUH,
             meikdqb.bianm gongysbm,
             jihkjb.bianm jihkjbm,
             pinzb.mingc,
             YUNSFSB_ID
        from yuetjkjb , meikdqb , jihkjb , pinzb ,diancxxb
       where yuetjkjb.gongysb_id = meikdqb.id
         and yuetjkjb.jihkjb_id = jihkjb.id
         and diancxxb.id=yuetjkjb.diancxxb_id
         and yuetjkjb.pinzb_id = pinzb.id %%', ' and yuetjkjb.id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (48, 'yueslb', 'select ID,YUETJKJB_ID,FENX,JINGZ,BIAOZ,YINGD,KUID,YUNS,KOUD,KOUS,KOUZ,KOUM,ZONGKD,SANFSL,JIANJL,RUCTZL,
ZHUANGT,LAIMSL
from yueslb %% ', ' where yueslb.id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (122174818, 'NIANDJH_CAIG', 'SELECT JH.ID,
       JH.DIANCXXB_ID,
       TO_CHAR(JH.RIQ, ''yyyy-mm-dd'') RIQ,
       G.SHANGJGSBM GONGYSB_ID,
       JH.HET_SL,
       JH.HET_REZ,
       JH.HET_MEIJ,
       JH.HET_YUNJ,
       JH.JIH_SL,
       JH.JIH_REZ,
       JH.JIH_MEIJ,
       JH.JIH_MEIJBHS,
       JH.JIH_YUNJ,
       JH.JIH_YUNJBHS,
       JH.JIH_ZAF,
       JH.JIH_ZAFBHS,
       JH.JIH_QIT,
       JH.JIH_QITBHS,
       JH.JIH_DAOCJ,
       JH.JIH_DAOCBMDJ,
       JH.ZHUANGT,
       J.BIANM JIHKJB_ID
  FROM NIANDJH_CAIG JH, GONGYSB G, JIHKJB J
 WHERE JH.JIHKJB_ID = J.ID
   AND JH.GONGYSB_ID = G.ID %%', ' and jh.id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (122174819, 'NIANDJH_ZAF', 'SELECT ID,DIANCXXB_ID,TO_CHAR(RIQ, ''yyyy-mm-dd'') RIQ,TO_CHAR(TBRQ, ''yyyy-mm-dd'') TBRQ,
ZAFMC,YUCJE,YUCSM,SHIJWCJE,YUJWCJE,YUJWCSM,ZHUANGT FROM NIANDJH_ZAF %%', ' where id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (122174820, 'NIANDJH_ZHIB', 'SELECT ID,DIANCXXB_ID,TO_CHAR(RIQ, ''yyyy-mm-dd'') RIQ,FADL,GONGDMH,FADCYDL,FADBML,
GONGRL,GONGRMH,GONGRBML,BIAOMLHJ,RUCRLRZC,MEIZBML,MEIZBMDJ,RANYL,YOUZBML,RANYDJ,
YOUZBMDJ,QITFY,RLZHBMDJ,ZHUANGT FROM NIANDJH_ZHIB %%', ' where id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (49, 'yuezlb', '
select
ID,fenx,yuetjkjb_id,qnet_ar,aar,ad,vdaf,mt,stad,aad,mad,qbad,had,vad,fcad,std,qbrad,hdaf,
  qgrad_daf,sdaf,var,qnet_ar_kf,aar_kf,ad_kf,vdaf_kf,mt_kf,stad_kf,aad_kf,mad_kf,qbad_kf,
  had_kf,vad_kf,fcad_kf,std_kf,qbrad_kf,hdaf_kf,qgrad_daf_kf,sdaf_kf,var_kf,zhuangt,diancrz
from yuezlb %% ', ' where yuezlb.id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (50, 'yuehcb', 'select ID,FENX,YUETJKJB_ID,QICKC,SHOUML,FADY,GONGRY,QITH,SUNH,DIAOCL,PANYK,KUC,SHUIFCTZ,JITCS,ZHUANGT from yuehcb %% ', ' where yuehcb.id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (51, 'yueshchjb', '
select
yueshchjb.ID,to_char(riq,''yyyy-mm-dd'') riq,fenx,qickc,shouml,fady,gongry,qith,sunh,diaocl,panyk,kuc,diancxxb.bianm diancxxb_id,shuifctz,zhuangt,yuns
from yueshchjb,diancxxb where yueshchjb.diancxxb_id=diancxxb.id %% ', ' and yueshchjb.id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (52, 'yuercbmdj', '
select ID,FENX,YUETJKJB_ID,HETJ,RELZJ,LIUFZJ,HUIFZJ,HUIFFZJ,SHUIFZJ,MEIJ,MEIJS,YUNJ,YUNJS,DAOZZF,
ZAF,QIT,QNET_AR,BIAOMDJ,BUHSBMDJ,JIAOHQZF,ZHUANGT,YUNSJL
from yuercbmdj  %% ', ' where yuercbmdj.id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (53, 'yuejsbmdj', 'select ID,FENX,YUETJKJB_ID,JIESL,MEIJ,MEIJS,YUNJ,YUNJS,DAOZZF,ZAF,QIT,QNET_AR,BIAOMDJ,BUHSBMDJ,KUANGQYF,ZHUANGT,ZAFS  from yuejsbmdj %% ', ' where yuejsbmdj.id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (54, 'yuezbb', 'SELECT yuezbb.ID, diancxxb.bianm DIANCXXB_ID,to_char(riq,''yyyy-mm-dd'') RIQ,FENX,FADGRYTRML,RULMZBZML,RULTRMPJFRL,FADYTRML,GONGRYTRML,FADMZBML,GONGRMZBML,FADGRYTRYL,RULTRYPJFRL,
FADYTRYL,GONGRYTRYL,RULYZBZML,FADYZBZML,GONGRYZBZML,FADGRYTRQL,RULTRQPJFRL,FADYTRQL,GONGRYTRQL,RULQZBZML,FADQZBZML,
GONGRQZBZML,FADL,GONGDL,SHOUDL,GOURDL,GONGRL,SHOURL,FADBZMH,GONGDBZMH,GONGRBZMH,RANLCB_BHS,FADMCB,FADYCB,GONGRMCB,
GONGRYCB,FADRQCB,GONGRRQCB,GONGRCYDFTRLF,QIZ_RANM,QIZ_RANY,QIZ_RANQ,SHOUDDWBDCB,SHOUDDWGDCB,SHOUDDJ,SHOURDJ,SHOURDWCB,
LIRZE,BENQRCMGJSL,BENQRCMGJZJE_BHS,RULTRMPJDJ,QIZ_FADTRMDJ,QIZ_GONGRTRMDJ,RULTRYPJDJ,QIZ_FADTRYDJ,QIZ_GONGRTRQDJ,
QIZ_GONGRTRYDJ,RULTRQPJDJ,QIZ_FADTRQDJ,FADBZMDJ,QIZ_MEIZBMDJ,QIZ_YOUZBMDJ,QIZ_QIZBMDJ,GONGRBZMDJ,QIZ_MEIZBMDJ_GR,
QIZ_YOUZBMDJ_GR,QIZ_QIZBMDJ_GR,FADDWRLCB,SHOUDDWRLCB_MEID,SHOUDDWRLCB_QID,ZHUANGT,GONGRCGDL,FADZHCGDL,QITFY
          FROM yuezbb,diancxxb where yuezbb.diancxxb_id=diancxxb.id %%', ' and yuezbb.id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (55, 'yunsdwb', '
select ID,
DIANCXXB_ID,
MINGC,
BEIZ,
QUANC,
DANWDZ,
YOUZBM,
SHUIH,
FADDBR,
WEITDLR,
KAIHYH,
ZHANGH,
DIANH,
CHUANZ
from yunsdwb %% ', ' where yunsdwb.id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (56, 'jizb', '
select ZHIZS,
BEIZ,
XIAOHMZ,
QNET_AR_XH,
VDAF_XH,
SAR_XH,
ST_D_XH,
ST_XH,
AAD_XH,
CANSMZ,
CANSBL,
MT,
MT_XH,
ID,
DIANCXXB_ID,
XUH,
JIZBH,
JIZURL,
to_char(TOUCRQ,''yyyy-mm-dd'')tcrq,
RIJHM,
MEIHL,
JIHDL,
SHEJMZ,
QNET_AR,
VDAF,
SAR,
ST_D,
ST,
AAD
from jizb %% ', ' where jizb.id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (57, 'pinzb', '
select ID,
XUH,
BIANM,
MINGC,
PINY,
ZHUANGT,
PINZMS,
LEIB
from pinzb %% ', ' where pinzb.id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (58, 'niancgjh', 'select niancgjh.ID,
to_char(RIQ,''yyyy-mm-dd'')riq,
DIANCXXB_ID,
gongysb.shangjgsbm gongysbm,
jihkjb.bianm jihkj,
faz.mingc,
daoz.mingc dz,
niandcgl,
CHEBJG,
YUNF,
ZAF,
REZ,
JIAKK,
JIHDDSJYSL,
HUIFF,
LIUF,
DAOCJ,
BIAOMDJ,
YUNSFSB_ID,
JIZZT
from niancgjh,gongysb,chezxxb faz,chezxxb daoz ,jihkjb
where niancgjh.gongysb_id=gongysb.id and niancgjh.faz_id=faz.id and niancgjh.daoz_id=daoz.id
and niancgjh.jihkjb_id=jihkjb.id %%
', '  and niancgjh.id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (60, 'yueshcyb', '
select yueshcyb.ID,
to_char(yueshcyb.RIQ,''yyyy-mm-dd'') riq,
yueshcyb.FENX,
yueshcyb.QICKC,
yueshcyb.SHOUYL,
yueshcyb.FADYY,
yueshcyb.GONGRY,
yueshcyb.QITHY,
yueshcyb.SUNH,
yueshcyb.DIAOCL,
yueshcyb.PANYK,
yueshcyb.KUC,
diancxxb.bianm DIANCXXB_ID,
pinzb.mingc pinzb_id,
yueshcyb.ZHUANGT
from yueshcyb,pinzb,diancxxb where yueshcyb.pinzb_id=pinzb.id and diancxxb.id=yueshcyb.diancxxb_id %% ', ' and yueshcyb.id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (62, 'guslsb', 'select g.id,
       g.fahb_id,
       g.rez,
       g.leix,
       g.heth,
       g.hetjg,
       g.hetbz,
       g.hetzk,
       g.meij,
       g.meis,
       g.yunf,
       g.zaf,
       g.fazzf,
       g.ditf,
       g.yunfs,
       g.zhuangt,
       to_char(g.riq,''yyyy-mm-dd'')riq,
       g.beiz
  from guslsb g %% ', ' where g.id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (63, 'shouhcfkb', 'SELECT S.ID,
       S.DIANCXXB_ID,
       S.REZ,
       S.MEIJ,
       nvl(S.MEIJS,0)MEIJS,
       S.YUNJ,
       nvl(S.YUNJS,0)YUNJS,
       nvl(S.HUOCYJ,0)HUOCYJ,
       nvl(S.HUOCYJS,0)HUOCYJS,
       nvl(S.QICYJ,0)QICYJ,
       nvl(S.QICYJS,0)QICYJS,
       TO_CHAR(S.RIQ, ''yyyy-mm-dd'') RIQ,
       S.LAIMSL,
       M.SHANGJGSBM MEIKXXB_ID,
       G.SHANGJGSBM GONGYSB_ID,
       P.MINGC PINZB_ID,
       J.BIANM JIHKJB_ID,S.YUNSFSB_ID
  FROM SHOUHCFKB S, MEIKXXB M, GONGYSB G, PINZB P, JIHKJB J
 WHERE S.MEIKXXB_ID = M.ID
   AND S.GONGYSB_ID = G.ID
   AND S.JIHKJB_ID = J.ID
   AND S.PINZB_ID = P.ID %%', ' and s.id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (64, 'PAND_GD', 'select ID,DIANCXXB_ID,BIANM,to_char(RIQ,''yyyy-mm-dd'') RIQ,to_char(KAISRQ,''yyyy-mm-dd hh24:mi:ss'')KAISRQ,to_char(JIESRQ,''yyyy-mm-dd hh24:mi:ss'')JIESRQ,PIZ,SHENH,BIANZ,BIANZBM from PAND_GD %% ', ' where PAND_GD.id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (65, 'PANDRYB', 'select ID,PAND_GD_ID,BUM,CANJRY,ZHIZ from PANDRYB %%   ', '  where pandryb.id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (66, 'MEICDXCSB', 'select cs.ID,cs.PAND_GD_ID,cs.SHANGK,cs.XIAK,cs.GAO,cs.DINGC,cs.JIC,cs.TIJ,cs.MID,cs.SHUL,cs.MEICMC,mc.diancxxb_id||mc.mingc MEICBbm from MEICDXCSB cs,meicb mc  where cs.meicb_id=mc.id  %%    ', '  and cs.id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (67, 'PANDQHBHB', 'select ID,PAND_GD_ID,DANGRSCLL,DANGRSCHL,PANQHLL,PANQHHL,RUCPJSF,RULPJSF,SHUIFCTZ from PANDQHBHB %%    ', '  where PANDQHBHB.id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (68, 'MEICDJMDCDB', 'select m.ID ID,PAND_GD_ID,mc.diancxxb_id||mc.mingc  MEICB_ID,TONGB,ZONGZ,PIZ,JINGZ,RONGJ,MID from MEICDJMDCDB m,meicb mc where mc.id=m.meicb_id %%    ', ' and m.id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (69, 'MEICCLB', 'select cl.ID,cl.DIANCXXB_ID,jz.diancxxb_id||jz.jizbh JIZBbm,cl.item_id,SHUL,MEIW from MEICCLB cl,jizb jz  where jz.id=cl.jizb_id %%', ' and cl.id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (70, 'PANDGDCMB', 'select ID,PAND_GD_ID,MEICCLB_ID,SHUL from PANDGDCMB %%  ', ' where PANDGDCMB.id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (71, 'PANDGDWZCMB', 'select ID,PAND_GD_ID,PANDGDCMB_ID,CUNML,BEIZ  from PANDGDWZCMB %%  ', ' where PANDGDWZCMB.id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (72, 'PANDSXB', ' select ID,PAND_GD_ID,PANDFF,SHIYYQ,MEITCFQK,MIDCD,RULMJLJSFCTZ,YINGKQKFX from PANDSXB %%  ', '  where PANDSXB.id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (73, 'PANDZMB', '  select ID,PAND_GD_ID,YUNXCS,JITCS,YUNS,DANGYLJRCSF,DANGYLJRLSF,SHUIFCTZ,QICKC,BENYLM,BENYHM,QITHM,DIAOCL,BENQKC,JITSHHKC,BENQYK from PANDZMB %%  ', '  where PANDZMB.id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (74, 'meicb', 'select ID,DIANCXXB_ID,XUH,MINGC,PINY,KUC,CHANGD,KUAND,MIANJ,GAOD,TIJ,MEICT,DIANDML,BEIZ from meicb %% ', ' where meicb.id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (75, 'item', 'select i.id,lower(s.bianm) itemsortid,i.xuh,i.bianm,i.mingc,i.zhuangt,i.beiz,vw.id diancxxb_id from item i,itemsort s,(select max(id) id from vwdianc) vw where i.itemsortid=s.id %%', ' and i.id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (1, 'fahb', 'select fahb.id id,yuanid,fahb.diancxxb_id,gongysb.shangjgsbm,meikxxb.shangjgsbm,pinzb.mingc,fz.mingc,dz.mingc,jihkjb.bianm,to_char(fahb.fahrq,''YYYY-MM-DD'')fahrq,to_char(fahb.daohrq,''YYYY-MM-DD HH24:MI:SS'')daohrq,fahb.hetb_id,fahb.zhilb_id,fahb.jiesb_id,fahb.yunsfsb_id,fahb.chec,maoz,fahb.piz,fahb.jingz,fahb.biaoz,fahb.yingd,fahb.yingk,fahb.yuns,fahb.yunsl,fahb.koud,fahb.kous,fahb.kouz,fahb.ches,yuandz.mingc,fahb.kuangfzlb_id,fahb.beiz,lie_id,zongkd,hedbz,sanfsl,yuanshdwb_id,laimsl,laimzl,laimkc from fahb,gongysb,meikxxb,pinzb,chezxxb fz,chezxxb dz,jihkjb,chezxxb yuandz where fahb.gongysb_id=gongysb.id  and fahb.meikxxb_id=meikxxb.id and fahb.pinzb_id=pinzb.id and fahb.faz_id=fz.id and fahb.daoz_id=dz.id and fahb.jihkjb_id=jihkjb.id and fahb.yuandz_id=yuandz.id(+)%%', ' and fahb.id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (4211276, 'kucmjgb', 'SELECT KUCMJGB.ID,KUCMJGB.DIANCXXB_ID,TO_CHAR(KUCMJGB.RIQ,''yyyy-mm-dd'')RIQ,PINZB.MINGC,KUCMJGB.LAIMSL,KUCMJGB.LAIMRL,KUCMJGB.RUCBMDJ,KUCMJGB.HAOMSL,KUCMJGB.HAOMRL,KUCMJGB.HAOMZF,KUCMJGB.RULBMDJ,KUCMJGB.KUCSL,KUCMJGB.KUCRL,KUCMJGB.KUCBMDJ, KUCMJGB.XIAZLMYC,KUCMJGB.BEIZ,KUCMJGB.ZHUANGT FROM KUCMJGB, PINZB WHERE PINZB.ID = KUCMJGB.PINZB_ID %%', ' and kucmjgb.id= ', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (79, 'zafb', 'select ID,to_char(riq,''yyyy-mm-dd'') RIQ, DIANCXXB_ID,MINGC,JINE,BEIZ,ZHUANGT FROM zafb %%', ' where id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (121403831, 'TUPCCB', 'SELECT ID, DIANCXXB_ID, to_char(RIQ,''yyyy-mm-dd'')riq, XUH, BIANM, MINGC, MOKMC FROM TUPCCB %%', '  WHERE ID= ', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (121403832, 'PAND_GDJT', 'SELECT ID,DIANCXXB_ID,TO_CHAR(RIQ, ''yyyy-mm-dd'') RIQ,ZHANGMKC,SHIPKC,CHANGSL,SHUIFCTZL,YINGKD,FUJZT,FUJMC,ZHUANGT FROM PAND_GDJT %%', '  WHERE ID= ', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (122174821, 'YUEDJH_CAIG', 'SELECT JH.ID,
       JH.DIANCXXB_ID,
       TO_CHAR(JH.RIQ, ''yyyy-mm-dd'') RIQ,
       G.SHANGJGSBM GONGYSB_ID,
       M.SHANGJGSBM MEIKXXB_ID,
       P.MINGC PINZB_ID,
       C.MINGC FAZ_ID,
       JH.JIH_SL,
       JH.JIH_REZ,
       JH.JIH_LIUF,
       JH.JIH_HFF,
       JH.JIH_MEIJ,
       JH.JIH_MEIJBHS,
       JH.JIH_YUNJ,
       JH.JIH_YUNJBHS,
       JH.JIH_ZAF,
       JH.JIH_ZAFBHS,
       JH.JIH_DAOCJ,
       JH.JIH_DAOCJBHS,
       JH.JIH_DAOCBMDJ,
       JH.JIH_DAOCBMDJBHS,
       JH.ZHUANGT,
       J.BIANM JIHKJB_ID
  FROM YUEDJH_CAIG JH, GONGYSB G, MEIKXXB M, PINZB P, CHEZXXB C, JIHKJB J
 WHERE JH.GONGYSB_ID = G.ID
   AND J.ID = JH.JIHKJB_ID
   AND JH.MEIKXXB_ID = M.ID(+)
   AND JH.PINZB_ID = P.ID(+)
   AND JH.FAZ_ID = C.ID(+) %%', 'and jh.id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (122174822, 'YUEDJH_ZHIB', 'SELECT ID,DIANCXXB_ID,TO_CHAR(RIQ, ''yyyy-mm-dd'') RIQ,FADL,GONGDMH,FADCYDL,FADBML,
GONGRL,GONGRMH,GONGRBML,BIAOMLHJ,SHANGYMKC,SHANGYMKCDJ,SHANGYMKCRZ,YUEMKCJHZ,
YUEMKCRZ,HAOYYML,RLZHBMDJ,ZHUANGT FROM YUEDJH_ZHIB %%', ' where id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (11038790, 'DANPCJSMXB', 'SELECT D.ID,D.XUH,D.JIESDID,ZB.BIANM ZHIBB_ID,D.HETBZ,D.GONGF,D.CHANGF,D.JIES,
D.YINGK,D.ZHEJBZ,D.ZHEJJE,D.GONGFSL,D.YANSSL,D.JIESSL,D.KOUD,D.KOUS,
D.KOUZ,D.CHES,D.JINGZ,D.KOUD_JS,D.YUNS,D.JIESSLCY,D.JIESDJ,D.JIAKHJ,
D.JIAKSK,D.JIASHJ,D.BIAOMDJ,D.BUHSBMDJ,D.LEIB,D.HETJ,D.QNETAR,D.STD,
D.STAD,D.STAR,D.VDAF,D.MT,D.MAD,D.AAD,D.AD,D.AAR,D.VAD,D.ZONGJE,
MK.SHANGJGSBM MEIKXXB_ID,C.MINGC FAZ_ID,D.CHAOKDL,D.JIAJQDJ,
D.ZHEKFS,D.SHULZBYK
  FROM DANPCJSMXB D, MEIKXXB MK,ZHIBB ZB,CHEZXXB C
 WHERE D.MEIKXXB_ID = MK.ID AND D.ZHIBB_ID=ZB.ID AND C.ID(+)=D.FAZ_ID %%', ' AND D.JIESDID=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (121061192, 'yuecgjhb', 'SELECT YUECGJHB.ID,
TO_CHAR(RIQ, ''yyyy-mm-dd'') RIQ,
DIANCXXB_ID,
GONGYSB.SHANGJGSBM GONGYSBM,
MEIKXXB.SHANGJGSBM MEIKXXB_ID,
JIHKJB.BIANM JIHKJ,
PINZB.MINGC PINZB_ID,
FAZ.MINGC,DAOZ.MINGC DZ,YUEJHCGL,CHEBJG,YUNF,ZAF,REZ,JIAKK,
JIHDDSJYSL,HUIFF,LIUF,DAOCJ,BIAOMDJ,YUNSFSB_ID,JIZZT
  FROM YUECGJHB, GONGYSB, CHEZXXB FAZ, CHEZXXB DAOZ, JIHKJB, MEIKXXB, PINZB
 WHERE YUECGJHB.GONGYSB_ID = GONGYSB.ID
   AND YUECGJHB.MEIKXXB_ID = MEIKXXB.ID
   AND YUECGJHB.PINZB_ID = PINZB.ID
   AND YUECGJHB.FAZ_ID = FAZ.ID
   AND YUECGJHB.DAOZ_ID = DAOZ.ID
   AND YUECGJHB.JIHKJB_ID = JIHKJB.ID %%', '  and yuecgjhb.id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (121061193, 'niandhtqkb', 'SELECT N.ID,TO_CHAR(N.RIQ, ''yyyy-mm-dd'') RIQ,
N.DIANCXXB_ID,G.SHANGJGSBM GONGYSB_ID,MK.SHANGJGSBM MEIKXXB_ID,
J.BIANM JIHKJB_ID,P.MINGC PINZB_ID,C.MINGC FAZ_ID,C.MINGC DAOZ_ID,
N.YUNSFSB_ID,N.HEJ,N.REZ,N.HUIFF,N.LIUF,N.CHEBJG,N.YUNF,N.ZAF,N.HANGID,N.BEIZ
  FROM NIANDHTQKB N, GONGYSB G, CHEZXXB C, PINZB P, JIHKJB J, MEIKXXB MK
 WHERE N.GONGYSB_ID = G.ID
   AND N.FAZ_ID = C.ID
   AND N.DAOZ_ID = C.ID
   AND N.PINZB_ID = P.ID
   AND N.MEIKXXB_ID = MK.ID
   AND N.JIHKJB_ID = J.ID %%', ' and n.id =', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (124349267, 'HETYSJGB', 'SELECT J.ID         AS ID,
       J.HETYS_ID,
       M.SHANGJGSBM AS MEIKXXB_ID,
       Z.BIANM      AS ZHIBB_ID,
       T.BIANM      AS TIAOJB_ID,
       J.SHANGX,
       J.XIAX,
       ZD.BIANM     AS DANWB_ID,
       J.YUNJA,
       YD.BIANM     AS YUNJDW_ID
  FROM HETYSJGB J, MEIKXXB M, ZHIBB Z, TIAOJB T, DANWB ZD, DANWB YD
 WHERE J.MEIKXXB_ID = M.ID(+)
   AND J.ZHIBB_ID = Z.ID(+)
   AND J.TIAOJB_ID = T.ID(+)
   AND J.DANWB_ID = ZD.ID(+)
   AND J.YUNJDW_ID = YD.ID(+) %%', ' AND J.HETYS_ID =  ', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (2, 'zhilb', 'select z.id id,z.huaybh,caiyb_id,to_char(huaysj,''YYYY-MM-DD HH24:MI:SS'')huaysj,Qnet_ar,aar,ad,vdaf,mt,stad,aad,mad,qbad,had,vad,fcad,std,hdaf,qgrad_daf,sdaf,var,t1,t2,t3,t4,jijsf,jijfrl,huayy,lury,z.beiz,shenhzt,banz,qgrad from zhilb z %%', 'where id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (3, 'fahb', 'fahb', 'fahb.id in', 'id', null, '修改hetb_id', 'hetb_id=');

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (4, 'fahb', 'fahb', 'fahb.id in', 'id', null, '修改zhilb_id', 'zhilb_id=');

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (5, 'fahb', 'fahb', 'fahb.id in', 'id', null, '修改jiesb_id', 'jiesb_id=');

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (6, 'fahb', 'fahb', 'fahb.id in', 'id', null, '修改kuangfzlb_id', 'kuangfzlb_id=');

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (7, 'rulmzlb', 'select ID,
       to_char(RULRQ, ''YYYY-MM-DD'') rulrq,
       to_char(FENXRQ, ''YYYY-MM-DD'') fenxrq,
       DIANCXXB_ID,
       RULBZB_ID,
       JIZFZB_ID,
       QNET_AR,
       AAR,
       AD,
       VDAF,
       MT,
       STAD,
       AAD,
       MAD,
       QBAD,
       HAD,
       VAD,
       FCAD,
       STD,
       QGRAD,
       HDAF,
       SDAF,
       VAR,
       HUAYY,
       BEIZ,
       LURY,
       to_char(LURSJ, ''YYYY-MM-DD HH24:MI:SS'') LURSJ,
       SHENHZT,
       bianm,
       har,
       QGRD,
       QGRAD_DAF
  from rulmzlb %%', ' where rulmzlb.id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (8, 'meihyb', 'select ID,      to_char(RULRQ,''YYYY-MM-DD'')rulrq,       DIANCXXB_ID,       RULMZLB_ID,       RULBZB_ID,       JIZFZB_ID,       FADHY,       GONGRHY,       QITY,       FEISCY,       BEIZ,       LURY,       to_char(LURSJ,''YYYY-MM-DD HH24:MI:SS'')LURSJ,       SHENHZT  from meihyb %%', ' where meihyb.id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (0, 'rucycbb', 'select rucycbb.ID,diancxxb.bianm diancxxb_id,to_char(riq,''yyyy-mm-dd'') riq,fenx,shul,hanszhj,buhszhj,youj,yous,yunfs,yunzf,qitfy,youfrl,zhebmdj,rucycbb.zhuangt,pinzb.mingc pinzb_id from rucycbb,diancxxb,pinzb where rucycbb.diancxxb_id=diancxxb.id and rucycbb.pinzb_id=pinzb.id %% ', ' and rucycbb.id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (100, 'niandhtzxqkb', 'SELECT niandhtzxqkb.id ID,
diancxxb.bianm diancxxb_id,
meikdqb.bianm gongysb_id,
niandhtzxqkb.beiz,
to_char(riq,''yyyy-mm-dd'') riq,
hetl,
hetjzrz,
hetjzckjg,
hetjzdcjg,
hetzjkhbz,
yansfs,
niandhtzxqkb.zhuangt
 FROM niandhtzxqkb,diancxxb,meikdqb
 where niandhtzxqkb.diancxxb_id=diancxxb.id and meikdqb.id=niandhtzxqkb.gongysb_id %%', ' and niandhtzxqkb.id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (9, 'hetb', 'select hetb.ID,
       hetb.FUID,
       DIANCXXB_ID,
       HETBH,
       to_char(QIANDRQ, ''YYYY-MM-DD'') QIANDRQ,
       QIANDDD,
       GONGFDWMC,
       GONGFDWDZ,
       GONGFDH,
       GONGFFDDBR,
       GONGFWTDLR,
       GONGFDBGH,
       GONGFKHYH,
       GONGFZH,
       GONGFYZBM,
       GONGFSH,
       XUFDWMC,
       XUFDWDZ,
       XUFFDDBR,
       XUFWTDLR,
       XUFDH,
       XUFDBGH,
       XUFKHYH,
       XUFZH,
       XUFYZBM,
       XUFSH,
       g1.shangjgsbm HETGYSBID,
       g2.shangjgsbm GONGYSB_ID,
       to_char(QISRQ,''yyyy-mm-dd'')QISRQ,
       to_char(GUOQRQ,''yyyy-mm-dd'') GUOQRQ,
       HETB_MB_ID,
       jihkjb.bianm,
       LIUCZTB_ID,
       LIUCGZID,
       LEIB,
       MEIKMCS
  from hetb, gongysb g1, gongysb g2, jihkjb
 where hetb.gongysb_id = g1.id
   and hetb.hetgysbid = g2.id
   and hetb.jihkjb_id = jihkjb.id %%', ' and hetb.id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (10, 'hetslb', 'select hetslb.ID,pinzb.mingc,YUNSFSB_ID,c1.mingc,c2.mingc,DIANCXXB_ID,to_char(RIQ,''YYYY-MM-DD'')riq,HETL,HETB_ID from hetslb ,pinzb,chezxxb c1,chezxxb c2 where hetslb.pinzb_id=pinzb.id and hetslb.faz_id=c1.id and hetslb.daoz_id=c2.id %%', ' and hetslb.hetb_id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (11, 'hetzlb', 'select ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,HETB_ID from hetzlb %%', 'where hetzlb.hetb_id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (12, 'hetjgb', 'select ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,JIJ,JIJDWID,HETJSFSB_ID,HETJSXSB_ID,YUNJ,YUNJDW_ID,YINGDKF,YUNSFSB_ID,ZUIGMJ,HETB_ID,HETJJFSB_ID,FENGSJJ,JIJLX,(select mingc from pinzb where id=hetjgb.PINZB_ID)pinzb_id,JIJGS from hetjgb %%', 'where hetjgb.hetb_id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (13, 'hetzkkb', 'select ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,JIS,JISDWID,KOUJ,KOUJDW,ZENGFJ,ZENGFJDW,XIAOSCL,JIZZKJ,JIZZB,CANZXM,CANZXMDW,CANZSX,CANZXX,HETJSXSB_ID,YUNSFSB_ID,BEIZ,HETB_ID from hetzkkb %%', 'where hetzkkb.hetb_id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (14, 'hetwzb', 'select ID  ,WENZNR  ,HETB_ID from hetwzb %%', 'where hetwzb.hetb_id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (15, 'jiesb', 'select jiesb.ID,DIANCXXB_ID,jiesb.BIANM,gongysb.shangjgsbm,GONGYSMC,YUNSFSB_ID,jiesb.yunj,YINGD,KUID,FAZ,to_char(FAHKSRQ,''YYYY-MM-DD'')FAHKSRQ,to_char(FAHJZRQ,''YYYY-MM-DD'')FAHJZRQ,jiesb.MEIZ,DAIBCH,YUANSHR,XIANSHR,to_char(YANSKSRQ,''YYYY-MM-DD'')YANSKSRQ,to_char(YANSJZRQ,''YYYY-MM-DD'')YANSJZRQ,YANSBH,SHOUKDW,jiesb.KAIHYH,jiesb.ZHANGH,FAPBH,FUKFS,DUIFDD,CHES,JIESSL,GUOHL,YUNS,KOUD,JIESSLCY,HANSDJ,BUKMK,HANSMK,BUHSMK,MEIKJE,SHUIK,SHUIL,BUHSDJ,JIESLX,to_char(JIESRQ,''YYYY-MM-DD'')JIESRQ,to_char(RUZRQ,''YYYY-MM-DD'')RUZRQ,HETB_ID,LIUCZTB_ID,LIUCGZID,RANLBMJBR,to_char(RANLBMJBRQ,''YYYY-MM-DD'')RANLBMJBRQ,jiesb.BEIZ,JIESFRL,jihkjb.bianm,meikxxb.shangjgsbm,HETJ,MEIKDWMC,ZHILJQ,QIYF,JIESRL,JIESLF,JIESRCRL from jiesb,gongysb ,meikxxb,jihkjb where jiesb.gongysb_id=gongysb.id  and jiesb.meikxxb_id=meikxxb.id and jiesb.jihkjb_id=jihkjb.id %%', 'and jiesb.id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (16, 'jiesyfb', 'select jiesyfb.ID,       DIANCXXB_ID,       jiesyfb.BIANM,       gongysb.shangjgsbm,       GONGYSMC,       YUNSFSB_ID,       jiesyfb.YUNJ,       YINGD,       KUID,       FAZ,      to_char(FAHKSRQ,''YYYY-MM-DD'')FAHKSRQ,      to_char(FAHJZRQ,''YYYY-MM-DD'')FAHJZRQ,       jiesyfb.MEIZ,       DAIBCH,       YUANSHR,       XIANSHR,       to_char(YANSKSRQ,''YYYY-MM-DD'')YANSKSRQ,       to_char(YANSJZRQ,''YYYY-MM-DD'')YANSJZRQ,       YANSBH,       SHOUKDW,       jiesyfb.KAIHYH,       jiesyfb.ZHANGH,       FAPBH,       FUKFS,       DUIFDD,       CHES,       JIESSL,       GUOHL,       YUNS,       KOUD,       JIESSLCY,       GUOTYF,       GUOTZF,       KUANGQYF,       KUANGQZF,       JISKC,       HANSDJ,       BUKYF,       HANSYF,       BUHSYF,       SHUIK,       SHUIL,       BUHSDJ,       JIESLX,      to_char(JIESRQ,''YYYY-MM-DD'')JIESRQ,      to_char(RUZRQ,''YYYY-MM-DD'')RUZRQ,       HETB_ID,       LIUCZTB_ID,       LIUCGZID,       DIANCJSMKB_ID,       RANLBMJBR,       to_char(RANLBMJBRQ,''YYYY-MM-DD'')RANLBMJBRQ,       jiesyfb.BEIZ,       GUOTYFJF,       GUOTZFJF,       GONGFSL,        YANSSL,        YINGK,        DITZF,        meikxxb.shangjgsbm,        DITYF,        MEIKDWMC  from jiesyfb,gongysb,meikxxb  where jiesyfb.gongysb_id=gongysb.id and meikxxb.id=jiesyfb.meikxxb_id %%', 'and jiesyfb.id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (19, 'pandb', 'select id,       diancxxb_id,       to_char(riq, ''yyyy-mm-dd'') riq,       bianm,       lury,       zhuangt,       beiz  from pandb %%', 'where id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (20, 'pandtjb', 'select id,       pandb_id,       meicb_id,       tij,       mid,       cunml,       pandff,       duig,       dingc,       dic,       dingk,       dik,       mingc  from pandtjb %%', 'where id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (21, 'tielyb', 'select tielyb.id id,tielyb.diancxxb_id diancxxb_id,to_char(riq,''yyyy-mm-dd'') riq,chezxxb.mingc,banjh,ches,tielyb.beiz from tielyb,chezxxb where tielyb.chezxxb_id=chezxxb.id %%', 'and tielyb.id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (22, 'zhuangcyb', 'select z.id id, z.diancxxb_id dianxxxb_id, to_char(z.riq,''yyyy-mm-dd'') riq,g.shangjgsbm bianm,c.mingc mingc,z.zuorzc,z.jinrzc,z.jinrcr,z.mingrqc,z.mingrsd,z.beiz  from zhuangcyb z, gongysb g, chezxxb c  where z.gongysb_id=g.id(+)  and z.chezxxb_id=c.id(+) %%', 'and z.id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (23, 'jizyxqkb', 'select j.id,       j.diancxxb_id,       j.jizb_id,       j.shebzt,       to_char(j.kaisrq, ''yyyy-mm-dd'') kaisrq,       to_char(j.jiesrq, ''yyyy-mm-dd'') jiesrq,       j.tingjyxdl,       j.shuom  from jizyxqkb j %%', 'where j.id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (24, 'riscsjb', 'select r.id,       r.diancxxb_id,       to_char(r.riq,''yyyy-mm-dd'') riq,       r.jizb_id,       r.fadl,       r.gongdl,       r.gongrl,       r.fadfhl,       r.shangwdl  from riscsjb r %%', 'where r.id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (26, 'shebyxqkb', 'select s.id,       s.diancxxb_id,       s.jiexsbb_id,       s.shebzt,       to_char(s.riq,''yyyy-mm-dd'') riq,       to_char(s.jiesrq,''yyyy-mm-dd'') jiesrq,       s.img,       s.shuom,       to_char(s.kaisrq,''yyyy-mm-dd'') kaisrq  from shebyxqkb s %%', 'where s.id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (27, 'shouhcrbb', 'SELECT S.ID,
       TO_CHAR(S.RIQ, ''yyyy-mm-dd'') RIQ,
       S.DIANCXXB_ID,
       S.DANGRGM,
       S.HAOYQKDR,
       S.KUC,
       S.DANGRFDL,
       S.TIAOZL,
       S.FADL,
       S.JINGZ,
       S.BIAOZ,
       S.YUNS,
       S.YINGD,
       S.KUID,
       S.FADY,
       S.GONGRY,
       S.QITY,
       S.CUNS,
       S.SHUIFCTZ,
       S.PANYK,
       S.DIAOC,
       S.FEISCY,
       S.CHANGWML,
       S.BUKDML,
       S.KEDKC,
       S.GONGRL
  FROM SHOUHCRBB S %%', 'where s.id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (28, 'pandmdb', 'select id,       pandb_id,       meicb_id,       ced,       celff,       yangpzl,       cedjj,       chentmz,       chentpz  from pandmdb %%', 'where id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (29, 'pandwzcmb', 'select id,pandb_id,pandcmwz_id,cunml,beiz from pandwzcmb %%', 'where id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (30, 'pandzmm', 'select id,pandb_id,benyjm,fadh,gongrh,feiscy,       qity,diaocl,cuns,yuns,shuifc,zhangmkc,       shijkc,panyk       from pandzmm %%', 'where id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (31, 'pandbmryzzb', 'select id,pandb_id,bum,reny,zhiz       from pandbmryzzb %%', 'where id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (32, 'quzpkb', 'select id,q.diancxxb_id,to_char(q.riq,''yyyy-mm-dd'')riq,q.quz18,q.paik18,q.daix18,q.zhanc18,q.quz6,q.paik6,q.daix6,q.zhanc6,q.changtcs,q.changtsj
from quzpkb q %% ', ' where q.id=', 'id', null, 'xml', null);

insert into jiekfspzb (ID, RENWMC, RENWSQL, RENWBS, RENWTJ, BEIZ, MINGLLX, GENGXY)
values (33, 'shouhcrbyb', 'select s.id,s.diancxxb_id,to_char(riq,''yyyy-mm-dd'')riq,pinzb.mingc,shourl,fady,gongry,qity,cuns,panyk,kuc
from shouhcrbyb s,pinzb
where s.pinzb_id=pinzb.id %% ', ' where s.id=', 'id', null, 'xml', null);

commit;
