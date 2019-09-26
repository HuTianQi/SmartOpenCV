cmake_minimum_required(VERSION 3.6)

# Enable C++11
set(CMAKE_CXX_STANDARD 11)
set(CMAKE_CXX_STANDARD_REQUIRED TRUE)

set(target detection_based_tracker)
project(${target} CXX)

set(path_to_opencv "${CMAKE_CURRENT_SOURCE_DIR}/third_part/opencv")

# 添加opencv库
add_library(opencv SHARED IMPORTED)
set_target_properties(opencv
        PROPERTIES IMPORTED_LOCATION
        ${path_to_opencv}/libs/${ANDROID_ABI}/libopencv_java4.so)

# 添加头文件
include_directories(
        ${path_to_opencv}/include
        ${CMAKE_CURRENT_LIST_DIR}
)

# set(ANDROID_OPENCV_COMPONENTS "opencv_java" CACHE STRING "")
message(STATUS "ANDROID_ABI=${ANDROID_ABI}")
# find_package(OpenCV REQUIRED COMPONENTS ${ANDROID_OPENCV_COMPONENTS})

file(GLOB srcs *.cpp *.c)
file(GLOB hdrs *.hpp *.h)

# include_directories()
add_library(${target} SHARED ${srcs} ${hdrs})

find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)

target_link_libraries(${target}
        ${log-lib}
        opencv
        )
