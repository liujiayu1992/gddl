--人员信息表
delete from renyxxb where mingc!='Enterprise'
select*from renyxxb;
select*from linh0.renyxxb;
insert into renyxxb (id,mingc,mim,quanc,diancxxb_id,bum,zhiw,xingb,zhuangt)
(select id,yonghm,(select mim from renyxxb where mingc='Enterprise'),quanm,diancxxb_id,bum,zhiw,decode(xingb,1,'男','女'),1
from linh0.renyxxb)
select*from zhilb
--电厂
select*from diancxxb@lhdb;
select*from diancxxb;
select*from gongysdcglb;
delete from gongysdcglb;commit;
insert into gongysdcglb(id,DIANCXXB_ID,GONGYSB_ID)(SELECT GETNEWID(192),197,g.id from (select distinct id from  gongysb) g);commit;
--品种
select*from pinzb@lhdb;
select*from pinzb;
DELETE from pinzb
insert INTO pinzb (id,xuh,bianm,mingc,piny,zhuangt,pinzms,leib)
(select            id,xuh,pinzbm,pinz,pinzbm,1,' ',leix from ranlpzb@lhdb  );
--车站
select*From CHEZXXB;
select*From chezxxb@lhdb
DELETE from chezxxb;
insert INTO chezxxb (id,xuh,bianm,mingc,quanc,piny,LUJXXB_ID,leib,beiz)
  (select id,1,chezbm,jianc,QUANC,'qi',LUJXXB_ID,leib,beiz from CHEZXXB@lhdb  );
--路局
--------------------------6口径----------------
select*From JIHKJB;
SELECT *FROM JIHKJB@lhdb;
DELETE from JIHKJB;
insert INTO jihkjb (id,xuh,mingc,bianm,beiz)
  (select id,xuh,mingc,koujbm,beiz from JIHKJB@lhdb  );
--7煤矿地区

select*from MEIKDQB;
select*From MEIKdqb@lhdb;
DELETE from MEIKDQB;
insert INTO MEIKDQB (id,xuh,mingc,quanc,zhuangt,bianm,SHENGFB_ID)
  (select id,xuh,meikdqmc,meikdqqc,1,meikdqbm,nvl(W_shengfen_id,0) from MEIKDQB@lhdb  );
--8煤矿信息-----------------------------------------
select*From chengsb
SELECT *from MEIKXXB;
SELECT *from MEIKXXB@lhdb;
insert INTO MEIKXXB (id,xuh,bianm,mingc,quanc,piny,shengfb_id,leib,jihkjb_id,leix,beiz,danwdz,yunj,chengsb_id,shangjgsbm,
                     meikdq_id,zhuangt,shiyzt)
  (select id,xuh,meikbm,meikdwmc,meikdwqc,'mk',(select w_SHENGFen_ID from MEIKDQB@lhdb where id=meikdqb_id) ,
leib,JIHKJB_ID,leix,beiz,danwdz,0,0,null,meikdqb_id,zhuangt,1
   from MEIKXXB@lhdb  )
--煤场------------------------------------------------
SELECT *from MEICB;
select*from linh0.changbb;
SELECT *FROM MEICB@lhdb m inner join linh0.meicfqb q on m.meicfqb_id=q.id;
SELECT *FROM linh0.meicfqb;
delete from MEICB;
insert into meicb (id,diancxxb_id,xuh,mingc,piny,kuc,changd,kuand,mianj,gaod,tij,meict,diandml,beiz)
 (select m.id,197,rownum,q.meicmc,'mc',m.kucl,0,0,0,0,0,0,0,null
        FROM MEICB@lhdb m inner join linh0.meicfqb q on m.meicfqb_id=q.id);
--------------供应商------------------------------------
select*from GONGYSB;
SELECT *from fahdwb@lhdb;
DELETE from GONGYSB;
alter table gongysb MODIFY  kaihyh VARCHAR2(225);
insert into gongysb(id,fuid,xuh,mingc,quanc,piny,bianm,danwdz,faddbr,weitdlr,kaihyh,zhangh,dianh,shuih,youzbm,chuanz,
                    meitly,meiz,chubnl,kaicnl,kaicnx,shengcnl,gongynl,liux,yunsfs,shiccgl,zhongdht,yunsnl,heznx,rongqgx,
                    xiny,gongsxz,kegywfmz,kegywfmzzb,shengfb_id,shifss,shangsdz,zicbfb,shoumbfb,qitbfb,beiz,shangjgsbm,leix,zhuangt)
            (SELECT id,-1,xuh,jianc,quanc,'gys',gongysbm,danwdz,faddbr,weitdlr,kaihyh,zhangh,dianh,null,youzbm,chuanz,meitly,
               meiz,chubnl,kaicnl,kaicnx,shengcnl,gongynl,liux,yunsfs,shiccgl,zhongdht,yunsnl,heznx,rongqgx,xiny,gongsxz,
               kegywfmz,kegywfmzzb,shengfb_id,shifss,shangsdz,zicbfb,shoumbfb,qitbfb,beiz,null,0,zhuangt
                      from fahdwb@lhdb);
