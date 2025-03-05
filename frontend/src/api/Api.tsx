import { TaskData } from '../App';

type CreateTaskResponse = {
  id: string;
};

export const createTask = async (
  task: TaskData
): Promise<CreateTaskResponse | undefined> => {
  const createTaskAPI = 'http://localhost:8080/create-task';
  try {
    const response = await fetch(createTaskAPI, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ task }),
    });
    const json: CreateTaskResponse = await response.json();
    return json;
  } catch (e) {
    console.error(e);
  }
};
