CREATE TABLE eventstore.events (
    id serial NOT NULL PRIMARY KEY,
    time_stamp TIMESTAMP NOT NULL, 
    object_type TEXT NOT NULL,
    object_data bytea NOT NULL
);