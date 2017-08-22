CREATE TABLE `sardine_acceptancebill` (
  `UUID` varchar(32)  NOT NULL,
  `BILLNUMBER` varchar(30)  NOT NULL,
  `STATE` varchar(100)  NOT NULL,
  `acceptanceReason` varchar(100)  NOT NULL,
  `DELIVERYSYSTEM` varchar(100)  NOT NULL,
  `DELIVERYTYPE` varchar(100)  NOT NULL,
  `SOURCEBILLNUMBER` varchar(30)  NOT NULL,
  `SOURCEBILLTYPE` varchar(100)  NOT NULL,
  `WRHUUID` varchar(32)  NOT NULL,
  `COMPANYUUID` varchar(32)  NOT NULL,
  `remark` varchar(255)  DEFAULT NULL,
  `VERSION` int(11) NOT NULL,
  `CUSTOMERUUID` varchar(32)  NOT NULL,
  `CREATEDID` varchar(32)  NOT NULL,
  `CREATEDCODE` varchar(30)  NOT NULL,
  `CREATEDNAME` varchar(100)  NOT NULL,
  `LASTMODIFYID` varchar(32)  NOT NULL,
  `LASTMODIFYCODE` varchar(30)  NOT NULL,
  `LASTMODIFYNAME` varchar(100)  NOT NULL,
  `CREATEDTIME` datetime NOT NULL,
  `LASTMODIFYTIME` datetime NOT NULL,
  `totalCaseQtyStr` varchar(30) NOT NULL DEFAULT '0',
  `totalAlcCaseQtyStr` varchar(30) NOT NULL DEFAULT '0',
  `TOTALAMOUNT` decimal(18,5) NOT NULL DEFAULT '0.00000',
  `totalalcamount` decimal(18,5) NOT NULL DEFAULT '0.00000',
  PRIMARY KEY (`UUID`)
) ;

CREATE TABLE `sardine_acceptancebillitem` (
  `UUID` varchar(32)  NOT NULL,
  `ACCEPTANCEBILLUUID` varchar(32)  NOT NULL,
  `LINE` int(12) NOT NULL,
  `ARTICLEUUID` varchar(32)  NOT NULL,
  `QPCSTR` varchar(30)  NOT NULL,
  `MUNIT` varchar(30)  NOT NULL,
  `QTY` decimal(18,5) NOT NULL DEFAULT '0.00000',
  `CASEQTYSTR` varchar(30) NOT NULL DEFAULT '0',
  `REALQTY` decimal(18,5) DEFAULT '0.00000',
  `REALCASEQTYSTR` varchar(30) DEFAULT '0',
  `PRICE` decimal(18,5) NOT NULL DEFAULT '0.00000',
  `AMOUNT` decimal(12,3) NOT NULL,
  `REMARK` varchar(255)  DEFAULT NULL,
  `BINCODE` varchar(30)  NOT NULL,
  `CONTAINERBARCODE` varchar(30)  NOT NULL,
  `productionDate` datetime NOT NULL,
  `validDate` datetime NOT NULL,
  `supplieruuid` varchar(32)  NOT NULL,
  `planqty` decimal(18,5) NOT NULL DEFAULT '0.00000',
  `plancaseqtystr` varchar(30) NOT NULL DEFAULT '0',
  PRIMARY KEY (`UUID`)
) ;

CREATE TABLE `sardine_alcntcbill` (
  `UUID` varchar(32) NOT NULL,
  `BILLNUMBER` varchar(30) NOT NULL,
  `STATE` varchar(100) NOT NULL,
  `DELIVERYREASON` varchar(100) NOT NULL,
  `DELIVERYSYSTEM` varchar(100) NOT NULL,
  `DELIVERYMODE` varchar(100) NOT NULL,
  `SOURCEBILLNUMBER` varchar(30) NOT NULL,
  `SOURCEBILLTYPE` varchar(100) NOT NULL,
  `WRHUUID` varchar(32) NOT NULL,
  `COMPANYUUID` varchar(32) NOT NULL,
  `REAMRK` varchar(255) DEFAULT NULL,
  `VERSION` int(11) NOT NULL,
  `CUSTOMERUUID` varchar(32) NOT NULL,
  `CUSTOMERCODE` varchar(30) NOT NULL,
  `CUSTOMERNAME` varchar(100) NOT NULL,
  `TASKBILLNUMBER` varchar(30) DEFAULT NULL,
  `TOTALCASEQTYSTR` varchar(30) NOT NULL,
  `TOTALAMOUNT` decimal(12,3) NOT NULL,
  `PLANTOTALCASEQTYSTR` varchar(30) DEFAULT NULL,
  `PLANTOTALAMOUNT` decimal(12,3) DEFAULT NULL,
  `REALTOTALCASEQTYSTR` varchar(30) DEFAULT NULL,
  `REALTOTALAMOUNT` decimal(12,3) DEFAULT NULL,
  `CREATEDID` varchar(32) NOT NULL,
  `CREATEDCODE` varchar(30) NOT NULL,
  `CREATEDNAME` varchar(100) NOT NULL,
  `LASTMODIFYID` varchar(32) NOT NULL,
  `LASTMODIFYCODE` varchar(30) NOT NULL,
  `LASTMODIFYNAME` varchar(100) NOT NULL,
  `CREATEDTIME` datetime NOT NULL,
  `LASTMODIFYTIME` datetime NOT NULL,
  PRIMARY KEY (`UUID`),
  UNIQUE KEY `IDX_SARDINE_ALCNTCBILL_01` (`BILLNUMBER`,`COMPANYUUID`)
) ;

CREATE TABLE `sardine_alcntcbillitem` (
  `UUID` varchar(32) NOT NULL,
  `ALCNTCBILLUUID` varchar(32) NOT NULL,
  `LINE` int(12) NOT NULL,
  `ARTICLEUUID` varchar(32) NOT NULL,
  `QPCSTR` varchar(30) NOT NULL,
  `MUNIT` varchar(30) NOT NULL,
  `QTY` decimal(12,3) NOT NULL,
  `CASEQTYSTR` varchar(30) NOT NULL,
  `PLANQTY` decimal(12,3) DEFAULT NULL,
  `PLANCASEQTYSTR` varchar(30) DEFAULT NULL,
  `REALQTY` decimal(12,3) DEFAULT NULL,
  `REALCASEQTYSTR` varchar(30) DEFAULT NULL,
  `PRICE` decimal(12,3) NOT NULL,
  `AMOUNT` decimal(12,3) NOT NULL,
  `REMARK` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`UUID`),
  KEY `IDX_SARDINE_ALCNTCBILLITEM_01` (`ALCNTCBILLUUID`) USING BTREE
) ;

CREATE TABLE `sardine_article` (
  `uuid` varchar(30)  NOT NULL,
  `code` varchar(30)  NOT NULL,
  `name` varchar(100)  NOT NULL,
  `spec` varchar(30)  NOT NULL,
  `state` varchar(30)  NOT NULL,
  `expDays` int(11) NOT NULL,
  `companyUuid` varchar(30)  NOT NULL,
  `categoryuuid` varchar(32)  NOT NULL,
  `categorycode` varchar(30)  NOT NULL,
  `categoryname` varchar(30)  NOT NULL,
  `firstInFirstOut` char(1)  DEFAULT '0',
  `version` int(11) DEFAULT '0',
  `createdtime` datetime NOT NULL,
  `createdID` varchar(32)  NOT NULL,
  `createdcode` varchar(30)  NOT NULL,
  `createdName` varchar(100)  NOT NULL,
  `lastModifytime` datetime NOT NULL,
  `lastModifyid` varchar(32)  NOT NULL,
  `lastModifycode` varchar(30)  NOT NULL,
  `lastModifyname` varchar(100)  NOT NULL,
  `remark` varchar(255)  DEFAULT NULL,
  `expflag` varchar(30)  NOT NULL,
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `sardine_article_code_idx` (`code`,`companyUuid`)
)  ;

