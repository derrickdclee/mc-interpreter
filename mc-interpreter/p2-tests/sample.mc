size 40 40
begin
  cat cat1 5 5 north ;
  mouse mouse1 10 10 south ;
  mouse mouse2 12 12 east ;
  hole 2 2 ;
  hole 7 7 ;
  hole 8 2 ;
  hole 1 1 ;
  move cat1 3 ;
  clockwise cat1 ;
  move cat1 ;
  move cat1 ;
  move mouse1 ;
  move mouse2 ;
  clockwise mouse1 ;
  move mouse1 3 ;
  move cat1 2 ;
  clockwise mouse2 ;
  move mouse2 5 ;
  clockwise cat1 ;
  repeat 3
    move cat1 3 ;
  end ;
halt
