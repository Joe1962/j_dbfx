CREATE TABLE public.sys_field_titles (
	"uuid" uuid DEFAULT uuid_generate_v4() NOT NULL,
	"table_name" varchar(64) NOT NULL,
	"field_order" int NOT NULL,
	"field_name" varchar(64) NOT NULL,
	"field_title" varchar(64),
	"field_datatype" varchar(16) NOT NULL,
	"field_prefwidth" int,
	"field_subtype" varchar(16),
	CONSTRAINT pk_sys_field_titles PRIMARY KEY (table_name, field_order),
	CONSTRAINT uq_sys_field_titles_names UNIQUE (table_name, field_name)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.sys_field_titles OWNER to siguapa_owner;
GRANT ALL ON TABLE public.sys_field_titles TO siguapa_owner;

CREATE INDEX idx_sys_field_titles_name ON public.sys_field_titles USING btree (table_name, field_name);

