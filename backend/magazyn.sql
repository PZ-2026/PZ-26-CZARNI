--
-- PostgreSQL database dump
--

\restrict xrVoMFh21PIQFkc0FRpUYqdPyxU7XM0sOn4r6BNRp9bGhV4Y9gKuFiZky2X0mwv

-- Dumped from database version 12.22 (Ubuntu 12.22-0ubuntu0.20.04.4)
-- Dumped by pg_dump version 17.6

-- Started on 2026-06-15 14:47:12

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 7 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: postgres
--

-- *not* creating schema, since initdb creates it


ALTER SCHEMA public OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 219 (class 1259 OID 17811)
-- Name: dane_finansowe; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.dane_finansowe (
    id integer NOT NULL,
    data timestamp(6) without time zone NOT NULL,
    id_zamowienia integer,
    przychody numeric(12,2) NOT NULL,
    typ character varying(255),
    wydatki numeric(12,2) NOT NULL,
    zysk numeric(12,2)
);


ALTER TABLE public.dane_finansowe OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 17809)
-- Name: dane_finansowe_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.dane_finansowe_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.dane_finansowe_id_seq OWNER TO postgres;

--
-- TOC entry 3099 (class 0 OID 0)
-- Dependencies: 218
-- Name: dane_finansowe_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.dane_finansowe_id_seq OWNED BY public.dane_finansowe.id;


--
-- TOC entry 202 (class 1259 OID 17571)
-- Name: dostawcy; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.dostawcy (
    id integer NOT NULL,
    nazwa_dostawcy text NOT NULL,
    adres text NOT NULL,
    telefon character varying(15) NOT NULL
);


ALTER TABLE public.dostawcy OWNER TO postgres;

--
-- TOC entry 203 (class 1259 OID 17577)
-- Name: dostawcy_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.dostawcy_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.dostawcy_id_seq OWNER TO postgres;

--
-- TOC entry 3100 (class 0 OID 0)
-- Dependencies: 203
-- Name: dostawcy_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.dostawcy_id_seq OWNED BY public.dostawcy.id;


--
-- TOC entry 221 (class 1259 OID 17819)
-- Name: konfiguracja_systemu; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.konfiguracja_systemu (
    id integer NOT NULL,
    aktywna boolean NOT NULL,
    nazwa_parametru character varying(100) NOT NULL,
    opis text,
    typ_parametru character varying(50),
    wartosc_parametru text
);


ALTER TABLE public.konfiguracja_systemu OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 17817)
-- Name: konfiguracja_systemu_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.konfiguracja_systemu_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.konfiguracja_systemu_id_seq OWNER TO postgres;

--
-- TOC entry 3101 (class 0 OID 0)
-- Dependencies: 220
-- Name: konfiguracja_systemu_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.konfiguracja_systemu_id_seq OWNED BY public.konfiguracja_systemu.id;


--
-- TOC entry 216 (class 1259 OID 17747)
-- Name: produkty; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.produkty (
    id integer NOT NULL,
    nazwa_produktu text NOT NULL,
    opis_produktu text,
    kod_kreskowy text NOT NULL,
    cena numeric(38,2) NOT NULL,
    id_dostawcy integer NOT NULL,
    jednostka character varying DEFAULT 'szt'::character varying NOT NULL,
    strefa text DEFAULT 'A-01'::text NOT NULL
);


ALTER TABLE public.produkty OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 17753)
-- Name: produkty_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.produkty_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.produkty_id_seq OWNER TO postgres;

--
-- TOC entry 3102 (class 0 OID 0)
-- Dependencies: 217
-- Name: produkty_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.produkty_id_seq OWNED BY public.produkty.id;


--
-- TOC entry 204 (class 1259 OID 17587)
-- Name: sesje; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sesje (
    id bigint NOT NULL,
    data_utworzenia timestamp(6) without time zone NOT NULL,
    data_wygasniecia timestamp(6) without time zone NOT NULL,
    token character varying(255) NOT NULL,
    uzytkownik_id integer NOT NULL
);


ALTER TABLE public.sesje OWNER TO postgres;

--
-- TOC entry 205 (class 1259 OID 17590)
-- Name: sesje_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.sesje_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.sesje_id_seq OWNER TO postgres;

--
-- TOC entry 3103 (class 0 OID 0)
-- Dependencies: 205
-- Name: sesje_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.sesje_id_seq OWNED BY public.sesje.id;


--
-- TOC entry 214 (class 1259 OID 17725)
-- Name: stan_magazynu; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.stan_magazynu (
    id integer NOT NULL,
    id_produktu integer NOT NULL,
    ilosc numeric(12,2) NOT NULL
);


ALTER TABLE public.stan_magazynu OWNER TO postgres;

--
-- TOC entry 215 (class 1259 OID 17731)
-- Name: stan_magazynu_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.stan_magazynu_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.stan_magazynu_id_seq OWNER TO postgres;

--
-- TOC entry 3104 (class 0 OID 0)
-- Dependencies: 215
-- Name: stan_magazynu_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.stan_magazynu_id_seq OWNED BY public.stan_magazynu.id;


--
-- TOC entry 206 (class 1259 OID 17600)
-- Name: uzytkownicy; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.uzytkownicy (
    id integer NOT NULL,
    imie character varying(255) NOT NULL,
    nazwisko character varying(255) NOT NULL,
    telefon character varying(15) NOT NULL,
    email character varying(255) NOT NULL,
    haslo character varying(255) NOT NULL,
    rola integer NOT NULL,
    firma character varying(100),
    nip character varying(10),
    zablokowany boolean
);


ALTER TABLE public.uzytkownicy OWNER TO postgres;

--
-- TOC entry 207 (class 1259 OID 17606)
-- Name: uzytkownicy_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.uzytkownicy_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.uzytkownicy_id_seq OWNER TO postgres;

