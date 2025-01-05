import { useState } from 'react';
import './App.css';

function App() {
  const [value, setValue] = useState('');
  return (
    <>
      <div className="main-container">
        <span>EchoTask</span>
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
      </div>
    </>
  );
}

export default App;
