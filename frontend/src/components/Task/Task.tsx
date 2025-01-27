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

  const handleEdit = () => {
    setIsEdit(true);
  };

  const displayDescription = () =>
    !isEdit ? <p>{description}</p> : <input></input>;

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
