"""Experiment 1 - Simple user registration form for an event.

Run locally:
    pip install -r requirements.txt
    python app.py

Run in Docker:
    docker build -t simple-flask-app .
    docker run -p 5000:5000 simple-flask-app
"""

import json
import os
from datetime import datetime, timezone

from flask import Flask, redirect, render_template, request, url_for
from werkzeug.security import generate_password_hash

app = Flask(__name__)

DATA_DIR = os.path.join(os.path.dirname(os.path.abspath(__file__)), "data")
DATA_FILE = os.path.join(DATA_DIR, "registrations.json")


def load_registrations():
    """Return the stored registrations, or an empty list on first run."""
    if not os.path.exists(DATA_FILE):
        return []
    with open(DATA_FILE, encoding="utf-8") as handle:
        return json.load(handle)


def save_registration(name, email, password):
    """Append one registration. Passwords are hashed, never stored as text."""
    os.makedirs(DATA_DIR, exist_ok=True)
    registrations = load_registrations()
    registrations.append(
        {
            "name": name,
            "email": email,
            "password_hash": generate_password_hash(password),
            "registered_at": datetime.now(timezone.utc).isoformat(),
        }
    )
    with open(DATA_FILE, "w", encoding="utf-8") as handle:
        json.dump(registrations, handle, indent=2)


@app.route("/")
def index():
    return redirect(url_for("register"))


@app.route("/register", methods=["GET", "POST"])
def register():
    if request.method == "POST":
        name = request.form.get("name", "").strip()
        email = request.form.get("email", "").strip()
        password = request.form.get("password", "")

        missing = [field for field, value in
                   (("name", name), ("email", email), ("password", password))
                   if not value]
        if missing:
            return render_template(
                "register.html",
                error="Please fill in: " + ", ".join(missing),
                name=name,
                email=email,
            ), 400

        save_registration(name, email, password)
        return render_template("success.html", name=name)

    return render_template("register.html")


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000)
