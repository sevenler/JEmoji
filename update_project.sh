#/bin/bash

android update project --name jemoji  --target android-20 --path ./

android update project --name jemoji  --target android-20 --path  thread_libs/umeng
cd thread_libs/umeng
ant clean
ant release

android update project --name jemoji  --target android-20 --path  thread_libs/viewpager_indicator
cd thread_libs/viewpager_indicator
ant clean
ant release

android update project --name jemoji  --target android-20 --path  thread_libs/slidingup
cd thread_libs/slidingup
ant clean
ant release