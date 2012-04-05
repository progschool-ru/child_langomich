package org.omich.lang;

import java.util.List;

import org.omich.lang.words.Word;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class MyAdapter extends ArrayAdapter<Word> {
        
        private final Activity context;
        private final List<Word> words;
        
        public MyAdapter(Activity context, List<Word> words){
                super(context, R.layout.item, words);
                this.context = context;
                this.words = words;
        }
        
        static class ViewHolder{
                protected TextView originalView;
                protected TextView translateView;
                protected TextView ratingView;
                protected CheckBox selected;
        }
        
        @Override
        public View getView(int position, View contentView, ViewGroup parent){
                        View view = null;
                        
                        LayoutInflater inflater = context.getLayoutInflater();
                        view = inflater.inflate(R.layout.item, null);
                        final ViewHolder viewHolder = new ViewHolder();
                        
                    viewHolder.originalView = (TextView) view.findViewById(R.id.original);
                    viewHolder.translateView = (TextView) view.findViewById(R.id.translate);
                    viewHolder.ratingView = (TextView) view.findViewById(R.id.rating);
                    viewHolder.selected = (CheckBox) view.findViewById(R.id.selected);
                    
                    view.setTag(viewHolder);
                    viewHolder.originalView.setText(words.get(position).getOriginal());
                    viewHolder.translateView.setText(words.get(position).getTranslation());
                    viewHolder.ratingView.setText(Integer.toString(words.get(position).getRating()));
                        return view;
        }

}
