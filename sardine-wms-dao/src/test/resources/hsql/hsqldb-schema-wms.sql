CREATE TABLE SARDINE_USER
(
  UUID           VARCHAR(32) NOT NULL,
  CODE           VARCHAR(30) NOT NULL,
  NAME           VARCHAR(100) NOT NULL,
  COMPANYUUID    VARCHAR(30) NOT NULL,
  COMPANYCODE    VARCHAR(30) NOT NULL,
  COMPANYNAME    VARCHAR(100) NOT NULL,
  PASSWD         VARCHAR(30) NOT NULL,
  PHONE          VARCHAR(30) NOT NULL,
  ADMINISTRATOR  VARCHAR(30) NOT NULL,
  CREATEDTIME    DATETIME NOT NULL,
  CREATEDID      VARCHAR(30) NOT NULL,
  CREATEDCODE    VARCHAR(30) NOT NULL,
  CREATEDNAME    VARCHAR(30) NOT NULL,
  LASTMODIFYTIME DATETIME NOT NULL,
  LASTMODIFYID   VARCHAR(30) NOT NULL,
  LASTMODIFYCODE VARCHAR(30) NOT NULL,
  LASTMODIFYNAME VARCHAR(30) NOT NULL,
  VERSION        NUMERIC(19) NOT NULL,
  USERSTATE      VARCHAR(30) NOT NULL
);

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

CREATE TABLE sequence(  
  name varchar(50) NOT NULL,
  current_value INT NOT NULL,  
  increment INT NOT NULL,
  companyuuid varchar(32) not null
);

CREATE TABLE SARDINE_ROLE
(
  UUID           VARCHAR(32) NOT NULL,
  CODE           VARCHAR(30) NOT NULL,
  NAME           VARCHAR(100) NOT NULL,
  COMPANYUUID    VARCHAR(30) NOT NULL,
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

CREATE TABLE SARDINE_RESOURCE
(
  UUID           VARCHAR(32) NOT NULL,
  CODE           VARCHAR(30) NOT NULL,
  NAME           VARCHAR(100) NOT NULL,
  UPPERUUID    VARCHAR(30)  NULL,
  TYPE      VARCHAR(30) NOT NULL
);

CREATE TABLE SARDINE_ROLE_RESOURCE
(
  ROLEUUID       VARCHAR(32) NOT NULL,
  RESOURCEUUID   VARCHAR(30) NOT NULL,
);

CREATE TABLE SARDINE_USER_RESOURCE
(
  USERUUID       VARCHAR(32) NOT NULL,
  RESOURCEUUID   VARCHAR(30) NOT NULL,
);

CREATE TABLE SARDINE_ROLE_USER
(
  USERUUID   VARCHAR(32) NOT NULL,
  ROLEUUID   VARCHAR(30) NOT NULL,
);

