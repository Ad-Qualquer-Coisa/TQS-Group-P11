# TQS-Group-P11 — [Product name]

![CI](https://github.com/Ad-Qualquer-Coisa/TQS-Group-P11/actions/workflows/ci.yml/badge.svg)
<!-- Badge activates once the CI workflow (ci.yml) is added. -->

## 1. Project abstract

**[Product name]** is a light-mobility fleet-as-a-service platform developed for the
*Teste e Qualidade de Software* (TQS) course at Universidade de Aveiro, 2026.

**Concretization — tourist mobility.** A specialized operator provides a fleet of e-bikes and
e-scooters to [fictional hotel group] in [fictional city]. Visitors buy day or multi-day passes
(discounted when staying at a partner hotel), find available vehicles at hubs near points of
interest and follow suggested routes between hubs, without an institutional account. The operator
manages the fleet, its maintenance and the rebalancing of vehicles between hubs, while the client
defines the rules of use and follows SLAs and usage KPIs. Tourism-specific challenges include pass
pricing and validity, guest users, seasonal demand peaks and a multilingual interface.

## 2. Project team

| Member           | Role                         |
|------------------|------------------            |
| Miguel Neto      | Product Owner / Developer    |
| Diogo Silva      | Team Coordinator / Developer |
| Fernando Santos  | QA Engineer / Developer      |
| Bernardo Reis    | DevOps Master / Developer    |

Roles: Team Coordinator, Product Owner, QA Engineer, DevOps Master. Every member is also a Developer.

## 3. Project bookmarks

| Resource                         | Link |
|----------------------------------|------|
| Jira backlog                     | [Click here](https://tqs-group11.atlassian.net/jira/software/projects/KAN/boards/1/backlog) |
| Related repositories             | [TBD — none yet] |
| API documentation (OpenAPI/Swagger) | [TBD] |
| Static analysis dashboard (SonarQube) | [Click here](https://sonarcloud.io/project/overview?id=Ad-Qualquer-Coisa_TQS-Group-P11) |
| Observability dashboard (Grafana)   | [TBD] |
| Deployed application             | [TBD] |
| Product Specification report     | [docs/](docs/) |
| QA Manual                        | [docs/](docs/) |

## Repository structure

```
README.md
AGENTS.md            agent instructions (AI2)
.github/             workflows, pull request template
docs/                reports (PDF), presentation
docker-compose.yml   whole stack as code: backend, frontend, PostgreSQL
.env.example         variable names for .env (copy, then set the values)
proj/backend/        Spring Boot (Java 21) backend
proj/frontend/       web UI (React, Vite, TypeScript; nginx in the container)
proj/simulator/      fleet telemetry simulator (separate container)
```

## Running locally

Requires Docker with Docker Compose. Nothing else (no local Java, Maven or Node).

```bash
cp .env.example .env          # then edit .env and set your own password
docker compose up --build     # backend, frontend and PostgreSQL
```

Open http://localhost (or the `FRONTEND_PORT` set in `.env`). The page shows `Backend status: UP` when the
frontend reaches the backend and the backend reaches the database. `docker compose down` stops the
stack and keeps the data; `docker compose down -v` also deletes the database volume.

## Contributing

All changes go through a Pull Request from a feature branch named after the Jira story
(`KAN-XX-short-description`). Direct pushes to `main` are blocked; merging requires at least one
approval from another team member.
