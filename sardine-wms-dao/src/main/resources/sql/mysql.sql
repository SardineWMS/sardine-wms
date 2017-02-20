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



