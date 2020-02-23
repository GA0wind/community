create table comment
(
	id bigint auto_increment,
	parent_id bigint not null,
	type int,
	commentator int not null,
	descript text,
	gmt_create bigint,
	gmt_modified bigint,
	like_count int default 0,
	constraint comment_pk
		primary key (id)
);

