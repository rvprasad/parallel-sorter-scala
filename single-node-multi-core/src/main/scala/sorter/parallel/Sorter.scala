/*
 * Copyright (c) 2018, Venkatesh-Prasad Ranganath
 *
 * BSD 3-clause License
 *
 * Author: Venkatesh-Prasad Ranganath (rvprasad)
 *
 */

package sorter.parallel

import java.io.{File, PrintWriter}
import scala.collection.mutable
import scala.io.Source
import scala.util.{Failure, Sorting, Success, Try}

class Sorter(val parallelism: Int, val numOfInts: Int,
             val tempDirName: String) {
  private val tempDir = new File(tempDirName)

  def sort(inFile: String, outFile: String): Try[Unit] = {
    try {
      implicit val ordering = Ordering.by { x:Long => x }
      val sortedChunkFiles = Source.fromFile(inFile)
        .getLines.map(_.toLong)
        .grouped(numOfInts).withPartial(true).map(_.toArray)
        .grouped(parallelism).withPartial(true).map { chunks =>
          chunks.par.foreach { Sorting.quickSort(_)(ordering) }
          val tmp1 = combineSortedChunksIntoIterator(chunks.map(_.iterator))
          val file = java.io.File.createTempFile("sort", "chunk", tempDir)
          file.deleteOnExit()
          val writer = new PrintWriter(file)
          try
            tmp1.map(_.toString).foreach(writer.println)
          finally
            writer.close()
          file
        }
      val ret = combineSortedChunksIntoFile(sortedChunkFiles, outFile)
      sortedChunkFiles.foreach(_.delete)
      ret
    } catch {
      case e: Exception => e.printStackTrace() ; Failure(e)
    }
  }

  private def combineSortedChunksIntoIterator(seqOfStreams: Seq[Iterator[Long]]): Iterator[Long] = {
    val tmp1 = seqOfStreams.filter(_.hasNext)
    if (tmp1.length == 1)
      return tmp1.head

    val heap = new mutable.PriorityQueue[(Long, Iterator[Long])]()(Ordering.by(-_._1))
    heap ++= tmp1.map { a => (a.next, a) }

    return new Iterator[Long] {
      override def hasNext = heap.nonEmpty

      override def next = {
        val (elem, chunk) = heap.dequeue()
        if (chunk.hasNext)
          heap += ((chunk.next, chunk))
        elem
      }
    }
  }

  private def combineSortedChunksIntoFile(sortedChunkFiles: Iterator[File],
                                          outFile: String): Try[Unit] = {
    val sink = new PrintWriter(new File(outFile))
    try {
      val sources = sortedChunkFiles.map(Source.fromFile).toSeq
      try {
        val seqOfLines = sources.map(_.getLines.map(_.toLong))
        combineSortedChunksIntoIterator(seqOfLines).foreach(sink.println)
        Success(())
      } finally
        sources.foreach(_.close)
    } finally
      sink.close()
  }
}