CREATE TABLE `sardine_article_barcode` (
  `uuid` varchar(32)  NOT NULL,
  `articleuuid` varchar(30)  NOT NULL,
  `qpcstr` varchar(30)  NOT NULL,
  `barcode` varchar(30)  NOT NULL,
  PRIMARY KEY (`uuid`)
)  ;

CREATE TABLE `sardine_article_config` (
  `articleUuid` varchar(32)  NOT NULL,
  `companyUuid` varchar(30)  NOT NULL,
  `putawayBin` varchar(30)  DEFAULT NULL,
  `storagearea` varchar(30)  DEFAULT NULL,
  `fixedPickBin` varchar(30)  DEFAULT NULL,
  `pickUpQpcStr` varchar(30)  DEFAULT NULL,
  `lowqty` decimal(12,3) DEFAULT NULL,
  `highqty` decimal(12,3) DEFAULT NULL,
  `version` int(11) DEFAULT '0',
  PRIMARY KEY (`articleUuid`,`companyUuid`)
)  ;

CREATE TABLE `sardine_article_fixedpickbin` (
  `fixedPickBin` varchar(30)  NOT NULL,
  `articleUuid` varchar(30)  NOT NULL,
  `companyUuid` varchar(30)  NOT NULL,
  PRIMARY KEY (`articleUuid`,`companyUuid`)
) ;

CREATE TABLE `sardine_article_qpc` (
  `uuid` varchar(32)  NOT NULL,
  `qpcstr` varchar(30)  NOT NULL,
  `articleuuid` varchar(30)  NOT NULL,
  `length` decimal(12,3) DEFAULT '0.000',
  `width` decimal(12,3) DEFAULT '0.000',
  `height` decimal(12,3) DEFAULT '0.000',
  `weight` decimal(12,3) DEFAULT '0.000',
  `default_` char(1)  DEFAULT '0',
  `munit` varchar(100)  NOT NULL DEFAULT '-',
  PRIMARY KEY (`uuid`),
  KEY `sardine_article_qpcStr_idx01` (`qpcstr`),
  KEY `sardine_article_qpcStr_idx02` (`articleuuid`),
  KEY `sardine_article_supplier_idx01` (`articleuuid`),
  KEY `sardine_article_barcode_idx01` (`articleuuid`)
)  ;

CREATE TABLE `sardine_article_supplier` (
  `uuid` varchar(32)  NOT NULL,
  `articleuuid` varchar(30)  NOT NULL,
  `supplieruuid` varchar(32)  NOT NULL,
  `suppliercode` varchar(30)  NOT NULL,
  `suppliername` varchar(100)  NOT NULL,
  `default_` char(1)  DEFAULT '0',
  PRIMARY KEY (`uuid`)
)  ;

CREATE TABLE `sardine_bin` (
  `uuid` varchar(32)  NOT NULL,
  `code` varchar(30)  NOT NULL,
  `bintypeuuid` varchar(32)  NOT NULL,
  `bintypecode` varchar(30)  NOT NULL,
  `bintypename` varchar(100)  NOT NULL,
  `wrhuuid` varchar(32)  NOT NULL,
  `wrhcode` varchar(30)  NOT NULL,
  `wrhname` varchar(100)  NOT NULL,
  `shelfuuid` varchar(32)  NOT NULL,
  `binusage` varchar(30)  NOT NULL,
  `binLevel` varchar(30)  NOT NULL,
  `binColumn` varchar(30)  NOT NULL,
  `state` varchar(30)  NOT NULL,
  `version` int(11) DEFAULT '0',
  `companyuuid` varchar(30)  NOT NULL,
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `idx_sardine_bin_01` (`code`,`companyuuid`),
  KEY `idx_sardine_bin_02` (`wrhuuid`),
  KEY `idx_sardine_bin_03` (`bintypeuuid`),
  KEY `idx_sardine_bin_04` (`shelfuuid`),
  KEY `idx_sardine_bin_05` (`binusage`),
  KEY `idx_sardine_bin_06` (`state`)
)  ;

CREATE TABLE `sardine_bintype` (
  `UUID` varchar(32)  NOT NULL,
  `CODE` varchar(30)  NOT NULL,
  `NAME` varchar(100)  NOT NULL,
  `LENGTH` decimal(12,3) DEFAULT NULL,
  `WIDTH` decimal(12,3) DEFAULT NULL,
  `HEIGHT` decimal(12,3) DEFAULT NULL,
  `PLOTRATIO` decimal(12,3) DEFAULT NULL,
  `BEARING` decimal(12,0) NOT NULL,
  `VERSION` int(11) NOT NULL,
  `CREATEDID` varchar(32)  NOT NULL,
  `CREATEDCODE` varchar(30)  NOT NULL,
  `CREATEDNAME` varchar(100)  NOT NULL,
  `LASTMODIFYID` varchar(32)  NOT NULL,
  `LASTMODIFYCODE` varchar(30)  NOT NULL,
  `LASTMODIFYNAME` varchar(100)  NOT NULL,
  `CREATEDTIME` datetime NOT NULL,
  `LASTMODIFYTIME` datetime NOT NULL,
  `companyuuid` varchar(30)  DEFAULT NULL,
  UNIQUE KEY `idx_sardine_bintype_01` (`UUID`,`CODE`)
)  ;

CREATE TABLE `sardine_carrier` (
  `UUID` varchar(32) NOT NULL,
  `CODE` varchar(30) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `ADDRESS` varchar(255) DEFAULT NULL,
  `CONTACTPHONE` varchar(30) DEFAULT NULL,
  `CONTACT` varchar(100) DEFAULT NULL,
  `COMPANYUUID` varchar(32) NOT NULL,
  `CREATEDID` varchar(32) NOT NULL,
  `CREATEDCODE` varchar(30) NOT NULL,
  `CREATEDNAME` varchar(100) NOT NULL,
  `LASTMODIFYID` varchar(32) NOT NULL,
  `LASTMODIFYCODE` varchar(30) NOT NULL,
  `LASTMODIFYNAME` varchar(100) NOT NULL,
  `CREATEDTIME` datetime NOT NULL,
  `LASTMODIFYTIME` datetime NOT NULL,
  `VERSION` int(11) NOT NULL,
  `STATE` varchar(100) NOT NULL,
  PRIMARY KEY (`UUID`),
  UNIQUE KEY `IDX_SARDINE_CARRIER_01` (`CODE`,`COMPANYUUID`)
) ;

CREATE TABLE `sardine_category` (
  `uuid` varchar(32)  NOT NULL,
  `code` varchar(32)  NOT NULL,
  `name` varchar(32)  NOT NULL,
  `companyUuid` varchar(32)  DEFAULT NULL,
  `uppercategory` varchar(32)  NOT NULL,
  `remark` varchar(255)  DEFAULT NULL,
  `version` int(11) DEFAULT '0',
  `createdtime` datetime NOT NULL,
  `createdID` varchar(32)  NOT NULL,
  `createdcode` varchar(30)  NOT NULL,
  `createdName` varchar(100)  NOT NULL,
  `lastModifytime` datetime NOT NULL,
  `lastModifyid` varchar(32)  NOT NULL,
  `lastModifycode` varchar(30)  NOT NULL,
  `lastModifyname` varchar(100)  NOT NULL
)  ;

CREATE TABLE `sardine_categorystoragearea_config` (
  `categoryuuid` varchar(32)  NOT NULL,
  `companyUuid` varchar(30)  NOT NULL,
  `storagearea` varchar(30)  DEFAULT NULL,
  `version` int(11) NOT NULL,
  PRIMARY KEY (`categoryuuid`,`companyUuid`)
) ;

