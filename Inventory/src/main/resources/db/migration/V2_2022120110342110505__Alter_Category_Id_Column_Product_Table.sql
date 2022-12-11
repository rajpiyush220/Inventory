CALL `DropForeignKeyIfExists`('FK_product_product_category', 'product');
CALL `DropForeignKeyIfExists`('product_ibfk_1', 'product');
CALL `AlterColumnIfExists`('99mall_inventory', 'product', 'category_id', 'binary(16) NOT NULL');
CALL `AddForeignKeyIfNotExists`('99mall_inventory', 'product', 'category_id', 'product_category', 'id');