--
-- TOC entry 3105 (class 0 OID 0)
-- Dependencies: 207
-- Name: uzytkownicy_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.uzytkownicy_id_seq OWNED BY public.uzytkownicy.id;


--
-- TOC entry 208 (class 1259 OID 17608)
-- Name: zamowienia_klienci; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.zamowienia_klienci (
    id integer NOT NULL,
    data timestamp with time zone DEFAULT now() NOT NULL,
    id_klienta integer NOT NULL,
    id_magazyniera integer,
    status integer NOT NULL
);


ALTER TABLE public.zamowienia_klienci OWNER TO postgres;

--
-- TOC entry 209 (class 1259 OID 17612)
-- Name: zamowienia_klienci_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.zamowienia_klienci_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.zamowienia_klienci_id_seq OWNER TO postgres;

--
-- TOC entry 3106 (class 0 OID 0)
-- Dependencies: 209
-- Name: zamowienia_klienci_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.zamowienia_klienci_id_seq OWNED BY public.zamowienia_klienci.id;


--
-- TOC entry 210 (class 1259 OID 17614)
-- Name: zamowienia_produkty_dostawcy_laczaca; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.zamowienia_produkty_dostawcy_laczaca (
    id_produktu integer NOT NULL,
    id_zamowienia integer NOT NULL,
    ilosc integer NOT NULL,
    CONSTRAINT zamowienia_produkty_dostawcy_laczaca_ilosc_check CHECK ((ilosc > 0))
);


ALTER TABLE public.zamowienia_produkty_dostawcy_laczaca OWNER TO postgres;

--
-- TOC entry 211 (class 1259 OID 17618)
-- Name: zamowienia_produkty_klienci_laczaca; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.zamowienia_produkty_klienci_laczaca (
    id_zamowienia integer NOT NULL,
    id_produktu integer NOT NULL,
    ilosc integer NOT NULL,
    cena_w_dniu_zakupu numeric(12,2),
    CONSTRAINT zamowienia_produkty_klienci_laczaca_ilosc_check CHECK ((ilosc > 0))
);


ALTER TABLE public.zamowienia_produkty_klienci_laczaca OWNER TO postgres;

--
-- TOC entry 212 (class 1259 OID 17622)
-- Name: zamowienia_zaopatrzeniowiec; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.zamowienia_zaopatrzeniowiec (
    id integer NOT NULL,
    data timestamp with time zone DEFAULT now() NOT NULL,
    id_dostawcy integer NOT NULL,
    id_uzytkownika integer NOT NULL,
    status integer NOT NULL
);


ALTER TABLE public.zamowienia_zaopatrzeniowiec OWNER TO postgres;

--
-- TOC entry 213 (class 1259 OID 17626)
-- Name: zamowienia_zaopatrzeniowiec_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.zamowienia_zaopatrzeniowiec_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.zamowienia_zaopatrzeniowiec_id_seq OWNER TO postgres;

--
-- TOC entry 3107 (class 0 OID 0)
-- Dependencies: 213
-- Name: zamowienia_zaopatrzeniowiec_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.zamowienia_zaopatrzeniowiec_id_seq OWNED BY public.zamowienia_zaopatrzeniowiec.id;


--
-- TOC entry 2902 (class 2604 OID 17814)
-- Name: dane_finansowe id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.dane_finansowe ALTER COLUMN id SET DEFAULT nextval('public.dane_finansowe_id_seq'::regclass);


--
-- TOC entry 2891 (class 2604 OID 17755)
-- Name: dostawcy id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.dostawcy ALTER COLUMN id SET DEFAULT nextval('public.dostawcy_id_seq'::regclass);


--
-- TOC entry 2903 (class 2604 OID 17822)
-- Name: konfiguracja_systemu id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.konfiguracja_systemu ALTER COLUMN id SET DEFAULT nextval('public.konfiguracja_systemu_id_seq'::regclass);


--
-- TOC entry 2899 (class 2604 OID 17756)
-- Name: produkty id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.produkty ALTER COLUMN id SET DEFAULT nextval('public.produkty_id_seq'::regclass);


--
-- TOC entry 2892 (class 2604 OID 17757)
-- Name: sesje id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sesje ALTER COLUMN id SET DEFAULT nextval('public.sesje_id_seq'::regclass);


--
-- TOC entry 2898 (class 2604 OID 17758)
-- Name: stan_magazynu id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.stan_magazynu ALTER COLUMN id SET DEFAULT nextval('public.stan_magazynu_id_seq'::regclass);


--
-- TOC entry 2893 (class 2604 OID 17759)
-- Name: uzytkownicy id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.uzytkownicy ALTER COLUMN id SET DEFAULT nextval('public.uzytkownicy_id_seq'::regclass);


--
-- TOC entry 2894 (class 2604 OID 17760)
-- Name: zamowienia_klienci id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.zamowienia_klienci ALTER COLUMN id SET DEFAULT nextval('public.zamowienia_klienci_id_seq'::regclass);


--
-- TOC entry 2896 (class 2604 OID 17761)
-- Name: zamowienia_zaopatrzeniowiec id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.zamowienia_zaopatrzeniowiec ALTER COLUMN id SET DEFAULT nextval('public.zamowienia_zaopatrzeniowiec_id_seq'::regclass);


--
-- TOC entry 3090 (class 0 OID 17811)
-- Dependencies: 219
-- Data for Name: dane_finansowe; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.dane_finansowe (id, data, id_zamowienia, przychody, typ, wydatki, zysk) FROM stdin;
\.


--
-- TOC entry 3073 (class 0 OID 17571)
-- Dependencies: 202
-- Data for Name: dostawcy; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.dostawcy (id, nazwa_dostawcy, adres, telefon) FROM stdin;
2	Elektronika PRO	ul. Techniczna 5, 31-000 Kraków	555666777
3	Heniek elekro	markowa 3	123456777
1	Hurtownia Budowlana ABCD	ul. Przemysłowa 10, 00-001 Warszawa	222333444
4	ssd	ddd	123456789
\.


