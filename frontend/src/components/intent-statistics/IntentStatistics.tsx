import { useEffect, useState } from 'react';
import './IntentStatistics.css';
import { Intent } from '../../api/Api';

interface IntentStatisticsProps {
  intentScores: Map<string, Set<Intent>>;
  taskDescription: string;
  transcript: string;
}

export default function IntentStatistics({
  intentScores,
  taskDescription,
  transcript,
}: IntentStatisticsProps) {
  const [addTaskScore, setAddTaskScore] = useState('00.00%');
  const [deleteTaskScore, setDeleteTaskScore] = useState('00.00%');
  const [completedTaskScore, setCompletedTaskScore] = useState('00.00%');
  const [highestIntent, setHighestIntent] = useState<Intent>();

  useEffect(() => {
    const handleIntentScores = () => {
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
    };
    handleIntentScores();
  }, [intentScores]);

  useEffect(() => {
    const calculateHighestIntent = () => {
      let highestScore = 0;
      let highestIntent: Intent;
      for (const [score, intents] of intentScores) {
        const scoreNum = Number(score);
        if (highestScore < scoreNum) {
          highestScore = scoreNum;
          highestIntent = Array.from(intents)[0];
          setHighestIntent(highestIntent);
        }
      }
    };
    calculateHighestIntent();
  }, [intentScores]);

  const getStyle = (intent: Intent) =>
    intent === highestIntent ? { fontWeight: 'bold' } : {};

  return (
    <div className="statistics-container">
      <div className="intent-scores">
        <span style={getStyle(Intent.ADD_TASK)}>ADD TASK - {addTaskScore}</span>
        <span style={getStyle(Intent.DELETE_TASK)}>DELETE TASK - {deleteTaskScore}</span>
        <span style={getStyle(Intent.COMPLETED_TASK)}>COMPLETED TASK - {completedTaskScore}</span>
      </div>
      <div className="text-container">
        <div className="text-row">
          <span className="label">Transcription :</span>
          <span>{transcript}</span>
        </div>
        <div className="text-row">
          <span className="label">Parsed Description :</span>
          <span>{taskDescription}</span>
        </div>
      </div>
    </div>
  );
}
