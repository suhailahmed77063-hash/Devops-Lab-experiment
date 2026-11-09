# Experiment 2 — Explore Git and GitHub commands

**Aim:** Explore the commonly used Git and GitHub commands.

**Tool:** Git 2.4x, a GitHub account

## Repository setup

```bash
git init                                  # create a repository in the current folder
git status                                # working tree / staging area state
git add <file-name>                       # stage one file
git add .                                 # stage everything
git commit -m "commit message"            # record the staged snapshot
git log --oneline --graph                 # commit history
git log --stat                            # history with changed files
git diff                                  # unstaged changes
git diff --staged                         # staged changes
```

## Undo / restore

```bash
git checkout <file-name>                  # discard changes in one file (manual)
git restore <file-name>                   # same, modern syntax
git restore --staged <file-name>          # unstage
git reset --soft HEAD~1                   # undo last commit, keep changes staged
git reset --hard HEAD~1                   # discard last commit and its changes
```

## Branching and merging

```bash
git branch                                # list local branches
git branch <branch-name>                  # create a branch
git checkout <branch-name>                # switch branch (manual)
git switch <branch-name>                  # switch branch (modern)
git switch -c <branch-name>               # create + switch
git merge <branch-name>                   # merge into the current branch
git branch -d <branch-name>               # delete a merged branch
```

## Remotes and collaboration

```bash
git remote add origin <repository-url>    # link a remote
git remote -v                             # list remotes
git clone <repository-url>                # copy a remote repository
git push origin <branch-name>             # upload commits
git push -u origin main                   # upload and set upstream
git pull origin <branch-name>             # fetch + merge
git fetch origin                          # download without merging
git tag v1.0.0                            # create a tag
git push origin --tags                    # upload tags
```

## Creating a pull request on GitHub

1. Push the feature branch: `git push -u origin feature-branch`.
2. Open the repository on GitHub and click **Compare & pull request**.
3. Choose base branch (`main`) and compare branch (`feature-branch`).
4. Add a title and description, then click **Create pull request**.
5. After review, use **Merge pull request**.

## Observed result

Commands were executed in this repository. `git log --oneline` shows the commit
history, and `git remote -v` shows:

```
origin  https://github.com/suhailahmed77063-hash/Devops-Lab-experiment.git (fetch)
origin  https://github.com/suhailahmed77063-hash/Devops-Lab-experiment.git (push)
```
