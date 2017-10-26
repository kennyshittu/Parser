
MySQL schema used for the log data

CREATE TABLE `LOG_ROW` (
  `id` BIGINT unsigned AUTO_INCREMENT,
  `status` INT NOT NULL,
  `ip` VARCHAR(20) NOT NULL,
  `request` VARCHAR(20) NOT NULL,
  `useragent` VARCHAR(255) NOT NULL,
  `startdate` TIMESTAMP NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `blocked_address` (
  `id` BIGINT unsigned AUTO_INCREMENT,
  `ip` VARCHAR(20) NOT NULL,
  `comment` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`)
);