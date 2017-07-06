----用户表
create table sardine_user(
			uuid varchar(30) not null primary key,
			code varchar(30) not null,
			name varchar(100) not null,
			passwd varchar(30),
			phone varchar(30),
			userstate varchar(30) not null,
			companyUuid varchar(30) not null,
			companycode varchar(30) not null,
			companyname varchar(100) not null,
			administrator char(1) default '0',
			version int default 0,
			createdtime datetime not null,
			createdID varchar(32) not null,
			createdcode varchar(30) not null,
			createdName varchar(100) not null,
			lastModifytime datetime not null,
			lastModifyid varchar(32) not null,
			lastModifycode varchar(30) not null,
			lastModifyname varchar(100) not null
);

create unique index sardine_user_code_idx on sardine_wms.sardine_user (code); 

----商品表
create table sardine_article(
			uuid varchar(30) not null primary key,
			code varchar(30) not null,
			name varchar(100) not null,
			spec varchar(30) not null,
			state varchar(30) not null,
			expDays int not null,
			companyUuid varchar(30) not null,
			categoryuuid varchar(32) not null,
			categorycode varchar(30) not null,
			categoryname varchar(30) not null,
			firstInFirstOut char(1) default '0',
			version int default 0,
			createdtime datetime not null,
			createdID varchar(32) not null,
			createdcode varchar(30) not null,
			createdName varchar(100) not null,
			lastModifytime datetime not null,
			lastModifyid varchar(32) not null,
			lastModifycode varchar(30) not null,
			lastModifyname varchar(100) not null
);

create unique index sardine_article_code_idx on sardine_wms.sardine_article (code, companyuuid); 

----商品规格表
create table sardine_article_qpc(
			uuid varchar(30) not null primary key,
			qpcstr varchar(30) not null,
			articleuuid varchar(30) not null,
			length DECIMAL(12,3) default 0,
			width DECIMAL(12,3) default 0,
			height DECIMAL(12,3) default 0,
			weight DECIMAL(12,3) default 0,
			default_ char(1) default '0'
);
create index sardine_article_qpcStr_idx01 on sardine_wms.sardine_article_qpc (qpcstr); 
create index sardine_article_qpcStr_idx02 on sardine_wms.sardine_article_qpc (articleuuid); 

----商品供应商表
create table sardine_article_supplier(
			uuid varchar(30) not null primary key,
			articleuuid varchar(30) not null,
			supplieruuid varchar(30) not null,
			suppliercode varchar(30) not null,
			suppliername varchar(100) not null,
			default_ char(1) default '0'
);
create index sardine_article_supplier_idx01 on sardine_wms.sardine_article_qpc (articleuuid); 

----商品条码表
create table sardine_article_barcode(
			uuid varchar(30) not null primary key,
			articleuuid varchar(30) not null,
			qpcstr varchar(30) not null,
			barcode varchar(30) not null
);
create index sardine_article_barcode_idx01 on sardine_wms.sardine_article_qpc (articleuuid);

----仓位表
create table sardine_wrh(
			uuid varchar(32) not null primary key,
			code varchar(30) not null,
			name varchar(100) not null,
			note varchar(254),
			companyuuid varchar(30) not null
);
create unique index idx_sardine_wrh_01 on sardine_wms.sardine_wrh (code, companyuuid);

----货区表
create table sardine_zone(
			uuid varchar(32) not null primary key,
			code varchar(30) not null,
			name varchar(100) not null,
			wrhuuid varchar(32) not null,
			wrhcode varchar(30) not null,
			wrhname varchar(100) not null,
			note varchar(254),
			companyuuid varchar(30) not null
);
create unique index idx_sardine_zone_01 on sardine_wms.sardine_zone (code, companyuuid);
create index idx_sardine_zone_02 on sardine_wms.sardine_zone (wrhuuid);

----货道表
create table sardine_path(
			uuid varchar(32) not null primary key,
			code varchar(30) not null,
			zoneuuid varchar(32) not null,
			note varchar(254),
			companyuuid varchar(30) not null
);
create unique index idx_sardine_path_01 on sardine_wms.sardine_path (code, companyuuid);
create index idx_sardine_path_02 on sardine_wms.sardine_path (zoneuuid);

----货架表
create table sardine_shelf(
			uuid varchar(32) not null primary key,
			code varchar(30) not null,
		    pathuuid varchar(32) not null,
			note varchar(254),
			companyuuid varchar(30) not null
);
create unique index idx_sardine_shelf_01 on sardine_wms.sardine_shelf (code, companyuuid);
create index idx_sardine_shelf_02 on sardine_wms.sardine_shelf (pathuuid);

