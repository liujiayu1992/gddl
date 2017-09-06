CREATE OR REPLACE Function getGongysId(strGongysName In varchar2)
  Return  number as
  begin
    declare
      gongysid number;
    begin
      select max(id) into gongysid from gongysb dc where dc.mingc=strGongysName;
      return gongysid;
    end;
  End;
-- Create table---------------------------------------------------------------------------------------------------------
create table NIANDHTZXQKB
(
  id          NUMBER(15) not null,
  diancxxb_id NUMBER(15) not null,
  gongysb_id  NUMBER(15) not null,
  beiz        VARCHAR2(200),
  riq         DATE not null,
  hetl        NUMBER(10,2),
  hetjzrz     NUMBER(10,2),
  hetjzckjg   NUMBER(10,3),
  hetjzdcjg   NUMBER(10,3),
  hetzjkhbz   NUMBER(10,3),
  yansfs      VARCHAR2(20),
  zhuangt     NUMBER(2) default 0
)
;
-- Add comments to the table
comment on table NIANDHTZXQKB
is '年度合同情况表';
-- Add comments to the columns
comment on column NIANDHTZXQKB.diancxxb_id
is '电厂ID';
comment on column NIANDHTZXQKB.gongysb_id
is '供应商ID';
comment on column NIANDHTZXQKB.beiz
is '备注';
comment on column NIANDHTZXQKB.riq
is '日期（年）';
comment on column NIANDHTZXQKB.hetl
is '合同数量';
comment on column NIANDHTZXQKB.hetjzrz
is '合同基准热值';
comment on column NIANDHTZXQKB.hetjzckjg
is '合同基准车板/平仓/出矿价格';
comment on column NIANDHTZXQKB.hetjzdcjg
is '合同基准到厂价格';
comment on column NIANDHTZXQKB.hetzjkhbz
is '合同质价考核标准';
comment on column NIANDHTZXQKB.yansfs
is '验收方式（矿方/到厂）';
comment on column NIANDHTZXQKB.zhuangt
is '审核状态（0，1，2）';
-- Create/Recreate primary, unique and foreign key constraints
alter table NIANDHTZXQKB
  add constraint MK_NIANDHTZXQKB primary key (ID)
  using index
 ;
------------------------------------------------------------------------------------------------------------------------


insert into ziyxxb (ID, XUH, MINGC, WENJWZ, JIB, FUID, LEIX, LEIB)
values (10025281763, 3, 'cpi燃料管理05表', 'Yuemtgyqkbreport', 3, 11103, -1, 3);

insert into ziyxxb (ID, XUH, MINGC, WENJWZ, JIB, FUID, LEIX, LEIB)
values (10025281766, 4, 'cpi燃料管理06表', 'Yuemthcqkbreport', 3, 11103, -1, 3);

insert into ziyxxb (ID, XUH, MINGC, WENJWZ, JIB, FUID, LEIX, LEIB)
values (10025281765, 5, 'cpi燃料管理07表', 'Yuesyhcqkbreport', 3, 11103, -1, 3);

insert into ziyxxb (ID, XUH, MINGC, WENJWZ, JIB, FUID, LEIX, LEIB)
values (10025281767, 6, 'cpi燃料管理08表', 'Biaomhyqkbreport', 3, 11103, -1, 3);

insert into ziyxxb (ID, XUH, MINGC, WENJWZ, JIB, FUID, LEIX, LEIB)
values (10025281768, 7, 'cpi燃料管理09表', 'Yuemyzlqkbreport', 3, 11103, -1, 3);

insert into ziyxxb (ID, XUH, MINGC, WENJWZ, JIB, FUID, LEIX, LEIB)
values (10025281769, 8, 'cpi燃料管理10表', 'Rucmdj_newreport', 3, 11103, -1, 3);

insert into ziyxxb (ID, XUH, MINGC, WENJWZ, JIB, FUID, LEIX, LEIB)
values (10025281770, 9, 'cpi燃料管理11表', 'yuefdbmdjqkbreport', 3, 11103, -1, 3);

insert into ziyxxb (ID, XUH, MINGC, WENJWZ, JIB, FUID, LEIX, LEIB)
values (20390209, 10, 'cpi燃料管理12表', 'Ranlcbfybreport', 3, 11103, -1, 3);

insert into ziyxxb (ID, XUH, MINGC, WENJWZ, JIB, FUID, LEIX, LEIB)
values (1006287631, 15, '月报审核', 'Yuebshsb', 3, 100299916, -1, 3);

insert into ziyxxb (ID, XUH, MINGC, WENJWZ, JIB, FUID, LEIX, LEIB)
values (1007141870, 22, '年度合同执行情况月报', 'Niandhtzxqkreport', 3, 11103, -1, 3);
;
-------------------------------------------------------------------------------------------------------------------------
create or replace view vwfenxyue as
  select 1 as xuh, decode(1,1,'本月') as fenx from dual
  union
  select 2 as xuh,decode(1,1,'累计') as fenx from dual;
