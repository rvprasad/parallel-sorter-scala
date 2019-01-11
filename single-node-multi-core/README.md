# Single-node Multi-core External Parallel Sorter

This implementation chunks uf the given numbers, sorts the chunks in parallel, and combines the sorted chunks to provide the final sorted output. 

The implementation relies on the support for stream/iterator-based parallel processing and futures in Scala.

## Requirements
- JDK 8
- Scala 2.12.7
- SBT 1.2.7

Strongly recommend using [SDKMan](https://sdkman.io/) for getting these requirements.

## Build
Execute `sbt assembly`.

## Use
Execute `java -jar target/scala-2.12/snmc-assembly-1.0.jar -w 4 -i 100000 -f numbersToSort.txt`.  If sorting succeeds, then the sorted numbers are written into `numbersToSort.txt.sorted`.

- The argument to `-f` is the name of the file containing the to be sorted integers
  one per line.
- The argument to `-w` is the number of workers to use for sorting.  This number
  defaults to 1 if the given number is less than 1.
- The argument to `-i` controls the number of ints given to a worker for sorting.
  This number defaults to 1000 if the given number is less than 1000.

## Attribution

Copyright (c) 2018, Venkatesh-Prasad Ranganath

Licensed under BSD 3-clause "New" or "Revised" License (https://choosealicense.com/licenses/bsd-3-clause/)

**Authors:** Venkatesh-Prasad Ranganath