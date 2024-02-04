package commands;

import de.ewu2000.galdreenblocksunlimited.CustomBlockCompound;
import de.ewu2000.galdreenblocksunlimited.GaldreenBlocksUnlimited;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TabCompleterGiveGaldreenBlocks implements TabCompleter {

    public TabCompleterGiveGaldreenBlocks(){
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, String alias, String[] args) {
        List<String> suggestions = new ArrayList<>();
        if(sender instanceof Player){
            PlainTextComponentSerializer serializer = PlainTextComponentSerializer.plainText();
            for(CustomBlockCompound cbcmp: GaldreenBlocksUnlimited.goalToCompound.values()) {
                if (cbcmp.getItemToUse().getItemMeta().hasDisplayName()){
                    String name = serializer.serialize(cbcmp.getItemToUse().displayName());
                    suggestions.add(name.substring(1,name.length() - 1));

                }
            }
        }
        return suggestions;
    }

}
