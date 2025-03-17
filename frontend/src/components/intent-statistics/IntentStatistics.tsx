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

      for (const intent of intents) {
        console.log('score - intent: ', score, intent);
        if (intent === Intent.ADD_TASK) {
          setAddTaskScore(score);
        } else if (intent === Intent.DELETE_TASK) {
          setDeleteTaskScore(score);
        } else {
          setCompletedTaskScore(score);
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
