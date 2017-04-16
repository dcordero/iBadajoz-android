package com.dcordero.ibadajoz.activities.news;

import android.support.v4.app.Fragment;

import com.dcordero.ibadajoz.activities.SingleFragmentActivity;
import com.dcordero.ibadajoz.fragments.news.NewsViewerFragment;
import com.pkmmte.pkrss.Article;

public class NewsViewerActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        Article article = (Article) getIntent().getParcelableExtra(NewsViewerFragment.EXTRA_PIECE_OF_NEWS);
        return NewsViewerFragment.newInstance(article);
    }
}
