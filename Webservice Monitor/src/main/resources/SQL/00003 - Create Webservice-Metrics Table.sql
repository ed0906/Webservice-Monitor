CREATE TABLE `service_monitor`.`webservice_metrics` (
  `name` VARCHAR(255) NOT NULL,
  `status` INT(3) NOT NULL,
  `response_time` INT(14) NOT NULL,
  `date` TIMESTAMP NOT NULL,
  PRIMARY KEY (`name`, `date`));