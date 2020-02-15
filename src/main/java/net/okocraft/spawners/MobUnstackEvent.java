package net.okocraft.spawners;

import org.bukkit.entity.Mob;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.NotNull;

/**
 * モブがキルなどでスタック数が減る際に発火されるイベント。
 */
public class MobUnstackEvent extends EntityEvent implements Cancellable {
    private static final HandlerList handlerList = new HandlerList();

    private final Mob target;
    private int amount;
    private boolean isCancelled;

    public MobUnstackEvent(@NotNull Mob target, int amount) {
        super(target);
        this.target = target;
        this.amount = amount;
        isCancelled = false;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlerList;
    }

    /**
     * スタックされているモブを返す。
     *
     * @return スタックされているモブ
     */
    @NotNull
    public Mob getTarget() {
        return target;
    }

    /**
     * アンスタック後のスタック数を返す。
     *
     * @return アンスタック後のスタック数
     */
    public int getAmount() {
        return amount;
    }

    /**
     * スタック数をセットする。
     *
     * @param i スタック数。
     */
    public void setAmount(int i) {
        amount = i;
    }

    /**
     * このイベントがキャンセルされているかどうか。
     *
     * @return キャンセルされていれば {@code true}, されていなければ {@code false}
     */
    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    @NotNull
    public HandlerList getHandlers() {
        return handlerList;
    }

    /**
     * このイベントのキャンセル状況をセットする。
     *
     * @param b キャンセルするなら {@code true}, しないなら {@code false}
     */
    @Override
    public void setCancelled(boolean b) {
        isCancelled = b;
    }
}