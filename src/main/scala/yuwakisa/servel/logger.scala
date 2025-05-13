package yuwakisa.servel

import org.slf4j.{Logger as SLF4JLogger, LoggerFactory}

/**
 * Extension methods for getting loggers
 */
extension (clazz: Class[?])
  def getLogger: SLF4JLogger = LoggerFactory.getLogger(clazz)

extension (obj: Any)
  def getLogger: SLF4JLogger = LoggerFactory.getLogger(obj.getClass) 
  
extension (name: String)
  def getLogger: SLF4JLogger = LoggerFactory.getLogger(name) 