--
-- TOC entry 3092 (class 0 OID 17819)
-- Dependencies: 221
-- Data for Name: konfiguracja_systemu; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.konfiguracja_systemu (id, aktywna, nazwa_parametru, opis, typ_parametru, wartosc_parametru) FROM stdin;
\.


--
-- TOC entry 3087 (class 0 OID 17747)
-- Dependencies: 216
-- Data for Name: produkty; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.produkty (id, nazwa_produktu, opis_produktu, kod_kreskowy, cena, id_dostawcy, jednostka, strefa) FROM stdin;
1	Wiertarka udarowa	Mocna wiertarka 800W	5901234567890	250.00	1	szt	A-01
2	Młotek	Młotek ciesielski 500g	5900987654321	45.50	1	szt	A-02
3	Kabel HDMI 2m	Kabel w oplocie, standard 2.1	5901112223334	39.99	2	szt	B-01
4	Zestaw śrubokrętów	10 sztuk, magnetyczne końcówki	5904445556667	75.00	1	kpl	B-02
\.


--
-- TOC entry 3075 (class 0 OID 17587)
-- Dependencies: 204
-- Data for Name: sesje; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.sesje (id, data_utworzenia, data_wygasniecia, token, uzytkownik_id) FROM stdin;
1	2026-04-25 16:01:52.849091	2026-04-25 16:31:52.849419	57617885-7b8c-4f7e-a218-ed4a9c1220bd	3
2	2026-04-25 16:03:29.242306	2026-04-25 16:33:29.242306	b0742cf9-fada-4608-a584-3f367e914e60	3
3	2026-04-25 19:40:27.024251	2026-04-25 20:10:27.024251	ec70e56d-f16c-444c-b3b8-0fd13328cb2b	4
4	2026-04-25 19:44:43.758041	2026-04-25 20:14:43.758041	d3f8b909-026c-4d7a-880b-97c0a241fbac	4
5	2026-05-01 19:50:11.820183	2026-05-01 20:20:11.820183	a586a591-a46d-48d5-add7-eb5782dd584b	1
6	2026-05-01 21:00:58.381136	2026-05-01 21:30:58.381136	a4fdf6dd-603b-4218-8c6b-795ead464ba2	1
7	2026-05-01 22:29:33.580196	2026-05-01 22:59:33.580196	048aef62-f5ea-47c1-849f-57e5fc801fcd	1
8	2026-05-02 00:13:35.569657	2026-05-02 00:43:35.569657	75dc8892-b71b-4187-b5c4-6a78254a8ed5	1
9	2026-05-02 10:26:51.386072	2026-05-02 10:56:51.387068	ab353aaf-0b3c-4e58-9402-394b100ef51f	1
10	2026-05-02 10:53:24.73981	2026-05-02 11:23:24.73981	9e6c3c31-7997-4e78-93d2-34a62869c153	1
11	2026-05-02 11:43:21.121986	2026-05-02 12:13:21.121986	eb9103d3-eb66-4ec5-93c8-f6eb57443a43	1
12	2026-05-02 11:43:21.225549	2026-05-02 12:13:21.225549	00289426-7fba-4758-b7af-0fc72c224214	1
13	2026-05-02 18:49:11.331431	2026-05-02 19:19:11.331431	455a8a0e-1bea-4738-90f1-1daba992e38e	1
14	2026-05-03 20:51:56.814232	2026-05-03 21:21:56.814232	b03078e3-f40d-4aea-868d-7c835a86717c	1
15	2026-05-03 21:27:24.702317	2026-05-03 21:57:24.702317	157161c2-2f96-4bc1-b337-b88b7946dee4	1
16	2026-05-05 13:38:53.942399	2026-05-05 14:08:53.942399	972da3fe-f7b6-467a-83c2-46974dbc2494	3
17	2026-05-05 13:44:20.986633	2026-05-05 14:14:20.986633	fbc35d11-6594-40d4-94d9-cd7e3ce740d2	4
18	2026-05-05 19:45:06.910425	2026-05-05 20:15:06.910425	56de57d7-c5c8-4a55-a7cf-97625b3424e2	1
19	2026-05-05 19:46:14.112172	2026-05-05 20:16:14.112172	b9acc98f-45e8-45c2-bc80-4611174bc7dc	3
20	2026-05-05 19:47:20.600435	2026-05-05 20:17:20.600435	a1acafe4-7b3f-4d86-b17e-2753663bb6ed	1
21	2026-05-05 19:50:56.637686	2026-05-05 20:20:56.637686	88f64b29-f55c-47c0-a51b-73650de12a04	4
22	2026-05-12 10:45:06.781703	2026-05-12 11:15:06.781703	f74b63a7-25f6-4f8e-98f7-0b73c315e470	4
23	2026-05-15 10:54:47.313666	2026-05-15 11:24:47.313666	bf08338e-9e4a-48ea-b46f-aa69ecb57e67	5
24	2026-05-15 10:56:33.594695	2026-05-15 11:26:33.594695	13aeee19-1448-4b8b-862c-68568e5fe5a5	5
25	2026-05-15 11:44:09.53086	2026-05-15 12:14:09.53086	34cbf4ef-87fd-429d-afba-01e89dd9a777	1
26	2026-05-15 12:15:14.947552	2026-05-15 12:45:14.948555	c5266cb8-b3eb-434d-88dc-53edb7253cdf	1
27	2026-05-15 12:57:25.034732	2026-05-15 13:27:25.034732	6f117501-d8ae-4b76-b923-196091b76952	1
28	2026-05-15 13:23:00.29475	2026-05-15 13:53:00.29475	0507396d-016c-4ae5-931e-bac67bc51d55	1
29	2026-05-15 13:54:21.153057	2026-05-15 14:24:21.153057	4acb29ea-2583-4815-a496-44a3c1ae4fef	1
30	2026-05-15 14:27:19.319204	2026-05-15 14:57:19.319204	1fd5f82d-a379-499b-80ba-e9500f46acc0	1
31	2026-05-15 15:22:21.97708	2026-05-15 15:52:21.97708	6160023e-eaea-45dd-9fda-fc63ecf7fd07	1
32	2026-05-15 15:47:32.534099	2026-05-15 16:17:32.534099	d723baef-91be-49bc-95a0-c572dd12ba13	4
33	2026-05-15 16:20:06.900072	2026-05-15 16:50:06.900072	2126f4d9-0da8-41be-8d46-96c15000a18c	4
34	2026-05-17 17:24:09.032058	2026-05-17 17:54:09.032058	8ff1c3ae-ac6b-4770-9396-7644d098fa2d	5
35	2026-05-17 17:53:16.249812	2026-05-17 18:23:16.249812	2492a5a8-1b9f-428c-aabc-5f661168bb49	5
36	2026-05-17 17:53:47.156348	2026-05-17 18:23:47.157895	567766a5-90b3-4766-98d8-13e2168dfb7b	5
37	2026-05-17 17:58:24.362823	2026-05-17 18:28:24.362823	355a8a5e-8da2-4a7d-a6ef-aa6671265e2a	5
38	2026-05-17 18:02:58.160194	2026-05-17 18:32:58.160194	3f14b83c-b3cb-4b09-9ced-f937bab3f0b9	5
39	2026-05-17 18:14:26.42689	2026-05-17 18:44:26.42689	c4663dd0-f522-429d-ab6b-8f62fdb75405	5
40	2026-05-17 18:26:00.767752	2026-05-17 18:56:00.767752	2fb36c03-a516-4a9a-8927-42e5a0e11146	4
41	2026-05-17 18:39:47.502899	2026-05-17 19:09:47.502899	a753569d-2d70-4303-abb1-bd87d41c3eda	3
42	2026-05-17 18:41:54.609743	2026-05-17 19:11:54.609743	d897f1f7-dfc7-4054-80ed-fe72fde1030e	5
43	2026-05-17 19:07:59.721461	2026-05-17 19:37:59.721461	4406d826-6534-45df-b71d-cf2081f88d0f	5
44	2026-05-17 19:12:27.990663	2026-05-17 19:42:27.990663	5a525d00-1ca4-4836-b98f-5868c1c714b8	5
45	2026-05-17 19:44:29.168276	2026-05-17 20:14:29.168276	f5320b96-679d-47a9-ad38-5533bdba31a0	5
46	2026-05-17 20:13:33.711871	2026-05-17 20:43:33.711871	3ca7a376-bb57-44ae-a71e-45196ff45bfd	5
47	2026-05-19 12:47:29.972412	2026-05-19 13:17:29.972412	2005032e-8450-43db-99e1-12d971c7ea55	3
48	2026-05-19 12:51:46.089182	2026-05-19 13:21:46.089182	1b19a3eb-c629-4443-8ee8-7a0cc10ea44b	4
49	2026-05-22 09:14:22.597475	2026-05-22 09:44:22.597475	a3d74dc2-64bf-4916-a348-4065a5dc8c92	5
50	2026-05-22 10:14:28.058896	2026-05-22 10:44:28.058896	75773feb-6daa-48be-8648-567714d5df24	5
51	2026-05-22 15:58:58.372958	2026-05-22 16:28:58.372958	5ad532ca-3a38-41ed-9e0b-93e2c84cad39	5
52	2026-05-22 16:06:02.063946	2026-05-22 16:36:02.064945	bf832f31-d264-43d1-b179-e9069c7d9e1f	5
53	2026-05-22 16:15:40.05626	2026-05-22 16:45:40.05626	9cdeeb80-a156-497f-a689-da1d31ad264e	5
54	2026-05-25 15:51:19.189129	2026-05-25 16:21:19.189129	d7a7537d-33ff-4322-b5a1-9cb78c618564	5
55	2026-05-25 16:26:29.89186	2026-05-25 16:56:29.89186	582a1f57-a2e0-4db3-aabd-f0a034ecef19	5
56	2026-05-26 10:24:42.909786	2026-05-26 10:54:42.909786	e3488754-eeec-4b9b-a03e-f5bb7b99bee9	5
57	2026-05-26 10:33:52.14349	2026-05-26 11:03:52.14349	afc538db-8cd0-43b5-8d01-14d623b88591	2
58	2026-05-26 10:34:52.622301	2026-05-26 11:04:52.622301	76800b4c-e352-425b-ab34-36a0b119587c	1
59	2026-05-26 10:37:41.353925	2026-05-26 11:07:41.353925	852961d4-ba63-4ba1-b1d5-b3f03df5a18c	2
60	2026-05-26 10:52:40.855585	2026-05-26 11:22:40.855585	b8da999c-43f3-4612-9c9f-3a823071baa2	2
61	2026-05-26 10:56:34.471314	2026-05-26 11:26:34.471314	7e04a6a5-7e2d-41bd-9d13-b4e06619258f	2
62	2026-05-26 11:33:22.072736	2026-05-26 12:03:22.072736	de71acf5-805b-46bb-8c4b-777e51146b32	2
63	2026-05-26 17:16:41.783863	2026-05-26 17:46:41.783863	37373eb8-4557-4e34-a577-c6372cf5affd	5
64	2026-05-27 21:45:52.024454	2026-05-27 22:15:52.024454	dfb3bf0a-2415-4fb8-8a82-0c3232c0f051	5
65	2026-05-27 23:45:13.005506	2026-05-28 00:15:13.005506	9a855756-4200-4a34-a4a0-9c728d5a7250	5
66	2026-05-28 17:28:36.796798	2026-05-28 17:58:36.796798	dd7bea46-34e5-44b4-a343-0e276de7b22c	5
67	2026-05-28 17:28:36.795792	2026-05-28 17:58:36.796798	9da6dfbb-3763-4c49-95e4-620b0732a14f	5
68	2026-05-28 17:28:36.795792	2026-05-28 17:58:36.796798	491a65c7-d426-452c-9e46-cd817d4b040b	5
69	2026-05-29 10:41:45.260721	2026-05-29 11:11:45.260721	5379e5a4-a67e-4907-bf88-9b911a5fba0d	4
70	2026-05-29 10:43:12.067355	2026-05-29 11:13:12.067355	58c7de4f-a149-4103-84ca-a2fee2e8871c	1
71	2026-05-29 10:44:28.660232	2026-05-29 11:14:28.660232	47127c33-e66f-4978-973e-e7df38a0125c	5
72	2026-05-29 10:54:00.428759	2026-05-29 11:24:00.428759	a457cc8b-b761-4807-b0f2-447b07d66405	5
73	2026-05-29 10:56:47.358784	2026-05-29 11:26:47.358784	0bdb8da3-c22f-430f-b05e-23346b2e51c7	3
74	2026-05-29 11:39:11.508233	2026-05-29 12:09:11.508233	4218cd99-d920-4228-98da-9a4af035731e	5
75	2026-05-29 11:41:14.635717	2026-05-29 12:11:14.635717	bd2732ac-ddfb-46ad-9070-91f7715aaa37	1
76	2026-05-29 11:42:44.983617	2026-05-29 12:12:44.983617	5968f157-8807-4708-9ac9-aa76bb611e27	1
77	2026-05-29 11:43:23.905708	2026-05-29 12:13:23.905708	554a4f96-5064-4a1a-a8d4-14a02d5fd0db	3
78	2026-05-29 11:45:53.039657	2026-05-29 12:15:53.039657	c130473b-9ab2-4d42-8237-5aae02a48fe2	4
79	2026-06-01 20:40:25.237463	2026-06-01 21:10:25.237463	f222bf5a-810f-45db-9c63-266b39668770	5
80	2026-06-01 21:49:54.775167	2026-06-01 22:19:54.775167	1f93f4f1-b91b-4b90-87ed-c7e071da3d12	5
81	2026-06-01 21:55:19.681328	2026-06-01 22:25:19.681328	5bd16fbd-b5d2-4bd9-8b49-106c7a1ae7b8	5
82	2026-06-01 23:03:28.729676	2026-06-01 23:33:28.729676	d5fa7c65-559a-4863-9d8b-c0686f1798ec	5
83	2026-06-01 23:08:18.035135	2026-06-01 23:38:18.035135	b6551935-f19f-4d3c-8f3a-77442cf3c852	5
84	2026-06-02 18:53:43.626193	2026-06-02 19:23:43.626193	3909732c-cf9c-4fc4-8aa4-d96e28a3adb3	5
85	2026-06-02 18:55:23.789058	2026-06-02 19:25:23.789058	387ec238-7f9a-4609-9a2f-ca9a1bcdb074	2
86	2026-06-02 18:55:44.922069	2026-06-02 19:25:44.922069	be7cea71-e08e-4d2d-9cc4-83f53479f3e6	1
87	2026-06-02 18:56:15.092538	2026-06-02 19:26:15.092538	a22a4d6f-f9e0-430e-9ff8-f1be8ff8807f	3
88	2026-06-02 18:58:13.381818	2026-06-02 19:28:13.381818	5daa56a8-1d25-4caa-ac62-50b9fcda8395	4
89	2026-06-02 19:13:21.368349	2026-06-02 19:43:21.368349	71007c32-3d9e-4da0-b657-02cf92fd1142	1
90	2026-06-02 19:44:40.280705	2026-06-02 20:14:40.280705	9ac0de99-db17-4234-8ed9-0f7fee53a7de	3
91	2026-06-03 10:23:26.483934	2026-06-03 10:53:26.483934	93b9926e-85bf-4d9c-a725-52f53ca24e58	4
92	2026-06-03 10:26:48.765628	2026-06-03 10:56:48.765628	deee5740-dc55-448f-a91c-ade9f65b798b	5
93	2026-06-03 10:33:25.125308	2026-06-03 11:03:25.125308	ebe503b3-5e66-4bf4-bf7b-28d360504afc	4
94	2026-06-03 10:47:52.638412	2026-06-03 11:17:52.638412	0fca1bb3-644e-4baf-b933-45c073098694	5
95	2026-06-03 11:11:54.687458	2026-06-03 11:41:54.687458	8c8a6ada-855a-45c4-ac6b-d9f8102470ce	2
96	2026-06-03 11:12:17.793455	2026-06-03 11:42:17.793455	88e3a1aa-1fac-43f0-a84a-19df8dfc38a1	1
97	2026-06-03 11:21:41.063553	2026-06-03 11:51:41.063553	31bc5358-d71a-4b78-854d-c53681db6876	3
98	2026-06-03 11:26:39.496628	2026-06-03 11:56:39.496628	d10eca85-2640-4e39-a257-d4dc4f6dbdab	4
99	2026-06-03 11:36:41.846496	2026-06-03 12:06:41.846496	cab7bcab-9c73-43bd-9f69-31b64581cf69	5
100	2026-06-07 17:20:08.102927	2026-06-07 17:50:08.102927	e648d989-1190-4bc0-a544-026bedd0b467	5
101	2026-06-07 17:30:38.072467	2026-06-07 18:00:38.072467	0b9fe385-a54e-4ced-b0fa-477ae9c57dec	5
102	2026-06-07 19:13:17.859904	2026-06-07 19:43:17.859904	102a73d5-18cf-496e-b940-a00b0cbe728e	5
103	2026-06-07 19:18:07.699497	2026-06-07 19:48:07.699497	2a0e3ef2-0062-42ca-83a3-21a834f5d37b	5
104	2026-06-07 19:20:16.979695	2026-06-07 19:50:16.979695	e7671b03-20b2-461b-921b-a643c8f56fc0	5
105	2026-06-07 19:22:58.773657	2026-06-07 19:52:58.773657	38f9f6e8-dd47-4c13-931b-a22caf5041fe	5
106	2026-06-07 19:27:10.508843	2026-06-07 19:57:10.508843	9cdc0719-d2a2-4d41-bf16-5e89796ea1e4	5
107	2026-06-07 19:30:57.945938	2026-06-07 20:00:57.945938	d0c252dd-79d7-4e63-82c8-ca95716ed6bb	5
108	2026-06-07 19:36:05.034922	2026-06-07 20:06:05.035439	f7da249d-0af8-494d-861b-1e3e28ace301	5
109	2026-06-08 12:15:43.387665	2026-06-08 12:45:43.387665	62fa277a-75d1-4698-9b48-2b223d633900	5
110	2026-06-08 14:44:55.267523	2026-06-08 15:14:55.267523	a762fd59-515a-4b60-902f-856f93a54cba	5
111	2026-06-08 14:51:59.287495	2026-06-08 15:21:59.287495	7746fb79-2935-45dc-9b54-5509de38cc4f	5
112	2026-06-08 14:57:58.09159	2026-06-08 15:27:58.09159	f377d796-b924-4fba-8bb2-621af684ac07	5
113	2026-06-08 15:28:57.517338	2026-06-08 15:58:57.517338	6643e42d-af12-4ef2-a0ef-12d9576d7696	5
114	2026-06-08 15:30:31.196321	2026-06-08 16:00:31.196321	e8df80da-c360-463b-9045-a5e8c2cb811a	5
115	2026-06-08 15:45:56.911043	2026-06-08 16:15:56.911043	4a6f2377-ec9a-4803-be43-17f18230253e	5
116	2026-06-08 15:57:27.160726	2026-06-08 16:27:27.160726	27bfddd0-c400-46c2-aaf9-c371f3dc030c	5
117	2026-06-08 16:10:36.924758	2026-06-08 16:40:36.924758	d2476d87-75ed-4fe6-afa1-77502c0fc7eb	5
118	2026-06-08 16:17:46.907789	2026-06-08 16:47:46.907789	8168e32e-f868-41ff-a536-ce48d6650109	5
119	2026-06-08 16:48:25.981925	2026-06-08 17:18:25.981925	eecd71e8-82b0-417a-9a79-bf767512325b	5
120	2026-06-08 16:53:21.089265	2026-06-08 17:23:21.089265	d8a47c83-a623-4f1d-ae5e-229a25b55d4f	5
\.


