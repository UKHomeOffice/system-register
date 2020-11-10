CREATE TABLE eventstore.snapshots (
    id serial NOT NULL PRIMARY KEY,
    object_type TEXT NOT NULL,
    object_data bytea NOT NULL
);