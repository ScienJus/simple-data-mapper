create table user_ (
  id_ int auto_increment comment 'id',
  username_ varchar(20) unique comment '用户名',
  password_ varchar(20) comment '密码',
  primary key(id_)
) comment '用户';