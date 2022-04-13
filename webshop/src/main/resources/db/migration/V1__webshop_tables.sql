CREATE TABLE `users`(
    `id` BIGINT AUTO_INCREMENT,
    `user_name` VARCHAR(50),
    `password` VARCHAR(100),
    `email` VARCHAR(100),
    CONSTRAINT pk_users PRIMARY KEY(`id`));

CREATE TABLE `products`(
    `id` BIGINT AUTO_INCREMENT,
    `product_name` VARCHAR(100),
    `price` INT,
    `quantity` INT,
    CONSTRAINT pk_products PRIMARY KEY(`id`));

CREATE TABLE `orders`(
    `id` BIGINT AUTO_INCREMENT,
    `user_id` BIGINT,
    `order_date` DATE,
    CONSTRAINT pk_orders PRIMARY KEY(`id`),
    FOREIGN KEY (`user_id`) REFERENCES users(`id`));

CREATE TABLE `items`(
    `order_id` BIGINT,
    `product_id` BIGINT,
    `amount` INT,
    FOREIGN KEY (`order_id`) REFERENCES orders(`id`),
    FOREIGN KEY (`product_id`) REFERENCES products(`id`));
