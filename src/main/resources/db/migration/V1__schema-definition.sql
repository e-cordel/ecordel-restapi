
CREATE TABLE public.cordel (
    id bigint NOT NULL,
    author character varying(255),
    content character varying(255),
    description character varying(255),
    title character varying(255),
    xilogravura character varying(255)
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

CREATE TABLE public.ecordel_user (
    id bigint NOT NULL,
    enabled boolean NOT NULL,
    password character varying(255),
    username character varying(255)
);

CREATE TABLE public.ecordel_user_authorities (
    ecorderl_user_id bigint NOT NULL,
    authorities_id bigint NOT NULL
);

CREATE SEQUENCE public.ecordel_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.ecordel_user_id_seq OWNED BY public.ecordel_user.id;

CREATE TABLE public.ecorderl_authority (
    id bigint NOT NULL,
    authority character varying(255)
);

CREATE SEQUENCE public.ecorderl_authority_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.ecorderl_authority_id_seq OWNED BY public.ecorderl_authority.id;

ALTER TABLE ONLY public.cordel ALTER COLUMN id SET DEFAULT nextval('public.cordel_id_seq'::regclass);

ALTER TABLE ONLY public.ecordel_user ALTER COLUMN id SET DEFAULT nextval('public.ecordel_user_id_seq'::regclass);

ALTER TABLE ONLY public.ecorderl_authority ALTER COLUMN id SET DEFAULT nextval('public.ecorderl_authority_id_seq'::regclass);