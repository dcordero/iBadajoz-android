package com.dcordero.ibadajoz.fragments.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dcordero.ibadajoz.R;
import com.pkmmte.pkrss.Article;

import java.io.UnsupportedEncodingException;

public class NewsViewerFragment extends Fragment {

    private static final String SAVE_INSTANCE_PIECE_OF_NEWS = "com.dcordero.ibadajoz.save_instance_state_piece_of_news";

    public static final String EXTRA_PIECE_OF_NEWS = "com.dcordero.ibadajoz.extra_piece_of_news";

    private Article mArticle;

    public static NewsViewerFragment newInstance(Article article)
    {
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_PIECE_OF_NEWS, article);

        NewsViewerFragment fragment = new NewsViewerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_news_viewer, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_browser_button:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(mArticle.getSource());
                startActivity(intent);
                return true;
            case R.id.menu_share_button:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, mArticle.getTitle() + mArticle.getDescription());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(SAVE_INSTANCE_PIECE_OF_NEWS, mArticle);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news_viewer, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        view.setBackgroundResource(R.drawable.background);

        if (savedInstanceState != null) {
            mArticle = savedInstanceState.getParcelable(SAVE_INSTANCE_PIECE_OF_NEWS);
        }
        else if (getArguments() != null) {
            mArticle = (Article) getArguments().getParcelable(EXTRA_PIECE_OF_NEWS);
        }

        try {
            TextView titleTextView = (TextView) view.findViewById(R.id.news_title);
            titleTextView.setText(new String(mArticle.getTitle().getBytes("ISO-8859-1"), "UTF-8"));

            TextView descriptionTextView = (TextView) view.findViewById(R.id.news_body);
            descriptionTextView.setText(mArticle.getDescription());
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