-- Create table
create table DIANCKJB
(
  id          NUMBER(15) not null,
  diancxxb_id NUMBER(15),
  xuh         VARCHAR2(10),
  mingc       VARCHAR2(20),
  fenl_id     VARCHAR2(15),
  beiz        VARCHAR2(200)
)
;
-- Add comments to the columns------------------------------------------------------------------------------------------
comment on column DIANCKJB.diancxxb_id
is '口径所属的';
comment on column DIANCKJB.xuh
is '序号';
comment on column DIANCKJB.mingc
is '口径名称';
comment on column DIANCKJB.fenl_id
is '分类id在item表中配置,(月报统计、日报统计)';
------------------------------------------------------------------------------------------------------------------------
create or replace function getShenhzt(koujid    in number,
                                      diancfuid in varchar2,
                                      dqmc      in varchar2,
                                      fgsmc     in varchar2,
                                      dcmc      in varchar2,
                                      fDate     in date, --riq1
                                      lDate     in date, --riq2
                                      tablename in varchar2,
                                      fenx      in varchar2,
                                      UserJib   in number) return integer is
  rtn integer;
  begin
    declare
      sql_stmt  varchar2(1000);
      sql_fenkj varchar2(500);
    begin
      sql_fenkj := ' and dc.id in (select dcid.diancxxb_id' || chr(10) ||
                   '       from (select distinct diancxxb_id' || chr(10) ||
                   '          from yuetjkjb' || chr(10) ||
                   '         where (riq = :fDate or' || chr(10) ||
                   '               riq = :lDate)) dcid,' || chr(10) ||
                   '       diancxxb  dc,' || chr(10) ||
                   '       dianckjmx kjmx' || chr(10) ||
                   ' where dc.id = dcid.diancxxb_id' || chr(10) ||
                   '   and kjmx.diancxxb_id(+) = dc.id' || chr(10) ||
                   '   and kjmx.dianckjb_id =:koujid)';
      if tablename = 'yueslb' then
        --cpi05
        if UserJib = 1 then
          --集团用户
          if dqmc is null and fgsmc is null and dcmc is null then
            --集团
            sql_stmt := 'select count(distinct(dc.id)) from ' || tablename ||
                        ' sl, yuetjkjb tj,jihkjb j,vwdianc dc where sl.yuetjkjb_id = tj.id and tj.jihkjb_id=j.id  and tj.diancxxb_id=dc.id and dc.fuid <> -1 and tj.riq >=:fDate and tj.riq <= :lDate and sl.zhuangt <> 2 and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
            if koujid > 0 then
              sql_stmt := sql_stmt || sql_fenkj;
              execute immediate sql_stmt
              into rtn
              using fDate, lDate, fenx, fDate, lDate, koujid;
            else
              execute immediate sql_stmt
              into rtn
              using fDate, lDate, fenx;
            end if;
          elsif dqmc is null and fgsmc is not null and dcmc is null then
            --按分公司统计公司层
            sql_stmt := 'select count(distinct(dc.id)) from ' || tablename ||
                        ' sl, yuetjkjb tj,jihkjb j,vwdianc dc where sl.yuetjkjb_id = tj.id and tj.jihkjb_id=j.id  and tj.diancxxb_id=dc.id and dc.fuid <> -1 and dc.fgsmc = :fgsmc and  tj.riq >=:fDate and tj.riq <= :lDate  and sl.zhuangt <> 2 and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
            if koujid > 0 then
              sql_stmt := sql_stmt || sql_fenkj;
              execute immediate sql_stmt
              into rtn
              using fgsmc, fDate, lDate, fenx, fDate, lDate, koujid;
            else
              execute immediate sql_stmt
              into rtn
              using fgsmc, fDate, lDate, fenx;
            end if;
          elsif dqmc is not null and fgsmc is null and dcmc is null then
            --按地区统计地区层
            sql_stmt := 'select count(distinct(dc.id)) from ' || tablename ||
                        ' sl, yuetjkjb tj,jihkjb j,diancxxb dc,shengfb sf,shengfdqb dq where dc.shengfb_id = sf.id and sf.shengfdqb_id = dq.id and sl.yuetjkjb_id = tj.id and tj.jihkjb_id=j.id  and tj.diancxxb_id=dc.id and dc.fuid <> -1 and dq.mingc = :dqmc and  tj.riq >=:fDate and tj.riq <= :lDate  and sl.zhuangt <> 2 and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
            if koujid > 0 then
              sql_stmt := sql_stmt || sql_fenkj;
              execute immediate sql_stmt
              into rtn
              using dqmc, fDate, lDate, fenx, fDate, lDate, koujid;
            else
              execute immediate sql_stmt
              into rtn
              using dqmc, fDate, lDate, fenx;
            end if;
          else
            if dqmc is not null and fgsmc is null and dcmc is not null then
              --按地区统计电厂层
              sql_stmt := 'select count(distinct(dc.id)) from ' || tablename ||
                          ' sl, yuetjkjb tj,jihkjb j,diancxxb dc,shengfb sf,shengfdqb dq where dc.shengfb_id = sf.id and sf.shengfdqb_id = dq.id and sl.yuetjkjb_id = tj.id and tj.jihkjb_id=j.id  and tj.diancxxb_id=dc.id and dc.fuid <> -1 and dc.mingc = :dcmc and  tj.riq >=:fDate and tj.riq <= :lDate  and sl.zhuangt <> 2 and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
              execute immediate sql_stmt
              into rtn
              using dcmc, fDate, lDate, fenx;
            elsif dqmc is null and fgsmc is not null and dcmc is not null then
              --按电厂统计电厂层
              sql_stmt := 'select count(distinct(dc.id)) from ' || tablename ||
                          ' sl, yuetjkjb tj,jihkjb j,vwdianc dc where sl.yuetjkjb_id = tj.id and tj.jihkjb_id=j.id  and tj.diancxxb_id=dc.id and dc.fuid <> -1 and dc.mingc = :dcmc and  tj.riq >=:fDate and tj.riq <= :lDate  and sl.zhuangt <> 2 and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
              execute immediate sql_stmt
              into rtn
              using dcmc, fDate, lDate, fenx;
            end if;
          end if;
        elsif Userjib = 2 then
          --公司级用户
          if dqmc is null and fgsmc is null and dcmc is null then
            --总计
            sql_stmt := 'select count(distinct(dc.id)) from ' || tablename ||
                        ' sl, yuetjkjb tj,jihkjb j,vwdianc dc where sl.yuetjkjb_id = tj.id and tj.jihkjb_id=j.id  and tj.diancxxb_id=dc.id and dc.fuid <> -1 and dc.fuid=:diancfuid  and  tj.riq >=:fDate and tj.riq <= :lDate  and sl.zhuangt =0 and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
            execute immediate sql_stmt
            into rtn
            using diancfuid, fDate, lDate, fenx;
          elsif dqmc is null and fgsmc is not null and dcmc is null then
            --按电厂统计公司层
            sql_stmt := 'select count(distinct(dc.id)) from ' || tablename ||
                        ' sl, yuetjkjb tj,jihkjb j,vwdianc dc where sl.yuetjkjb_id = tj.id and tj.jihkjb_id=j.id  and tj.diancxxb_id=dc.id and dc.fuid <> -1 and dc.fuid=:diancfuid and dc.fgsmc=:fgsmc and  tj.riq >=:fDate and tj.riq <= :lDate  and sl.zhuangt =0 and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
            execute immediate sql_stmt
            into rtn
            using diancfuid, fgsmc, fDate, lDate, fenx;
          elsif dqmc is not null and fgsmc is null and dcmc is null then
            --按地区统计地区层
            sql_stmt := 'select count(distinct(dc.id)) from ' || tablename ||
                        ' sl, yuetjkjb tj,jihkjb j,diancxxb dc,shengfb sf,shengfdqb dq where dc.shengfb_id = sf.id and sf.shengfdqb_id = dq.id and sl.yuetjkjb_id = tj.id and tj.jihkjb_id=j.id  and tj.diancxxb_id=dc.id and dc.fuid <> -1 and dc.fuid=:diancfuid and dq.mingc = :dqmc and  tj.riq >=:fDate and tj.riq <= :lDate  and sl.zhuangt =0 and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
            execute immediate sql_stmt
            into rtn
            using diancfuid, dqmc, fDate, lDate, fenx;
          else
            --电厂层
            if dqmc is not null and fgsmc is null and dcmc is not null then
              --按地区统计电厂层
              sql_stmt := 'select count(distinct(dc.id)) from ' || tablename ||
                          ' sl, yuetjkjb tj,jihkjb j,diancxxb dc,shengfb sf,shengfdqb dq where dc.shengfb_id = sf.id and sf.shengfdqb_id = dq.id and sl.yuetjkjb_id = tj.id and tj.jihkjb_id=j.id  and tj.diancxxb_id=dc.id and dc.fuid <> -1 and dc.mingc = :dcmc and  tj.riq >=:fDate and tj.riq <= :lDate  and sl.zhuangt =0 and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
              execute immediate sql_stmt
              into rtn
              using dcmc, fDate, lDate, fenx;
            else
              if dqmc is null and fgsmc is not null and dcmc is not null then
                --按电厂统计电厂层
                sql_stmt := 'select count(distinct(dc.id)) from ' ||
                            tablename ||
                            ' sl, yuetjkjb tj,jihkjb j,vwdianc dc where sl.yuetjkjb_id = tj.id and tj.jihkjb_id=j.id  and tj.diancxxb_id=dc.id and dc.fuid <> -1 and dc.mingc = :dcmc and  tj.riq >=:fDate and tj.riq <= :lDate  and sl.zhuangt =0 and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
                execute immediate sql_stmt
                into rtn
                using dcmc, fDate, lDate, fenx;
              end if;
            end if;
          end if;
        end if;
      elsif tablename = 'yueshchjb' then
        --cpi06
        if UserJib = 1 then
          --集团用户
          if dqmc is null and fgsmc is null and dcmc is null then
            --总计
            sql_stmt := 'select count(distinct(dc.id)) from ' || tablename ||
                        ' sl,yuezbb zb,vwdianc dc where sl.diancxxb_id = dc.id and zb.diancxxb_id=dc.id and dc.fuid <> -1 and sl.riq >=:fDate and sl.riq <= :lDate and zb.riq >=:fDate and zb.riq <= :lDate and (sl.zhuangt <> 2 or zb.zhuangt <> 2) and zb.fenx=:fenx and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
            if koujid > 0 then
              sql_stmt := sql_stmt || sql_fenkj;
              execute immediate sql_stmt
              into rtn
              using fDate, lDate, fDate, lDate, fenx, fenx, fDate, lDate, koujid;
            else
              execute immediate sql_stmt
              into rtn
              using fDate, lDate, fDate, lDate, fenx, fenx;
            end if;
          elsif dqmc is null and fgsmc is not null and dcmc is null then
            --按分公司统计公司层
            sql_stmt := 'select count(distinct(dc.id)) from ' || tablename ||
                        ' sl,yuezbb zb,vwdianc dc where sl.diancxxb_id = dc.id and zb.diancxxb_id=dc.id and dc.fuid <> -1  and dc.fgsmc=:fgsmc and sl.riq >=:fDate and sl.riq <= :lDate and zb.riq >=:fDate and zb.riq <= :lDate and (sl.zhuangt <> 2 or zb.zhuangt <> 2) and zb.fenx=:fenx and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
            if koujid > 0 then
              sql_stmt := sql_stmt || sql_fenkj;
              execute immediate sql_stmt
              into rtn
              using fgsmc, fDate, lDate, fDate, lDate, fenx, fenx, fDate, lDate, koujid;
            else
              execute immediate sql_stmt
              into rtn
              using fgsmc, fDate, lDate, fDate, lDate, fenx, fenx;
            end if;
          elsif dqmc is not null and fgsmc is null and dcmc is null then
            --按地区统计地区层
            sql_stmt := 'select count(distinct(dc.id)) from ' || tablename ||
                        ' sl,yuezbb zb,diancxxb dc,shengfb sf,shengfdqb dq where dc.shengfb_id = sf.id and sf.shengfdqb_id = dq.id and sl.diancxxb_id = dc.id and zb.diancxxb_id=dc.id and dc.fuid <> -1 and dq.mingc=:dqmc and sl.riq >=:fDate and sl.riq <= :lDate and zb.riq >=:fDate and zb.riq <= :lDate and (sl.zhuangt <> 2 or zb.zhuangt <> 2) and zb.fenx=:fenx and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
            if koujid > 0 then
              sql_stmt := sql_stmt || sql_fenkj;
              execute immediate sql_stmt
              into rtn
              using dqmc, fDate, lDate, fDate, lDate, fenx, fenx, fDate, lDate, koujid;
            else
              execute immediate sql_stmt
              into rtn
              using dqmc, fDate, lDate, fDate, lDate, fenx, fenx;
            end if;
          else
            --电厂层
            if dqmc is not null and fgsmc is null and dcmc is not null then
              --按地区统计电厂层
              sql_stmt := 'select count(distinct(dc.id)) from ' || tablename ||
                          ' sl,yuezbb zb,diancxxb dc,shengfb sf,shengfdqb dq where dc.shengfb_id = sf.id and sf.shengfdqb_id = dq.id and sl.diancxxb_id = dc.id and zb.diancxxb_id=dc.id and dc.fuid <> -1 and dc.mingc=:dcmc and sl.riq >=:fDate and sl.riq <= :lDate and zb.riq >=:fDate and zb.riq <= :lDate and (sl.zhuangt <> 2 or zb.zhuangt <> 2) and zb.fenx=:fenx and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
              execute immediate sql_stmt
              into rtn
              using dcmc, fDate, lDate, fDate, lDate, fenx, fenx;
            else
              if dqmc is null and fgsmc is not null and dcmc is not null then
                --按电厂统计电厂层
                sql_stmt := 'select count(distinct(dc.id)) from ' ||
                            tablename ||
                            ' sl,yuezbb zb,vwdianc dc where sl.diancxxb_id = dc.id and zb.diancxxb_id=dc.id and dc.fuid <> -1  and dc.mingc=:mingc and sl.riq >=:fDate and sl.riq <= :lDate and zb.riq >=:fDate and zb.riq <= :lDate and (sl.zhuangt <> 2 or zb.zhuangt <> 2) and zb.fenx=:fenx and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
                execute immediate sql_stmt
                into rtn
                using dcmc, fDate, lDate, fDate, lDate, fenx, fenx;
              end if;
            end if;
          end if;
        elsif Userjib = 2 then
          --公司级用户
          if dqmc is null and fgsmc is null and dcmc is null then
            --总计
            sql_stmt := 'select count(distinct(dc.id)) from ' || tablename ||
                        ' sl,yuezbb zb,vwdianc dc where sl.diancxxb_id = dc.id and zb.diancxxb_id=dc.id and dc.fuid <> -1 and dc.fuid=:diancfuid and sl.riq >=:fDate and sl.riq <= :lDate and zb.riq >=:fDate and zb.riq <= :lDate and (sl.zhuangt =0 or zb.zhuangt =0) and zb.fenx=:fenx and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
            execute immediate sql_stmt
            into rtn
            using diancfuid, fDate, lDate, fDate, lDate, fenx, fenx;
          elsif dqmc is null and fgsmc is not null and dcmc is null then
            --按电厂统计公司层
            sql_stmt := 'select count(distinct(dc.id)) from ' || tablename ||
                        ' sl,yuezbb zb,vwdianc dc where sl.diancxxb_id = dc.id and zb.diancxxb_id=dc.id and dc.fuid <> -1 and dc.fuid=:diancfuid and dc.fgsmc=:fgsmc and sl.riq >=:fDate and sl.riq <= :lDate and zb.riq >=:fDate and zb.riq <= :lDate and (sl.zhuangt =0 or zb.zhuangt =0) and zb.fenx=:fenx and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
            execute immediate sql_stmt
            into rtn
            using diancfuid, fgsmc, fDate, lDate, fDate, lDate, fenx, fenx;
          elsif dqmc is not null and fgsmc is null and dcmc is null then
            --按地区统计地区层
            sql_stmt := 'select count(distinct(dc.id)) from ' || tablename ||
                        ' sl,yuezbb zb,diancxxb dc,shengfb sf,shengfdqb dq where dc.shengfb_id = sf.id and sf.shengfdqb_id = dq.id and sl.diancxxb_id = dc.id and zb.diancxxb_id=dc.id and dc.fuid <> -1 and and dc.fuid=:diancfuid dq.mingc=:dqmc and sl.riq >=:fDate and sl.riq <= :lDate and zb.riq >=:fDate and zb.riq <= :lDate and (sl.zhuangt =0 or zb.zhuangt =0) and zb.fenx=:fenx and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
            execute immediate sql_stmt
            into rtn
            using diancfuid, dqmc, fDate, lDate, fDate, lDate, fenx, fenx;
          else
            if dqmc is not null and fgsmc is null and dcmc is not null then
              --按地区统计电厂层
              sql_stmt := 'select count(distinct(dc.id)) from ' || tablename ||
                          ' sl,yuezbb zb,diancxxb dc,shengfb sf,shengfdqb dq where dc.shengfb_id = sf.id and sf.shengfdqb_id = dq.id and sl.diancxxb_id = dc.id and zb.diancxxb_id=dc.id and dc.fuid <> -1 and dc.mingc=:dcmc and sl.riq >=:fDate and sl.riq <= :lDate and zb.riq >=:fDate and zb.riq <= :lDate and (sl.zhuangt =0 or zb.zhuangt =0) and zb.fenx=:fenx and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
              execute immediate sql_stmt
              into rtn
              using dcmc, fDate, lDate, fDate, lDate, fenx, fenx;
            else
              if dqmc is null and fgsmc is not null and dcmc is not null then
                --按电厂统计电厂层
                sql_stmt := 'select count(distinct(dc.id)) from ' ||
                            tablename ||
                            ' sl,yuezbb zb,vwdianc dc where sl.diancxxb_id = dc.id and zb.diancxxb_id=dc.id and dc.fuid <> -1  and dc.mingc=:mingc and sl.riq >=:fDate and sl.riq <= :lDate and zb.riq >=:fDate and zb.riq <= :lDate and (sl.zhuangt =0 or zb.zhuangt =0) and zb.fenx=:fenx and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
                execute immediate sql_stmt
                into rtn
                using dcmc, fDate, lDate, fDate, lDate, fenx, fenx;
              end if;
            end if;
          end if;
        end if;
      elsif tablename = 'yueshcyb' then
        --cpi07
        if UserJib = 1 then
          --集团用户
          if dqmc is null and fgsmc is null and dcmc is null then
            --总计
            sql_stmt := 'select count(distinct(dc.id)) from ' || tablename ||
                        ' sl,vwdianc dc where sl.diancxxb_id = dc.id and dc.fuid <> -1 and sl.riq >=:fDate and sl.riq <= :lDate and sl.zhuangt <> 2 and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
            if koujid > 0 then
              sql_stmt := sql_stmt || sql_fenkj;
              execute immediate sql_stmt
              into rtn
              using fDate, lDate, fenx, fDate, lDate, koujid;
            else
              execute immediate sql_stmt
              into rtn
              using fDate, lDate, fenx;
            end if;
          elsif dqmc is null and fgsmc is not null and dcmc is null then
            --按分公司统计公司层
            sql_stmt := 'select count(distinct(dc.id)) from ' || tablename ||
                        ' sl,vwdianc dc where sl.diancxxb_id = dc.id and dc.fuid <> -1 and dc.fgsmc = :fgsmc and sl.riq >=:fDate and sl.riq <= :lDate  and sl.zhuangt <> 2 and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
            if koujid > 0 then
              sql_stmt := sql_stmt || sql_fenkj;
              execute immediate sql_stmt
              into rtn
              using fgsmc, fDate, lDate, fenx, fDate, lDate, koujid;
            else
              execute immediate sql_stmt
              into rtn
              using fgsmc, fDate, lDate, fenx;
            end if;
          elsif dqmc is not null and fgsmc is null and dcmc is null then
            --按地区统计地区层
            sql_stmt := 'select count(distinct(dc.id)) from ' || tablename ||
                        ' sl,diancxxb dc,shengfb sf,shengfdqb dq where dc.shengfb_id = sf.id and sf.shengfdqb_id = dq.id and sl.diancxxb_id = dc.id and  dc.fuid <> -1 and dq.mingc=:dqmc and sl.riq >=:fDate and sl.riq <= :lDate and sl.zhuangt <> 2 and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
            if koujid > 0 then
              sql_stmt := sql_stmt || sql_fenkj;
              execute immediate sql_stmt
              into rtn
              using dqmc, fDate, lDate, fenx, fDate, lDate, koujid;
            else
              execute immediate sql_stmt
              into rtn
              using dqmc, fDate, lDate, fenx;
            end if;
          else
            --电厂
            if dqmc is not null and fgsmc is null and dcmc is not null then
              --按地区统计电厂层
              sql_stmt := 'select count(distinct(dc.id)) from ' || tablename ||
                          ' sl,diancxxb dc,shengfb sf,shengfdqb dq where dc.shengfb_id = sf.id and sf.shengfdqb_id = dq.id and sl.diancxxb_id = dc.id and  dc.fuid <> -1 and dc.mingc=:dcmc and sl.riq >=:fDate and sl.riq <= :lDate and sl.zhuangt <> 2 and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
              execute immediate sql_stmt
              into rtn
              using dcmc, fDate, lDate, fenx;
            else
              if dqmc is null and fgsmc is not null and dcmc is not null then
                --按电厂统计电厂层
                sql_stmt := 'select count(distinct(dc.id)) from ' ||
                            tablename ||
                            ' sl,vwdianc dc where sl.diancxxb_id = dc.id and dc.fuid <> -1 and dc.mingc = :mingc and sl.riq >=:fDate and sl.riq <= :lDate  and sl.zhuangt <> 2 and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
                execute immediate sql_stmt
                into rtn
                using dcmc, fDate, lDate, fenx;
              end if;
            end if;
          end if;
        elsif Userjib = 2 then
          --公司级用户
          if dqmc is null and fgsmc is null and dcmc is null then
            --总计
            sql_stmt := 'select count(distinct(dc.id)) from ' || tablename ||
                        ' sl,vwdianc dc where sl.diancxxb_id = dc.id and dc.fuid <> -1 and dc.fuid=:diancfuid and sl.riq >=:fDate and sl.riq <= :lDate  and sl.zhuangt =0 and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
            execute immediate sql_stmt
            into rtn
            using diancfuid, fDate, lDate, fenx;
          elsif dqmc is null and fgsmc is not null and dcmc is null then
            --按电厂统计公司层
            sql_stmt := 'select count(distinct(dc.id)) from ' || tablename ||
                        ' sl,vwdianc dc where sl.diancxxb_id = dc.id and dc.fuid <> -1 and dc.fuid=:diancfuid and dc.fgsmc = :fgsmc and sl.riq >=:fDate and sl.riq <= :lDate  and sl.zhuangt =0 and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
            execute immediate sql_stmt
            into rtn
            using diancfuid, fgsmc, fDate, lDate, fenx;
          elsif dqmc is not null and fgsmc is null and dcmc is null then
            --按地区统计地区层
            sql_stmt := 'select count(distinct(dc.id)) from ' || tablename ||
                        ' sl,diancxxb dc,shengfb sf,shengfdqb dq where dc.shengfb_id = sf.id and sf.shengfdqb_id = dq.id and sl.diancxxb_id = dc.id and  dc.fuid <> -1 and dc.fuid=:diancfuid and dq.mingc=:dqmc and sl.riq >=:fDate and sl.riq <= :lDate and sl.zhuangt =0 and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
            execute immediate sql_stmt
            into rtn
            using diancfuid, dqmc, fDate, lDate, fenx;
          else
            --电厂层
            if dqmc is not null and fgsmc is null and dcmc is not null then
              --按地区统计电厂层
              sql_stmt := 'select count(distinct(dc.id)) from ' || tablename ||
                          ' sl,diancxxb dc,shengfb sf,shengfdqb dq where dc.shengfb_id = sf.id and sf.shengfdqb_id = dq.id and sl.diancxxb_id = dc.id and  dc.fuid <> -1 and dc.mingc=:dcmc and sl.riq >=:fDate and sl.riq <= :lDate and sl.zhuangt =0 and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
              execute immediate sql_stmt
              into rtn
              using dcmc, fDate, lDate, fenx;
            else
              if dqmc is null and fgsmc is not null and dcmc is not null then
                --按电厂统计电厂层
                sql_stmt := 'select count(distinct(dc.id)) from ' ||
                            tablename ||
                            ' sl,vwdianc dc where sl.diancxxb_id = dc.id and dc.fuid <> -1 and dc.mingc = :mingc and sl.riq >=:fDate and sl.riq <= :lDate  and sl.zhuangt =0 and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
                execute immediate sql_stmt
                into rtn
                using dcmc, fDate, lDate, fenx;
              end if;
            end if;
          end if;
        end if;
      elsif tablename = 'rucycbb' then
        --cpi09
        if UserJib = 1 then
          --集团用户
          if dqmc is null and fgsmc is null and dcmc is null then
            --总计
            sql_stmt := 'select count(distinct(dc.id)) from ' || tablename ||
                        ' sl,yuetjkjb tj,yueslb slb,yuezlb zl,yuezbb zb,vwdianc dc where tj.diancxxb_id=dc.id and zb.diancxxb_id=dc.id and sl.diancxxb_id=dc.id and dc.fuid<>-1 and slb.yuetjkjb_id=tj.id and zl.yuetjkjb_id=slb.yuetjkjb_id and sl.riq>=:fDate and sl.riq<=:lDate and tj.riq>=:fDate and tj.riq<=:lDate and zb.riq>=:fDate and zb.riq<=:lDate and (slb.zhuangt<>2 or zl.zhuangt<>2 or zb.zhuangt<>2) and slb.fenx=:fenx and zl.fenx = slb.fenx and zb.fenx=:fenx and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
            if koujid > 0 then
              sql_stmt := sql_stmt || sql_fenkj;
              execute immediate sql_stmt
              into rtn
              using fDate, lDate, fDate, lDate, fDate, lDate, fenx, fenx, fenx, fDate, lDate, koujid;
            else
              execute immediate sql_stmt
              into rtn
              using fDate, lDate, fDate, lDate, fDate, lDate, fenx, fenx, fenx;
            end if;
          elsif dqmc is null and fgsmc is not null and dcmc is null then
            --按分公司统计公司层
            sql_stmt := 'select count(distinct(dc.id)) from ' || tablename ||
                        ' sl,yuetjkjb tj,yueslb slb,yuezlb zl,yuezbb zb,vwdianc dc where dc.fgsmc=:fgsmc and tj.diancxxb_id=dc.id and zb.diancxxb_id=dc.id and sl.diancxxb_id=dc.id and dc.fuid<>-1 and slb.yuetjkjb_id=tj.id and zl.yuetjkjb_id=slb.yuetjkjb_id and sl.riq>=:fDate and sl.riq<=:lDate and tj.riq>=:fDate and tj.riq<=:lDate and zb.riq>=:fDate and zb.riq<=:lDate and (slb.zhuangt<>2 or zl.zhuangt<>2 or zb.zhuangt<>2) and slb.fenx=:fenx and zl.fenx = slb.fenx and zb.fenx=:fenx and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
            if koujid > 0 then
              sql_stmt := sql_stmt || sql_fenkj;
              execute immediate sql_stmt
              into rtn
              using fgsmc, fDate, lDate, fDate, lDate, fDate, lDate, fenx, fenx, fenx, fDate, lDate, koujid;
            else
              execute immediate sql_stmt
              into rtn
              using fgsmc, fDate, lDate, fDate, lDate, fDate, lDate, fenx, fenx, fenx;
            end if;
          elsif dqmc is not null and fgsmc is null and dcmc is null then
            --按地区统计地区层
            sql_stmt := 'select count(distinct(dc.id)) from ' || tablename ||
                        ' sl,yuetjkjb tj,yueslb slb,yuezlb zl,yuezbb zb,diancxxb dc,shengfb sf,shengfdqb dq where dc.shengfb_id = sf.id and sf.shengfdqb_id = dq.id and dq.mingc=:dqmc and tj.diancxxb_id=dc.id and zb.diancxxb_id=dc.id and sl.diancxxb_id=dc.id and dc.fuid<>-1 and slb.yuetjkjb_id=tj.id and zl.yuetjkjb_id=slb.yuetjkjb_id and sl.riq>=:fDate and sl.riq<=:lDate and tj.riq>=:fDate and tj.riq<=:lDate and zb.riq>=:fDate and zb.riq<=:lDate and (slb.zhuangt<>2 or zl.zhuangt<>2 or zb.zhuangt<>2) and slb.fenx=:fenx and zl.fenx = slb.fenx and zb.fenx=:fenx and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
            if koujid > 0 then
              sql_stmt := sql_stmt || sql_fenkj;
              execute immediate sql_stmt
              into rtn
              using dqmc, fDate, lDate, fDate, lDate, fDate, lDate, fenx, fenx, fenx, fDate, lDate, koujid;
            else
              execute immediate sql_stmt
              into rtn
              using dqmc, fDate, lDate, fDate, lDate, fDate, lDate, fenx, fenx, fenx;
            end if;
          else
            --电厂层
            if dqmc is not null and fgsmc is null and dcmc is not null then
              --按地区统计电厂层
              sql_stmt := 'select count(distinct(dc.id)) from ' || tablename ||
                          ' sl,yuetjkjb tj,yueslb slb,yuezlb zl,yuezbb zb,diancxxb dc,shengfb sf,shengfdqb dq where dc.shengfb_id = sf.id and sf.shengfdqb_id = dq.id and dc.mingc=:dcmc and tj.diancxxb_id=dc.id and zb.diancxxb_id=dc.id and sl.diancxxb_id=dc.id and dc.fuid<>-1 and slb.yuetjkjb_id=tj.id and zl.yuetjkjb_id=slb.yuetjkjb_id and sl.riq>=:fDate and sl.riq<=:lDate and tj.riq>=:fDate and tj.riq<=:lDate and zb.riq>=:fDate and zb.riq<=:lDate and (slb.zhuangt<>2 or zl.zhuangt<>2 or zb.zhuangt<>2) and slb.fenx=:fenx and zl.fenx = slb.fenx and zb.fenx=:fenx and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
              execute immediate sql_stmt
              into rtn
              using dcmc, fDate, lDate, fDate, lDate, fDate, lDate, fenx, fenx, fenx;
            else
              if dqmc is null and fgsmc is not null and dcmc is not null then
                --按电厂统计电厂层
                sql_stmt := 'select count(distinct(dc.id)) from ' ||
                            tablename ||
                            ' sl,yuetjkjb tj,yueslb slb,yuezlb zl,yuezbb zb,vwdianc dc where dc.mingc=:mingc and tj.diancxxb_id=dc.id and zb.diancxxb_id=dc.id and sl.diancxxb_id=dc.id and dc.fuid<>-1 and slb.yuetjkjb_id=tj.id and zl.yuetjkjb_id=slb.yuetjkjb_id and sl.riq>=:fDate and sl.riq<=:lDate and tj.riq>=:fDate and tj.riq<=:lDate and zb.riq>=:fDate and zb.riq<=:lDate and (slb.zhuangt<>2 or zl.zhuangt<>2 or zb.zhuangt<>2) and slb.fenx=:fenx and zl.fenx = slb.fenx and zb.fenx=:fenx and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
                execute immediate sql_stmt
                into rtn
                using dcmc, fDate, lDate, fDate, lDate, fDate, lDate, fenx, fenx, fenx;
              end if;
            end if;
          end if;
        elsif Userjib = 2 then
          --公司级用户
          if dqmc is null and fgsmc is null and dcmc is null then
            --总计
            sql_stmt := 'select count(distinct(dc.id)) from ' || tablename ||
                        ' sl,yuetjkjb tj,yueslb slb,yuezlb zl,yuezbb zb,vwdianc dc where dc.fuid=:diancfuid and tj.diancxxb_id=dc.id and zb.diancxxb_id=dc.id and sl.diancxxb_id=dc.id and dc.fuid<>-1 and slb.yuetjkjb_id=tj.id and zl.yuetjkjb_id=slb.yuetjkjb_id and sl.riq>=:fDate and sl.riq<=:lDate and tj.riq>=:fDate and tj.riq<=:lDate and zb.riq>=:fDate and zb.riq<=:lDate and (slb.zhuangt=0 or zl.zhuangt=0 or zb.zhuangt=0) and slb.fenx=:fenx and zl.fenx = slb.fenx and zb.fenx=:fenx and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
            execute immediate sql_stmt
            into rtn
            using diancfuid, fDate, lDate, fDate, lDate, fDate, lDate, fenx, fenx, fenx;
          elsif dqmc is null and fgsmc is not null and dcmc is null then
            --按电厂统计公司层
            sql_stmt := 'select count(distinct(dc.id)) from ' || tablename ||
                        ' sl,yuetjkjb tj,yueslb slb,yuezlb zl,yuezbb zb,vwdianc dc where dc.fuid=:diancfuid and dc.fgsmc=:fgsmc and tj.diancxxb_id=dc.id and zb.diancxxb_id=dc.id and sl.diancxxb_id=dc.id and dc.fuid<>-1 and slb.yuetjkjb_id=tj.id and zl.yuetjkjb_id=slb.yuetjkjb_id and sl.riq>=:fDate and sl.riq<=:lDate and tj.riq>=:fDate and tj.riq<=:lDate and zb.riq>=:fDate and zb.riq<=:lDate and (slb.zhuangt=0 or zl.zhuangt=0 or zb.zhuangt=0) and slb.fenx=:fenx and zl.fenx = slb.fenx and zb.fenx=:fenx and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
            execute immediate sql_stmt
            into rtn
            using diancfuid, fgsmc, fDate, lDate, fDate, lDate, fDate, lDate, fenx, fenx, fenx;
          elsif dqmc is not null and fgsmc is null and dcmc is null then
            --按地区统计地区层
            sql_stmt := 'select count(distinct(dc.id)) from ' || tablename ||
                        ' sl,yuetjkjb tj,yueslb slb,yuezlb zl,yuezbb zb,diancxxb dc,shengfb sf,shengfdqb dq where dc.shengfb_id = sf.id and sf.shengfdqb_id = dq.id and dc.fuid=:diancfuid and dq.mingc=:dqmc and tj.diancxxb_id=dc.id and zb.diancxxb_id=dc.id and sl.diancxxb_id=dc.id and dc.fuid<>-1 and slb.yuetjkjb_id=tj.id and zl.yuetjkjb_id=slb.yuetjkjb_id and sl.riq>=:fDate and sl.riq<=:lDate and tj.riq>=:fDate and tj.riq<=:lDate and zb.riq>=:fDate and zb.riq<=:lDate and (slb.zhuangt=0 or zl.zhuangt=0 or zb.zhuangt=0) and slb.fenx=:fenx and zl.fenx = slb.fenx and zb.fenx=:fenx and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
            execute immediate sql_stmt
            into rtn
            using diancfuid, dqmc, fDate, lDate, fDate, lDate, fDate, lDate, fenx, fenx, fenx;
          else
            --电厂层
            if dqmc is not null and fgsmc is null and dcmc is not null then
              --按地区统计电厂层
              sql_stmt := 'select count(distinct(dc.id)) from ' || tablename ||
                          ' sl,yuetjkjb tj,yueslb slb,yuezlb zl,yuezbb zb,diancxxb dc,shengfb sf,shengfdqb dq where dc.shengfb_id = sf.id and sf.shengfdqb_id = dq.id and dc.mingc=:dcmc and tj.diancxxb_id=dc.id and zb.diancxxb_id=dc.id and sl.diancxxb_id=dc.id and dc.fuid<>-1 and slb.yuetjkjb_id=tj.id and zl.yuetjkjb_id=slb.yuetjkjb_id and sl.riq>=:fDate and sl.riq<=:lDate and tj.riq>=:fDate and tj.riq<=:lDate and zb.riq>=:fDate and zb.riq<=:lDate and (slb.zhuangt=0 or zl.zhuangt=0 or zb.zhuangt=0) and slb.fenx=:fenx and zl.fenx = slb.fenx and zb.fenx=:fenx and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';

              execute immediate sql_stmt
              into rtn
              using dcmc, fDate, lDate, fDate, lDate, fDate, lDate, fenx, fenx, fenx;
            else
              if dqmc is null and fgsmc is not null and dcmc is not null then
                --按电厂统计电厂层
                sql_stmt := 'select count(distinct(dc.id)) from ' ||
                            tablename ||
                            ' sl,yuetjkjb tj,yueslb slb,yuezlb zl,yuezbb zb,vwdianc dc where dc.mingc=:mingc and tj.diancxxb_id=dc.id and zb.diancxxb_id=dc.id and sl.diancxxb_id=dc.id and dc.fuid<>-1 and slb.yuetjkjb_id=tj.id and zl.yuetjkjb_id=slb.yuetjkjb_id and sl.riq>=:fDate and sl.riq<=:lDate and tj.riq>=:fDate and tj.riq<=:lDate and zb.riq>=:fDate and zb.riq<=:lDate and (slb.zhuangt=0 or zl.zhuangt=0 or zb.zhuangt=0) and slb.fenx=:fenx and zl.fenx = slb.fenx and zb.fenx=:fenx and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
                execute immediate sql_stmt
                into rtn
                using dcmc, fDate, lDate, fDate, lDate, fDate, lDate, fenx, fenx, fenx;
              end if;
            end if;
          end if;
        end if;
      elsif tablename = 'yuercbmdj' then
        --cpi10
        if UserJib = 1 then
          --集团用户
          if dqmc is null and fgsmc is null and dcmc is null then
            --总计
            sql_stmt := 'select count(distinct(dc.id)) from ' || tablename ||
                        ' sl,yuetjkjb tj,yueslb slb,yuezlb zl,rucycbb rcy,vwdianc dc where tj.diancxxb_id = dc.id and rcy.diancxxb_id=dc.id and sl.yuetjkjb_id = tj.id and slb.yuetjkjb_id = tj.id and zl.yuetjkjb_id = tj.id and dc.fuid <> -1 and tj.riq >=:fDate and tj.riq <= :lDate and rcy.riq>=:fDate and rcy.riq<=:lDate and (sl.zhuangt <> 2 or zl.zhuangt <> 2 or slb.zhuangt <> 2 or rcy.zhuangt<>2) and slb.fenx=sl.fenx and zl.fenx=sl.fenx and rcy.fenx=:fenx and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
            if koujid > 0 then
              sql_stmt := sql_stmt || sql_fenkj;
              execute immediate sql_stmt
              into rtn
              using fDate, lDate, fDate, lDate, fenx, fenx, fDate, lDate, koujid;
            else
              execute immediate sql_stmt
              into rtn
              using fDate, lDate, fDate, lDate, fenx, fenx;
            end if;
          elsif dqmc is null and fgsmc is not null and dcmc is null then
            --按分公司统计公司层
            sql_stmt := 'select count(distinct(dc.id)) from ' || tablename ||
                        ' sl,yuetjkjb tj,yueslb slb,yuezlb zl,rucycbb rcy,vwdianc dc where tj.diancxxb_id = dc.id and rcy.diancxxb_id=dc.id and sl.yuetjkjb_id = tj.id and slb.yuetjkjb_id = tj.id and zl.yuetjkjb_id = tj.id and dc.fuid <> -1 and dc.fgsmc = :fgsmc and tj.riq >=:fDate and tj.riq <= :lDate and rcy.riq>=:fDate and rcy.riq<=:lDate and (sl.zhuangt <> 2 or zl.zhuangt <> 2 or slb.zhuangt <> 2 or rcy.zhuangt<>2) and slb.fenx=sl.fenx and zl.fenx=sl.fenx and rcy.fenx=:fenx and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
            if koujid > 0 then
              sql_stmt := sql_stmt || sql_fenkj;
              execute immediate sql_stmt
              into rtn
              using fgsmc, fDate, lDate, fDate, lDate, fenx, fenx, fDate, lDate, koujid;
            else
              execute immediate sql_stmt
              into rtn
              using fgsmc, fDate, lDate, fDate, lDate, fenx, fenx;
            end if;
          elsif dqmc is not null and fgsmc is null and dcmc is null then
            --按地区统计地区层
            sql_stmt := 'select count(distinct(dc.id)) from ' || tablename ||
                        ' sl,yuetjkjb tj,yueslb slb,yuezlb zl,rucycbb rcy,diancxxb dc,shengfb sf,shengfdqb dq where dc.shengfb_id = sf.id and sf.shengfdqb_id = dq.id and tj.diancxxb_id = dc.id and rcy.diancxxb_id=dc.id and sl.yuetjkjb_id = tj.id and slb.yuetjkjb_id = tj.id and zl.yuetjkjb_id = tj.id and dc.fuid <> -1 and dq.mingc = :dqmc and tj.riq >=:fDate and tj.riq <= :lDate and rcy.riq>=:fDate and rcy.riq<=:lDate and (sl.zhuangt <> 2 or zl.zhuangt <> 2 or slb.zhuangt <> 2 or rcy.zhuangt<>2) and slb.fenx=sl.fenx and zl.fenx=sl.fenx and rcy.fenx=:fenx and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
            if koujid > 0 then
              sql_stmt := sql_stmt || sql_fenkj;
              execute immediate sql_stmt
              into rtn
              using dqmc, fDate, lDate, fDate, lDate, fenx, fenx, fDate, lDate, koujid;
            else
              execute immediate sql_stmt
              into rtn
              using dqmc, fDate, lDate, fDate, lDate, fenx, fenx;
            end if;
          else
            --电厂层
            if dqmc is null and fgsmc is null and dcmc is not null then
              --按地区统计电厂层
              sql_stmt := 'select count(distinct(dc.id)) from ' || tablename ||
                          ' sl,yuetjkjb tj,yueslb slb,yuezlb zl,rucycbb rcy,diancxxb dc,shengfb sf,shengfdqb dq where dc.shengfb_id = sf.id and sf.shengfdqb_id = dq.id and tj.diancxxb_id = dc.id and rcy.diancxxb_id=dc.id and sl.yuetjkjb_id = tj.id and slb.yuetjkjb_id = tj.id and zl.yuetjkjb_id = tj.id and dc.fuid <> -1 and dc.mingc = :dcmc and tj.riq >=:fDate and tj.riq <= :lDate and rcy.riq>=:fDate and rcy.riq<=:lDate and (sl.zhuangt <> 2 or zl.zhuangt <> 2 or slb.zhuangt <> 2 or rcy.zhuangt<>2) and slb.fenx=sl.fenx and zl.fenx=sl.fenx and rcy.fenx=:fenx and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
              execute immediate sql_stmt
              into rtn
              using dcmc, fDate, lDate, fDate, lDate, fenx, fenx;
            else
              if dqmc is null and fgsmc is not null and dcmc is not null then
                --按电厂统计电厂层
                sql_stmt := 'select count(distinct(dc.id)) from ' ||
                            tablename ||
                            ' sl,yuetjkjb tj,yueslb slb,yuezlb zl,rucycbb rcy,vwdianc dc where tj.diancxxb_id = dc.id and rcy.diancxxb_id=dc.id and sl.yuetjkjb_id = tj.id and slb.yuetjkjb_id = tj.id and zl.yuetjkjb_id = tj.id and dc.fuid <> -1 and dc.mingc = :mingc and tj.riq >=:fDate and tj.riq <= :lDate and rcy.riq>=:fDate and rcy.riq<=:lDate and (sl.zhuangt <> 2 or zl.zhuangt <> 2 or slb.zhuangt <> 2 or rcy.zhuangt<>2) and slb.fenx=sl.fenx and zl.fenx=sl.fenx and rcy.fenx=:fenx and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
                execute immediate sql_stmt
                into rtn
                using dcmc, fDate, lDate, fDate, lDate, fenx, fenx;
              end if;
            end if;
          end if;
        elsif Userjib = 2 then
          --公司级用户
          if dqmc is null and fgsmc is null and dcmc is null then
            --总计
            sql_stmt := 'select count(distinct(dc.id)) from ' || tablename ||
                        ' sl,yuetjkjb tj,yueslb slb,yuezlb zl,rucycbb rcy,vwdianc dc where tj.diancxxb_id = dc.id and rcy.diancxxb_id=dc.id and sl.yuetjkjb_id = tj.id and slb.yuetjkjb_id = tj.id and zl.yuetjkjb_id = tj.id and dc.fuid <> -1 and dc.fuid = :diancfuid and tj.riq >=:fDate and tj.riq <= :lDate and rcy.riq>=:fDate and rcy.riq<=:lDate and (sl.zhuangt =0 or zl.zhuangt =0 or slb.zhuangt =0 or rcy.zhuangt=0) and slb.fenx=sl.fenx and zl.fenx=sl.fenx and rcy.fenx=:fenx and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
            execute immediate sql_stmt
            into rtn
            using diancfuid, fDate, lDate, fDate, lDate, fenx, fenx;
          elsif dqmc is null and fgsmc is not null and dcmc is null then
            --按电厂统计公司层
            sql_stmt := 'select count(distinct(dc.id)) from ' || tablename ||
                        ' sl,yuetjkjb tj,yueslb slb,yuezlb zl,rucycbb rcy,vwdianc dc where tj.diancxxb_id = dc.id and rcy.diancxxb_id=dc.id and sl.yuetjkjb_id = tj.id and slb.yuetjkjb_id = tj.id and zl.yuetjkjb_id = tj.id and dc.fuid <> -1 and dc.fuid = :diancfuid and dc.fgsmc=:fgsmc and tj.riq >=:fDate and tj.riq <= :lDate and rcy.riq>=:fDate and rcy.riq<=:lDate and (sl.zhuangt =0 or zl.zhuangt =0 or slb.zhuangt =0 or rcy.zhuangt=0) and slb.fenx=sl.fenx and zl.fenx=sl.fenx and rcy.fenx=:fenx and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
            execute immediate sql_stmt
            into rtn
            using diancfuid, fgsmc, fDate, lDate, fDate, lDate, fenx, fenx;
          elsif dqmc is not null and fgsmc is null and dcmc is null then
            --按地区统计地区层
            sql_stmt := 'select count(distinct(dc.id)) from ' || tablename ||
                        ' sl,yuetjkjb tj,yueslb slb,yuezlb zl,rucycbb rcy,diancxxb dc,shengfb sf,shengfdqb dq where dc.shengfb_id = sf.id and sf.shengfdqb_id = dq.id and tj.diancxxb_id = dc.id and rcy.diancxxb_id=dc.id and sl.yuetjkjb_id = tj.id and slb.yuetjkjb_id = tj.id and zl.yuetjkjb_id = tj.id and dc.fuid <> -1 and dc.fuid=:diancfuid and dq.mingc = :dqmc and tj.riq >=:fDate and tj.riq <= :lDate and rcy.riq>=:fDate and rcy.riq<=:lDate and (sl.zhuangt =0 or zl.zhuangt =0 or slb.zhuangt =0 or rcy.zhuangt=0) and slb.fenx=sl.fenx and zl.fenx=sl.fenx and rcy.fenx=:fenx and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
            execute immediate sql_stmt
            into rtn
            using diancfuid, dqmc, fDate, lDate, fDate, lDate, fenx, fenx;
          else
            --电厂层
            if dqmc is null and fgsmc is null and dcmc is not null then
              --按地区统计电厂层
              sql_stmt := 'select count(distinct(dc.id)) from ' || tablename ||
                          ' sl,yuetjkjb tj,yueslb slb,yuezlb zl,rucycbb rcy,diancxxb dc,shengfb sf,shengfdqb dq where dc.shengfb_id = sf.id and sf.shengfdqb_id = dq.id and tj.diancxxb_id = dc.id and rcy.diancxxb_id=dc.id and sl.yuetjkjb_id = tj.id and slb.yuetjkjb_id = tj.id and zl.yuetjkjb_id = tj.id and dc.fuid <> -1 and dc.mingc = :dcmc and tj.riq >=:fDate and tj.riq <= :lDate and rcy.riq>=:fDate and rcy.riq<=:lDate and (sl.zhuangt =0 or zl.zhuangt =0 or slb.zhuangt =0 or rcy.zhuangt=0) and slb.fenx=sl.fenx and zl.fenx=sl.fenx and rcy.fenx=:fenx and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
              execute immediate sql_stmt
              into rtn
              using dcmc, fDate, lDate, fDate, lDate, fenx, fenx;
            else
              if dqmc is null and fgsmc is not null and dcmc is not null then
                --按电厂统计电厂层
                sql_stmt := 'select count(distinct(dc.id)) from ' ||
                            tablename ||
                            ' sl,yuetjkjb tj,yueslb slb,yuezlb zl,rucycbb rcy,vwdianc dc where tj.diancxxb_id = dc.id and rcy.diancxxb_id=dc.id and sl.yuetjkjb_id = tj.id and slb.yuetjkjb_id = tj.id and zl.yuetjkjb_id = tj.id and dc.fuid <> -1 and dc.mingc = :mingc and tj.riq >=:fDate and tj.riq <= :lDate and rcy.riq>=:fDate and rcy.riq<=:lDate and (sl.zhuangt =0 or zl.zhuangt =0 or slb.zhuangt =0 or rcy.zhuangt=0) and slb.fenx=sl.fenx and zl.fenx=sl.fenx and rcy.fenx=:fenx and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
                execute immediate sql_stmt
                into rtn
                using dcmc, fDate, lDate, fDate, lDate, fenx, fenx;
              end if;
            end if;
          end if;
        end if;
      elsif tablename = 'yuezbb' then
        --cpi08、cpi11、cpi12
        if UserJib = 1 then
          --集团用户
          if dqmc is null and fgsmc is null and dcmc is null then
            --总计
            sql_stmt := 'select count(distinct(dc.id)) from ' || tablename ||
                        ' sl,vwdianc dc where sl.diancxxb_id = dc.id and dc.fuid <> -1 and sl.riq >=:fDate and sl.riq <= :lDate and sl.zhuangt <> 2 and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
            if koujid > 0 then
              sql_stmt := sql_stmt || sql_fenkj;
              execute immediate sql_stmt
              into rtn
              using fDate, lDate, fenx, fDate, lDate, koujid;
            else
              execute immediate sql_stmt
              into rtn
              using fDate, lDate, fenx;
            end if;
          elsif dqmc is null and fgsmc is not null and dcmc is null then
            --按分公司统计公司层
            sql_stmt := 'select count(distinct(dc.id)) from ' || tablename ||
                        ' sl,vwdianc dc where sl.diancxxb_id = dc.id and dc.fuid <> -1 and dc.fgsmc = :fgsmc and sl.riq >=:fDate and sl.riq <= :lDate  and sl.zhuangt <> 2 and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
            if koujid > 0 then
              sql_stmt := sql_stmt || sql_fenkj;
              execute immediate sql_stmt
              into rtn
              using fgsmc, fDate, lDate, fenx, fDate, lDate, koujid;
            else
              execute immediate sql_stmt
              into rtn
              using fgsmc, fDate, lDate, fenx;
            end if;
          elsif dqmc is not null and fgsmc is null and dcmc is null then
            --按地区统计地区层
            sql_stmt := 'select count(distinct(dc.id)) from ' || tablename ||
                        ' sl,diancxxb dc,shengfb sf,shengfdqb dq where dc.shengfb_id = sf.id and sf.shengfdqb_id = dq.id and sl.diancxxb_id = dc.id and dc.fuid <> -1 and dq.mingc = :dqmc and sl.riq >=:fDate and sl.riq <= :lDate  and sl.zhuangt <> 2 and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
            if koujid > 0 then
              sql_stmt := sql_stmt || sql_fenkj;
              execute immediate sql_stmt
              into rtn
              using dqmc, fDate, lDate, fenx, fDate, lDate, koujid;
            else
              execute immediate sql_stmt
              into rtn
              using dqmc, fDate, lDate, fenx;
            end if;
          else
            if dqmc is not null and fgsmc is null and dcmc is not null then
              --按地区统计电厂层
              sql_stmt := 'select count(distinct(dc.id)) from ' || tablename ||
                          ' sl,diancxxb dc,shengfb sf,shengfdqb dq where dc.shengfb_id = sf.id and sf.shengfdqb_id = dq.id and sl.diancxxb_id = dc.id and dc.fuid <> -1 and dc.mingc = :dcmc and sl.riq >=:fDate and sl.riq <= :lDate  and sl.zhuangt <> 2 and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
              execute immediate sql_stmt
              into rtn
              using dcmc, fDate, lDate, fenx;
            else
              if dqmc is null and fgsmc is not null and dcmc is not null then
                --按电厂统计电厂层
                sql_stmt := 'select count(distinct(dc.id)) from ' ||
                            tablename ||
                            ' sl,vwdianc dc where sl.diancxxb_id = dc.id and dc.fuid <> -1 and dc.mingc = :dcmc and sl.riq >=:fDate and sl.riq <= :lDate  and sl.zhuangt <> 2 and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
                execute immediate sql_stmt
                into rtn
                using dcmc, fDate, lDate, fenx;
              end if;
            end if;
          end if;
        elsif Userjib = 2 then
          --公司级用户
          if dqmc is null and fgsmc is null and dcmc is null then
            --总计
            sql_stmt := 'select count(distinct(dc.id)) from ' || tablename ||
                        ' sl,vwdianc dc where sl.diancxxb_id = dc.id and dc.fuid <> -1 and dc.fuid=:diancfuid and sl.riq >=:fDate and sl.riq <= :lDate  and sl.zhuangt =0 and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
            execute immediate sql_stmt
            into rtn
            using diancfuid, fDate, lDate, fenx;
          elsif dqmc is null and fgsmc is not null and dcmc is null then
            --按电厂统计公司层
            sql_stmt := 'select count(distinct(dc.id)) from ' || tablename ||
                        ' sl,vwdianc dc where sl.diancxxb_id = dc.id and dc.fuid <> -1 and dc.fuid=:diancfuid and dc.fgsmc = :fgsmc and sl.riq >=:fDate and sl.riq <= :lDate  and sl.zhuangt =0 and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
            execute immediate sql_stmt
            into rtn
            using diancfuid, fgsmc, fDate, lDate, fenx;
          elsif dqmc is not null and fgsmc is null and dcmc is null then
            --按地区统计地区层
            sql_stmt := 'select count(distinct(dc.id)) from ' || tablename ||
                        ' sl,diancxxb dc,shengfb sf,shengfdqb dq where dc.shengfb_id = sf.id and sf.shengfdqb_id = dq.id and sl.diancxxb_id = dc.id and dc.fuid <> -1 and dq.mingc = :dqmc and sl.riq >=:fDate and sl.riq <= :lDate  and sl.zhuangt =0 and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
            execute immediate sql_stmt
            into rtn
            using dqmc, fDate, lDate, fenx;
          else
            if dqmc is not null and fgsmc is null and dcmc is not null then
              --按地区统计电厂层
              sql_stmt := 'select count(distinct(dc.id)) from ' || tablename ||
                          ' sl,diancxxb dc,shengfb sf,shengfdqb dq where dc.shengfb_id = sf.id and sf.shengfdqb_id = dq.id and sl.diancxxb_id = dc.id and dc.fuid <> -1 and dc.mingc = :dcmc and sl.riq >=:fDate and sl.riq <= :lDate  and sl.zhuangt =0 and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
              execute immediate sql_stmt
              into rtn
              using dcmc, fDate, lDate, fenx;
            else
              if dqmc is null and fgsmc is not null and dcmc is not null then
                --按电厂统计电厂层
                sql_stmt := 'select count(distinct(dc.id)) from ' ||
                            tablename ||
                            ' sl,vwdianc dc where sl.diancxxb_id = dc.id and dc.fuid <> -1 and dc.mingc = :dcmc and sl.riq >=:fDate and sl.riq <= :lDate  and sl.zhuangt =0 and sl.fenx=:fenx and dc.id not in(122,133,134,135,182,912)';
                execute immediate sql_stmt
                into rtn
                using dcmc, fDate, lDate, fenx;
              end if;
            end if;
          end if;
        end if;
      end if;
    end;
    return(rtn);
  end getShenhzt;
