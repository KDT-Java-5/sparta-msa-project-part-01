CREATE TABLE product_options
(
    id               BIGINT GENERATED ALWAYS AS IDENTITY,
    product_id       BIGINT       NOT NULL,
    name             VARCHAR(50)  NOT NULL,
    additional_price INT          NOT NULL DEFAULT 0,
    stock            INT          NOT NULL DEFAULT 0,
    created_at       TIMESTAMP(6) NOT NULL,
    updated_at       TIMESTAMP(6) NOT NULL,
    PRIMARY KEY (id),
    -- 상품이 삭제되면 옵션도 함께 삭제된다
    CONSTRAINT fk_product_options_product FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE,
    CONSTRAINT ck_product_options_additional_price CHECK (additional_price >= 0),
    CONSTRAINT ck_product_options_stock CHECK (stock >= 0)
);

CREATE INDEX idx_product_options_product_id ON product_options (product_id);
