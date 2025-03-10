import { useCallback, useEffect, useState } from 'react';
import TodoForm from './components/taskform/TaskForm';
import Task from './components/task/Task';
import Microphone from './components/microphone/Microphone';
import { IntentResponse } from './hooks/useSpeech';
import {
  addTaskAPI,
  deleteTaskAPI,
  getAllTasksAPI,
  updateTaskStatusAPI,
} from './api/Api';
import './App.css';

export type TaskData = {
  description: string;
  id: string;
  status: TaskStatus;
};

export enum TaskStatus {
  PENDING,
  DONE,
}

export default function App() {
  const [tasks, setTasks] = useState<TaskData[]>([]);

  const addTask = async (task: string | TaskData) => {
    if (typeof task === 'string') {
      const newTask = await addTaskAPI(task);
      console.log('new task: ', newTask);
      if (newTask) {
        setTasks((tasks) => [...tasks, newTask]);
      }
    } else {
      setTasks((tasks) => [...tasks, task]);
    }
  };

  const deleteTask = async (taskToBeDeleted: string | TaskData) => {
    let deletedTask: TaskData | undefined;
    if (typeof taskToBeDeleted === 'string') {
      deletedTask = await deleteTaskAPI(taskToBeDeleted);
    } else {
      deletedTask = taskToBeDeleted;
    }

    if (deletedTask) {
      setTasks((prevTasks) =>
        prevTasks.filter((task) => task.id !== deletedTask.id)
      );
    }
  };

  const editTaskDescription = (id: string, newDescription: string): void => {
    setTasks((prevTasks) => {
      return prevTasks.map((task) =>
        task.id === id ? { ...task, description: newDescription } : task
      );
    });
  };

  const editTaskStatus = async (
    id: string,
    newStatus: TaskStatus
  ): Promise<void> => {
    const success = await updateTaskStatusAPI(id, newStatus);
    if (success) {
      setTasks((prevTasks) => {
        return prevTasks.map((task) =>
          task.id === id ? { ...task, status: newStatus } : task
        );
      });
    }
  };

  const handleVoiceCommands = useCallback((intentPayload: IntentResponse) => {
    const { id, intent, description } = intentPayload;
    const task: TaskData = { id, description, status: TaskStatus.PENDING };
    if (intent === 'ADD_TASK') {
      addTask(task);
    } else if (intent === 'DELETE_TASK') {
      console.log('here');
      deleteTask(task);
    }
  }, []);

  useEffect(() => {
    console.log('tasks: ', tasks);
  }, [tasks]);

  useEffect(() => {
    async function fetchTasks() {
      const response = await getAllTasksAPI();
      if (response) {
        setTasks(response);
      }
    }
    fetchTasks();
  }, []);

  const displayTasks = tasks.map((task) => (
    <Task
      key={task.id}
      id={task.id}
      description={task.description}
      status={task.status}
      deleteTask={deleteTask}
      editTaskDescription={editTaskDescription}
      editTaskStatus={editTaskStatus}
    />
  ));

  return (
    <>
      <div className="main-container">
        <span>EchoTask</span>
        <TodoForm addTask={addTask} />
        <div className="task-container">{displayTasks}</div>
        <Microphone handleVoiceCommands={handleVoiceCommands} />
      </div>
    </>
  );
}