CREATE TABLE `sardine_container` (
  `uuid` varchar(32)  DEFAULT NULL,
  `barcode` varchar(32)  DEFAULT NULL,
  `containerTypeUuid` varchar(32)  DEFAULT NULL,
  `containerTypeCode` varchar(32)  DEFAULT NULL,
  `containerTypeName` varchar(32)  DEFAULT NULL,
  `companyUuid` varchar(32)  DEFAULT NULL,
  `state` varchar(32)  DEFAULT NULL,
  `position` varchar(32)  NOT NULL,
  `toposition` varchar(32)  NOT NULL,
  `remark` varchar(255)  DEFAULT NULL,
  `version` int(11) DEFAULT '0',
  `createdtime` datetime NOT NULL,
  `createdID` varchar(32)  NOT NULL,
  `createdcode` varchar(30)  NOT NULL,
  `createdName` varchar(100)  NOT NULL,
  `lastModifytime` datetime NOT NULL,
  `lastModifyid` varchar(32)  NOT NULL,
  `lastModifycode` varchar(30)  NOT NULL,
  `lastModifyname` varchar(100)  NOT NULL
)  ;

CREATE TABLE `sardine_containertype` (
  `uuid` varchar(100)  NOT NULL,
  `code` varchar(30)  NOT NULL,
  `name` varchar(100)  NOT NULL,
  `barCodePrefix` varchar(32)  DEFAULT NULL,
  `barCodeLength` int(2) DEFAULT NULL,
  `inLength` decimal(12,3) DEFAULT NULL,
  `inWidth` decimal(12,3) DEFAULT NULL,
  `inHeight` decimal(12,3) DEFAULT NULL,
  `outLength` decimal(12,3) DEFAULT NULL,
  `outWidth` decimal(12,3) DEFAULT NULL,
  `outHeight` decimal(12,3) DEFAULT NULL,
  `weight` decimal(12,3) DEFAULT NULL,
  `bearingWeight` decimal(12,3) DEFAULT NULL,
  `isship` varchar(30)  DEFAULT NULL,
  `barCodeType` varchar(30)  DEFAULT NULL,
  `rate` decimal(8,5) DEFAULT NULL,
  `version` decimal(19,0) NOT NULL DEFAULT '0',
  `companyUuid` varchar(30)  NOT NULL,
  `createdtime` datetime NOT NULL,
  `createdID` varchar(32)  NOT NULL,
  `createdcode` varchar(30)  NOT NULL,
  `createdName` varchar(100)  NOT NULL,
  `lastModifytime` datetime NOT NULL,
  `lastModifyid` varchar(32)  NOT NULL,
  `lastModifycode` varchar(30)  NOT NULL,
  `lastModifyname` varchar(100)  NOT NULL,
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `idx_sardine_containertype_01` (`code`,`companyUuid`) USING BTREE
)  ;

CREATE TABLE `sardine_customer` (
  `UUID` varchar(32)  NOT NULL,
  `CODE` varchar(30)  NOT NULL,
  `NAME` varchar(100)  NOT NULL,
  `CUSTOMERTYPE` varchar(100)  NOT NULL,
  `PHONE` varchar(30)  DEFAULT NULL,
  `ADDRESS` varchar(100)  DEFAULT NULL,
  `STATE` varchar(100)  NOT NULL,
  `COMPANYUUID` varchar(30)  NOT NULL,
  `REMARK` varchar(255)  DEFAULT NULL,
  `VERSION` int(11) NOT NULL,
  `CREATEDID` varchar(32)  NOT NULL,
  `CREATEDCODE` varchar(30)  NOT NULL,
  `CREATEDNAME` varchar(100)  NOT NULL,
  `LASTMODIFYID` varchar(32)  NOT NULL,
  `LASTMODIFYCODE` varchar(30)  NOT NULL,
  `LASTMODIFYNAME` varchar(100)  NOT NULL,
  `CREATEDTIME` datetime NOT NULL,
  `LASTMODIFYTIME` datetime NOT NULL,
  UNIQUE KEY `idx_sardine_customer_01` (`UUID`,`CODE`)
)  ;

CREATE TABLE `sardine_decincinvbill` (
  `uuid` varchar(32)  NOT NULL,
  `billNumber` varchar(30)  NOT NULL,
  `type` varchar(30)  NOT NULL,
  `wrhuuid` varchar(32)  NOT NULL,
  `state` varchar(30)  NOT NULL,
  `companyUuid` varchar(30)  NOT NULL,
  `version` int(11) DEFAULT '0',
  `createdtime` datetime NOT NULL,
  `createdID` varchar(32)  NOT NULL,
  `createdcode` varchar(30)  NOT NULL,
  `createdName` varchar(100)  NOT NULL,
  `lastModifytime` datetime NOT NULL,
  `lastModifyid` varchar(32)  NOT NULL,
  `lastModifycode` varchar(30)  NOT NULL,
  `lastModifyname` varchar(100)  NOT NULL,
  `remark` varchar(255)  DEFAULT NULL,
  `operatoruuid` varchar(32)  NOT NULL,
  `operatorcode` varchar(30)  NOT NULL,
  `operatorname` varchar(100)  NOT NULL,
  `totalamount` decimal(12,3) NOT NULL,
  `totalcaseqtystr` varchar(30) NOT NULL,
  PRIMARY KEY (`uuid`)
) ;

CREATE TABLE `sardine_decincinvbillitem` (
  `uuid` varchar(32)  NOT NULL,
  `line` int(11) DEFAULT '1',
  `billBillUuid` varchar(32)  NOT NULL,
  `articleuuid` varchar(32)  NOT NULL,
  `qpcStr` varchar(30)  NOT NULL,
  `measureUnit` varchar(30)  NOT NULL,
  `qty` decimal(12,3) NOT NULL,
  `caseQtyStr` varchar(30)  NOT NULL,
  `supplieruuid` varchar(32)  NOT NULL,
  `containerBarcode` varchar(30)  DEFAULT NULL,
  `stockBatch` varchar(30)  NOT NULL,
  `productionDate` datetime NOT NULL,
  `expireDate` datetime NOT NULL,
  `reason` varchar(255)  DEFAULT NULL,
  `binCode` varchar(30)  NOT NULL,
  `price` decimal(12,3) NOT NULL,
  `amount` decimal(12,3) NOT NULL,
  PRIMARY KEY (`uuid`)
) ;

CREATE TABLE `sardine_entitylog` (
  `uuid` varchar(100)  NOT NULL,
  `OPERATEINFO` varchar(255)  NOT NULL,
  `TIME` datetime NOT NULL,
  `EVENT` varchar(100)  NOT NULL,
  `MESSAGE` varchar(4000)  NOT NULL,
  `SERVICECLASS` varchar(100)  NOT NULL,
  `SERVICECAPTION` varchar(100)  NOT NULL,
  `ENTITYUUID` varchar(100)  NOT NULL,
  `ENTITYCAPTION` varchar(255)  NOT NULL,
  PRIMARY KEY (`uuid`)
)  ;

CREATE TABLE `sardine_orderbill` (
  `uuid` varchar(32)  NOT NULL,
  `billNumber` varchar(30)  NOT NULL,
  `sourceBillNumber` varchar(30)  DEFAULT NULL,
  `billType` varchar(100)  DEFAULT NULL,
  `supplieruuid` varchar(32)  NOT NULL,
  `wrhuuid` varchar(32)  NOT NULL,
  `expireDate` datetime DEFAULT NULL,
  `bookedDate` datetime DEFAULT NULL,
  `state` varchar(30)  NOT NULL,
  `companyUuid` varchar(30)  NOT NULL,
  `totalCaseQtyStr` varchar(30)  NOT NULL,
  `totalReceivedCaseQtyStr` varchar(30)  DEFAULT NULL,
  `note` varchar(255)  DEFAULT NULL,
  `version` int(11) DEFAULT '0',
  `createdtime` datetime NOT NULL,
  `createdID` varchar(32)  NOT NULL,
  `createdcode` varchar(30)  NOT NULL,
  `createdName` varchar(100)  NOT NULL,
  `lastModifytime` datetime NOT NULL,
  `lastModifyid` varchar(32)  NOT NULL,
  `lastModifycode` varchar(30)  NOT NULL,
  `lastModifyname` varchar(100)  NOT NULL,
  `totalAmount` decimal(18,5) NOT NULL DEFAULT '0.00000',
  `totalReceiveAmount` decimal(18,5) NOT NULL DEFAULT '0.00000',
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `sardine_article_code_idx` (`billNumber`,`companyUuid`)
)  ;

