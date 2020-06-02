package com.example.newsapk.adaptor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.newsapk.R;
import com.example.newsapk.model.News;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {

    /**
     * Tag for log messages
     **/
    private static final String LOG_TAG = NewsAdapter.class.getName();
    static int a = 1;
    private Context context;

    public NewsAdapter(Context context, List<News> NewsItem) {
        super(context, 0, NewsItem);
        this.context = context;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View list_item = convertView;
        if (list_item == null) {
            list_item = LayoutInflater.from(getContext()).inflate(R.layout.news_item_list, parent, false);

        }

        final News current = getItem(position);

        ImageView img_thumbnail = list_item.findViewById(R.id.img_thumbnail);
        assert current != null;
        load_img(current.getThumbnail(), img_thumbnail);

        TextView tv_title = list_item.findViewById(R.id.tv_title);
        tv_title.setText(current.getTitle());

        TextView tv_section = list_item.findViewById(R.id.tv_section);
        tv_section.setText(current.getSection());

        TextView tv_author = (TextView) list_item.findViewById(R.id.tv_author);
        tv_author.setText(current.getAuthor());

        // Find the TextView with view ID location offset
        TextView tv_date = list_item.findViewById(R.id.tv_date);
        // Display the location offset of the current earthquake in that TextView
        tv_date.setText(time_format(current.getDate()));
        return list_item;
    }


    private void load_img(String url, ImageView img_thumbnail) {
        if (url != null) {
            Picasso.with(context).load(url).placeholder(R.drawable.loading)
                    .error(R.drawable.no_img)
                    .into(img_thumbnail, new Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError() {

                        }

                    });
        } else {
            img_thumbnail.setImageResource(R.drawable.no_img);
        }
    }

    // Format publish date
    private String time_format(final String time) {
        // If not the correct base format
        String str_time = "N.A.";
        // Check time validation
        if ((time != null) && (!time.isEmpty())) {
            try {
                // Create current format
                @SuppressLint("SimpleDateFormat") SimpleDateFormat date_fromat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                // Create new format
                @SuppressLint("SimpleDateFormat") SimpleDateFormat new_date_format = new SimpleDateFormat("dd.MM.yyyy  |  HH:mm");
                // Parse time
                str_time = new_date_format.format(date_fromat.parse(time));
            } catch (ParseException e) {
                // If an error occurs don't stop the app
                str_time = "N.A.";
                // But log the error
                Log.e(LOG_TAG, "Error while parsing the published date", e);
            }
        }

        return str_time;
    }

}
