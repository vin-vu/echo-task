import { TaskData } from '../App';

type CreateTaskResponse = {
  id: string;
  description: string;
};

export const createTask = async (
  description: string
): Promise<TaskData | undefined> => {
  const createTaskAPI = 'http://localhost:8080/create-task';
  try {
    const response = await fetch(createTaskAPI, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ description }),
    });
    const json: TaskData = await response.json();
    return json;
  } catch (e) {
    console.error(e);
  }
};

export const deleteTaskAPI = async (
  id: string
): Promise<TaskData | undefined> => {
  const createTaskAPI = 'http://localhost:8080/delete-task';
  try {
    const response = await fetch(createTaskAPI, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ id }),
    });
    const json: TaskData = await response.json();
    return json;
  } catch (e) {
    console.error(e);
  }
};