----货位表
create table sardine_bin(
			uuid varchar(32) not null primary key,
			code varchar(30) not null,
			bintypeuuid varchar(32) not null,
			bintypecode varchar(30) not null,
			bintypename varchar(100) not null,
			wrhuuid varchar(32) not null,
			wrhcode varchar(30) not null,
			wrhname varchar(100) not null,
		    shelfuuid varchar(32) not null,
		    usage varchar(30) not null,
		    binLevel varchar(30) not null,
		    binColumn varchar(30) not null,
		    state varchar(30) not null,
		    version INT default 0,
			companyuuid varchar(30) not null
);
create unique index idx_sardine_bin_01 on sardine_wms.sardine_bin (code, companyuuid);
create index idx_sardine_bin_02 on sardine_wms.sardine_bin (wrhuuid);
create index idx_sardine_bin_03 on sardine_wms.sardine_bin (bintypeuuid);
create index idx_sardine_bin_04 on sardine_wms.sardine_bin (shelfuuid);
create index idx_sardine_bin_05 on sardine_wms.sardine_bin (binusage);
create index idx_sardine_bin_06 on sardine_wms.sardine_bin (state);

--- 序列表
CREATE TABLE IF NOT EXISTS sequence (  
  name varchar(50) NOT NULL,
  current_value int(11) NOT NULL,  
  increment int(11) NOT NULL DEFAULT '1',
  companyuuid varchar(32) not null
);

DROP FUNCTION IF EXISTS currval;  
CREATE  FUNCTION currval(seq_name VARCHAR(50),companyuuid varchar(32)) RETURNS int(11)
    READS SQL DATA
    DETERMINISTIC
BEGIN  
DECLARE VALUE INTEGER;  
SET VALUE = 0;  
SELECT current_value INTO VALUE FROM sequence WHERE NAME = seq_name and companyuuid = companyuuid;  
RETURN VALUE;  
END;

DROP FUNCTION IF EXISTS nextval;  
CREATE  FUNCTION nextval(seq_name VARCHAR(50),companyuuid varchar(32)) RETURNS int(11)  
    DETERMINISTIC  
BEGIN  
UPDATE sequence SET current_value = current_value + increment WHERE NAME = seq_name and companyuuid = companyuuid;  
RETURN currval(seq_name,companyuuid);  
END;

CREATE TABLE sardine_container (
  uuid varchar(32)  not NULL primary key,
  barcode varchar(32) not NULL,
  containerTypeUuid varchar(32) not NULL,
  containerTypeCode varchar(32) not NULL,
  containerTypeName varchar(32) not NULL,
  companyUuid varchar(32)  not NULL,
  state varchar(32)  not NULL,
  position varchar(32) not NULL,
  toposition varchar(32)  NOT NULL,
  remark varchar(255)  DEFAULT NULL,
  version int(11) DEFAULT '0',
  createdtime datetime NOT NULL,
  createdID varchar(30)  NOT NULL,
  createdcode varchar(30)  NOT NULL,
  createdName varchar(100)  NOT NULL,
  lastModifytime datetime NOT NULL,
  lastModifyid varchar(30)  NOT NULL,
  lastModifycode varchar(30)  NOT NULL,
  lastModifyname varchar(100)  NOT NULL
);
create unique index idx_sardine_container_01 on sardine_wms.sardine_container (barcode, companyuuid);

CREATE TABLE sardine_category (
	uuid  varchar(32)  NOT NULL primary key,
	code  varchar(32)  NOT NULL ,
	name  varchar(32)  NOT NULL ,
	companyUuid  varchar(32)  NULL DEFAULT NULL ,
	uppercategory  varchar(32)  NOT NULL ,
	remark  varchar(255)  NULL DEFAULT NULL ,
	version  int(11) NULL DEFAULT 0 ,
	createdtime  datetime NOT NULL ,
	createdID  varchar(30)  NOT NULL ,
	createdcode  varchar(30)  NOT NULL ,
	createdName  varchar(100)  NOT NULL ,
	lastModifytime  datetime NOT NULL ,
	lastModifyid  varchar(30)  NOT NULL ,
	lastModifycode  varchar(30)  NOT NULL ,
	lastModifyname  varchar(100)  NOT NULL 
);
create unique index idx_sardine_category_01 on sardine_wms.sardine_category (barcode, companyuuid);