insert into gongysb(id,fuid,xuh,mingc,quanc,piny,bianm,danwdz,faddbr,weitdlr,kaihyh,zhangh,dianh,shuih,youzbm,chuanz,meitly,
                    meiz,chubnl,kaicnl,kaicnx,shengcnl,gongynl,liux,yunsfs,shiccgl,zhongdht,yunsnl,heznx,rongqgx,xiny,gongsxz,
                    kegywfmz,kegywfmzzb,shengfb_id,shifss,shangsdz,zicbfb,shoumbfb,qitbfb,beiz,shangjgsbm,leix,zhuangt)
   (SELECT id,-1,xuh,meikdqmc,meikdqqc,'gys',meikdqbm,danwdz,faddbr,weitdlr,kaihyh,zhangh,dianh,shuih,youzbm,null,1,
   meiz, chubnl, kaicnl,kaicnx,shengcnl,gongynl,liux,yunsfs,shiccgl,zhongdht, yunsnl, heznx,rongqgx,xiny,null, null,null,
      w_shengfen_id,null,null,null,null,null,null,null,0,1
   from MEIKDQB@lhdb);
--数量-------------------------------------------------------------------------------------------------------------------
select*from FAHB order by DAOHRQ DESC
select*from qichjjbtmp@lhdb order by jianmsj DESC;
SELECT *from fahdwb@lhdb;
select*From fahb@lhdb f inner join MEIKDQB@lhdb g on f.fahdwb_id=g.id;
select*from qichjjbtmp@lhdb q inner join fahdwb@lhdb g on q.meikdqb_id=g.id;
SELECT *from MEIKDQB@lhdb;
select*from GONGYSMKGLB;
DELETE from GONGYSMKGLB;
insert into GONGYSMKGLB (id,GONGYSB_ID,MEIKXXB_ID)(
    select GETNEWID(197),g.* from
      (select DISTINCT gongysb_id,meikxxb_id from FAHB) g
);
select*from linh0.fahb;
delete from FAHB;
insert into fahb (id,yuanid,diancxxb_id,gongysb_id,meikxxb_id,pinzb_id,faz_id,daoz_id,jihkjb_id,fahrq,daohrq,hetb_id,zhilb_id,jiesb_id,
                  yunsfsb_id,chec,maoz,piz,jingz,biaoz,yingd,yingk,yuns,yunsl,koud,kous,kouz,koum,zongkd,sanfsl,ches,tiaozbz,yansbhb_id,
                  lie_id,yuandz_id,yuanshdwb_id,kuangfzlb_id,liucb_id,liucztb_id,hedbz,beiz,ruccbb_id,leiid,ditjsbz,ditjsb_id,lieid,laimsl,
                  laimzl,laimkc,jiancl,luncxxb_id,daobrq,kaobrq,yundh,xiemkssj,xiemjssj,zhuanggkssj,zhuanggjssj,chex)
    (SELECT id,yuanid,diancxxb_id,fahdwb_id,meikxxb_id,ranlpzb_id,faz_id,daoz_id,jihkjb_id,fahrq,trunc(daohrq),0 ,zhilb_id,jiesb_id,2,0,maoz,piz,maoz-piz-koud,biaoz,0,0,
      yuns,   0 ,koud, 0,0,0,0, 0,ches,tiaozbz,-1,0,-1,0,0,0,1,3,beiz,null,null,0,0,null,maoz-piz-KOUD,maoz-piz-KOUD,maoz-piz-KOUD,
      null,null,null,null,yundh,null,null,null,null,0 from FAHB@lhdb)
