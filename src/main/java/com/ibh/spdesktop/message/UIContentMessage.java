package com.ibh.spdesktop.message;

import com.ibh.spdesktop.gui.CRUDEnum;
import com.ibh.spdesktop.gui.ViewEnum;

public class UIContentMessage extends BaseMessage {
	private final int id;
	private final CRUDEnum crud;

	public UIContentMessage(ViewEnum content, int id, CRUDEnum crud) {
		super(content);
		this.id = id;
		this.crud = crud;

	}

	public int getId() {
		return id;
	}

	public CRUDEnum getCrud() {
		return crud;
	}

}
