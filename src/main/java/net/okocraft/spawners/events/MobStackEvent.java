package net.okocraft.spawners.events;

import org.bukkit.entity.Mob;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.NotNull;

/**
 * スポナーからスポーンした Mob がスタックされた際に発火されるイベント
 */
public class MobStackEvent extends EntityEvent implements Cancellable {
    private static final HandlerList handlerList = new HandlerList();

    private final Mob target;
    private final Mob stacked;
    private int amount;
    private boolean isCancelled;

    public MobStackEvent(@NotNull Mob target, @NotNull Mob stacked, int amount) {
        super(target);
        this.target = target;
        this.amount = amount;
        this.stacked = stacked;
        isCancelled = false;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlerList;
    }

    /**
     * スタックされた後に残る(スタック先の)モブを返す。
     *
     * @return スタック先のモブ
     */
    @NotNull
    public Mob getTarget() {
        return target;
    }

    /**
     * スタックされるモブを返す。
     *
     * @return スタックされるモブ
     */
    @NotNull
    public Mob getStacked() {
        return stacked;
    }

    /**
     * スタック後のスタック数を返す。
     *
     * @return スタック後のスタック数
     */
    public int getAmount() {
        return amount;
    }

    /**
     * スタック数をセットする。
     *
     * @param i スタック数
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
