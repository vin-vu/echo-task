import { FaTrash } from 'react-icons/fa';
import { TiEdit } from 'react-icons/ti';
import { useState } from 'react';
import './Task.css';
import { TaskStatus } from '../../App';

type TaskProps = {
  id: string;
  description: string;
  status: TaskStatus;
  deleteTask: (id: string) => void;
  editTaskDescription: (id: string, newDescription: string) => void;
  editTaskStatus: (id: string, status: TaskStatus) => void;
};

export default function Task({
  id,
  description,
  deleteTask,
  status,
  editTaskDescription,
  editTaskStatus,
}: TaskProps) {
  const [isEdit, setIsEdit] = useState<boolean>(false);
  const [localDescription, setLocalDescription] = useState<string>(description);

  const handleEdit = () => {
    setIsEdit(!isEdit);
    setLocalDescription(description);
  };

  const handleEditStatus = (event: React.MouseEvent) => {
    const taskItem: Element = document.getElementsByClassName('task-item')[0];
    const descriptionBox: Element =
      document.getElementsByClassName('task-description')[0];
    const buttonContainer: Element =
      document.getElementsByClassName('button-container')[0];

    const target = event.target as Element;

    if (
      taskItem.contains(target) &&
      descriptionBox &&
      !descriptionBox.contains(target) &&
      !buttonContainer.contains(target)
    ) {
      if (status === TaskStatus.PENDING) {
        console.log('update to done');
        editTaskStatus(id, TaskStatus.DONE);
      } else {
        console.log('update to pending');
        editTaskStatus(id, TaskStatus.PENDING);
      }
    }
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    editTaskDescription(id, localDescription);
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
    <div className="task-item" onClick={(e) => handleEditStatus(e)}>
      {displayDescription()}
      <div className="button-container">
        <FaTrash onClick={() => deleteTask(id)} />
        <TiEdit onClick={() => handleEdit()} />
      </div>
    </div>
  );
}