--
-- TOC entry 3085 (class 0 OID 17725)
-- Dependencies: 214
-- Data for Name: stan_magazynu; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.stan_magazynu (id, id_produktu, ilosc) FROM stdin;
2	2	117.00
3	3	48.00
4	4	12.00
1	1	11.00
\.


--
-- TOC entry 3077 (class 0 OID 17600)
-- Dependencies: 206
-- Data for Name: uzytkownicy; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.uzytkownicy (id, imie, nazwisko, telefon, email, haslo, rola, firma, nip, zablokowany) FROM stdin;
3	Piotrr	Zieliński	777888999	magazyn1@firma.pl	mag123	1	Nasza Firma	9876543210	\N
5	Admin	sssss	123456780	xd@xd.pl	haslo123	3	\N	\N	\N
1	Jan	Kowalski	111222333	jan.kowalski@example.com	haslo123	4	Kowal-Bud	1234567890	f
8	jassdddd	ddddd	123456788	superemail@pl.pll	DefaultPassword123!	2	\N	\N	f
4	Katarzynaa	Wiśniewska	000111222	zaopatrzenie@firma.pl	zaop123	2	Nasza Firma	9876543210	\N
9	wefwsedfa	wedfwef	123456789	wefwse@gmail.com	DefaultPassword123!	4	wefdwdsf	1234567890	f
2	Annaa	Nowak	444555666	anna.nowak@example.com	tajnehaslo	4	\N	\N	f
\.