drop table xiasmxqtb;
-- Create table---------------------------------------------------------------------------------------------------------
create table XIASMXQTB
(
  id          NUMBER(15) not null,
  riq         DATE not null,
  diancxxb_id NUMBER(15) not null,
  gongysb_id  NUMBER(15) not null,
  pinz_id     NUMBER(15),
  xuql        NUMBER(15,2),
  hedl        NUMBER(15,2),
  hetl        NUMBER(15,2)
)
;
-- Add comments to the columns
comment on column XIASMXQTB.riq
is '日期';
comment on column XIASMXQTB.diancxxb_id
is '电厂ID';
comment on column XIASMXQTB.gongysb_id
is '供应商ID';
comment on column XIASMXQTB.pinz_id
is '品种';
comment on column XIASMXQTB.xuql
is '需求量(万吨)';
comment on column XIASMXQTB.hedl
is '核定量(万吨)';
comment on column XIASMXQTB.hetl
is '合同量(万吨)';
-- Create table------------------------------------------------------------------------------------------------------------
create table XIASMFYQKB
(
  id          NUMBER(15) not null,
  riq         DATE not null,
  diancxxb_id NUMBER(15) not null,
  gongysb_id  NUMBER(15) not null,
  chezxxb_id  NUMBER(15) not null,
  jihn        NUMBER(15,4),
  jihw        NUMBER(15,4),
  hetl        NUMBER(15,4)
)
;
-- Add comments to the columns
comment on column XIASMFYQKB.riq
is '日期';
comment on column XIASMFYQKB.diancxxb_id
is '电厂ID';
comment on column XIASMFYQKB.gongysb_id
is '供应商ID';
comment on column XIASMFYQKB.chezxxb_id
is '港口ID';
comment on column XIASMFYQKB.jihn
is '计划内(万吨)';
comment on column XIASMFYQKB.jihw
is '计划外(万吨)';
comment on column XIASMFYQKB.hetl
is '合同量(万吨)';

