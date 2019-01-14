/*
 * Copyright (c) 2018, Venkatesh-Prasad Ranganath
 *
 * BSD 3-clause License
 *
 * Author: Venkatesh-Prasad Ranganath (rvprasad)
 *
 */

package sorter.parallel

import org.apache.commons.cli.{DefaultParser, HelpFormatter, Options, ParseException, Option => CLIOption}
import scala.util.{Failure, Success}

object Main extends App {
  val options = new Options
  options.addOption(CLIOption.builder("w").longOpt("numOfWorkers")
    .hasArg.required(true).desc("Number of workers to use").build)
  options.addOption(CLIOption.builder("i").longOpt("intsPerWorker")
    .hasArg.required(true).desc("Number of ints per workers").build)
  options.addOption(CLIOption.builder("f").longOpt("filename")
    .hasArg.required(true).desc("Name of file containing ints to sort")
    .build)
  options.addOption(CLIOption.builder("t").longOpt("tempDir")
    .hasArg.required(true).desc("Name of temp directory").build)

  val cmdLine = try {
     new DefaultParser().parse(options, args)
  } catch {
    case _: ParseException => {
      new HelpFormatter().printHelp("java -jar snmc-assembly.jar " +
        "sorter.parallel.Main", options)
      sys.exit(-1)
    }
  }

  val parallelism = math.max(cmdLine.getOptionValue('w').toInt, 1)
  val intsPerWorker = math.max(cmdLine.getOptionValue('i').toInt, 1)
  val filename = cmdLine.getOptionValue('f')
  val tempDirName = cmdLine.getOptionValue('t')
  val output = s"$filename.sorted"
  val master = new Sorter(parallelism, intsPerWorker, tempDirName)
  master.sort(filename, output) match {
    case Success(_) => println(s"Sorted $filename into $output")
    case Failure(i) => println(s"Failed to sort $filename: $i")
  }
}