CREATE TABLE SARDINE_CONTAINERTYPE
(
	UUID	       VARCHAR(32)	NOT NULL,
	CODE	       VARCHAR(30)	NOT NULL,
	NAME	       VARCHAR(100)	NOT NULL,
	BARCODEPREFIX  VARCHAR(32)	NOT NULL,
	BARCODELENGTH  INT		    NOT NULL,
	INLENGTH	   DECIMAL(12,3)    NULL,
	INWIDTH		   DECIMAL(12,3)    NULL,
	INHEIGHT	   DECIMAL(12,3)    NULL,
	OUTLENGTH	   DECIMAL(12,3)    NULL,
	OUTWIDTH	   DECIMAL(12,3)    NULL,
	OUTHEIGHT	   DECIMAL(12,3)    NULL,
	WEIGHT	       DECIMAL(12,3)    NULL,
	BEARINGWEIGHT  DECIMAL(12,3)    NULL,
  	ISSHIP		   VARCHAR(30)      NULL,
  	BARCODETYPE	   VARCHAR(30)      NULL,
	RATE		   DECIMAL(8,5)     NULL,
	COMPANYUUID	   VARCHAR(30)	NOT NULL,
	VERSION	       INT	NOT NULL,
	CREATEDID	   VARCHAR(32)	NOT NULL,
	CREATEDCODE	   VARCHAR(30)	NOT NULL,
	CREATEDNAME	   VARCHAR(100)	NOT NULL,
	LASTMODIFYID   VARCHAR(32)	NOT NULL,
	LASTMODIFYCODE VARCHAR(30)	NOT NULL,
	LASTMODIFYNAME VARCHAR(100)	NOT NULL,
	CREATEDTIME	   DATETIME	NOT NULL,
	LASTMODIFYTIME DATETIME	NOT NULL
);
create unique index idx_sardine_containertype_01 on sardine_wms.sardine_containertype (code, companyuuid);


CREATE TABLE SARDINE_SUPPLIER
(
  UUID           VARCHAR(32) NOT NULL,
  CODE           VARCHAR(30) NOT NULL,
  NAME           VARCHAR(100) NOT NULL,
  COMPANYUUID    VARCHAR(30) NOT NULL,
  ADDRESS        VARCHAR(100) NOT NULL,
  PHONE          VARCHAR(30) NOT NULL,
  REMARK	     VARCHAR(255)	NULL,
  CREATEDTIME    DATETIME NOT NULL,
  CREATEDID      VARCHAR(30) NOT NULL,
  CREATEDCODE    VARCHAR(30) NOT NULL,
  CREATEDNAME    VARCHAR(30) NOT NULL,
  LASTMODIFYTIME DATETIME NOT NULL,
  LASTMODIFYID   VARCHAR(30) NOT NULL,
  LASTMODIFYCODE VARCHAR(30) NOT NULL,
  LASTMODIFYNAME VARCHAR(30) NOT NULL,
  VERSION        NUMERIC(19) NOT NULL,
  STATE      VARCHAR(30) NOT NULL
);
create unique index sardine_supplier_code_idx on sardine_wms.sardine_supplier (code, companyuuid);


--客户表
CREATE TABLE SARDINE_CUSTOMER
(
	UUID	       VARCHAR(32)	NOT NULL,
	CODE	       VARCHAR(30)	NOT NULL,
	NAME	       VARCHAR(100)	NOT NULL,
	CUSTOMERTYPE   VARCHAR(100)	NOT NULL,
	PHONE	       VARCHAR(100)	NOT NULL,
	ADDRESS	       VARCHAR(100)	NOT NULL,
	STATE		   VARCHAR(100)	NOT NULL,
	COMPANYUUID	   VARCHAR(30)	NOT NULL,
	REMARK	       VARCHAR(255)	NULL,
	VERSION	       INT	NOT NULL,
	CREATEDID	   VARCHAR(32)	NOT NULL,
	CREATEDCODE	   VARCHAR(30)	NOT NULL,
	CREATEDNAME	   VARCHAR(100)	NOT NULL,
	LASTMODIFYID   VARCHAR(32)	NOT NULL,
	LASTMODIFYCODE VARCHAR(30)	NOT NULL,
	LASTMODIFYNAME VARCHAR(100)	NOT NULL,
	CREATEDTIME	   DATETIME	NOT NULL,
	LASTMODIFYTIME DATETIME	NOT NULL
);
CREATE UNIQUE INDEX idx_sardine_customer_01 on sardine_customer(UUID,CODE)

