import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';

function CountriesPage() {
  const [countries, setCountries] = useState([]);
  const [newCountryName, setNewCountryName] = useState(''); // 新規登録用のstate
  const [message, setMessage] = useState(''); // フィードバックメッセージ用のstate

  useEffect(() => {
    fetch('/countries') // APIエンドポイント
      .then((response) => response.json())
      .then((data) => setCountries(data));
  }, []);

  // フォームの入力値を管理
  const handleInputChange = (e) => {
    setNewCountryName(e.target.value);
  };

  // 新しい国を登録する処理
  const handleFormSubmit = (e) => {
    e.preventDefault(); // ページリロードを防ぐ

    fetch('/country', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
      body: new URLSearchParams({
        name: newCountryName,
      }),
    })
      .then((response) => {
        if (response.ok) {
          return response.json();
        }
        throw new Error('Failed to register country');
      })
      .then((newCountry) => {
        setCountries([...countries, newCountry]); // 新しい国をリストに追加
        setNewCountryName(''); // 入力欄をリセット
        setMessage('Country registered successfully!');
      })
      .catch((error) => {
        setMessage('Failed to register country.');
        console.error(error);
      });
  };

  return (
    <div>
      {/* Homeに戻るリンク */}
      <Link to="/">Home</Link>
      <h1>Countries</h1>
      <ul>
        {countries.map((country) => (
          <li key={country.id}>
            <Link to={`/countries/${country.id}/leagues`}>{country.name}</Link>
          </li>
        ))}
      </ul>

      {/* 新しい国の登録フォーム */}
      <h2>Register New Country</h2>
      <form onSubmit={handleFormSubmit}>
        <input
          type="text"
          placeholder="Country name"
          value={newCountryName}
          onChange={handleInputChange}
          required
        />
        <button type="submit">Register</button>
      </form>

      {/* 登録結果のフィードバックメッセージ */}
      {message && <p>{message}</p>}
    </div>
  );
}

export default CountriesPage;
