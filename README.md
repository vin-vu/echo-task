# EchoTask

EchoTask is a full stack  application that enables users to manage tasks through voice commands, similar to assistants like Siri and Alexa. It combines speech recognition with a lightweight NLP pipeline to convert spoken input into structured actions such as adding, deleting, and completing tasks. Built with React and Spring Boot, the app provides a voice-driven task management experience.

## Table of Contents

- [Tech Stack](#tech-stack)
- [NLP Approach](#nlp-approach)
- [NLP Statistics](#nlp-statistics)
- [Local Setup Guide](#local-setup-guide)
- [Usage](#usage)
- [Known Limitations](#known-limitations)
- [Example Phrases](#example-phrases)

## Demo

![demo](/echo-task-demo.gif)

## Tech Stack

#### Frontend

* **React**: Component based UI development
* **React Speech Recognition**: Speech-to-text input

#### Backend

- **Spring Boot (Java)** – REST APIs, NLP orchestration, request handling
- **PostgreSQL** – Task persistence

#### NLP

- **Apache OpenNLP**: 
   - Document Categorizer for intent classification - [Doccat Documentation](https://opennlp.apache.org/docs/3.0.0-M1/manual/opennlp.html#tools.doccat)
   - Named Entity Recogniation (NER) - [NER Documentation](https://opennlp.apache.org/docs/3.0.0-M1/manual/opennlp.html#tools.namefind)

## NLP Approach

### Pipeline
User input is preprocessed (tokenization, normalization, optional lemmatization) before being passed through two stages:

1. **Intent Classification (Document Categorizer)**
2. **Task Extraction (NER Model)**

Each NLP component currently maintains its own preprocessing for simplicity. In production, this would be unified.

---

### Intent Classification (Document Categorizer)

A custom Document Categorizer (Doccat) model classifies utterances into CRUD like intents:

- **ADD** → create task (`"add go to Costco"`)
- **DELETE** → remove task (`"delete go to Costco"`)
- **COMPLETE** → update task (`"finished go to Costco"`)

This determines which backend operation to execute.

---

### Task Extraction (NER)

A custom Named Entity Recognition (NER) model extracts the task description from the utterance.

- Input: `"add go to Costco"`
- Output: `"go to Costco"`

The model identifies spans labeled as task-related entities, enabling flexible extraction across different phrasing.

### Why NER replaced initial Dependency Parsing implementation

Dependency parsing required manual tree traversal logic and struggled with variability in natural language.

NER provides:
- Direct span extraction
- Better generalization across phrasing
- Simpler and more maintainable implementation

## NLP Statistics

The UI includes a **Stats Section** that provides:

- **Intent Probabilities** – confidence scores for each intent  
- **Transcription** – raw speech-to-text output  
- **Parsed Description** – extracted task text from NER  

## Usage

1. After starting the application, visit **`http://localhost:5173/`** in your browser
2. Allow microphone access
3. Hold mic button → speak → release
4. Supported actions:
- Add task
- Delete task
- Complete task

## Known Limitations

- **Intent Misclassification** → requires more training data
- **NER Extraction Errors** → may miss or incorrectly span task descriptions
- **Speech Variability** → dependent on transcription accuracy

## Example Phrases

Some training data is stored in the following file, which can provide guidance on effective commands:

```
echo-task\backend\src\main\resources\data\doccat-training.txt
echo-task\backend\src\main\resources\data\task-ner-training.txt
```

## Local Setup Guide


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