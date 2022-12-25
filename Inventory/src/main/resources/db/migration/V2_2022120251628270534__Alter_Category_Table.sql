CALL `AlterColumnIfExists`('99mall_inventory', 'category', 'sub_category', 'varchar(255) NOT NULL');
CALL `DropIndexIfExists`('category', 'UQ_product_category_category');
CALL `CreateIndex`('99mall_inventory', 'category', 'UQ_category_sub_category', 'category,sub_category');