--货位类型表
CREATE TABLE SARDINE_BINTYPE
(
	UUID	       VARCHAR(32)	NOT NULL,
	CODE	       VARCHAR(30)	NOT NULL,
	NAME	       VARCHAR(100)	NOT NULL,
	LENGTH   	   DECIMAL		NOT NULL,
	WIDTH	       DECIMAL		NOT NULL,
	HEIGHT	       DECIMAL		NOT NULL,
	PLOTRATIO	   DECIMAL		NOT NULL,
	BEARING	       DECIMAL		NOT NULL,
	VERSION	       NUMERIC(19)	NOT NULL,
	CREATEDID	   VARCHAR(32)	NOT NULL,
	CREATEDCODE	   VARCHAR(30)	NOT NULL,
	CREATEDNAME	   VARCHAR(100)	NOT NULL,
	LASTMODIFYID   VARCHAR(32)	NOT NULL,
	LASTMODIFYCODE VARCHAR(30)	NOT NULL,
	LASTMODIFYNAME VARCHAR(100)	NOT NULL,
	CREATEDTIME	   DATETIME		NOT NULL,
	LASTMODIFYTIME DATETIME		NOT NULL
);
CREATE UNIQUE INDEX idx_sardine_bintype_01 on sardine_bintype(UUID,CODE)

--企业表
create table sardine_company (
	uuid varchar(32) not NULL,
	companycode varchar(30) NOT NULL,
	companyname varchar(100) NOT NULL,
	companytype VARCHAR(100) NOT NULL,
	address varchar(100) not null,
	homepage varchar(100)
);
CREATE UNIQUE INDEX IDX_SARDINE_COMPANY_01 ON SARDINE_COMPANY(UUID,COMPANYCODE);

--配单表
CREATE TABLE sardine_alcntcbill 
(
	UUID VARCHAR(32) NOT NULL PRIMARY KEY,
	BILLNUMBER VARCHAR(30) NOT NULL,
	STATE VARCHAR(100) NOT NULL,
	DELIVERYREASON VARCHAR(100) NOT NULL,
	DELIVERYSYSTEM VARCHAR(100) NOT NULL,
	DELIVERYMODE VARCHAR(100) NOT NULL,
	SOURCEBILLNUMBER VARCHAR(30) NOT NULL,
	SOURCEBILLTYPE VARCHAR(100) NOT NULL,
	WRHUUID VARCHAR(32) NOT NULL,
	COMPANYUUID VARCHAR(32) NOT NULL,
	REAMRK VARCHAR(255) ,
	VERSION INT(11) NOT NULL,
	CUSTOMERUUID VARCHAR(32) NOT NULL,
	CUSTOMERCODE VARCHAR(30) NOT NULL,
	CUSTOMERNAME VARCHAR(100) NOT NULL,
	TASKBILLNUMBER VARCHAR(30) NOT NULL,
	TOTALCASEQTYSTR VARCHAR(30) NOT NULL,
	TOTALAMOUNT DECIMAL(12) NOT NULL,
	PLANTOTALCASEQTYSTR VARCHAR(30) NOT NULL,
	PLANTOTALAMOUNT DECIMAL(12) NOT NULL,
	REALTOTALCASEQTYSTR VARCHAR(30) NOT NULL,
	REALTOTALAMOUNT DECIMAL(12) NOT NULL,
	CREATEDID VARCHAR(32) NOT NULL,
	CREATEDCODE VARCHAR(30) NOT NULL,
	CREATEDNAME VARCHAR(100) NOT NULL,
	LASTMODIFYID VARCHAR(32) NOT NULL,
	LASTMODIFYCODE VARCHAR(30) NOT NULL,
	LASTMODIFYNAME VARCHAR(100) NOT NULL,
	CREATEDTIME DATETIME NOT NULL,
	LASTMODIFYTIME DATETIME NOT NULL
);
CREATE UNIQUE INDEX IDX_SARDINE_ALCNTCBILL_01 ON sardine_alcntcbill(BILLNUMBER,COMPANYUUID);

