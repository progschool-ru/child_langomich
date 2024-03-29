package org.smdme;

import java.util.Date;

public class Settings extends Records
{
        public final int NUMBER_OF_WORDS = 1;
        public final int LANGUAGE = 2;
        public final int LAST_TIMING = 3;
        public final int NUMBER_OF_TIMING = 4;
        public final int LOGIN = 5;
        public final int PASSWORD = 6;
        public final int URL = 7;
        public final int TEXT = 8;
		public final int LAST_SERVER_TIMING = 9;

        public final String NAME = "Settings";

        Settings()
        {
            recordStoreInit(NAME, null, null);

            if(getRecord(NUMBER_OF_WORDS, SINGLE_RECORD)==null)
                addRecord("1");
            if(getRecord(LANGUAGE, SINGLE_RECORD) == null)
                addRecord("null");
            if(getRecord(LAST_TIMING, SINGLE_RECORD) == null)
                addRecord(Long.toString(new Date().getTime()));
            if(getRecord(NUMBER_OF_TIMING, SINGLE_RECORD) == null)
                addRecord("0");
            if(getRecord(LOGIN, SINGLE_RECORD) == null)
                addRecord("kkauut");//addRecord("null");
				//addRecord("chivorotkiv");
            if(getRecord(PASSWORD, SINGLE_RECORD) == null)
                addRecord("665577");//addRecord("null");
            if(getRecord(URL, SINGLE_RECORD) == null)
                addRecord("lang.omich.net");// TODO: (3.low) хорошо бы вынести эту настройку в какой-нибудь config.properties файл.
				//addRecord("localhost:8080");
            if(getRecord(TEXT, SINGLE_RECORD) == null)
                addRecord("null");
            if(getRecord(LAST_SERVER_TIMING, SINGLE_RECORD) == null)
                addRecord("-1");
        }
        public void setNumberOfWords(int numberOfWords)
        {
            setRecord(Integer.toString(numberOfWords), NUMBER_OF_WORDS);
        }
        public int getNumberOfWords()
        {
            String record = getRecord(NUMBER_OF_WORDS, SINGLE_RECORD);
            if(record!= null)
                return Integer.parseInt(record);
            return 1;
        }
        public void setLastServerTiming(long timing)
        {
                setRecord(Long.toString(timing), LAST_SERVER_TIMING);
        }
        public long getLastServerTiming()
        {
                String record = getRecord(LAST_SERVER_TIMING, SINGLE_RECORD);
                if(record != null && !record.equals("null"))
                {
                        return Long.parseLong(record);
                }
                return -1;
        }
        public void setLanguage(String language)
        {
            setRecord(language, LANGUAGE);
        }
        public String getLanguage()
        {
            String record = getRecord(LANGUAGE, SINGLE_RECORD);
            if(record != null)
                return record;
            return "1";
        }
        public void setLastTiming(long lastTiming)
        {
            setRecord(Long.toString(lastTiming), LAST_TIMING);
        }
        public long getLastTiming()
        {
            String record = getRecord(LAST_TIMING, SINGLE_RECORD);
            if(record != null)
                return Long.parseLong(record);
            return new Date().getTime();
        }
        public void setNumberOfTiming(int numberOfTiming)
        {
            setRecord(Integer.toString(numberOfTiming), NUMBER_OF_TIMING);
        }
        public int getNumberOfTiming()
        {
            String record = getRecord(NUMBER_OF_TIMING, SINGLE_RECORD);
            if(record != null)
                return Integer.parseInt(record);
            return 0;
        }
        public void setLogin(String login)
        {
            setRecord(login, LOGIN);
        }
        public String getLogin()
        {
            String record = getRecord(LOGIN, SINGLE_RECORD);
            if(!record.equals("null"))
                return record;
            return null;
        }
        public void setPassword(String password)
        {
            setRecord(password, PASSWORD);
        }
        public String getPassword()
        {
            String record = getRecord(PASSWORD, SINGLE_RECORD);
            if(!record.equals("null"))
                return record;
            return null;
        }
        public void setURL(String url)
        {
            setRecord(url, URL);
        }
        public String getURL()
        {
            String record = getRecord(URL, SINGLE_RECORD);
            if(!record.equals("null"))
                return record;
            return null;
        }
        public void setText(String text)
        {
            setRecord(text, TEXT);
        }
        public String getText()
        {
            String record = getRecord(TEXT, SINGLE_RECORD);
            if(!record.equals("null"))
                return record;
            return null;
        }
}