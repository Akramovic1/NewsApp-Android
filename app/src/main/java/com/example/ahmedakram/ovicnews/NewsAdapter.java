package com.example.ahmedakram.ovicnews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(@NonNull Context context, List<News> news) {
        super (context, 0, news);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listView = convertView;
        if (listView == null) {
            listView = LayoutInflater.from (getContext ()).inflate (R.layout.list_items, parent, false);
        }
        News currentNews = getItem (position);
        TextView title = listView.findViewById (R.id.title_article);
        title.setText (currentNews.getTitle ());
        TextView author = listView.findViewById (R.id.author_article);
        author.setText (currentNews.getAuthor ());
        TextView section = listView.findViewById (R.id.section_aticle);
        section.setText (currentNews.getSection ());
        String date = currentNews.getDate ().substring (0, 10);
        TextView date2 = listView.findViewById (R.id.date_article);
        date2.setText (date);
        return listView;
    }
}
