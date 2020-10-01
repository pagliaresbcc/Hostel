insert into address(address_name, zip_code, city, state, country) 
	values ('rua 2', '13900000', 'Amparo', 'SP', 'Brasil');
	
INSERT INTO Customer(title, name, last_Name, birthday, address_ID, email, password) 
	VALUES ('MRS.', 'Aluno', '2', DATE('2019-09-01'), 1, 'aluno@email.com', '$2a$10$ztYTBinS/LitQOno2jjwf.x7aNLRPs0iO1pIQ9ITqtNwTMybwV/MW');

insert into daily_rate(price) values (300), (120), (120), (200), (100);

insert into room(description, number, dimension, max_number_of_guests, daily_rate_id) 
	values 	('Quarto com uma cama de casal, duas camas de solteiro e varanda', 13, 500, 4, 1),
			('Quarto com uma cama de casal', 14, 250, 2, 2),
			('Quarto com uma cama de casal e duas camas de solteiro', 15, 460, 4, 3),
			('Quarto com uma cama de casal e com varanda', 16, 400, 2, 4),
			('Quarto com duas camas de solteiro', 11, 230, 2, 5);