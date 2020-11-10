CREATE ROLE r_read_eventstore_ro NOSUPERUSER INHERIT NOCREATEDB NOCREATEROLE NOREPLICATION;
GRANT USAGE ON SCHEMA eventstore to r_read_eventstore_ro;
GRANT SELECT ON ALL TABLES IN SCHEMA eventstore to r_read_eventstore_ro;
ALTER DEFAULT PRIVILEGES IN SCHEMA eventstore GRANT SELECT ON TABLES TO r_read_eventstore_ro;

CREATE ROLE g_eventstorereaders_ro NOSUPERUSER INHERIT NOCREATEDB NOCREATEROLE NOREPLICATION;
GRANT r_read_eventstore_ro to g_eventstorereaders_ro;

CREATE ROLE ${api-username} WITH LOGIN ;
ALTER ROLE ${api-username} WITH PASSWORD '${api-password}';
ALTER ROLE ${api-username} VALID UNTIL 'infinity' ;
GRANT g_eventstorereaders_ro TO ${api-username};