package pavelnazimok.uitestingfeatures.testrail;

import android.os.Build;

import org.junit.Ignore;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ReporterRunListener extends RunListener {

    private static final long PASSED_ID = 1;
    private static final long FAILED_ID = 5;
    private static final long IGNORED_ID = 6;

    private long currentTestStatusId = PASSED_ID;

    private APIClient client = APIClientFactory.create();
    private long runId = 0;
    private long testStartTime = System.currentTimeMillis();
    private String comment = "";

    private void addResultForCase(long caseId, long statusId, long elapsed, String comment) throws InterruptedException {
        Map<String, Object> data = new HashMap<>();

        data.put("status_id", statusId);
        if (elapsed != 0) {
            data.put("elapsed", String.format("%ds", elapsed));
        }

        data.put("comment", String.format("OS: %s, API: %d%s",
                Build.VERSION.RELEASE,
                Build.VERSION.SDK_INT,
                comment.equals("") ? "" : "\n" + comment
        ));

        int maxAttempts = 5;
        for (int i = 0; i < maxAttempts; i++) {
            try {
                client.sendPost(String.format("add_result_for_case/%d/%d", runId, caseId), data);
                break;
            } catch (IOException e) {
                e.printStackTrace();
                Thread.sleep(1000);
            } catch (APIException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    @Override
    public void testRunStarted(Description description) throws IOException, APIException {
        /* Set your current Test Run ID.
        Another option is to create new Test Run here, but remember that when you use
        Android Test Orchestrator with 'clearPackageData' flag this event will be called for each test*/
        runId = 0;

        //noinspection ConstantConditions
        if (runId != 0) {
            Map<String, Object> data = new HashMap<>();

            String runName = "Your run name";
            String runDescription = "Your run description";

            data.put("name", runName);
            data.put("description", runDescription);

            client.sendPost(String.format("update_run/%d", runId), data);
        }
    }

    @Override
    public void testStarted(Description description) {
        currentTestStatusId = PASSED_ID;
        comment = "";

        if (runId != 0) {
            testStartTime = System.currentTimeMillis();
        }
    }

    @Override
    public void testFailure(Failure failure) {
        currentTestStatusId = FAILED_ID;
    }

    @Override
    public void testAssumptionFailure(Failure failure) {
        currentTestStatusId = IGNORED_ID;
        comment = failure.getMessage();
    }

    @Override
    public void testIgnored(Description description) throws InterruptedException {
        comment = description.getAnnotation(Ignore.class).value();

        if (runId != 0) {
            long caseId = 0;
            if (description.getAnnotation(CaseId.class) != null) {
                caseId = description.getAnnotation(CaseId.class).value();
            }

            if (caseId != 0) {
                addResultForCase(caseId, IGNORED_ID, 0, comment);
            }
        }
    }

    @Override
    public void testFinished(Description description) throws InterruptedException {
        if (runId != 0) {
            long elapsed = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - testStartTime);
            if (elapsed == 0) {
                elapsed = 1;
            }

            long caseId = 0;
            if (description.getAnnotation(CaseId.class) != null) {
                caseId = description.getAnnotation(CaseId.class).value();
            }

            if (caseId != 0) {
                addResultForCase(caseId, currentTestStatusId, elapsed, comment);
            }
        }
    }
}
