insert into cordel_authority(authority) values ('ADMIN');

insert into cordel_user (username, password, enabled) values ('admin','$2a$10$a1U/XuTEv1zrHOKStAEO6OUrIvVvQJLrLOUffqzCuD1Z6Thac1yBC',true);

insert into user_authority (user_id, authority_id) values (
    (select id from cordel_user where username = 'admin'),
    (select id from cordel_authority where authority='ADMIN')
);
