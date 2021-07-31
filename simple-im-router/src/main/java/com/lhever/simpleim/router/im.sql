DROP TABLE IF EXISTS im_user;
CREATE TABLE im_user
(
    "id" VARCHAR(32),
    "name" VARCHAR(64),
    "pwd" VARCHAR(64),
    "status" int4,
    "create_time" TIMESTAMP default now(),
    "update_time" TIMESTAMP default now(),
    CONSTRAINT pk_im_user PRIMARY KEY (id)
) WITH (
    OIDS = FALSE
);



DROP TABLE IF EXISTS im_group;
CREATE TABLE im_group
(
    "id" VARCHAR(32),
    "group_name" VARCHAR(64),
    "create_id" varchar(32),
    "member_ids" varchar(8192),
    "status" int4,
    "create_time" TIMESTAMP default now(),
    "update_time" TIMESTAMP default now(),
    CONSTRAINT pk_im_group PRIMARY KEY (id)
) WITH (
    OIDS = FALSE
);


DROP TABLE IF EXISTS im_user_group;
CREATE TABLE im_user_group
(
    "id" VARCHAR(32),
    "user_id" VARCHAR(32),
    "group_id" VARCHAR(32),
    "create_time" TIMESTAMP default now(),
    "update_time" TIMESTAMP default now(),
    CONSTRAINT pk_im_user_group PRIMARY KEY (id)
) WITH (
    OIDS = FALSE
);

DROP TABLE IF EXISTS im_user_msg;
CREATE TABLE im_user_msg
(
    "id" VARCHAR(32),
    "create_id" VARCHAR(32),
    "receive_id" VARCHAR(32),
    "type" int4,
    "content" VARCHAR(600),
    "send_status" int4,
    "read_status" int4,
    "create_time" TIMESTAMP default now(),
    "update_time" TIMESTAMP default now(),
    CONSTRAINT pk_im_user_msg PRIMARY KEY (id)
) WITH (
    OIDS = FALSE
);



DROP TABLE IF EXISTS im_group_msg;
CREATE TABLE im_group_msg
(
    "id" VARCHAR(32),
    "create_id" VARCHAR(64),
    "group_id" VARCHAR(64),
    "receive_ids" VARCHAR(64),
    "type" int4,
    "content" VARCHAR(600),
    "send_status" int4,
    "read_status" int4,
    "create_time" TIMESTAMP default now(),
    "update_time" TIMESTAMP default now(),
    CONSTRAINT pk_im_group_msg PRIMARY KEY (id)
) WITH (
    OIDS = FALSE
);


DROP TABLE IF EXISTS im_user_group_msg;
CREATE TABLE im_user_group_msg
(
    "id" VARCHAR(32),
    "create_id" VARCHAR(32),
    "group_id" VARCHAR(32),
    "receive_id" VARCHAR(32),
    "type" int4,
    "content" VARCHAR(600),
    "status" int4,
    "create_time" TIMESTAMP default now(),
    "update_time" TIMESTAMP default now(),
    CONSTRAINT pk_im_user_group_msg PRIMARY KEY (id)
) WITH (
    OIDS = FALSE
);



