# System Register

- What systems are in my organisation?
- Where is the risk?
- Who do I talk to about that?

Crowdsource systems and the risks they contain.

## Prerequisites

- pre-commit <sup>†</sup>
- Java 11+ JDK
- Node 10
- Maven
- Yarn
- Docker & docker-compose
- Python 3

<sup>†</sup> There are Git hooks in this repository that perform linting and security-related checks before commits.
Use pre-commit to install these:

```bash
pre-commit install
```

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

## Detecting secrets

The pre-commit Git hook checks changes for possible secrets using [detect-secrets](https://github.com/Yelp/detect-secrets).
These checks look for keywords and regions of randomness that might be passwords.

As the checks are heuristic-based, false-positives are possible. If, after reviewing any rejected commits, you’re
confident that there are no secrets, run the following command and include the `.secrets.baseline` file in the commit:

```bash
detect-secrets scan > .secrets.baseline
```

For this to work, you’ll need to have the `detect-secrets` tool installed directly. `pre-commit` has its own copy which
isn’t normally accessible. To install it, use `pip` (or `pip3`):

```bash
pip install detect-secrets
```
