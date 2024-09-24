import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import CountriesPage from './pages/CountriesPage.js';
// import LeaguesPage from './pages/LeaguesPage';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<CountriesPage />} />
        {/* 国IDに基づいてリーグページに遷移 */}
        {/* <Route path="/countries/:countryId/leagues" element={<LeaguesPage />} /> */}
      </Routes>
    </Router>
  );
}

export default App;
