
CREATE DATABASE IF NOT EXISTS bms DEFAULT CHARSET utf8 COLLATE utf8_general_ci;

USE bms;

-- 供应商表
DROP TABLE IF EXISTS t_supplier;
CREATE TABLE t_supplier (
       id                   CHAR(36)                       PRIMARY KEY             ,           -- 供应商编号
       name                 VARCHAR(90)                    NOT NULL                ,           -- 供应商名称
       contact              VARCHAR(20)                                            ,           -- 供应商联系人
       tel                  VARCHAR(17)                                            ,           -- 供应商电话
       fax                  VARCHAR(11)                                            ,           -- 供应商传真
       address              VARCHAR(300)                                           ,           -- 供应商地址
       descriptions         VARCHAR(300)                                                       -- 供应商描述
);


-- 账单表
DROP TABLE IF EXISTS t_bill;
CREATE TABLE t_bill (
       id                   CHAR(36)                       PRIMARY KEY            ,           -- 商品编号
       name                 VARCHAR(90)                    NOT NULL               ,           -- 商品名称
       num                  INT                            NOT NULL               ,           -- 商品数量
       unit                 VARCHAR(10)                    NOT NULL               ,           -- 交易单位
       money                DOUBLE                         NOT NULL               ,           -- 交易金额
       isPayment            CHAR(1)                        NOT NULL               ,           -- 是否付款（Y 表示已付款，N 表示未付款）
       supplierId           CHAR(36)                       NOT NULL               ,           -- 外键，对应供应商表主键
       descriptions         VARCHAR(300)                                          ,           -- 端口描述
       createTime           VARCHAR(19)                                                       -- 账单时间（2012-12-12 12:12:12）
       -- CONSTRAINT supplier_bill_fk FOREIGN KEY(supplierId) REFERENCES t_supplier(id)
);


-- 权限表
DROP TABLE IF EXISTS t_privilege;
CREATE TABLE t_privilege (
       id                   CHAR(36)                       PRIMARY KEY            ,           -- 权限编号
       name                 VARCHAR(30)                    NOT NULL               ,           -- 权限名称
       url                  VARCHAR(200)                                          ,           -- 权限对应的访问地址
       parentId             CHAR(36)                                                          -- 上级权限名称
);

-- 1
INSERT INTO t_privilege(id, name, url) VALUES(uuid(), '用户管理', 'user/UserServlet?method=LIST');
INSERT INTO t_privilege(id, name, url, parentId) VALUES(uuid(), '新增用户', 'user/UserServlet?method=ADD', (SELECT tp.id FROM t_privilege AS tp WHERE tp.name='用户管理'));
INSERT INTO t_privilege(id, name, url, parentId) VALUES(uuid(), '删除用户', 'user/UserServlet?method=DEL', (SELECT tp.id FROM t_privilege AS tp WHERE tp.name='用户管理'));
INSERT INTO t_privilege(id, name, url, parentId) VALUES(uuid(), '更新用户', 'user/UserServlet?method=UPDATE', (SELECT tp.id FROM t_privilege AS tp WHERE tp.name='用户管理'));
INSERT INTO t_privilege(id, name, url, parentId) VALUES(uuid(), '修改密码', 'user/UserServlet?method=UPDATE_PWD', (SELECT tp.id FROM t_privilege AS tp WHERE tp.name='用户管理'));

-- 6
INSERT INTO t_privilege(id, name, url) VALUES(uuid(), '供应商管理', 'supplier/SupplierServlet?method=LIST');
INSERT INTO t_privilege(id, name, url, parentId) VALUES(uuid(), '新增供应商', 'supplier/SupplierServlet?method=ADD', (SELECT tp.id FROM t_privilege AS tp WHERE tp.name='供应商管理'));
INSERT INTO t_privilege(id, name, url, parentId) VALUES(uuid(), '删除供应商', 'supplier/SupplierServlet?method=DEL', (SELECT tp.id FROM t_privilege AS tp WHERE tp.name='供应商管理'));
INSERT INTO t_privilege(id, name, url, parentId) VALUES(uuid(), '更新供应商', 'supplier/SupplierServlet?method=UPDATE', (SELECT tp.id FROM t_privilege AS tp WHERE tp.name='供应商管理'));

-- 10
INSERT INTO t_privilege(id, name, url) VALUES(uuid(), '账单管理', 'bill/BillServlet?method=LIST');
INSERT INTO t_privilege(id, name, url, parentId) VALUES(uuid(), '新增账单', 'bill/BillServlet?method=ADD', (SELECT tp.id FROM t_privilege AS tp WHERE tp.name='账单管理'));
INSERT INTO t_privilege(id, name, url, parentId) VALUES(uuid(), '删除账单', 'bill/BillServlet?method=DEL', (SELECT tp.id FROM t_privilege AS tp WHERE tp.name='账单管理'));
INSERT INTO t_privilege(id, name, url, parentId) VALUES(uuid(), '更新账单', 'bill/BillServlet?method=UPDATE', (SELECT tp.id FROM t_privilege AS tp WHERE tp.name='账单管理'));

--
INSERT INTO t_privilege(id, name, url) VALUES(uuid(), '在线用户列表', 'user/OnlineUserServlet?method=LIST');

