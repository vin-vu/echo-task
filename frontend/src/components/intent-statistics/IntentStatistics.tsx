import { useCallback, useEffect, useState } from 'react';
import './IntentStatistics.css';
import { Intent } from '../../api/Api';

interface IntentStatisticsProps {
  intentScores: Map<string, Set<string>>;
}

export default function IntentStatistics({
  intentScores,
}: IntentStatisticsProps) {
  const [addTaskScore, setAddTaskScore] = useState('0');
  const [deleteTaskScore, setDeleteTaskScore] = useState('0');
  const [completedTaskScore, setCompletedTaskScore] = useState('0');

  const handleIntentScores = useCallback(() => {
    for (const [score, intents] of intentScores) {
      const scorePercentage = (Number(score) * 100).toFixed(2) + '%';
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
        <span>ADD TASK: {addTaskScore}</span>
        <span>DELETED TASK: {deleteTaskScore}</span>
        <span>COMPLETED TASK: {completedTaskScore}</span>
      </div>
    </div>
  );
}
