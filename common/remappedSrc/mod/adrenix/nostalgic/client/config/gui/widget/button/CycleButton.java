package mod.adrenix.nostalgic.client.config.gui.widget.button;

import mod.adrenix.nostalgic.NostalgicTweaks;
import mod.adrenix.nostalgic.client.config.gui.widget.IPermissionWidget;
import mod.adrenix.nostalgic.client.config.reflect.TweakClientCache;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import java.util.Arrays;
import java.util.Optional;

public class CycleButton<E extends Enum<E>> extends ControlButton implements IPermissionWidget
{
    private final TweakClientCache<E> cache;
    private final Class<E> values;

    public CycleButton(TweakClientCache<E> cache, Class<E> values, PressAction onPress)
    {
        super(Text.empty(), onPress);
        this.cache = cache;
        this.values = values;
    }

    public void toggle()
    {
        E[] enums = this.values.getEnumConstants();
        if (enums.length == 0)
        {
            NostalgicTweaks.LOGGER.warn("Tried to toggle an empty enumeration list. This shouldn't happen!");
            return;
        }

        Optional<E> firstSearch = Arrays.stream(enums).findFirst();
        E firstConstant = firstSearch.orElse(this.cache.getCurrent());
        E nextConstant = firstConstant;
        E lastConstant = enums[enums.length - 1];
        E currentConstant = this.cache.getCurrent();

        if (Screen.hasShiftDown())
        {
            if (firstConstant == currentConstant)
            {
                this.cache.setCurrent(lastConstant);
                return;
            }

            E previousConstant = currentConstant;

            for (E next : enums)
            {
                if (next == currentConstant)
                {
                    this.cache.setCurrent(previousConstant);
                    return;
                }

                previousConstant = next;
            }
        }
        else
        {
            if (lastConstant == currentConstant)
            {
                this.cache.setCurrent(firstConstant);
                return;
            }

            boolean isCurrent = false;

            for (E next : enums)
            {
                if (isCurrent)
                {
                    nextConstant = next;
                    break;
                }

                if (next == currentConstant)
                    isCurrent = true;
            }

            this.cache.setCurrent(nextConstant);
        }
    }

    @Override
    public Text getMessage() { return Text.literal(this.cache.getCurrent().toString()); }
}