CREATE TABLE `sardine_orderbillitem` (
  `uuid` varchar(32)  NOT NULL,
  `line` int(11) DEFAULT '1',
  `orderBillUuid` varchar(32)  NOT NULL,
  `articleuuid` varchar(32)  NOT NULL,
  `qpcStr` varchar(30)  NOT NULL,
  `munit` varchar(30)  NOT NULL,
  `qty` decimal(18,5) NOT NULL,
  `caseQtyStr` varchar(30)  NOT NULL,
  `receivedQty` decimal(18,5) DEFAULT '0.00000',
  `receivedCaseQtyStr` varchar(30)  DEFAULT '0',
  `price` decimal(18,5) DEFAULT '0.00000',
  PRIMARY KEY (`uuid`)
)  ;

CREATE TABLE `sardine_path` (
  `uuid` varchar(32)  NOT NULL,
  `code` varchar(30)  NOT NULL,
  `zoneuuid` varchar(32)  NOT NULL,
  `note` varchar(254)  DEFAULT NULL,
  `companyuuid` varchar(30)  NOT NULL,
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `idx_sardine_path_01` (`code`,`companyuuid`),
  KEY `sardine_path_zoneuuid_idx` (`zoneuuid`)
)  ;

CREATE TABLE `sardine_pickarea` (
  `UUID` varchar(32) NOT NULL,
  `CODE` varchar(30) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `BINSCOPE` varchar(100) NOT NULL,
  `STORAGEAREA` varchar(100) DEFAULT NULL,
  `PICKMODE` varchar(30) NOT NULL,
  `PICKVOLUME` decimal(12,3) NOT NULL,
  `RPLMODE` varchar(30) NOT NULL,
  `REMARK` varchar(255) DEFAULT NULL,
  `RPLQTYMODE` varchar(30) NOT NULL,
  `COMPANYUUID` varchar(32) NOT NULL,
  `CREATEDID` varchar(32) NOT NULL,
  `CREATEDCODE` varchar(30) NOT NULL,
  `CREATEDNAME` varchar(100) NOT NULL,
  `LASTMODIFYID` varchar(32) NOT NULL,
  `LASTMODIFYCODE` varchar(30) NOT NULL,
  `LASTMODIFYNAME` varchar(100) NOT NULL,
  `CREATEDTIME` datetime NOT NULL,
  `LASTMODIFYTIME` datetime NOT NULL,
  `VERSION` int(11) NOT NULL,
  PRIMARY KEY (`UUID`),
  KEY `IDX_SARDINE_PICKAREA_01` (`CODE`,`COMPANYUUID`)
) ;

CREATE TABLE `sardine_reason_config` (
  `reasonType` varchar(100)  NOT NULL,
  `reason` varchar(255)  NOT NULL,
  `companyUuid` varchar(32)  NOT NULL
) ;

CREATE TABLE `sardine_receivebill` (
  `uuid` varchar(32)  NOT NULL,
  `billNumber` varchar(30)  NOT NULL,
  `orderBillNumber` varchar(100)  NOT NULL,
  `receiveruuid` varchar(32)  NOT NULL,
  `receivercode` varchar(30)  NOT NULL,
  `receivername` varchar(30)  NOT NULL,
  `supplieruuid` varchar(32)  NOT NULL,
  `wrhuuid` varchar(32)  NOT NULL,
  `method` varchar(30)  NOT NULL,
  `state` varchar(30)  NOT NULL,
  `companyUuid` varchar(30)  NOT NULL,
  `caseQtyStr` varchar(30)  NOT NULL,
  `remark` varchar(255)  DEFAULT NULL,
  `version` int(11) DEFAULT '0',
  `createdtime` datetime NOT NULL,
  `createdID` varchar(32)  NOT NULL,
  `createdcode` varchar(30)  NOT NULL,
  `createdName` varchar(100)  NOT NULL,
  `lastModifytime` datetime NOT NULL,
  `lastModifyid` varchar(32)  NOT NULL,
  `lastModifycode` varchar(30)  NOT NULL,
  `lastModifyname` varchar(100)  NOT NULL,
  `totalAmount` decimal(18,5) NOT NULL,
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `sardine_article_code_idx` (`billNumber`,`companyUuid`)
)  ;

CREATE TABLE `sardine_receivebillitem` (
  `uuid` varchar(32)  NOT NULL,
  `line` int(11) DEFAULT '1',
  `receiveBillUuid` varchar(32)  NOT NULL,
  `orderBillLineUuid` varchar(32)  NOT NULL,
  `binCode` varchar(30)  NOT NULL,
  `articleuuid` varchar(32)  NOT NULL,
  `qpcStr` varchar(30)  NOT NULL,
  `munit` varchar(30)  NOT NULL,
  `qty` decimal(18,5) NOT NULL,
  `caseQtyStr` varchar(30)  NOT NULL,
  `produceDate` datetime NOT NULL,
  `validDate` datetime NOT NULL,
  `receiveDate` datetime DEFAULT NULL,
  `stockBatch` varchar(30)  NOT NULL,
  `containerBarcode` varchar(30)  NOT NULL,
  `articleSpec` varchar(30)  NOT NULL,
  `price` decimal(18,5) NOT NULL,
  PRIMARY KEY (`uuid`)
)  ;

CREATE TABLE `sardine_returnbill` (
  `UUID` varchar(32) NOT NULL,
  `BILLNUMBER` varchar(30) NOT NULL,
  `STATE` varchar(30) NOT NULL,
  `RETURNNTCBILLNUMBER` varchar(30) NOT NULL,
  `WRHUUID` varchar(32) NOT NULL,
  `COMPANYUUID` varchar(30) NOT NULL,
  `CUSTOMERUUID` varchar(32) NOT NULL,
  `CUSTOMERCODE` varchar(30) NOT NULL,
  `CUSTOMERNAME` varchar(100) NOT NULL,
  `RETURNORUUID` varchar(32) NOT NULL,
  `RETURNORCODE` varchar(30) NOT NULL,
  `RETURNORNAME` varchar(100) NOT NULL,
  `TOTALCASEQTYSTR` varchar(30) NOT NULL,
  `TOTALAMOUNT` decimal(12,3) NOT NULL,
  `REMARK` varchar(255) DEFAULT NULL,
  `VERSION` int(11) NOT NULL,
  `CREATEDID` varchar(32) NOT NULL,
  `CREATEDCODE` varchar(30) NOT NULL,
  `CREATEDNAME` varchar(100) NOT NULL,
  `LASTMODIFYID` varchar(32) NOT NULL,
  `LASTMODIFYCODE` varchar(30) NOT NULL,
  `LASTMODIFYNAME` varchar(100) NOT NULL,
  `CREATEDTIME` datetime NOT NULL,
  `LASTMODIFYTIME` datetime NOT NULL,
  KEY `SARDINE_RETURNBILL_01` (`BILLNUMBER`,`COMPANYUUID`)
) ;

