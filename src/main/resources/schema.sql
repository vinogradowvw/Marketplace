CREATE SCHEMA public;

CREATE SEQUENCE IF NOT EXISTS cart_seq start with 1 increment by 50;
CREATE SEQUENCE IF NOT EXISTS image_seq start with 1 increment by 50;
CREATE SEQUENCE IF NOT EXISTS orders_seq start with 1 increment by 50;
CREATE SEQUENCE IF NOT EXISTS payment_seq start with 1 increment by 50;
CREATE SEQUENCE IF NOT EXISTS post_seq start with 1 increment by 50;
CREATE SEQUENCE IF NOT EXISTS product_seq start with 1 increment by 50;
CREATE SEQUENCE IF NOT EXISTS review_seq start with 1 increment by 50;
CREATE SEQUENCE IF NOT EXISTS role_seq start with 1 increment by 1;
CREATE SEQUENCE IF NOT EXISTS subscription_seq start with 1 increment by 50;
CREATE SEQUENCE IF NOT EXISTS tag_seq start with 1 increment by 50;
CREATE SEQUENCE IF NOT EXISTS users_seq start with 1 increment by 50;

create table if not exists cart (id_cart bigint not null, id_user bigint unique, timestamp timestamp(6), primary key (id_cart));
create table if not exists cart_product (id_cart bigint not null, id_product bigint not null, quantity bigint, primary key (id_cart, id_product));
create table if not exists image (id_image bigint not null, id_post bigint, extension varchar(255), name varchar(255), primary key (id_image));
create table if not exists likes (id_post bigint not null, id_user bigint not null);
create table if not exists order_product (id_order bigint not null, id_product bigint not null, quantity bigint, primary key (id_order, id_product));
create table if not exists orders (id_order bigint not null, id_payment bigint unique, id_user bigint, timestamp timestamp(6), state varchar(255) check (state in ('NOT_PAID','PROCESSING','SHIPPED','DELIVERED','CANCELLED','RETURNED')), primary key (id_order));
create table if not exists payment (amount bigint, id_payment bigint not null, timestamp timestamp(6), primary key (id_payment));
create table if not exists post (id_post bigint not null, id_user bigint, views bigint, description varchar(255), name varchar(255), primary key (id_post));
create table if not exists post_tag (id_post bigint not null, id_tag bigint not null);
create table if not exists product (price integer, id_post bigint unique, id_product bigint not null, name varchar(255), primary key (id_product));
create table if not exists review (rating integer not null check ((rating<=5) and (rating>=1)), id bigint not null, id_post bigint, id_user bigint, description varchar(255), title varchar(255), primary key (id));
create table if not exists role (id_role bigint not null, name varchar(255), primary key (id_role));
create table if not exists roles (id_role bigint not null, id_user bigint not null);
create table if not exists subscription (id_subscriber bigint, id_subscription bigint not null, id_user bigint, timestamp timestamp(6), primary key (id_subscription));
create table if not exists tag (id_tag bigint not null, name varchar(255), primary key (id_tag));
create table if not exists users (id_user bigint not null, email varchar(255), password varchar(255), username varchar(255), primary key (id_user));

alter table if exists cart add constraint FKk0lcwd6e5q8ldue7je6efhdot foreign key (id_user) references users;
alter table if exists cart_product add constraint FKm778sxvaac8r8mi6rqgb876gm foreign key (id_cart) references cart;
alter table if exists cart_product add constraint FKr7eix56fc7n75p6kn03syh0ce foreign key (id_product) references product;
alter table if exists image add constraint FKfu33yyd0ooyym34y104rbwwi9 foreign key (id_post) references post;
alter table if exists likes add constraint FKogxqufbmgi1r85cee7g3xr7or foreign key (id_user) references users;
alter table if exists likes add constraint FK9e6id0navspjvg5swqwtl3fy6 foreign key (id_post) references post;
alter table if exists order_product add constraint FKg3uugag7e9dxyx74h2tftyp1c foreign key (id_order) references orders;
alter table if exists order_product add constraint FKkm8jfm4t1ocixswclleb7xkxj foreign key (id_product) references product;
alter table if exists orders add constraint FK5phng0rr9yex7v321tef65svq foreign key (id_payment) references payment;
alter table if exists orders add constraint FKtb6jdc061vu6ydv1445lrigtb foreign key (id_user) references users;
alter table if exists post add constraint FK3n2i1j8bfx7d7ac00iohhimul foreign key (id_user) references users;
alter table if exists post_tag add constraint FK411wa0msw4j4s9sg47twujpxb foreign key (id_tag) references tag;
alter table if exists post_tag add constraint FKcomigf7ep7ofg3rou1swvmkcx foreign key (id_post) references post;
alter table if exists product add constraint FK4tqnib1gw6uakmn8fbhyg7u9x foreign key (id_post) references post;
alter table if exists review add constraint FK1kahqq7u7xs4mx4snpqmpyjhf foreign key (id_user) references users;
alter table if exists review add constraint FKr8bl551uccq5eyqpby6av2bm9 foreign key (id_post) references post;
alter table if exists roles add constraint FK40d4m5dluy4a79sk18r064avh foreign key (id_user) references users;
alter table if exists roles add constraint FKgg40bgw33ryd3dq0k8qejewv6 foreign key (id_role) references role;
alter table if exists subscription add constraint FK2jbbyv8p9j8q0ih6gccx3oe9n foreign key (id_subscriber) references users;
alter table if exists subscription add constraint FK62h2bb9uvmjcjoada9y0iicx3 foreign key (id_user) references users;