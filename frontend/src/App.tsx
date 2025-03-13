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

  const createTaskFromDescription = async (
    description: string
  ): Promise<TaskData | undefined> => {
    return await addTaskAPI(description);
  };

  const addTask = useCallback(async (taskInput: string | TaskData) => {
    const task =
      typeof taskInput === 'string'
        ? await createTaskFromDescription(taskInput)
        : taskInput;
    if (task) {
      setTasks((tasks) => [...tasks, task]);
    }
  }, []);

  const deleteTaskById = async (id: string): Promise<TaskData | undefined> => {
    return await deleteTaskAPI(id);
  };

  const deleteTask = useCallback(async (taskInput: string | TaskData) => {
    const deletedTask =
      typeof taskInput === 'string'
        ? await deleteTaskById(taskInput)
        : taskInput;

    if (deletedTask) {
      setTasks((prevTasks) =>
        prevTasks.filter((task) => task.id !== deletedTask.id)
      );
    }
  }, []);

  const editTaskDescription = (id: string, newDescription: string): void => {
    setTasks((prevTasks) => {
      return prevTasks.map((task) =>
        task.id === id ? { ...task, description: newDescription } : task
      );
    });
  };

  const editTaskStatus = useCallback(
    async (id: string, completedStatus: boolean, voiceCommand: boolean) => {
      if (!voiceCommand) {
        updateTaskStatusAPI(id, completedStatus);
      }
      setTasks((prevTasks) => {
        return prevTasks.map((task) =>
          task.id === id ? { ...task, completed: completedStatus } : task
        );
      });
    },
    []
  );

  const handleVoiceCommands = useCallback(
    (intentPayload: IntentResponse) => {
      const { id, intent, description, completed } = intentPayload;
      const task: TaskData = { id, description, completed };
      if (intent === 'ADD_TASK') {
        addTask(task);
      } else if (intent === 'DELETE_TASK') {
        deleteTask(task);
      } else if (intent === 'COMPLETED_TASK') {
        editTaskStatus(task.id, task.completed, true);
      }
    },
    [addTask, deleteTask, editTaskStatus]
  );

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