-- Create table-------------------------------------------------------------------------------------------------------------
create table YUEBBDYSJB
(
  id         NUMBER(15) not null,
  baobmc     VARCHAR2(50) not null,
  shujbmc    VARCHAR2(20) not null,
  guanlbmc   VARCHAR2(50),
  riq        DATE,
  zhuangt    NUMBER(5),
  shujbmcbz  VARCHAR2(20),
  guanlbmcbz VARCHAR2(50),
  xuh        NUMBER(5)
)
;
-- Add comments to the table
comment on table YUEBBDYSJB
is '月报表名称对应数据表';
-- Add comments to the columns
comment on column YUEBBDYSJB.baobmc
is 'cpi报表名称';
comment on column YUEBBDYSJB.shujbmc
is '月报填报名称';
comment on column YUEBBDYSJB.guanlbmc
is 'cpi报表数据关联到的月报填报名称';
comment on column YUEBBDYSJB.riq
is '日期';
comment on column YUEBBDYSJB.zhuangt
is '状态';
comment on column YUEBBDYSJB.shujbmcbz
is '月报填报名称备注';
comment on column YUEBBDYSJB.guanlbmcbz
is 'cpi报表数据关联到的月报填报名称备注';
comment on column YUEBBDYSJB.xuh
is '序号';
-- Create/Recreate primary, unique and foreign key constraints
alter table YUEBBDYSJB
  add constraint MK_YUEBBDYSJB primary key (ID)
  using index
