CREATE TABLE products
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY,
    category_id BIGINT       NOT NULL,
    name        VARCHAR(100) NOT NULL,
    description TEXT,
    price       INT          NOT NULL,
    stock       INT          NOT NULL DEFAULT 0,
    status      VARCHAR(20)  NOT NULL DEFAULT 'FOR_SALE',
    created_at  TIMESTAMP(6) NOT NULL,
    updated_at  TIMESTAMP(6) NOT NULL,
    PRIMARY KEY (id),
    -- 상품이 속한 카테고리는 삭제할 수 없다
    CONSTRAINT fk_products_category FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE RESTRICT,
    CONSTRAINT ck_products_price CHECK (price >= 0),
    CONSTRAINT ck_products_stock CHECK (stock >= 0),
    CONSTRAINT ck_products_status CHECK (status IN ('FOR_SALE', 'STOP_SALE', 'OUT_OF_STOCK'))
);

-- 목록 검색(카테고리 필터, 가격 범위, 최신순/가격순 정렬)용 인덱스
CREATE INDEX idx_products_category_id ON products (category_id);
CREATE INDEX idx_products_price ON products (price);
CREATE INDEX idx_products_created_at ON products (created_at);
