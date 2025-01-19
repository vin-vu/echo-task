import { useState } from 'react';
import { TaskData } from '../../App';
import { v4 as uuid4 } from 'uuid';
import './TaskForm.css';

interface TodoFormProps {
  addTask: (task: TaskData) => void;
}

export default function TodoForm({ addTask }: TodoFormProps) {
  const [value, setValue] = useState('');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    addTask({ description: value, id: uuid4() });
    setValue('');
  };

  return (
    <div className="input-container">
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          placeholder='A task for now...'
          value={value}
          onChange={(e) => setValue(e.target.value)}
        />
        <button type="submit">Add Task</button>
      </form>
    </div>
  );
}
