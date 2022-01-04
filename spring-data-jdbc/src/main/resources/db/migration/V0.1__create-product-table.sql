CREATE TABLE product (
    id                      BIGSERIAL NOT NULL,
    name                    VARCHAR NOT NULL,
    price                   NUMERIC NOT NULL,
    created                 TIMESTAMP NOT NULL,
    modified                TIMESTAMP NOT NULL,
    PRIMARY KEY(id)
);
