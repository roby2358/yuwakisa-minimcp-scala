package yuwakisa.servel

extension [A](a: A)
  def tap(f: A => Unit): A =
    f(a)
    a