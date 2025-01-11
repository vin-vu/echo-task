import { useState } from 'react';
import TodoForm from './components/TodoForm';
import './App.css';

type Task = {
  value: string
}

function App() {
  const [tasks, setTasks] = useState<Task[]>([]);

  const addTask = (task: Task) => {
    setTasks([...tasks, task])
  }

  return (
    <>
      <div className="main-container">
        <span>EchoTask</span>
        <TodoForm/>
      </div>
    </>
  );
}

export default App;
