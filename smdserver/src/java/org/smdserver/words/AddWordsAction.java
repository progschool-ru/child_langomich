package org.smdserver.words;

import java.text.ParseException;
import org.smdserver.actionssystem.SessionKeys;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smdserver.actionssystem.ActionParams;
import org.smdserver.auth.CheckLoginAction;
import java.util.List;
import java.util.ArrayList;

public class AddWordsAction extends CheckLoginAction
{	
	protected String doAction (HttpServletRequest request)
	{
		String dataString = request.getParameter(ActionParams.DATA);
                
		try
		{ 
			JSONObject json = new JSONObject(JavaString.decode(dataString));
			List<Language> languages = parseJSON(json.getJSONArray(ActionParams.LANGUAGES));
			IWordsStorage storage = getServletContext().getWordsStorage();
			storage.addUserWords(getUser().getUserId(), languages);
			setAnswerParam(ActionParams.SUCCESS, true);


                        languages = storage.getUserWords(getUser().getUserId());
                        ArrayList al = new ArrayList();
                        for(int i = 0; i < languages.size();i++)
                            al.add(languages.get(i));
                        request.getSession().setAttribute(SessionKeys.LANGUAGES, al);
                        return "/main.jsp";
		}
		catch(JSONException e)
		{
			setAnswerParam(ActionParams.SUCCESS, false);
			setAnswerParam(ActionParams.MESSAGE, e.getMessage());
		}
		catch(WordsException e)
		{
			setAnswerParam(ActionParams.SUCCESS, false);
			setAnswerParam(ActionParams.MESSAGE, e.getMessage());
		}
                catch(Exception e)
                {
                        setAnswerParam(ActionParams.SUCCESS, false);
                        setAnswerParam(ActionParams.MESSAGE, e.getMessage());
                }
		return "/addWords.jsp";
	}

	private List<Language> parseJSON (JSONArray json) throws WordsException
	{
		List<Language> languages = new ArrayList<Language>();
		int length = json.length();

		try
		{
			for(int i = 0; i < length; i++)
			{
				JSONObject value = json.getJSONObject(i);
				languages.add(new Language(value));
			}
		}
		catch(JSONException e)
		{
			throw new WordsException(WordsException.JSON_ERROR + "; " + e.getMessage());
		}

		return languages;
	}

	@Override
	protected boolean validateParams (HttpServletRequest request)
	{
		return request.getParameter(ActionParams.DATA) != null;
	}
}


class JavaString {


   public JavaString() {



     _Contents = new StringBuffer();



     clear();



   }

   public void clear() {



     _Escaped = false;                // not processing escape code



     _UnicodeCnt = 0;                // not processing unicode escape



     _UnicodeChar = 0;



     _Contents.setLength(0);        // clear the buffer contents



   }



   public static String decode(String from) throws ParseException {


     if (from == null) return null;







     JavaString js = new JavaString();



     js.decodeAppend(from.toCharArray());



     return js.toString();



   }


   public void decodeAppend(char[] buf) throws ParseException {



     if (buf == null) return;


     int blen = buf.length;


     for (int i = 0; i < blen; i++) decodeAppend(buf[i],i);



   }


   public void decodeAppend(String str) throws ParseException {


     if (str == null) return;


     int slen = str.length();



     for (int i = 0; i < slen; i++) decodeAppend(str.charAt(i),i);


   }

   public void decodeAppend(char c, int i) throws ParseException {



     if (_Escaped) {



                                 // character, then join 4 hex digits



       if (_UnicodeCnt > 0) {



         int val = Character.digit(c,16);



         if (val == -1) {



           throw new ParseException("invalid \"\\uXXXX\" unicode "+



                                    "escape sequence",i);



         }



         _UnicodeChar <<= 4;



         _UnicodeChar += val;



         _UnicodeCnt--;



         if (_UnicodeCnt == 0) {



           _Contents.append(_UnicodeChar);



           _Escaped = false;



         }



       }


       else if (c == 'u') {



         _UnicodeChar = 0;



         _UnicodeCnt = 4;



       }



       else {



         String escChars = "btnfr\"\'\\";



         char[] escSub = {



           '\b', '\t', '\n', '\f', '\r', '\"', '\'', '\\'



         };



         int pos = escChars.indexOf(c);



         if (pos >= 0) _Contents.append(escSub[pos]);



         else {                // otherwise, just quote next character



           _Contents.append(c);


         }


         _Escaped = false;


       }



       return;


     }


     else if (c == '\\') {



       _Escaped = true;



     }


     else _Contents.append(c);



   }


   public static String encode(String from) {

     if (from == null) return null;


     JavaString js = new JavaString();



     js.encodeAppend(from.toCharArray());


     return js.toString();



   }

   public void encodeAppend(char[] buf) {


     if (buf == null) return;





     for (int i = 0; i < buf.length; i++) encodeAppend(buf[i]);



   }


   public void encodeAppend(String str) {


     if (str == null) return;


     int slen = str.length();


     for (int i = 0; i < slen; i++) encodeAppend(str.charAt(i));


   }


   public void encodeAppend(char c) {



     String escChars = "\b\t\n\f\r\"\'\\";



     char[] escSub = {



       'b', 't', 'n', 'f', 'r', '\"', '\'', '\\'



     };



                                 // see if short escape sequence available



     int epos = escChars.indexOf(c);



     if (epos >= 0) {



       _Contents.append('\\');



       _Contents.append(escSub[epos]);



     }



     else if ((c >= ' ') && (c < 127)) {



       _Contents.append(c);



       return;



     }

     else {


       char[] hexChars = {

         '0', '1', '2', '3', '4', '5', '6', '7',


         '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'


       };


       _Contents.append("\\u");


       for (int i = 12; i >= 0; i -= 4) {


         _Contents.append(hexChars[(c >> i) & 0xF]);


       }

     }

   }


   public String toString() {



     return _Contents.toString();


   }


   private StringBuffer _Contents;

   private boolean _Escaped;

   private int _UnicodeCnt;

   private char _UnicodeChar;        // used to build unicode escape character

 }