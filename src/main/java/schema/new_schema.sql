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