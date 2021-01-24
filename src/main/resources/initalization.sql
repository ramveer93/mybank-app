
CREATE DATABASE bank;

create user bank;

ALTER USER bank WITH PASSWORD 'bank';

grant all privileges on database bank to bank;