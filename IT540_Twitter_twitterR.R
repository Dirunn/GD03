library(httpuv)
library(lubridate, warn.conflicts = FALSE)
library(tidyverse)
library(twitteR)

CONSUMER_KEY <- "8cPoRHx47ckSDdjEttoO2jEw5"
CONSUMER_SECRET <- "W153Jnm7djAwUZzQxUnbOPkVBt8nnxmVrYpKW46Oeahc5hmZeE"
atoken="3150545869-KbTzrNoWue2O4LLpHxQXVe5zIvxEWNbmfQp9JUV"
asecret="qH84W1BvMaWodNknOWG6FPlPIienkxIU88rb3l4ht0cyv"

setup_twitter_oauth(consumer_key = CONSUMER_KEY, consumer_secret = CONSUMER_SECRET, 
                    access_token= atoken, access_secret= asecret)

# Search Twitter for tweets that contain the string 'squid game'
my_tweets = searchTwitter("squid game", n=3500) #think this is capped at 1500

# Next, create a dataframe based around the results
df = do.call("rbind", lapply(my_tweets, as.data.frame))
names(df) #get the columns

# Check the dimensions of df (rows by columns)
dim(df)

# View df data frame
View(df)

# Write df data frame to current working directory as .csv
write.csv(df, 'tweets1.csv')

# Write my_tweets_dates data frame to current working directory as .csv
write.csv(my_tweets_dates, 'tweets2.csv')
