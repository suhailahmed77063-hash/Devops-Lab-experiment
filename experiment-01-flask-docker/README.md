# Experiment 1 — User registration form for an event

**Aim:** Write code for a simple user registration form for an event and run it
inside a Docker container.

**Tools:** Python 3.12, Flask 3.0.3, Docker

## Files

| File | Purpose |
|------|---------|
| `app.py` | Flask application with the `/register` route |
| `templates/register.html` | Registration form |
| `templates/success.html` | Confirmation page shown after submit |
| `requirements.txt` | Python dependencies |
| `Dockerfile` | Image definition for the Flask app |

## Steps

```bash
# 1. Build the image
docker build -t simple-flask-app .

# 2. Run the container
docker run -p 5000:5000 simple-flask-app

# 3. Open the form
#    http://localhost:5000/register
```

Stop the container with `Ctrl+C`, or if it was started detached:

```bash
docker ps
docker stop <container-id>
```

## What the program does

* `GET /register` renders the form with `name`, `email` and `password` fields.
* `POST /register` validates that no field is empty, hashes the password with
  `werkzeug.security.generate_password_hash`, and appends the record to
  `registrations.json`.
* A successful submit renders `success.html`.

## Differences from the printed manual

The manual uses `FROM python:3.8` and `Flask==1.1.2`. Those versions are end of
life and no longer install reliably, so this folder uses `python:3.12-slim` and
`Flask==3.0.3`. The route names, form fields and flow are unchanged, so the
commands below still match the manual:

```bash
docker build -t simple-flask-app .
docker run -p 5000:5000 simple-flask-app
```

The manual itself warns that its snippet stores the password as plain text and
has no error handling. This version hashes the password and validates input;
that is the only behavioural difference.

## Notes

* `registrations.json` is written inside the container and is lost when the
  container is removed. Mount a volume if you want it to survive:
  `docker run -p 5000:5000 -v ${PWD}/data:/app/data simple-flask-app`
* This is a lab exercise. A production registration flow also needs CSRF
  protection, rate limiting, email verification and HTTPS.
