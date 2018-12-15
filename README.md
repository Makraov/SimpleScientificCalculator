# SimpleScientificCalculator

Very simple calculator!  
Only need to push a expression string then execute().  

Sample:  
Scientific5Executor calc = new Scientific5Executor("1+1*2+(((((2+5/3%7)*1)-1++----+++---8*7)))");  
calc.execute(); // return double  

Known issues:  
1.precision loss.  
2.not support fractional.  
3.not best performance.  
