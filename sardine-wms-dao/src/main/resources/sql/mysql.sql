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