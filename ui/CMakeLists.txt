cmake_minimum_required(VERSION 3.4.1)

if (CMAKE_COMPILER_IS_GNUCXX)
    set(CMAKE_CXX_FLAGS "-std=c++11 ${CMAKE_CXX_FLAGS}")
    message(STATUS "optional:-std=c++11")
endif (CMAKE_COMPILER_IS_GNUCXX)

set(libjpeg_dir ${CMAKE_SOURCE_DIR}/libjpeg)
set(INC_DIR ${libjpeg_dir}/jpeg)
set(libjpeg_lib_dir ${libjpeg_dir}/so)

#添加头文件目录
include_directories(${INC_DIR})
include_directories(./src/main/cpp/include)


#指定源文件的目录
set(SOURCE_FILES src/main/cpp/compress-lib.cpp)
aux_source_directory(./src/main/cpp/file SOURCE_FILES)
aux_source_directory(./src/main/cpp/common SOURCE_FILES)
add_library(compress-lib
        SHARED
        ${SOURCE_FILES})

find_library(graphics jnigraphics)

add_library(libjpeg SHARED IMPORTED)
set_target_properties(libjpeg
        PROPERTIES IMPORTED_LOCATION
        ${libjpeg_lib_dir}/${ANDROID_ABI}/libjpeg.so)

find_library(log-lib
        log)

target_link_libraries(compress-lib
        libjpeg
        ${log-lib}
        ${graphics})