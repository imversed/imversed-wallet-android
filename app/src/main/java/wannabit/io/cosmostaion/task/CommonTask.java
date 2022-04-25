package wannabit.io.cosmostaion.task;

import android.os.AsyncTask;

import com.fulldive.wallet.interactors.secret.SecretInteractor;
import com.joom.lightsaber.Injector;

import wannabit.io.cosmostaion.base.BaseApplication;

public class CommonTask extends AsyncTask<String, Void, TaskResult> {

    protected BaseApplication context;
    protected TaskListener mListener;
    protected TaskResult result;
    protected SecretInteractor secretInteractor;

    public CommonTask(BaseApplication app, TaskListener listener) {
        this.context = app;
        this.mListener = listener;
        this.result = new TaskResult();
        secretInteractor = getInjector().getInstance(SecretInteractor.class);
    }

    @Override
    protected TaskResult doInBackground(String... strings) {
        return null;
    }

    protected Injector getInjector() {
        return context.getInjector();
    }

    protected boolean checkPassword(String password) {
        return secretInteractor
                .checkPassword(password)
                .onErrorReturnItem(false)
                .blockingGet();
    }

    @Override
    protected void onPostExecute(TaskResult taskResult) {
        super.onPostExecute(taskResult);
        if (mListener != null)
            mListener.onTaskResponse(taskResult);
    }
}
