CALL `AddColumnIfNotExistsAfterColumn`('99mall_inventory', 'stock_audit', 'stock_id', 'binary(16) not null', 'product_id');
CALL `AddForeignKeyIfNotExists`('99mall_inventory', 'stock_audit', 'stock_id', 'stock', 'id');

