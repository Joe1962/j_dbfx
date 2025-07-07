INSERT INTO public.sys_field_titles (table_name, field_order, field_name, field_title, field_prefwidth, field_datatype, field_subtype) VALUES ('sys_users', 1, 'name', 'Nombre', 350, 'String', null);
INSERT INTO public.sys_field_titles (table_name, field_order, field_name, field_title, field_prefwidth, field_datatype, field_subtype) VALUES ('sys_users', 2, 'admin', 'Admin.', 75, 'boolean', null);

-- INSERT INTO public.sys_field_titles (table_name, field_order, field_name, field_title, field_prefwidth, field_datatype, field_subtype) VALUES ('sys_cost_areas', 1, 'id', 'Cód.', 100, 'int', null);
-- INSERT INTO public.sys_field_titles (table_name, field_order, field_name, field_title, field_prefwidth, field_datatype, field_subtype) VALUES ('sys_cost_areas', 2, 'name', 'Nombre', 450, 'String', null);
-- INSERT INTO public.sys_field_titles (table_name, field_order, field_name, field_title, field_prefwidth, field_datatype, field_subtype) VALUES ('sys_cost_centers', 1, 'id', 'ID.', 50, 'int', null);
-- INSERT INTO public.sys_field_titles (table_name, field_order, field_name, field_title, field_prefwidth, field_datatype, field_subtype) VALUES ('sys_cost_centers', 2, 'name', 'Nombre', 450, 'String', null);
-- INSERT INTO public.sys_field_titles (table_name, field_order, field_name, field_title, field_prefwidth, field_datatype, field_subtype) VALUES ('sys_cost_centers', 3, 'area_id', 'Area', 150, 'String', null);
-- INSERT INTO public.sys_field_titles (table_name, field_order, field_name, field_title, field_prefwidth, field_datatype, field_subtype) VALUES ('sys_cost_centers', 5, 'has_sales', 'Ventas', 75, 'boolean', null);
-- INSERT INTO public.sys_field_titles (table_name, field_order, field_name, field_title, field_prefwidth, field_datatype, field_subtype) VALUES ('sys_cost_centers', 6, 'is_almacen', 'Almacén', 75, 'boolean', null);
-- INSERT INTO public.sys_field_titles (table_name, field_order, field_name, field_title, field_prefwidth, field_datatype, field_subtype) VALUES ('sys_cost_centers', 4, 'has_inventory', 'Inventario', 100, 'boolean', null);

-- INSERT INTO public.sys_field_titles (table_name, field_order, field_name, field_title, field_prefwidth, field_datatype, field_subtype) VALUES ('sys_um', 1, 'id', 'Cód.', 50, 'int', null);
-- INSERT INTO public.sys_field_titles (table_name, field_order, field_name, field_title, field_prefwidth, field_datatype, field_subtype) VALUES ('sys_um', 2, 'name', 'Nombre', 150, 'String', null);
-- INSERT INTO public.sys_field_titles (table_name, field_order, field_name, field_title, field_prefwidth, field_datatype, field_subtype) VALUES ('sys_um', 3, 'cfactor', 'Factor', 100, 'BigDecimal', 'numeric');
-- INSERT INTO public.sys_field_titles (table_name, field_order, field_name, field_title, field_prefwidth, field_datatype, field_subtype) VALUES ('sys_um', 4, 'abrev', 'Siglas', 100, 'String', null);

-- INSERT INTO public.sys_field_titles (table_name, field_order, field_name, field_title, field_prefwidth, field_datatype, field_subtype) VALUES ('sys_cost_menus', 1, 'id', 'Cód.', 50, 'int', null);
-- INSERT INTO public.sys_field_titles (table_name, field_order, field_name, field_title, field_prefwidth, field_datatype, field_subtype) VALUES ('sys_cost_menus', 2, 'name', 'Nombre', 250, 'String', null);
-- INSERT INTO public.sys_field_titles (table_name, field_order, field_name, field_title, field_prefwidth, field_datatype, field_subtype) VALUES ('sys_cost_menus', 3, 'activo', 'Activo', 75, 'boolean', null);

