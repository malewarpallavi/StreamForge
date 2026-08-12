import React from 'react';
import { getStreamUrl } from '../api';

function VideoPlayer({ videoId }) {
    if (!videoId) return null;

    return (
        <div style={{ marginTop: '2rem' }}>
            <h2>Now Playing</h2>
            <video controls width="600" src={getStreamUrl(videoId)}>
                Your browser does not support the video tag.
            </video>
        </div>
    );
}

export default VideoPlayer;