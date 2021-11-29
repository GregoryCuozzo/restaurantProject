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
                       resto bigint,
                       primary key (id_user)
) engine=InnoDB;

create table restaurants (
                             id_restaurants bigint auto_increment  not null,
                             name varchar(255) not null,
                             adress text not null,
                             nb_place int not null,
                             opening_day text not null,
                             email text,
                             telephone text not null,
                             ville bigint,
                             primary key (id_restaurants),
                             restaurateur bigint
) engine=InnoDB;

create table reservations (
                              id_reservation bigint auto_increment  not null,
                              date date not null,
                              time time not null,
                              nbCouverts bigint not null,
                              restaurant bigint,
                              user bigint,
                              admin bigint,
                              primary key (id_reservation)
) engine=InnoDB;

create table villes (
                            id_ville bigint auto_increment not null,
                            name varchar(50),
                            pays bigint,
                            primary key (id_ville)
) engine =InnoDB;

create table pays (
                            id_pays bigint auto_increment not null,
                            name varchar(50),
                            primary key(id_pays)
) engine = InnoDB;

create index INDEX_USER_ROLE on roles (id_user);

alter table villes add constraint FK_pays foreign key (pays) references pays(id_pays) on delete CASCADE ;
alter table users add constraint UK_r43af9ap4edm43mmtq01oddj6 unique (username) ;
alter table roles add constraint FK40d4m5dluy4a79sk18r064avh foreign key (id_user) references users (id_user) on delete CASCADE;
alter table restaurants add constraint  FK_rest foreign key(restaurateur) references users(id_user) ;
alter table reservations add constraint FK_user foreign key(user) references users(id_user) ;
alter table reservations add constraint FK_rest1 foreign key (restaurant) references restaurants(id_restaurants) ;
alter table restaurants add constraint FK_ville foreign key (ville) references villes(id_ville) on delete CASCADE;
alter table reservations add constraint FK_admin foreign key(admin) references users(id_user);





