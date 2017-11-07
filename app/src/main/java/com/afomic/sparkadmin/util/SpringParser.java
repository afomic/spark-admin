package com.afomic.sparkadmin.util;

import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;

/**
 * Created by afomic on 9/22/17.
 *
 */

public class SpringParser {
    private static final char BOLD_SPAN='*';
    private static final char ITTALIC_SPAN='_';
    private static final char STRIKE_THROUGH_SPAN='-';
    private static final char UNDERLINE_SPAN='$';
    private static final char HEADING_ONE_SPAN='#';


    public SpringParser(){

    }

    public Spannable parseString(String text){
        SpannableStringBuilder span=new SpannableStringBuilder(text);
        setSpan(BOLD_SPAN,span);
        setSpan(ITTALIC_SPAN,span);
        setSpan(STRIKE_THROUGH_SPAN,span);
        setSpan(HEADING_ONE_SPAN,span);
        return span;
    }
    private void setHeadingSpan(SpannableStringBuilder text,int start, int end) {
        text.setSpan(new RelativeSizeSpan(1.5f), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
    private void setBoldSpan(SpannableStringBuilder text,int start, int end) {
        text.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
    private void setItalicsSpan(SpannableStringBuilder text,int start, int end) {
        text.setSpan(new StyleSpan(Typeface.ITALIC), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
    private void setStrikeSpan(SpannableStringBuilder text,int start, int end) {
        text.setSpan(new StrikethroughSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
    private void setUnderlineSpan(SpannableStringBuilder text,int start, int end) {
        text.setSpan(new UnderlineSpan(), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public void setSpan(char markup,SpannableStringBuilder span){
        int start=0;
        while (getCharIndex(markup,span,start)!=-1){
            start=getCharIndex(markup,span,start);
            span.delete(start,start+1);
            int end=getCharIndex(markup,span,start+1);
            Log.e("spark", "setSpan: "+start+" end "+end );
            if(end!=-1){
                span.delete(end,end+1);
                switch (markup){
                    case BOLD_SPAN:
                        setBoldSpan(span,start,end);
                        break;
                    case ITTALIC_SPAN:
                        setItalicsSpan(span,start,end);
                        break;
                    case STRIKE_THROUGH_SPAN:
                        setStrikeSpan(span,start,end);
                        break;
                    case UNDERLINE_SPAN:
                        setUnderlineSpan(span,start,end);
                        break;
                    case HEADING_ONE_SPAN:
                        setHeadingSpan(span,start,end);
                        break;
                }
            }
            start=end+1;
        }
    }
    public int getCharIndex(char pattern,SpannableStringBuilder span,int from){
        int length=span.length();
        if(from==length) return -1;
        for(int i=from;i<length;i++){
            if(span.charAt(i)==pattern){
                return i;
            }
        }
        return -1;
    }
    public void removeChar(int position,SpannableStringBuilder span){
        span.insert(position,"");
    }


}