--
-- TOC entry 3079 (class 0 OID 17608)
-- Dependencies: 208
-- Data for Name: zamowienia_klienci; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.zamowienia_klienci (id, data, id_klienta, id_magazyniera, status) FROM stdin;
1	2023-10-25 10:30:00+02	1	3	2
2	2023-10-26 14:15:00+02	2	\N	1
8	2026-05-03 21:40:42.61517+02	1	3	2
9	2026-05-03 21:46:10.212933+02	1	3	2
10	2026-05-05 19:45:18.380297+02	1	3	2
11	2026-05-05 19:45:42.242115+02	1	3	2
14	2026-06-03 11:13:06.90413+02	1	3	0
15	2026-06-03 11:17:56.449091+02	1	3	0
13	2026-05-29 10:43:54.353001+02	1	3	1
12	2026-05-15 15:22:31.022971+02	1	3	2
\.


--
-- TOC entry 3081 (class 0 OID 17614)
-- Dependencies: 210
-- Data for Name: zamowienia_produkty_dostawcy_laczaca; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.zamowienia_produkty_dostawcy_laczaca (id_produktu, id_zamowienia, ilosc) FROM stdin;
1	1	10
2	1	50
3	2	100
1	3	9
2	3	2
4	3	1
1	4	1
2	4	2
4	4	2
3	5	4
3	6	3
1	7	2
2	7	2
1	8	6
2	8	6
4	8	4
3	9	1
1	10	2
2	10	1
3	11	2
1	12	1
2	12	1
1	13	1
2	13	2
3	14	5
3	15	2
\.


