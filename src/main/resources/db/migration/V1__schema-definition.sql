-- schema definition v1
--
-- Copyright 2020 Projeto e-cordel.
-- e-cordel project (http://www.ecordel.com.br/).

CREATE TABLE public.author (
    id bigint primary key,
    name character varying(255),
    about character varying(255),
    email character varying(255)
);

CREATE SEQUENCE public.author_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.author_id_seq OWNED BY public.author.id;

CREATE TABLE public.xilogravura (
    id bigint primary key,
    description character varying(255),
    url character varying(255),
    xilografo_id bigint,
    FOREIGN KEY (xilografo_id) REFERENCES author (id)
);

CREATE SEQUENCE public.xilogravura_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.xilogravura_id_seq OWNED BY public.xilogravura.id;

CREATE TABLE public.cordel (
    id bigint primary key,
    author_id bigint,
    content text,
    description character varying(255),
    title character varying(255),
    xilogravura_id bigint,
    FOREIGN KEY (author_id) REFERENCES author (id),
    FOREIGN KEY (xilogravura_id) REFERENCES xilogravura (id)
);

CREATE SEQUENCE public.cordel_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.cordel_id_seq OWNED BY public.cordel.id;

CREATE TABLE public.cordel_tags (
    cordel_id bigint NOT NULL,
    tags character varying(255)
);

CREATE TABLE public.cordel_user (
    id bigint NOT NULL,
    enabled boolean NOT NULL,
    password character varying(255),
    username character varying(255) UNIQUE
);

CREATE TABLE public.user_authority (
    user_id bigint NOT NULL,
    authority_id bigint NOT NULL,
    CONSTRAINT unique_user_authority UNIQUE (user_id, authority_id)
);

CREATE SEQUENCE public.cordel_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.cordel_user_id_seq OWNED BY public.cordel_user.id;

CREATE TABLE public.cordel_authority (
    id bigint NOT NULL,
    authority character varying(255)
);

CREATE SEQUENCE public.cordel_authority_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.cordel_authority_id_seq OWNED BY public.cordel_authority.id;

ALTER TABLE ONLY public.cordel ALTER COLUMN id SET DEFAULT nextval('public.cordel_id_seq'::regclass);

ALTER TABLE ONLY public.author ALTER COLUMN id SET DEFAULT nextval('public.author_id_seq'::regclass);

ALTER TABLE ONLY public.xilogravura ALTER COLUMN id SET DEFAULT nextval('public.xilogravura_id_seq'::regclass);

ALTER TABLE ONLY public.cordel_user ALTER COLUMN id SET DEFAULT nextval('public.cordel_user_id_seq'::regclass);

ALTER TABLE ONLY public.cordel_authority ALTER COLUMN id SET DEFAULT nextval('public.cordel_authority_id_seq'::regclass);
