import React, { useState, useEffect } from 'react';
import { getTaskHistory } from '../services/apiService';

const TaskHistory = ({ taskId }) => {
  const [history, setHistory] = useState([]);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const fetchHistory = async () => {
      try {
        setIsLoading(true);
        const response = await getTaskHistory(taskId);
        setHistory(response.data);
      } catch (error) {
        console.error("Failed to fetch task history:", error);
      } finally {
        setIsLoading(false);
      }
    };
    fetchHistory();
  }, [taskId]); // Refetch if the taskId changes

  if (isLoading) {
    return <p className="text-gray-400 p-4">Loading history...</p>;
  }

  return (
    <div className="mt-4 p-4 bg-gray-900 rounded-lg">
      <h4 className="font-semibold text-gray-300 mb-2">Execution History:</h4>
      {history.length > 0 ? (
        <ul className="space-y-2">
          {history.map((log) => (
            <li key={log.id} className="text-sm p-2 bg-gray-800 rounded">
              <span className={`font-bold ${log.status === 'SUCCESS' ? 'text-green-500' : 'text-red-500'}`}>
                {log.status}
              </span>
              <span className="text-gray-400 mx-2">
                - {new Date(log.executionTime).toLocaleString()}
              </span>
              <p className="text-gray-500 text-xs mt-1">{log.details}</p>
            </li>
          ))}
        </ul>
      ) : (
        <p className="text-gray-500">No execution history found for this task.</p>
      )}
    </div>
  );
};

export default TaskHistory;