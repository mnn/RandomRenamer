Random Renamer
==============
It is a console application with only one task - rename all files in a current working directory (excluding itself) to a random name.
A new name is generated as a number 0-999999.
In the default settings the application retains file extensions.


Compilation
-----------
[SBT](http://www.scala-sbt.org/)


Command line arguments
----------------------
```
Random Renamer 0.2
 Created by monnef
Usage: randomrenamer [options]

  -e | --drop-extensions
        drops file extensions
  -p | --disable-parallel
        disables parallel renaming
  -m <value> | --max-int-value <value>
        maximum integer value
  -v | --verbose
        more verbose output
  -h | --help
        prints this usage text
```


Donation
--------
If you use this project, please consider a donation of any amount (author is a not-wealthy student).

[![PayPal](https://www.paypalobjects.com/en_US/i/btn/btn_donate_SM.gif)](https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=U6PGB7P24WWSU&lc=CZ&item_name=RandomRenamer&item_number=RR&currency_code=USD&bn=PP%2dDonationsBF%3abtn_donate_LG%2egif%3aNonHosted)


License
-------
GNU General Public License 3 (GPL3)  
For more details read `LICENSE.txt`.
