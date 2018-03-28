package breakout.levels.loader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import breakout.levels.BrickCharsMapBuilder;
import breakout.levels.Level;

/**
 * A level loader which understands XML level files.
 * @author Jacob
 */
public class XMLLevelLoader implements LevelLoader {
	
	private DocumentBuilderFactory factory;
	private DocumentBuilder builder;

	public XMLLevelLoader() throws ParserConfigurationException {
		factory = DocumentBuilderFactory.newInstance();
		builder = factory.newDocumentBuilder();	
	}
	
	/* Loads the level from an XML file at 'path'.
	 * @see breakout.levels.loader.LevelLoader#load(java.lang.String)
	 */
	@Override
	public Level load(String path) {
		try {
			FileInputStream fis = new FileInputStream(path);
			Document doc = builder.parse(fis);
			
			Element el = (Element) doc.getElementsByTagName("level").item(0);

			double minBallSpeed = Double.parseDouble(getVal(el, "minBallSpeed"));
			double batWidth = Double.parseDouble(getVal(el, "batWidth"));
			double batSpeedTransferMod = Double.parseDouble(getVal(el, "batSpeedTransferMod"));
			String brickMap = getVal(el, "brickMap");
			
			return new Level(new BrickCharsMapBuilder(brickMap.toCharArray()), minBallSpeed, batWidth, batSpeedTransferMod);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	private String getVal(Element root, String tagName) {
		return root.getElementsByTagName(tagName).item(0).getTextContent();
	}
}
