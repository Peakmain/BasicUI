//
// Created by admin on 2021/7/7.
//

#ifndef BASICUI_FILELOGGER_H
#define BASICUI_FILELOGGER_H

#include <unistd.h>
#include <malloc.h>
#include <string.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <jni.h>
#include <unistd.h>
#include <sys/mman.h>
#include <jni.h>
#include "ui_log.h"

#ifdef __cplusplus
extern "C" {
#endif
#define REMMAP_TIMES 4
class FileLogger {
public:
    /**
     * 当前log日志文件
     */
    char *logPath;

    /**
     * 每次加载的大小
     */
    int increaseSize;
    /**
     * 当前写入数据的位置
     */
    long dataPos = 0;
    /**
     * 当前文件映射的大小
     */
    int mmapSize = 0;
    /**
     * mmap映射的头文件
     */
    char *mmapPtr = NULL;
    /**
     * 文件允许的最大范围
     */
    int maxFileSize;
public:
    FileLogger(const char *logPath, int maxFileSize);

    ~FileLogger();

    void mmapFile(int start, int fileLen);

    void writeData(const char *data, int dataLen);

    int getLogFileSize();
};

#ifdef __cplusplus
}

#endif //BASICUI_FILELOGGER_H
