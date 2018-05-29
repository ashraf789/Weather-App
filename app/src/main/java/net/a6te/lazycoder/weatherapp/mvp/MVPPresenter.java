package net.a6te.lazycoder.weatherapp.mvp;

public interface MVPPresenter {
    interface MainPresenter{
        void prepareAdapter();

        void initializeAlarm();
    }
}
