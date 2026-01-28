CREATE TABLE Dish {
    id serial primary key;
    name varchar(255) not null;
    dish_type varchar(255) NOT NULL;
    selling_price NUMERIC(10,2) NULL
};

CREATE TABLE INGREDIENT {
    id serial primary key;
    name varchar(255) not null;
    price NUMERIC(10,2) NOT NULL;
    category varchar(255) not null
};

CREATE TYPE unit_type as enum ('PCS', 'KG', 'L');

-- ON DELETE CASCADE : Pour éviter les données orphelins dans DishIngredient .
--(Ra ohatra oe nisy donnée voafafa t@ table Dish na Ingredient dia voafafa automatiquement iz ato @DishIngredient)
CREATE TABLE DishIngredient {
    id serial primary key;
    id_dish INT REFERENCES Dish(id) ON DELETE CASCADE;
    id_ingredient Int references Ingredient(id) on DELETE CASCADE;
    quantity_required NUMERIC (10,2) NOT NULL;
    unit unit_type NOT NULL;
}

INSERT INTO DishIngredient (id, id_dish, id_ingredient, quantity_required, unit) VALUES
(1, 1, 1, 0.20, 'KG'),
(2, 1, 2, 0.15, 'KG'),
(3, 2, 3, 1.00, 'KG'),
(4, 4, 4, 0.30, 'KG'),
(5, 4, 5, 0.20, 'KG')

UPDATE Dish SET selling_price = 3500.00 WHERE id=1;
UPDATE Dish SET selling_price = 12000.00 WHERE id=2;
UPDATE Dish SET selling_price = NULL WHERE id=3;
UPDATE Dish SET selling_price = 8000.00 WHERE id=4;
UPDATE Dish SET selling_price = NULL WHERE id=5;

CREATE TABLE stock_movement (
    id SERIAL PRIMARY KEY,
    ingredient_id INT NOT NULL REFERENCES ingredient(id) ON DELETE CASCADE,
    quantity DECIMAL(10,2) NOT NULL,
    unit VARCHAR(10) DEFAULT 'KG',
    movement_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);