--配单明细表
CREATE TABLE SARDINE_ALCNTCBILLITEM
(
	UUID VARCHAR(32) NOT NULL PRIMARY KEY,
	ALCNTCBILLUUID VARCHAR(32) NOT NULL,
	LINE INT(12) NOT NULL,
	ARTICLEUUID VARCHAR(32) NOT NULL,
	QPCSTR VARCHAR(30) NOT NULL,
	MUNIT VARCHAR(30) NOT NULL,
	QTY DECIMAL(12,3) NOT NULL,
	CASEQTYSTR VARCHAR(30) NOT NULL,
	PLANQTY DECIMAL(12,3) ,
	PLANCASEQTYSTR VARCHAR(30),
	REALQTY DECIMAL(12,3),
	REALCASEQTYSTR VARCHAR(30),
	PRICE DECIMAL(12,3) NOT NULL,
	AMOUNT DECIMAL(12,3) NOT NULL,
	REMARK VARCHAR(255)
);

--承运商表
CREATE TABLE SARDINE_CARRIER
(
	UUID VARCHAR(32) NOT NULL PRIMARY KEY,
	CODE VARCHAR(30) NOT NULL,
	NAME VARCHAR(100) NOT NULL,
	ADDRESS VARCHAR(255),
	CONTACTPHONE VARCHAR(30),
	CONTACT VARCHAR(100),
	COMPANYUUID VARCHAR(32) NOT NULL,
	CREATEDID VARCHAR(32) NOT NULL,
	CREATEDCODE VARCHAR(30) NOT NULL,
	CREATEDNAME VARCHAR(100) NOT NULL,
	LASTMODIFYID VARCHAR(32) NOT NULL,
	LASTMODIFYCODE VARCHAR(30) NOT NULL,
	LASTMODIFYNAME VARCHAR(100) NOT NULL,
	CREATEDTIME DATETIME NOT NULL,
	LASTMODIFYTIME DATETIME NOT NULL,
	VERSION INT(11) NOT NULL,
	STATE VARCHAR(100) NOT NULL
);

CREATE UNIQUE INDEX IDX_SARDINE_CARRIER_01 ON sardine_carrier(CODE,COMPANYUUID);

--车型表
CREATE TABLE SARDINE_VEHICLETYPE
(
	UUID VARCHAR(32) NOT NULL PRIMARY KEY,
	CODE VARCHAR(30) NOT NULL,
	NAME VARCHAR(100) NOT NULL,
	BEARWEIGHT DECIMAL(12,3) NOT NULL,
	WEIGHT DECIMAL(12,3) NOT NULL,
	LENGTH DECIMAL(12,3) NOT NULL,
	WIDTH DECIMAL(12,3) NOT NULL,
	HEIGHT DECIMAL(12,3) NOT NULL,
	VOLUME DECIMAL(12,3) NOT NULL,
	BEARVOLUME DECIMAL(12,3) NOT NULL,
	COMPANYUUID VARCHAR(32) NOT NULL,
	CREATEDID VARCHAR(32) NOT NULL,
	CREATEDCODE VARCHAR(30) NOT NULL,
	CREATEDNAME VARCHAR(100) NOT NULL,
	LASTMODIFYID VARCHAR(32) NOT NULL,
	LASTMODIFYCODE VARCHAR(30) NOT NULL,
	LASTMODIFYNAME VARCHAR(100) NOT NULL,
	CREATEDTIME DATETIME NOT NULL,
	LASTMODIFYTIME DATETIME NOT NULL,
	VERSION INT(11) NOT NULL
)

CREATE UNIQUE INDEX IDX_SARDINE_VEHICLETYPE_01 ON sardine_vehicleTYPE(CODE,COMPANYUUID);

--车辆表
CREATE TABLE SARDINE_VEHICLE
(
	UUID VARCHAR(32) NOT NULL PRIMARY KEY,
	CODE VARCHAR(30) NOT NULL,
	VEHICLENO VARCHAR(30) NOT NULL,
	STATE VARCHAR(30) NOT NULL,
	VEHICLETYPEUUID VARCHAR(32) NOT NULL,
	VEHICLETYPECODE VARCHAR(30) NOT NULL,
	VEHICLETYPENAME VARCHAR(100) NOT NULL,
	CARRIERUUID VARCHAR(32) NOT NULL,
	CARRIERCODE VARCHAR(30) NOT NULL,
	CARRIERNAME VARCHAR(100) NOT NULL,
	COMPANYUUID VARCHAR(32) NOT NULL,
	CREATEDID VARCHAR(32) NOT NULL,
	CREATEDCODE VARCHAR(30) NOT NULL,
	CREATEDNAME VARCHAR(100) NOT NULL,
	LASTMODIFYID VARCHAR(32) NOT NULL,
	LASTMODIFYCODE VARCHAR(30) NOT NULL,
	LASTMODIFYNAME VARCHAR(100) NOT NULL,
	CREATEDTIME DATETIME NOT NULL,
	LASTMODIFYTIME DATETIME NOT NULL,
	VERSION INT(11) NOT NULL
 );
 
