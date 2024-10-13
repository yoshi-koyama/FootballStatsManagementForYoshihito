import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { toast } from 'react-toastify'; // トースト通知を追加
import 'react-toastify/dist/ReactToastify.css'; // トーストのスタイル
import { getCountries } from '../apis/GetMappings.js';

function CountriesPage() {
  const [countries, setCountries] = useState([]);
  const [newCountryName, setNewCountryName] = useState(''); // 新規登録用のstate

  useEffect(() => {
    getCountries(setCountries);
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
        return response.text().then((text) => { throw new Error(text); });
      })
      .then((newCountry) => {
        setCountries([...countries, newCountry]); // 新しい国をリストに追加
        setNewCountryName(''); // 入力欄をリセット
        // 成功メッセージをトーストで表示
        toast.success(`Country '${newCountry.name}' registered successfully!`);
      })
      .catch((error) => {
        alert('Error: ' + error.message);
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
    </div>
  );
}

export default CountriesPage;
