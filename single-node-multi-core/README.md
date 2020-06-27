# Single-node Multi-core External Parallel Sorter

This implementation chunks uf the given numbers, sorts the chunks in parallel, and combines the sorted chunks to provide the final sorted output. 

The implementation relies on the support for stream/iterator-based parallel processing and futures in Scala.

## Requirements
- JDK 8
- Scala 2.12.8
- ScalaCheck 1.14.0
- SBT 1.2.8

We recommend using [SDKMan](https://sdkman.io/) for getting these requirements.

## Build
Execute `sbt assembly`.

## Use
Execute `java -jar target/scala-2.12/snmc-assembly-1.0.jar -w 4 -i 100000 -f numbersToSort.txt -t /tmp`.  If sorting succeeds, then the sorted numbers are written into `numbersToSort.txt.sorted`.

- The argument to `-w` is the number of workers to use for sorting.  This number defaults to 1 if the given number is less than 1.
- The argument to `-i` controls the number of integers given to a worker for sorting.  This number defaults to 1000 if the given number is less than 1000.
- The argument to `-f` is the name of the file containing the integers to be sorted; one per line.
- The argument to `-t` is the name of the folder in which the prgoram can create temporary files can be created.

## Evaluation

An evaluation of this implementation on desktop and Raspberry Pi 3B can be found [here](https://medium.com/@rvprasad/parallel-external-sorting-175c7a9247cb).

## Attribution

Copyright (c) 2019, Venkatesh-Prasad Ranganath

Licensed under BSD 3-clause "New" or "Revised" License (https://choosealicense.com/licenses/bsd-3-clause/)

**Authors:** Venkatesh-Prasad Ranganath
