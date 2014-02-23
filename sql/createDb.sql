create table infrastructure (
	id serial primary key,
	mac character(17) not null
);

create table samples (
	id serial primary key,
	timestamp bigint not null,
	infra_mac smallint not null,
	device_mac character(17) not null,	
	rssi smallint not null,
	foreign key (infra_mac) references infrastructure(id)
);

insert into infrastructure(mac) values ('58:6D:8F:64:05:08');
insert into infrastructure(mac) values ('58:6D:8F:64:04:E4');