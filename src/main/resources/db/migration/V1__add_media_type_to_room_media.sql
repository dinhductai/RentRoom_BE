-- Add media_type column to room_media table
ALTER TABLE room_media 
ADD COLUMN media_type VARCHAR(20) DEFAULT 'IMAGE';

-- Update existing records to have IMAGE as media_type
UPDATE room_media 
SET media_type = 'IMAGE' 
WHERE media_type IS NULL;

