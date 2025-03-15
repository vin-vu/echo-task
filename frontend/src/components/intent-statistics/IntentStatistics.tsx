import { useState } from 'react';

interface IntentStatisticsProps {
  intentScores: Map<string, Set<string>>;
}

export default function IntentStatistics({
  intentScores,
}: IntentStatisticsProps) {
  const [addTaskScore, setAddTaskScore] = useState(0);
  const [deleteTaskScore, setDeleteTaskScore] = useState(0);
  const [completedTaskScore, setCompletedTaskScore] = useState(0);

  return (
    <div className="intent-statistics">
      <div className="intent-scores">
        <span>ADD TASK: {addTaskScore}</span>
        <span>DELETED TASK: {deleteTaskScore}</span>
        <span>COMPLETED TASK: {completedTaskScore}</span>
      </div>
    </div>
  );
}
