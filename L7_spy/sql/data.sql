CREATE TABLE word (
 id bigint not null auto_increment,
 name text,
 primary key (id)
);

INSERT INTO word(id, name) VALUES (1, 'пересдача');
INSERT INTO word(id, name) VALUES (2, 'зачет');
INSERT INTO word(id, name) VALUES (3, 'экзамен');
INSERT INTO word(id, name) VALUES (4, 'два');