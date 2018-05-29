package net.a6te.lazycoder.weatherapp.mvp;

import android.support.v7.widget.RecyclerView;

public interface MVPView {
    interface mainView{

        void initializeRecyclerView(RecyclerView.Adapter adapter);

        void showInternetConnectionRequiredTv();

        void updateRemainder(int hours, int mints);
    }
}
