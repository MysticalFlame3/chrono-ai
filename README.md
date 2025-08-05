ChronoAI 🧠⏰
An intelligent, full-stack task scheduling platform that understands natural language.

ChronoAI is a full-stack web application designed to simplify task automation. It provides a user-friendly web interface where users can create, manage, and monitor automated tasks by describing them in plain English. The application's core feature is its ability to use a Large Language Model (LLM) to convert queries like "send a report every Friday at 5 PM" into precise CRON schedules, managing the entire task lifecycle.

Key Features
🧠 AI-Powered Parsing: Leverages the Google Gemini API to translate natural language commands into valid CRON expressions.

🔐 Secure Authentication: End-to-end user registration and login system using Spring Security and JSON Web Tokens (JWT).

📋 Task Management Dashboard: A dynamic React frontend for creating, viewing, deleting, and monitoring all scheduled tasks and their execution history.

⏰ Reliable Scheduling: Uses the Quartz Scheduler for persistent, stateful, and reliable background job execution.

📊 Execution History: Logs every task execution, recording its success or failure status for user visibility and debugging.

🐳 Containerized Environment: All required services (PostgreSQL, RabbitMQ) are containerized with Docker for a consistent and reproducible development setup.

Tech Stack
Category	Technology
Frontend	React (Vite), Tailwind CSS, Axios
Backend	Java 17, Spring Boot, Spring Security, Spring Data JPA
Database	PostgreSQL
Scheduling	Quartz Scheduler
Messaging	RabbitMQ
AI	Google Gemini API
Infrastructure	Docker, Docker Compose
