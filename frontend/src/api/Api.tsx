import { TaskData } from '../App';

export const addTaskAPI = async (
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
    const task: TaskData = await response.json();
    console.log('add task json: ', task);
    return task;
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
    const task: TaskData = await response.json();
    console.log('delete task json: ', task);
    return task;
  } catch (e) {
    console.error(e);
  }
};
