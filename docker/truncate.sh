#!/bin/bash

LOG_FILE="$HOME/app/log/app.log"
BACKUP_DIR="$HOME/app/log/backup"
DATE=$(date +%Y%m%d_%H%M%S)

# 백업 디렉터리 생성
mkdir -p "$BACKUP_DIR"

# 로그 파일이 존재하고 비어있지 않은 경우에만 처리
if [ -f "$LOG_FILE" ] && [ -s "$LOG_FILE" ]; then
    # 로그 파일 백업 (압축)
    cp "$LOG_FILE" "$BACKUP_DIR/app.log.$DATE"
    gzip "$BACKUP_DIR/app.log.$DATE"

    # 원본 로그 파일 truncate
    truncate -s 0 "$LOG_FILE"

    echo "$(date): Log file truncated and backed up to app.log.$DATE.gz"
else
    echo "$(date): Log file does not exist or is already empty"
fi

# 30일 이상 된 백업 파일 삭제
find "$BACKUP_DIR" -name "app.log.*.gz" -mtime +30 -delete