select*from qichjjbtmp@lhdb;
---车皮表
DELETE FROM CHEPB;
select*From chepb@lhdb;
insert into chepb(id,xuh,cheph,piaojh,yuanmz,maoz,piz,biaoz,yingd,yingk,yuns,koud,kous,kouz,koum,zongkd,sanfsl,ches,jianjfs,
                  guohb_id,fahb_id,chebb_id,yuanmkdw,yunsdwb_id,qingcsj,qingchh,qingcjjy,zhongcsj,zhongchh,zhongcjjy,meicb_id,
                  xiecb_id,daozch,hedbz,lursj,lury,beiz,yansbhb_id,kuangfzlzb_id,qicrjhb_id,xiecfsb_id,yuanpz,bulsj,banz,zhuangcdw_item_id,
                  meigy)
   (SELECT id,xuh,cheph,piaojh,yuanmz,maoz,piz,biaoz,yingd,yingk,yuns,koud,0,kouz,0,0,0,ches,jianjfs,guohb_id,fahb_id,3,yuanmkdw,
 (select id from YUNSDWB where MINGC=yunsdw),qingcsj,
4,jianjy,guohsj,2,diaodry, meicb_id,0,null,hedbz,guohsj,'系统管理员', beiz,0,0,0,2,piz,0, banz,0,meigy From chepb@lhdb);

--质量-------------------------------------------------------------------------------------------------------------------
select*from zhilb@lhdb;
DELETE from zhilb;
insert into zhilb(id,huaybh,caiyb_id,huaysj,qnet_ar,aar,ad,vdaf,mt,stad,aad,mad,qbad,had,vad,fcad,std,qgrad,hdaf,qgrad_daf,
                  sdaf,var,t1,t2,t3,t4,jijsf,jijfrl,huayy,lury,beiz,shenhzt,banz,liucztb_id,har,qgrd,star)
   (select id, huaybh,0, huaysj,farl,shoudjhf,ganzjhf,huiff,quansf,liuf,kongqgzjhf,kongqgzjsf,dantrl,kongqgzjq,kongqgzjhff,
fcad,ganzjl, ganzjgwrz,hdaf,ganzwhjgwrz, sdaf,0,t1,t2,t3,t4,  jijsf,jijfrl,huayy,lury2,null, shenhzt,null,1,0,qgr_d,
      round_new(LIUF*(100-QUANSF)/(100-KONGQGZJSF),2)
        from zhilb@lhdb);
--质量临时表
select*from zhillsb;
SELECT *FROM zhongw.huayrlb;
select*from zhongw.zhillsb;
SELECT *from zhilb;
DELETE from ZHILLSB;
insert into zhillsb(id,huaysj,qnet_ar,aar,ad,vdaf,mt,stad,aad,mad,qbad,had,vad,fcad,std,qgrad,hdaf,qgrad_daf,sdaf,var,t1,t2,t3,
                    t4,huayy,lury,beiz,shenhzt,HUAYLBB_ID,huaylb,bumb_id,har,qgrd,zhilb_id,
                    shenhry,shenhryej,shifsy,bil)
    (select id,huaysj,qnet_ar,aar,ad,vdaf,mt,stad,aad,mad,qbad,had,vad,fcad,std,qgrad,hdaf,qgrad_daf,sdaf,var,t1,t2,t3,t4,
          huayy,lury,beiz,7,20117454,'汽车采样',2018300,har,qgrd,id,(select max(shenhry) from linh0.zhilb l where l.id=id),
         (select max(shenhry) from linh0.zhilb l where l.id=id),1,1 from zhilb)
SELECT *from linh0.zhilb;
---zhuanmb
SELECT *from ZHUANMB;
select*from ZHUANMLB;
DELETE from ZHUANMB;
INSERT into zhuanmb (id,zhillsb_id,bianm,zhuanmlb_id)
select getnewid(197),z.id,z.huaybh,100663 from (select id,huaybh from zhilb) z;
---收耗存日报-------------------------------------------------------------------------------------------------------------
select*From shouhcrbb;
select*From  SHOUHCRBMB@lhdb
delete from shouhcrbb;
INSERT INTO SHOUHCRBB(id,diancxxb_id,riq,dangrgm,haoyqkdr,kuc,diancscsj,beiz,dangrfdl,tiaozl,shangbkc,diancscwjm,fadl,
                      quemtjts,quemtjrl,jingz,biaoz,yuns,yingd,kuid,fady,gongry,qity,cuns,shuifctz,panyk,diaoc,feiscy,
                      changwml,bukdml,kedkc,zhaungt,gongrl)
     (select id,diancxxb_id,riq,shisl,0,kuc,null,null,0,tiaoz,0,null,0,0,0,shisl,0,yuns,0,0,fady,gongry,qity,chus,SHUIFCTZ,
        0,diaoc,0,0,0,0,zhuangt,0 from SHOUHCRBMB@lhdb);

