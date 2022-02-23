library(data.table)
library(rtweet)
library(stringr)
library(textclean)
library(tidyverse)

# Store API keys
api_key <- "8cPoRHx47ckSDdjEttoO2jEw5"
api_secret_key <- "W153Jnm7djAwUZzQxUnbOPkVBt8nnxmVrYpKW46Oeahc5hmZeE"
access_token <- "3150545869-KbTzrNoWue2O4LLpHxQXVe5zIvxEWNbmfQp9JUV"
access_token_secret <- "qH84W1BvMaWodNknOWG6FPlPIienkxIU88rb3l4ht0cyv"

# Authenticate via web browser
token <- create_token(
  app = "IT516 - GD03 Twitter Capture", consumer_key = api_key,
  consumer_secret = api_secret_key, access_token = access_token,
  access_secret = access_token_secret)

# Search for 250,000 tweets containing the string "squid game"
rt <- search_tweets("squid game", n = 250000, retryonratelimit = TRUE)

# Preview tweets data
View(rt)

# Write rt data frame to current working directory as .csv
fwrite(rt, file = "tweets9.csv")



