CREATE TABLE IF NOT EXISTS campaigns
     (
         id       BIGINT PRIMARY KEY AUTO_INCREMENT,
         name     VARCHAR(255) NOT NULL,
         budget   DECIMAL      NOT NULL,
         spending DECIMAL      NOT NULL
     );

CREATE TABLE IF NOT EXISTS keywords
     (
         id       BIGINT PRIMARY KEY AUTO_INCREMENT,
         text     VARCHAR(255) NOT NULL,
         campaign_id BIGINT NOT NULL REFERENCES campaigns(id)
     );

-- CREATE SEQUENCE HIBERNATE_SEQUENCE START WITH 1 INCREMENT BY 1;
