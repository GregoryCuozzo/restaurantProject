create table roles (
                       id_user bigint not null,
                       role varchar(255) not null
) engine=InnoDB;

create table users (
                       id_user bigint auto_increment not null,
                       account_non_expired bit,
                       account_non_locked bit,
                       credentials_non_expired bit,
                       enabled bit,
                       firstname varchar(255) not null,
                       lastname varchar(255) not null,
                       password varchar(255) not null,
                       username varchar(255) not null,
                       email text,
                       primary key (id_user)
) engine=InnoDB;

create table restaurants (
                             id_restaurants bigint not null,
                             name varchar(255) not null,
                             adress text not null,
                             nbPlace int not null,
                             joursOuverture text not null,
                             primary key (id_restaurants),
                             horaires date not null,
                             email text not null,
                             telephone int not null,
                             XIDadmin bigint not null,
                             XIDville bigint not null


) engine=InnoDB;

create table reservations (
                              id_reservation bigint not null,
                              primary key (id_reservation),
                              date date not null,
                              time time not null,
                              restaurant bigint,
                              client bigint
) engine=InnoDB;

create table villes (
                            id_ville bigint not null,
                            name varchar(255) not null,
                            cp int not null,
                            primary key (id_ville),
                            XIDpays bigint not null
) engine=InnoDB;

create table pays (
                           id_pays bigint not null,
                           primary key (id_pays),
                           name varchar (255) not null
) engine=InnoDB;

create index INDEX_USER_ROLE on roles (id_user);

alter table users add constraint UK_r43af9ap4edm43mmtq01oddj6 unique (username);
alter table roles add constraint FK40d4m5dluy4a79sk18r064avh foreign key (id_user) references users (id_user);
alter table restaurants add constraint  FK_rest foreign key(restaurateur) references users(id_user);
alter table reservations add constraint FK_user foreign key(client) references users(id_user);
alter table reservations add constraint FK_rest1 foreign key (restaurant) references restaurants(id_restaurants);