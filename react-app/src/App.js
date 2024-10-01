import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import HomePage from './pages/HomePage.js';
import CountriesPage from './pages/CountriesPage.js';
import LeaguesPage from './pages/LeaguesPage.js';
import ClubsPage from './pages/ClubsPage.js';
import PlayersPage from './pages/PlayersPage.js';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/countries" element={<CountriesPage />} />
        {/* 国IDに基づいてリーグページに遷移 */}
        <Route path="/countries/:countryId/leagues" element={<LeaguesPage />} />
        {/* リーグIDに基づいてクラブページに遷移 */}
        <Route path="/countries/:countryId/leagues/:leagueId/clubs" element={<ClubsPage />} />
        {/* クラブIDに基づいて選手ページに遷移 */}
        <Route path="/countries/:countryId/leagues/:leagueId/clubs/:clubId/players" element={<PlayersPage />} />
      </Routes>
    </Router>
  );
}

export default App;
