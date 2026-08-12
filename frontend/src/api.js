import axios from 'axios';

const API_BASE = 'http://localhost:8080/api/videos';

export const uploadVideo = (formData) => {
    return axios.post(`${API_BASE}/upload`, formData, {
        headers: { 'Content-Type': 'multipart/form-data' },
    });
};

export const getVideos = (page = 0, size = 10) => {
    return axios.get(`${API_BASE}?page=${page}&size=${size}`);
};

export const getStreamUrl = (id) => {
    return `${API_BASE}/${id}/stream`;
};