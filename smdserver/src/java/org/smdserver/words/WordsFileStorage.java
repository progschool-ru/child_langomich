package org.smdserver.words;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class WordsFileStorage extends WordsStorage
{
	private String realPath;
	private long  lastModified;

	public WordsFileStorage (String realPath)
	{
		super();
		this.realPath = realPath;
	}

        @Override
        public void setUserWords (String userId, List<Language> languages)
	{
                super.setUserWords(userId, languages);
                try
		{
			SaveLanguages(userId, languages);
		}
		catch(Exception e){}
	}

        @Override
	public void addUserWords (String userId, List<Language> languages)
	{
                super.addUserWords(userId, languages);
                try
		{
			SaveLanguages(userId, getLanguages(userId));
		}
		catch(Exception e){}
	}

        @Override
	protected void checkUpdated(String userId)
	{
		File file = new File(realPath+"/"+userId+".dat");
		if(lastModified < file.lastModified())
			readFile(userId);
	}

	private void readFile(String userId)
	{
		try
                {
                        List<Language> languages = new ArrayList<Language>();
			BufferedReader br = new BufferedReader(new FileReader(realPath+"/"+userId+".dat"));
                        String str;
			while((str = br.readLine()) != null)
				languages.add(new Language(str));
                        super.setUserWords(userId, languages);
			File file = new File(realPath+"/"+userId+".dat");
			lastModified = file.lastModified();
		}
		catch(IOException ioe){}

	}

	private void SaveLanguages(String userId, List<Language> languages)  throws IOException
	{
		BufferedWriter bw = new BufferedWriter(new FileWriter(realPath+"/"+userId+".dat"));
                for(int i = 0;i<languages.size();i++)
                {
                    bw.write(languages.get(i).getName()+"@@@");
                    for(int j = 0;j<languages.get(i).getWords().size(); j++)
                    {
                        bw.write(languages.get(i).getWords().get(j).getOriginal()+",");
                        bw.write(languages.get(i).getWords().get(j).getTranslation()+",");
                        bw.write(languages.get(i).getWords().get(j).getRating()+",");
                        bw.write(s.format(languages.get(i).getWords().get(j).getModified())+"     ");
                    }
                    bw.write("\n");
                }
                bw.close();
	}
}