CREATE TABLE `sardine_returnbillitem` (
  `UUID` varchar(32) NOT NULL,
  `RETURNBILLUUID` varchar(32) NOT NULL,
  `RETURNNTCBILLITEMUUID` varchar(32) NOT NULL,
  `LINE` int(11) NOT NULL,
  `ARTICLEUUID` varchar(32) NOT NULL,
  `MUNIT` varchar(30) NOT NULL,
  `QPCSTR` varchar(30) NOT NULL,
  `SUPPLIERUUID` varchar(32) NOT NULL,
  `QTY` decimal(12,3) NOT NULL,
  `CASEQTYSTR` varchar(30) NOT NULL,
  `RETURNTYPE` varchar(30) NOT NULL,
  `PRODUCTIONDATE` datetime NOT NULL,
  `VALIDDATE` datetime NOT NULL,
  `STOCKBATCH` varchar(30) NOT NULL,
  `PRICE` decimal(12,3) NOT NULL,
  `AMOUNT` decimal(12,3) NOT NULL,
  `CONTAINERBARCODE` varchar(30) NOT NULL,
  `BINCODE` varchar(30) NOT NULL,
  KEY `SARDINE_RETURNBILLITEM_01` (`RETURNBILLUUID`),
  KEY `SARDINE_RETURNBILLITEM_02` (`RETURNNTCBILLITEMUUID`)
) ;

CREATE TABLE `sardine_returnntcbill` (
  `UUID` varchar(32) NOT NULL,
  `BILLNUMBER` varchar(30) NOT NULL,
  `STATE` varchar(30) NOT NULL,
  `SOURCEBILLTYPE` varchar(100) DEFAULT NULL,
  `SOURCEBILLNUMBER` varchar(30) DEFAULT NULL,
  `WRHUUID` varchar(32) NOT NULL,
  `COMPANYUUID` varchar(32) NOT NULL,
  `CUSTOMERUUID` varchar(32) NOT NULL,
  `CUSTOMERCODE` varchar(30) NOT NULL,
  `CUSTOMERNAME` varchar(100) NOT NULL,
  `RETURNDATE` datetime NOT NULL,
  `TOTALCASEQTYSTR` varchar(30) NOT NULL,
  `TOTALRETURNEDCASEQTYSTR` varchar(30) NOT NULL,
  `TOTALAMOUNT` decimal(12,3) NOT NULL,
  `TOTALRETURNEDAMOUNT` decimal(12,3) NOT NULL,
  `REMARK` varchar(255) DEFAULT NULL,
  `VERSION` int(11) NOT NULL,
  `CREATEDID` varchar(32) NOT NULL,
  `CREATEDCODE` varchar(30) NOT NULL,
  `CREATEDNAME` varchar(100) NOT NULL,
  `LASTMODIFYID` varchar(32) NOT NULL,
  `LASTMODIFYCODE` varchar(30) NOT NULL,
  `LASTMODIFYNAME` varchar(100) NOT NULL,
  `CREATEDTIME` datetime NOT NULL,
  `LASTMODIFYTIME` datetime NOT NULL,
  PRIMARY KEY (`UUID`),
  KEY `IDX_SARDINE_RETURNNTCBILL_01` (`BILLNUMBER`,`COMPANYUUID`)
) ;

CREATE TABLE `sardine_returnntcbillitem` (
  `UUID` varchar(32) NOT NULL,
  `RETURNNTCBILLUUID` varchar(32) NOT NULL,
  `LINE` int(11) NOT NULL,
  `ARTICLEUUID` varchar(32) NOT NULL,
  `MUNIT` varchar(30) NOT NULL,
  `QPCSTR` varchar(30) NOT NULL,
  `SUPPLIERUUID` varchar(32) NOT NULL,
  `QTY` decimal(12,3) NOT NULL,
  `CASEQTYSTR` varchar(30) NOT NULL,
  `REALQTY` decimal(12,3) NOT NULL,
  `REALCASEQTYSTR` varchar(30) NOT NULL,
  `REASON` varchar(30) DEFAULT NULL,
  `PRICE` decimal(12,3) NOT NULL,
  `AMOUNT` decimal(12,3) NOT NULL,
  PRIMARY KEY (`UUID`),
  KEY `SARDINE_RETURNNTCBILLITEM_01` (`RETURNNTCBILLUUID`)
) ;

CREATE TABLE `sardine_returnsupplierbill` (
  `UUID` varchar(32)  NOT NULL,
  `BILLNUMBER` varchar(30)  NOT NULL,
  `VERSION` int(11) NOT NULL,
  `COMPANYUUID` varchar(32)  NOT NULL,
  `RTNSUPPLIERNTCBILLNUMBER` varchar(32)  NOT NULL,
  `SUPPLIERUUID` varchar(32)  NOT NULL,
  `SUPPLIERCODE` varchar(32)  NOT NULL,
  `SUPPLIERNAME` varchar(32)  NOT NULL,
  `METHOD` varchar(100)  NOT NULL,
  `STATE` varchar(100)  NOT NULL,
  `TOTALCASEQTY` varchar(30)  NOT NULL DEFAULT '0',
  `TOTALAMOUNT` decimal(18,5) NOT NULL DEFAULT '0.00000',
  `CREATEDID` varchar(32)  NOT NULL,
  `CREATEDCODE` varchar(30)  NOT NULL,
  `CREATEDNAME` varchar(100)  NOT NULL,
  `LASTMODIFYID` varchar(32)  NOT NULL,
  `LASTMODIFYCODE` varchar(30)  NOT NULL,
  `LASTMODIFYNAME` varchar(100)  NOT NULL,
  `CREATEDTIME` datetime NOT NULL,
  `LASTMODIFYTIME` datetime NOT NULL,
  `WRHUUID` varchar(32)  NOT NULL,
  `WRHCODE` varchar(32)  NOT NULL,
  `WRHNAME` varchar(32)  NOT NULL,
  `RETURNERUUID` varchar(32)  NOT NULL,
  `RETURNERCODE` varchar(32)  NOT NULL,
  `RETURNERNAME` varchar(32)  NOT NULL,
  `REMARK` varchar(255)  DEFAULT NULL,
  PRIMARY KEY (`UUID`)
) ;

CREATE TABLE `sardine_returnsupplierbillitem` (
  `UUID` varchar(32)  NOT NULL,
  `RETURNSUPPLIERBILLUUID` varchar(32)  NOT NULL,
  `ARTICLEUUID` varchar(32)  NOT NULL,
  `QPCSTR` varchar(30)  NOT NULL,
  `MUNIT` varchar(30)  NOT NULL,
  `BINCODE` varchar(30)  NOT NULL,
  `CONTAINERBARCODE` varchar(30)  NOT NULL,
  `PRODUCTIONDATE` datetime NOT NULL,
  `VALIDDATE` datetime NOT NULL,
  `QTY` decimal(18,5) NOT NULL,
  `CASEQTYSTR` varchar(30)  NOT NULL,
  `ARTICLECODE` varchar(32)  NOT NULL,
  `ARTICLENAME` varchar(32)  NOT NULL,
  `SPEC` varchar(30)  NOT NULL,
  `LINE` int(12) NOT NULL,
  `RETURNSUPPLIERDATE` datetime NOT NULL,
  `AMOUNT` decimal(18,5) NOT NULL,
  PRIMARY KEY (`UUID`)
) ;

CREATE TABLE `sardine_role` (
  `uuid` varchar(32)  NOT NULL,
  `code` varchar(30)  NOT NULL,
  `name` varchar(100)  NOT NULL,
  `companyUuid` varchar(30)  NOT NULL,
  `state` varchar(30)  NOT NULL,
  `version` int(11) DEFAULT '0',
  `createdtime` datetime NOT NULL,
  `createdID` varchar(32)  NOT NULL,
  `createdcode` varchar(30)  NOT NULL,
  `createdName` varchar(100)  NOT NULL,
  `lastModifytime` datetime NOT NULL,
  `lastModifyid` varchar(32)  NOT NULL,
  `lastModifycode` varchar(30)  NOT NULL,
  `lastModifyname` varchar(100)  NOT NULL,
  PRIMARY KEY (`uuid`)
)  ;

CREATE TABLE `sardine_role_resource` (
  `roleuuid` varchar(32)  NOT NULL,
  `resourceuuid` varchar(32)  NOT NULL,
  PRIMARY KEY (`roleuuid`,`resourceuuid`)
)  ;

