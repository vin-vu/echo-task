import { FaTrash } from 'react-icons/fa';
import { TiEdit } from 'react-icons/ti';
import './Task.css';
import { useEffect, useState } from 'react';
import { TaskStatus } from '../../App';

type TaskProps = {
  id: string;
  description: string;
  status: TaskStatus;
  deleteTask: (id: string) => void;
  editTask: (id: string, newDescription: string) => void;
  editTaskStatus: (id: string) => void;
};

export default function Task({
  id,
  description,
  status,
  deleteTask,
  editTask,
  editTaskStatus,
}: TaskProps) {
  const [isEdit, setIsEdit] = useState<boolean>(false);
  const [localDescription, setLocalDescription] = useState<string>(description);
  const [localStatus, setLocalStatus] = useState<TaskStatus>(status);

  const handleEdit = () => {
    setIsEdit(!isEdit);
    setLocalDescription(description);
  };

  const handleEditStatus = () => {
    setLocalStatus((prevStatus) => {
      if (prevStatus === TaskStatus.DONE) {
        return TaskStatus.PENDING;
      } else {
        return TaskStatus.DONE;
      }
    });
  };

  useEffect(() => {
    console.log('task status: ', localStatus);
  }, [localStatus]);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    editTask(id, localDescription);
    setLocalDescription('');
    setIsEdit(false);
  };

  const displayDescription = () =>
    !isEdit ? (
      <span className="task-description" onClick={() => handleEdit()}>
        {description}
      </span>
    ) : (
      <form onSubmit={handleSubmit} onBlur={() => setIsEdit(false)}>
        <input
          type="text"
          value={localDescription}
          onChange={(e) => setLocalDescription(e.target.value)}
        ></input>
      </form>
    );

  return (
    <div className="task-item" onClick={() => handleEditStatus()}>
      {displayDescription()}
      <div className="button-container">
        <FaTrash onClick={() => deleteTask(id)} />
        <TiEdit onClick={() => handleEdit()} />
      </div>
    </div>
  );
}
