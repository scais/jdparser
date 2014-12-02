package uk.azdev.openfire.client;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import uk.azdev.openfire.friendlist.Friend;

public class FriendListModel extends AbstractTableModel {

	private static final int NAME_COLUMN = 0;
	private static final int GAME_COLUMN = 1;
	private static final int COLUMN_COUNT = GAME_COLUMN + 1;
	
	private static final long serialVersionUID = 1L;
	private List<Friend> friends;
	
	public FriendListModel() {
		this.friends = new ArrayList<Friend>();
	}
	
	public void updateList(List<Friend> newFriends) {
		friends.clear();
		friends.addAll(newFriends);
		fireTableDataChanged();
	}
	
	public void clear() {
		friends.clear();
		fireTableDataChanged();
	}
	
	public Object getElementAt(int index) {
		return friends.get(index).getDisplayName();
	}
	
	public Friend getFriendAt(int index) {
		return friends.get(index);
	}

	public int getSize() {
		return friends.size();
	}
	
	public int getColumnCount() {
		return COLUMN_COUNT;
	}
	
	public int getRowCount() {
		return friends.size();
	}
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		Friend friend = friends.get(rowIndex);
		switch(columnIndex) {
		case NAME_COLUMN: return friend.getDisplayName();
		case GAME_COLUMN: return friend.getGame();
		default: return "";
		}
	}
	
	@Override
	public String getColumnName(int column) {
		switch(column) {
		case NAME_COLUMN: return "Name";
		case GAME_COLUMN: return "Game";
		default: return "";
		}
	}

}
