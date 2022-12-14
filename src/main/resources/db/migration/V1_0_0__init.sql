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
                       reset_password_token varchar(45),
                       firstname varchar(255) not null,
                       lastname varchar(255) not null,
                       password varchar(255) not null,
                       username varchar(255) not null,
                       email text not null,
                       phone text,
                       resto bigint,
                       contact varchar(15),
                       primary key (id_user)
) engine=InnoDB;

create table visitors(
                         id_visitor bigint auto_increment not null,
                         firstName varchar(255) not null,
                         lastname varchar(255) not null,
                         date date not null,
                         time varchar(255) not null,
                         nbcouverts Integer not null,
                         email text not null,
                         phone text not null,
                         resto bigint,
                         primary key (id_visitor)

)engine=InnoDB;


create table restaurants (
                             id_restaurants bigint auto_increment  not null,
                             name varchar(255) not null,
                             adress text not null,
                             nb_place int not null,

                             email text,
                             telephone text not null,
                             ville bigint,
                             primary key (id_restaurants),
                             restaurateur bigint,
                             rappel bigint
) engine=InnoDB;

create table reservations (
                              id_reservation bigint auto_increment  not null,
                              date date not null,
                              time varchar(255) not null,
                              nbcouverts int not null,
                              restaurant bigint not null,
                              user bigint not null,
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


create table horaires (
                            id_horaire bigint auto_increment not null,
                            restaurant bigint not null,
                            jour varchar(15) not null,
                            ouverture varchar(50),
                            fermeture varchar(50),
                            primary key(id_horaire)
) engine = InnoDB;

create index INDEX_USER_ROLE on roles (id_user);

alter table villes add constraint FK_pays foreign key (pays) references pays(id_pays) on delete CASCADE ;
alter table users add constraint UK_r43af9ap4edm43mmtq01oddj6 unique (username) ;
alter table roles add constraint FK40d4m5dluy4a79sk18r064avh foreign key (id_user) references users (id_user) on delete CASCADE;
alter table restaurants add constraint  FK_rest foreign key(restaurateur) references users(id_user) on delete CASCADE;
alter table reservations add constraint FK_user foreign key(user) references users(id_user) on delete CASCADE;
alter table reservations add constraint FK_resto foreign key (restaurant) references restaurants(id_restaurants) on delete CASCADE ;
alter table restaurants add constraint FK_ville foreign key (ville) references villes(id_ville) on delete CASCADE;
alter table reservations add constraint FK_admin foreign key(admin) references users(id_user) on delete CASCADE;





