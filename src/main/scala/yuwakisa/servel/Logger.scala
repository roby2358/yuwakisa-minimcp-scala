package yuwakisa.servel

import org.slf4j.{Logger as SLF4JLogger, LoggerFactory}

/**
 * A simple logging utility for the server
 */
object Logger:
  // Get logger for a class
  def apply[T](clazz: Class[T]): SLF4JLogger = LoggerFactory.getLogger(clazz)

  // Get logger for a name
  def apply(name: String): SLF4JLogger = LoggerFactory.getLogger(name)