;
--------------------------------------------------------------------------------------------------------------------------------
insert into yuebbdysjb (ID, BAOBMC, SHUJBMC, GUANLBMC, RIQ, ZHUANGT, SHUJBMCBZ, GUANLBMCBZ, XUH)
values (4, 'cpi燃料管理09表', 'yuezlb', 'yueslb,yuezbb_zdt,rucycbb', null, 1, '质量填报', 'cpi燃料管理05表、cpi燃料管理08表、入厂油成本', 7);

insert into yuebbdysjb (ID, BAOBMC, SHUJBMC, GUANLBMC, RIQ, ZHUANGT, SHUJBMCBZ, GUANLBMCBZ, XUH)
values (3, 'cpi燃料管理05表', 'yueslb', 'yuecgjhb', null, 1, '数量填报', '月采购计划录入', 3);

insert into yuebbdysjb (ID, BAOBMC, SHUJBMC, GUANLBMC, RIQ, ZHUANGT, SHUJBMCBZ, GUANLBMCBZ, XUH)
values (2, '入厂油成本', 'rucycbb', null, null, 1, '入厂油成本', null, 2);

insert into yuebbdysjb (ID, BAOBMC, SHUJBMC, GUANLBMC, RIQ, ZHUANGT, SHUJBMCBZ, GUANLBMCBZ, XUH)
values (6, 'cpi燃料管理07表', 'yueshcyb', null, null, 1, '月油耗存', null, 6);

insert into yuebbdysjb (ID, BAOBMC, SHUJBMC, GUANLBMC, RIQ, ZHUANGT, SHUJBMCBZ, GUANLBMCBZ, XUH)
values (5, 'cpi燃料管理10表', 'yuercbmdj', 'rucycbb', null, 1, '入厂煤成本', '入厂油成本', 8);

insert into yuebbdysjb (ID, BAOBMC, SHUJBMC, GUANLBMC, RIQ, ZHUANGT, SHUJBMCBZ, GUANLBMCBZ, XUH)
values (7, 'cpi燃料管理08表、cpi燃料管理11表、cpi燃料管理12表', 'yuezbb', null, null, 1, '财务生产数据', null, 4);

insert into yuebbdysjb (ID, BAOBMC, SHUJBMC, GUANLBMC, RIQ, ZHUANGT, SHUJBMCBZ, GUANLBMCBZ, XUH)
values (8, 'cpi燃料管理06表', 'yueshchjb', 'yuezbb', null, 1, '月煤耗存', 'cpi燃料管理08表', 5);

insert into yuebbdysjb (ID, BAOBMC, SHUJBMC, GUANLBMC, RIQ, ZHUANGT, SHUJBMCBZ, GUANLBMCBZ, XUH)
values (11, 'cpi燃料管理01表		', 'cpi燃料管理01表		', 'cpi燃料管理01表		', null, 2, null, null, 11);

insert into yuebbdysjb (ID, BAOBMC, SHUJBMC, GUANLBMC, RIQ, ZHUANGT, SHUJBMCBZ, GUANLBMCBZ, XUH)
values (10, '调燃02表', '调燃02表', '调燃02表', null, 2, null, null, 10);

insert into yuebbdysjb (ID, BAOBMC, SHUJBMC, GUANLBMC, RIQ, ZHUANGT, SHUJBMCBZ, GUANLBMCBZ, XUH)
values (9, '调燃01表', '调燃01表', '调燃01表', null, 2, null, null, 9);

insert into yuebbdysjb (ID, BAOBMC, SHUJBMC, GUANLBMC, RIQ, ZHUANGT, SHUJBMCBZ, GUANLBMCBZ, XUH)
values (1, '月计划录入', 'yuejhbb', null, null, 1, '月计划录入', null, 1);

