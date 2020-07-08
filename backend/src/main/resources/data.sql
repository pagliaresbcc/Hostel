insert into address(address_name, zip_code, city, state, country) values('rua 2', '13900000', 'Amparo', 'SP', 'Brasil');
INSERT INTO Customer(title, name, last_Name, birthday, address_ID, email, password) VALUES('MRS.', 'Aluno', '2', DATE('2019-09-01'), 1, 'aluno@email.com', '$2a$10$ztYTBinS/LitQOno2jjwf.x7aNLRPs0iO1pIQ9ITqtNwTMybwV/MW');
insert into daily_rate(price) values(500);
insert into room(number, dimension, daily_rate_id) values(13, 230, 1),(14, 230, 1),(15, 230, 1),(16, 230, 1),(11, 230, 1);