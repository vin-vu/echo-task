import { FaTrash } from 'react-icons/fa';
import { TiEdit } from 'react-icons/ti';
import './Task.css';

type TaskProps = {
  description: string;
};

export default function Task({ description }: TaskProps) {
  return (
    <div className="task-item">
      <p>{description}</p>
      <div className="button-container">
        <FaTrash />
        <TiEdit />
      </div>
    </div>
  );
}
