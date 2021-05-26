-- schema definition v1
--
-- Copyright 2021 Projeto e-cordel.
-- e-cordel project (http://www.ecordel.com.br/).

alter table public.cordel add column published boolean not null default false;

update public.cordel set published = true;
