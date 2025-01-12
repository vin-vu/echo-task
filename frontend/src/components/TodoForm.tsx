import { useState } from 'react';
import { Task } from '../App';

interface TodoFormProps {
  addTask: (task: Task) => void;
}

export default function TodoForm({ addTask }: TodoFormProps) {
  const [value, setValue] = useState('');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    addTask({ description: value });
    setValue('');
  };

  return (
    <div className="input-container">
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          value={value}
          onChange={(e) => setValue(e.target.value)}
        />
        <button type="submit">Add Task</button>
      </form>
    </div>
  );
}
