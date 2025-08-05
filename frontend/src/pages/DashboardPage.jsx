import React, { useState, useEffect } from 'react';
import { logoutUser, getTasks, deleteTask } from '../services/apiService';
import CreateTaskForm from '../components/CreateTaskForm';
import TaskHistory from '../components/TaskHistory'; 

const DashboardPage = ({ setIsLoggedIn }) => {
  const [tasks, setTasks] = useState([]);
  const [viewingHistoryOf, setViewingHistoryOf] = useState(null); 

  const fetchTasks = async () => {
    // ... 
  };

  useEffect(() => {
    fetchTasks();
  }, []);

  const handleLogout = () => {
    // ... )
  };

  const handleTaskCreated = (newTask) => {
    // ... 
  };

  const handleDeleteTask = async (taskId) => {
    // ... 
  };

  const toggleHistory = (taskId) => {
    if (viewingHistoryOf === taskId) {
      setViewingHistoryOf(null); 
    } else {
      setViewingHistoryOf(taskId); 
    }
  };

  return (
    <div className="w-full max-w-6xl mx-auto p-4">
      <header /* ... */ >
          {/* ... Header is  same ... */}
      </header>

      <main>
        <CreateTaskForm onTaskCreated={handleTaskCreated} />

        <div className="bg-gray-800 p-8 rounded-lg shadow-lg">
          <h2 className="text-xl text-white mb-4">Your Scheduled Tasks</h2>
          <div className="space-y-4">
            {tasks.length > 0 ? (
              tasks.map((task) => (
                <div key={task.id} className="bg-gray-700 p-4 rounded-lg">
                  <div className="flex justify-between items-center">
                    <div>
                      <h3 className="font-bold text-lg text-blue-400">{task.name}</h3>
                      <p className="text-gray-300">{task.description}</p>
                      <p className="text-sm text-gray-400 mt-2 font-mono bg-gray-900 px-2 py-1 rounded inline-block">{task.cronExpression}</p>
                    </div>
                    <div className="flex gap-2">
                      <button onClick={() => toggleHistory(task.id)} className="bg-gray-600 text-white px-3 py-1 rounded hover:bg-gray-500 text-sm">
                        {viewingHistoryOf === task.id ? 'Hide History' : 'View History'}
                      </button>
                      <button onClick={() => handleDeleteTask(task.id)} className="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600 text-sm">
                        Delete
                      </button>
                    </div>
                  </div>
                  {/*  render history component */}
                  {viewingHistoryOf === task.id && <TaskHistory taskId={task.id} />}
                </div>
              ))
            ) : (
              <p className="text-gray-400">You haven't created any tasks yet.</p>
            )}
          </div>
        </div>
      </main>
    </div>
  );
};

export default DashboardPage;
