-- schema definition v1
--
-- Copyright 2020 Projeto e-cordel.
-- e-cordel project (http://www.ecordel.com.br/).

CREATE TABLE public.author (
    id bigint primary key,
    name character varying(255) not null ,
    about text,
    email character varying(255)
);

CREATE SEQUENCE public.author_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.author_id_seq OWNED BY public.author.id;

CREATE TABLE public.cordel (
    id bigint primary key,
    author_id bigint,
    content text,
    description character varying(255),
    title character varying(255),
    published boolean not null default false,
    xilogravura_url character varying(255),
    FOREIGN KEY (author_id) REFERENCES author (id)
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

ALTER TABLE ONLY public.cordel_user ALTER COLUMN id SET DEFAULT nextval('public.cordel_user_id_seq'::regclass);

ALTER TABLE ONLY public.cordel_authority ALTER COLUMN id SET DEFAULT nextval('public.cordel_authority_id_seq'::regclass);

-- token

CREATE TABLE IF NOT EXISTS jwt_token (
    id bigint primary key,
    expiration bigint,
    secret_key TEXT,
    active boolean not null
);

CREATE SEQUENCE IF NOT EXISTS  jwt_token_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.jwt_token_id_seq OWNED BY jwt_token.id;

ALTER TABLE ONLY jwt_token ALTER COLUMN id SET DEFAULT nextval('public.jwt_token_id_seq'::regclass);

INSERT INTO jwt_token (id, expiration, secret_key, active) VALUES ((SELECT nextval('public.jwt_token_id_seq')), 86400000, 'rm''!@N=Ke!~p8VTA2ZRK~nMDQX5Uvm!m''D&]{@Vr?G;2?XhbC:Qa#9#eMLN\}x3?JR3.2zr~v)gYF^8\:8>:XfB:Ww75N/emt9Yj[bQMNCWwW\J?N,nvH.<2\.r~w]*e~vgak)X"v8H`MH/7"2E`,^k@n<vE-wD3g9JWPy;CrY*.Kd2_D])=><D?YhBaSua5hW%{2]_FVXzb9`8FH^b[X3jzVER&:jw2<=c38=>L/zBq`}C6tT*cCSVC^c]-L}&/', true);
