CREATE TABLE IF NOT EXISTS rss_feeds (
    id BIGINT PRIMARY KEY,
    author TEXT,
    title TEXT,
    link TEXT,
    publisher TEXT,
    lang TEXT,
    created TIMESTAMP
)
