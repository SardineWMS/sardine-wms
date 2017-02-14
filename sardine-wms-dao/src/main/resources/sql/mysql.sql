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
			createdID varchar(30) not null,
			createdcode varchar(30) not null,
			createdName varchar(100) not null,
			lastModifytime datetime not null,
			lastModifyid varchar(30) not null,
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
			categoryuuid varchar(30) not null,
			categorycode varchar(30) not null,
			categoryname varchar(30) not null,
			firstInFirstOut char(1) default '0',
			version int default 0,
			createdtime datetime not null,
			createdID varchar(30) not null,
			createdcode varchar(30) not null,
			createdName varchar(100) not null,
			lastModifytime datetime not null,
			lastModifyid varchar(30) not null,
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
			note varchar(254) not null,
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
			note varchar(254) not null,
			companyuuid varchar(30) not null
);
create unique index idx_sardine_zone_01 on sardine_wms.sardine_zone (code, companyuuid);
create index idx_sardine_zone_02 on sardine_wms.sardine_zone (wrhuuid);

----货道表
create table sardine_path(
			uuid varchar(32) not null primary key,
			code varchar(30) not null,
			zoneuuid varchar(32) not null,
			note varchar(254) not null,
			companyuuid varchar(30) not null
);
create unique index idx_sardine_path_01 on sardine_wms.sardine_path (code, companyuuid);
create index idx_sardine_path_02 on sardine_wms.sardine_path (zoneuuid);

----货架表
create table sardine_shelf(
			uuid varchar(32) not null primary key,
			code varchar(30) not null,
		    pathuuid varchar(32) not null,
			note varchar(254) not null,
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