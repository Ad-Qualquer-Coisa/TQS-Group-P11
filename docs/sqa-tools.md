# SQA tools (draft)

Tools chosen for the project's software quality assurance, and why. It feeds sections 3.3 (static analysis and quality gates), 5.1 and 5.2 of the QA Manual. Status: **adopted** means configured in the repository; **planned** means chosen, to be configured when the related story starts.

## Choices

| Area | Tool | Status | Why |
|------|------|--------|-----|
| Static analysis and dashboard | SonarCloud | adopted | The guidelines require a dashboard with a blocking quality gate (Q1, Q2). Hosted, so there is no server to run; analyses pull requests and `main`; free for public repositories |
| Code coverage | JaCoCo | adopted | Standard for Maven; one report covers unit and integration tests and feeds SonarCloud. A Maven check also fails the build below 90 % line and branch coverage of `domain` and `application` |
| Mutation testing | PIT (with the JUnit 5 plugin) | adopted | Coverage shows which lines ran, not whether the tests would notice a bug. PIT changes the code and checks that a test fails. It also exposes tautological tests. Slow, so it runs on request and on pull requests, with a minimum mutation score of 80 % on `domain` |
| Architecture tests | ArchUnit | adopted | Turns the hexagonal rules (domain knows no framework or adapter, modules form no cycles, application knows no adapter) into tests that fail the build when broken |
| Code formatting | Spotless with google-java-format | adopted | One style (Google Java Style) applied by a tool, so reviews are not about formatting. `spotless:check` runs in `verify` |
| Unit and integration tests | JUnit 5, AssertJ, Mockito (Spring Boot test starter), Surefire and Failsafe | adopted | Unit tests (`*Test`) and integration tests (`*IT`) run in separate Maven phases (T2, T3) |
| Integration with real services | Testcontainers | planned | Real PostgreSQL, MQTT broker and simulated payment gateway in tests (T3). Added with the first persistence story |
| Acceptance tests (BDD) | Cucumber | planned | Gherkin scenarios for core stories, tagged with business rule IDs (T1) |
| Test management | Jira with Xray | planned | Required (PM5): tests linked to stories, CI results imported |
| Load testing | k6 | planned | Thresholds on a critical flow, checked against the SLOs (T5) |
| End-to-end tests | Playwright | planned | Web UI journeys, run on demand and not on every build (T4). Pairs with the React frontend |
| Dependency vulnerabilities | Dependabot | planned | Zero setup on GitHub, runs on every dependency (Q3) |

Not adopted for now: SpotBugs (SonarCloud already reports the same bug patterns, so it would add a plugin and build time without new findings), container image scanning (Trivy, valued practice, revisited when images are built), accessibility checks (axe or Lighthouse, valued practice).

## Quality gate

SonarCloud judges every pull request and `main`. The build fails when the gate fails.

| Scope | Condition |
|-------|-----------|
| New code | no new issues; coverage at least 80 %; duplicated lines at most 3 %; security hotspots 100 % reviewed |
| Overall | coverage at least 80 %; duplicated lines at most 3 %; security hotspots 100 % reviewed; ratings A for reliability, security and maintainability |

If SonarCloud's free plan does not allow a custom gate, its built-in "Sonar way" gate applies and the JaCoCo check in Maven remains the blocking gate for coverage.

## Running the tools

```bash
cd proj/backend
./mvnw verify                  # unit tests, integration tests, JaCoCo report and check, Spotless check
./mvnw spotless:apply          # fix formatting
./mvnw verify -Pmutation       # also run mutation testing (target/pit-reports/index.html)
```

The JaCoCo report is `target/site/jacoco/index.html`. In CI, `verify` runs on every push and pull request, mutation testing on pull requests, and the SonarCloud analysis on pull requests from this repository and on `main`.
