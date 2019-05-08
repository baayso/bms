
-- 生成符合rfc 4122的标准uuid的函数
-- drop function get_uuid;
CREATE OR REPLACE
FUNCTION get_uuid
RETURN CHAR
IS
guid VARCHAR (50);
BEGIN
guid := lower(RAWTOHEX(sys_guid()));
RETURN
substr(guid,1,8)||'-'||substr(guid,9,4)||'-'||substr(guid,13,4)||'-'||substr(guid,17,4)||'-'||substr(guid,21,12);
END get_uuid;


-- 以下部分请选中执行
-- 供应商表
drop table t_supplier;
create table t_supplier (
       id               char(36)        primary key, -- 供应商编号
       name             varchar2(90)    not null, -- 供应商名称
       contact          varchar2(20), -- 供应商联系人
       tel              varchar2(17), -- 供应商电话
       fax              varchar2(11), -- 供应商传真
       address          varchar2(300), -- 供应商地址
       descriptions     varchar2(300) -- 供应商描述
);


-- 账单表
drop table t_bill;
create table t_bill (
       id               char(36)           primary key, -- 商品编号
       name             varchar2(90)       not null, -- 商品名称
       num              number(11)         not null, -- 商品数量
       unit             varchar2(10)       not null, -- 交易单位
       money            number(11,2)       not null, -- 交易金额
       isPayment        char(1)            not null, -- 是否付款（Y 表示已付款，N 表示未付款）
       supplierId       char(36)           not null, -- 外键，对应供应商表主键
       descriptions     varchar2(300), -- 端口描述
       createTime       varchar2(19)   -- 账单时间（2012-12-12 12:12:12）
       -- constraint supplier_bill_fk foreign key(supplierId) references t_supplier(id)
);


-- 权限表
drop table t_privilege;
create table t_privilege(
       id               char(36)               primary key, -- 权限编号
       name             varchar2(30)           not null, -- 权限名称
       url              varchar2(200), -- 权限对应的访问地址
       parentId         char(36) -- 上级权限名称
);

-- 1
insert into t_privilege(id, name, url) values(get_uuid(), '用户管理', '/servlet/user/list');
insert into t_privilege(id, name, url, parentId) values(get_uuid(), '新增用户', '/servlet/user/add', (select id from t_privilege where name='用户管理'));
insert into t_privilege(id, name, url, parentId) values(get_uuid(), '删除用户', '/servlet/user/delete', (select id from t_privilege where name='用户管理'));
insert into t_privilege(id, name, url, parentId) values(get_uuid(), '更新用户', '/servlet/user/update', (select id from t_privilege where name='用户管理'));
insert into t_privilege(id, name, url, parentId) values(get_uuid(), '修改密码', '/servlet/user/updatePwd', (select id from t_privilege where name='用户管理'));

-- 6
insert into t_privilege(id, name, url) values(get_uuid(), '供应商管理', '/servlet/supplier/list');
insert into t_privilege(id, name, url, parentId) values(get_uuid(), '新增供应商', '/servlet/supplier/add', (select id from t_privilege where name='供应商管理'));
insert into t_privilege(id, name, url, parentId) values(get_uuid(), '删除供应商', '/servlet/supplier/delete', (select id from t_privilege where name='供应商管理'));
insert into t_privilege(id, name, url, parentId) values(get_uuid(), '更新供应商', '/servlet/supplier/update', (select id from t_privilege where name='供应商管理'));

-- 10
insert into t_privilege(id, name, url) values(get_uuid(), '账单管理', '/servlet/bill/list');
insert into t_privilege(id, name, url, parentId) values(get_uuid(), '新增账单', '/servlet/bill/add', (select id from t_privilege where name='账单管理'));
insert into t_privilege(id, name, url, parentId) values(get_uuid(), '删除账单', '/servlet/bill/delete', (select id from t_privilege where name='账单管理'));
insert into t_privilege(id, name, url, parentId) values(get_uuid(), '更新账单', '/servlet/bill/update', (select id from t_privilege where name='账单管理'));

