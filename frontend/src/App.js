import React, { useState } from 'react';
import UploadVideo from './components/UploadVideo';
import VideoList from './components/VideoList';
import VideoPlayer from './components/VideoPlayer';

function App() {
  const [refreshTrigger, setRefreshTrigger] = useState(0);
  const [selectedVideoId, setSelectedVideoId] = useState(null);

  const handleUploadSuccess = () => {
    setRefreshTrigger((prev) => prev + 1); // triggers VideoList to re-fetch
  };

  return (
      <div style={{ padding: '2rem', fontFamily: 'sans-serif' }}>
        <h1>StreamForge</h1>
        <UploadVideo onUploadSuccess={handleUploadSuccess} />
        <VideoList refreshTrigger={refreshTrigger} onSelectVideo={setSelectedVideoId} />
        <VideoPlayer videoId={selectedVideoId} />
      </div>
  );
}

export default App;