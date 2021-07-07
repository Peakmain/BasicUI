//
// Created by admin on 2021/7/7.
//

#include "../include/FileLogger.h"
#include "ui_log.h"
#ifdef __cplusplus
extern "C" {
#endif
/**
 * 默认最小的增长大小
 */
static int DEFAULT_INCREASE_SIZE = getpagesize() * 128;

FileLogger::FileLogger(const char *logPath, int maxFileSize) {
    this->logPath = (char *) malloc(strlen(logPath) + 1);
    memcpy(this->logPath, logPath, strlen(logPath) + 1);

    this->maxFileSize = maxFileSize;
    // 计算每次映射多大数据
    increaseSize = maxFileSize / REMMAP_TIMES;

    if (mmapSize < DEFAULT_INCREASE_SIZE) {
        increaseSize = DEFAULT_INCREASE_SIZE;
    } else if (increaseSize % getpagesize() != 0) {
        // 保证是页的整数倍
        increaseSize = (increaseSize / getpagesize() + 1) * getpagesize();
    }

    // 读取到整个文件的大小
    int fileSize = getLogFileSize();

    if (fileSize <= 0) {
        mmapSize = increaseSize;
        // 打开映射文件
        mmapFile(0, mmapSize);
    } else {
        mmapSize = fileSize;
        mmapFile(0, fileSize);
        if (dataPos >= maxFileSize) {
            // 释放之前映射过的内存
            if (mmapPtr != NULL) {
                munmap(mmapPtr, mmapSize);
            }
        }
    }
}

void FileLogger::mmapFile(int start, int end) {
    // 打开文件
    int logFd = open(logPath, O_RDWR | O_CREAT, S_IRWXU);
    // 给文件新增大小空间
    ftruncate(logFd, mmapSize);
    int size = end - start;
    mmapPtr = (char *) mmap(NULL, size, PROT_READ | PROT_WRITE, MAP_SHARED, logFd, start);
    // 关闭文件
    close(logFd);
}

void FileLogger::writeData(const char *data, int dataLen) {
    if (dataPos + dataLen > mmapSize) {
        if (dataPos + dataLen > maxFileSize) {
            int maxSize = dataPos + dataLen;
            if (maxSize % getpagesize() != 0) {
                maxSize = ((maxSize / getpagesize()) + 1) * getpagesize();
            }
            increaseSize = maxSize - mmapSize;
        }

        // 释放之前映射过的内存
        if (mmapPtr != NULL) {
            munmap(mmapPtr, mmapSize);
        }
        mmapSize += increaseSize;
        mmapFile(0, mmapSize);
    }

    // 把数据写入映射区域 (文件)
    memcpy(mmapPtr + dataPos, data, dataLen);
    dataPos += dataLen;
    // memcpy(mmapPtr + (mmapSize - DEFAULT_POSITION_SIZE - 1), &dataPos, DEFAULT_POSITION_SIZE);
}

int FileLogger::getLogFileSize() {
    FILE *fp = fopen(logPath, "r");
    if (fp == NULL) {
        return 0;
    }

    fseek(fp, 0L, SEEK_END);
    int size = ftell(fp);
    fclose(fp);
    return size;
}

FileLogger::~FileLogger() {
    if (logPath != NULL) {
        free(logPath);
    }

    // 释放映射过的内存
    if (mmapPtr != NULL) {
        munmap(mmapPtr, mmapSize);
    }
}
#ifdef __cplusplus
}


#endif