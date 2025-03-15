import { TaskData } from "../App";

// spreading the task array twice, filtering based on completed/incomplete status,
// then concating the filtered arrays is more readable solution
// but I wanted to have fun with a 2 pointer in place sorting solution
export const sortTasks = (tasks: TaskData[]): TaskData[] => {
  const sortedTasks = tasks;
  let left = 0;
  let right = sortedTasks.length - 1;
  let temp;
  while (left < right) {
    if (sortedTasks[left].completed) {
      temp = sortedTasks[right];
      sortedTasks[right] = sortedTasks[left]
      sortedTasks[left] = temp; 
      right--
    } else if (!sortedTasks[right].completed) {
      temp = sortedTasks[left]
      sortedTasks[left] = sortedTasks[right]
      sortedTasks[right] =  temp;
      left++
    } else {
      right--;
      left++;
    }
  }
  return sortedTasks;
}