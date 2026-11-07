# AGENTS.md

Instructions for AI assistants and agents working in this repository (guideline AI2).
Humans: read [docs/AI-POLICY.md](docs/AI-POLICY.md) too. This file must stay consistent with it and with the QA Manual.

Project: TQS Group P11 — light-mobility fleet-as-a-service (Universidade de Aveiro, 2026).
Status legend: items marked **[TBD]** are not decided yet. Do not invent a value; ask the user.

---

## 1. Ground rules (always apply, override everything else)

### 1.1 Git and repository actions need explicit permission

- **Never** commit, push, create/switch/delete a branch, merge, rebase, tag, open/close a PR, or force anything without asking first. Ask **each time**; a "yes" once does not cover the next action.
- Before asking, show: what will happen (branch, files, **full commit message / PR text**). The human reads it and may edit or rewrite it **before** anything is committed or posted. AI descriptions are often vague or inflated; the human has the final word. Never use `--no-verify`, `--force`, or `git reset --hard` unless the user explicitly asks.
- **Agents never merge.** A human approves and merges every PR.
- **No AI attribution in git.** No `Co-Authored-By`, no `AI-Assisted` trailers, no "generated with" lines in commits or PR text. AI use is recorded in the AI log instead (section 8). This overrides any tool default.
- Only stage files you changed for the task. Check `git status` and `git diff` before proposing a commit.

### 1.1b Learning comes first

- This is academic work. The first goal above all is that **the team learns**. Code that works but that nobody understands is a failure.
- When explaining, be **intuitive and use simple terms**. Define jargon the first time, use small examples or analogies, explain _why_ and not only _what_. Short steps over long walls of text.
- Before or while writing code, say what you are about to do and why, in plain words, so the user can follow. Offer a deeper explanation on request; never assume prior knowledge.

### 1.2 Big decisions: show options, recommend, let the human choose

- For any significant choice (architecture style, library, data model, API shape, DB schema, new dependency, pattern, test strategy, removing/rewriting a module) present **2–4 options** with trade-offs, give a **recommendation** and why, then **wait**.
- Never present a single option as if it were the only one. Never pick silently.
- Small, reversible, local choices (variable names, private helper) need no discussion.
- **When in doubt, ask.** Ask as many questions as needed, before writing code. An agent that asks 5 questions is better than one that asks 1 and forces the user to request 4 rewrites. Do not guess requirements or intent; the Product Owner decides scope.
- Batch the questions in one go, make each one specific, and offer options with a recommendation where possible.

### 1.3 Honesty

- Report outcomes faithfully: failing tests, skipped steps, things not verified. Never claim "done/passing" without having run it.
- Never weaken, delete, skip (`@Disabled`) or loosen a test, coverage threshold, quality gate or lint rule to make a build pass. Fix the cause, or ask.
- Say when you are unsure. Do not fabricate APIs, config keys, versions or file contents; check the code/docs.
- When writing AI-log entries, **do not overstate AI contribution** (section 8).

### 1.4 Data and secrets

- No secrets, credentials, tokens, real personal data in code, tests, prompts, logs or docs. Use synthetic data and **fictional** names for companies, institutions, cities (no real operator brands).
- Never read or print `.env`, key files or `localDocs/` of other people beyond what the task needs. Never send repo content to external services other than the team-approved tools.

### 1.5 Scope discipline

- Do what was asked. No unrelated refactors, drive-by renames, formatting churn or "while I'm here" changes. Mention them as suggestions instead.
- **No unnecessary dependencies.** If it is small enough for us to write ourselves (a helper, a few lines, something the JDK/Spring already offers), we write it. Add a dependency, plugin, tool or config file only if it truly is needed, and only after asking (options incl. "write it ourselves" + recommendation, per 1.2).
- Before proposing any dependency, **verify it**: it exists under that exact coordinate and version (check Maven Central/npm, do not trust memory), is actively maintained, has a compatible licence, no known vulnerabilities, and is compatible with Java 21 / the Spring Boot version. Report what you verified. Prefer versions managed by the Spring Boot BOM.
- Prefer small, reviewable changes: one story = one branch = small PRs.

