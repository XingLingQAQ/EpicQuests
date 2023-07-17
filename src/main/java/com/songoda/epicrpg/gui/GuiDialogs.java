package com.songoda.epicrpg.gui;

import com.songoda.core.compatibility.CompatibleMaterial;
import com.songoda.core.gui.Gui;
import com.songoda.core.gui.GuiUtils;
import com.songoda.epicrpg.EpicRPG;
import com.songoda.epicrpg.dialog.AttachedSpeech;
import com.songoda.epicrpg.dialog.Dialog;
import com.songoda.epicrpg.dialog.DialogManager;
import org.bukkit.entity.Player;

import java.util.List;

public class GuiDialogs extends Gui {
    private final EpicRPG plugin;
    private final DialogManager dialogManager;
    private final Player player;
    private final AttachedSpeech attachedSpeech;

    public GuiDialogs(EpicRPG plugin, Player player, AttachedSpeech attachedSpeech) {
        this.plugin = plugin;
        this.dialogManager = plugin.getDialogManager();
        this.player = player;
        this.attachedSpeech = attachedSpeech;
        setRows(6);
        setDefaultItem(null);

        setTitle("Stories");

        show();
    }

    public void show() {
        reset();

        if (this.attachedSpeech == null) {
            setButton(0, 0, GuiUtils.createButtonItem(CompatibleMaterial.GREEN_DYE, "Create Dialog"),
                    (event) -> {
                        this.plugin.getContendentManager().getPlayer(this.player).setInDialogCreation(true);
                        this.player.sendMessage("Click a citizen to create a new dialog.");
                        this.player.closeInventory();
                    });

            setButton(0, 8, GuiUtils.createButtonItem(CompatibleMaterial.OAK_DOOR, "Back"),
                    (event) -> this.guiManager.showGUI(this.player, new GuiMain(this.plugin, this.player)));
        }

        List<Dialog> dialogs = this.dialogManager.getDialogs();
        for (int i = 0; i < dialogs.size(); i++) {
            Dialog dialog = dialogs.get(i);

            if (dialog.getCitizen() == null) {
                this.dialogManager.removeDialog(dialog);
                continue;
            }

            setButton(i + 9, GuiUtils.createButtonItem(CompatibleMaterial.PAPER, dialog.getCitizen().getName()),
                    (event) -> this.guiManager.showGUI(this.player, new GuiDialog(this.plugin, this.player, dialog, this.attachedSpeech)));
        }
    }
}
