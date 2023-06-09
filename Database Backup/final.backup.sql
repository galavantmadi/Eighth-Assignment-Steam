PGDMP         3                {            postgres    13.2    13.2     �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            �           1262    13442    postgres    DATABASE     l   CREATE DATABASE postgres WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE = 'English_United States.1252';
    DROP DATABASE postgres;
                postgres    false            �           0    0    DATABASE postgres    COMMENT     N   COMMENT ON DATABASE postgres IS 'default administrative connection database';
                   postgres    false    3005                        2615    2200    public    SCHEMA        CREATE SCHEMA public;
    DROP SCHEMA public;
                postgres    false            �           0    0    SCHEMA public    COMMENT     6   COMMENT ON SCHEMA public IS 'standard public schema';
                   postgres    false    4            �            1259    16422    account    TABLE     �   CREATE TABLE public.account (
    id bigint NOT NULL,
    username text,
    password text,
    birth_of_date text,
    token text
);
    DROP TABLE public.account;
       public         heap    postgres    false    4            �            1259    16420    account_id_seq    SEQUENCE     �   ALTER TABLE public.account ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.account_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    202    4            �            1259    16448 	   downloads    TABLE     e   CREATE TABLE public.downloads (
    account_id text,
    game_id text,
    download_count integer
);
    DROP TABLE public.downloads;
       public         heap    postgres    false    4            �            1259    16440    game    TABLE     �   CREATE TABLE public.game (
    id text NOT NULL,
    title text,
    developer text,
    genre text,
    price double precision,
    release_year integer,
    controller_support boolean,
    reviews integer,
    size integer,
    file_path text
);
    DROP TABLE public.game;
       public         heap    postgres    false    4            �          0    16422    account 
   TABLE DATA           O   COPY public.account (id, username, password, birth_of_date, token) FROM stdin;
    public          postgres    false    202   ]       �          0    16448 	   downloads 
   TABLE DATA           H   COPY public.downloads (account_id, game_id, download_count) FROM stdin;
    public          postgres    false    204   �       �          0    16440    game 
   TABLE DATA           ~   COPY public.game (id, title, developer, genre, price, release_year, controller_support, reviews, size, file_path) FROM stdin;
    public          postgres    false    203   �       �           0    0    account_id_seq    SEQUENCE SET     <   SELECT pg_catalog.setval('public.account_id_seq', 1, true);
          public          postgres    false    201            /           2606    16429    account account_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.account
    ADD CONSTRAINT account_pkey PRIMARY KEY (id);
 >   ALTER TABLE ONLY public.account DROP CONSTRAINT account_pkey;
       public            postgres    false    202            1           2606    16447    game game_pkey 
   CONSTRAINT     L   ALTER TABLE ONLY public.game
    ADD CONSTRAINT game_pkey PRIMARY KEY (id);
 8   ALTER TABLE ONLY public.game DROP CONSTRAINT game_pkey;
       public            postgres    false    203            �   _   x��;�  ��ò_�Xx
�eFH��<���OHX�(e���T�*{f�I�0J�f'��D\��K��t
P����3�wn{<��S��      �      x�3�4���06�4����� �h      �   I  x����r�0���Shו=���I���3m8��Ab$A�>}�m�ˎ3��2�$��:��$��^��^���)�Z;4@��1$,���h�*���>����ZY��[E���skJ�[!�b�h4���ޔh9�3eөzE)�I��6�|E��N�)��mנC����Z�C��Ȧp[�֢��{��<��0��,�����3�BNc�p���PTğ`ۍ$��A�'�!�E�7�]�4��<�4�KS'ޅ�',�VV�Ud?Ȇ|C���+uEo9���m ��IG!��..�T�0�Y�˶o��������/�M����u׈�x�]��� ZX6k&Y� af�/���h����/j�xf[Q OG$_�+�hHt�O��<���vG>��'76���k��@�/�p�3
#���WԺ�W'��<KG��+�-����*�?����Y2�X�|]�tK��P���%���~#�������YZ߄�^����X����Ɖgyv��8$���Mc��R��F��谔�����m%�D�K���:�4�� I^�3{��Y�V? 41%R     