CREATE INDEX IDX_SARDINE_VEHICLE_01 ON sardine_vehicle(CODE,COMPANYUUID);
CREATE INDEX IDX_SARDINE_VEHICLE_02 ON sardine_vehicle(VEHICLENO,COMPANYUUID);

--拣货分区表
CREATE TABLE SARDINE_PICKAREA
(
	UUID VARCHAR(32) NOT NULL PRIMARY KEY,
	CODE VARCHAR(30) NOT NULL,
	NAME VARCHAR(100) NOT NULL,
	BINSCOPE VARCHAR(100) NOT NULL,
	STORAGEAREA VARCHAR(100) NULL,
	PICKMODE VARCHAR(30) NOT NULL,
	PICKVOLUME DECIMAL(12,3) NOT NULL,
	RPLMODE VARCHAR(30) NOT NULL,
	REMARK VARCHAR(255) NULL,
	RPLQTYMODE VARCHAR(30) NOT NULL,
	COMPANYUUID VARCHAR(32) NOT NULL,
	CREATEDID VARCHAR(32) NOT NULL,
	CREATEDCODE VARCHAR(30) NOT NULL,
	CREATEDNAME VARCHAR(100) NOT NULL,
	LASTMODIFYID VARCHAR(32) NOT NULL,
	LASTMODIFYCODE VARCHAR(30) NOT NULL,
	LASTMODIFYNAME VARCHAR(100) NOT NULL,
	CREATEDTIME DATETIME NOT NULL,
	LASTMODIFYTIME DATETIME NOT NULL,
	VERSION INT(11) NOT NULL
);
CREATE INDEX IDX_SARDINE_PICKAREA_01 ON sardine_pickarea(CODE,COMPANYUUID);

--线路体系表
CREATE TABLE SARDINE_SERIALARCH
(
	UUID VARCHAR(32) PRIMARY KEY NOT NULL,
	CODE VARCHAR(30) NOT NULL,
	NAME VARCHAR(100) NOT NULL,
	COMPANYUUID VARCHAR(32) NOT NULL,
	CREATEDID VARCHAR(32) NOT NULL,
	CREATEDCODE VARCHAR(30) NOT NULL,
	CREATEDNAME VARCHAR(100) NOT NULL,
	LASTMODIFYID VARCHAR(32) NOT NULL,
	LASTMODIFYCODE VARCHAR(30) NOT NULL,
	LASTMODIFYNAME VARCHAR(100) NOT NULL,
	CREATEDTIME DATETIME NOT NULL,
	LASTMODIFYTIME DATETIME NOT NULL,
	VERSION INT(11) NOT NULL
);
CREATE INDEX IDX_SARDINE_SERIALARCH_01 ON SARDINE_SERIALARCH(CODE,COMPANYUUID);

--运输线路表
CREATE TABLE SARDINE_SERIALARCHLINE
(
	UUID VARCHAR(32) PRIMARY KEY NOT NULL,
	SERIALARCHUUID VARCHAR(32) NOT NULL,
	CODE VARCHAR(30) NOT NULL,
	NAME VARCHAR(100) NOT NULL,
	COMPANYUUID VARCHAR(32) NOT NULL,
	CREATEDID VARCHAR(32) NOT NULL,
	CREATEDCODE VARCHAR(30) NOT NULL,
	CREATEDNAME VARCHAR(100) NOT NULL,
	LASTMODIFYID VARCHAR(32) NOT NULL,
	LASTMODIFYCODE VARCHAR(30) NOT NULL,
	LASTMODIFYNAME VARCHAR(100) NOT NULL,
	CREATEDTIME DATETIME NOT NULL,
	LASTMODIFYTIME DATETIME NOT NULL,
	VERSION INT(11) NOT NULL
);
CREATE INDEX IDX_SARDINE_SERIALARCHLINE_01 ON SARDINE_SERIALARCHLINE(CODE,COMPANYUUID);
CREATE INDEX IDX_SARDINE_SERIALARCHLINE_02 ON SARDINE_SERIALARCHLINE(SERIALARCHUUID);

--运输线路客户顺序表
CREATE TABLE SARDINE_SERIALARCHLINECUSTOMER
(
	CUSTOMERUUID VARCHAR(32) NOT NULL,
	LINEORDER INT(12) NOT NULL,
	SERIALARCHLINEUUID VARCHAR(32) NOT NULL
);
CREATE INDEX IDX_SARDINE_SERIALARCHLINECUSTOMER ON SARDINE_SERIALARCHLINECUSTOMER(SERIALARCHLINEUUID);