UPDATE shouhcrbb set haoyqkdr=(fady+gongry+qity);
--发电量找不到
------
SELECT *FROM pinzb;
select*From shouhcrbyb;
select*From  Haoyqkyb@lhdb;
DELETE from shouhcrbyb;
INSERT into shouhcrbyb(id,diancxxb_id,riq,pinzb_id,shourl,fady,gongry,qity,cuns,panyk,kuc,zhuangt)
   (select id,diancxxb_id,riq,43,0,fadyy,GONGRYy,QITYy,0,0,kuc,1 from Haoyqkyb@lhdb);
-----
------------------------------------------------------------------------------------------------------------------------
---结算表
SELECT *from jiesb;
insert into jiesb(id,bianm,diancxxb_id,meikxxb_id,gongysb_id,fahksrq,fahjzrq,ches,fapbh,yuanshr,yansbh,shoukdw,kaihyh,zhangh,
                  fukfs,duifdd,jiesrq,jiessl,buhsdj,buhsyf,ruzrq,ranlbmjbr,ranlbmjbrq,jieslx,yingd,zhiljq,kuid,yunj,jihkjb_id,
                  kuidjfyf,yuns,beiz,gongysmc,yunsfsb_id,faz,meiz,daibch,xianshr,guohl,koud,yansksrq,yansjzrq,jiesslcy,
                  hansdj,bukmk,hansmk,buhsmk,meikje,shuik,shuil,hetb_id/**/,liucztb_id,liucgzid,
                  jiesfrl,hetj,meikdwmc,qiyf,jiesrl,jieslf/**/,jiesrcrl,fuid,fengsjj,jiajqdj,jijlx,chaokdl,yunfhsdj,hansyf,
                  yunfjsl,kouk_js,weicdje,jieslx_dt,is_yujsd,chongdjsb_id,liucgzbid

                  )
 (SELECT id,jiesbh,diancxxb_id,meikxxb_id,fahdwb_id,fahksrq,fahjzrq,ches,fapbh,yuanshr,yansbh,shoukdw,kaihyh,zhangh,fukfs,duifdd,
jiesrq,jiessl,buhsdj,buhsyf,ruzrq,ranlbmjbr,ranlbmjbrq,jieslx,yingd,zhiljq,kuid,yunj,jihkjb_id,kuidjfyf,yuns,beiz,(select max(MINGC) from gongysb where id=fahdwb_id),
1,'汽',pinz,null,jizlx,gongfsl,0,kaisysrq,jiesysrq,0,buhsdj*1.17,bukyqjk,jiasje,jiakje,jiakje,jiaksk,jiaksl,0,0,0,
yansrl,hetjg,(select max(MINGC) from MEIKXXB where id=MEIKDQ_ID),0,yansrl,0,yansrl,0,0,buhsdj*1.17,0,0,0,0,yunfsl,0,0,0,0,0,0 from linh0.jiesb);
--结算运费表
SELECT *FROM linh0.jiesyfb
-----运输单位
INSERT  into YUNSDWB(id,DIANCXXB_ID,MINGC,QUANC,bianm)
 select GETNEWID(197),197,y.yunsdw,y.yunsdw,y.yunsdw from (select distinct yunsdw from linh0.chepb) y;
select distinct yunsdw from linh0.chepb;
SELECT *from YUNSDWB;
DELETE FROM YUNSDWB;
------------------------------------------------------------------------------------------------------------------------
select*from NIANDJH_CAIG;
select*from linh0.NIANDJHFB;
SELECT *FROM linh0.NIANDJHZB;
select*from linh0.hetxxb;
select*from hetb;
insert into hetb
(id,diancxxb_id,hetbh,qiandrq,gongfdwmc,gongfdwdz,gongffddbr,gongfwtdlr,gongfdh,gongfdbgh,gongfkhyh,
 gongfyzbm,xufdwmc,xufdwdz,xuffddbr,xufwtdlr,xufdh,xufdbgh,xufkhyh,xufzh,xufyzbm,gongfzh,
 gongfsh,xufsh,hetgysbid,gongysb_id,qianddd,qisrq,
 guoqrq,hetb_mb_id,jihkjb_id,liucztb_id,
 liucgzid,leib,hetjjfsb_id,meikmcs,xiaf,fuid)

  (select id, diancxxb_id, heth, qiandrq, ' ', gongfdwdz, gongffddbr, gongfwtdlr, gongfdh, gongfdbgh, gongfkhyh,
 gongfyzbm, xufdwmc, xufdwdz, xuffddbr, xufwtdlr, xufdh, xufdbgh, xufkhyh, xufzh, xufyzbm, gongfzh,0,shuih,0,0,' ',nvl(qissj,sysdate),nvl(guoqsj,sysdate),
 0,0,0,0,0,0,null,0,0 from linh0.hetxxb)
