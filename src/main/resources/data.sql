insert into APP_USER(ID, PASSWORD, USERNAME) values(1, '$2a$10$AzEK0EnYVBvEs.xGpSCFSuFAFkzYpSjzJRpU6vAidLU59lBdG8VuW', 'yavor.bachev@gmail.com');
insert into USER_ROLE(APP_USER_ID, ROLE) values(1, 'ADMIN');
insert into USER_ROLE(APP_USER_ID, ROLE) values(1, 'MEMBER');