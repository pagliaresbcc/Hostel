insert into address(address_name, zip_code, city, state, country) 
	values ('Rua Duque de Caxias', '13900-000', 'Amparo', 'SP', 'Brasil');
	
INSERT INTO guest(title, name, last_Name, birthday, address_ID, email, password, role) 
	VALUES ('Mrs.', 'Maria', 'Silva', DATE('1972-11-08'), 1, 'maria@email.com', '$2a$10$ztYTBinS/LitQOno2jjwf.x7aNLRPs0iO1pIQ9ITqtNwTMybwV/MW', 'ROLE_ADMIN');
	
insert into address(address_name, zip_code, city, state, country) 
	values ('Rua Pintangueiras', '17900-000', 'Holambra', 'SP', 'Brasil');
	
INSERT INTO guest(title, name, last_Name, birthday, address_ID, email, password, role) 
	VALUES ('Sr.', 'Ivanilson', 'Silva', DATE('1990-09-01'), 1, 'ivanilson@email.com', '$2a$10$ztYTBinS/LitQOno2jjwf.x7aNLRPs0iO1pIQ9ITqtNwTMybwV/MW', 'ROLE_USER');

insert into daily_rate(price) values (500), (200), (300), (350), (150);

insert into room(description, number, dimension, max_number_of_guests, daily_rate_id) 
	values 	('Quarto com uma cama de casal, duas camas de solteiro e varanda', 13, 500, 4, 1),
			('Quarto com uma cama de casal', 14, 250, 2, 2),
			('Quarto com uma cama de casal e duas camas de solteiro', 15, 460, 4, 3),
			('Quarto com uma cama de casal e com varanda', 16, 400, 2, 4),
			('Quarto com duas camas de solteiro', 11, 230, 2, 5);