
CREATE TABLE product(
	id SERIAL,
	category varchar(255),
	date_added DATETIME DEFAULT CURRENT_TIMESTAMP,
	source_user SERIAL,
	PRIMARY KEY(id),
	FOREIGN KEY(source_user)
		REFERENCES user(user_id), --user table doesn't exist but we can pretend like it does
)

CREATE TABLE product_price(
	product_id SERIAL,
	product_price DECIMAL(12,2),
	discount_percent TINYINT UNSIGNED,
	date_updated DATETIME DEFAULT CURRENT_TIMESTAMP
		ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY(product_id),
	FOREIGN KEY(product_id)
		REFERENCES product(id),

)

CREATE TABLE price_change_log(
	id SERIAL,
	product_id SERIAL,
	old_price DECIMAL(12,2),
	new_price DECIMAL(12,2),
	source_user SERIAL,
	date_of_change DATETIME DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY(id),
	FOREIGN KEY(product_id)
		REFERENCES product(id),
	FOREIGN KEY(source_user)
		REFERENCES user(id)
)
