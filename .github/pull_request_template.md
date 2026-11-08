## Jira issue

<!-- Replace KAN-XX with the story key. The key must also appear in the branch name and commit messages (PM3). -->
Jira: [KAN-XX](https://tqs-group11.atlassian.net/browse/KAN-XX)

## Summary

<!-- What does this PR change, and why? One or two sentences. -->

## Type of change

- [ ] Feature (new user story)
- [ ] Bug fix
- [ ] Tests only
- [ ] Refactoring (no behaviour change)
- [ ] Documentation
- [ ] CI / infrastructure

## Business rules and tests

<!-- Rule IDs from the Product Spec (e.g. BR1) and the tests that cover them. Delete if not applicable. -->

| Rule | Tests (unit / integration / Gherkin) |
|------|--------------------------------------|
|      |                                      |

## Design patterns

<!-- Name any pattern applied (Strategy, State, Adapter + Port, ...) and the problem it solves. Delete if none. -->

## How was it tested?

<!-- Commands run and their real result, e.g. `./mvnw clean verify` green. -->

## Specification-driven change (AI4)

<!-- Only if this PR was generated from a specification: link the Gherkin feature or OpenSpec proposal. Otherwise delete this section. -->

## AI review findings

<!-- One row per finding from the AI-assisted review (AI3). The human reviewer checks and corrects this table; the human's wording wins. -->

| Finding | Accepted / Rejected | Reason |
|---------|---------------------|--------|
|         |                     |        |

## Author checklist

- [ ] Branch name and commits include the Jira key (e.g. `KAN-XX-short-description`, footer `Jira: KAN-XX`)
- [ ] Smart commits: `KAN-XX #in-progress` on the first commit; `KAN-XX #done` only in the squash-merge message
- [ ] Acceptance criteria of the story are met
- [ ] Tests written first (TDD) and checked for tautologies and duplicates
- [ ] Boundary values of affected business rules tested
- [ ] `./mvnw clean verify` green locally; CI green (build, tests, SonarQube quality gate)
- [ ] Code formatted (`./mvnw spotless:apply`)
- [ ] No new dependency without team approval; no secrets, credentials or personal data committed
- [ ] Documentation updated if needed (README, Product Spec rules table, OpenAPI, AGENTS.md)
- [ ] AI-log entry written for any AI-assisted work (kept in `localDocs/`)

## Reviewer checklist

- [ ] Code is readable and follows the contributor guidelines (AGENTS.md)
- [ ] Logic matches the story and its business rules (BR IDs)
- [ ] Tests cover the change, including boundary values and edge cases
- [ ] No new SonarQube issues or unreviewed security hotspots
- [ ] AI review findings table checked and corrected
- [ ] I can explain this change at a checkpoint