-- INSERT INTO public.sys_field_titles (table_name, field_order, field_name, field_title, field_prefwidth, field_datatype, field_subtype) VALUES ('inv_generics', 1, 'uuid', null, 0, 'String', null);
-- INSERT INTO public.sys_field_titles (table_name, field_order, field_name, field_title, field_prefwidth, field_datatype, field_subtype) VALUES ('inv_generics', 2, 'name', 'Nombre', 200, 'String', null);
-- INSERT INTO public.sys_field_titles (table_name, field_order, field_name, field_title, field_prefwidth, field_datatype, field_subtype) VALUES ('inv_generics', 3, 'description', 'Descripción', 350, 'String', 'TextArea');
-- INSERT INTO public.sys_field_titles (table_name, field_order, field_name, field_title, field_prefwidth, field_datatype, field_subtype) VALUES ('inv_generics', 4, 'invType', 'Tipo', 100, 'combobox', null);
-- INSERT INTO public.sys_field_titles (table_name, field_order, field_name, field_title, field_prefwidth, field_datatype, field_subtype) VALUES ('inv_generics', 5, 'sysUmBean', 'U.M.', 100, 'combobox', null);
-- INSERT INTO public.sys_field_titles (table_name, field_order, field_name, field_title, field_prefwidth, field_datatype, field_subtype) VALUES ('inv_generics', 6, 'presentation', 'Present.', 75, 'BigDecimal', 'numeric');
-- INSERT INTO public.sys_field_titles (table_name, field_order, field_name, field_title, field_prefwidth, field_datatype, field_subtype) VALUES ('inv_generics', 7, 'ingredient', 'Ingred.', 75, 'boolean', null);
-- INSERT INTO public.sys_field_titles (table_name, field_order, field_name, field_title, field_prefwidth, field_datatype, field_subtype) VALUES ('inv_generics', 8, 'subprod', 'Subprod.', 75, 'boolean', null);

-- INSERT INTO public.sys_field_titles (table_name, field_order, field_name, field_title, field_prefwidth, field_datatype, field_subtype) VALUES ('inv_productos', 1, 'uuid', null, 0, 'String', null);
-- INSERT INTO public.sys_field_titles (table_name, field_order, field_name, field_title, field_prefwidth, field_datatype, field_subtype) VALUES ('inv_productos', 3, 'name', 'Nombre', 200, 'String', null);
-- INSERT INTO public.sys_field_titles (table_name, field_order, field_name, field_title, field_prefwidth, field_datatype, field_subtype) VALUES ('inv_productos', 2, 'invGeneric', 'Genérico', 200, 'combobox', null);
-- INSERT INTO public.sys_field_titles (table_name, field_order, field_name, field_title, field_prefwidth, field_datatype, field_subtype) VALUES ('inv_productos', 4, 'description', 'Descripción', 250, 'String', 'TextArea');
-- INSERT INTO public.sys_field_titles (table_name, field_order, field_name, field_title, field_prefwidth, field_datatype, field_subtype) VALUES ('inv_productos', 5, 'invType', 'Tipo', 200, 'combobox', null);
-- INSERT INTO public.sys_field_titles (table_name, field_order, field_name, field_title, field_prefwidth, field_datatype, field_subtype) VALUES ('inv_productos', 6, 'sysUmBean', 'U.M.', 100, 'combobox', null);
-- INSERT INTO public.sys_field_titles (table_name, field_order, field_name, field_title, field_prefwidth, field_datatype, field_subtype) VALUES ('inv_productos', 7, 'presentation', 'Present.', 75, 'BigDecimal', 'numeric');
-- INSERT INTO public.sys_field_titles (table_name, field_order, field_name, field_title, field_prefwidth, field_datatype, field_subtype) VALUES ('inv_productos', 8, 'ingredient', 'Ingred.', 75, 'boolean', null);
-- INSERT INTO public.sys_field_titles (table_name, field_order, field_name, field_title, field_prefwidth, field_datatype, field_subtype) VALUES ('inv_productos', 9, 'subprod', 'Subprod.', 75, 'boolean', null);

-- INSERT INTO public.sys_field_titles (table_name, field_order, field_name, field_title, field_prefwidth, field_datatype, field_subtype) VALUES ('inv_ingredients', 1, 'uuid', null, 0, 'uuid', null);
-- INSERT INTO public.sys_field_titles (table_name, field_order, field_name, field_title, field_prefwidth, field_datatype, field_subtype) VALUES ('inv_ingredients', 2, 'name', 'Nombre', 200, 'String', null);
-- INSERT INTO public.sys_field_titles (table_name, field_order, field_name, field_title, field_prefwidth, field_datatype, field_subtype) VALUES ('inv_ingredients', 3, 'description', 'Descripción', 225, 'String', 'TextArea');
-- INSERT INTO public.sys_field_titles (table_name, field_order, field_name, field_title, field_prefwidth, field_datatype, field_subtype) VALUES ('inv_ingredients', 4, 'invType', 'Tipo', 150, 'combobox', null);
-- INSERT INTO public.sys_field_titles (table_name, field_order, field_name, field_title, field_prefwidth, field_datatype, field_subtype) VALUES ('inv_ingredients', 5, 'sysUm', 'UM', 100, 'combobox', null);

