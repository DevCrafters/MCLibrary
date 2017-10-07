package kr.rvs.mclibrary.bukkit.wizard;

import kr.rvs.mclibrary.bukkit.MCUtils;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Junhyeong Lim on 2017-10-06.
 */
public class ClickWizard extends Wizard<List<Block>, List<Block>> {
    public static final String COUNT = "%count%";
    public static final String REMAIN_COUNT = "%remain_count";
    private final String remainMessage;
    private final int count;
    private int remainCount = 0;

    public ClickWizard(Player player, String startMessage, String completeMessage, String remainMessage, int count) {
        super(player, new ArrayList<>(), startMessage, completeMessage);
        this.remainMessage = remainMessage;
        this.count = count;
        this.remainCount = count;
    }

    public ClickWizard(Player player, int count) {
        this(
                player,
                "&a블럭을 &f" + COUNT + " &a회 클릭하세요.",
                "&f" + REMAIN_COUNT + " &a회 남았습니다.",
                "&a완료했습니다.",
                count
        );
    }

    private String formatting(String target, int remainCount) {
        return MCUtils.colorize(target
                .replace(REMAIN_COUNT, String.valueOf(remainCount)));
    }

    @Override
    protected void process(List<Block> data) {
        registerEvents(new Listener() {
            @EventHandler
            public void onClick(PlayerInteractEvent event) {
                Player player = event.getPlayer();
                Block clickedBlock = event.getClickedBlock();
                if (clickedBlock != null && !clickedBlock.isEmpty()) {
                    data.add(clickedBlock);
                    remainCount = count - data.size();
                    if (data.size() >= count) {
                        release(data);
                    } else {
                        player.sendMessage(messageCaught(remainMessage));
                    }
                    event.setCancelled(true);
                }
            }
        });
    }

    @Override
    protected String messageCaught(String message) {
        return formatting(message, remainCount);
    }

    public String getRemainMessage() {
        return remainMessage;
    }
}
