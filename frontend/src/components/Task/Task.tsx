import { FaTrash } from 'react-icons/fa';
import { TiEdit } from 'react-icons/ti';
import { useState } from 'react';
import './Task.css';

type TaskProps = {
  id: string;
  description: string;
  completed: boolean;
  deleteTask: (id: string) => void;
  editTaskDescription: (id: string, newDescription: string) => void;
  editTaskStatus: (id: string, completed: boolean) => void;
};

export default function Task({
  id,
  description,
  deleteTask,
  completed,
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
    const taskItems: HTMLCollectionOf<Element> =
      document.getElementsByClassName('task-item');
    const descriptionBoxs: HTMLCollectionOf<Element> =
      document.getElementsByClassName('task-description');
    const buttonContainers: HTMLCollectionOf<Element> =
      document.getElementsByClassName('button-container');

    const target = event.target as Element;

    for (let i = 0; i < taskItems.length; i++) {
      if (
        taskItems[i].contains(target) &&
        descriptionBoxs[i] &&
        !descriptionBoxs[i].contains(target) &&
        !buttonContainers[i].contains(target)
      ) {
        if (completed === false) {
          editTaskStatus(id, true);
        } else {
          editTaskStatus(id, false);
        }
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
    <div
      className={`task-item ${completed ? 'completed' : 'pending'}`}
      onClick={(e) => handleEditStatus(e)}
    >
      {displayDescription()}
      <div className="button-container">
        <FaTrash onClick={() => deleteTask(id)} />
        <TiEdit onClick={() => handleEdit()} />
      </div>
    </div>
  );
}
