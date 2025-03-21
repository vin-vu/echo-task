import { TaskData } from '../App';

export type IntentResponse = {
  id: string;
  intent: 'ADD_TASK' | 'DELETE_TASK' | 'COMPLETED_TASK';
  rankedIntentScores: Map<string, Set<string>>;
  description: string;
  completed: boolean;
};

export enum Intent {
  ADD_TASK = 'ADD_TASK',
  DELETE_TASK = 'DELETE_TASK',
  COMPLETED_TASK = 'COMPLETED_TASK',
}

export const fetchIntent = async (
  transcript: string
): Promise<IntentResponse | undefined> => {
  if (transcript.length !== 0) {
    console.log('sending: ', transcript);
    const backendAPI = 'http://localhost:8080/detect-intent';
    try {
      const response = await fetch(backendAPI, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ transcript }),
      });
      const json: IntentResponse = await response.json();
      console.log('voice command response: ', json);
      return json;
    } catch (e) {
      console.error(e);
    }
  }
};

export const getAllTasksAPI = async (): Promise<TaskData[] | undefined> => {
  const createTaskAPI = 'http://localhost:8080/get-tasks';
  try {
    const response = await fetch(createTaskAPI, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
    });
    const tasks: TaskData[] = await response.json();
    console.log('fetched all Tasks: ', tasks);
    return tasks;
  } catch (e) {
    console.error(e);
  }
};

export const addTaskAPI = async (
  description: string
): Promise<TaskData | undefined> => {
  const url = 'http://localhost:8080/create-task';
  try {
    const response = await fetch(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ description }),
    });
    const task: TaskData = await response.json();
    console.log('add task response: ', task);
    return task;
  } catch (e) {
    console.error(e);
  }
};

export const deleteTaskAPI = async (
  id: string
): Promise<TaskData | undefined> => {
  const url = 'http://localhost:8080/delete-task';
  try {
    const response = await fetch(url, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ id }),
    });
    const task: TaskData = await response.json();
    console.log('delete task response: ', task);
    return task;
  } catch (e) {
    console.error(e);
  }
};

export const updateTaskStatusAPI = async (
  id: string,
  completed: boolean
): Promise<TaskData | undefined> => {
  const url = 'http://localhost:8080/update-task-status';
  try {
    const response = await fetch(url, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ id, completed }),
    });
    const task: TaskData = await response.json();
    console.log('delete task response: ', task);
    return task;
  } catch (e) {
    console.error(e);
    return;
  }
};
