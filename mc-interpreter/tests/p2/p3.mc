size 40 40
begin
	cat jeff 30 25 west ;
	mouse fred 0 0 east ;
	hole 25 5 ;
	move jeff 15 ;
	move fred 5 ;
	repeat 3
		clockwise jeff ;
	end ;
	repeat 2
		move fred 5 ;
		move jeff 10 ;
	end ;
	mouse susan 5 5 north ;
	clockwise jeff ;
	move susan 10 ;
	move jeff 10 ;
	move fred 10 ;
	repeat 3
		clockwise fred ;
	end ;
	move fred 5 ;
	clockwise susan ;
	clockwise jeff ;
	repeat 2
		move susan 10 ;
		move jeff 5 ;
	end ;
	clockwise jeff ;
	move jeff 10 ;
	clockwise susan ;
	move susan 10 ;
halt