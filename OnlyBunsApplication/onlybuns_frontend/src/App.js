import "./App.css";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Login from "./pages/Login";
import Registration from "./pages/Registration";
import CancelTrainingUser from "./pages/CancelTrainingUser";
import BookTrainingUser from "./pages/BookTrainingUser";
import CancelTrainingTrainer from './pages/CancelTrainingTrainer';
import BookTrainingTrainer from './pages/BookTrainingTrainer';
import React from 'react';
import BookedTrainings from "./pages/BookedTrainings";
import TrainingView from "./pages/TrainingView";

function App() {
  return (
    <div className="App">
      <Router>
        <Routes>
          <Route path="/" element={<Login />} />
          <Route path="/login" element={<Login />} />
          <Route path="/registration" element={<Registration />} />
          <Route path="/cancelTrainingUser" element={<CancelTrainingUser/>} />
          <Route path="/BookTrainingUser" element={<BookTrainingUser/>} />
          <Route path="/cancelTrainingTrainer" element={<CancelTrainingTrainer />} />
          <Route path="/bookTrainingTrainer" element={<BookTrainingTrainer />} />
          <Route path="/bookedTrainings" element={<BookedTrainings />} />
            <Route path="/trainingView/:id" element={<TrainingView />} />
        </Routes>
      </Router>
    </div>
  );
}

export default App;
