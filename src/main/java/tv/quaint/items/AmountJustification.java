package tv.quaint.items;

import com.google.re2j.Matcher;
import tv.quaint.utils.MainUtils;

import java.util.Random;

public class AmountJustification {
    public String fromIn;
    public String toOut;
    public int cascadeFactor;

    public AmountJustification(String raw) {
        raw = raw.replace("%random", "");
        raw = raw.replace("%", "");

        String[] split = raw.split(",", 2);
        this.fromIn = split[0];
        this.toOut = split[1];

        this.cascadeFactor = 0;
    }

    public AmountJustification(String raw, int cascadeFactor) {
        String[] split = raw.split(",", 2);
        this.fromIn = split[0];
        this.toOut = split[1];
        this.cascadeFactor = cascadeFactor;
    }

    public AmountJustification(String fromIn, String toOut) {
        this.fromIn = fromIn;
        this.toOut = toOut;
        this.cascadeFactor = 0;
    }

    public int roll() {
        try {
            if (cascadeFactor > 3) return 0;

            Matcher f = MainUtils.setupMatcher("(([%][r][a][n][d][o][m])([1-9]+)[,]([1-9]+)[%])", fromIn);
            Matcher t = MainUtils.setupMatcher("(([%][r][a][n][d][o][m])([1-9]+)[,]([1-9]+)[%])", toOut);
            int fi;
            int ti;

            try {
                fi = Integer.parseInt(fromIn);
            } catch (Exception e) {
                fi = new AmountJustification(fromIn, cascadeFactor + 1).roll();
            }
            try {
                ti = Integer.parseInt(toOut);
            } catch (Exception e) {
                ti = new AmountJustification(toOut, cascadeFactor + 1).roll();
            }

            return new Random().nextInt(fi, ti);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
