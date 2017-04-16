package com.dcordero.ibadajoz.core.workers;

import android.content.Context;

import com.pkmmte.pkrss.Article;
import com.pkmmte.pkrss.Callback;
import com.pkmmte.pkrss.PkRSS;

import java.util.List;

public class NewsWorker {

    public static final String URL_NEWS = "http://feeds.feedburner.com/aytobadajoz-noticias";
    public static final String URL_SPECIAL_NEWS = "http://feeds.feedburner.com/aytobadajoz-especiales";
    public static final String URL_JOBS_NEWS = "http://feeds.feedburner.com/aytobadajoz-empleo";

    public static void fetchNews(final Context context, final String url, final WorkerCallback callback)
    {
        try {
            PkRSS.with(context).load(url).callback(new Callback() {
                @Override
                public void OnPreLoad() {
                }

                @Override
                public void OnLoaded(List<Article> articles) {
                    List<Article> articleList = PkRSS.with(context).get(url);
                    callback.success(articleList);
                }

                @Override
                public void OnLoadFailed() {
                    callback.failure();
                }
            }).async();

        } catch (Exception e) {
            callback.failure();
        }
    }
}