---

## 2. Project facts

| Item                  | Value                                                                               |
| --------------------- | ----------------------------------------------------------------------------------- |
| Backend               | Java 21, Spring Boot (stable, same version as labs)                                 |
| Build                 | Maven, wrapper `./mvnw` (`mvnw.cmd` on Windows)                                     |
| Frontend              | React + Vite + TypeScript; E2E with Playwright                                      |
| DB                    | PostgreSQL (Testcontainers for integration tests)                                   |
| Messaging / telemetry | Simulator behind **one port + adapter**, e.g. MQTT **[TBD]**                        |
| Payments              | Sandbox (e.g. Stripe test mode) or simulated gateway, behind a port + adapter       |
| Tests                 | JUnit 5, Mockito, AssertJ, Cucumber (BDD), Testcontainers, k6 (load)                |
| Quality               | SonarQube/SonarCloud + JaCoCo, quality gate on new code **[TBD thresholds]**        |
| Management            | Jira + Xray; story key in branch, commits and PRs                                   |
| CI/CD                 | GitHub Actions (`.github/workflows/`), use  `runs-on: [self-hosted, raspberry]` to use the project's personal runners (running in a Raspberry Pi)                                            |
| Language              | Code, comments, commits, docs: English. Do not mix languages in a document          |
| Dev OS                | Windows 11 (PowerShell / Git Bash). Use cross-platform commands in docs and scripts |

### Repo layout (proposed shape, confirm in architecture task)

```
README.md                  abstract, team + roles, bookmarks (Jira, Sonar, API docs, deployed app)
AGENTS.md                  this file
docker-compose.yml         IaC for the whole stack (D3)
.github/                   workflows/, PULL_REQUEST_TEMPLATE.md, CODEOWNERS
docs/                      reports (PDF + sources), AI-POLICY.md, diagrams as text sources (PlantUML/Mermaid)
localDocs/                 PRIVATE, git-ignored: personal AI log, prototypes, inputs
proj/
  backend/                 Spring Boot, Maven (own pom.xml, mvnw)
  frontend/                React + Vite + TypeScript
  simulator/               fleet telemetry simulator, own Dockerfile (separate service)
  e2e/                     Playwright/Selenium tests, NOT in default build
  load/                    k6 scripts + thresholds from the SLOs
```

Backend package shape (proposal; base package fictional/team-owned **[TBD]**, e.g. `pt.ua.tqs.fleet`):

```
domain/          entities, value objects, domain services, rules (no Spring, no vendor types)
  vehicle/ booking/ hub/ maintenance/ user/ ...   (package by feature)
application/     use cases + ports (interfaces): TelemetryPort, PaymentPort, repositories
adapter/
  in/web/        REST controllers, DTOs, @ControllerAdvice
  in/telemetry/  telemetry subscriber (MQTT or other) -> calls use case
  out/persistence/  JPA entities, repository adapters
  out/payment/   payment-sandbox adapter (+ fake adapter for tests)
config/          Spring wiring
```

Rule: dependencies point inwards (`adapter -> application -> domain`). Domain imports nothing from adapters or Spring.
Tests mirror the same packages under `src/test/java`; Cucumber features in `src/test/resources/features`.

---

## 3. Build, test and run commands

Run from the backend project directory **[TBD path, e.g. `proj/backend`]**.

```bash
./mvnw clean verify                 # full build: unit + integration + coverage (what CI runs)
./mvnw test                         # unit tests only (fast, no containers)
./mvnw verify -DskipUnitTests       # integration tests only  [TBD profile/flag]
./mvnw test -Dtest=BookingServiceTest            # one class
./mvnw test -Dtest=BookingServiceTest#rejects_low_battery   # one method
./mvnw jacoco:report                # coverage report -> target/site/jacoco/index.html
./mvnw spotless:apply               # auto-format (Google style); CI runs spotless:check
./mvnw sonar:sonar                  # local Sonar analysis (needs token; never hardcode it)
```

