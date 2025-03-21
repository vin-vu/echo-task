# EchoTask

Voice Controlled Task Management App

EchoTask is a full stack web application designed to emulate IoT tools like Siri and Alexa allowing users to manage their tasks using voice commands. It leverages React for the frontend and Spring Boot for the backend, integrating speech recognition and natural language processing (NLP) to create an intuitive task  management experience. EchoTask supports adding, deleting, and marking tasks as complete with verbal inputs.

## Table of Contents
- [Tech Stack](#tech-stack)
- [Local Setup Guide](#local-setup-guide)
- [Usage](#usage)
- [Known Limitations](#known-limitations)
- [NLP Statistics](#nlp-statistics)

## Demo
![demo](/echo-task-demo.gif)

## Tech Stack

#### Frontend
* **React**: Component-based UI development
* **React Speech Recognition**: Speech-to-text functionality

#### Backend
* **Java Spring Boot**: REST API for handling communication, NLP integration, and speech processing
* **PostgreSQL**: Database to store tasks

#### NLP
* **Apache OpenNLP**: Custom trained Document Categorizer to detect user intent
* **Stanford CoreNLP**: Neural Network Dependency Parser to produce tree like relationships between words 

## Local Setup Guide

This guide outlines  steps to run the EchoTask locally

#### Prerequisites

Ensure you have the following installed on your machine:

- **Node.js** (Recommended: Latest LTS version)
- **Java 17+**
- **PostgreSQL** (Ensure the service is running)

#### Environment Variables

Create a `.env` file at the **root level** of the project with the following contents:

```
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/echotask
SPRING_DATASOURCE_USERNAME=username
SPRING_DATASOURCE_PASSWORD=password
```

#### Database Setup

1. Start your PostgreSQL service.
2. Create a database named `echotask` using your preferred tool (e.g., pgAdmin, DBeaver, or CLI):

```sql
CREATE DATABASE echotask;
```

## Running the Application

### 1. Start the Backend (Spring Boot)

1. Open a terminal and navigate to the `backend` folder:
   ```sh
   cd backend
   ```
2. Run the Spring Boot application using the following command:
   ```sh
   ./mvnw spring-boot:run   # For Maven builds
   ```

If you’re using IntelliJ, you can also start the backend by running the application directly from the IDE.

### 2. Start the Frontend (React)

1. Open a **new terminal** and navigate to the `frontend` folder:
   ```sh
   cd frontend
   ```
2. Install dependencies:
   ```sh
   npm install
   ```
3. Start the React development server:
   ```sh
   npm run dev
   ```

## Usage

1. After starting the application, visit **`http://localhost:5173/`** in your browser
2. Grant the application permission to use your microphone when prompted
3. Click and hold the microphone button while giving verbal commands, then release the button when finished
4. Supported commands include adding tasks, deleting tasks, and`marking tasks as completed
5. It is recommended to annunciate words clearly to get an accurate transcript.

#### Example Phrases for Voice Commands
Some training data is stored in the following file, which can provide guidance on effective commands:
```
echo-task\backend\src\main\resources\data\doccat-training.txt
```

## Known Limitations
- **Incorrect Commands**: The document categorizer model may need additional tuning and training data if the wrong intent is classified.
- **Incorrect Description Extraction**: The dependency parser may struggle to ignore intent actions from task descriptions due to the wide variability in natural speech patterns. This would require additional algorithmic solutions to handle the variety of utterances.

## NLP Statistics
The UI includes a **Stats Section** that provides:
- **Intent Probabilities:** Displays confidence levels for `Add`, `Delete`, and `Complete` intents
- **Captured Transcription:** Shows the raw text captured from the microphone
- **Parsed Description:** Displays the extracted task description generated by the dependency parser

