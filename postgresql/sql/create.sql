CREATE TYPE title AS ENUM ('AGENT', 'SUPERVISOR', 'ADMIN');

CREATE TABLE employee
(
    id              SERIAL                   NOT NULL,
    name            VARCHAR(100)             NOT NULL,
    start_date      DATE                     NOT NULL,
    team            VARCHAR(20)              NOT NULL,
    title           title                    NOT NULL,
    PRIMARY KEY (id)
);