-- 角色表
DROP TABLE IF EXISTS t_role;
CREATE TABLE t_role (
       id                   CHAR(36)                       PRIMARY KEY            ,           -- 角色编号
       name                 VARCHAR(60)                    NOT NULL               ,           -- 角色名称
       descriptions         VARCHAR(300)                                                      -- 角色描述
);

-- 1
INSERT INTO t_role(id, name) VALUES(uuid(), '普通用户');
-- 2
INSERT INTO t_role(id, name) VALUES(uuid(), '管理员用户');


-- 角色与权限的对应中间表
DROP TABLE IF EXISTS t_roles_privilege;
CREATE TABLE t_roles_privilege (
       roleId               CHAR(36)                                              ,           --
       privilegeId          CHAR(36)                                              ,           --
       CONSTRAINT pk_role_privilege PRIMARY KEY (roleId, privilegeId)
);

INSERT INTO t_roles_privilege(roleId, privilegeId) VALUES((SELECT tr.id FROM t_role AS tr WHERE tr.name='管理员用户'), (SELECT tp.id FROM t_privilege AS tp WHERE tp.name='用户管理'));
INSERT INTO t_roles_privilege(roleId, privilegeId) VALUES((SELECT tr.id FROM t_role AS tr WHERE tr.name='管理员用户'), (SELECT tp.id FROM t_privilege AS tp WHERE tp.name='新增用户'));
INSERT INTO t_roles_privilege(roleId, privilegeId) VALUES((SELECT tr.id FROM t_role AS tr WHERE tr.name='管理员用户'), (SELECT tp.id FROM t_privilege AS tp WHERE tp.name='删除用户'));
INSERT INTO t_roles_privilege(roleId, privilegeId) VALUES((SELECT tr.id FROM t_role AS tr WHERE tr.name='管理员用户'), (SELECT tp.id FROM t_privilege AS tp WHERE tp.name='更新用户'));
INSERT INTO t_roles_privilege(roleId, privilegeId) VALUES((SELECT tr.id FROM t_role AS tr WHERE tr.name='管理员用户'), (SELECT tp.id FROM t_privilege AS tp WHERE tp.name='修改密码'));
INSERT INTO t_roles_privilege(roleId, privilegeId) VALUES((SELECT tr.id FROM t_role AS tr WHERE tr.name='管理员用户'), (SELECT tp.id FROM t_privilege AS tp WHERE tp.name='在线用户列表'));


-- 用户表
DROP TABLE IF EXISTS t_user;
CREATE TABLE t_user (
       id                   CHAR(36)                       PRIMARY KEY            ,           -- 用户编号
       loginName            VARCHAR(20)                    NOT NULL               ,           -- 登录名
       userName             VARCHAR(30)                    NOT NULL               ,           -- 用户名
       password             VARCHAR(30)                    NOT NULL               ,           -- 用户密码
       gender               VARCHAR(4)                                            ,           -- 用户性别，值：男，女，未知
       birthday             VARCHAR(10)                                           ,           -- 用户出生日期
       tel                  VARCHAR(17)                                           ,           -- 用户电话
       address              VARCHAR(300)                                          ,           -- 用户地址
       roleId               CHAR(36)                       NOT NULL               ,           -- 外键，对应角色表的主键
       createTime           VARCHAR(19)                    NOT NULL                           -- 用户创建时间
       -- CONSTRAINT role_user_fk FOREIGN KEY(roleId) REFERENCES t_role(id)
);

-- 1
INSERT INTO t_user(id, loginName, userName, password, birthday, tel, address, roleId, createTime) VALUES(uuid(), 'admin', '超级管理员', 'admin', '0000-00-00', '00000000000', '未知', (SELECT tr.id FROM t_role AS tr WHERE tr.name='管理员用户'), '0000-00-00 00:00:00');
-- 2
INSERT INTO t_user(id, loginName, userName, password, gender, birthday, tel, address, roleId, createTime) VALUES(uuid(), 'test', '管理员用户', 'test', '男', '1990-09-09', '00000000000', '测试账户没有地址', (SELECT tr.id FROM t_role AS tr WHERE tr.name='管理员用户'), '2013-09-03 15:22:10');
-- 3
INSERT INTO t_user(id, loginName, userName, password, gender, birthday, tel, address, roleId, createTime) VALUES(uuid(), 'test2', '普通用户', 'test2', '女', '1991-01-01', '00000000000', '测试账户没有地址', (SELECT tr.id FROM t_role AS tr WHERE tr.name='普通用户'), '2013-09-03 16:22:10');


-- 在线用户信息表
DROP TABLE IF EXISTS t_online_user;
CREATE TABLE t_online_user (
       sessionId            CHAR(36)                       PRIMARY KEY            ,           -- 用户Session ID
       userId               CHAR(36)                       NOT NULL               ,           -- 外键，对应用户表的主键
       loginName            VARCHAR(20)                    NOT NULL               ,           -- 外键，对应用户表的登录名
       userName             VARCHAR(30)                    NOT NULL               ,           -- 外键，对应用户表的用户名
       clientIp             VARCHAR(36)                                           ,           -- 客户端IP
       pageUrl              VARCHAR(255)                                          ,           -- 用户停留页面
       createTime           BIGINT                         NOT NULL               ,           -- 在线用户信息创建时间
       lastAccessTime       BIGINT                                                            -- 用户最后访问时间
);


-- 提交事务
COMMIT;