insert into yuebbdysjb (ID, BAOBMC, SHUJBMC, GUANLBMC, RIQ, ZHUANGT, SHUJBMCBZ, GUANLBMCBZ, XUH)
values (12, '年度合同执行情况表', 'niandhtzxqkb', null, null, 1, '年度合同执行情况表', null, 12);
CREATE OR REPLACE Function get_Zhuangt(strdiancId In number,strriq In date,stryear In varchar2,strbiaob In varchar2)
  Return  varchar2 as
  begin
    declare
      strzhuangt number;
      dc_jb number;
    begin
      select max(dc.jib) into dc_jb from diancxxb dc where dc.id=strdiancId;
      if strbiaob='yueslb' then
        if dc_jb=3 then
          select min(str.zhuangt) into strzhuangt  from yueslb str,yuetjkjb tj,diancxxb dc
          where str.yuetjkjb_id=tj.id and tj.diancxxb_id=dc.id and dc.id=strdiancId and tj.riq=strriq;
        elsif dc_jb=2 then
          select min(str.zhuangt) into strzhuangt  from yueslb str,yuetjkjb tj,diancxxb dc
          where str.yuetjkjb_id=tj.id and tj.diancxxb_id=dc.id and ( dc.fuid=strdiancId or dc.shangjgsid=strdiancId) and tj.riq=strriq;
        elsif dc_jb=1 then
          select min(str.zhuangt) into strzhuangt  from yueslb str,yuetjkjb tj,diancxxb dc
          where str.yuetjkjb_id=tj.id and tj.diancxxb_id=dc.id  and tj.riq=strriq;
        end if;
      elsif strbiaob='yuezlb' then
        if dc_jb=3 then
          select min(str.zhuangt) into strzhuangt  from yuezlb str,yuetjkjb tj,diancxxb dc
          where str.yuetjkjb_id=tj.id and tj.diancxxb_id=dc.id and dc.id=strdiancId  and tj.riq=strriq;
        elsif dc_jb=2 then
          select min(str.zhuangt) into strzhuangt  from yuezlb str,yuetjkjb tj,diancxxb dc
          where str.yuetjkjb_id=tj.id and tj.diancxxb_id=dc.id and ( dc.fuid=strdiancId or dc.shangjgsid=strdiancId) and tj.riq=strriq;
        elsif dc_jb=1 then
          select min(str.zhuangt) into strzhuangt  from yuezlb str,yuetjkjb tj,diancxxb dc
          where str.yuetjkjb_id=tj.id and tj.diancxxb_id=dc.id and tj.riq=strriq;
        end if;
      elsif strbiaob='yuercbmdj' then
        if dc_jb=3 then
          select min(str.zhuangt) into strzhuangt from yuercbmdj str,yuetjkjb tj,diancxxb dc
          where str.yuetjkjb_id=tj.id and tj.diancxxb_id= dc.id and dc.id=strdiancId and tj.riq=strriq;
        elsif dc_jb=2 then
          select min(str.zhuangt) into strzhuangt from yuercbmdj str,yuetjkjb tj,diancxxb dc
          where str.yuetjkjb_id=tj.id and tj.diancxxb_id= dc.id and ( dc.fuid=strdiancId or dc.shangjgsid=strdiancId) and tj.riq=strriq;
        elsif dc_jb=1 then
          select min(str.zhuangt) into strzhuangt from yuercbmdj str,yuetjkjb tj,diancxxb dc
          where str.yuetjkjb_id=tj.id and tj.diancxxb_id= dc.id and tj.riq=strriq;
        end if;
      elsif  strbiaob='yuejhbb' then
        if dc_jb=3 then
          select min(str.zhuangt) into strzhuangt from yuecgjhb str,diancxxb dc
          where str.diancxxb_id=dc.id and dc.id=strdiancId  and str.riq=strriq;
        elsif dc_jb=2 then
          select min(str.zhuangt) into strzhuangt from yuecgjhb str,diancxxb dc
          where str.diancxxb_id=dc.id and ( dc.fuid=strdiancId or dc.shangjgsid=strdiancId)  and str.riq=strriq;
        elsif dc_jb=1 then
          select min(str.zhuangt) into strzhuangt from yuecgjhb str,diancxxb dc
          where str.diancxxb_id=dc.id  and str.riq=strriq;
        end if;
      elsif  strbiaob='yuezbb' then
        if dc_jb=3 then
          select min(str.zhuangt) into strzhuangt from yuezbb str,diancxxb dc
          where str.diancxxb_id=dc.id and dc.id=strdiancId and str.riq=strriq;
        elsif dc_jb=2 then
          select min(str.zhuangt) into strzhuangt from yuezbb str,diancxxb dc
          where str.diancxxb_id=dc.id and ( dc.fuid=strdiancId or dc.shangjgsid=strdiancId) and str.riq=strriq;
        elsif dc_jb=1 then
          select min(str.zhuangt) into strzhuangt from yuezbb str,diancxxb dc
          where str.diancxxb_id=dc.id  and str.riq=strriq;
        end if;
      elsif  strbiaob='rucycbb' then
        if dc_jb=3 then
          select min(str.zhuangt) into strzhuangt from rucycbb str,diancxxb dc
          where str.diancxxb_id=dc.id and dc.id=strdiancId and str.riq=strriq;
        elsif dc_jb=2 then
          select min(str.zhuangt) into strzhuangt from rucycbb str,diancxxb dc
          where str.diancxxb_id=dc.id and ( dc.fuid=strdiancId or dc.shangjgsid=strdiancId) and str.riq=strriq;
        elsif dc_jb=1 then
          select min(str.zhuangt) into strzhuangt from rucycbb str,diancxxb dc
          where str.diancxxb_id=dc.id  and str.riq=strriq;
        end if;
      elsif strbiaob='yuexqjhh' then
        if dc_jb=3 then
          select min(str.zhuangt) into strzhuangt  from yuexqjhh str,diancxxb dc
          where str.diancxxb_id=dc.id and dc.id=strdiancId  and str.riq=strriq;
        elsif dc_jb=2 then
          select min(str.zhuangt) into strzhuangt  from yuexqjhh str,diancxxb dc
          where str.diancxxb_id=dc.id and ( dc.fuid=strdiancId or dc.shangjgsid=strdiancId)  and str.riq=strriq;
        elsif dc_jb=1 then
          select min(str.zhuangt) into strzhuangt  from yuexqjhh str,diancxxb dc
          where str.diancxxb_id=dc.id and dc.id=strdiancId  and str.riq=strriq;
          select min(str.zhuangt) into strzhuangt  from yuezlb str,yuetjkjb tj,diancxxb dc
          where str.yuetjkjb_id=tj.id and tj.diancxxb_id=dc.id and tj.riq=strriq;
        end if;

      elsif  strbiaob='yueshcyb' then
        if dc_jb=3 then
          select min(str.zhuangt) into strzhuangt from yueshcyb str,diancxxb dc
          where str.diancxxb_id=dc.id and dc.id=strdiancId and str.riq=strriq;
        elsif dc_jb=2 then
          select min(str.zhuangt) into strzhuangt from yueshcyb str,diancxxb dc
          where str.diancxxb_id=dc.id and ( dc.fuid=strdiancId or dc.shangjgsid=strdiancId) and str.riq=strriq;
        elsif dc_jb=1 then
          select min(str.zhuangt) into strzhuangt from yueshcyb str,diancxxb dc
          where str.diancxxb_id=dc.id and str.riq=strriq;
        end if;
      elsif  strbiaob='yueshchjb' then
        if dc_jb=3 then
          select min(str.zhuangt) into strzhuangt from yueshchjb str,diancxxb dc
          where str.diancxxb_id=dc.id and dc.id=strdiancId and str.riq=strriq;
        elsif dc_jb=2 then
          select min(str.zhuangt) into strzhuangt from yueshchjb str,diancxxb dc
          where str.diancxxb_id=dc.id and ( dc.fuid=strdiancId or dc.shangjgsid=strdiancId) and str.riq=strriq;
        elsif dc_jb=1 then
          select min(str.zhuangt) into strzhuangt from yueshchjb str,diancxxb dc
          where str.diancxxb_id=dc.id and str.riq=strriq;
        end if;
      elsif  strbiaob='niandhtzxqkb' then
        if dc_jb=3 then
          select min(str.zhuangt) into strzhuangt from niandhtzxqkb str,diancxxb dc
          where str.diancxxb_id=dc.id and dc.id=strdiancId and str.riq=strriq;
        elsif dc_jb=2 then
          select min(str.zhuangt) into strzhuangt from niandhtzxqkb str,diancxxb dc
          where str.diancxxb_id=dc.id and ( dc.fuid=strdiancId or dc.shangjgsid=strdiancId) and str.riq=strriq;
        elsif dc_jb=1 then
          select min(str.zhuangt) into strzhuangt from niandhtzxqkb str,diancxxb dc
          where str.diancxxb_id=dc.id and str.riq=strriq;
        end if;
      end if;
      dbms_output.put_line(strzhuangt);
      if dc_jb=3 then
        if strzhuangt is null then
          return '未填写';
        elsif strzhuangt=0  then
          return '未上报分公司';
        elsif strzhuangt=1 then
          return '已上报分公司';
        elsif strzhuangt=2 then
          return '已上报集团';
        end if;
      elsif dc_jb=2 then
        if strzhuangt is null then
          return '未上报分公司';
        elsif strzhuangt=0 then
          return '未上报分公司';
        elsif strzhuangt=1 then
          return '未审核';
        elsif strzhuangt=2 then
          return '已审核';
        end if;
      elsif dc_jb=1 then
        if strzhuangt=2 then
          return '已审核';
        elsif strzhuangt is null then
          return '未审核';
        else
          return '未审核';
        end if;
      end if;
    end;
  End;


-- Create table-----------------------------------------------------------------------------------------------------------
create table YUEBSHSJB
(
  id            NUMBER(15) not null,
  diancxxb_id   NUMBER(15) not null,
  shijr         VARCHAR2(100) not null,
  riq           DATE not null,
  xiugrq        DATE not null,
  yuebbdysjb_id NUMBER(15) not null,
  shij          VARCHAR2(20),
  beiz          VARCHAR2(50)
)
;
-- Add comments to the table
comment on table YUEBSHSJB
is '月报审核事件表';
-- Add comments to the columns
comment on column YUEBSHSJB.diancxxb_id
is '电厂id';
comment on column YUEBSHSJB.shijr
is '事件人';
comment on column YUEBSHSJB.riq
is '日期（系统时间）';
comment on column YUEBSHSJB.xiugrq
is '修改数据日期';
comment on column YUEBSHSJB.yuebbdysjb_id
is '月报表名称对应数据表id';
comment on column YUEBSHSJB.shij
is '事件';
comment on column YUEBSHSJB.beiz
is '备注';

-- Create sequence------------------------------------------------------------------------------------------------------
create sequence XL_RIJHDYB_ID
minvalue 1
maxvalue 9999999999999999999999999999
start with 181
increment by 1
cache 20;





-- Create table---------------------------------------------------------------------------------------------------------
create table RIDYJHB
(
  id         NUMBER(15) not null,
  meikxxb_id NUMBER(15) not null,
  yunsdwb_id NUMBER(15) not null,
  ches       NUMBER,
  duns       NUMBER(15,3),
  beiz       VARCHAR2(500),
  createtime DATE,
  updatetime DATE,
  yewrq      DATE
);

