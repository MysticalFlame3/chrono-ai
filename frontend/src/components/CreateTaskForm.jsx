import React, { useState } from 'react';
import { createTask, parseQueryToCron } from '../services/apiService';

const CreateTaskForm = ({ onTaskCreated }) => {
  const [aiQuery, setAiQuery] = useState('');
  const [formData, setFormData] = useState({
    name: '',
    description: '',
    cronExpression: '',
    notificationType: 'EMAIL',
    notificationTarget: '',
  });

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleParseQuery = async () => {
    if (!aiQuery) {
      alert('Please enter a query for the AI.');
      return;
    }
    try {
      const response = await parseQueryToCron(aiQuery);
      setFormData({ ...formData, cronExpression: response.data });
    } catch (error) {
      console.error('Failed to parse query:', error);
      alert('AI failed to generate a CRON expression. Please try a different query.');
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await createTask(formData);
      onTaskCreated(response.data); // Pass the new task up to the dashboard
      // Reset form
      setFormData({ name: '', description: '', cronExpression: '', notificationType: 'EMAIL', notificationTarget: '' });
      setAiQuery('');
      alert('Task created successfully!');
    } catch (error) {
      console.error('Failed to create task:', error);
      alert('Failed to create task.');
    }
  };

  return (
    <form onSubmit={handleSubmit} className="bg-gray-800 p-8 rounded-lg shadow-lg mb-8">
      <h2 className="text-xl text-white mb-4">Create a New Task</h2>

      {/* AI Parser Input */}
      <div className="flex items-center gap-2 mb-4">
        <input
          type="text"
          value={aiQuery}
          onChange={(e) => setAiQuery(e.target.value)}
          placeholder="Describe your schedule (e.g., 'every day at 5am')"
          className="flex-grow bg-gray-700 text-white p-2 rounded"
        />
        <button type="button" onClick={handleParseQuery} className="bg-purple-600 text-white p-2 rounded hover:bg-purple-700">
          Generate with AI
        </button>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <input type="text" name="name" value={formData.name} onChange={handleChange} placeholder="Task Name" className="bg-gray-700 text-white p-2 rounded" required />
        <input type="text" name="cronExpression" value={formData.cronExpression} onChange={handleChange} placeholder="CRON Expression (e.g., * * * * *)" className="bg-gray-700 text-white p-2 rounded" required />
        <textarea name="description" value={formData.description} onChange={handleChange} placeholder="Description" className="md:col-span-2 bg-gray-700 text-white p-2 rounded" />
        <select name="notificationType" value={formData.notificationType} onChange={handleChange} className="bg-gray-700 text-white p-2 rounded">
          <option value="EMAIL">Email</option>
          <option value="WEBHOOK">Webhook</option>
        </select>
        <input type="text" name="notificationTarget" value={formData.notificationTarget} onChange={handleChange} placeholder="Email Address or Webhook URL" className="bg-gray-700 text-white p-2 rounded" required />
      </div>
      <button type="submit" className="mt-4 w-full bg-blue-600 text-white p-2 rounded hover:bg-blue-700">Create Task</button>
    </form>
  );
};

export default CreateTaskForm;