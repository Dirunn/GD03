# GD03
This program takes in thousands of tweets and outputs metrics about them. More specifically this java file reads from two .txt files, one with a list of tweets taken and one with a list of countries along with their country codes. After reading those files the code will output two aspects about various types of metrics. The first being the number of unique elements in that metric and the second the most common element in that matrix. Finally if you want to get the frequency of any specific phrase in the set of tweets you can do that and see that result displayed visually using standard draw. 


Originally wanted to use data source from Kaggle but was informed data must be retrieved (mined) by us

=========================================================================================================================================================================
Tweet Retrieval

Attempted to use Spring Social Twitter for Java but was unable to get it to work, so we used decided to use R & Python

Initial R code, using twitterR package, to retrieve tweets with search term "squid game" was used.

Original R code, using twitterR package, was insufficient as it:
 - would pull the same data as initial pull when ran at different times of the day
 - included 16 fields, of which 5 were always blank
 - search results limited to:
     - ~1300 retrieved tweets per 24 hour period
     - looks back up to six (6) days
     - 23 available functions within twitterR package
 - resulted in obtaining needed total size of tweets
 - was determined the following were removed during pre-procesing:
  -  @ and # symbols
  -  http and https references

Secondary R code, using rtweets package, proved better:
- multiple pulls resulted in different tweet retrieval
- included 90 fields, 13 of which were always blank (a 462.5% increase of potentially accessible data over twitterR package)
- search results limited to:
    - up to 250K retrieved tweets per run
    - looks back up to 14 days (a 133.3% increase of potentially accessible data over twitterR package)
    - 63 available functions within rtweets package (a 173.9% increase of potentially accessible data over twitterR package)
- resulted in obtaining more than needed size of tweets
- was determined the following were retained during pre-processing:
  -  @ and # symbols
  -  http and https references

Tweets were also retrieved using Python code
