import { useState } from 'react';
import './TaskForm.css';

interface TodoFormProps {
  addTask: (task: string, voiceCommand: boolean) => void;
}

export default function TodoForm({ addTask }: TodoFormProps) {
  const [value, setValue] = useState('');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    addTask(value, false);
    setValue('');
  };

  return (
    <div className="input-container">
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          placeholder="A task for now..."
          value={value}
          onChange={(e) => setValue(e.target.value)}
        />
        <button type="submit">Add Task</button>
      </form>
    </div>
  );
}
