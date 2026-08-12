import React, { useEffect, useState } from 'react';
import { getVideos } from '../api';

function VideoList({ refreshTrigger, onSelectVideo }) {
    const [videos, setVideos] = useState([]);

    useEffect(() => {
        getVideos()
            .then((res) => setVideos(res.data.content))
            .catch((err) => console.error(err));
    }, [refreshTrigger]); // re-fetch whenever refreshTrigger changes (e.g. after upload)

    return (
        <div>
            <h2>Videos</h2>
            <div className="video-grid">
                {videos.map((video) => (
                    <div
                        key={video.id}
                        onClick={() => onSelectVideo(video.id)}
                        className="video-card"
                    >
                        <h3>{video.title}</h3>
                        <p>{video.description}</p>
                        <small>{(video.fileSize / 1024 / 1024).toFixed(2)} MB</small>
                    </div>
                ))}
            </div>
        </div>
    );
}

export default VideoList;