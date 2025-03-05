import { useCallback, useState } from 'react';
import TodoForm from './components/taskform/TaskForm';
import Task from './components/task/Task';
import Microphone from './components/microphone/Microphone';
import { IntentResponse } from './hooks/useSpeech';
import { createTask } from './api/Api';
import './App.css';

export type TaskData = {
  description: string;
  id: string;
};

export default function App() {
  const [tasks, setTasks] = useState<TaskData[]>([]);

  const addTask = async (task: string | TaskData) => {
    if (typeof task === 'string') {
      const createdTask = await createTask(task);
      if (createdTask) {
        setTasks((tasks) => [...tasks, createdTask]);
      }
    } else {
      setTasks((tasks) => [...tasks, task]);
    }
  };

  const deleteTask = (id: string): void => {
    const updatedTasks = [];
    for (const task of tasks) {
      if (task.id !== id) {
        updatedTasks.push(task);
      }
    }
    setTasks(updatedTasks);
  };

  const editTask = (id: string, newDescription: string): void => {
    const updatedTasks = [];
    for (const task of tasks) {
      if (task.id === id) {
        task.description = newDescription;
      }
      updatedTasks.push(task);
    }
    setTasks(updatedTasks);
  };

  const editTasksHandler = useCallback((intentPayload: IntentResponse) => {
    const { id, intent, description } = intentPayload;
    const newTask: TaskData = { id, description };
    if (intent === 'ADD_TASK') {
      addTask(newTask);
    }
  }, []);

  const displayTasks = tasks.map((task) => (
    <Task
      key={task.id}
      id={task.id}
      description={task.description}
      deleteTask={deleteTask}
      editTask={editTask}
    />
  ));

  return (
    <>
      <div className="main-container">
        <span>EchoTask</span>
        <TodoForm addTask={addTask} />
        <div className="task-container">{displayTasks}</div>
        <Microphone editTasksHandler={editTasksHandler} />
      </div>
    </>
  );
}