--波次单表
CREATE TABLE SARDINE_WAVEBILL
(
	UUID VARCHAR(32) NOT NULL PRIMARY KEY,
	BILLNUMBER VARCHAR(30) NOT NULL,
	SERIALARCHUUID VARCHAR(32) NOT NULL,
	SERIALARCHCODE VARCHAR(30) NOT NULL,
	SERIALARCHNAME VARCHAR(100) NOT NULL,
	WAVETYPE VARCHAR(100) NOT NULL,
	REMARK VARCHAR(255) NULL,
	STATE VARCHAR(100) NOT NULL,
	COMPANYUUID VARCHAR(32) NOT NULL,
	VERSION INT(11) NOT NULL,
	CREATEDID VARCHAR(32) NOT NULL,
	CREATEDCODE VARCHAR(30) NOT NULL,
	CREATEDNAME VARCHAR(100) NOT NULL,
	LASTMODIFYID VARCHAR(32) NOT NULL,
	LASTMODIFYCODE VARCHAR(30) NOT NULL,
	LASTMODIFYNAME VARCHAR(100) NOT NULL,
	CREATEDTIME DATETIME NOT NULL,
	LASTMODIFYTIME DATETIME NOT NULL
);
CREATE INDEX IDX_SARDINE_WAVEBILL_01 ON SARDINE_WAVEBILL(BILLNUMBER,COMPANYUUID);

--波次单明细表
CREATE TABLE SARDINE_WAVEBILLITEM
(
	UUID VARCHAR(32) NOT NULL PRIMARY KEY,
	WAVEBILLUUID VARCHAR(32) NOT NULL,
	LINE INT(12) NOT NULL,
	ALCNTCBILLNUMBER VARCHAR(30) NOT NULL,
	ALCNTCBILLSTATE VARCHAR(100) NOT NULL,
	CUSTOMERUUID VARCHAR(32) NOT NULL,
	CUSTOMERCODE VARCHAR(30) NOT NULL,
	CUSTOMERNAME VARCHAR(100) NOT NULL
);
CREATE INDEX IDX_SARDINE_WAVEBILLITEM_01 ON SARDINE_WAVEBILLITEM(WAVEBILLUUID);

--退仓通知单表
CREATE TABLE SARDINE_RETURNNTCBILL
(
	UUID VARCHAR(32) NOT NULL PRIMARY KEY,
	BILLNUMBER VARCHAR(30) NOT NULL,
	STATE VARCHAR(30) NOT NULL,
	SOURCEBILLTYPE VARCHAR(100) NULL,
	SOURCEBILLNUMBER VARCHAR(30) NULL,
	WRHUUID VARCHAR(32) NOT NULL,
	COMPANYUUID VARCHAR(32) NOT NULL,
	CUSTOMERUUID VARCHAR(32) NOT NULL,
	CUSTOMERCODE VARCHAR(30) NOT NULL,
	CUSTOMERNAME VARCHAR(100) NOT NULL,
	RETURNDATE DATETIME NOT NULL,
	TOTALCASEQTYSTR VARCHAR(30) NOT NULL,
	TOTALRETURNEDCASEQTYSTR VARCHAR(30) NOT NULL,
	TOTALAMOUNT DECIMAL(12,3) NOT NULL,
	TOTALRETURNEDAMOUNT DECIMAL(12,3) NOT NULL,
	REMARK VARCHAR(255) NULL,
	VERSION INT(11) NOT NULL,
	CREATEDID VARCHAR(32) NOT NULL,
	CREATEDCODE VARCHAR(30) NOT NULL,
	CREATEDNAME VARCHAR(100) NOT NULL,
	LASTMODIFYID VARCHAR(32) NOT NULL,
	LASTMODIFYCODE VARCHAR(30) NOT NULL,
	LASTMODIFYNAME VARCHAR(100) NOT NULL,
	CREATEDTIME DATETIME NOT NULL,
	LASTMODIFYTIME DATETIME NOT NULL
);
CREATE INDEX IDX_SARDINE_RETURNNTCBILL_01 ON SARDINE_RETURNNTCBILL(BILLNUMBER,COMPANYUUID);