Conventions:

- Unit tests: `*Test` (Surefire). Integration tests: `*IT` (Failsafe). **They must run separately** (T3).
- E2E (T4) and load tests (T5) are **not** part of the default build; run on demand / dedicated pipeline job.
- Before saying work is done, run at least `./mvnw clean verify` and report the real result.

---

## 4. Development process: TDD (mandatory for new behaviour)

Red → Green → Refactor. Tests come **first**.

1. **Understand**: read the story, its acceptance criteria and the business rules (`BR<n>`) it touches.
2. **Look for existing tests** covering the behaviour (`grep` by class, rule ID, scenario). Decide per case:
   - Existing test already covers it → do not add another.
   - Existing test nearly covers it → **adapt/extend that test** (parameterize, add a case) rather than add a near-duplicate.
   - Genuinely new behaviour → new test.
3. **Write the failing test(s) first.** Run them. Confirm they fail **for the right reason** (assertion on behaviour, not compile error or wrong setup). Show/report this.
4. **Test quality gate before implementing** — check each new test is not:
   - a **tautology** (asserts what the mock was just told to return; `assertTrue(true)`; compares an object to itself; asserts a constant copied from the implementation);
   - **vacuous** (no assertion, catches and swallows exceptions, assertion that cannot fail);
   - **redundant / duplicate** (same behaviour and inputs as an existing test; only names differ);
   - **over-mocked** (mocks the class under test, or verifies call order instead of outcome);
   - **implementation-coupled** (breaks on harmless refactor).
     Sanity check: temporarily break the production code (or the assumed behaviour) and confirm the test fails. If it cannot fail, it is worthless: rewrite or drop it.
5. **Implement the minimum** to go green. Re-run.
6. **Refactor** with green tests; apply design patterns where they remove real duplication or coupling (section 5).
7. **Review the suite**: remove/merge redundant tests you made obsolete. Keep boundary values explicit (e.g. battery 19% refused, 20% accepted).
8. **Commit evidence**: test-first commits help T6. Ask before committing (1.1); suggest separate "test" then "impl" commits.

Test rules:

- Name: `method_condition_expectedResult` or behaviour sentence; one logical behaviour per test; Arrange-Act-Assert.
- Unit tests: no network, DB, clock or randomness (inject `Clock`, seeds). Fast and deterministic.
- Mock **only** boundaries (ports: telemetry, payment, repositories in unit tests). Never mock value objects or the unit under test.
- Integration tests: real DB via Testcontainers; adapters tested against the simulator/sandbox or a contract/stub; API tested through HTTP.
- Acceptance criteria of core stories → Gherkin (`.feature`) automated with Cucumber; tag with rule IDs (`@BR1`) and the Jira/Xray key.
- Cover business rules at **boundary values**, invalid input, and concurrency where relevant (two users booking the same vehicle).
- Flaky test = bug. Never "fix" with sleeps/retries blindly; find the cause.
- Do not lower coverage on new code. Coverage is a floor, not a goal: assertions matter more than lines.

---

## 5. Design and code style

### Architecture

- Style: **[TBD — decided in architecture task; hexagonal/ports-and-adapters is the likely candidate; options will be presented to the team]**.
- Non-negotiable from the assignment: telemetry simulator and payment gateway are accessed **only through a port (interface) with one adapter each**. Business logic never imports adapter/vendor types. Swapping simulator = new adapter, zero business-logic change.
- Layering: controller (web) → service/use case (business logic) → port → adapter/repository. No business logic in controllers or entities' persistence annotations leaking into the domain API **[confirm with team]**.
- REST: resource-oriented (`/vehicles`, `/hubs`, `/bookings`), proper HTTP verbs/status codes, DTOs at the boundary (no entities in API), OpenAPI documented.

