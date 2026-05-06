# AGENTS.md

This project uses Google's official Now in Android sample as its reference architecture. Before making any decision, design choice, implementation, or test change, first inspect `~/best-practice-android/nowinandroid`, and follow its architecture, implementation patterns, and testing approach as closely as possible.

## Now in Android First

- Before deciding on feature design, module structure, dependency direction, UI state modeling, data layer design, synchronization, navigation, or testing strategy, always inspect `~/best-practice-android/nowinandroid` first.
- Before implementing a change, find and analyze how Now in Android solves the same or closest related problem, including relevant source files, packages, and tests.
- Treat Now in Android patterns as the default for this project. Adjust only the minimum necessary parts when this project has a clear, project-specific reason to differ.
- If a different approach from Now in Android is required, first verify why the difference is necessary and include that rationale in the response.

## Response And Work Log Requirements

- When explaining architectural or implementation decisions, include the relevant Now in Android example that informed the decision.
- In responses, state how Now in Android handles the same concern and how that pattern was applied to this project.
- When adding or modifying tests, first reference Now in Android's test structure, naming, fixture/fake usage, and coroutine/test dispatcher patterns.
- During reviews or refactors, check how closely the current code matches the Now in Android structure. Treat meaningful mismatches as primary improvement candidates.

## Implementation Guidelines

- For modularization, build logic, convention plugins, and Gradle configuration, prefer Now in Android's structure and naming.
- For UI, follow Now in Android's Compose architecture, state hoisting, ViewModel state exposure, preview patterns, and UI testing patterns.
- For the data layer, follow Now in Android's separation of repositories, data sources, model mapping, and sync responsibilities.
- For tests, use Now in Android's unit test, UI test, fake repository/data source, and coroutine test patterns as the baseline.
- Before adding a new abstraction, first check whether Now in Android has a similar abstraction and prefer that established pattern.
