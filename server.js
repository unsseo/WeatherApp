require('dotenv').config();
console.log('API Key:', process.env.YOUTUBE_API_KEY);

const express = require('express');
const axios = require('axios');
const cors = require('cors');
const app = express();
const port = 3000;

app.use(cors());

// YouTube API 키를 숨기기 위한 환경 변수 사용
const API_KEY = process.env.YOUTUBE_API_KEY;  // 환경 변수로 API 키 관리

app.get('/youtube/v3/search', async (req, res) => {   // 경로 수정
    const query = req.query.q; // 여기도 'q'로 받아야 Retrofit과 일치
    console.log("요청 받은 검색어:", query);

    try {
        const response = await axios.get('https://www.googleapis.com/youtube/v3/search', {
            params: {
                key: API_KEY,
                part: 'snippet',
                q: query,
                type: 'video'
            }
        });
         const simplifiedItems = response.data.items.map(item => ({
            id: {
                videoId: item.id.videoId // Android가 기대하는 구조에 맞춤
            },
            snippet: {
                title: item.snippet.title
            }
        }));

        // 변환된 데이터 전송
        res.json({
            items: simplifiedItems // Android가 요구하는 필드명 유지
        });
    } catch (error) {
        if (error.response) {
            console.error('API 요청 오류:', error.response.status, error.response.data);
        } else {
            console.error('API 요청 실패:', error.message);
        }
        res.status(500).send('YouTube API 요청 실패');
    }
});


app.listen(port,'0.0.0.0', () => {
    console.log(`Server running at http://localhost:${port}`);
});
