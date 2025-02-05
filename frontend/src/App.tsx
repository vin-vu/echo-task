import { useState } from 'react';
import TodoForm from './components/taskform/TaskForm';
import Task from './components/task/Task';
import Microphone from './components/microphone/Microphone';
import './App.css';

export type TaskData = {
  description: string;
  id: string;
};

export default function App() {
  const [tasks, setTasks] = useState<TaskData[]>([]);

  const addTask = (task: TaskData) => {
    setTasks([...tasks, task]);
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
        <Microphone />
      </div>
    </>
  );
}