CREATE TABLE `sardine_role_user` (
  `roleuuid` varchar(32)  NOT NULL,
  `useruuid` varchar(32)  NOT NULL,
  PRIMARY KEY (`roleuuid`,`useruuid`)
)  ;

CREATE TABLE `sardine_rtnsupplierntcbill` (
  `UUID` varchar(32) NOT NULL,
  `BILLNUMBER` varchar(30) NOT NULL,
  `STATE` varchar(30) NOT NULL,
  `WRHUUID` varchar(32) NOT NULL,
  `COMPANYUUID` varchar(30) NOT NULL,
  `SUPPLIERUUID` varchar(32) NOT NULL,
  `SUPPLIERCODE` varchar(30)  NOT NULL,
  `SUPPLIERNAME` varchar(100)  NOT NULL,
  `SOURCEBILLUUID` varchar(32) NOT NULL,
  `SOURCEBILLNUMBER` varchar(30) NOT NULL,
  `SOURCEBILLTYPE` varchar(30) NOT NULL,
  `RTNDATE` datetime NOT NULL,
  `TOTALCASEQTYSTR` varchar(30) NOT NULL,
  `TOTALAMOUNT` decimal(12,3) NOT NULL,
  `UNSHELVEDCASEQTYSTR` varchar(30) NOT NULL,
  `UNSHELVEDAMOUNT` decimal(12,3) NOT NULL,
  `REMARK` varchar(255) DEFAULT NULL,
  `VERSION` int(11) NOT NULL,
  `CREATEDID` varchar(32) NOT NULL,
  `CREATEDCODE` varchar(30) NOT NULL,
  `CREATEDNAME` varchar(100) NOT NULL,
  `LASTMODIFYID` varchar(32) NOT NULL,
  `LASTMODIFYCODE` varchar(30) NOT NULL,
  `LASTMODIFYNAME` varchar(100) NOT NULL,
  `CREATEDTIME` datetime NOT NULL,
  `LASTMODIFYTIME` datetime NOT NULL,
  KEY `SARDINE_RTNSUPPLIERNTCBILL_01` (`BILLNUMBER`,`COMPANYUUID`)
) ;

CREATE TABLE `sardine_rtnsupplierntcbillitem` (
  `UUID` varchar(32)  NOT NULL,
  `RTNSUPPLIERNTCBILLUUID` varchar(32)  NOT NULL,
  `LINE` int(11) NOT NULL,
  `ARTICLEUUID` varchar(32)  NOT NULL,
  `MUNIT` varchar(30)  NOT NULL,
  `QPCSTR` varchar(30)  NOT NULL,
  `QTY` decimal(12,3) NOT NULL,
  `CASEQTYSTR` varchar(30)  NOT NULL,
  `PRICE` decimal(12,3) NOT NULL,
  `AMOUNT` decimal(12,3) NOT NULL,
  `RTNREASON` varchar(100)  DEFAULT NULL,
  `UNSHELVEDCASEQTYSTR` varchar(30)  DEFAULT NULL,
  `UNSHELVEDQTY` decimal(12,3) DEFAULT NULL,
  KEY `SARDINE_RTNSUPPLIERNTCBILLITEM_01` (`RTNSUPPLIERNTCBILLUUID`),
  KEY `SARDINE_RTNSUPPLIERNTCBILLITEM_02` (`UUID`)
) ;

CREATE TABLE `sardine_serialarch` (
  `UUID` varchar(32) NOT NULL,
  `CODE` varchar(30) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `COMPANYUUID` varchar(32) NOT NULL,
  `CREATEDID` varchar(32) NOT NULL,
  `CREATEDCODE` varchar(30) NOT NULL,
  `CREATEDNAME` varchar(100) NOT NULL,
  `LASTMODIFYID` varchar(32) NOT NULL,
  `LASTMODIFYCODE` varchar(30) NOT NULL,
  `LASTMODIFYNAME` varchar(100) NOT NULL,
  `CREATEDTIME` datetime NOT NULL,
  `LASTMODIFYTIME` datetime NOT NULL,
  `VERSION` int(11) NOT NULL,
  PRIMARY KEY (`UUID`),
  KEY `IDX_SARDINE_SERIALARCH_01` (`CODE`,`COMPANYUUID`)
) ;

CREATE TABLE `sardine_serialarchline` (
  `UUID` varchar(32) NOT NULL,
  `SERIALARCHUUID` varchar(32) NOT NULL,
  `CODE` varchar(30) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `COMPANYUUID` varchar(32) NOT NULL,
  `CREATEDID` varchar(32) NOT NULL,
  `CREATEDCODE` varchar(30) NOT NULL,
  `CREATEDNAME` varchar(100) NOT NULL,
  `LASTMODIFYID` varchar(32) NOT NULL,
  `LASTMODIFYCODE` varchar(30) NOT NULL,
  `LASTMODIFYNAME` varchar(100) NOT NULL,
  `CREATEDTIME` datetime NOT NULL,
  `LASTMODIFYTIME` datetime NOT NULL,
  `VERSION` int(11) NOT NULL,
  PRIMARY KEY (`UUID`),
  KEY `IDX_SARDINE_SERIALARCHLINE_01` (`CODE`,`COMPANYUUID`),
  KEY `IDX_SARDINE_SERIALARCHLINE_02` (`SERIALARCHUUID`)
) ;

CREATE TABLE `sardine_serialarchlinecustomer` (
  `CUSTOMERUUID` varchar(32) NOT NULL,
  `LINEORDER` int(12) NOT NULL,
  `SERIALARCHLINEUUID` varchar(32) NOT NULL,
  PRIMARY KEY (`CUSTOMERUUID`,`SERIALARCHLINEUUID`),
  KEY `IDX_SARDINE_SERIALARCHLINECUSTOMER` (`SERIALARCHLINEUUID`)
) ;

CREATE TABLE `sardine_shelf` (
  `uuid` varchar(32)  NOT NULL,
  `code` varchar(30)  NOT NULL,
  `pathuuid` varchar(32)  NOT NULL,
  `note` varchar(254)  DEFAULT NULL,
  `companyuuid` varchar(30)  NOT NULL,
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `idx_sardine_shelf_01` (`code`,`companyuuid`),
  KEY `idx_sardine_shelf_02` (`pathuuid`)
)  ;

CREATE TABLE `sardine_shipbill` (
  `UUID` varchar(32)  NOT NULL,
  `BILLNUMBER` varchar(30)  NOT NULL,
  `VERSION` int(11) NOT NULL,
  `COMPANYUUID` varchar(32)  NOT NULL,
  `VEHICLENUM` varchar(32)  NOT NULL,
  `CARRIERUUID` varchar(32)  NOT NULL,
  `DRIVERUUID` varchar(32)  NOT NULL,
  `DRIVERCODE` varchar(32)  NOT NULL,
  `DRIVERNAME` varchar(32)  NOT NULL,
  `DELIVERYTYPE` varchar(100)  NOT NULL,
  `METHOD` varchar(100)  NOT NULL,
  `STATE` varchar(100)  NOT NULL,
  `TOTALCASEQTY` varchar(30)  NOT NULL DEFAULT '0',
  `TOTALVOLUME` decimal(18,5) NOT NULL DEFAULT '0.00000',
  `TOTALWEIGHT` decimal(18,5) NOT NULL DEFAULT '0.00000',
  `TOTALAMOUNT` decimal(18,5) NOT NULL DEFAULT '0.00000',
  `CREATEDID` varchar(32)  NOT NULL,
  `CREATEDCODE` varchar(30)  NOT NULL,
  `CREATEDNAME` varchar(100)  NOT NULL,
  `LASTMODIFYID` varchar(32)  NOT NULL,
  `LASTMODIFYCODE` varchar(30)  NOT NULL,
  `LASTMODIFYNAME` varchar(100)  NOT NULL,
  `CREATEDTIME` datetime NOT NULL,
  `LASTMODIFYTIME` datetime NOT NULL,
  `CARRIERCODE` varchar(32)  NOT NULL,
  `CARRIERNAME` varchar(32)  NOT NULL,
  PRIMARY KEY (`UUID`)
) ;