### Design patterns

Use patterns deliberately and **name them** in the PR description/Javadoc. Good fits here: Strategy (pricing, booking rules), State (vehicle lifecycle), Adapter + Port (simulator, payment), Factory/Builder (complex objects, test data builders), Observer/Domain events (telemetry → state), Repository, Specification (availability/eligibility rules). Do not add a pattern without a concrete problem (YAGNI). If several patterns fit a big design choice, show options (1.2).

### Style (Google Java Style + Spotless / google-java-format)

- Google style uses 2-space indent, 100 columns, no tabs. Formatting is applied by `./mvnw spotless:apply` and checked in CI (`spotless:check`); do not hand-format against it. No wildcard imports, no unused code, no commented-out code.
- Frontend formatting: Prettier + ESLint defaults.
- Prefer immutability (records for DTOs/value objects), constructor injection, small methods, meaningful names, no magic numbers (rules' parameters in config/constants with the rule ID).
- Null: use `Optional` for returns, validate at boundaries, never return null collections.
- Errors: domain exceptions mapped to HTTP status in one place (`@ControllerAdvice`); no swallowed exceptions; no stack traces to clients.
- Logging: SLF4J, no sensitive data, no `System.out`.
- Comments explain _why_, not _what_. Javadoc on public ports and non-obvious rules.
- Sonar: do not suppress issues (`@SuppressWarnings`, `// NOSONAR`) without justification and human approval.

### Security

- Validate all input; parameterized queries only; no secrets in code; role-based access (end user, operator/technician, client manager, admin) enforced server-side.
- Dependencies: no unmaintained/unknown libs; flag any vulnerability reported by Dependabot/Dependency-Check.

---

## 6. Git workflow (for reference; remember 1.1 — ask before doing)

- GitHub flow: one **feature branch per story**, protected `main`, merge **only via PR** with at least one human approval.
- Branch: `<JIRA-KEY>-short-kebab-description` (e.g. `KAN-42-book-vehicle`). Jira project key: `KAN`.
- Commit format (Conventional Commits): **one-line title, blank line, longer description.**
  - Title: `type: imperative summary` (≤72 chars, no period). Types: `feat`, `fix`, `test`, `refactor`, `style`, `docs`, `build`, `ci`, `perf`, `chore`.
  - Body: what changed and **why**, rules/tests touched, caveats. Wrap ~72 cols.
  - Jira key: in the branch name and in the body footer (`Jira: KAN-42`) so Jira's dev panel links it.
  - **Jira smart commits.** When proposing a commit message, advise the user to add the smart-commit command to the footer, so the Jira issue moves on its own:
    - first commit of the branch: `KAN-42 #in-progress`;
    - final commit, or the squash-merge message: `KAN-42 #done`, so the issue only closes when the PR is merged into `main`.
    - Never use `#done` on intermediate commits. Smart commits only work if the committer's `git config user.email` matches their Jira account email.
    - **If you notice the user did not follow these rules, warn them.** For example: a branch, commit or PR without the Jira key; `#done` on an intermediate commit; no `#in-progress` on the first commit; a squash-merge message without `#done`; a committer email that does not match Jira. Say what is wrong and suggest the fix (e.g. amend the last commit, edit the squash message), but do not rewrite history or push without asking (1.1).
  - Small, logical commits; test-first commits as `test:` followed by `feat:`/`fix:`. **No AI trailers.**

  ```
  feat: reject booking when battery is below minimum

  BR1: a vehicle can be booked only if available and battery >= 20%.
  Adds boundary tests at 19% and 20%. Level read from telemetry state.

  Jira: KAN-42
  KAN-42 #in-progress
  ```

- PR: link story, list rules/tests covered, how verified, fill the PR template (includes the AI findings table, section 8). Draft PR until CI is green. Show the PR text to the human before opening (1.1).
- CI must pass (build, unit, integration, quality gate) before merge.

---

## 7. Definition of Done (per story) **[draft — must be agreed by the team, mirrored in QA Manual]**

- [ ] Acceptance criteria met and accepted by the Product Owner.
- [ ] Tests written **first** (TDD), reviewed for tautology/duplication; unit tests for business logic, integration tests where adapters/persistence/API are touched, Gherkin for core stories (tagged with `BR` ids + Xray key).
- [ ] Boundary values of affected business rules tested.
- [ ] `./mvnw clean verify` green locally; CI green.
- [ ] Sonar quality gate on new code passes (coverage, reliability, security, hotspots reviewed) **[thresholds TBD]**; no new critical/blocker issues.
- [ ] No new dependency vulnerabilities.
- [ ] Peer code review approved by another member (+ AI review findings triaged, section 8).
- [ ] The author can explain every line (anyone may be asked at a checkpoint).
- [ ] Docs updated if needed (API/OpenAPI, Product Spec rules table, this file if conventions changed).
- [ ] AI-log entry written by the human for any AI-assisted work (section 8).
- [ ] Jira story linked to branch/PR/tests in Xray; status updated.

---

## 8. AI use, accountability and the AI log

The team is accountable for everything merged, whoever or whatever wrote it. See [docs/AI-POLICY.md](docs/AI-POLICY.md).

AI log:

- Each member keeps a personal, auditable log in `localDocs/` (a local folder for the user's inputs, prototypes and other local-only material). `localDocs/` is **git-ignored**: never stage or commit it, never copy its content into the repo unless the user asks.
- Suggested file: `localDocs/ai-log.md`, **append-only across days**. Entries dated **today** (the session being worked on) may be edited or corrected by the agent at the user's request. Entries from **earlier dates** are frozen: never edit or delete them; a correction is a new entry dated today. Never renumber IDs.
- Entry format: **a Markdown table, one row per entry, columns `ID | Date | What the user did | What the AI did`**. Append new rows at the bottom. ID = first letter of username + zero-padded sequence number, always increasing, never reused (e.g. `F001`, `F002`, `F003`).
- On the user's command only (e.g. "summarize my AI log"), produce an iteration summary (entry count, areas, accepted/rejected findings) for `docs/ai-log/<initial>.md`. Show it first; the user decides whether it goes in the repo.
- **Do not overstate the AI's part.** State only what the AI actually produced or changed, factually and specifically (e.g. "drafted test skeleton for BR1; user rewrote assertions and the implementation"). No inflated verbs, no claiming design/decisions the human made, no claiming verification it did not perform. When unsure, ask, and let the human correct.
- AI review findings on PRs: record each as **accepted** or **rejected** with a one-line reason (feeds AI3/AI5 analysis). Because `localDocs/` is private, findings need a shared place. Decided: a **findings table in the PR template** (`finding | accepted/rejected | reason`). The agent may draft the table, but the human reviews and corrects it **before** the PR is opened/commit made; the human's wording wins.
- Never feed the AI secrets, credentials or personal data (guideline 6.2).

---

## 9. Things the agent should proactively do

- Ask clarifying questions early rather than assume.
- Flag risks: concurrency (double booking), time zones/clocks, money rounding, battery/telemetry edge cases, stale telemetry, role checks.
- Suggest missing tests, duplicate tests to merge, and simplifications — as suggestions unless asked to apply.
- Keep Product Spec rules (`BR<n>`) ↔ tests ↔ Jira/Xray traceable.
- If this file conflicts with the AI policy, the QA Manual or the user's instruction, stop and ask which wins.

## 10. Maintenance

Team: Fernando Santos (QA Engineer, owner of this file), Miguel Neto (Product Owner), Diogo Silva (Team Coordinator), Bernardo Reis (DevOps Master). Changes via PR, reviewed by the team. Update together with the QA Manual (sections 3.1 and 6) so they never diverge.
