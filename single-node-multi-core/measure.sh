#!/bin/bash

FOLDER=$1
TEMP_DIR=$FOLDER/temp

factors=([1]=104 [2]=64 [4]=48 [8]=40)
for file in `find $FOLDER/*txt -size -10G -name '*txt' | xargs ls -rS` ; do 
  for heap in 200 400 800 ; do 
    for workers in 1 2 4 8 ; do
      let ints_per_worker=$heap*1024*1024/$workers/${factors[$workers]}
      echo "File: $file, Heap: ${heap}m, Workers: $workers, Ints Per Worker: $ints_per_worker"
      #echo java -Xmx${heap}m \
      #  -jar target/scala-2.12/snmc-assembly-1.0.jar \
      # -i $ints_per_worker -w $workers -f $file -t $TEMP_DIR
      /usr/bin/time -f "%e" java -Xmx${heap}m \
        -jar target/scala-2.12/snmc-assembly-1.0.jar \
        -i $ints_per_worker -w $workers -f $file -t $TEMP_DIR
    done
  done
done 
