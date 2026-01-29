CREATE TYPE ingredient_category AS ENUM ('VEGETABLE', 'ANIMAL', 'MARINE', 'DAIRY', 'OTHER');
CREATE TYPE dish_type_enum AS ENUM ('START', 'MAIN', 'DESSERT');

DO $$
BEGIN
    CREATE TYPE payment_status AS ENUM ('PAID', 'UNPAID');
EXCEPTION
    WHEN duplicate_object THEN NULL;
END $$;

CREATE TABLE Dish (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    dish_type dish_type_enum NOT NULL
);

CREATE TABLE Ingredient (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price NUMERIC(10, 2) NOT NULL,
    category ingredient_category NOT NULL,
    id_dish INT,
    FOREIGN KEY (id_dish) REFERENCES Dish(id)
);

ALTER TABLE Ingredient
ADD COLUMN IF NOT EXISTS required_quantity NUMERIC NULL;

CREATE TABLE IF NOT EXISTS sale (
    id SERIAL PRIMARY KEY,
    creation_datetime TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS orders (
    id SERIAL PRIMARY KEY,
    reference VARCHAR(255) NOT NULL UNIQUE,
    creation_datetime TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    payment_status payment_status NOT NULL DEFAULT 'UNPAID',
    id_sale INT UNIQUE NULL REFERENCES sale(id)
);

delete from dish-orders;
delete from order;
delete from sale;

INSERT INTO orders (id, reference, creation_datetime, payment_status, id_sale) VALUES
(1, '201', '2024-01-15 12:30:00', 'PAID', 1),
(2, '202', '2024-01-16 13:45:00', 'UNPAID', NULL);

INSERT INTO sale (id, creation_datetime) VALUES
(1, '2024-01-15 12:30:00');

