-- schema definition v1
--
-- Copyright 2021 Projeto e-cordel.
-- e-cordel project (http://www.ecordel.com.br/).

ALTER TABLE public.author ALTER COLUMN about TYPE text;

ALTER TABLE public.author ALTER COLUMN name SET NOT NULL;
