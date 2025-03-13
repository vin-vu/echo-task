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
  completed: boolean;
};

export default function App() {
  const [tasks, setTasks] = useState<TaskData[]>([]);

  const addTask = async (task: string | TaskData) => {
    if (typeof task === 'string') {
      const newTask = await addTaskAPI(task);
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

  const updateTasksStatusInState = (id: string, completedStatus: boolean) => {
    setTasks((prevTasks) => {
      return prevTasks.map((task) =>
        task.id === id ? { ...task, completed: completedStatus } : task
      );
    });
  };

  const editTaskStatus = async (
    id: string,
    completedStatus: boolean,
    voiceCommand: boolean
  ): Promise<void> => {
    if (voiceCommand) {
      updateTasksStatusInState(id, completedStatus);
    } else {
      const success = await updateTaskStatusAPI(id, completedStatus);
      if (success) {
        updateTasksStatusInState(id, completedStatus);
      }
    }
  };

  const handleVoiceCommands = useCallback((intentPayload: IntentResponse) => {
    const { id, intent, description, completed } = intentPayload;
    const task: TaskData = { id, description, completed };
    if (intent === 'ADD_TASK') {
      addTask(task);
    } else if (intent === 'DELETE_TASK') {
      deleteTask(task);
    } else if (intent === 'COMPLETED_TASK') {
      editTaskStatus(task.id, task.completed, true);
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
      completed={task.completed}
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
