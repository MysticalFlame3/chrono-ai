import React, { useState, useEffect } from 'react';
import { logoutUser, getTasks, deleteTask, createTask } from '../services/apiService';
import CreateTaskForm from '../components/CreateTaskForm';
import TaskHistory from '../components/TaskHistory';

const DashboardPage = ({ setIsLoggedIn }) => {
  const [tasks, setTasks] = useState([]);
  const [viewingHistoryOf, setViewingHistoryOf] = useState(null);

  const fetchTasks = async () => {
    try {
      const response = await getTasks();
      setTasks(response.data);
    } catch (error) {
      console.error('Failed to fetch tasks:', error);
      if (error.response && (error.response.status === 401 || error.response.status === 403)) {
        handleLogout();
      }
    }
  };

  useEffect(() => {
    fetchTasks();
  }, []);

  const handleLogout = () => {
    logoutUser();
    setIsLoggedIn(false);
  };

  const handleTaskCreated = (newTask) => {
    setTasks((prevTasks) => [...prevTasks, newTask]);
  };

  const handleDeleteTask = async (taskId) => {
    if (window.confirm('Are you sure you want to delete this task?')) {
      try {
        await deleteTask(taskId);
        setTasks(tasks.filter((task) => task.id !== taskId));
        alert('Task deleted successfully!');
      } catch (error) {
        console.error('Failed to delete task:', error);
        alert('Failed to delete task.');
      }
    }
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
      <header className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold text-white">ChronoAI Dashboard</h1>
        <button
          onClick={handleLogout}
          className="bg-red-600 text-white px-4 py-2 rounded-lg hover:bg-red-700 transition duration-300"
        >
          Logout
        </button>
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
