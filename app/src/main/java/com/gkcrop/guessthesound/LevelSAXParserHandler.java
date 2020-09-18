package com.gkcrop.guessthesound;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

class LevelSAXParserHandler extends DefaultHandler {

	private boolean currentElement = false;
	private String currentValue = "";
	private Level productInfo;
	private final ArrayList<Level> cartList;

	public LevelSAXParserHandler() {
		// TODO Auto-generated constructor stub
		 cartList = new ArrayList<>();
	}
	public ArrayList<Level> getCartList() {
		return cartList;
	}

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) {

		currentElement = true;
		
		if (qName.equals("level")) {
			productInfo = new Level();
		}

	}

	public void endElement(String uri, String localName, String qName) {

		currentElement = false;

		if (qName.equalsIgnoreCase("musicId"))
			productInfo.setMusicId(currentValue.trim());
		else if (qName.equalsIgnoreCase("ribbon"))
			productInfo.setRibbon(currentValue.trim());
		else if (qName.equalsIgnoreCase("answer"))
			productInfo.setAnswer(currentValue.trim());
		else if (qName.equalsIgnoreCase("level"))
			cartList.add(productInfo);

		currentValue = "";
	}

	public void characters(char[] ch, int start, int length) {

		if (currentElement) {
			currentValue = currentValue + new String(ch, start, length);
		}

	}

}