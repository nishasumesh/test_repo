# test_repo
Interview repository

Movie Recommendation App : Deployment workflow

Softwares : STS IDE, Postgresql 9.6 or above, Maven latest, Java 8 or above,

Step1 : create database epsilon change owner to your database owner
Step2 : Create schema and  the  tables
Step3 : insert data using below queries
Step4 : Load the application from Git (https://github.com/nishasumesh/test_repo -> MovieManager)
Step5 : Change the DB credentials in application.yml file
Step6 : Build and Execute the project (mvn clean install , mvn spring-boot:run)


-- Database: epsilon

-- DROP DATABASE epsilon;

CREATE DATABASE epsilon
    WITH 
    OWNER = cometadmin
    ENCODING = 'UTF8'
    LC_COLLATE = 'C'
    LC_CTYPE = 'C'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;
    
-- SCHEMA: movie

-- DROP SCHEMA movie ;

CREATE SCHEMA movie
    AUTHORIZATION nishaks;
    
    -- Table: movie.film

-- DROP TABLE movie.film;

CREATE TABLE movie.film
(
    filmid text COLLATE pg_catalog."default" NOT NULL,
    filmname text COLLATE pg_catalog."default",
    director text COLLATE pg_catalog."default",
    actors text[] COLLATE pg_catalog."default",
    category text[] COLLATE pg_catalog."default",
    link text COLLATE pg_catalog."default",
    language text COLLATE pg_catalog."default",
    CONSTRAINT film_pkey PRIMARY KEY (filmid)
)

TABLESPACE pg_default;

ALTER TABLE movie.film
    OWNER to nishaks;
    
-- Table: movie."user"

-- DROP TABLE movie."user";

CREATE TABLE movie."user"
(
    user_id text COLLATE pg_catalog."default" NOT NULL,
    name text COLLATE pg_catalog."default",
    username text COLLATE pg_catalog."default",
    password text COLLATE pg_catalog."default",
    CONSTRAINT user_pkey PRIMARY KEY (user_id)
)

TABLESPACE pg_default;

ALTER TABLE movie."user"
    OWNER to nishaks;
    
-- Table: movie.userfilm

-- DROP TABLE movie.userfilm;

CREATE TABLE movie.userfilm
(
    userfilmid text COLLATE pg_catalog."default" NOT NULL,
    status text COLLATE pg_catalog."default",
    userid text COLLATE pg_catalog."default",
    filmid text COLLATE pg_catalog."default",
    CONSTRAINT userfilm_pkey PRIMARY KEY (userfilmid),
    CONSTRAINT filmid FOREIGN KEY (filmid)
        REFERENCES movie.film (filmid) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID,
    CONSTRAINT userid FOREIGN KEY (userid)
        REFERENCES movie."user" (user_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
)

TABLESPACE pg_default;

ALTER TABLE movie.userfilm
    OWNER to nishaks;
    
    
    
insert into movie.film (filmid, filmname, director, actors, category, link, language) values
('flm04',	'The Dark Knight',	'Christopher Nolan'	,'{Christian Bale,Heath Ledger,Aaron Eckhart,Michael Caine}',
'{superhero,crime,adventure}',	'https://www.youtube.com/embed/NMz0X-0Ldjg',	'English');

insert into movie.film (filmid, filmname, director, actors, category, link, language) values
('flm05',	'SpiderMan',	'Sam Raimi',	'{Tobey Maguire,Willem Dafoe,Kirsten Dunst}',	'{superhero,crime,adventure,action,fantasy}',	
'https://www.youtube.com/embed/BoM7G8qdD1w',	'English');

insert into movie.film (filmid, filmname, director, actors, category, link, language) values
('flm03',	'Iron Man',	'Jon Favreau',	'{Robert Downey Jr.,Terrence Howard,Gwyneth Paltrow,Leslie Bibb}',	'{superhero,comedy}',	
'https://www.youtube.com/embed/mAA3XMBFoEw',	'English');

insert into movie.film (filmid, filmname, director, actors, category, link, language) values
('flm02',	'Godzilla',	'Roland Emmerich',	'{Matthew Broderick,Jean Reno}',	'{fiction,monster,horror,superhero}',	
'https://www.youtube.com/embed/O_of2SQrcd0',	'English');

insert into movie.film (filmid, filmname, director, actors, category, link, language) values
('flm01'	,'Jurassic World',	'Colin Trevorrow',	'{Chris Pratt,Jake Johnson,Nick Robinson,Irrfan Khan}',	'{adventure,action,science fiction,thriller,horror,superhero}',
'https://www.youtube.com/embed/GHUGMxk6Xpo',	'English');

insert into movie."user" (user_id, name, username, password) values ('a01',	'Nisha K S',	'nisha',	'nisha');
insert into movie."user" (user_id, name, username, password) values ('a02',	'Administrator',	'admin',	'admin');


insert into movie.userfilm (userfilmid, status, userid, filmid) values ('uf01',	'watching',	'a01',	'flm02');
insert into movie.userfilm (userfilmid, status, userid, filmid) values ('uf02',	'watched',	'a01',	'flm01');
insert into movie.userfilm (userfilmid, status, userid, filmid) values ('uf03',	'watched',	'a02',	'flm01');
insert into movie.userfilm (userfilmid, status, userid, filmid) values ('uf04',	'watched',	'a02',	'flm01');


