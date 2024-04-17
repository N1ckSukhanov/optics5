create table if not exists customer
(
    id       int primary key,
    name     varchar(50),
    phone    varchar(30),
    birthday date
);

create table if not exists other
(
    id             int primary key,
    customer_id    int references customer (id) on delete cascade,
    product_type   varchar(20),
    name           varchar(50),
    price          decimal(10, 2),
    reception_date date,
    article        varchar(50)
);

create table if not exists lenses
(
    id             int primary key,
    customer_id    int references customer (id) on delete cascade,
    product_type   varchar(20),
    name           varchar(50),
    price          decimal(10, 2),
    reception_date date,
    power          decimal(10, 2),
    curvature      decimal(10, 1),
    color          varchar(50)
);

create table if not exists cost
(
    id       int primary key,
    frame    decimal(10, 2),
    lenses   decimal(10, 2),
    work     decimal(10, 2),
    sum      decimal(10, 2),
    discount decimal(10, 2),
    total    decimal(10, 2),
    paid     decimal(10, 2),
    extra    decimal(10, 2)
);

create table if not exists oculus
(
    id     int primary key,
    od_sph decimal(10, 2),
    os_sph decimal(10, 2),
    od_cyl decimal(10, 2),
    os_cyl decimal(10, 2),
    od_ax  decimal(10),
    os_ax  decimal(10),
    od_add decimal(10, 2),
    os_add decimal(10, 2)
);

create table if not exists recipe
(
    id             int primary key,
    customer_id    int references customer (id) on delete cascade,
    product_type   varchar(20),
    reception_date date,
    delivery_date  date,
    order_id       int,
    for_who        varchar(50),
    frame          varchar(50),
    ratio          varchar(30),
    coverage       varchar(30),
    geometry       varchar(30),
    oculus_id      int references oculus (id) on delete cascade,
    distance       decimal(3),
    info           varchar(250),
    cost_id        int references cost (id) on delete cascade
);

create table if not exists options
(
    id   int primary key,
    name varchar(30),
    type varchar(30)
);

create table if not exists photo
(
    id   int primary key,
    name varchar(40),
    type varchar(40),
    data blob
);
/*drop table if exists customer;
drop table if exists recipe;
drop table if exists oculus;
drop table if exists cost;
drop table if exists lenses;
drop table if exists other;
drop sequence if exists PRODUCT_SEQ;
drop sequence if exists COST_SEQ;
drop sequence if exists CUSTOMER_SEQ;
drop sequence if exists OCULUS_SEQ;*/
