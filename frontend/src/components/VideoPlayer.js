import React, { useRef, useEffect } from 'react';
import axios from 'axios';
import { getStreamUrl } from '../api';

const API_BASE = 'http://localhost:8080/api/videos';
const USER_ID = 'pallavi';

function VideoPlayer({ videoId }) {
    const videoRef = useRef(null);

    useEffect(() => {
        if (!videoId) return;

        // Load saved progress on video change
        axios.get(`${API_BASE}/${videoId}/progress?userIdentifier=${USER_ID}`)
            .then((res) => {
                if (videoRef.current && res.data.positionSeconds) {
                    videoRef.current.currentTime = res.data.positionSeconds;
                }
            })
            .catch(() => {});

        // Save progress every 5 seconds, regardless of exact currentTime value
        const intervalId = setInterval(() => {
            const video = videoRef.current;
            if (video && !video.paused) {
                const currentTime = Math.floor(video.currentTime);
                axios.post(
                    `${API_BASE}/${videoId}/progress?userIdentifier=${USER_ID}&positionSeconds=${currentTime}`
                ).catch((err) => console.error('Failed to save progress', err));
            }
        }, 5000);

        // Also save immediately when the user pauses
        const video = videoRef.current;
        const handlePause = () => {
            if (video) {
                const currentTime = Math.floor(video.currentTime);
                axios.post(
                    `${API_BASE}/${videoId}/progress?userIdentifier=${USER_ID}&positionSeconds=${currentTime}`
                ).catch((err) => console.error('Failed to save progress', err));
            }
        };
        video?.addEventListener('pause', handlePause);

        return () => {
            clearInterval(intervalId);
            video?.removeEventListener('pause', handlePause);
        };
    }, [videoId]);

    if (!videoId) return null;

    return (
        <div className="player-section">
            <h2>Now Playing</h2>
            <video ref={videoRef} controls width="600" src={getStreamUrl(videoId)}>
                Your browser does not support the video tag.
            </video>
        </div>
    );
}

export default VideoPlayer;