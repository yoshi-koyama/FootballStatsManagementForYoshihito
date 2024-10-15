import React, { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { useToast } from '../contexts/ToastContext';
import { getCurrentSeason } from '../apis/GetMappings.js';

function RegisterSeasonPage() {
    const { showToast } = useToast();

    const [newSeasonName, setNewSeasonName] = useState(''); // 新規登録用のstate
    const [newStartDate, setNewStartDate] = useState(''); // 新規登録用のstate
    const [newEndDate, setNewEndDate] = useState(''); // 新規登録用のstate
    const [currentSeason, setCurrentSeason] = useState(null);

    useEffect(() => {
        getCurrentSeason(setCurrentSeason);
    }, []);

    // フォームの入力値を管理
    const handleInputChange = (e) => {
        const { name, value } = e.target; // 変更されたフィールドのnameとvalueを取得
        if (name === 'name') {
            setNewSeasonName(value); // シーズン名を更新
        } else if (name === 'startDate') {
            setNewStartDate(value); // 開始日を更新
        } else if (name === 'endDate') {
            setNewEndDate(value); // 終了日を更新
        }
    };

    // 新しいシーズンを登録する処理
    const handleForSubmit = (e) => {
        e.preventDefault(); // ページリロードを防ぐ

        // リクエストボディを作成
        const seasonForJson = {
            name: newSeasonName, // フォームから取得したシーズン名
            startDate: newStartDate, // フォームから取得した開始日
            endDate: newEndDate, // フォームから取得した終了日
        };

        fetch(`/season`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json', // JSON形式で送信
            },
            body: JSON.stringify(seasonForJson), // JSONとして送信
        })
            .then((response) => {
                if (response.ok) {
                    return response.json();
                }
                return response.text().then((text) => { throw new Error(text); });
            })
            .then((newSeason) => {
                setNewSeasonName(''); // 入力欄をリセット
                setNewStartDate(''); // 入力欄をリセット
                setNewEndDate(''); // 入力欄をリセット
                showToast(`Season '${newSeason.name}' registered successfully!`);

                // 新しいシーズンが登録された後、現在のシーズンを再度更新
                setCurrentSeason(newSeason); // 直接新しいシーズンで更新
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
            <br />
            {/* 現在のシーズン名を表示 */}
            {currentSeason && <p>Current Season: {currentSeason.name}</p>}
            <h1>Register Season</h1>
            <form onSubmit={handleForSubmit}>
                <input
                    type="text"
                    name="name"
                    placeholder="Season name like yyyy-yy"
                    value={newSeasonName}
                    onChange={handleInputChange}
                    required
                />
                <input
                    type="date"
                    name="startDate"
                    placeholder="Start Date"
                    value={newStartDate}
                    onChange={handleInputChange}
                    required
                />
                <input
                    type="date"
                    name="endDate"
                    placeholder="End Date"
                    value={newEndDate}
                    onChange={handleInputChange}
                    required
                />
                <button type="submit">Register</button>
            </form>
        </div>
    );
}

export default RegisterSeasonPage;