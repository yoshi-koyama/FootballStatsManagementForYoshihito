import React, { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';

function HomePage() {
    return (
        <div>
            {/* CountriesPageへのリンク */}
            <Link to="/countries">データ管理</Link>
            <br />
            <Link to="/register-season">新シーズン登録</Link>
        </div>
    );
}

export default HomePage;
