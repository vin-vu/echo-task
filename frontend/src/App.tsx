import { useCallback, useEffect, useState } from 'react';
import TodoForm from './components/taskform/TaskForm';
import Task from './components/task/Task';
import Microphone from './components/microphone/Microphone';
import { Intent, IntentResponse } from './api/Api';
import { sortTasks } from './algorithm/Algorithm';
import {
  addTaskAPI,
  deleteTaskAPI,
  getAllTasksAPI,
  updateTaskStatusAPI,
} from './api/Api';
import './App.css';
import IntentStatistics from './components/intent-statistics/IntentStatistics';

export type TaskData = {
  description: string;
  id: string;
  completed: boolean;
};

export default function App() {
  const [tasks, setTasks] = useState<TaskData[]>([]);
  const [taskDescription, setTaskDescription] = useState('');
  const [intentScores, setIntentScores] = useState<Map<string, Set<Intent>>>(
    new Map()
  );

  const addVoiceTask = (task: TaskData) => {
    setTasks((tasks) => [task, ...tasks]);
  };

  const addNonVoiceTask = async (taskDescription: string) => {
    const task = await addTaskAPI(taskDescription);
    if (task) {
      setTasks((tasks) => [task, ...tasks]);
    }
  };

  const addTask = useCallback(
    async (taskInput: string | TaskData, voiceCommand: boolean) => {
      if (voiceCommand) {
        addVoiceTask(taskInput as TaskData);
      } else {
        addNonVoiceTask(taskInput as string);
      }
    },
    []
  );

  const deleteTask = useCallback(async (id: string, voiceCommand: boolean) => {
    if (!voiceCommand) {
      await deleteTaskAPI(id);
    }
    setTasks((prevTasks) => prevTasks.filter((task) => task.id !== id));
  }, []);
  const editTaskDescription = (id: string, newDescription: string): void => {
    setTasks((prevTasks) => {
      return prevTasks.map((task) =>
        task.id === id ? { ...task, description: newDescription } : task
      );
    });
  };

  const editTaskStatus = useCallback(
    async (id: string, completedStatus: boolean, voiceCommand: boolean) => {
      if (!voiceCommand) {
        updateTaskStatusAPI(id, completedStatus);
      }
      setTasks((prevTasks) => {
        const updatedTasks = prevTasks.map((task) =>
          task.id === id ? { ...task, completed: completedStatus } : task
        );
        return [
          ...updatedTasks
            .filter((task) => !task.completed)
            .concat(...updatedTasks.filter((task) => task.completed)),
        ];
      });
    },
    []
  );

  const handleVoiceCommands = useCallback(
    (intentPayload: IntentResponse) => {
      const { id, intent, description, completed } = intentPayload;
      const task: TaskData = { id, description, completed };
      if (intent === Intent.ADD_TASK) {
        addTask(task, true);
      } else if (intent === Intent.DELETE_TASK) {
        deleteTask(id, true);
      } else if (intent === Intent.COMPLETED_TASK) {
        editTaskStatus(task.id, task.completed, true);
      }

      // JSON serialization from HTTP response converts scores data structure to plain object
      const convertedIntentScores = new Map<string, Set<Intent>>(
        Object.entries(intentPayload.rankedIntentScores).map(([key, value]) => [
          key,
          new Set(value),
        ])
      );
      setIntentScores(convertedIntentScores);
      setTaskDescription(description);
    },
    [addTask, deleteTask, editTaskStatus]
  );

  useEffect(() => {
    async function fetchTasks() {
      const allTaskResponse = await getAllTasksAPI();
      if (allTaskResponse) {
        const sortedTasks = sortTasks(allTaskResponse);
        setTasks(sortedTasks);
      }
    }
    fetchTasks();
  }, []);

  const displayTasks = tasks.map((task) => (
    <Task
      key={task.id}
      id={task.id}
      description={task.description}
      completed={task.completed}
      deleteTask={deleteTask}
      editTaskDescription={editTaskDescription}
      editTaskStatus={editTaskStatus}
    />
  ));

  return (
    <>
      <div className="app-container">
        <div className="main-container">
          <span>EchoTask</span>
          <div className="task-container">
            <TodoForm addTask={addTask} />
            <div className="task-list">{displayTasks}</div>
          </div>
          <Microphone handleVoiceCommands={handleVoiceCommands} />
        </div>
        <IntentStatistics intentScores={intentScores} taskDescription={taskDescription}/>
      </div>
    </>
  );
}
