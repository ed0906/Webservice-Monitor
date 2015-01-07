CREATE TABLE `service_monitor`.`webservice_link` (
  `name_1` VARCHAR(255) NOT NULL,
  `name_2` VARCHAR(255) NOT NULL,
  `severity` INT(1) NULL,
  PRIMARY KEY (`name_1`, `name_2`));