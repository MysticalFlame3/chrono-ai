import React, { useState, useEffect } from 'react';
import toast, { Toaster } from 'react-hot-toast';
import { logoutUser, getTasks, deleteTask } from '../services/apiService';
import CreateTaskForm from '../components/CreateTaskForm';
import TaskHistory from '../components/TaskHistory';
import { LogOut, History, Trash2 } from 'lucide-react';

const DashboardPage = ({ setIsLoggedIn }) => {
  const [tasks, setTasks] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [viewingHistoryOf, setViewingHistoryOf] = useState(null);
  const [confirmDelete, setConfirmDelete] = useState(null);

  useEffect(() => {
    const fetchTasks = async () => {
      setIsLoading(true);
      try {
        const response = await getTasks();
        setTasks(response.data);
      } catch (error) {
        console.error('Failed to fetch tasks:', error);
        toast.error('Could not fetch your tasks.');
        if (error.response && (error.response.status === 401 || error.response.status === 403)) {
          handleLogout();
        }
      } finally {
        setIsLoading(false);
      }
    };
    fetchTasks();
  }, []);

  const handleLogout = () => {
    logoutUser();
    setIsLoggedIn(false);
    toast.success('Logged out successfully.');
  };

  const handleTaskCreated = (newTask) => {
    setTasks((prevTasks) => [newTask, ...prevTasks]);
  };

  const handleDeleteTask = async (taskId) => {
    try {
      await deleteTask(taskId);
      setTasks(tasks.filter((task) => task.id !== taskId));
      toast.success('Task deleted successfully!');
      setConfirmDelete(null);
    } catch (error) {
      console.error('Failed to delete task:', error);
      toast.error('Failed to delete task.');
    }
  };

  const toggleHistory = (taskId) => {
    setViewingHistoryOf(viewingHistoryOf === taskId ? null : taskId);
  };

  return (
    <div className="w-screen h-screen bg-gradient-to-br from-gray-900 via-gray-800 to-gray-700 text-white flex flex-col">
      <Toaster position="top-right" reverseOrder={false} />

      {/* Header */}
      <header className="flex justify-between items-center p-6 bg-gray-800 shadow-md">
        <h1 className="text-4xl font-bold text-white">ChronoAI Dashboard</h1>
        <button
          onClick={handleLogout}
          className="flex items-center gap-2 text-red-500 hover:text-red-400 transition"
        >
          <LogOut /> Logout
        </button>
      </header>

      {/* Main Content */}
      <main className="flex-1 overflow-auto p-6 flex flex-col gap-6">
        {/* Task creation form */}
        <div className="bg-gray-800/70 backdrop-blur-md rounded-2xl p-6 shadow-2xl">
          <CreateTaskForm onTaskCreated={handleTaskCreated} />
        </div>

        {/* Tasks List */}
        {isLoading ? (
          <div className="text-center text-gray-300 mt-10">Loading tasks...</div>
        ) : tasks.length === 0 ? (
          <div className="text-center text-gray-300 mt-10">No tasks available. Create your first task!</div>
        ) : (
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
            {tasks.map((task) => (
              <div
                key={task.id}
                className="bg-gray-800/70 backdrop-blur-md rounded-2xl p-6 shadow-2xl flex flex-col justify-between"
              >
                <div>
                  <h3 className="text-2xl font-semibold mb-2">{task.title}</h3>
                  <p className="text-gray-300 mb-4">{task.description}</p>
                </div>

                <div className="flex justify-between items-center mt-4">
                  <button
                    className="flex items-center gap-2 text-gray-300 hover:text-white transition"
                    onClick={() => toggleHistory(task.id)}
                  >
                    <History /> History
                  </button>

                  {confirmDelete === task.id ? (
                    <div className="flex gap-2">
                      <button
                        className="px-3 py-1 bg-red-600 hover:bg-red-500 rounded-md text-white text-sm"
                        onClick={() => handleDeleteTask(task.id)}
                      >
                        Yes
                      </button>
                      <button
                        className="px-3 py-1 bg-gray-600 hover:bg-gray-500 rounded-md text-white text-sm"
                        onClick={() => setConfirmDelete(null)}
                      >
                        No
                      </button>
                    </div>
                  ) : (
                    <button
                      className="flex items-center gap-2 text-orange-400 hover:text-orange-300 transition"
                      onClick={() => setConfirmDelete(task.id)}
                    >
                      <Trash2 /> Delete
                    </button>
                  )}
                </div>

                {/* Task history */}
                {viewingHistoryOf === task.id && (
                  <div className="mt-4 bg-gray-700/50 backdrop-blur-md p-4 rounded-xl transition-all duration-300">
                    <TaskHistory key={task.id} taskId={task.id} />
                  </div>
                )}
              </div>
            ))}
          </div>
        )}
      </main>
    </div>
  );
};

export default DashboardPage;
