# thread schema

# --- !Ups

CREATE TABLE thread (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    title varchar(255) NOT NULL,
    user_id int NOT NULL,
    create_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);


INSERT INTO thread (title,user_id) values ('タイトル1',1);
INSERT INTO thread (title,user_id) values ('タイトル2',1);

# --- !Downs

DROP TABLE thread;