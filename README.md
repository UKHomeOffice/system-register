# System Register

- What systems are in my organisation?
- Where is the risk?
- Who do I talk to about that?

Crowdsource systems and the risk they contain.

## Prerequisites

- pre-commit [^1]
- Java 11+ JDK
- Node 10
- Maven
- Yarn
- Docker & docker-compose

[^1]:
    There are Git hooks in this repository that perform linting before commits. Use pre-commit to install these:

    `pre-commit install`

## Development

There are 2 services, frontend (UI) and backend (API).

The UI is SPA react app created via create-react-app.

The API is a Quarkus based java app.

By default, the API serves up the UI but they are designed to be
independently deployable.

To build and start developing the API locally:

```bash
./tasks build-frontend
./tasks build-backend
./tasks dev-backend
```

This will build the UI, copy the files
over to be served up by the backend, build the backend
and start the backend in dev mode with file watch and
hot reload for the backend only.

If you want to develop the frontend with hot reload

```bash
./tasks dev-frontend
```
