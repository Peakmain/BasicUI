# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

# 开启cmake一些能力
#ENABLE_INPROCESS Enables in-process mode for a library.
#ENABLE_OUTOFPROCESS Enables in-process mode for a library.
#ENABLE_OUTOFPROCESS_ALL_THREADS Enables all threads unwinding for in-process mode. Ignored if out-process-mode is disabled.
#ENABLE_LIBCORKSCREW Enables "libcorkscrew" unwinder.
#ENABLE_LIBUNWIND Enables "libunwind" unwinder.
#ENABLE_LIBUNWINDSTACK Enables "libunwindstack" unwinder.
#ENABLE_CXXABI Enables "cxxabi" unwinder.
#ENABLE_STACKSCAN Enables "stackscan" unwinder.
set(ENABLE_INPROCESS ON)
set(ENABLE_OUTOFPROCESS ON)
set(ENABLE_LIBCORKSCREW ON)
set(ENABLE_LIBUNWIND ON)
set(ENABLE_LIBUNWINDSTACK ON)
set(ENABLE_CXXABI ON)
set(ENABLE_STACKSCAN ON)

if (${ENABLE_INPROCESS})
    add_definitions(-DENABLE_INPROCESS)
endif ()
if (${ENABLE_OUTOFPROCESS})
    add_definitions(-DENABLE_OUTOFPROCESS)
endif ()

set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -Werror=implicit-function-declaration")
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11 ")

# 设置环境变量，BREAKPAD_ROOT指向xxxx/breakpad/libbreakpad/src/main/cpp/breakpad
set(BREAKPAD_ROOT src/main/cpp/breakpad)

#head file path
include_directories(${BREAKPAD_ROOT}/src ${BREAKPAD_ROOT}/src/common/android/include)

# 指定breakpad 源码文件
file(GLOB BREAKPAD_SOURCES_COMMON
        ${BREAKPAD_ROOT}/src/client/linux/crash_generation/crash_generation_client.cc
        ${BREAKPAD_ROOT}/src/client/linux/dump_writer_common/thread_info.cc
        ${BREAKPAD_ROOT}/src/client/linux/dump_writer_common/ucontext_reader.cc
        ${BREAKPAD_ROOT}/src/client/linux/handler/exception_handler.cc
        ${BREAKPAD_ROOT}/src/client/linux/handler/minidump_descriptor.cc
        ${BREAKPAD_ROOT}/src/client/linux/log/log.cc
        ${BREAKPAD_ROOT}/src/client/linux/microdump_writer/microdump_writer.cc
        ${BREAKPAD_ROOT}/src/client/linux/minidump_writer/linux_dumper.cc
        ${BREAKPAD_ROOT}/src/client/linux/minidump_writer/linux_ptrace_dumper.cc
        ${BREAKPAD_ROOT}/src/client/linux/minidump_writer/minidump_writer.cc
        ${BREAKPAD_ROOT}/src/client/minidump_file_writer.cc
        ${BREAKPAD_ROOT}/src/common/convert_UTF.cc
        ${BREAKPAD_ROOT}/src/common/md5.cc
        ${BREAKPAD_ROOT}/src/common/string_conversion.cc
        ${BREAKPAD_ROOT}/src/common/linux/elfutils.cc
        ${BREAKPAD_ROOT}/src/common/linux/file_id.cc
        ${BREAKPAD_ROOT}/src/common/linux/guid_creator.cc
        ${BREAKPAD_ROOT}/src/common/linux/linux_libc_support.cc
        ${BREAKPAD_ROOT}/src/common/linux/memory_mapped_file.cc
        ${BREAKPAD_ROOT}/src/common/linux/safe_readlink.cc
        )

# 指定汇编文件路径
file(GLOB BREAKPAD_ASM_SOURCE ${BREAKPAD_ROOT}/src/common/linux/breakpad_getcontext.S)

# 指定汇编文件使用的语言
set_source_files_properties(${BREAKPAD_ASM_SOURCE} PROPERTIES LANGUAGE C)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

# 设置最终生成的so文件的名字，文件的类型(shared动态库，static静态库)，参与生成so文件源码文件
add_library( # Sets the name of the library.
        breakpad
        # Sets the library as a shared library.
        SHARED
        # Provides a relative path to your source file(s).
        src/main/cpp/breakpad.cpp
        ${BREAKPAD_SOURCES_COMMON}
        ${BREAKPAD_ASM_SOURCE}
        )

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

# 上面生成so文件需要用到的系统的依赖库
find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

# 把依赖库和breakpad-core相关联
target_link_libraries( # Specifies the target library.
        breakpad

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib}
        )