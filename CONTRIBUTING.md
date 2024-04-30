# How to Contribute

We'd love to accept your patches and contributions to Mosaic, wether its a new component / tool or an update to existing ones! There are
just a few small guidelines you need to follow :) 

## New Features/Libraries

Before contributing large new features and/or library please reach out with discussion first.
## Code Reviews

All submissions, including submissions by project members, require review. We
use GitHub pull requests for this purpose. Consult
[GitHub Help](https://help.github.com/articles/about-pull-requests/) for more
information on using pull requests.

### CI/CD Checks
We have a GitHub Actions workflow that is being activated as part of any raised Pull Request, it will run the tests, spotless and detekt checks.

You can also run this locally to make sure the PR will pass these checks:

- Spotless: Run `./gradlew spotlessApply` to apply formatting to the code according to the spec. 

- Detekt: Run `./gradlew detekt` to run static code analysis using Detekt.