--
insert into t_privilege(id, name, url) values(get_uuid(), '在线用户列表', '/servlet/user/onlineUser/list');

-- 角色表
drop table t_role;
create table t_role(
       id               char(36)               primary key, -- 角色编号
       name             varchar2(60)           not null, -- 角色名称
       -- 角色描述
       descriptions     varchar2(300)
);

-- 1
insert into t_role(id, name) values(get_uuid(), '普通用户');
-- 2
insert into t_role(id, name) values(get_uuid(), '管理员用户');

-- 角色与权限的对应中间表
drop table t_roles_privilege;
create table t_roles_privilege(
       roleId          char(36),
       privilegeId     char(36),
       constraint pk_role_privilege primary key (roleId, privilegeId)
);

insert into t_roles_privilege(roleId, privilegeId) values((select id from t_role where name='管理员用户'), (select id from t_privilege where name='用户管理'));
insert into t_roles_privilege(roleId, privilegeId) values((select id from t_role where name='管理员用户'), (select id from t_privilege where name='新增用户'));
insert into t_roles_privilege(roleId, privilegeId) values((select id from t_role where name='管理员用户'), (select id from t_privilege where name='删除用户'));
insert into t_roles_privilege(roleId, privilegeId) values((select id from t_role where name='管理员用户'), (select id from t_privilege where name='更新用户'));
insert into t_roles_privilege(roleId, privilegeId) values((select id from t_role where name='管理员用户'), (select id from t_privilege where name='修改密码'));
insert into t_roles_privilege(roleId, privilegeId) values((select id from t_role where name='管理员用户'), (select id from t_privilege where name='在线用户列表'));


-- 用户表
drop table t_user;
create table t_user(
       id         char(36)         primary key, -- 用户编号
       loginName  varchar2(20)     not null, -- 登录名
       userName   varchar2(30)     not null, -- 用户名
       password   varchar2(30)     not null, -- 用户密码
       gender     varchar2(4),   -- 用户性别，值：男，女，未知
       birthday   varchar2(10),  -- 用户出生日期
       tel        varchar2(17),  -- 用户电话
       address    varchar2(300), -- 用户地址
       roleId     char(36)         not null, -- 外键，对应角色表的主键
       createTime varchar2(19)     not null  -- 用户创建时间
       -- constraint role_user_fk foreign key(roleId) references t_role(id)
);

-- 1
insert into t_user(id, loginName, userName, password, birthday, tel, address, roleId, createTime) values(get_uuid(), 'admin', '超级管理员', 'admin', '0000-00-00', '00000000000', '未知', (select id from t_role where name='管理员用户'), '0000-00-00 00:00:00');
-- 2
insert into t_user(id, loginName, userName, password, gender, birthday, tel, address, roleId, createTime) values(get_uuid(), 'test', '管理员用户', 'test', '男', '1990-09-09', '00000000000', '测试账户没有地址', (select id from t_role where name='管理员用户'), '2013-09-03 15:22:10');
-- 3
insert into t_user(id, loginName, userName, password, gender, birthday, tel, address, roleId, createTime) values(get_uuid(), 'test2', '普通用户', 'test2', '女', '1991-01-01', '00000000000', '测试账户没有地址', (select id from t_role where name='普通用户'), '2013-09-03 16:22:10');


-- 在线用户信息表
drop table t_online_user;
create table t_online_user(
       sessionId        char(36)           primary key, -- 用户Session ID
       userId           CHAR(36)           not null, -- 外键，对应用户表的主键
       loginName        varchar2(20)       not null, -- 外键，对应用户表的登录名
       userName         varchar2(30)       not null, -- 外键，对应用户表的用户名
       clientIp         varchar2(36), -- 客户端IP
       pageUrl          varchar2(255), -- 用户停留页面
       createTime       number(13)         not null, -- 在线用户信息创建时间
       lastAccessTime   number(13) -- 用户最后访问时间
);

-- 提交事务
commit;