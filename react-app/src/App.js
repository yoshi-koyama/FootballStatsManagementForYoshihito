import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import HomePage from './pages/HomePage.js';
import RegisterSeasonPage from './pages/RegisterSeasonPage.js';
import CountriesPage from './pages/CountriesPage.js';
import LeaguesPage from './pages/LeaguesPage.js';
import ClubsPage from './pages/ClubsPage.js';
import PlayersPage from './pages/PlayersPage.js';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<HomePage />} />
        {/* シーズン登録ページに遷移 */}
        <Route path="/register-season" element={<RegisterSeasonPage />} />
        {/* データ管理ページに遷移 */}
        <Route path="/countries" element={<CountriesPage />} />
        {/* 国IDに基づいてリーグページに遷移 */}
        <Route path="/countries/:countryId/leagues" element={<LeaguesPage />} />
        {/* リーグIDに基づいてクラブページに遷移 */}
        <Route path="/countries/:countryId/leagues/:leagueId/clubs" element={<ClubsPage />} />
        {/* クラブIDに基づいて選手ページに遷移 */}
        <Route path="/countries/:countryId/leagues/:leagueId/clubs/:clubId/players" element={<PlayersPage />} />
        {/* 選手IDに基づいて選手詳細ページに遷移 */}
        {/* 試合結果登録画面に遷移 */}
      </Routes>
    </Router>
  );
}

export default App;
