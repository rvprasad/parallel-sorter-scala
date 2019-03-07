/*
 * Copyright (c) 2018, Venkatesh-Prasad Ranganath
 *
 * BSD 3-clause License
 *
 * Author: Venkatesh-Prasad Ranganath (rvprasad)
 *
 */

package sorter.parallel

import java.io._
import java.nio.ByteBuffer

import scala.collection.mutable
import scala.io.Source
import scala.util.{Failure, Sorting, Success, Try}

private object Sorter {
  val LONG_IN_BYTES = 8
  val NUM_OF_LONGS = 4096
}

class Sorter(val parallelism: Int, val numOfInts: Int, val tempDirName: String) {
  private val tempDir = new File(tempDirName)

  def sort(inFile: String, outFile: String): Try[Unit] = {
    try {
      val sortedChunkFiles = Source.fromFile(inFile)
        .getLines.map(_.toLong)
        .grouped(numOfInts).withPartial(true).map(_.toArray)
        .grouped(parallelism).withPartial(true).map { chunks =>
          chunks.par.foreach { Sorting.quickSort(_)(Ordering.Long) }
          val tmp1 = combineSortedChunksIntoIterator(chunks.map(_.iterator))
          val file = java.io.File.createTempFile("sort", "chunk", tempDir)
          writeLongsInto(tmp1, file)
          file
        }.toSeq
      val ret = combineSortedChunksIntoFile(sortedChunkFiles.iterator, outFile)
      sortedChunkFiles.foreach(_.delete)
      ret
    } catch {
      case e: Exception => e.printStackTrace() ; Failure(e)
    }
  }

  private def writeLongsInto(tmp1: Iterator[Long], file: File): Unit = {
    val fs = new FileOutputStream(file)
    val fc = fs.getChannel
    val bb = ByteBuffer.allocate(Sorter.NUM_OF_LONGS * Sorter.LONG_IN_BYTES)
    try
      tmp1.grouped(Sorter.NUM_OF_LONGS).foreach { g =>
        bb.clear
        g.foreach(bb.putLong)
        bb.flip
        fc.write(bb)
      }
    finally
      fs.close()
  }

  private def combineSortedChunksIntoIterator(intChunks: Seq[Iterator[Long]]): Iterator[Long] = {
    val tmp1 = intChunks.filter(_.hasNext)
    if (tmp1.length == 1)
      return tmp1.head

    val heap = new mutable.PriorityQueue[(Long, Iterator[Long])]()(Ordering.by(-_._1))
    heap ++= tmp1.map { a => (a.next, a) }

    new Iterator[Long] {
      override def hasNext: Boolean = heap.nonEmpty

      override def next: Long = {
        val (elem, chunk) = heap.dequeue
        if (chunk.hasNext)
          heap += ((chunk.next, chunk))
        elem
      }
    }
  }

  private def combineSortedChunksIntoFile(sortedChunkFiles: Iterator[File], outFile: String): Try[Unit] = {
    val sink = new PrintWriter(new File(outFile))
    val sources = sortedChunkFiles.map(new FileInputStream(_))
    try {
      val intChunks = sources.map(readLongsFrom)
      combineSortedChunksIntoIterator(intChunks.toSeq).foreach(sink.println)
      Success(())
    } finally {
      sources.foreach(_.close())
      sink.close()
    }
  }

  private def readLongsFrom(fs: FileInputStream): Iterator[Long] = {
    val fc = fs.getChannel
    val bb = ByteBuffer.allocate(Sorter.NUM_OF_LONGS * Sorter.LONG_IN_BYTES)
    new Iterator[Long] {
      bb.limit(0)

      override def hasNext: Boolean = bb.hasRemaining || fs.available > 0

      override def next: Long = {
        if (!bb.hasRemaining) {
          bb.clear
          fc.read(bb)
          bb.flip
        }
        bb.getLong
      }
    }
  }
}