CREATE TABLE `sardine_shipbillcontainerstock` (
  `UUID` varchar(32)  NOT NULL,
  `SHIPBILLUUID` varchar(32)  NOT NULL,
  `ARTICLEUUID` varchar(32)  NOT NULL,
  `CUSTOMERUUID` varchar(32)  NOT NULL,
  `QPCSTR` varchar(30)  NOT NULL,
  `MUNIT` varchar(30)  NOT NULL,
  `SOURCEBILLUUID` varchar(32)  NOT NULL,
  `SOURCEBILLNUMBER` varchar(30)  NOT NULL,
  `SOURCEBILLTYPE` varchar(100)  NOT NULL,
  `BINCODE` varchar(30)  NOT NULL,
  `CONTAINERBARCODE` varchar(30)  NOT NULL,
  `PRODUCTIONDATE` datetime NOT NULL,
  `VALIDDATE` datetime NOT NULL,
  `QTY` decimal(18,5) NOT NULL,
  `CASEQTY` varchar(30)  NOT NULL,
  `SUPPLIERUUID` varchar(32)  NOT NULL,
  `ARTICLECODE` varchar(32)  NOT NULL,
  `ARTICLENAME` varchar(32)  NOT NULL,
  `CUSTOMERCODE` varchar(32)  NOT NULL,
  `CUSTOMERNAME` varchar(32)  NOT NULL,
  `SPEC` varchar(30)  NOT NULL,
  `SUPPLIERCODE` varchar(32)  NOT NULL,
  `SUPPLIERNAME` varchar(32)  NOT NULL,
  `LINE` int(12) NOT NULL,
  PRIMARY KEY (`UUID`)
) ;

CREATE TABLE `sardine_shipbillcustomeritem` (
  `UUID` varchar(32)  NOT NULL,
  `SHIPBILLUUID` varchar(32)  NOT NULL,
  `CUSTOMERUUID` varchar(32)  NOT NULL,
  `ORDERNO` int(12) NOT NULL,
  `TOTALCASEQTY` varchar(30)  NOT NULL DEFAULT '0',
  `TOTALVOLUME` decimal(18,5) NOT NULL DEFAULT '0.00000',
  `CONTAINERCOUNT` int(12) NOT NULL,
  `SHIPERUUID` varchar(32)  NOT NULL,
  `SHIPERCODE` varchar(30)  NOT NULL,
  `SHIPERNAME` varchar(100)  NOT NULL,
  `CUSTOMERCODE` varchar(32)  NOT NULL,
  `CUSTOMERNAME` varchar(32)  NOT NULL,
  `LINE` int(12) NOT NULL,
  PRIMARY KEY (`UUID`)
) ;