--  yunsfsb_id, faz_id, fahr, daoz_id,
-- shouhr, yunsfycdf, yunsfy,  shenhbz, shenhry, lury, jijfs, meikdqfdw_id, jihxz,  bentjs, caozlx, citjs,
--  jiesff, quypfyj, quypfr, jitpfyj, jitpfr, bucxybz, bucxybh, changbb_id, quyshyj, quycz,quyshr,quyshsj,jitshyj,jitshr,jitshsj,url,url2)
------------------------------------------------------------------------------------------------------------------------
--Qgr_ad = round_new((Qb_ad - (0.0941 * St_ad + dblA * Qb_ad)), 3);
--Qgr_d = round_new(Qgr_ad * 100 / (100 - Mt_ad), 3);
SELECT *FROM RULMZLB
--rulmzlb z,meihyb h,diancxxb d , rulbzb b, jizfzb j;
DELETE from RULMZLB;
insert into rulmzlb (id,rulrq,fenxrq,DIANCXXB_ID,rulbzb_id,jizfzb_id,
                     qnet_ar,aar,ad,vdaf,mt,stad,aad,mad,qbad,had,vad,/*fcad,*/std,qgrad,hdaf,qgrad_daf,sdaf,var,qgrd,star,har,
                     huayy,lury,beiz,lursj,shenhzt,erjshzt,meil)
  (SELECT r.id,rulrq,fenxrq,197,(select id from rulbzb l where l.mingc=BANC.mingc),476146581,
           farl,shoudjhf,ganzjhf,huiff,quansf,liuf,kongqgzjhf,kongqgzjsf,dantrl,kongqgzjq,kongqgzjhff,/*fcad,*/ganzjl,
           ganzjgwrz,
           hdaf,ganzwhjgwrz, sdaf,0,round_new(ganzjgwrz * 100 / (100 - kongqgzjsf), 3),round_new(LIUF*(100-QUANSF)/(100-KONGQGZJSF),3),0,
        huayy,shenhry,null, sysdate,shenhzt,0,meil from linh0.rulmzlb r,
   (SELECT *
    FROM linh0.JICXXB
    WHERE LEIX = '取煤位置'
   ) QUMWZB,
   (SELECT *
    FROM linh0.JICXXB
    WHERE LEIX = '入炉班次'
   ) BANC
 WHERE r.QUMWZ = QUMWZB.JIESBZ
       AND r.BANC = BANC.JIESBZ);
SELECT *from linh0.rulmzlb;
-----banz
SELECT *from RULBZB;
DELETE from RULBZB;
insert into rulbzb (id,diancxxb_id,mingc,xuh)
  (select id,197,mingc,xuh from linh0.JICXXB where leix='入炉班次');
--煤耗用
DELETE from meihyb;
select*from MEIHYB order by rulrq DESC;
insert into meihyb(id,rulrq,diancxxb_id,rulmzlb_id,rulbzb_id,jizfzb_id,fadhy,gongrhy,qity,feiscy,lury,lursj,shenhzt)
 SELECT * from (select id,riq,diancxxb_id,
           (select r.id from rulmzlb r inner join rulbzb b on r.rulbzb_id=b.id where r.rulrq=riq and b.mingc=banz) rulmzlb_id,
           (select id from rulbzb b where b.mingc=banz),476146581,
           fadym,gongrym,qitym,0,lury,sysdate,5 from linh0.haoyqkmb ) where  rulmzlb_id is not null

