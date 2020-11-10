# User Model

**Roles** look like **r_*_ro** and give fine grain permission to a single schema, they are small
**Groups** look like **g_*_ro** and are a collection of finer grained roles and describe a use case
**Users** look like ***_user** and have logins and are the people and apps which will talk to the db, they contain 1 or more groups. They do not contain **Roles** directly

[Influence from here](https://severalnines.com/database-blog/how-secure-your-postgresql-database-10-tips)