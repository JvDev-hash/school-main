CREATE TABLE Section (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(30) NOT NULL,
    title VARCHAR(255) NOT NULL,
    author_username VARCHAR(500),
    course_code VARCHAR(30) NOT NULL
);