package com.dcordero.ibadajoz.fragments.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dcordero.ibadajoz.R;
import com.dcordero.ibadajoz.activities.news.NewsViewerActivity;
import com.dcordero.ibadajoz.core.workers.NewsWorker;
import com.dcordero.ibadajoz.core.workers.WorkerCallback;
import com.pkmmte.pkrss.Article;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends ListFragment {

    private static final String SAVE_INSTANCE_NEWS = "com.dcordero.ibadajoz.save_instance_news";

    public static final String EXTRA_NEWS_URL = "com.dcordero.ibadajoz.extra_news_url";

    private ArrayList<Article> mNews = new ArrayList<Article>();
    private String mNewsURL;

    public static NewsFragment newInstance(String url)
    {
        Bundle args = new Bundle();
        args.putString(EXTRA_NEWS_URL, url);

        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(SAVE_INSTANCE_NEWS, mNews);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupAdapter();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        getListView().setBackgroundResource(R.drawable.background);

        if (savedInstanceState != null) {
            mNews = savedInstanceState.getParcelableArrayList(SAVE_INSTANCE_NEWS);
            ((NewsAdapter) getListAdapter()).notifyDataSetChanged();
        }
        else if (getArguments() != null) {
            mNewsURL = getArguments().getString(EXTRA_NEWS_URL);
        }

        if (mNews.isEmpty()) {
            fetchNews();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_news, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_news_refresh_button:
                fetchNews();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Article article = (Article) mNews.get(position);

        if (article.getDescription() != null && !article.getDescription().isEmpty()) {
            Intent intent = new Intent(getActivity(), NewsViewerActivity.class);
            intent.putExtra(NewsViewerFragment.EXTRA_PIECE_OF_NEWS, article);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(article.getSource());
            startActivity(intent);
        }
    }

    private void setupAdapter()
    {
        setListAdapter(new NewsAdapter(mNews));
    }

    private void fetchNews()
    {
        setListShown(false);
        NewsWorker.fetchNews(getActivity(), mNewsURL, new WorkerCallback() {
            @Override
            public void success(Object responseObject) {
                mNews.clear();
                mNews.addAll((List<Article>) responseObject);

                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((NewsAdapter) getListAdapter()).notifyDataSetChanged();
                            setListShown(true);
                        }
                    });
                }
            }
            @Override
            public void failure() {
                setListShown(false);
            }
        });
    }

    private class NewsAdapter extends ArrayAdapter<Article>
    {

        public NewsAdapter(List<Article> articles) {
            super(getActivity(), 0, articles);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_news, null);
            }

            Article currentArticle = mNews.get(position);

            try {
                TextView newsTitleTextView = (TextView) convertView.findViewById(R.id.news_title);
                newsTitleTextView.setText(new String (currentArticle.getTitle().getBytes("ISO-8859-1"), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            TextView newsBodyTextView = (TextView) convertView.findViewById(R.id.news_body);
            newsBodyTextView.setText(Html.fromHtml(currentArticle.getDescription()).toString());

            return convertView;
        }
    }
}
