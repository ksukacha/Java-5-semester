Program comments;
  Var a,b:Integer; //comment
  {kkkfkk}
  // тут 'строчный комментарий
  Var s1,s2,s3: String;(**comme*nt*)
  Begin
    s1:='hello' + 'world'; //comment
    s2:='hello'; {comment}
    s3:='hello';
    Writeln('Some text  {//not comment'); // single-line comment //again
	a:=0;
	{kjkkjkjkj}
	s1:='Some ''s//tring';
	Write('//another{str')(*comme{nt}*);
	for a:=1 to 10000 do
	  begin
	    Writeln (a, 'bb//str');
	  end;
  End.