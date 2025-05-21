const http = require('http');

const hostname = '127.0.0.1'; // localhost
const port = 3000;

const server = http.createServer((req, res) => {
  res.statusCode = 200; // 성공 상태 코드
  res.setHeader('Content-Type', 'text/plain');
  res.end('Hello, this is weatherapp!\n'); // 클라이언트에 응답 메시지 보내기
});

server.listen(port, hostname, () => {
  console.log(`Server running at http://${hostname}:${port}/`);
});
