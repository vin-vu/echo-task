import { TaskData } from "../App";

// for fun in place sorting algo
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