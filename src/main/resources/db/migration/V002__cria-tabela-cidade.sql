create table cidade (
  id bigint not null auto_increment,
  nome varchar(80) not null,
  nome_estado varchar(80) not null,
  primary key (id)
) engine=InnoDB default charset=utf8;