-- INSERT INTO public.sys_field_titles (table_name, field_order, field_name, field_title, field_prefwidth, field_datatype, field_subtype) VALUES ('inv_types', 1, 'cod', '#', 50, 'int', null);
-- INSERT INTO public.sys_field_titles (table_name, field_order, field_name, field_title, field_prefwidth, field_datatype, field_subtype) VALUES ('inv_types', 2, 'name', 'Nombre', 250, 'String', null);

-- INSERT INTO public.sys_field_titles (table_name, field_order, field_name, field_title, field_prefwidth, field_datatype, field_subtype) VALUES ('inv_mov_types', 1, 'cod', '#', 50, 'int', null);
-- INSERT INTO public.sys_field_titles (table_name, field_order, field_name, field_title, field_prefwidth, field_datatype, field_subtype) VALUES ('inv_mov_types', 2, 'name', 'Nombre.', 250, 'String', null);

-- INSERT INTO public.sys_field_titles (table_name, field_order, field_name, field_title, field_prefwidth, field_datatype, field_subtype) VALUES ('menu_master', 1, 'id', 'Cód.', 60, 'int', null);
-- INSERT INTO public.sys_field_titles (table_name, field_order, field_name, field_title, field_prefwidth, field_datatype, field_subtype) VALUES ('menu_master', 2, 'name', 'Nombre', 150, 'String', null);
-- INSERT INTO public.sys_field_titles (table_name, field_order, field_name, field_title, field_prefwidth, field_datatype, field_subtype) VALUES ('menu_master', 3, 'description', 'Descripción', 150, 'String', 'TextArea');
-- INSERT INTO public.sys_field_titles (table_name, field_order, field_name, field_title, field_prefwidth, field_datatype, field_subtype) VALUES ('menu_master', 4, 'sysMenuGroup', 'Grupo menú', 100, 'combobox', null);
-- INSERT INTO public.sys_field_titles (table_name, field_order, field_name, field_title, field_prefwidth, field_datatype, field_subtype) VALUES ('menu_master', 5, 'priceCuc', 'Precio CUC', 100, 'BigDecimal', 'currency');
-- INSERT INTO public.sys_field_titles (table_name, field_order, field_name, field_title, field_prefwidth, field_datatype, field_subtype) VALUES ('menu_master', 6, 'priceCup', 'Precio CUP', 100, 'BigDecimal', 'currency');
-- INSERT INTO public.sys_field_titles (table_name, field_order, field_name, field_title, field_prefwidth, field_datatype, field_subtype) VALUES ('menu_master', 7, 'activo', 'Activo', 75, 'boolean', null);
-- INSERT INTO public.sys_field_titles (table_name, field_order, field_name, field_title, field_prefwidth, field_datatype, field_subtype) VALUES ('menu_master', 8, 'invCaja', 'Inventario', 85, 'boolean', null);
-- INSERT INTO public.sys_field_titles (table_name, field_order, field_name, field_title, field_prefwidth, field_datatype, field_subtype) VALUES ('menu_master', 9, 'variable', 'Variable', 75, 'boolean', null);

-- INSERT INTO public.sys_field_titles (table_name, field_order, field_name, field_title, field_prefwidth, field_datatype, field_subtype) VALUES ('menu_detail', 1, 'uuid', null, 0, 'uuid', null);
-- INSERT INTO public.sys_field_titles (table_name, field_order, field_name, field_title, field_prefwidth, field_datatype, field_subtype) VALUES ('menu_detail', 2, 'menuMaster', 'Nombre', 150, 'String', null);
-- INSERT INTO public.sys_field_titles (table_name, field_order, field_name, field_title, field_prefwidth, field_datatype, field_subtype) VALUES ('menu_detail', 3, 'ingredient', 'Ingrediente', 150, 'combobox', null);
-- INSERT INTO public.sys_field_titles (table_name, field_order, field_name, field_title, field_prefwidth, field_datatype, field_subtype) VALUES ('menu_detail', 4, 'quant', 'Cant.', 75, 'BigDecimal', 'numeric');
-- INSERT INTO public.sys_field_titles (table_name, field_order, field_name, field_title, field_prefwidth, field_datatype, field_subtype) VALUES ('menu_detail', 5, 'dateFrom', 'Desde', 100, 'Date', null);
-- INSERT INTO public.sys_field_titles (table_name, field_order, field_name, field_title, field_prefwidth, field_datatype, field_subtype) VALUES ('menu_detail', 6, 'dateUntil', 'Hasta', 100, 'date', null);
