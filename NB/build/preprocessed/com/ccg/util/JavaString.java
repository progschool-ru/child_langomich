/*
 * This clas from this project: http://www.redali.com/com.ccg/com.ccg.core/index.html
 * Documentation: http://www.redali.com/com.ccg/com.ccg.core/apidocs/com/ccg/util/JavaString.html
 * Source (I haven't found where I can get library): http://www.redali.com/com.ccg/com.ccg.core/cobertura/com.ccg.util.JavaString.html
 * License: http://www.redali.com/com.ccg/com.ccg.core/license.html

BSD

The license text below the dashed line pertains to the "com/ccg" and
"com/include" libraries, documentation, executable binaries, and the
source code as distributed in this package by Paul Blankenbaker
(paul@mekwin.com).

------------------------------------------------------------------------------
This software is copyrighted by Paul Blankenbaker, Redali Consulting,
Inc., Global Atmospherics, Inc., and Unified Technologies, Inc., and
other parties.  The following terms apply to all files associated with
the software unless explicitly disclaimed in individual files.

The authors hereby grant permission to use, copy, modify, distribute,
and license this software and its documentation for any purpose, provided
that existing copyright notices are retained in all copies and that this
notice is included verbatim in any distributions. No written agreement,
license, or royalty fee is required for any of the authorized uses.
Modifications to this software may be copyrighted by their authors
and need not follow the licensing terms described here, provided that
the new terms are clearly indicated on the first page of each file where
they apply.

IN NO EVENT SHALL THE AUTHORS OR DISTRIBUTORS BE LIABLE TO ANY PARTY
FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES
ARISING OUT OF THE USE OF THIS SOFTWARE, ITS DOCUMENTATION, OR ANY
DERIVATIVES THEREOF, EVEN IF THE AUTHORS HAVE BEEN ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.

THE AUTHORS AND DISTRIBUTORS SPECIFICALLY DISCLAIM ANY WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE, AND NON-INFRINGEMENT.  THIS SOFTWARE
IS PROVIDED ON AN "AS IS" BASIS, AND THE AUTHORS AND DISTRIBUTORS HAVE
NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR
MODIFICATIONS.

RESTRICTED RIGHTS: Use, duplication or disclosure by the government
is subject to the restrictions as set forth in subparagraph (c) (1) (ii)
of the Rights in Technical Data and Computer Software Clause as DFARS
252.227-7013 and FAR 52.227-19.

 * 
 */

package com.ccg.util;

public class JavaString {

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

   public static String decode(String from){
     if (from == null) return null;

     JavaString js = new JavaString();
     js.decodeAppend(from.toCharArray());
     return js.toString();
   }

   public void decodeAppend(char[] buf){
     if (buf == null) return;

     int blen = buf.length;

     for (int i = 0; i < blen; i++) decodeAppend(buf[i],i);
   }

   public void decodeAppend(String str){
     if (str == null) return;

     int slen = str.length();

     for (int i = 0; i < slen; i++) decodeAppend(str.charAt(i),i);
   }

   public void decodeAppend(char c, int i){

     if (_Escaped) {                            // character, then join 4 hex digits
       if (_UnicodeCnt > 0) {

         int val = Character.digit(c,16);

         if (val == -1) {

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