select*from RULMZLB;
select*from linh0.rulmzlb;
-------------------------------------盘点-------------------------------------------------------------------------------
select*from linh0.YUEMCMYB;
SELECT *FROM PAND_GDJT;
DELETE from PAND_GDjt ;
select*from linh0.YUEMCMYB a inner join linh0.yuemcmyzb b on a.yuemcmyzb_id=b.id;
insert into PAND_GDJT (id,riq,diancxxb_id,zhangmkc,shipkc,changsl,shuifctzl,yingkd,/*fujzt,fujmc,*/zhuangt)
(
  select max(a.id),trunc(riq),diancxxb_id,
  sum(case when a.xiangm like '%月末24时帐面库存%' then a.shulm end) zhangmkc ,
  sum(case when a.xiangm like '%月末24时实际库存%' then a.shulm end) shipkc ,
  sum(case
      when a.xiangm like '%盘点后储存损耗%' then a.shulm
      when a.xiangm like '%盘点后运输损耗%' then a.shulm
      when a.xiangm like '%发电耗%' then a.shulm
      when a.xiangm like '%供热耗%' then a.shulm
      when a.xiangm like '%其他耗%' then a.shulm
      end) changsl,
  sum(case when a.xiangm like '%盘点后水分差调整%' then a.shulm end) shipkc ,
  sum(case when a.xiangm like '%盘盈(+)或盘亏(-)%' then a.shulm end) yingkd,0
  from linh0.YUEMCMYB a inner join linh0.yuemcmyzb b on a.yuemcmyzb_id=b.id
  group by trunc(riq),diancxxb_id
)
--select*from jiesb order by jiesrq DESC
--select*from hetb order by qiandrq desc

-------------------------------月报--------------------------------------------------------------------------------------
--统计口径
select * from zhongdt.yuetjkjb where diancxxb_id=197 order by riq desc
select*from zhongdt.pinzb where id=102
select*From pinzb where id=62
SELECT *from YUETJKJB ORDER BY riq DESC ;
DELETE from YUETJKJB;
SELECT *from zhongdt.gongysb;
SELECT *from MEIKDQB;
insert into yuetjkjb (ID, RIQ, DIANCXXB_ID, XUH, GONGYSB_ID, JIHKJB_ID, PINZB_ID, YUNSFSB_ID)
 (
   select t.ID, t.RIQ, t.DIANCXXB_ID, t.XUH,
    (select max(a.id) from meikdqb a where a.mingc=g.mingc),
    (select max(a.id) from jihkjb a where a.mingc=j.mingc),
    62, t.YUNSFSB_ID
from zhongdt.yuetjkjb t inner join zhongdt.gongysb g on t.gongysb_id=g.id
  INNER JOIN zhongdt.jihkjb j on t.jihkjb_id=j.id
 where DIANCXXB_ID=197 --and g.mingc is not null
 );
SELECT *from zhongdt.yuetjkjb where DIANCXXB_ID=197;
SELECT *from zhongdt.yuetjkjb where id in(19711302627,19711337817);
select*from zhongdt.gongysb where id=3123
SELECT *from zhongdt.jihkjb
select*from yunsfsb
select *from zhongdt.gongysb where id=3123

insert INTO MEIKDQB (id,xuh,mingc,quanc,zhuangt,bianm,SHENGFB_ID)
  (select id,0,mingc,quanc,zhuangt,bianm,SHENGFB_ID from zhongdt.gongysb where id=3123 );
select*From shengfb where id=1012481;
select*from zhongdt.shengfb where id=1012481
--数量-------------------------------------------------------------------------------------------------------------------
SELECT *from yueslb;
delete from yueslb;
insert into yueslb (ID, YUETJKJB_ID, FENX, JINGZ, BIAOZ, YINGD, KUID, YUNS, KOUD, KOUS, KOUZ, KOUM, ZONGKD, SANFSL, JIANJL, RUCTZL, ZHUANGT, LAIMSL)
 (select*from zhongdt.yueslb where YUETJKJB_ID in (select id from YUETJKJB));


--质量-------------------------------------------------------------------------------------------------------------------
SELECT *from yuezlb
DELETE from yuezlb;
insert into yuezlb (ID, FENX, YUETJKJB_ID, QNET_AR, AAR, AD, VDAF, MT, STAD, AAD, MAD, QBAD, HAD, VAD, FCAD, STD, QBRAD,
                    HDAF, QGRAD_DAF, SDAF, VAR, QNET_AR_KF, AAR_KF, AD_KF, VDAF_KF, MT_KF, STAD_KF, AAD_KF, MAD_KF, QBAD_KF,
                    HAD_KF, VAD_KF, FCAD_KF, STD_KF, QBRAD_KF, HDAF_KF, QGRAD_DAF_KF, SDAF_KF, VAR_KF, ZHUANGT, DIANCRZ)
 (select ID, FENX, YUETJKJB_ID, QNET_AR, AAR, AD, VDAF, MT, STAD, AAD, MAD, QBAD, HAD, VAD, FCAD, STD, QBRAD,
        HDAF, QGRAD_DAF, SDAF, VAR, QNET_AR_KF, AAR_KF, AD_KF, VDAF_KF, MT_KF, STAD_KF, AAD_KF, MAD_KF, QBAD_KF,
        HAD_KF, VAD_KF, FCAD_KF, STD_KF, QBRAD_KF, HDAF_KF, QGRAD_DAF_KF, SDAF_KF, VAR_KF, ZHUANGT, DIANCRZ
from zhongdt.yuezlb where yuetjkjb_id in(select id from yuetjkjb));


