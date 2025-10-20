CREATE TABLE bug_submission_entity (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    image VARCHAR(512),
    name VARCHAR(255),
    latin VARCHAR(255),
    confidence INT,
    hint TEXT,
    detail VARCHAR(100),
    submitted_by VARCHAR(255),
    anonymous BOOLEAN NOT NULL,
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE bug_entity (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    image VARCHAR(512) NOT NULL,
    name VARCHAR(255) NOT NULL,
    latin VARCHAR(255),
    confidence INT NOT NULL,
    submitted_by VARCHAR(255) NOT NULL,
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE bug_entity_tags (
    bug_id BIGINT NOT NULL,
    tag VARCHAR(100) NOT NULL,
    FOREIGN KEY (bug_id) REFERENCES bug_entity(id) ON DELETE CASCADE,
    PRIMARY KEY (bug_id, tag)
);