--
-- TOC entry 3082 (class 0 OID 17618)
-- Dependencies: 211
-- Data for Name: zamowienia_produkty_klienci_laczaca; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.zamowienia_produkty_klienci_laczaca (id_zamowienia, id_produktu, ilosc, cena_w_dniu_zakupu) FROM stdin;
1	1	2	250.00
1	2	5	45.00
2	3	1	39.99
8	2	1	0.00
9	2	3	45.50
9	3	1	39.99
10	1	2	250.00
10	2	2	45.50
11	4	1	75.00
12	1	2	250.00
12	2	2	45.50
13	1	1	250.00
13	2	2	45.50
13	3	2	39.99
14	1	2	250.00
15	1	2	250.00
\.


--
-- TOC entry 3083 (class 0 OID 17622)
-- Dependencies: 212
-- Data for Name: zamowienia_zaopatrzeniowiec; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.zamowienia_zaopatrzeniowiec (id, data, id_dostawcy, id_uzytkownika, status) FROM stdin;
1	2023-10-20 09:00:00+02	1	4	3
2	2023-10-27 11:45:00+02	2	4	1
3	2026-05-05 13:45:27.241773+02	1	4	1
4	2026-05-05 19:59:17.676511+02	1	4	2
5	2026-05-05 19:59:36.923735+02	2	4	1
6	2026-05-05 19:59:51.17856+02	2	4	2
7	2026-05-05 20:00:16.028768+02	1	4	2
8	2026-05-05 20:06:40.927064+02	1	4	1
9	2026-05-12 10:45:21.729495+02	2	4	2
10	2026-05-15 15:47:44.771086+02	1	4	1
12	2026-05-15 16:29:39.344076+02	1	4	2
11	2026-05-15 15:51:52.1891+02	2	4	2
13	2026-05-29 10:42:01.441727+02	1	4	2
14	2026-06-02 18:58:23.358818+02	2	4	1
15	2026-06-03 11:31:02.556313+02	2	4	1
\.