-- Add comments to the table
comment on table RIDYJHB
is '日调运计划表';
-- Add comments to the columns
comment on column RIDYJHB.meikxxb_id
is '煤矿单位id';
comment on column RIDYJHB.yunsdwb_id
is '运输单位id';
comment on column RIDYJHB.ches
is '车数';
comment on column RIDYJHB.duns
is '吨数';
comment on column RIDYJHB.beiz
is '备注';
comment on column RIDYJHB.createtime
is '创建时间';
comment on column RIDYJHB.updatetime
is '更新时间';
comment on column RIDYJHB.yewrq
is '业务日期';
-------------------------------------------------------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION GETHETDXL(LNGDIANCXXB_ID IN NUMBER,
                                     DATRIQ         DATE,
                                     LEIX           STRING) RETURN NUMBER IS
  --RESULT VARCHAR2(1000); --重点合同兑现率
  BEGIN
    DECLARE

      MIAOS    NUMBER;
      YUEJHCGL NUMBER;

      YUEF  NUMBER;
      NIANF NUMBER;

      LTH VARCHAR2(1000);

      JIB NUMBER;

    BEGIN

      MIAOS := 0;
      SELECT JIB INTO JIB FROM DIANCXXB WHERE ID = LNGDIANCXXB_ID;

      SELECT TO_CHAR(DATRIQ, 'mm') INTO YUEF FROM DUAL;
      SELECT TO_CHAR(DATRIQ, 'yyyy') INTO NIANF FROM DUAL;

      SELECT TO_CHAR(DATRIQ, 'yyyy-mm') INTO LTH FROM DUAL;
      --------------上海电厂--------------------------

      IF (LNGDIANCXXB_ID = 174 OR LNGDIANCXXB_ID = 138 OR
          LNGDIANCXXB_ID = 173 OR LNGDIANCXXB_ID = 136 OR
          LNGDIANCXXB_ID = 137 OR LNGDIANCXXB_ID = 139 OR
          LNGDIANCXXB_ID = 135 OR LNGDIANCXXB_ID = 191 OR
          LNGDIANCXXB_ID = 134 OR LNGDIANCXXB_ID = 181 OR
          LNGDIANCXXB_ID = 180 OR LNGDIANCXXB_ID = 133 OR
          LNGDIANCXXB_ID = 151 OR LNGDIANCXXB_ID = 157) THEN
        IF (LEIX = '本月') THEN
          SELECT MAX(DUIXL) ---------------本月
          INTO MIAOS
          FROM DUIXLB
          WHERE RIQ = DATRIQ
                AND DIANCXXB_ID = LNGDIANCXXB_ID
                AND FENX = '本月';
        ELSE
          ----------累计
          SELECT MAX(DUIXL) ---------------本月
          INTO MIAOS
          FROM DUIXLB
          WHERE RIQ = DATRIQ
                AND DIANCXXB_ID = LNGDIANCXXB_ID
                AND FENX = '累计';

        END IF;

        -------------一般电厂---------------------
      ELSE

        IF (LEIX = '本月') THEN
          IF (JIB = 3) THEN
            ---------------本月
            SELECT NVL(SUM(HTQK.YUEJHCGL), 0)
            INTO YUEJHCGL
            FROM YUECGJHB HTQK
            WHERE HTQK.RIQ = DATRIQ
                  AND (HTQK.JIHKJB_ID = 1 OR HTQK.JIHKJB_ID = 3)
                  AND HTQK.DIANCXXB_ID = LNGDIANCXXB_ID;

            SELECT NVL(SUM(LAIMSL), 0)
            INTO MIAOS
            FROM YUESLB SL, YUETJKJB KJ
            WHERE KJ.ID = SL.YUETJKJB_ID
                  AND KJ.RIQ = DATRIQ
                  AND KJ.DIANCXXB_ID = LNGDIANCXXB_ID
                  AND (KJ.JIHKJB_ID = 1 OR KJ.JIHKJB_ID = 3)
                  AND SL.FENX = '本月';
            IF (YUEJHCGL != 0) THEN
              MIAOS := ROUND_NEW(MIAOS / YUEJHCGL * 100, 2);
            ELSE
              MIAOS := 0;
            END IF;
          ELSE
            IF (JIB = 2) THEN
              ---------------本月
              SELECT NVL(SUM(HTQK.YUEJHCGL), 0)
              INTO YUEJHCGL
              FROM YUECGJHB HTQK, DIANCXXB DC
              WHERE HTQK.RIQ = DATRIQ
                    AND DC.ID = HTQK.DIANCXXB_ID
                    AND (HTQK.JIHKJB_ID = 1 OR HTQK.JIHKJB_ID = 3)
                    AND DC.FUID = LNGDIANCXXB_ID
                    AND dc.id IN (SELECT diancxxb_id FROM dianckjmx WHERE dianckjb_id=1006951090);

              SELECT NVL(SUM(LAIMSL), 0)
              INTO MIAOS
              FROM YUESLB SL, YUETJKJB KJ, DIANCXXB DC
              WHERE KJ.ID = SL.YUETJKJB_ID
                    AND KJ.RIQ = DATRIQ
                    AND KJ.DIANCXXB_ID = DC.ID
                    AND DC.FUID = LNGDIANCXXB_ID
                    AND (KJ.JIHKJB_ID = 1 OR KJ.JIHKJB_ID = 3)
                    AND SL.FENX = '本月'
                    AND dc.id IN (SELECT diancxxb_id FROM dianckjmx WHERE dianckjb_id=1006951090);
              IF (YUEJHCGL != 0) THEN
                MIAOS := ROUND_NEW(MIAOS / YUEJHCGL * 100, 2);
              ELSE
                MIAOS := 0;
              END IF;
            ELSE
              IF (JIB = 1) THEN
                ---------------本月
                SELECT NVL(SUM(HTQK.YUEJHCGL), 0)
                INTO YUEJHCGL
                FROM YUECGJHB HTQK
                WHERE HTQK.RIQ = DATRIQ
                      AND HTQK.DIANCXXB_ID  IN (SELECT diancxxb_id FROM dianckjmx WHERE dianckjb_id=1006951090)
                      AND (HTQK.JIHKJB_ID = 1 OR HTQK.JIHKJB_ID = 3);
                -- AND HTQK.DIANCXXB_ID = LNGDIANCXXB_ID;

                SELECT NVL(SUM(LAIMSL), 0)
                INTO MIAOS
                FROM YUESLB SL, YUETJKJB KJ
                WHERE KJ.ID = SL.YUETJKJB_ID
                      AND KJ.RIQ = DATRIQ
                      AND KJ.DIANCXXB_ID  IN (SELECT diancxxb_id FROM dianckjmx WHERE dianckjb_id=1006951090)
                      --  AND KJ.DIANCXXB_ID = LNGDIANCXXB_ID
                      AND (KJ.JIHKJB_ID = 1 OR KJ.JIHKJB_ID = 3)
                      AND SL.FENX = '本月';
                IF (YUEJHCGL != 0) THEN
                  MIAOS := ROUND_NEW(MIAOS / YUEJHCGL * 100, 2);
                ELSE
                  MIAOS := 0;
                END IF;
              END IF;
            END IF;
          END IF;

        ELSE
          ----------累计

          IF (JIB =3) THEN
            SELECT NVL(SUM(HTQK.YUEJHCGL), 0)
            INTO YUEJHCGL
            FROM YUECGJHB HTQK
            WHERE HTQK.RIQ > = GETYEARFIRSTDATE(DATRIQ)
                  AND HTQK.RIQ < = DATRIQ
                  AND (HTQK.JIHKJB_ID = 1 OR HTQK.JIHKJB_ID = 3)

                  AND HTQK.DIANCXXB_ID = LNGDIANCXXB_ID;

            SELECT NVL(SUM(LAIMSL), 0)
            INTO MIAOS
            FROM YUESLB SL, YUETJKJB KJ
            WHERE KJ.ID = SL.YUETJKJB_ID
                  AND KJ.RIQ >= GETYEARFIRSTDATE(DATRIQ)
                  AND KJ.RIQ <= DATRIQ
                  AND KJ.DIANCXXB_ID = LNGDIANCXXB_ID
                  AND (KJ.JIHKJB_ID = 1 OR KJ.JIHKJB_ID = 3)
                  AND SL.FENX = '本月';

            IF (YUEJHCGL != 0) THEN
              MIAOS := ROUND_NEW(MIAOS / YUEJHCGL * 100, 2);
            ELSE
              MIAOS := 0;
            END IF;
          ELSE IF (JIB =2) THEN
            ----------累计----------
            SELECT NVL(SUM(HTQK.YUEJHCGL), 0)
            INTO YUEJHCGL
            FROM YUECGJHB HTQK,diancxxb dc
            WHERE HTQK.RIQ > = GETYEARFIRSTDATE(DATRIQ)
                  AND HTQK.RIQ < = DATRIQ
                  AND (HTQK.JIHKJB_ID = 1 OR HTQK.JIHKJB_ID = 3)
                  AND DC.ID = HTQK.DIANCXXB_ID
                  --  AND htqk.zhuangt>=1
                  AND dc.fuid = LNGDIANCXXB_ID
                  AND dc.id IN (SELECT diancxxb_id FROM dianckjmx WHERE dianckjb_id=1006951090);

            SELECT NVL(SUM(LAIMSL), 0)
            INTO MIAOS
            FROM YUESLB SL, YUETJKJB KJ,diancxxb dc
            WHERE KJ.ID = SL.YUETJKJB_ID
                  AND KJ.RIQ >= GETYEARFIRSTDATE(DATRIQ)
                  AND KJ.RIQ <= DATRIQ
                  AND DC.ID = KJ.DIANCXXB_ID
                  AND dc.fuid = LNGDIANCXXB_ID
                  AND dc.id IN (SELECT diancxxb_id FROM dianckjmx WHERE dianckjb_id=1006951090)
                  --  AND sl.zhuangt>=1
                  AND (KJ.JIHKJB_ID = 1 OR KJ.JIHKJB_ID = 3)
                  AND SL.FENX = '本月';

            IF (YUEJHCGL != 0) THEN
              MIAOS := ROUND_NEW(MIAOS / YUEJHCGL * 100, 2);
            ELSE
              MIAOS := 0;
            END IF;
          ELSE IF (JIB = 1) THEN
            ----------累计----------
            SELECT NVL(SUM(HTQK.YUEJHCGL), 0)
            INTO YUEJHCGL
            FROM YUECGJHB HTQK
            WHERE HTQK.RIQ > = GETYEARFIRSTDATE(DATRIQ)
                  AND HTQK.RIQ < = DATRIQ
                  -- AND HTQK.zhuangt=2
                  AND (HTQK.JIHKJB_ID = 1 OR HTQK.JIHKJB_ID = 3)

                  AND HTQK.DIANCXXB_ID IN (SELECT diancxxb_id FROM dianckjmx WHERE dianckjb_id=1006951090);
            --  AND HTQK.DIANCXXB_ID = LNGDIANCXXB_ID;

            SELECT NVL(SUM(LAIMSL), 0)
            INTO MIAOS
            FROM YUESLB SL, YUETJKJB KJ
            WHERE KJ.ID = SL.YUETJKJB_ID
                  AND KJ.RIQ >= GETYEARFIRSTDATE(DATRIQ)
                  AND KJ.RIQ <= DATRIQ
                  -- AND KJ.DIANCXXB_ID = LNGDIANCXXB_ID
                  AND (KJ.JIHKJB_ID = 1 OR KJ.JIHKJB_ID = 3)
                  --  AND sl.zhuangt=2
                  AND KJ.DIANCXXB_ID  IN (SELECT diancxxb_id FROM dianckjmx WHERE dianckjb_id=1006951090)
                  AND SL.FENX = '本月';

            IF (YUEJHCGL != 0) THEN
              MIAOS := ROUND_NEW(MIAOS / YUEJHCGL * 100, 2);
            ELSE
              MIAOS := 0;
            END IF;
          END IF ;
          END IF;
          END IF;

        END IF;

      END IF;
      RETURN MIAOS;
      EXCEPTION
      WHEN OTHERS THEN

      RETURN MIAOS;
    END;

  END GETHETDXL;


-- Create table-----------------------------------------------------------------------------------------------------------
create table DUIXLB
(
  id          NUMBER(15) not null,
  diancxxb_id VARCHAR2(50) not null,
  riq         DATE,
  fenx        VARCHAR2(50),
  duixl       VARCHAR2(20) default 0
)
;
-- Create table-----------------------------------------------------------------------------------------------------------
create table DIANCKJMX
(
  id          NUMBER(15),
  dianckjb_id NUMBER(10),
  xuh         VARCHAR2(10),
  diancxxb_id NUMBER(15),
  beiz        VARCHAR2(100)
);
insert into item (ID, ITEMSORTID, XUH, BIANM, MINGC, ZHUANGT)
values (15830, 204, 1, 'YB', '等比口径', 0);

insert into item (ID, ITEMSORTID, XUH, BIANM, MINGC, ZHUANGT)
values (15829, 204, 1, 'YB', '月报电厂口径', 1);

alter table diancxxb add konggbl number;
update diancxxb set konggbl=1.00;

-- Create table-------------------------算法配置表 ----------------------------------------------------------------------
create table SUANFPZB
(
  id          NUMBER not null,
  yuebrq      DATE not null,
  diancxxb_id NUMBER not null,
  jisff       NUMBER not null,
  beiz        VARCHAR2(500)
);

insert into suanfpzb (ID, YUEBRQ, DIANCXXB_ID, JISFF, BEIZ)
values (630, to_date('01-03-2016', 'dd-mm-yyyy'), 938, 1, '老算法');

insert into suanfpzb (ID, YUEBRQ, DIANCXXB_ID, JISFF, BEIZ)
values (894, to_date('01-01-2015', 'dd-mm-yyyy'), 938, 1, '老算法');

insert into suanfpzb (ID, YUEBRQ, DIANCXXB_ID, JISFF, BEIZ)
values (1154, to_date('01-02-2015', 'dd-mm-yyyy'), 938, 1, '老算法');

insert into suanfpzb (ID, YUEBRQ, DIANCXXB_ID, JISFF, BEIZ)
values (1934, to_date('01-04-2015', 'dd-mm-yyyy'), 938, 1, '老算法');

insert into suanfpzb (ID, YUEBRQ, DIANCXXB_ID, JISFF, BEIZ)
values (2194, to_date('01-04-2016', 'dd-mm-yyyy'), 938, 1, '老算法');

insert into suanfpzb (ID, YUEBRQ, DIANCXXB_ID, JISFF, BEIZ)
values (9458, to_date('01-08-2016', 'dd-mm-yyyy'), 938, 1, '老算法');

insert into suanfpzb (ID, YUEBRQ, DIANCXXB_ID, JISFF, BEIZ)
values (9718, to_date('01-09-2016', 'dd-mm-yyyy'), 938, 1, '老算法');

insert into suanfpzb (ID, YUEBRQ, DIANCXXB_ID, JISFF, BEIZ)
values (9978, to_date('01-10-2016', 'dd-mm-yyyy'), 938, 1, '老算法');

insert into suanfpzb (ID, YUEBRQ, DIANCXXB_ID, JISFF, BEIZ)
values (1414, to_date('01-03-2015', 'dd-mm-yyyy'), 938, 1, '老算法');

insert into suanfpzb (ID, YUEBRQ, DIANCXXB_ID, JISFF, BEIZ)
values (1674, to_date('01-03-2015', 'dd-mm-yyyy'), 938, 1, '老算法');

insert into suanfpzb (ID, YUEBRQ, DIANCXXB_ID, JISFF, BEIZ)
values (114, to_date('01-01-2016', 'dd-mm-yyyy'), 938, 1, '老算法');

insert into suanfpzb (ID, YUEBRQ, DIANCXXB_ID, JISFF, BEIZ)
values (372, to_date('01-02-2016', 'dd-mm-yyyy'), 938, 1, '老算法');

insert into suanfpzb (ID, YUEBRQ, DIANCXXB_ID, JISFF, BEIZ)
values (8938, to_date('01-06-2016', 'dd-mm-yyyy'), 938, 1, '老算法');

insert into suanfpzb (ID, YUEBRQ, DIANCXXB_ID, JISFF, BEIZ)
values (9198, to_date('01-07-2016', 'dd-mm-yyyy'), 938, 1, '老算法');

insert into suanfpzb (ID, YUEBRQ, DIANCXXB_ID, JISFF, BEIZ)
values (10238, to_date('01-11-2016', 'dd-mm-yyyy'), 938, 1, '老算法');

insert into suanfpzb (ID, YUEBRQ, DIANCXXB_ID, JISFF, BEIZ)
values (6598, to_date('01-05-2015', 'dd-mm-yyyy'), 938, 1, '老算法');

insert into suanfpzb (ID, YUEBRQ, DIANCXXB_ID, JISFF, BEIZ)
values (6858, to_date('01-05-2016', 'dd-mm-yyyy'), 938, 1, '老算法');

insert into suanfpzb (ID, YUEBRQ, DIANCXXB_ID, JISFF, BEIZ)
values (7118, to_date('01-06-2015', 'dd-mm-yyyy'), 938, 1, '老算法');

insert into suanfpzb (ID, YUEBRQ, DIANCXXB_ID, JISFF, BEIZ)
values (7378, to_date('01-07-2015', 'dd-mm-yyyy'), 938, 1, '老算法');

insert into suanfpzb (ID, YUEBRQ, DIANCXXB_ID, JISFF, BEIZ)
values (7638, to_date('01-08-2015', 'dd-mm-yyyy'), 938, 1, '老算法');

insert into suanfpzb (ID, YUEBRQ, DIANCXXB_ID, JISFF, BEIZ)
values (7898, to_date('01-09-2015', 'dd-mm-yyyy'), 938, 1, '老算法');

insert into suanfpzb (ID, YUEBRQ, DIANCXXB_ID, JISFF, BEIZ)
values (8158, to_date('01-10-2015', 'dd-mm-yyyy'), 938, 1, '老算法');

insert into suanfpzb (ID, YUEBRQ, DIANCXXB_ID, JISFF, BEIZ)
values (8418, to_date('01-11-2015', 'dd-mm-yyyy'), 938, 1, '老算法');

insert into suanfpzb (ID, YUEBRQ, DIANCXXB_ID, JISFF, BEIZ)
values (8678, to_date('01-12-2015', 'dd-mm-yyyy'), 938, 1, '老算法');

insert into suanfpzb (ID, YUEBRQ, DIANCXXB_ID, JISFF, BEIZ)
values (12318, to_date('01-07-2017', 'dd-mm-yyyy'), 938, 1, '老算法');

insert into suanfpzb (ID, YUEBRQ, DIANCXXB_ID, JISFF, BEIZ)
values (12578, to_date('01-08-2017', 'dd-mm-yyyy'), 938, 1, '老算法');

insert into suanfpzb (ID, YUEBRQ, DIANCXXB_ID, JISFF, BEIZ)
values (14658, to_date('01-01-2018', 'dd-mm-yyyy'), 938, 1, '老算法');

insert into suanfpzb (ID, YUEBRQ, DIANCXXB_ID, JISFF, BEIZ)
values (14918, to_date('01-02-2018', 'dd-mm-yyyy'), 938, 1, '老算法');

insert into suanfpzb (ID, YUEBRQ, DIANCXXB_ID, JISFF, BEIZ)
values (15178, to_date('01-03-2018', 'dd-mm-yyyy'), 938, 1, '老算法');

insert into suanfpzb (ID, YUEBRQ, DIANCXXB_ID, JISFF, BEIZ)
values (15438, to_date('01-04-2018', 'dd-mm-yyyy'), 938, 1, '老算法');

insert into suanfpzb (ID, YUEBRQ, DIANCXXB_ID, JISFF, BEIZ)
values (15698, to_date('01-05-2018', 'dd-mm-yyyy'), 938, 1, '老算法');

