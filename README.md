# EchoTask

Voice Controlled To-Do List App (in development)

EchoTask is a web application designed to emulate IoT tools like Siri and Alexa allowing users to manage their tasks using voice commands. It leverages React for the frontend and Spring Boot for the backend, integrating speech recognition and natural language processing (NLP) to create a seamless and intuitive to-do list experience.

## Features

* **Voice Controlled Task Management**: 
Create, read, update, and delete tasks using natural voice commands.
* **Speech Recognition**:
Utilizes React Speech Recognition to transcribe speech into text.
* **Natural Language Understanding**:
Implements Apache OpenNLP for intent detection and NLP capabilities to process commands effectively.
* **Cross-Platform Accessibility**:
Accessible on both desktop and mobile browsers.
* **Real-Time Updates**:
Task management changes reflect instantly without page reloads.

## Tech Stack

### Frontend
* **React**: Component-based UI development.
* **React Speech Recognition**: Speech-to-text functionality.

### Backend
* **Java Spring Boot**: REST API for handling tasks and NLP integration.
* **Apache OpenNLP**: NLP tools for intent detection.
* **PostgreSQL**: Database to store tasks

### Containerization
* **Docker**: To package the frontend and backend for easy deployment.