CREATE TABLE `sardine_stock` (
  `UUID` varchar(32)  NOT NULL,
  `OWNER` varchar(32)  NOT NULL DEFAULT '-',
  `companyuuid` varchar(32)  NOT NULL,
  `SUPPLIERUUID` varchar(32)  NOT NULL,
  `BINCODE` varchar(30)  NOT NULL,
  `CONTAINERBARCODE` varchar(30)  NOT NULL,
  `ARTICLEUUID` varchar(32)  NOT NULL,
  `STOCKBATCH` varchar(30)  NOT NULL,
  `PRODUCTIONDATE` date NOT NULL,
  `VALIDDATE` date NOT NULL,
  `BILLTYPE` varchar(30)  NOT NULL,
  `BILLNUMBER` varchar(32)  NOT NULL,
  `BILLUUID` varchar(32)  NOT NULL,
  `STATE` varchar(30)  NOT NULL,
  `QTY` decimal(18,5) NOT NULL,
  `QPCSTR` varchar(50)  NOT NULL,
  `MEASUREUNIT` varchar(50)  NOT NULL,
  `INSTOCKTIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `MODIFIED` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `OPERATEBILLTYPE` varchar(30)  NOT NULL,
  `OPERATEBILLNUMBER` varchar(32)  NOT NULL,
  `OPERATEBILLUUID` varchar(32)  NOT NULL,
  `version` mediumtext  NOT NULL,
  `price` decimal(18,5) NOT NULL DEFAULT '0.00000',
  `productionbatch` varchar(30)  DEFAULT NULL
)  ;

CREATE TABLE `sardine_stock_log` (
  `STOCKUUID` varchar(32) NOT NULL,
  `ARTICLEUUID` varchar(32) NOT NULL,
  `LOGTIME` datetime NOT NULL,
  `BEFOREQTY` decimal(18,5) NOT NULL,
  `QTY` decimal(18,5) NOT NULL,
  `AFTERQTY` decimal(18,5) NOT NULL,
  `BILLUUID` varchar(32) NOT NULL,
  `BILLNUMBER` varchar(32) NOT NULL,
  `BILLTYPE` varchar(32) NOT NULL,
  `OWNER` varchar(32) NOT NULL DEFAULT '-',
  `companyuuid` varchar(32) NOT NULL,
  `SUPPLIERUUID` varchar(32) NOT NULL,
  `BINCODE` varchar(30) NOT NULL,
  `CONTAINERBARCODE` varchar(30) NOT NULL,
  `STATE` varchar(30) NOT NULL,
  `QPCSTR` varchar(50) NOT NULL,
  `STOCKBATCH` varchar(32) DEFAULT NULL,
  `uuid` varchar(32) DEFAULT NULL,
  `productionbatch` varchar(30) DEFAULT NULL
) ;

CREATE TABLE `sardine_supplier` (
  `uuid` varchar(100)  NOT NULL,
  `code` varchar(30)  NOT NULL,
  `name` varchar(100)  NOT NULL,
  `address` varchar(100)  DEFAULT NULL,
  `phone` varchar(30)  DEFAULT NULL,
  `state` varchar(30)  NOT NULL,
  `companyUuid` varchar(32)  NOT NULL,
  `remark` varchar(255)  DEFAULT NULL,
  `version` int(11) DEFAULT '0',
  `createdtime` datetime NOT NULL,
  `createdID` varchar(32)  NOT NULL,
  `createdcode` varchar(30)  NOT NULL,
  `createdName` varchar(100)  NOT NULL,
  `lastModifytime` datetime NOT NULL,
  `lastModifyid` varchar(32)  NOT NULL,
  `lastModifycode` varchar(30)  NOT NULL,
  `lastModifyname` varchar(100)  NOT NULL,
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `sardine_supplier_code_idx` (`code`,`companyUuid`) USING BTREE
)  ;

CREATE TABLE `sardine_task` (
  `uuid` varchar(32)  NOT NULL,
  `taskNo` varchar(30)  NOT NULL,
  `taskType` varchar(30)  NOT NULL,
  `articleuuid` varchar(32)  NOT NULL,
  `state` varchar(30)  NOT NULL,
  `qpcStr` varchar(30)  NOT NULL,
  `qty` decimal(18,5) NOT NULL DEFAULT '0.00000',
  `caseQtyStr` varchar(30)  NOT NULL DEFAULT '0',
  `realQty` decimal(18,5) DEFAULT '0.00000',
  `realCaseQtyStr` varchar(30)  DEFAULT '0',
  `productionDate` date DEFAULT NULL,
  `validDate` date DEFAULT NULL,
  `stockBatch` varchar(30)  NOT NULL,
  `supplieruuid` varchar(32)  NOT NULL,
  `fromBinCode` varchar(30)  NOT NULL,
  `fromContainerBarcode` varchar(30)  NOT NULL DEFAULT '-',
  `toBinCode` varchar(30)  DEFAULT NULL,
  `toContainerBarcode` varchar(30)  DEFAULT NULL,
  `owner` varchar(30)  NOT NULL,
  `sourceBillType` varchar(30)  NOT NULL,
  `sourceBillNumber` varchar(30)  NOT NULL,
  `sourceBillUuid` varchar(32)  NOT NULL,
  `sourceBillLine` int(11) DEFAULT NULL,
  `creatoruuid` varchar(32)  NOT NULL,
  `creatorcode` varchar(30)  NOT NULL,
  `creatorname` varchar(30)  NOT NULL,
  `type` varchar(30)  DEFAULT NULL,
  `createTime` datetime NOT NULL,
  `beginOperateTime` datetime DEFAULT NULL,
  `endOperateTime` datetime DEFAULT NULL,
  `operatoruuid` varchar(32)  DEFAULT NULL,
  `operatorcode` varchar(30)  DEFAULT NULL,
  `operatorname` varchar(30)  DEFAULT NULL,
  `version` int(11) DEFAULT '0',
  `companyUuid` varchar(30)  NOT NULL,
  `sourcebilllineuuid` varchar(32)  NOT NULL DEFAULT '-',
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `idx_sardine_task_taskno` (`taskNo`,`companyUuid`)
)  ;

CREATE TABLE `sardine_taskarea_config` (
  `operatorUuid` varchar(32)  NOT NULL,
  `operatorCode` varchar(30)  NOT NULL,
  `operatorName` varchar(100)  NOT NULL,
  `taskArea` varchar(255)  DEFAULT NULL,
  `companyUuid` varchar(30)  NOT NULL,
  `version` int(11) NOT NULL,
  `uuid` varchar(32)  NOT NULL,
  PRIMARY KEY (`uuid`)
) ;

CREATE TABLE `sardine_test` (
  `uuid` varchar(100) DEFAULT NULL,
  `code` varchar(100) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL
) ;

CREATE TABLE `sardine_vehicle` (
  `UUID` varchar(32) NOT NULL,
  `CODE` varchar(30) NOT NULL,
  `VEHICLENO` varchar(30) NOT NULL,
  `STATE` varchar(30) NOT NULL,
  `VEHICLETYPEUUID` varchar(32) NOT NULL,
  `VEHICLETYPECODE` varchar(30) NOT NULL,
  `VEHICLETYPENAME` varchar(100) NOT NULL,
  `CARRIERUUID` varchar(32) NOT NULL,
  `CARRIERCODE` varchar(30) NOT NULL,
  `CARRIERNAME` varchar(100) NOT NULL,
  `COMPANYUUID` varchar(32) NOT NULL,
  `CREATEDID` varchar(32) NOT NULL,
  `CREATEDCODE` varchar(30) NOT NULL,
  `CREATEDNAME` varchar(100) NOT NULL,
  `LASTMODIFYID` varchar(32) NOT NULL,
  `LASTMODIFYCODE` varchar(30) NOT NULL,
  `LASTMODIFYNAME` varchar(100) NOT NULL,
  `CREATEDTIME` datetime NOT NULL,
  `LASTMODIFYTIME` datetime NOT NULL,
  `VERSION` int(11) NOT NULL,
  `REMARK` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`UUID`),
  KEY `IDX_SARDINE_VEHICLE_01` (`CODE`,`COMPANYUUID`),
  KEY `IDX_SARDINE_VEHICLE_02` (`VEHICLENO`,`COMPANYUUID`)
) ;

CREATE TABLE `sardine_vehicletype` (
  `UUID` varchar(32) NOT NULL,
  `CODE` varchar(30) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `BEARWEIGHT` decimal(12,3) NOT NULL,
  `WEIGHT` decimal(12,3) NOT NULL,
  `LENGTH` decimal(12,3) NOT NULL,
  `WIDTH` decimal(12,3) NOT NULL,
  `HEIGHT` decimal(12,3) NOT NULL,
  `VOLUME` decimal(12,3) NOT NULL,
  `BEARVOLUME` decimal(12,3) NOT NULL,
  `COMPANYUUID` varchar(32) NOT NULL,
  `CREATEDID` varchar(32) NOT NULL,
  `CREATEDCODE` varchar(30) NOT NULL,
  `CREATEDNAME` varchar(100) NOT NULL,
  `LASTMODIFYID` varchar(32) NOT NULL,
  `LASTMODIFYCODE` varchar(30) NOT NULL,
  `LASTMODIFYNAME` varchar(100) NOT NULL,
  `CREATEDTIME` datetime NOT NULL,
  `LASTMODIFYTIME` datetime NOT NULL,
  `VERSION` int(11) NOT NULL,
  PRIMARY KEY (`UUID`),
  UNIQUE KEY `IDX_SARDINE_VEHICLETYPE_01` (`CODE`,`COMPANYUUID`)
) ;

CREATE TABLE `sardine_wavebill` (
  `UUID` varchar(32) NOT NULL,
  `BILLNUMBER` varchar(30) NOT NULL,
  `SERIALARCHUUID` varchar(32) DEFAULT NULL,
  `SERIALARCHCODE` varchar(30) DEFAULT NULL,
  `SERIALARCHNAME` varchar(100) DEFAULT NULL,
  `WAVETYPE` varchar(100) NOT NULL,
  `REMARK` varchar(255) DEFAULT NULL,
  `STATE` varchar(100) NOT NULL,
  `COMPANYUUID` varchar(32) NOT NULL,
  `VERSION` int(11) NOT NULL,
  `CREATEDID` varchar(32) NOT NULL,
  `CREATEDCODE` varchar(30) NOT NULL,
  `CREATEDNAME` varchar(100) NOT NULL,
  `LASTMODIFYID` varchar(32) NOT NULL,
  `LASTMODIFYCODE` varchar(30) NOT NULL,
  `LASTMODIFYNAME` varchar(100) NOT NULL,
  `CREATEDTIME` datetime NOT NULL,
  `LASTMODIFYTIME` datetime NOT NULL,
  PRIMARY KEY (`UUID`),
  KEY `IDX_SARDINE_WAVEBILL_01` (`BILLNUMBER`,`COMPANYUUID`)
) ;

CREATE TABLE `sardine_wavebillitem` (
  `UUID` varchar(32) NOT NULL,
  `WAVEBILLUUID` varchar(32) NOT NULL,
  `LINE` int(12) NOT NULL,
  `ALCNTCBILLNUMBER` varchar(30) NOT NULL,
  `ALCNTCBILLSTATE` varchar(100) NOT NULL,
  `CUSTOMERUUID` varchar(32) NOT NULL,
  `CUSTOMERCODE` varchar(30) NOT NULL,
  `CUSTOMERNAME` varchar(100) NOT NULL,
  PRIMARY KEY (`UUID`),
  KEY `IDX_SARDINE_WAVEBILLITEM_01` (`WAVEBILLUUID`)
) ;

CREATE TABLE `sardine_wrh` (
  `uuid` varchar(32)  NOT NULL,
  `code` varchar(30)  NOT NULL,
  `name` varchar(100)  NOT NULL,
  `note` varchar(254)  DEFAULT NULL,
  `companyuuid` varchar(30)  NOT NULL,
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `idx_sardine_wrh_01` (`code`,`companyuuid`)
)  ;

CREATE TABLE `sardine_zone` (
  `uuid` varchar(32)  NOT NULL,
  `code` varchar(30)  NOT NULL,
  `name` varchar(100)  NOT NULL,
  `wrhuuid` varchar(32)  NOT NULL,
  `wrhcode` varchar(30)  NOT NULL,
  `wrhname` varchar(100)  NOT NULL,
  `note` varchar(254)  DEFAULT NULL,
  `companyuuid` varchar(30)  NOT NULL,
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `idx_sardine_zone_01` (`code`,`companyuuid`),
  KEY `idx_sardine_zone_02` (`wrhuuid`)
)  ;
