import { useState } from 'react';
import TodoForm from './components/TodoForm';
import './App.css';

type Task = {
  description: string
}

function App() {
  const [tasks, setTasks] = useState<Task[]>([]);

  const addTask = (task: Task) => {
    setTasks([...tasks, task])
  }
  console.log('tasks: ', tasks)

  return (
    <>
      <div className="main-container">
        <span>EchoTask</span>
        <TodoForm addTask={addTask} />
      </div>
    </>
  );
}

export default App;