--
-- TOC entry 3108 (class 0 OID 0)
-- Dependencies: 218
-- Name: dane_finansowe_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.dane_finansowe_id_seq', 1, false);


--
-- TOC entry 3109 (class 0 OID 0)
-- Dependencies: 203
-- Name: dostawcy_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.dostawcy_id_seq', 4, true);


--
-- TOC entry 3110 (class 0 OID 0)
-- Dependencies: 220
-- Name: konfiguracja_systemu_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.konfiguracja_systemu_id_seq', 1, false);


--
-- TOC entry 3111 (class 0 OID 0)
-- Dependencies: 217
-- Name: produkty_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.produkty_id_seq', 4, true);


--
-- TOC entry 3112 (class 0 OID 0)
-- Dependencies: 205
-- Name: sesje_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.sesje_id_seq', 120, true);


--
-- TOC entry 3113 (class 0 OID 0)
-- Dependencies: 215
-- Name: stan_magazynu_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.stan_magazynu_id_seq', 4, true);


--
-- TOC entry 3114 (class 0 OID 0)
-- Dependencies: 207
-- Name: uzytkownicy_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.uzytkownicy_id_seq', 9, true);


--
-- TOC entry 3115 (class 0 OID 0)
-- Dependencies: 209
-- Name: zamowienia_klienci_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.zamowienia_klienci_id_seq', 15, true);


--
-- TOC entry 3116 (class 0 OID 0)
-- Dependencies: 213
-- Name: zamowienia_zaopatrzeniowiec_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.zamowienia_zaopatrzeniowiec_id_seq', 15, true);


--
-- TOC entry 2931 (class 2606 OID 17816)
-- Name: dane_finansowe dane_finansowe_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.dane_finansowe
    ADD CONSTRAINT dane_finansowe_pkey PRIMARY KEY (id);


--
-- TOC entry 2907 (class 2606 OID 17636)
-- Name: dostawcy dostawcy_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.dostawcy
    ADD CONSTRAINT dostawcy_pkey PRIMARY KEY (id);


--
-- TOC entry 2933 (class 2606 OID 17827)
-- Name: konfiguracja_systemu konfiguracja_systemu_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.konfiguracja_systemu
    ADD CONSTRAINT konfiguracja_systemu_pkey PRIMARY KEY (id);


--
-- TOC entry 2927 (class 2606 OID 17763)
-- Name: produkty produkty_kod_kreskowy_unique; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.produkty
    ADD CONSTRAINT produkty_kod_kreskowy_unique UNIQUE (kod_kreskowy);


--
-- TOC entry 2929 (class 2606 OID 17765)
-- Name: produkty produkty_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.produkty
    ADD CONSTRAINT produkty_pkey PRIMARY KEY (id);


--
-- TOC entry 2909 (class 2606 OID 17642)
-- Name: sesje sesje_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sesje
    ADD CONSTRAINT sesje_pkey PRIMARY KEY (id);


--
-- TOC entry 2925 (class 2606 OID 17741)
-- Name: stan_magazynu stan_magazynu_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.stan_magazynu
    ADD CONSTRAINT stan_magazynu_pkey PRIMARY KEY (id);


--
-- TOC entry 2935 (class 2606 OID 17829)
-- Name: konfiguracja_systemu uk_15su9c2re5vye23rwi9c9nx0r; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.konfiguracja_systemu
    ADD CONSTRAINT uk_15su9c2re5vye23rwi9c9nx0r UNIQUE (nazwa_parametru);


--
-- TOC entry 2911 (class 2606 OID 17646)
-- Name: sesje uk_b222ioe5ebwkcltadch54grcg; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sesje
    ADD CONSTRAINT uk_b222ioe5ebwkcltadch54grcg UNIQUE (token);


