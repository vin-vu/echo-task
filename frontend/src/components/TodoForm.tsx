import { useState } from 'react';

interface TodoFormProps {
  addTask: (task: Task) => void;
}

export default function TodoForm() {
  const [value, setValue] = useState('');
  return (
    <div className="input-container">
      <form>
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
