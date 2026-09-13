CREATE TABLE categories
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY,
    parent_id   BIGINT,
    name        VARCHAR(50)  NOT NULL,
    description VARCHAR(255),
    created_at  TIMESTAMP(6) NOT NULL,
    updated_at  TIMESTAMP(6) NOT NULL,
    PRIMARY KEY (id),
    -- 자기 참조: 하위 카테고리가 남아 있으면 부모 삭제를 막는다
    CONSTRAINT fk_categories_parent FOREIGN KEY (parent_id) REFERENCES categories (id) ON DELETE RESTRICT,
    CONSTRAINT ck_categories_not_self_parent CHECK (parent_id IS NULL OR parent_id <> id)
);

CREATE INDEX idx_categories_parent_id ON categories (parent_id);
