SET MODE MYSQL;
CREATE TABLE products
(
    product_id     int PRIMARY KEY auto_increment,
    product_name   VARCHAR(20)  NOT NULL,
    category       VARCHAR(50)  NOT NULL,
    price          int          NOT NULL,
    stock_quantity int          NOT NULL,
    description    VARCHAR(500) NOT NULL,
    removed        BOOLEAN      NOT NULL DEFAULT 0,
    created_at     datetime(6)  NOT NULL,
    updated_at     datetime(6)  DEFAULT NULL
);

CREATE TABLE orders
(
    order_id     int PRIMARY KEY auto_increment,
    email        VARCHAR(50)  NOT NULL,
    address      VARCHAR(200) NOT NULL,
    postcode     VARCHAR(200) NOT NULL,
    order_status VARCHAR(50)  NOT NULL,
    created_at   datetime(6)  NOT NULL,
    updated_at   datetime(6) DEFAULT NULL
);

CREATE TABLE order_items
(
    order_item_id int PRIMARY KEY auto_increment,
    order_id   int  NOT NULL,
    product_id int  NOT NULL,
    price      int      NOT NULL,
    quantity   int         NOT NULL,
    created_at datetime(6) NOT NULL,
    updated_at datetime(6) DEFAULT NULL,
    INDEX (order_id),
    CONSTRAINT fk_order_items_to_order FOREIGN KEY (order_id) REFERENCES orders (order_id) ON DELETE CASCADE,
    CONSTRAINT fk_order_items_to_product FOREIGN KEY (product_id) REFERENCES products (product_id)
);