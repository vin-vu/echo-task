import { FaTrash } from 'react-icons/fa';
import { TiEdit } from 'react-icons/ti';
import './Task.css';
import { useState } from 'react';

type TaskProps = {
  id: string;
  description: string;
  deleteTask: (id: string) => void;
  editTask: (id: string, newDescription: string) => void;
};

export default function Task({
  id,
  description,
  deleteTask,
  editTask,
}: TaskProps) {
  const [isEdit, setIsEdit] = useState<boolean>(false);
  const [newDescription, setNewDescription] = useState<string>('');

  const handleEdit = () => {
    setIsEdit(!isEdit);
    setNewDescription(description);
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    editTask(id, newDescription);
    setNewDescription('');
    setIsEdit(false);
  };

  const displayDescription = () =>
    !isEdit ? (
      <span onClick={() => handleEdit()}>{description}</span>
    ) : (
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          value={newDescription}
          onChange={(e) => setNewDescription(e.target.value)}
        ></input>
      </form>
    );

  return (
    <div className="task-item">
      {displayDescription()}
      <div className="button-container">
        <FaTrash onClick={() => deleteTask(id)} />
        <TiEdit onClick={() => handleEdit()} />
      </div>
    </div>
  );
}
