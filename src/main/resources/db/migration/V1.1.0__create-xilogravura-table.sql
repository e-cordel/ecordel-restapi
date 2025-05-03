-- schema definition v1.1.0
--
-- Copyright 2025 Projeto e-cordel.
-- e-cordel project (https://www.ecordel.com.br/).

CREATE TABLE public.xilogravura (
    id bigint primary key,
    url character varying(255) not null,
    title character varying(255),
    description character varying(255)
);

COMMENT ON COLUMN public.xilogravura.id IS 'Primary key, unique identifier for xilogravura records';
COMMENT ON COLUMN public.xilogravura.url IS 'URL of the xilogravura, cannot be null';
COMMENT ON COLUMN public.xilogravura.title IS 'Title of the xilogravura';
COMMENT ON COLUMN public.xilogravura.description IS 'Description of the xilogravura. It will be used for accessibility purposes';

CREATE SEQUENCE public.xilogravura_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.xilogravura_id_seq OWNED BY public.xilogravura.id;

ALTER TABLE ONLY public.xilogravura ALTER COLUMN id SET DEFAULT nextval('public.xilogravura_id_seq'::regclass);

-- Migration script to generate xilogravura entries from cordel table
INSERT INTO public.xilogravura (id, url)
SELECT nextval('public.xilogravura_id_seq'), xilogravura_url
FROM (
    SELECT DISTINCT xilogravura_url
    FROM public.cordel
    WHERE xilogravura_url IS NOT NULL
    AND xilogravura_url != ''
) AS distinct_urls;

-- Add xilogravura_id to cordel table
ALTER TABLE public.cordel
ADD COLUMN xilogravura_id bigint;

ALTER TABLE public.cordel
ADD CONSTRAINT cordel_xilogravura_fkey
FOREIGN KEY (xilogravura_id)
REFERENCES public.xilogravura (id);

-- update cordel table with xilogravura_id based on xilogravura_url
UPDATE public.cordel c set xilogravura_id =
    (SELECT x.id
     FROM public.xilogravura x
     WHERE x.url = c.xilogravura_url
     LIMIT 1)
WHERE c.xilogravura_url IS NOT NULL;

-- The cordel.xilogravura_url column will be kept for backward compatibility.
-- It will be removed in a future migration.

-- undo changes
--ALTER TABLE public.cordel DROP COLUMN xilogravura_id;
--DROP TABLE public.xilogravura;
--DELETE FROM public.flyway_schema_history where version = '1.1.0';