--煤收耗存---------------------------------------------------------------------------------------------------------------
DELETE from YUESHCHJB;
SELECT *from yueshchjb;
insert into yueshchjb (ID, RIQ, FENX, QICKC, SHOUML, FADY, GONGRY, QITH, SUNH, DIAOCL, PANYK, KUC, DIANCXXB_ID, SHUIFCTZ, ZHUANGT, YUNS)
 (select * from zhongdt.yueshchjb where DIANCXXB_ID=197);


--油收耗存---------------------------------------------------------------------------------------------------------------
DELETE from YUESHCYB;
SELECT *from yueshcyb;
insert into yueshcyb (ID, RIQ, FENX, QICKC, SHOUYL, FADYY, GONGRY, QITHY, SUNH, DIAOCL, PANYK, KUC, DIANCXXB_ID, PINZB_ID, ZHUANGT)
 (select * from zhongdt.yueshcyb where DIANCXXB_ID=197);


--入厂煤成本--------------------------------------------------------------------------------------------------------------
DELETE from yuercbmdj;
SELECT *FROM yuercbmdj;
insert into yuercbmdj (ID, FENX, YUETJKJB_ID, HETJ, RELZJ, LIUFZJ, HUIFZJ, HUIFFZJ, SHUIFZJ, MEIJ, MEIJS, YUNJ, YUNJS,
                       DAOZZF, ZAF, QIT, QNET_AR, BIAOMDJ, BUHSBMDJ, JIAOHQZF, ZHUANGT, YUNSJL, YUNFSL, YUNFSLFS, DAOZZFS,
                       ZAFS, QITS, JIAOHQZFS, DAOZZFSL, DAOZZFSLFS, ZAFSL, ZAFSLFS, QITSL, QITSLFS, JIAOHQZFSL, JIAOHQZFSLFS)
 (select ID, FENX, YUETJKJB_ID, HETJ, RELZJ, LIUFZJ, HUIFZJ, HUIFFZJ, SHUIFZJ, MEIJ, MEIJS, YUNJ, YUNJS,
    DAOZZF, ZAF, QIT, QNET_AR, BIAOMDJ, BUHSBMDJ, JIAOHQZF, ZHUANGT, YUNSJL, YUNFSL, YUNFSLFS, DAOZZFS,
    ZAFS, QITS, JIAOHQZFS, DAOZZFSL, DAOZZFSLFS, ZAFSL, ZAFSLFS, QITSL, QITSLFS, JIAOHQZFSL, JIAOHQZFSLFS
  from zhongdt.yuercbmdj where yuetjkjb_id in (select id from yuetjkjb) );



--入厂油成本--------------------------------------------------------------------------------------------------------------
DELETE FROM rucycbb;
SELECT * FROM RUCYCBB;
insert into rucycbb (YOUFRL, ZHEBMDJ, ID, DIANCXXB_ID, RIQ, FENX, SHUL, HANSZHJ, BUHSZHJ, YOUJ, YOUS, YUNF, YUNFS,YUNZF,
QITFY, ZHUANGT, PINZB_ID)
  (SELECT YOUFRL, ZHEBMDJ, ID, DIANCXXB_ID, RIQ, FENX, SHUL, HANSZHJ, BUHSZHJ, YOUJ, YOUS, YUNF, YUNFS,
   YUNZF, QITFY, ZHUANGT, PINZB_ID from zhongdt.rucycbb where DIANCXXB_ID=197);


