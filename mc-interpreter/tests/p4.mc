    size 60 60
    begin
      cat charlotte 20 21 east ;
			cat dom 58 58 west ;
			cat charlie 10 40 north ;
      mouse 98A 5 6 north ;
			mouse CCCBBB 12 28 east ;
			mouse rat 30 30 east ;
      hole 5 8 ;
			hole 1 1 ;
			hole 8 8 ;
			repeat 5
				move charlie ;
				move CCCBBB ;
				clockwise dom ;
				clockwise CCCBBB ;
			end ;
			repeat 3
				clockwise rat ;
				move rat 5 ;
				clockwise rat ;
				move rat 3 ;
				clockwise rat ;
			end ;
			repeat 2
				clockwise 98A ;
				move 98A 7 ;
				clockwise 98A ;
				move 98A 2 ;
			end ;
    halt       	 
