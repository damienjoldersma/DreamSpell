 #!/bin/sh
 for i in *
 do
 j=`echo $i | tr '[A-Z]' '[a-z]'`
 mv $i $j
 done 
