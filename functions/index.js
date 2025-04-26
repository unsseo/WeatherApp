const functions = require("firebase-functions");
const axios = require("axios");

const API_KEY = "AIzaSyAU7f_gIf4-WTlf2d3cr1fz86nlrkzJ5bo";

exports.searchYouTube = functions.https.onRequest(async (req, res) => {
  const query = req.query.query || "비 저녁 플레이리스트"; // 기본값 설정

  try {
    // YouTube API 요청
    const response = await axios.get(
        "https://www.googleapis.com/youtube/v3/search",
        {
          params: {
            part: "snippet",
            q: query,
            key: API_KEY,
            type: "video",
            maxResults: 1,
          },
        },
    );

    // 첫 번째 비디오 ID를 반환
    const videoId = response.data.items[0].id.videoId;
    res.json({videoId});
  } catch (error) {
    console.error(error);
    res.status(500).send("YouTube API 요청 실패");
  }
});
