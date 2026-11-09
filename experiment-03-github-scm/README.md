# Experiment 3 — Source code management on GitHub

**Aim:** Practise source code management on GitHub and experiment with the source
code written in Experiment 1.

**Tools:** Git, GitHub, Flask, Docker

The Experiment 1 application is committed in [`src/`](src) so this experiment is
self-contained.

## Steps performed

```bash
# 1. Create the repository on GitHub, then clone it
git clone https://github.com/suhailahmed77063-hash/Devops-Lab-experiment.git
cd Devops-Lab-experiment

# 2. Add the Experiment 1 source code
mkdir -p experiment-03-github-scm/src
cp -r experiment-01-flask-docker/* experiment-03-github-scm/src/

# 3. Stage the changes
git add experiment-03-github-scm

# 4. Commit
git commit -m "Added source code for a simple user registration form"

# 5. Push to the remote repository
git push origin master     # (or: git push origin main)

# 6. Verify on GitHub that the files are visible in the repository
git log --oneline
```

## Experimenting with the code through source control

```bash
# Create a feature branch for a change
git switch -c feature/email-validation

# ... edit src/app.py ...

git diff                                   # review the change before committing
git add src/app.py
git commit -m "Validate email format on registration"
git push -u origin feature/email-validation
```

Then open a pull request on GitHub, review the diff and merge it.

## What to observe

* Every commit has an author, timestamp and message: `git log --format=fuller`.
* `git diff <commit1> <commit2>` shows exactly what a change introduced.
* A pull request gives a reviewable diff plus a comment thread.
* The same workflow scales from one file to the whole repository, and it is the
  foundation the CI/CD pipeline in Experiment 5 builds on.

## Run the imported application

```bash
cd src
docker build -t simple-flask-app .
docker run -p 5000:5000 simple-flask-app
# http://localhost:5000/register
```
