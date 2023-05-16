insert into author (name, about, email)
values ('Author 1', 'About Author 1', 'author1@ecordel.com');

insert into author (name, about, email)
values ('Author 2', 'About Author 2', 'author2@ecordel.com');

insert into author (name, about, email)
values ('Author 3', 'About Author 3', 'author3@ecordel.com');

insert into cordel (title, author_id, content, xilogravura_url, description)
values ('Title 01', 1, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed turpis nisi, mollis vitae odio ac, dignissim gravida nunc. Nam ac bibendum lectus. Nulla id fermentum eros, sed ornare risus. Pellentesque faucibus dui et luctus efficitur.', 'https://i.pinimg.com/originals/25/9d/47/259d47304bf26a4678cb039b8d8ce7f9.jpg', 'Cordel descrition');
insert into cordel_tags (cordel_id, tags) values (1,'tag1');
insert into cordel_tags (cordel_id, tags) values (1,'tag2');
insert into cordel_tags (cordel_id, tags) values (1,'tag3');

insert into cordel (title, author_id, content, xilogravura_url, description)
values ('Title 01', 2, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed turpis nisi, mollis vitae odio ac, dignissim gravida nunc. Nam ac bibendum lectus. Nulla id fermentum eros, sed ornare risus. Pellentesque faucibus dui et luctus efficitur.', 'https://i.pinimg.com/originals/25/9d/47/259d47304bf26a4678cb039b8d8ce7f9.jpg', 'Cordel descrition');
insert into cordel_tags (cordel_id, tags) values (2,'tag1');
insert into cordel_tags (cordel_id, tags) values (2,'tag2');
insert into cordel_tags (cordel_id, tags) values (2,'tag3');

insert into cordel (title, author_id, content, xilogravura_url, description)
values ('Title 03', 3, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed turpis nisi, mollis vitae odio ac, dignissim gravida nunc. Nam ac bibendum lectus. Nulla id fermentum eros, sed ornare risus. Pellentesque faucibus dui et luctus efficitur.', 'https://i.pinimg.com/originals/25/9d/47/259d47304bf26a4678cb039b8d8ce7f9.jpg', 'Cordel descrition');

insert into cordel (title, author_id, content, xilogravura_url, description)
values ('Title 04', 1, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed turpis nisi, mollis vitae odio ac, dignissim gravida nunc. Nam ac bibendum lectus. Nulla id fermentum eros, sed ornare risus. Pellentesque faucibus dui et luctus efficitur.', 'https://i.pinimg.com/originals/25/9d/47/259d47304bf26a4678cb039b8d8ce7f9.jpg', 'Cordel descrition');

insert into cordel (title, author_id, content, xilogravura_url, description)
values ('Title 05', 2, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed turpis nisi, mollis vitae odio ac, dignissim gravida nunc. Nam ac bibendum lectus. Nulla id fermentum eros, sed ornare risus. Pellentesque faucibus dui et luctus efficitur.', 'https://i.pinimg.com/originals/25/9d/47/259d47304bf26a4678cb039b8d8ce7f9.jpg', 'Cordel descrition');

insert into cordel (title, author_id, content, xilogravura_url, description)
values ('Title 06', 3, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed turpis nisi, mollis vitae odio ac, dignissim gravida nunc. Nam ac bibendum lectus. Nulla id fermentum eros, sed ornare risus. Pellentesque faucibus dui et luctus efficitur.', 'https://i.pinimg.com/originals/25/9d/47/259d47304bf26a4678cb039b8d8ce7f9.jpg', 'Cordel descrition');

insert into cordel (title, author_id, content, xilogravura_url, description)
values ('Title 07', 1, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed turpis nisi, mollis vitae odio ac, dignissim gravida nunc. Nam ac bibendum lectus. Nulla id fermentum eros, sed ornare risus. Pellentesque faucibus dui et luctus efficitur.', 'https://i.pinimg.com/originals/25/9d/47/259d47304bf26a4678cb039b8d8ce7f9.jpg', 'Cordel descrition');

insert into cordel (title, author_id, content, xilogravura_url, description)
values ('Title 08', 2, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed turpis nisi, mollis vitae odio ac, dignissim gravida nunc. Nam ac bibendum lectus. Nulla id fermentum eros, sed ornare risus. Pellentesque faucibus dui et luctus efficitur.', 'https://i.pinimg.com/originals/25/9d/47/259d47304bf26a4678cb039b8d8ce7f9.jpg', 'Cordel descrition');

insert into cordel (title, author_id, content, xilogravura_url, description)
values ('Title 09', 3, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed turpis nisi, mollis vitae odio ac, dignissim gravida nunc. Nam ac bibendum lectus. Nulla id fermentum eros, sed ornare risus. Pellentesque faucibus dui et luctus efficitur.', 'https://i.pinimg.com/originals/25/9d/47/259d47304bf26a4678cb039b8d8ce7f9.jpg', 'Cordel descrition');

insert into cordel (title, author_id, content, xilogravura_url, description)
values ('Title 10', 1, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed turpis nisi, mollis vitae odio ac, dignissim gravida nunc. Nam ac bibendum lectus. Nulla id fermentum eros, sed ornare risus. Pellentesque faucibus dui et luctus efficitur.', 'https://i.pinimg.com/originals/25/9d/47/259d47304bf26a4678cb039b8d8ce7f9.jpg', 'Cordel descrition');

insert into cordel_authority(authority) values ('ADMIN');
insert into cordel_authority(authority) values ('USER');
insert into cordel_authority(authority) values ('AUTHOR');
insert into cordel_authority(authority) values ('EDITOR');

insert into cordel_user (username, password, enabled) values ('admin','$2a$10$a1U/XuTEv1zrHOKStAEO6OUrIvVvQJLrLOUffqzCuD1Z6Thac1yBC',true);

insert into user_authority (user_id, authority_id) values (1,1);