--退仓通知单明细表
CREATE TABLE SARDINE_RETURNNTCBILLITEM
(
	UUID VARCHAR(32) NOT NULL PRIMARY KEY,
	RETURNNTCBILLUUID VARCHAR(32) NOT NULL,
	LINE INT(11) NOT NULL,
	ARTICLEUUID VARCHAR(32) NOT NULL,
	MUNIT VARCHAR(30) NOT NULL,
	QPCSTR VARCHAR(30) NOT NULL,
	SUPPLIERUUID VARCHAR(32) NOT NULL,
	QTY DECIMAL(12,3) NOT NULL,
	CASEQTYSTR VARCHAR(30) NOT NULL,
	REALQTY DECIMAL(12,3) NOT NULL,
	REALCASEQTYSTR VARCHAR(30) NOT NULL,
	REASON VARCHAR(30) NULL,
	PRICE DECIMAL(12,3) NOT NULL,
	AMOUNT DECIMAL(12,3) NOT NULL
);
CREATE INDEX SARDINE_RETURNNTCBILLITEM_01 ON SARDINE_RETURNNTCBILLITEM(RETUARNBTCBILLUUID);

--退仓单表
CREATE TABLE SARDINE_RETURNBILL
(
	UUID	VARCHAR(32)	NOT NULL,
	BILLNUMBER	VARCHAR(30)	NOT NULL,
	STATE	VARCHAR(30)	NOT NULL,
	RETURNNTCBILLNUMBER	VARCHAR(30)	NOT NULL,
	WRHUUID	VARCHAR(32)	NOT NULL,
	COMPANYUUID	VARCHAR(30)	NOT NULL,
	CUSTOMERUUID	VARCHAR(32)	NOT NULL,
	CUSTOMERCODE	VARCHAR(30)	NOT NULL,
	CUSTOMERNAME	VARCHAR(100)	NOT NULL,
	RETURNORUUID	VARCHAR(32)	NOT NULL,
	RETURNORCODE	VARCHAR(30)	NOT NULL,
	RETURNORNAME	VARCHAR(100)	NOT NULL,
	TOTALCASEQTYSTR	VARCHAR(30)	NOT NULL,
	TOTALAMOUNT	DECIMAL(12,3)	NOT NULL,
	REMARK	VARCHAR(255)	NULL,
	VERSION INT(11) NOT NULL,
	CREATEDID VARCHAR(32) NOT NULL,
	CREATEDCODE VARCHAR(30) NOT NULL,
	CREATEDNAME VARCHAR(100) NOT NULL,
	LASTMODIFYID VARCHAR(32) NOT NULL,
	LASTMODIFYCODE VARCHAR(30) NOT NULL,
	LASTMODIFYNAME VARCHAR(100) NOT NULL,
	CREATEDTIME DATETIME NOT NULL,
	LASTMODIFYTIME DATETIME NOT NULL
);
CREATE INDEX SARDINE_RETURNBILL_01 ON SARDINE_RETURNBILL(BILLNUMBER,COMPANYUUID);


--退仓单明细表
CREATE TABLE SARDINE_RETURNBILLITEM
(
	UUID	VARCHAR(32)	NOT NULL,
	RETURNBILLUUID	VARCHAR(32)	NOT NULL,
	RETURNNTCBILLITEMUUID	VARCHAR(32)	NOT NULL,
	LINE	INT	NOT NULL,
	ARTICLEUUID	VARCHAR(32)	NOT NULL,
	MUNIT	VARCHAR(30)	NOT NULL,
	QPCSTR	VARCHAR(30)	NOT NULL,
	SUPPLIERUUID	VARCHAR(32)	NOT NULL,
	QTY	DECIMAL(12,3)	NOT NULL,
	CASEQTYSTR	VARCHAR(30)	NOT NULL,
	RETURNTYPE	VARCHAR(30)	NOT NULL,
	PRODUCTIONDATE	DATE	NOT NULL,
	VALIDDATE	DATE	NOT NULL,
	STOCKBATCH	VARCHAR(30)	NOT NULL,
	PRICE	DECIMAL(12,3)	NOT NULL,
	AMOUNT	DECIMAL(12,3)	NOT NULL,
	CONTAINERBARCODE VARCHAR(30) NOT NULL,
	BINCODE VARCHAR(30) NOT NULL
);
CREATE INDEX SARDINE_RETURNBILLITEM_01 ON SARDINE_RETURNBILLITEM(RETURNBILLUUID);
CREATE INDEX SARDINE_RETURNBILLITEM_02 ON SARDINE_RETURNBILLITEM(RETURNNTCBILLITEMUUID);