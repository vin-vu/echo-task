import { useState } from 'react';
import TodoForm from './components/TodoForm';
import './App.css';

export type Task = {
  description: string;
  id: string;
};

function App() {
  const [tasks, setTasks] = useState<Task[]>([]);

  const addTask = (task: Task) => {
    setTasks([...tasks, task]);
  };

  const displayTasks = tasks.map((task) => (
    <li key={task.id}>{task.description}</li>
  ));

  return (
    <>
      <div className="main-container">
        <span>EchoTask</span>
        <TodoForm addTask={addTask} />
        <ul>{displayTasks}</ul>
      </div>
    </>
  );
}

export default App;
