# Team AI Policy — TQS Group P11 (DRAFT v0.1)

Status: **draft, not yet approved.** Approval = all four members approve the PR that merges this file (record names/dates below).
Companion files: [AGENTS.md](../AGENTS.md) (agent instructions), QA Manual sections 6.1–6.6.
Course rules (guidelines 6.2) apply in addition and win on conflict.

## 1. Principles

1. **Intentional**: AI is a tool we choose to use for a reason, not a default.
2. **Accountable**: the team owns every merged change. Any member may be asked at a checkpoint to explain any merged code, whoever wrote it.
3. **Human decides**: agents never merge, never approve their own work; a human approves every PR.
4. **Honest**: we record AI use truthfully and do not overstate (or hide) what AI did.
5. **Verified**: AI output is untrusted until tested and reviewed.
6. **Learning first**: this is academic work; the first goal is that we learn. AI must help us understand, not replace our understanding.

## 2. Allowed uses

| Area                                                    | Allowed                 | Conditions                                                                          |
| ------------------------------------------------------- | ----------------------- | ----------------------------------------------------------------------------------- |
| Production code                                         | Yes                     | TDD, tests first; author understands every line; reviewed by a peer                 |
| Tests                                                   | Yes                     | Checked for tautologies, redundancy, duplicates; must fail when behaviour is broken |
| Documentation                                           | Yes                     | Facts checked against code; diagrams/text reviewed by a human                       |
| Code review                                             | Yes (assistive)         | AI findings triaged by a human; accepted/rejected recorded                          |
| Spec-driven generation (AI4)                            | Yes, at least 2 stories | From Gherkin/OpenSpec; agent produces a PR; CI + human review decide                |
| Brainstorming, explanation, learning                    | Yes                     | Do not copy blindly                                                                 |
| CI agents (PR summary, failure triage, flaky detection) | Optional (AI7)          | Suggest only; never auto-merge or auto-push                                         |
| Several agents with roles (AI6)                         | Optional                | Human checkpoint between handoffs                                                   |

## 3. Forbidden

- Giving AI services **secrets, credentials, tokens, real personal data** (use synthetic data, fictional names).
- Agents merging, force-pushing, approving PRs, or bypassing hooks/CI/quality gates.
- Committing code nobody on the team understands.
- Weakening tests/gates/coverage to make AI-written code pass.
- Using AI to impersonate a teammate (reviews, peer-review feedback, checkpoint explanations, individual grade inputs).
- Passing off AI work as unassisted, or the reverse.
- Agents adding dependencies, plugins or tools without human approval. Small things are written by us; a dependency is added only if truly needed and after it is verified (exists, maintained, licence, vulnerabilities, version compatibility).
- Agents guessing when unsure: they must ask, as many questions as needed, before coding.
- Unapproved tools handling repository content. Approved tools: **[TBD list, e.g. Claude Code, GitHub Copilot review]**.

## 4. Human review rules

- Every PR: at least one **other** member approves. The author may not approve their own PR.
- Reviewer checklist adds: tests meaningful (not tautological/duplicate)? design pattern justified? no hidden scope creep? secrets/PII absent? AI-log entry exists?
- AI-assisted review is an extra reviewer, not a replacement. Each AI finding is marked **accepted** or **rejected + reason**.
- Big design decisions: agent must give options and a recommendation; humans choose (see AGENTS.md 1.2).
- Agents ask before any commit/push/branch/merge (AGENTS.md 1.1).

## 5. Recording AI use

- **No AI trailers or co-author lines in commits/PRs.** Traceability is by the AI log instead.
- **Personal AI log** in each member's `localDocs/`. Markdown table, one row per entry, columns: `ID | Date | What the user did | What the AI did`.
  - ID = first letter of username + zero-padded sequence number, always increasing, never reused (`F001`, `F002`, ...). Log is **append-only across days**: entries from the current day may still be corrected; entries from earlier days are frozen, and corrections to them are new entries.
  - `localDocs/` is git-ignored (private). An iteration summary can be exported to `docs/ai-log/<initial>.md` **only on the member's command**.
  - Written by the human. Describe AI contribution **conservatively and specifically**; do not let the AI write a flattering account of itself.
- **AI review findings**: accepted/rejected + reason **[TBD location: PR template table]**.
- **Declaration of AI use** (one for the project, QA Manual 6.2): tool/model, used for, how verified. QA Engineer consolidates from the logs.
- **AI5 data** (QA Manual 6.6): share of AI-assisted changes, findings accepted/rejected, defects/rework traced to AI, gate failures on AI changes. Derived from the logs; so logs must be kept current (updated per work session, before the PR is opened).

## 6. Responsibilities

| Role                          | Responsibility                                                                            |
| ----------------------------- | ----------------------------------------------------------------------------------------- |
| Every member                  | Follow this policy; keep own log; understand what they merge                              |
| QA Engineer (Fernando Santos) | Maintains AGENTS.md and this policy, checks compliance, consolidates declaration/AI5 data |
| Team Coordinator (Diogo Silva) | Ensures review happens, raises policy breaches early                                      |
| DevOps Master (Bernardo Reis)  | Secrets handling, CI agents (if any) have least privilege and cannot merge                |
| Product Owner (Miguel Neto)    | Confirms AI-generated specs/stories reflect real requirements                             |

## 7. Breaches

First time: discuss at team checkpoint, fix, update log. Repeated or concealment: raise with teaching staff per course rules.

## 8. Approval

| Member          | Approved (date) |
| --------------- | --------------- |
| Miguel Neto     |                 |
| Diogo Silva     |                 |
| Fernando Santos | 07/11/2026      |
| Bernardo Reis   | 07/11/2026      |
