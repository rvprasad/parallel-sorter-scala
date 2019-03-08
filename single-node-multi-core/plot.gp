set terminal png size 1280, 720 fontscale 1.2
set datafile separator ","
set style fill pattern
set style histogram clustered title offset 0, 1 
set style data histograms 
set style fill solid 
set ylabel font ",14"
set xlabel font ",14"
set grid y
set ylabel "Wall clock time (seconds)" 
set xlabel "Number of integers to sort | Heap size" offset 0, -2, 0
set key on left autotitles columnhead 

set output "rasppi.png"
plot newhistogram "5M Ints" lt 1, \
       "rasppi.csv" index 0 u 3:xtic(2), \
       "" index 0 u 4:xtic(2), \
       "" index 0 u 5:xtic(2), \
     newhistogram "10M Ints" lt 1, \
       "rasppi.csv" index 1 u 3:xtic(2) notitle, \
       "" index 1 u 4 notitle, \
       "" index 1 u 5 notitle, \
     newhistogram "50M Ints" lt 1, \
       "rasppi.csv" index 2 u 3:xtic(2) notitle, \
       "" index 2 u 4 notitle, \
       "" index 2 u 5 notitle, \
     newhistogram "100M Ints" lt 1, \
       "rasppi.csv" index 3 u 3:xtic(2) notitle, \
       "" index 3 u 4 notitle, \
       "" index 3 u 5 notitle


set output "desktop.png"
plot newhistogram "5M Ints" lt 1, \
       "desktop.csv" index 0 u 3:xtic(2), \
       "" index 0 u 4:xtic(2), \
       "" index 0 u 5:xtic(2), \
       "" index 0 u 6:xtic(2), \
     newhistogram "10M Ints" lt 1, \
       "desktop.csv" index 1 u 3:xtic(2) notitle, \
       "" index 1 u 4 notitle, \
       "" index 1 u 5 notitle, \
       "" index 1 u 6 notitle, \
     newhistogram "50M Ints" lt 1, \
       "desktop.csv" index 2 u 3:xtic(2) notitle, \
       "" index 2 u 4 notitle, \
       "" index 2 u 5 notitle, \
       "" index 2 u 6 notitle, \
     newhistogram "100M Ints" lt 1, \
       "desktop.csv" index 3 u 3:xtic(2) notitle, \
       "" index 3 u 4 notitle, \
       "" index 3 u 5 notitle, \
       "" index 3 u 6 notitle
