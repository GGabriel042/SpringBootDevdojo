insert into "user" (id, email,first_name,last_name) values (1, 'Rodolfonso@teste.com','Rodolfo','Afonso');
insert into "user" (id, email,first_name,last_name) values (2, 'yusuke@teste.com','Yusuke','Urameshi');
insert into profile (id, description, name) values (1, 'Manages everything','Admin');
insert into user_profile (id, user_id, profile_id) values (1,1,1);
insert into user_profile (id, user_id, profile_id) values (2,2,1);