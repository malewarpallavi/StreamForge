import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  stages: [
    { duration: '10s', target: 50 },   // ramp up to 50 concurrent users
    { duration: '20s', target: 200 },  // ramp up to 200 concurrent users
    { duration: '20s', target: 200 },  // hold at 200 for 20s
    { duration: '10s', target: 0 },    // ramp down
  ],
};

export default function () {
  // Test 1: list videos (paginated)
  const listRes = http.get('http://localhost:8080/api/videos?page=0&size=10');
  check(listRes, { 'list status is 200': (r) => r.status === 200 });

  // Test 2: stream a video with a byte-range request
  const streamRes = http.get('http://localhost:8080/api/videos/1/stream', {
    headers: { Range: 'bytes=0-102400' }, // first 100KB
  });
  check(streamRes, { 'stream status is 206': (r) => r.status === 206 });

  sleep(1);
}
