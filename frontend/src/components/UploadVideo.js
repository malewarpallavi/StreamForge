import React, { useState } from 'react';
import { uploadVideo } from '../api';

function UploadVideo({ onUploadSuccess }) {
    const [file, setFile] = useState(null);
    const [title, setTitle] = useState('');
    const [description, setDescription] = useState('');
    const [uploading, setUploading] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!file || !title) {
            alert('Please select a file and enter a title');
            return;
        }

        const formData = new FormData();
        formData.append('file', file);
        formData.append('title', title);
        formData.append('description', description);

        setUploading(true);
        try {
            await uploadVideo(formData);
            setFile(null);
            setTitle('');
            setDescription('');
            onUploadSuccess(); // tells parent to refresh the video list
        } catch (err) {
            console.error(err);
            alert('Upload failed');
        } finally {
            setUploading(false);
        }
    };

    return (
        <form onSubmit={handleSubmit} className="upload-form">
            <h2>Upload Video</h2>
            <input
                type="file"
                accept="video/*"
                onChange={(e) => setFile(e.target.files[0])}
            />
            <br />
            <input
                type="text"
                placeholder="Title"
                value={title}
                onChange={(e) => setTitle(e.target.value)}
            />
            <br />
            <textarea
                placeholder="Description"
                value={description}
                onChange={(e) => setDescription(e.target.value)}
            />
            <br />
            <button type="submit" disabled={uploading}>
                {uploading ? 'Uploading...' : 'Upload'}
            </button>
        </form>
    );
}

export default UploadVideo;