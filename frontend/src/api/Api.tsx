import { TaskData, TaskStatus } from '../App';

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
  status: TaskStatus
): Promise<TaskData | undefined> => {
  const url = 'http://localhost:8080/update-task-status';
  try {
    const response = await fetch(url, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ id, status }),
    });
    const task: TaskData = await response.json();
    console.log('update task status response: ', task);
    return task;
  } catch (e) {
    console.error(e);
  }
};