--月指标-----------------------------------------------------------------------------------------------------------------
SELECT *from yuezbb;
DELETE from yuezbb;
insert into yuezbb (ID, DIANCXXB_ID, RIQ, FENX, FADGRYTRML, RULMZBZML, RULTRMPJFRL, FADYTRML, GONGRYTRML, FADMZBML,
GONGRMZBML, FADGRYTRYL, RULTRYPJFRL, FADYTRYL, GONGRYTRYL, RULYZBZML, FADYZBZML, GONGRYZBZML, FADGRYTRQL,
RULTRQPJFRL, FADYTRQL, GONGRYTRQL, RULQZBZML, FADQZBZML, GONGRQZBZML, FADL, GONGDL, SHOUDL, GOURDL,
GONGRL, SHOURL, FADBZMH, GONGDBZMH, GONGRBZMH, RANLCB_BHS, FADMCB, FADYCB, GONGRMCB, GONGRYCB, FADRQCB,
GONGRRQCB, GONGRCYDFTRLF, QIZ_RANM, QIZ_RANY, QIZ_RANQ, SHOUDDWBDCB, SHOUDDWGDCB, SHOUDDJ, SHOURDJ,
SHOURDWCB, LIRZE, BENQRCMGJSL, BENQRCMGJZJE_BHS, RULTRMPJDJ, QIZ_FADTRMDJ, QIZ_GONGRTRMDJ, RULTRYPJDJ,
QIZ_FADTRYDJ, QIZ_GONGRTRQDJ, QIZ_GONGRTRYDJ, RULTRQPJDJ, QIZ_FADTRQDJ, FADBZMDJ, QIZ_MEIZBMDJ,
QIZ_YOUZBMDJ, QIZ_QIZBMDJ, GONGRBZMDJ, QIZ_MEIZBMDJ_GR, QIZ_YOUZBMDJ_GR, QIZ_QIZBMDJ_GR, FADDWRLCB,
SHOUDDWRLCB_MEID, SHOUDDWRLCB_QID, ZHUANGT, GONGRCGDL, FADZHCGDL, QITFY)
(SELECT ID, DIANCXXB_ID, RIQ, FENX, FADGRYTRML, RULMZBZML, RULTRMPJFRL, FADYTRML, GONGRYTRML, FADMZBML,
GONGRMZBML, FADGRYTRYL, RULTRYPJFRL, FADYTRYL, GONGRYTRYL, RULYZBZML, FADYZBZML, GONGRYZBZML, FADGRYTRQL,
RULTRQPJFRL, FADYTRQL, GONGRYTRQL, RULQZBZML, FADQZBZML, GONGRQZBZML, FADL, GONGDL, SHOUDL, GOURDL,
GONGRL, SHOURL, FADBZMH, GONGDBZMH, GONGRBZMH, RANLCB_BHS, FADMCB, FADYCB, GONGRMCB, GONGRYCB, FADRQCB,
GONGRRQCB, GONGRCYDFTRLF, QIZ_RANM, QIZ_RANY, QIZ_RANQ, SHOUDDWBDCB, SHOUDDWGDCB, SHOUDDJ, SHOURDJ,
SHOURDWCB, LIRZE, BENQRCMGJSL, BENQRCMGJZJE_BHS, RULTRMPJDJ, QIZ_FADTRMDJ, QIZ_GONGRTRMDJ, RULTRYPJDJ,
QIZ_FADTRYDJ, QIZ_GONGRTRQDJ, QIZ_GONGRTRYDJ, RULTRQPJDJ, QIZ_FADTRQDJ, FADBZMDJ, QIZ_MEIZBMDJ,
QIZ_YOUZBMDJ, QIZ_QIZBMDJ, GONGRBZMDJ, QIZ_MEIZBMDJ_GR, QIZ_YOUZBMDJ_GR, QIZ_QIZBMDJ_GR, FADDWRLCB,
SHOUDDWRLCB_MEID, SHOUDDWRLCB_QID, ZHUANGT, GONGRCGDL, FADZHCGDL, QITFY FROM zhongdt.yuezbb where DIANCXXB_ID=197);

--年度合同执行------------------------------------------------------------------------------------------------------------
SELECT *from NIANDHTZXQKB;
DELETE from NIANDHTZXQKB;
insert into niandhtzxqkb (ID, DIANCXXB_ID, GONGYSB_ID, BEIZ, RIQ, HETL, HETJZRZ, HETJZCKJG, HETJZDCJG, HETZJKHBZ, YANSFS, ZHUANGT, YUNZF)
SELECT ID, DIANCXXB_ID, GONGYSB_ID, BEIZ, RIQ, HETL, HETJZRZ, HETJZCKJG, HETJZDCJG, HETZJKHBZ, YANSFS, ZHUANGT, 0
 from zhongdt.NIANDHTZXQKB where diancxxb_id=197;
commit;
tables=(yuetjkjb,NIANDHTZXQKB,yuezbb,rucycbb,yuercbmdj,YUESHCYB,YUESHCHJB,yuezlb,yueslb)


