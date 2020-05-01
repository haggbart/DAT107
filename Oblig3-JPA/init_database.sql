drop schema if exists oblig3_1 cascade;

create schema oblig3_1;
set search_path to oblig3_1;

/*
 Ansatt
• Unik ansatt-id (automatisk generert løpenummer)
• Unikt brukernavn (initialer, 3-4 bokstaver, f.eks. «lph»)
• Fornavn
• Etternavn
• Dato for ansettelse
• Stilling
• Månedslønn
• Hvilken avdeling den ansatte jobber i
• Hvilke prosjekter den ansatte deltar/har deltatt i m/ rolle og antall arbeidstimer
 */

create table avdeling
(
    id     serial primary key,
    navn   varchar(30) not null,
    sjef_id smallint /* vil egentlig ha not null, men må legges til etter sjef(ansatt) er opprettet som beskrevet i oppgaven */
);

create table ansatt
( /* mange-til-en */
    id           serial primary key, /* Unik ansatt-id (automatisk generert løpenummer) */
    brukernavn   varchar(10)  not null unique,
    fornavn      varchar(30) not null,
    etternavn    varchar(30) not null,
    ansattdato   date        not null default current_date,
    stilling     varchar(30),
    manedslonn decimal(10, 2),
    avdeling_id   smallint    not null,
    foreign key (avdeling_id) references avdeling (id)
);

create table prosjekt
( /* mange-til mange */
    id          serial primary key, /* Unik prosjektID (automatisk generert)*/
    navn        varchar(40) not null,
    beskrivelse varchar(255)
);

create table prosjektdeltagelse
(
    id serial,
    ansatt_id    int,
    prosjekt_id  int,
    arbeidstimer int,
    rolle        varchar(40),
    constraint prosjektdeltagelse_pk primary key (id),
    constraint prosjektdettagelse_unique unique (ansatt_id, prosjekt_id),
    foreign key (ansatt_id) references ansatt (id),
    foreign key (prosjekt_id) references prosjekt (id)
);

Alter table avdeling /* legge til fremmednøkkel etter ansatt-tabellen er opprettet */
    add foreign key (sjef_id) references ansatt (id);

insert into avdeling (navn) /* vent med sjef til ansatt er opprettet */
values ('Frukt og grønt'),
       ('Kjøtt'),
       ('Meieri');

insert into ansatt(brukernavn, fornavn, etternavn, ansattdato, avdeling_id, stilling, manedslonn)
values ('ahi', 'Arild', 'Hilsson', '2020-02-01', 1, 'avdelingsleder', 25000),
       ('amo', 'Arvid', 'Moe', '2020-01-25', 1, 'vedlikeholder', 15000),
       ('mse', 'Marius', 'Sekse', '2019-11-10', 2, 'teknikker', 23000),
       ('mje', 'Mariam', 'Jensen', '2019-11-12', 1, 'mekaniker', 23500),
       ('est', 'Elisabeth', 'Støen', '2019-12-1', 3, 'prosjektingeniør', 15600);

insert into prosjekt(navn, beskrivelse)
values ('Tjene meir penger', 'Dette prosjektet er til for å tjene meir penger'),
       ('Idémyldring til kampanje', 'Formålet med dette prosjektet er å samle idéer til neste reklamekampanje');

insert into Prosjektdeltagelse(ansatt_id, prosjekt_id, arbeidstimer, rolle)
values (5, 2, 15, 'Fluffer'),
       (5, 1, 90, 'Kekmaster'),
       (4, 2, 20, 'Prosjektleder'),
       (2, 1, 80, 'Hoffnarr');

/* test dagens dato med auto date */
insert into ansatt(brukernavn, fornavn, etternavn, avdeling_id, stilling, manedslonn)
values ('sbe', 'Siri', 'Berget', 3, 'vikar', 34000),
       ('bbj', 'Benjamin', 'Bjerkan', 2, 'lærer', 28900);

/* sett en sjef for hver avdeling */
update avdeling
set sjef_id = 1
where id = 1;
update avdeling
set sjef_id = 3
where id = 2;
update avdeling
set sjef_id = 5
where id = 3;
