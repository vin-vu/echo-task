import { useCallback, useEffect, useState } from 'react';
import './IntentStatistics.css';
import { Intent } from '../../api/Api';

interface IntentStatisticsProps {
  intentScores: Map<string, Set<string>>;
  taskDescription: string;
}

export default function IntentStatistics({
  intentScores,
  taskDescription,
}: IntentStatisticsProps) {
  const [addTaskScore, setAddTaskScore] = useState('00.00%');
  const [deleteTaskScore, setDeleteTaskScore] = useState('00.00%');
  const [completedTaskScore, setCompletedTaskScore] = useState('00.00%');

  const handleIntentScores = useCallback(() => {
    for (const [score, intents] of intentScores) {
      const scorePercentage =
        (Number(score) * 100).toFixed(2).padStart(5, '0') + '%';
      for (const intent of intents) {
        if (intent === Intent.ADD_TASK) {
          setAddTaskScore(scorePercentage);
        } else if (intent === Intent.DELETE_TASK) {
          setDeleteTaskScore(scorePercentage);
        } else if (intent === Intent.COMPLETED_TASK) {
          setCompletedTaskScore(scorePercentage);
        }
      }
    }
  }, [intentScores]);

  useEffect(() => {
    handleIntentScores();
  }, [handleIntentScores]);

  return (
    <div className="statistics-container">
      <div className="intent-scores">
        <span>ADD TASK - {addTaskScore}</span>
        <span>DELETED TASK - {deleteTaskScore}</span>
        <span>COMPLETED TASK - {completedTaskScore}</span>
      </div>
      <div className="text-container">
        <div className="text-row">
          <span className="label">Transcription :</span>
          <span>test</span>
        </div>
        <div className="text-row">
          <span className="label">Parsed Description :</span>
          <span>{taskDescription}</span>
        </div>
      </div>
    </div>
  );
}
