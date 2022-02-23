/*    Class: IT-516
 *     Date: 14 01 2022
 *  Student: Aaron Rider
 *
 *  Compile:
 *  javac-algs4 DataCollector.java
 *
 *  Run:
 *  java-algs4 DataCollector text.txt testCities.txt 25
 *  java-algs4 DataCollector final.txt finalCities.txt 25
 *
 */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

import java.awt.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataCollector {
    public static void main(String[] args) throws ParseException, IOException {
       // In in = new In(args[0]);
      //  String input = in.readAll();
        Stopwatch myCounter = new Stopwatch();

        /***************************************************************************************************************
         * CREATE AND POPULATE HASH MAPS
         **************************************************************************************************************/

        String[] listOfMetrics = {
                "username",
                "followerCount",
                "friends",
                "favorites",
                "retweets",
                "source",
                "month",
                "day",
                "year",
                "hour",
                "minute",
                "second",
                "amPm",
              // "country",
               // "state",
                "city",
                "hashtag",
               // "specialCharacters",
                "words",
                "mentions"
        };

        Map<String, Map<String, Integer>> listOfMaps = new HashMap<>();
        //Outer map stores metric name, inner map stores keys and values
        for (String metric : listOfMetrics) //For each metric being tracked
        {
            Map<String, Integer> metricData = new HashMap<>(); //Create a hash map storing the metric data
            listOfMaps.put(metric, metricData); //add the new hash map to the outer hash map
        }

        ArrayList<String[]> tweets = new ArrayList<String[]>();
        ArrayList<String[]> worldCities = new ArrayList<String[]>();

        //import tweets
        String tweetFileName = args[0];
        In tweetFile = new In(tweetFileName);
        tweetFile.readLine();
        while (tweetFile.hasNextLine())
        {
            String line = tweetFile.readLine();
            String[] tweetData = line.split("\t");
            tweets.add(tweetData);
        }
        //import world cities
        //https://download.geonames.org/export/dump/
        String worldCitiesFileName = args[1];
        In worldCitiesFile = new In(worldCitiesFileName);
        while (worldCitiesFile.hasNextLine())
        {
            String line = worldCitiesFile.readLine();
            String[] worldCityEntry = line.split("\t");
            worldCities.add(worldCityEntry);
        }

        int lineCounter = 1;
        for (String[] tweetData : tweets)
        {
            if (lineCounter > Integer.parseInt(args[2]))
            {
                break;
            }
           // StdOut.println("Diag Line: " + lineCounter);
            String timeStamp = tweetData[0];
            String tweetText = tweetData[1];
            ArrayList<String> tweetWords = new ArrayList<>();
            String username = tweetData[2];

            //Process mentions

            //https://kagan.mactane.org/blog/2009/09/22/what-characters-are-allowed-in-twitter-usernames/comment-page-1/

            ArrayList<String> mentions = new ArrayList<>();

            Pattern mentionPattern = Pattern.compile("\\@([a-zA-Z0-9_]{1,15})");
            Matcher mentionMatches = mentionPattern.matcher(tweetText.toLowerCase());
            if (mentionMatches.find())
            {
                //StdOut.println(mentionMatches.group(1));
                if (mentionMatches.group(1).length() > 0)
                {
                    mentions.add(mentionMatches.group(1));
                }
            }


            //Process hashtags
            //String[] hashtags = tweetData[3].split("\\|");
            String hashtagsUnprocessed = tweetData[3].toLowerCase();
            ArrayList<String> hashtags = new ArrayList<>();


            //([a-zA-Z])\w+
            //['stickers'; 'tshirts'; 'pillows'; 'mugs'; 'masks'; 'redlightgre enlight'; 'squidgame']
            Pattern hashTagPattern = Pattern.compile("([a-zA-Z0-9]\\w+)");
            Matcher hashTagMatches = hashTagPattern.matcher(hashtagsUnprocessed);
            if (hashTagMatches.find())
            {
                if (hashTagMatches.group(1).length() > 0)
                {
                    //StdOut.println("Match " + hashTagMatches.group(1));
                    hashtags.add(hashTagMatches.group(1));
                }
            }


            String followerCount = tweetData[4];
            String location = tweetData[5];
            String city = "";
            String country = "";

            String description = tweetData[6];
            int friends = Integer.parseInt(tweetData[8]);
            int favorites = Integer.parseInt(tweetData[9]);
            String retweets = tweetData[10];
            String source = tweetData[11];

            //StdOut.println("Description: " + description);
            //StdOut.println(timeStamp);

            /*******************************************************************************************************
             * TRY TO PARSE LOCATION
             ******************************************************************************************************/

            for (String[] cityEntry : worldCities)
            {
                String cityName = cityEntry[1];
                String cityNameAscii = cityEntry[2];
                String[] cityNamesAlternate = cityEntry[3].split(",");
                String countryCode = cityEntry[1];
                String cityPopulation = cityEntry[1];
                String timeZone = cityEntry[1];

                Pattern cityNamePattern = Pattern.compile("(" + cityName + ")" + "|" + "(" + cityNameAscii + ")");
                Matcher cityNameMatches = cityNamePattern.matcher(location);
                if (cityNameMatches.find())
                {
                    city = cityNameAscii;
                    country = countryCode;
                    //StdOut.println("Matched city: " + location);
                    break;
                }

            }


            /*******************************************************************************************************
             * PARSE TWEET TEXT
             ******************************************************************************************************/
            Pattern wordPattern = Pattern.compile("^.*?([a-zA-Z]+).*$"); //Regex to get just the word, no punctuation

            for (String tweetWordToTest : tweetText.toLowerCase().split("\\s+"))
            {
                Matcher wordMatches = wordPattern.matcher(tweetWordToTest); //The object that processes the regex
                // pattern and
                // input string
                if (wordMatches.find()) //Perform the regex operation, if it matches
                {
                    if (wordMatches.group(1).length() > 0) //If a word in the capture group exists
                    {
                        tweetWords.add(wordMatches.group(1));
                    }
                }
            }

            /*******************************************************************************************************
             * PARSE TIME AND DATE
             ******************************************************************************************************/
            Pattern
                    timeStampPattern = Pattern.compile(
                    "([0-9]{4})-([0-9]{2})-([0-9]{2}) ([0-9]{2}):([0-9]{2}):([0-9]{2})\\+([0-9]{2}):([0-9]{2})"
            );
            Matcher matches = timeStampPattern.matcher(timeStamp); //The object that processes the regex
            // pattern and
            // input string
            if (matches.find()) //Perform the regex operation, if it matches
            {


                int year = Integer.parseInt(matches.group(1));
                int month = Integer.parseInt(matches.group(2));
                int day = Integer.parseInt(matches.group(3));


                int hour = Integer.parseInt(matches.group(4));
                int minute = Integer.parseInt(matches.group(5));
                int second = Integer.parseInt(matches.group(6));
                //String amPm = matches.group(7);

                //Get value of AM or PM
                //https://stackoverflow.com/questions/6531632/conversion-from-12-hours-time-to-24-hours-time-in-java
                SimpleDateFormat parseFormat = new SimpleDateFormat("HH");
                SimpleDateFormat displayFormat = new SimpleDateFormat("a");
                Date date = parseFormat.parse(Integer.toString(hour));
                String amPm = displayFormat.format(date);


                String monthString;
                switch (month)
                {
                    case 1:
                        monthString = "January";
                        break;
                    case 2:
                        monthString = "February";
                        break;
                    case 3:
                        monthString = "March";
                        break;
                    case 4:
                        monthString = "April";
                        break;
                    case 5:
                        monthString = "May";
                        break;
                    case 6:
                        monthString = "June";
                        break;
                    case 7:
                        monthString = "July";
                        break;
                    case 8:
                        monthString = "August";
                        break;
                    case 9:
                        monthString = "September";
                        break;
                    case 10:
                        monthString = "October";
                        break;
                    case 11:
                        monthString = "November";
                        break;
                    case 12:
                        monthString = "December";
                        break;
                    default:
                        monthString = "Invalid month";
                        break;
                }

                /*******************************************************************************************************
                 * UPDATE HASH MAPS
                 ******************************************************************************************************/
                for (String metric : listOfMetrics)
                {
                    String[] keys;

                    switch (metric)
                    {
                        case "username":
                            keys = new String[]{username};
                            break;
                        case "followerCount":
                            keys = new String[]{followerCount};
                            break;
                        case "friends":
                            keys = new String[]{Integer.toString(friends)};
                            break;
                        case "favorites":
                            keys = new String[]{Integer.toString(favorites)};
                            break;
                        case "retweets":
                            keys = new String[]{retweets};
                            break;
                        case "source":
                            keys = new String[]{source};
                            break;
                        case "hashtag":
                            if (hashtags.size() == 0)
                            {
                                continue;
                            }
                            keys = hashtags.toArray(new String[hashtags.size()]);
                            break;
                        case "month":
                            keys = new String[]{monthString};
                            break;
                        case "day":
                            keys = new String[]{Integer.toString(day)};
                            break;
                        case "year":
                            keys = new String[]{Integer.toString(year)};
                            break;
                        case "hour":
                            keys = new String[]{Integer.toString(hour)};
                            break;
                        case "minute":
                            keys = new String[]{Integer.toString(minute)};
                            break;
                        case "second":
                            keys = new String[]{Integer.toString(second)};
                            break;
                        case "amPm":
                            keys = new String[]{amPm};
                            break;
                        case "words":
                            if (tweetWords.size() == 0)
                            {
                                continue;
                            }
                            keys = tweetWords.toArray(new String[tweetWords.size()]);
                            break;
                        case "mentions":
                            if (mentions.size() == 0)
                            {
                                continue;
                            }

                            keys = mentions.toArray(new String[mentions.size()]);
                            break;
                        case "city":
                            keys = new String[]{city};
                            break;
                        case "country":
                            keys = new String[]{country};
                            break;
                        default: //if metric is not in this list and thus key is undefined
                            continue; //skip and go to the next metric
                    }

                    if (keys[0].length() == 0)
                    {
                        //keys[0] = "null";
                        continue;
                    }
                    for (String key : keys)
                    {
                        if (listOfMaps.get(metric).get(key) != null) //if key already exists
                        {
                            int occurrenceCounter = listOfMaps.get(metric).get(key);
                            occurrenceCounter++;
                            listOfMaps.get(metric).put(key, occurrenceCounter);
                        } else //if key does not exist
                        {
                            listOfMaps.get(metric).put(key, 1);
                        }
                    }
                }

            }
            lineCounter++;
        }

        /*******************************************************************************************************
         * STATISTICS OUTPUT
         ******************************************************************************************************/

        for (String metric : listOfMetrics)
        {
            StdOut.println("========================");
            StdOut.println("Metric:" + metric);
            StdOut.println("========================");
            StdOut.println("        Number of unique "+ metric+": "+ listOfMaps.get(metric).size());
            StdOut.print("          Most Frequent: ");
            Integer timesUsed = 0;
            String mostUsed = "";
            for(String key : listOfMaps.get(metric).keySet())
            {
                if(listOfMaps.get(metric).get(key) > timesUsed){
                    mostUsed = key;
                    timesUsed = listOfMaps.get(metric).get(key);
                }
            }
            StdOut.println(mostUsed);
        }
        StdOut.println("========================");
        StdOut.println("Total time elapsed: " + myCounter.elapsedTime());

        Visual(tweetFileName, "squidgame", "minecraft", "USA", "netflix");


        }
    /*******************************************************************************************************
     * Visuals
     ******************************************************************************************************/
        public static void Visual(String file, String one, String two, String three, String four){
            In in = new In(file);
            String input = in.readAll();
            String [] words = {one, two, three, four};

            Pattern lineArg1 = Pattern.compile(one, Pattern.CASE_INSENSITIVE);
            Pattern lineArg2 = Pattern.compile(two, Pattern.CASE_INSENSITIVE);
            Pattern lineArg3 = Pattern.compile(three, Pattern.CASE_INSENSITIVE);
            Pattern lineArg4 = Pattern.compile(four, Pattern.CASE_INSENSITIVE);

            int countArg1 = 0;
            int countArg2 = 0;
            int countArg3 = 0;
            int countArg4 = 0;

            Matcher matcherArg1 = lineArg1.matcher(input);
            Matcher matcherArg2 = lineArg2.matcher(input);
            Matcher matcherArg3 = lineArg3.matcher(input);
            Matcher matcherArg4 = lineArg4.matcher(input);

            while(matcherArg1.find()){
                countArg1++;
            }
            while(matcherArg2.find()){
                countArg2++;
            }
            while(matcherArg3.find()){
                countArg3++;
            }
            while(matcherArg4.find()){
                countArg4++;
            }
            System.out.println(">Number of tweets mentioning \"" + one + "\" = " + countArg1);
            System.out.println(">Number of tweets mentioning \"" + two + "\" = " + countArg2);
            System.out.println(">Number of tweets mentioning \"" + three + "\" = " + countArg3);
            System.out.println(">Number of tweets from location \"" + four + "\" = " + countArg4);

            Integer[] myArgs = {countArg1, countArg2, countArg3, countArg4};
            double myScalingFactor = 0.9;                         // The actual size of the bars being printed
            double myIncrement = (double) 1 / myArgs.length / 2;
            int myMax = 0;

            for (int i = 0; i < myArgs.length; i++) {
                if (myArgs[i] > myMax) {
                    myMax = myArgs[i] ;
                }
            }

            StdDraw.setCanvasSize(1000, 500);

            StdDraw.setPenColor(Color.RED);
            StdDraw.text(0.5, 0.85, "Frequency");
            for (int i = 1; i <= myArgs.length; i++) {
                double mySize = (double) myArgs[i - 1];
                mySize = mySize / myMax;
                StdDraw.setPenColor(Color.RED);
                StdDraw.filledRectangle((i * 2 * myIncrement) - myIncrement, 0.5 + (myIncrement * mySize * myScalingFactor), myIncrement * myScalingFactor, myIncrement * myScalingFactor * mySize);
                StdDraw.setPenColor(Color.BLACK);
                StdDraw.text((i * 2 * myIncrement) - myIncrement, 0.70, String.valueOf(myArgs[i - 1]));
                StdDraw.text((i * 2 * myIncrement) - myIncrement, 0.40, String.valueOf(words[(i + 1) - 2]));
            }
        }


}
