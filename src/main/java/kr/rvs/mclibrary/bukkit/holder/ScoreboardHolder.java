package kr.rvs.mclibrary.bukkit.holder;

import kr.rvs.mclibrary.Static;
import kr.rvs.mclibrary.bukkit.MCUtils;
import kr.rvs.mclibrary.bukkit.Replaceable;
import kr.rvs.mclibrary.bukkit.mock.MockFactory;
import kr.rvs.mclibrary.collection.StringArrayList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Arrays;

/**
 * Created by Junhyeong Lim on 2017-10-08.
 */
public class ScoreboardHolder extends Replaceable<ScoreboardHolder> {
    private String id;
    private final String title;
    private StringArrayList contents = new StringArrayList();

    public static OfflinePlayer getOffline(String contents) {
        String parsed = contents.substring(0, Math.min(contents.length(), 16));
        return MockFactory.createOfflinePlayer(
                MCUtils.colorize(parsed));
    }

    public ScoreboardHolder(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public ScoreboardHolder(String title) {
        this("mclibrary", title);
    }

    public ScoreboardHolder content(String... contents) {
        this.contents.addAll(Arrays.asList(contents));
        return this;
    }

    public void setContent(int index, String content) {
        // Fill
        for (int i = contents.size(); i < index + 1; i++) {
            contents.add("");
        }
        contents.set(index, content);
    }

    private String ensure(String content) {
        if (content.length() > 40) {
            Static.log("스코어보드 내용 \"" + content + "\" 이 40 글자를 넘었습니다.");
            content = content.substring(0, 40);
        }
        return content;
    }

    @SuppressWarnings("deprecation")
    public Scoreboard create() {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective(id, "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(formatting(title));

        StringArrayList contents = new StringArrayList(this.contents.size());
        for (String content : this.contents) {
            contents.add(formatting(content));
        }

        for (int i = 0; i < contents.size(); i++) {
            String content = ensure(formatting(contents.get(i)));
            int number = contents.size() - i;
            Score score = objective.getScore(getOffline(content)); // For Legacy version
            score.setScore(number);
        }

        return scoreboard;
    }

    public Scoreboard show(Iterable<? extends Player> players) {
        Scoreboard scoreboard = create();
        for (Player player : players) {
            if (player == null)
                continue;
            player.setScoreboard(scoreboard);
        }
        return scoreboard;
    }

    public Scoreboard show(Player... players) {
        return show(Arrays.asList(players));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
}
