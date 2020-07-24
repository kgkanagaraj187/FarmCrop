-- - 13-01-2017
DROP TABLE IF EXISTS farmer_field;
CREATE TABLE farmer_field (
  ID bigint(20) NOT NULL AUTO_INCREMENT,
  NAME varchar(255) DEFAULT NULL,
  TYPE varchar(3) DEFAULT NULL,
  TYPE_NAME varchar(45) DEFAULT NULL,
  PARENT bigint(20) DEFAULT NULL,
  STATUS bigint(3) DEFAULT NULL,
  
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of farmer_field
-- ----------------------------
INSERT INTO farmer_field VALUES ('1', 'Cert Details', '2', 'farmer_info', null, '1');
INSERT INTO farmer_field VALUES ('2', 'Personal Details', '2', 'dateOfJoining', null, '1');
INSERT INTO farmer_field VALUES ('3', 'Contact Details', '2', 'contact_info', null, '1');
INSERT INTO farmer_field VALUES ('4', 'Farmer Family Details', '2', 'family_info', null, '1');
INSERT INTO farmer_field VALUES ('5', 'Assets Detail', '2', 'assets_info', null, '1');
INSERT INTO farmer_field VALUES ('6', 'Loan Detail', '2', 'loan_info', null, '1');
INSERT INTO farmer_field VALUES ('7', 'Dwelling Detail', '2', 'dwelling_info', null, '1');
INSERT INTO farmer_field VALUES ('8', 'Bank Details', '2', 'bank_info', null, '1');
INSERT INTO farmer_field VALUES ('9', 'Insurance Details', '2', 'insurance_info', null, '1');
INSERT INTO farmer_field VALUES ('10', 'House Hold Details', '2', 'houseHold_info', null, '1');
INSERT INTO farmer_field VALUES ('11', 'Ivestigator Deails', '2', 'investigator_info', null, '1');
INSERT INTO farmer_field VALUES ('12', 'Enroll Place', '1', 'farmer.enrollmentPlace', '1', '1');
INSERT INTO farmer_field VALUES ('13', 'Enroll Date', '1', 'dateOfJoining', '1', '1');
INSERT INTO farmer_field VALUES ('14', 'Is Certified Farmer', '1', 'farmer.isCertifiedFarmer', '1', '1');
INSERT INTO farmer_field VALUES ('15', 'ICS Name', '1', 'farmer.icsName', '1', '1');
INSERT INTO farmer_field VALUES ('16', 'ICS Code', '1', 'farmer.icsCode', '1', '1');
INSERT INTO farmer_field VALUES ('17', 'Cert Type', '1', 'farmer.certificationType', '1', '1');
INSERT INTO farmer_field VALUES ('18', 'Ics Unit No', '1', 'farmer.icsUnitNo', '1', '1');
INSERT INTO farmer_field VALUES ('19', 'Ics Tracenet', '1', 'farmer.icsTracenetRegNo', '1', '1');
INSERT INTO farmer_field VALUES ('20', 'Farmer Code by Ics', '1', 'farmer.farmerCodeByIcs', '1', '1');
INSERT INTO farmer_field VALUES ('21', 'Farmer Code Tracenet', '1', 'farmer.farmersCodeTracenet', '1', '1');
INSERT INTO farmer_field VALUES ('22', 'Beneficiary Scheme', '1', 'farmer.isBeneficiaryInGovScheme', '1', '1');
INSERT INTO farmer_field VALUES ('23', 'Scheme Name', '1', 'farmer.nameOfTheScheme', '1', '1');
INSERT INTO farmer_field VALUES ('24', 'Govt Dept', '1', 'farmer.govtDept', '1', '1');
INSERT INTO farmer_field VALUES ('25', 'First Name', '1', 'farmer.firstName', '2', '1');
INSERT INTO farmer_field VALUES ('26', 'Father Name', '1', 'farmer.lastName', '2', '1');
INSERT INTO farmer_field VALUES ('27', 'Sur name', '1', 'farmer.surName', '2', '1');
INSERT INTO farmer_field VALUES ('28', 'Gender', '1', 'farmer.gender', '2', '1');
INSERT INTO farmer_field VALUES ('29', 'DOB', '1', 'calendar', '2', '1');
INSERT INTO farmer_field VALUES ('30', 'Age', '1', 'age', '2', '1');
INSERT INTO farmer_field VALUES ('31', 'Farmer Photo', '1', 'farmerImage', '2', '1');
INSERT INTO farmer_field VALUES ('32', 'Education', '1', 'farmer.education', '2', '1');
INSERT INTO farmer_field VALUES ('33', 'Marital Status', '1', 'farmer.maritalSatus', '2', '1');
INSERT INTO farmer_field VALUES ('34', 'Acc Balance', '1', 'accBalance', '2', '1');
INSERT INTO farmer_field VALUES ('35', 'Id Proof', '1', 'farmer.idProof', '2', '1');
INSERT INTO farmer_field VALUES ('36', 'Farmer Code', '1', 'farmer.farmerCode', '2', '1');
INSERT INTO farmer_field VALUES ('37', 'Living Status', '1', 'farmer.personalStatus', '2', '1');
INSERT INTO farmer_field VALUES ('38', 'Farmer Category', '1', 'farmer.category', '2', '1');
INSERT INTO farmer_field VALUES ('39', 'Caste ', '1', 'farmer.casteName', '2', '1');
INSERT INTO farmer_field VALUES ('40', 'Social Categiry', '1', 'farmer.socialCategory', '2', '1');
INSERT INTO farmer_field VALUES ('41', 'Aadhar', '1', 'farmer.adhaarNo', '2', '1');
INSERT INTO farmer_field VALUES ('42', 'Religion', '1', 'farmer.religion', '2', '1');
INSERT INTO farmer_field VALUES ('43', 'Family Type', '1', 'farmer.typeOfFamily', '2', '1');
INSERT INTO farmer_field VALUES ('44', 'Household Wet', '1', 'farmer.houseHoldLandWet', '2', '1');
INSERT INTO farmer_field VALUES ('45', 'Household Dry', '1', 'farmer.houseHoldLandDry', '2', '1');
INSERT INTO farmer_field VALUES ('46', 'Farmer Code', '1', 'farmer.farmerCode', '2', '1');
INSERT INTO farmer_field VALUES ('47', 'Farmer Address', '1', 'farmer.address', '3', '1');
INSERT INTO farmer_field VALUES ('48', 'Mobile Number', '1', 'farmer.mobileNumber', '3', '1');
INSERT INTO farmer_field VALUES ('49', 'Email', '1', 'farmer.email', '3', '1');
INSERT INTO farmer_field VALUES ('50', ' Country', '1', 'selectedCountry', '3', '1');
INSERT INTO farmer_field VALUES ('51', 'selectedState', '1', 'selectedState', '3', '1');
INSERT INTO farmer_field VALUES ('52', 'District', '1', 'selectedLocality', '3', '1');
INSERT INTO farmer_field VALUES ('53', 'City', '1', 'selectedCity', '3', '1');
INSERT INTO farmer_field VALUES ('54', 'Grampanchayat', '1', 'selectedPanchayat', '3', '1');
INSERT INTO farmer_field VALUES ('55', 'Village', '1', 'selectedVillage', '3', '1');
INSERT INTO farmer_field VALUES ('56', 'Samithi', '1', 'selectedSamithi', '3', '1');
INSERT INTO farmer_field VALUES ('57', 'Sangham', '1', 'selectedSangham', '3', '1');
INSERT INTO farmer_field VALUES ('58', 'Group Position', '1', 'selectedGroupPosition', '3', '1');
INSERT INTO farmer_field VALUES ('59', 'FPO', '1', 'farmer.fpo', '3', '1');
INSERT INTO farmer_field VALUES ('60', 'Phone Number', '1', 'farmer.phoneNumber', '3', '1');
INSERT INTO farmer_field VALUES ('61', 'Status', '1', 'farmer.status', '3', '1');
INSERT INTO farmer_field VALUES ('62', 'No of family Members', '1', 'farmer.noOfFamilyMembers', '4', '1');
INSERT INTO farmer_field VALUES ('63', 'House Hold members', '1', 'farmer.noOfHouseHoldMem', '4', '1');
INSERT INTO farmer_field VALUES ('64', 'Head of Family', '1', 'farmer.headOfFamily', '4', '1');
INSERT INTO farmer_field VALUES ('65', 'Adult Cnt', '1', 'farmer.adultCountMale', '4', '1');
INSERT INTO farmer_field VALUES ('66', 'Child Cnt', '1', 'farmer.childCountMale', '4', '1');
INSERT INTO farmer_field VALUES ('67', 'School going child', '1', 'farmer.noOfSchoolChildMale', '4', '1');
INSERT INTO farmer_field VALUES ('68', 'Consumer Electronics', '1', 'farmer.consumerElectronics', '5', '1');
INSERT INTO farmer_field VALUES ('69', 'Vehicle ', '1', 'farmer.vehicle', '5', '1');
INSERT INTO farmer_field VALUES ('70', 'Cell', '1', 'farmer.cellPhone', '5', '1');
INSERT INTO farmer_field VALUES ('71', 'Loan Taken Last Year', '1', 'farmer.loanTakenLastYear', '6', '1');
INSERT INTO farmer_field VALUES ('72', 'Loan Taken From', '1', 'farmer.loanTakenFrom', '6', '1');
INSERT INTO farmer_field VALUES ('73', 'Loan Amount', '1', 'farmer.loanAmount', '6', '1');
INSERT INTO farmer_field VALUES ('74', 'Purpose ', '1', 'farmer.loanPupose', '6', '1');
INSERT INTO farmer_field VALUES ('75', 'Intrest Rate', '1', 'farmer.loanIntRate', '6', '1');
INSERT INTO farmer_field VALUES ('76', 'Interest Period', '1', 'farmer.loanIntPeriod', '6', '1');
INSERT INTO farmer_field VALUES ('77', 'Security', '1', 'farmer.loanSecurity', '6', '1');
INSERT INTO farmer_field VALUES ('78', 'Loan Replacement Date', '1', 'repaymentDate', '6', '1');
INSERT INTO farmer_field VALUES ('79', 'Loan Replacement Amount', '1', 'farmer.loanRepaymentAmount', '6', '1');
INSERT INTO farmer_field VALUES ('80', 'Housing Ownership', '1', 'farmer.farmerEconomy.housingOwnership', '7', '1');
INSERT INTO farmer_field VALUES ('81', 'Electrified Housse', '1', 'farmer.farmerEconomy.electrifiedHouse', '7', '1');
INSERT INTO farmer_field VALUES ('82', 'Housing Type', '1', 'farmer.farmerEconomy.housingType', '7', '1');
INSERT INTO farmer_field VALUES ('83', 'Drinking Water source', '1', 'farmer.farmerEconomy.drinkingWaterSource', '7', '1');
INSERT INTO farmer_field VALUES ('84', 'Cooking Fuel', '1', 'farmer.farmerEconomy.cookingFuel', '7', '1');
INSERT INTO farmer_field VALUES ('85', 'Toilet Available', '1', 'farmer.farmerEconomy.toiletAvailable', '7', '1');
INSERT INTO farmer_field VALUES ('86', 'Life Insurance', '1', 'farmer.lifeInsurance', '9', '1');
INSERT INTO farmer_field VALUES ('87', 'Life Insurance Amt', '1', 'farmer.lifeInsAmount', '9', '1');
INSERT INTO farmer_field VALUES ('88', 'Health Ins', '1', 'farmer.healthInsurance', '9', '1');
INSERT INTO farmer_field VALUES ('89', 'Health Ins Amt', '1', 'farmer.healthInsAmount', '9', '1');
INSERT INTO farmer_field VALUES ('90', 'Crop Ins', '1', 'farmer.isCropInsured', '9', '1');
INSERT INTO farmer_field VALUES ('91', 'Crop Insured', '1', 'farmer.farmerCropInsurance', '9', '1');
INSERT INTO farmer_field VALUES ('92', 'No of acres Insured', '1', 'farmer.acresInsured', '9', '1');
INSERT INTO farmer_field VALUES ('93', 'Primary Occupation', '1', 'farmer.houseOccupationPrimary', '10', '1');
INSERT INTO farmer_field VALUES ('94', 'Secondary Occupation', '1', 'farmer.houseOccupationSecondary', '10', '1');
INSERT INTO farmer_field VALUES ('95', 'Members in CBOs', '1', 'farmer.familyMember', '10', '1');
INSERT INTO farmer_field VALUES ('96', 'Member of GRS', '1', 'farmer.grsMember', '10', '1');
INSERT INTO farmer_field VALUES ('97', 'Share Capital 1000', '1', 'farmer.paidShareCapitial', '10', '1');
INSERT INTO farmer_field VALUES ('98', 'Investigator Opinion', '1', 'farmer.investigatorOpinion', '11', '1');
INSERT INTO farmer_field VALUES ('99', 'Investigate Date', '1', 'investigatorDate', '11', '1');
INSERT INTO farmer_field VALUES ('100', 'Investigator Name', '1', 'farmer.investigatorName', '11', '1');



-- -16 MAR 2017
-- -Report Menu Order Changes
-- -Run for All Tenant

UPDATE ese_menu SET ORD='1' WHERE (NAME='report.warehouse');
UPDATE ese_menu SET ORD='2' WHERE (NAME='report.distribution');
UPDATE ese_menu SET ORD='3' WHERE (NAME='report.procurementReport');
UPDATE ese_menu SET ORD='4' WHERE (NAME='report.mtnt.procurement');
UPDATE ese_menu SET ORD='5' WHERE (NAME='report.mtnr.procurement');
UPDATE ese_menu SET ORD='6' WHERE (NAME='report.periodicInspectionReport');
UPDATE ese_menu SET ORD='7' WHERE (NAME='report.farmer');
INSERT INTO ese_menu VALUES (NULL, 'report.fieldStaff', 'FieldStaff', 'javascript:void(0)', '8', '4', '0', '0', '0', '0', NULL);

UPDATE ese_menu SET ORD='9' WHERE (NAME='report.training');
UPDATE ese_menu SET ORD='10' WHERE (NAME='report.sensitizingReport');
UPDATE ese_menu SET ORD='11' WHERE (NAME='report.samithiAccessReport');
UPDATE ese_menu SET ORD='12' WHERE (NAME='report.farmerInspectionReport');
UPDATE ese_menu SET ORD='13' WHERE (NAME='report.failedInspectionReport');
UPDATE ese_menu SET ORD='14' WHERE (NAME='report.offlineReport');

DELETE FROM ese_role_menu WHERE (MENU_ID=(SELECT ID FROM ese_menu WHERE NAME='report.balanceSheet'));

SET @ids = (SELECT id FROM ese_menu WHERE NAME='report.fieldStaff');
UPDATE ese_menu  SET ORD='1',PARENT_ID=@ids WHERE NAME='report.fieldStaffStockReport' ;
UPDATE ese_menu  SET ORD='2',PARENT_ID=@ids WHERE NAME='report.fieldStaffBalanceReport' ;
UPDATE ese_menu  SET ORD='3',PARENT_ID=@ids WHERE NAME='report.fieldStaffAccessReport' ;
UPDATE ese_menu  SET ORD='4',PARENT_ID=@ids WHERE NAME='report.fieldStaffManagement' ;

SET @ids = (SELECT id FROM ese_menu WHERE NAME='report.farmer');
UPDATE ese_menu SET ORD='1' WHERE (NAME='report.farmerList');
UPDATE ese_menu SET ORD='2',PARENT_ID=@ids WHERE NAME='report.sowingReport' ;
UPDATE ese_menu SET ORD='3' WHERE (NAME='report.farmerCropReport');
UPDATE ese_menu SET ORD='4' WHERE (NAME='report.cultivationReport');
UPDATE ese_menu SET ORD='5' WHERE (NAME='report.cBRReport');
UPDATE ese_menu SET ORD='6' WHERE (NAME='report.farmerIncomeReport');
UPDATE ese_menu SET ORD='7',PARENT_ID=@ids WHERE NAME='report.farmerBalanceReport' ;

UPDATE ese_menu SET ORD='1' WHERE (NAME='report.warehouseStockEntryReport');
UPDATE ese_menu SET ORD='2' WHERE (NAME='report.warehouseStockReport');

SET @ids = (SELECT id FROM ese_menu WHERE NAME='report.procurementReport');
UPDATE ese_menu SET ORD='4',PARENT_ID=@ids WHERE NAME='report.procurementStock' ;
UPDATE ese_menu SET ORD='2' WHERE (NAME='report.cropHarvestReport');
UPDATE ese_menu SET ORD='3' WHERE (NAME='report.cropSaleReport');
UPDATE ese_menu SET ORD='1',PARENT_ID=@ids WHERE NAME='report.procurement' ;


INSERT INTO ese_role_menu VALUES (NULL, (SELECT MAX(id) FROM ese_menu), '1');
INSERT INTO ese_role_menu VALUES (NULL, (SELECT MAX(id) FROM ese_menu), '4');
INSERT INTO ese_role_menu VALUES (NULL, (SELECT MAX(id) FROM ese_menu), '3');

DELETE FROM ese_role_menu WHERE (MENU_ID=(SELECT ID FROM ese_menu WHERE NAME='report.balanceSheet'));



-- ----------------------------
-- Table structure for farm_field
-- ----------------------------
DROP TABLE IF EXISTS farm_field;
CREATE TABLE farm_field (
  ID bigint(20) NOT NULL AUTO_INCREMENT,
  NAME varchar(255) DEFAULT NULL,
  TYPE varchar(3) DEFAULT NULL,
  TYPE_NAME varchar(255) DEFAULT NULL,
  PARENT bigint(20) DEFAULT NULL,
  STATUS bigint(3) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of farm_field
-- ----------------------------
INSERT INTO farm_field VALUES ('1', 'Farm Info', '1', 'farmInfo', null, '1');
INSERT INTO farm_field VALUES ('2', 'Contact Info', '1', 'contactInfo', null, '1');
INSERT INTO farm_field VALUES ('3', 'Soil And Irrigation ', '1', 'soilIrrigationInfo', null, '1');
INSERT INTO farm_field VALUES ('4', 'Farm Labour ', '1', 'farmLabourInfo', null, '1');
INSERT INTO farm_field VALUES ('5', 'Conversion Information ', '1', 'conversionInfo', null, '1');
INSERT INTO farm_field VALUES ('6', 'Conversion Status', '1', 'conversionStatus', null, '1');
INSERT INTO farm_field VALUES ('7', 'GFS Details', '1', 'gfsDetails', null, '1');
INSERT INTO farm_field VALUES ('8', 'Integrated Farming System', '1', 'ifsInfo', null, '1');
INSERT INTO farm_field VALUES ('9', 'Soil testing', '1', 'soilTest', null, '1');
INSERT INTO farm_field VALUES ('10', 'Farm Name', '1', 'farm.farmName', '1', '1');
INSERT INTO farm_field VALUES ('11', 'Survey Number', '1', 'farm.farmDetailedInfo.surveyNumber', '1', '1');
INSERT INTO farm_field VALUES ('12', 'Total Land', '1', 'farm.farmDetailedInfo.totalLandHolding', '1', '1');
INSERT INTO farm_field VALUES ('13', 'Proposed Land', '1', 'farm.farmDetailedInfo.proposedPlantingArea', '1', '1');
INSERT INTO farm_field VALUES ('14', 'Total Land Hectare Calc', '1', 'farm.totalLandHectare', '1', '1');
INSERT INTO farm_field VALUES ('15', 'Total Proposed Hectare Calc', '1', 'farm.proposedPlantingArea', '1', '1');
INSERT INTO farm_field VALUES ('16', 'Farm Owned', '1', 'selectedFarmOwned', '1', '1');
INSERT INTO farm_field VALUES ('17', 'Land Gradient', '1', 'selectedGradient', '1', '1');
INSERT INTO farm_field VALUES ('18', 'Same Address as Farmer', '1', 'farm.farmDetailedInfo.sameAddressofFarmer', '1', '1');
INSERT INTO farm_field VALUES ('19', 'Farm Address', '1', 'farm.farmDetailedInfo.farmAddress', '1', '1');
INSERT INTO farm_field VALUES ('20', 'Approach Road', '1', 'selectedRoad', '1', '1');
INSERT INTO farm_field VALUES ('21', 'Certification Year', '1', 'farm.certYear', '1', '1');
INSERT INTO farm_field VALUES ('22', 'Farm Photo', '1', 'farm.farmImageFileName', '1', '1');
INSERT INTO farm_field VALUES ('23', 'Farm Reg Number', '1', 'farm.farmRegNumber', '1', '1');
INSERT INTO farm_field VALUES ('24', 'Reg Year', '1', 'farm.regYear', '1', '1');
INSERT INTO farm_field VALUES ('25', 'Topology', '1', 'farm.landTopology', '1', '1');
INSERT INTO farm_field VALUES ('26', 'District', '1', 'selectedLocality', '2', '1');
INSERT INTO farm_field VALUES ('27', 'City', '1', 'selectedCity', '2', '1');
INSERT INTO farm_field VALUES ('28', 'panchayat.name', '1', 'selectedPanchayat', '2', '1');
INSERT INTO farm_field VALUES ('29', 'profile.samithi', '1', 'selectedSamithi', '2', '1');
INSERT INTO farm_field VALUES ('30', 'fpoGroup', '1', 'selectedFpo', '2', '1');
INSERT INTO farm_field VALUES ('31', 'farm.soilType', '1', 'selectedSoilType', '3', '1');
INSERT INTO farm_field VALUES ('32', 'farm.soilTexture', '1', 'selectedTexture', '3', '1');
INSERT INTO farm_field VALUES ('33', 'farm.soilFertilityStatus', '1', 'selectedSoilFertility', '3', '1');
INSERT INTO farm_field VALUES ('34', 'farm.farmIrrigationSource', '1', 'selectedIrrigation', '3', '1');
INSERT INTO farm_field VALUES ('35', 'Method of Irrigation', '1', 'selectedIrrigationSource', '3', '1');
INSERT INTO farm_field VALUES ('36', 'farm.farmDetailedInfo.boreWellRechargeStructure', '1', 'farm.farmDetailedInfo.boreWellRechargeStructure', '3', '1');
INSERT INTO farm_field VALUES ('37', 'Last Date of Chemical Applicatio', '1', 'farm.farmDetailedInfo.lastDateOfChemicalApplication', '4', '1');
INSERT INTO farm_field VALUES ('38', 'Conventional Lands ', '1', 'farm.farmDetailedInfo.conventionalLand', '4', '1');
INSERT INTO farm_field VALUES ('39', 'Fallow/Pasture Land ', '1', 'farm.farmDetailedInfo.fallowOrPastureLand', '4', '1');
INSERT INTO farm_field VALUES ('40', 'Conventional Crops ', '1', 'farm.farmDetailedInfo.conventionalCrops', '4', '1');
INSERT INTO farm_field VALUES ('41', 'Estimated Yield ', '1', 'farm.farmDetailedInfo.conventionalEstimatedYield', '4', '1');
INSERT INTO farm_field VALUES ('42', 'IFS Practices', '1', 'farm.ifs', '1', '1');
INSERT INTO farm_field VALUES ('43', 'Vegetables in Kitchen Garden ', '1', 'farm.vegetableName', '1', '1');
INSERT INTO farm_field VALUES ('44', 'Uses of Kitchen Garden ', '1', 'farm.kitchenGarden', '1', '1');
INSERT INTO farm_field VALUES ('45', 'Benefits from Backyard Poultry ', '1', 'farm.backYardPoultry', '1', '1');
INSERT INTO farm_field VALUES ('46', 'Soil Conservation Measures', '1', 'farm.soilConservation', '1', '1');
INSERT INTO farm_field VALUES ('47', 'Water Conservation Measures ', '1', 'farm.waterConservation', '1', '1');
INSERT INTO farm_field VALUES ('48', 'Farmer Service Centres ', '1', 'farm.serviceCentres', '1', '1');
INSERT INTO farm_field VALUES ('49', 'Farmer Field school', '1', 'farm.farmDetailedInfo.farmerFieldSchool', '1', '1');
INSERT INTO farm_field VALUES ('50', 'How did it benefited to you? ', '1', 'farm.farmDetailedInfo.isFFSBenifited', '1', '1');
INSERT INTO farm_field VALUES ('51', 'Trainings and Programs ', '1', 'farm.trainingProgram', '1', '1');
INSERT INTO farm_field VALUES ('52', 'Did you cultivated millets this year ? ', '1', 'farm.farmDetailedInfo.milletCultivated', '1', '1');


-- - 17-04-2017
-- - Report Mail Configuration Menu Script 

SET @ids = (SELECT id FROM ese_menu WHERE NAME='service');

INSERT INTO ese_menu VALUES (null, 'service.reportMailConfig', 'Report Mail Configuration', 'reportMailConfiguration_list.action', '15', @ids, '0', '0','0','0',null);

INSERT INTO ese_ent VALUES (null, 'service.reportMailConfig.list');
INSERT INTO ese_ent VALUES (null, 'service.reportMailConfig.create');
INSERT INTO ese_ent VALUES (null, 'service.reportMailConfig.update');
INSERT INTO ese_ent VALUES (null, 'service.reportMailConfig.delete');


INSERT INTO ese_role_ent VALUES ('1',(SELECT id FROM ese_ent WHERE NAME='service.reportMailConfig.list'));
INSERT INTO ese_role_ent VALUES ('1',(SELECT id FROM ese_ent WHERE NAME='service.reportMailConfig.create'));
INSERT INTO ese_role_ent VALUES ('1',(SELECT id FROM ese_ent WHERE NAME='service.reportMailConfig.update'));
INSERT INTO ese_role_ent VALUES ('1',(SELECT id FROM ese_ent WHERE NAME='service.reportMailConfig.delete'));

INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='service.reportMailConfig'), '1');
INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='service.reportMailConfig'), '2');
INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='service.reportMailConfig'), '3');
INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='service.reportMailConfig'), '4');

INSERT INTO ese_role_menu VALUES (null,(SELECT id FROM ese_menu WHERE NAME='service.reportMailConfig'), '1');

-- - 
ALTER TABLE procurement
ADD COLUMN BRANCH_ID  varchar(45) NULL AFTER LONGITUDE;

-- -20 APR 2017
-- - Runn all for tenant
SET @ids = (SELECT id FROM ese_menu WHERE NAME='service.productReturn');
INSERT INTO ese_menu VALUES (NULL, 'service.productReturn', 'Product Return', 'javascript:void(0)', '5', '3', '0', '0', '0', '0', NULL);
INSERT INTO ese_menu VALUES (null, 'service.productReturn.farmer', 'Farmer', 'productReturnFromFarmer_list.action', '2',@ids, '0', '0','0','0',null);
INSERT INTO ese_menu  VALUES (NULL, 'service.productReturn.fieldStaff', 'FieldStaff ', 'productReturnFromFieldStaff_list.action', '1',@ids,'0', '0', '0', '0', NULL);
INSERT INTO ese_role_menu VALUES (null,(SELECT id FROM ese_menu WHERE NAME='service.productReturn'), '1');

INSERT INTO ese_ent VALUES (null, 'service.productReturn.farmer.list');
INSERT INTO ese_ent VALUES (null, 'service.productReturn.farmer.create');
INSERT INTO ese_ent VALUES (null, 'service.productReturn.farmer.update');
INSERT INTO ese_ent VALUES (null, 'service.productReturn.farmer.delete');


INSERT INTO ese_role_ent VALUES ('1',(SELECT id FROM ese_ent WHERE NAME='service.productReturn.farmer.list'));
INSERT INTO ese_role_ent VALUES ('1',(SELECT id FROM ese_ent WHERE NAME='service.productReturn.farmer.create'));
INSERT INTO ese_role_ent VALUES ('1',(SELECT id FROM ese_ent WHERE NAME='service.productReturn.farmer.update'));
INSERT INTO ese_role_ent VALUES ('1',(SELECT id FROM ese_ent WHERE NAME='service.productReturn.farmer.delete'));

INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='service.productReturn.farmer'), '1');
INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='service.productReturn.farmer'), '2');
INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='service.productReturn.farmer'), '3');
INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='service.productReturn.farmer'), '4');

INSERT INTO ese_role_menu VALUES (null,(SELECT id FROM ese_menu WHERE NAME='service.productReturn.farmer'), '1');


INSERT INTO ese_ent VALUES (null, 'service.productReturn.fieldStaff.list');
INSERT INTO ese_ent VALUES (null, 'service.productReturn.fieldStaff.create');
INSERT INTO ese_ent VALUES (null, 'service.productReturn.fieldStaff.update');
INSERT INTO ese_ent VALUES (null, 'service.productReturn.fieldStaff.delete');


INSERT INTO ese_role_ent VALUES ('1',(SELECT id FROM ese_ent WHERE NAME='service.productReturn.fieldStaff.list'));
INSERT INTO ese_role_ent VALUES ('1',(SELECT id FROM ese_ent WHERE NAME='service.productReturn.fieldStaff.create'));
INSERT INTO ese_role_ent VALUES ('1',(SELECT id FROM ese_ent WHERE NAME='service.productReturn.fieldStaff.update'));
INSERT INTO ese_role_ent VALUES ('1',(SELECT id FROM ese_ent WHERE NAME='service.productReturn.fieldStaff.delete'));

INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='service.productReturn.fieldStaff'), '1');
INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='service.productReturn.fieldStaff'), '2');
INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='service.productReturn.fieldStaff'), '3');
INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='service.productReturn.fieldStaff'), '4');

INSERT INTO ese_role_menu VALUES (null,(SELECT id FROM ese_menu WHERE NAME='service.productReturn.fieldStaff'), '1');


INSERT INTO ese_seq VALUES (NULL, 'PRODUCT_RETURN_FROM_FIELDSTAFF_SEQ', '0');

-- -Product Return 
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for product_return
-- ----------------------------
DROP TABLE IF EXISTS product_return;
CREATE TABLE product_return (
  ID bigint(45) NOT NULL AUTO_INCREMENT,
  RECEIPT_NO varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  REFERENCE_NO varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  SERIAL_NO varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  VILLAGE_ID bigint(20) DEFAULT NULL,
  VILLAGE_NAME varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  TRXN_AGRO_ID bigint(20) DEFAULT NULL,
  IS_FREE_DISTRIBUTION varchar(2) COLLATE utf8_unicode_ci DEFAULT NULL,
  TOTAL_AMOUNT double(20,2) DEFAULT '0.00',
  TAX double(20,2) DEFAULT '0.00',
  FINAL_AMOUNT double(20,2) DEFAULT '0.00',
  PAYNMENT_MODE varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  SEASON_ID bigint(10) DEFAULT NULL,
  PAYMENT_AMT double(20,2) DEFAULT '0.00',
  MOBILE_NO varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  INT_BAL double(20,2) DEFAULT '0.00',
  TXN_AMT double(20,2) DEFAULT '0.00',
  BAL_AMT double(20,2) DEFAULT '0.00',
  AGENT_ID varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  AGENT_NAME varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  FARMER_ID varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  FARMER_NAME varchar(90) COLLATE utf8_unicode_ci DEFAULT NULL,
  SERVICE_POINT_ID varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  SERVICE_POINT_NAME text COLLATE utf8_unicode_ci,
  TXN_TIME datetime DEFAULT NULL,
  SAMITHI_NAME varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  TXN_TYPE varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  branch_id varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  season_code varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  STATUS int(2) DEFAULT '0',
  UPDATED_USER_NAME varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  UPDATED_TIME datetime DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for product_return_detail
-- ----------------------------
DROP TABLE IF EXISTS product_return_detail;
CREATE TABLE product_return_detail (
  ID bigint(45) NOT NULL AUTO_INCREMENT,
  PRODUCT_ID bigint(45) DEFAULT NULL,
  BATCH_NO varchar(20) COLLATE utf8_unicode_ci DEFAULT 'NA',
  QUANTITY double(20,3) DEFAULT NULL,
  UNIT varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRICE_PER_UNIT double(20,2) DEFAULT NULL,
  SUB_TOTAL double(20,2) DEFAULT NULL,
  PRODUCT_RETURN_ID bigint(45) DEFAULT NULL,
  SELLING_PRICE double(20,2) DEFAULT '0.00',
  EXISTING_QUANTITY double(20,3) DEFAULT '0.000',
  CURRENT_QUANTITY double(20,3) DEFAULT '0.000',
  COST_PRICE double(20,3) DEFAULT '0.000',
  PRIMARY KEY (ID),
  KEY PRODUCT_ID (PRODUCT_ID) USING BTREE,
  KEY PRODUCT_RETURN_ID (PRODUCT_RETURN_ID) USING BTREE,
  CONSTRAINT product_return_detail_ibfk_1 FOREIGN KEY (PRODUCT_RETURN_ID) REFERENCES product_return (ID) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT product_return_detail_ibfk_2 FOREIGN KEY (PRODUCT_ID) REFERENCES product (ID) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- -24 APR 2017
-- - Product Return Report Menu

SET @ids = (SELECT id FROM ese_menu WHERE NAME='report.productReturn');
INSERT INTO ese_menu VALUES (NULL, 'report.productReturn', 'Product Return', 'javascript:void(0)', '9', '4', '0', '0', '0', '0', NULL);

INSERT INTO ese_menu VALUES (NULL, 'report.productReturn.farmer', 'Farmer', 'productReturnReport_list.action?type=farmer', '1', @ids, '0', '1', '0', '0', NULL);
INSERT INTO ese_menu VALUES (NULL, 'report.productReturn.agent', 'Agent', 'productReturnReport_list.action?type=agent', '2', @ids, '0', '1', '0', '0', NULL);

INSERT INTO ese_ent VALUES (NULL, 'report.productReturn.farmer.list');
INSERT INTO ese_ent VALUES (NULL, 'report.productReturn.agent.list');


INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='report.productReturn.farmer'), '1');
INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='report.productReturn.agent'), '1');


INSERT INTO ese_role_ent (ROLE_ID, ENT_ID) VALUES ('1', (SELECT id FROM ese_ent WHERE NAME='report.productReturn.farmer.list'));
INSERT INTO ese_role_ent (ROLE_ID, ENT_ID) VALUES ('1', (SELECT id FROM ese_ent WHERE NAME='report.productReturn.agent.list'));

INSERT INTO ese_role_menu VALUES (NULL, (SELECT id FROM ese_menu WHERE NAME='report.productReturn'), '1');
INSERT INTO ese_role_menu VALUES (NULL,(SELECT id FROM ese_menu WHERE NAME='report.productReturn.agent'), '1');
INSERT INTO ese_role_menu VALUES (NULL, (SELECT id FROM ese_menu WHERE NAME='report.productReturn.farmer'), '1');


INSERT INTO locale_property (id, code, lang_code, lang_value) VALUES (null, 'farm.farmDetailedInfo.totalLandHolding', 'en', 'Total Land Holding(Acres)');


ALTER TABLE trxn_agro
ADD COLUMN PRODUCT_RETURN_ID  bigint(20) NULL AFTER BRANCH_ID;

-- - Global Balance Report

DROP view if EXISTS vw_global_report;

CREATE VIEW vw_global_report AS (
SELECT uuid_short() AS ID, txn.TXN_TIME,txn.FARMER_ID,txn.FARMER_NAME,txn.SEASON_CODE,
IFNULL(
(
SELECT d.FINAL_AMOUNT),0.00
) AS DIS_AMOUNT,
IFNULL(
( SELECT p.TOTAL_AMOUNT),0.00
)  AS PRO_AMOUNT
FROM (
				(
					trxn_agro txn
					LEFT JOIN distribution d ON (
						(
							d.TRXN_AGRO_ID = txn.ID
						)
					)
				)
				LEFT JOIN procurement p ON (
					(
						p.TRXN_AGRO_ID = txn.ID
					)
				)
			)

WHERE txn.FARMER_ID IS NOT NULL AND  txn.FARMER_ID <> "N/A" AND txn.TXN_TYPE IN(314,316)
);

-- - Global Balance Report Menu

INSERT INTO ese_menu VALUES (NULL , 'report.globalReport', 'Global Report', 'globalReport_list.action', '19','4','0','0','0','0','NULL');
INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='report.globalReport'), '1');
INSERT INTO ese_ent VALUES (NULL, 'report.globalReport.list');
INSERT INTO ese_role_ent VALUES ('1', (SELECT id FROM ese_ent WHERE NAME='report.globalReport.list'));
INSERT INTO ese_role_menu VALUES (NULL, (SELECT MAX(id) FROM ese_menu), '1');

-- - 04-25-2017 -- -
ALTER TABLE pmt
ADD COLUMN BRANCH_ID  varchar(50) NULL AFTER SEASON_CODE;

ALTER TABLE pmt_detail
ADD COLUMN BRANCH_ID  varchar(50) NULL AFTER FARMER_ID;

-- - 04-27-2017 
INSERT INTO locale_property (id, code, lang_code, lang_value) VALUES (null, 'prices', 'en', 'Price(INR)');


-- -27-04-2017
DROP TABLE IF EXISTS dynamic_report_config;
CREATE TABLE dynamic_report_config (
  ID bigint(20) NOT NULL AUTO_INCREMENT,
  REPORT varchar(100) DEFAULT '',
  FETCH_TYPE bigint(2) DEFAULT NULL,
  GRID_TYPE varchar(2) DEFAULT NULL,
  STATUS varchar(2) DEFAULT NULL,
  BRANCH_ID varchar(45) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS dynamic_report_config_detail;
CREATE TABLE dynamic_report_config_detail (
  ID bigint(20) NOT NULL AUTO_INCREMENT,
  REPORT_CONFIG_ID bigint(20) DEFAULT NULL,
  LABEL_NAME varchar(255) DEFAULT NULL,
  FIELD varchar(200) DEFAULT NULL,
  ACESS_TYPE int(2) DEFAULT NULL,
  METHOD varchar(200) DEFAULT NULL,
  GROUP_PROP bigint(2) DEFAULT NULL,
  SUM_PROP bigint(2) DEFAULT NULL,
  WIDTH double(5,0) DEFAULT NULL,
  ORDER int(2) DEFAULT NULL,
  IS_GRID_AVAILABILITY int(2) DEFAULT '1',
  IS_EXPORT_AVAILABILITY int(2) DEFAULT '1',
  STATUS bigint(2) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS dynamic_report_config_filter;
CREATE TABLE dynamic_report_config_filter (
  ID bigint(20) NOT NULL AUTO_INCREMENT,
  REPORT_CONFIG bigint(20) DEFAULT NULL,
  FIELD varchar(100) DEFAULT NULL,
  LABEL varchar(255) DEFAULT NULL,
  METHOD varchar(100) DEFAULT NULL,
  STATUS int(2) DEFAULT NULL,
  PRIMARY KEY (ID),
  KEY fk_report_config (REPORT_CONFIG),
  CONSTRAINT fk_report_config FOREIGN KEY (REPORT_CONFIG) REFERENCES dynamic_report_config (ID) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;

-- -03-05-2017
UPDATE farm_catalogue SET NAME='Unit' WHERE (CATALOGUE_TYPEZ='10');

---12-05-2017--
ALTER TABLE warehouse_payment_details ADD PARENT_CODE VARCHAR(30);
ALTER TABLE warehouse_payment_details ADD PACKETS_INFO VARCHAR(45);
ALTER TABLE warehouse_payment_details ADD SEED_TYPE VARCHAR(30);
ALTER TABLE warehouse_payment_details ADD UOM VARCHAR(30);
ALTER TABLE warehouse_payment_details ADD VARIETY VARCHAR(30);
  
ALTER TABLE warehouse_product_detail ADD PARENT_CODE VARCHAR(30);
ALTER TABLE warehouse_product_detail ADD PACKETS_INFO VARCHAR(45);
ALTER TABLE warehouse_product_detail ADD SEED_TYPE VARCHAR(30);
ALTER TABLE warehouse_product_detail ADD UOM VARCHAR(30);
ALTER TABLE warehouse_product_detail ADD VARIETY VARCHAR(30);

INSERT INTO locale_property (id, code, lang_code, lang_value) VALUES ('null', 'paymentInfo', 'en', 'Payment Amount');


-- -26-05-2017 - --
ALTER TABLE distribution_detail
ADD COLUMN SAGI_CODE  varchar(45) NULL AFTER PRODUCT_ID;

DROP TABLE IF EXISTS ptr;
CREATE TABLE ptr (
  ID bigint(20) NOT NULL AUTO_INCREMENT,
  SEASON_CODE varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  WAREHOUSE_ID bigint(20) DEFAULT NULL,
  DATE datetime DEFAULT NULL,
  RECEIPT_NO varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  TXN_TYPE varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  BRANCH_ID varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  REMARKS varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  LOCATION_ID bigint(20) DEFAULT NULL,
  PRIMARY KEY (ID),
  KEY FK_PTR_WAREHOUSE (WAREHOUSE_ID) USING BTREE,
  KEY FK_PTR_LOCATION (LOCATION_ID) USING BTREE,
  CONSTRAINT ptr_ibfk_1 FOREIGN KEY (WAREHOUSE_ID) REFERENCES warehouse (ID) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT ptr_ibfk_2 FOREIGN KEY (LOCATION_ID) REFERENCES warehouse (ID) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

DROP TABLE IF EXISTS ptr_detail;
CREATE TABLE ptr_detail (
  ID bigint(20) NOT NULL AUTO_INCREMENT,
  PTR_ID bigint(20) DEFAULT NULL,
  PRODUCT bigint(20) DEFAULT NULL,
  SAGI_CODE varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  LOT_NO varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  QUANTITY double(50,0) DEFAULT NULL,
  DAMAGED_QTY double(50,0) DEFAULT '0',
  PRIMARY KEY (ID),
  KEY FK_PTR_ID (PTR_ID) USING BTREE,
  CONSTRAINT ptr_detail_ibfk_1 FOREIGN KEY (PTR_ID) REFERENCES ptr (ID) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


-- - 31May2017

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for seeds_planting
-- ----------------------------
DROP TABLE IF EXISTS seeds_planting;
CREATE TABLE seeds_planting (
  ID bigint(20) NOT NULL AUTO_INCREMENT,
  SEASON_CODE varchar(15) DEFAULT NULL,
  CENTER_ID bigint(20) DEFAULT NULL,
  PRODUCT_ID bigint(20) DEFAULT NULL,
  SAGI_CODE varchar(20) DEFAULT NULL,
  LOT_NO varchar(20) DEFAULT NULL,
  VILLAGE_ID varchar(20) DEFAULT NULL,
  FARMER_NAME varchar(45) DEFAULT NULL,
  FATHER_NAME varchar(45) DEFAULT NULL,
  REMARKS text,
  PRIMARY KEY (ID),
  KEY center_id (CENTER_ID) USING BTREE,
  KEY prod_id (PRODUCT_ID) USING BTREE,
  KEY village_id (VILLAGE_ID) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for seeds_planting_details
-- ----------------------------
DROP TABLE IF EXISTS seeds_planting_details;
CREATE TABLE seeds_planting_details (
  ID bigint(20) NOT NULL AUTO_INCREMENT,
  TYPE varchar(10) DEFAULT NULL,
  SEED_TYPE varchar(10) DEFAULT NULL,
  LOT_NO varchar(30) DEFAULT NULL,
  QTY varchar(30) DEFAULT NULL,
  PLANTING_DATE datetime DEFAULT NULL,
  POPULATION varchar(40) DEFAULT NULL,
  TARGET_YIELD varchar(40) DEFAULT NULL,
  PLANTING_ID bigint(20) DEFAULT NULL,
  PRIMARY KEY (ID),
  KEY planting_id (PLANTING_ID) USING BTREE,
  CONSTRAINT ibfk_planting_id FOREIGN KEY (PLANTING_ID) REFERENCES seeds_planting (ID) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- - 01-06-2017
ALTER TABLE seeds_planting
ADD COLUMN BRANCH_ID  varchar(20) NULL AFTER REMARKS;

/*
Navicat MySQL Data Transfer

Source Server         : localHost
Source Server Version : 50524
Source Host           : localhost:3306
Source Database       : eses_sagi

Target Server Type    : MYSQL
Target Server Version : 50524
File Encoding         : 65001

Date: 2017-06-02 18:51:38
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for seeds_sowing
-- ----------------------------
DROP TABLE IF EXISTS seeds_sowing;
CREATE TABLE seeds_sowing (
  ID bigint(20) NOT NULL AUTO_INCREMENT,
  SOWING_TYPE varchar(2) DEFAULT NULL,
  SEASON_CODE varchar(8) DEFAULT NULL,
  CENTER_ID bigint(20) DEFAULT NULL,
  PRODUCT_ID bigint(20) DEFAULT NULL,
  SAGI_CODE varchar(45) DEFAULT NULL,
  LOT_NO varchar(45) DEFAULT NULL,
  SEED_QTY varchar(15) DEFAULT NULL,
  SOWING_DATE date DEFAULT NULL,
  TRAYS varchar(15) DEFAULT NULL,
  TRAYS_HOLE varchar(15) DEFAULT NULL,
  SEED varchar(15) DEFAULT NULL,
  SEED_PERCENTAGE varchar(15) DEFAULT NULL,
  PROJ_SEEDLING varchar(15) DEFAULT NULL,
  PROJ_DELIVARY varchar(15) DEFAULT NULL,
  BRANCH_ID varchar(25) DEFAULT NULL,
  PRIMARY KEY (ID),
  KEY center_id (CENTER_ID) USING BTREE,
  KEY prod_id (PRODUCT_ID) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


- -- 06-06-2017 - --

ALTER TABLE ptr
ADD COLUMN STATUS_CODE  tinyint(2) NULL DEFAULT 0 AFTER LOCATION_ID;

- -- 07-06-2017 - --
DROP TABLE IF EXISTS crop_stages;
CREATE TABLE crop_stages (
  ID bigint(40) NOT NULL AUTO_INCREMENT,
  NAME varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
-- ----------------------------
-- Records of crop_stages
-- ----------------------------


DROP TABLE IF EXISTS crop_stage_fields;
CREATE TABLE crop_stage_fields (
  ID bigint(20) NOT NULL AUTO_INCREMENT,
  CROP_STAGE_ID bigint(40) DEFAULT NULL,
  NAME varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  COMPONENT tinyint(10) DEFAULT '-1',
  IS_MANDATORY tinyint(5) DEFAULT '0',
  IS_NUMERIC tinyint(5) DEFAULT '0',
  IS_REPORT tinyint(5) DEFAULT '0',
  COL_WIDTH int(15) DEFAULT '0',
  ANSWERS text COLLATE utf8_unicode_ci,
  PRIMARY KEY (ID),
  KEY FK_CS_ID (CROP_STAGE_ID) USING BTREE,
  CONSTRAINT cs_detail_ibfk_1 FOREIGN KEY (CROP_STAGE_ID) REFERENCES crop_stages (ID) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of crop_stage_fields
-- ----------------------------



DROP TABLE IF EXISTS seed_process;
CREATE TABLE seed_process (
  ID bigint(20) NOT NULL AUTO_INCREMENT,
  SEASON varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRODUCTION_CENTER bigint(20) DEFAULT NULL,
  VILLAGE_ID bigint(20) DEFAULT NULL,
  FARMER_ID bigint(20) DEFAULT NULL,
  FARMER_NAME varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRODUCT_ID bigint(20) DEFAULT NULL,
  SAGI_CODE varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  CROP_STAGE_ID bigint(20) DEFAULT NULL,
  BRANCH_ID varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (ID),
  KEY location_id (PRODUCTION_CENTER) USING BTREE,
  KEY village_id (VILLAGE_ID) USING BTREE,
  KEY product_id (PRODUCT_ID) USING BTREE,
  KEY stage_id (CROP_STAGE_ID) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


DROP TABLE IF EXISTS seed_process_data;
CREATE TABLE seed_process_data (
  ID bigint(20) NOT NULL AUTO_INCREMENT,
  FIELD varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  VALUE varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  SEED_PROCESS_ID bigint(20) DEFAULT NULL,
  DESCRIPTION varchar(250) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=303 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;



ALTER TABLE ptr_detail
MODIFY COLUMN QUANTITY  double(50,3) NULL DEFAULT NULL AFTER LOT_NO;

ALTER TABLE ptr_detail
MODIFY COLUMN DAMAGED_QTY  double(50,3) NULL DEFAULT 0 AFTER QUANTITY;

ALTER TABLE warehouse_payment
MODIFY COLUMN TOTAL_GOOD_STOCK  double(12,3) NULL DEFAULT 0.00 AFTER ORDER_NO,
MODIFY COLUMN TOTAL_DAMAGED_STOCK  double(12,3) NULL DEFAULT 0.00 AFTER TOTAL_GOOD_STOCK,
MODIFY COLUMN TOTAL_QTY  double(12,3) NULL DEFAULT 0.00 AFTER TOTAL_DAMAGED_STOCK;

ALTER TABLE warehouse_payment_details
MODIFY COLUMN STOCK  double(20,3) NULL DEFAULT 0.00 AFTER COST_PRICE,
MODIFY COLUMN DAMAGED_STOCK  double(20,3) NULL DEFAULT 0.00 AFTER STOCK;

-- - 12-06-2017 - --
ALTER TABLE warehouse_product
ADD COLUMN SEED  bigint(20) NULL DEFAULT 0 AFTER BATCH_NO, 
ADD COLUMN SEEDLING  double(20,3) NULL DEFAULT 0.000 AFTER SEED;



ALTER TABLE seeds_sowing
MODIFY COLUMN PROJ_DELIVARY  date NULL DEFAULT NULL AFTER PROJ_SEEDLING,
MODIFY COLUMN BRANCH_ID  varchar(45) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL AFTER PROJ_DELIVARY;

-- -14-06-2017 - --
ALTER TABLE seeds_sowing
MODIFY COLUMN SOWING_DATE  datetime NULL DEFAULT NULL AFTER SEED_QTY,
MODIFY COLUMN PROJ_DELIVARY  datetime NULL DEFAULT NULL AFTER PROJ_SEEDLING;

ALTER TABLE seeds_planting
DROP COLUMN SAGI_CODE,
DROP COLUMN LOT_NO;

ALTER TABLE seeds_planting_details
ADD COLUMN SAGI_CODE  varchar(50) NULL DEFAULT NULL AFTER SEED_TYPE;

ALTER TABLE warehouse_product
MODIFY COLUMN SEEDLING  bigint(20) NULL DEFAULT 0 AFTER SEED;


-- - 15-06-2017 - --
ALTER TABLE agent_prof
MODIFY COLUMN IS_TRAINING_AVAILABLE  tinyint(2) NULL DEFAULT 0 AFTER DELIVERY_NO_SEQ;

ALTER TABLE seeds_planting_details
ADD COLUMN PRODUCT_ID  bigint(20) NULL DEFAULT NULL AFTER ID,
ADD INDEX prod_id (PRODUCT_ID) USING BTREE ;
ALTER TABLE seeds_planting
DROP COLUMN PRODUCT_ID;
ALTER TABLE warehouse_product
MODIFY COLUMN SEEDLING  double(20,3) NULL DEFAULT 0.000 AFTER SEED;


INSERT INTO pref (ID, NAME, VAL, ESE_ID, DESCRIPTION) VALUES (null, 'CURRENCY_TYPE', '1', '1', '0-INR,1-DOLLAR');

-- -14-06-2017
-- -To keep track of the received product from product transfer
ALTER TABLE pmt_detail
ADD COLUMN STATUS  bigint(1) NULL DEFAULT 0 AFTER BRANCH_ID;
-- -15-06-2017
-- -Training Material and Observations locale property
INSERT INTO locale_property  VALUES (null, 'trainingObservation', 'en', 'Training Observation');
INSERT INTO locale_property VALUES (null, 'trainingMaterial', 'en', 'Training Material');
INSERT INTO locale_property VALUES (null, 'info.training', 'en', 'Training Information');

-- -19-06-2017

SET @ids = (SELECT id FROM ese_menu WHERE NAME='service');

INSERT INTO ese_menu VALUES (null, 'service.coc', 'Cultivation', 'cultivation_list.action?type=service', '10', @ids, '0', '0','0','0',null);

INSERT INTO ese_ent VALUES (null, 'service.coc.list');
INSERT INTO ese_ent VALUES (null, 'service.coc.update');


INSERT INTO ese_role_ent VALUES ('1',(SELECT id FROM ese_ent WHERE NAME='service.coc.list'));
INSERT INTO ese_role_ent VALUES ('1',(SELECT id FROM ese_ent WHERE NAME='service.coc.update'));

INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='service.coc'), '1');
INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='service.coc'), '3');

INSERT INTO ese_role_menu VALUES (null,(SELECT id FROM ese_menu WHERE NAME='service.coc'), '1');


-- - 20JUNE2017 - --

ALTER TABLE dynamic_report_config_detail
ADD COLUMN PARAMTERS  varchar(150) NULL DEFAULT NULL AFTER ACESS_TYPE;

ALTER TABLE warehouse_product
ADD COLUMN LOT_NO  varchar(20) NULL DEFAULT NULL AFTER BATCH_NO;

-- -Bug Fix to load unregistered farmer name in procurement report
update 
	dynamic_report_config dn
JOIN dynamic_report_config_detail ds ON dn.ID = ds.REPORT_CONFIG_ID
SET ds.FIELD = 'agroTxn.farmerName',
 ds.ACESS_TYPE = '1',
 ds.PARAMTERS = '',
 ds.METHOD = ''
WHERE
	(
		dn.REPORT = 'PROCUREMENT_REPORT'
		AND ds.LABEL_NAME = 'farmerId'
	);


-- - 21June2017

-- -Payment Service Menu Script 

INSERT INTO ese_menu VALUES (null, 'service.payment', 'Payment ', 'paymentAgentLogin_execute.action?type=payment',  '16','3','0','0','0','0','NULL');


INSERT INTO ese_ent VALUES (NULL, 'service.payment.list');
INSERT INTO ese_ent VALUES (NULL, 'service.payment.update');

INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='service.payment'), '1');
INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='service.payment'), '3');


INSERT INTO ese_role_ent VALUES ('1', (SELECT id FROM ese_ent WHERE NAME='service.payment.list'));
INSERT INTO ese_role_ent VALUES ('1', (SELECT id FROM ese_ent WHERE NAME='service.payment.update'));

INSERT INTO ese_role_menu (ID, MENU_ID, ROLE_ID) VALUES (null, (SELECT MAX(ID) FROM ese_menu), '1');

SET @ids = (SELECT ID FROM ese_menu WHERE NAME='service.payment');
UPDATE ese_menu SET ID=@ids, NAME='service.payment', DES='Payment ', URL='payment_create.action', ORD='7', PARENT_ID='3', FILTER='0', EXPORT_AVILABILITY='0', DIMENSION='0', PRIORITY='0', ICON_CLASS=NULL WHERE (ID=@ids);


INSERT INTO ese_seq (ID, SEQ_KEY, SEQ_VAL) VALUES (null, 'PAYMENT_PREFIX_LENGTH', '0');
INSERT INTO ese_seq (ID, SEQ_KEY, SEQ_VAL) VALUES (null, 'PAYMENT_UNIQUE_ID_SEQ', '0');

INSERT INTO payment_mode (ID, CODE, NAME) VALUES (null, '1', 'Cash Advance');
INSERT INTO payment_mode (ID, CODE, NAME) VALUES (null, '2', 'Distribution Payment');
INSERT INTO payment_mode (ID, CODE, NAME) VALUES (null, '3', 'Procurement Advance');
INSERT INTO payment_mode (ID, CODE, NAME) VALUES (null, '4', 'Procurement Payment');
INSERT INTO payment_mode (ID, CODE, NAME) VALUES (null, '5', 'Distribution Advance');

-- -Payment Report Menu Script 

INSERT INTO ese_menu VALUES (NULL , 'report.payment', 'Payment Report', 'paymentReport_list.action', '25','4','0','0','0','0',NULL);
INSERT INTO ese_ent VALUES (NULL, 'report.payment.list');
INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='report.payment'), '1');
INSERT INTO ese_role_ent VALUES ('1', (SELECT id FROM ese_ent WHERE NAME='report.payment.list'));
INSERT INTO ese_role_menu VALUES (NULL, (SELECT MAX(id) FROM ese_menu), '1');

-- - Dynamic Report Script For Payment

INSERT INTO dynamic_report_config (ID, REPORT, FETCH_TYPE, GRID_TYPE, STATUS, BRANCH_ID) VALUES (NULL, 'PAYMENT_REPORT', '2', '1', '1', NULL);

INSERT INTO dynamic_report_config_detail (ID, REPORT_CONFIG_ID, LABEL_NAME, FIELD, ACESS_TYPE, METHOD, GROUP_PROP, SUM_PROP, WIDTH, ORDER, IS_GRID_AVAILABILITY, IS_EXPORT_AVAILABILITY, STATUS) VALUES (NULL, (SELECT id FROM dynamic_report_config WHERE REPORT='PAYMENT_REPORT'), 'id', 'id', '1', NULL, '0', '0', '0', '0', '0', '0', '1');
INSERT INTO dynamic_report_config_detail (ID, REPORT_CONFIG_ID, LABEL_NAME, FIELD, ACESS_TYPE, METHOD, GROUP_PROP, SUM_PROP, WIDTH, ORDER, IS_GRID_AVAILABILITY, IS_EXPORT_AVAILABILITY, STATUS) VALUES (NULL, (SELECT id FROM dynamic_report_config WHERE REPORT='PAYMENT_REPORT'), 'txnDate', 'txnTime', '2', 'getGeneralDateFormat', '0', '0', '200', '1', '1', '1', '1');
INSERT INTO dynamic_report_config_detail (ID, REPORT_CONFIG_ID, LABEL_NAME, FIELD, ACESS_TYPE, METHOD, GROUP_PROP, SUM_PROP, WIDTH, ORDER, IS_GRID_AVAILABILITY, IS_EXPORT_AVAILABILITY, STATUS) VALUES (NULL, (SELECT id FROM dynamic_report_config WHERE REPORT='PAYMENT_REPORT'), 'seasonCode', 'seasonCode', '2', 'getSeasonByCode', '0', '0', '200', '2', '1', '1', '1');
INSERT INTO dynamic_report_config_detail (ID, REPORT_CONFIG_ID, LABEL_NAME, FIELD, ACESS_TYPE, METHOD, GROUP_PROP, SUM_PROP, WIDTH, ORDER, IS_GRID_AVAILABILITY, IS_EXPORT_AVAILABILITY, STATUS) VALUES (NULL, (SELECT id FROM dynamic_report_config WHERE REPORT='PAYMENT_REPORT'), 'agentName', 'agentName', '1', NULL, '0', '0', '200', '3', '1', '1', '1');
INSERT INTO dynamic_report_config_detail (ID, REPORT_CONFIG_ID, LABEL_NAME, FIELD, ACESS_TYPE, METHOD, GROUP_PROP, SUM_PROP, WIDTH, ORDER, IS_GRID_AVAILABILITY, IS_EXPORT_AVAILABILITY, STATUS) VALUES (NULL, (SELECT id FROM dynamic_report_config WHERE REPORT='PAYMENT_REPORT'), 'farmerName', 'farmerName', '1', NULL , '0', '0', '200', '4', '1', '1', '1');
INSERT INTO dynamic_report_config_detail (ID, REPORT_CONFIG_ID, LABEL_NAME, FIELD, ACESS_TYPE, METHOD, GROUP_PROP, SUM_PROP, WIDTH, ORDER, IS_GRID_AVAILABILITY, IS_EXPORT_AVAILABILITY, STATUS) VALUES (NULL, (SELECT id FROM dynamic_report_config WHERE REPORT='PAYMENT_REPORT'), 'txnType', 'txnDesc', '2', 'getTxnType', '0', '0', '200', '5', '1', '1', '1');
INSERT INTO dynamic_report_config_detail (ID, REPORT_CONFIG_ID, LABEL_NAME, FIELD, ACESS_TYPE, METHOD, GROUP_PROP, SUM_PROP, WIDTH, ORDER, IS_GRID_AVAILABILITY, IS_EXPORT_AVAILABILITY, STATUS) VALUES (NULL, (SELECT id FROM dynamic_report_config WHERE REPORT='PAYMENT_REPORT'), 'txnAmount', 'txnAmount', '1', NULL, '0', '0', '100', '6', '1', '1', '1');
INSERT INTO dynamic_report_config_detail (ID, REPORT_CONFIG_ID, LABEL_NAME, FIELD, ACESS_TYPE, METHOD, GROUP_PROP, SUM_PROP, WIDTH, ORDER, IS_GRID_AVAILABILITY, IS_EXPORT_AVAILABILITY, STATUS) VALUES (NULL, (SELECT id FROM dynamic_report_config WHERE REPORT='PAYMENT_REPORT'), 'remarks', 'txnDesc', '2', 'getRemarks', '0', '0', '100', '7', '1', '1', '1');

INSERT INTO dynamic_report_config_filter (ID, REPORT_CONFIG, FIELD, LABEL, METHOD, STATUS) VALUES (NULL, (SELECT id FROM dynamic_report_config WHERE REPORT='PAYMENT_REPORT'), 'seasonCode', 'Season', 'getSeasonList', '1');
INSERT INTO dynamic_report_config_filter (ID, REPORT_CONFIG, FIELD, LABEL, METHOD, STATUS) VALUES (NULL, (SELECT id FROM dynamic_report_config WHERE REPORT='PAYMENT_REPORT'), 'agentName', 'agent', 'getAgentList', '1');
INSERT INTO dynamic_report_config_filter (ID, REPORT_CONFIG, FIELD, LABEL, METHOD, STATUS) VALUES (NULL, (SELECT id FROM dynamic_report_config WHERE REPORT='PAYMENT_REPORT'), 'farmerName', 'farmerName', 'getFarmerFirstNameList', '1');


-- -06-22-2017- --
ALTER TABLE dynamic_report_config_filter
ADD COLUMN ORDER  int(2) NULL AFTER STATUS;

-- -23JUNE2017 - --
ALTER TABLE crop_stage_fields
ADD COLUMN VALIDATION_FORMULA varchar(250) NULL DEFAULT NULL AFTER COMPONENT;
ALTER TABLE crop_stage_fields
ADD COLUMN PARAMETERS  varchar(200) NULL AFTER VALIDATION_FORMULA;
ALTER TABLE crop_stage_fields
ADD COLUMN ERROR_MESSAGE  text NULL AFTER ANSWERS;

-- -28JUNE2017 - --
INSERT INTO locale_property (id, code, lang_code, lang_value) VALUES (null, 'state.select', 'en', 'Select State');
INSERT INTO locale_property (id, code, lang_code, lang_value) VALUES (null, 'crop.select', 'en ', 'Select Crop');
INSERT INTO locale_property (id, code, lang_code, lang_value) VALUES (null, 'cooperative.select', 'en', 'Select Cooperative');
INSERT INTO locale_property (id, code, lang_code, lang_value) VALUES (null, 'empty.ProductName', 'en', 'Please Enter Product Name');




ALTER TABLE procurement
ADD COLUMN CROP_TYPE   varchar(4) NULL AFTER INVOICE_COST;

ALTER TABLE offline_procurement
ADD COLUMN CROP_TYPE  varchar(4) NULL AFTER LONGITUDE;

ALTER TABLE procurement
ADD COLUMN WAREHOUSE_ID   varchar(4) NULL AFTER INVOICE_COST;

ALTER TABLE offline_procurement
ADD COLUMN WAREHOUSE_ID  varchar(4) NULL AFTER LONGITUDE;

-- -3July2017
ALTER TABLE periodic_inspection
ADD COLUMN CROP_CODE  varchar(20) NULL AFTER FARM_ID;

-- -04/07/2017

INSERT INTO ese_menu VALUES (NULL , 'report.qrCodeGen', 'qrCodeGen Report', 'qrCodeGen_list.action', '25','3','0','0','0','0',NULL);
INSERT INTO ese_ent VALUES (NULL, 'report.qrCodeGen.list');
INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='report.qrCodeGen'), '1');
INSERT INTO ese_role_ent VALUES ('1', (SELECT id FROM ese_ent WHERE NAME='report.qrCodeGen.list'));
INSERT INTO ese_role_menu VALUES (NULL, (SELECT MAX(id) FROM ese_menu), '1');


DROP TABLE IF EXISTS dynamic_fields_config; 
CREATE TABLE dynamic_fields_config (
  ID bigint(10) NOT NULL AUTO_INCREMENT,
  COMPONENT_TYPE varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  COMPONENT_NAME varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  COMPONENT_DATA_TYPE varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  COMPONENT_MAX_LENGTH varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  DEFAULT_VALUE varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  IS_REQUIRED varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  CREATED_DATE datetime DEFAULT NULL,
  UPDATED_DATE datetime DEFAULT NULL,
  SECTION_ID varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL,
  BEFORE_ADD varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  AFTER_ADD varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  CATALOGUE_TYPE varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
 PARENT_DEPENDANCY_ID  bigint(40) NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

DROP TABLE IF EXISTS farmer_dynamic_field_value; 
CREATE TABLE farmer_dynamic_field_value (
  ID bigint(15) NOT NULL AUTO_INCREMENT,
  FIELD_NAME varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  FIELD_VALUE varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  FARMER_ID bigint(15) DEFAULT NULL,
  PRIMARY KEY (ID),
  KEY FARMER_ID_FK (FARMER_ID) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- -4july2017

ALTER TABLE farm_inventory
CHANGE COLUMN FARM_ID FARMER_ID  bigint(20) NOT NULL AFTER ID;

ALTER TABLE farm_inventory
ADD INDEX farmer (FARMER_ID) USING BTREE ;

ALTER TABLE dynamic_fields_config
ADD COLUMN COMPONENT_CODE  varchar(45) NULL AFTER ID,
ADD COLUMN ORDER  bigint(40) NULL AFTER PARENT_DEPENDANCY_ID;


ALTER TABLE animal_husbandary
CHANGE COLUMN FARM_ID FARMER_ID  bigint(20) NOT NULL AFTER ID,
ADD INDEX farmer (FARMER_ID) USING BTREE ;


ALTER TABLE catalogue_value
ADD COLUMN DISP_NAME  varchar(250) NULL AFTER NAME;


ALTER TABLE farmer_dynamic_field_value
MODIFY COLUMN FIELD_NAME  varchar(250) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER ID;

-- -07-07-2017

DROP TABLE IF EXISTS dynamic_section_config; 
CREATE TABLE dynamic_section_config (
  ID bigint(20) NOT NULL AUTO_INCREMENT,
  SECTION_NAME varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  TABLE_ID varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  BRANCH_ID varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  SECTION_CODE varchar(25) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (ID),
  KEY SECTION_CODE (SECTION_CODE) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

ALTER TABLE farmer
ADD COLUMN IS_LOAN_TAKEN_SCHEME  tinyint(1) NULL AFTER FINGER_PRINT,
ADD COLUMN LOAN_TAKEN_SCHEME  varchar(50) NULL AFTER IS_LOAN_TAKEN_SCHEME;

ALTER TABLE farmer
MODIFY COLUMN IS_LOAN_TAKEN_SCHEME  tinyint(1) NULL DEFAULT 0 AFTER FINGER_PRINT;


ALTER TABLE dynamic_fields_config
CHANGE COLUMN ORDER ORDER_SET  bigint(40) NULL DEFAULT NULL AFTER PARENT_DEPENDANCY_ID;




INSERT INTO ese_txn_type (CODE, NAME) VALUES ( '377', 'farmerFeedback');
INSERT INTO txn_type (CODE, NAME) VALUES ( '377', 'farmerFeedback');

CREATE TABLE farmer_feedback (
ID  bigint NOT NULL AUTO_INCREMENT,
FARMER_ID  varchar(30) NOT NULL ,
ANSWERED_DATE  datetime NULL ,
QUESTION_1  varchar(255) NULL ,
QUESTION_2  varchar(255) NULL ,
QUESTION_3  varchar(255) NULL ,
QUESTION_4  varchar(255) NULL ,
PRIMARY KEY (ID)
);



ALTER TABLE farmer_feedback
ADD COLUMN FARMER_NAME  varchar(250) NULL AFTER FARMER_ID,
ADD COLUMN VILLAGE  bigint(45) NULL AFTER QUESTION_4,
ADD COLUMN SAMITHI  bigint(45) NULL AFTER VILLAGE;



ALTER TABLE farmer
ADD COLUMN HOME_DIFFICULTY  varchar(50) NULL AFTER LOAN_TAKEN_SCHEME,
ADD COLUMN COMMUNITIY_DIFFICULTY  varchar(50) NULL AFTER HOME_DIFFICULTY,
ADD COLUMN WORK_DIFFFICULTY  varchar(50) NULL AFTER COMMUNITIY_DIFFICULTY,
ADD COLUMN ASSISTIVE_DEIVCE  varchar(50) NULL AFTER WORK_DIFFFICULTY,
ADD COLUMN ASSISTIVE_DEIVCE_NAME  varchar(50) NULL AFTER ASSISTIVE_DEIVCE,
ADD COLUMN ASSISTIVE_DEVICE_REQ  varchar(50) NULL AFTER ASSISTIVE_DEIVCE_NAME,
ADD COLUMN HEALTH_ISSUE  varchar(50) NULL AFTER ASSISTIVE_DEVICE_REQ,
ADD COLUMN HEALTH_ISSUE_DESCRIBE  varchar(50) NULL AFTER HEALTH_ISSUE;

-- ----------------------------
-- Table structure for farmer_health_assesment
-- ----------------------------
DROP TABLE IF EXISTS farmer_health_assesment;
CREATE TABLE farmer_health_assesment (
  ID bigint(20) NOT NULL AUTO_INCREMENT,
  FARMER_ID bigint(20) DEFAULT NULL,
  DISABILITY_TYPE varchar(45) DEFAULT NULL,
  ORIGIN varchar(45) DEFAULT NULL,
  REMARK varchar(200) DEFAULT NULL,
  CONSULTATION_STATUS varchar(45) DEFAULT NULL,
  CONSULTATION_DETAIL varchar(45) DEFAULT NULL,
  REVISION_NO bigint(20) DEFAULT NULL,
  PRIMARY KEY (ID),
  KEY fk_farmer (FARMER_ID),
  CONSTRAINT fk_farmer FOREIGN KEY (FARMER_ID) REFERENCES farmer (ID) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- ----------------------------
-- Table structure for farmer_self_assesment
-- ----------------------------
DROP TABLE IF EXISTS farmer_self_assesment;
CREATE TABLE farmer_self_assesment (
  ID bigint(20) NOT NULL AUTO_INCREMENT,
  FARMER_ID bigint(20) DEFAULT NULL,
  ACTIVITY varchar(45) DEFAULT NULL,
  VALUE varchar(45) DEFAULT NULL,
  REMARK varchar(200) DEFAULT NULL,
  REVISION_NO bigint(20) DEFAULT NULL,
  PRIMARY KEY (ID),
  KEY fk_farmer_asses (FARMER_ID),
  CONSTRAINT fk_farmer_asses FOREIGN KEY (FARMER_ID) REFERENCES farmer (ID) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

ALTER TABLE farmer
ADD COLUMN PREFERENCE_OF_WORK  varchar(50) NULL AFTER HEALTH_ISSUE_DESCRIBE;



ALTER TABLE farmer
ADD COLUMN FORM_FILLED_BY  varchar(250) NULL AFTER HEALTH_ISSUE_DESCRIBE,
ADD COLUMN ASSESSMENT  varchar(250) NULL AFTER FORM_FILLED_BY,
ADD COLUMN PLACE_OF_ASSESSMENT  varchar(10) NULL AFTER ASSESSMENT,
ADD COLUMN OBJECTIVE  varchar(250) NULL AFTER PLACE_OF_ASSESSMENT;

-- - 11july2017
ALTER TABLE farm_crops
ADD COLUMN EST_COTTON  double(10,3) NULL AFTER SEED_QTY_COST;

ALTER TABLE farm
ADD COLUMN OWN_LAND  varchar(20) NULL AFTER FPO,
ADD COLUMN LEASED_LAND  varchar(20) NULL AFTER OWN_LAND,
ADD COLUMN IRRI_LAND  varchar(20) NULL AFTER LEASED_LAND;

ALTER TABLE languageADD COLUMN web_status tinyint(2) NULL DEFAULT 1 AFTER name,ADD COLUMN survey_status tinyint(2) NULL DEFAULT 1 AFTER web_status;

ALTER TABLE farm_crops
ADD COLUMN PRE_SOWING  varchar(20) NULL AFTER LONGITUDE,
ADD COLUMN PRE_HARVEST  varchar(20) NULL AFTER PRE_SOWING,
ADD COLUMN POST_HARVEST  varchar(20) NULL AFTER PRE_HARVEST;
ALTER TABLE farm_crops
ADD COLUMN SEED_COTTON  varchar(30) NULL AFTER POST_HARVEST,
ADD COLUMN LINT_COTTON  varchar(30) NULL AFTER SEED_COTTON,
ADD COLUMN ACTUAL_SEED_YIELD  varchar(30) NULL AFTER LINT_COTTON;
ALTER TABLE farm_crops
ADD COLUMN GROSS_INCOME  varchar(30) NULL AFTER ACTUAL_SEED_YIELD,
ADD COLUMN TOTAL_CROP_HARVEST  varchar(30) NULL AFTER GROSS_INCOME;
ALTER TABLE farm_crops
ADD COLUMN INTER_TYPE  varchar(10) NULL AFTER TOTAL_CROP_HARVEST;
ALTER TABLE farm_crops
ADD COLUMN INTER_ACRE  varchar(20) NULL AFTER INTER_TYPE;



INSERT INTO farmer_field (ID, NAME, TYPE, TYPE_NAME, PARENT, STATUS) VALUES (NULL, 'Assistive Device', '1', 'farmer.assistiveDeivce', '11', '0');
INSERT INTO farmer_field (ID, NAME, TYPE, TYPE_NAME, PARENT, STATUS) VALUES (NULL, 'Health issue', '1', 'farmer.healthIssue', '11', '0');
INSERT INTO farmer_field (ID, NAME, TYPE, TYPE_NAME, PARENT, STATUS) VALUES (NULL, 'Social Info', '4', 'socialClass', '11', '0');

ALTER TABLE farmer_field
ADD COLUMN FIELD  varchar(45) NULL AFTER STATUS,
ADD COLUMN DATA_LEVEL_ID  bigint(3) NULL AFTER FIELD;



-- -13-07-2017
INSERT INTO farmer_field (ID, NAME, TYPE, TYPE_NAME, PARENT, STATUS) VALUES (null, 'farmerHealthInfo', '4', 'farmerHealthInfo', NULL, '0');


INSERT INTO farmer_field (ID, NAME, TYPE, TYPE_NAME, PARENT, STATUS) VALUES (null, 'social_info', '4', 'social_info', NULL, '0');

INSERT INTO farmer_field (ID, NAME, TYPE, TYPE_NAME, PARENT, STATUS) VALUES (null, 'Self Asses', '4', 'self_Assesment', NULL, '0');


-- -13July2017
ALTER TABLE farmer
ADD COLUMN ICS_YEAR  varchar(12) NULL AFTER PREFERENCE_OF_WORK;

ALTER TABLE farmer
ADD COLUMN AGRI_IMPLEMENTS  varchar(45) NULL DEFAULT NULL AFTER OBJECTIVE;

INSERT INTO locale_property  VALUES (null, 'farmer.agricultureImplements', 'en', 'Agricultural Implements');
INSERT INTO locale_property  VALUES (null, 'farmer.agricultureImplements', 'fr', 'Implantations agricoles');


INSERT INTO farmer_field VALUES (null, 'Agriculture Implements', '1', 'farmer.agricultureImplements',(SELECT id FROM farmer_field ff where ff.TYPE_NAME='assets_info'), '0', NULL, NULL);


INSERT INTO farm_field (ID, NAME, TYPE, TYPE_NAME, PARENT, STATUS) VALUES (null, 'Crop Information', '4', 'crop_info', NULL, '1');
INSERT INTO farm_field (ID, NAME, TYPE, TYPE_NAME, PARENT, STATUS) VALUES (null, 'Crop', '1', 'selectedCrop', (SELECT id FROM farm_field ff where ff.TYPE_NAME='crop_info'), '1');
INSERT INTO farm_field (ID, NAME, TYPE, TYPE_NAME, PARENT, STATUS) VALUES (null, 'Variety', '1', 'selectedVariety', (SELECT id FROM farm_field ff where ff.TYPE_NAME='crop_info'), '1');

ALTER TABLE cultivation
ADD COLUMN COTTON_QTY  varchar(20) NULL AFTER LABOUR_COST,
ADD COLUMN SALE_PRICE  varchar(20) NULL AFTER COTTON_QTY,
ADD COLUMN SALE_CT_INCOME  varchar(20) NULL AFTER SALE_PRICE;


-- -21July2017
ALTER TABLE farmer_training
ADD COLUMN SELECTION_TYPE  varchar(50) NULL AFTER BRANCH_ID;

ALTER TABLE farmer
MODIFY COLUMN AGRI_IMPLEMENTS  varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER OBJECTIVE;

ALTER TABLE loc_history
ADD COLUMN DISTANCE  varchar(10) NULL DEFAULT 0 AFTER CREATED_TIME,
ADD COLUMN ADDRESS  varchar(255) NULL AFTER DISTANCE;

-- -24july2017
ALTER TABLE cultivation_detail
MODIFY COLUMN COST  double(20,2) NULL DEFAULT NULL AFTER CULTIVATION_ID;

-- - 25-07-2017 --
UPDATE locale_property SET lang_value='Village' WHERE (code='village.name' and lang_value='Village Name' and lang_code='en');
UPDATE locale_property SET  lang_value='village' WHERE (code='village.name' and lang_value='nom du village' and lang_code='fr');

INSERT INTO farmer_field (ID, NAME, TYPE, TYPE_NAME, PARENT, STATUS, FIELD, DATA_LEVEL_ID) VALUES (null, 'prefWrk', '1', 'farmer.prefWrk', '1', '0', NULL, NULL);
INSERT INTO farmer_field (ID, NAME, TYPE, TYPE_NAME, PARENT, STATUS, FIELD, DATA_LEVEL_ID) VALUES (null, 'formFilledBy', '1', 'farmer.formFilledBy', '1', '0', NULL, NULL);
INSERT INTO farmer_field (ID, NAME, TYPE, TYPE_NAME, PARENT, STATUS, FIELD, DATA_LEVEL_ID) VALUES (null, 'objective', '1', 'farmer.objective', '1', '0', NULL, NULL);
INSERT INTO farmer_field (ID, NAME, TYPE, TYPE_NAME, PARENT, STATUS, FIELD, DATA_LEVEL_ID) VALUES (null, 'assess', '1', 'farmer.assess', '1', '0', NULL, NULL);
INSERT INTO farmer_field (ID, NAME, TYPE, TYPE_NAME, PARENT, STATUS, FIELD, DATA_LEVEL_ID) VALUES (null, 'placeOfAsss', '1', 'farmer.placeOfAsss', '1', '0', NULL, NULL);
INSERT INTO farmer_field (ID, NAME, TYPE, TYPE_NAME, PARENT, STATUS, FIELD, DATA_LEVEL_ID) VALUES (null, 'isDisabled', '1', 'selectedDisabled', '1', '0', NULL, NULL);

ALTER TABLE farm
ADD COLUMN FARM_PLAT_NO  varchar(10) NULL AFTER IRRI_LAND;

UPDATE dynamic_report_config_detail SET ID='26', REPORT_CONFIG_ID='3', LABEL_NAME='farmer', FIELD='farmerName', ACESS_TYPE='1', PARAMTERS=NULL, METHOD=NULL, GROUP_PROP='0', SUM_PROP='0', WIDTH='100', ORDER='7', IS_GRID_AVAILABILITY='1', IS_EXPORT_AVAILABILITY='1', STATUS='1' WHERE (ID='26');

-- - 26-07-2017
ALTER TABLE farmer
ADD COLUMN IS_DISABLE  varchar(50) NULL AFTER AGRI_IMPLEMENTS;

-- -28july2017
ALTER TABLE cultivation
ADD COLUMN SOIL_PREPARATION  varchar(20) NULL AFTER SALE_CT_INCOME;


ALTER TABLE periodic_inspection_data_list
ADD COLUMN UOM  varchar(50) NULL AFTER PERIODIC_INSPECTION_ID;

-- -02Aug2017
UPDATE farm_catalogue SET NAME='Seed Type' WHERE (ID='43');
INSERT INTO farm_field (ID, NAME, TYPE, TYPE_NAME, PARENT, STATUS) VALUES (null, 'Farm Name Detail', '1', 'farm.farmNameDet', (SELECT id FROM farm_field ff where ff.TYPE_NAME='farmInfo'), '0');

UPDATE farm_catalogue SET NAME='Cooperative' WHERE (CATALOGUE_TYPEZ='11');

-- -02 Aug 2017
INSERT INTO locale_property VALUES (NULL, 'uom', 'en', 'Unit of Measurement');
-- -03Aug2017
INSERT INTO farmer_field (ID, NAME, TYPE, TYPE_NAME, PARENT, STATUS, FIELD, DATA_LEVEL_ID) VALUES (null, 'FarmerId', '1', 'farmer.farmerId', (SELECT id FROM farmer_field ff where ff.TYPE_NAME='pers_info'), '0', NULL, NULL);
delete from pref where name='CURRENCY_TYPE';
INSERT INTO pref (ID, NAME, VAL, ESE_ID, DESCRIPTION) VALUES (null, 'CURRENCY_TYPE', 'INR', '1', null);
INSERT INTO pref (ID, NAME, VAL, ESE_ID, DESCRIPTION) VALUES (null, 'AREA_TYPE', 'Acres', '1', NULL);

-- -03 Aug 2017
UPDATE locale_property SET id='53', code='prices', lang_code='en', lang_value='Price' WHERE (id='53');
INSERT INTO farm_field (ID, NAME, TYPE, TYPE_NAME, PARENT, STATUS) VALUES (null, 'ICS Status', '1', 'farm.farmIcsConv.icsType', (SELECT id FROM farm_field ff where ff.TYPE_NAME='farmInfo'), '0');
UPDATE locale_property SET id='53', code='prices', lang_code='en', lang_value='SalePrice' WHERE (id='53');

-- -04-08-2017

UPDATE dynamic_report_config_detail SET ID='12', REPORT_CONFIG_ID='1', LABEL_NAME='amt@', FIELD='pd.subTotal', ACESS_TYPE='1', PARAMTERS=NULL, METHOD=NULL, GROUP_PROP='0', SUM_PROP='1', WIDTH='100', ORDER='10', IS_GRID_AVAILABILITY='1', IS_EXPORT_AVAILABILITY='1', STATUS='1' WHERE (ID='12');
UPDATE dynamic_report_config_detail SET ID='13', REPORT_CONFIG_ID='1', LABEL_NAME='payementAmount@', FIELD='agroTxn.txnAmount', ACESS_TYPE='1', PARAMTERS=NULL, METHOD=NULL, GROUP_PROP='0', SUM_PROP='0', WIDTH='100', ORDER='11', IS_GRID_AVAILABILITY='1', IS_EXPORT_AVAILABILITY='1', STATUS='1' WHERE (ID='13');
UPDATE dynamic_report_config_detail SET ID='18', REPORT_CONFIG_ID='2', LABEL_NAME='pricePerUnit@', FIELD='ratePerUnit', ACESS_TYPE='1', PARAMTERS=NULL, METHOD=NULL, GROUP_PROP='0', SUM_PROP='0', WIDTH='100', ORDER='5', IS_GRID_AVAILABILITY='1', IS_EXPORT_AVAILABILITY='1', STATUS='1' WHERE (ID='18');
UPDATE dynamic_report_config_detail SET ID='20', REPORT_CONFIG_ID='2', LABEL_NAME='grossAmt@', FIELD='grossWeight', ACESS_TYPE='1', PARAMTERS=NULL, METHOD=NULL, GROUP_PROP='0', SUM_PROP='0', WIDTH='100', ORDER='7', IS_GRID_AVAILABILITY='1', IS_EXPORT_AVAILABILITY='1', STATUS='1' WHERE (ID='20');
UPDATE dynamic_report_config_detail SET ID='38', REPORT_CONFIG_ID='3', LABEL_NAME='totalAmt@', FIELD='totalAmount', ACESS_TYPE='1', PARAMTERS=NULL, METHOD=NULL, GROUP_PROP='0', SUM_PROP='0', WIDTH='100', ORDER='12', IS_GRID_AVAILABILITY='1', IS_EXPORT_AVAILABILITY='1', STATUS='1' WHERE (ID='38');
UPDATE dynamic_report_config_detail SET ID='50', REPORT_CONFIG_ID='4', LABEL_NAME='costPrice@', FIELD='p.price', ACESS_TYPE='1', PARAMTERS=NULL, METHOD=NULL, GROUP_PROP='0', SUM_PROP='0', WIDTH='50', ORDER='5', IS_GRID_AVAILABILITY='1', IS_EXPORT_AVAILABILITY='1', STATUS='1' WHERE (ID='50');
UPDATE dynamic_report_config_detail SET ID='51', REPORT_CONFIG_ID='4', LABEL_NAME='sellingPrice@', FIELD='sellingPrice', ACESS_TYPE='1', PARAMTERS=NULL, METHOD=NULL, GROUP_PROP='0', SUM_PROP='0', WIDTH='50', ORDER='6', IS_GRID_AVAILABILITY='1', IS_EXPORT_AVAILABILITY='1', STATUS='1' WHERE (ID='51');
UPDATE dynamic_report_config_detail SET ID='52', REPORT_CONFIG_ID='4', LABEL_NAME='subTotal@', FIELD='subTotal', ACESS_TYPE='1', PARAMTERS=NULL, METHOD=NULL, GROUP_PROP='0', SUM_PROP='0', WIDTH='50', ORDER='7', IS_GRID_AVAILABILITY='1', IS_EXPORT_AVAILABILITY='1', STATUS='1' WHERE (ID='52');



-- - 7Aug2017
INSERT INTO farm_field (ID, NAME, TYPE, TYPE_NAME, PARENT, STATUS) VALUES (null, 'Other Irrigation', '1', 'farm.farmDetailedInfo.irrigatedOther', (SELECT id FROM farm_field ff where ff.TYPE_NAME='soilIrrigationInfo'), '0');

-- - 09 Aug 2017
ALTER TABLE offline_procurement
ADD COLUMN FFC  varchar(45) NULL AFTER SUBSTITUTE_NAME;

ALTER TABLE procurement
ADD COLUMN FFC  varchar(45) NULL AFTER SUBSTITUTE_NAME;

-- -10Aug2017
SET @ser = (SELECT id FROM ese_menu WHERE NAME='training');
INSERT INTO ese_menu VALUES (null, 'service.trainingPlanner', 'Farmer Training Planner', 'trainingPlanner_list.action', '2', @ser, '0','0', '0','0',null);
INSERT INTO ese_ent VALUES (null, 'service.trainingPlanner.list');
INSERT INTO ese_ent VALUES (null, 'service.trainingPlanner.update');

INSERT INTO ese_role_ent VALUES ('1',(SELECT id FROM ese_ent WHERE NAME='service.trainingPlanner.list'));
INSERT INTO ese_role_ent VALUES ('1',(SELECT id FROM ese_ent WHERE NAME='service.trainingPlanner.update'));

INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='service.trainingPlanner'), '1');
INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='service.trainingPlanner'), '3');

INSERT INTO ese_role_menu VALUES (null,(SELECT id FROM ese_menu WHERE NAME='service.trainingPlanner'), '1');


-- -Removed Sagi Seeds Releated Tables
-- -10-08-2017
set foreign_key_checks=0;
drop table ptr;
drop table ptr_detail;
drop table crop_stages;
drop table crop_stage_fields;
drop table seeds_planting;
drop table seeds_planting_details;
drop table seeds_sowing;drop table seed_process;
drop table seed_process_data;
alter table warehouse_product drop column lot_no,drop column seed,drop column seedling;
alter table warehouse_product_detail drop column PARENT_CODE,drop column PACKETS_INFO,drop column SEED_TYPE,drop column UOM,drop column variety;

alter table warehouse_payment_details drop column PARENT_CODE,drop column PACKETS_INFO,drop column SEED_TYPE,drop column UOM,drop column variety;


-- -11Aug2017
UPDATE locale_property SET id='341', code='exportFarmerCropColumnHeader', lang_code='en', lang_value='S.no,Season,Farmer,Name Of ICS,Village,Farm,Crop(MainCrop),Area in %#,Type,InterCrop Details,Estimated yield (Kg),Actual Harvested (Kg),Per acre yield (Kg)' WHERE (id='341');
INSERT INTO locale_property (id, code, lang_code, lang_value) VALUES (NULL, 'exportFarmerCropColumnHeaderBranch', 'en', 'S.no,Organization,Season,Farmer,Name Of ICS,Village,Farm,Crop(MainCrop),Area in %#,Type,InterCrop Details,Estimated yield (Kg),Actual Harvested (Kg),Per acre yield (Kg)');

-- -08-14-2017

ALTER TABLE farmer_field
ADD COLUMN OTHERS  bigint(2) NULL AFTER DATA_LEVEL_ID;

ALTER TABLE farm_field
ADD COLUMN OTHERS  bigint(2) NULL AFTER STATUS;

-- -16-08-2017

SET @ids = (SELECT id FROM ese_menu WHERE NAME='service');

INSERT INTO ese_menu VALUES (null, 'service.farmerIncome', 'Farmer Income', 'farmerIncome_list.action?type=service', '11', @ids, '0', '0','0','0',null);

INSERT INTO ese_ent VALUES (null, 'service.farmerIncome.list');
INSERT INTO ese_ent VALUES (null, 'service.farmerIncome.update');

INSERT INTO ese_role_ent VALUES ('1',(SELECT id FROM ese_ent WHERE NAME='service.farmerIncome.list'));
INSERT INTO ese_role_ent VALUES ('1',(SELECT id FROM ese_ent WHERE NAME='service.farmerIncome.update'));

INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='service.farmerIncome'), '1');
INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='service.farmerIncome'), '3');

INSERT INTO ese_role_menu VALUES (null,(SELECT id FROM ese_menu WHERE NAME='service.farmerIncome'), '1');

/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50508
Source Host           : localhost:3306
Source Database       : eses_agro

Target Server Type    : MYSQL
Target Server Version : 50508
File Encoding         : 65001

Date: 2017-08-16 12:15:06
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for farm_crops_field
-- ----------------------------
DROP TABLE IF EXISTS farm_crops_field;
CREATE TABLE farm_crops_field (
  ID bigint(20) NOT NULL AUTO_INCREMENT,
  NAME varchar(255) DEFAULT NULL,
  TYPE varchar(3) DEFAULT NULL,
  TYPE_NAME varchar(255) DEFAULT NULL,
  PARENT bigint(20) DEFAULT NULL,
  STATUS bigint(3) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of farm_crops_field
-- ----------------------------
INSERT INTO farm_crops_field VALUES ('1', 'Crop Category', '1', 'farmCrops.cropCategory', null, '1');
INSERT INTO farm_crops_field VALUES ('2', 'Crop Season', '1', 'cropSeasonCode', null, '1');
INSERT INTO farm_crops_field VALUES ('3', 'Cultivation Type', '1', 'selectedCropCategoryList', null, '1');
INSERT INTO farm_crops_field VALUES ('4', 'Crop Name', '1', 'selectedCrop', null, '1');
INSERT INTO farm_crops_field VALUES ('5', 'Crop Name', '5', 'cropNameCls', null, '1');
INSERT INTO farm_crops_field VALUES ('6', 'Variety', '1', 'selectedVariety', null, '1');
INSERT INTO farm_crops_field VALUES ('7', 'Cultivation Area', '1', 'farmCrops.cultiArea', null, '1');
INSERT INTO farm_crops_field VALUES ('8', 'sowingDate', '1', 'sowingDate', null, '1');
INSERT INTO farm_crops_field VALUES ('9', 'Type', '1', 'farmCrops.type', null, '1');
INSERT INTO farm_crops_field VALUES ('10', 'Seed Source', '1', 'farmCrops.seedSource', null, '1');
INSERT INTO farm_crops_field VALUES ('11', 'Seed Treatment', '1', 'farmCrops.seedTreatmentDetails', null, '1');
INSERT INTO farm_crops_field VALUES ('12', 'Risk Assesment', '1', 'farmCrops.riskAssesment', null, '1');
INSERT INTO farm_crops_field VALUES ('13', 'Risk Buffer Zone', '1', 'farmCrops.riskBufferZoneDistanse', null, '1');
INSERT INTO farm_crops_field VALUES ('14', 'Staple Len Text Box', '5', 'stapleLengthTextBox', null, '1');
INSERT INTO farm_crops_field VALUES ('15', 'Seed Qty Used', '1', 'farmCrops.seedQtyUsedPfx', null, '1');
INSERT INTO farm_crops_field VALUES ('16', 'Seed Qty Cost', '1', 'farmCrops.seedQtyCostPfx', null, '1');
INSERT INTO farm_crops_field VALUES ('17', 'Staple Len Dropdown', '5', 'stapleLengthDropDown', null, '1');
INSERT INTO farm_crops_field VALUES ('18', 'Seed Cotton', '1', 'farmCrops.seedCotton', null, '1');
INSERT INTO farm_crops_field VALUES ('19', 'lint Cotton', '1', 'farmCrops.lintCotton', null, '1');
INSERT INTO farm_crops_field VALUES ('20', 'Actual Seed Cotton', '1', 'farmCrops.actualSeedYield', null, '1');
INSERT INTO farm_crops_field VALUES ('21', 'Estimated Yield', '1', 'farmCrops.estimatedYield', null, '1');
INSERT INTO farm_crops_field VALUES ('22', 'Estimated Cotton', '1', 'farmCrops.estimatedCotton', null, '1');
INSERT INTO farm_crops_field VALUES ('23', 'Inter Acre', '1', 'farmCrops.interAcre', null, '1');
INSERT INTO farm_crops_field VALUES ('24', 'Total Crop Harvested', '1', 'farmCrops.totalCropHarv', null, '1');
INSERT INTO farm_crops_field VALUES ('25', 'Gross Income', '1', 'farmCrops.grossIncome', null, '1');
INSERT INTO farm_crops_field VALUES ('26', 'Estimated Yield With Metric Calc', '1', 'farmCrops.estYldPfx', null, '1');


INSERT INTO locale_property (id, code, lang_code, lang_value) VALUES (null, 'trainingName', 'en', 'Training Topic');
INSERT INTO locale_property (id, code, lang_code, lang_value) VALUES (null, 'trainingName', 'fr', 'ThÃ¨me de la formation');
INSERT INTO locale_property (id, code, lang_code, lang_value) VALUES (null, 'category', 'en', 'Criteria category');
INSERT INTO locale_property (id, code, lang_code, lang_value) VALUES (null, 'category', 'fr', 'CatÃ©gorie de critÃ¨res');
INSERT INTO locale_property (id, code, lang_code, lang_value) VALUES (null, 'trainingcompletionStatusReportColumnHeadersBranch', 'en', 'S.no,Organization,Date,Farmer Training Code,Mobile User,Group,No of Farmers,Remarks');
INSERT INTO locale_property (id, code, lang_code, lang_value) VALUES (null, 'trainingcompletionStatusReportColumnHeaders', 'en', 'S.no,Date,Farmer Training Code,Mobile User,Group,No of Farmers,Remarks');
INSERT INTO locale_property (id, code, lang_code, lang_value) VALUES (null, 'warehouseLabel', 'en', 'Group');
INSERT INTO locale_property (id, code, lang_code, lang_value) VALUES (null, 'warehouseLabel', 'fr', 'groupe');
INSERT INTO locale_property (id, code, lang_code, lang_value) VALUES (null, 'uom', 'en', 'Unit of Measurement');
INSERT INTO locale_property (id, code, lang_code, lang_value) VALUES (null, 'uom', 'fr', 'UnitÃ© de mesure');
INSERT INTO locale_property (id, code, lang_code, lang_value) VALUES (null, 'Group', 'en', 'LG');


--- - 18 AUG 2017
ALTER TABLE warehouse
ADD COLUMN GROUP_TYPE  varchar(45) NULL AFTER USER_VILLAGE_ID;


-- -18-08-2017
INSERT INTO locale_property (id, code, lang_code, lang_value) VALUES (null, 'procurementQtyLabel', 'en', 'Quantity');
INSERT INTO locale_property (id, code, lang_code, lang_value) VALUES (null, 'procurementQtyLabel', 'fr', 'QuantitÃ©');

-- - 21Aug2017
ALTER TABLE farmer
ADD COLUMN TOTAL_HSLD  varchar(20) NULL AFTER IS_DISABLE;

INSERT INTO farmer_field (ID, NAME, TYPE, TYPE_NAME, PARENT, STATUS, FIELD, DATA_LEVEL_ID, OTHERS) VALUES (null, 'House Hold Label', '1', 'farmer.totalHsldLabel', (SELECT id FROM farmer_field ff where ff.TYPE_NAME='family_info'), '0', NULL, NULL, '0');

ALTER TABLE cultivation
ADD COLUMN DEVICE_INFO  varchar(45) NULL AFTER SOIL_PREPARATION,
ADD COLUMN USER_INFO  varchar(20) NULL AFTER DEVICE_INFO,
ADD COLUMN MOBILE_USER_INFO  varchar(20) NULL AFTER USER_INFO;

ALTER TABLE farmer
ADD COLUMN DEVICE_INFO  varchar(50) NULL AFTER TOTAL_HSLD;

INSERT INTO farm_field (ID, NAME, TYPE, TYPE_NAME, PARENT, STATUS, OTHERS) VALUES (null, 'Other Irrigation', '1', 'selectedIrrigationSource', (SELECT id FROM farm_field ff where ff.TYPE_NAME='soilIrrigationInfo'), '0', '0');

ALTER TABLE cultivation
DROP COLUMN DEVICE_INFO,
DROP COLUMN USER_INFO,
DROP COLUMN MOBILE_USER_INFO;

ALTER TABLE farmer
DROP COLUMN DEVICE_INFO;

UPDATE locale_property SET id='114', code='procurement', lang_code='en', lang_value='Amount' WHERE (id='114');
UPDATE locale_property SET id='115', code='procurement', lang_code='fr', lang_value='Montant' WHERE (id='115');

UPDATE locale_property SET id='194', code='procurementQtyLabel', lang_code='en', lang_value='Quantity(Kgs)' WHERE (id='194');
UPDATE locale_property SET id='195', code='procurementQtyLabel', lang_code='fr', lang_value='QuantitÃ©(Kgs)' WHERE (id='195');


-- -22-08-2017

ALTER TABLE farmer
MODIFY COLUMN AGRI_IMPLEMENTS  varchar(250) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER OBJECTIVE;

ALTER TABLE farmer_dynamic_field_value
MODIFY COLUMN FIELD_NAME  varchar(250) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER ID;
ALTER TABLE farmer_dynamic_field_value
MODIFY COLUMN FIELD_NAME  varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER ID,
MODIFY COLUMN FIELD_VALUE  varchar(150) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER FIELD_NAME;
-- - 22Aug2017
INSERT INTO pref (ID, NAME, VAL, ESE_ID, DESCRIPTION) VALUES (null, 'CURRENCY_TYPE', 'INR', '1', NULL);
INSERT INTO pref (ID, NAME, VAL, ESE_ID, DESCRIPTION) VALUES (null, 'AREA_TYPE', '1-Hectare', '1',NULL);


-- - 23-08-2017 - --

ALTER TABLE farmer
ADD COLUMN TYPEZ  varchar(20) NULL AFTER DEVICE_INFO;

ALTER TABLE dynamic_section_config
ADD COLUMN TYPEZ  varchar(2) NULL DEFAULT 1 AFTER SECTION_CODE;


-- -28 AUG 2017
ALTER TABLE offline_procurement
ADD COLUMN IS_REG_FARMER  varchar(5) NULL AFTER FARMER_ID;

alter table farmer add column REF_ID BIGINT(45);

ALTER TABLE branch_master ADD COLUMN WEB_SEQ  varchar(15) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '0' AFTER STATUS;
ALTER TABLE branch_master ADD COLUMN REMOTE_SEQ  varchar(15) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '0' AFTER WEB_SEQ;
ALTER TABLE branch_master ADD COLUMN SEQ_LIMIT  varchar(15) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '0' AFTER REMOTE_SEQ;
ALTER TABLE branch_master ADD COLUMN WEB_ID_LIMIT  varchar(15) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '0' AFTER SEQ_LIMIT;
ALTER TABLE farmer ADD COLUMN REF_ID  bigint(45) NULL DEFAULT NULL AFTER farmer_id_old;

ALTER TABLE cultivation_detail
ADD COLUMN UOM  varchar(45) NULL AFTER TYPE;

-- -05 SEPT 2017 - --
UPDATE dynamic_report_config_filter SET  METHOD='getSeasonsList' WHERE (REPORT_CONFIG='1' and FIELD='seasonCode' and LABEL='Season');


-- -09-08-2017- --
SET @ids = (SELECT id FROM ese_menu WHERE NAME='report.farmer');

INSERT INTO ese_menu VALUES (NULL , 'report.farmerActivityReport', 'Farmer Activity Report', 'farmerActivityReport_list.action', '29',@ids,'0','0','0','0',NULL);
INSERT INTO ese_ent VALUES (NULL, 'report.farmerActivityReport.list');
INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='report.farmerActivityReport'), '1');
INSERT INTO ese_role_ent VALUES ('1', (SELECT id FROM ese_ent WHERE NAME='report.farmerActivityReport.list'));
INSERT INTO ese_role_menu VALUES (NULL, (SELECT MAX(id) FROM ese_menu), '1');

-- - 09-11-2017 - --

INSERT INTO locale_property (id, code, lang_code, lang_value) VALUES (null, 'categories', 'en', 'Category');
INSERT INTO locale_property (id, code, lang_code, lang_value) VALUES (null, 'categories', 'fr', 'CatÃ©gorie');

UPDATE locale_property SET lang_value='S.no,Date,Season,Farmer,Farm,Village,Total Yield Quantity (Kg)' WHERE (code='exportCropHarvestColumnHeader');
UPDATE locale_property SET lang_value='S.no,Organization,Date,Season,Farmer,Farm,Village,Total Yield Quantity (Kg)' WHERE (code='exportCropHarvestColumnHeaderBranch');
UPDATE locale_property SET lang_value='Crop Type,Crop,Variety,Grade,Unit,Quantity' WHERE (code='exportHarvestSubgridHeadings');

-- Not for Canda
UPDATE locale_property SET lang_value='S.no,Organization,Season,Farmer,Farm,Village,Income from Primary Crop (@*),Income from Intercrop (@*),Income from Other Sources (@*),Gross Agricultural Income (@*)' WHERE (code='exportFarmerIncomeColumnHeaderBranch');
UPDATE locale_property SET lang_value='S.no,Season,Farmer,Farm,Village,Income from Primary Crop (@*),Income from Intercrop (@*),Income from Other Sources (@*),Gross Agricultural Income (@*)' WHERE (code='exportFarmerIncomeColumnHeader');

UPDATE locale_property SET lang_value='S.no,Season,Farmer Code,Farmer,Farm,Village,Land Preparation (@*),Sowing (@*),Gap Filling & Thinning (@*),Weeding (@*),InterCultural (@*),Irrigation (@*),Fertilizer (@*),Pesticide (@*),Farm Yard Manure(FYM),Harvesting (@*),Other Expenses (@*),Labour Cost (@*),Total Cost (@*)' WHERE (code='exportCultivationColumnHeader');
UPDATE locale_property SET lang_value='S.no,Organization,Season,Farmer Code,Farmer,Farm,Village,Land Preparation (@*),Sowing (@*),Gap Filling & Thinning (@*),Weeding (@*),InterCultural (@*),Irrigation (@*),Fertilizer (@*),Pesticide (@*),Farm Yard Manure(FYM),Harvesting (@*),Other Expenses (@*),Labour Cost (@*),Total Cost (@*)' WHERE (code='exportCultivationColumnHeaderBranch');

-- -18/09/2017
ALTER TABLE cultivation
DROP COLUMN LAND_PLOUG,
DROP COLUMN RIDGE_FURROW,
DROP COLUMN SEED_COST,
DROP COLUMN SOWING_TREAT,
DROP COLUMN SOWLING_COST_MEN,
DROP COLUMN SOWING_COST_WOMEN,
DROP COLUMN GAP_COST_MEN,
DROP COLUMN GAP_COST_WOMEN,
DROP COLUMN WEEDING_COST_MEN,
DROP COLUMN WEEDING_COST_WOMEN,
DROP COLUMN CULTURE_COST_MEN,
DROP COLUMN CULTURE_COST_WOMEN,
DROP COLUMN IRRIGATION_COST_MEN,
DROP COLUMN IRRIGATION_COST_WOMEN,
DROP COLUMN FERTILIZER_COST_MEN,
DROP COLUMN FERTILIZER_COST_WOMEN,
DROP COLUMN PESTICIDE_COST_MEN,
DROP COLUMN PESTICIDE_COST_WOMEN,
DROP COLUMN HARVEST_COST_MEN,
DROP COLUMN HARVEST_COST_WOMEN,
DROP COLUMN PACKING_MATERIAL,
DROP COLUMN TRANSPORT,
DROP COLUMN MISCELLANEOUS,
DROP COLUMN MANURE_COST_MEN,
DROP COLUMN MANURE_COST_WOMEN,
DROP COLUMN DEVICE_INFO,
DROP COLUMN USER_INFO,
DROP COLUMN MOBILE_USER_INFO;



INSERT INTO farm_catalogue (ID, NAME, CATALOGUE_TYPEZ, REVIOSION_NO, STATUS) VALUES ('111', 'Crop Category', '111', '1', '1');

UPDATE locale_property SET lang_value='S.no,Organization,Season,Farmer,Gender,Age,Name of the ICS,Cooperative,Village,Taluk,District,State' WHERE (code='exportFarmerListColumnHeaderBranch');
UPDATE locale_property SET lang_value='S.no,Season,Farmer,Gender,Age,Name of the ICS,Cooperative,Village,Taluk,District,State' WHERE (code='exportFarmerListColumnHeader');

UPDATE locale_property SET lang_value='Category,Product,Unit,Existing Quantity,Cost Price (@*),Amount (@*),Distribution Quantity,Available Quantity' WHERE (code='exportSubColumnHeaderDistributionfarmer');

-- -3-10-2017

INSERT INTO locale_property (id, code, lang_code, lang_value) VALUES (null, 'donutChart.preSowing', 'en', 'Pre Sowing');
INSERT INTO locale_property (id, code, lang_code, lang_value) VALUES (null, 'donutChart.preSowing', 'fr', 'PrÃ©-semis');
INSERT INTO locale_property (id, code, lang_code, lang_value) VALUES (null, 'donutChart.preHavst', 'en', 'Pre Harvest');
INSERT INTO locale_property (id, code, lang_code, lang_value) VALUES (null, 'donutChart.preHavst', 'fr', 'PrÃ©-rÃ©colte');
INSERT INTO locale_property (id, code, lang_code, lang_value) VALUES (null, 'donutChart.postHavst', 'en', 'Post Harvest');
INSERT INTO locale_property (id, code, lang_code, lang_value) VALUES (null, 'donutChart.postHavst', 'fr', 'Post Harvest');
INSERT INTO locale_property (id, code, lang_code, lang_value) VALUES (null, 'farmerSowingHarvestChart', 'en', 'Farmer Sowing And Harvest');
INSERT INTO locale_property (id, code, lang_code, lang_value) VALUES (null, 'farmerSowingHarvestChart', 'fr', 'Semis et rÃ©colte des agriculteurs');

INSERT INTO pref (ID, NAME, VAL, ESE_ID, DESCRIPTION) VALUES (null, 'FARMER_SOWING_HARVEST_BAR_CHART', '1', '1', '0=No,1=Yes');


-- -04 Oct 2017
-- - Run this for all Tenant

-- ----------------------------
-- Table structure for offline_supplier_procurement
-- ----------------------------
DROP TABLE IF EXISTS offline_supplier_procurement;
CREATE TABLE offline_supplier_procurement (
  ID bigint(45) NOT NULL AUTO_INCREMENT,
  RECEIPT_NO varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  INVOICE_NO varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  IS_REG_SUPPLIER varchar(5) COLLATE utf8_unicode_ci DEFAULT NULL,
  PROCUREMENT_DATE varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  TOTAL_AMOUNT varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  PAYMENT_AMOUNT varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  AGENT_ID varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  DEVICE_ID varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  SERVICE_POINT_ID varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  MOBILE_NO varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  MASTER_TYPE varchar(8) COLLATE utf8_unicode_ci DEFAULT NULL,
  MASTER_TYPE_ID varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  BRANCH_ID varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  STATUS_CODE tinyint(1) DEFAULT NULL,
  STATUS_MSG varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  SEASON_CODE varchar(25) COLLATE utf8_unicode_ci DEFAULT NULL,
  WAREHOUSE_ID varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  LATITUDE varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  LONGITUDE varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for offline_supplier_procurement_detail
-- ----------------------------
DROP TABLE IF EXISTS offline_supplier_procurement_detail;
CREATE TABLE offline_supplier_procurement_detail (
  ID bigint(20) NOT NULL AUTO_INCREMENT,
  FARMER_ID varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  FARMER_NAME varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  IS_REG_FARMER varchar(5) COLLATE utf8_unicode_ci DEFAULT NULL,
  MOBILE_NO varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  VILLAGE_CODE varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  QUALITY varchar(35) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRODUCT_CODE varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  CROP_TYPE varchar(4) COLLATE utf8_unicode_ci DEFAULT NULL,
  NUMBER_OF_BAGS varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  UNIT varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  BATCH_NO varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  GROSS_WEIGHT varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  NET_WEIGHT varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  RATE_PER_UNIT varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  SUB_TOTAL varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  OFFLINE_SUPPLIER_PROCUREMENT_ID bigint(20) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for supplier_procurement
-- ----------------------------
DROP TABLE IF EXISTS supplier_procurement;
CREATE TABLE supplier_procurement (
  ID bigint(45) NOT NULL AUTO_INCREMENT,
  TYPEE tinyint(4) NOT NULL,
  INVOICE_NO varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  TRXN_AGRO_ID bigint(20) DEFAULT NULL,
  SEASON_ID bigint(20) DEFAULT NULL,
  PAYMENT_AMT double(20,2) DEFAULT '0.00',
  TOTAL_AMOUNT double(20,2) DEFAULT NULL,
  IS_REG_SUPPLIER varchar(5) COLLATE utf8_unicode_ci DEFAULT NULL,
  MOBILE_NO varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  MASTER_TYPE varchar(8) COLLATE utf8_unicode_ci DEFAULT NULL,
  MASTER_TYPE_ID varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  SEASON_CODE varchar(25) COLLATE utf8_unicode_ci DEFAULT NULL,
  CREATED_DATE datetime DEFAULT NULL,
  CREATED_USER varchar(25) COLLATE utf8_unicode_ci DEFAULT NULL,
  UPDATED_USER varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  UPDATED_DATE datetime DEFAULT NULL,
  WAREHOUSE_ID varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  LATITUDE varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  LONGITUDE varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  BRANCH_ID varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (ID),
  KEY FK_SUPPLIER_PROCUREMENT_TRXN_AGRO (TRXN_AGRO_ID) USING BTREE,
  KEY FK_SUPPLIER_PROCUREMENT_SEASON_MASTER (SEASON_ID) USING BTREE,
  CONSTRAINT supplier_procurement_ibfk_2 FOREIGN KEY (TRXN_AGRO_ID) REFERENCES trxn_agro (ID) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT supplier_procurement_ibfk_3 FOREIGN KEY (SEASON_ID) REFERENCES season_master (ID) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for supplier_procurement_detail
-- ----------------------------
DROP TABLE IF EXISTS supplier_procurement_detail;
CREATE TABLE supplier_procurement_detail (
  ID bigint(20) NOT NULL AUTO_INCREMENT,
  FARMER_ID varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  FARMER_NAME varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  IS_REG_FARMER varchar(5) COLLATE utf8_unicode_ci DEFAULT NULL,
  MOBILE_NO varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  VILLAGE_CODE varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  QUALITY varchar(35) COLLATE utf8_unicode_ci DEFAULT NULL,
  PROCUREMENT_PRODUCT_ID bigint(20) DEFAULT NULL,
  PROCUREMENT_GRADE_ID bigint(20) DEFAULT NULL,
  BATCH_NO varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  NUMBER_OF_BAGS bigint(20) DEFAULT NULL,
  UNIT varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  CROP_TYPE bigint(10) DEFAULT NULL,
  GROSS_WEIGHT double(20,3) DEFAULT NULL,
  NET_WEIGHT double(20,3) DEFAULT NULL,
  RATE_PER_UNIT double(20,2) DEFAULT NULL,
  SUB_TOTAL double(20,3) DEFAULT NULL,
  SUPPLIER_PROCUREMENT_ID bigint(20) DEFAULT NULL,
  PRIMARY KEY (ID),
  KEY FK_SUPPLIER_PROCUREMENT_DETAIL_PROCUREMENT_PRODUCT (PROCUREMENT_PRODUCT_ID) USING BTREE,
  KEY FK_SUPPLIER_PROCUREMENT_DETAIL_SUPPLIER_PROCUREMENT (SUPPLIER_PROCUREMENT_ID) USING BTREE,
  KEY procurement_detail_ibfk_3 (PROCUREMENT_GRADE_ID) USING BTREE,
  CONSTRAINT supplier_procurement_detail_ibfk_1 FOREIGN KEY (PROCUREMENT_PRODUCT_ID) REFERENCES procurement_product (ID) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT supplier_procurement_detail_ibfk_2 FOREIGN KEY (SUPPLIER_PROCUREMENT_ID) REFERENCES supplier_procurement (ID) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT supplier_procurement_detail_ibfk_3 FOREIGN KEY (PROCUREMENT_GRADE_ID) REFERENCES procurement_grade (ID) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for supplier_warehouse
-- ----------------------------
DROP TABLE IF EXISTS supplier_warehouse;
CREATE TABLE supplier_warehouse (
  ID bigint(20) NOT NULL AUTO_INCREMENT,
  CO_OPERATIVE_ID bigint(20) DEFAULT NULL,
  PROCUREMENT_PRODUCT_ID bigint(20) DEFAULT NULL,
  NUMBER_OF_BAGS bigint(20) DEFAULT NULL,
  GROSS_WEIGHT double(20,3) DEFAULT NULL,
  AGENT_ID varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  QUALITY varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  IS_DELETE tinyint(4) DEFAULT '0',
  REVISION_NO bigint(20) DEFAULT '1',
  BRANCH_ID varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (ID),
  KEY FK_SUPPLIER_WAREHOUSE_WAREHOUSE (CO_OPERATIVE_ID) USING BTREE,
  KEY FK_SUPPLIER_WAREHOUSE_PROCUREMENT_PRODUCT (PROCUREMENT_PRODUCT_ID) USING BTREE,
  CONSTRAINT supplier_warehouse_ibfk_1 FOREIGN KEY (PROCUREMENT_PRODUCT_ID) REFERENCES procurement_product (ID) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT supplier_warehouse_ibfk_3 FOREIGN KEY (CO_OPERATIVE_ID) REFERENCES warehouse (ID) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for supplier_warehouse_detail
-- ----------------------------
DROP TABLE IF EXISTS supplier_warehouse_detail;
CREATE TABLE supplier_warehouse_detail (
  ID bigint(20) NOT NULL AUTO_INCREMENT,
  TXN_DATE datetime DEFAULT NULL,
  TYPEE tinyint(2) DEFAULT NULL,
  TXN_REFERENCE_ID bigint(20) DEFAULT NULL,
  PREVIOUS_NO_OF_BAGS bigint(20) DEFAULT NULL,
  PREVIOUS_GROSS_WEIGHT double(20,3) DEFAULT NULL,
  TXN_NO_OF_BAGS bigint(20) DEFAULT NULL,
  TXN_GROSS_WEIGHT double(20,3) DEFAULT NULL,
  TOTAL_NO_OF_BAGS bigint(20) DEFAULT NULL,
  TOTAL_GROSS_WEIGHT double(20,3) DEFAULT NULL,
  DESCRIPTION varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  BATCH_NO varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  SUPPLIER_WAREHOUSE_ID bigint(20) DEFAULT NULL,
  PRIMARY KEY (ID),
  KEY FK_SUPPLIER_WAREHOUSE_DETAIL_SUPPLIER_WAREHOUSE (SUPPLIER_WAREHOUSE_ID) USING BTREE,
  CONSTRAINT supplier_warehouse_detail_ibfk_1 FOREIGN KEY (SUPPLIER_WAREHOUSE_ID) REFERENCES supplier_warehouse (ID) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


-- -06-10-2017
ALTER TABLE dynamic_fields_config
ADD COLUMN REFERENCE_ID  bigint(40) NULL AFTER ORDER_SET;

-- - 05-10-2017 -- -
ALTER TABLE farmer_family
MODIFY COLUMN RELATION  varchar(20) NULL DEFAULT NULL AFTER NAME,
MODIFY COLUMN EDUCATION  varchar(20) NULL DEFAULT NULL AFTER GENDER,
ADD COLUMN DISABILITY  bigint(3) NULL DEFAULT 2 AFTER OTHER_PROFESSION,
ADD COLUMN MARITAL_STATUS  varchar(20) NULL DEFAULT NULL AFTER DISABILITY,
ADD COLUMN EDU_STATUS  varchar(20) NULL DEFAULT NULL AFTER MARITAL_STATUS;

ALTER TABLE farm
ADD COLUMN WATER_HARVEST  varchar(150) NULL AFTER FARM_PLAT_NO,
ADD COLUMN AVG_STORE  varchar(100) NULL AFTER WATER_HARVEST,
ADD COLUMN TREE_NAME  varchar(150) NULL AFTER AVG_STORE;


-- -10-06-2017 - --

ALTER TABLE farmer
ADD COLUMN SMART_PHONE  bigint(3) NULL DEFAULT NULL AFTER REF_ID;

ALTER TABLE animal_husbandary
ADD COLUMN MANURE_COLLECTED  varchar(20) NULL AFTER BREED,
ADD COLUMN URINE_COLLECTED  varchar(20) NULL AFTER MANURE_COLLECTED;

ALTER TABLE warehouse
ADD COLUMN TOTAL_MEMBERS  bigint(3) NULL DEFAULT NULL AFTER GROUP_TYPE,
ADD COLUMN GROUP_FORMATION_DATE  date NULL DEFAULT NULL AFTER TOTAL_MEMBERS,
ADD COLUMN NO_OF_MALE  bigint(3) NULL DEFAULT NULL AFTER GROUP_FORMATION_DATE,
ADD COLUMN NO_OF_FEMALE  bigint(3) NULL DEFAULT NULL AFTER NO_OF_MALE,
ADD COLUMN PRESIDENT_NAME  varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER NO_OF_FEMALE,
ADD COLUMN PRESIDENT_MOBILE_NUMBER  varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER PRESIDENT_NAME,
ADD COLUMN SECRETARY_NAME  varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER PRESIDENT_MOBILE_NUMBER,
ADD COLUMN SECRETARY_MOBILE_NUMBER  varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER SECRETARY_NAME,
ADD COLUMN TREASURER  varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER SECRETARY_MOBILE_NUMBER,
ADD COLUMN TREASURER_MOBILE_NUMBER  varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER TREASURER;


ALTER TABLE bank_info
ADD COLUMN WAREHOUSE_ID  bigint(20) NULL DEFAULT NULL AFTER ACC_TYPE,
ADD INDEX FK_WAREHOUSE (WAREHOUSE_ID) USING BTREE ;

ALTER TABLE bank_info ADD CONSTRAINT bank_info_ibfk_2 FOREIGN KEY (WAREHOUSE_ID) REFERENCES warehouse (ID) ON DELETE NO ACTION ON UPDATE NO ACTION;


alter table survey_question add column ANSWER_CAT_TYPE BIGINT(45);

alter table agent_prof add column language varchar(5);

-- -10 Oct 2017
ALTER TABLE supplier_procurement_detail
MODIFY COLUMN IS_REG_FARMER  varchar(10) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER FARMER_NAME;


ALTER TABLE dynamic_fields_config
ADD COLUMN DATA_FORMAT  varchar(45) NULL AFTER IS_REQUIRED;

ALTER TABLE dynamic_fields_config
ADD COLUMN VALIDATION  varchar(45) NULL AFTER DATA_FORMAT,
ADD COLUMN MIN_LENGTH  varchar(45) NULL AFTER VALIDATION,
ADD COLUMN LIST_METHOD_NAME  varchar(45) NULL AFTER MIN_LENGTH;


ALTER TABLE farmer_dynamic_field_value
ADD COLUMN COMPONENT_TYPE  varchar(45) NULL AFTER FARMER_ID;

ALTER TABLE dynamic_fields_config
ADD COLUMN DEPENDENCY_KEY  varchar(45) NULL AFTER PARENT_DEPENDANCY_ID;



ALTER TABLE farmer_dynamic_field_value
ADD COLUMN TYPEZ  varchar(45) NULL AFTER NAME;

ALTER TABLE dynamic_section_config
MODIFY COLUMN TYPEZ  varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '1' AFTER SECTION_CODE;



ALTER TABLE farmer_dynamic_field_value
ADD COLUMN REFERENCE_ID  varchar(45) NULL AFTER TYPEZ;


ALTER TABLE farmer_dynamic_field_value
ADD COLUMN TXN_TYPE  varchar(45) NULL AFTER REFERENCE_ID;


ALTER TABLE dynamic_section_config
ADD COLUMN M_TXN_TYPEZ  varchar(50) NULL AFTER TYPEZ;



ALTER TABLE dynamic_fields_config
ADD COLUMN IS_MOBILE_AVAILABLE  varchar(2) NULL AFTER REFERENCE_ID;

ALTER TABLE farmer_economy
ADD COLUMN QTY_COOKING_PERMONTH  varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER COOKING_FUEL_OTHER,
ADD COLUMN COST_COOKING_PERMONTH  varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER QTY_COOKING_PERMONTH;


ALTER TABLE farmer_dynamic_field_value
ADD COLUMN CREATED_DATE  datetime NULL AFTER TXN_TYPE,
ADD COLUMN CREATED_USER  varchar(100) NULL AFTER CREATED_DATE;

-- - 14-10-2017 -- -
ALTER TABLE farmer
MODIFY COLUMN FARMER_CROP_INSURANCE  varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL AFTER HEALTH_INSURANCE;


-- - 16-10-2017 -- -

-- ----------------------------
-- Table structure for procurement_traceability
-- ----------------------------
DROP TABLE IF EXISTS procurement_traceability;
CREATE TABLE procurement_traceability (
  ID bigint(20) NOT NULL AUTO_INCREMENT,
  FARMER_ID bigint(20) NOT NULL,
  WAREHOUSE_ID bigint(20) NOT NULL,
  RECEIPT_NO varchar(45) DEFAULT NULL,
  STRIP_POSITIVE bigint(2) DEFAULT NULL,
  PHOTO longblob,
  TRASH varchar(45) DEFAULT NULL,
  MOISTURE varchar(45) DEFAULT NULL,
  STAPLE_LEN varchar(45) DEFAULT NULL,
  KOWDI_KAPAS varchar(45) DEFAULT NULL,
  YELLOW_COTTON varchar(45) DEFAULT NULL,
  REVISON_NO bigint(20) DEFAULT NULL,
  BRANCH_ID varchar(45) DEFAULT NULL,
  CREATED_DATE datetime DEFAULT NULL,
  UPDATED_DATE datetime DEFAULT NULL,
  CREATED_USER varchar(45) DEFAULT NULL,
  UPDATED_USER varchar(45) DEFAULT NULL,
  SEASON varchar(45) NOT NULL,
  TRXN_ID bigint(20) DEFAULT NULL,
  TOTAL_AMOUNT double(20,2) DEFAULT '0.00',
  PAYMENT_AMOUNT double(20,2) DEFAULT '0.00',
  PROCUREMENT_DATE datetime DEFAULT NULL,
  PRIMARY KEY (ID),
  KEY fk_farmer (FARMER_ID) USING BTREE,
  KEY fk_warehouse (WAREHOUSE_ID) USING BTREE,
  CONSTRAINT procurement_traceability_ibfk_1 FOREIGN KEY (FARMER_ID) REFERENCES farmer (ID) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT procurement_traceability_ibfk_2 FOREIGN KEY (WAREHOUSE_ID) REFERENCES warehouse (ID) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for procurement_traceability_details
-- ----------------------------
DROP TABLE IF EXISTS procurement_traceability_details;
CREATE TABLE procurement_traceability_details (
  ID bigint(20) NOT NULL AUTO_INCREMENT,
  PROCUREMENT_TRACEABILITY_ID bigint(20) DEFAULT NULL,
  GRADE_ID bigint(20) DEFAULT NULL,
  UNIT varchar(45) DEFAULT NULL,
  PRICE double(20,2) DEFAULT '0.00',
  PREMIUM_PRICE double(20,2) DEFAULT '0.00',
  PRO_PRODUCT_ID bigint(20) DEFAULT NULL,
  NET_WEIGHT double(20,2) DEFAULT '0.00',
  TOTAL_PRICE_PREMIUM double(20,2) DEFAULT '0.00',
  TOTAL_PRICE double(20,2) DEFAULT '0.00',
  NUMBER_OF_BAGS bigint(20) DEFAULT NULL,
  PRIMARY KEY (ID),
  KEY fk_procurment_traceability (PROCUREMENT_TRACEABILITY_ID) USING BTREE,
  KEY fk_grade (GRADE_ID) USING BTREE,
  CONSTRAINT procurement_traceability_details_ibfk_1 FOREIGN KEY (GRADE_ID) REFERENCES procurement_grade (ID) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT procurement_traceability_details_ibfk_2 FOREIGN KEY (PROCUREMENT_TRACEABILITY_ID) REFERENCES procurement_traceability (ID) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;


-- ----------------------------
-- Table structure for procurement_traceability_stock
-- ----------------------------
DROP TABLE IF EXISTS procurement_traceability_stock;
CREATE TABLE procurement_traceability_stock (
  ID bigint(20) NOT NULL AUTO_INCREMENT,
  PROCUREMENT_PRODUCT_ID bigint(20) DEFAULT NULL,
  CITY_ID bigint(20) DEFAULT NULL,
  VILLAGE_ID bigint(20) DEFAULT NULL,
  CO_OPERATIVE_ID bigint(20) DEFAULT NULL,
  NUMBER_OF_BAGS bigint(20) DEFAULT '0',
  TOTAL_STOCK double(10,5) DEFAULT '0.00000',
  AGENT_ID varchar(45) DEFAULT NULL,
  GRADE varchar(45) DEFAULT NULL,
  REVISION_NO bigint(45) DEFAULT NULL,
  ICS varchar(45) DEFAULT NULL,
  RECEIPT_NO varchar(45) DEFAULT NULL,
  PRIMARY KEY (ID),
  KEY procurementTrace_ibfk_1 (PROCUREMENT_PRODUCT_ID) USING BTREE,
  KEY procurementTrace_ibfk_2 (CITY_ID) USING BTREE,
  KEY procurementTrace_ibfk_3 (VILLAGE_ID) USING BTREE,
  KEY procurementTrace_ibfk_4 (CO_OPERATIVE_ID) USING BTREE,
  CONSTRAINT FK_PROCUREMENTTRACR_PRODUCT FOREIGN KEY (PROCUREMENT_PRODUCT_ID) REFERENCES procurement_product (ID) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT FK_PROCUREMENT_TRACE_CITY FOREIGN KEY (CITY_ID) REFERENCES city (ID) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT FK_PROCUREMENT_TRACE_COOPERATIVE FOREIGN KEY (CO_OPERATIVE_ID) REFERENCES warehouse (ID) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT FK_PROCUREMENT_TRACE_VILLAGE FOREIGN KEY (VILLAGE_ID) REFERENCES village (ID) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for procurement_traceability_stock_details
-- ----------------------------
DROP TABLE IF EXISTS procurement_traceability_stock_details;
CREATE TABLE procurement_traceability_stock_details (
  ID bigint(20) NOT NULL AUTO_INCREMENT,
  TXN_DATE datetime DEFAULT NULL,
  TXN_REFERENCE_ID bigint(20) DEFAULT NULL,
  PREVIOUS_NO_OF_BAGS bigint(20) DEFAULT '0',
  TOTAL_NO_OF_BAGS bigint(20) DEFAULT '0',
  TXN_NO_OF_BAGS bigint(20) DEFAULT NULL,
  PREVIOUS_STOCK double(20,5) DEFAULT '0.00000',
  TXN_STOCK double(20,5) DEFAULT '0.00000',
  TOTAL_STOCK double(20,5) DEFAULT '0.00000',
  DESCRIPTION varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  PROCUREMENT_TRACEABILITY_STOCK_ID bigint(20) DEFAULT NULL,
  FARMER_ID varchar(45) DEFAULT NULL,
  BRANCH_ID varchar(45) DEFAULT NULL,
  PRIMARY KEY (ID),
  KEY procurementTraceIndex1 (PROCUREMENT_TRACEABILITY_STOCK_ID) USING BTREE,
  CONSTRAINT FKB_1 FOREIGN KEY (PROCUREMENT_TRACEABILITY_STOCK_ID) REFERENCES procurement_traceability_stock (ID) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




-- -17-10-2017

ALTER TABLE procurement_traceability
ADD COLUMN LATITUDE  varchar(30) NULL AFTER PROCUREMENT_DATE,
ADD COLUMN LONGITUDE  varchar(30) NULL AFTER LATITUDE;


ALTER TABLE procurement_product
ADD COLUMN MSP_RATE  double(15,0) NULL AFTER BRANCH_ID,
ADD COLUMN MSP_PERCENTAGE  int(10) NULL AFTER MSP_RATE;



-- -23-10-2017
DROP TABLE IF EXISTS offline_procurement_traceability;
CREATE TABLE offline_procurement_traceability (
  ID bigint(20) NOT NULL AUTO_INCREMENT,
  FARMER_ID varchar(20) NOT NULL,
  WAREHOUSE_ID varchar(20) DEFAULT NULL,
  RECEIPT_NO varchar(45) DEFAULT NULL,
  STRIP_POSITIVE bigint(2) DEFAULT NULL,
  PHOTO longblob,
  LATITUDE varchar(45) DEFAULT NULL,
  LONGITUDE varchar(45) DEFAULT NULL,
  TRASH bigint(2) DEFAULT NULL,
  MOISTURE bigint(2) DEFAULT NULL,
  STAPLE_LEN bigint(2) DEFAULT NULL,
  KOWDI_KAPAS bigint(2) DEFAULT NULL,
  YELLOW_COTTON bigint(2) DEFAULT NULL,
  BRANCH_ID varchar(45) DEFAULT NULL,
  CREATED_DATE datetime DEFAULT NULL,
  UPDATED_DATE datetime DEFAULT NULL,
  SEASON_ID varchar(20) NOT NULL,
  TOTAL_AMOUNT double(20,2) DEFAULT NULL,
  STATUS_CODE tinyint(1) DEFAULT NULL,
  STATUS_MSG varchar(500) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  CREATED_USER varchar(40) DEFAULT NULL,
  UPDATED_USER varchar(40) DEFAULT NULL,
  PROCUREMENT_DATE datetime DEFAULT NULL,
  DEVICE_ID varchar(75) DEFAULT NULL,
  AGENT_ID varchar(25) DEFAULT NULL,
  VILLAGE_CODE varchar(30) DEFAULT NULL,
  PRIMARY KEY (ID),
  KEY fk_farmer (FARMER_ID) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;



DROP TABLE IF EXISTS offline_procurement_traceability_details;
CREATE TABLE offline_procurement_traceability_details (
  ID bigint(20) NOT NULL AUTO_INCREMENT,
  OFFLINE_PROCUREMENT_TRACEABILITY_ID varchar(20) DEFAULT NULL,
  GRADE_ID varchar(20) DEFAULT NULL,
  UNIT varchar(45) DEFAULT NULL,
  PRICE double(20,2) DEFAULT '0.00',
  PREMIUM_PRICE double(20,2) DEFAULT '0.00',
  PRO_PRODUCT_ID varchar(20) DEFAULT NULL,
  NET_WEIGHT double(20,2) DEFAULT '0.00',
  TOTAL_PRICE_PREMIUM double(20,2) DEFAULT '0.00',
  TOTAL_PRICE double(20,2) DEFAULT '0.00',
  NUMBER_OF_BAGS varchar(20) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;




-- - 10-11-2017 (Prod-agro-theme)- --
INSERT INTO locale_property (id, code, lang_code, lang_value) VALUES (NULL, 'exportColumnHeaderDistributionBranchfarmer', 'en', 'S.No,Organization,Date,Stock Type,Warehouse,MobileUser,Farmer Type,Farmer,Village,Free Distribution,Total Qty,Gross Amount,Final Amount,Payment Amount,Season');
INSERT INTO locale_property (id, code, lang_code, lang_value) VALUES (NULL, 'exportSubColumnHeaderDistributionfarmer', 'en', 'Category,Product,Unit,Existing Qty,CostPrice (INR),Amount (INR),Distribution Qty,Available Qty');
INSERT INTO locale_property (id, code, lang_code, lang_value) VALUES (NULL, 'exportColumnHeaderDistributionBranchagent', 'en', 'S.No,Organization,Date,Warehouse,Mobile User,Category,Product,Unit,Existing qty,Distribution Qty,Current Qty,Season');=======

-- - 10-23-2017- --
INSERT INTO ese_menu VALUES (NULL , 'report.procurementTraceability', 'Procurement Traceability Report', 'procurementTraceabilityReport_list.action', '26','4','0','0','0','0',NULL);
INSERT INTO ese_ent VALUES (NULL, 'report.procurementTraceability.list');
INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='report.procurementTraceability'), '1');
INSERT INTO ese_role_ent VALUES ('1', (SELECT id FROM ese_ent WHERE NAME='report.procurementTraceability.list'));
INSERT INTO ese_role_menu VALUES (NULL, (SELECT MAX(id) FROM ese_menu), '1');

ALTER TABLE trxn_agro ADD COLUMN PROCUREMENT_TRACEABILITY_ID  bigint(20) NULL DEFAULT NULL AFTER PRODUCT_RETURN_ID;


-- -24-10-2017

INSERT INTO txn_type (ID, CODE, NAME) VALUES (null, '383', 'procurementTraceabilityAdapter');


ALTER TABLE pmt_detail
ADD COLUMN ICS  varchar(100) NULL AFTER PROCUREMENT_GRADE_ID;

-- ----------------------------
-- Table structure for pmt_farmer_detail
-- ----------------------------
DROP TABLE IF EXISTS pmt_farmer_detail;
CREATE TABLE pmt_farmer_detail (
  ID bigint(20) NOT NULL AUTO_INCREMENT,
  PMT_ID bigint(20) DEFAULT NULL,
  VILLAGE_ID bigint(20) DEFAULT NULL,
  PROCUREMENT_PRODUCT_ID bigint(20) DEFAULT NULL,
  GRADE_MASTER_ID bigint(20) DEFAULT NULL,
  PROCUREMENT_GRADE_ID bigint(20) DEFAULT NULL,
  MTNT_GROSS_WEIGHT double(20,3) DEFAULT NULL,
  MTNR_GROSS_WEIGHT double(20,3) DEFAULT NULL,
  MTNT_NO_OF_BAGS bigint(20) DEFAULT NULL,
  MTNR_NO_OF_BAGS bigint(20) DEFAULT NULL,
  COOPRATIVE_ID bigint(20) DEFAULT NULL,
  FARMER_ID bigint(20) DEFAULT NULL,
  PRICE_PER_UNIT varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  SUB_TOTAL varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  UOM varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  BRANCH_ID varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  STATUS bigint(1) DEFAULT '0',
  PRIMARY KEY (ID),
  KEY FK_PMT_DETAIL_PMT (PMT_ID) USING BTREE,
  KEY FK_PMT_DETAIL_VILLAGE (VILLAGE_ID) USING BTREE,
  KEY FK_PMT_DETAIL_PROCUREMENT_PRODUCT (PROCUREMENT_PRODUCT_ID) USING BTREE,
  KEY FK_PMT_DETAIL_GRADE_MASTER (GRADE_MASTER_ID) USING BTREE,
  KEY pmt_detail_ibfk_5 (PROCUREMENT_GRADE_ID) USING BTREE,
  KEY FK_PMT_DETAIL_FARMER_ID (FARMER_ID) USING BTREE,
  CONSTRAINT pmt_farmer_detail_ibfk_1 FOREIGN KEY (PROCUREMENT_GRADE_ID) REFERENCES procurement_grade (ID) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT pmt_farmer_detail_ibfk_2 FOREIGN KEY (FARMER_ID) REFERENCES farmer (ID) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT pmt_farmer_detail_ibfk_3 FOREIGN KEY (PMT_ID) REFERENCES pmt (ID) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT pmt_farmer_detail_ibfk_4 FOREIGN KEY (VILLAGE_ID) REFERENCES village (ID) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT pmt_farmer_detail_ibfk_5 FOREIGN KEY (PROCUREMENT_PRODUCT_ID) REFERENCES procurement_product (ID) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT pmt_farmer_detail_ibfk_6 FOREIGN KEY (GRADE_MASTER_ID) REFERENCES grade_master (ID) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- -26-10-2017- --
ALTER TABLE farmer_family
ADD COLUMN DISABLE_DETAIL  varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER DISABILITY;

-- - 27-10-2017- -- 

ALTER TABLE dynamic_fields_config
ADD COLUMN IS_REPORT_AVAILABLE  varchar(2) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER IS_MOBILE_AVAILABLE;

ALTER TABLE farmer_dynamic_field_value
ADD COLUMN TXN_UNIQUE_ID  bigint(20) NULL DEFAULT NULL AFTER CREATED_USER;

DROP TABLE IF EXISTS farmer_dynamic_data;
CREATE TABLE farmer_dynamic_data (
  ID bigint(15) NOT NULL AUTO_INCREMENT,
  TXN_UNIQUE_ID varchar(45) DEFAULT NULL,
  REFERENCE_ID varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  FARMER_ID varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  TXN_TYPE varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  DATE datetime DEFAULT NULL,
  LATITUDE varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  LONGITUDE varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  CREATED_DATE date DEFAULT NULL,
  CREATED_USER varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

ALTER TABLE farmer_dynamic_field_value
ADD COLUMN FARMER_DYNAMIC_DATA_ID  bigint(20) NULL DEFAULT NULL AFTER TXN_UNIQUE_ID;

INSERT INTO txn_type (ID, CODE, NAME) VALUES (null, '384', 'shgroupRecord');

-- - 28Oct2017
ALTER TABLE farmer
ADD COLUMN MSG_NO  varchar(45) NULL ;

ALTER TABLE offline_procurement_traceability
ADD COLUMN PAYMENT_AMOUNT  double(20,2) NULL DEFAULT '0.00' AFTER VILLAGE_CODE;

ALTER TABLE procurement_product
MODIFY COLUMN MSP_RATE  double(15,3) NULL DEFAULT NULL AFTER BRANCH_ID;

ALTER TABLE product_return
ADD COLUMN STOCK_TYPE  varchar(45) NULL AFTER UPDATED_TIME;

UPDATE dynamic_report_config_detail SET FIELD='stockType', ACESS_TYPE='1', METHOD='' WHERE (ID='10')

ALTER TABLE offline_product_return
ADD COLUMN STOCK_TYPE  varchar(45) NULL AFTER CURRENT_SEASON_CODE;


UPDATE product_return
SET STOCK_TYPE = 'WarehouseStock'
WHERE
	SERVICE_POINT_ID IS NOT NULL
AND SERVICE_POINT_ID != "";


UPDATE product_return
SET STOCK_TYPE = 'MobileUserStock'
WHERE
	AGENT_ID IS NOT NULL
AND TXN_TYPE='344';

ALTER TABLE dynamic_section_config
ADD COLUMN SECTION_ORDER  varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER M_TXN_TYPEZ;

-- -31-10-2017
ALTER TABLE farm ADD COLUMN REVISION_NO BIGINT(20);
ALTER TABLE farm_crops ADD COLUMN  REVISION_NO BIGINT(20);
-- -Run this once
update farm f join farmer fm on fm.id  = f.farmer_id set f.REVISION_NO = fm.REVISION_NO;
update farm_crops fc join farm f  on f.id = fc.farm_id join farmer fm on fm.id  = f.farmer_id set fc.REVISION_NO = f.REVISION_NO;

INSERT INTO ese_txn_type (ID, CODE, NAME) VALUES (null, '385', 'farmDownload');
INSERT INTO ese_txn_type (ID, CODE, NAME) VALUES (null, '386', 'farmCropsDownload');


INSERT INTO txn_type (ID, CODE, NAME) VALUES (null, '385', 'farmDownload');
INSERT INTO txn_type (ID, CODE, NAME) VALUES (null, '386', 'farmCropsDownload');

-- -06 NOV 2017
-- -Run for all tenant
ALTER TABLE farm
ADD COLUMN DISTANCE_PROCESSING_UNIT  varchar(45) NULL AFTER REVISION_NO;

ALTER TABLE farmer
ADD COLUMN MASTER_DATA  varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER MSG_NO;

ALTER TABLE farm_crops
ADD COLUMN NO_OF_TREES  varchar(20) NULL AFTER REVISION_NO;

ALTER TABLE supplier_procurement
ADD COLUMN TRADER  varchar(45) NULL AFTER MASTER_TYPE_ID;

ALTER TABLE farm_detailed_info
ADD COLUMN PROCESSING_ACTIVITY  bigint(3) NULL AFTER FFS_BENIFIT;

ALTER TABLE farm_ics_conversion
ADD COLUMN ORGANIC_STATUS  varchar(45) NULL AFTER STATUS;

ALTER TABLE farm_detailed_info
MODIFY COLUMN PROCESSING_ACTIVITY  bigint(3) NULL DEFAULT '-1' AFTER FFS_BENIFIT;

update farm_detailed_info fd set fd.PROCESSING_ACTIVITY='-1' where fd.PROCESSING_ACTIVITY is NULL ;


-- -07-11-2017
DROP TABLE IF EXISTS gmo;
CREATE TABLE gmo (
  ID bigint(15) unsigned NOT NULL AUTO_INCREMENT,
  NO_OF_SAMPLES double(20,2) DEFAULT NULL,
  NO_OF_POSITIVE double(20,2) DEFAULT NULL,
  CONTAMINATION_PERCENTAGE double(20,2) DEFAULT NULL,
  TYPE varchar(10) DEFAULT NULL,
  SEASON_CODE varchar(30) DEFAULT NULL,
  BRANCH_ID varchar(30) DEFAULT NULL,
  CREATED_DATE datetime DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


CREATE TABLE ginner_quantity_sold (
  ID bigint(10) unsigned NOT NULL AUTO_INCREMENT,
  NAME varchar(40) DEFAULT NULL,
  QUANTITY double(15,2) DEFAULT NULL,
  ADDRESS varchar(250) DEFAULT NULL,
  SEASON_CODE varchar(35) DEFAULT NULL,
  BRANCH_ID varchar(30) DEFAULT NULL,
  CREATED_DATE datetime DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- -GMO and Ginner Quantity Menu for canda
INSERT INTO ese_menu VALUES (NULL, 'profile.gmo', 'GMO', 'gmo_list.action', '8', '2', '0', '0', '0', '0', NULL);
INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='profile.gmo'), '1');
INSERT INTO ese_ent VALUES (null, 'profile.gmo.list');
INSERT INTO ese_role_ent (ROLE_ID, ENT_ID) VALUES ('1', (SELECT id FROM ese_ent WHERE NAME='profile.gmo.list'));
INSERT INTO ese_role_menu VALUES (NULL, (SELECT id FROM ese_menu WHERE NAME='profile.gmo'), '1');

INSERT INTO ese_menu VALUES (NULL, 'profile.ginnerQuantitySold', 'Ginner Qunatity Sold', 'ginnerQuantitySold_list.action', '9', '2', '0', '0', '0', '0', NULL);
INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='profile.ginnerQuantitySold'), '1');
INSERT INTO ese_ent VALUES (null, 'profile.ginnerQuantitySold.list');
INSERT INTO ese_role_ent (ROLE_ID, ENT_ID) VALUES ('1', (SELECT id FROM ese_ent WHERE NAME='profile.ginnerQuantitySold.list'));
INSERT INTO ese_role_menu VALUES (NULL, (SELECT id FROM ese_menu WHERE NAME='profile.ginnerQuantitySold'), '1');

INSERT INTO locale_property (id, code, lang_code, lang_value) VALUES (null, 'dashboard.dashboard', 'en', 'Programme Impacts');


-- ----------------------------
-- Table structure for dynamic_image_data
-- ----------------------------
DROP TABLE IF EXISTS dynamic_image_data;
CREATE TABLE dynamic_image_data (
  ID bigint(20) NOT NULL AUTO_INCREMENT,
  FARMER_DYNMAIC_FIELD_ID bigint(20) DEFAULT NULL,
  IMAGE blob,
  PHOTO_CAPTURE_TIME datetime DEFAULT NULL,
  LATITUDE varchar(45) DEFAULT NULL,
  LONGITUDE varchar(45) DEFAULT NULL,
  ORDERZ varchar(45) DEFAULT NULL,
  PRIMARY KEY (ID),
  KEY fk_fdfv (FARMER_DYNMAIC_FIELD_ID),
  CONSTRAINT fk_fdfv FOREIGN KEY (FARMER_DYNMAIC_FIELD_ID) REFERENCES farmer_dynamic_field_value (ID) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- - 10-11-2017 - --
ALTER TABLE pmt
ADD COLUMN TRANSPORTER  varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER TRANSFER_INFO;

SET FOREIGN_KEY_CHECKS=0;
DROP TABLE IF EXISTS heap_data;
CREATE TABLE heap_data (
  ID bigint(20) NOT NULL AUTO_INCREMENT,
  CREATED_DATE datetime DEFAULT NULL,
  NAME varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  COOPERATIVE bigint(20) DEFAULT NULL,
  GINNING bigint(20) DEFAULT NULL,
  PRODUCT bigint(20) DEFAULT NULL,
  ICS varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  TOTAL_STOCK double(20,3) DEFAULT NULL,
  TOTAL_LINT_COTTON double(20,3) DEFAULT NULL,
  TOTAL_SEED_COTTON double(20,3) DEFAULT NULL,
  TOTAL_SCRUP double(20,3) DEFAULT NULL,
  BRANCH varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (ID),
  KEY heap_inx_1 (COOPERATIVE) USING BTREE,
  KEY heap_inx_2 (PRODUCT) USING BTREE,
  KEY heap_inx_3 (GINNING) USING BTREE,
  CONSTRAINT heap_fk_cooperative FOREIGN KEY (COOPERATIVE) REFERENCES warehouse (ID) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT heap_fk_ginning FOREIGN KEY (GINNING) REFERENCES warehouse (ID) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT heap_fk_product FOREIGN KEY (PRODUCT) REFERENCES procurement_product (ID) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;


SET FOREIGN_KEY_CHECKS=0;
DROP TABLE IF EXISTS heap_data_detail;
CREATE TABLE heap_data_detail (
  ID bigint(20) NOT NULL AUTO_INCREMENT,
  TXN_DATE datetime DEFAULT NULL,
  HEAP_DATA_ID bigint(20) DEFAULT NULL,
  PMT_DETAIL_ID bigint(20) DEFAULT NULL,
  GRADE_ID bigint(20) DEFAULT NULL,
  PREVIOUS_STOCK double(20,3) DEFAULT NULL,
  TXN_STOCK double(20,3) DEFAULT NULL,
  TOTAL_STOCK double(20,3) DEFAULT NULL,
  LINT_COTTON double(20,3) DEFAULT NULL,
  SEED_COTTON double(20,3) DEFAULT NULL,
  SCRUP double(20,3) DEFAULT NULL,
  STATUS tinyint(3) DEFAULT '0',
  DESCRIPTION varchar(200) DEFAULT NULL,
  PRIMARY KEY (ID),
  KEY indx_heap_data (HEAP_DATA_ID) USING BTREE,
  CONSTRAINT fk_heap_data FOREIGN KEY (HEAP_DATA_ID) REFERENCES heap_data (ID) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;


ALTER TABLE farm
ADD COLUMN STATUS  bigint(10) default 1;
ALTER TABLE farm_crops
ADD COLUMN STATUS  bigint(10) default 1;

ALTER TABLE warehouse
ADD COLUMN LATITUDE  varchar(20) NULL AFTER TREASURER_MOBILE_NUMBER,
ADD COLUMN LONGITUDE  varchar(20) NULL AFTER LATITUDE;

-- -20-11-2017
DROP TABLE IF EXISTS offline_pmtnr;
CREATE TABLE offline_pmtnr (
  ID bigint(15) unsigned NOT NULL AUTO_INCREMENT,
  CO_OPERATIVE_CODE varchar(20) DEFAULT NULL,
  MTNR_DATE varchar(50) DEFAULT NULL,
  RECEIPT_NO varchar(25) DEFAULT NULL,
  AGENT_ID varchar(25) DEFAULT NULL,
  TRUCK_ID varchar(25) DEFAULT NULL,
  DRIVER_NAME varchar(20) DEFAULT NULL,
  SEASON_CODE varchar(30) DEFAULT NULL,
  DEVICE_ID varchar(50) DEFAULT NULL,
  BRANCH_ID varchar(25) DEFAULT NULL,
  CREATED_USER varchar(30) DEFAULT NULL,
  UPDATED_USER varchar(30) DEFAULT NULL,
  CREATED_DATE varchar(25) DEFAULT NULL,
  STATUS_CODE varchar(30) DEFAULT NULL,
  STATUS_MSG varchar(35) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS offline_pmtnr_details;
CREATE TABLE offline_pmtnr_details (
  ID bigint(15) unsigned NOT NULL AUTO_INCREMENT,
  PMTNR_ID varchar(20) DEFAULT NULL,
  PROCUREMENT_PRODUCT_ID varchar(25) DEFAULT NULL,
  PROCUREMENT_GRADE_ID varchar(25) DEFAULT NULL,
  GROSS_WEIGHT varchar(20) DEFAULT NULL,
  NO_OF_BAGS varchar(25) DEFAULT NULL,
  CO_OPERATIVE_CODE varchar(20) DEFAULT NULL,
  ICS varchar(35) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS offline_pmt_image_details;
CREATE TABLE offline_pmt_image_details (
  ID bigint(20) NOT NULL AUTO_INCREMENT,
  PMT_ID bigint(20) DEFAULT NULL,
  PHOTO longblob,
  PHOTO_CAPTURE_TIME datetime DEFAULT NULL,
  LATITUDE varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  LONGITUDE varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS pmt_image_details;
CREATE TABLE pmt_image_details (
  ID bigint(20) NOT NULL AUTO_INCREMENT,
  PMT_ID bigint(20) DEFAULT NULL,
  PHOTO longblob,
  PHOTO_CAPTURE_TIME datetime DEFAULT NULL,
  LATITUDE varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  LONGITUDE varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

INSERT INTO txn_type (ID, CODE, NAME) VALUES (null, '388', 'transferStockDownload');
INSERT INTO txn_type (ID, CODE, NAME) VALUES (null, '389', 'productReceptionAdapter');
ALTER TABLE dynamic_fields_config
ADD COLUMN FORMULA  varchar(255) NULL AFTER IS_REPORT_AVAILABLE;

ALTER TABLE prof
ADD COLUMN WAREHOUSE_ID  bigint(20) NULL DEFAULT  AFTER UPDATED_USER;
ALTER TABLE prof
ADD INDEX FK_WAREHOUSE (WAREHOUSE_ID) USING BTREE ;
ALTER TABLE prof ADD CONSTRAINT profile_ibfk_1 FOREIGN KEY (WAREHOUSE_ID) REFERENCES warehouse (ID) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE offline_mtnt_detail
ADD COLUMN ICS_CODE  varchar(45) NULL AFTER UOM;

INSERT INTO txn_type VALUES (null, '390', 'transferTraceabilityAdapter');

-- -22-11-2017 - --
DROP TABLE IF EXISTS ginning_process;
CREATE TABLE ginning_process (
  ID bigint(20) NOT NULL AUTO_INCREMENT,
  PROCESS_DATE datetime DEFAULT NULL,
  GINNING_ID bigint(20) DEFAULT NULL,
  HEAP_DATA_ID bigint(20) DEFAULT NULL,
  TOT_LINT_COTTON double(20,5) DEFAULT NULL,
  TOT_SEED_COTTON double(20,5) DEFAULT NULL,
  TOT_SCRUP double(20,5) DEFAULT NULL,
  PRIMARY KEY (ID),
  KEY indx_ginning (GINNING_ID) USING BTREE,
  KEY indx_heapData (HEAP_DATA_ID) USING BTREE,
  CONSTRAINT fk_ginning FOREIGN KEY (GINNING_ID) REFERENCES warehouse (ID) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_heapData FOREIGN KEY (HEAP_DATA_ID) REFERENCES heap_data (ID) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;


-- - 23-11-2017 - --
ALTER TABLE pmt_detail
ADD COLUMN MTN_QUINTAL_WEIGHT  double(20,3) NULL DEFAULT NULL AFTER MTNT_GROSS_WEIGHT;
alter table pmt_farmer_detail  add column  ICS varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL;
update pmt_farmer_detail pdf join farmer f on f.id = pdf.FARMER_ID set pdf.ICS = f.ICS_NAME

-- - 27-11-2017 - --
ALTER TABLE procurement_product
MODIFY COLUMN MSP_PERCENTAGE  double(10,2) NULL DEFAULT NULL AFTER MSP_RATE;

-- -29-11-2017

CREATE TABLE cotton_price (
  ID bigint(15) unsigned NOT NULL AUTO_INCREMENT,
  PRICE double(25,2) DEFAULT NULL,
  MSP double(20,2) DEFAULT NULL,
  STAPLE_LENGTH varchar(30) DEFAULT NULL,
  SEASON_CODE varchar(30) DEFAULT NULL,
  BRANCH_ID varchar(30) DEFAULT NULL,
  CREATED_DATE datetime DEFAULT NULL,
  UPDATED_DATE datetime DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


ALTER TABLE offline_mtnt 
ADD COLUMN TRANSPORTER varchar(250) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER TOTAL_AMOUNT;

-- - 30-11-2017 -- -
ALTER TABLE offline_pmtnr_details 
MODIFY COLUMN PMTNR_ID varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER ID,
MODIFY COLUMN PROCUREMENT_PRODUCT_ID varchar(25) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER PMTNR_ID,
MODIFY COLUMN PROCUREMENT_GRADE_ID varchar(25) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER PROCUREMENT_PRODUCT_ID,
MODIFY COLUMN GROSS_WEIGHT varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER PROCUREMENT_GRADE_ID,
MODIFY COLUMN NO_OF_BAGS varchar(25) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER GROSS_WEIGHT,
MODIFY COLUMN CO_OPERATIVE_CODE varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER NO_OF_BAGS,
MODIFY COLUMN ICS varchar(35) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER CO_OPERATIVE_CODE,
ADD COLUMN TRANSFER_BAGS varchar(25) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER ICS,
ADD COLUMN TRANSFER_WEIGHT varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER TRANSFER_BAGS;


ALTER TABLE offline_pmtnr 
ADD COLUMN TRANSPORTER varchar(250) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER DRIVER_NAME;

-- - 01 DEC 2017
ALTER TABLE farmer_dynamic_data
ADD COLUMN ENTITY_ID varchar(45) NULL AFTER CREATED_USER,
ADD COLUMN CONVERSION_STATUS varchar(45) NULL AFTER ENTITY_ID,
ADD COLUMN CORRECTIVE_ACTION_PLAN varchar(45) NULL AFTER CONVERSION_STATUS,
ADD COLUMN STATUS varchar(45) NULL AFTER CORRECTIVE_ACTION_PLAN;

-- - cotton price menu for canda

INSERT INTO ese_menu VALUES (NULL, 'profile.cottonPrice', 'Cotton Price', 'cottonPrice_list.action', '10', '2', '0', '0', '0', '0', NULL);
INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='profile.cottonPrice'), '1');
INSERT INTO ese_ent VALUES (null, 'profile.cottonPrice.list');
INSERT INTO ese_role_ent (ROLE_ID, ENT_ID) VALUES ('1', (SELECT id FROM ese_ent WHERE NAME='profile.cottonPrice.list'));
INSERT INTO ese_role_menu VALUES (NULL, (SELECT id FROM ese_menu WHERE NAME='profile.cottonPrice'), '1');

-- - 05 DEC 2017
-- - crop sale image Table
DROP TABLE IF EXISTS crop_supply_image;
CREATE TABLE crop_supply_image  (
  ID bigint(15) UNSIGNED NOT NULL AUTO_INCREMENT,
  CROP_SUPPLY_ID bigint(15) NULL DEFAULT NULL,
  PHOTO longblob NULL,
  PHOTO_CAPTURE_TIME datetime NULL DEFAULT NULL,
  LATITUDE varchar(25) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  LONGITUDE varchar(25) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (ID) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO ese_menu (ID, NAME, DES, URL, ORD, PARENT_ID, FILTER, EXPORT_AVILABILITY, DIMENSION, PRIORITY, ICON_CLASS) VALUES (NULL, 'admin.menuCreationToolGrid', 'Menu Creation Tool Grid', 'menuCreationToolGrid_list.action', '12', '5', '0', '0', '0', '0', NULL);
INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='admin.menuCreationToolGrid'), '2');
INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='admin.menuCreationToolGrid'), '1');
INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='admin.menuCreationToolGrid'), '3');
INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='admin.menuCreationToolGrid'), '4');
INSERT INTO ese_ent (ID, NAME) VALUES (NULL, 'admin.menuCreationToolGrid.create');
INSERT INTO ese_ent (ID, NAME) VALUES (NULL, 'admin.menuCreationToolGrid.list');
INSERT INTO ese_ent (ID, NAME) VALUES (NULL, 'admin.menuCreationToolGrid.update');
INSERT INTO ese_ent (ID, NAME) VALUES (NULL, 'admin.menuCreationToolGrid.delete');
INSERT INTO ese_role_ent VALUES ('1', (SELECT id FROM ese_ent WHERE NAME='admin.menuCreationToolGrid.create'));
INSERT INTO ese_role_ent VALUES ('1', (SELECT id FROM ese_ent WHERE NAME='admin.menuCreationToolGrid.list'));
INSERT INTO ese_role_ent VALUES ('1', (SELECT id FROM ese_ent WHERE NAME='admin.menuCreationToolGrid.update'));
INSERT INTO ese_role_ent VALUES ('1', (SELECT id FROM ese_ent WHERE NAME='admin.menuCreationToolGrid.delete'));

INSERT INTO ese_role_menu VALUES (NULL, (SELECT MAX(id) FROM ese_menu), '1');

ALTER TABLE farmer
MODIFY COLUMN FINGER_PRINT  longblob NULL AFTER SEASON_CODE;

ALTER TABLE procurement_product 
MODIFY COLUMN MSP_PERCENTAGE double(10, 3) NULL DEFAULT NULL AFTER MSP_RATE;

ALTER TABLE offline_pmtnr_details 
ADD COLUMN HEAP varchar(25) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER TRANSFER_WEIGHT;

ALTER TABLE pmt_detail 
ADD COLUMN HEAP varchar(25) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER STATUS;

ALTER TABLE heap_data 
ADD COLUMN CODE varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER NAME;

ALTER TABLE heap_data_detail 
MODIFY COLUMN PREVIOUS_STOCK double(20, 3) NULL DEFAULT 0 AFTER GRADE_ID,
MODIFY COLUMN TXN_STOCK double(20, 3) NULL DEFAULT 0 AFTER PREVIOUS_STOCK,
MODIFY COLUMN TOTAL_STOCK double(20, 3) NULL DEFAULT 0 AFTER TXN_STOCK,
MODIFY COLUMN LINT_COTTON double(20, 3) NULL DEFAULT 0 AFTER TOTAL_STOCK,
MODIFY COLUMN SEED_COTTON double(20, 3) NULL DEFAULT 0 AFTER LINT_COTTON,
MODIFY COLUMN SCRUP double(20, 3) NULL DEFAULT 0 AFTER SEED_COTTON;

ALTER TABLE heap_data 
MODIFY COLUMN TOTAL_STOCK double(20, 3) NULL DEFAULT 0 AFTER ICS,
MODIFY COLUMN TOTAL_LINT_COTTON double(20, 3) NULL DEFAULT 0 AFTER TOTAL_STOCK,
MODIFY COLUMN TOTAL_SEED_COTTON double(20, 3) NULL DEFAULT 0 AFTER TOTAL_LINT_COTTON,
MODIFY COLUMN TOTAL_SCRUP double(20, 3) NULL DEFAULT 0 AFTER TOTAL_SEED_COTTON;


-- -06-11-2017

ALTER TABLE cotton_price 
DROP COLUMN PRICE;

-- -07 DEC 2017
ALTER TABLE farmer_dynamic_data 
ADD COLUMN ICS_NAME varchar(45) NULL AFTER CORRECTIVE_ACTION_PLAN;

ALTER TABLE farmer_dynamic_data
MODIFY COLUMN CORRECTIVE_ACTION_PLAN varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL AFTER CONVERSION_STATUS;

-- -12 DEC 2017
ALTER TABLE dynamic_report_config_detail 
ADD COLUMN EXPRESSION varchar(50) NULL AFTER METHOD;

ALTER TABLE dynamic_menu_field_map 
drop index fk_field,
DROP FOREIGN KEY fk_field,
 DROP FOREIGN KEY fk_menu,
MODIFY COLUMN MENU_ID bigint(20) NOT NULL,
ADD COLUMN ID bigint(45) NOT NULL  AUTO_INCREMENT FIRST,
DROP PRIMARY KEY,
ADD PRIMARY KEY (ID, MENU_ID, FIELD_ID) USING BTREE;
-- -12-12-2017
-- -Menu Mapping Table
ALTER TABLE dynamic_menu_section_map DROP FOREIGN KEY fk_menu_id;

ALTER TABLE dynamic_menu_section_map DROP FOREIGN KEY fk_section;

ALTER TABLE dynamic_menu_section_map 
DROP INDEX fk_section;

ALTER TABLE dynamic_menu_section_map 
MODIFY COLUMN MENU_ID bigint(20) NOT NULL,
ADD COLUMN ID bigint(45) NOT NULL AUTO_INCREMENT FIRST,
DROP PRIMARY KEY,
ADD PRIMARY KEY (ID, MENU_ID, SECTION_ID) USING BTREE;

INSERT INTO ese_seq(SEQ_KEY, SEQ_VAL) VALUES ('DYN_TXN_ID_SEQ', 2000)

INSERT INTO farm_catalogue(ID, NAME, CATALOGUE_TYPEZ, BRANCH_ID, REVIOSION_NO, STATUS) VALUES (110, 'Training Selection', 110, NULL, 1, 1);


-- -14-12-2017- --
ALTER TABLE offline_pmtnr 
ADD COLUMN MTNT_RECEIPT_NO varchar(25) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER MTNR_DATE;

-- - farmer location map

INSERT INTO pref(ID, NAME, VAL, ESE_ID, DESCRIPTION) VALUES (null, 'ENABLE_CROP_FILTER', '0', 1, '0=No,1=Yes');
INSERT INTO pref(ID, NAME, VAL, ESE_ID, DESCRIPTION) VALUES (null, 'ENABLE_PRODUCTION_AREA', '1', 1, '0=No,1=Yes');
INSERT INTO pref(ID, NAME, VAL, ESE_ID, DESCRIPTION) VALUES (null, 'ENABLE_YIELD', '1', 1, '0=No,1=Yes');

delete from  pref where name='ENABLE_CROP_FILTER';
delete from  pref where name='ENABLE_PRODUCTION_AREA';
delete from  pref where name='ENABLE_YIELD';

-- -18-12-2017- --
SET FOREIGN_KEY_CHECKS=0;
CREATE TABLE bale_generation (
ID  bigint(20) NOT NULL AUTO_INCREMENT ,
GINNING_PROCESS_ID  bigint(20) NULL DEFAULT NULL ,
GINNING  bigint(20) NULL DEFAULT NULL ,
GEN_DATE  date NULL DEFAULT NULL ,
HEAP_CODE  varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL ,
LOT_NO  varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL ,
PR_NO  varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL ,
BALE_WEIGHT  double(20,5) NULL DEFAULT NULL ,
BRANCH  varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL ,
PRIMARY KEY (ID),
CONSTRAINT FK_GINNING_KEY FOREIGN KEY (GINNING) REFERENCES warehouse (ID) ON DELETE NO ACTION ON UPDATE NO ACTION,
CONSTRAINT FK_GINNING_PROCESS_KEY FOREIGN KEY (GINNING_PROCESS_ID) REFERENCES ginning_process (ID) ON DELETE NO ACTION ON UPDATE NO ACTION,
INDEX bal_ind_ginning (GINNING) USING BTREE ,
INDEX bal_ind_ginning_proc (GINNING_PROCESS_ID) USING BTREE 
)

ALTER TABLE ginning_process DROP INDEX indx_heapData;
ALTER TABLE ginning_process DROP FOREIGN KEY fk_heapData;
ALTER TABLE ginning_process MODIFY COLUMN PROCESS_DATE  varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER ID;
ALTER TABLE ginning_process ADD COLUMN PRODUCT  bigint(20) NULL DEFAULT NULL AFTER GINNING_ID;
ALTER TABLE ginning_process ADD COLUMN ICS_CODE  varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER PRODUCT;
ALTER TABLE ginning_process ADD COLUMN HEAP_CODE  varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER ICS_CODE;
ALTER TABLE ginning_process ADD COLUMN PROCESS_QTY  double(20,5) NULL DEFAULT NULL AFTER HEAP_CODE;
ALTER TABLE ginning_process ADD COLUMN BALE_COUNT  bigint(20) NULL DEFAULT NULL AFTER PROCESS_QTY;
ALTER TABLE ginning_process ADD COLUMN TOT_SCRAP  double(20,5) NULL DEFAULT NULL AFTER TOT_SEED_COTTON;
ALTER TABLE ginning_process ADD COLUMN LINT_PER  double(20,5) NULL DEFAULT NULL AFTER TOT_SCRAP;
ALTER TABLE ginning_process ADD COLUMN SEED_PER  double(20,5) NULL DEFAULT NULL AFTER LINT_PER;
ALTER TABLE ginning_process ADD COLUMN SCRAP_PER  double(20,5) NULL DEFAULT NULL AFTER SEED_PER;
ALTER TABLE ginning_process ADD COLUMN BRANCH_ID  varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER SCRAP_PER;
ALTER TABLE ginning_process DROP COLUMN HEAP_DATA_ID;
ALTER TABLE ginning_process DROP COLUMN TOT_SCRUP;
ALTER TABLE ginning_process ADD CONSTRAINT fk_product FOREIGN KEY (PRODUCT) REFERENCES procurement_product (ID) ON DELETE NO ACTION ON UPDATE NO ACTION;
CREATE INDEX indx_product ON ginning_process(PRODUCT) USING BTREE ;
ALTER TABLE heap_data_detail ADD COLUMN STOCK_TYPE  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER SCRUP;
ALTER TABLE heap_data_detail DROP COLUMN STATUS;
CREATE TABLE ledger_data (
ID  bigint(20) NOT NULL AUTO_INCREMENT ,
PMT_DET_ID  bigint(20) NULL DEFAULT NULL ,
DATE  datetime NULL DEFAULT NULL ,
GINNING  bigint(20) NULL DEFAULT NULL ,
PRODUCT  bigint(20) NULL DEFAULT NULL ,
ICS  varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL ,
HEAP_CODE  varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL ,
OPENING_STK  double(10,3) NULL DEFAULT NULL ,
INWARD_STK  double(10,3) NULL DEFAULT NULL ,
ISSUE_STK  double(10,3) NULL DEFAULT NULL ,
CLOSING_STK  double(10,3) NULL DEFAULT NULL ,
LINT_RECOVERY  double(10,3) NULL DEFAULT NULL ,
SEED_RECOVERY  double(10,3) NULL DEFAULT NULL ,
SCRAP  double(10,3) NULL DEFAULT NULL ,
LINT_PER  double(10,3) NULL DEFAULT NULL ,
SEED_PER  double(10,3) NULL DEFAULT NULL ,
SCRAP_PER  double(10,3) NULL DEFAULT NULL ,
BRANCH  varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL ,
LEDGER_TYPE  tinyint(2) NULL DEFAULT NULL ,
PRIMARY KEY (ID)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
ROW_FORMAT=Compact
;
CREATE TABLE offline_bale_generation (
ID  bigint(20) NOT NULL AUTO_INCREMENT ,
AGENT_ID  varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL ,
CREATED_USER  varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL ,
UPDATED_USER  varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL ,
STATUS_CODE  varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL ,
STATUS_MSG  varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL ,
BRANCH_ID  varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL ,
DEVICE_ID  varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL ,
CREATED_DATE  datetime NULL DEFAULT NULL ,
GEN_DATE  varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL ,
HEAP_CODE  varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL ,
LOT_NO  varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL ,
BALE_WEIGHT  varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL ,
PRIMARY KEY (ID)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
ROW_FORMAT=Compact
;
CREATE TABLE offline_ginning_process (
ID  bigint(20) NOT NULL AUTO_INCREMENT ,
PROCESS_DATE  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
AGENT_ID  varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL ,
CREATED_USER  varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL ,
UPDATED_USER  varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL ,
STATUS_CODE  varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL ,
STATUS_MAG  varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL ,
BRANCH_ID  varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL ,
DEVICE_ID  varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL ,
CREATED_DATE  datetime NULL DEFAULT NULL ,
HEAP_CODE  varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL ,
PRODUCT_CODE  varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL ,
ICS  varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL ,
PROCESS_QTY  varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL ,
LINT_QTY  varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL ,
SEED_QTY  varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL ,
SCRAP_QTY  varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL ,
REMARK  varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL ,
PRIMARY KEY (ID)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
ROW_FORMAT=Compact
;
ALTER TABLE offline_pmtnr ADD COLUMN MTNT_RECEIPT_NO  varchar(25) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER MTNR_DATE;
CREATE TABLE offline_spinning_transfer (
ID  bigint(20) NOT NULL AUTO_INCREMENT ,
AGENT_ID  varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL ,
CREATED_USER  varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL ,
UPDATED_USER  varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL ,
STATUS_CODE  varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL ,
STATUS_MSG  varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL ,
BRANCH_ID  varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL ,
DEVICE_ID  varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL ,
CREATED_DATE  datetime NULL DEFAULT NULL ,
TRANS_DATE  varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL ,
INVOICE_NO  varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL ,
TRUCK_NO  varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL ,
SPINNER_CODE  varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL ,
LOT_NO  varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL ,
PR_NO  varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL ,
NO_OF_BALES  varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL ,
NET_WEIGHT  varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL ,
RATE  varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL ,
NET_AMT  varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL ,
TYPE  varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL ,
PRIMARY KEY (ID)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
ROW_FORMAT=Compact
;
CREATE TABLE spinning_transfer (
ID  bigint(20) NOT NULL AUTO_INCREMENT ,
TRANS_DATE  datetime NULL DEFAULT NULL ,
INVOICE_NO  varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL ,
TRUCK_NO  varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL ,
GINNING  bigint(20) NULL DEFAULT NULL ,
SPINNING  bigint(20) NULL DEFAULT NULL ,
LOT_NO  varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL ,
PR_NO  varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL ,
NO_OF_BALES  bigint(20) NULL DEFAULT NULL ,
NET_WEIGHT  double(20,5) NULL DEFAULT NULL ,
RATE  double(20,5) NULL DEFAULT NULL ,
NET_AMT  double(20,5) NULL DEFAULT NULL ,
TYPE  varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL ,
BRANCH  varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL ,
PRIMARY KEY (ID),
CONSTRAINT fkb_ginning FOREIGN KEY (GINNING) REFERENCES warehouse (ID) ON DELETE NO ACTION ON UPDATE NO ACTION,
CONSTRAINT fkb_spinning FOREIGN KEY (SPINNING) REFERENCES warehouse (ID) ON DELETE NO ACTION ON UPDATE NO ACTION,
INDEX inx_ginning (GINNING) USING BTREE ,
INDEX inx_spinning (SPINNING) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
ROW_FORMAT=Compact
;

INSERT INTO farm_catalogue VALUES (null, 'Heap', '117', 'chetna', 1, 1);
INSERT INTO farm_catalogue VALUES (null, 'Cotton Type', '118', 'chetna', '1', '1');

INSERT INTO locale_property(id, code, lang_code, lang_value) VALUES (null, 'village.select', 'en', 'Select Village');
INSERT INTO locale_property(id, code, lang_code, lang_value) VALUES (null, 'variety.select', 'en', 'Select Variety');


ALTER TABLE farm_crops_coordinates
MODIFY COLUMN ORDER_NO  bigint(20) NULL DEFAULT 0 AFTER LONGITUDE;

INSERT INTO locale_property(id, code, lang_code, lang_value) VALUES (null, 'village.select', 'en', 'Select Village');
INSERT INTO locale_property(id, code, lang_code, lang_value) VALUES (null, 'variety.select', 'en', 'Select Variety');

-- - 12-18-2017- --

INSERT INTO ese_menu  VALUES (null, 'service.plotting', 'Plotting', 'farmerPlotting_list.action', '25', '3', '0', '0', '0', '0', NULL);
INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='service.plotting'), '1');
INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='service.plotting'), '2');
INSERT INTO ese_role_menu VALUES (null,(SELECT id FROM ese_menu WHERE NAME='service.plotting'), '1');



DROP TABLE IF EXISTS farmer_location_map_field;
CREATE TABLE farmer_location_map_field  (
  ID bigint(20) NOT NULL AUTO_INCREMENT,
  NAME varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  TYPE varchar(3) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  TYPE_NAME varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  PARENT bigint(20) NULL DEFAULT NULL,
  STATUS bigint(3) NULL DEFAULT NULL,
  PRIMARY KEY (ID) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Compact;


INSERT INTO farmer_location_map_field VALUES (null, 'Crop Info', '0', 'cropInfo', NULL, 1);
INSERT INTO farmer_location_map_field VALUES (null, 'Inspection Info', '0', 'inspectInfo', NULL, 1);
INSERT INTO farmer_location_map_field VALUES (null, 'Estimated Harvest Date', '0', 'estHavstDateInfo', NULL, 1);
INSERT INTO farmer_location_map_field VALUES (null, 'Total Land Info', '0', 'totalLandInfo', NULL, 1);
INSERT INTO farmer_location_map_field VALUES (null, 'Estimated Yield', '0', 'estYieldInfo', NULL, 1);
INSERT INTO farmer_location_map_field VALUES (null, 'Crop Filter', '0', 'cropFilter', NULL, 1);
INSERT INTO farmer_location_map_field VALUES (null, 'Production Under Area', '0', 'prodArea', NULL, 1);
INSERT INTO farmer_location_map_field VALUES (null, 'Yield', '0', 'yield', NULL, 1);

delete from pref where name='ENABLE_CROP_FILTER';
delete from pref where name='ENABLE_PRODUCTION_AREA';
delete from  pref where name='ENABLE_YIELD';

ALTER TABLE offline_ginning_process 
ADD COLUMN GINNING_ID bigint(45) NULL AFTER REMARK;

ALTER TABLE ginning_process 
DROP COLUMN ICS_CODE;


-- -12/19/2017
-- -Language Based in dynamic menus
CREATE TABLE language_pref (
  ID bigint(20) NOT NULL AUTO_INCREMENT,
  CODE varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  NAME text COLLATE utf8_unicode_ci,
  SHORT_NAME varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  INFO longtext COLLATE utf8_unicode_ci,
  LANG varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  LTYPE tinyint(2) DEFAULT NULL,
  PRIMARY KEY (ID),
  KEY CODE (CODE) USING BTREE,
  KEY LTYPE (LTYPE) USING BTREE,
  KEY LANG (LANG) USING BTREE,
  KEY LANG_2 (LANG,LTYPE) USING BTREE
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

ALTER TABLE dynamic_menu_config
ADD COLUMN IS_SEASON varchar(20) NULL DEFAULT 0 AFTER ORDERZ,
ADD COLUMN IS_SINGLE_RECORD varchar(20) NULL DEFAULT 0 ;

ALTER TABLE farmer_dynamic_data
ADD COLUMN SEASON varchar(20) NULL ,
ADD COLUMN BRANCH_ID varchar(20) NULL ;



CREATE TABLE dynamic_menu_section_map (
  MENU_ID bigint(20) NOT NULL,
  SECTION_ID bigint(20) NOT NULL,
  ORDERZ bigint(20) DEFAULT NULL,
  ID bigint(45) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (ID,MENU_ID,SECTION_ID) USING BTREE
) ENGINE=InnoDB  ;

CREATE TABLE dynamic_menu_field_map (
  MENU_ID bigint(20) NOT NULL DEFAULT '0',
  FIELD_ID bigint(20) NOT NULL,
  ORDERZ bigint(20) DEFAULT NULL,
  ID bigint(45) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (ID,FIELD_ID,MENU_ID)
) ENGINE=InnoDB ;


CREATE TABLE  dynamic_field_report_config  (
  ID bigint(20) NOT NULL AUTO_INCREMENT,
  ENTITY varchar(45)  DEFAULT NULL,
  TXN_TYPE varchar(45)  DEFAULT NULL,
  LABEL varchar(255)  DEFAULT NULL,
  NAME varchar(45)  DEFAULT NULL,
  IS_SUM_PROP varchar(45)  DEFAULT NULL,
  IS_GROUP_PROP varchar(45)  DEFAULT NULL,
  ORDERZ varchar(45)  DEFAULT NULL,
  IS_GRID_AVAILABLE varchar(45)  DEFAULT NULL,
  IS_EXPORT_AVAILABLE varchar(45)  DEFAULT NULL,
  PRIMARY KEY (ID) USING BTREE
) ENGINE = InnoDB ROW_FORMAT = Compact;

CREATE TABLE  dynamic_field_report_join_map  (
  ID bigint(20) NOT NULL AUTO_INCREMENT,
  TXN_TYPE varchar(45)  DEFAULT NULL,
  ENTITY_TYPE varchar(255)  DEFAULT NULL,
  PROPERTIES text ,
  PRIMARY KEY (ID) USING BTREE
) ENGINE = InnoDB  ROW_FORMAT = Compact;

ALTER TABLE  dynamic_fields_config MODIFY COLUMN COMPONENT_NAME varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER COMPONENT_TYPE;

ALTER TABLE  dynamic_image_data DROP FOREIGN KEY fk_fdfv;

ALTER TABLE  dynamic_image_data ADD CONSTRAINT dynamic_image_data_ibfk_1 FOREIGN KEY (FARMER_DYNMAIC_FIELD_ID) REFERENCES  farmer_dynamic_field_value (ID) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE  dynamic_report_config_detail MODIFY COLUMN EXPRESSION varchar(255)  DEFAULT NULL AFTER STATUS;

ALTER TABLE  dynamic_section_config MODIFY COLUMN SECTION_NAME varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER ID;

ALTER TABLE  dynamic_section_config MODIFY COLUMN TYPEZ varchar(25) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '1' AFTER SECTION_CODE;



ALTER TABLE  farmer_dynamic_data MODIFY COLUMN CORRECTIVE_ACTION_PLAN varchar(255)  DEFAULT NULL AFTER CONVERSION_STATUS;

ALTER TABLE  farmer_dynamic_data ADD COLUMN ICS_NA\ME varchar(45)  DEFAULT NULL AFTER CORRECTIVE_ACTION_PLAN;
ALTER TABLE  farmer_dynamic_field_value DROP COLUMN NAME;

-- -12/20/2017
-- -creation tool menu script
INSERT INTO ese_menu (ID, NAME, DES, URL, ORD, PARENT_ID, FILTER, EXPORT_AVILABILITY, DIMENSION, PRIORITY, ICON_CLASS) VALUES (NULL, 'admin.creationToolGrid', 'creation Tool Grid', 'creationTool_grid.action', '12', '5', '0', '0', '0', '0', NULL);
INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='admin.creationToolGrid'), '1');
INSERT INTO ese_ent (ID, NAME) VALUES (NULL, 'admin.creationToolGrid.list');
INSERT INTO ese_role_ent VALUES ('1', (SELECT id FROM ese_ent WHERE NAME='admin.creationToolGrid.list'));
INSERT INTO ese_role_menu VALUES (NULL, (SELECT MAX(id) FROM ese_menu), '1');

ALTER TABLE dynamic_fields_config 
ADD COLUMN ACCESS_TYPE int(5) NULL DEFAULT 0 AFTER CATALOGUE_TYPE;
update dynamic_fields_config set ACCESS_TYPE  = CASE
      WHEN CATALOGUE_TYPE is null OR CATALOGUE_TYPE='' then '0'
when CATALOGUE_TYPE like 'CG%'  THEN 2
ELSE 1
    END
		where CATALOGUE_TYPE IS NOT NULL;
		
-- -21-12-2017
		
alter table dynamic_menu_config add column AGENT_TYPE VARCHAR(10) default '02';

ALTER TABLE offline_bale_generation 
ADD COLUMN BALE_DATE varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER GEN_DATE;

ALTER TABLE bale_generation 
ADD COLUMN BALE_DATE date NULL AFTER GEN_DATE;

ALTER TABLE bale_generation 
ADD COLUMN STATUS tinyint(5) NULL DEFAULT 0 AFTER BALE_WEIGHT;

ALTER TABLE ginning_process 
MODIFY COLUMN TOT_LINT_COTTON double(20, 2) NULL DEFAULT NULL AFTER BALE_COUNT,
MODIFY COLUMN TOT_SEED_COTTON double(20, 2) NULL DEFAULT NULL AFTER TOT_LINT_COTTON,
MODIFY COLUMN TOT_SCRAP double(20, 2) NULL DEFAULT NULL AFTER TOT_SEED_COTTON,
MODIFY COLUMN LINT_PER double(20, 2) NULL DEFAULT NULL AFTER TOT_SCRAP,
MODIFY COLUMN SEED_PER double(20, 2) NULL DEFAULT NULL AFTER LINT_PER,
MODIFY COLUMN SCRAP_PER double(20, 2) NULL DEFAULT NULL AFTER SEED_PER;

-- -12-22-2017 -- -

alter table dynamic_menu_config add column AGENT_TYPE VARCHAR(10) default '02';

ALTER TABLE farmer_dynamic_data
ADD COLUMN IS_SINGLE_RECORD varchar(20) NULL DEFAULT 0 ;
 
ALTER TABLE dynamic_menu_config 
ADD COLUMN M_TXN_TYPEZ varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER AGENT_TYPE;

ALTER TABLE farmer_dynamic_field_value
ADD COLUMN VALIDATION_TYPE varchar(20) NULL DEFAULT 0 ;

ALTER TABLE farmer_dynamic_field_value
ADD COLUMN IS_MOBILE_AVAILABLE varchar(20) NULL DEFAULT 0 ;

-- -27-12-2017 -- -

ALTER TABLE pmt_image_details 
ADD COLUMN TYPE tinyint(5) NULL DEFAULT 1 AFTER LONGITUDE;

ALTER TABLE offline_pmt_image_details 
ADD COLUMN TYPE tinyint(5) NULL DEFAULT 1 AFTER LONGITUDE;

ALTER TABLE ginning_process 
ADD COLUMN STATUS tinyint(5) NULL AFTER BRANCH_ID;

-- -27-12-2017- --
ALTER TABLE bale_generation 
ADD COLUMN SPINNING_TRANSFER_ID bigint(20) NULL DEFAULT NULL AFTER GINNING_PROCESS_ID,
ADD INDEX bal_ind_spinning_trans(SPINNING_TRANSFER_ID) USING BTREE,
ADD CONSTRAINT FK_SPINNING_TRANS_KEY FOREIGN KEY (SPINNING_TRANSFER_ID) REFERENCES spinning_transfer (ID) ON DELETE NO ACTION ON UPDATE NO ACTION;


alter table pmt_image_details add COLUMN FILE_NAME text default null,add COLUMN FILE_TYPE varchar(20) default null;
-- -28-12-2017

alter table dynamic_menu_config
ADD COLUMN BEFORE_ADD varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
 ADD COLUMN AFTER_ADD varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL;
 
ALTER TABLE procurement 
ADD COLUMN INVOICE_NO varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER TYPEE;


ALTER TABLE dynamic_fields_config 
MODIFY COLUMN IS_MOBILE_AVAILABLE varchar(2) DEFAULT 0 AFTER REFERENCE_ID;
update dynamic_fields_config set IS_MOBILE_AVAILABLE='1' where IS_MOBILE_AVAILABLE is null;


-- -05 JAN 2018
INSERT INTO ese_seq (ID, SEQ_KEY, SEQ_VAL) VALUES (0, 'FARM_CROPS_MASTER_CODE_SEQ', '0');

DROP TABLE IF EXISTS farm_crops_master;
CREATE TABLE farm_crops_master (
  ID bigint(20) NOT NULL AUTO_INCREMENT,
  CROP_CODE varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  CROP_NAME varchar(45) COLLATE utf8_unicode_ci NOT NULL,
  REVISION_NO bigint(20) DEFAULT '1',
  KML_FILE longblob,
  PRIMARY KEY (ID) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT;


ALTER TABLE farm_crops
ADD COLUMN PRODUCTIVE_TREES  varchar(20) NULL AFTER STATUS;

-- -05-01-2018
-- -To remove the Entity type paramter in menu url
update ese_menu set url = substring_index(url, '&entityType=',1)  where name in ('service.dynamicCertification','report.dynmaicCertification');

-- - 08 JAN 2018

ALTER TABLE supplier_procurement 
ADD COLUMN TOTAL_LABOUR_COST double(20, 2) NULL DEFAULT 0.00 AFTER BRANCH_ID,
ADD COLUMN TRANSPORT_COST double(20, 2) NULL DEFAULT 0.00 AFTER TOTAL_LABOUR_COST,
ADD COLUMN INVOICE_VALUE double(20, 2) NULL DEFAULT 0.00 AFTER TRANSPORT_COST;


ALTER TABLE offline_supplier_procurement 
ADD COLUMN TOTAL_LABOUR_COST double(20, 2) NULL DEFAULT 0.00 AFTER BRANCH_ID,
ADD COLUMN TRANSPORT_COST double(20, 2) NULL DEFAULT 0.00 AFTER TOTAL_LABOUR_COST,
ADD COLUMN INVOICE_VALUE double(20, 2) NULL DEFAULT 0.00 AFTER TRANSPORT_COST;

ALTER TABLE farmer_dynamic_field_value 
MODIFY COLUMN FIELD_VALUE text CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL AFTER FIELD_NAME;


ALTER TABLE device 
ADD COLUMN APP_VERSION varchar(15) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER CREATED_DATE,
ADD COLUMN LOGIN_TIME varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER APP_VERSION;

-- -01-19-2018- --

ALTER TABLE dynamic_menu_section_map 
ADD INDEX FK_SECTION_1(SECTION_ID) USING BTREE,
ADD CONSTRAINT FK_SECTION_1 FOREIGN KEY (SECTION_ID) REFERENCES dynamic_section_config (ID) ON DELETE CASCADE ON UPDATE CASCADE;
 

INSERT INTO ese_menu VALUES (null, 'service.dynamicCertification', 'Dynamic Certification', 'dynamicCertification_list.action?txnType=2001', 16, 3, 0, 0, 0, 0, NULL);
INSERT INTO ese_ent VALUES (null, 'service.dynamicCertification.create');
INSERT INTO ese_ent VALUES (null, 'service.dynamicCertification.update');
INSERT INTO ese_ent VALUES (null, 'service.dynamicCertification.list');
INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='service.dynamicCertification'), '1');
INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='service.dynamicCertification'), '2');
INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='service.dynamicCertification'), '3');
INSERT INTO ese_role_ent (ROLE_ID, ENT_ID) VALUES ('1', (SELECT id FROM ese_ent WHERE NAME='service.dynamicCertification.create'));
INSERT INTO ese_role_ent (ROLE_ID, ENT_ID) VALUES ('1', (SELECT id FROM ese_ent WHERE NAME='service.dynamicCertification.update'));
INSERT INTO ese_role_ent (ROLE_ID, ENT_ID) VALUES ('1', (SELECT id FROM ese_ent WHERE NAME='service.dynamicCertification.list'));
INSERT INTO ese_role_menu VALUES (null,(SELECT id FROM ese_menu WHERE NAME='service.dynamicCertification'), '1');

INSERT INTO ese_menu VALUES (null, 'report.dynmaicCertification', 'Dynamic Cerrification Report', 'dynmaicCertificationReport_list.action?txnType=2001', 16, 4, 0, 0, 0, 0, NULL);
INSERT INTO ese_ent VALUES (null, 'report.dynmaicCertification.list');
INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='report.dynmaicCertification'), '1');
INSERT INTO ese_role_ent (ROLE_ID, ENT_ID) VALUES ('1', (SELECT id FROM ese_ent WHERE NAME='report.dynmaicCertification.list'));
INSERT INTO ese_role_menu VALUES (null,(SELECT id FROM ese_menu WHERE NAME='report.dynmaicCertification'), '1');

INSERT INTO dynamic_field_report_join_map(ID, FILTER_NAME, ENTITY_TYPE, PROPERTIES, FILTER_ID) VALUES (null, 'f.id', '4', 'f=farmer', 'farmerId');
INSERT INTO dynamic_field_report_join_map(ID, FILTER_NAME, ENTITY_TYPE, PROPERTIES, FILTER_ID) VALUES (null, 'v.id', '4', 'v =f.village', NULL);
INSERT INTO dynamic_field_report_join_map(ID, FILTER_NAME, ENTITY_TYPE, PROPERTIES, FILTER_ID) VALUES (null, 'g.id', '4', 'g=f.samithi', 'groupId');
INSERT INTO dynamic_field_report_join_map(ID, FILTER_NAME, ENTITY_TYPE, PROPERTIES, FILTER_ID) VALUES (null, 'f.id', '2', 'f=farmer', 'farmerId');
INSERT INTO dynamic_field_report_join_map(ID, FILTER_NAME, ENTITY_TYPE, PROPERTIES, FILTER_ID) VALUES (null, 'v.id', '2', 'v =f.village', NULL);
INSERT INTO dynamic_field_report_join_map(ID, FILTER_NAME, ENTITY_TYPE, PROPERTIES, FILTER_ID) VALUES (null, 'g.id', '2', 'g=f.samithi', 'groupId');
INSERT INTO dynamic_field_report_join_map(ID, FILTER_NAME, ENTITY_TYPE, PROPERTIES, FILTER_ID) VALUES (null, 'v.id', '1', 'v =village', '');
INSERT INTO dynamic_field_report_join_map(ID, FILTER_NAME, ENTITY_TYPE, PROPERTIES, FILTER_ID) VALUES (null, 'g.id', '1', 'g=samithi', 'groupId');

INSERT INTO dynamic_field_report_config(ID, ENTITY, TXN_TYPE, LABEL, NAME, IS_SUM_PROP, IS_GROUP_PROP, ORDERZ, IS_GRID_AVAILABLE, IS_EXPORT_AVAILABLE) VALUES (null, '4', NULL, 'profile.farm', 'farmName', NULL, NULL, '3', '1', '1');
INSERT INTO dynamic_field_report_config(ID, ENTITY, TXN_TYPE, LABEL, NAME, IS_SUM_PROP, IS_GROUP_PROP, ORDERZ, IS_GRID_AVAILABLE, IS_EXPORT_AVAILABLE) VALUES (null, '4', NULL, 'profile.farmer', 'f.firstName', NULL, NULL, '4', '1', '1');
INSERT INTO dynamic_field_report_config(ID, ENTITY, TXN_TYPE, LABEL, NAME, IS_SUM_PROP, IS_GROUP_PROP, ORDERZ, IS_GRID_AVAILABLE, IS_EXPORT_AVAILABLE) VALUES (null, '4', NULL, 'villageName', 'v.name', NULL, NULL, '5', '1', '1');
INSERT INTO dynamic_field_report_config(ID, ENTITY, TXN_TYPE, LABEL, NAME, IS_SUM_PROP, IS_GROUP_PROP, ORDERZ, IS_GRID_AVAILABLE, IS_EXPORT_AVAILABLE) VALUES (null, '4', NULL, 'groupName', 'g.name', NULL, NULL, '6', '1', '1');
INSERT INTO dynamic_field_report_config(ID, ENTITY, TXN_TYPE, LABEL, NAME, IS_SUM_PROP, IS_GROUP_PROP, ORDERZ, IS_GRID_AVAILABLE, IS_EXPORT_AVAILABLE) VALUES (null, '3', NULL, 'wname', 'name', NULL, NULL, '3', '1', '1');
INSERT INTO dynamic_field_report_config(ID, ENTITY, TXN_TYPE, LABEL, NAME, IS_SUM_PROP, IS_GROUP_PROP, ORDERZ, IS_GRID_AVAILABLE, IS_EXPORT_AVAILABLE) VALUES (null, '1', NULL, 'profile.farmer', 'firstName', NULL, NULL, '3', '1', '1');
INSERT INTO dynamic_field_report_config(ID, ENTITY, TXN_TYPE, LABEL, NAME, IS_SUM_PROP, IS_GROUP_PROP, ORDERZ, IS_GRID_AVAILABLE, IS_EXPORT_AVAILABLE) VALUES (null, '1', NULL, 'villageName', 'v.name', NULL, NULL, '4', '1', '1');
INSERT INTO dynamic_field_report_config(ID, ENTITY, TXN_TYPE, LABEL, NAME, IS_SUM_PROP, IS_GROUP_PROP, ORDERZ, IS_GRID_AVAILABLE, IS_EXPORT_AVAILABLE) VALUES (null, '1', NULL, 'groupName', 'g.name', NULL, NULL, '5', '1', '1');
INSERT INTO dynamic_field_report_config(ID, ENTITY, TXN_TYPE, LABEL, NAME, IS_SUM_PROP, IS_GROUP_PROP, ORDERZ, IS_GRID_AVAILABLE, IS_EXPORT_AVAILABLE) VALUES (null, '2', NULL, 'profile.farm', 'farmName', NULL, NULL, '3', '1', '1');
INSERT INTO dynamic_field_report_config(ID, ENTITY, TXN_TYPE, LABEL, NAME, IS_SUM_PROP, IS_GROUP_PROP, ORDERZ, IS_GRID_AVAILABLE, IS_EXPORT_AVAILABLE) VALUES (null, '2', NULL, 'profile.farmer', 'f.firstName', NULL, NULL, '4', '1', '1');
INSERT INTO dynamic_field_report_config(ID, ENTITY, TXN_TYPE, LABEL, NAME, IS_SUM_PROP, IS_GROUP_PROP, ORDERZ, IS_GRID_AVAILABLE, IS_EXPORT_AVAILABLE) VALUES (null, '2', NULL, 'villageName', 'v.name', NULL, NULL, '5', '1', '1');
INSERT INTO dynamic_field_report_config(ID, ENTITY, TXN_TYPE, LABEL, NAME, IS_SUM_PROP, IS_GROUP_PROP, ORDERZ, IS_GRID_AVAILABLE, IS_EXPORT_AVAILABLE) VALUES (null, '2', NULL, 'groupName', 'g.name', NULL, NULL, '6', '1', '1');


-- -20-01-2018
alter table dynamic_image_data add column FILE_EXT varchar(500);

-- -22-01-2018
ALTER TABLE supplier_procurement 
ADD COLUMN OTHER_COST double(20, 2) NULL AFTER INVOICE_VALUE,
ADD COLUMN TAX_AMT double(20, 2) NULL AFTER OTHER_COST;

-- -30-01-2018
ALTER TABLE offline_supplier_procurement 
ADD COLUMN TAX_AMT double(20, 0) NULL DEFAULT 0.00 AFTER LONGITUDE,
ADD COLUMN OTHER_COST double(20, 2) NULL DEFAULT 0.00 AFTER TAX_AMT;


update ese_menu set url='farmerPlotting_list.action' where name='service.plotting';

-- -09-02-2018
ALTER TABLE farmer 
ADD COLUMN existingFarmer_Flag int(2) NOT NULL DEFAULT 0 AFTER MASTER_DATA;

-- -13-02-2018
-- -NSwitch Distribution Changes
CREATE TABLE distribution_balance (
  ID bigint(20) NOT NULL AUTO_INCREMENT,
  FARMER_ID bigint(20) NOT NULL,
  PRODUCT_ID bigint(20) NOT NULL,
  STOCK bigint(5) DEFAULT '0',
  REVISION_NO bigint(20) DEFAULT '0',
  PRIMARY KEY (ID) USING BTREE,
  KEY farmer (FARMER_ID) USING BTREE,
  KEY product (PRODUCT_ID) USING BTREE
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=COMPACT;

alter table distribution_balance MODIFY column stock DOUBLE(3,2) default 0.0;


alter table distribution_detail
add column INIT_FBAL DOUBLE(5,2) default 0.0,
add column INIT_TBAL DOUBLE(5,2) default 0.0,
add column FINAL_FARMER_BALANCE DOUBLE(5,2) default 0.0;

-- - 14-02-2018- --
INSERT INTO ese_menu VALUES (null, 'service.distributionBalance', 'Distribution Balance', 'distributionBalance_list.action', 17, 3, 0, 0, 0, 0, NULL);
INSERT INTO ese_ent VALUES (null, 'service.distributionBalance.list');
INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='service.distributionBalance'), '1');
INSERT INTO ese_role_ent (ROLE_ID, ENT_ID) VALUES ('1', (SELECT id FROM ese_ent WHERE NAME='service.distributionBalance.list'));
INSERT INTO ese_role_menu VALUES (null,(SELECT id FROM ese_menu WHERE NAME='service.distributionBalance'), '1');

ALTER TABLE farm_ics_conversion 
ADD COLUMN SEASON varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL AFTER ORGANIC_STATUS,
ADD COLUMN INSPECTOR_MOBILE_NO varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL AFTER SEASON,
ADD COLUMN SCOPE_OPERATION varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL AFTER INSPECTOR_MOBILE_NO,
ADD COLUMN TOTAL_LANDHOLD varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL AFTER SCOPE_OPERATION,
ADD COLUMN LAND_ORGANIC varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL AFTER TOTAL_LANDHOLD,
ADD COLUMN TOTAL_SITE varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL AFTER LAND_ORGANIC,
ADD COLUMN INSPECTION_TYPE varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '0' AFTER TOTAL_SITE,
ADD COLUMN IS_ACTIVE tinyint(5) NULL DEFAULT 1 AFTER INSPECTION_TYPE;

ALTER TABLE farm_ics_conversion 
ADD COLUMN FARMER_ID varchar(90) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER IS_ACTIVE;


ALTER TABLE farmer_dynamic_data 
ADD COLUMN FARM_ICS_CONV_ID varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER IS_SINGLE_RECORD;
ALTER TABLE farmer_dynamic_data 
ADD INDEX FK_FARM_ICS_CONV_ID(FARM_ICS_CONV_ID) USING BTREE;
DROP view if EXISTS vw_global_report;
DROP VIEW vw_afl_export;
CREATE VIEW vw_afl_export AS
( SELECT
f.id AS ID,
f.FARMER_CODE AS FARMER_CODE,
'' AS APEDA_NO,
f.FIRST_NAME AS FARMER_NAME,
f.LAST_NAME AS FATHER_NAME,
f.AGE AS AGE,
f.CASTE_NAME AS CASTE,
f.ADDRESS AS ADDRESS,
'' AS HOUSE_NO,
f.MOBILE_NUMBER AS MOBILE_NO,
f.ADHAAR_NO AS ADHAAR_NO,
v.NAME AS VILLAGE_NAME,
'' AS GPS_TRACKING,
c.NAME AS CITY_NAME,
l.CODE AS DISTRICT_CODE,
l.NAME AS DISTRICT_NAME,
s.NAME AS STATE,
f.PIN_CODE AS PINCODE,
fm.LANDMARK AS LAND_MARK,
fd.TOTAL_LAND_HOLDING AS TOTAL_AREA,
fd.PROPOSED_PLANTING_AREA AS AREA_UNDER_ORGANIC,
fd.SURV_NO AS SURVEY_NO,-- '' as NO_OF_PLOT,
'' AS LANDMARK_LATTITUDE,
'' AS LANDMARK_LONGITUDE,
fm.LATITUDE AS FIELD_LATTITUDE,
fm.LONGITUDE AS FIELD_LONGITUDE,
GROUP_CONCAT( CONCAT( ah.FARM_ANIMAL, '-', ah.ANIMAL_COUNT ) ) AS ANIMALHUSBANDARY,
GROUP_CONCAT( CONCAT( pc.id, '-', IFNULL( fc.CULTIVATION_AREA, '' ) ) ) AS CULTIVATION_AREA,
GROUP_CONCAT( CONCAT( pc.id, '-', IFNULL( fc.CROP_CATEGORY, '' ) ) ) AS CROP_TYPE,
GROUP_CONCAT( CONCAT( pc.id, '-', IFNULL( pp.NAME, '' ) ) ) AS VARIETY,
GROUP_CONCAT( CONCAT( pc.id, '-', IFNULL( fc.NO_OF_TREES, '' ) ) ) AS NO_OF_TREES,
GROUP_CONCAT( CONCAT( pc.id, '-', IFNULL( fc.EST_YIELD, '' ) ) ) AS ESTIMATED_YIELD,
GROUP_CONCAT( CONCAT( pc.id, '-', IFNULL( fc.ACTUAL_SEED_YIELD, '' ) ) ) AS ACTUAL_YIELD,
f.ICS_NAME AS ICS_NAME,
f.GENDER AS GENDER,
f.DOJ AS DATE_OF_REG,
fd.LAST_DATE_CHE_APP AS LAST_DATE_CHE_APP,
f.BRANCH_ID AS BRANCH 
FROM
	farmer f
	JOIN village v ON v.id = f.VILLAGE_ID
	JOIN city c ON c.id = f.CITY_ID
	JOIN location_detail l ON l.id = c.LOCATION_ID
	JOIN state s ON s.ID = l.STATE_ID
	LEFT JOIN farm fm ON f.id = fm.FARMER_ID
	LEFT JOIN farm_detailed_info fd ON fd.id = fm.FARM_DETAILED_INFO_ID
	LEFT JOIN farm_crops fc ON fc.FARM_ID = fm.id
	LEFT JOIN procurement_variety pp ON pp.id = fc.PROCUREMENT_CROPS_VARIETY_ID
	LEFT JOIN procurement_product pc ON pc.id = pp.PROCUREMENT_PRODUCT_ID
	LEFT JOIN animal_husbandary ah ON ah.FARMER_ID = f.id 
GROUP BY
	f.id,fm.id 
	) 
	
ALTER TABLE dynamic_menu_config 
ADD COLUMN REVISION_NO bigint(20) NULL DEFAULT 0 AFTER AFTER_ADD	
	
-- -21-02-2018
UPDATE FARM_DETAILED_INFO 
set LAST_DATE_CHE_APP = SUBSTRING(LAST_DATE_CHE_APP, 4)
WHERE
	LENGTH( TRIm( LAST_DATE_CHE_APP ) ) = 10;
	
-- -26-02-2018
ALTER TABLE dynamic_report_config_detail 
ADD COLUMN ALIGNMENT varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER EXPRESSION;

ALTER TABLE crop_harvest 
ADD COLUMN AGENT_ID varchar(45) NULL AFTER HARVEST_DATE;

ALTER TABLE crop_supply 
ADD COLUMN AGENT_ID varchar(45) NULL AFTER BUYER_INFO;

ALTER TABLE crop_harvest 
MODIFY COLUMN FARMER_NAME varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER FARMER_ID;

-- -05-03-2018
UPDATE farm_field SET  TYPE_NAME = 'farm.farmImage',STATUS = 1, OTHERS = 0 WHERE NAME = 'Farm Photo' AND  PARENT = 1;

ALTER TABLE device 
ADD COLUMN ANDROID_VERSION varchar(15) NULL AFTER LOGIN_TIME,
ADD COLUMN MOBILE_MODEL varchar(50) NULL AFTER ANDROID_VERSION;

alter table dynamic_fields_config add column CATALOGUE_DEP_KEY varchar(45),
add column PARENT_DEP_KEY varchar(45);

-- - 22-03-2018
INSERT INTO locale_property(id, code, lang_code, lang_value) VALUES (NULL, 'txnAmountLabel', 'en', 'Transaction Amount');
INSERT INTO locale_property(id, code, lang_code, lang_value) VALUES (NULL, 'txnAmountLabel', 'fr', 'Transaction Amount');
INSERT INTO locale_property(id, code, lang_code, lang_value) VALUES (NULL, 'totalNetWeightLabel', 'en', 'Total Net Weight');
INSERT INTO locale_property(id, code, lang_code, lang_value) VALUES (NULL, 'totalNetWeightLabel', 'fr', 'Total Net Weight');
INSERT INTO locale_property(id, code, lang_code, lang_value) VALUES (NULL, 'paymentAmountLabel', 'en', 'Payment Amount');
INSERT INTO locale_property(id, code, lang_code, lang_value) VALUES (NULL, 'paymentAmountLabel', 'fr', 'Payment Amount');
INSERT INTO locale_property(id, code, lang_code, lang_value) VALUES (NULL, 'procurementChartTitle', 'en', 'Procurement Chart');
INSERT INTO locale_property(id, code, lang_code, lang_value) VALUES (NULL, 'procurementChartTitle', 'fr', 'Procurement Chart');
INSERT INTO locale_property(id, code, lang_code, lang_value) VALUES (NULL, 'weightUnit', 'en', 'Kgs');
INSERT INTO locale_property(id, code, lang_code, lang_value) VALUES (NULL, 'weightUnit', 'fr', 'Kgs');
INSERT INTO locale_property(id, code, lang_code, lang_value) VALUES (NULL, 'drillDownMsg', 'en', 'Click to show product wise procurement weight. Click again to show total procurement weight.');
INSERT INTO locale_property(id, code, lang_code, lang_value) VALUES (NULL, 'drillDownMsg', 'fr', 'Click to show product wise procurement weight. Click again to show total procurement weight.');
INSERT INTO locale_property(id, code, lang_code, lang_value) VALUES (NULL, 'drilldownTooltip1', 'en', 'Click to view product wise procurement weight');
INSERT INTO locale_property(id, code, lang_code, lang_value) VALUES (NULL, 'drilldownTooltip1', 'fr', 'Click to view product wise procurement weight');
INSERT INTO locale_property(id, code, lang_code, lang_value) VALUES (NULL, 'drilldownTooltip2', 'en', 'Click to view total procurement weight');
INSERT INTO locale_property(id, code, lang_code, lang_value) VALUES (NULL, 'drilldownTooltip2', 'fr', 'Click to view total procurement weight');
INSERT INTO locale_property(id, code, lang_code, lang_value) VALUES (NULL, 'procurementWeightChartLegend', 'en', 'Procurement Product');
INSERT INTO locale_property(id, code, lang_code, lang_value) VALUES (NULL, 'procurementWeightChartLegend', 'fr', 'Procurement Product');

ALTER TABLE offline_procurement 
ADD COLUMN BUYER int(25) NULL AFTER CROP_TYPE;

ALTER TABLE procurement 
ADD COLUMN BUYER int(25) NULL AFTER CROP_TYPE;

ALTER TABLE cultivation 
ADD COLUMN AGENT_ID varchar(45) NULL AFTER SOIL_PREPARATION;


ALTER TABLE dynamic_menu_section_map 
MODIFY COLUMN ID bigint(45) NOT NULL AUTO_INCREMENT;


ALTER TABLE eses_gar.farmer 
MODIFY COLUMN ICS_YEAR varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER PREFERENCE_OF_WORK;


	
-- - 18-04-2018
ALTER TABLE farm 
ADD COLUMN WATER_SOURCE varchar(50) NULL AFTER STATUS;

ALTER TABLE farm 
ADD COLUMN LOCAL_NAME_OF_CROTEN_TREE varchar(20) NULL AFTER WATER_SOURCE;

ALTER TABLE farm 
ADD COLUMN NO_OF_CROTEN_TREE varchar(5) NULL AFTER LOCAL_NAME_OF_CROTEN_TREE;

-- -19-04-2018
ALTER TABLE dynamic_section_config 
ADD COLUMN AFTER_INSERT varchar(20) NULL AFTER SECTION_ORDER;

ALTER TABLE procurement 
ADD COLUMN VEHICLE_TYPE varchar(70) NULL AFTER BUYER;

ALTER TABLE offline_procurement 
ADD COLUMN VEHICLE_TYPE varchar(70) NULL AFTER BUYER;

-- -19-04-2018
UPDATE farm_ics_conversion  set ORGANIC_STATUS='3' where ICS_TYPE='3';
UPDATE farm_ics_conversion  set ORGANIC_STATUS='0' where ICS_TYPE = '0' OR ICS_TYPE = '1' OR ICS_TYPE = '2';

INSERT INTO locale_property(id, code, lang_code, lang_value) VALUES (null, 'inprocess', 'en', 'Inconversion');
INSERT INTO locale_property(id, code, lang_code, lang_value) VALUES (null, 'alrdyCertified', 'en', 'Organic');
INSERT INTO locale_property(id, code, lang_code, lang_value) VALUES (null, 'Conventional', 'en', 'Conventional');

ALTER TABLE pmt_image_details
ADD COLUMN OFFLINE_PMT_ID bigint(20) NULL AFTER ID;
INSERT INTO pref VALUES (null, 'ENABLE_DISTRIBUTION_IMAGE', '0', 1, '0-No,1-Yes');

-- -25-04-2018
ALTER TABLE ese_account 
ADD COLUMN BRANCH_ID varchar(45) COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER SAVING_AMOUNT;

update ese_account e inner join prof f on f.PROF_ID=e.PROFILE_ID and e.TYPEE='3' set e.BRANCH_ID=f.BRANCH_ID;
update ese_account e inner join vendor f on f.VENDOR_ID=e.PROFILE_ID and e.TYPEE='4' set e.BRANCH_ID=f.BRANCH_ID;
update ese_account e inner join branch_master f on f.BRANCH_ID=e.PROFILE_ID and e.TYPEE='7' set e.BRANCH_ID=f.BRANCH_ID;
update ese_account e inner join prof f on f.PROF_ID=e.PROFILE_ID  and  e.TYPEE='1' 
and substring(e.CRE_TIME,1,16)  = substring(f.CREATED_DATE,1,16)
 and e.BRANCH_ID is null
set e.BRANCH_ID=f.BRANCH_ID;

-- -26-04-2018
ALTER TABLE distribution 
ADD COLUMN WAREHOUSE_CODE varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER UPDATED_TIME,
ADD COLUMN WAREHOUSE_NAME text CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL AFTER WAREHOUSE_CODE;


-- -27-04-2018
ALTER TABLE loc_history
ADD COLUMN ACCURACY  bigint(45) NULL AFTER ADDRESS;

UPDATE loc_history set ACCURACY='1.0' WHERE ACCURACY IS NULL;

INSERT INTO pref(ID, NAME, VAL, ESE_ID, DESCRIPTION) VALUES (null, 'IS_ACCURACY_VALUE', '250', 1, NULL);


-- -30-04-2018
ALTER TABLE procurement 
ADD COLUMN STATUS int(255) NULL AFTER VEHICLE_TYPE;

-- -03-05-2018

alter table dynamic_menu_field_map add column IS_FILTER bigint(2) default 0;

alter table farmer_dynamic_field_value add column ACCESS_TYPE BIGINT(2) default 0;
alter table farmer_dynamic_field_value add column CATALOGUE_TYPE varchar(45) ;

alter table farmer_dynamic_field_value add column PARENT_ID BIGINT(45) DEFAULT 0 ;

update dynamic_fields_config set ACCESS_TYPE = CASE 
WHEN CATALOGUE_TYPE is null OR CATALOGUE_TYPE='' then '0'
when CATALOGUE_TYPE like 'CG%'  THEN 2
ELSE 1
END ;

UPDATE farmer_dynamic_field_value fd
JOIN dynamic_fields_config dc ON dc.COMPONENT_CODE = fd.FIELD_NAME
set fd.ACCESS_TYPE = dc.ACCESS_TYPE, fd.CATALOGUE_TYPE = dc.CATALOGUE_TYPE,fd.PARENT_ID = dc.REFERENCE_ID;

ALTER TABLE farmer_dynamic_data ADD COLUMN UPDATED_DATE date DEFAULT NULL,
ADD COLUMN UPDATED_USER VARCHAR ( 45 ) DEFAULT NULL;



DROP TABLE IF EXISTS dynamic_constants;
CREATE TABLE dynamic_constants  (
  ID bigint(45) NOT NULL,
  CODE varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  NAME varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  FIELD_NAME varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  DATA_TYPE varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  MCODE varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (ID) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

ALTER TABLE agent_prof
ADD COLUMN VERSION  bigint(45) NULL AFTER language;

-- -04-05-2018
ALTER TABLE dynamic_report_config 
ADD COLUMN ENTITY_NAME varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER BRANCH_ID;

-- -08-05-2018
ALTER TABLE dynamic_report_config_filter 
ADD COLUMN TYPE int(2) NULL AFTER ORDER,
ADD COLUMN IS_DATE_FILTER int(2) NULL AFTER TYPE;

ALTER TABLE dynamic_report_config 
ADD COLUMN XLS_FILE varchar(255) NULL DEFAULT NULL AFTER ENTITY_NAME;



CREATE OR REPLACE ALGORITHM = UNDEFINED DEFINER = root@localhost SQL SECURITY DEFINER VIEW dynamic_export AS SELECT
	fd.FARMER_DYNAMIC_DATA_ID AS FARMER_DYNAMIC_DATA,
	dref.COMPONENT_CODE AS FIELD_NAME,
	group_concat(
	fd.TYPEZ,
	'~',
	fd.FIELD_NAME,
	'-',
	(
CASE
	
	WHEN ( df.COMPONENT_TYPE IN ( 2, 4, 6 ) ) THEN
	( SELECT catalogue_value.NAME FROM catalogue_value WHERE ( catalogue_value.CODE = fd.FIELD_VALUE ) ) 
	WHEN ( df.COMPONENT_TYPE = '9' ) THEN
	( SELECT group_concat( catalogue_value.NAME SEPARATOR ',' ) FROM catalogue_value WHERE find_in_set( catalogue_value.CODE, fd.FIELD_VALUE ) ) 
	WHEN ( df.COMPONENT_TYPE = '12' ) THEN
	( CONVERT ( dt.ID USING utf8 ) COLLATE utf8_general_ci ) ELSE fd.FIELD_VALUE 
END 
	) SEPARATOR '|' 
	) AS FIELD_VALUE 
FROM
	(
		(
			(
				(
					( dynamic_menu_config m JOIN dynamic_menu_field_map dmap ON ( ( dmap.MENU_ID = m.ID ) ) )
					JOIN dynamic_fields_config df ON ( ( df.ID = dmap.FIELD_ID ) ) 
				)
				JOIN farmer_dynamic_field_value fd ON ( ( fd.FIELD_NAME = df.COMPONENT_CODE ) ) 
			)
			JOIN dynamic_fields_config dref ON ( ( dref.ID = df.REFERENCE_ID ) ) 
		)
		LEFT JOIN dynamic_image_data dt ON ( ( cast( fd.ID AS CHAR charset BINARY ) = cast( dt.FARMER_DYNMAIC_FIELD_ID AS CHAR charset BINARY ) ) ) 
	) 
WHERE
	( df.REFERENCE_ID IS NOT NULL ) 
GROUP BY
	df.REFERENCE_ID,
	fd.FARMER_DYNAMIC_DATA_ID UNION
SELECT
	fd.FARMER_DYNAMIC_DATA_ID AS FARMER_DYNAMIC_DATA,
	df.COMPONENT_CODE AS FIELD_NAME,
	(
	CASE
			
			WHEN ( df.COMPONENT_TYPE IN ( '2', '4', '6' ) ) THEN
			( SELECT catalogue_value.NAME FROM catalogue_value WHERE ( catalogue_value.CODE = fd.FIELD_VALUE ) ) 
			WHEN ( df.COMPONENT_TYPE = '9' ) THEN
			( SELECT group_concat( catalogue_value.NAME SEPARATOR ',' ) FROM catalogue_value WHERE find_in_set( catalogue_value.CODE, fd.FIELD_VALUE ) ) 
			WHEN ( df.COMPONENT_TYPE = '12' ) THEN
			( CONVERT ( dt.ID USING utf8 ) COLLATE utf8_general_ci ) 
			WHEN ( df.COMPONENT_TYPE = '14' ) THEN
			concat(
				'Temperature :',
				substring_index( fd.FIELD_VALUE, '|', 1 ),
				' , Rain :',
				substring_index( substring_index( fd.FIELD_VALUE, '|', 2 ), '|',- ( 1 ) ),
				' , Humidity :',
				substring_index( substring_index( fd.FIELD_VALUE, '|', 3 ), '|',- ( 1 ) ),
				' , Wind :',
				substring_index( substring_index( fd.FIELD_VALUE, '|', 4 ), '|',- ( 1 ) ) 
			) ELSE fd.FIELD_VALUE 
		END 
		) AS FIELD_VALUE 
	FROM
		(
			(
				(
					( dynamic_menu_config m JOIN dynamic_menu_field_map dmap ON ( ( dmap.MENU_ID = m.ID ) ) )
					JOIN dynamic_fields_config df ON ( ( df.ID = dmap.FIELD_ID ) ) 
				)
				JOIN farmer_dynamic_field_value fd ON ( ( fd.FIELD_NAME = df.COMPONENT_CODE ) ) 
			)
			LEFT JOIN dynamic_image_data dt ON ( ( cast( fd.ID AS CHAR charset BINARY ) = cast( dt.FARMER_DYNMAIC_FIELD_ID AS CHAR charset BINARY ) ) ) 
		) 
	WHERE
		isnull( df.REFERENCE_ID ) 
	GROUP BY
	df.COMPONENT_CODE,
fd.FARMER_DYNAMIC_DATA_ID ;

ALTER TABLE dynamic_section_config 
ADD COLUMN M_BEFORE_INSERT varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER AFTER_INSERT,
ADD COLUMN M_AFTER_INSERT varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER M_BEFORE_INSERT;

ALTER TABLE dynamic_fields_config 
ADD COLUMN M_BEFORE_INSERT varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER PARENT_DEP_KEY,
ADD COLUMN M_AFTER_INSERT varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER M_BEFORE_INSERT;

-- -16-05-2018
ALTER TABLE trxn_agro 
ADD COLUMN DISTRIBUTION_STOCK_ID bigint(45) NULL AFTER PROCUREMENT_TRACEABILITY_ID;

DROP TABLE IF EXISTS distribution_stock;
CREATE TABLE distribution_stock  (
  ID bigint(45) NOT NULL AUTO_INCREMENT,
  RECEIPT_NO varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  TXN_TIME datetime NULL DEFAULT NULL,
  SENDER_WAREHOUSE bigint(45) NULL DEFAULT NULL,
  RECEIVER_WAREHOUSE bigint(45) NULL DEFAULT NULL,
  LOGGER_USER varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  DRIVER_NAME varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  TRUCK_ID varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  TXN_TYPE varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  STATUS tinyint(5) NULL DEFAULT NULL,
  BRANCH varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (ID) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

DROP TABLE IF EXISTS distribution_stock_detail;
CREATE TABLE distribution_stock_detail  (
  ID bigint(45) NOT NULL AUTO_INCREMENT,
  PRODUCT bigint(45) NULL DEFAULT NULL,
  DAMAGE_QTY double(20, 3) NULL DEFAULT NULL,
  DIST_QTY double(20, 3) NULL DEFAULT NULL,
  TOT_QTY double(20, 3) NULL DEFAULT NULL,
  DISTRIBUTION_STOCK_ID bigint(45) NULL DEFAULT NULL,
  PRIMARY KEY (ID) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- -18-05-2018
alter table farmer add ID_PROOF_PHOTO LONGBLOB after OTHER_ID_PROOF;

INSERT INTO farmer_field(ID, NAME, TYPE, TYPE_NAME, PARENT, STATUS, FIELD, DATA_LEVEL_ID, OTHERS) VALUES (null, 'ID Proof Image', '1', 'idProofImg', NULL, 1, NULL, NULL, 1);

ALTER TABLE dynamic_report_config_detail 
ADD COLUMN IS_FOOTER_SUM tinyint(5) NULL AFTER ALIGNMENT;


-- -17-05-2018
-- --Farmer Activity Report
SET GLOBAL log_bin_trust_function_creators = 1;
CREATE DEFINER=root@localhost FUNCTION getDynamicTxn(p_farmer_ID BIGINT(45)) RETURNS text CHARSET utf8
    COMMENT 'Fn to return answer value'
BEGIN
	-- Local Declaration
	DECLARE l_answer VARCHAR(255) DEFAULT '';
	
	
SELECT GROUP_CONCAT(q.TXN_TYPE,"-",q.CNT) INTO l_answer FROM (
SELECT
	dm.TXN_TYPE,COUNT(df.id) AS CNT, dm.ORDERZ AS ORDERZ
FROM
	farmer_dynamic_data df
	JOIN dynamic_menu_config dm ON df.TXN_TYPE = dm.TXN_TYPE
	JOIN farmer f ON FIND_IN_SET( f.id, df.REFERENCE_ID ) 
WHERE
	dm.ENTITY IN ( '1', '5' ) and f.id=p_farmer_ID GROUP BY dm.TXN_TYPE 
	UNION
SELECT
	dm.TXN_TYPE,COUNT(df.id) AS CNT, dm.ORDERZ AS ORDERZ
FROM
	farmer_dynamic_data df
	JOIN dynamic_menu_config dm ON df.TXN_TYPE = dm.TXN_TYPE
	JOIN farm f ON FIND_IN_SET( f.id, df.REFERENCE_ID ) 
	join farmer fm on fm.id = f.FARMER_ID and fm.id=p_farmer_ID
WHERE
	dm.ENTITY IN ( '2', '4' ) GROUP BY dm.TXN_TYPE
	UNION
	SELECT
	dm.TXN_TYPE,COUNT(df.id) AS CNT, dm.ORDERZ AS ORDERZ
FROM
	farmer_dynamic_data df
	JOIN dynamic_menu_config dm ON df.TXN_TYPE = dm.TXN_TYPE
	JOIN warehouse f ON FIND_IN_SET( f.id, df.REFERENCE_ID ) 
WHERE
	dm.ENTITY ='3'and f.id=p_farmer_ID GROUP BY dm.TXN_TYPE 
	
	) q order by q.TXN_TYPE ASC;
	
	
	return l_answer;
	
    END;
    
-- -01/06/2018   
    ALTER TABLE dynamic_report_config_detail 
CHANGE COLUMN ORDER ORDERR int(2) NULL DEFAULT NULL AFTER WIDTH,
CHANGE COLUMN STATUS STATUSS bigint(2) NULL DEFAULT NULL AFTER IS_EXPORT_AVAILABILITY;

ALTER TABLE dynamic_report_config_filter 
CHANGE COLUMN ORDER ORDERR int(2) NULL DEFAULT NULL;

-- -04-06-2018
ALTER TABLE loc_history
ADD COLUMN NET_STATUS  varchar(255) NULL AFTER ACCURACY;

ALTER TABLE loc_history
ADD COLUMN GPS_STATUS  varchar(255) NULL AFTER NET_STATUS;

-- -12-06-2018
ALTER TABLE distribution_stock 
ADD COLUMN SEASON varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER BRANCH_ID;



-- - Added Entity in dynamic Module - Farm Crops.
INSERT INTO dynamic_field_report_config(ID, ENTITY, TXN_TYPE, LABEL, NAME, IS_SUM_PROP, IS_GROUP_PROP, ORDERZ, IS_GRID_AVAILABLE, IS_EXPORT_AVAILABLE) VALUES (13, '6', NULL, 'profile.farm', 'fm.farmName', NULL, NULL, '3', '1', '1');
INSERT INTO dynamic_field_report_config(ID, ENTITY, TXN_TYPE, LABEL, NAME, IS_SUM_PROP, IS_GROUP_PROP, ORDERZ, IS_GRID_AVAILABLE, IS_EXPORT_AVAILABLE) VALUES (14, '6', NULL, 'profile.farmer', 'f.firstName', NULL, NULL, '4', '1', '1');
INSERT INTO dynamic_field_report_config(ID, ENTITY, TXN_TYPE, LABEL, NAME, IS_SUM_PROP, IS_GROUP_PROP, ORDERZ, IS_GRID_AVAILABLE, IS_EXPORT_AVAILABLE) VALUES (15, '6', NULL, 'villageName', 'vi.name', NULL, NULL, '5', '1', '1');
INSERT INTO dynamic_field_report_config(ID, ENTITY, TXN_TYPE, LABEL, NAME, IS_SUM_PROP, IS_GROUP_PROP, ORDERZ, IS_GRID_AVAILABLE, IS_EXPORT_AVAILABLE) VALUES (16, '6', NULL, 'groupName', 'g.name', NULL, NULL, '6', '1', '1');
INSERT INTO dynamic_field_report_config(ID, ENTITY, TXN_TYPE, LABEL, NAME, IS_SUM_PROP, IS_GROUP_PROP, ORDERZ, IS_GRID_AVAILABLE, IS_EXPORT_AVAILABLE) VALUES (17, '6', NULL, 'variety', 'pv.name', NULL, NULL, '1', '1', '1');
INSERT INTO dynamic_field_report_config(ID, ENTITY, TXN_TYPE, LABEL, NAME, IS_SUM_PROP, IS_GROUP_PROP, ORDERZ, IS_GRID_AVAILABLE, IS_EXPORT_AVAILABLE) VALUES (18, '6', NULL, 'farmcropName', 'pp.name', NULL, NULL, '2', '1', '1');

INSERT INTO dynamic_field_report_join_map(ID, FILTER_NAME, ENTITY_TYPE, PROPERTIES, FILTER_ID) VALUES (9, 'f.id', '6', 'f=fm.farmer', 'farmerId');
INSERT INTO dynamic_field_report_join_map(ID, FILTER_NAME, ENTITY_TYPE, PROPERTIES, FILTER_ID) VALUES (10, 'vi.id', '6', 'vi =f.village', NULL);
INSERT INTO dynamic_field_report_join_map(ID, FILTER_NAME, ENTITY_TYPE, PROPERTIES, FILTER_ID) VALUES (11, 'g.id', '6', 'g=f.samithi', 'groupId');
INSERT INTO dynamic_field_report_join_map(ID, FILTER_NAME, ENTITY_TYPE, PROPERTIES, FILTER_ID) VALUES (12, 'fm.id', '6', 'fm=farm', 'farmId');
INSERT INTO dynamic_field_report_join_map(ID, FILTER_NAME, ENTITY_TYPE, PROPERTIES, FILTER_ID) VALUES (13, 'pv.id', '6', 'pv=procurementVariety', 'varietyId');
INSERT INTO dynamic_field_report_join_map(ID, FILTER_NAME, ENTITY_TYPE, PROPERTIES, FILTER_ID) VALUES (14, 'pp.id', '6', 'pp=pv.procurementProduct', 'cropId');

-- -22-06-2018

ALTER TABLE farm_detailed_info
ADD COLUMN FIELD_NAME  varchar(100) NULL DEFAULT NULL AFTER PROCESSING_ACTIVITY,
ADD COLUMN FIELD_AREA  varchar(20) NULL DEFAULT NULL AFTER FIELD_NAME,
ADD COLUMN FIELD_CROP  varchar(100) NULL AFTER FIELD_AREA,
ADD COLUMN QUANTITY_APPLIED  varchar(20) NULL AFTER FIELD_CROP,
ADD COLUMN LAST_DATE_OF_CHEMICAL  date NULL DEFAULT NULL AFTER QUANTITY_APPLIED,
ADD COLUMN INPUT_APPLIED  varchar(100) NULL DEFAULT NULL AFTER LAST_DATE_OF_CHEMICAL,
ADD COLUMN INPUT_SOURCE  varchar(100) NULL DEFAULT NULL AFTER INPUT_APPLIED,
ADD COLUMN ACTIVITIES_IN_COCOCNUT  varchar(100) NULL DEFAULT NULL AFTER INPUT_SOURCE;



-- -25-06-2018
DROP TABLE IF EXISTS tree_detail;
CREATE TABLE tree_detail  (
  ID bigint(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  VARIETY varchar(25) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  YEARS varchar(25) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  NO_OF_TREES varchar(5) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  FARM_ID int(11) NULL DEFAULT NULL,
  PRODUCT_STATUS varchar(25) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (ID) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Compact;


ALTER TABLE farm 
ADD COLUMN PRESENCE_BANANA_TREE varchar(25) NULL AFTER NO_OF_CROTEN_TREE,
ADD COLUMN PARALLEL_PRODUCTION varchar(20) NULL AFTER PRESENCE_BANANA_TREE,
ADD COLUMN INPUT_ORGANIC_UNIT varchar(20) NULL AFTER PARALLEL_PRODUCTION,
ADD COLUMN PRESENCE_HIRED_LABOUR varchar(25) NULL AFTER INPUT_ORGANIC_UNIT,
ADD COLUMN RISK_CATEGORY varchar(25) NULL AFTER PRESENCE_HIRED_LABOUR,
ADD COLUMN TOTAL_ORGANIC_TREES double(20, 2) NULL DEFAULT NULL AFTER RISK_CATEGORY,
ADD COLUMN TOTAL_CONVENTIONAL_TREES double(20, 2) NULL DEFAULT NULL AFTER TOTAL_ORGANIC_TREES,
ADD COLUMN TOTAL_AVOCADO_TREES double(20, 2) NULL DEFAULT NULL AFTER TOTAL_CONVENTIONAL_TREES,
ADD COLUMN HECTAR_ORGANIC_TREES double(20, 2) NULL DEFAULT NULL AFTER TOTAL_AVOCADO_TREES,
ADD COLUMN HECTAR_CONVENTIONAL_TREES double(20, 2) NULL DEFAULT NULL AFTER HECTAR_ORGANIC_TREES,
ADD COLUMN HECTAR_AVOCADO_TREES double(20, 2) NULL DEFAULT NULL AFTER HECTAR_CONVENTIONAL_TREES;

-- - 06-28-2018 - -- 
ALTER TABLE farm 
ADD COLUMN PLOTTING_CAPTURING_TIME datetime NULL DEFAULT NULL AFTER HECTAR_AVOCADO_TREES;

ALTER TABLE farm_crops 
ADD COLUMN PLOTTING_CAPTURING_TIME datetime NULL DEFAULT NULL AFTER PRODUCTIVE_TREES;

-- - 06-29-2018 - --
INSERT INTO locale_property(id, code, lang_code, lang_value) VALUES (NULL, 'distribution.warehouseToMobileUserChartTitle', 'en', 'Distribution done for Warehouse to mobileuser');
INSERT INTO locale_property(id, code, lang_code, lang_value) VALUES (NULL, 'distribution.MobileUserToFarmerChartTitle', 'en', 'Distribution done for Mobile user to farmer');
INSERT INTO locale_property(id, code, lang_code, lang_value) VALUES (NULL, 'distribution.WarehouseToFarmerChartTitle', 'en', 'Distribution done for Warehouse to farmer');
INSERT INTO locale_property(id, code, lang_code, lang_value) VALUES (NULL, 'distribution.warehouseToMobileUserChartTitle', 'en', 'Distribution done for Warehouse to mobileuser');
INSERT INTO locale_property(id, code, lang_code, lang_value) VALUES (NULL, 'distribution.MobileUserToFarmerChartTitle', 'en', 'Distribution done for Mobile user to farmer');
INSERT INTO locale_property(id, code, lang_code, lang_value) VALUES (NULL, 'distribution.WarehouseToFarmerChartTitle', 'en', 'Distribution done for Warehouse to farmer');

ALTER TABLE farm_crops
ADD COLUMN AFFECTED_TREES  varchar(20) NULL AFTER PLOTTING_CAPTURING_TIME;


-- - 07-03-2018- --
ALTER TABLE farmer_training DROP FOREIGN KEY farmer_training_ibfk_1;

ALTER TABLE farmer_training 
CHANGE COLUMN TRAINING_TOPIC_ID TRAINING_TOPIC varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER CODE,
DROP INDEX FK_FT_TRAINING_TOPIC;

INSERT INTO pref(ID, NAME, VAL, ESE_ID, DESCRIPTION) VALUES (158, 'ENABLE_PRODUCT_RETURN_IMAGE', '1', 1, '0-No,1-Yes');


INSERT INTO dynamic_report_config_detail(ID, REPORT_CONFIG_ID, LABEL_NAME, FIELD, ACESS_TYPE, PARAMTERS, METHOD, GROUP_PROP, SUM_PROP, WIDTH, ORDERR, IS_GRID_AVAILABILITY, IS_EXPORT_AVAILABILITY, STATUSS, EXPRESSION, ALIGNMENT, IS_FOOTER_SUM) VALUES (NULL, 3, 'photo', 'id', 2, NULL, 'getPhoto', 0, 0, 100, 17, 1, 1, 1, NULL, NULL, NULL);


alter table dynamic_fields_config drop column COMPONENT_DATA_TYPE,
drop column LIST_METHOD_NAME,
add column IS_OTHER bigint(1) default 0;

-- -17-07-2018
ALTER TABLE transfer_info 
ADD COLUMN AGENT_TYPE varchar(10) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER OPER_TYPE;

-- -07-20-2018 - --

-- - Farmer Balance Report Detail updation - - -
update trxn_agro tn  join procurement p on p.TRXN_AGRO_ID = tn.ID and p.id is not null
set tn.PROCUREMENT_ID = p.id;

update trxn_agro tn join distribution p on p.TRXN_AGRO_ID = tn.ID and p.id is not null
set tn.DISTRIBUTION_ID = p.id;

CREATE TRIGGER UPDATE_TRX_AGRO_PROD_ID AFTER INSERT ON 
procurement FOR EACH ROW begin
update trxn_agro set procurement_id = NEW.ID where id = new.trxn_agro_id;
END;

CREATE TRIGGER UPDATE_TRXN_AGRO_DIS_ID AFTER INSERT ON 
distribution FOR EACH ROW begin
update trxn_agro set distribution_id = NEW.ID where id = new.trxn_agro_id;
END;

-- -07-24-2018

INSERT INTO dynamic_report_config_detailVALUES (5, 1, 'Mobile User', 'agroTxn.agentId', 2, NULL, 'getAgentByProfile', 0, 0, 200, 8, 1, 1, 1, NULL, NULL, NULL);
INSERT INTO dynamic_report_config_filterVALUES (18, 1, 'agroTxn.agentId', 'Mobile User', 'getAgentsList', 1, NULL, NULL, NULL);

INSERT INTO locale_property VALUES (NULL, 'exportColumnHeaderDistributionBranchfarmer', 'en', 'S.no,Organization,Date,Stock Type,Warehouse,Mobile User,Farmer Type,Farmer,Mobile Number,Village,Free Distribution,Total Quantity,Gross Amount (@*),Tax,Final Amount (@*),Payment Amount (@*),Season ');

INSERT INTO locale_property VALUES (NULL, 'exportColumnHeaderDistributionfarmer', 'en', 'S.no,Date,Stock Type,Warehouse,Mobile User,Farmer Type,Farmer,Mobile Number,Village,Free Distribution,Total Quantity,Gross Amount (@*),Tax,Final Amount (@*),Payment Amount (@*),Season ');

ALTER TABLE product 
ADD COLUMN MANUFACTURE varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER TYPE,
ADD COLUMN INGREDIENT varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER MANUFACTURE;

-- - Dynamic Export - - -
SELECT
	fd.FARMER_DYNAMIC_DATA_ID AS FARMER_DYNAMIC_DATA,
	dref.COMPONENT_CODE AS FIELD_NAME,
	group_concat(
	fd.TYPEZ,
	'~',
	fd.FIELD_NAME,
	'-',
	(
CASE
	
	WHEN ( df.COMPONENT_TYPE IN ( 2, 4, 6 ) ) THEN
	( SELECT catalogue_value.NAME FROM catalogue_value WHERE ( catalogue_value.CODE = fd.FIELD_VALUE ) ) 
	WHEN ( df.COMPONENT_TYPE = '9' ) THEN
	( SELECT group_concat( catalogue_value.NAME SEPARATOR ',' ) FROM catalogue_value WHERE find_in_set( catalogue_value.CODE, fd.FIELD_VALUE ) ) 
	WHEN ( df.COMPONENT_TYPE = '12' ) THEN
	( CONVERT ( dt.ID USING utf8 ) COLLATE utf8_general_ci ) 
	WHEN ( df.COMPONENT_TYPE = '14' ) THEN
	concat(
		'Temperature :',
		substring_index( fd.FIELD_VALUE, '|', 1 ),
		' , Rain :',
		substring_index( substring_index( fd.FIELD_VALUE, '|', 2 ), '|',- ( 1 ) ),
		' , Humidity :',
		substring_index( substring_index( fd.FIELD_VALUE, '|', 3 ), '|',- ( 1 ) ),
		' , Wind :',
		substring_index( substring_index( fd.FIELD_VALUE, '|', 4 ), '|',- ( 1 ) ) 
	) ELSE fd.FIELD_VALUE 
END 
	) SEPARATOR '|' 
	) AS FIELD_VALUE 
FROM
	(
		(
			(
				(
					( dynamic_menu_config m JOIN dynamic_menu_field_map dmap ON ( ( dmap.MENU_ID = m.ID ) ) )
					JOIN dynamic_fields_config df ON ( ( df.ID = dmap.FIELD_ID ) ) 
				)
				JOIN farmer_dynamic_field_value fd ON ( ( fd.FIELD_NAME = df.COMPONENT_CODE ) ) 
			)
			JOIN dynamic_fields_config dref ON ( ( dref.ID = df.REFERENCE_ID ) ) 
		)
		LEFT JOIN dynamic_image_data dt ON ( ( cast( fd.ID AS CHAR charset BINARY ) = cast( dt.FARMER_DYNMAIC_FIELD_ID AS CHAR charset BINARY ) ) ) 
	) 
WHERE
	( df.REFERENCE_ID IS NOT NULL ) 
GROUP BY
	df.REFERENCE_ID,
	fd.FARMER_DYNAMIC_DATA_ID UNION
SELECT
	fd.FARMER_DYNAMIC_DATA_ID AS FARMER_DYNAMIC_DATA,
	df.COMPONENT_CODE AS FIELD_NAME,
	(
	CASE
			
			WHEN ( df.COMPONENT_TYPE IN ( '2', '4', '6' ) ) THEN
			( SELECT catalogue_value.NAME FROM catalogue_value WHERE ( catalogue_value.CODE = fd.FIELD_VALUE ) ) 
			WHEN ( df.COMPONENT_TYPE = '9' ) THEN
			( SELECT group_concat( catalogue_value.NAME SEPARATOR ',' ) FROM catalogue_value WHERE find_in_set( catalogue_value.CODE, fd.FIELD_VALUE ) ) 
			WHEN ( df.COMPONENT_TYPE = '12' ) THEN
			( CONVERT ( dt.ID USING utf8 ) COLLATE utf8_general_ci ) 
			WHEN ( df.COMPONENT_TYPE = '14' ) THEN
			concat(
				'Temperature :',
				substring_index( fd.FIELD_VALUE, '|', 1 ),
				' , Rain :',
				substring_index( substring_index( fd.FIELD_VALUE, '|', 2 ), '|',- ( 1 ) ),
				' , Humidity :',
				substring_index( substring_index( fd.FIELD_VALUE, '|', 3 ), '|',- ( 1 ) ),
				' , Wind :',
				substring_index( substring_index( fd.FIELD_VALUE, '|', 4 ), '|',- ( 1 ) ) 
			) ELSE fd.FIELD_VALUE 
		END 
		) AS FIELD_VALUE 
	FROM
		(
			(
				(
					( dynamic_menu_config m JOIN dynamic_menu_field_map dmap ON ( ( dmap.MENU_ID = m.ID ) ) )
					JOIN dynamic_fields_config df ON ( ( df.ID = dmap.FIELD_ID ) ) 
				)
				JOIN farmer_dynamic_field_value fd ON ( ( fd.FIELD_NAME = df.COMPONENT_CODE ) ) 
			)
			LEFT JOIN dynamic_image_data dt ON ( ( cast( fd.ID AS CHAR charset BINARY ) = cast( dt.FARMER_DYNMAIC_FIELD_ID AS CHAR charset BINARY ) ) ) 
		) 
	WHERE
		isnull( df.REFERENCE_ID ) 
	GROUP BY
	df.COMPONENT_CODE,
fd.FARMER_DYNAMIC_DATA_ID;


drop view if exists dynamic_export;
CREATE OR REPLACE ALGORITHM = UNDEFINED DEFINER = root@localhost SQL SECURITY DEFINER VIEW dynamic_export AS SELECT
	fd.FARMER_DYNAMIC_DATA_ID AS FARMER_DYNAMIC_DATA,
	dref.COMPONENT_CODE AS FIELD_NAME,
	group_concat(
	fd.TYPEZ,
	'~',
	fd.FIELD_NAME,
	'#',
	(
CASE
	
	WHEN ( df.COMPONENT_TYPE IN ( 2, 4, 6 ) ) THEN
	( SELECT catalogue_value.NAME FROM catalogue_value WHERE ( catalogue_value.CODE = fd.FIELD_VALUE ) ) 
	WHEN ( df.COMPONENT_TYPE = '9' ) THEN
	( SELECT group_concat( catalogue_value.NAME SEPARATOR ',' ) FROM catalogue_value WHERE find_in_set( catalogue_value.CODE, fd.FIELD_VALUE ) ) 
	WHEN ( df.COMPONENT_TYPE = '12' ) THEN
	( CONVERT ( dt.ID USING utf8 ) COLLATE utf8_general_ci ) 
	WHEN ( df.COMPONENT_TYPE = '14' ) THEN
	concat(
		'Temperature :',
		substring_index( fd.FIELD_VALUE, '|', 1 ),
		' , Rain :',
		substring_index( substring_index( fd.FIELD_VALUE, '|', 2 ), '|',- ( 1 ) ),
		' , Humidity :',
		substring_index( substring_index( fd.FIELD_VALUE, '|', 3 ), '|',- ( 1 ) ),
		' , Wind :',
		substring_index( substring_index( fd.FIELD_VALUE, '|', 4 ), '|',- ( 1 ) ) 
	) ELSE fd.FIELD_VALUE 
END 
	) SEPARATOR '|' 
	) AS FIELD_VALUE 
FROM
	(
		(
			(
				(
					( dynamic_menu_config m JOIN dynamic_menu_field_map dmap ON ( ( dmap.MENU_ID = m.ID ) ) )
					JOIN dynamic_fields_config df ON ( ( df.ID = dmap.FIELD_ID ) ) 
				)
				JOIN farmer_dynamic_field_value fd ON ( ( fd.FIELD_NAME = df.COMPONENT_CODE ) ) 
			)
			JOIN dynamic_fields_config dref ON ( ( dref.ID = df.REFERENCE_ID ) ) 
		)
		LEFT JOIN dynamic_image_data dt ON ( ( cast( fd.ID AS CHAR charset BINARY ) = cast( dt.FARMER_DYNMAIC_FIELD_ID AS CHAR charset BINARY ) ) ) 
	) 
WHERE
	( df.REFERENCE_ID IS NOT NULL ) 
GROUP BY
	df.REFERENCE_ID,
	fd.FARMER_DYNAMIC_DATA_ID UNION
SELECT
	fd.FARMER_DYNAMIC_DATA_ID AS FARMER_DYNAMIC_DATA,
	df.COMPONENT_CODE AS FIELD_NAME,
	(
	CASE
			
			WHEN ( df.COMPONENT_TYPE IN ( '2', '4', '6' ) ) THEN
			( SELECT catalogue_value.NAME FROM catalogue_value WHERE ( catalogue_value.CODE = fd.FIELD_VALUE ) ) 
			WHEN ( df.COMPONENT_TYPE = '9' ) THEN
			( SELECT group_concat( catalogue_value.NAME SEPARATOR ',' ) FROM catalogue_value WHERE find_in_set( catalogue_value.CODE, fd.FIELD_VALUE ) ) 
			WHEN ( df.COMPONENT_TYPE = '12' ) THEN
			( CONVERT ( dt.ID USING utf8 ) COLLATE utf8_general_ci ) 
			WHEN ( df.COMPONENT_TYPE = '14' ) THEN
			concat(
				'Temperature :',
				substring_index( fd.FIELD_VALUE, '|', 1 ),
				' , Rain :',
				substring_index( substring_index( fd.FIELD_VALUE, '|', 2 ), '|',- ( 1 ) ),
				' , Humidity :',
				substring_index( substring_index( fd.FIELD_VALUE, '|', 3 ), '|',- ( 1 ) ),
				' , Wind :',
				substring_index( substring_index( fd.FIELD_VALUE, '|', 4 ), '|',- ( 1 ) ) 
			) ELSE fd.FIELD_VALUE 
		END 
		) AS FIELD_VALUE 
	FROM
		(
			(
				(
					( dynamic_menu_config m JOIN dynamic_menu_field_map dmap ON ( ( dmap.MENU_ID = m.ID ) ) )
					JOIN dynamic_fields_config df ON ( ( df.ID = dmap.FIELD_ID ) ) 
				)
				JOIN farmer_dynamic_field_value fd ON ( ( fd.FIELD_NAME = df.COMPONENT_CODE ) ) 
			)
			LEFT JOIN dynamic_image_data dt ON ( ( cast( fd.ID AS CHAR charset BINARY ) = cast( dt.FARMER_DYNMAIC_FIELD_ID AS CHAR charset BINARY ) ) ) 
		) 
	WHERE
		isnull( df.REFERENCE_ID ) 
	GROUP BY
	df.COMPONENT_CODE,
fd.FARMER_DYNAMIC_DATA_ID;

ALTER TABLE dynamic_menu_field_map 
MODIFY COLUMN MENU_ID bigint(45) NULL AFTER ID,
MODIFY COLUMN FIELD_ID bigint(45) NULL AFTER MENU_ID,
DROP PRIMARY KEY,
ADD PRIMARY KEY (ID) USING BTREE;


ALTER TABLE dynamic_menu_section_map 
MODIFY COLUMN MENU_ID bigint(45) NULL AFTER ID,
MODIFY COLUMN SECTION_ID bigint(45) NULL AFTER MENU_ID,
DROP PRIMARY KEY,
ADD PRIMARY KEY (ID) USING BTREE;

-- -08-13-2018
ALTER TABLE farmer_field 
ADD COLUMN farmerProfileExport bigint(3) NULL DEFAULT 0 AFTER OTHERS;

ALTER TABLE farm_field 
ADD COLUMN farmerProfileExport bigint(3) NULL DEFAULT 0 AFTER OTHERS;

ALTER TABLE farm_crops_field 
ADD COLUMN farmerProfileExport bigint(3) NULL DEFAULT 0 AFTER STATUS;

-- 20-08-2018

INSERT INTO ese_ent VALUES (null, 'service.dynamicCertification.delete');
INSERT INTO ese_role_ent VALUES (1, (SELECT id FROM ese_ent WHERE NAME='service.dynamicCertification.delete'));


-- -21 Aug 2018
-- -Griffith - Cold Storage Table
INSERT INTO pref(ID, NAME, VAL, ESE_ID, DESCRIPTION) VALUES (0, 'ENABLE_STORAGE', '1', 0, NULL);

-- ----------------------------
-- Table structure for warehouse_storage_map
-- ----------------------------
DROP TABLE IF EXISTS warehouse_storage_map;
CREATE TABLE warehouse_storage_map  (
  ID bigint(15) NOT NULL AUTO_INCREMENT,
  WAREHOUSE_ID bigint(10) NULL DEFAULT NULL,
  COLD_STORAGE_NAME varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  MAX_BAY_HOLD double(20, 3) NULL DEFAULT NULL,
  BRANCH_ID varchar(25) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (ID) USING BTREE,
  INDEX IDX_WAREHOUSE_MAP1(WAREHOUSE_ID) USING BTREE,
  CONSTRAINT warehouse_storage_map_ibfk_1 FOREIGN KEY (WAREHOUSE_ID) REFERENCES warehouse (ID) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for cold_storage_entry
-- ----------------------------
DROP TABLE IF EXISTS cold_storage_entry;
CREATE TABLE cold_storage_entry  (
  ID bigint(15) NOT NULL AUTO_INCREMENT,
  WAREHOUSE_ID bigint(15) NULL DEFAULT NULL,
  COLD_STORAGE_NAME varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  BATCH_NO varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  BOND_NO varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  TOTAL_BAGS bigint(25) NULL DEFAULT 0,
  TOTAL_QTY double(25, 3) NULL DEFAULT 0.000,
  CREATED_DATE datetime NULL DEFAULT NULL,
  CREATED_USER_NAME varchar(90) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  UPDATED_DATE datetime NULL DEFAULT NULL,
  UPDATED_USER_NAME varchar(90) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  BRANCH_ID varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  REVISION_NO varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (ID) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for cold_storage_entry_detail
-- ----------------------------
DROP TABLE IF EXISTS cold_storage_entry_detail;
CREATE TABLE cold_storage_entry_detail  (
  ID bigint(15) NOT NULL AUTO_INCREMENT,
  PROCUREMENT_GRADE_ID bigint(15) NULL DEFAULT NULL,
  BLOCK_NAME varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  FLOOR_NAME varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  BAY_NUMBER varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  NO_OF_BAGS bigint(25) NULL DEFAULT NULL,
  TXN_QTY double(25, 3) NULL DEFAULT NULL,
  COLD_STORAGE_ENTRY_ID bigint(15) NULL DEFAULT NULL,
  PRIMARY KEY (ID) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Compact;

ALTER TABLE city_warehouse 
ADD COLUMN COLD_STORAGE_NAME varchar(45) NULL AFTER FARMER_ID;

ALTER TABLE city_warehouse_detail 
ADD COLUMN BLOCK_NAME varchar(45) NULL AFTER BATCH_NO,
ADD COLUMN FLOOR_NAME varchar(45) NULL AFTER BLOCK_NAME,
ADD COLUMN BAY_NUMBER varchar(45) NULL AFTER FLOOR_NAME;


INSERT INTO ese_menu VALUES (0, 'service.coldStorageEntryService', 'Cold Storage Entry', 'coldStorageEntry_list.action', 10, 3, 0, 0, 0, 0, '');
INSERT INTO ese_ent VALUES (0, 'service.coldStorageEntryService.list');
INSERT INTO ese_ent VALUES (0, 'service.coldStorageEntryService.create');
INSERT INTO ese_ent VALUES (0, 'service.coldStorageEntryService.update');
INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='service.coldStorageEntryService'), '1');
INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='service.coldStorageEntryService'), '2');
INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='service.coldStorageEntryService'), '3');
INSERT INTO ese_role_ent VALUES ('1', (SELECT id FROM ese_ent WHERE NAME='service.coldStorageEntryService.list'));
INSERT INTO ese_role_ent VALUES ('2', (SELECT id FROM ese_ent WHERE NAME='service.coldStorageEntryService.list'));
INSERT INTO ese_role_menu VALUES (0,(SELECT id FROM ese_menu WHERE NAME='service.service.coldStorageEntryService'), '1');


-- -22-08-2018
alter table dynamic_fields_config  add column PROFILE_FIELD varchar(250), add column IS_PROFILE_UPDATE BIGINT(2);
alter table farmer_dynamic_field_value  add column PROFILE_FIELD varchar(250), add column IS_PROFILE_UPDATE BIGINT(2);


alter table  dynamic_menu_config add column IS_ACTIVITY BIGINT(2) default 0;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for saved_dynamic_report
-- ----------------------------
DROP TABLE IF EXISTS saved_dynamic_report;
CREATE TABLE saved_dynamic_report  (
  id int(11) NOT NULL AUTO_INCREMENT,
  title varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  des varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  query varchar(2550) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  header_fields varchar(2550) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  filter_data varchar(2550) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  ENTITY varchar(10) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  FIELDS_SELECTED longtext CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL,
  GROUP_BY_FIELD varchar(250) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;




SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for dynamic_report_fields_config
-- ----------------------------
DROP TABLE IF EXISTS dynamic_report_fields_config;
CREATE TABLE dynamic_report_fields_config  (
  id int(10) NOT NULL AUTO_INCREMENT,
  field varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  label varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  dataType varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  Is_GroupByField varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  entity varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  filter_alias varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for dynamic_report_table_config
-- ----------------------------
DROP TABLE IF EXISTS dynamic_report_table_config;
CREATE TABLE dynamic_report_table_config  (
  id int(11) NOT NULL AUTO_INCREMENT,
  EntityName varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  Table varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  Alias varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  Parent int(255) NULL DEFAULT NULL,
  JoinString varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  EntityQuery varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  Entity int(255) NULL DEFAULT NULL,
  PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;

-- -23-08-2018
alter table dynamic_image_data add column FARMER_DYNMAIC_DATA_ID BIGINT(45);

-- -30 Aug 2018
INSERT INTO ese_seq(ID, SEQ_KEY, SEQ_VAL) VALUES (0, 'COLD_STORAGE_STOCK_TRANSFER_RECEIPT_NO_SEQ', 0);


-- ----------------------------
-- Table structure for cold_storage_stock_transfer
-- ----------------------------
DROP TABLE IF EXISTS cold_storage_stock_transfer;
CREATE TABLE cold_storage_stock_transfer  (
  ID bigint(15) NOT NULL AUTO_INCREMENT,
  TXN_DATE datetime NULL DEFAULT NULL,
  WAREHOUSE_ID bigint(15) NULL DEFAULT NULL,
  COLD_STORAGE_NAME varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  BATCH_NO varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  BUYER_ID bigint(15) NULL DEFAULT NULL,
  TRUCK_ID varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  DRIVER_NAME varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  CREATED_USER_NAME varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  CREATED_DATE datetime NULL DEFAULT NULL,
  UPDATED_USER_NAME varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  UPDATED_DATE datetime NULL DEFAULT NULL,
  BRANCH_ID varchar(25) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  RECEIPT_NO varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  REVISION_NO varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (ID) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for cold_storage_stock_transfer_detail
-- ----------------------------
DROP TABLE IF EXISTS cold_storage_stock_transfer_detail;
CREATE TABLE cold_storage_stock_transfer_detail  (
  ID bigint(15) NOT NULL AUTO_INCREMENT,
  PROCUREMENT_PRODUCT_ID bigint(15) NULL DEFAULT NULL,
  PROCURMENT_GRADE_ID bigint(15) NULL DEFAULT NULL,
  BLOCK_NAME varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  FLOOR_NAME varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  BAY_NUMBER varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  NO_OF_BAGS int(50) NULL DEFAULT NULL,
  QTY double(25, 2) NULL DEFAULT NULL,
  COLD_STORAGE_STOCK_TRANSFER_ID bigint(15) NULL DEFAULT NULL,
  PRIMARY KEY (ID) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Compact;


-- - Cold Storage Stok Transfer Menu Script

INSERT INTO ese_menu VALUES (0, 'service.coldStorageStockTransfer', 'Cold Storage Stock Transfer', 'coldStorageStockTransfer_list.action', 11, 3, 0, 0, 0, 0, NULL);

INSERT INTO ese_ent() VALUES (0, 'service.coldStorageStockTransfer.list');
INSERT INTO ese_ent() VALUES (0, 'service.coldStorageStockTransfer.create');
INSERT INTO ese_ent() VALUES (0, 'service.coldStorageStockTransfer.update');

INSERT INTO ese_role_menu VALUES (null,(SELECT id FROM ese_menu WHERE NAME='service.coldStorageStockTransfer'), '1');

INSERT INTO ese_role_ent VALUES ('1',(SELECT id FROM ese_ent WHERE NAME='service.coldStorageStockTransfer.list'));
INSERT INTO ese_role_ent VALUES ('1',(SELECT id FROM ese_ent WHERE NAME='service.coldStorageStockTransfer.create'));
INSERT INTO ese_role_ent VALUES ('1',(SELECT id FROM ese_ent WHERE NAME='service.coldStorageStockTransfer.update'));


INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='service.coldStorageStockTransfer'), '1');
INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='service.coldStorageStockTransfer'), '2');
INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='service.coldStorageStockTransfer'), '3');

-- -04 SEPT 2018
-- - Fruit Master
INSERT INTO pref VALUES (0, 'NO_OF_CRATES', '1', 1, NULL);
INSERT INTO farm_field VALUES (0, 'totalLandKanalName', '1', 'totalLandKanalName', 1, 1, 0, 0);
INSERT INTO farm_field VALUES (0, 'proposedPlantingAreaName', '1', 'proposedPlantingAreaName', 1, 1, 0, 0);

-- -09 SEPT 2018
-- - Coffee Board
alter table farm add column PHOTO_ID VARCHAR(20) NULL AFTER PLOTTING_CAPTURING_TIME;
 
-- 25 SEPT 2018
--Olivado
ALTER TABLE sensitizing 
ADD COLUMN VILLAGE varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER UPDATED_DT;

-- -26-09-2018
SELECT
	fd.FARMER_DYNAMIC_DATA_ID AS FARMER_DYNAMIC_DATA,
	dref.COMPONENT_CODE AS FIELD_NAME,
	group_concat(
	fd.TYPEZ,
	'~',
	fd.FIELD_NAME,
	'#',
	(
CASE
	
	WHEN ( df.COMPONENT_TYPE IN ( 2, 4, 6 ) ) THEN
	getAnswerDynamic ( df.ACCESS_TYPE, fd.FIELD_VALUE, df.CATALOGUE_TYPE, 0 ) 
	WHEN ( df.COMPONENT_TYPE = '9' ) THEN
	getAnswerDynamic ( df.ACCESS_TYPE, fd.FIELD_VALUE, df.CATALOGUE_TYPE, 1 ) 
	WHEN ( df.COMPONENT_TYPE = '12' ) THEN
	( CONVERT ( dt.ID USING utf8 ) COLLATE utf8_general_ci ) 
	WHEN ( df.COMPONENT_TYPE = '14' ) THEN
	concat(
		'Temperature :',
		substring_index( fd.FIELD_VALUE, '|', 1 ),
		' , Rain :',
		substring_index( substring_index( fd.FIELD_VALUE, '|', 2 ), '|',- ( 1 ) ),
		' , Humidity :',
		substring_index( substring_index( fd.FIELD_VALUE, '|', 3 ), '|',- ( 1 ) ),
		' , Wind :',
		substring_index( substring_index( fd.FIELD_VALUE, '|', 4 ), '|',- ( 1 ) ) 
	) ELSE fd.FIELD_VALUE 
END 
	) SEPARATOR '|' 
	) AS FIELD_VALUE 
FROM
	(
		(
			(
				(
					( dynamic_menu_config m JOIN dynamic_menu_field_map dmap ON ( ( dmap.MENU_ID = m.ID ) ) )
					JOIN dynamic_fields_config df ON ( ( df.ID = dmap.FIELD_ID ) ) 
				)
				JOIN farmer_dynamic_field_value fd ON ( ( fd.FIELD_NAME = df.COMPONENT_CODE ) ) 
			)
			JOIN dynamic_fields_config dref ON ( ( dref.ID = df.REFERENCE_ID ) ) 
		)
		LEFT JOIN dynamic_image_data dt ON ( ( cast( fd.ID AS CHAR charset BINARY ) = cast( dt.FARMER_DYNMAIC_FIELD_ID AS CHAR charset BINARY ) ) ) 
	) 
WHERE
	( df.REFERENCE_ID IS NOT NULL ) 
GROUP BY
	df.REFERENCE_ID,
	fd.FARMER_DYNAMIC_DATA_ID UNION
SELECT
	fd.FARMER_DYNAMIC_DATA_ID AS FARMER_DYNAMIC_DATA,
	df.COMPONENT_CODE AS FIELD_NAME,
	(
	CASE
			
			WHEN ( df.COMPONENT_TYPE IN ( 2, 4, 6 ) ) THEN
			getAnswerDynamic ( df.ACCESS_TYPE, fd.FIELD_VALUE, df.CATALOGUE_TYPE, 0 ) 
			WHEN ( df.COMPONENT_TYPE = '9' ) THEN
			getAnswerDynamic ( df.ACCESS_TYPE, fd.FIELD_VALUE, df.CATALOGUE_TYPE, 1 ) 
			WHEN ( df.COMPONENT_TYPE = '12' ) THEN
			( CONVERT ( dt.ID USING utf8 ) COLLATE utf8_general_ci ) 
			WHEN ( df.COMPONENT_TYPE = '14' ) THEN
			concat(
				'Temperature :',
				substring_index( fd.FIELD_VALUE, '|', 1 ),
				' , Rain :',
				substring_index( substring_index( fd.FIELD_VALUE, '|', 2 ), '|',- ( 1 ) ),
				' , Humidity :',
				substring_index( substring_index( fd.FIELD_VALUE, '|', 3 ), '|',- ( 1 ) ),
				' , Wind :',
				substring_index( substring_index( fd.FIELD_VALUE, '|', 4 ), '|',- ( 1 ) ) 
			) ELSE fd.FIELD_VALUE 
		END 
		) AS FIELD_VALUE 
	FROM
		(
			(
				(
					( dynamic_menu_config m JOIN dynamic_menu_field_map dmap ON ( ( dmap.MENU_ID = m.ID ) ) )
					JOIN dynamic_fields_config df ON ( ( df.ID = dmap.FIELD_ID ) ) 
				)
				JOIN farmer_dynamic_field_value fd ON ( ( fd.FIELD_NAME = df.COMPONENT_CODE ) ) 
			)
			LEFT JOIN dynamic_image_data dt ON ( ( cast( fd.ID AS CHAR charset BINARY ) = cast( dt.FARMER_DYNMAIC_FIELD_ID AS CHAR charset BINARY ) ) ) 
		) 
	WHERE
		isnull( df.REFERENCE_ID ) 
	GROUP BY
	df.COMPONENT_CODE,
fd.FARMER_DYNAMIC_DATA_ID


CREATE DEFINER=root@localhost FUNCTION getAnswerDynamic(accessType BIGINT(45), fieldName varchar(45),cataType BIGINT(45), isMulti BIGINT(45)) RETURNS text CHARSET utf8 COLLATE utf8_unicode_ci
BEGIN
declare resultVal varchar(100);
set resultVal = (
case when  accessType='1' and isMulti ='0' then 
	
		 (
			SELECT
				catalogue_value.NAME 
			FROM
				catalogue_value 
			WHERE
				( ( catalogue_value.CODE = fieldName) AND ( catalogue_value.TYPEZ =cataType) ) 
			) 
		when  accessType='1' and isMulti ='1' then 
			 	(
			SELECT
				group_concat( catalogue_value.NAME SEPARATOR ',' ) 
			FROM
				catalogue_value 
			WHERE
				( find_in_set( catalogue_value.CODE, fieldName ) AND ( catalogue_value.TYPEZ =cataType ) ) 
			) 
			when  accessType='3' and isMulti ='0' then 
			
			 (case WHEN  cataType = '9'  THEN
			(SELECT procurement_product.NAME FROM procurement_product WHERE  procurement_product.CODE = fieldName )
			WHEN  cataType = '10'  THEN
			( SELECT procurement_variety.NAME FROM procurement_variety WHEREprocurement_variety.CODE = fieldName)
			WHEN  cataType = '13' THEN
		( SELECT procurement_grade.NAME FROM procurement_grade WHERE  procurement_grade.CODE =fieldName)
		 END )
		 when  accessType='3' and isMulti ='1' then 
			
			 (case WHEN  cataType = '9'  THEN
			(SELECT GROUP_concat(procurement_product.NAME) FROM procurement_product WHERE  find_in_set(procurement_product.CODE , fieldName))
			WHEN  cataType = '10'  THEN
			( SELECT GROUP_concat(procurement_variety.NAME) FROM procurement_variety WHERE find_in_set(procurement_variety.CODE, fieldName))
			WHEN  cataType = '13' THEN
		( SELECT GROUP_concat(procurement_grade.NAME) FROM procurement_grade WHERE  find_in_set(procurement_grade.CODE, fieldName))
		 END)
		 
		 ELSE  ''
	 END);
	 
	 return resultVal;

END

-- 01.10.2018

ALTER TABLE farm_crops 
ADD COLUMN CROP_EDIT_STATUS bigint(10) NULL DEFAULT 1 AFTER AFFECTED_TREES;
-- -01-10-2018
ALTER TABLE farm 
ADD COLUMN ACTIVE_COORDINATES bigint(20) NULL AFTER PHOTO_ID;

ALTER TABLE farm_crops 
ADD COLUMN ACTIVE_COORDINATES bigint(20) NULL AFTER AFFECTED_TREES;

ALTER TABLE coordinates 
ADD COLUMN COORDINATES_MAP_ID bigint(20) NULL AFTER ORDER_NO;

ALTER TABLE farm_crops_coordinates 
ADD COLUMN COORDINATES_MAP_ID bigint(20) NULL AFTER ORDER_NO;

SET SESSION group_concat_max_len = 1000000;

-- 08-10-2018
DROP TABLE IF EXISTS coordinates_map;
CREATE TABLE coordinates_map  (
  ID bigint(20) NOT NULL AUTO_INCREMENT,
  DATE datetime NULL DEFAULT NULL,
  AGENT_ID varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  STATUS tinyint(5) NULL DEFAULT 0,
  FARM_ID bigint(20) NULL DEFAULT NULL,
  FARM_CROPS_ID bigint(20) NULL DEFAULT NULL,
  AREA varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  MID_LATITUDE varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  MID_LONGITUDE varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (ID) USING BTREE
) 

-- 10-10-2018
ALTER TABLE dynamic_report_config 
ADD COLUMN ALIAS varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER XLS_FILE,
ADD COLUMN PARENT_ID int(3) NULL DEFAULT NULL AFTER ALIAS,
ADD COLUMN GROUP_PROPERTY varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER PARENT_ID;

-- 23-10-2018
ALTER TABLE pref 
MODIFY COLUMN VAL text CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL AFTER NAME;

-- - 24 OCT 2018
-- - Crop Calendar
-- ----------------------------
-- Table structure for crop_calendar
-- ----------------------------
DROP TABLE IF EXISTS crop_calendar;
CREATE TABLE crop_calendar  (
  ID bigint(10) NOT NULL AUTO_INCREMENT,
  TXN_DATE datetime NULL DEFAULT NULL,
  NAME varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  CROP_ID bigint(45) NULL DEFAULT NULL,
  VARIETY_ID bigint(45) NULL DEFAULT NULL,
  SEASON_CODE varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  ACTIVITY_TYPE int(3) NULL DEFAULT NULL,
  BRANCH_ID varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  CREATED_USER varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  CREATED_DATE datetime NULL DEFAULT NULL,
  UPDATED_USER varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  UPDATED_DATE datetime NULL DEFAULT NULL,
  REVISION_NO varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (ID) USING BTREE,
  INDEX FK_CROP_CALENDAR_PROCUREMENT_PRODUCT(CROP_ID) USING BTREE,
  INDEX FK_CROP_CALENDAR_PROCUREMENT_VARIETY(VARIETY_ID) USING BTREE,
  CONSTRAINT crop_calendar_ibfk_1 FOREIGN KEY (CROP_ID) REFERENCES procurement_product (ID) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT crop_calendar_infk_2 FOREIGN KEY (VARIETY_ID) REFERENCES procurement_variety (ID) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for crop_calendar_detail
-- ----------------------------
DROP TABLE IF EXISTS crop_calendar_detail;
CREATE TABLE crop_calendar_detail  (
  ID bigint(10) NOT NULL AUTO_INCREMENT,
  CROP_CALENDAR_ID bigint(10) NULL DEFAULT NULL,
  ACTIVITY_METHOD varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  NO_OF_DAYS varchar(25) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (ID) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Compact;


INSERT INTO pref(ID, NAME, VAL, ESE_ID, DESCRIPTION) VALUES (0, 'ENABLE_CROP_CALENDAR', '1', 0, '');

INSERT INTO ese_menu VALUES (0, 'service.cropCalendar', 'Crop Calendar', 'cropCalendar_list.action', 11, 3, 0, 0, 0, 0, NULL);

INSERT INTO ese_ent() VALUES (0, 'service.cropCalendar.list');
INSERT INTO ese_ent() VALUES (0, 'service.cropCalendar.create');
INSERT INTO ese_ent() VALUES (0, 'service.cropCalendar.update');

INSERT INTO ese_role_menu VALUES (null,(SELECT id FROM ese_menu WHERE NAME='service.cropCalendar'), '1');

INSERT INTO ese_role_ent VALUES ('1',(SELECT id FROM ese_ent WHERE NAME='service.cropCalendar.list'));
INSERT INTO ese_role_ent VALUES ('1',(SELECT id FROM ese_ent WHERE NAME='service.cropCalendar.create'));
INSERT INTO ese_role_ent VALUES ('1',(SELECT id FROM ese_ent WHERE NAME='service.cropCalendar.update'));


INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='service.cropCalendar'), '1');
INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='service.cropCalendar'), '2');
INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='service.cropCalendar'), '3');


-- - Farm Crop Calendar Txn
-- ----------------------------
-- Table structure for farm_crop_calendar
-- ----------------------------
DROP TABLE IF EXISTS farm_crop_calendar;
CREATE TABLE farm_crop_calendar  (
  ID int(15) NOT NULL AUTO_INCREMENT,
  FARM_ID bigint(25) NULL DEFAULT NULL,
  VARIETY_ID bigint(25) NULL DEFAULT NULL,
  AGENT_ID varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  SEASON_CODE varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  CREATED_DATE datetime NULL DEFAULT NULL,
  CREATED_USER varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  UPDATED_DATE datetime NULL DEFAULT NULL,
  UPDATED_USER varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  BRANCH_ID varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (ID) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for farm_crop_calendar_detail
-- ----------------------------
DROP TABLE IF EXISTS farm_crop_calendar_detail;
CREATE TABLE farm_crop_calendar_detail  (
  ID int(15) NOT NULL AUTO_INCREMENT,
  ACTIVITY_METHOD varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  STATUS int(1) NULL DEFAULT NULL,
  DATE varchar(15) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  REMARKS varchar(90) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  FARM_CROP_CALENDAR_ID int(15) NULL DEFAULT NULL,
  PRIMARY KEY (ID) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for offline_farm_crop_calendar
-- ----------------------------
DROP TABLE IF EXISTS offline_farm_crop_calendar;
CREATE TABLE offline_farm_crop_calendar  (
  ID int(10) NOT NULL AUTO_INCREMENT,
  FARM_CODE varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  VARIETY_ID varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  SEASON_CODE varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  CREATED_DATE varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  CREATED_USER varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  UPDATED_DATE varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  UPDATED_USER varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  BRANCH_ID varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  AGENT_ID varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  DEVICE_ID varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  STATUS_CODE tinyint(1) NULL DEFAULT NULL,
  STATUS_MSG varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (ID) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for offline_farm_crop_calendar_detail
-- ----------------------------
DROP TABLE IF EXISTS offline_farm_crop_calendar_detail;
CREATE TABLE offline_farm_crop_calendar_detail  (
  ID int(15) NOT NULL AUTO_INCREMENT,
  ACTIVITY_METHOD varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  STATUS varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  DATE varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  REMARKS varchar(90) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  OFFLINE_FARM_CROP_CALENDAR_ID int(15) NULL DEFAULT NULL,
  PRIMARY KEY (ID) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;


-- - 03 Dec 2018

-- ----------------------------
-- Table structure for dynamic_field_score_map
-- ----------------------------
DROP TABLE IF EXISTS dynamic_field_score_map;
CREATE TABLE dynamic_field_score_map  (
  ID bigint(45) NOT NULL AUTO_INCREMENT,
  DYNAMIC_MENU_FIELD_MAP_ID bigint(45) NOT NULL,
  CATALOGUE_CODE varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  SCORE int(15) NULL DEFAULT NULL,
  PRIMARY KEY (ID, DYNAMIC_MENU_FIELD_MAP_ID) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Compact;


ALTER TABLE dynamic_menu_field_map 
ADD COLUMN SCORE_TYPE bigint(2) default 0 AFTER IS_FILTER;

ALTER TABLE farmer_dynamic_data 
ADD COLUMN TOTAL_SCORE double(4,2) NULL DEFAULT NULL AFTER UPDATED_USER;

ALTER TABLE farmer_dynamic_field_value 
ADD COLUMN SCORE int(3) NULL DEFAULT NULL AFTER IS_PROFILE_UPDATE;

ALTER TABLE dynamic_field_score_map 
ADD COLUMN PERCENTAGE double(45, 2) NULL AFTER SCORE;

ALTER TABLE farmer_dynamic_field_value 
ADD COLUMN PERCENTAGE double(45, 2) NULL AFTER SCORE;

alter table dynamic_menu_config add column is_score bigint(2) default 0;
alter table farmer_dynamic_data add column is_score bigint(2) default 0;


-- -21-11-2018
ALTER TABLE heap_data 
ADD COLUMN FARMER text CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL AFTER BRANCH;

ALTER TABLE heap_data_detail 
ADD COLUMN ICS text CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL AFTER DESCRIPTION,
ADD COLUMN FARMER text CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL AFTER ICS;

-- -04-012-2018
ALTER TABLE ledger_data 
ADD COLUMN FARMER varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER LEDGER_TYPE;

ALTER TABLE ginning_process 
ADD COLUMN FARMER varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER TOT_SCRUP;

ALTER TABLE spinning_transfer 
ADD COLUMN FARMER text CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL AFTER BRANCH;

ALTER TABLE farm
ADD COLUMN PLOTTING_STATUS  bigint(1) NULL DEFAULT 0 AFTER ACTIVE_COORDINATES;

-- - 20 DEC 2018
-- - Griffith Sowing Location Menun Script

INSERT INTO ese_menu VALUES (null, 'sowing.location', 'Sowing Location', 'sowingLocation_list.action', 1, 1, 0, 0, 0, 0, NULL);
INSERT INTO ese_ent VALUES (null, 'sowing.location.list');
INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='sowing.location'), '1');
INSERT INTO ese_role_ent (ROLE_ID, ENT_ID) VALUES ('1', (SELECT id FROM ese_ent WHERE NAME='sowing.location.list'));
INSERT INTO ese_role_menu VALUES (null,(SELECT id FROM ese_menu WHERE NAME='sowing.location'), '1');


ALTER TABLE cold_storage_entry 
ADD COLUMN FARMER_ID bigint(15) NULL AFTER ID;

ALTER TABLE cold_storage_entry 
ADD COLUMN ROUND_OF_HARVESTING varchar(45) NULL AFTER REVISION_NO;

-- Dynamic Question Configuration menu script
INSERT INTO ese_menu VALUES (0, 'admin.dynamicUI', 'Dynamic Question Configuration', 'dynamicUI_list.action', 13, 5, 0, 0, 0, 0, NULL);

INSERT INTO ese_ent() VALUES (0, 'admin.dynamicUI.list');
INSERT INTO ese_ent() VALUES (0, 'admin.dynamicUI.update');

INSERT INTO ese_role_menu VALUES (null,(SELECT id FROM ese_menu WHERE NAME='admin.dynamicUI'), '1');

INSERT INTO ese_role_ent VALUES ('1',(SELECT id FROM ese_ent WHERE NAME='admin.dynamicUI.list'));
INSERT INTO ese_role_ent VALUES ('1',(SELECT id FROM ese_ent WHERE NAME='admin.dynamicUI.update'));


INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='admin.dynamicUI'), '1');
INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='admin.dynamicUI'), '2');
INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='admin.dynamicUI'), '3');

-- -28-12-2018
INSERT INTO ese_menu VALUES (null, 'service.ginningProcess', 'Ginning Process', 'ginningProcess_create.action', 13, 3, '0', '0','0','0',NULL);
INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='service.ginningProcess'), '2');
INSERT INTO ese_ent VALUES (null, 'service.ginningProcess.create');
INSERT INTO ese_role_ent VALUES ('1', (SELECT id FROM ese_ent WHERE NAME='service.ginningProcess.create'));
INSERT INTO ese_role_menu VALUES (NULL, (SELECT MAX(id) FROM ese_menu), '1')


-- -04 JAN 2019

-- -Griffith
ALTER TABLE cold_storage_stock_transfer_detail 
ADD COLUMN FARMER_ID bigint(15) NULL AFTER ID;


-- 04-01-2019
ALTER TABLE heap_data 
ADD COLUMN SEASON varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER FARMER;

ALTER TABLE bale_generation 
ADD COLUMN SEASON varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER BRANCH;

ALTER TABLE ginning_process 
ADD COLUMN SEASON varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER FARMER;


ALTER TABLE spinning_transfer 
ADD COLUMN SEASON varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER FARMER;

ALTER TABLE ledger_data 
ADD COLUMN SEASON varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER FARMER;

ALTER TABLE procurement_traceability_stock 
ADD COLUMN SEASON varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '' AFTER RECEIPT_NO;

ALTER TABLE pmt_farmer_detail 
ADD COLUMN SEASON varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER ICS;

ALTER TABLE offline_ginning_process 
ADD COLUMN SEASON varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER GINNING_ID;

ALTER TABLE offline_bale_generation 
ADD COLUMN SEASON varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER BALE_WEIGHT;

ALTER TABLE offline_spinning_transfer 
ADD COLUMN SEASON varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER TYPE;

ALTER TABLE procurement_traceability_stock 
ADD COLUMN BRANCH_ID varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER SEASON;

ALTER TABLE ginning_process 
ADD COLUMN ICS text CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL AFTER SEASON;


-- 10-01-2019
ALTER TABLE offline_bale_generation 
ADD COLUMN MSG_NO varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER SEASON;

ALTER TABLE offline_mtnt 
ADD COLUMN MSG_NO varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER TRANSPORTER;

ALTER TABLE offline_pmtnr 
ADD COLUMN MSG_NO varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER STATUS_MSG;

ALTER TABLE offline_ginning_process 
ADD COLUMN MSG_NO varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER SEASON;

ALTER TABLE offline_procurement_traceability 
ADD COLUMN MSG_NO varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER PAYMENT_AMOUNT;

ALTER TABLE offline_spinning_transfer 
ADD COLUMN MSG_NO varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER SEASON;


-- -18-01-2019
--  Dynami Module Export Optimization


-- -26-09-2018
CREATE DEFINER=root@localhost FUNCTION getAnswerDynamic(accessType BIGINT(45), fieldName varchar(45),cataType LONGTEXT, isMulti BIGINT(45)) RETURNS text CHARSET utf8 COLLATE utf8_unicode_ci
BEGIN
declare resultVal varchar(100);
set resultVal = (
case 
when  ( accessType='2') and isMulti ='0' then 
	
		 (
			SELECT
				GROUP_CONCAT(IFNULL(catalogue_value.NAME ,''))
			FROM
				catalogue_value 
			WHERE
				(  catalogue_value.CODE = fieldName)  AND find_in_set( catalogue_value.CODE, cataType )
			) 
		when  ( accessType='2') and isMulti ='1' then 
			 	(
			SELECT
				group_concat( catalogue_value.NAME SEPARATOR ',' ) 
			FROM
				catalogue_value 
			WHERE
				( find_in_set( catalogue_value.CODE, fieldName ) AND find_in_set( catalogue_value.CODE, cataType ) ) 
			) 
when  (accessType='1' ) and isMulti ='0' then 
	
		 (
			SELECT
				catalogue_value.NAME 
			FROM
				catalogue_value 
			WHERE
				( ( catalogue_value.CODE = fieldName) AND ( catalogue_value.TYPEZ =cataType) ) 
			) 
		when  (accessType='1' ) and isMulti ='1' then 
			 	(
			SELECT
				group_concat( catalogue_value.NAME SEPARATOR ',' ) 
			FROM
				catalogue_value 
			WHERE
				( find_in_set( catalogue_value.CODE, fieldName ) AND ( catalogue_value.TYPEZ =cataType ) ) 
			) 
			when  accessType='3' and isMulti ='0' then 
			
			 (case WHEN  cataType = '9'  THEN
			(SELECT procurement_product.NAME FROM procurement_product WHERE  procurement_product.CODE = fieldName )
			WHEN  cataType = '10'  THEN
			( SELECT procurement_variety.NAME FROM procurement_variety WHEREprocurement_variety.CODE = fieldName)
			WHEN  cataType = '13' THEN
		( SELECT procurement_grade.NAME FROM procurement_grade WHERE  procurement_grade.CODE =fieldName)
		 END )
		 when  accessType='3' and isMulti ='1' then 
			
			 (case WHEN  cataType = '9'  THEN
			(SELECT GROUP_concat(procurement_product.NAME) FROM procurement_product WHERE  find_in_set(procurement_product.CODE , fieldName))
			WHEN  cataType = '10'  THEN
			( SELECT GROUP_concat(procurement_variety.NAME) FROM procurement_variety WHERE find_in_set(procurement_variety.CODE, fieldName))
			WHEN  cataType = '13' THEN
		( SELECT GROUP_concat(procurement_grade.NAME) FROM procurement_grade WHERE  find_in_set(procurement_grade.CODE, fieldName))
		 END)
		 
		 ELSE  ''
	 END);
	 
	 return resultVal;

END;

CREATE DEFINER=root@localhost PROCEDURE export_fn(p1 LONGTEXT)
BEGIN
  SELECT
	fd.FARMER_DYNAMIC_DATA_ID AS FARMER_DYNAMIC_DATA,
	dref.COMPONENT_CODE AS FIELD_NAME,
	group_concat(
	fd.TYPEZ,
	'~',
	fd.FIELD_NAME,
	'#',
	(
CASE
	
	WHEN ( df.COMPONENT_TYPE IN ( 2, 4, 6 ) ) THEN
	getAnswerDynamic ( df.ACCESS_TYPE, fd.FIELD_VALUE, df.CATALOGUE_TYPE, 0 ) 
	WHEN ( df.COMPONENT_TYPE = '9' ) THEN
	getAnswerDynamic ( df.ACCESS_TYPE, fd.FIELD_VALUE, df.CATALOGUE_TYPE, 1 ) 
	WHEN ( df.COMPONENT_TYPE = '12' ) THEN
	( CONVERT ( dt.ID USING utf8 ) COLLATE utf8_general_ci ) 
	WHEN ( df.COMPONENT_TYPE = '14' ) THEN
	concat(
		'Temperature :',
		substring_index( fd.FIELD_VALUE, '|', 1 ),
		' , Rain :',
		substring_index( substring_index( fd.FIELD_VALUE, '|', 2 ), '|',- ( 1 ) ),
		' , Humidity :',
		substring_index( substring_index( fd.FIELD_VALUE, '|', 3 ), '|',- ( 1 ) ),
		' , Wind :',
		substring_index( substring_index( fd.FIELD_VALUE, '|', 4 ), '|',- ( 1 ) ) 
	) ELSE fd.FIELD_VALUE 
END 
	) SEPARATOR '|' 
	) AS FIELD_VALUE 
FROM
	(
		(
			(
				(
					( dynamic_menu_config m JOIN dynamic_menu_field_map dmap ON ( ( dmap.MENU_ID = m.ID ) ) )
					JOIN dynamic_fields_config df ON ( ( df.ID = dmap.FIELD_ID ) ) 
				)
				JOIN farmer_dynamic_field_value fd ON ( ( fd.FIELD_NAME = df.COMPONENT_CODE ) )  AND find_in_set( fd.FARMER_DYNAMIC_DATA_ID,p1 )
			)
			JOIN dynamic_fields_config dref ON ( ( dref.ID = df.REFERENCE_ID ) ) 
		)
		LEFT JOIN dynamic_image_data dt ON ( ( cast( fd.ID AS CHAR charset BINARY ) = cast( dt.FARMER_DYNMAIC_FIELD_ID AS CHAR charset BINARY ) ) ) 
	) 
WHERE
	( df.REFERENCE_ID IS NOT NULL ) 
GROUP BY
	df.REFERENCE_ID,
	fd.FARMER_DYNAMIC_DATA_ID UNION
SELECT
	fd.FARMER_DYNAMIC_DATA_ID AS FARMER_DYNAMIC_DATA,
	df.COMPONENT_CODE AS FIELD_NAME,
	(
	CASE
			
			WHEN ( df.COMPONENT_TYPE IN ( 2, 4, 6 ) ) THEN
			getAnswerDynamic ( df.ACCESS_TYPE, fd.FIELD_VALUE, df.CATALOGUE_TYPE, 0 ) 
			WHEN ( df.COMPONENT_TYPE = '9' ) THEN
			getAnswerDynamic ( df.ACCESS_TYPE, fd.FIELD_VALUE, df.CATALOGUE_TYPE, 1 ) 
			WHEN ( df.COMPONENT_TYPE = '12' ) THEN
			( CONVERT ( dt.ID USING utf8 ) COLLATE utf8_general_ci ) 
			WHEN ( df.COMPONENT_TYPE = '14' ) THEN
			concat(
				'Temperature :',
				substring_index( fd.FIELD_VALUE, '|', 1 ),
				' , Rain :',
				substring_index( substring_index( fd.FIELD_VALUE, '|', 2 ), '|',- ( 1 ) ),
				' , Humidity :',
				substring_index( substring_index( fd.FIELD_VALUE, '|', 3 ), '|',- ( 1 ) ),
				' , Wind :',
				substring_index( substring_index( fd.FIELD_VALUE, '|', 4 ), '|',- ( 1 ) ) 
			) ELSE fd.FIELD_VALUE 
		END 
		) AS FIELD_VALUE 
	FROM
		(
			(
				(
					( dynamic_menu_config m JOIN dynamic_menu_field_map dmap ON ( ( dmap.MENU_ID = m.ID ) ) )
					JOIN dynamic_fields_config df ON ( ( df.ID = dmap.FIELD_ID ) ) 
				)
				JOIN farmer_dynamic_field_value fd ON ( ( fd.FIELD_NAME = df.COMPONENT_CODE ) )  AND find_in_set( fd.FARMER_DYNAMIC_DATA_ID,p1 )
			)
			LEFT JOIN dynamic_image_data dt ON ( ( cast( fd.ID AS CHAR charset BINARY ) = cast( dt.FARMER_DYNMAIC_FIELD_ID AS CHAR charset BINARY ) ) ) 
		) 
	WHERE
		isnull( df.REFERENCE_ID ) 
	GROUP BY
	df.COMPONENT_CODE,
fd.FARMER_DYNAMIC_DATA_ID;
END;

--24--01--2019
ALTER TABLE eses_olivado.farmer_land_details 
ADD COLUMN YEAR varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER FARM_ID,
ADD COLUMN CROPS varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER YEAR,
ADD COLUMN SEEDLINGS varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER CROPS,
ADD COLUMN ESTIMATED_ACREAGE double(50, 0) NULL DEFAULT NULL AFTER SEEDLINGS,
ADD COLUMN NO_TREES varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER ESTIMATED_ACREAGE,
ADD COLUMN PEST_DISEASES varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER NO_TREES,
ADD COLUMN PEST_DISEASE_CTRL varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER PEST_DISEASES,
ADD COLUMN FERTI_METHOD varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER PEST_DISEASE_CTRL,
ADD COLUMN INPUTS varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER FERTI_METHOD,
ADD COLUMN WITH_BUFFER varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER INPUTS,
ADD COLUMN WITHOUT_BUFFER varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER WITH_BUFFER,
ADD COLUMN DATE date NULL DEFAULT NULL AFTER WITHOUT_BUFFER;
ADD COLUMN BRANCH_ID varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER DATE;

INSERT INTO locale_property(id, code, lang_code, lang_value) VALUES (NULL, 'info.farmHistory', 'en', 'Farm History');
INSERT INTO locale_property(id, code, lang_code, lang_value) VALUES (NULL, 'farmerLandDetails.year', 'en', 'Year');
INSERT INTO locale_property(id, code, lang_code, lang_value) VALUES (NULL, 'farmerLandDetails.crops', 'en', 'Crops');
INSERT INTO locale_property(id, code, lang_code, lang_value) VALUES (NULL, 'farmerLandDetails.seedlings', 'en', 'Are seeds/seedlings treated');
INSERT INTO locale_property(id, code, lang_code, lang_value) VALUES (NULL, 'farmerLandDetails.estimatedAcreage', 'en', 'Estimated Acreage');
INSERT INTO locale_property(id, code, lang_code, lang_value) VALUES (NULL, 'farmerLandDetails.noOfTrees', 'en', 'No. of trees/bushes');
INSERT INTO locale_property(id, code, lang_code, lang_value) VALUES (NULL, 'farmerLandDetails.pestdiseases', 'en', 'Pest and diseases');
INSERT INTO locale_property(id, code, lang_code, lang_value) VALUES (NULL, 'farmerLandDetails.pestdiseasesControl', 'en', 'Pest and diseases control');
INSERT INTO locale_property(id, code, lang_code, lang_value) VALUES (NULL, 'farmerLandDetails.fertilizationMethod', 'en', 'Fertilization Method');
INSERT INTO locale_property(id, code, lang_code, lang_value) VALUES (NULL, 'farmerLandDetails.inputs', 'en', 'Is 2m distance maintained when un-allowed inputs are used');
INSERT INTO locale_property(id, code, lang_code, lang_value) VALUES (NULL, 'farmerLandDetails.withBuffer', 'en', 'Is 10m distance maintained when un-allowed sprays are used with buffer zone');
INSERT INTO locale_property(id, code, lang_code, lang_value) VALUES (NULL, 'farmerLandDetails.withoutBuffer', 'en', 'Is 30m distance maintained when un-allowed sprays are used without buffer zone');
INSERT INTO locale_property(id, code, lang_code, lang_value) VALUES (NULL, 'farmerLandDetails.date', 'en', 'Date of last application of the un-allowed inputs/sprays');
INSERT INTO locale_property(id, code, lang_code, lang_value) VALUES (NULL, 'info.farmHistoryDetails', 'en', 'Farm History Details');
INSERT INTO locale_property(id, code, lang_code, lang_value) VALUES (NULL, 'crops', 'en', 'Crops');

--31--01--2019
ALTER TABLE offline_procurement 
MODIFY COLUMN WAREHOUSE_ID varchar(10) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER LONGITUDE;


DROP view if EXISTS vw_farmer_traceability;

CREATE VIEW vw_farmer_traceability AS
(

SELECT
	CONCAT(f.id,bg.LOT_NO) AS ID,
	f.id AS FARMER_ID,
	f.FRAMER_CODE_BY_TRCAENET AS TRCAENET_CODE,
	CONCAT(f.FIRST_NAME,' ',f.LAST_NAME) AS NAME,
	f.VILLAGE_ID AS VILLAGE,
	f.ICS_NAME AS ICS,
	f.SAMITHI_ID AS SHG,
	bg.LOT_NO AS LOT_NO,
  GROUP_CONCAT(distinct bg.PR_NO) AS PR_NO,
	pt.WAREHOUSE_ID as PROCUREMENT_CENTER,	
	gp.GINNING_ID as GINNING,
	st.SPINNING as SPINNING,
	bg.SEASON AS SEASON,
	f.BRANCH_ID AS BRANCH_ID 
FROM
bale_generation bg inner join ginning_process gp on gp.id=bg.GINNING_PROCESS_ID
INNER JOIN farmer f ON FIND_IN_SET(f.ID,gp.FARMER)
inner join procurement_traceability pt on FIND_IN_SET(pt.FARMER_ID,gp.FARMER)
left join spinning_transfer st on st.ID=bg.SPINNING_TRANSFER_ID
GROUP BY f.id,bg.LOT_NO
order by f.id
) 

--1--02--2019
DROP FUNCTION IF EXISTS getAnswerDynamic;

CREATE DEFINER=root@localhost FUNCTION getAnswerDynamic(accessType BIGINT(45), fieldName varchar(45),cataType BIGINT(45), isMulti BIGINT(45)) RETURNS text CHARSET utf8 COLLATE utf8_unicode_ci
BEGIN
declare resultVal text;
set resultVal = (
case when  accessType='1' and isMulti ='0' then 
	
		 (
			SELECT
				catalogue_value.NAME 
			FROM
				catalogue_value 
			WHERE
				( ( catalogue_value.CODE = fieldName) AND ( catalogue_value.TYPEZ =cataType) ) 
			) 
		when  accessType='1' and isMulti ='1' then 
			 	(
			SELECT
				group_concat( catalogue_value.NAME SEPARATOR ',' ) 
			FROM
				catalogue_value 
			WHERE
				( find_in_set( catalogue_value.CODE, fieldName ) AND ( catalogue_value.TYPEZ =cataType ) ) 
			) 
			when  accessType='3' and isMulti ='0' then 
			
			 (case WHEN  cataType = '9'  THEN
			(SELECT procurement_product.NAME FROM procurement_product WHERE  procurement_product.CODE = fieldName )
			WHEN  cataType = '10'  THEN
			( SELECT procurement_variety.NAME FROM procurement_variety WHEREprocurement_variety.CODE = fieldName)
			WHEN  cataType = '13' THEN
		( SELECT procurement_grade.NAME FROM procurement_grade WHERE  procurement_grade.CODE =fieldName)
		 END )
		 when  accessType='3' and isMulti ='1' then 
			
			 (case WHEN  cataType = '9'  THEN
			(SELECT GROUP_concat(procurement_product.NAME) FROM procurement_product WHERE  find_in_set(procurement_product.CODE , fieldName))
			WHEN  cataType = '10'  THEN
			( SELECT GROUP_concat(procurement_variety.NAME) FROM procurement_variety WHERE find_in_set(procurement_variety.CODE, fieldName))
			WHEN  cataType = '13' THEN
		( SELECT GROUP_concat(procurement_grade.NAME) FROM procurement_grade WHERE  find_in_set(procurement_grade.CODE, fieldName))
		 END)
		 
		 ELSE  ''
	 END);
	 
	 return resultVal;

END;

-- -06-02-2019
ALTER TABLE cultivation 
ADD COLUMN LAND_LABOUR_COST varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER AGENT_ID,
ADD COLUMN SOWING_LABOUR_COST varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER LAND_LABOUR_COST,
ADD COLUMN GAP_LABOUR_COST varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER SOWING_LABOUR_COST,
ADD COLUMN WEED_LABOUR_COST varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER GAP_LABOUR_COST,
ADD COLUMN CULTURE_LABOUR_COST varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER WEED_LABOUR_COST,
ADD COLUMN IRRI_LABOUR_COST varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER CULTURE_LABOUR_COST,
ADD COLUMN FERTI_LABOUR_COST varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER IRRI_LABOUR_COST,
ADD COLUMN PEST_LABOUR_COST varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER FERTI_LABOUR_COST,
ADD COLUMN HARVEST_LABOUR_COST varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER PEST_LABOUR_COST,
ADD COLUMN MANURE_LABOUR_COST varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER HARVEST_LABOUR_COST,
ADD COLUMN TRANSPORST_COST varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER MANURE_LABOUR_COST,
ADD COLUMN FUEL_COST varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER TRANSPORST_COST,
ADD COLUMN OTHER_COST varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER FUEL_COST;

-- -18-02-2019

ALTER TABLE dynamic_report_config_detail 
ADD COLUMN IS_GROUP_HEADER varchar(255) NULL DEFAULT NULL AFTER IS_FOOTER_SUM;

-- -21-02-2019

alter table farmer_crop_prod_answers add index(FARMER_ID);
alter table procurement add INDEX(farmer_id);
alter table distribution add INDEX(farmer_id);
alter table trxn_agro add INDEX(farmer_id);
alter table farmer_dynamic_data add index(REFERENCE_ID);
alter table farmer_dynamic_field_value add index(REFERENCE_ID);
alter table dynamic_menu_field_map add index(menu_id);
alter table dynamic_menu_section_map add index(menu_id);
alter table dynamic_image_data add index(FARMER_DYNMAIC_FIELD_ID);


-- -04-03-2019

ALTER TABLE crop_yield ADD COLUMN MOLECULE_NAME longtext CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER CROP_YIELD_DATE;
ALTER TABLE crop_yield ADD COLUMN IMG longblob NULL AFTER MOLECULE_NAME;
ALTER TABLE crop_yield ADD COLUMN STATUS int(3) NULL DEFAULT NULL AFTER IMG;
ALTER TABLE crop_yield_detail ADD COLUMN STATUS int(3) NULL DEFAULT NULL AFTER CROP_YIELD_ID;
ALTER TABLE crop_yield_detail ADD COLUMN TYPE int(3) NULL DEFAULT NULL AFTER STATUS;
ALTER TABLE crop_yield_detail MODIFY COLUMN YIELD longtext CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER PROCUREMENT_PRODDUCT_ID;


-- -06-03-2019
alter table farmer_dynamic_field_value add column ACTION_PLAN bigint(45),
add column deadline bigint(45),
add column RECOM varchar(250),
add column ACT_STATUS BIGINT(1) default 0;

alter table farmer_dynamic_data add column ACT_STATUS BIGINT(1) default 0;


CREATE TABLE farmer_action_plan (
  ID bigint(45)  NOT NULL AUTO_INCREMENT ,
  FARMER_DYNAMIC_FIELD_ID bigint(45) DEFAULT NULL,
  RECOMMENDATION text,
  ACTION_PLAN_STATUS bigint(10) DEFAULT NULL,
  FOLLOW_UP_DATE datetime DEFAULT NULL,
  CREATED_DATE datetime DEFAULT NULL,
  CREATED_USER varchar(255) DEFAULT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


alter table dynamic_fields_config add column FOLLOW_UP BIGINT(1) default 0,
add column PARENT_ACTION_FIELD varchar(50),
add column PARENT_ACTION_KEY varchar(50);

alter table farmer_dynamic_field_value add column FOLLOW_UP BIGINT(1) default 0,
add column PARENT_ACTION_FIELD varchar(50),
add column PARENT_ACTION_KEY varchar(50);

alter table farmer_dynamic_field_value add column GRADE varchar(10);


alter table dynamic_fields_config add column GRADE varchar(10);
alter table certificate_standard add column criteria text;
alter table dynamic_fields_config add column GRADE varchar(10);

-----------------11-03-2019-- Sticky------------------------------------
ALTER TABLE farmer 
ADD COLUMN Digital_Signature longblob NULL AFTER existingFarmer_Flag;

--------------------------------------------------------------------------------


-- -12 March 2019
-- -Agro 
INSERT INTO pref(ID, NAME, VAL, ESE_ID, DESCRIPTION) VALUES (null, 'WEB_URL', 'http://localhost:8085/agroweb/login_execute_agro', 1, NULL);
INSERT INTO pref(ID, NAME, VAL, ESE_ID, DESCRIPTION) VALUES (null, 'APK_URL', 'http://pro4.sourcetrace.com:10088/awipp/awi.apk', 1, NULL);
INSERT INTO pref(ID, NAME, VAL, ESE_ID, DESCRIPTION) VALUES (null, 'CC_EMAIL', 'harii@sourcetrace.com ', 1, NULL);
INSERT INTO assets(id, code, description, content, file) VALUES (null, 'qr_logo', 'QR Logo', NULL, '');

ALTER TABLE certificate_standard 
ADD COLUMN criteria text NULL AFTER CERTIFICATE_CATEGORY_ID;

ALTER TABLE locale_property 
MODIFY COLUMN lang_value text CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL AFTER lang_code;


alter table farmer modify column CERTIFICATE_STANDARD_LEVEL bigint(2) default -1;
alter table farmer_dynamic_field_value modify column FOLLOW_UPDATE datetime default null;

-- 18-03-2019
ALTER TABLE farmer_dynamic_field_value 
ADD COLUMN FDV_ID bigint(20) NULL AFTER PERCENTAGE,
drop column RECOM;

-- -18-03-2019
alter table farmer_dynamic_data add column REVISION_NO BIGINT(45) default 0;

alter table farmer_dynamic_field_value add column orderz BIGINT(45) default 0;
-- -20-03-2019
alter table farmer_dynamic_field_value drop column FOLLOW_AGENT,drop column FOLLOW_UPDATE
alter table farmer_dynamic_data add column FOLLOW_UPDATE datetime default null , add column FOLLOW_AGENT varchar(45);

-- -25-03-2019
ALTER TABLE dynamic_image_data 
ADD COLUMN typez int(3) NOT NULL DEFAULT -1 AFTER FARMER_DYNMAIC_DATA_ID;

-- - 25Mar2019

INSERT INTO pref(ID, NAME, VAL, ESE_ID, DESCRIPTION) VALUES (null, 'TXN_TYPES_MOBILE_ACTIVITY', '308, 314,363,364,360', 1, NULL);

-- - 26Mar2019
INSERT INTO  locale_property  VALUES (NULL, 'groupMasterTypeList', 'en ', '1~MANDI TRADER,2~MANDI AGGREGATOR,3~FARM AGGREGATOR,4~FPO,7~PRODUCE IMPORTER,8~FPC,10~Agriculture Company,11~FG,12~Customer,13~Cash Purchase');

INSERT INTO farm_catalogue(ID, NAME, CATALOGUE_TYPEZ, BRANCH_ID, REVIOSION_NO, STATUS) VALUES (140, 'Land Gradient', 140, NULL, 1, 1);
INSERT INTO farm_catalogue(ID, NAME, CATALOGUE_TYPEZ, BRANCH_ID, REVIOSION_NO, STATUS) VALUES (144, 'Approach Road', 144, NULL, 1, 1);

-- - 28-03-2019
ALTER TABLE offline_procurement_detail 
ADD COLUMN NUMBER_OF_FRUIT_BAGS varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER UNIT;

ALTER TABLE procurement_detail 
ADD COLUMN NUMBER_OF_FRUIT_BAGS bigint(20) NULL DEFAULT NULL AFTER UNIT;

-- - 02-04-2019
ALTER TABLE farm_crops_coordinates 
ADD COLUMN TYPE int(5) NOT NULL DEFAULT -1 AFTER COORDINATES_MAP_ID,
ADD COLUMN TITLE varchar(255) NULL AFTER TYPE,
ADD COLUMN DESCRIPTION text NULL AFTER TITLE;

UPDATE farm_crops_coordinates SET TYPE = 0 WHERE TYPE = -1;

-- - 04-04-2019
ALTER TABLE coordinates 
ADD COLUMN TYPE int(5) NOT NULL DEFAULT -1 AFTER COORDINATES_MAP_ID,
ADD COLUMN TITLE varchar(255) NULL AFTER TYPE,
ADD COLUMN DESCRIPTION text NULL AFTER TITLE;

UPDATE coordinates SET TYPE = 0 WHERE TYPE = -1;
-- - 23-04-2019
ALTER TABLE dynamic_menu_field_map
ADD COLUMN BRANCH  varchar(255) NULL AFTER SCORE_TYPE;


ALTER TABLE dynamic_menu_config
ADD COLUMN BRANCH  varchar(255) NULL ;

ALTER TABLE DYNAMIC_MENU_SECTION_MAP
ADD COLUMN BRANCH  varchar(255) NULL ;

-- - 15-05-2019

UPDATE locale_property SET code = 'farmingTotalLand', lang_code = 'en', lang_value = 'Total Area(Ha)' WHERE code = 'farmingTotalLand';
INSERT INTO locale_property VALUES (NULL, 'subOrganisationFilter', 'en', 'Sub Organisation');
INSERT INTO locale_property VALUES (NULL, 'crops', 'en', 'Crops');
INSERT INTO locale_property VALUES (NULL, 'cropyield', 'en', 'Yield');

-- - 23-05-2019
ALTER TABLE cultivation 
ADD COLUMN SAMITHI_CODE varchar(15) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER OTHER_COST;

ALTER TABLE distribution 
ADD COLUMN SAMITHI_ID varchar(15) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER WAREHOUSE_NAME;

-- - 31-05-2019
INSERT INTO ese_seq(ID, SEQ_KEY, SEQ_VAL) VALUES (0, 'DYNAMIC_SECTION_CODE_SEQ', 0);
INSERT INTO ese_seq(ID, SEQ_KEY, SEQ_VAL) VALUES (0, 'DYNAMIC_MENU_CODE_SEQ', 0);
INSERT INTO ese_seq(ID, SEQ_KEY, SEQ_VAL) VALUES (0, 'DYNAMIC_FIELD_CODE_SEQ', 0);

-- - 07-06-2019

INSERT INTO pref VALUES (NULL, 'US_SMS_URL', 'https://www.thetexting.com/rest/sms/json', 1, NULL);
INSERT INTO pref VALUES (NULL, 'US_SMS_API_KEY', 'wm6jxtv5bnpck0i', 1, NULL);
INSERT INTO pref VALUES (NULL, 'US_SMS_API_SECRET', 'yhjfc65ofq3p6dw', 1, NULL);
INSERT INTO pref VALUES (NULL, 'US_SMS_FROM','test',1,NULL);

-- - 22-06-2019
ALTER TABLE farm 
ADD COLUMN CERTIFICATE_STANDARD_LEVEL int(3) NULL DEFAULT -1 AFTER PLOTTING_STATUS;

-- -04-07-2019
ALTER TABLE farm 
ADD COLUMN ACT_STATUS int(3) NULL DEFAULT 0 AFTER CERTIFICATE_STANDARD_LEVEL;

-- -15-07-2019
ALTER TABLE village 
ADD COLUMN SEQ varchar(3) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER BRANCH_ID;

-- -19 AUG 2019

-- - Olivado
ALTER TABLE farmer 
MODIFY COLUMN GENDER varchar(15) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER SUR_NAME;

----agro
UPDATE eses_agro.locale_property SET lang_value =  'S.no,Date,Warehouse,Mobile User,Category,Product,Unit,Distribution Quantity,season '  WHERE code = 'exportColumnHeaderDistributionBranchagent';
UPDATE eses_agro.locale_property SET lang_value = 'Organization,Farmer Code,Farmer Name,Father Name,Village,Group,Is Certified,FPO/FG Group,Total Land Holding,Status' WHERE code = 'exportFarmerHeadingBranch';
UPDATE eses_agro.locale_property SET lang_value = 'Farm ,Crop Type ,Crop,Variety,Estimated yield(Kg),Sowing Date,Cultivation Area' WHERE code = 'sowingSubgrid';
UPDATE eses_agro.locale_property SET lang_value = 'Farmer Summary' WHERE code = 'title.transactions';
UPDATE eses_agro.locale_property SET lang_value = 'Procurement ' WHERE code = 'pieChart.procurementCharts';

-- - 10-09-2019
ALTER TABLE farm 
ADD COLUMN CREATED_USERNAME varchar(50) NULL AFTER ACT_STATUS,
ADD COLUMN CREATED_DATE timestamp NULL AFTER CREATED_USERNAME,
ADD COLUMN UPDATED_USERNAME varchar(50) NULL AFTER CREATED_DATE,
ADD COLUMN UPDATED_DATE timestamp NULL AFTER UPDATED_USERNAME;

UPDATE eses_agro.locale_property SET lang_value = 'Farmer,Group,Village,Total Land Holding (%#),Estimated yield (Kg),Season' WHERE code = 'sowingExportWithCode';

UPDATE eses_agro.locale_property SET lang_value = 'Farmer,Group,Village,Total Land Holding (%#),Estimated yield (Kg),Season' WHERE code = 'exportFarmerListColumnHeader';
UPDATE eses_agro.pref SET VAL = '1-Ha' WHERE NAME = 'AREA_TYPE';
UPDATE eses_agro.locale_property SET lang_value = 'Farmer Code,Farmer Name,Father Name,Village,Group,Is Certified,FPO/FG,Total Land Holding,Status' WHERE code = 'exportFarmerHeading';

-- -17-10-2019
UPDATE ese_menu SET ORD = 8 WHERE NAME = 'profile.cooperative';
UPDATE ese_menu SET ORD = 11 WHERE NAME = 'location';
UPDATE ese_menu SET ORD = 6 WHERE NAME = 'profile.location.village';
UPDATE ese_menu SET ORD = 3 WHERE NAME = 'profile.farmer';
UPDATE ese_menu SET ORD = 4 WHERE NAME = 'profile.products';
UPDATE ese_menu SET ORD = 2 WHERE NAME = 'profile.procurementProduct';
UPDATE ese_menu SET ORD = 7 WHERE NAME = 'profile.customer';
UPDATE ese_menu SET ORD = 6 WHERE NAME = 'profile.vendor';
UPDATE ese_menu SET ORD = 1 WHERE NAME = 'profile.season';
UPDATE ese_menu SET ORD = 9 WHERE NAME = 'profile.catalogue';
UPDATE ese_menu SET ORD = 10 WHERE NAME = 'profile.trainingMaster.trainingTopic';


UPDATE ese_menu SET ORD = 12 WHERE NAME = 'Training';
UPDATE ese_menu SET ORD = 1 WHERE NAME = 'service.farmerTrainingSelection';
UPDATE ese_menu SET ORD = 1 WHERE NAME = 'service.trainingPlanner';
UPDATE ese_menu SET ORD = 1 WHERE NAME = 'service.warehouseProduct';
UPDATE ese_menu SET ORD = 2 WHERE NAME = 'service.distribution';
UPDATE ese_menu SET ORD = 1 WHERE NAME = 'service.distribution.fieldStaff';
UPDATE ese_menu SET ORD = 2 WHERE NAME = 'service.distribution.farmer';
UPDATE ese_menu SET ORD = 5 WHERE NAME = 'service.procurement';
UPDATE ese_menu SET ORD = 2 WHERE NAME = 'service.report.cropSaleEntryReport';
UPDATE ese_menu SET ORD = 1 WHERE NAME = 'service.report.cropHarvestReport';
UPDATE ese_menu SET ORD = 4 WHERE NAME = 'service.farmInspection';
UPDATE ese_menu SET ORD = 7 WHERE NAME = 'service.stockTransfer';
UPDATE ese_menu SET ORD = 8 WHERE NAME = 'service.mtnr.procurement';
UPDATE ese_menu SET ORD = 5 WHERE NAME = 'service.procurementService';
UPDATE ese_menu SET ORD = 3 WHERE NAME = 'service.agentBalance';
UPDATE ese_menu SET ORD = 15 WHERE NAME = 'service.smsAlert';
UPDATE ese_menu SET ORD = 16 WHERE NAME = 'service.reportMailConfig';
UPDATE ese_menu SET ORD = 9 WHERE NAME = 'service.productReturn';
UPDATE ese_menu SET ORD = 2 WHERE NAME = 'service.productReturn.farmer';
UPDATE ese_menu SET ORD = 1 WHERE NAME = 'service.productReturn.fieldStaff';
UPDATE ese_menu SET ORD = 10 WHERE NAME = 'service.coc';
UPDATE ese_menu SET ORD = 17 WHERE NAME = 'report.qrCodeGen';
UPDATE ese_menu SET ORD = 11 WHERE NAME = 'service.farmerIncome';
UPDATE ese_menu SET ORD = 13 WHERE NAME = 'service.plotting';
UPDATE ese_menu SET ORD = 16 WHERE NAME = 'service.dynamicCertification';
UPDATE ese_menu SET ORD = 14 WHERE NAME = 'service.cropCalendar';
UPDATE ese_menu SET ORD = 13 WHERE NAME = 'service.plotting';


UPDATE ese_menu SET ORD = 10 WHERE NAME = 'report.training';
UPDATE ese_menu SET ORD = 1 WHERE NAME = 'report.training.trainingCompletion';
UPDATE ese_menu SET ORD = 2 WHERE NAME = 'report.training.offlineTrainingCompletion';
UPDATE ese_menu SET ORD = 11 WHERE NAME = 'report.farmerInspectionReport';
UPDATE ese_menu SET ORD = 3 WHERE NAME = 'report.farmerInspectionReport.npop';
UPDATE ese_menu SET ORD = 1 WHERE NAME = 'report.farmerInspectionReport.fairTrade';
UPDATE ese_menu SET ORD = 2 WHERE NAME = 'report.farmerInspectionReport.organic';
UPDATE ese_menu SET ORD = 4 WHERE NAME = 'report.farmerInspectionReport.ics';
UPDATE ese_menu SET ORD = 5 WHERE NAME = 'report.farmerInspectionReport.rspo';
UPDATE ese_menu SET ORD = 3 WHERE NAME = 'report.periodicInspectionReport';
UPDATE ese_menu SET ORD = 4 WHERE NAME = 'report.procurementReport';
UPDATE ese_menu SET ORD = 2 WHERE NAME = 'report.cropHarvestReport';
UPDATE ese_menu SET ORD = 3 WHERE NAME = 'report.cropSaleReport';
UPDATE ese_menu SET ORD = 1 WHERE NAME = 'report.procurement';
UPDATE ese_menu SET ORD = 4 WHERE NAME = 'report.procurementStock';
UPDATE ese_menu SET ORD = 2 WHERE NAME = 'report.cropHarvestReport';
UPDATE ese_menu SET ORD = 8 WHERE NAME = 'report.farmer';
UPDATE ese_menu SET ORD = 1 WHERE NAME = 'report.farmerList';
UPDATE ese_menu SET ORD = 4 WHERE NAME = 'report.cultivationReport';
UPDATE ese_menu SET ORD = 3 WHERE NAME = 'report.farmerCropReport';
UPDATE ese_menu SET ORD = 5 WHERE NAME = 'report.cBRReport';
UPDATE ese_menu SET ORD = 6 WHERE NAME = 'report.farmerIncomeReport';
UPDATE ese_menu SET ORD = 7 WHERE NAME = 'report.farmerBalanceReport';
UPDATE ese_menu SET ORD = 2 WHERE NAME = 'report.sowingReport';
UPDATE ese_menu SET ORD = 8 WHERE NAME = 'report.farmerActivityReport';
UPDATE ese_menu SET ORD = 18 WHERE NAME = 'report.offlineReport';
UPDATE ese_menu SET ORD = 1 WHERE NAME = 'report.offline.farmer';
UPDATE ese_menu SET ORD = 12 WHERE NAME = 'report.failedInspectionReport';
UPDATE ese_menu SET ORD = 1 WHERE NAME = 'report.failedInspectionReport.questions';
UPDATE ese_menu SET ORD = 2 WHERE NAME = 'report.failedInspectionReport.farmers';
UPDATE ese_menu SET ORD = 1 WHERE NAME = 'report.warehouse';
UPDATE ese_menu SET ORD = 2 WHERE NAME = 'report.warehouseStockReport';
UPDATE ese_menu SET ORD = 1 WHERE NAME = 'report.warehouseStockEntryReport';
UPDATE ese_menu SET ORD = 2 WHERE NAME = 'report.distribution';
UPDATE ese_menu SET ORD = 1 WHERE NAME = 'report.distribution.farmer';
UPDATE ese_menu SET ORD = 2 WHERE NAME = 'report.distribution.agent';
UPDATE ese_menu SET ORD = 6 WHERE NAME = 'report.mtnr.procurement';
UPDATE ese_menu SET ORD = 5 WHERE NAME = 'report.mtnt.procurement';
UPDATE ese_menu SET ORD = 19 WHERE NAME = 'report.balanceSheet';
UPDATE ese_menu SET ORD = 15 WHERE NAME = 'report.samithiAccessReport';
UPDATE ese_menu SET ORD = 13 WHERE NAME = 'report.sensitizingReport';
UPDATE ese_menu SET ORD = 22 WHERE NAME = 'report.globalReport';
UPDATE ese_menu SET ORD = 9 WHERE NAME = 'report.fieldStaff';
UPDATE ese_menu SET ORD = 4 WHERE NAME = 'report.fieldStaffManagement';
UPDATE ese_menu SET ORD = 2 WHERE NAME = 'report.fieldStaffBalanceReport';
UPDATE ese_menu SET ORD = 1 WHERE NAME = 'report.fieldStaffStockReport';
UPDATE ese_menu SET ORD = 3 WHERE NAME = 'report.fieldStaffAccessReport';
DELETE from ese_menuWHERE NAME = 'report.fieldStaffManagement' AND id=147;
UPDATE ese_menu SET ORD = 7 WHERE NAME = 'report.productReturn';
UPDATE ese_menu SET ORD = 2 WHERE NAME = 'report.productReturn.farmer';
UPDATE ese_menu SET ORD = 1 WHERE NAME = 'report.productReturn.agent';
UPDATE ese_menu SET ORD = 9 WHERE NAME = 'report.fieldStaff';
UPDATE ese_menu SET ORD = 23 WHERE NAME = 'report.payment';
UPDATE ese_menu SET ORD = 9 WHERE NAME = 'report.fieldStaff';
UPDATE ese_menu SET ORD = 20 WHERE NAME = 'report.dynmaicCertification';
UPDATE ese_menu SET ORD = 1 WHERE NAME = 'Dynamic Report';
UPDATE ese_menu SET ORD = 24 WHERE NAME = 'report.villageCropHarvestReport';


UPDATE ese_menu SET ORD = 3 WHERE NAME = 'profile.device';
UPDATE ese_menu SET ORD = 2 WHERE NAME = 'profile.agent';
UPDATE ese_menu SET ORD = 1 WHERE NAME = 'profile.samithi';
UPDATE ese_menu SET ORD = 4 WHERE NAME = 'profile.user';
UPDATE ese_menu SET ORD = 5 WHERE NAME = 'role';
UPDATE ese_menu SET ORD = 1 WHERE NAME = 'profile.role';
UPDATE ese_menu SET ORD = 2 WHERE NAME = 'profile.role.menu';
UPDATE ese_menu SET ORD = 3 WHERE NAME = 'profile.role.entitlement';
UPDATE ese_menu SET ORD = 4 WHERE NAME = 'system.prefernces';
UPDATE ese_menu SET ORD = 9 WHERE NAME = 'profile.branchMaster';
UPDATE ese_menu SET ORD = 10 WHERE NAME = 'admin.masterData';
UPDATE ese_menu SET ORD = 12 WHERE NAME = 'admin.menuCreationToolGrid';


UPDATE eses_agro.locale_property SET lang_value = 'Farmer Code,Farmer Name,Father Name,Village,Group,Is Certified,Total Land Holding,Status' WHERE code = 'exportFarmerHeading';


ALTER TABLE  Periodic_Inspection ADD COLUMN CROP_PROTECTION_PRACTICE varchar(45)  DEFAULT NULL AFTER TXN_TYPE;


-- - 30-10-2019

ALTER TABLE farm 
ADD COLUMN GC_PARCEL_ID varchar(25) NULL AFTER CERTIFICATE_STANDARD_LEVEL;


ALTER TABLE farm_crops 
ADD COLUMN GC_PARCEL_ID varchar(45) NULL AFTER CROP_EDIT_STATUS;

-- - 06-11-2019

ALTER TABLE crop_yield 
ADD COLUMN BRANCH_ID varchar(50) NULL DEFAULT NULL AFTER STATUS;

-- - 08-11-2019
ALTER TABLE event_report 
ADD COLUMN EVENT_TYPE varchar(255) NULL AFTER FARMER_NAME,
ADD COLUMN START_DATE date NULL DEFAULT NULL AFTER EVENT_TYPE,
ADD COLUMN END_DATE date NULL DEFAULT NULL AFTER START_DATE,
ADD COLUMN START_TIME varchar(255) NULL AFTER END_DATE,
ADD COLUMN END_TIME varchar(255) NULL AFTER START_TIME,
ADD COLUMN PURPOSE varchar(255) NULL AFTER END_TIME,
ADD COLUMN WAREHOUSE_NAME varchar(255) NULL AFTER PURPOSE,
ADD COLUMN EVENTTYPE_CATALOGUE_NAME varchar(255) NULL AFTER WAREHOUSE_NAME;

ALTER TABLE event_report 
ADD COLUMN STATUS_CATALOGUE_NAME varchar(255) NULL AFTER EVENTTYPE_CATALOGUE_NAME;

ALTER TABLE event_report 
ADD COLUMN AGENT_PERS_INFO_NAME varchar(255) NULL AFTER STATUS_CATALOGUE_NAME;

update event_report e set e.EVENT_TYPE = (SELECT ec.EVENT_TYPE from event_calendar ec where ec.MSG_NO = e.EVENT_ID limit 1);
update event_report e set e.START_DATE = (SELECT ec.START_DATE from event_calendar ec where ec.MSG_NO = e.EVENT_ID limit 1);
update event_report e set e.END_DATE = (SELECT ec.END_DATE from event_calendar ec where ec.MSG_NO = e.EVENT_ID limit 1);
update event_report e set e.START_TIME = (SELECT ec.START_TIME from event_calendar ec where ec.MSG_NO = e.EVENT_ID limit 1);
update event_report e set e.END_TIME = (SELECT ec.END_TIME from event_calendar ec where ec.MSG_NO = e.EVENT_ID limit 1);
update event_report e set e.PURPOSE = (SELECT ec.PURPOUSE from event_calendar ec where ec.MSG_NO = e.EVENT_ID limit 1);
update event_report e set e.WAREHOUSE_NAME = (SELECT w.NAME from warehouse w where w.code =  e.GROUP_CODE limit 1);
update event_report e set e.EVENTTYPE_CATALOGUE_NAME = (SELECT cv.NAME FROM catalogue_value cv WHERE  cv.CODE =   e.EVENT_TYPE limit 1);
update event_report e set e.STATUS_CATALOGUE_NAME = (SELECT ifnull( cv.NAME, 'Initiated' ) FROM catalogue_value cv WHERE  cv.CODE =   e.STATUS limit 1);
update event_report e set e.AGENT_PERS_INFO_NAME = (select concat( pi.FIRST_NAME, pi.LAST_NAME ) from pers_info pi where pi.ID = (select p.PERS_INFO_ID from prof p where p.PROF_ID = e.AGENT_ID));

-- - 13-011-2019
ALTER TABLE ese_account ADD COLUMN LOAN_ACC_NO varchar(50) NULL DEFAULT NULL AFTER SAVING_AMOUNT;

ALTER TABLE ese_account ADD COLUMN LOAN_AMOUNT double(20, 2) NULL DEFAULT 0.00 AFTER LOAN_ACC_NO;

ALTER TABLE ese_account ADD COLUMN OUTSTANDING_AMOUNT double(20, 2) NULL DEFAULT 0.00 AFTER LOAN_AMOUNT;


CREATE TABLE loan_application  (
  ID bigint(45) NOT NULL AUTO_INCREMENT,
  FARMER_ID bigint(45) NULL DEFAULT NULL,
  SEEDLING_QTY varchar(45) NULL DEFAULT NULL,
  LOAN_STATUS varchar(255) NULL DEFAULT NULL,
  DECLINE_REASON varchar(255) NULL DEFAULT NULL,
  CREATED_USER varchar(250) NULL DEFAULT NULL,
  CREATED_DATE datetime(0) NULL DEFAULT NULL,
  UPDATED_USER varchar(250) NULL DEFAULT NULL,
  UPDATED_DATE datetime(0) NULL DEFAULT NULL,
  BRANCH_ID varchar(25) NULL DEFAULT NULL,
  FARMER_DYNAMIC_DATA_ID bigint(45) NULL DEFAULT NULL,
  PRIMARY KEY (ID) USING BTREE
) ENGINE = InnoDB ROW_FORMAT = Dynamic;

CREATE TABLE loan_distribution  (
  ID bigint(45) NOT NULL AUTO_INCREMENT,
  LOAN_DATE datetime(0) NULL DEFAULT NULL,
  LOAN_TO bigint(10) NULL DEFAULT NULL,
  VILLAGE_ID bigint(45) NULL DEFAULT NULL,
  FARMER_ID bigint(45) NULL DEFAULT NULL,
  TRXN_AGRO_ID bigint(45) NULL DEFAULT NULL,
  GROUP_ID bigint(45) NULL DEFAULT NULL,
  LOAN_CATEGORY bigint(10) NULL DEFAULT NULL,
  CREATED_USER varchar(90) NULL DEFAULT NULL,
  CREATED_DATE datetime(0) NULL DEFAULT NULL,
  UPDATED_USER varchar(90) NULL DEFAULT NULL,
  UPDATED_DATE datetime(0) NULL DEFAULT NULL,
  BRANCH_ID varchar(45) NULL DEFAULT NULL,
  DOWN_PAYMENT_QTY double(20, 2) NULL DEFAULT NULL,
  DOWN_PAYMENT_AMT double(20, 2) NULL DEFAULT NULL,
  INTEREST_PERCENTAGE double(20, 2) NULL DEFAULT NULL,
  INTEREST_AMOUNT double(20, 2) NULL DEFAULT NULL,
  LOAN_TENURE bigint(10) NULL DEFAULT NULL,
  TOTAL_COST_TO_FARMER double(20, 2) NULL DEFAULT NULL,
  LOAN_TENURE_AMT double(20, 2) NULL DEFAULT NULL,
  MONTHLY_REPAYMENT_AMT double(20, 2) NULL DEFAULT NULL,
  LOAN_STATUS bigint(20) NULL DEFAULT NULL,
  VENDOR_ID bigint(3) NULL DEFAULT NULL,
  PRIMARY KEY (ID) USING BTREE
) ENGINE = InnoDB ROW_FORMAT = Dynamic;

CREATE TABLE loan_distribution_detail  (
  ID bigint(45) NOT NULL AUTO_INCREMENT,
  PRODUCT_CODE bigint(45) NULL DEFAULT NULL,
  RATE_PER_UNIT double(20, 2) NULL DEFAULT NULL,
  QUANTITY double(20, 2) NULL DEFAULT NULL,
  AMOUNT double(20, 2) NULL DEFAULT NULL,
  SUBSIDY_INTEREST double(20, 2) NULL DEFAULT NULL,
  SUBSIDY_AMT double(20, 2) NULL DEFAULT NULL,
  TOTAL_AMT double(20, 2) NULL DEFAULT NULL,
  LOAN_DISTRIBUTION_ID bigint(45) NULL DEFAULT NULL,
  PRIMARY KEY (ID) USING BTREE
) ENGINE = InnoDB ROW_FORMAT = Dynamic;

CREATE TABLE loan_ledger  (
  ID bigint(20) NOT NULL AUTO_INCREMENT,
  TXN_TIME datetime(0) NULL DEFAULT NULL,
  RECEIPT_NO varchar(45) NULL DEFAULT NULL,
  ESE_ACCOUNT_ID bigint(45) NULL DEFAULT NULL,
  FARMER_ID varchar(20) NULL DEFAULT NULL,
  ACC_NO varchar(50) NULL DEFAULT NULL,
  ACTUAL_AMT double(25, 3) NULL DEFAULT 0.000,
  LOAN_PERCENTAGE double(25, 3) NULL DEFAULT NULL,
  FINAL_PAY_AMOUNT double(25, 3) NULL DEFAULT NULL,
  FARMER_OUTSTANDING_BAL double(25, 3) NULL DEFAULT NULL,
  FARMER_LOAN_BAL double(25, 3) NULL DEFAULT NULL,
  TXN_TYPE varchar(10) NULL DEFAULT NULL,
  LOAN_DESC varchar(255) NULL DEFAULT NULL,
  BRANCH varchar(255) NULL DEFAULT NULL,
  VENDOR_ID varchar(20) NULL DEFAULT NULL,
  LOAN_STATUS bigint(2) NULL DEFAULT NULL,
  PRIMARY KEY (ID) USING BTREE
) ENGINE = InnoDB ROW_FORMAT = Dynamic;


-- - 15-11-2019

INSERT INTO eses_wel_spun_qa.locale_property(id, code, lang_code, lang_value) VALUES (NULL, 'farm.proposedPlantingAreas', 'en', 'Total Cultivable Land(Acre)');
INSERT INTO eses_wel_spun_qa.locale_property(id, code, lang_code, lang_value) VALUES (NULL, 'totcultiland', 'en', 'Total Cultivable Land(Acre)');
INSERT INTO eses_wel_spun_qa.locale_property(id, code, lang_code, lang_value) VALUES (NULL, 'totland', 'en', 'Field Area (Acres)');
INSERT INTO eses_wel_spun_qa.locale_property(id, code, lang_code, lang_value) VALUES (NULL, 'farmcode', 'en', 'Farmer Code');
INSERT INTO eses_wel_spun_qa.locale_property(id, code, lang_code, lang_value) VALUES (NULL, 'farmercode', 'en', 'Farmer name');
INSERT INTO eses_wel_spun_qa.locale_property(id, code, lang_code, lang_value) VALUES (NULL, 'rainfed', 'en', 'Rainfed');
INSERT INTO eses_wel_spun_qa.locale_property(id, code, lang_code, lang_value) VALUES (NULL, 'irrigated', 'en', 'Irrigated');
INSERT INTO eses_wel_spun_qa.locale_property(id, code, lang_code, lang_value) VALUES (NULL, 'sowing.location', 'en', 'Sowing Location');

-- -19-11-2019
ALTER TABLE dynamic_report_config_filter 
ADD COLUMN DEFAULT_FILTER varchar(255) CHARACTER SET Utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER IS_DATE_FILTER;

INSERT INTO locale_property(id, code, lang_code, lang_value) VALUES (null, 'farmerNPOPColumnHeaderBranchWithFarmerCode', 'en', 'S.no,Organization,Farmer Code,Farmer Name,Farm Code,Farm Name,Answered Date');
INSERT INTO locale_property(id, code, lang_code, lang_value) VALUES (null, 'farmerNPOPColumnHeaderBranch', 'en', 'S.no,Organization,Farmer Name,Farm Code,Farm Name,Answered Date');
INSERT INTO locale_property(id, code, lang_code, lang_value) VALUES (null, 'farmerNPOPColumnHeaderWithFarmerCode', 'en', 'S.no,Farmer Code,Farmer Name,Farm Code,Farm Name,Answered Date');
INSERT INTO locale_property(id, code, lang_code, lang_value) VALUES (null, 'farmerNPOPColumnHeader', 'en', 'S.no,Farmer Name,Farm Code,Farm Name,Answered Date');

-- -21 NOV 2019
-- -SusAgri ICS Summary Report Menu Script
INSERT INTO ese_menu VALUES (0, 'report.icsSummary', 'ICS Summary Report', 'icsSummaryReport_list.action', 20, 4, 0, 1, 0, 0, NULL);
INSERT INTO ese_ent VALUES (0, 'report.icsSummary.list');
INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='report.icsSummary'), '1');
INSERT INTO ese_role_ent VALUES ('1', (SELECT id FROM ese_ent WHERE NAME='report.icsSummary.list'));
INSERT INTO ese_role_menu VALUES (null,(SELECT id FROM ese_menu WHERE NAME='report.icsSummary'), '1');


-- - 10-12-2019

set @parent_id = 3;
set @menu_order = (SELECT max(ord) FROM ese_menu WHERE PARENT_ID = @parent_id);
set @menu_order = @menu_order+1;

INSERT INTO ese_menu (ID, NAME, DES, URL, ORD, PARENT_ID, FILTER, EXPORT_AVILABILITY) VALUES (NULL, 'service.ndvi', 'NDVI INFO', 'ndvi_list.action', @menu_order, @parent_id, '0', '0');

SET @max_menu_id = (SELECT MAX(id) FROM ese_menu);

INSERT INTO ese_role_menu(ID, MENU_ID, ROLE_ID) VALUES (NULL, @max_menu_id, 1);

INSERT INTO ese_menu_action (MENU_ID, ACTION_ID) VALUES (@max_menu_id, '1');


INSERT INTO ese_ent (ID, NAME) VALUES (NULL, 'service.ndvi.list');

INSERT INTO ese_role_ent(ROLE_ID, ENT_ID) VALUES (1, (SELECT ID FROM ese_ent WHERE ese_ent.NAME = 'service.ndvi.list'));


-- -24-12-2019
UPDATE eses_agro.locale_property SET code = 'ExportMainGridHeadingProcurmentStockBranch', lang_code = 'en', lang_value = 'S.no,Organization,Warehouse,Product Name,Unit,Number of Bags,Net Weight' WHERE code = 'ExportMainGridHeadingProcurmentStockBranch';
UPDATE eses_agro.locale_property SET code = 'exportSubColumnHeaderpmtnt', lang_code = 'en', lang_value = 'Product,Unit,Variety,Grade,No Of Bags,Net Weight' WHERE  code = 'exportSubColumnHeaderpmtnt';
UPDATE eses_agro.locale_property SET code = 'exportMobileUserStockReportColumnHeaderBranch', lang_code = 'en', lang_value = 'S.no,Organization,Mobile User Id ,Mobile User,Farm Input,Product ,Unit,Stock,Season' WHERE code = 'exportMobileUserStockReportColumnHeaderBranch';
UPDATE eses_agro.locale_property SET code = 'sowingSubgrid', lang_code = 'en', lang_value = 'Farm ,Crop Type ,Crop,Unit,Variety,Estimated yield(Kg),Sowing Date,Cultivation Area' WHERE code = 'sowingSubgrid';
UPDATE eses_agro.locale_property SET code = 'exportFarmerCropColumnHeaderBranch', lang_code = 'en', lang_value = 'S.no,Organization,Season,Farmer,Village,Farm,Crop(MainCrop),Unit,Area in %#,Type,InterCrop Details,Estimated yield (Kg),Actual Harvested (Kg),Per acre yield (Kg)' WHERE  code = 'exportFarmerCropColumnHeaderBranch';

INSERT INTO eses_agro.locale_property(id, code, lang_code, lang_value) VALUES (NULL, 'amt', 'en', 'Transaction Amount (₹)');
INSERT INTO eses_agro.locale_property(id, code, lang_code, lang_value) VALUES (NULL, 'payementAmount', 'en', 'Payement Amount (₹)');
INSERT INTO eses_agro.locale_property(id, code, lang_code, lang_value) VALUES (NULL, 'txnAmount', 'en', 'Transaction Amount (₹)');
INSERT INTO eses_agro.locale_property(id, code, lang_code, lang_value) VALUES (NULL, 'produnit', 'en', 'Unit');
INSERT INTO eses_agro.locale_property(id, code, lang_code, lang_value) VALUES (NULL, 'pricePerUnit', 'en', 'Price Per Unit (₹)');
INSERT INTO eses_agro.locale_property(id, code, lang_code, lang_value) VALUES (NULL, 'grossAmt', 'en', 'Gross Amount (₹)');

-- -22 jan 2020
UPDATE eses_agro.locale_property SET code = 'exportFarmerCropColumnHeader', lang_code = 'en', lang_value = 'S.no,Season,Farmer,Village,Farm,Crop(MainCrop),Unit,Area in %#,Type,InterCrop Details,Estimated yield (Kg),Actual Harvested (Kg),Per acre yield (Kg)' WHERE code = 'exportFarmerCropColumnHeader';
UPDATE eses_agro.locale_property SET code = 'exportColumnHeaderBranchCropSale', lang_code = 'en', lang_value = 'S.no,Organization,Date,Season,Mobile User Id,Farmer,Farmer Code,Farm,Village,Buyer,Invoice/Receipt No.,Sale Price/Kg (@*),Total Sale Quantity (Kg),Total Sale Value (@*),Transporter,Vehicle Number,Payment Amount (@*)' WHERE code = 'exportColumnHeaderBranchCropSale';
UPDATE eses_agro.locale_property SET code = 'exportSubColumnHeaderpmtnt', lang_code = 'en', lang_value = 'Product,Unit,Variety,Grade,No Of Bags,Net Weight (Kg)' WHERE code = 'exportSubColumnHeaderpmtnt';

-- -03.02.2020
INSERT INTO pref VALUES (null, 'PLOTTING_TYPE_MAP', '1', 1, '1=Farm,2=Farm crop,3=Both');



-- -04.02.2020
INSERT INTO ese_menu(ID, NAME, DES, URL, ORD, PARENT_ID, FILTER, EXPORT_AVILABILITY, DIMENSION, PRIORITY, ICON_CLASS) VALUES (NULL, 'report.FarmReport', 'Farm Report', 'dynamicViewReport_list.action?id=10', 25, 4, 0, 1, 0, 0, NULL);


INSERT INTO  dynamic_report_config_detail(ID, REPORT_CONFIG_ID, LABEL_NAME, FIELD, ACESS_TYPE, PARAMTERS, METHOD, GROUP_PROP, SUM_PROP, WIDTH, ORDERR, IS_GRID_AVAILABILITY, IS_EXPORT_AVAILABILITY, STATUSS, EXPRESSION, ALIGNMENT, IS_FOOTER_SUM, IS_GROUP_HEADER) VALUES (NULL, 10, 'Id', 'ID', 1, NULL, NULL, 0, 0, 100, 1, 1, 1, 1, NULL, NULL, NULL, NULL);
INSERT INTO  dynamic_report_config_detail(ID, REPORT_CONFIG_ID, LABEL_NAME, FIELD, ACESS_TYPE, PARAMTERS, METHOD, GROUP_PROP, SUM_PROP, WIDTH, ORDERR, IS_GRID_AVAILABILITY, IS_EXPORT_AVAILABILITY, STATUSS, EXPRESSION, ALIGNMENT, IS_FOOTER_SUM, IS_GROUP_HEADER) VALUES (NULL, 10, 'Farm Name', 'FARM', 1, NULL, NULL, 0, 0, 100, 7, 1, 1, 1, NULL, NULL, NULL, NULL);
INSERT INTO  dynamic_report_config_detail(ID, REPORT_CONFIG_ID, LABEL_NAME, FIELD, ACESS_TYPE, PARAMTERS, METHOD, GROUP_PROP, SUM_PROP, WIDTH, ORDERR, IS_GRID_AVAILABILITY, IS_EXPORT_AVAILABILITY, STATUSS, EXPRESSION, ALIGNMENT, IS_FOOTER_SUM, IS_GROUP_HEADER) VALUES (NULL, 10, 'Farmer Id', 'FARMER_ID', 1, NULL, NULL, 0, 0, 100, 3, 1, 1, 1, NULL, NULL, NULL, NULL);
INSERT INTO  dynamic_report_config_detail(ID, REPORT_CONFIG_ID, LABEL_NAME, FIELD, ACESS_TYPE, PARAMTERS, METHOD, GROUP_PROP, SUM_PROP, WIDTH, ORDERR, IS_GRID_AVAILABILITY, IS_EXPORT_AVAILABILITY, STATUSS, EXPRESSION, ALIGNMENT, IS_FOOTER_SUM, IS_GROUP_HEADER) VALUES (NULL, 10, 'Plottting Status', 'PLOTTING_STATUS', 1, NULL, NULL, 0, 0, 100, 8, 1, 1, 1, NULL, NULL, NULL, NULL);
INSERT INTO  dynamic_report_config_detail(ID, REPORT_CONFIG_ID, LABEL_NAME, FIELD, ACESS_TYPE, PARAMTERS, METHOD, GROUP_PROP, SUM_PROP, WIDTH, ORDERR, IS_GRID_AVAILABILITY, IS_EXPORT_AVAILABILITY, STATUSS, EXPRESSION, ALIGNMENT, IS_FOOTER_SUM, IS_GROUP_HEADER) VALUES (NULL, 10, 'Farmer Name', 'FARMER_NAME', 1, NULL, NULL, 0, 0, 100, 4, 1, 1, 1, NULL, NULL, NULL, NULL);
INSERT INTO  dynamic_report_config_detail(ID, REPORT_CONFIG_ID, LABEL_NAME, FIELD, ACESS_TYPE, PARAMTERS, METHOD, GROUP_PROP, SUM_PROP, WIDTH, ORDERR, IS_GRID_AVAILABILITY, IS_EXPORT_AVAILABILITY, STATUSS, EXPRESSION, ALIGNMENT, IS_FOOTER_SUM, IS_GROUP_HEADER) VALUES (NULL, 10, 'Branch', 'BRANCH_ID', 1, NULL, NULL, 0, 0, 100, 2, 1, 1, 1, NULL, NULL, NULL, NULL);
INSERT INTO  dynamic_report_config_detail(ID, REPORT_CONFIG_ID, LABEL_NAME, FIELD, ACESS_TYPE, PARAMTERS, METHOD, GROUP_PROP, SUM_PROP, WIDTH, ORDERR, IS_GRID_AVAILABILITY, IS_EXPORT_AVAILABILITY, STATUSS, EXPRESSION, ALIGNMENT, IS_FOOTER_SUM, IS_GROUP_HEADER) VALUES (NULL, 10, 'Farm Code', 'farm_code', 1, NULL, NULL, 0, 0, 100, 6, 1, 1, 1, NULL, NULL, NULL, NULL);
INSERT INTO  dynamic_report_config_detail(ID, REPORT_CONFIG_ID, LABEL_NAME, FIELD, ACESS_TYPE, PARAMTERS, METHOD, GROUP_PROP, SUM_PROP, WIDTH, ORDERR, IS_GRID_AVAILABILITY, IS_EXPORT_AVAILABILITY, STATUSS, EXPRESSION, ALIGNMENT, IS_FOOTER_SUM, IS_GROUP_HEADER) VALUES (NULL, 10, 'Village', 'villagename', 1, NULL, NULL, 0, 0, 100, 5, 1, 1, 1, NULL, NULL, NULL, NULL);

INSERT INTO  dynamic_report_config_filter(ID, REPORT_CONFIG, FIELD, LABEL, METHOD, STATUS, ORDERR, TYPE, IS_DATE_FILTER, DEFAULT_FILTER) VALUES (NULL, 10, 'FARM', 'Farm Name', 'getFarmsList', 1, 4, 3, NULL, NULL);
INSERT INTO  dynamic_report_config_filter(ID, REPORT_CONFIG, FIELD, LABEL, METHOD, STATUS, ORDERR, TYPE, IS_DATE_FILTER, DEFAULT_FILTER) VALUES (NULL, 10, 'farmer_name', 'Farmer Name', 'getFarmerFirstNameList', 1, 2, 3, NULL, NULL);
INSERT INTO  dynamic_report_config_filter(ID, REPORT_CONFIG, FIELD, LABEL, METHOD, STATUS, ORDERR, TYPE, IS_DATE_FILTER, DEFAULT_FILTER) VALUES (NULL, 10, 'villagename', 'Village', 'getVillageList', 1, 3, 3, NULL, NULL);

UPDATE locale_property SET lang_value = 'S.no,Organization,Date,Season,Transfer Receipt Number,User Name/Mobile User,Sender Warehouse/Village,Receiver Warehouse,Truck Id,Driver ,Total No Of Bags,Total Net Weight (Kg)' WHERE id = 426
UPDATE locale_property SET lang_value = 'S.no,Organization,Date,Season,Transfer Receipt Number,Receiver Receipt Number,Sender Warehouse/Village,Receiver Warehouse ,Truck Id,Driver,Total Transferred Net Weight (Kg),Total Recieved Net Weight (Kg), Transferred Loss(Kg)' WHERE id = 429

---- Farm report filter

UPDATE dynamic_report_config_filter SET FIELD = 'VILLAGE_ID' WHERE ID = 31;
UPDATE dynamic_report_config_filter SET FIELD = 'FARMER_ID' WHERE ID = 30;
UPDATE dynamic_report_config_filter SET FIELD = 'ID' WHERE ID = 29;

---Farm_View 

DROP view if EXISTS farm_view;

CREATE VIEW farm_view AS (
SELECT
	farm.ID AS ID,
	farm.FARM_NAME AS FARM,
	farmer.FARMER_ID AS FARMER_ID,
	( CASE WHEN ( farm.PLOTTING_STATUS= '0' ) THEN 'Pending' ELSE 'Completed' END ) AS PLOTTING_STATUS,
	farmer.BRANCH_ID AS BRANCH_ID,
	village.ID AS VILLAGE_ID,
	village.NAME AS villagename,
	concat( farmer.FIRST_NAME, '', farmer.LAST_NAME ) AS farmer_name,
	farm.FARM_CODE AS farm_code 
FROM
	(
	farm
	JOIN ( village JOIN farmer ) ON ( ( ( farm.FARMER_ID = farmer.ID ) AND ( farmer.VILLAGE_ID = village.ID ) ) ) 
	)
	);
----06-02-2020
INSERT INTO locale_property(id, code, lang_code, lang_value) VALUES (NULL, 'warehouse', 'en', 'Warehouse/Group Name');

--Farmer crop export

INSERT INTO locale_property(id, code, lang_code, lang_value) VALUES (NULL, 'exportFarmerCropColumnHeaderWithoutFarmerCodeBrn', 'en', 'S.no,Organization,Season,Farmer,Village,Farm,Crop(MainCrop),Unit,Area in (%#),Type,InterCrop Details,Estimated yield (Kg),Actual Harvested (Kg),Per acre yield (Kg)');
INSERT INTO locale_property(id, code, lang_code, lang_value) VALUES (NULL, 'exportFarmerCropColumnHeaderWithoutFarmerCode', 'en', 'S.no,Season,Farmer,Village,Farm,Crop(MainCrop),Unit,Area in (%#),Type,InterCrop Details,Estimated yield (Kg),Actual Harvested (Kg),Per acre yield (Kg)');

-- 10Feb2020
UPDATE locale_property SET lang_value = 'S.no,Organization,Season,Farmer Code,Farmer,Village,Farm,Crop(MainCrop),Unit,Area in (%#),Type,InterCrop Details,Estimated yield (Kg),Actual Harvested (Kg),Per acre yield (Kg)' WHERE code = 'exportFarmerCropColumnHeaderBranch';
UPDATE locale_property SET lang_value = 'S.no,Season,Farmer Code,Farmer,Village,Farm,Crop(MainCrop),Unit,Area in %#,Type,InterCrop Details,Estimated yield (Kg),Actual Harvested (Kg),Per acre yield (Kg)' WHERE code = 'exportFarmerCropColumnHeader'



UPDATE eses_livelihood.locale_property SET lang_value = 'Farmer Group' WHERE code = 'profile.samithi';
UPDATE eses_livelihood.ese_menu SET DES = 'Preferences' WHERE name = 'system.prefernces';

INSERT INTO eses_livelihood.locale_property(code, lang_code, lang_value) VALUES ('system.prefernces', 'en', 'Preferences');
INSERT INTO eses_livelihood.locale_property(code, lang_code, lang_value) VALUES ('profile.catalogue Farm Catalogue', 'en', 'Farm Catalogue');
INSERT INTO eses_livelihood.locale_property(code, lang_code, lang_value) VALUES ('farmer.dateOfJoin', 'en', 'Enrollment date');
INSERT INTO eses_livelihood.locale_property(code, lang_code, lang_value) VALUES ('CatalogueAction.breadCrumb', 'en', 'Profile~#, Farm Catalogue~catalogue_list.action');
INSERT INTO eses_livelihood.locale_property(code, lang_code, lang_value) VALUES ('PreferncesAction.breadCrumb', 'en', 'Preferences~prefernce_list.action');
INSERT INTO eses_livelihood.locale_property(code, lang_code, lang_value) VALUES ('SowingLocationAction.breadCrumb', 'en', 'Home~#,Coconut Location~sowingLocation_list.action');
INSERT INTO eses_livelihood.locale_property(code, lang_code, lang_value) VALUES ('profile.catalogue', 'en', 'Farm Catalogue');


INSERT INTO eses_livelihood.locale_property(id, code, lang_code, lang_value) VALUES (NULL, 'status1', 'en', 'Approved');
INSERT INTO eses_livelihood.locale_property(id, code, lang_code, lang_value) VALUES (NULL, 'status0', 'en', 'Not Approved');


-- - 20 FEB 2020
-- -Griffith

ALTER TABLE `eses_griffith`.`cold_storage_stock_transfer_detail` 
ADD COLUMN `BATCH_NO` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER `FARMER_ID`;

ALTER TABLE `procurement_variety`
ADD COLUMN `BRANCH_ID`  varchar(45) NULL AFTER `INITIAL_HARVEST_DAYS`;

ALTER TABLE `dynamic_menu_config`
ADD COLUMN `METHOD`  varchar(45) NULL AFTER `BRANCH`;

ALTER TABLE `dynamic_menu_config`
ADD COLUMN `PARAMETER`  varchar(250) NULL AFTER `METHOD`;

-- -21 Feb 2020 -- Welspun
ALTER TABLE `crop_supply` 
ADD COLUMN `PO_NUMBER` varchar(200) NULL DEFAULT NULL AFTER `COOPERATIVE`;

ALTER TABLE `farm` 
ADD COLUMN `SEASON` varchar(200) NULL DEFAULT NULL AFTER `UPDATED_DATE`;

ALTER TABLE `crop_yield` 
ADD COLUMN `TYPE` varchar(200) NULL DEFAULT NULL AFTER `BRANCH_ID`;


ALTER TABLE `eses_kenyafpo`.`warehouse` 
ADD COLUMN `VILLAGE_ID` bigint(6) NULL DEFAULT -1 AFTER `LONGITUDE`,
ADD COLUMN `EMAIL_ID` varchar(60) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER `VILLAGE_ID`;


-- -6 march 2020 -- Farm Agg
ALTER TABLE `bank_info` 
ADD COLUMN `ACC_NAME` varchar(255) CHARACTER SET UTf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER `WAREHOUSE_ID`;

ALTER TABLE `farmer` 
ADD COLUMN `LIFEINS_CMP_NAME` varchar(150) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER `Digital_Signature`,
ADD COLUMN `HEALTH_INS_CMP_NAME` varchar(150) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER `LIFEINS_CMP_NAME`,
ADD COLUMN `CRP_INS_CMP_NAME` varchar(150) CHARACTER SET UTF8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER `HEALTH_INS_CMP_NAME`;

CREATE TABLE `samithi_ics`  (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `WAREHOUSE_ID` bigint(20) NULL DEFAULT NULL,
  `SEASION_ID` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `LAND_HOLDING` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `ICS_DATE` datetime(0) NULL DEFAULT NULL,
  `IMG` longblob NULL,
  `STATUS` int(3) NULL DEFAULT NULL,
  `BRANCH_ID` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `TYPE` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- -25 MARCH 2020 
ALTER TABLE `dynamic_report_config` 
ADD COLUMN `DETAIL_METHOD` varchar(255) NULL AFTER `GROUP_PROPERTY`;

------Agro New Dashboard Menu

SET @ids = (SELECT id FROM ese_menu WHERE NAME='dashboard');

INSERT INTO `ese_menu` VALUES (null, 'dashboard.agrodashboard', ' Agro Dashboard', 'agrodashboard_list.action', '3', @ids, '0', '0','0','0',null);

INSERT INTO `ese_ent` VALUES (null, 'dashboard.agrodashboard.list');

INSERT INTO `ese_role_ent` VALUES ('1',(SELECT id FROM ese_ent WHERE NAME='dashboard.agrodashboard.list'));

INSERT INTO `ese_menu_action` VALUES ((SELECT id FROM ese_menu WHERE NAME='dashboard.agrodashboard'), '1');

INSERT INTO `ese_role_menu` VALUES (null,(SELECT id FROM ese_menu WHERE NAME='dashboard.agrodashboard'), '1');

-- -26 MARCH 2020
ALTER TABLE `dynamic_report_config_detail` 
ADD COLUMN `DATA_TYPE` varchar(10) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL AFTER `IS_GROUP_HEADER`;


-- -30 March 2020
ALTER TABLE `dynamic_report_config_detail` 
MODIFY COLUMN `METHOD` longtext CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL AFTER `PARAMTERS`;

-- -02 Apirl 2020

INSERT INTO `pref` VALUES (null, 'SMS_MESSAGE', '<ProcurementProduct> of <quantity> <unit> in <ProcurementVariety> variety is procured on  <procurementDate> for <RatePerUnit> Euro price/kg (<grade>) and total procurement amount is <SubTotal> Rupees', 1, 'What ever text inside <> that will be replaced while sending sms');
INSERT INTO `pref` VALUES (null, 'ENABLE_SMS_IN_PROCUREMENT_MODULE', '0', 1, '0=No,1=Yes');



-- -08 APRIL 2020
-- - Loan Module
SET FOREIGN_KEY_CHECKS=0;

INSERT INTO pref VALUES (0, 'ENABLE_LOAN_MODULE', '1', 1, NULL);

INSERT INTO ese_seq VALUES (0, 'LOAN_ACCOUNT_NO_SEQ', 0);
INSERT INTO ese_seq VALUES (0, 'LOAN_DISTRIBUTION_RECEIPT_NO_SEQ', 0);
INSERT INTO ese_seq VALUES (0, 'LOAN_REPAYMENT_RECEIPT_NO_SEQ', 0);

INSERT INTO payment_mode VALUES (null, '6', 'Loan Repayment');


ALTER TABLE dynamic_report_config_filter MODIFY COLUMN METHOD varchar(255) NULL DEFAULT NULL AFTER LABEL;

ALTER TABLE ese_account MODIFY COLUMN CASH_BALANCE double(25, 2) NULL DEFAULT 0.00 AFTER PROFILE_ID;

ALTER TABLE ese_account MODIFY COLUMN CREDIT_BALANCE double(25, 2) NULL DEFAULT 0.00 AFTER CASH_BALANCE;

ALTER TABLE ese_account MODIFY COLUMN BALANCE double(25, 2) NULL DEFAULT 0.00 AFTER CREDIT_BALANCE;

ALTER TABLE ese_account MODIFY COLUMN DIST_BALANCE double(25, 2) NULL DEFAULT 0.00 AFTER BALANCE;

ALTER TABLE ese_account MODIFY COLUMN SHARE_AMOUNT double(25, 2) NULL DEFAULT 0.00 AFTER DIST_BALANCE;

ALTER TABLE ese_account MODIFY COLUMN SAVING_AMOUNT double(25, 2) NULL DEFAULT 0.00 AFTER SHARE_AMOUNT;

ALTER TABLE ese_account MODIFY COLUMN LOAN_AMOUNT double(25, 2) NULL DEFAULT 0.00 AFTER SAVING_AMOUNT;

ALTER TABLE ese_account MODIFY COLUMN LOAN_ACC_NO varchar(45) NULL DEFAULT NULL AFTER LOAN_AMOUNT;

ALTER TABLE ese_account MODIFY COLUMN OUTSTANDING_AMOUNT double(25, 2) NULL DEFAULT 0.00 AFTER LOAN_ACC_NO;

ALTER TABLE ese_account MODIFY COLUMN BRANCH_ID varchar(45) NULL DEFAULT NULL AFTER OUTSTANDING_AMOUNT;


DROP TABLE IF EXISTS loan_application;
CREATE TABLE loan_application  (
  ID bigint(45) NOT NULL AUTO_INCREMENT,
  FARMER_ID bigint(45) NULL DEFAULT NULL,
  SEEDLING_QTY varchar(45) NULL DEFAULT NULL,
  LOAN_STATUS varchar(255) NULL DEFAULT NULL,
  DECLINE_REASON varchar(255) NULL DEFAULT NULL,
  CREATED_USER varchar(250) NULL DEFAULT NULL,
  CREATED_DATE datetime NULL DEFAULT NULL,
  UPDATED_USER varchar(250) NULL DEFAULT NULL,
  UPDATED_DATE datetime NULL DEFAULT NULL,
  BRANCH_ID varchar(25) NULL DEFAULT NULL,
  FARMER_DYNAMIC_DATA_ID bigint(45) NULL DEFAULT NULL,
  PRIMARY KEY (ID) USING BTREE
);
DROP TABLE IF EXISTS loan_distribution;
CREATE TABLE loan_distribution  (
  ID bigint(45) NOT NULL AUTO_INCREMENT,
  LOAN_DATE datetime NULL DEFAULT NULL,
  LOAN_TO bigint(10) NULL DEFAULT NULL,
  VILLAGE_ID bigint(45) NULL DEFAULT NULL,
  FARMER_ID bigint(45) NULL DEFAULT NULL,
  TRXN_AGRO_ID bigint(45) NULL DEFAULT NULL,
  GROUP_ID bigint(45) NULL DEFAULT NULL,
  LOAN_CATEGORY bigint(10) NULL DEFAULT NULL,
  CREATED_USER varchar(90) NULL DEFAULT NULL,
  CREATED_DATE datetime NULL DEFAULT NULL,
  UPDATED_USER varchar(90) NULL DEFAULT NULL,
  UPDATED_DATE datetime NULL DEFAULT NULL,
  BRANCH_ID varchar(45) NULL DEFAULT NULL,
  DOWN_PAYMENT_QTY double(20, 2) NULL DEFAULT NULL,
  DOWN_PAYMENT_AMT double(20, 2) NULL DEFAULT NULL,
  INTEREST_PERCENTAGE double(20, 2) NULL DEFAULT NULL,
  INTEREST_AMOUNT double(20, 2) NULL DEFAULT NULL,
  LOAN_TENURE bigint(10) NULL DEFAULT NULL,
  TOTAL_COST_TO_FARMER double(20, 2) NULL DEFAULT NULL,
  LOAN_TENURE_AMT double(20, 2) NULL DEFAULT NULL,
  MONTHLY_REPAYMENT_AMT double(20, 2) NULL DEFAULT NULL,
  LOAN_STATUS bigint(20) NULL DEFAULT NULL,
  VENDOR_ID bigint(3) NULL DEFAULT NULL,
  PRIMARY KEY (ID) USING BTREE
);
DROP TABLE IF EXISTS loan_distribution_detail;
CREATE TABLE loan_distribution_detail  (
  ID bigint(45) NOT NULL AUTO_INCREMENT,
  PRODUCT_CODE bigint(45) NULL DEFAULT NULL,
  RATE_PER_UNIT double(20, 2) NULL DEFAULT NULL,
  QUANTITY double(20, 2) NULL DEFAULT NULL,
  AMOUNT double(20, 2) NULL DEFAULT NULL,
  SUBSIDY_INTEREST double(20, 2) NULL DEFAULT NULL,
  SUBSIDY_AMT double(20, 2) NULL DEFAULT NULL,
  TOTAL_AMT double(20, 2) NULL DEFAULT NULL,
  LOAN_DISTRIBUTION_ID bigint(45) NULL DEFAULT NULL,
  PRIMARY KEY (ID) USING BTREE
);
DROP TABLE IF EXISTS loan_ledger;
CREATE TABLE loan_ledger  (
  ID bigint(20) NOT NULL AUTO_INCREMENT,
  TXN_TIME datetime NULL DEFAULT NULL,
  RECEIPT_NO varchar(45) NULL DEFAULT NULL,
  ESE_ACCOUNT_ID bigint(45) NULL DEFAULT NULL,
  FARMER_ID varchar(20) NULL DEFAULT NULL,
  ACC_NO varchar(50) NULL DEFAULT NULL,
  ACTUAL_AMT double(25, 3) NULL DEFAULT 0.000,
  LOAN_PERCENTAGE double(25, 3) NULL DEFAULT NULL,
  FINAL_PAY_AMOUNT double(25, 3) NULL DEFAULT NULL,
  FARMER_OUTSTANDING_BAL double(25, 3) NULL DEFAULT NULL,
  FARMER_LOAN_BAL double(25, 3) NULL DEFAULT NULL,
  TXN_TYPE varchar(10) NULL DEFAULT NULL,
  LOAN_DESC varchar(255) NULL DEFAULT NULL,
  BRANCH varchar(255) NULL DEFAULT NULL,
  VENDOR_ID varchar(20) NULL DEFAULT NULL,
  LOAN_STATUS bigint(2) NULL DEFAULT NULL,
  PRIMARY KEY (ID) USING BTREE
);
DROP TABLE IF EXISTS loan_repayment_report;
CREATE TABLE loan_repayment_report  (
  ID bigint(20) NOT NULL DEFAULT 0,
  YEAR int(4) NULL DEFAULT NULL,
  FIRST_NAME varchar(50) NULL DEFAULT NULL,
  FARMER_ID varchar(50) NULL DEFAULT NULL,
  BRANCH_ID varchar(50) NULL DEFAULT NULL,
  VILLAGE_ID bigint(45) NULL DEFAULT NULL,
  NAME varchar(35) NOT NULL,
  VILLAGE_CODE varchar(20) NOT NULL,
  GROUP_ID bigint(20) NOT NULL DEFAULT 0,
  GROUP_CODE varchar(20) NOT NULL,
  GROUP_NAME varchar(45) NOT NULL,
  TOTAL_LOAN_AMT double(25, 2) NULL DEFAULT 0.00,
  REPAYMENT_PER_YEAR double(20, 2) NULL DEFAULT NULL,
  AMOUNT_REPAID decimal(30, 1) NULL DEFAULT NULL,
  PENDING_REPAYMENT decimal(30, 1) NULL DEFAULT NULL
);

DROP TABLE IF EXISTS loan_interest;
CREATE TABLE loan_interest  (
  ID bigint(20) NOT NULL AUTO_INCREMENT,
  MIN_RANGE bigint(255) NULL DEFAULT NULL,
  MAX_RANGE bigint(255) NULL DEFAULT NULL,
  INTEREST double(100, 0) NULL DEFAULT NULL,
  ESE_ID bigint(20) NOT NULL,
  PRIMARY KEY (ID) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of loan_interest
-- ----------------------------
INSERT INTO loan_interest VALUES (0, 1000000, 100000000, 5, 1);
INSERT INTO loan_interest VALUES (0, 1, 100000, 2, 1);
INSERT INTO loan_interest VALUES (0, 100000, 1000000, 3, 1);


ALTER TABLE offline_procurement ADD COLUMN LOAN_INTEREST_PERCENT double(20, 2) NULL DEFAULT 0.00 AFTER BUYER;

ALTER TABLE offline_procurement ADD COLUMN LOAN_REPAYMENT_AMT double(25, 2) NULL DEFAULT 0.00 AFTER LOAN_INTEREST_PERCENT;

ALTER TABLE procurement ADD COLUMN ACTUAL_AMT double(25, 3) NULL DEFAULT NULL AFTER BUYER;

ALTER TABLE procurement ADD COLUMN LOAN_PERCENTAGE double(10, 3) NULL DEFAULT NULL AFTER ACTUAL_AMT;

ALTER TABLE procurement ADD COLUMN LOAN_AMT double(25, 3) NULL DEFAULT NULL AFTER LOAN_PERCENTAGE;

ALTER TABLE procurement ADD COLUMN FINAL_PAY_AMOUNT double(25, 3) NULL DEFAULT NULL AFTER LOAN_AMT;


-- -Loan Distribution
INSERT INTO ese_menu VALUES (NULL, 'service.loanDistributionService', 'Loan Disbursement', 'loanDistribution_create.action', '13', '3', '0', '0', '0', '0', '');
INSERT INTO ese_ent VALUES (NULL, 'service.loanDistributionService.list');
INSERT INTO ese_ent VALUES (NULL, 'service.loanDistributionService.create');
INSERT INTO ese_ent VALUES (NULL, 'service.loanDistributionService.update');
INSERT INTO ese_role_ent VALUES ('1', (SELECT id FROM ese_ent WHERE NAME='service.loanDistributionService.list'));
INSERT INTO ese_role_ent VALUES ('1', (SELECT id FROM ese_ent WHERE NAME='service.loanDistributionService.create'));
INSERT INTO ese_role_ent VALUES ('1', (SELECT id FROM ese_ent WHERE NAME='service.loanDistributionService.update'));
INSERT INTO ese_role_menu VALUES (null,(SELECT id FROM ese_menu WHERE NAME='service.loanDistributionService'), '1');
INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='service.loanDistributionService'), '1');
INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='service.loanDistributionService'), '2');
INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='service.loanDistributionService'), '3');

-- - Loan Distribution Report
INSERT INTO ese_menu VALUES (0, 'report.loanDistribution', 'Loan Disbursement Report', 'loanDistributionReport_list.action', 16, 4, 0, 1, 0, 0, NULL);
INSERT INTO ese_ent VALUES (0, 'report.loanDistribution.list');
INSERT INTO ese_role_ent VALUES ('1', (SELECT id FROM ese_ent WHERE NAME='report.loanDistribution.list'));
INSERT INTO ese_role_menu VALUES (null,(SELECT id FROM ese_menu WHERE NAME='report.loanDistribution'), '1');
INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='report.loanDistribution'), '1');

-- -Farmer Loan Report Statement Report
INSERT INTO ese_menu VALUES (0, 'report.farmerLoanStatementReport', 'Farmer Loan Statement Report', 'farmerLoanStatementReport_list.action', 9, 54, 0, 0, 0, 0, 'NULL');
INSERT INTO ese_ent VALUES (0, 'report.farmerLoanStatementReport.list');
INSERT INTO ese_role_ent VALUES ('1', (SELECT id FROM ese_ent WHERE NAME='report.farmerLoanStatementReport.list'));
INSERT INTO ese_role_menu VALUES (null,(SELECT id FROM ese_menu WHERE NAME='report.farmerLoanStatementReport'), '1');
INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='report.farmerLoanStatementReport'), '1');

-- -Loan Management Report
INSERT INTO ese_menu VALUES (null, 'report.loanManagement', 'Loan Managament Report', 'loanManagementReport_list.action', 17, 4, 0, 0, 0, 0, NULL);
INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='report.loanManagement'), 1);
INSERT INTO ese_ent VALUES (null, 'report.loanManagement.list');
INSERT INTO ese_role_ent VALUES ('1',(SELECT id FROM ese_ent WHERE NAME='report.loanManagement.list'));
INSERT INTO ese_role_ent VALUES ('2',(SELECT id FROM ese_ent WHERE NAME='report.loanManagement.list'));
INSERT INTO ese_role_menu VALUES (null,(SELECT id FROM ese_menu WHERE NAME='report.loanManagement'), '1');


-- -Farmer Statement Report
INSERT INTO ese_menu VALUES (0, 'report.farmerStatementReport', 'Farmer Statement Report', 'farmerStatementReport_list.action', 9, 54, 0, 0, 0, 0, 'NULL');
INSERT INTO ese_ent VALUES (0, 'report.farmerStatementReport.list');
INSERT INTO ese_role_ent VALUES ('1', (SELECT id FROM ese_ent WHERE NAME='report.farmerStatementReport.list'));
INSERT INTO ese_role_menu VALUES (null,(SELECT id FROM ese_menu WHERE NAME='report.farmerStatementReport'), '1');
INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='report.farmerStatementReport'), '1');


-- -Pending Repayment Report Menu Script

INSERT INTO ese_menu VALUES (null, 'report.pendingRepaymentReport', 'Pending Repayment Report', 'dynamicViewReport_list.action?id=7', 92,4, 0, 0, 0, 0, NULL);
INSERT INTO ese_ent VALUES (null, 'report.pendingRepaymentReport.list');
INSERT INTO ese_role_ent VALUES ('1', (SELECT id FROM ese_ent WHERE NAME='report.pendingRepaymentReport.list'));
INSERT INTO ese_role_menu VALUES (null,(SELECT id FROM ese_menu WHERE NAME='report.pendingRepaymentReport'), '1');
INSERT INTO ese_menu_action VALUES ((SELECT id FROM ese_menu WHERE NAME='report.pendingRepaymentReport'), '1');


-- - View Pending Repayment Report Menu
select f.ID AS ID,year(ll.TXN_TIME) AS YEAR,f.FIRST_NAME AS FIRST_NAME,f.FARMER_ID AS FARMER_ID,f.BRANCH_ID AS BRANCH_ID,ld.VILLAGE_ID AS VILLAGE_ID,v.NAME AS NAME,v.CODE AS VILLAGE_CODE,w.ID AS GROUP_ID,w.CODE AS GROUP_CODE,w.NAME AS GROUP_NAME,ea.LOAN_AMOUNT AS TOTAL_LOAN_AMT,ld.MONTHLY_REPAYMENT_AMT AS REPAYMENT_PER_YEAR,(select (case when isnull((select coalesce(cast(sum(lld.ACTUAL_AMT) as decimal(30,1)),0) from (loan_ledger lld join ese_account ea on((ea.PROFILE_ID = lld.FARMER_ID))) where ((lld.TXN_TYPE = '702') and (lld.FARMER_ID = f.FARMER_ID) and (year(lld.TXN_TIME) = year(ll.TXN_TIME)) and (ea.LOAN_AMOUNT <> 0) and (ea.OUTSTANDING_AMOUNT <> 0) and (lld.FARMER_LOAN_BAL <> 0)) group by year(lld.TXN_TIME))) then 0 else (select coalesce(cast(sum(lld.ACTUAL_AMT) as decimal(30,1)),0) from (loan_ledger lld join ese_account ea on((ea.PROFILE_ID = lld.FARMER_ID))) where ((lld.TXN_TYPE = '702') and (lld.FARMER_ID = f.FARMER_ID) and (year(lld.TXN_TIME) = year(ll.TXN_TIME)) and (ea.LOAN_AMOUNT <> 0) and (ea.OUTSTANDING_AMOUNT <> 0) and (lld.FARMER_LOAN_BAL <> 0)) group by year(lld.TXN_TIME)) end)) AS AMOUNT_REPAID,cast((ld.MONTHLY_REPAYMENT_AMT - (select (case when isnull((select coalesce(cast(sum(lld.ACTUAL_AMT) as decimal(30,1)),0) from (loan_ledger lld join ese_account ea on((ea.PROFILE_ID = lld.FARMER_ID))) where ((lld.TXN_TYPE = '702') and (lld.FARMER_ID = f.FARMER_ID) and (year(lld.TXN_TIME) = year(ll.TXN_TIME)) and (ea.LOAN_AMOUNT <> 0) and (ea.OUTSTANDING_AMOUNT <> 0) and (lld.FARMER_LOAN_BAL <> 0)) group by year(lld.TXN_TIME))) then 0 else (select coalesce(cast(sum(lld.ACTUAL_AMT) as decimal(30,1)),0) from (loan_ledger lld join ese_account ea on((ea.PROFILE_ID = lld.FARMER_ID))) where ((lld.TXN_TYPE = '702') and (lld.FARMER_ID = f.FARMER_ID) and (year(lld.TXN_TIME) = year(ll.TXN_TIME)) and (ea.LOAN_AMOUNT <> 0) and (ea.OUTSTANDING_AMOUNT <> 0) and (lld.FARMER_LOAN_BAL <> 0)) group by year(lld.TXN_TIME)) end))) as decimal(30,1)) AS PENDING_REPAYMENT from (((((loan_distribution ld join farmer f on((f.ID in (ld.FARMER_ID,ld.GROUP_ID)))) join ese_account ea on((ea.PROFILE_ID = f.FARMER_ID))) join loan_ledger ll on((ll.FARMER_ID = f.FARMER_ID))) join village v on((v.ID = f.VILLAGE_ID))) join warehouse w on((w.ID = f.SAMITHI_ID))) where ((ea.LOAN_AMOUNT <> 0) and (ea.OUTSTANDING_AMOUNT <> 0) and (ll.FARMER_LOAN_BAL <> 0)) group by ld.FARMER_ID,ld.GROUP_ID,year(ll.TXN_TIME)


INSERT INTO locale_property VALUES (0, 'service.loanDistributionService', 'en', 'Loan Disbursement');
INSERT INTO locale_property VALUES (0, 'report.loanDistribution', 'en', 'Loan Disbursement Report');
INSERT INTO locale_property VALUES (0, 'info.loanDistributiongeneral', 'en', 'Loan Disbursement Information');
INSERT INTO locale_property VALUES (0, 'loanDate', 'en', 'Loan Date');
INSERT INTO locale_property VALUES (0, 'loanTo', 'en', 'Loan To');
INSERT INTO locale_property VALUES (0, 'loanCategory', 'en', 'Loan Category');
INSERT INTO locale_property VALUES (0, 'subsidyPercentage', 'en', 'Subsidy(%)');
INSERT INTO locale_property VALUES (0, 'costToFarmer', 'en', 'Cost to Farmer');
INSERT INTO locale_property VALUES (0, 'downPayment', 'en', 'Down Payment');
INSERT INTO locale_property VALUES (0, 'interestPercentage', 'en', 'Interest(%) p.a');
INSERT INTO locale_property VALUES (0, 'totalCostToFarmer', 'en', 'Total Loan Amount');
INSERT INTO locale_property VALUES (0, 'loanTenure', 'en', 'Loan Tenure');
INSERT INTO locale_property VALUES (0, 'monthlyLoanRepay', 'en', 'Total Repayment per Year');
INSERT INTO locale_property VALUES (0, 'totalAmt', 'en', 'Total Amount');
INSERT INTO locale_property VALUES (0, 'loanTenureLabel', 'en', 'Loan Tenure(Year)');
INSERT INTO locale_property VALUES (0, 'LoanDistributionAction.breadCrumb', 'en', 'Service~#,Loan Disbursement~loanDistribution_create.action');
INSERT INTO locale_property VALUES (0, 'loanInterest', 'en', 'Loan Deduction(%)');
INSERT INTO locale_property VALUES (0, 'finalPay', 'en', 'Final Amount');
INSERT INTO locale_property VALUES (0, 'repaymentAmt', 'en', 'Loan Repayment Amt(₹)');
INSERT INTO locale_property VALUES (0, 'loanDistributionSucess', 'en', 'Loan Disbursement Success');
INSERT INTO locale_property VALUES (0, 'info.loanDistribution', 'en', 'Loan Disbursement');
INSERT INTO locale_property VALUES (0, 'interestPercentageAmt', 'en', 'Interest(%) Amt');
INSERT INTO locale_property VALUES (0, 'interestPercentageAmt', 'en', 'Interest(%) Amt');
INSERT INTO locale_property VALUES (0, 'monthlyRepaymentAmt', 'en', 'Total Repayment Amt per Year');
INSERT INTO locale_property VALUES (0, 'subTotal', 'en', 'Sub Total');
INSERT INTO locale_property VALUES (0, 'loanTenureAmt', 'en', 'Loan Tenure Amt');
INSERT INTO locale_property VALUES (0, 'totalLoanAmount', 'en', 'Total Loan Amount');
INSERT INTO locale_property VALUES (0, 'loanpendingAmount', 'en', 'Total Principal Pending');
INSERT INTO locale_property VALUES (0, 'loanRepaymentAmount', 'en', 'Total Repayment Amount');
INSERT INTO locale_property VALUES (0, 'loanAccNo', 'en', 'Loan Acc No');
INSERT INTO locale_property VALUES (0, 'farmerGroupName', 'en', 'Farmer/Group Name');
INSERT INTO locale_property VALUES (0, 'farmerGroupCode', 'en', 'Farmer / Group Code');
INSERT INTO locale_property VALUES (0, 'loanAmt', 'en', 'Loan Amount');
INSERT INTO locale_property VALUES (0, 'loanBalance', 'en', 'Loan Balance');
INSERT INTO locale_property VALUES (0, 'loanRepaymentAmount', 'en', 'Total Repayment Amount');
INSERT INTO locale_property VALUES (0, 'exportColumnFarmerLoanStatementHeader', 'en', 'Loan Acc No, Farmer / Group Name,Farmer / Group Code,Village/Town, Loan Amount(₹), Loan Repayment Amount(₹), Loan Balance(₹)');
INSERT INTO locale_property VALUES (0, 'exportFarmerLoanStatementReportSubColumnHeader', 'en', 'Date,Receipt No,Description,Transaction Amount(₹)');
INSERT INTO locale_property VALUES (0, 'exportFarmerLoanStatementTitle', 'en', 'Farmer Loan Statement Report');
INSERT INTO locale_property VALUES (0, 'farmerLoanStatementReportList', 'en', 'FarmerLoanStatementReportList');
INSERT INTO locale_property VALUES (0, 'overDraftRepaymentAmt', 'en', 'Repayment Amount is Exceeding your Loan Amount');
INSERT INTO locale_property VALUES (0, 'exportColumnFarmerStatementHeader', 'en', 'Farmer Name/Group Name,Farmer Code/Group Code,Village/Town, Total Purchase Amount(₹), Total Repayment Amount(₹)');
INSERT INTO locale_property VALUES (0, 'exportFarmerStatementReportSubColumnHeader', 'en', 'Date,Receipt No,Description,Transaction Amount(₹)');
INSERT INTO locale_property VALUES (0, 'exportFarmerStatementTitle', 'en', 'Farmer Statement Report');
INSERT INTO locale_property VALUES (0, 'farmerStatementReportList', 'en', 'FarmerStatementReportList');
INSERT INTO locale_property VALUES (0, 'LoanDistributionReportAction.breadCrumb', 'en', 'Report~#,Loan Disbursement Report~loanDistributionReport_list.action');
INSERT INTO locale_property VALUES (0, 'loanDistributionReportList', 'en', 'Loan-Disbursement-Report');
INSERT INTO locale_property VALUES (0, 'exportColumnLoanDistributionHeader', 'en', 'Loan Date, Loan Acc No, Loan To, Group Name, Farmer Name, Village/Town, Loan Category, Total Quantity, Cost to Farmer(₹), Interest(%)p.a, Interest(%) Amt(₹), LoanTenure(Years), Loan Tenure Amt(₹), Total Cost(₹), Total Repayment Amt per year(₹)');
INSERT INTO locale_property VALUES (0, 'exportColumnLoanDistributionSubHeader', 'en', 'Product, Price/Unit(₹), Quantity(KG), Amount(₹), Subsidy Interest(%), Subsidy Amount(₹), Total Amount (₹)');
INSERT INTO locale_property VALUES (0, 'exportLoanDistributionTitle', 'en', 'Loan Disbursement Report');
INSERT INTO locale_property VALUES (0, 'service.loan', 'en', 'Loan Module');
INSERT INTO locale_property VALUES (0, 'report.loanReport', 'en', 'Loan Reports');
INSERT INTO locale_property VALUES (0, 'loanTenureAmt', 'en', 'Loan Tenure Amt');
INSERT INTO locale_property VALUES (0, 'totalLoanAmount', 'en', 'Total Loan Amount');
INSERT INTO locale_property VALUES (0, 'loanpendingAmount', 'en', 'Total Principal Pending');
INSERT INTO locale_property VALUES (0, 'loanRepaymentAmount', 'en', 'Total Repayment Amount');
INSERT INTO locale_property VALUES (0, 'loanAccNo', 'en', 'Loan Acc No');
INSERT INTO locale_property VALUES (0, 'farmerGroupName', 'en', 'Farmer/Group Name');
INSERT INTO locale_property VALUES (0, 'farmerGroupCode', 'en', 'Farmer / Group Code');
INSERT INTO locale_property VALUES (0, 'loanAmt', 'en', 'Loan Amount');
INSERT INTO locale_property VALUES (0, 'loanBalance', 'en', 'Loan Balance');
INSERT INTO locale_property VALUES (0, 'loanRepaymentAmount', 'en', 'Total Repayment Amount');
INSERT INTO locale_property VALUES (0, 'totalRepayment', 'en', 'Total Repayment');
INSERT INTO locale_property VALUES (0, 'totalPurchase', 'en', 'Total Purchase');
INSERT INTO locale_property VALUES (0, 'txnDesc', 'en', 'Description');
INSERT INTO locale_property VALUES (0, 'report.loanManagement', 'en', 'Loan Management Report');
INSERT INTO locale_property VALUES (0, 'totLoanAmt', 'en', 'Total Loan Amount(₹)');
INSERT INTO locale_property VALUES (0, 'repayPerYear', 'en ', 'Repayment Per Year(₹)');
INSERT INTO locale_property VALUES (0, 'amtRepaid', 'en', 'Amount Repaid(₹)');
INSERT INTO locale_property VALUES (0, 'pendingRepay', 'en', 'Pending Repayment(₹)');
INSERT INTO locale_property VALUES (0, 'loanRepayment', 'en', 'Loan Repayment');
INSERT INTO locale_property VALUES (0, 'farmerLoanAmt', 'en', 'Farmer Loan Amount');
INSERT INTO locale_property VALUES (0, 'farmerLoanBal', 'en', 'Farmer Loan Balance');
INSERT INTO locale_property VALUES (0, 'farmer.loanAmount', 'en', 'Farmer Loan Amount');
INSERT INTO locale_property VALUES (0, 'farmer.outstandingBal', 'en', 'Farmer Outstanding Amount');
INSERT INTO locale_property VALUES (0, 'error.downpaymentQty', 'en', 'Please Enter DownPayment Qty');
INSERT INTO locale_property VALUES (0, 'error.downpaymentAmt', 'en', 'Please Enter DownPayment Amt');
INSERT INTO locale_property VALUES (0, 'error.loanTenure', 'en', 'Please Enter Loan Tenure Year');
INSERT INTO locale_property VALUES (0, 'error.interestPercentage', 'en', 'Please Enter Interest %');
INSERT INTO locale_property VALUES (0, 'loanDeduction', 'en', 'Loan Deduction Percentage');
INSERT INTO locale_property VALUES (0, 'finalPayAmt', 'en', 'Final Payment Amt(₹)');
INSERT INTO locale_property VALUES (0, 'loanDeductionAmt', 'en', 'Loan Deduction Amount(₹)');

SET FOREIGN_KEY_CHECKS=1;



-- - April 16 2020 Agro
alter table loc_history CHANGE column ACCURACY  END_ADDRESS longtext, modify column ADDRESS LONGTEXT;


-- -20 APR 2020
ALTER TABLE `offline_procurement` 
ADD COLUMN `FINAL_PAY_AMT` double(20, 2) NULL AFTER `VEHICLE_TYPE`;

-- -27 APR 2020
alter table dynamic_fields_config 
add column VALUE_DEPENDENCY BIGINT(2) NULL DEFAULT 0 AFTER `GRADE`;

-- -08 May 2020 geo coordinates 

ALTER TABLE pmt
ADD COLUMN LATITUDE  varchar(50) NULL AFTER TRANSPORTER;

ALTER TABLE pmt
ADD COLUMN LONGITUDE  varchar(50) NULL AFTER LATITUDE;

ALTER TABLE offline_mtnt
ADD COLUMN LATITUDE  varchar(50) NULL AFTER MSG_NO;

ALTER TABLE offline_mtnt
ADD COLUMN LONGITUDE  varchar(50) NULL AFTER LATITUDE;

INSERT INTO dynamic_report_config_detail VALUES (NULL, 33, 'Map', 'pt.latitude|pt.longitude', 5, NULL, NULL, 0, 0, 100, 16, 1, 0, 1, NULL, NULL, NULL, NULL, '1');
INSERT INTO dynamic_report_config_detail VALUES (NULL, 26, 'Map', 'latitude|longitude', 5, NULL, NULL, 0, 0, 100, 26, 1, 0, 1, NULL, NULL, NULL, NULL, '1');

-- -- 11/05/2020

ALTER TABLE crop_supply
ADD COLUMN LATITUDE  varchar(50) NULL AFTER PO_NUMBER;

ALTER TABLE crop_supply
ADD COLUMN LONGITUDE  varchar(50) NULL AFTER LATITUDE;

INSERT INTO `dynamic_report_config_detail` VALUES (null, 22, 'Map', 'latitude|longitude', 5, NULL, NULL, 0, 0, 100, 23, 1, 0, 1, NULL, NULL, NULL, NULL, '1');

-- -- 12/05/2020
ALTER TABLE crop_harvest
ADD COLUMN LATITUDE  varchar(50) NULL after OTHER_PACKEDIN_TYPE;

ALTER TABLE crop_harvest
ADD COLUMN LONGITUDE  varchar(50) NULL AFTER LATITUDE;

INSERT INTO dynamic_report_config_detail VALUES (NULL, 29, 'Map', 'latitude|longitude', 5, NULL, NULL, 0, 0, 100, 15, 1, 0, 1, NULL, NULL, NULL, NULL, '1');


ALTER TABLE offline_distribution
ADD COLUMN LATITUDE  varchar(50) NULL after CURRENT_SEASON_CODE;

ALTER TABLE offline_distribution
ADD COLUMN LONGITUDE  varchar(50) NULL AFTER LATITUDE;


ALTER TABLE distribution
ADD COLUMN LATITUDE  varchar(50) NULL after SAMITHI_ID;

ALTER TABLE distribution
ADD COLUMN LONGITUDE  varchar(50) NULL AFTER LATITUDE;

INSERT INTO dynamic_report_config_detail VALUES (NULL, 34, 'Map', 'latitude|longitude', 5, NULL, NULL, 0, 0, 100, 15, 1, 0, 1, NULL, NULL, NULL, NULL, '1');

ALTER TABLE cultivation
ADD COLUMN LATITUDE  varchar(50) NULL after SAMITHI_CODE;

ALTER TABLE cultivation
ADD COLUMN LONGITUDE  varchar(50) NULL AFTER LATITUDE;

INSERT INTO dynamic_report_config_detail VALUES (NULL, 18, 'Map', 'latitude|longitude', 5, NULL, NULL, 0, 0, 100, 15, 1, 0, 1, NULL, NULL, NULL, NULL, '1');

-- -- 13/05/2020

INSERT INTO `eses_agro_new`.`dynamic_report_config_detail`(`ID`, `REPORT_CONFIG_ID`, `LABEL_NAME`, `FIELD`, `ACESS_TYPE`, `PARAMTERS`, `METHOD`, `GROUP_PROP`, `SUM_PROP`, `WIDTH`, `ORDERR`, `IS_GRID_AVAILABILITY`, `IS_EXPORT_AVAILABILITY`, `STATUSS`, `EXPRESSION`, `ALIGNMENT`, `IS_FOOTER_SUM`, `IS_GROUP_HEADER`, `DATA_TYPE`) VALUES (null, 16, 'Map', 'latitude|longitude', 5, NULL, NULL, 0, 0, 100, 12, 1, 0, 1, NULL, NULL, NULL, NULL, '1');
-- -- 14/05/2020
INSERT INTO `eses_agro_new`.`dynamic_report_config_detail`(`ID`, `REPORT_CONFIG_ID`, `LABEL_NAME`, `FIELD`, `ACESS_TYPE`, `PARAMTERS`, `METHOD`, `GROUP_PROP`, `SUM_PROP`, `WIDTH`, `ORDERR`, `IS_GRID_AVAILABILITY`, `IS_EXPORT_AVAILABILITY`, `STATUSS`, `EXPRESSION`, `ALIGNMENT`, `IS_FOOTER_SUM`, `IS_GROUP_HEADER`, `DATA_TYPE`) VALUES (null, 6, 'Map', 'latitude|longitude', 5, NULL, NULL, 0, 0, 100, 8, 1, 0, 1, NULL, NULL, NULL, NULL, NULL);

ALTER TABLE trxn_agro
ADD COLUMN LATITUDE  varchar(50) NULL after DISTRIBUTION_STOCK_ID;

ALTER TABLE trxn_agro
ADD COLUMN LONGITUDE  varchar(50) NULL AFTER LATITUDE;

-- - 15-05-2020

INSERT INTO `pref`(`ID`, `NAME`, `VAL`, `ESE_ID`, `DESCRIPTION`) VALUES (NULL, 'MSA_TRANSLATOR_TEXT_SUBSCRIPTION_KEY', '08d288480d9b47749843168613ae92a3', 1, NULL);
INSERT INTO `pref`(`ID`, `NAME`, `VAL`, `ESE_ID`, `DESCRIPTION`) VALUES (NULL, 'MSA_TRANSLATOR_TEXT_ENDPOINT', 'https://agronew.cognitiveservices.azure.com/', 1, NULL);

-- -- 18/05/2020
ALTER TABLE farmer_crop_prod_answers
ADD COLUMN LATITUDE  varchar(50) NULL after CERTIFICATION_STATUS;

ALTER TABLE farmer_crop_prod_answers
ADD COLUMN LONGITUDE  varchar(50) NULL AFTER LATITUDE;

-- - 22-05-2020

INSERT INTO `pref`(`ID`, `NAME`, `VAL`, `ESE_ID`, `DESCRIPTION`) VALUES (NULL, 'IBM_TRANSLATOR_TEXT_SUBSCRIPTION_API_KEY', 'YXBpa2V5Om1RaUd0andGUnRPWFFPWnh4UTM3T1YtMmE2QTNFbXVWSmsydDRXVF9RT3pI', 1, NULL);
INSERT INTO `pref`(`ID`, `NAME`, `VAL`, `ESE_ID`, `DESCRIPTION`) VALUES (NULL, 'IBM_TRANSLATOR_TEXT_ENDPOINT', 'https://api.eu-gb.language-translator.watson.cloud.ibm.com/instances/0ada1e27-7c2d-42d8-8408-cdb891425886/v3/translate?version=2018-05-01', 1, NULL);


UPDATE `pref` SET `NAME` = 'MSA_TRANSLATOR_TEXT_SUBSCRIPTION_KEY', `VAL` = '53099c2440ab408f9001679b4626307e', `ESE_ID` = 1, `DESCRIPTION` = NULL WHERE `NAME` = 'MSA_TRANSLATOR_TEXT_SUBSCRIPTION_KEY';
UPDATE `pref` SET `NAME` = 'MSA_TRANSLATOR_TEXT_ENDPOINT', `VAL` = 'https://api.cognitive.microsofttranslator.com', `ESE_ID` = 1, `DESCRIPTION` = NULL WHERE `NAME` = 'MSA_TRANSLATOR_TEXT_ENDPOINT';