--
-- TOC entry 2913 (class 2606 OID 17648)
-- Name: uzytkownicy uzytkownicy_email_unique; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.uzytkownicy
    ADD CONSTRAINT uzytkownicy_email_unique UNIQUE (email);


--
-- TOC entry 2915 (class 2606 OID 17650)
-- Name: uzytkownicy uzytkownicy_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.uzytkownicy
    ADD CONSTRAINT uzytkownicy_pkey PRIMARY KEY (id);


--
-- TOC entry 2917 (class 2606 OID 17652)
-- Name: zamowienia_klienci zamowienia_klienci_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.zamowienia_klienci
    ADD CONSTRAINT zamowienia_klienci_pkey PRIMARY KEY (id);


--
-- TOC entry 2919 (class 2606 OID 17654)
-- Name: zamowienia_produkty_dostawcy_laczaca zamowienia_produkty_dostawcy_laczaca_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.zamowienia_produkty_dostawcy_laczaca
    ADD CONSTRAINT zamowienia_produkty_dostawcy_laczaca_pkey PRIMARY KEY (id_produktu, id_zamowienia);


--
-- TOC entry 2921 (class 2606 OID 17656)
-- Name: zamowienia_produkty_klienci_laczaca zamowienia_produkty_klienci_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.zamowienia_produkty_klienci_laczaca
    ADD CONSTRAINT zamowienia_produkty_klienci_pkey PRIMARY KEY (id_zamowienia, id_produktu);


--
-- TOC entry 2923 (class 2606 OID 17658)
-- Name: zamowienia_zaopatrzeniowiec zamowienia_zaopatrzeniowiec_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.zamowienia_zaopatrzeniowiec
    ADD CONSTRAINT zamowienia_zaopatrzeniowiec_pkey PRIMARY KEY (id);


--
-- TOC entry 2936 (class 2606 OID 17659)
-- Name: sesje fk6e9bhoshoquuhk2fkn94wh4dw; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sesje
    ADD CONSTRAINT fk6e9bhoshoquuhk2fkn94wh4dw FOREIGN KEY (uzytkownik_id) REFERENCES public.uzytkownicy(id);


--
-- TOC entry 2939 (class 2606 OID 17766)
-- Name: zamowienia_produkty_dostawcy_laczaca fk_lacz_dost_prod; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.zamowienia_produkty_dostawcy_laczaca
    ADD CONSTRAINT fk_lacz_dost_prod FOREIGN KEY (id_produktu) REFERENCES public.produkty(id);


--
-- TOC entry 2940 (class 2606 OID 17669)
-- Name: zamowienia_produkty_dostawcy_laczaca fk_lacz_dost_zam; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.zamowienia_produkty_dostawcy_laczaca
    ADD CONSTRAINT fk_lacz_dost_zam FOREIGN KEY (id_zamowienia) REFERENCES public.zamowienia_zaopatrzeniowiec(id);


--
-- TOC entry 2941 (class 2606 OID 17771)
-- Name: zamowienia_produkty_klienci_laczaca fk_lacz_prod; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.zamowienia_produkty_klienci_laczaca
    ADD CONSTRAINT fk_lacz_prod FOREIGN KEY (id_produktu) REFERENCES public.produkty(id);


--
-- TOC entry 2942 (class 2606 OID 17679)
-- Name: zamowienia_produkty_klienci_laczaca fk_lacz_zam; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.zamowienia_produkty_klienci_laczaca
    ADD CONSTRAINT fk_lacz_zam FOREIGN KEY (id_zamowienia) REFERENCES public.zamowienia_klienci(id);


--
-- TOC entry 2946 (class 2606 OID 17776)
-- Name: produkty fk_produkt_dostawca; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.produkty
    ADD CONSTRAINT fk_produkt_dostawca FOREIGN KEY (id_dostawcy) REFERENCES public.dostawcy(id);


--
-- TOC entry 2945 (class 2606 OID 17781)
-- Name: stan_magazynu fk_stan_produkt; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.stan_magazynu
    ADD CONSTRAINT fk_stan_produkt FOREIGN KEY (id_produktu) REFERENCES public.produkty(id);


--
-- TOC entry 2937 (class 2606 OID 17694)
-- Name: zamowienia_klienci fk_zam_klient; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.zamowienia_klienci
    ADD CONSTRAINT fk_zam_klient FOREIGN KEY (id_klienta) REFERENCES public.uzytkownicy(id);


--
-- TOC entry 2938 (class 2606 OID 17699)
-- Name: zamowienia_klienci fk_zam_magazynier; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.zamowienia_klienci
    ADD CONSTRAINT fk_zam_magazynier FOREIGN KEY (id_magazyniera) REFERENCES public.uzytkownicy(id);


--
-- TOC entry 2943 (class 2606 OID 17704)
-- Name: zamowienia_zaopatrzeniowiec fk_zaop_dostawca; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.zamowienia_zaopatrzeniowiec
    ADD CONSTRAINT fk_zaop_dostawca FOREIGN KEY (id_dostawcy) REFERENCES public.dostawcy(id);


--
-- TOC entry 2944 (class 2606 OID 17709)
-- Name: zamowienia_zaopatrzeniowiec fk_zaop_uzytkownik; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.zamowienia_zaopatrzeniowiec
    ADD CONSTRAINT fk_zaop_uzytkownik FOREIGN KEY (id_uzytkownika) REFERENCES public.uzytkownicy(id);


--
-- TOC entry 3098 (class 0 OID 0)
-- Dependencies: 7
-- Name: SCHEMA public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE USAGE ON SCHEMA public FROM PUBLIC;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2026-06-15 14:47:14

--
-- PostgreSQL database dump complete
--

\unrestrict xrVoMFh21PIQFkc0FRpUYqdPyxU7XM0sOn4r6BNRp9bGhV4Y9gKuFiZky2X0mwv

