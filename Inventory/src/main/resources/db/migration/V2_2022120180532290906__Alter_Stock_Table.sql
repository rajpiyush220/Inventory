
DROP TABLE IF EXISTS `stock_audit` ;
DROP TABLE IF EXISTS `stock` ;

CREATE TABLE IF NOT EXISTS `stock` (
  `id` BINARY(16) NOT NULL,
  `created` DATETIME(6) NOT NULL,
  `updated` DATETIME(6) NOT NULL,
  `version` BIGINT NOT NULL ,
  `product_id` binary(16) NOT NULL,
  `quantity` BIGINT DEFAULT 0,
  PRIMARY KEY (`id`),
  FOREIGN KEY `FK_stock_product` (product_id) REFERENCES product(id));

CREATE TABLE IF NOT EXISTS `stock_audit` (
  `id` BINARY(16) NOT NULL,
  `created` DATETIME(6) NOT NULL,
  `version` BIGINT NOT NULL,
  `product_id` binary(16) NOT NULL,
  `stock_id`  binary(16) NOT NULL,
  `quantity` BIGINT DEFAULT 0,
  `operation_type` VARCHAR(50) NOT NULL,
  `operated_by` VARCHAR(50) NOT NULL,
  `transaction_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  FOREIGN KEY `FK_stock_audit_product` (product_id) REFERENCES product(id),
  FOREIGN KEY `FK_stock_audit_stock` (stock_id) REFERENCES stock(id));