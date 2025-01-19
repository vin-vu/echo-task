import { FaTrash } from 'react-icons/fa';
import { TiEdit } from 'react-icons/ti';
import './Task.css';

type TaskProps = {
  id: string;
  description: string;
  deleteTask: (id: string) => void;
  editTask: (id: string, newDescription: string) => void;
};

export default function Task({ id, description, deleteTask, editTask }: TaskProps) {

  return (
    <div className="task-item">
      <p>{description}</p>
      <div className="button-container">
        <FaTrash onClick={() => deleteTask(id)} />
        <TiEdit />
      </div>
    </div>
  );
}