insert into suanfpzb (ID, YUEBRQ, DIANCXXB_ID, JISFF, BEIZ)
values (12838, to_date('01-09-2017', 'dd-mm-yyyy'), 938, 1, '老算法');

insert into suanfpzb (ID, YUEBRQ, DIANCXXB_ID, JISFF, BEIZ)
values (13098, to_date('01-10-2017', 'dd-mm-yyyy'), 938, 1, '老算法');

insert into suanfpzb (ID, YUEBRQ, DIANCXXB_ID, JISFF, BEIZ)
values (13358, to_date('01-11-2017', 'dd-mm-yyyy'), 938, 1, '老算法');

insert into suanfpzb (ID, YUEBRQ, DIANCXXB_ID, JISFF, BEIZ)
values (13618, to_date('01-12-2017', 'dd-mm-yyyy'), 938, 1, '老算法');

insert into suanfpzb (ID, YUEBRQ, DIANCXXB_ID, JISFF, BEIZ)
values (15958, to_date('01-06-2018', 'dd-mm-yyyy'), 938, 1, '老算法');

insert into suanfpzb (ID, YUEBRQ, DIANCXXB_ID, JISFF, BEIZ)
values (13878, to_date('01-03-2017', 'dd-mm-yyyy'), 938, 1, '老算法');

insert into suanfpzb (ID, YUEBRQ, DIANCXXB_ID, JISFF, BEIZ)
values (14138, to_date('01-04-2017', 'dd-mm-yyyy'), 938, 1, '老算法');

insert into suanfpzb (ID, YUEBRQ, DIANCXXB_ID, JISFF, BEIZ)
values (14398, to_date('01-05-2017', 'dd-mm-yyyy'), 938, 1, '老算法');

insert into suanfpzb (ID, YUEBRQ, DIANCXXB_ID, JISFF, BEIZ)
values (10498, to_date('01-12-2016', 'dd-mm-yyyy'), 938, 1, '老算法');

insert into suanfpzb (ID, YUEBRQ, DIANCXXB_ID, JISFF, BEIZ)
values (10758, to_date('01-01-2017', 'dd-mm-yyyy'), 938, 1, '老算法');

insert into suanfpzb (ID, YUEBRQ, DIANCXXB_ID, JISFF, BEIZ)
values (11018, to_date('01-02-2017', 'dd-mm-yyyy'), 938, 1, '老算法');

insert into suanfpzb (ID, YUEBRQ, DIANCXXB_ID, JISFF, BEIZ)
values (11278, to_date('01-03-2017', 'dd-mm-yyyy'), 938, 1, '老算法');

insert into suanfpzb (ID, YUEBRQ, DIANCXXB_ID, JISFF, BEIZ)
values (11538, to_date('01-04-2017', 'dd-mm-yyyy'), 938, 1, '老算法');

insert into suanfpzb (ID, YUEBRQ, DIANCXXB_ID, JISFF, BEIZ)
values (11798, to_date('01-05-2017', 'dd-mm-yyyy'), 938, 1, '老算法');

insert into suanfpzb (ID, YUEBRQ, DIANCXXB_ID, JISFF, BEIZ)
values (12058, to_date('01-06-2017', 'dd-mm-yyyy'), 938, 1, '老算法');

insert into suanfpzb (ID, YUEBRQ, DIANCXXB_ID, JISFF, BEIZ)
values (17258, to_date('01-11-2018', 'dd-mm-yyyy'), 938, 1, '老算法');

insert into suanfpzb (ID, YUEBRQ, DIANCXXB_ID, JISFF, BEIZ)
values (16218, to_date('01-07-2018', 'dd-mm-yyyy'), 938, 1, '老算法');

insert into suanfpzb (ID, YUEBRQ, DIANCXXB_ID, JISFF, BEIZ)
values (16478, to_date('01-08-2018', 'dd-mm-yyyy'), 938, 1, '老算法');

insert into suanfpzb (ID, YUEBRQ, DIANCXXB_ID, JISFF, BEIZ)
values (16738, to_date('01-09-2018', 'dd-mm-yyyy'), 938, 1, '老算法');

insert into suanfpzb (ID, YUEBRQ, DIANCXXB_ID, JISFF, BEIZ)
values (16998, to_date('01-10-2018', 'dd-mm-yyyy'), 938, 1, '老算法');
------------------------------------------------------------------------------------------------------------------------
update meikdqb set bianm='216499000000' where mingc='宁夏小矿';
update meikdqb set bianm='990001000001' where mingc='神华宁夏煤业集团有限公司';
-----------------------月报上传触发器-------------------------------------------------------------------------------------
CREATE OR REPLACE TRIGGER trigger_ud_yueslb
Before Insert Or Update Or Delete On yueslb
For Each Row
  Declare
    v_diancxxb_id number(15);
    v_riq date;
  Begin
    if inserting then
      select  diancxxb_id,riq into v_diancxxb_id,v_riq  from yuetjkjb t where t.id=:new.yuetjkjb_id;
      AddInterfaceTask('yueslb', :new.id,0, v_diancxxb_id, 'xml', :new.id, v_riq );
    elsif deleting then
      select  diancxxb_id,riq into v_diancxxb_id,v_riq  from yuetjkjb t where t.id=:old.yuetjkjb_id;
      AddInterfaceTask('yueslb', :old.id,1 , v_diancxxb_id, 'xml', :old.id,v_riq);
    elsif updating then
      select  diancxxb_id,riq into v_diancxxb_id,v_riq  from yuetjkjb t where t.id=:old.yuetjkjb_id;
      AddInterfaceTask('yueslb', :old.id, 2, v_diancxxb_id, 'xml', :old.id,v_riq);
    end if;
    exception
    when others then
    return ;
  End;
--------------------------------------月统计口径表触发器-------------------------------------------------------------------
CREATE OR REPLACE TRIGGER trigger_ud_yuetjkjb
Before Insert Or Update Or Delete On yuetjkjb
For Each Row
  Declare
  Begin
    if inserting then
      AddInterfaceTask('yuetjkjb', :new.id,0, :new.diancxxb_id, 'xml', :new.id,:new.riq);
    elsif deleting then
      AddInterfaceTask('yuetjkjb', :old.id,1 , :old.diancxxb_id, 'xml', :old.id,:old.riq);
    elsif updating then
      AddInterfaceTask('yuetjkjb', :old.id, 2, :old.diancxxb_id, 'xml', :old.id,:old.riq);
    end if;
    exception
    when others then
    return ;
  End;

------------------------------------------------------------------------------------------------------------------------
insert into jiekzhb (id,yonghmc,mim,diancxxb_id,endpointaddress)values(0,'青铝交换',1234,938,'http://10.112.104.14:8080/gongysinterface/InterfaceSupplyer.jws');

------------------------------------------------------------------------------------------------------------------------
delete from jihkjb;
insert into jihkjb (ID, XUH, MINGC, BIANM, BEIZ)
values (1, 1, '重点订货', '41', null);

insert into jihkjb (ID, XUH, MINGC, BIANM, BEIZ)
values (2, 2, '市场采购', '43', null);

insert into jihkjb (ID, XUH, MINGC, BIANM, BEIZ)
values (3, 3, '区域订货', '42', null);

insert into jihkjb (ID, XUH, MINGC, BIANM, BEIZ)
values (4, 4, '无', '44', null);
-------------------------月质量表触发器-----------------------------------------------------------------------------------
CREATE OR REPLACE TRIGGER trigger_ud_yuezlb
Before Insert Or Update Or Delete On yuezlb
For Each Row
  Declare
    v_diancxxb_id number(15);
    v_riq date;
  Begin
    if inserting then
      select  diancxxb_id,riq into v_diancxxb_id,v_riq  from yuetjkjb t where t.id=:new.yuetjkjb_id;
      AddInterfaceTask('yuezlb', :new.id,0, v_diancxxb_id, 'xml', :new.id, v_riq );
    elsif deleting then
      select  diancxxb_id,riq into v_diancxxb_id,v_riq  from yuetjkjb t where t.id=:old.yuetjkjb_id;
      AddInterfaceTask('yuezlb', :old.id,1 , v_diancxxb_id, 'xml', :old.id,v_riq);
    elsif updating then
      select  diancxxb_id,riq into v_diancxxb_id,v_riq  from yuetjkjb t where t.id=:old.yuetjkjb_id;
      AddInterfaceTask('yuezlb', :old.id, 2, v_diancxxb_id, 'xml', :old.id,v_riq);
    end if;
    exception
    when others then
    return ;
  End;
  -------------------------------------月收耗存合计表触发器--------------------------------------------------------
CREATE OR REPLACE TRIGGER trigger_ud_yueshchjb
Before Insert Or Update Or Delete On yueshchjb
For Each Row
  Declare
  Begin
    if inserting then
      AddInterfaceTask('yueshchjb', :new.id,0, :new.diancxxb_id, 'xml', :new.id,:new.riq );
    elsif deleting then
      AddInterfaceTask('yueshchjb', :old.id,1 , :old.diancxxb_id, 'xml', :old.id,:old.riq);
    elsif updating then
      AddInterfaceTask('yueshchjb', :old.id, 2, :old.diancxxb_id, 'xml', :old.id,:old.riq );
    end if;
    exception
    when others then
    return ;
  End;
-------------------------------------月油收耗存合计表触发器--------------------------------------------------------
CREATE OR REPLACE TRIGGER trigger_ud_yueshcyb
Before Insert Or Update Or Delete On yueshcyb
For Each Row
  Declare
  Begin
    if inserting then
      AddInterfaceTask('yueshcyb', :new.id,0, :new.diancxxb_id, 'xml', :new.id,:new.riq );
    elsif deleting then
      AddInterfaceTask('yueshcyb', :old.id,1 , :old.diancxxb_id, 'xml', :old.id,:old.riq);
    elsif updating then
      AddInterfaceTask('yueshcyb', :old.id, 2, :old.diancxxb_id, 'xml', :old.id,:old.riq );
    end if;
    exception
    when others then
    return ;
  End;



-------------------------------------月入厂标煤单价表触发器--------------------------------------------------------


CREATE OR REPLACE TRIGGER trigger_ud_yuercbmdj
Before Insert Or Update Or Delete On yuercbmdj
For Each Row
  Declare
    v_diancxxb_id number(15);
    v_riq date;
  Begin
    if inserting then
      select  diancxxb_id,riq into v_diancxxb_id,v_riq  from yuetjkjb t where t.id=:new.yuetjkjb_id;
      AddInterfaceTask('yuercbmdj', :new.id,0, v_diancxxb_id, 'xml', :new.id, v_riq );
    elsif deleting then
      select  diancxxb_id,riq into v_diancxxb_id,v_riq  from yuetjkjb t where t.id=:old.yuetjkjb_id;
      AddInterfaceTask('yuercbmdj', :old.id,1 , v_diancxxb_id, 'xml', :old.id,v_riq);
    elsif updating then
      select  diancxxb_id,riq into v_diancxxb_id,v_riq  from yuetjkjb t where t.id=:old.yuetjkjb_id;
      AddInterfaceTask('yuercbmdj', :old.id, 2, v_diancxxb_id, 'xml', :old.id,v_riq);
    end if;
    exception
    when others then
    return ;
  End;
-------------------------------------入厂油成本触发器--------------------------------------------------------
CREATE OR REPLACE TRIGGER trigger_ud_rucycbb
Before Insert Or Update Or Delete On rucycbb
For Each Row
  Declare
  Begin
    if inserting then
      AddInterfaceTask('rucycbb', :new.id,0, :new.diancxxb_id, 'xml', :new.id,:new.riq );
    elsif deleting then
      AddInterfaceTask('rucycbb', :old.id,1 , :old.diancxxb_id, 'xml', :old.id,:old.riq);
    elsif updating then
      AddInterfaceTask('rucycbb', :old.id, 2, :old.diancxxb_id, 'xml', :old.id,:old.riq );
    end if;
    exception
    when others then
    return ;
  End;
  ---------------------------------------------------------------------------------------------------------

-------------------------------------月指标表触发器--------------------------------------------------------
CREATE OR REPLACE TRIGGER trigger_ud_yuezbb
Before Insert Or Update Or Delete On yuezbb
For Each Row
  Declare
  Begin
    if inserting then
      AddInterfaceTask('yuezbb', :new.id,0, :new.diancxxb_id, 'xml', :new.id,:new.riq );
    elsif deleting then
      AddInterfaceTask('yuezbb', :old.id,1 , :old.diancxxb_id, 'xml', :old.id,:old.riq);
    elsif updating then
      AddInterfaceTask('yuezbb', :old.id, 2, :old.diancxxb_id, 'xml', :old.id,:old.riq );
    end if;
    exception
    when others then
    return ;
  End;
-------------------------------------年度合同执行情况表触发器--------------------------------------------------------
CREATE OR REPLACE TRIGGER trigger_ud_niandhtzxqkb
Before Insert Or Update Or Delete On niandhtzxqkb
For Each Row
  Declare
  Begin
    if inserting then
      AddInterfaceTask('niandhtzxqkb', :new.id,0, :new.diancxxb_id, 'xml', :new.id,:new.riq );
    elsif deleting then
      AddInterfaceTask('niandhtzxqkb', :old.id,1 , :old.diancxxb_id, 'xml', :old.id,:old.riq);
    elsif updating then
      AddInterfaceTask('niandhtzxqkb', :old.id, 2, :old.diancxxb_id, 'xml', :old.id,:old.riq );
    end if;
    exception
    when others then
    return ;
  End;
  --------------------------------------------------------------------------------------------------------------------
update baobpzzb set format='0.00' where id= 476145864;
update baobpzzb set format='0.00' where id= 47614775334;commit;
---------------------------------------------------------------------------------------------------------------------------

insert into ziyxxb (ID, XUH, MINGC, WENJWZ, JIB, FUID, LEIX, LEIB)
values (10025281877, 2, '日调运计划', 'Ridyjh', 3, 10801, -1, 3);

insert into ziyxxb (ID, XUH, MINGC, WENJWZ, JIB, FUID, LEIX, LEIB)
values (10025281878, 3, '日调运计划查询', 'Ridyjhcx', 3, 10801, -1, 3);
commit;

