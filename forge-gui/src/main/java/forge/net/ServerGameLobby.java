package forge.net;

import java.util.Collections;

import org.apache.commons.lang3.StringUtils;

import forge.AIOption;
import forge.interfaces.IGuiGame;
import forge.match.GameLobby;
import forge.match.LobbySlot;
import forge.net.game.LobbySlotType;

public final class ServerGameLobby extends GameLobby {

    public ServerGameLobby() {
        super(true);
        addSlot(new LobbySlot(LobbySlotType.LOCAL, localName(), localAvatarIndices()[0], 0, true, Collections.<AIOption>emptySet()));
        addSlot(new LobbySlot(LobbySlotType.OPEN, null, -1, 1, false, Collections.<AIOption>emptySet()));
    }

    public int connectPlayer(final String name, final int avatarIndex) {
        final int nSlots = getNumberOfSlots();
        for (int index = 0; index < nSlots; index++) {
            final LobbySlot slot = getSlot(index);
            if (slot.getType() == LobbySlotType.OPEN) {
                connectPlayer(name, avatarIndex, slot);
                return index;
            }
        }
        return -1;
    }
    private void connectPlayer(final String name, final int avatarIndex, final LobbySlot slot) {
        slot.setType(LobbySlotType.REMOTE);
        slot.setName(name);
        slot.setAvatarIndex(avatarIndex);
        updateView();
    }
    public void disconnectPlayer(final int index) {
        final LobbySlot slot = getSlot(index);
        slot.setType(LobbySlotType.OPEN);
        slot.setName(StringUtils.EMPTY);
        updateView();
    }

    @Override public boolean hasControl() {
        return true;
    }

    @Override public boolean mayEdit(final int index) {
        final LobbySlotType type = getSlot(index).getType();
        return type != LobbySlotType.REMOTE && type != LobbySlotType.OPEN;
    }

    @Override public boolean mayControl(final int index) {
        return getSlot(index).getType() != LobbySlotType.REMOTE;
    }

    @Override public boolean mayRemove(final int index) {
        return true;
    }

    @Override public IGuiGame getGui(final int index) {
        return FServerManager.getInstance().getGui(index);
    }
}
