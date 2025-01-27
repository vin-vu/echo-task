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
  const [editValue, setEditValue] = useState<string>('');

  const handleEdit = () => {
    setIsEdit(!isEdit);
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    editTask(id, editValue);
    setEditValue('');
    setIsEdit(false);
  };

  const displayDescription = () =>
    !isEdit ? (
      <p>{description}</p>
    ) : (
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          placeholder={description}
          value={editValue}
          onChange={(e) => setEditValue(e.target.value)}
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
