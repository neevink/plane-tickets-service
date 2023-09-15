drop table flat;

create table flats
(
    id                     bigserial primary key,
    name                   text                   not null,
    creation_date          timestamp,
    coordinates_x          int                      not null,
    coordinates_y          float                    not null,
    area                   int,
    number_of_rooms        bigint                 not null,
    floor                  integer,
    time_to_metro_on_foot  int                    not null,
    balcony                bool                   not null,
    view                   character varying(255),
    house_name             text,
    house_year             bigint,
    house_number_of_floors int,
    price                  double precision       not null
);

insert into flats values (default, 'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa', '2016-06-22 19:10:25-07', 1, 1.0, 2, 3, 5, 100, True, 'STREET', 'houseTest', 2000, 2, 132.3);

ALTER TABLE flat
    ALTER COLUMN name TYPE text;

select * from flat;
