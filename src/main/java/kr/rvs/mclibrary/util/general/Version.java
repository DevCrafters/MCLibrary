package kr.rvs.mclibrary.util.general;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Junhyeong Lim on 2017-08-27.
 */
public class Version {
    private static final Pattern PATTERN = Pattern.compile("(\\d+)(?:\\.(\\d+))?(?:\\.(\\d+))?");

    private int major = 0;
    private int minor = 0;
    private int maintenance = 0;

    public Version(int major, int minor, int maintenance) {
        this.major = major;
        this.minor = minor;
        this.maintenance = maintenance;
    }

    public Version(String version) {
        Matcher matcher = PATTERN.matcher(version);
        if (matcher.find()) {
            String majorStr = matcher.group(1);
            String minorStr = matcher.group(2);
            String maintenance = matcher.group(3);

            if (majorStr != null)
                this.major = Integer.parseInt(majorStr);
            if (minorStr != null)
                this.minor = Integer.parseInt(minorStr);
            if (maintenance != null)
                this.maintenance = Integer.parseInt(maintenance);
        }
    }
}
