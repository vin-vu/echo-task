import { useState } from 'react';
import TodoForm from './components/TaskForm';
import Task from './components/Task';
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

  const displayTasks = tasks.map((task) => (
    <Task key={task.id} description={task.description} />
  ));

  return (
    <>
      <div className="main-container">
        <span>EchoTask</span>
        <TodoForm addTask={addTask} />
        <div className="task-container">{displayTasks}</div>
      </div>
    </>
  );
}
